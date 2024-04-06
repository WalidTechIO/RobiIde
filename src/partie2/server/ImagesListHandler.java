package partie2.server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ImagesListHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		
		File currentDir = new File(".");
		String computedResponse = "";
		FilenameFilter imageFilter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				List<String> acceptedExts = new ArrayList<String>();
				acceptedExts.add(".png");
				acceptedExts.add(".gif");
				acceptedExts.add(".jpg");
				return acceptedExts.contains(name.substring(name.lastIndexOf('.')));
			}
		};
		
		File[] files = currentDir.listFiles(imageFilter);
		for(File file : files) computedResponse+=file.getName()+"\n";
		
		OutputStream os = t.getResponseBody();
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.sendResponseHeaders(200, computedResponse.length());
        os.write(computedResponse.getBytes());
        os.close();
	}

}
