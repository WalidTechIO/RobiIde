package partie2.client.ui;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import partie2.io.Request.RequestType;
import partie2.io.State;
import partie2.utils.GraphicsUtils;
import partie2.utils.GraphicsUtils.RendererException;

public class RreViewportControleur {
	
	@FXML
	private TextField feedback;
	
	@FXML
	private ImageView image;
	
	@FXML
	private TextArea request;
	
	public void setViewport(State state) {
		if(state.response() != null) {
			BufferedImage img = null;
			try {
				img = GraphicsUtils.b64ToImg(GraphicsUtils.render(state.response().world()));
			} catch (RendererException e) {
				new Alert(Alert.AlertType.ERROR, "Erreur de rendu: " + e.getMessage()).show();
			}
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
