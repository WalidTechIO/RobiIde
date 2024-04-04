package partie2.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import partie2.io.ScriptInfo;
import stree.parser.SDefaultNode;
import stree.parser.SNode;

public class Script {

	private final SNode proto;
	private final SNode script;
	private SNode replaced;
	private final String refName;
	private final Map<Integer, String> params = new HashMap<>();
	
	//Constructeur d'un script
	public Script(String referenceName, SNode script) {
		if(script.size() < 1 || script.get(0).size() < 1) throw new IllegalArgumentException();
		refName = referenceName;
		
		//On recupere le nom et l'ordre des arguments du script
		int pos = 0;
		for(SNode node : script.get(0).children()) {
			params.put(pos++, node.contents());
		}
		
		//On stock le script et le prototype
		this.proto = script.get(0);
		this.script = new SDefaultNode();
		for(int i = 1; i<script.size(); i++) this.script.addChild(script.get(i));
	}
	
	//Methode de demande d'execution d'un script
	public Reference call(Interpreter interpreter, List<String> args) {
		if(args.size() != params.size() - 1) throw new IllegalArgumentException();
		Environment environment = interpreter.getEnvironment();
		
		Map<String, String> replaceValues = new HashMap<>();
		replaceValues.put(params.get(0), refName);
		for(int i = 0; i<args.size(); i++) replaceValues.put(params.get(i+1), args.get(i));
		
		replaced = copy(script);
		
		replace(replaced, replaceValues);
		
		for(SNode children : replaced.children()) {
			interpreter.compute(children);
		}
		
		return environment.getReferenceByName(refName);
	}
	
	//Parcourt d'un arbre en profondeur et remplacement de certaines valeurs selon la map passé en parametre
	void replace(SNode node, Map<String, String> replaceValues) {
		if(node.isLeaf()) {
			for(String paramName : replaceValues.keySet()) {
				
				if(node.contents().contains("." + paramName) || node.contents().contains(paramName+ ".")) {
					node.setContents(node.contents().replace(paramName, replaceValues.get(paramName)));
				}
				
				if(node.contents().equals(paramName)) {
					node.setContents(replaceValues.get(paramName));
				}
			}
		} else {
			for(SNode children : node.children()) {
				replace(children, replaceValues);
			}
		}
	}
	
	//Renvoie les infos du script
	public ScriptInfo info() {
		return new ScriptInfo(params.size()-1, Interpreter.nodeToFormattedString(script), Interpreter.nodeToString(proto));
	}
	
	//Renvoie une copie en profondeur de l'arbre original
	private SNode copy(SNode original) {
		SNode node = new SDefaultNode();
		if(original.isLeaf()) {
			node.setContents(original.contents());
		} else {
			for(SNode children : original.children()) node.addChild(copy(children));
		}
		return node;
	}
	
}
