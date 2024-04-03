package partie2.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Point de demarrage de l'UI.
 */
public class IHMRobiMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = IHMRobiMain.class.getResource("ui.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			HBox root = (HBox) fxmlLoader.load();
			
			Scene scene = new Scene(root, 930, 590);
			
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.setTitle("IDE ROBI");
			primaryStage.show();
			
		} catch(IOException e) {
			System.err.println("Erreur de chargement: " + e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
