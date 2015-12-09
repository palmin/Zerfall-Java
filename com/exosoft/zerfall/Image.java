package com.exosoft.zerfall;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Image extends Applet {
	private BufferedImage image;
	private int x;
	private int y;
	private int translateX;
	private int translateY;
	private boolean translate;
	public Image() {
		image = null;
	}
	
	public Image(String filename) throws IOException {
		image = ImageIO.read(new File("/resources/" + filename));
	}
	
	public Image(BufferedImage img) {
		image = img;
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
		return image.getRGB(x, y);
	}
	
	public Image get(int x, int y, int w, int h) {
		return new Image(image.getSubimage(x, y, w, h));
	}
	
	public int width() {
		return image.getWidth();
	}
	
	public int height() {
		return image.getHeight();
	}

	public void set(int x, int y, int c) {
		image.setRGB(x, y, c);
	}

}
