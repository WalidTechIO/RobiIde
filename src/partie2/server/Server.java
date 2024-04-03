package partie2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import partie2.io.Program;
import partie2.io.Response;

public class Server implements Runnable {
	
	private final Interpreter interpreter;
	private final Socket s;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	
	private int fail = 0;
	
	public Server(int port) throws IOException {
		this.interpreter = new Interpreter(this);
		
		ServerSocket ss = new ServerSocket(port);
		s = ss.accept();
		ss.close();
		
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
		
		System.err.println("Connection lost or received incorrect data");
		fail++;
		if(fail == 3) {
			System.exit(1);
		}
		return false;
	}
	
	public void sendResponse(Response response) {
		try {
			out.writeObject(response);
		} catch (IOException ignored) {}
	}

	@Override
	public void run() {
		while(true) {
			receiveData();
		}
	}
}