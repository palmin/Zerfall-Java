package com.exosoft.zerfall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	private BufferedImage image;
	private int x;
	private int y;
	private int translateX;
	private int translateY;
	private boolean translate;
	public Image(String filename) throws IOException {
		image = ImageIO.read(new File(filename));
	}
	
	public Image(BufferedImage image) {
	}
	
	public void draw(int x, int y) {
		translate = false;
	}
	
	public void draw(int x, int y, int translateX, int translateY) {
		translate = true;
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
