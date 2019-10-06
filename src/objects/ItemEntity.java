package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import engine.Engine;
import utilities.Images;
import utilities.Item;

public class ItemEntity
{
	public float x, y, velX, velY;
	public Item id;
	
	public float deX, deY, dirX, dirY;
	
	public int rX, rY;
	public Random r;
	
	BufferedImage tex;
	
	public ItemEntity(float x, float y, Item id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		
		r = new Random();
		
		deX = (float) (r.nextInt(10) + 2) / 120 + 1;
		deY = (float) (r.nextInt(10) + 2) / 120 + 1;
		
		velX = (float) Math.sin(r.nextInt(360));
		velY = (float) Math.sin(r.nextInt(360));
		
		tex = Images.getSprite(Images.itemSheet, id.getX(), id.getY(), 16, 16);
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(tex, (int) x, (int) y, (int) Engine.zoom * 8, (int) Engine.zoom * 8, null);
		
		x += velX;
		y += velY;
		
		velX /= deX;
		velY /= deY;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, (int) Engine.zoom * 8, (int) Engine.zoom * 8);
	}
}
