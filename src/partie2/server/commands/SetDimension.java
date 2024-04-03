package partie2.server.commands;

import java.awt.Dimension;

import graphicLayer.GBounded;
import graphicLayer.GSpace;
import partie2.server.Reference;
import stree.parser.SNode;

public class SetDimension implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 4) throw new IllegalArgumentException("SetDimension: Required 4 args, passed: " + method.size());
		Dimension dim = new Dimension(Integer.parseInt(method.get(2).contents()), Integer.parseInt(method.get(3).contents()));
		
		if(reference.getRef() instanceof GBounded bndref) {
			bndref.setWidth(dim.width);
			bndref.setHeight(dim.height);
		}
		
		if(reference.getRef() instanceof GSpace sparef) {
			sparef.changeWindowSize(dim);
		}
		
		return reference;
	}

}
