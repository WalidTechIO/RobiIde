package exercice3;

import java.awt.Color;

public abstract class ChangeColor implements Command {
	Color newColor;

	public ChangeColor(Color newColor) {
		this.newColor = newColor;
	}

}