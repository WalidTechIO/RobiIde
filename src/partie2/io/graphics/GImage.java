package partie2.io.graphics;

import java.awt.Dimension;
import java.util.Set;

public class GImage extends GObject {
	
	private static final long serialVersionUID = -8788474230420661928L;
	
	public String path;
	
	public GImage() {
		super(Type.WORLD);
	}

	public GImage(String path) {
		super(Type.IMAGE);
		this.path = path;
	}
	
	public String path() {
		return path;
	}
	
	public Type type() {
		return type;
	}
	
	public Dimension dimension() {
		return dimension;
	}
	
	public Dimension position() {
		return position;
	}
	
	public String color() {
		return color;
	}
	
	public Set<GObject> childrens() {
		return childrens;
	}
	
	public void translate(int x, int y) {
		position.width += x;
		position.height += y;
	}
	
	public void setDim(int x, int y) {
	}
	
	public void setColor(String color) {
	}
	
	public void add(GObject object) {
	}
	
	public void del(GObject object) {
	}
	
	public void clear() {
	}

}
