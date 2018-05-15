package battleShipGUI;


import battleShipGame.Game;
import battleShipGame.Settings;

import javafx.application.Application;
import javafx.stage.Stage;


public class GUIApplication extends Application {
	
	
	//call the game constructor
	public static Game game = new Game();
		
	
	//the main menu window
	public static MainMenu mainMenu = new MainMenu();
	
	
	//this the entry point of the GUI mode game
	public static void main(String args[])
	{
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//set game settings to default
		Settings.defaultSettings();
		
		Settings.consoleMode = false;
		
		//display the main menu
		mainMenu.display();	
		
	}
	
}
