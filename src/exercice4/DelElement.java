package exercice4;

import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class DelElement implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		throw new UnsupportedOperationException("Del Element need access to environment");
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("Wrong number of args");
		
		GElement element = (GElement) environment.getReferenceByName(method.get(2).contents()).getRef();
		((GSpace) reference.getRef()).removeElement(element);
		((GSpace) reference.getRef()).repaint();
		environment.clear(method.get(2).contents());
		
		return reference;
	}

}
