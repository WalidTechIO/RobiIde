package partie2.client.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import partie2.utils.SceneWrapper;
import partie2.utils.UIUtils;

import java.io.IOException;

/**
 * Point de demarrage de l'UI.
 */
public class IHMRobiMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			
			SceneWrapper<LoginControleur> loginWrapper = UIUtils.login();
			
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			
			primaryStage.setScene(loginWrapper.scene());
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
