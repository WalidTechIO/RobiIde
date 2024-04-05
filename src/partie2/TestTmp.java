package partie2;

import partie2.io.graphics.GImage;
import partie2.io.graphics.GText;
import partie2.io.graphics.GWorld;
import partie2.utils.GraphicsUtils;

/**
 * Classe testant les classes semi-graphique et l'interprete graphique
 */
public class TestTmp {
	
	public static void main(String args[]) {
		GWorld world = new GWorld();
		GImage image = new GImage("resources/alien.gif");
		
		world.setDim(500, 200);
		world.setColor("black");
		
		image.translate(100, 200);
		
		world.add(image);
		world.add(new GText("Hello World"));
		
		System.out.println(GraphicsUtils.compute(world));
	}

}
