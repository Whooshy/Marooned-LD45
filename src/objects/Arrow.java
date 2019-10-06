package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import engine.Engine;
import utilities.Images;

public class Arrow 
{
	public float x, y, velX, velY;
	public int direction, lifetime;
	
	public Arrow(float x, float y, int direction)
	{
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		lifetime = 1000;
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(Images.arrows[direction], (int) x, (int) y, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
		
		if(direction == 0)
			velY = -5;
		if(direction == 1)
			velX = 5;
		if(direction == 2)
			velX = -5;
		if(direction == 3)
			velY = 5;
		
		lifetime--;
		
		x += velX;
		y += velY;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16));
	}
}
