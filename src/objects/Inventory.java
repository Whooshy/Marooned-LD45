package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import engine.Engine;
import engine.World;
import utilities.Images;
import utilities.Item;
import utilities.Tile;

public class Inventory
{
	public Item[] slotID = new Item[8];
	public int[] slotAmnt = new int[8];
	public int[] slotSelected = new int[8];
	
	public int curSelection, health;
	public boolean toggleSound = true;
	
	public Inventory()
	{
		curSelection = 0;
		health = 6;
		
		for(int i = 0; i < 8; i++)
		{
			slotID[i] = Item.AIR;
			slotAmnt[i] = 0;
			slotSelected[i] = 0;
		}
	}
	
	public void update(Graphics2D g, World world)
	{
		for(int i = 0; i < 8; i++)
		{
			if(curSelection == i) g.drawImage(Images.getSprite(Images.slotSheet, 1, 0, 16, 16), (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)), (int) (Engine.height - Engine.zoom * 28), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			else if(slotSelected[i] == 1) g.drawImage(Images.getSprite(Images.slotSheet, 2, 0, 16, 16), (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)), (int) (Engine.height - Engine.zoom * 28), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			else if(slotSelected[i] == 2) g.drawImage(Images.getSprite(Images.slotSheet, 3, 0, 16, 16), (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)), (int) (Engine.height - Engine.zoom * 28), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			else g.drawImage(Images.getSprite(Images.slotSheet, 0, 0, 16, 16), (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)), (int) (Engine.height - Engine.zoom * 28), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			if(slotID[i] != Item.AIR)
			{
				g.drawImage(Images.getSprite(Images.itemSheet, slotID[i].getX(), slotID[i].getY(), 16, 16), (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)), (int) (Engine.height - Engine.zoom * 28), (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
				
				g.setFont(new Font("Courier", Font.BOLD, 30));
				g.setColor(Color.BLACK);
				g.drawString("" + slotAmnt[i], (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)) + 34, (int) (Engine.height - Engine.zoom * 28) + 66);
				g.setColor(Color.WHITE);
				g.drawString("" + slotAmnt[i], (int) (Engine.width - (Engine.zoom * 20 * 8 + 16) + (i * Engine.zoom * 20)) + 32, (int) (Engine.height - Engine.zoom * 28) + 64);
			}
		}
		
		if(health < 0) health = 0;
		
		for(int i = 0; i < 3; i++)
		{
			if(health >= 2 * (i + 1))
				g.drawImage(Images.getSprite(Images.healthSheet, 0, 0, 16, 16), (int) (20 + i * (Engine.zoom * 18)), Engine.height - 110, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			else if(health >= 2 * (i + 1) - 1)
				g.drawImage(Images.getSprite(Images.healthSheet, 1, 0, 16, 16), (int) (20 + i * (Engine.zoom * 18)), Engine.height - 110, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
			else 
				g.drawImage(Images.getSprite(Images.healthSheet, 2, 0, 16, 16), (int) (20 + i * (Engine.zoom * 18)), Engine.height - 110, (int) (Engine.zoom * 16), (int) (Engine.zoom * 16), null);
		}
		
		if(curSelection > 7) curSelection -= 8;
		if(curSelection < -1) curSelection += 8;
		
		g.setFont(new Font("Courier", Font.BOLD, 24));
		
		g.setColor(Color.BLACK);
		g.drawString("Wave: " + world.wave, 12, 36);
		g.setColor(Color.WHITE);
		g.drawString("Wave: " + world.wave, 10, 34);
		
		g.setColor(Color.BLACK);
		g.drawString("Time: " + world.waveTimer, 12, 66);
		g.setColor(Color.WHITE);
		g.drawString("Time: " + world.waveTimer, 10, 64);
	}
	
	public void use(World world, Player player)
	{
		for(int i = 0; i < 8; i++)
		{
			if(curSelection == i)
			{
				if(slotID[i] == Item.WOOD_WALL)
				{
					int tX = (int) (Engine.mouseX / (16 * Engine.zoom));
					int tY = (int) (Engine.mouseY / (16 * Engine.zoom));
					
					int cX = tX / 16;
					int cY = tY / 16;
					
					int pX = tX % 16;
					int pY = tY % 16;
					
					if(!world.chunks[cX][cY].tiles[pX][pY].isSolid())
					{
						world.chunks[cX][cY].tiles[pX][pY] = Tile.WOOD_WALL;
					
	 					if(pY != 15 && world.chunks[cX][cY].tiles[pX][pY + 1] != Tile.WOOD_WALL)
							world.chunks[cX][cY].tiles[pX][pY] = Tile.WOOD_WALL_EXPOSED;
						
						if(pY != 0 && world.chunks[cX][cY].tiles[pY][pY - 1] != Tile.WOOD_WALL_EXPOSED)
							world.chunks[cX][cY].tiles[pX][pY] = Tile.WOOD_WALL;
						
						remove(Item.WOOD_WALL, 1);
						world.chunks[cX][cY].build();
					}
				}
				if(slotID[i] == Item.WOOD_SWORD)
				{
					player.isFighting = true;
				}
				if(slotID[i] == Item.BERRIES)
				{
					if(health < 6)
					{
						health++;
						remove(Item.BERRIES, 1);
					}
				}
				if(slotID[i] == Item.BOW)
				{
					if(checkFor(Item.ARROW, 1))
					{
						remove(Item.ARROW, 1);
						if(player.direction == 0 || player.direction == 6)
							world.arrows.add(new Arrow(player.x, player.y - (Engine.zoom * 16), 0));
						if(player.direction == 1 || player.direction == 4)
							world.arrows.add(new Arrow(player.x + (Engine.zoom * 16), player.y, 1));
						if(player.direction == 2 || player.direction == 5)
							world.arrows.add(new Arrow(player.x - (Engine.zoom * 16), player.y, 2));
						if(player.direction == 3 || player.direction == 7)
							world.arrows.add(new Arrow(player.x, player.y + (Engine.zoom * 16), 3));
					}
				}
			}
		}
	}
	
	public void craft()
	{
		for(int i = 0; i < 8; i++)
		{
			if(slotID[i] == Item.WOOD && slotSelected[i] == 1)
			{
				for(int j = 0; j < 8; j++) 
				{
					if(slotID[j] == Item.STICKS && slotSelected[j] == 1)
					{
						for(int k = 0; k < 8; k++)
						{
							if(slotID[k] == Item.VINES && slotSelected[k] == 1)
							{
								remove(Item.WOOD, 1);
								remove(Item.VINES, 1);
								remove(Item.STICKS, 1);
								
								add(Item.WOOD_SWORD, 1);
								Engine.playSound("/audio/confirm.wav");
								return;
							}
						}
					}
				}
				
				Engine.playSound("/audio/confirm.wav");
				add(Item.STICKS, 2);
				remove(Item.WOOD, 1);
				return;
			}
			if(slotID[i] == Item.WOOD && slotAmnt[i] > 1 && slotSelected[i] == 2)
			{
				for(int j = 0; j < 8; j++)
				{
					if(j == i) continue;
					if(slotID[j] == Item.VINES && slotAmnt[j] > 1 && slotSelected[i] == 2)
					{
						remove(Item.WOOD, 2);
						remove(Item.VINES, 2);
						add(Item.WOOD_WALL, 1);
						Engine.playSound("/audio/confirm.wav");
						return;
					}
				}
			}
			if(slotID[i] == Item.STICKS && slotAmnt[i] > 1 && slotSelected[i] == 2)
			{
				for(int j = 0; j < 8; j++)
				{
					if(slotID[j] == Item.VINES && slotAmnt[j] >= 1 && slotSelected[j] == 1)
					{
						Engine.playSound("/audio/confirm.wav");
						remove(Item.STICKS, 2);
						remove(Item.VINES, 1);
						add(Item.BOW, 1);
						return;
					}
				}
			}
			if(slotID[i] == Item.ARROWHEAD && slotAmnt[i] >= 1 && slotSelected[i] == 1)
			{
				for(int j = 0; j < 8; j++)
				{
					if(slotID[j] == Item.VINES && slotAmnt[j] >= 1 && slotSelected[j] == 1)
					{
						for(int k = 0; k < 8; k++)
						{
							if(slotID[k] == Item.STICKS && slotAmnt[k] >= 1 && slotSelected[k] == 1)
							{
								remove(Item.ARROWHEAD, 1);
								remove(Item.VINES, 1);
								remove(Item.STICKS, 1);
								
								Engine.playSound("/audio/confirm.wav");
								add(Item.ARROW, 3);
								return;
							}
						}
					}
				}
			}
			if(slotID[i] == Item.STONES && slotAmnt[i] > 1 && slotSelected[i] == 2)
			{
				Engine.playSound("/audio/confirm.wav");
				remove(Item.STONES, 2);
				add(Item.ARROWHEAD, 1);
				return;
			}
		}
		for(int i = 0; i < 8; i++)
		{
			if(slotSelected[i] >= 1)
			{
				slotSelected[i] = 0;
			}
		}
		
		Engine.playSound("/audio/denial.wav");
	}
	
	public void add(Item id, int amount)
	{
		if(id == Item.HEALTH) 
		{
			if(health < 6) health++;
			return;
		}
		for(int i = 0; i < 8; i++)
		{
			if(id == slotID[i] && slotAmnt[i] + amount <= 20)
			{
				slotAmnt[i] += amount;
				return;
			}
		}
		
		for(int i = 0; i < 8; i++)
		{
			if(id == slotID[i] && slotAmnt[i] + amount > 20)
			{
				slotAmnt[i] = 20;
				int newAmnt = slotAmnt[i] + amount - 20;
				for(int j = 0; j < 8; j++)
				{
					if(slotID[j] == Item.AIR)
					{
						slotID[j] = id;
						slotAmnt[j] = newAmnt;
						return;
					}
				}
			}
		}
		
		for(int i = 0; i < 8; i++)
		{
			if(slotID[i] == Item.AIR)
			{
				slotID[i] = id;
				slotAmnt[i] = amount;
				return;
			}
		}
	}
	
	public void remove(Item id, int amount)
	{
		if(checkFor(id, amount))
		{
			for(int i = 0; i < 8; i++)
			{
				if(id == slotID[i] && slotAmnt[i] >= amount)
					slotAmnt[i] -= amount;
				if(slotAmnt[i] == 0) slotID[i] = Item.AIR;
			}
		}
	}
	
	public boolean checkFor(Item id, int amount)
	{
		for(int i = 0; i < 8; i++)
		{
			if(id == slotID[i] && slotAmnt[i] >= amount)
				return true;
		}
		
		return false;
	}
}
