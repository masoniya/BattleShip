package battleShipGUI;


import java.util.Random;

import battleShipGame.Coordinates;
import battleShipGame.HumanPlayer;
import battleShipGame.Mine;
import battleShipGame.Settings;
import battleShipGame.State;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class BattleScreen {
	
	public Button outputButtons[][];
	public Button inputButtons[][];
	
	public CheatWindow cheat = new CheatWindow();
	
	public int remainingSeconds = Settings.turnTimer;
	
	public boolean humanTurn = true;
	
	public Label timerLabel;
	public Label turnLabel;
	
	public Thread thread;
	

	public Scene apply()
	{
		if(Settings.threads == true)
		{
			thread = new Thread(new AIThread());
			thread.start();
			
			//this timeline takes care of both the input buttons and the timer running out
			Timeline refreshThread = new Timeline(new KeyFrame(
			        Duration.millis(50),
			        e -> {
			        	if(thread.isAlive() == false && humanTurn == false)
			        	{
			        		computerAttack();
			        		endTurn();
			        		thread = new Thread(new AIThread());
							thread.start();
			        	}
			        }));
			
			refreshThread.setCycleCount(Animation.INDEFINITE);
			refreshThread.play();
		}
		
		
		
		//this is the player grid on the left
		
		GridPane playerGrid = new GridPane();
		playerGrid.setHgap(5);
		playerGrid.setVgap(5);
		playerGrid.setPadding(new Insets(5, 5, 5, 5));
		playerGrid.setMinWidth(350);
		playerGrid.setMinHeight(350);
		playerGrid.setAlignment(Pos.CENTER);
		
		
		outputButtons = new Button[Settings.gridHeight][Settings.gridWidth];
		
		for(int i = 0; i < Settings.gridHeight; i++)
		{
			for(int j = 0; j < Settings.gridWidth; j++)
			{
				
				Button tempButton = new Button();
				outputButtons[i][j] = tempButton;
				outputButtons[i][j].setPrefWidth(25);
				outputButtons[i][j].setPrefHeight(25);
				
				if(GUIApplication.game.human.playerGrid.tileArray[i][j].TileState == State.ship)
				{
					outputButtons[i][j].setStyle("-fx-background-color: DarkSlateGray ");
				}
				else
				{
					outputButtons[i][j].setStyle("-fx-background-color: DarkTurquoise ");
				}
				
				playerGrid.add(outputButtons[i][j], j, i);
			}
		}
		
		
		
		//this is the computer grid on the right
		
		GridPane computerGrid = new GridPane();
		computerGrid.setHgap(5);
		computerGrid.setVgap(5);
		computerGrid.setPadding(new Insets(5, 5, 5, 5));
		computerGrid.setMinWidth(350);
		computerGrid.setMinHeight(350);
		computerGrid.setAlignment(Pos.CENTER);
		
		
		//the logic here determines what happens when a button on the computer grid is clicked
		inputButtons = new Button[Settings.gridHeight][Settings.gridWidth];
		
		for(int i = 0; i < Settings.gridHeight; i++)
		{
			for(int j = 0; j < Settings.gridWidth; j++)
			{
				int x = i;
				int y = j;
				
				Button tempButton = new Button();
				inputButtons[i][j] = tempButton;
				inputButtons[i][j].setPrefWidth(25);
				inputButtons[i][j].setPrefHeight(25);
				inputButtons[i][j].setStyle("-fx-background-color: DarkTurquoise ");
				//add buttons functionality here
				
				inputButtons[i][j].setOnAction(e -> {
					
					//this is the threads mode logic
					if(Settings.threads == true)
					{
						if(humanTurn == true)
						{
							humanAttack(x, y);
							endTurn();
						}
					}
					
					//this is executed in no threads mode
					//the computer will always finish their turn before the player gets to click again
					else
					{
						humanAttack(x, y);
						endTurn();
						
						computerAttack();
						endTurn();
					}
					
				});
				
				computerGrid.add(inputButtons[i][j], j, i);
			}
		}
		
		
		//activate the mines and move them around
		if(Settings.mines == true)
		{
			
			Mine humanMine = GUIApplication.game.human.playerMine;
			outputButtons[humanMine.location.x][humanMine.location.y].setStyle("-fx-background-color: Red ");
			
			Mine CPUMine = GUIApplication.game.CPU.playerMine;
			
			Timeline moveMines = new Timeline(new KeyFrame(
			        Duration.millis(100),
			        e -> {
			        	
			        	CPUMine.move();
			        	
			        	outputButtons[humanMine.location.x][humanMine.location.y].setStyle("-fx-background-color: DarkTurquoise ");
			        	humanMine.move();
			        	outputButtons[humanMine.location.x][humanMine.location.y].setStyle("-fx-background-color: Red ");
			        	
			        }));
			
			moveMines.setCycleCount(Animation.INDEFINITE);
			moveMines.play();
		}
		
		
		//this is the stuff on the left
		
		Label playerLabel = new Label("your grid");
		
		VBox leftPanel = new VBox(10);
		leftPanel.getChildren().addAll(playerLabel, playerGrid);
		leftPanel.setAlignment(Pos.CENTER);
		
		
		//this is the stuff on the right
		
		Label computerLabel = new Label("opponents grid");
		
		VBox rightPanel = new VBox(10);
		rightPanel.getChildren().addAll(computerLabel, computerGrid);
		rightPanel.setAlignment(Pos.CENTER);
		
		
		
		//this is the stuff in the middle
		
		//turn label
		turnLabel = new Label("human turn");
		
		
		//timer label
		timerLabel = new Label(Integer.toString(remainingSeconds));
		
		
		//only activate the timer if the game is timed
		//the logic here determines what the game does when the timer reaches 0
		if(Settings.timedGame == true)
		{
			//refresh the timer and the timer label
			Timeline refreshTimer = new Timeline(new KeyFrame(
			        Duration.millis(1000),
			        e -> {
			        	if(remainingSeconds == 0)
			        	{
			        		remainingSeconds = Settings.turnTimer;
			        		//what happens when timer reaches 0
			        		if(Settings.threads == true)
			        		{
			        			//timer runs out during human turn
			        			if(humanTurn == true)
			        			{
				        			endTurn();
			        			}
			        			
			        			//timer runs out during computer turn
			        			else
			        			{
			        				thread.interrupt();
				        			thread = new Thread(new AIThread());
				        			thread.start();
			        				endTurn();
			        			}
			        		}
			        		
			        		//if no threads the computer will never run out of time so this is only human
			        		else
				        	{
			        			//first end the human turn
			        			endTurn();
			        			
			        			//execute and end the computer turn
			        			computerAttack();
								endTurn();
				        	}
			        			
			        	}
			        	else
			        	{
			        		remainingSeconds--;
			        	}
			        	
			        	timerLabel.setText(Integer.toString(remainingSeconds));
			        }));
			refreshTimer.setCycleCount(Animation.INDEFINITE);
			refreshTimer.play();
		}
		
		
		
		//pass button
		//the logic here determines what the game does when the pass button is clicked
		Button passButton = new Button("pass");
		passButton.setOnAction(e -> {
			if(Settings.threads == true)
			{
				if(humanTurn == true)
				{
					endTurn();
				}
			}
			
			else
			{
				if(humanTurn == true)
				{
					//if the human passes their turn ends
					endTurn();
					
					//the computer plays immediately after
					computerAttack();
					endTurn();
				}
			}
		});	
		
		
		//cheat button
		Button cheatButton = new Button("cheat");
		cheatButton.setOnAction(e -> {
			
			//only one cheat window can be active at a time
			cheat.window.close();
			
			cheat.display();
		});
		
		
		
		//give up button
		Button giveUpButton = new Button("give up");
		giveUpButton.setOnAction(e -> {
			cheat.window.close();
			GameOverScreen.winner = ("CPU");
			GameOverScreen gameOver = new GameOverScreen();
			Scene tempScene = gameOver.apply();
			MainMenu.window.setScene(tempScene);	
		});
		
		
		VBox middlePanel = new VBox(30);
		middlePanel.setMinWidth(200);

		
		middlePanel.getChildren().addAll(turnLabel, timerLabel, passButton, cheatButton, giveUpButton);
		middlePanel.setAlignment(Pos.CENTER);
		
		
		
		
		//this is the final layout of the window
		HBox layout = new HBox(30);
		layout.getChildren().addAll(leftPanel, middlePanel, rightPanel);
		layout.setAlignment(Pos.CENTER);
		
			
		
		
		Scene scene = new Scene(layout, 1000, 600);
		
		return scene;
	}
	
	
	//this method colors the pressed buttons according to the result of the attack and disables them after
	private void updateComputerGrid(){
		
		if(GUIApplication.game.human.getPreviousAttackResult() == State.hit)
		{
			inputButtons[GUIApplication.game.human.getPreviousAttackCoordinates().x]
						[GUIApplication.game.human.getPreviousAttackCoordinates().y].setStyle("-fx-background-color: LimeGreen ");
			inputButtons[GUIApplication.game.human.getPreviousAttackCoordinates().x]
						[GUIApplication.game.human.getPreviousAttackCoordinates().y].setDisable(true);
		}
		else
		{
			inputButtons[GUIApplication.game.human.getPreviousAttackCoordinates().x]
						[GUIApplication.game.human.getPreviousAttackCoordinates().y].setStyle("-fx-background-color: Maroon ");
			inputButtons[GUIApplication.game.human.getPreviousAttackCoordinates().x]
					[GUIApplication.game.human.getPreviousAttackCoordinates().y].setDisable(true);
		}
	
	}
	
	private void updateHumanGrid(){
		
		if(GUIApplication.game.CPU.getPreviousAttackResult() == State.hit)
		{
			outputButtons[GUIApplication.game.CPU.getPreviousAttackCoordinates().x]
					   	[GUIApplication.game.CPU.getPreviousAttackCoordinates().y].setStyle("-fx-background-color: LimeGreen ");
		}
		else
		{
			outputButtons[GUIApplication.game.CPU.getPreviousAttackCoordinates().x]
						[GUIApplication.game.CPU.getPreviousAttackCoordinates().y].setStyle("-fx-background-color: Maroon ");
		}
		
	}
	
	
	private void endTurn()
	{
		
		if(humanTurn == true)
		{
			humanTurn = false;
			remainingSeconds = Settings.turnTimer;
			timerLabel.setText(Integer.toString(remainingSeconds));
			turnLabel.setText("computer is thinking");
		}
		
		else
		{
			humanTurn = true;
			remainingSeconds = Settings.turnTimer;
			timerLabel.setText(Integer.toString(remainingSeconds));
			turnLabel.setText("human turn");
		}
		
	}
	
	
	private void humanAttack(int x, int y)
	{
		//human attack
		HumanPlayer.GUIInput = new Coordinates(x, y);
		GUIApplication.game.processPlayerInput(GUIApplication.game.human);
		
		if(GUIApplication.game.CPU.getDefeat() == true)
		{
			GameOverScreen.winner = ("human");
			GameOverScreen gameOver = new GameOverScreen();
			Scene tempScene = gameOver.apply();
			cheat.window.close();
			MainMenu.window.setScene(tempScene);
		}
		
		updateComputerGrid();
		
		//human attack finished
	}
	
	
	private void computerAttack()
	{
		//computer attack
		if(Settings.threads == false)
		{
			GUIApplication.game.processPlayerInput(GUIApplication.game.CPU);
		}
		
		
		if(GUIApplication.game.human.getDefeat() == true)
		{
			GameOverScreen.winner = ("CPU");
			GameOverScreen gameOver = new GameOverScreen();
			Scene tempScene = gameOver.apply();
			cheat.window.close();
			MainMenu.window.setScene(tempScene);
		}
		
		updateHumanGrid();
		
		//computer attack finished
	}
	
	
	class AIThread implements Runnable
	{
		Random randomizer = new Random();
		public void run()
		{
			try {
				Thread.sleep(randomizer.nextInt(Settings.turnTimer) + 5);
			} catch (InterruptedException e) {
				return;
			}
			//if the thread is not interrupted this will execute
			GUIApplication.game.processPlayerInput(GUIApplication.game.CPU);
		}
	}
	
	
}


