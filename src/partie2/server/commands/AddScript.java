package partie2.server.commands;

import java.util.Objects;

import partie2.server.Interpreter;
import partie2.server.Reference;
import partie2.server.Script;
import stree.parser.SNode;

public class AddScript implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("AddScript: Required 4 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		String scriptName = method.get(2).contents();
		
		if(reference.getCommandByName(scriptName) != null) throw new IllegalArgumentException("AddScript: A primitive called " + scriptName + " already exists for the entity " + method.get(0).contents());
		
		Script script = new Script(method.get(0).contents(), method.get(3));
		reference.addScript(scriptName, script);
		
		return reference;
	}

}
