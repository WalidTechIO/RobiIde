package partie2.server.commands;

import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public interface Command {
	
	Reference run(Interpreter interpreter, SNode method);

}
