package partie2.io.graphics;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//Class simple name will be put in JSON serialized object in a property named 'type' when serializing
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//Register all sub-types for JSON deserialization
@JsonSubTypes({
	@JsonSubTypes.Type(value = GWorld.class),
	@JsonSubTypes.Type(value = GRect.class),
	@JsonSubTypes.Type(value = GOval.class),
	@JsonSubTypes.Type(value = GText.class),
	@JsonSubTypes.Type(value = GImage.class),
})
public abstract class GObject implements Serializable, Cloneable {

	private static final long serialVersionUID = -2898754037405728359L;
	
	public final UUID uuid = UUID.randomUUID();
	public Dimension dimension = new Dimension(-1, -1);
	public Dimension position = new Dimension(0, 0);
	public String color = "default";
	public List<GObject> childrens = new ArrayList<>();
	
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(!(obj instanceof GObject other)) return false;
		return other.uuid.equals(uuid);
	}
	
	private void setChildrens(List<GObject> childrens) {
		this.childrens = childrens;
	}
	
	public Object clone() {
		GObject o = null;
		try {
			List<GObject> childrens = new ArrayList<>();
			o = (GObject) super.clone();
			this.childrens.forEach(child -> childrens.add((GObject)child.clone()));
			o.setChildrens(childrens);
			o.setDimension(dimension);
			o.setPosition(position);
		} catch(CloneNotSupportedException ignored) {} //Clone supported, will never be catch
		return o;
	}

	private void setPosition(Dimension position) {
		this.position = new Dimension(position.width, position.height);
	}

	private void setDimension(Dimension dimension) {
		this.dimension = new Dimension(dimension.width, dimension.height);
	}

}
