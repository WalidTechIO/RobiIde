package exercice4;

import stree.parser.SNode;

public class Sleep implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		
		if(method.size() < 3) throw new IllegalArgumentException("sleep called without delay");
		
		int delay = 0;
		try {
			delay = Integer.parseInt(method.get(2).contents());
		} catch(NumberFormatException e) {
			System.err.println("Delay is not valid.");
		}
		
		
		try {
			Thread.sleep(delay);
		} catch(InterruptedException e) {
			
		}
		
		return reference;
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		// TODO Auto-generated method stub
		return run(reference, method);
	}

}
