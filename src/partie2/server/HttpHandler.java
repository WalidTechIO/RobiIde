package partie2.server;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sun.net.httpserver.HttpExchange;

import partie2.io.Request;

/**
 * Pour le moment ce serveur supporte uniquement les requetest type:
 * 
 * POST /endpoint avec un format de body : 
 * {
 * 	"type": "PROGRAM",
 * 	"program": {
 *     "mode": "Don't care", -> Will be DIRECT everytime due to the interpreter
 *     "contenu": "....."
 *  }
 * }
 */

public class HttpHandler implements com.sun.net.httpserver.HttpHandler, canSendResponse {
	
	private String computedResponse = "Error";

	@Override
	public void handle(HttpExchange t) throws IOException {
		OutputStream os;
		try {
			Interpreter interpreter = new Interpreter(this);
			ObjectReader reader = new ObjectMapper().reader();
			String json = new String(t.getRequestBody().readAllBytes());
			
			Request req = reader.readValue(json, Request.class);
			System.out.println("HTTP Endpoint: Receipt HTTP Request");
			
			interpreter.setProgram(req.program());
			interpreter.runProgram();
			
	        t.sendResponseHeaders(200, computedResponse.length());
	        os = t.getResponseBody();
	        os.write(computedResponse.getBytes());
	        os.close();
	        
	        System.out.println("HTTP Endpoint: HTTP Response sent");
		} catch (IOException e) {
			e.printStackTrace();
			t.sendResponseHeaders(400, computedResponse.length());
			os = t.getResponseBody();
			os.write(computedResponse.getBytes());
			os.close();
		}
	}

	@Override
	public void sendResponse(String reponse) {
		computedResponse = reponse;
	}

}
