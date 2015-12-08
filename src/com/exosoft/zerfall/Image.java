package com.exosoft.zerfall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Image extends BufferedImage {
	private BufferedImage image;
	private int x;
	private int y;
	public Image(String filename) {
		image = ImageIO.read(new File(filename));
	}
	
	public void draw(int x, int y) {
		repaint();
	}
	
	public void paint(Graphics g) {
        g.drawImage(image, x, y, null);
    }

}
