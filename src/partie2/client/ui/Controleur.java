package partie2.client.ui;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import partie2.client.Client;
import partie2.io.Mode;
import partie2.io.Program;

/**
 * Controleur de l'UI.
 */
public class Controleur {
	
	/**
	 * Client logique.
	 */
	private Client client;
	
	/**
	 * Mode courant du client.
	 */
	private Mode mode;
	
	/**
	 * Resources FXML.
	 */
	@FXML
	private ResourceBundle resources;
	
	/**
	 * Location FXML.
	 */
	@FXML
	private URL location;
	
	/**
	 * Zone de code.
	 */
	@FXML
	private TextArea codeTextArea;
	
	/**
	 * Bouton d'envoi du programme.
	 */
	@FXML
	private Button buttonSendProgram;
	
	/**
	 * Bouton gestion des modes.
	 */
	@FXML
	private Button buttonMode;
	
	/**
	 * Bouton gestion de l'execution.
	 */
	@FXML
	private Button buttonExecute;
	
	/**
	 * Label du mode.
	 */
	@FXML
	private Label labelMode;
	
	/**
	 * Zone de rendu.
	 */
	@FXML
	private ImageView image;
	
	/**
	 * Methode d'initialisation de l'UI.
	 */
	@FXML
	void initialize() {
		buttonMode.setOnMouseClicked(this::clickStepByStep);
		buttonExecute.setOnMouseClicked(this::execute);
		buttonSendProgram.setOnMouseClicked(this::sendProgram);
		client = new Client(7777, this);
		mode = Mode.DIRECT;
	}
	
	/**
	 * Methode au click sur le bouton mode (Direct -> S_B_S).
	 * @param e Evenement du click.
	 */
	void clickStepByStep(MouseEvent e) {
		mode = Mode.S_B_S;
		labelMode.setText("Mode: Pas a Pas");
		buttonMode.setText("Mode Direct");
		buttonMode.setOnMouseClicked(this::clickDirect);
	}
	
	/**
	 * Methode au click sur le bouton mode (S_B_S -> Direct).
	 * @param e Evenement du click.
	 */
	void clickDirect(MouseEvent e) {
		mode = Mode.DIRECT;
		labelMode.setText("Mode: Direct");
		buttonMode.setText("Mode Pas a Pas");
		buttonMode.setOnMouseClicked(this::clickStepByStep);
	}
	
	void sendProgram(MouseEvent e) {
		client.sendProgram(new Program(mode, codeTextArea.getText()));
	}
	
	/**
	 * Methode d'execution general.
	 * @param e Evenement du click.
	 */
	void execute(MouseEvent e) {
		client.execute();
	}
	
	/**
	 * Affiche une image dans la zone prévue.
	 * @param img Image a afficher.
	 */
	public void imageReceipt(BufferedImage img) {
		if(img != null) image.setImage(SwingFXUtils.toFXImage(img, null));
	}

	public void commandFeedBack(String inst) {
		Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, inst).show());
	}

}
