package partie2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import partie2.utils.ArgsParser;

public class MainServer {

	public static void main(String args[]) {
		
		String usage = """
				Usage: program [-p port] [-e port] [-h] [-m true|false]
				m: true = multi-client, false = mono-client (Default false)
				p: port ServerSocket (Default: 7777)
				e: port Endpoint HTTP (Default: 8080)
				h: Affiche cette aide
				""";
		
		ArgsParser argsParser = new ArgsParser("pieihnmb").parse(args);
		
		if(argsParser.hasParsed('h')) System.out.println(usage);
		
		String mode = argsParser.hasParsed('m') && ((Boolean)argsParser.get('m')) ? "multi" : "mono";
		
		final Integer portTcp = argsParser.hasParsed('p') ? (Integer)argsParser.get('p') : 7777;
		final Integer portEndpoint = argsParser.hasParsed('e') ? (Integer)argsParser.get('e') : 8080;
		
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
		
		System.out.println("Starting HTTP Endpoint on port " + portEndpoint);
		final Thread httpServer = new Thread(() -> HttpServer.launch(portEndpoint));
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
