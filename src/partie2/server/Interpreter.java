package partie2.server;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import partie2.server.commands.AddElement;
import partie2.server.commands.AddScript;
import partie2.server.commands.Clear;
import partie2.server.commands.DelElement;
import partie2.server.commands.DeleteScript;
import partie2.server.commands.NewElement;
import partie2.server.commands.NewImage;
import partie2.server.commands.NewString;
import partie2.server.commands.SetColor;
import partie2.server.commands.SetDimension;
import partie2.server.commands.Sleep;
import stree.parser.SNode;

public class Interpreter {
	
	private final Environment env = new Environment();
	private final GSpace space;
	
	public Interpreter() {
		space = new GSpace("Server", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("setDim", new SetDimension());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("clear", new Clear());
		
		spaceRef.addCommand("addScript", new AddScript());
		spaceRef.addCommand("delScript", new DeleteScript());
		
		spaceRef.addCommand("add", new AddElement());
		spaceRef.addCommand("del", new DelElement());
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		env.addReference("space", spaceRef);
		env.addReference("Rect", rectClassRef);
		env.addReference("Oval", ovalClassRef);
		env.addReference("Image", imageClassRef);
		env.addReference("Label", stringClassRef);
	}
	
	public Reference compute(SNode expr) {
		String receiverName = expr.get(0).contents();
		Reference receiver = env.getReferenceByName(receiverName);
		try {
			if(receiver == null) throw new NullPointerException("Environment doesn't know \"" + receiverName + "\" reference.");
			return receiver.run(this, expr);
		} catch(Exception e) {
			System.err.println(e.getMessage() + "\n");
		}
		return null;
	}

	public Reference getReferenceByNode(SNode node) {
		if(node == null || !node.isLeaf()) return null;
		return env.getReferenceByName(node.contents());
	}
	
	public Environment getEnvironment() {
		return env;
	}

	public BufferedImage snapshot() {
	    int w = space.getWidth();
	    int h = space.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    space.print(g);
	    g.dispose();
	    return bi;
	}

}
