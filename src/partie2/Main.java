package partie2;

import partie2.client.ui.IHMRobiMain;
import partie2.server.MainServer;

public class Main {
	
	public static void main(String args[]) {
		
		new Thread(() -> MainServer.main(args)).start();
		IHMRobiMain.main(args);
		
	}

}
