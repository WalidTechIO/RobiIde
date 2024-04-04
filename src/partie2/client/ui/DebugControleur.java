package partie2.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import partie2.io.DebugInfo;
import partie2.io.ReferenceInfo;
import partie2.io.ScriptInfo;
import partie2.utils.NodeUtils;

public class DebugControleur {
	
	@FXML
	private ListView<String> env;
	
	@FXML
	private ListView<String> call;
	
	@FXML
	private ListView<String> scripts;
	
	@FXML
	private TextArea info;
	
	private DebugInfo dbginfo;
	
	public void initialize() {
		env.setOnMouseClicked(this::onReferenceSelected);
		scripts.setOnMouseClicked(this::onScriptSelected);
		call.setOnMouseClicked(this::onExpressionSelected);
	}
	
	public void setInfo(String message) {
		info.setText(message);
	}
	
	public void addCall(String expr) {
		call.getItems().add(expr);
		call.scrollTo(call.getItems().size());
	}
	
	public void onReferenceSelected(MouseEvent e) {
		String ref = env.getSelectionModel().getSelectedItem();
		scripts.getItems().clear();
		if(ref != null) {
			ReferenceInfo refInfo = dbginfo.env().get(ref);
			refInfo.scripts().forEach((n,s) -> {
				scripts.getItems().add(n);
			});
			String infoMsg = "Type: Reference\nNom: " + ref + "\nReference type: " + refInfo.className() + "\nListe des primitives:\n";
			for(String s : refInfo.primitives()) infoMsg += "\t- " + s + "\n"; 
			setInfo(infoMsg);
		}
	}
	
	public void onScriptSelected(MouseEvent e) {
		String ref = env.getSelectionModel().getSelectedItem();
		String scriptName = scripts.getSelectionModel().getSelectedItem();
		if(ref != null && scriptName != null) {
			ref = ref.split(" ")[0];
			ScriptInfo scriptInfo = dbginfo.env().get(ref).scripts().get(scriptName);
			String infoMsg = "Type: Script\nNom: " + scriptName + "\nNb de parametres: " + scriptInfo.nbParams() + "\nPrototpye: " + scriptInfo.proto() +"\nExpression complète:\n" + scriptInfo.expr();
			setInfo(infoMsg);
		}
		
	}
	
	public void onExpressionSelected(MouseEvent e) {
		String expr = call.getSelectionModel().getSelectedItem();
		if(expr != null) {
			String infoMsg = "Type: Expression\nFormattage:\n" + NodeUtils.stringToFormattedString(expr);
			setInfo(infoMsg);
		}
	}
	
	public void debugReceipt(DebugInfo dbginfo) {
		this.dbginfo = dbginfo;
		addCall(dbginfo.expr());
		env.getItems().clear();
		scripts.getItems().clear();
		dbginfo.env().forEach((n,r) -> {
			env.getItems().add(n);
		});
	}
	
	

}
