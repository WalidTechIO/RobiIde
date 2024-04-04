package partie2.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DebugControleur {
	
	@FXML
	private ListView<String> env;
	
	@FXML
	private ListView<String> call;
	
	public void addCall(String expr) {
		call.getItems().add(expr);
		call.scrollTo(call.getItems().size());
	}
	
	public void displayEnv(String environment) {
		env.getItems().clear();
		for(String s : environment.split("\n")) env.getItems().add(s);
	}

}
