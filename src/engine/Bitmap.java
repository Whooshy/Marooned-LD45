package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import objects.Material;

public class Bitmap 
{
	public BufferedImage bitmap;
	public Random random;
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 320;
	
	public static float camX, camY;
	
	public float[][] vis;
	
	public Bitmap()
	{
		bitmap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		vis = new float[WIDTH][HEIGHT];
		
		random = new Random();
		
		for(int x = 0; x < WIDTH; x++)
		for(int y = 0; y < HEIGHT; y++)
		{
			vis[x][y] = 1;
		}
	}
	
	public void clear()
	{
		for(int x = 0; x < WIDTH; x++)
		for(int y = 0; y < HEIGHT; y++)
		{
			bitmap.setRGB(x, y, 0);
		}
	}
	
	public void setPixel(int x, int y, Color color)
	{
		if(x - (int) camX < 0 || y - (int) camY < 0 || x - (int) camX >= WIDTH || y - (int) camY >= HEIGHT);
		else bitmap.setRGB(x - (int) camX, y - (int) camY, new Color(color.getRed() * vis[x][y], color.getGreen() * vis[x][y], color.getBlue() * vis[x][y]).getRGB());
	}
	
	public void setPixel(int x, int y, int color)
	{
		Color c = new Color(color);
		if(x - (int) camX < 0 || y - (int) camY < 0 || x - (int) camX >= WIDTH || y - (int) camY >= HEIGHT);
		else bitmap.setRGB(x - (int) camX, y - (int) camY, c.getRGB());
	}
	
	public void drawTexture(int x, int y, BufferedImage tex)
	{
		for(int i = 0; i < tex.getWidth(); i++)
		for(int j = 0; j < tex.getHeight(); j++)
		{
			int curPixel = tex.getRGB(i, j);
			int alpha = (curPixel >> 24) & 0xff;
			if(alpha != 0) setPixel(x + i, y + j, tex.getRGB(i, j));
		}
	}
	
	public float getTrueWidth(int width, int height)
	{
		float trueWidth = 0;
		if(width < height * 1.5f)
		{
			trueWidth = (float) height * 1.25f;
		}
		else
		{
			trueWidth = (float) height * 1.5f;
		}
		
		return trueWidth;
	}
	
	public void update(Graphics2D g, int width, int height)
	{	
		float trueWidth = getTrueWidth(width, height);
		float x = (float) width / 2 - trueWidth / 2;
		
		g.drawImage(bitmap, (int) x, 0, (int) trueWidth, height, null);
	}
}
