package org.java.snake.game;

import java.awt.Rectangle;

import org.java.snake.App;
import org.java.snake.model.Food;
import org.java.snake.model.Snake;

public class Game implements Runnable {
	private boolean started = false;
	private boolean finishedBlink1 = false;
	private boolean finishedBlink2 = false;
	private boolean finished = false;														
	private int score = 0;
	private int maxScoreLeft = 0;
	private int seconds = 0;
	private int minutes = 0;
	private boolean pause = false;
	private boolean restart = false;
	private boolean startMenu = true;
	private int difficultyLevel = 0;
	private int difficultyLevelTh;
	
	private String menuSelection = "Start game";
	private Snake snake;
	private Game snakeGame;
	private Food food;

	public void startGame(Game snakeGame) {										
		started = true;
		Thread t = new Thread(snakeGame);
		menuSelection = "Restart game";

		switch (snakeGame.getDifficultyLevel()) {
			case 0:
				snakeGame.setDifficultyLevelTh(130);
				break;
			case 1:
				snakeGame.setDifficultyLevelTh(100);
				break;
			case 2:
				snakeGame.setDifficultyLevelTh(80);
				break;		
		}
		t.start();
	}
	
	public void restartGame() {
		snake.setSnakeX(100);
		snake.setSnakeY(100);
		snake.setSnakeLeft(false);
		snake.setSnakeUp(false);
		snake.setSnakeDown(false);
		snake.setSnakeRight(true);
		snakeGame.setFinished(false);
		snakeGame.setScore(0);
		snakeGame.setSeconds(0);
		snakeGame.setMinutes(0);
		snakeGame.setMaxScoreLeft(100);
		snakeGame.setPause(false);
		snake.getList().clear();
		snake.getList().add(new Rectangle(snake.getSnakeX(), snake.getSnakeY(), 10, 10));
	}


	public void finishGame() {															
		started = false;
		for (int i = 0; i < 4; i++) {			
			finishedBlink1 = true;
			GUI.frame.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finishedBlink1 = false;
			finishedBlink2 = true;
			GUI.frame.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finishedBlink2 = false;
		}
	
		finishedBlink1 = false;
		finishedBlink2 = false;
		finished = true;
		menuSelection = "Restart game";											
		GUI.frame.repaint();	
	}
	
	public void run() {	
		snake = new Snake();
		food = new Food();
		food.setFood(food);
		App.gui.setFood(food);							
		App.gui.setSnake(snake);
		
		snake.getList().add(new Rectangle(snake.getSnakeX(), snake.getSnakeY(), 10, 10));

		long startTime = System.currentTimeMillis();
		long currentTime = 0;
		while (snakeGame.started == true) {
				if (snakeGame.isPause() == false) {
					collisionWall();
					placeFood();
					if (snakeGame.getMaxScoreLeft() > 0) {
						snakeGame.setMaxScoreLeft(snakeGame.getMaxScoreLeft() - 1);
					}
					
					collisionFood();
					collisionSnake();
					snake.getList().add(new Rectangle(snake.getSnakeX(), snake.getSnakeY(), 10, 10));
					snake.getList().remove(0);
					
					currentTime = System.currentTimeMillis();
					if (currentTime - startTime >= 901) {						
						if (snakeGame.getSeconds() < 59) {
							snakeGame.setSeconds(snakeGame.getSeconds() + 1);
						}else if (snakeGame.getSeconds() >= 59) {
							snakeGame.setSeconds(0);
							snakeGame.setMinutes(snakeGame.getMinutes() + 1);
						}	
						startTime += currentTime - startTime + 100;
					}
				}
			GUI.frame.repaint();

			try {
				Thread.sleep(snakeGame.getDifficultyLevelTh());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void collisionWall() {
		if (snake.isSnakeUp() == true) {
			if (snake.getSnakeY() < 20)
				finishGame();
			else
				snake.setSnakeY(snake.getSnakeY() - 15);
		} else if (snake.isSnakeDown() == true) {
			if (snake.getSnakeY() > 730)
				finishGame();
			else
				snake.setSnakeY(snake.getSnakeY() + 15);
		} else if (snake.isSnakeLeft() == true) {
			if (snake.getSnakeX() < 20) 
				finishGame();
			else
				snake.setSnakeX(snake.getSnakeX() - 15);
		} else if (snake.isSnakeRight() == true) {
			if (snake.getSnakeX() > 1249)
				finishGame();
			else
				snake.setSnakeX(snake.getSnakeX() + 15);
		}
	}
	
	public void collisionSnake() {
		for (int i = 1; i < snake.getList().size() - 1; i++) {
			if (i + 1 < snake.getList().size()) {
				if (snake.getList().get(0).intersects(snake.getList().get(i + 1)))
					finishGame();
			}
		}
	}
	
	public void collisionFood() {
		if (Math.abs(food.getFoodX() - snake.getSnakeX()) <= 8 && Math.abs(food.getFoodY() - snake.getSnakeY()) <= 8) {
			food.setFoodPlaced(false);
			snake.getList().add(new Rectangle(snake.getSnakeX(), snake.getSnakeY(), 10, 10));
			snakeGame.score += snakeGame.getMaxScoreLeft();
		}
	}
	
	public void placeFood() {
		if(food.isFoodPlaced() == false) {
			food.setFoodX((int) (35 + Math.random() * 1235));
			food.setFoodY((int) (35 + Math.random() * 710));
			food.setFoodPlaced(true);
			snakeGame.maxScoreLeft = 100;
		}
	}
	
	public Snake getSnake() {
		return snake;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Game getSnakeGame() {
		return snakeGame;
	}

	public void setSnakeGame(Game snakeGame) {
		this.snakeGame = snakeGame;
	}

	public boolean isFinishedBlink1() {
		return finishedBlink1;
	}

	public void setFinishedBlink1(boolean finishedBlink1) {
		this.finishedBlink1 = finishedBlink1;
	}

	public boolean isFinishedBlink2() {
		return finishedBlink2;
	}

	public void setFinishedBlink2(boolean finishedBlink2) {
		this.finishedBlink2 = finishedBlink2;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public String getMenuSelection() {
		return menuSelection;
	}

	public void setMenuSelection(String menuSelection) {
		this.menuSelection = menuSelection;
	}

	public int getMaxScoreLeft() {
		return maxScoreLeft;
	}

	public void setMaxScoreLeft(int maxScoreLeft) {
		this.maxScoreLeft = maxScoreLeft;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public boolean isStartmenu() {
		return startMenu;
	}

	public void setStartmenu(boolean startmenu) {
		this.startMenu = startmenu;
	}

	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public int getDifficultyLevelTh() {
		return difficultyLevelTh;
	}

	public void setDifficultyLevelTh(int dificultyLevelTh) {
		this.difficultyLevelTh = dificultyLevelTh;
	}
}
