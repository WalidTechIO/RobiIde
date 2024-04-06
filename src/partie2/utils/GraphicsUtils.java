package partie2.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
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
	
	public static String render(GWorld world) throws RendererException {
		return render(world, true);
	}
	
	/**
	 * Render a world.
	 * @param world to render.
	 * @param ignore image rendering.
	 * @return String in Base64 format representing the world rendered as a png.
	 * @throws RendererException If a render exception happens.
	 */
	public static String render(GWorld world, boolean renderImage) throws RendererException {
		
		try {
			CustomGSpace space = new CustomGSpace(world.name(), world.dimension());
			space.open();
			
			space.changeWindowSize(world.dimension());
			space.setColor(getColorFromName(world.color()));
			
			world.childrens().forEach((c) -> render(space, c, renderImage));
			
			space.repaint();
			
			String snapshot = snapshot(space);
			
			space.end();
			
			return snapshot;
		} catch(Exception e) {
			throw new RendererException(e.getMessage());
		}
		
	}
	
	private static void render(GContainer container, GObject child, boolean renderImage) {
		
		GElement element = null;
		
		switch(child.type()) {
		case IMAGE:
			if(!renderImage) break;
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
			if(child.dimension().height > 0 && child.dimension().width > 0) gbounded.setDimension(child.dimension());
			for(GObject c : child.childrens()) render(gbounded, c, renderImage);
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
	
	private static class CustomGSpace extends GSpace {

		private static final long serialVersionUID = -619926373478257073L;

		public CustomGSpace(String name, Dimension dim) {
			super(name, dim);
		}
		
		public void open() {
			JFrame frame = new JFrame(name);
			frame.getContentPane().add(this);
			frame.pack();
		}
		
		public void end() {
			JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
			frame.dispose();
		}

	}
	
	public static String b64ImageToHtmlSrc(String img) {
		return "src=\"data:image/png;base64, " + img.trim() + " \" />";
	}


}
