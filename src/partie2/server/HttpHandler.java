package partie2.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;

import partie2.io.Request;
import partie2.io.Response;
import partie2.io.graphics.GWorld;
import partie2.utils.GraphicsUtils;
import partie2.utils.GraphicsUtils.RendererException;
import partie2.utils.ImageWrapper;
import partie2.utils.ResponseWrapper;

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
	private List<ResponseWrapper> responses = new ArrayList<>();
	
	private int currentDelay = 0;
	private Mode mode;
	
	static enum Mode {
		RENDER,
		WORLD
	}
	
	public HttpHandler(Mode mode) {
		this.mode = mode;
	}

	//Gere les echanges HTTP arrivant sur l'endpoint
	@Override
	public void handle(HttpExchange t) throws IOException {
		OutputStream os;
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		try {
			if(!t.getRequestMethod().equals("POST")) throw new Exception("Invalid HTTP method");
			Interpreter interpreter = new Interpreter(this);
			ObjectReader reader = new ObjectMapper().reader();
			String json = new String(t.getRequestBody().readAllBytes());
			
			Request req = reader.readValue(json, Request.class);
			System.out.println("HTTP Endpoint: Receipt HTTP Request");
			
			interpreter.setProgram(req.program());
			interpreter.runProgram();
			
			computedResponse = buildResponse(mode);
			
			imgs.clear();
			responses.clear();
			currentDelay = 0;
			
	        t.sendResponseHeaders(200, computedResponse.length());
	        os = t.getResponseBody();
	        os.write(computedResponse.getBytes());
	        os.close();
	        
	        System.out.println("HTTP Endpoint: HTTP Response sent");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			computedResponse = e.getMessage();
			t.sendResponseHeaders(400, computedResponse.length());
			os = t.getResponseBody();
			os.write(computedResponse.getBytes());
			os.close();
		}
	}
	
	private String buildResponse(Mode mode) {
		if(isRendering()) return delegateRender();
		else return delegateWorld();
	}
	
	private String delegateRender() {
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
						<script id="animationScript" defer>
							%s
						</script>
					</body>
				</html>
				""".formatted(buildScript());
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
				.orElse("setTimeout(() => { document.getElementById('renderer').alt = 'No images to display'; document.getElementById('renderer').src = '' }, 0)");
		
	}

	private String delegateWorld() {
		ObjectWriter ow = new ObjectMapper().writer();
		try {
			return ow.writeValueAsString(responses);
		} catch (JsonProcessingException e) {
			//List<ResponseWrapper> is Serializable so this wouldn't happen 
			System.err.println(e.getMessage());
		}
		return "Error";
	}

	@Override
	public void sendResponse(Response reponse) {
		reponse = new Response(reponse.feedback(), reponse.world().copy(), reponse.info()); //Make a World copy before stock response
		if(!isRendering()) responses.add(new ResponseWrapper(reponse, currentDelay));
	}

	public void registerPause(GWorld space, int delay) {
		
		if(isRendering()) {
			try {
				String image = GraphicsUtils.render(space, false);
				imgs.add(new ImageWrapper(image, currentDelay));
				currentDelay += delay;
			} catch (RendererException e) {
				System.err.println(e.getMessage());
			}
		} else {
			 currentDelay += delay;
		}
		
	}
	
	public boolean isRendering() {
		return mode == Mode.RENDER;
	}
	

}
