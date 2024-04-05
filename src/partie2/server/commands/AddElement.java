package partie2.server.commands;

import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;

import java.util.Objects;

import graphicLayer.GBounded;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class AddElement implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("AddElement: Required 4 args, passed: " + method.size());
		
		Environment env = interpreter.getEnvironment();
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		String refName = method.get(0).contents() + "." + method.get(2).contents();
		Reference newRef = interpreter.compute(method.get(3));
		
		if(newRef == null) return null;
		
		GElement element = (GElement) newRef.getRef();
		
		newRef.addCommand("setColor", new SetColor());
		newRef.addCommand("translate", new Translate());
		newRef.addCommand("addScript", new AddScript());
		newRef.addCommand("delScript", new DeleteScript());
		if(element instanceof GBounded) {
			newRef.addCommand("setDim", new SetDimension());
			newRef.addCommand("add", new AddElement());
			newRef.addCommand("del", new DelElement());
			newRef.addCommand("clear", new Clear());
		}
		
		((GContainer) reference.getRef()).addElement(element);
		((GContainer) reference.getRef()).repaint();
		
		env.addReference(refName, newRef);
		
		return newRef;
	}

}
