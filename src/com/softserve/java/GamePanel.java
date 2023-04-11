package com.softserve.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 600;
	public static final int UNIT_SIZE = 25;
	public static final int TOTAL_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH) / UNIT_SIZE;
	public static final int DELAY = 75;
	private final int[] oX = new int[TOTAL_UNITS];
	private final int[] oY = new int[TOTAL_UNITS];
	private int bodyParts = 6;
	private int appleX;
	private int appleY;
	char direction = 'R'; //Right, Left, Up, Down
	boolean running = false;
	Timer timer;
	Random random;
	
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}
	
	public void draw(Graphics graphics) {
		if (running) {
			//apple
			graphics.setColor(Color.red);
			graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			//snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					graphics.setColor(Color.green);
				} else {
					graphics.setColor(new Color(45, 180, 0));
				}
				graphics.fillRect(oX[i], oY[i], UNIT_SIZE, UNIT_SIZE);
			}
		} else {
			endGame(graphics);
		}
	}
	
	public void newApple(){
		appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}
	
	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			oX[i] = oX[i-1];
			oY[i] = oY[i-1];
		}
		switch (direction) {
			case 'U':
				oY[0] = oY[0] - UNIT_SIZE;
				break;
			case 'D':
				oY[0] = oY[0] + UNIT_SIZE;
				break;
			case 'L':
				oX[0] = oX[0] - UNIT_SIZE;
				break;
			case 'R':
				oX[0] = oX[0] + UNIT_SIZE;
				break;
		}
	}
	
	public void checkApple() {
		if (oX[0] == appleX && oY[0] == appleY) {
			bodyParts++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		//head collides body
		for (int i = bodyParts; i > 0; i--) {
			if (oX[0] == oX[i] && oY[0] == oY[i]) {
				running = false;
				break;
			}
		}
		//head crush left/right/bottom/upper boarder
		if (oX[0] < 0) {
			running = false;
		}
		if (oX[0] > SCREEN_WIDTH) {
			running = false;
		}
		if (oY[0] < 0) {
			running = false;
		}
		if (oY[0] > SCREEN_WIDTH) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	
	public void endGame(Graphics graphics) {
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Game over", (SCREEN_WIDTH - fontMetrics.stringWidth("Game over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	private class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U') {
						direction = 'D';
					}
					break;
			}
		}
	}
}
