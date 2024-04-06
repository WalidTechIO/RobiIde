package partie2.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sun.net.httpserver.HttpExchange;

import partie2.io.Request;
import partie2.io.Response;
import partie2.io.graphics.GWorld;
import partie2.utils.GraphicsUtils;
import partie2.utils.GraphicsUtils.RendererException;
import partie2.utils.ImageWrapper;

/**
 * 
 * Pour le moment ce serveur supporte uniquement les requete type:
 * 
 * POST /endpoint avec un format de body : 
 * {
 * 	"type": "PROGRAM",
 * 	"program": {
 *     "mode": "Don't care", -> Will be DIRECT everytime due to this worker
 *     "contenu": "program"
 *  }
 * }
 * 
 * Renvoie une page HTML executant l'animation (hors image pour le moment et necessite un environnement graphique cote serveur)
 */

public class HttpHandler implements com.sun.net.httpserver.HttpHandler, canSendResponse {
	
	private String computedResponse = null;
	private List<ImageWrapper> imgs = new ArrayList<>();
	
	private int currentDelay = 0;

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
			
			computedResponse = buildResponse(buildScript());
			
			imgs.clear();
			currentDelay = 0;
			
	        t.sendResponseHeaders(200, computedResponse.length());
	        os = t.getResponseBody();
	        os.write(computedResponse.getBytes());
	        os.close();
	        
	        System.out.println("HTTP Endpoint: HTTP Response sent");
		} catch (Exception e) {
			e.printStackTrace();
			t.sendResponseHeaders(400, computedResponse.length());
			os = t.getResponseBody();
			os.write(computedResponse.getBytes());
			os.close();
		}
	}

	private String buildScript() {
		
		Function<ImageWrapper, String> mapper = (img) -> {
			return """
					setTimeout(() => { document.getElementById('renderer').src = 'data:image/png;base64, %s' }, %d)
					""".formatted(img.image(), img.delay());
		};
		
		return imgs.stream()
				.map(mapper)
				.reduce(String::concat)
				.orElse("setTimeout(() => { document.getElementById('renderer').alt = 'No images to display' }, 0)");
		
	}

	private String buildResponse(String script) {
		return  """
				<!DOCTYPE html>
				<html>
					<head>
						<title>ROBI Renderer</title>
					</head>
					<body>
						<div style="margin:auto;">
							<img id="renderer" alt="renderer" />
						</div>
						<script defer>
							%s
						</script>
					</body>
				</html>
				""".formatted(script);
	}

	@Override
	public void sendResponse(Response reponse) {
		//Do nothing for now
	}

	public void addImage(GWorld space, int delay) {
		try {
			String image = GraphicsUtils.render(space, false);
			imgs.add(new ImageWrapper(image, currentDelay));
			currentDelay += delay;
		} catch (RendererException e) {
			e.printStackTrace();
		}
	}
	
	

}
