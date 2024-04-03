package exercice4;

import java.awt.Point;

import graphicLayer.GElement;
import stree.parser.SNode;

public class Translate implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		
		if(method.size() < 4) throw new IllegalArgumentException("translate called without translation vector");
		
		Point vec = new Point(Integer.parseInt(method.get(2).contents()), Integer.parseInt(method.get(3).contents()));
		((GElement)reference.getRef()).translate(vec);
		
		return reference;
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		// TODO Auto-generated method stub
		return run(reference, method);
	}

}
