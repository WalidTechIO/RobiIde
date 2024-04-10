package partie2.server.commands;

import partie2.io.graphics.GImage;
import partie2.io.graphics.GObject;
import partie2.io.graphics.GText;
import partie2.server.Environment;
import partie2.server.Interpreter;
import partie2.server.Reference;

import java.util.Objects;

import stree.parser.SDefaultNode;
import stree.parser.SNode;

public class AddElement implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("AddElement: Required 4 args, passed: " + method.size());
		
		Environment env = interpreter.getEnvironment();
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		String refParent = method.get(0).contents();
		String refName = refParent + "." + method.get(2).contents();
		Reference newRef = interpreter.compute(method.get(3));
		
		if(newRef == null) return null;
		
		GObject element = (GObject) newRef.getRef();
		
		//Images et textes n'ont pas d'enfants (pas d'add, de del ou de clear)
		//Tout les objets peuvent avoir ou supprimer des scripts, definir leur couleur, leur dimension et translater dans l'espace
		newRef.addCommand("translate", new Translate());
		newRef.addCommand("addScript", new AddScript());
		newRef.addCommand("delScript", new DeleteScript());
		newRef.addCommand("setDim", new SetDimension());
		newRef.addCommand("setColor", new SetColor());
			
		if(!(element instanceof GImage) && !(element instanceof GText)) {
			newRef.addCommand("add", new AddElement());
			newRef.addCommand("del", new DelElement());
			newRef.addCommand("clear", new Clear());
		}
		
		//On prend le partie d'override la reference si elle etait deja presente (Nos variables robi ne sont pas des constantes)
		if(env.getReferenceByName(refName) != null) {
			SNode delMethod = new SDefaultNode();
			for(int i=0;i<3;i++) delMethod.addChild(new SDefaultNode());
			delMethod.get(0).setContents(refParent);
			delMethod.get(2).setContents(refName);
			new DelElement().run(interpreter, delMethod);
		}
		
		((GObject) reference.getRef()).add(element);
		env.addReference(refName, newRef);
		
		return newRef;
	}

}
