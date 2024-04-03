package exercice2;

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


public class Exercice2_1_0 {
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "(space setColor black) (robi setColor yellow) (robi translate 100 0) (space sleep 1000) (robi translate -100 0)";

	public Exercice2_1_0() {
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
	
	private Color getColorFromName(String colorName) {
		try {
			return (Color) Class.forName("java.awt.Color").getField(colorName).get(null);
		} catch(Exception e) {
			return null;
		}
		
	}
	
	private void run(SNode expr) {
		if(expr.children().size() < 2) {
			throw new IllegalArgumentException("Expression must reference an object and specify at least one method name");
		}
		
		String refName = expr.get(0).contents();
		String methodName = expr.get(1).contents();
		
		switch(refName) {
			case "space":
				switch(methodName) {
					case "sleep":
						if(expr.children().size() < 3) throw new IllegalArgumentException("Incorrect number of args for trasnlate");
						try {
							Thread.sleep(Integer.parseInt(expr.get(2).contents()));
						} catch (InterruptedException e) {}
						break;
					case "setColor":
						if(expr.children().size() < 3) throw new IllegalArgumentException("Incorrect number of args for trasnlate");
						space.setColor(getColorFromName(expr.get(2).contents()));
						break;
				}
				break;
			
			case "robi":
				switch(methodName) {
					case "translate":
						if(expr.children().size() < 4) throw new IllegalArgumentException("Incorrect number of args for trasnlate");
						Point p = new Point(Integer.parseInt(expr.get(2).contents()), Integer.parseInt(expr.get(3).contents()));
						robi.translate(p);
						break;
					case "setColor":
						if(expr.children().size() < 3) throw new IllegalArgumentException("Incorrect number of args for trasnlate");
						robi.setColor(getColorFromName(expr.get(2).contents()));
						break;
				}
				break;
		}
		
		
	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}

}