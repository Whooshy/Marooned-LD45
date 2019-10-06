package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import engine.Engine;
import utilities.Images;
import utilities.Tile;

public class Particle 
{
	public float x, y, velX, velY;
	public Tile id;
	
	public float deX, deY, dirX, dirY;
	
	public int rX, rY, lifetime;
	public Random r;
	
	BufferedImage tex;
	
	public Particle(float x, float y, Tile id, BufferedImage sheet)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		
		r = new Random();
		
		rX = r.nextInt(8);
		rY = r.nextInt(8);
		
		deX = (float) (r.nextInt(10) + 2) / 240 + 1;
		deY = (float) (r.nextInt(10) + 2) / 240 + 1;
		
		velX = (float) Math.sin(r.nextInt(360));
		velY = (float) Math.sin(r.nextInt(360));
		
		tex = Images.getSprite(sheet, id.getTX(), id.getTY(), 16, 16).getSubimage(rX * 2, rY * 2, 2, 2);
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(tex, (int) x, (int) y, (int) Engine.zoom * 2, (int) Engine.zoom * 2, null);
		
		x += velX;
		y += velY;
		
		velX /= deX;
		velY /= deY;
		
		lifetime++;
	}
}
