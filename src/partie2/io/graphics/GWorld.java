package partie2.io.graphics;

import java.awt.Dimension;

public class GWorld extends GObject {
	

	private static final long serialVersionUID = 1919482233925692763L;

	public GWorld() {
	}
	
	public GWorld(String windowName, Dimension dim) {
		setDim(dim.width, dim.height);
	}
	
	public GWorld copy() {
		return (GWorld)clone();
	}
	
}
