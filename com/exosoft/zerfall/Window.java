package com.exosoft.zerfall;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JPanel {
	public class Text extends JLabel {
		Text(String label, int x, int y, int align) {
			JLabel index = new JLabel(label);
			index.setHorizontalAlignment(align);
			index.setLocation(x, y);
			labels.add(index);
			content.add(index);
		}
	}

	private JFrame frame;
	private JPanel content = new JPanel();
	public int width;
	public int height;
	public ArrayList<BufferedImage> images = new ArrayList<>();
	public ArrayList<Integer> xCoords = new ArrayList<>();
	public ArrayList<Integer> yCoords = new ArrayList<>();
	public ArrayList<JLabel> labels = new ArrayList<>();

	public Window(String title, int width, int height) {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.add(content, BorderLayout.CENTER);
	}

	public void drawImage(Image image, int x, int y) {
		images.add(image.image);
	}

	public void addLabel(String label, int x, int y, int align) {

	}

	public void reDraw() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < images.size(); i++) {
			g.drawImage(images.get(i), xCoords.get(i), yCoords.get(i), null);
		}
	}
}
