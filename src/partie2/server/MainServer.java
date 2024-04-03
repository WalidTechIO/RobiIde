package partie2.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import stree.parser.SNode;
import stree.parser.SParser;

public class MainServer {

	public static void main(String args[]) {
		
		Server server = null;
		Interpreter interpreter = new Interpreter();
		try {
			server = new Server(7777);
		} catch (IOException e) {
			System.err.println("Can't initiate server.");
			System.exit(1);
		}
		
		int tentativeEchoue = 0;
		
		while (true) {
			
			String input = null;
			try {
				input = server.getInstruction();
				if(input == null) throw new IOException();
				tentativeEchoue = 0;
			} catch(IOException|ClassNotFoundException e) {
				System.err.println("Connection lost or received incorrect data");
				tentativeEchoue++;
				if(tentativeEchoue == 3) {
					System.exit(1);
				}
				continue;
			}
			
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// execution des s-expressions compilees puis envoie du resultat
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				interpreter.compute(itor.next());
				server.sendRender(interpreter.snapshot());
			}
		}
	}
	
}
