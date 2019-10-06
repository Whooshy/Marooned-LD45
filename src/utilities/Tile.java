package utilities;

public enum Tile 
{
	GRASS(0, 0, false, 0),
	WATER(1, 1, true, 0),
	SAND(0, 1, false, 0),
	FLOWERS(1, 0, false, 0),
	BUSH(2, 0, true, 3),
	TREE_BOTTOM(3, 1, false, 0),
	TREE_TOP(3, 0, true, 5),
	BOULDER(2, 1, true, 8),
	WOOD_WALL(0, 2, true, 6),
	WOOD_WALL_EXPOSED(1, 2, true, 6);
	
	int tX, tY, hardness;
	boolean isSolid;
	
	private Tile(final int tX, final int tY, final boolean isSolid, final int hardness)
	{
		this.tX = tX;
		this.tY = tY;
		this.hardness = hardness;
		this.isSolid = isSolid;
	}
	
	public int getTX() { return tX; }
	public int getTY() { return tY; }
	
	public int getHardness() { return hardness; }
	public boolean isSolid() { return isSolid; }
}
