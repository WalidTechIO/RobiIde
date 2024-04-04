package partie2.client.ui;

import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class DebugControleur {
	
	@FXML
	private ListView<String> env;
	
	@FXML
	private ListView<String> call;
	
	@FXML
	private ListView<String> scripts;
	
	private Map<String, List<String>> scriptsMap = null;
	
	public void addCall(String expr) {
		call.getItems().add(expr);
		call.scrollTo(call.getItems().size());
	}
	
	public void displayEnv(String environment) {
		env.getItems().clear();
		for(String s : environment.split("\n")) env.getItems().add(s);
	}
	
	public void initialize() {
		env.setOnMouseClicked(this::onReferenceSelected);
	}
	
	public void onReferenceSelected(MouseEvent e) {
		String ref = env.getSelectionModel().getSelectedItem();
		scripts.getItems().clear();
		if(ref != null) {
			ref = ref.split(" ")[0];
			scriptsMap.get(ref).forEach((s) -> scripts.getItems().add(s));
		}
	}

	public void loadScriptsMap(Map<String, List<String>> scriptsMap) {
		this.scriptsMap = scriptsMap;
	}

}
