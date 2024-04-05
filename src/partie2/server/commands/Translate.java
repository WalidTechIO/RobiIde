package partie2.server.commands;

import java.awt.Point;
import java.util.Objects;

import partie2.io.graphics.GObject;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class Translate implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("Translate: Required 4 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		try {
			Point vec = new Point(Integer.parseInt(method.get(2).contents()), Integer.parseInt(method.get(3).contents()));
			((GObject)reference.getRef()).translate(vec.x, vec.y);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage().split(":")[1].trim().replace("\"", "") + " is not a valid translation value.");
		}
		
		return reference;
	}

}
