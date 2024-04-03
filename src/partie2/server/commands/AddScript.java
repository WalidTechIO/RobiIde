package partie2.server.commands;

import java.util.Objects;

import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;
import partie2.server.Script;
import stree.parser.SNode;

public class AddScript implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		Environment env = interpreter.getEnvironment();
		if(method.size() != 4) throw new IllegalArgumentException("AddScript: Required 4 args, passed: " + method.size());
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		String refName = env.getNameByReference(reference);
		if(refName == null) throw new NullPointerException("Try to add a Script to an unreferenced object.");
		Script script = new Script(refName, method.get(3));
		reference.addScript(method.get(2).contents(), script);
		return reference;
	}

}
