package com.exosoft.zerfall;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class Display extends Main {

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(-(Player.xpos + Player.sheet[0].getWidth() / 2) + window.getWidth() / 2,
				-(Player.ypos + Player.sheet[0].getHeight() / 2) + window.getHeight() / 2);
		g2d.drawImage(map, 0, 0, null);
		g2d.drawImage(Player.sheet[Player.sprite], Player.xpos, Player.ypos, null);
		for (zombieClass zombie : zombies)
			g2d.drawImage(zombie.sheet.getSubimage(zombie.sprite * 100, 0, 100, 162), zombie.xpos, zombie.ypos, null);
		g2d.drawImage(foreground, 0, 0, null);
		g2d.translate(Player.xpos + Player.sheet[0].getWidth() / 2, Player.ypos + Player.sheet[0].getHeight() / 2);
		printText(Integer.toString(Player.gunClip) + "/" + Integer.toString(Player.clipSize[Player.weapon]), 1270, 700,
				36, 0xFFFFFF, JLabel.RIGHT);
		printText(Player.gunID[Player.weapon], 1270, 660, 24, 0xEEEEEE, JLabel.RIGHT);
	}

	void printText(String text, int x, int y, int size, int fill, int align) {
		JLabel label = new JLabel(text, align);
		label.setFont(orbitron);
		label.setForeground(new Color(fill));
		label.setLocation(x, y);
	}

	void printText(int text, int x, int y, int size, int fill, int align) {
		printText(Integer.toString(text), x, y, size, fill, align);
	}
	
}