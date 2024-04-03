package partie2.server.commands;

import java.awt.Point;

import graphicLayer.GElement;
import partie2.server.Reference;
import stree.parser.SNode;

public class Translate implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {

		if(method.size() != 4) throw new IllegalArgumentException("Translate: Required 4 args, passed: " + method.size());
		
		Point vec = new Point(Integer.parseInt(method.get(2).contents()), Integer.parseInt(method.get(3).contents()));
		((GElement)reference.getRef()).translate(vec);
		
		return reference;
	}

}
