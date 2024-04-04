package partie2.client.ui;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import partie2.utils.UIUtils;

public class RreViewportControleur {
	
	@FXML
	private TextField feedback;
	
	@FXML
	private ImageView image;
	
	public void setViewport(String image, String feedback) {
		BufferedImage img = UIUtils.b64ToImg(image);
		if(img != null) this.image.setImage(SwingFXUtils.toFXImage(img, null));
		this.feedback.setText(feedback);
	}

}
