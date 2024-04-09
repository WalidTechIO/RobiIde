package exercice5_6.commands;

import exercice5_6.Environment;
import exercice5_6.Reference;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class DelElement implements Command {
	
	private final Environment env;
	
	public DelElement(Environment environment) {
		env = environment;
	}

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("DelElement: Required 3 args, passed: " + method.size());
		
		String toDelete = method.get(2).contents();
		
		if(!toDelete.startsWith(method.get(0).contents()+".")) throw new IllegalArgumentException(toDelete + " is not a children of " + method.get(0).contents());
		
		GElement element = (GElement) env.getReferenceByName(toDelete).getRef();
		((GContainer) reference.getRef()).removeElement(element);
		((GContainer) reference.getRef()).repaint();
		env.clear(toDelete);
		
		return reference;
	}

}
