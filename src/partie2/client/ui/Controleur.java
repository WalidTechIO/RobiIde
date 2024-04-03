package partie2.client.ui;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	 * Bouton MenuBar Charger
	 */
	@FXML
	private MenuItem buttonLoad;
	
	/**
	 * Bouton MenuBar Sauvegarder
	 */
	@FXML
	private MenuItem buttonSave;
	
	/**
	 * Bouton MenuBar A propos
	 */
	@FXML
	private MenuItem buttonAbout;
	
	/**
	 * Zone de code.
	 */
	@FXML
	private TextArea codeTextArea;
	
	/**
	 * Zone de feedback.
	 */
	@FXML
	private TextArea feedbackArea;
	
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
		buttonAbout.setOnAction(this::about);
		buttonLoad.setOnAction(this::load);
		buttonSave.setOnAction(this::save);
		feedbackArea.setEditable(false);
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
	
	/**
	 * Envoie un programme.
	 * @param e Evenement du click.
	 */
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

	/**
	 * Ajoute un feedback a la zone de feedback
	 * @param msg
	 */
	public void commandFeedBack(String msg) {
		feedbackArea.appendText(msg + "\n");
	}
	
	/**
	 * Ouvre la fenetre d'a propos.
	 * @param e Evenement du click.
	 */
	public void about(ActionEvent e) {
		String about = """
				A propos de ROBI IDE
				____________________
				
				ROBI est un scripting langage permettant de générer des animations facilement.
				
				Liste des developpeurs: 
				- EL OUAZIZI Walid
				- KALDA Taha
				- HAJJI Youssef
				- ECHERARHI Hamza
				""";
		
		
		Alert alert = new Alert(AlertType.NONE, about, ButtonType.CLOSE);
		alert.setTitle("A propos de ROBI IDE");
		alert.show();
	}
	
	public void save(ActionEvent e) {
		ExtensionFilter filter = new ExtensionFilter("Robi File (*.robi)", "*.robi");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez un emplacement de sauvegarde");
		fc.getExtensionFilters().add(filter);
		try {
			if(codeTextArea.getText().isBlank()) throw new IOException();
			File f = fc.showSaveDialog(null);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for(String line : codeTextArea.getText().split("\n")) {
				bw.append(line + "\n");
			}
			bw.close();
		} catch(Exception ex) {
			new Alert(AlertType.ERROR, "Impossible de sauvegarder").show();
			return;
		}
		new Alert(AlertType.INFORMATION, "Fichier sauvegardé.").show();
	}
	
	public void load(ActionEvent e) {
		ExtensionFilter filter = new ExtensionFilter("Robi File (*.robi)", "*.robi");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez un emplacement de sauvegarde");
		fc.getExtensionFilters().add(filter);
		if(!codeTextArea.getText().isBlank()) {
			ButtonType bt = new Alert(AlertType.CONFIRMATION, "Vous êtes sur le point d'ouvrir un fichier alors que la zone de code n'est pas vide.\nEtes-vous sur ?", ButtonType.YES, ButtonType.NO).showAndWait().get();
			if(bt != ButtonType.YES) return;
		}
		try {
			File f = fc.showOpenDialog(null);
			codeTextArea.clear();
			codeTextArea.setText("");
			for(String line : Files.readAllLines(Paths.get(f.getAbsolutePath()))) {
				codeTextArea.appendText(line + "\n");
			}
		} catch(Exception ex) {
			new Alert(AlertType.ERROR, "Impossible de charger").show();
			return;
		}
	}

}
