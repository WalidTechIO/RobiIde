package partie2.server.commands;

import java.util.Objects;

import graphicLayer.GString;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class NewString implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 2) throw new IllegalArgumentException("NewString: Required 3 args, passed: " + method.size());
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		String msg = method.get(3).contents();
		msg = msg.substring(1, msg.length() - 1);
		GString obj = new GString(msg);
		return new Reference(obj);
	}

}
