package exercice5_6.commands;

import exercice5_6.Environment;
import exercice5_6.Interpreter;
import exercice5_6.Reference;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GString;
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
		
		//Images et textes n'ont pas d'enfants (pas d'add, de del ou de clear)
		//Les images n'ont pas besoin de couleur ni de dimension (Le graphicsLayer n'integere pas de methode permettant de leur assigner une dimension)
		//Tout les objets peuvent avoir ou supprimer des scripts et translater dans l'espace
		ref.addCommand("translate", new Translate());
		ref.addCommand("addScript", new AddScript(env));
		ref.addCommand("delScript", new DeleteScript());
		if(!(element instanceof GImage)) {
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("setDim", new SetDimension());
			if(!(element instanceof GString)) {
				ref.addCommand("add", new AddElement(env));
				ref.addCommand("del", new DelElement(env));
				ref.addCommand("clear", new Clear(env));
			}
		}
		
		((GContainer) reference.getRef()).addElement(element);
		((GContainer) reference.getRef()).repaint();
		
		env.addReference(refName, ref);
		
		return ref;
	}

}
