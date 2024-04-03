package partie2.server.commands;

import java.awt.Color;

import graphicLayer.GElement;
import graphicLayer.GSpace;
import partie2.server.Reference;
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
		if(method.size() != 3) throw new IllegalArgumentException("SetColor: Required 3 args, passed: " + method.size());
		
		if(reference.getRef() instanceof GElement) ((GElement) reference.getRef()).setColor(getColorFromName(method.get(2).contents()));
		if(reference.getRef() instanceof GSpace) ((GSpace) reference.getRef()).setColor(getColorFromName(method.get(2).contents()));
		
		return reference;
	}

}
