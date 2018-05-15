package battleShipGame;

public class MineReport {
	
	public State tileState;
	public Coordinates tileCoordinates;
	
	public MineReport(int x, int y, State state)
	{
		tileState = state;
		tileCoordinates = new Coordinates(x, y);
	}

}
