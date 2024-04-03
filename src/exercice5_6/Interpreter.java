package exercice5_6;

import stree.parser.SNode;

public class Interpreter {
	
	public Reference compute(Environment environment, SNode expr) {
		String receiverName = expr.get(0).contents();
		Reference receiver = environment.getReferenceByName(receiverName);
		if(receiver == null) throw new NullPointerException("Environment doesn't know \"" + receiverName + "\" reference.");
		try {
			return receiver.run(environment, expr);
		} catch(NullPointerException|IllegalArgumentException e) {
			System.err.println(e.getMessage() + "\n");
		}
		return null;
	}

}
