package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images 
{
	public static BufferedImage tileSheet, playerSheet, itemSheet, slotSheet, enemy1Sheet, healthSheet, fightSheet, arrowSheet;
	
	public static BufferedImage[] player = new BufferedImage[12];
	public static BufferedImage[] redman = new BufferedImage[12];
	public static BufferedImage[] arrows = new BufferedImage[4];
	
	public Images()
	{
		try 
		{
			tileSheet = ImageIO.read(getClass().getResource("/textures/tilesheet.png"));
			playerSheet = ImageIO.read(getClass().getResource("/textures/playersheet.png"));
			itemSheet = ImageIO.read(getClass().getResource("/textures/itemsheet.png"));
			slotSheet = ImageIO.read(getClass().getResource("/textures/slotsheet.png"));
			enemy1Sheet = ImageIO.read(getClass().getResource("/textures/enemy1sheet.png"));
			healthSheet = ImageIO.read(getClass().getResource("/textures/healthsheet.png"));
			fightSheet = ImageIO.read(getClass().getResource("/textures/fightsheet.png"));
			arrowSheet = ImageIO.read(getClass().getResource("/textures/arrowsheet.png"));
			
			for(int i = 0; i < 12; i++)
			{
				player[i] = getSprite(playerSheet, i, 0, 16, 16);
			}
			for(int i = 0; i < 12; i++)
			{
				redman[i] = getSprite(enemy1Sheet, i, 0, 16, 16);
			}
			for(int i = 0; i < 4; i++)
			{
				arrows[i] = getSprite(arrowSheet, i, 0, 16, 16);
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getSprite(BufferedImage sheet, int x, int y, int width, int height)
	{
		return sheet.getSubimage(x * width, y * height, width, height);
	}
}
