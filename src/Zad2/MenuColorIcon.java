package Zad2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class MenuColorIcon implements Icon{

	private Color colorToPaint;
	
	public MenuColorIcon(Color color) {
		colorToPaint = color;
	}

	@Override
	public int getIconHeight() {
		return 10;
	}

	@Override
	public int getIconWidth() {
		return 10;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(colorToPaint);
		g.fillOval(5, 5, 10, 10);
	}

}
