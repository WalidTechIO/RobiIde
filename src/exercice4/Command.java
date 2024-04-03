package exercice4;

import stree.parser.SNode;

public interface Command {
	
	Reference run(Reference reference, SNode method);
	
	Reference run(Reference reference, Environment environment, SNode method);

}
