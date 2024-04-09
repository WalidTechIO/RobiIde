package exercice5_6.commands;

import exercice5_6.Environment;
import exercice5_6.Reference;
import exercice5_6.Script;
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
		if(reference.getCommandByName(method.get(2).contents()) != null) throw new IllegalArgumentException("AddScript: A primitive called " + method.get(2).contents() + " already exists for the entity " + method.get(0).contents());
		Script script = new Script(refName, method.get(3));
		reference.addScript(method.get(2).contents(), script);
		return reference;
	}

}
