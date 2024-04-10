package partie2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import partie2.io.Request;
import partie2.io.Request.RequestType;
import partie2.io.Response;
public class ClientManager implements Runnable, canSendResponse {
	
	/**
	 * Interpreter s'occupant du client
	 */
	private final Interpreter interpreter;
	/**
	 * Socket du client
	 */
	private final Socket s;
	/**
	 * Stream d'object entrant du client
	 */
	private final ObjectInputStream in;
	/**
	 * Stream d'object sortant vers le client
	 */
	private final ObjectOutputStream out;
	/**
	 * Statut de travail du client manager
	 */
	private boolean working = true;
	
	/**
	 * Constructeur
	 */
	public ClientManager(Socket client) throws IOException {
		this.interpreter = new Interpreter(this);
		s = client;
		
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
	}
	
	/**
	 * Methode de traitement des données reçues
	 */
	boolean receiveData() {
		try {
			Object obj = in.readObject();			
			if(obj instanceof String msg) {
				ObjectMapper mapper = new ObjectMapper();
				Request req = mapper.readValue(msg, Request.class);
				if(req.type() == RequestType.EXE) {
					if(interpreter.isRunning()) return true;
					else {
						interpreter.runProgram();
						return false;
					}
				} else if(req.type() == RequestType.PROG) {
					interpreter.setProgram(req.program());
					return false;
				}
			}
		
			
		} catch(IOException|ClassNotFoundException e) {
			stop();
		}
		return false;
	}
	
	/**
	 * Méthode d'envoie de reponse
	 */
	public void sendResponse(Response response) {		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String res = null;
		
		try {
			res = ow.writeValueAsString(response);
		} catch(JsonProcessingException ignored) {}
		
		try {
			out.writeObject(res);
		} catch(IOException e) {
			stop();
		}
	}

	/**
	 * mainloop du client manager
	 */
	@Override
	public void run() {
		while(working) {
			receiveData();
		}
	}
	
	/**
	 * Arret du client manager
	 */
	public void stop() {
		working = false;
		System.out.println("Disconnected: " + s.getInetAddress().getHostAddress() + ":" + s.getPort());
		try {
			s.close();
		} catch(IOException ignored) {}
	}
}