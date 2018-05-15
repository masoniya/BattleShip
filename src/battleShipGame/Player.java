package battleShipGame;

import java.util.ArrayList;
import java.util.Random;

public abstract class Player {
	
	public Grid playerGrid;
	
	public State[][] draft = new State[Settings.gridHeight][Settings.gridWidth];
	
	public Ship shipArray[];
	
	private int remainingShips;
	
	protected Coordinates nextCoordinates;
	
	protected boolean nextDirectionSwitch;
	
	private boolean defeat;
	
	private State previousAttackResult;
	
	private Coordinates previousAttackCoordinates;
	
	public String playerName;
	
	public String message;
	
	public Mine playerMine;
	
	
	Player()
	{
		playerGrid = new Grid(this);
		
		
		for(int i = 0; i < Settings.gridHeight; i++)
		{
			for(int j = 0; j < Settings.gridWidth; j++)
			{
				draft[i][j] = State.empty;
			}
		}
		
		
		shipArray = new Ship[Settings.shipSizes.size()];
		for(int i=0;i<Settings.shipSizes.size();i++)
		{
			shipArray[i] = new Ship(Settings.shipSizes.get(i), this);
		}
		
		
		remainingShips = shipArray.length;
		
		nextCoordinates = new Coordinates(0, 0);
		
		nextDirectionSwitch = false;
		
		defeat = false;
		
		previousAttackResult = State.miss;
		
		previousAttackCoordinates = new Coordinates(0, 0);
		
		message = "";
		
		playerMine = new Mine(new Coordinates(0, 0), playerGrid);
		
	}
	
	
	public void placeMine()
	{
		Random randomizer = new Random();
		int x, y;
		
		do
		{
			x = randomizer.nextInt(Settings.gridHeight);
			y = randomizer.nextInt(Settings.gridWidth);
		}
		while(playerGrid.tileArray[x][y].TileState != State.empty);
		
		playerGrid.tileArray[x][y].TileState = State.mine;
		
		playerMine = new Mine(new Coordinates(x, y), playerGrid);
		
	}
	
	
	public abstract boolean placeShip(int index);
	

	public abstract void placeAllShips();
	
	
	public abstract Coordinates SelectAttackTarget();
	
	
	public State AcceptAttack(Coordinates xy)
	{
		State result = playerGrid.ProcessAttack(xy);
		
		return result;
	}
	
	
	public void reportMine(Coordinates xy, ArrayList<MineReport> enemyMineReport)
	{
		int x = 0, y = 0;
		for(int i = 0; i < enemyMineReport.size(); i++)
		{
			x = enemyMineReport.get(i).tileCoordinates.x;
			y = enemyMineReport.get(i).tileCoordinates.y;
			
			draft[x][y] = enemyMineReport.get(i).tileState;
		}
		
		previousAttackResult = State.mine;
		previousAttackCoordinates = new Coordinates(xy.x, xy.y);
	}
	
	
	public void report(Coordinates xy, State report)
	{
		if(report != State.mine)
		{
			draft[xy.x][xy.y] = report;
			previousAttackResult = report;
			previousAttackCoordinates = new Coordinates(xy.x, xy.y);
		}
		else
		{
			
		}
		
		
		if(Settings.consoleMode == true)
		{
			message += playerName;
			message += " attacked coordinates " + previousAttackCoordinates.x + " " + previousAttackCoordinates.y;
			message += " and the the attack result is " + previousAttackResult.toString() + "\n";
		}
	}
	
	
	public void loseShip()
	{
		remainingShips--;
		
		if(remainingShips == 0)
		{
			if(Settings.consoleMode == true)
			{
				message += (" " + playerName + " has been defeated\n");
			}
			
			defeat = true;
		}
		
	}
	
	
	public boolean getDefeat()
	{
		return defeat;
	}
	
	
	public State getPreviousAttackResult()
	{
		return previousAttackResult;
	}
	
	
	public Coordinates getPreviousAttackCoordinates()
	{
		return previousAttackCoordinates;
	}
	
	//console mode only
	public void displayGrid()
	{
		playerGrid.display();
	}
	
	//console mode only
	void displayDraft()
	{
		System.out.printf("%n%n**************************************************%n%n");
		
		for (int i = 0; i < Settings.gridHeight; i++){
			for(int j = 0; j < Settings.gridWidth ; j++){
				switch(draft[i][j]){
				
				case empty : System.out.print('.');
				break;
				
				case ship : System.out.print('S');
				break;
				
				case hit : System.out.print('H');
					break;
					
				case miss : System.out.print('M');
					break;
					
				case mine: System.out.print('*');
					
				default:
					break;
				
				}
			}
			System.out.print("\n");
		}
		
		System.out.printf("%n**************************************************%n%n");
	}
	
	//console mode only
	public String getMessage()
	{
		String temp = message;
		
		clearMessage();
		
		return temp;
	}
	
	//console mode only
	private void clearMessage()
	{
		message = "";
	}
	
}


