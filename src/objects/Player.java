package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import engine.Bitmap;
import engine.Engine;
import engine.World;
import utilities.Images;

public class Player 
{
	public float x, y, velX, velY, camX, camY;
	public int frame, direction, cooldown, attackCooldown, attackFrame;

	public boolean isMovingEast, isMovingWest, isMovingNorth, isMovingSouth, isFighting;
	public boolean isCollidingEast1, isCollidingWest1, isCollidingNorth1, isCollidingSouth1, isCollidingEast2, isCollidingWest2, isCollidingNorth2, isCollidingSouth2;
	
	public float speed = 2.5f;
	
	public Player(float x, float y)
	{
		attackCooldown = 0;
		this.x = x;
		this.y = y;
		
		velX = 0;
		velY = 0;
		camX = 0;
		camY = 0;
	}
	
	public void update(Graphics2D g, World world, Inventory inv)
	{
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
		
		if(isFighting && attackFrame == -30)
		{
			attackFrame = 20;
		}
		
		if(attackFrame > -30)
		{
			attackFrame--;
			if((direction == 0 || direction == 6) && attackFrame >= 0)
			{
				g.drawImage(Images.getSprite(Images.fightSheet, attackFrame / 5, 0, 16, 16), (int) x, (int) (y - (Engine.zoom * 12)), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			}
			if((direction == 1 || direction == 4) && attackFrame >= 0)
			{
				g.drawImage(Images.getSprite(Images.fightSheet, attackFrame / 5, 1, 16, 16), (int) (x + (Engine.zoom * 12)), (int) y, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			}
			if((direction == 2 || direction == 5) && attackFrame >= 0)
			{
				g.drawImage(Images.getSprite(Images.fightSheet, attackFrame / 5, 3, 16, 16), (int) (x - (Engine.zoom * 12)), (int) y, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			}
		}
		
		if(cooldown > 0)
			cooldown--;
		
		frame++;
		if(frame >= 40)
			frame = 0;
		
		if(direction < 4)
		{
			if((cooldown > 0 && cooldown % 2 == 0) || cooldown == 0)
				g.drawImage(Images.player[direction], (int) x, (int) y, (int) (16 * Engine.zoom), (int) (16 * Engine.zoom), null);
		}
		else
		{
			int curFrame = (direction - 4) * 2 + (frame / 20) + 4;
			
			if((cooldown > 0 && cooldown % 2 == 0) || cooldown == 0)
				g.drawImage(Images.player[curFrame], (int) x, (int) y, (int) (16 * Engine.zoom), (int) (16 * Engine.zoom), null);
		}
		
		if(attackFrame > -30)
		{
			if((direction == 3 || direction == 7) && attackFrame >= 0)
			{
				g.drawImage(Images.getSprite(Images.fightSheet, attackFrame / 5, 2, 16, 16), (int) x, (int) (y + (Engine.zoom * 12)), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			}
		}
		
		if(isFighting && attackFrame >= 0)
		{
			for(int i = 0; i < world.redmen.size(); i++)
			{
				if(world.redmen.get(i).getBounds().intersects(getLeftHitBounds()) && (direction == 2 || direction == 5) && world.redmen.get(i).hpCooldown == 0)
				{
					world.redmen.get(i).hit(1);
					Engine.playSound("/audio/hit.wav");
				}
				if(world.redmen.get(i).getBounds().intersects(getRightHitBounds()) && (direction == 1 || direction == 4) && world.redmen.get(i).hpCooldown == 0)
				{
					world.redmen.get(i).hit(1);
					Engine.playSound("/audio/hit.wav");
				}
				if(world.redmen.get(i).getBounds().intersects(getTopHitBounds()) && (direction == 0 || direction == 6) && world.redmen.get(i).hpCooldown == 0)
				{
					world.redmen.get(i).hit(1);
					Engine.playSound("/audio/hit.wav");
				}
				if(world.redmen.get(i).getBounds().intersects(getBottomHitBounds()) && (direction == 3 || direction == 7) && world.redmen.get(i).hpCooldown == 0)
				{
					world.redmen.get(i).hit(1);
					Engine.playSound("/audio/hit.wav");
				}
			}
		}
		
		for(int i = 0; i < world.redmen.size(); i++)
		{
			if(world.redmen.get(i).getBounds().intersects(getBounds()))
			{
				if(cooldown == 0) hit(inv);
			}
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
	
	public void hit(Inventory inv)
	{
		cooldown = 100;
		Engine.playSound("/audio/hit.wav");
		inv.health--;
		if(inv.health == 0) die();
	}
	
	public void die()
	{
		Engine.STATE = 2;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) (x + (Engine.zoom * 3)), (int) (y + (Engine.zoom * 3)), (int) (10 * Engine.zoom), (int) (10 * Engine.zoom));
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
	
	public Rectangle getTopHitBounds()
	{
		return new Rectangle((int) x, (int) (y - (Engine.zoom * 12)) - 16, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16) + 16);
	}
	
	public Rectangle getBottomHitBounds()
	{
		return new Rectangle((int) x, (int) (y + (Engine.zoom * 12)), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16) + 32);
	}
	
	public Rectangle getLeftHitBounds()
	{
		return new Rectangle((int) (x + (Engine.zoom * (12 - 20))), (int) y, (int) (Engine.zoom * (16 + 20)), (int) (Engine.zoom * 16));
	}
	
	public Rectangle getRightHitBounds()
	{
		return new Rectangle((int) (x - (Engine.zoom * 12)), (int) y, (int) (Engine.zoom * (16 + 32)), (int) (Engine.zoom * 16));
	}
}
