package partie2.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

import javax.imageio.ImageIO;
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
	
	/**
	 * Contructeur.
	 * @param port
	 * @param controller
	 */
	public Client(int port, Controleur controller) {
		try {
			this.controller = controller;
			s = new Socket(InetAddress.getLocalHost(), port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.exit(1);
		}
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
		while(true) {
			try {
				if(!(in.readObject() instanceof Response res)) throw new IOException();
				BufferedImage img = b64ToImg(res.image());
				if(img == null) throw new IOException(); 
				controller.commandFeedBack(res.feedback());
				controller.imageReceipt(img);
			} catch(IOException|ClassNotFoundException e) {
				//Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Erreur lors de la reception de la reponse du serveur.").show());
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	private BufferedImage b64ToImg(String b64) {
		ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(b64));
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			return null;
		}
	}

}
