package exercice4;

import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class AddElement implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		throw new UnsupportedOperationException("New Element need access to environment");
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("Wrong number of args");
		
		Reference obj = new Interpreter().compute(environment, method.get(3));
		GElement element = (GElement) obj.getRef();
		obj.addCommand("translate", new Translate());
		obj.addCommand("setColor", new SetColor());
		((GSpace) reference.getRef()).addElement(element);
		((GSpace) reference.getRef()).repaint();
		environment.addReference(method.get(2).contents(), obj);
		
		return obj;
	}

}
