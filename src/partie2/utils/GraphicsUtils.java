package partie2.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import graphicLayer.GBounded;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import partie2.io.graphics.GObject;
import partie2.io.graphics.GText;
import partie2.io.graphics.GWorld;

public class GraphicsUtils {
	
	public static String compute(GWorld world) throws RendererException {
		
		try {
			CustomGSpace space = new CustomGSpace(world.name(), world.dimension());
			space.open();
			SwingUtilities.getWindowAncestor(space).dispose();
			
			space.changeWindowSize(world.dimension());
			space.setColor(getColorFromName(world.color()));
			
			world.childrens().forEach((c) -> computeObject(space, c));
			
			space.repaint();
			
			return snapshot(space);
		} catch(Exception e) {
			throw new RendererException(e.getMessage());
		}
		
	}
	
	private static void computeObject(GContainer container, GObject child) {
		
		GElement element = null;
		
		switch(child.type()) {
		case IMAGE:
			File path = new File(((partie2.io.graphics.GImage)child).path());
			BufferedImage rawImage = null;
			try {
				rawImage = ImageIO.read(path);
			} catch (IOException e) {
				throw new IllegalArgumentException("Invalid path for image");
			}
			element = new GImage(rawImage);
			break;
		case OVAL:
			element = new GOval();
			break;
		case RECT:
			element = new GRect();
			break;
		case TEXT:
			element = new GString(((GText)child).text());
			break;
		case WORLD:
			break;
		default:
			break;				
		}
		
		element.setColor(getColorFromName(child.color()));
		element.translate(new Point(child.position().width, child.position().height));
		if(element instanceof GBounded gbounded) {
			gbounded.setDimension(child.dimension());
			for(GObject c : child.childrens()) computeObject(gbounded, c);
		}
		
		container.addElement(element);
		
	}

	private static Color getColorFromName(String colorName) {
		try {
			return (Color) Class.forName("java.awt.Color").getField(colorName).get(null);
		} catch(Exception e) {
			return null;
		}
	}
	
	private static String snapshot(GSpace space) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
	    int w = space.getWidth();
	    int h = space.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    space.print(g);
	    g.dispose();
	    try {
			ImageIO.write(bi, "png", os);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch(Exception e) {
			return null;
		}
	}
	
	public static class RendererException extends Exception {

		private static final long serialVersionUID = -651413780878138481L;
		
		public RendererException(String msg) {
			super(msg);
		}
		
	}

}
