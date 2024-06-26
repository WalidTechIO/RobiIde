package partie2.utils;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import partie2.client.ui.Controleur;
import partie2.client.ui.DebugControleur;
import partie2.client.ui.IHMRobiMain;
import partie2.client.ui.LoginControleur;
import partie2.client.ui.RreViewportControleur;

//Generation of scenes the application use
public class UIUtils {
	
	public static SceneWrapper<LoginControleur> login() throws IOException {
		URL url = IHMRobiMain.class.getResource("resources/login.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		VBox root = (VBox) fxmlLoader.load();
		
		return new SceneWrapper<LoginControleur>(new Scene(root, 600, 380), fxmlLoader.getController());
	}
	
	public static SceneWrapper<DebugControleur> debug() throws IOException {
		URL url = IHMRobiMain.class.getResource("resources/debug.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		VBox root = (VBox) fxmlLoader.load();
		
		return new SceneWrapper<DebugControleur>(new Scene(root, 1334, 200), fxmlLoader.getController());
	}
	
	public static SceneWrapper<Controleur> main() throws IOException {
		URL url = IHMRobiMain.class.getResource("resources/ui.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		VBox root = (VBox) fxmlLoader.load();
		
		return new SceneWrapper<Controleur>(new Scene(root, 680, 492), fxmlLoader.getController());
	}
	
	public static SceneWrapper<RreViewportControleur> rreViewPort() throws IOException {
		URL url = IHMRobiMain.class.getResource("resources/rreViewPort.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		VBox root = (VBox) fxmlLoader.load();
		
		return new SceneWrapper<RreViewportControleur>(new Scene(root, 410, 540), fxmlLoader.getController());
	}

}
