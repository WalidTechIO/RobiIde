package partie2.server;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {
	
	public static void launch(int port) {
		try {
			com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port),0);
			server.createContext("/endpoint", new HttpHandler());
			server.createContext("/images/list", new ImagesListHandler());
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
