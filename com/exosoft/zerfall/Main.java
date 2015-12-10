package com.exosoft.zerfall;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
H
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	static Image bitmap, map, foreground;
	static boolean keys[];
	static boolean itemsLoaded = true;
	static Font orbitron = new Font("fonts/orbitron.ttf", Font.PLAIN, 36);
	static ArrayList<zombieClass> zombies;
	static Player player;
	// XML guns;
	public static JFrame window;
	public static JLabel label;

	public static void main(String args[]) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		window = new JFrame("Zerfall");
		window.setSize(1280, 720);
		window.setVisible(true);S
		label = new JLabel("First Name");
		label.setForeground(Color.GRAY);
		zombies = new ArrayList<zombieClass>();
		bitmap = new Image("Maps/bitmap.png");
		map = new Image("Maps/map.png");
		foreground = new Image("Maps/foreground.png");
		keys = new boolean[128];
		player = new Player();
		// guns = loadXML("Resources/Gun Information.xml");
		while (itemsLoaded) {
			player.weapon();
			player.movement();
			for (zombieClass zombie : zombies)
				zombie.movement();
		}
	}

	public static void paint(Graphics g) {
		int translateX = (int) (-(player.xpos + player.sheet[0].width() / 2) + window.getWidth() / 2);
		int translateY = (int) (-(player.ypos + player.sheet[0].height() / 2) + window.getHeight() / 2);
		map.draw(0, 0, translateX, translateY);
		player.sheet[player.sprite].draw(player.xpos, player.ypos, translateX, translateY);
		for (zombieClass zombie : zombies)
			zombie.sheet.get(zombie.sprite * 100, 0, 100, 162).draw(zombie.xpos, zombie.ypos, translateX, translateY);
		foreground.draw(0, 0, translateX, translateY);
		printText(Integer.toString(player.gunClip) + "/" + Integer.toString(player.clipSize[player.weapon]), 1270, 700,
				36, 0xFFFFFF, JLabel.RIGHT);
		printText(player.gunID[player.weapon], 1270, 660, 24, 0xEEEEEE, JLabel.RIGHT);
	}

	static void printText(String text, int x, int y, int size, int fill, int align) {
		JLabel label = new JLabel(text, align);
		label.setFont(orbitron);
		label.setForeground(new Color(fill));
		label.setLocation(x, y);
	}

	void printText(int text, int x, int y, int size, int fill, int align) {
		printText(Integer.toString(text), x, y, size, fill, align);
	}

	public void keyPressed(KeyEvent evt) {
		keys[evt.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent evt) {
		keys[evt.getKeyCode()] = false;
	}

	public static class Player extends Main {
		Image sheet[] = new Image[8];
		int sprite = 0;
		int xpos = 2270;
		int ypos = 940;
		int yspeed = 1;
		int weapon = 0;
		int gunClip = 30;
		int gunRPM[] = { 6, 5, 120, 48, 3, 6, 600, 144, 6, 5, 6, 90, 7, 240, 6, 4, 6 };
		int clipSize[] = { 30, 30, 10, 30, 25, 20, 5, 7, 20, 50, 100, 10, 32, 8, 75, 150, 32 };
		int boltPosition = 0;
		boolean collision[] = new boolean[5];
		Audio gunAudio[][] = new Audio[2][18], dryfire;
		timer swap = new timer(.2), reload = new timer(1);
		String gunID[] = { "AK47", "AUG", "Dragunov", "FAL", "FAMAS", "G3", "L2A3", "M1911", "M1918", "M1928", "M60",
				"M9", "MP40", "PPK", "RPK", "Stoner63", "Uzi" };

		public Player() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
			for (int i = 0; i < gunID.length; i++) {
				gunAudio[0][i] = new Audio("Sounds/Guns/" + gunID[i] + " Gunshot.ogg");
				gunAudio[1][i] = new Audio("Sounds/Guns/" + gunID[i] + " Reload.ogg");
			}
			Image temp = new Image("sprites/player.png");
			for (int i = 0; i < 8; i++)
				sheet[i] = temp.get(i * 175, 0, 175, 161);
			temp = null;
			dryfire = new Audio("Sounds/Dry-Fire.ogg");
		}

		void movement() {
			for (int i = 0; i < 5; i++)
				collision[i] = false;
			for (int x = xpos + 25; x <= xpos + 150; x++) {
				for (int y = ypos + 161; y <= ypos + 162 + Math.abs(yspeed); y++) {
					int c = bitmap.get(x, y);
					switch (c) {
					case 0x000000:
						collision[1] = true;
						break;
					case 0x0000FF:
						collision[0] = true;
						break;
					}
				}
			}
			for (int x = xpos + 25; x <= xpos + 150; x++) {
				for (int y = ypos; y <= ypos - 1 - Math.abs(yspeed); y--) {
					int c = bitmap.get(x, y);
					switch (c) {
					case 0x000000:
						collision[2] = true;
						break;
					}
				}
			}
			for (int x = xpos + 20; x <= xpos + 25; x++) {
				for (int y = ypos; y <= ypos + 161; y++) {
					int c = bitmap.get(x, y);
					switch (c) {
					case 0xFF0000:
						if (keys[69])
							doors(x, y);
					case 0x000000:
						collision[3] = true;
						break;
					}
				}
			}
			for (int x = xpos + 150; x <= xpos + 155; x++) {
				for (int y = ypos; y <= ypos + 161; y++) {
					int c = bitmap.get(x, y);
					if (collision[4] == false && (c == 0xFF0000 || c == 0x000000))
						collision[4] = true; // Right bound
					if (c == 0xFF0000 && keys[69])
						doors(x, y);
				}
			}
			if (keys[65] && collision[3] == false)
				xpos -= 5;
			if (keys[68] && collision[4] == false)
				xpos += 5;
			if (keys[65] && collision[1] && keys[' '] == false) {
				sprite = 0;
			} else if (keys[68] && collision[1] && keys[' '] == false) {
				sprite = 2;
			}
			if (keys[87] && collision[2] == false) {
				if (collision[1]) {
					yspeed = -10;
					collision[1] = false;
					if (sprite % 2 == 0)
						sprite++;
				}
				if (collision[0]) {
					yspeed = -4;
					collision[1] = false;
				}
			}
			yspeed = (collision[1] || collision[2]) ? 1 : yspeed + 1;
			ypos += (collision[1] == false) ? yspeed : 0;
			if (sprite > 3 && boltPosition != 2) {
				sprite -= 4;
			} else if (keys[32] && gunClip > 0 && sprite <= 3) {
				sprite += 4;
			}
		}

		public void doors(int x, int y) {
			int doorColor = 0xFF0000;
			int w = 0;
			int h = 0;
			while (bitmap.get(x - 1, y) == doorColor)
				x--;
			while (bitmap.get(x, y - 1) == doorColor)
				y--;
			while (bitmap.get(x + w + 1, y) == doorColor)
				w++;
			while (bitmap.get(x, y + h + 1) == doorColor)
				h++;
			for (int x2 = x; x2 < x + w + 1; x2++) {
				for (int y2 = y; y2 < y + h + 1; y2++) {
					bitmap.set(x2, y2, 0xFFFFFF);
				}
			}
		}

		public void weapon() {
			if (reload.active) {
				reload.check();
				if (reload.active == false)
					gunClip = clipSize[weapon];
			}
			if (swap.active)
				swap.check();

			if (keys[70] && swap.active == false) {
				weapon = (weapon < 16) ? weapon + 1 : 0;
				gunClip = clipSize[weapon];
				swap.activate();
				reload.set(gunAudio[1][weapon].length());
			}
			if (keys[32] && reload.active == false) {
				if (boltPosition == 1 && gunClip > 0) {
					gunAudio[0][weapon].play();
					gunClip--;
				} else if (boltPosition == 1 && gunClip == 0) {
					dryfire.play();
				}
			}
			boltPosition = (keys[32] && boltPosition <= gunRPM[weapon]) ? boltPosition + 1 : 1;
			if (keys[82] && reload.active == false) { // If the R key is pressed
				reload.activate();
				gunAudio[1][weapon].play();
			}
		}

	}

	class zombieClass extends Main {
		Image sheet = null;
		int sprite = 0, xpos = 2270, ypos = 940, health = 100, yspeed = 1, xspeed;
		boolean collision[] = new boolean[5];

		zombieClass(int xspeed) throws IOException {
			sheet = new Image("sprites/zombie.png");
		}

		void movement() {
			for (int i = 0; i < 5; i++)
				collision[i] = false;
			for (int x = xpos; x <= xpos + 100; x++) {
				for (int y = ypos + 161; y <= ypos + 161 + Math.abs(yspeed); y++) {
					int c = bitmap.get(x, y);
					collision[1] = (c == 0xFF0000 || c == 0x0000FF) ? true : collision[1];
					collision[0] = (c == 0x0000FF) ? true : collision[0];
				}
			}
			for (int x = xpos; x <= xpos + 100; x++) {
				for (int y = ypos; y <= ypos - 1 - Math.abs(yspeed); y--) {
					int c = bitmap.get(x, y);
					collision[2] = (c == 0x0000FF) ? true : collision[2];
				}
			}
			for (int x = xpos; x <= xpos - xspeed; x--) {
				for (int y = ypos; y <= ypos + 162; y++) {
					int c = bitmap.get(x, y);
					collision[3] = (c == 0xFF0000 || c == 0x0000FF) ? true : collision[3];
				}
			}
			for (int x = xpos + 100; x <= xpos + 100 + xspeed; x++) {
				for (int y = ypos; y <= ypos + 162; y++) {
					int c = bitmap.get(x, y);
					collision[4] = (c == 0x0000FF || c == 0xFF0000) ? true : collision[4];
				}
			}
			if (collision[1] || collision[2]) {
				yspeed = 1;
			} else {
				yspeed++;
			}
			if (collision[0] && player.ypos < ypos && collision[2] == false) {
				yspeed = -xspeed;
				collision[1] = false;
			}
			if (collision[1] == false)
				ypos += yspeed;
			if (player.xpos > xpos && player.ypos > ypos - 40 && player.ypos < ypos + 40) {
				xpos = xpos + xspeed;
				sprite = 1;
			} else if (player.xpos + 50 < xpos && player.ypos > ypos - 5 && player.ypos < ypos + 5) {
				xpos = xpos - xspeed;
				sprite = 0;
			}
		}

	}

	class timer extends Main {
		double activation, duration;
		boolean active;

		timer(double duration) {
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
}
