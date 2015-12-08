package com.exosoft.zerfall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public abstract class DrawImage extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	int x, y;

	public void drawBackground(BufferedImage img, int x, int y) {
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, x, y, null);
	}

}
