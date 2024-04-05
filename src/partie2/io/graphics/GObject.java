package partie2.io.graphics;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class GObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	final Type type;
	
	final UUID uuid = UUID.randomUUID();
	Dimension dimension = new Dimension(50, 50);
	Dimension position = new Dimension(0, 0);
	String color = "blue";
	List<GObject> childrens = new ArrayList<>();
	
	public static enum Type {
		WORLD,
		IMAGE,
		TEXT,
		RECT,
		OVAL
	}
	
	public GObject(Type type) {
		this.type = type;
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
	
	public List<GObject> childrens() {
		return childrens;
	}
	
	public void translate(int x, int y) {
		position.width += x;
		position.height += y;
	}
	
	public void setDim(int x, int y) {
		dimension.width += x;
		dimension.height += y;
	}
	
	public void setColor(String color) {
		if(type == Type.IMAGE) throw new UnsupportedOperationException("Can't setColor of an image");
		this.color = color;
	}
	
	public void add(GObject object) {
		if(type == Type.IMAGE) throw new UnsupportedOperationException("Can't add child to an image");
		childrens.add(object);
	}
	
	public void del(GObject object) {
		if(type == Type.IMAGE) throw new UnsupportedOperationException("Can't delete children of an image");
		childrens.remove(object);
	}
	
	public void clear() {
		childrens.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(!(obj instanceof GObject other)) return false;
		return other.uuid.equals(uuid);
	}

}
