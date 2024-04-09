package partie2.io.graphics;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GImage.class, name = "IMAGE"),

    @JsonSubTypes.Type(value = GOval.class, name = "OVAL"),
    
    @JsonSubTypes.Type(value = GRect.class, name = "RECT"),
    
    @JsonSubTypes.Type(value = GText.class, name = "TEXT"),
    
    @JsonSubTypes.Type(value = GWorld.class, name = "WORLD"),
    
})
public abstract class GObject implements Serializable, Cloneable {

	private static final long serialVersionUID = -2898754037405728359L;

	public final Type type;
	
	public final UUID uuid = UUID.randomUUID();
	public Dimension dimension = new Dimension(-1, -1);
	public Dimension position = new Dimension(0, 0);
	public String color = "blue";
	public List<GObject> childrens = new ArrayList<>();
	
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
		if(type == Type.IMAGE) throw new UnsupportedOperationException("Can't setDimension of an image");
		dimension.width = x;
		dimension.height = y;
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
		childrens.remove(object); //Searching by UUID so no pb
	}
	
	public void clear() {
		if(type == Type.IMAGE) throw new UnsupportedOperationException("Can't delete children of an image");
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
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	private void setPosition(Dimension position) {
		this.position = new Dimension(position.width, position.height);
	}

	private void setDimension(Dimension dimension) {
		this.dimension = new Dimension(dimension.width, dimension.height);
	}

}
