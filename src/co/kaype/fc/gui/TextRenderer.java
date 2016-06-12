package co.kaype.fc.gui;

import co.kaype.fc.img.Image;
import co.kaype.fc.img.SpriteSheet;

//
// A really rushed text renderer
//
public class TextRenderer
{
	// The dimensions of a single character
	public static final int FONT_WIDTH = 6;
	public static final int FONT_HEIGHT = 8;
	
	// The font spritesheet
	public static SpriteSheet font;
	
	// Every character in this font
	private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789                      ";
	
	//
	// Load up the font spritesheet
	//
	public static void init()
	{
		font = new SpriteSheet("/font.png", FONT_WIDTH, FONT_HEIGHT);
	}
	
	//
	// This is the really horrible function I used for processing kerning in a font.
	// IT JUST WORKS OKAY!
	//
	private static int processKerning(String msg, int i, int offs)
	{
		if (msg.charAt(i) == 'i' || msg.charAt(i) == 'l')
			offs += 2;
		if (msg.charAt(i) == 't')
			offs += 1;
		if (msg.charAt(i) == '.')
			offs += 1;
		if (msg.charAt(i) == '!')
			offs += 1;
		if (msg.charAt(i) == ':')
			offs += 1;
		if (msg.charAt(i) == 'f')
			offs += 1;

		if (i > 0)
		{
			if (msg.charAt(i - 1) == 'i' || msg.charAt(i - 1) == 'l')
				offs += 2;
			if (msg.charAt(i - 1) == 't')
				offs += 1;
			if (msg.charAt(i - 1) == '.')
				offs += 2;
			if (msg.charAt(i - 1) == ':')
				offs += 4;
			if (msg.charAt(i - 1) == '\'')
				offs += 3;
			if (msg.charAt(i - 1) == 'f')
				offs += 1;
			if (msg.charAt(i - 1) == 'r')
				offs += 1;
			if (msg.charAt(i - 1) == '<')
				offs += 1;
			if (msg.charAt(i - 1) == ',')
				offs += 4;
			if (msg.charAt(i - 1) == '{')
				offs += 1;
			if (msg.charAt(i - 1) == '/')
				offs += 1;
			if (msg.charAt(i - 1) == 'k')
				offs += 1;
			if (msg.charAt(i - 1) == 'j')
				offs += 1;
		}

		return offs;
	}
	
	//
	// Gets the width of a string
	//
	public static int getWidth(String msg)
	{
		return getWidth(msg, FONT_WIDTH); // Default to font width
	}

	//
	// Gets the width of a string
	//
	// The 'z' parameter is for, say, if you want to make the font appear bold
	// but you need the width in GUI components to respect that.
	//
	public static int getWidth(String msg, int z)
	{
		int offs = 0;
		int w = 0;

		for (int i = 0; i < msg.length(); i++)
		{
			offs = processKerning(msg, i, offs); // Get position offset for each character
			w = (i + 1) * z - offs; // Set width
		}

		return w;
	}
	
	//
	// Draws a string to an image surface
	//
	public static void drawString(Image surface, int scale, String msg, double xm, double ym, int col)
	{
		int offs = 0;
		
		for (int i = 0; i < msg.length(); i++)
		{
			int ch = characters.indexOf(msg.charAt(i));
			
			if (ch < 0)
				continue;

			int xs = ch % 84;
			
			offs = processKerning(msg, i, offs);
			
			Image img = font.getImage(xs, 0);
			double xo = xm + (i * (6 * scale) - offs);
			double yo = ym;
			
			for (int y = 0; y < img.h * scale; y++)
			{
				int yp = (int) (y + yo);

				if (yp < 0 || yp >= surface.h)
					continue;

				for (int x = 0; x < img.w * scale; x++)
				{
					int xp = (int) (x + xo);

					if (xp < 0 || xp >= surface.w)
						continue;

					if (img.px[(x / scale) + (y / scale) * img.w] == 0xFFFFFFFF) // Filtering out
						surface.px[xp + yp * surface.w] = img.px[(x / scale) + (y / scale) * img.w] & col;
				}
			}
		}
	}
	
	//
	// Draws a string with a specified color + a shadow
	//
	public static void draw(Image surface, int scale, String msg, double xm, double ym, int col)
	{
		drawString(surface, scale, msg, xm+scale, ym+scale, 0x000000);
		drawString(surface, scale, msg, xm, ym, col);
	}
	
	//
	// Draws a string with a shadow
	//
	public static void draw(Image surface, int scale, String msg, double xm, double ym)
	{
		drawString(surface, scale, msg, xm+scale, ym+scale, 0x000000);
		drawString(surface, scale, msg, xm, ym, 0xFFFFFF);
	}
}