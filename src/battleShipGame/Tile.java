package battleShipGame;


public class Tile{
	
	Ship shipReference;
	
	public State TileState ;
	
	Tile(Ship ship, State state)
	{
		this.shipReference = ship;
		this.TileState = state;
	}
	
	
}
