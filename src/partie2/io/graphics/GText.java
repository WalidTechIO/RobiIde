package partie2.io.graphics;

import java.awt.Dimension;
import java.util.List;

public class GText extends GObject {
	
	private static final long serialVersionUID = -6307850286316445700L;
	
	public String text;
	
	public GText() {
		super(Type.TEXT);
	}
	
	public GText(String text) {
		super(Type.TEXT);
		this.text = text;
	}
	
	public String text() {
		return text;
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
		dimension.width = x;
		dimension.height = y;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void add(GObject object) {
		childrens.add(object);
	}
	
	public void del(GObject object) {
		childrens.remove(object);
	}
	
	public void clear() {
		childrens.clear();
	}

}
