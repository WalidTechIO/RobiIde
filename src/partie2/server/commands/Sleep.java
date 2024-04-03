package partie2.server.commands;

import partie2.server.Reference;
import stree.parser.SNode;

public class Sleep implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("Sleep: Required 3 args, passed: " + method.size());
		
		int delay = Integer.parseInt(method.get(2).contents());
		
		try {
			Thread.sleep(delay);
		} catch(InterruptedException e) {
			
		}
		
		return reference;
	}

}
