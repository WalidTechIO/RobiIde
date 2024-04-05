package partie2.server.commands;

import java.util.Objects;

import partie2.io.graphics.GText;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class NewString implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("NewString: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		String msg = method.get(2).contents();
		msg = msg.substring(1, msg.length() - 1); //Remove double quote from start and end
		GText obj = new GText(msg);
		
		return new Reference(obj);
	}

}
