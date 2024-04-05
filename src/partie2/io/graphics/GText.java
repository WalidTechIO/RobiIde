package partie2.io.graphics;

public class GText extends GObject {
	
	private static final long serialVersionUID = 1L;
	
	private final String text;
	
	GText(String text) {
		super(Type.TEXT);
		this.text = text;
	}

}
