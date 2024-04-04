package partie2.server;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import partie2.io.Mode;
import partie2.io.Program;
import partie2.io.Response;
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
import stree.parser.SParser;

public class Interpreter {
	
	private final Environment env = new Environment();
	private final GSpace space;
	private final Server server;
	private boolean sbs = false;
	private boolean status = false;
	private boolean sbsend = false;
	private List<SNode> program = null;
	
	public Interpreter(Server server) {
		this.server = server;
		
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
		String msg = receiverName + "->" + expr.get(1).contents();
		Reference receiver = env.getReferenceByName(receiverName);
		
		Reference ref = null;
		
		try {
			if(receiver == null) throw new NullPointerException("Environment doesn't know \"" + receiverName + "\" reference.");
			ref = receiver.run(this, expr);
			msg += " Success";
		} catch(Exception e) {
			System.err.println(e.getMessage() + "\n");
			msg += " Error: " + e.getMessage();
		}
		
		server.sendResponse(new Response(msg, imgToB64(snapshot()), nodeToString(expr), env.toString(), env.scriptsMap()));
		if(sbs && !server.receiveData()) sbsend = true;
		return ref;
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
	
	public void setProgram(Program program) {
		
		if(program.mode() == Mode.S_B_S) sbs = true;
		else sbs = false;
		
		// creation du parser
		SParser<SNode> parser = new SParser<>();
		
		try {
			this.program = parser.parse(program.contenu());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void runProgram() {
		
		if(program == null) return;
		
		status = true;
		
		Iterator<SNode> programIterator = program.iterator();
		
		while(programIterator.hasNext()) {
			compute(programIterator.next());
			if(sbsend) {
				sbsend = false;
				break;
			}
		}
		
		status = false;
	}
	
	public boolean isRunning() {
		return status;
	}
	
	private String nodeToString(SNode node) {
		if(node.isLeaf()) {
			return node.contents() + " ";
		} else {
			String res = "( ";
			for(SNode children : node.children()) res += nodeToString(children);
			return res + ") ";
		}
	}
	
	private String imgToB64(BufferedImage image) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(image, "png", os);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch(Exception e) {
			return null;
		}
	}

}
