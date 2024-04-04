package partie2.utils;

import java.awt.Dimension;

import javax.swing.JFrame;

import graphicLayer.GSpace;
import partie2.server.ClientManager;

public class CustomGSpace extends GSpace {

	private static final long serialVersionUID = -619926373478257073L;

	public CustomGSpace(String name, Dimension dim) {
		super(name, dim);
	}
	
	public void open(ClientManager client) {
		JFrame frame = new JFrame(name);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
		requestFocus();
	}

}
