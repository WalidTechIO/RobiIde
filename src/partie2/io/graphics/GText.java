package partie2.io.graphics;

public class GText extends GObject {
	
	private static final long serialVersionUID = -6307850286316445700L;
	
	public String text;
	
	public GText() {
	}
	
	public GText(String text) {
		this.text = text;
	}
	
	public String text() {
		return text;
	}

}
