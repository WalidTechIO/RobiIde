package partie2.client.ui;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import partie2.client.Client;

/**
 * Controleur de l'UI.
 */
public class Controleur {
	
	/**
	 * Enumeration representant les modes du client.
	 */
	private enum MODE {
		S_B_S,
		DIRECT
	}
	
	/**
	 * Index de la prochaine inst a effectuer en mode S_B_S
	 */
	private int sbsindice = 0;
	
	/**
	 * Tableau d'instructions si MODE.S_B_S
	 */
	private String[] inst = null;
	
	/**
	 * Client logique.
	 */
	private Client client;
	
	/**
	 * Mode courant du client.
	 */
	private MODE mode;
	
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
		client = new Client(7777, this);
		mode = MODE.DIRECT;
	}
	
	/**
	 * Methode au click sur le bouton mode (Direct -> S_B_S).
	 * @param e Evenement du click.
	 */
	void clickStepByStep(MouseEvent e) {
		mode = MODE.S_B_S;
		labelMode.setText("Mode: Pas a Pas");
		buttonMode.setText("Mode Direct");
		buttonMode.setOnMouseClicked(this::clickDirect);
	}
	
	/**
	 * Methode au click sur le bouton mode (S_B_S -> Direct).
	 * @param e Evenement du click.
	 */
	void clickDirect(MouseEvent e) {
		mode = MODE.DIRECT;
		labelMode.setText("Mode: Direct");
		buttonMode.setText("Mode Pas a Pas");
		buttonMode.setOnMouseClicked(this::clickStepByStep);
		buttonExecute.setOnMouseClicked(this::execute);
	}
	
	/**
	 * Methode d'execution general.
	 * @param e Evenement du click.
	 */
	void execute(MouseEvent e) {
		
		if(mode == MODE.DIRECT) client.execute(codeTextArea.getText());
		
		else {
			sbsindice = 0;			
			inst = codeTextArea.getText().split("\n");
			buttonExecute.setOnMouseClicked(this::oneStep);		
			oneStep(null);
		}
		
	}
	
	/**
	 * Methode d'execution d'une seule instruction.
	 * @param e Evenement du click.
	 */
	void oneStep(MouseEvent e) {
		if(inst == null) return;
		if(sbsindice == inst.length - 1) {
			buttonExecute.setOnMouseClicked(this::execute);
		}
		client.execute(inst[sbsindice++]);
	}
	
	/**
	 * Affiche une image dans la zone prévue.
	 * @param img Image a afficher.
	 */
	public void imageReceipt(BufferedImage img) {
		if(img != null) image.setImage(SwingFXUtils.toFXImage(img, null));
	}

}
