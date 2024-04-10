package partie2.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import partie2.server.HttpHandler.Mode;

public class HttpServer {
	
	//Demarre un service HTTP avec 1 a 2 endpoints selon le mode
	public static void launch(int port, boolean renderApiOn) {
		try {
			com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port),0);
			System.out.println("Starting HTTP Endpoint on port " + server.getAddress().getPort());
			if(renderApiOn) {
				System.out.println("HTTP Endpoint: /render endpoint will be accessible");
				server.createContext("/render", new HttpHandler(Mode.RENDER));
			}
			server.createContext("/world", new HttpHandler(Mode.WORLD));
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
