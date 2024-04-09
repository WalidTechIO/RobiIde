package partie2.server.commands;

import partie2.io.graphics.GObject;
import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;

import java.util.Objects;

import stree.parser.SNode;

public class DelElement implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("DelElement: Required 3 args, passed: " + method.size());
		Environment env = interpreter.getEnvironment();
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		String toDelete = method.get(2).contents();
		
		if(!toDelete.startsWith(method.get(0).contents()+".")) throw new IllegalArgumentException(toDelete + " is not a children of " + method.get(0).contents());
		
		GObject element = (GObject) env.getReferenceByName(toDelete).getRef();
		((GObject) reference.getRef()).del(element);
		env.clear(toDelete); //Unreference element and all his children
		
		return reference;
	}

}
