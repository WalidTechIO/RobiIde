package partie2.server.commands;

import partie2.server.Reference;
import stree.parser.SNode;

public interface Command {
	
	Reference run(Reference reference, SNode method);

}
