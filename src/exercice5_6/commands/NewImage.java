package exercice5_6.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import exercice5_6.Reference;
import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		if(method.size() != 3) throw new IllegalArgumentException("NewImage: Required 3 args, passed: " + method.size());
		File path = new File(method.get(2).contents());
		BufferedImage rawImage = null;
		try {
			rawImage = ImageIO.read(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("Image path is invalid");
		}
		return new Reference(new GImage(rawImage));
	}

}
