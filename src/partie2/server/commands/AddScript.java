package partie2.server.commands;

import partie2.server.Environment;
import partie2.server.Reference;
import partie2.server.Script;
import stree.parser.SNode;

public class AddScript implements Command {
	
	private final Environment env;
	
	public AddScript(Environment environment) {
		env = environment;
	}

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("AddScript: Required 4 args, passed: " + method.size());
		String refName = env.getNameByReference(reference);
		if(refName == null) throw new NullPointerException("Try to add a Script to an unreferenced object.");
		Script script = new Script(refName, method.get(3));
		reference.addScript(method.get(2).contents(), script);
		return reference;
	}

}
