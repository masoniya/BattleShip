package battleShipGUI;

import battleShipGame.ComputerPlayer;
import battleShipGame.Coordinates;
import battleShipGame.ShipDirection;
import battleShipGame.HumanPlayer;
import battleShipGame.Settings;
import battleShipGame.Ship;
import battleShipGame.State;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;


public class ShipPlacementScreen {
	
	public static int currentShipIndex = 0;
	
	//array of buttons to add to the buttons grid
	public Button inputButtons[][];
	
	
	public Scene apply()
	{
		//computer places their ships immediately
		GUIApplication.game.CPU = new ComputerPlayer();
		GUIApplication.game.CPU.placeAllShips();
		
		if(Settings.mines)
		{
			GUIApplication.game.CPU.placeMine();
		}
		
		GUIApplication.game.human = new HumanPlayer();
		
		Label insertMessage = new Label("insert your ships");
		
		//set initial value for this label
		Label currentDirection = new Label(GUIApplication.game.human.shipArray[currentShipIndex].getDirection().toString());
		
		
		//switch button
		Button switchButton = new Button("Switch Direction");
		switchButton.setOnAction(e -> {
			if(HumanPlayer.GUISwitch == ShipDirection.horizontal)
			{
				HumanPlayer.GUISwitch = ShipDirection.vertical;
			}
			else
			{
				HumanPlayer.GUISwitch = ShipDirection.horizontal;
			}
			
			currentDirection.setText(HumanPlayer.GUISwitch.toString());
		});
		
		
		//next button
		Button nextButton = new Button("Begin Battle");
		nextButton.setOnAction(e -> {
			currentShipIndex = 0;
			BattleScreen gameStart = new BattleScreen();
			Scene tempScene = gameStart.apply();
			MainMenu.window.setScene(tempScene);
		});
		nextButton.setDisable(true);
		
		
		
		//the grid of buttons to place the ships
		GridPane gridLayout = new GridPane();
		gridLayout.setHgap(5);
		gridLayout.setVgap(5);
	    gridLayout.setPadding(new Insets(5, 5, 5, 5));
	    gridLayout.setMinWidth(400);
		gridLayout.setMinHeight(400);
		gridLayout.setAlignment(Pos.CENTER);

		
		this.inputButtons = new Button[Settings.gridHeight][Settings.gridWidth];
		
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
				inputButtons[i][j].setStyle("-fx-background-color: DarkTurquoise  ;"); 
				
				//adding functionality to the buttons in the grid
				
				inputButtons[i][j].setOnMouseEntered(e->{
					
					if(currentShipIndex<Settings.shipSizes.size())
					highlightShip(x,y,GUIApplication.game.human.shipArray[currentShipIndex]);
					
				});
				
				inputButtons[i][j].setOnMouseExited(e->{
					
					if(currentShipIndex<Settings.shipSizes.size())
					unhighlightShip(x,y,GUIApplication.game.human.shipArray[currentShipIndex]);
										
				});
				
				
				inputButtons[i][j].setOnAction(e -> {
					
					if(currentShipIndex < Settings.shipSizes.size())
					{
						HumanPlayer.GUIInput = new Coordinates(x, y);
						if(GUIApplication.game.human.placeShip(currentShipIndex))
						{
							colorShip(GUIApplication.game.human.shipArray[currentShipIndex]);
							currentShipIndex++;
							if(currentShipIndex == Settings.shipSizes.size())
							{
								
								nextButton.setDisable(false);
								switchButton.setDisable(true);
								
								if(Settings.mines == true)
								{
									GUIApplication.game.human.placeMine();
								}
							}
										
						}
					}
				});
				
				gridLayout.add(inputButtons[i][j], j, i);
			}
		}
		
		
		
		
		VBox labelAndButton = new VBox(10);
		labelAndButton.getChildren().addAll(switchButton, currentDirection);
		labelAndButton.setAlignment(Pos.CENTER);
		
		
		HBox middlePart = new HBox(10);
		middlePart.getChildren().addAll(labelAndButton, gridLayout);
		middlePart.setAlignment(Pos.CENTER);
		
		
		//final layout for this window
		VBox layout = new VBox(10);
		layout.getChildren().addAll(insertMessage, middlePart, nextButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 700, 500);
		
		return scene;
	}
	
	
	//give placed ships a different color
	private void colorShip(Ship ship)
	{
		
		if(ship.getDirection() == ShipDirection.vertical)
		{
			for(int i = HumanPlayer.GUIInput.x; i < HumanPlayer.GUIInput.x + ship.getSize(); i++)
			{
				inputButtons[i][HumanPlayer.GUIInput.y].setStyle("-fx-background-color: DarkSlateGray ");
			}
		}
		else
		{
			for(int j = HumanPlayer.GUIInput.y; j < HumanPlayer.GUIInput.y + ship.getSize(); j++)
			{
				inputButtons[HumanPlayer.GUIInput.x][j].setStyle("-fx-background-color: DarkSlateGray ");
			}
		}
	}
	
	//color potential ship position when button is hovered over
	private void highlightShip(int x , int y , Ship ship)
	{
		if(HumanPlayer.GUISwitch == ShipDirection.vertical)
		{
			for(int i = x; i < x + ship.getSize(); i++)
			{
				if(i>=Settings.gridHeight)
				{
					break;
				}
				if(GUIApplication.game.human.playerGrid.tileArray[i][y].TileState==State.empty)
				{
					inputButtons[i][y].setStyle("-fx-background-color: DimGrey ");
				}
			}
		}
		else
		{
			for(int j = y; j < y + ship.getSize(); j++)
			{
				if(j>=Settings.gridWidth)
				{
					break;
				}
				
				if(GUIApplication.game.human.playerGrid.tileArray[x][j].TileState==State.empty)
				{
					inputButtons[x][j].setStyle("-fx-background-color: DimGrey ");
				}
			}
		}
	}
			
	//return original color of button when not hovered over anymore
	private void unhighlightShip(int x , int y , Ship ship)
	{
		if(HumanPlayer.GUISwitch == ShipDirection.vertical)
		{
			for(int i = x; i < x + ship.getSize(); i++)
			{
				if(i>=Settings.gridHeight)
				{
					break;
				}
				if(GUIApplication.game.human.playerGrid.tileArray[i][y].TileState==State.empty)
				{
					inputButtons[i][y].setStyle("-fx-background-color: DarkTurquoise ");
				}
			}
		}
		else
		{
			for(int j = y; j < y + ship.getSize(); j++)
			{
				if(j>=Settings.gridWidth)
				{
					break;
				}
				
				if(GUIApplication.game.human.playerGrid.tileArray[x][j].TileState==State.empty)
				{
					inputButtons[x][j].setStyle("-fx-background-color: DarkTurquoise ");
				}
			}
		}
	}
	
	
	
}
