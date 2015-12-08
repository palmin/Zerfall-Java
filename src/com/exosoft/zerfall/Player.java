package com.exosoft.zerfall;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import processing.sound.SoundFile;
import sun.audio.AudioPlayer;

public abstract class Player extends Main {
	BufferedImage sheet[] = new BufferedImage[8];
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
	Clip gunAudio[][] = new Clip[2][18], dryfire;
	timer swap = new timer(.2), reload = new timer(1);
	String gunID[] = { "AK47", "AUG", "Dragunov", "FAL", "FAMAS", "G3", "L2A3", "M1911", "M1918", "M1928", "M60", "M9",
			"MP40", "PPK", "RPK", "Stoner63", "Uzi" };
	AudioLoader aud = new AudioLoader();
	public Player() throws IOException {
		for (int i = 0; i < gunID.length; i++) {
			gunAudio[0][i] = aud.loadClip("Sounds/Guns/" + gunID[i] + " Gunshot.ogg");
			gunAudio[1][i] = aud.loadClip("Sounds/Guns/" + gunID[i] + " Reload.ogg");
		}
		BufferedImage temp = ImageIO.read(new File("sprites/player.png"));
		for (int i = 0; i < 8; i++)
			sheet[i] = temp.getSubimage(i * 175, 0, 175, 161);
		temp = null;
		dryfire = aud.loadClip("Sounds/Dry-Fire.ogg");
	}

	void movement() {
		for (int i = 0; i < 5; i++)
			collision[i] = false;
		for (int x = xpos + 25; x <= xpos + 150; x++) {
			for (int y = ypos + 161; y <= ypos + 162 + Math.abs(yspeed); y++) {
				int c = bitmap.getRGB(x, y);
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
				int c = bitmap.getRGB(x, y);
				switch (c) {
				case 0x000000:
					collision[2] = true;
					break;
				}
			}
		}
		for (int x = xpos + 20; x <= xpos + 25; x++) {
			for (int y = ypos; y <= ypos + 161; y++) {
				int c = bitmap.getRGB(x, y);
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
				int c = bitmap.getRGB(x, y);
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
		while (bitmap.getRGB(x - 1, y) == doorColor)
			x--;
		while (bitmap.getRGB(x, y - 1) == doorColor)
			y--;
		while (bitmap.getRGB(x + w + 1, y) == doorColor)
			w++;
		while (bitmap.getRGB(x, y + h + 1) == doorColor)
			h++;
		for (int x2 = x; x2 < x + w + 1; x2++) {
			for (int y2 = y; y2 < y + h + 1; y2++) {
				bitmap.setRGB(x2, y2, 0xFFFFFF);
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
			reload.set(gunAudio[1][weapon].getMicrosecondLength());
		}
		if (keys[32] && reload.active == false) {
			if (boltPosition == 1 && gunClip > 0) {
				gunAudio[0][weapon].start();
				gunClip--;
			} else if (boltPosition == 1 && gunClip == 0) {
				dryfire.setFramePosition(0);
				dryfire.start();
			}
		}
		boltPosition = (keys[32] && boltPosition <= gunRPM[weapon]) ? boltPosition + 1 : 1;
		if (keys[82] && reload.active == false) { // If the R key is pressed
			reload.activate();
			gunAudio[1][weapon].setFramePosition(0);
			gunAudio[1][weapon].start();
		}
	}

	private class Gun extends Main {
		public Clip gunshot, reload;
		public int clip, damage, rate;
		public String name;

		public Gun(String name, int rate, int damage, int clip) {
			gunshot = aud.loadClip("Sounds/Guns/" + name + " Gunshot.ogg");
			reload = aud.loadClip("Sounds/Guns/" + name + " Gunshot.ogg");
		}
	}

}
