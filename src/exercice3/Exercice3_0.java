package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "" +
	"   (space setColor black) " +
	"   (robi setColor yellow)" +
	"   (space sleep 1000)" +
	"   (space setColor white)\n" + 
	"   (space sleep 1000)" +
	"	(robi setColor red) \n" + 
	"   (space sleep 1000)" +
	"	(robi translate 100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 50)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate -100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 -40)";

	public Exercice3_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<SNode> itor = rootNodes.iterator();
		while (itor.hasNext()) {
			this.run(itor.next());
		}
	}

	private void run(SNode expr) {
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		cmd.run();
	}
	
	private Color getColorFromName(String colorName) {
		try {
			return (Color) Class.forName("java.awt.Color").getField(colorName).get(null);
		} catch(Exception e) {
			return null;
		}
	}

	Command getCommandFromExpr(SNode expr) {
		if(expr.size() < 2) {
			throw new IllegalArgumentException("Expression need to specify at least one object and< one method name");
		}
		
		String refName = expr.get(0).contents();
		String methodName = expr.get(1).contents();
		
		switch(refName) {
			case "space":
				
				switch(methodName) {
					case "setColor":
						if(expr.size() < 3) throw new IllegalArgumentException("Incorrect number of args for space setColor");
						return new SpaceChangeColor(getColorFromName(expr.get(2).contents()));
					case "sleep":
						if(expr.size() < 3) throw new IllegalArgumentException("Incorrect number of args for space sleep");
						return new SpaceSleep(Integer.parseInt(expr.get(2).contents()));
				}
				
				break;
			case "robi":
				
				switch(methodName) {
					case "setColor":
						if(expr.children().size() < 3) throw new IllegalArgumentException("Incorrect number of args for robi setColor");
						return new RobiChangeColor(getColorFromName(expr.get(2).contents()));
					case "translate":
						if(expr.children().size() < 4) throw new IllegalArgumentException("Incorrect number of args for robi translate");
						Point p = new Point(Integer.parseInt(expr.get(2).contents()), Integer.parseInt(expr.get(3).contents()));
						return new RobiTranslate(p);
				}
				
				break;
		}
		
		return null;		
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}
	
	public class SpaceChangeColor extends ChangeColor {

		public SpaceChangeColor(Color newColor) {
			super(newColor);
		}

		@Override
		public void run() {
			space.setColor(newColor);
		}
		
	}
	
	public class RobiChangeColor extends ChangeColor {

		public RobiChangeColor(Color newColor) {
			super(newColor);
		}

		@Override
		public void run() {
			robi.setColor(newColor);
		}
		
	}
	
	public class SpaceSleep implements Command {
		int delay;
		
		public SpaceSleep(int delay) {
			this.delay = delay;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ignored) {}
		}
	}
	
	public class RobiTranslate implements Command {
		Point translationVec;
		
		public RobiTranslate(Point translationVec) {
			this.translationVec = translationVec;
		}
		
		@Override
		public void run() {
			robi.translate(translationVec);
		}
	}
	
	
}