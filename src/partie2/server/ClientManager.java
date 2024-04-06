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
	
	private final Interpreter interpreter;
	private final Socket s;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	
	private boolean working = true;
	
	public ClientManager(Socket client) throws IOException {
		this.interpreter = new Interpreter(this);
		s = client;
		
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
	}
	
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
	
	public void sendResponse(Response response) {		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String res = null;
		
		try {
			res = ow.writeValueAsString(response);
		} catch(JsonProcessingException e) {
			res = "{\n\"status\": \"error\"\n}";
			e.printStackTrace();
		}
		
		try {
			out.writeObject(res);
		} catch(IOException e) {
			stop();
		}
	}

	@Override
	public void run() {
		while(working) {
			receiveData();
		}
	}
	
	public void stop() {
		working = false;
		System.out.println("Disconnected: " + s.getInetAddress().getHostAddress() + ":" + s.getPort());
		try {
			s.close();
		} catch(IOException ignored) {}
	}
}