package partie2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
	
		System.out.println("ROBI Server ready\nListening on 0.0.0.0:" + serverSocket.getLocalPort() + "\nMode: " + mode);
		
		System.out.println("Starting HTTP Endpoint on port 8080");
		final Thread httpServer = new Thread(() -> HttpServer.launch(8080));
		httpServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> httpServer.interrupt()));
		
		while(true) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("Accepted: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
				if(mode.equals("mono")) {
					System.out.println("Mode Mono: Start ClientManager, other clients will be put on waiting queue");
					new ClientManager(client).run();
				} else {
					new Thread(new ClientManager(client)).start();
					System.out.println("Mode Multi: New Thread started");
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
