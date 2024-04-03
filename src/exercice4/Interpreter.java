package exercice4;

import stree.parser.SNode;

public class Interpreter {
	
	public Reference compute(Environment environment, SNode expr) {
		String receiverName = expr.get(0).contents();
		Reference receiver = environment.getReferenceByName(receiverName);
		try {
			return receiver.run(environment, expr);
		} catch(NullPointerException|IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
