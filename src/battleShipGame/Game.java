package battleShipGame;

import java.util.Scanner;



public class Game {
	
	public Player CPU;
	public Player human;
	
	private boolean gameOver;
	private Player winningPlayer;
	
	public static Scanner scan;
	
	
	
	public Game()
	{
		gameOver = false;
	}
	
	
	//take input from one player and pass it to the other player to accept then return the result to the attacker
	public void processPlayerInput(Player currentPlayer)
	{
		Player Attacker, Defender;
		if(currentPlayer.equals(human))
		{
			Attacker = human;
			Defender = CPU;
		}
		else
		{
			Attacker = CPU;
			Defender = human;
		}
		
		Coordinates xy = Attacker.SelectAttackTarget();
		State result = Defender.AcceptAttack(xy);
		if(result == State.mine)
		{
			Attacker.reportMine(xy, Defender.playerMine.report);
		}
		else
		{
			Attacker.report(xy, result);
		}
		
		//end the game if the defender loses after the attack
		if(Defender.getDefeat() == true)
		{
			winningPlayer = Attacker;
			gameOver = true;
		}
	}
	
	//console mode only
	public void runDefault()
	{
		System.out.println("Starting BattleShip game\n");
		
		Settings.defaultSettings();
		System.out.println("Default settings have been set\n");
		
		run();
	}
	
	//can be called from GUI to play with modified settings
	public void run()
	{
		Settings.consoleMode = true;
		scan = new Scanner(System.in);
		
		initialize();
		
		gameLoop();
		
		endGame();
		
	}
	
	//console mode only
	private void initialize()
	{
		
		
		System.out.println("Initializing Players\n");
		
		initPlayers();
		
		//CPU is the default winner
		winningPlayer = CPU;
		
		System.out.println("All Players have been initialized\n");
		
		if(Settings.mines == true)
		{
			System.out.println("Initializing Mines\n");
			
			human.placeMine();
			CPU.placeMine();
			
			System.out.println("Player Mines have been Initialized\n");
		}
		
	}
	
	
	//console mode only
	private void gameLoop()
	{
		System.out.println("Game Started\n");
		
		while(gameOver != true)
		{
			processInput();
			
			outputGame();
			
		}
	}
	
	
	//console mode only
	private void endGame()
	{
		
		System.out.println("Game Over\n");
		System.out.println(winningPlayer.playerName + " is the winner!\n");
		
		//closes System.in with it and it cannot be opened again
		//only call this at the very end of the program
		scan.close();
				
		//the game ends here
		
	}
	
	
	//console mode only
	private void initPlayers()
	{
		CPU = new ComputerPlayer();
		CPU.playerName = ("CPU");
		
		
		human = new HumanPlayer();
		human.playerName = ("human");
		
		System.out.println("Initializing " + CPU.playerName);
		
		CPU.placeAllShips();
		
		System.out.println(CPU.playerName + " has been initialized\n");
		
		System.out.println("Initializing " + human.playerName);
		
		human.placeAllShips();
		
		System.out.println(human.playerName + " human has been initialized\n");
	}
	
	
	//console mode only
	public void processInput()
	{
		processPlayerInput(human);
		
		//skip computers turn if the game is over
		if(gameOver == true)
		{
			return;
		}
		
		processPlayerInput(CPU);
	}
	
	
	//console mode only
	private void outputGame()
	{
		
		human.displayGrid();
		human.displayDraft();
		
		CPU.playerMine.move();
		human.playerMine.move();
		
		System.out.println(human.getMessage());
		System.out.println(CPU.getMessage());
		
	}
	
	
}
