package partie2.server.commands;

import partie2.io.graphics.GImage;
import partie2.io.graphics.GObject;
import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;

import java.util.Objects;

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
		
		GObject element = (GObject) newRef.getRef();
		
		newRef.addCommand("setColor", new SetColor());
		newRef.addCommand("translate", new Translate());
		newRef.addCommand("addScript", new AddScript());
		newRef.addCommand("delScript", new DeleteScript());
		if(!(element instanceof GImage)) {
			newRef.addCommand("setDim", new SetDimension());
			newRef.addCommand("add", new AddElement());
			newRef.addCommand("del", new DelElement());
			newRef.addCommand("clear", new Clear());
		}
		
		((GObject) reference.getRef()).add(element);
		
		env.addReference(refName, newRef);
		
		return newRef;
	}

}
