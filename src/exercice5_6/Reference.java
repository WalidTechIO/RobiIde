package exercice5_6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exercice5_6.commands.Command;
import stree.parser.SNode;

public class Reference {
	
	private final Object reference;
	private final Map<String, Command> primitives = new HashMap<>();
	private final Map<String, Script> scripts = new HashMap<>();
	
	public Reference(Object reference) {
		this.reference = reference;
	}
	
	public Command getCommandByName(String selector) {
		return primitives.get(selector);
	}
	
	public void addCommand(String selector, Command primitive) {
		primitives.put(selector, primitive);
	}
	
	public Reference run(Environment environment, SNode method) {
		if(method.size() < 2) throw new IllegalArgumentException("Entity " + environment.getNameByReference(this) + ": Wrong run call");
		
		String methodName = method.get(1).contents();
		
		//Primitive
		if(primitives.containsKey(methodName)) {
			return primitives.get(method.get(1).contents()).run(this, method);
		}
		
		//Scripts
		if(scripts.containsKey(methodName)) {
			List<String> argsList = new ArrayList<>();
			for(int i = 2; i<method.size(); i++) {
				if(method.get(i).isLeaf()) argsList.add(method.get(i).contents());
				else throw new IllegalArgumentException("Pass an instruction as param for a script is forbidden");
			}
			
			return scripts.get(methodName).call(environment, argsList);
		}
		
		throw new IllegalArgumentException("Method \"" + methodName + "\" is unknow for entity: " + environment.getNameByReference(this));
	}
	
	public Object getRef() {
		return reference;
	}
	
	public void addScript(String name, Script script) {
		scripts.put(name, script);
	}
	
	public void deleteScript(String name) {
		scripts.remove(name);
	}

}
