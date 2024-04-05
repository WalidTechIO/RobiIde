package partie2.server.commands;

import java.util.Objects;

import partie2.io.graphics.GObject;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class SetColor implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("SetColor: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		((GObject)reference.getRef()).setColor(method.get(2).contents());
		
		return reference;
	}

}
