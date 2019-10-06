package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import engine.Engine;
import engine.World;
import utilities.Images;

public class Redman 
{
	public float x, y, velX, velY, camX, camY;
	public int frame, direction, health, hpCooldown;

	public boolean isMovingEast, isMovingWest, isMovingNorth, isMovingSouth;
	public boolean isCollidingEast1, isCollidingWest1, isCollidingNorth1, isCollidingSouth1, isCollidingEast2, isCollidingWest2, isCollidingNorth2, isCollidingSouth2;
	
	public float speed = 1.f;
	
	public Redman(float x, float y)
	{
		health = 10;
		hpCooldown = 0;
		this.x = x;
		this.y = y;
	}
	
	public void update(Graphics2D g, World world, Player p)
	{
		if(p.x >= x)
			isMovingWest = true;
		else
			isMovingWest = false;
		
		if(p.x < x)
			isMovingEast = true;
		else
			isMovingEast = false;
		
		if(p.y <= y)
			isMovingNorth = true;
		else
			isMovingNorth = false;
		
		if(p.y > y)
			isMovingSouth = true;
		else
			isMovingSouth = false;
		
		if(isMovingEast && !isCollidingEast1 && !isCollidingEast2)
		{
			velX = -speed;
			direction = 5;
		}
		else if(isMovingWest && !isCollidingWest1 && !isCollidingWest2)
		{
			velX = speed;
			direction = 4;
		}
		else
		{
			velX = 0;
		}
		
		if(isMovingNorth && !isCollidingNorth1 && !isCollidingNorth2)
		{
			velY = -speed;
			direction = 6;
		}
		else if(isMovingSouth && !isCollidingSouth1 && !isCollidingSouth2)
		{
			velY = speed;
			direction = 7;
		}
		else
		{
			velY = 0;
		}
		
		if(hpCooldown > 0)
			hpCooldown--;
		
		frame++;
		if(frame >= 40)
			frame = 0;
		
		if(direction < 4)
		{
			if(hpCooldown == 0 || (hpCooldown > 0 && hpCooldown % 2 == 0)) 
				g.drawImage(Images.redman[direction], (int) x, (int) y, (int) (16 * Engine.zoom), (int) (16 * Engine.zoom), null);
		}
		else
		{
			int curFrame = (direction - 4) * 2 + (frame / 20) + 4;
			if(hpCooldown == 0 || (hpCooldown > 0 && hpCooldown % 2 == 0)) 
				g.drawImage(Images.redman[curFrame], (int) x, (int) y, (int) (16 * Engine.zoom), (int) (16 * Engine.zoom), null);
		}
		
		camX = x - Engine.width / 2 + 16;
		camY = y - Engine.height / 2 + 16;
		
		x += velX;
		y += velY;
		
		collision(g, world);
	}
	
	public void collision(Graphics2D g, World world)
	{	
		for(int x = 0; x < 10; x++)
		for(int y = 0; y < 7; y++)
		{
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getLeftBounds1()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getLeftBounds1()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingEast1 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getLeftBounds1()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingEast1 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getRightBounds1()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getRightBounds1()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingWest1 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getRightBounds1()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingWest1 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getTopBounds1()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getTopBounds1()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingNorth1 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getTopBounds1()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingNorth1 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getBottomBounds1()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getBottomBounds1()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingSouth1 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getBottomBounds1()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingSouth1 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getLeftBounds2()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getLeftBounds2()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingEast2 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getLeftBounds2()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingEast2 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getRightBounds2()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getRightBounds2()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingWest2 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getRightBounds2()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingWest2 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getTopBounds2()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getTopBounds2()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingNorth2 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getTopBounds2()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingNorth2 = false;
					}
				}
			}
			
			if(world.chunks[x][y].getChunkBounds(x, y).intersects(getBottomBounds2()))
			{
				for(int a = 0; a < 16; a++)
				for(int b = 0; b < 16; b++)
				{
					if(world.chunks[x][y].getTileBounds(a, b).intersects(getBottomBounds2()) && world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingSouth2 = true;
					}
					else if(world.chunks[x][y].getTileBounds(a, b).intersects(getBottomBounds2()) && !world.chunks[x][y].tiles[a][b].isSolid())
					{
						isCollidingSouth2 = false;
					}
				}
			}
		}
	}
	
	public void hit(int damage)
	{
		health -= damage;
		
		x -= (velX * 5);
		y -= (velY * 5);
		
		hpCooldown = 10;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, (int) (16 * Engine.zoom), (int) (16 * Engine.zoom));
	}
	
	public Rectangle getTopBounds1()
	{
		return new Rectangle((int) (x + 2 * Engine.zoom), (int) y, 8, 4);
	}
	
	public Rectangle getBottomBounds1()
	{
		return new Rectangle((int) (x + 2 * Engine.zoom), (int) (y + 15 * Engine.zoom), 8, 4);
	}
	
	public Rectangle getLeftBounds1()
	{
		return new Rectangle((int) x, (int) (y + 2 * Engine.zoom), 4, 8);
	}
	
	public Rectangle getRightBounds1()
	{
		return new Rectangle((int) (x + 15 * Engine.zoom), (int) (y + 2 * Engine.zoom), 4, 8);
	}
	
	public Rectangle getTopBounds2()
	{
		return new Rectangle((int) (x + 13 * Engine.zoom), (int) y, 8, 4);
	}
	
	public Rectangle getBottomBounds2()
	{
		return new Rectangle((int) (x + 13 * Engine.zoom), (int) (y + 15 * Engine.zoom), 8, 4);
	}
	
	public Rectangle getLeftBounds2()
	{
		return new Rectangle((int) x, (int) (y + 13 * Engine.zoom), 4, 8);
	}
	
	public Rectangle getRightBounds2()
	{
		return new Rectangle((int) (x + 15 * Engine.zoom), (int) (y + 12 * Engine.zoom), 4, 8);
	}
}
