package partie2.server.commands;

import java.util.Objects;

import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class Sleep implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("Sleep: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		int delay = 0;
		try {
			delay = Integer.parseInt(method.get(2).contents());
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage().split(":")[1].trim().replace("\"", "") + " is not a valid delay.");
		}
		
		if(interpreter.isRunningFromHttpRequest()) {
			interpreter.registerPause(delay);
		} else {
			try {
				Thread.sleep(delay);
			} catch(InterruptedException ignored) {
				
			}
		}
		
		return reference;
	}

}
