package partie2.server.commands;

import java.util.Objects;

import graphicLayer.GElement;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class NewElement implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 2) throw new IllegalArgumentException("NewElement: Required 2 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getRef()).getDeclaredConstructor().newInstance();
			return new Reference(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
