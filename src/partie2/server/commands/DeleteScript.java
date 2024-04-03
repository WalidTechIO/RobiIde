package partie2.server.commands;

import java.util.Objects;

import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class DeleteScript implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("DelScript: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		reference.deleteScript(method.get(2).contents());
		
		return null;
	}

}
