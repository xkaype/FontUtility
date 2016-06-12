package co.kaype.fc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import co.kaype.fc.fwk.Display;
import co.kaype.fc.gui.*;
import co.kaype.fc.img.*;

//
// This code was really quickly written because I wanted to convert some fonts over to work with LÖVE.
// Eventually I'll rewrite it.
//
public class App
{
	// App instance
	private static final App instance = new App();

	// GUI scale
	public static final int SCALE = 2;
	
	// The window and everything on it
	public Display display;
	public Image screen;
	
	// List of GUI components
	public ArrayList<Component> gui = new ArrayList<Component>();
	
	// Count of each frame
	public int time = 0;
	
	// The status of the font and the image it gets tossed into
	public Image fontImage = new Image(2, 2);
	public boolean fontLoaded = false;
	
	// The little mascot thing
	public SpriteSheet fontsMascot = new SpriteSheet("/mascot.png", 16, 16);
	
	// The convert/render buttons
	public Button convertButton, renderButton;
	
	// The file chooser
	public static JFileChooser fileChooser;
	
	//
	// MAIN FUNCTION
	//
	public static void main(String[] args)
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PNG image (*.png)", "png");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(fileFilter);
		
		instance.init();
	}
	
	//
	// Set up the GUI
	//
	public void init()
	{
		TextRenderer.init();
		
		gui.add(new Button("Load Font", 16, 32)
		{
			public void onAction()
			{
				loadFont();
			}
		});

		gui.add(new Button("Exit", 81, 32)
		{
			public void onAction()
			{
				System.exit(0);
			}
		});

		display = new Display("FÖNTS", 320, 512, SCALE);
		display.init();

		screen = new Image(display.getScaledWidth(), display.getScaledHeight());
	}
	
	//
	// Load up that font, yo
	//
	public void loadFont()
	{
		fontImage = new Image(2, 2);
		fontLoaded = false;

		fileChooser.showOpenDialog(display.getFrame());

		if (fileChooser.getSelectedFile() != null)
		{
			String path = fileChooser.getSelectedFile().getAbsolutePath();
			System.out.println("Loading from: " + path);
			fontImage = ImageLoader.loadImageFromSystem(path);
		}
	}

	public Image fontBuffer;
	public SpriteSheet fontSheet;
	public int fontWidth, fontHeight;
	public boolean fontConverted;
	
	//
	// I suppose this is a rather silly way of getting the size of each character, but it works.
	//
	public void convertFont()
	{
		String s = (String) JOptionPane.showInputDialog(display.getFrame(), "Please enter the individual character dimensions, e.g. 6x8");

		String[] fontDimensions = s.split("x");
		fontWidth = Integer.parseInt(fontDimensions[0]);
		fontHeight = Integer.parseInt(fontDimensions[1]);

		fontSheet = new SpriteSheet(fontImage, fontWidth, fontHeight);
	}
	
	//
	// Export the font!
	//
	public void renderFont()
	{
		fileChooser.showSaveDialog(display.getFrame());

		if (fileChooser.getSelectedFile() != null)
		{
			try
			{
				String path = fileChooser.getSelectedFile().getAbsolutePath();
				System.out.println("Saving to: " + path);
				BufferedImage output = ImageLoader.toBufferedImage(fontBuffer);
				ImageIO.write(output, "png", new File(path));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//
	// This is called whenever we resize the window.
	//
	public void resize()
	{
		screen = new Image(display.getScaledWidth(), display.getScaledHeight());
	}
	
	// Blinking effect on the mascot thingy
	private int xTime = 160;
	private int blinkEnd = 0, blinkStart = 0;
	private boolean blink = true;

	//
	// Update program
	//
	public void update()
	{
		time += 6;

		for (Component component : gui)
			component.update();

		if (fontImage.w > 2 && !fontLoaded)
		{
			if (convertButton == null) gui.add(convertButton = new Button("Convert", 16, 64 + fontImage.h + 16)
			{
				@Override
				public void onAction()
				{
					convertFont();
				}
			});

			fontLoaded = true;
		}

		if (fontLoaded && fontSheet != null)
		{
			int sw = fontImage.w / fontWidth;
			int sh = fontImage.h / fontHeight;

			int max = ((sw - 1) * (fontWidth + 1)) + (((sh - 1) * sw)) * (fontWidth + 1);

			xTime--;

			if (xTime < -((max + (320 / 2)))) xTime = 320;

			if (!fontConverted)
			{
				fontBuffer = new Image(max, fontHeight);
				fontBuffer.clear(0x00000000);

				if (renderButton == null) gui.add(renderButton = new Button("Export", convertButton.x + convertButton.w + 4, 64 + fontImage.h + 16)
				{
					@Override
					public void onAction()
					{
						renderFont();
					}
				});
			}
		}

		if (blinkStart++ >= 300)
		{
			blink = true;
			blinkStart = 0;
		}

		if (blink)
		{
			if (blinkEnd++ >= 20)
			{
				blinkEnd = 0;
				blink = false;
			}
		}
	}
	
	//
	// Render program
	//
	public void render()
	{
		screen.clear(0xFF222222);

		for (Component component : gui)
			component.render(screen);

		TextRenderer.draw(screen, 1, "Font Converter", 16, 16, 0xFFFFFFFF);

		if (fontLoaded) screen.drawImage(fontImage, 16, 64);
		if (fontLoaded && convertButton != null) convertButton.y = 64 + fontImage.h + 16;

		if (fontLoaded && fontSheet != null)
		{
			int sw = fontImage.w / fontWidth;
			int sh = fontImage.h / fontHeight;

			for (int x = 0; x < sw; x++)
			{
				for (int y = 0; y < sh; y++)
				{
					// Convert other colors to white
					for (int x2 = 0; x2 < fontBuffer.w; x2++)
					{
						for (int y2 = 0; y2 < fontBuffer.h; y2++)
						{
							if (!isBlank(fontBuffer.px[x2 + y2 * fontBuffer.w]))
							{
								if (fontBuffer.px[x2 + y2 * fontBuffer.w] != 0xFF5BE95D)
								{
									if (fontBuffer.px[x2 + y2 * fontBuffer.w] != 0xFFFFFFFF) fontBuffer.px[x2 + y2 * fontBuffer.w] = 0xFFFFFFFF;
								}
							}
						}
					}

					// Drawing position for preview
					int xx = xTime;
					int yy = convertButton.y + 14 + 16;

					// Drawing offsets
					int xOffs = 1 + (x * (fontWidth + 1)) + ((y * sw)) * (fontWidth + 1);
					int yOffs = 0;

					// Draw changes to font image
					fontBuffer.drawImage(fontSheet.getImage(x, y), xOffs, yOffs);
					fontBuffer.drawRect(0xFF5BE95D, fontWidth + xOffs, yOffs, 1, fontHeight);
					fontBuffer.drawRect(0xFF5BE95D, 0, yOffs, 1, fontHeight);

					// Display image
					screen.drawImage(fontBuffer, xx, yy);
				}
			}
		}

		screen.drawImage(fontsMascot.getImage(blink ? 1 : 0, 0), 2, 116, 14);

		fadeIn();
	}
	
	//
	// This checks if the alpha value of a pixel is 0.
	//
	private boolean isBlank(int res)
	{
		return (((res >> 24) & 0xff) == 0);
	}
	
	//
	// Do the fade in effect you see when you open the program.
	//
	private void fadeIn()
	{
		int delay = 160;

		for (int x = 0; x < screen.w; x++)
		{
			for (int y = 0; y < screen.h; y++)
			{
				if (time > 0 && time < 40 + delay) screen.px[x + y * screen.w] = 0;

				int col1 = (screen.px[x + y * screen.w] & 0xFCFCFC) >> 2;
				int col2 = (screen.px[x + y * screen.w] & 0xFEFEFE) >> 1;

				if (time > 40 + delay && time < 80 + delay)
				{
					if ((x - y) % 2 == 0) screen.drawPoint(col1, x, y);
				}

				if (time > 80 + delay && time < 120 + delay)
				{
					if (x % 2 == 0 && y % 2 == 0) screen.drawPoint(col2, x, y);
				}
			}
		}
	}
	
	//

	public static App getInstance()
	{
		return instance;
	}
}
