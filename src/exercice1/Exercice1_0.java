package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import graphicLayer.GRect;
import graphicLayer.GSpace;

public class Exercice1_0 {
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();

	public Exercice1_0() throws InterruptedException {
		space.addElement(robi);
		space.open();
		while(robi.getPosition().getX() < space.getSize().getWidth() - robi.getWidth()) {
			robi.translate(new Point(1,0));
			Thread.sleep(10);
		}
		while(robi.getPosition().getY() < space.getSize().getHeight() - robi.getHeight()) {
			robi.translate(new Point(0, 1));
			Thread.sleep(10);
		}
		while(robi.getPosition().getX() > 0) {
			robi.translate(new Point(-1, 0));
			Thread.sleep(10);
		}
		while(robi.getPosition().getY() > 0) {
			robi.translate(new Point(0, -1));
			Thread.sleep(10);
		}
		robi.setColor(new Color((int) (Math.random() * 0x1000000)));
	}

	public static void main(String[] args) throws InterruptedException {
		new Exercice1_0();
	}

}