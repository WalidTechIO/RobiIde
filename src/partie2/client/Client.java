package partie2.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import partie2.client.ui.Controleur;
import partie2.io.Program;
import partie2.io.Request;
import partie2.io.Request.RequestType;
import partie2.io.Response;
import partie2.io.State;
import partie2.utils.GraphicsUtils;
import partie2.utils.GraphicsUtils.RendererException;

/**
 * Client
 */
public class Client implements Runnable {
	
	/**
	 * Socket client.
	 */
	private Socket s;
	/**
	 * Flux d'objets entrant.
	 */
	private ObjectInputStream in;
	/**
	 * Flux d'objets sortant.
	 */
	private ObjectOutputStream out;
	/**
	 * Controleur de l'UI.
	 */
	private Controleur controller;
	
	private boolean working = true;
	
	private Request lastRequest = null;
	
	private Response lastResponse = null;
	
	/**
	 * Contructeur.
	 * @param port
	 * @param controller
	 */
	public Client(InetSocketAddress adr, Controleur controller) throws IOException {
		this.controller = controller;
		s = new Socket(adr.getAddress(), adr.getPort());
		out = new ObjectOutputStream(s.getOutputStream());
		in = new ObjectInputStream(s.getInputStream());
		new Thread(this).start();
	}
	
	/**
	 * Envoie un Exe au serveur.
	 */
	public void execute() {
		
		try {
			lastRequest = new Request(RequestType.EXE, null);
			out.writeObject(toJson(lastRequest));
		} catch (IOException ignored) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la transmission de la commande.").show();
		}
	}
	
	public void sendProgram(Program program) {
		
		try {
			lastRequest = new Request(RequestType.PROG, program);
			out.writeObject(toJson(lastRequest));
			new Alert(Alert.AlertType.INFORMATION, "Le programme a bien été transmis.").show();
		} catch (IOException ignored) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la transmission du programme.").show();
		}
	}
	
	/**
	 * Attends des images et demande au controleur de les afficher
	 */
	@Override
	public void run() {
		while(working) {
			try {
				if(!(in.readObject() instanceof String msg)) throw new IOException();
				
				ObjectMapper mapper = new ObjectMapper();
				final Response res = mapper.readValue(msg, Response.class);
				try {
					BufferedImage render = GraphicsUtils.b64ToImg(GraphicsUtils.render(res.world()));
					Platform.runLater(() -> {				
						controller.imageReceipt(render);
					});
				} catch(RendererException e) {
					Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Impossible d'effectuer le rendu: " + e.getMessage()).showAndWait());
					e.printStackTrace();
				}
				
				Platform.runLater(() -> {
					controller.commandFeedBack(res.feedback());
					if(controller.isDebugging()) controller.debugReceipt(res.info());
				});
				
				lastResponse = res;
			} catch(IOException|ClassNotFoundException e) {
				if(working) Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Déconnecté").showAndWait());
				break;
			}
		}
		//TODO: Notifier controller de fermer l'application apres avoir proposer de sauvegarder si contenu dans code
	}
	
	public void close() {
		working = false;
		try {
			s.close();
		} catch(IOException ignored) {}
	}
	
	public String exportState() {
		try {
			return toJson(new State(lastRequest, lastResponse));
		} catch (IOException e) {
			return null;
		}
	}
	
	public String toJson(Object obj) throws IOException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(obj);
	}
}
