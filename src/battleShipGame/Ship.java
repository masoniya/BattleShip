package battleShipGame;


public class Ship {
	
	private int shipSize;
	private ShipDirection shipDirection = ShipDirection.horizontal;
	private int hp;
	private Player playerReference;
	
	Ship(int size, Player player)
	{
		shipSize = size;
		hp = shipSize;
		playerReference = player;
	}
	
	
	public int getSize()
	{
		return shipSize;
	}
	
	
	public ShipDirection getDirection()
	{
		if(shipDirection == ShipDirection.horizontal)
		{
			return ShipDirection.horizontal;
		}
		
		else
		{
			return ShipDirection.vertical;
		}
	}
	
	
	public void setDirection(ShipDirection direction)
	{
		shipDirection = direction;
	}
	
	
	public void switchDirection()
	{
		if(getDirection() == ShipDirection.horizontal)
		{
			shipDirection = ShipDirection.vertical;
		}
		else
		{
			shipDirection = ShipDirection.horizontal;
		}
		
	}
	
	
	public void takeDamage()
	{
		hp--;
		
		if(hp == 0)
		{
			if(Settings.consoleMode == true)
			{
				playerReference.message += playerReference.playerName;
				playerReference.message += " has lost a ship\n";
			}
			
			playerReference.loseShip();
		}
	}
	
	
}
