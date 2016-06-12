package co.kaype.fc.fwk;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import co.kaype.fc.App;

//
// It's a bit verbose for just a window.
// Welcome to Java, son!
//
public class Display extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	// The JFrame used by the display
	private JFrame frame;
	
	// The BufferedImage and BufferStrategy the program uses for drawing graphics
	private BufferedImage viewport;
	private BufferStrategy viewportBs;
	private int[] viewportPx;
	
	// Whether or not the program is running
	private boolean running = false;

	// The display title
	private String title;
	
	// The display dimensions
	private int width, height, scale;
	
	// The FPS
	private int fps;
	
	//
	// Creates the display
	//
	public Display(String title, int width, int height, int scale)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		
		setSize(width, height);

		InputHandler inputAssistant = new InputHandler();
		addKeyListener(inputAssistant);
		addMouseListener(inputAssistant);
		addMouseMotionListener(inputAssistant);
	}
	
	//
	// Initializes the display
	//
	public void init()
	{
		frame = new JFrame(title);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		initViewport();
		start();
	}
	
	//
	// Initializes the BufferedImage the program draws graphics to
	//
	public void initViewport()
	{
		viewport = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
		viewportPx = ((DataBufferInt) viewport.getRaster().getDataBuffer()).getData();
	}
	
	//
	// Creates a buffer strategy
	// Starts the program
	// Spins up a new thread
	//
	public void start()
	{
		createBufferStrategy(3);
		viewportBs = getBufferStrategy();
		
		if (!running)
			running = true;

		new Thread(this).start();
	}
	
	//
	// Stops the program
	//
	public void stop()
	{
		if (running)
			running = false;
	}

	@Override
	public void run()
	{
		double target = 60.0;
		
		int processedFrames = 0;
		int processedUpdates = 0;
		
		long past = System.nanoTime();
		
		double unprocessedSeconds = 0;
		double secondsPerUpdate = 1 / target;
		
		requestFocus();
		
		while (running)
		{
			long now = System.nanoTime();
			long total = now - past;
			
			past = now;
			
			if (total < 0)
				total = 0;

			if (total > 100000000)
				total = 100000000;

			unprocessedSeconds += total / 1000000000.0;
			boolean hasUpdate = false;

			while (unprocessedSeconds > secondsPerUpdate)
			{
				update();
				
				unprocessedSeconds -= secondsPerUpdate;
				hasUpdate = true;

				processedUpdates++;
				
				if (processedUpdates % target == 0)
				{
					fps = processedFrames;
					past += 1000;
					processedFrames = 0;
				}
			}

			if (hasUpdate)
			{
				render();
				
				processedFrames++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	//
	// Updates the display
	//
	public void update()
	{
		if (width != getWidth() || height != getHeight())
		{
			width = getWidth();
			height = getHeight();
			
			initViewport();
			App.getInstance().resize();
		}
		
		App.getInstance().update();
	}
	
	//
	// Renders the display
	//
	public void render()
	{
		Graphics g = viewportBs.getDrawGraphics();

		int scaledWidth = width / scale;
		int scaledHeight = height / scale;
		
		App.getInstance().render();
		
		for (int x = 0; x < scaledWidth; x++)
		{
			for (int y = 0; y < scaledHeight; y++)
			{
				viewportPx[x + y * scaledWidth] = App.getInstance().screen.px[x + y * scaledWidth];
			}
		}
		
		g.setColor(new Color(0x000000));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(viewport, 0, 0, width, height, null);
		g.dispose();
		
		viewportBs.show();
	}
	
	//
	// Destroys the display
	//
	public void destroy()
	{
		frame.dispose();
	}
	
	//
	// Returns the JFrame this uses
	//
	public JFrame getFrame()
	{
		return frame;
	}
	
	//
	// Returns the BufferedImage used for drawing the screen
	//
	public BufferedImage getViewport()
	{
		return viewport;
	}
	
	//
	// Returns the scaled width of the display
	//
	public int getScaledWidth()
	{
		return width / scale;
	}

	//
	// Returns the scaled height of the display
	//
	public int getScaledHeight()
	{
		return height / scale;
	}
	
	//
	// Returns the scale of the display
	//
	public int getScale()
	{
		return scale;
	}
	
	//
	// Returns the FPS
	//
	public int getDisplayFPS()
	{
		return fps;
	}
}
