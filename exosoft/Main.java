package exosoft;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
class Main extends JFrame implements KeyListener {
	static boolean keys[] = new boolean[256];
	static BufferedImage map = null;
	static BufferedImage bitmap = null;
	static Sheet sheet = new Sheet();
	static Player player = new Player();
	// Initializes the variables that keep track of the loops
	static Timer drawTimer, logicTimer;
	static BufferedImage foreground = null;

	Main() {
		super("Zerfall");
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(sheet);
		setVisible(true);
		setResizable(false);
		addKeyListener(this);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
		drawTimer = new Timer(1000 / 60, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sheet.repaint();
			}
		});
		logicTimer = new Timer(1000 / 120, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.logic();
			}
		});

		try {
			map = ImageIO.read(new File("resources/Maps/map.png"));
			bitmap = ImageIO.read(new File("resources/Maps/bitmap.png"));
			foreground = ImageIO.read(new File("resources/Maps/foreground.png"));
		} catch (IOException e) {
		}
		drawTimer.start();
		logicTimer.start();
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g.translate((int) -(player.xPos + player.sprites[0].getWidth() / 2 - getWidth() / 2),
					(int) -(player.yPos + player.sprites[0].getHeight() / 2 - getHeight() / 2));
			g.drawImage(map, 0, 0, null);
			g.drawImage(player.sprites[player.spriteNum], (int) player.xPos, (int) player.yPos, null);
			g.drawImage(foreground, 0, 0, null);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not sure why this needs to be here, but whatever.
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public static class Player {
		BufferedImage sprites[] = new BufferedImage[8];
		double xPos = 2270;
		double yPos = 1240;
		double yVel = 0;
		int spriteNum = 0;
		boolean collision[] = new boolean[5];

		public Player() {
			BufferedImage temp = null;
			try {
				temp = ImageIO.read(new File("resources/sprites/player.png"));
			} catch (IOException e) {
			}
			for (int i = 0; i < 8; i++) {
				sprites[i] = temp.getSubimage(i * 175, 0, 175, 161);
			}
		}

		void logic() {
			int c;
			for (int i = 0; i < 5; i++)
				collision[i] = false;
			for (int x = (int) (xPos + 25); x <= xPos + 150; x++) {
				for (int y = (int) (yPos + 161); y <= yPos + 162 + Math.abs(yVel); y++) {
					c = bitmap.getRGB(x, y);
					switch (c) {
					case 0xFF000000:
						collision[1] = true;
						break;
					case 0xFF0000FF:
						collision[0] = true;
						break;
					}
				}
			}
			for (int x = (int) (xPos + 25); x <= xPos + 150; x++) {
				for (int y = (int) yPos; y <= yPos - 1 - Math.abs(yVel); y--) {
					c = bitmap.getRGB(x, y);
					switch (c) {
					case 0xFF000000:
						collision[2] = true;
						break;
					}
				}
			}
			for (int x = (int) (xPos + 20); x <= xPos + 25; x++) {
				for (int y = (int) yPos; y <= yPos + 161; y++) {
					c = bitmap.getRGB(x, y);
					switch (c) {
					case 0xFFFF0000:
						if (keys[KeyEvent.VK_E]) {
							doors(x, y);
							break;
						}
					case 0xFF000000:
						collision[3] = true;
						break;
					}
				}
			}
			for (int x = (int) (xPos + 150); x <= xPos + 155; x++) {
				for (int y = (int) yPos; y <= yPos + 161; y++) {
					c = bitmap.getRGB(x, y);
					switch (c) {
					case 0xFFFF0000:
						if (keys[KeyEvent.VK_E]) {
							doors(x, y);
							continue;
						}
					case 0xFF000000:
						collision[4] = true;
						continue;
					}
				}
			}
			if (collision[1] == false) {
				yVel += .4;
			} else {
				yVel = 0;
				if (spriteNum % 2 == 1)
					spriteNum--;
			}
			if (keys[KeyEvent.VK_A] && collision[3] == false) {
				xPos -= 4;
				if (collision[1] == true && spriteNum != 0) {
					spriteNum = 0;
				}
			}
			if (keys[KeyEvent.VK_D] && collision[4] == false) {
				xPos += 4;
				if (collision[1] == true && spriteNum != 2)
					spriteNum = 2;
			}
			if (keys[KeyEvent.VK_W]) {
				if (collision[0] == true) {
					yVel = -4;
				} else if (collision[1] == true) {
					yVel -= 12;
					if (spriteNum % 2 == 0) {
						spriteNum++;
					}
				}

			}
			yPos += yVel;
		}

		private void doors(int x, int y) {
			int doorColor = 0xFFFF0000;
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
					foreground.setRGB(x2, y2, 0x00FFFFFF);
					bitmap.setRGB(x2, y2, 0xFFFFFFFF);
				}
			}
		}
	}
}
