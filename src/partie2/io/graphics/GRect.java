package partie2.io.graphics;

import java.awt.Dimension;
import java.util.List;

public class GRect extends GObject {

	private static final long serialVersionUID = -4323739487667881330L;

	public GRect() {
		super(Type.RECT);
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
		this.color = color;
	}
	
	public void add(GObject object) {
		childrens.add(object);
	}
	
	public void del(GObject object) {
		childrens.remove(object); //Searching by UUID so no pb
	}
	
	public void clear() {
		childrens.clear();
	}
	
}
