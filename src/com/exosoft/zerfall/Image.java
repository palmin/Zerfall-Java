package com.exosoft.zerfall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Image extends BufferedImage {
	private BufferedImage image;
	private int x;
	private int y;
	private int translateX;
	private int translateY;
	private boolean translate;
	public Image(String filename) {
		image = ImageIO.read(new File(filename));
	}
	
	public void draw(int x, int y) {
		translate = false;
		repaint();
	}
	
	public void draw(int x, int y, int translateX, int translateY) {
		translate = true;
		repaint();
	}
	
	public void paint(Graphics g) {
        if (translate == true) {
        	g.translate(translateX, translateY);
        }
        g.drawImage(image, x, y, null);
    }

	public int get(int x, int y) {
		Color.rgb(image.getRGB());
	}

}
