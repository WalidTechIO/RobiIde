package partie2.server;

import stree.parser.SNode;

public class Interpreter {
	
	public Interpreter() {
		
	}
	
	public Reference compute(Environment environment, SNode expr) {
		String receiverName = expr.get(0).contents();
		Reference receiver = environment.getReferenceByName(receiverName);
		try {
			if(receiver == null) throw new NullPointerException("Environment doesn't know \"" + receiverName + "\" reference.");
			return receiver.run(environment, expr);
		} catch(Exception e) {
			System.err.println(e.getMessage() + "\n");
		}
		return null;
	}
	

}
