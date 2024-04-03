package exercice4;

import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		String msg = method.get(2).contents();
		msg = msg.substring(1, msg.length() - 1);
		GString obj = new GString(msg);
		return new Reference(obj);
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		return run(reference, method);
	}

}
