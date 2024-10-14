package org.java.snake.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.java.snake.model.Food;
import org.java.snake.model.Snake;

public class GUI {
	static JFrame frame;
	static JPanel panel;
	private Snake snake;
	private Game snakeGame;
	private Food food;
	private Font fontMenu;
	private Font fontHeader;
	
	public GUI() {
    	snakeGame = new Game();
    	snakeGame.setSnakeGame(snakeGame);
	}
	
	public void createGameWindow() {	
		frame = new JFrame("Snake Game");
		frame.setSize(1275, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		frame.setLocationRelativeTo(null);
		panel = new Draw();
		panel.setLayout(null);
		frame.add(panel);
		
	    fontMenu = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	    fontHeader = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		
		frame.requestFocus();	
		frame.repaint();
		ImageIcon icon = new ImageIcon(getClass().getResource("../../../../res/snake.png"));
		frame.setIconImage(icon.getImage());
		frame.setVisible(true);

		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if (snakeGame.isStarted() == true) {
					if (snakeGame.isPause() == true)
						checkPauseMenuInputs(e);
					else if (snakeGame.isPause() == false)
						checkCurrentGameInputs(e);	
				} else if (snakeGame.isFinished() == true) 
					checkGameOverInputs(e);
				else if (snakeGame.isStartmenu() == true)
					checkStartMenuInputs(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});	
	}

	class Draw extends JPanel {	
		private static final long serialVersionUID = 1L;
	
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
	
