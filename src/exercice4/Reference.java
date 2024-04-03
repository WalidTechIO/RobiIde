package exercice4;

import java.util.HashMap;
import java.util.Map;

import stree.parser.SNode;

public class Reference {
	
	private final Object reference;
	private final Map<String, Command> primitives = new HashMap<>();
	
	public Reference(Object reference) {
		this.reference = reference;
	}
	
	public Command getCommandByName(String selector) {
		return primitives.get(selector);
	}
	
	public void addCommand(String selector, Command primitive) {
		primitives.put(selector, primitive);
	}
	
	//Ex 4.1
	public Reference run(SNode method) {
		if(method.size() < 2) throw new IllegalArgumentException("Method need at least one reference and one method name");
		return primitives.get(method.children().get(1).contents()).run(this, method);
	}
	
	//Ex 4.2
	public Reference run(Environment environment, SNode method) {
		if(method.size() < 2) throw new IllegalArgumentException("Method need at least one reference and one method name");
		return primitives.get(method.children().get(1).contents()).run(this, environment ,method);
	}
	
	public Object getRef() {
		return reference;
	}

}
