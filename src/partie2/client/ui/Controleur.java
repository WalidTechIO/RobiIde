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

import com.fasterxml.jackson.databind.ObjectMapper;

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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import partie2.client.Client;
import partie2.io.DebugInfo;
import partie2.io.Program;
import partie2.io.Program.Mode;
import partie2.io.State;
import partie2.utils.SceneWrapper;
import partie2.utils.UIUtils;

/**
 * Controleur de l'UI.
 */
public class Controleur {
	
	/**
	 * Controleur de la vue de debug.
	 */
	private DebugControleur debug = null;
	
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
	 * Bouton MenuBar Debug View.
	 */
	@FXML
	private MenuItem buttonDebug;
	
	/**
	 * Bouton MenuBar Logout.
	 */
	@FXML
	private MenuItem exportDebug;
	
	/**
	 * Bouton MenuBar Logout.
	 */
	@FXML
	private MenuItem importDebug;
	
	/**
	 * Bouton MenuBar Logout.
	 */
	@FXML
	private MenuItem buttonLogout;
	
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
		buttonDebug.setOnAction(this::debug);
		buttonAbout.setOnAction(this::about);
		buttonLoad.setOnAction(this::load);
		buttonSave.setOnAction(this::save);
		buttonLogout.setOnAction(this::logout);
		exportDebug.setOnAction(this::exportDebug);
		importDebug.setOnAction(this::importDebug);
		feedbackArea.setEditable(false);
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
				""";
		
		
		Alert alert = new Alert(AlertType.NONE, about, ButtonType.CLOSE);
		alert.setTitle("A propos de ROBI IDE");
		alert.show();
	}
	
	/**
	 * Enregistrement du script robi courant
	 */
	public void save(ActionEvent e) {
		ExtensionFilter filter = new ExtensionFilter("Robi File (*.robi)", "*.robi");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez un emplacement de sauvegarde");
		fc.getExtensionFilters().add(filter);
		try {
			if(codeTextArea.getText().isBlank()) throw new IOException();
			File f = fc.showSaveDialog(null);
			if(f == null) return;
			String path = f.getAbsolutePath();
			if(!path.endsWith(".robi")) {
				f = new File(path + ".robi");
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(codeTextArea.getText());
			bw.close();
		} catch(Exception ex) {
			new Alert(AlertType.ERROR, "Impossible de sauvegarder").show();
			return;
		}
		new Alert(AlertType.INFORMATION, "Fichier sauvegardé.").show();
	}
	
	/**
	 * Chargement d'un script
	 */
	public void load(ActionEvent e) {
		ExtensionFilter filter = new ExtensionFilter("Robi File (*.robi)", "*.robi");
		FileChooser fc = new FileChooser();
		fc.setTitle("Ouvrir un script ROBI");
		fc.getExtensionFilters().add(filter);
		if(!codeTextArea.getText().isBlank()) {
			ButtonType bt = new Alert(AlertType.CONFIRMATION, "Vous êtes sur le point d'ouvrir un fichier alors que la zone de code n'est pas vide.\nEtes-vous sur ?", ButtonType.YES, ButtonType.NO).showAndWait().get();
			if(bt != ButtonType.YES) return;
		}
		try {
			File f = fc.showOpenDialog(null);
			if(f == null) return;
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
	
	/**
	 * Deconnexion du serveur
	 */
	public void logout(ActionEvent e) {
		endDebug(null);
		try {
			client.close();
			SceneWrapper<LoginControleur> loginWrapper = UIUtils.login();
			((Stage)buttonExecute.getScene().getWindow()).setScene(loginWrapper.scene());
		} catch (IOException ignored) {}
	}
	
	/**
	 * Affichage de la vue de debug
	 */
	public void debug(ActionEvent e) {
		if(isDebugging()) return;
		try {
			SceneWrapper<DebugControleur> debugWrapper = UIUtils.debug();
			Stage stage = new Stage();
			stage.setOnCloseRequest(this::endDebug);
			debug = debugWrapper.controller();
			stage.setScene(debugWrapper.scene());
			stage.setTitle("IDE ROBI - Debug info");
			stage.show();
		} catch(Exception ex) {
			new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la fenetre de debug").show();
		}
		
	}
	
	/**
	 * Exporter l'etat courant
	 */
	public void exportDebug(ActionEvent e) {
		String resp = client.exportState();
		ExtensionFilter filter = new ExtensionFilter("JSON file (*.json)", "*.json");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez un emplacement de sauvegarde");
		fc.getExtensionFilters().add(filter);
		
		try {
			File f = fc.showSaveDialog(null);
			if(f == null) return;
			String path = f.getAbsolutePath();
			if(!path.endsWith(".json")) {
				f = new File(path + ".json");
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(resp);
			bw.close();
		} catch(IOException ex) {
			new Alert(Alert.AlertType.ERROR, "Export impossible").show();
		}
	}
	
	/**
	 * Ouvrir une liseuse d'etat
	 */
	public void importDebug(ActionEvent e) {
		//Prepare FileChooser with extension filter
		ExtensionFilter filter = new ExtensionFilter("JSON file (*.json)", "*.json");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez un export ROBI a ouvrir");
		fc.getExtensionFilters().add(filter);
		try {
			//Get filepath (return if null)
			File f = fc.showOpenDialog(null);
			if(f == null) return;
			
			//Turn file into Response object
			String json = Files.readAllLines(Paths.get(f.getAbsolutePath())).stream().reduce((a,s) -> a+=s+"\n").get();
			ObjectMapper mapper = new ObjectMapper();
			State res = mapper.readValue(json, State.class);
			
			//Prepare viewport window which will display env values
			SceneWrapper<DebugControleur> debugWrapper = UIUtils.debug();
			final Stage stage1 = new Stage();
			stage1.setScene(debugWrapper.scene());
			stage1.setTitle("IDE ROBI - RRE Viewport");
			
			//Prepare viewport window which will display image and feedback of the response
			SceneWrapper<RreViewportControleur> rreWrapper = UIUtils.rreViewPort();
			final Stage stage2 = new Stage();
			stage2.setScene(rreWrapper.scene());
			stage2.setTitle("IDE ROBI - RRE Viewport");
			
			//Add Close Event Listener on both windows
			stage1.setOnCloseRequest(event -> {
				stage1.close();
				stage2.close();
			});
			stage2.setOnCloseRequest(event -> {
				stage1.close();
				stage2.close();
			});
			
			//Show windows
			stage1.show();
			stage2.show();
			
			if(res.request() == null && res.response() == null) throw new Exception();
			
			//Send response data to both controller of the both windows
			rreWrapper.controller().setViewport(res);
			if(res.response() != null) debugWrapper.controller().debugReceipt(res.response().info());
			
		} catch(Exception ex) {
			new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir l'export.").show();;
		}
	}

	/**
	 * Callback de fermeture de la fenetre de debug
	 */
	private Object endDebug(WindowEvent event) {
		if(!isDebugging()) return null;
		debug.close();
		debug = null;
		return null;
	}

	public boolean isDebugging() {
		return debug != null;
	}

	public void debugReceipt(DebugInfo info) {
		if(isDebugging()) debug.debugReceipt(info);
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

}
