package partie2.io.graphics;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.List;

abstract class GObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	final Type type;
	
	Dimension dimension;
	Dimension position;
	String color;
	List<GObject> children;
	
	public static enum Type {
		IMAGE,
		TEXT,
		RECT,
		OVAL
	}
	
	GObject(Type type) {
		this.type = type;
	}

}
