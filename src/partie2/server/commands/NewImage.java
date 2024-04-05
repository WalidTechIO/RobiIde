package partie2.server.commands;

import java.util.Objects;

import partie2.io.graphics.GImage;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class NewImage implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("NewImage: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		return new Reference(new GImage(method.get(2).contents()));
	}

}
