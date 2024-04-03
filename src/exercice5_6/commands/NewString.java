package exercice5_6.commands;

import exercice5_6.Reference;
import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 2) throw new IllegalArgumentException("NewString: Required 3 args, passed: " + method.size());
		String msg = method.get(3).contents();
		msg = msg.substring(1, msg.length() - 1);
		GString obj = new GString(msg);
		return new Reference(obj);
	}

}
