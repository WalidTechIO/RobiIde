package exercice4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		File path = new File(method.get(2).contents());
		BufferedImage rawImage = null;
		try {
			rawImage = ImageIO.read(path);
		} catch (IOException e) {
			System.err.println("Image not found");
			return null;
		}
		return new Reference(new GImage(rawImage));
	}

	@Override
	public Reference run(Reference reference, Environment environment, SNode method) {
		return run(reference, method);
	}

}
