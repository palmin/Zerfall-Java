package com.exosoft.zerfall;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Image extends Main {
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
		System.out.println("Reading image file " + filename);
		image = ImageIO.read(new File("resources/" + filename));
	}

	public Image(BufferedImage img) {
		image = img;
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
		if (x >= image.getWidth() || x < 0 || y >= image.getHeight() || y < 0) {
			return 0x00000000;
		} else {
			return image.getRGB(x, y);
		}
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
