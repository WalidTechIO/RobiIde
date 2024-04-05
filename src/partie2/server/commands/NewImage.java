package partie2.server.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import partie2.server.Interpreter;
import partie2.server.Reference;
import stree.parser.SNode;

public class NewImage implements Command {

	@Override
	public Reference run(Interpreter interpreter, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("NewImage: Required 3 args, passed: " + method.size());
		
		Reference reference = interpreter.getReferenceByNode(method.get(0));
		Objects.requireNonNull(reference);
		
		File path = new File(method.get(2).contents());
		BufferedImage rawImage = null;
		try {
			rawImage = ImageIO.read(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid path for image");
		}
		
		return new Reference(new GImage(rawImage));
	}

}
