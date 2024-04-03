package partie2.server.commands;

import java.awt.Dimension;
import java.util.Objects;

import graphicLayer.GBounded;
import graphicLayer.GSpace;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class SetDimension implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("SetDimension: Required 4 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		Dimension dim = null;
		try {
			dim = new Dimension(Integer.parseInt(method.get(2).contents()), Integer.parseInt(method.get(3).contents()));
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage().split(":")[1].trim().replace("\"", "") + " is not a valid dimension value.");
		}
		if(reference.getRef() instanceof GBounded bndref) {
			bndref.setWidth(dim.width);
			bndref.setHeight(dim.height);
		}
		
		if(reference.getRef() instanceof GSpace sparef) {
			sparef.changeWindowSize(dim);
		}
		
		return reference;
	}

}
