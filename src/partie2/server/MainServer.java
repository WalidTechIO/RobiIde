package partie2.server;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {

	public static void main(String args[]) {
		
		String usage = """
				Usage: program port [mode]
				mode: mono | multi -> Nombre de client géré en même temps
				""";
		
		if(args.length < 1 || args.length > 2 || (args.length == 2 && !args[1].matches("mono|multi"))) {
			System.out.println(usage);
			System.exit(1);
		}
		
		String mode = args.length == 2 ? args[1] : "mono";
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		} catch (IllegalArgumentException e) {
			System.err.println("Port invalid");
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Can't open server socket: " + e.getMessage());
			System.exit(1);
		}
	
		while(true) {
			try {
				if(mode.equals("mono")) {
					new ClientManager(serverSocket.accept()).run();
				} else {
					new Thread(new ClientManager(serverSocket.accept())).start();
				}
			} catch(IOException e) {
				break;
			}
			
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
