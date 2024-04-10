package partie2.client.ui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import partie2.client.Client;
import partie2.utils.SceneWrapper;
import partie2.utils.UIUtils;

public class LoginControleur {
	
	@FXML
	private TextField ip;
	
	@FXML
	private TextField port;
	
	@FXML
	private Button login;
	
	public void initialize() {
		login.setOnMouseClicked(this::loginAttempt);
		ip.setOnKeyPressed(this::enterPressedOnInput);
		port.setOnKeyPressed(this::enterPressedOnInput);
		
	}
	
	public void loginAttempt(MouseEvent e) {
		InetSocketAddress addr = null;
		try {
			InetAddress adr = InetAddress.getByName(ip.getText());
			int srvPort = Integer.parseInt(port.getText());
			addr = new InetSocketAddress(adr, srvPort);
		} catch(IllegalArgumentException ex) {
			new Alert(Alert.AlertType.ERROR, "Port incorrect").show();
			return;
		} catch (UnknownHostException ex) {
			new Alert(Alert.AlertType.ERROR, "Hote introuvable").show();
			return;
		}	
		
		try {
			SceneWrapper<Controleur> mainWrapper = UIUtils.main();
			Client client = new Client(addr, mainWrapper.controller());
			mainWrapper.controller().setClient(client);
			((Stage)ip.getScene().getWindow()).setScene(mainWrapper.scene());
		} catch (IOException ex) {
			new Alert(Alert.AlertType.ERROR, "Impossible de se connecter au serveur").show();
		}
	}
	
	private void enterPressedOnInput(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			loginAttempt(null);
		}
	}

}
