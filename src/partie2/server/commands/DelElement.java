package partie2.server.commands;

import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;

import java.util.Objects;

import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class DelElement implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		Environment env = interpreter.getEnvironment();
		if(method.size() != 3) throw new IllegalArgumentException("DelElement: Required 3 args, passed: " + method.size());
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		GElement element = (GElement) env.getReferenceByName(method.get(2).contents()).getRef();
		((GContainer) reference.getRef()).removeElement(element);
		((GContainer) reference.getRef()).repaint();
		env.deleteReference(method.get(2).contents());
		
		return reference;
	}

}
