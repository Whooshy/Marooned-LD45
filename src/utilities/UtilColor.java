package utilities;

import java.awt.Color;

public class UtilColor 
{
	public float r, g, b;
	
	public UtilColor(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public UtilColor(int rgb)
	{
		Color color = new Color(rgb);
		
		r = (float) color.getRed() / 256;
		g = (float) color.getGreen() / 256;
		b = (float) color.getBlue() / 256;
	}
	
	public void multiply(UtilColor c)
	{
		r *= c.r;
		g *= c.g;
		b *= c.b;
	}
	
	public Color getColor()
	{
		System.out.println(r * 256 + ", " + g * 256 + ", " + b * 256);
		return new Color((int) (r * 256), (int) (g * 256), (int) (b * 256));
	}
}
