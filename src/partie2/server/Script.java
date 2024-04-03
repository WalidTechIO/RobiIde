package partie2.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stree.parser.SDefaultNode;
import stree.parser.SNode;

public class Script {

	private final SNode script;
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
		
		//On stock le script sans le prototype
		this.script = new SDefaultNode();
		for(int i = 1; i<script.size(); i++) this.script.addChild(script.get(i));
	}
	
	//Methode de demande d'execution d'un script
	public Reference call(Environment environment, List<String> args) {
		if(args.size() != params.size() - 1) throw new IllegalArgumentException();
		
		Map<String, String> replaceValues = new HashMap<>();
		replaceValues.put(params.get(0), refName);
		for(int i = 0; i<args.size(); i++) replaceValues.put(params.get(i+1), args.get(i));
		
		replace(script, replaceValues);
		
		for(SNode children : script.children()) {
			new Interpreter().compute(environment, children);
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
}
