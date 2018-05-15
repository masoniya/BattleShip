package battleShipGUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class GameOverScreen {
	
	public static String winner = "human";
	
	public Scene apply()
	{
		
		Label finalMessage = new Label(winner + " is the winner");		
		
		//exit button
		Button ExitButton = new Button("exit");
		ExitButton.setOnAction(e -> {
			
			MainMenu.window.close();
			
		});
		
		//play again button
		Button playAgainButton = new Button("play again");
		playAgainButton.setOnAction(e -> {
			
			MainMenu.window.close();
			
			/*Settings.defaultSettings();*/
			
			GUIApplication.mainMenu.display();
			
		});
		
		//this is the final layout of the window
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(finalMessage, playAgainButton, ExitButton);
		
		
		Scene scene = new Scene(layout, 250, 250);

		return scene;
	
	}

}
