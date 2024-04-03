package exercice5_6.commands;

import exercice5_6.Reference;
import graphicLayer.GContainer;
import stree.parser.SNode;

public class Clear implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 2) throw new IllegalArgumentException("Clear: Required 2 args, passed: " + method.size());
		
		((GContainer)reference.getRef()).clear();
		
		return reference;
	}

}
