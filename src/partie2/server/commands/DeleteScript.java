package partie2.server.commands;

import partie2.server.Reference;
import stree.parser.SNode;

public class DeleteScript implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("DelScript: Required 3 args, passed: " + method.size());
		reference.deleteScript(method.get(2).contents());
		return null;
	}

}
