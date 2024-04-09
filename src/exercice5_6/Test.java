package exercice5_6;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import exercice5_6.commands.AddElement;
import exercice5_6.commands.AddScript;
import exercice5_6.commands.Clear;
import exercice5_6.commands.DelElement;
import exercice5_6.commands.DeleteScript;
import exercice5_6.commands.NewElement;
import exercice5_6.commands.NewImage;
import exercice5_6.commands.NewString;
import exercice5_6.commands.SetColor;
import exercice5_6.commands.SetDimension;
import exercice5_6.commands.Sleep;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Test {

	public static void main(String args[]) {
		
		Environment environment = new Environment();
		
		GSpace space = new GSpace("Exercice 5 et 6 - TEST", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("setDim", new SetDimension());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("clear", new Clear(environment));
		
		spaceRef.addCommand("addScript", new AddScript(environment));
		spaceRef.addCommand("delScript", new DeleteScript());
		
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
		
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// execution des s-expressions compilees
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				new Interpreter().compute(environment, itor.next());
			}
		}
	}
	
}
