package battleShipGUI;

import battleShipGame.Difficulty;
import battleShipGame.Settings;
import battleShipGame.InputManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SettingsWindow {
	
	
	ShipInputBox addShip = new ShipInputBox();
	ShipRemoveBox removeShip = new ShipRemoveBox();
	
	Stage window;
	
	public void display()
	{
		
		window = new Stage();
		window.setMinWidth(300);
		window.setMinHeight(400);
		window.setTitle("Settings");
		window.initModality(Modality.APPLICATION_MODAL);
		
		
		
		
		//the area where we input rows and columns
		
		Label rowsLabel = new Label("rows");
		Label columnsLabel = new Label("columns");
		
		TextField rowsInput = new TextField();
		rowsInput.setPromptText(Integer.toString(Settings.gridHeight));
		
		TextField columnsInput = new TextField();
		columnsInput.setPromptText(Integer.toString(Settings.gridWidth));
		
		AnchorPane rowAndColumnInputLayout = new AnchorPane();
		
		AnchorPane.setTopAnchor(rowsLabel, 10.0);
		AnchorPane.setLeftAnchor(rowsLabel, 10.0);
		
		AnchorPane.setTopAnchor(rowsInput, 10.0);
		AnchorPane.setLeftAnchor(rowsInput, 70.0);
		
		AnchorPane.setTopAnchor(columnsLabel, 40.0);
		AnchorPane.setLeftAnchor(columnsLabel, 10.0);
		
		AnchorPane.setTopAnchor(columnsInput, 40.0);
		AnchorPane.setLeftAnchor(columnsInput, 70.0);
		
		rowAndColumnInputLayout.getChildren().addAll(rowsLabel, columnsLabel, rowsInput, columnsInput);
		
		
		
		

		//this is the area where we input ships and remove ships and see current ships
		
		Menu shipMenu = new Menu("ships");
		for(int i = 0; i < Settings.shipSizes.size(); i++)
		{
			shipMenu.getItems().add(new MenuItem(Integer.toString(Settings.shipSizes.get(i))));
		}
		
		//a menu item has to be in a menu bar
		MenuBar bar = new MenuBar();
		bar.getMenus().add(shipMenu);
		
		Button addShipButton = new Button("Add Ship");
		addShipButton.setOnAction(e -> addShip.display());
		
		Button removeShipButton = new Button("Remove Ship");
		removeShipButton.setOnAction(e -> removeShip.display());
		
		HBox shipInputLayout = new HBox(10);
		shipInputLayout.getChildren().addAll(bar, addShipButton, removeShipButton);

		
		//this is the area where we choose the difficulty
		Label computerDifficultyLabel = new Label("Computer Difficulty");
		
		RadioButton easy = new RadioButton("easy");
		RadioButton normal = new RadioButton("normal");
		RadioButton hard = new RadioButton("hard");
		
		if(Settings.CPULevel == Difficulty.easy)
		{
			easy.setSelected(true);
		}
		else if(Settings.CPULevel == Difficulty.normal)
		{
			normal.setSelected(true);
		}
		else
		{
			hard.setSelected(true);
		}
		
		ToggleGroup difficulty = new ToggleGroup();
		easy.setToggleGroup(difficulty);
		normal.setToggleGroup(difficulty);
		hard.setToggleGroup(difficulty);
		
		HBox difficultyButtons = new HBox(10);
		difficultyButtons.getChildren().addAll(easy, normal, hard);
		
		VBox difficultyInputLayout = new VBox(10);
		difficultyInputLayout.getChildren().addAll(computerDifficultyLabel, difficultyButtons);
		
		
		//threads mines and timed check boxes and turnTimer text field
		
		//turn timer input field
		TextField turnTimerInput = new TextField();
		turnTimerInput.setPromptText(Integer.toString(Settings.turnTimer));
		if(Settings.timedGame == false)
		{
			turnTimerInput.setDisable(true);
		}
		
		
		//threads button
		CheckBox threadsBox = new CheckBox("threads");
		if(Settings.threads == true)
		{
			threadsBox.setSelected(true);
		}
		threadsBox.setOnAction(e -> {
			Settings.threads = !Settings.threads;
		});
		
		
		//mines button
		CheckBox minesBox = new CheckBox("mines");
		if(Settings.mines == true)
		{
			minesBox.setSelected(true);
		}
		minesBox.setOnAction(e -> {
			Settings.mines = !Settings.mines;
		});
		
		//timed button
		CheckBox timedGameBox = new CheckBox("timed game");
		if(Settings.timedGame == true)
		{
			timedGameBox.setSelected(true);
		}
		timedGameBox.setOnAction(e -> {
			Settings.timedGame = !Settings.timedGame;
			if(turnTimerInput.isDisable() == true)
			{
				turnTimerInput.setDisable(false);
			}
			else
			{
				turnTimerInput.setDisable(true);
			}
		});
		
		
		HBox topButtonsLayout = new HBox(10);
		topButtonsLayout.getChildren().addAll(threadsBox, minesBox, timedGameBox ,turnTimerInput);
		
		
		
		//back OK and default buttons
		
		//back button
		Button backButton = new Button("back");
		backButton.setOnAction(e -> window.close());
		
		//OK button
		Button okButton = new Button("ok");
		okButton.setOnAction(e -> {
			if(InputManager.isType(rowsInput.getText(), "int"))
			{
				Settings.setGridHeight(Integer.parseInt(rowsInput.getText()));
			}
			if(InputManager.isType(columnsInput.getText(), "int"))
			{
				Settings.setGridWidth(Integer.parseInt(columnsInput.getText()));
			}
			
			if(easy.isSelected())
			{
				Settings.GUIsetCPULevel("easy");
			}
			else if(normal.isSelected())
			{
				Settings.GUIsetCPULevel("normal");
			}
			else
			{
				Settings.GUIsetCPULevel("hard");
			}
			
			if(InputManager.isType(turnTimerInput.getText(), "int"))
			{
				Settings.setTurnTimer(Integer.parseInt(turnTimerInput.getText()));
			}
			
			window.close();
		});
		
		//default button
		Button defaultButton = new Button("restore dafault");
		defaultButton.setOnAction(e -> {
			Settings.defaultSettings();
			window.close();
		});
		
		
		HBox bottomButtonsLayout = new HBox(10);
		bottomButtonsLayout.getChildren().addAll(backButton, okButton, defaultButton);
		
		
		
		
		
		//the final layout of the window
		VBox layout = new VBox(40);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setAlignment(Pos.TOP_CENTER);
		layout.getChildren().addAll(rowAndColumnInputLayout, shipInputLayout, difficultyInputLayout, topButtonsLayout, bottomButtonsLayout);
		
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}
	
	
}