			if (snakeGame.isStarted() == true) {
				if (snakeGame.isPause() == true)
					drawPauseMenu(g2);
				else if (snakeGame.isPause() == false)
					drawCurrentGame(g2);
			} else if (snakeGame.isFinishedBlink1() == true)
				drawBlinkAnimation1(g2);
			else if (snakeGame.isFinishedBlink2() == true)
				drawBlinkAnimation2(g2);
			else if (snakeGame.isFinished() == true) 
				drawGameOver(g2);
			else if (snakeGame.isStartmenu() == true)
				drawStartMenu(g2);
		}
	}
	
	public void checkPauseMenuInputs(KeyEvent e) {
		if (e.getKeyCode() == 27)
			snakeGame.setPause(false);
		else if (e.getKeyCode() == 38) {
			if (snakeGame.getMenuSelection().equals("Exit game")) {
				snakeGame.setMenuSelection("Back to main menu");
				GUI.frame.repaint();
			} else if (snakeGame.getMenuSelection().equals("Back to main menu")) {
				snakeGame.setMenuSelection("Restart game");
				GUI.frame.repaint();
			}
		} else if (e.getKeyCode() == 40) {
			if (snakeGame.getMenuSelection().equals("Restart game")) {
				snakeGame.setMenuSelection("Back to main menu");
				GUI.frame.repaint();
			} else if(snakeGame.getMenuSelection().equals("Back to main menu")) {
				snakeGame.setMenuSelection("Exit game");
				GUI.frame.repaint();
			}		
		} else if (e.getKeyCode() == 10) {
			switch(snakeGame.getMenuSelection()) {
			case "Restart game":
				snakeGame.restartGame();
				break;
			case "Exit game":
				System.exit(0);
				break;
			case "Back to main menu":
				snakeGame.setPause(false);
				snakeGame.setStarted(false);
				snakeGame.restartGame();
				snakeGame.setStartmenu(true);
				snakeGame.setMenuSelection("Start game");
				GUI.frame.repaint();
				break;
			}
		}
	}
	
	public void checkCurrentGameInputs(KeyEvent e) {
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
			if (snake.isSnakeDown() == false) {
				snake.setSnakeDown(false);
				snake.setSnakeRight(false);
				snake.setSnakeLeft(false);
				snake.setSnakeUp(true);
			}
		} else if (e.getKeyCode() == 40 || e.getKeyCode() == 83) {
			if (snake.isSnakeUp() == false) {
				snake.setSnakeUp(false);
				snake.setSnakeRight(false);
				snake.setSnakeLeft(false);
				snake.setSnakeDown(true);
			}
		} else if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
			if (snake.isSnakeRight() == false) {
				snake.setSnakeUp(false);
				snake.setSnakeRight(false);
				snake.setSnakeDown(false);
				snake.setSnakeLeft(true);
			}
		}else if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
			if (snake.isSnakeLeft() == false) {
				snake.setSnakeUp(false);
				snake.setSnakeLeft(false);
				snake.setSnakeDown(false);
				snake.setSnakeRight(true);
			}
		} else if (e.getKeyCode() == 27)
			snakeGame.setPause(true);
	}
	
	public void checkGameOverInputs(KeyEvent e) {
		if (e.getKeyCode() == 38) {
			if (snakeGame.getMenuSelection().equals("Exit game")) {
				snakeGame.setMenuSelection("Restart game");
				GUI.frame.repaint();
			}	
		} else if (e.getKeyCode() == 40) {
			if (snakeGame.getMenuSelection().equals("Restart game")) {
				snakeGame.setMenuSelection("Exit game");
				GUI.frame.repaint();
			}	
		} else if (e.getKeyCode() == 10) {
			switch(snakeGame.getMenuSelection()) {
				case "Restart game":
					snakeGame.restartGame();
					snakeGame.startGame(snakeGame);
					break;
				case "Exit game":
					System.exit(0);
					break;
			}
		}
	}
	
	public void checkStartMenuInputs(KeyEvent e) {
		if (e.getKeyCode() == 38) {
			if (snakeGame.getMenuSelection().equals("Exit game")) {
				snakeGame.setMenuSelection("Difficulty");
				GUI.frame.repaint();
			} else if (snakeGame.getMenuSelection().equals("Difficulty")) {
				snakeGame.setMenuSelection("Start game");
				GUI.frame.repaint();
			}	
		} else if (e.getKeyCode() == 40) {
			if (snakeGame.getMenuSelection().equals("Start game")) {
				snakeGame.setMenuSelection("Difficulty");
				GUI.frame.repaint();
			} else if (snakeGame.getMenuSelection().equals("Difficulty")) {
				snakeGame.setMenuSelection("Exit game");
				GUI.frame.repaint();
			}	
		} else if (e.getKeyCode() == 39) {
			if (snakeGame.getMenuSelection().equals("Difficulty") && snakeGame.getDifficultyLevel() != 2) {
				snakeGame.setDifficultyLevel(snakeGame.getDifficultyLevel() + 1);
				GUI.frame.repaint();
			}	
		} else if (e.getKeyCode() == 37) {
			if (snakeGame.getMenuSelection().equals("Difficulty") && snakeGame.getDifficultyLevel() != 0) {
				snakeGame.setDifficultyLevel(snakeGame.getDifficultyLevel() - 1);
				GUI.frame.repaint();
			}
		} else if (e.getKeyCode() == 10) {
			switch(snakeGame.getMenuSelection()) {
			case "Start game":
				snakeGame.startGame(snakeGame);
				break;
			case "Exit game":
				System.exit(0);
				break;
			}
		}
	}

	public void drawPauseMenu(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(470, 100, 600, 400);
		g2.setColor(Color.WHITE);
		g2.setFont(fontHeader);
		g2.drawString("Pause menu", 720, 170);
		g2.setFont(fontMenu);
		g2.drawString("Restart game", 720, 250);
		g2.drawString("Back to main menu", 720, 300);
		g2.drawString("Exit game", 720, 350);
		
		switch (snakeGame.getMenuSelection()) {
		  case "Restart game":
			g2.fillOval(690, 240, 10, 10);
			break; 
		  case "Back to main menu":
			g2.fillOval(690, 290, 10, 10);
			break; 
		  case "Exit game":
			g2.fillOval(690, 340, 10, 10);
			break; 	
		}	
	}
	
	public void drawCurrentGame(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 1251, 800);
		g2.setColor(Color.BLACK);
		g2.fillRect(1251, 0, 10, 800);
		g2.fillRect(0, 0, 10, 800);		
		g2.fillRect(0, 0, 1250, 10);
		g2.fillRect(0, 751, 1250, 10);

		for (int i = 1; i <= snake.getList().size(); i++) { 
			g2.fillRect(snake.getList().get(i - 1).x, snake.getList().get(i - 1).y, 10, 10);
		}
		
		if (food.isFoodPlaced() == true) {
			g2.setColor(Color.GREEN);
			g2.fillRect(food.getFoodX(), food.getFoodY(), 10, 10);
		}
		
		g2.setColor(Color.BLACK);
		g2.setFont(fontMenu);
		if (snakeGame.getMinutes() < 10 && snakeGame.getSeconds() < 10)
			g2.drawString("0" + snakeGame.getMinutes() + ":0" + snakeGame.getSeconds(), 1330, 30);
		else if (snakeGame.getMinutes() < 10 && snakeGame.getSeconds() > 9)
			g2.drawString("0" + snakeGame.getMinutes() + ":" + snakeGame.getSeconds(), 1330, 30);
		else if (snakeGame.getMinutes() > 9 && snakeGame.getSeconds() > 10)
			g2.drawString(snakeGame.getMinutes() + ":" + snakeGame.getSeconds(), 1330, 30);
		else if (snakeGame.getMinutes() > 9 && snakeGame.getSeconds() < 10)
			g2.drawString(snakeGame.getMinutes() + ":0" + snakeGame.getSeconds(), 1330, 30);
		g2.drawString("s: " + snakeGame.getScore(), 1405, 30);
		g2.drawString("m.s.l: " + snakeGame.getMaxScoreLeft(), 1470, 30);
	}
	
	public void drawBlinkAnimation1(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 1530, 800);
		g2.setColor(Color.BLACK);
		g2.setColor(Color.BLACK);
		g2.fillRect(1530, 0, 10, 800);
		g2.fillRect(0, 0, 10, 800);		
		g2.fillRect(0, 0, 1530, 10);
		g2.fillRect(0, 751, 1530, 10);
	
		for (int i = 1; i <= snake.getList().size(); i++) { 
			g2.fillRect(snake.getList().get(i - 1).x, snake.getList().get(i - 1).y, 10, 10);
		}
		
		if (food.isFoodPlaced() == true) {
			g2.setColor(Color.GREEN);
			g2.fillRect(food.getFoodX(), food.getFoodY(), 10, 10);
		}
	}
	
	public void drawBlinkAnimation2(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(1530, 0, 10, 800);
		g2.fillRect(0, 0, 10, 800);		
		g2.fillRect(0, 0, 1530, 10);
		g2.fillRect(0, 751, 1530, 10);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 1530, 800);
		g2.setColor(Color.BLACK);
		g2.fillRect(1530, 0, 10, 800);
		g2.fillRect(0, 0, 10, 800);		
		g2.fillRect(0, 0, 1530, 10);
		g2.fillRect(0, 751, 1530, 10);
	}
	
	public void drawGameOver(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(470, 100, 600, 400);
		g2.setColor(Color.WHITE);
		g2.setFont(fontHeader);
		g2.drawString("Game over! \n Your score: " + snakeGame.getScore(), 680, 170);
		g2.setFont(fontMenu);
		g2.drawString("Restart game", 710, 250);
		g2.drawString("Exit game", 710, 300);
		
		switch (snakeGame.getMenuSelection()) {
		  case "Restart game":
			g2.fillOval(680, 240, 10, 10);
			break; 
		  case "Exit game":
			g2.fillOval(680, 290, 10, 10);
			break;
		  case "Difficulty":
			  g2.fillOval(680, 330, 10, 10);
			  break;
		}
	}
	
	public void drawStartMenu(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(300, 100, 600, 400);
		g2.setColor(Color.WHITE);
		g2.setFont(fontHeader);
		g2.drawString("Snake Game", 550, 170);
		g2.setFont(fontMenu);
		g2.drawString("Start game", 410, 250);
		g2.drawString("Difficulty:", 410, 300);
		g2.drawString("Exit game", 410, 350);
		g2.drawString("Easy", 610, 300);
		g2.drawString("Normal", 664, 300);
		g2.drawString("Hard", 730, 300);

		switch (snakeGame.getMenuSelection()) {
		  case "Start game":
			g2.fillOval(370, 240, 10, 10);
			break; 
		  case "Difficulty":
			g2.fillOval(370, 290, 10, 10);
			break; 
		  case "Exit game":
			g2.fillOval(370, 340, 10, 10);
			break; 
		}
	
		switch(snakeGame.getDifficultyLevel()) {
			case 0:
				g2.drawRect(606, 288, 35, 16);
				break;
			case 1:
				g2.drawRect(659, 288, 50, 16);
				break;
			case 2:
				g2.drawRect(726, 288, 35, 16);
				break;
		}
	}

	public Game getSnakeGame() {
		return snakeGame;
	}

	public void setSnakeGame(Game snakeGame) {
		this.snakeGame = snakeGame;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	public Snake getSnake() {
		return snake;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}
}
