package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import objects.Inventory;
import objects.Player;
import utilities.Images;

public class Engine extends Canvas implements Runnable, KeyListener, MouseListener, MouseWheelListener
{
	private Thread thread;
	private boolean isRunning = false;
	
	private int frameCount = 0;
	
	public JFrame frame;
	
	public Images imgs;
	public Inventory inv;
	public World world;
	public Player player;
	
	public static int width, height;
	public static int mouseX, mouseY;
	
	public static int STATE = 0;
	
	public static float zoom = 4;
	
	public Engine()
	{
		frame = new JFrame("Marooned");
		
		frame.setPreferredSize(new Dimension(800, 600));
		
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		frame.add(this, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		imgs = new Images();
		
		mouseX = -100;
		mouseY = -100;
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		
		start();
	}
	
	public static void main(String[] args)
	{
		new Engine();
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		
		isRunning = true;
	}
	
	public synchronized void stop()
	{
		try {
			thread.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		long lastTime = System.currentTimeMillis();
		
		float start, wait, difference;
		start = 0;
		
		while(isRunning)
		{
		    try
		    {
				thread.sleep(4);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		    
		    start = System.nanoTime();
			
			update();
			frameCount++;
			
			if(System.currentTimeMillis() - lastTime >= 1000)
			{
				System.out.println("FPS: " + frameCount);
				frameCount = 0;
				lastTime += 1000;
			}
		}
		stop();
	}
	
	public static void playSound(String name)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				try 
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(name));
					
					clip.open(ais);
					clip.start();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void load()
	{
		world = new World();
		player = new Player(5 * 256 * zoom, 3.5f * 256.f * zoom);
		inv = new Inventory();
	}
	
	public void update()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D) graphics;
		
		width = frame.getWidth();
		height = frame.getHeight();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		if(STATE == 0)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.drawString("Marooned", Engine.width / 2 - 127, Engine.height / 2 - 250);
			
			g.setFont(new Font("Courier", Font.PLAIN, 30));
			g.drawString("Press <ENTER> to play", Engine.width / 2 - 200, Engine.height / 2 + 100);
			g.drawString("Press <H> for help", Engine.width / 2 - 175, Engine.height / 2 + 140);
			g.drawString("Press <ESCAPE> to quit", Engine.width / 2 - 210, Engine.height / 2 + 180);
		}
		else if(STATE == 1)
		{
			if(world.isLoaded) g.translate(-player.camX, -player.camY);
			world.update(g, player, inv);
			if(!world.isLoaded)
			{
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier", Font.PLAIN, 30));
				g.drawString("Loading: " + (int) ((float) world.loadVal / 70 * 100) + "%", Engine.width / 2 - 150, Engine.height / 2 - 15);
			}
			if(world.isLoaded)
			{
				player.update(g, world, inv);
				g.translate(player.camX, player.camY);
				inv.update(g, world);
			}
		}
		else if(STATE == 2)
		{
			g.setColor(Color.RED);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.drawString("You died.", Engine.width / 2 - 150, Engine.height / 2 - 25);
		}
		else if(STATE == 3)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.PLAIN, 20));
			g.drawString("Controls:", 10, 25);
			g.drawString("W, A, S, D: Movement", 10, 50);
			g.drawString("Left-click: Use whatever you've selected in the hotbar.", 10, 75);
			g.drawString("Also destroy placed tiles or trees, etc.", 10, 100);
			g.drawString("Right-click: Select items once or twice to craft.", 10, 125);
			g.drawString("C: Confirm the craft.", 10, 150);
			g.drawString("Scroll-wheel: Scroll through the hotbar.", 10, 175);
			
			g.drawString("Crafting recipes:", 10, 225);
			g.drawString("Sticks: 1 (blue) wood.", 10, 250);
			g.drawString("Bow: 2 vines, 1 (yellow) sticks.", 10, 275);
			g.drawString("Wood Wall: 2 wood, 2 vines.", 10, 300);
			g.drawString("Arrowhead: 2 stones.", 10, 325);
			g.drawString("Wood Sword: 1 wood, 1 vine, 1 sticks.", 10, 350);
			g.drawString("Arrow: 1 arrowhead, 2 sticks, 1 vine.", 10, 375);
		}
		
		graphics.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e) 
	{
		int k = e.getKeyCode();
		
		if(STATE == 0)
		{
			if(k == e.VK_ENTER)
			{
				load();
				STATE = 1;
			}
			if(k == e.VK_H)
			{
				STATE = 3;
			}
			if(k == e.VK_ESCAPE)
			{
				System.exit(0);
			}
		}
		if(STATE == 1)
		{
			if(k == e.VK_W)
				player.isMovingNorth = true;
			if(k == e.VK_S)
				player.isMovingSouth = true;
			if(k == e.VK_A)
				player.isMovingEast = true;
			if(k == e.VK_D)
				player.isMovingWest = true;
			
			if(k == e.VK_C)
			{
				inv.craft();
			}
		}
		if(STATE == 2 || STATE == 3)
		{
			if(k == e.VK_ESCAPE)
			{
				STATE = 0;
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int k = e.getKeyCode();
		if(STATE == 1)
		{
			if(k == e.VK_W)
			{
				player.direction = 0;
				player.isMovingNorth = false;
			}
			if(k == e.VK_S)
			{
				player.direction = 3;
				player.isMovingSouth = false;
			}
			if(k == e.VK_A)
			{
				player.direction = 2;
				player.isMovingEast = false;
			}
			if(k == e.VK_D)
			{
				player.direction = 1;
				player.isMovingWest = false;
			}
			if(k == e.VK_C)
			{
				inv.toggleSound = true;
			}
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) 
	{
		if(STATE == 1)
		{
			mouseX = e.getX() + (int) player.camX;
			mouseY = e.getY() + (int) player.camY;
			
			if(SwingUtilities.isLeftMouseButton(e))
			{
				inv.use(world, player);
			}
			
			if(SwingUtilities.isRightMouseButton(e))
			{
				inv.slotSelected[inv.curSelection] += 1;
				if(inv.slotSelected[inv.curSelection] > 2)
					inv.slotSelected[inv.curSelection] = 0;
			}
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
		mouseX = -100;
		mouseY = -100;
		
		if(STATE == 1) player.isFighting = false;
	}

	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		if(STATE == 1) inv.curSelection += e.getUnitsToScroll() / 3;
	}
}
