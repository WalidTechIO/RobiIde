package partie2.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import partie2.client.ui.Controleur;
import partie2.io.Program;
import partie2.io.Response;

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
			out.writeObject("Exe");
		} catch (IOException ignored) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la tranmission de la commande.").show();
		}
	}
	
	public void sendProgram(Program program) {
		try {
			out.writeObject(program);
		} catch (IOException ignored) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la tranmission du programme.").show();
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
				Response res = mapper.readValue(msg, Response.class);
				
				BufferedImage img = b64ToImg(res.image());
				if(img == null) throw new IOException(); 
				Platform.runLater(() -> {
					controller.commandFeedBack(res.feedback());
					controller.imageReceipt(img);
					if(controller.isDebugging()) {
						controller.debugReceipt(res.info());
					}
				});
			} catch(IOException|ClassNotFoundException e) {
				if(working) Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Erreur lors de la reception.").showAndWait());
				break;
			}
		}
		//TODO: Notifier controller de fermer l'application apres avoir proposer de sauvegarder si contenu dans code
	}
	
	private BufferedImage b64ToImg(String b64) {
		ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(b64));
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			return null;
		}
	}
	
	public void close() {
		working = false;
		try {
			s.close();
		} catch(IOException ignored) {}
	}

}
