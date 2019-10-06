package utilities;

public enum Item 
{
	AIR(-1, -1),
	WOOD(0, 0),
	BERRIES(1, 0),
	VINES(2, 0),
	STICKS(3, 0),
	BOW(0, 1),
	WOOD_WALL(1, 1),
	STONES(2, 1),
	ARROWHEAD(3, 1),
	WOOD_SWORD(0, 2),
	HEALTH(1, 2),
	ARROW(2, 2);
	
	int tX, tY;
	
	private Item(final int tX, final int tY)
	{
		this.tX = tX;
		this.tY = tY;
	}
	
	public int getX() { return tX; }
	public int getY() { return tY; }
}
