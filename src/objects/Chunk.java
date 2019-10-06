package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import engine.Engine;
import engine.World;
import utilities.Images;
import utilities.Tile;

public class Chunk 
{	
	public int x, y;
	
	public Tile[][] tiles;
	
	public BufferedImage tex;
	public Random random;
	
	public Chunk(int x, int y, int seed)
	{
		this.x = x;
		this.y = y;
		
		tiles = new Tile[16][16];
		random = new Random();
		
		for(int a = 0; a < 16; a++)
		for(int b = 0; b < 16; b++)
		{
			float dist = (float) Math.sqrt(Math.pow((x * 16 + a) - 80, 2) + Math.pow((y * 16 + b) - 56, 2));
			if(World.getValue(x * 16 + a, y * 16 + b, seed, 5) > dist / 70)
			{
				float diff = World.getValue(x * 16 + a, y * 16 + b, seed, 5) - dist / 70;
				if(diff > 0.1f)
				{
					float val = World.getValue(x * 16 + a, y * 16 + b, seed + 1, 3);
					if(val > 0.6f && b != 15 && diff > 0.2f)
						tiles[a][b] = Tile.TREE_TOP;
					else 
						tiles[a][b] = Tile.GRASS;
				}
				else tiles[a][b] = Tile.SAND;
			}
			else tiles[a][b] = Tile.WATER;
		}
		
		for(int a = 0; a < 16; a++)
		for(int b = 0; b < 16; b++)
		{
			if(b != 0 && tiles[a][b - 1] == Tile.TREE_TOP && tiles[a][b] == Tile.GRASS)
			{
				tiles[a][b] = Tile.TREE_BOTTOM;
			}
			
			if(random.nextInt(10) == 0 && tiles[a][b] == Tile.GRASS)
			{
				tiles[a][b] = Tile.FLOWERS;
			}
			
			if(random.nextInt(20) == 0 && tiles[a][b] == Tile.GRASS)
			{
				tiles[a][b] = Tile.BUSH;
			}
			
			if(random.nextInt(30) == 0 && tiles[a][b] == Tile.GRASS)
			{
				tiles[a][b] = Tile.BOULDER;
			}
		}
		
		tex = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		build();
	}
	
	public void build()
	{
		for(int x = 0; x < 16; x++)
		for(int y = 0; y < 16; y++)
		{
			for(int a = 0; a < 16; a++)
			for(int b = 0; b < 16; b++)
			{
				int color = Images.getSprite(Images.tileSheet, tiles[x][y].getTX(), tiles[x][y].getTY(), 16, 16).getRGB(a, b);
				tex.setRGB(x * 16 + a, y * 16 + b, color);
			}
		}
	}
	
	public void debug(Graphics2D g)
	{
		for(int x = 0; x < 16; x++)
		for(int y = 0; y < 16; y++)
		{
			g.setColor(Color.RED);
			g.draw(getTileBounds(x, y));
		}
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(tex, x * (int) (256 * Engine.zoom), y * (int) (256 * Engine.zoom), (int) (256 * Engine.zoom), (int) (256 * Engine.zoom), null);
	}
	
	public Rectangle getTileBounds(int a, int b)
	{
		return new Rectangle((int) (x * 256 * Engine.zoom + (a * 16 * Engine.zoom)), (int) (y * 256 * Engine.zoom + (b * 16 * Engine.zoom)), 16 * (int) Engine.zoom, 16 * (int) Engine.zoom);
	}
	
	public Rectangle getChunkBounds(int a, int b)
	{
		return new Rectangle((int) (x * 256 * Engine.zoom), (int) (y * 256 * Engine.zoom), (int) (256 * Engine.zoom), (int) (256 * Engine.zoom));
	}
}
