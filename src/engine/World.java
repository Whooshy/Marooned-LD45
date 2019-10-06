package engine;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import objects.Arrow;
import objects.Chunk;
import objects.Inventory;
import objects.ItemEntity;
import objects.Particle;
import objects.Player;
import objects.Redman;
import utilities.Images;
import utilities.Item;
import utilities.Tile;

public class World 
{
	public Chunk[][] chunks;
	private int tileTimer, tileHealth, timer, musicTimer;
	public int wave, waveTimer, seed;
	
	public int loadVal = 0;
	public int lX, lY;
	
	public boolean isLoaded = false;
	public boolean isPlayingMusic = false;
	
	public Random r;
	
	public static final int[] permutations = {3, 14, 7, 8, 10, 6, 5, 12, 9, 1, 4, 15, 2, 0, 13, 11};
	
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<ItemEntity> items = new ArrayList<ItemEntity>();
	public ArrayList<Redman> redmen = new ArrayList<Redman>();
	public ArrayList<Arrow> arrows = new ArrayList<Arrow>();
	
	public World()
	{
		chunks = new Chunk[10][7];
		r = new Random();
		
		waveTimer = 100;
		wave = 0;
		
		lX = 0;
		lY = 0;
		
		seed = r.nextInt(10000000);
		
		tileTimer = 0;
		tileHealth = 0;
		timer = 0;
		
		isLoaded = false;
	}
	
	public static int getRandom(int x, int y, int seed)
	{
		int s = (seed % 10000) ^ 2 - seed;
		int sp = s + permutations[Math.abs((seed - s) % 16)];
		int valA = (x + permutations[Math.abs(sp * x + y) % 16] - sp) % ((permutations[Math.abs(seed * x - y) % 16]) + seed + 1);
		int valB = (y + sp) % (permutations[Math.abs(seed * x) % 16] + 1);
		
		return Math.abs((valA + valB) % 2);
	}
	
	public static float getValue(int x, int y, int seed, float range)
	{
		float average = 0;
		for(int a = (int) (x - range); a <= (int) (x + range); a++)
		for(int b = (int) (y - range); b <= (int) (y + range); b++)
		{
			float distance = (float) Math.sqrt(Math.pow(a - x, 2) + Math.pow(b - y, 2));
			if(distance < range)
			{
				average += getRandom(a, b, seed);
			}
		}
		
		float area = (float) (Math.PI * Math.pow(range, 2));
		return average / area;
	}
	
	public void update(Graphics2D g, Player p, Inventory inv)
	{
		if(r.nextInt(10000) == 0 && !isPlayingMusic)
		{
			Engine.playSound("/audio/music.wav");
			isPlayingMusic = true;
		}
		if(isPlayingMusic)
		{
			musicTimer++;
			if(musicTimer > 5580)
			{
				isPlayingMusic = false;
				musicTimer = 0;
			}
		}
		if(isLoaded)
		{
			timer++;
			if(timer > 150)
			{
				timer = 0;
				waveTimer -= 1;
				if(waveTimer == 0)
				{
					waveTimer = 100;
					wave++;
					
					Engine.playSound("/audio/siren.wav");
					
					for(int i = 0; i < wave * 2 + r.nextInt(3); i++)
					{
						redmen.add(new Redman((r.nextInt(20) + 70) * Engine.zoom * 16, (r.nextInt(20) + 46) * Engine.zoom * 16));
					}
				}
			}
			
			for(int x = (int) (p.x / (256 * Engine.zoom)) - 3; x <= (int) (p.x / (256 * Engine.zoom)) + 3; x++)
			for(int y = (int) (p.y / (256 * Engine.zoom)) - 2; y <= (int) (p.y / (256 * Engine.zoom)) + 2; y++)
			{
				if(x < 0 || y < 0 || x >= 10 || y >= 7) continue;
				chunks[x][y].update(g);
			}
			
			for(int i = 0; i < particles.size(); i++)
			{
				particles.get(i).update(g);
				if(particles.get(i).lifetime >= 200)
				{
					particles.remove(i);
				}
			}
			
			for(int i = 0; i < items.size(); i++)
			{
				items.get(i).update(g);
				if(p.getBounds().intersects(items.get(i).getBounds()))
				{
					inv.add(items.get(i).id, 1);
					items.remove(i);
				}
			}
			
			for(int i = 0; i < redmen.size(); i++)
			{
				redmen.get(i).update(g, this, p);
				if(redmen.get(i).health <= 0)
				{
					for(int j = 0; j < 7; j++)
					{
						particles.add(new Particle(redmen.get(i).x + (Engine.zoom * 8), redmen.get(i).y + (Engine.zoom * 8), Tile.GRASS, Images.enemy1Sheet));
					}
					if(r.nextInt(3) == 0) items.add(new ItemEntity(redmen.get(i).x + (Engine.zoom * 8), redmen.get(i).y + (Engine.zoom * 8), Item.HEALTH));
					redmen.remove(i);
				}
			}
			
			for(int i = 0; i < arrows.size(); i++)
			{
				arrows.get(i).update(g);
				for(int j = 0; j < redmen.size(); j++)
				{
					if(arrows.get(i).getBounds().intersects(redmen.get(j).getBounds()))
					{
						redmen.get(j).hit(5);
						//arrows.remove(i);
					}
				}
				if(arrows.get(i).lifetime <= 0)
				{
					arrows.remove(i);
				}
			}
			
			updateMouseEvents();
		}
		
		if(!isLoaded)
		{
			chunks[lX][lY] = new Chunk(lX, lY, seed);
			System.out.println("LOADING: " + (int) (((float) (lX * 7 + lY + 1) / 70) * 100) + "%");
			
			loadVal++;
			
			lY += 1;
			if(lY >= 7)
			{
				lY = 0;
				lX += 1;
				if(lX >= 10)
					isLoaded = true;
			}
		}
	}
	
