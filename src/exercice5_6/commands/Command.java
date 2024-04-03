package exercice5_6.commands;

import exercice5_6.Reference;
import stree.parser.SNode;

public interface Command {
	
	Reference run(Reference reference, SNode method);

}
