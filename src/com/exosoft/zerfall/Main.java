package com.exosoft.zerfall;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.XML;
import processing.sound.SoundFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public abstract class Main {
	BufferedImage bitmap, map, foreground;
	boolean keys[];
	static boolean itemsLoaded;
	static final Font orbitron = new Font("fonts/orbitron.ttf", Font.PLAIN, 36);
	static ArrayList<zombieClass> zombies;
	static Player Player;
	XML guns;
	static JFrame window;
	static JLabel label;

	public void init() {
		window = new JFrame();
		window.setSize(1280, 720);
		label = new JLabel("First Name");
		label.setFont(new Font("Courier New", Font.ITALIC, 12));
		label.setForeground(Color.GRAY);
		zombies = new ArrayList<zombieClass>();
		loadItems();
	}

	public static void main(String args[]) {
		if (itemsLoaded) {
			Player.weapon();
			Player.movement();
			for (zombieClass zombie : zombies)
				zombie.movement();
		}
	}

	public void keyPressed(KeyEvent evt) {
		keys[evt.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent evt) {
		keys[evt.getKeyCode()] = false;
	}

	public synchronized void loadItems() {
		try {
			bitmap = ImageIO.read(new File("Maps/bitmap.png"));
			map = ImageIO.read(new File("Maps/map.png"));
			foreground = ImageIO.read(new File("Maps/foreground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		keys = new boolean[128];
		//guns = loadXML("Resources/Gun Information.xml");
		itemsLoaded = true;
	}

}

class zombieClass extends Main {
	BufferedImage sheet = null;
	int sprite = 0, xpos = 2270, ypos = 940, health = 100, yspeed = 1, xspeed;
	boolean collision[] = new boolean[5];

	zombieClass(int speed) {
		try {
			sheet = ImageIO.read(new File("sprites/zombie.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xspeed = speed;
	}

	void movement() {
		for (int i = 0; i < 5; i++)
			collision[i] = false;
		for (int x = xpos; x <= xpos + 100; x++) {
			for (int y = ypos + 161; y <= ypos + 161 + Math.abs(yspeed); y++) {
				int c = bitmap.getRGB(x, y);
				collision[1] = (c == 0xFF0000 || c == 0x0000FF) ? true : collision[1];
				collision[0] = (c == 0x0000FF) ? true : collision[0];
			}
		}
		for (int x = xpos; x <= xpos + 100; x++) {
			for (int y = ypos; y <= ypos - 1 - Math.abs(yspeed); y--) {
				int c = bitmap.getRGB(x, y);
				collision[2] = (c == 0x0000FF) ? true : collision[2];
			}
		}
		for (int x = xpos; x <= xpos - xspeed; x--) {
			for (int y = ypos; y <= ypos + 162; y++) {
				int c = bitmap.getRGB(x, y);
				collision[3] = (c == 0xFF0000 || c == 0x0000FF) ? true : collision[3];
			}
		}
		for (int x = xpos + 100; x <= xpos + 100 + xspeed; x++) {
			for (int y = ypos; y <= ypos + 162; y++) {
				int c = bitmap.getRGB(x, y);
				collision[4] = (c == 0x0000FF || c == 0xFF0000) ? true : collision[4];
			}
		}
		if (collision[1] || collision[2]) {
			yspeed = 1;
		} else {
			yspeed++;
		}
		if (collision[0] && Player.ypos < ypos && collision[2] == false) {
			yspeed = -xspeed;
			collision[1] = false;
		}
		if (collision[1] == false)
			ypos += yspeed;
		if (Player.xpos > xpos && Player.ypos > ypos - 40 && Player.ypos < ypos + 40) {
			xpos = xpos + xspeed;
			sprite = 1;
		} else if (Player.xpos + 50 < xpos && Player.ypos > ypos - 5 && Player.ypos < ypos + 5) {
			xpos = xpos - xspeed;
			sprite = 0;
		}
	}
}

class timer extends Main {
	double activation, duration;
	boolean active;

	timer(double d) {
		duration = d;
	}

	void check() {
		if (System.currentTimeMillis() * .001 - activation > duration)
			active = false;
	}

	void activate() {
		activation = System.currentTimeMillis() * .001;
		active = true;
	}

	void set(float d) {
		duration = d;
	}
}