package partie2.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import partie2.client.ui.Controleur;

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
	 * Envoie la command au serveur.
	 * @param command
	 */
	public void execute(String command) {
		try {
			out.writeObject(command);
		} catch (IOException ignored) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la tranmission de la commande.").show();
		}
	}
	
	/**
	 * Attends des images et demande au controleur de les afficher
	 */
	@Override
	public void run() {
		while(true) {
			try {
				BufferedImage img = ImageIO.read(in);
				controller.imageReceipt(img);
			} catch(IOException e) {
				new Alert(Alert.AlertType.ERROR, "Erreur lors de la reception de l'image.").show();
			}
		}
	}

}
