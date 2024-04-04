package partie2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import partie2.io.Program;

public class ClientManager implements Runnable {
	
	private final Interpreter interpreter;
	private final Socket s;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	
	private int fail = 0;
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
			if(obj instanceof String && obj.equals("Exe")) {
				if(interpreter.isRunning()) return true;
				else {
					interpreter.runProgram();
					return false;
				}
			} 
			if(obj instanceof Program prg) {
				interpreter.setProgram(prg);
				return false;
			}
		} catch(IOException|ClassNotFoundException ignored) {}
		fail++;
		if(fail == 3) stop();
		return false;
	}
	
	public void sendResponse(String response) {
		try {
			out.writeObject(response);
		} catch (IOException ignored) {}
	}

	@Override
	public void run() {
		while(working) {
			receiveData();
		}
	}
	
	public void stop() {
		working = false;
		interpreter.stop();
		try {
			s.close();
		} catch(IOException ignored) {}
	}
}