package partie2.server;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import partie2.io.DebugInfo;
import partie2.io.Mode;
import partie2.io.Program;
import partie2.io.Response;
import partie2.io.graphics.GImage;
import partie2.io.graphics.GOval;
import partie2.io.graphics.GRect;
import partie2.io.graphics.GText;
import partie2.io.graphics.GWorld;
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
import partie2.utils.NodeUtils;
import stree.parser.SNode;
import stree.parser.SParser;

public class Interpreter {
	
	private final Environment env = new Environment();
	private final GWorld space;
	private final canSendResponse clientManager;
	private boolean sbs = false;
	private boolean status = false;
	private boolean sbsend = false;
	private List<SNode> program = null;
	
	public Interpreter(canSendResponse clientManager) {
		this.clientManager = clientManager;
		
		space = new GWorld("Renderer", new Dimension(200, 100));

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GText.class);

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
		Reference ref = null;
		String msg = null;
		
		try {
			String receiverName = expr.get(0).contents();
			msg = receiverName + "->" + expr.get(1).contents();
			Reference receiver = env.getReferenceByName(receiverName);
			if(receiver == null) throw new NullPointerException("Environment doesn't know \"" + receiverName + "\" reference.");
			ref = receiver.run(this, expr);
			msg += " Success";
		} catch(Exception e) {
			System.err.println(e.getMessage() + "\n");
			if(msg != null) msg += " Error: " + e.getMessage();
			else msg = "Error: Expression need at least one reference and one primitive/scriptName";
		}
		Response resp = new Response(msg, space, new DebugInfo(NodeUtils.nodeToString(expr), env.info()));
		clientManager.sendResponse(resp);
		if(clientManager instanceof ClientManager && sbs && !((ClientManager)clientManager).receiveData()) sbsend = true;
		return ref;
	}

	public Reference getReferenceByNode(SNode node) {
		if(node == null || !node.isLeaf()) return null;
		return env.getReferenceByName(node.contents());
	}
	
	public Environment getEnvironment() {
		return env;
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
		
		//En mode rendu a la fin du programme on veut envoyer l'etat courant dans tout les cas
		if(clientManager instanceof HttpHandler httphandler && httphandler.isRendering()) httphandler.registerPause(space, 0);
		
		status = false;
	}
	
	public void registerPause(int delay) {
		if(clientManager instanceof HttpHandler httphandler) httphandler.registerPause(space, delay);
	}
	
	public boolean isRunningFromHttpRequest() {
		return clientManager instanceof HttpHandler;
	}
	
	public boolean isRunning() {
		return status;
	}

	
}
