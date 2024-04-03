package partie2.server;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;

public class MainServer {

	public static void main(String args[]) {
		
		Environment environment = new Environment();
		Server server = null;
		try {
			server = new Server(7777);
		} catch (IOException e) {
			System.err.println("Can't initiate server.");
			System.exit(1);
		}
		
		GSpace space = new GSpace("Server", new Dimension(200, 100));
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
		
		int tentativeEchoue = 0;
		
		while (true) {
			
			String input = null;
			try {
				input = server.getInstruction();
				if(input == null) throw new IOException();
				tentativeEchoue = 0;
			} catch(IOException|ClassNotFoundException e) {
				System.err.println("Connection lost or received incorrect data");
				tentativeEchoue++;
				if(tentativeEchoue == 3) {
					System.exit(1);
				}
				continue;
			}
			
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// execution des s-expressions compilees puis envoie du resultat
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				new Interpreter().compute(environment, itor.next());
				server.sendRender(createImage(spaceRef));
			}
		}
	}
	
	static public BufferedImage createImage(Reference ref) {
		if(!(ref.getRef() instanceof GSpace space)) throw new IllegalArgumentException();
	    int w = space.getWidth();
	    int h = space.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    space.print(g);
	    g.dispose();
	    return bi;
	}
	
}
