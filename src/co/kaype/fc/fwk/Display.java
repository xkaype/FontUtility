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

public class Display extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private BufferedImage viewport;
	private BufferStrategy viewportBs;
	private int[] viewportPx;
	
	private boolean running = false;

	private String title;
	private int width, height, scale;
	
	private int fps;
	
	public Display(String title, int width, int height, int scale)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		
		setSize(width, height);

		InputAssistant inputAssistant = new InputAssistant();
		addKeyListener(inputAssistant);
		addMouseListener(inputAssistant);
		addMouseMotionListener(inputAssistant);
	}
	
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
	
	public void initViewport()
	{
		viewport = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
		viewportPx = ((DataBufferInt) viewport.getRaster().getDataBuffer()).getData();
	}

	public void start()
	{
		createBufferStrategy(3);
		viewportBs = getBufferStrategy();
		
		if (!running)
			running = true;

		new Thread(this).start();
	}

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

	public int myWidth, myHeight;
	
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
	
	public void destroy()
	{
		frame.dispose();
	}
	
	public JFrame getFrame()
	{
		return frame;
	}

	public BufferedImage getViewport()
	{
		return viewport;
	}
	
	public int getScaledWidth()
	{
		return width / scale;
	}
	
	public int getScaledHeight()
	{
		return height / scale;
	}
	
	public int getScale()
	{
		return scale;
	}
	
	public int getDisplayFPS()
	{
		return fps;
	}
}
