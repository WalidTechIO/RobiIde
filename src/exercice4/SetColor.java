package exercice4;

import java.awt.Color;

import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetColor implements Command {
	
	private Color getColorFromName(String colorName) {
		try {
			return (Color) Class.forName("java.awt.Color").getField(colorName).get(null);
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() < 3) throw new IllegalArgumentException("setColor called without color");
		
		if(reference.getRef() instanceof GElement) ((GElement) reference.getRef()).setColor(getColorFromName(method.get(2).contents()));
		if(reference.getRef() instanceof GSpace) ((GSpace) reference.getRef()).setColor(getColorFromName(method.get(2).contents()));
		
		return reference;
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		// TODO Auto-generated method stub
		return run(reference, method);
	}

}
