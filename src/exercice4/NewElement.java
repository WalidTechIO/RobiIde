package exercice4;

import graphicLayer.GElement;
import stree.parser.SNode;

public class NewElement implements Command {
	@Override
	public Reference run(Reference reference, SNode method) {
if(method.size() != 2) throw new IllegalArgumentException("new called with wrong number of args");
		
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getRef()).getDeclaredConstructor().newInstance();
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			return ref;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		return run(reference, method);
	}

}
