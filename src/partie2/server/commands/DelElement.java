package partie2.server.commands;

import partie2.server.Environment;
import partie2.server.Reference;
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
		
		GElement element = (GElement) env.getReferenceByName(method.get(2).contents()).getRef();
		((GContainer) reference.getRef()).removeElement(element);
		((GContainer) reference.getRef()).repaint();
		env.deleteReference(method.get(2).contents());
		
		return reference;
	}

}
