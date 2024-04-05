package partie2.io.graphics;

public class GImage extends GObject {
	
	private static final long serialVersionUID = -9090591957100706529L;
	
	private String path;

	public GImage(String path) {
		super(Type.IMAGE);
		this.path = path;
	}
	
	public String path() {
		return path;
	}

}
