package partie2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import partie2.utils.ArgsParser;

public class MainServer {

	public static void main(String args[]) {
		
		String usage = """
				Usage: program [-p port] [-e port] [-h] [-m] [-r]
				m: multi-client (Default false)
				p: port ServerSocket (Default: 7777)
				e: port Endpoint HTTP (Default: 8080)
				r: activer l'endpoint /render
				h: Affiche cette aide
				""";
		
		ArgsParser argsParser = new ArgsParser("pieihnmnrn").parse(args);
		
		if(argsParser.hasParsed('h')) {
			System.out.println(usage);
			System.exit(0);
		}
		
		String mode = argsParser.hasParsed('m') ? "multi" : "mono";
		
		final Integer portTcp = argsParser.hasParsed('p') ? (Integer)argsParser.get('p') : 7777;
		final Integer portEndpoint = argsParser.hasParsed('e') ? (Integer)argsParser.get('e') : 8080;
		final boolean renderApiOn = argsParser.hasParsed('r');
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(portTcp);
			if(portEndpoint > 65535 || portEndpoint < 0) {
				serverSocket.close();
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Port invalid");
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Can't open server socket: " + e.getMessage());
			System.exit(1);
		}
	
		System.out.println("ROBI Server ready\nListening on 0.0.0.0:" + serverSocket.getLocalPort() + "\nMode: " + mode);
		
		final Thread httpServer = new Thread(() -> HttpServer.launch(portEndpoint, renderApiOn));
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
		} catch (IOException ignored) {}
	}
	
}
