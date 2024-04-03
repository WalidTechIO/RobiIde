package partie2.server.commands;

import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;
import graphicLayer.GBounded;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class AddElement implements Command {
	
	private final Environment env;
	
	public AddElement(Environment environment) {
		env = environment;
	}

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("AddElement: Required 4 args, passed: " + method.size());
		
		String refName = method.get(0).contents() + "." + method.get(2).contents();
		Reference ref = new Interpreter().compute(env, method.get(3));
		if(ref == null) {
			return null;
		}
		
		GElement element = (GElement) ref.getRef();
		
		ref.addCommand("setColor", new SetColor());
		ref.addCommand("translate", new Translate());
		ref.addCommand("addScript", new AddScript(env));
		ref.addCommand("delScript", new DeleteScript());
		if(element instanceof GBounded) {
			ref.addCommand("setDim", new SetDimension());
			ref.addCommand("add", new AddElement(env));
			ref.addCommand("del", new DelElement(env));
			ref.addCommand("clear", new Clear());
		}
		
		((GContainer) reference.getRef()).addElement(element);
		((GContainer) reference.getRef()).repaint();
		
		env.addReference(refName, ref);
		
		return ref;
	}

}
