package partie2.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
	 * @param ignore image rendering if false.
	 * @return String in Base64 format representing the world rendered as a png.
	 * @throws RendererException If a render exception happens.
	 */
	public static String render(GWorld world, boolean clientRender) throws RendererException {
		
		try {
			CustomGSpace space = new CustomGSpace("Renderer", world.dimension());
			space.open();
			
			space.changeWindowSize(world.dimension());
			space.setColor(getColorFromName(world.color()));
			
			world.childrens().forEach((c) -> render(space, c, clientRender));
			
			space.repaint();
			
			String snapshot = snapshot(space);
			
			space.end();
			
			return snapshot;
		} catch(Exception e) {
			throw new RendererException(e.getMessage());
		}
		
	}
	
	private static void render(GContainer container, GObject child, boolean clientRender) {
		
		GElement element = null;
		
		Class<? extends GObject> childClass = child.getClass();

		if(childClass == partie2.io.graphics.GImage.class) {
			BufferedImage rawImage = null;
			String path = ((partie2.io.graphics.GImage)child).path();
			File file = new File(path);
			if(!clientRender) {
				if(!file.exists()) {
					try {
						rawImage = b64ToImg(path);
					} catch(IllegalArgumentException e) {
						System.err.println("Rendu cote serveur: L'image reçue n'est ni au format Base64 ni présente cote serveur"); 
					}
					
					if(rawImage == null) return;
				}
			}
			
			try {
				if(rawImage == null) rawImage = ImageIO.read(file);
			} catch (IOException e) {
				throw new IllegalArgumentException("Invalid path for image");
			}
			element = new GImage(rawImage);
		}
		
		if(childClass == partie2.io.graphics.GOval.class) element = new GOval();

		if(childClass == partie2.io.graphics.GRect.class) element = new GRect();
		
		if(childClass == partie2.io.graphics.GText.class) element = new GString(((GText)child).text());
		
		if(element == null) return; //If element is unknown don't treat
		
		element.setColor(getColorFromName(child.color()));
		element.translate(new Point(child.position().width, child.position().height));
		if(element instanceof GBounded gbounded) {
			if(child.dimension().height > 0 && child.dimension().width > 0) gbounded.setDimension(child.dimension());
			for(GObject c : child.childrens()) render(gbounded, c, clientRender);
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
	
	public static BufferedImage b64ToImg(String b64) {
		ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(b64));
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			return null;
		}
	}


}
