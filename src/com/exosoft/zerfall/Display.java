package com.exosoft.zerfall;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class Display extends Main {

	public void paintComponent(Graphics g) {
		int translateX = (int) (-(Player.xpos + Player.sheet[0].getWidth() / 2) + window.getWidth() / 2);
		int translateY = (int) (-(Player.ypos + Player.sheet[0].getHeight() / 2) + window.getHeight() / 2);
		map.draw(0, 0, translateX, translateY);
		Player.sheet[Player.sprite].draw(Player.xpos, Player.ypos, translateX, translateY);
		for (zombieClass zombie : zombies)
			zombie.sheet.get(zombie.sprite * 100, 0, 100, 162).draw(zombie.xpos, zombie.ypos, translateX, translateY);
		foreground.draw(0, 0, translateX, translateY);
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
