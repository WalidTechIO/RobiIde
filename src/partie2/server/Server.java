package partie2.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Server {
	
	private final Socket s;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	
	public Server(int port) throws IOException {
		ServerSocket ss = new ServerSocket(port);
		s = ss.accept();
		ss.close();
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
	}
	
	public String getInstruction() throws IOException, ClassNotFoundException {
		if(in.readObject() instanceof String inst) return inst;
		else return null;
	}

	public void sendRender(BufferedImage image) {
		try {
			ImageIO.write(image, "png", out);
		} catch (IOException ignored) {
		}
	}

}