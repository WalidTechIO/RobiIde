package partie2.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import partie2.io.ReferenceInfo;
import partie2.io.ScriptInfo;
import partie2.server.commands.Command;
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
	
	public Reference run(Interpreter interpreter, SNode method) {
		
		Environment environment = interpreter.getEnvironment();
		
		if(method.size() < 2) throw new IllegalArgumentException("Entity " + environment.getNameByReference(this) + ": Wrong run call");
		
		String methodName = method.get(1).contents();
		
		//Primitive
		if(primitives.containsKey(methodName)) {
			return primitives.get(method.get(1).contents()).run(interpreter, method);
		}
		
		//Scripts
		if(scripts.containsKey(methodName)) {
			List<String> argsList = new ArrayList<>();
			for(int i = 2; i<method.size(); i++) {
				if(method.get(i).isLeaf()) argsList.add(method.get(i).contents());
				else throw new IllegalArgumentException("Pass an instruction as param for a script is forbidden");
			}
			
			return scripts.get(methodName).call(interpreter, argsList);
		}
		
		throw new IllegalArgumentException("Method \"" + methodName + "\" is unknow for the entity: " + environment.getNameByReference(this));
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
	
	ReferenceInfo info() {
		String className = reference.getClass().getSimpleName();
		if(className.equals("Class")) {
			Class<?> classe = (Class<?>) reference;
			className = "The class " + classe.getSimpleName();
		} else {
			className = "An instance of " + className;
		}
		List<String> primitivesName = new ArrayList<>(primitives.keySet());
		Map<String, ScriptInfo> scriptsInfo = new HashMap<>();
		scripts.forEach((n,s) -> {
			scriptsInfo.put(n, s.info());
		});
		
		return new ReferenceInfo(className, primitivesName, scriptsInfo);
	}

}
