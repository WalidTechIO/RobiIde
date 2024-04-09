package partie2.io.graphics;

public class GImage extends GObject {
	
	private static final long serialVersionUID = -8788474230420661928L;
	
	public String path;
	
	public GImage() {
	}

	public GImage(String path) {
		this.path = path;
	}
	
	public String path() {
		return path;
	}

}
