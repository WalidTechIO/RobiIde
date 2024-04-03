package partie2.server;

import java.io.IOException;

public class MainServer {

	public static void main(String args[]) {
	
		Server server = null;
		try {
			server = new Server(7777);
		} catch (IOException e) {
			System.err.println("Can't initiate server.");
			System.exit(1);
		}
		
		server.run();
	}
	
}
