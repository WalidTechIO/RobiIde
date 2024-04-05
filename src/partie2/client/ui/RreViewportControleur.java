package partie2.client.ui;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import partie2.io.Request.RequestType;
import partie2.io.State;
import partie2.utils.UIUtils;

public class RreViewportControleur {
	
	@FXML
	private TextField feedback;
	
	@FXML
	private ImageView image;
	
	@FXML
	private TextArea request;
	
	public void setViewport(State state) {
		if(state.response() != null) {
			BufferedImage img = UIUtils.b64ToImg(state.response().image());
			if(img != null) image.setImage(SwingFXUtils.toFXImage(img, null));
			feedback.setText(state.response().feedback());
		}
		
		if(state.request() != null) {
			if(state.request().type() == RequestType.EXE) {
				request.setText("Type: EXECUTE");
			} else if(state.request().type() == RequestType.PROG && state.request().program() != null) {
				request.setText("Type: PROGRAM\n\nProgram Mode: " + state.request().program().mode() + "\nExpression:\n" + state.request().program().contenu());
			}
		}
		
	}

}