package partie2.server.commands;

import java.util.Objects;

import graphicLayer.GContainer;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class Clear implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 2) throw new IllegalArgumentException("Clear: Required 2 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		((GContainer)reference.getRef()).clear();
		
		return reference;
	}

}
