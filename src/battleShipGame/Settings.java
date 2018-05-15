package battleShipGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Settings {
	
	public static int gridHeight;
	public static int gridWidth;
	public static List<Integer> shipSizes;
	public static Difficulty CPULevel;
	public static boolean cheat;
	public static boolean consoleMode;
	public static boolean threads;
	public static boolean timedGame;
	public static int turnTimer;
	public static boolean mines;
	
	public static void defaultSettings(){
		
		gridHeight = 10;
		gridWidth = 10;
		
		CPULevel = Difficulty.normal;
		
		shipSizes = new ArrayList<Integer>();
		shipSizes.clear();
		shipSizes.add(5);
		shipSizes.add(4);
		shipSizes.add(3);
		shipSizes.add(3);
		shipSizes.add(2);
		Collections.sort(shipSizes, Collections.reverseOrder());

		consoleMode = true;
		threads = true;
		timedGame = true;
		turnTimer = 10;
		mines = true;
		}
	
	
	
	public static void setGridHeight(int height)
	{
		//minimum height is 5 maximum is 10 and cannot be set smaller than biggest ship or number of ships
		if (height >= 5 && height <= 10 && height >= shipSizes.get(0) && height >= shipSizes.size())
			gridHeight = height;
	}
	
	
	public static void setGridWidth(int width)
	{
		//minimum width is 5 maximum is 10 and cannot be set smaller than biggest ship or number of ships
		if (width >= 5 && width <= 10 && width >= shipSizes.get(0) && width >= shipSizes.size())
			gridWidth = width;
	}
	
	//console mode only (not used yet)
	public static void addShip()
	{
		
		System.out.println("Input new ship size");
			
		shipSizes.add(Game.scan.nextInt());
		
		//sorting the arrayList by descending order 
		Collections.sort(shipSizes, Collections.reverseOrder());
			
	}
	
	
	public static void GUIaddShip(int i)
	{
		if(i <= gridWidth && i <= gridHeight && i > 0 && shipSizes.size() < gridWidth && shipSizes.size() < gridHeight)
		{
			shipSizes.add(i);
			Collections.sort(shipSizes, Collections.reverseOrder());
		}
			
	}
	
	//console mode only (not used yet)
	public static void removeShip()
	{
		System.out.println("Input index of ship to remove");
		
		int i = Game.scan.nextInt();
		shipSizes.remove(i);
		
	}
	
	
	public static void GUIremoveShip(int size)
	{
		
		for(int i = 0; i < shipSizes.size(); i++)
		{
			//cannot remove the last ship
			if(shipSizes.get(i) == size && shipSizes.size() > 1)
				{
					shipSizes.remove(i);
					break;
				}			
		}	
	}
	
	
	public static Difficulty getCPULevel() {
		return CPULevel;
	}

	public static void GUIsetCPULevel(String level) {
		
		if(level == "easy")
		{
			CPULevel = Difficulty.easy;
		}
		else if(level == "normal")
		{
			CPULevel = Difficulty.normal;
		}
		else
		{
			CPULevel = Difficulty.hard;
		}
		
	}
	
	public static void setTurnTimer(int i)
	{
		if(i >= 5 && i < 30)
		{
			turnTimer = i;
		}
		else
		{
			turnTimer = 10;
		}
	}
	
}



