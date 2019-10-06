package objects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import utilities.Images;
import utilities.UtilColor;

public class Material 
{
	public int id, tX, tY;
	public float lightReflectivity;
	
	public boolean isSolid;
	
	public Color color = null;
	
	private BufferedImage tex;
	
	public Material(int id, int tX, int tY, float lightRelectivity, boolean isSolid)
	{
		this.id = id;
		this.tX = tX;
		this.tY = tY;
		this.lightReflectivity = lightRelectivity;
		this.isSolid = isSolid;
		
		tex = Images.getSprite(Images.tileSheet, tX, tY, 16, 16);
	}
	
	public Material(int id, int tX, int tY, float lightRelectivity, boolean isSolid, Color color)
	{
		this.id = id;
		this.tX = tX;
		this.tY = tY;
		this.lightReflectivity = lightRelectivity;
		this.isSolid = isSolid;
		this.color = color;
		
		tex = Images.getSprite(Images.tileSheet, tX, tY, 16, 16);
		
		for(int x = 0; x < tex.getWidth(); x++)
		for(int y = 0; y < tex.getHeight(); y++)
		{
			UtilColor c = new UtilColor((float) color.getRed() / 256, (float) color.getGreen() / 256, (float) color.getBlue() / 256);
			c.multiply(new UtilColor(tex.getRGB(x, y)));
			tex.setRGB(x, y, c.getColor().getRGB());
		}
	}
	
	public BufferedImage getRawTexture()
	{
		return Images.getSprite(Images.tileSheet, tX, tY, 16, 16);
	}
	
	public BufferedImage getTexture()
	{
		return tex;
	}
}