	public void updateMouseEvents()
	{
		int tX = (int) (Engine.mouseX / (16 * Engine.zoom));
		int tY = (int) (Engine.mouseY / (16 * Engine.zoom));
		
		int cX = tX / 16;
		int cY = tY / 16;
		
		int pX = tX % 16;
		int pY = tY % 16;
		
		if(Engine.mouseX != -100 && Engine.mouseY != -100 && chunks[cX][cY].tiles[pX][pY].isSolid())
		{
			tileTimer++;
			if(tileTimer == 40)
			{
				tileHealth++;
				Engine.playSound("/audio/hit.wav");
				for(int i = 0; i < 7; i++)
				{
					particles.add(new Particle(Engine.mouseX, Engine.mouseY, chunks[cX][cY].tiles[pX][pY], Images.tileSheet));
				}
				tileTimer = 0;
				if(chunks[cX][cY].tiles[pX][pY].getHardness() == tileHealth)
				{
					switch(chunks[cX][cY].tiles[pX][pY])
					{
					case TREE_TOP:
						for(int i = 0; i < r.nextInt(2) + 1; i++)
						{
							items.add(new ItemEntity(Engine.mouseX, Engine.mouseY, Item.WOOD));
						}
						for(int i = 0; i < r.nextInt(2); i++)
						{
							items.add(new ItemEntity(Engine.mouseX, Engine.mouseY, Item.VINES));
						}
						break;
					case BUSH:
						for(int i = 0; i < r.nextInt(2); i++)
						{
							items.add(new ItemEntity(Engine.mouseX, Engine.mouseY, Item.BERRIES));
						}
						break;
					case BOULDER:
						for(int i = 0; i < r.nextInt(2) + 1; i++)
						{
							items.add(new ItemEntity(Engine.mouseX, Engine.mouseY, Item.STONES));
						}
						break;
					case WOOD_WALL:
					case WOOD_WALL_EXPOSED:
						items.add(new ItemEntity(Engine.mouseX, Engine.mouseY, Item.WOOD_WALL));
						break;
					default:
						break;
					}
					
					if(pY != 15 && chunks[cX][cY].tiles[pX][pY + 1] == Tile.TREE_BOTTOM && chunks[cX][cY].tiles[pX][pY] == Tile.TREE_TOP)
						chunks[cX][cY].tiles[pX][pY + 1] = Tile.GRASS;
					if(pY != 0 && chunks[cX][cY].tiles[pX][pY - 1] == Tile.TREE_TOP && chunks[cX][cY].tiles[pX][pY] == Tile.TREE_TOP)
						chunks[cX][cY].tiles[pX][pY] = Tile.TREE_BOTTOM;
					else chunks[cX][cY].tiles[pX][pY] = Tile.GRASS;
					
					chunks[cX][cY].build();
				}
			}
		}
		else
		{
			tileTimer = 0;
			tileHealth = 0;
		}
	}
}
