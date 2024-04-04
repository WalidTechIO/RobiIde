package partie2.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class DebugControleur {
	
	@FXML
	private TextArea env;
	
	@FXML
	private TextArea call;
	
	public void addCall(String expr) {
		call.appendText(expr + "\n\n");
	}
	
	public void displayEnv(String environment) {
		env.setText(environment);
	}

}
