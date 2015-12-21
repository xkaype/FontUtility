package co.kaype.fc.gui;

import co.kaype.fc.img.Image;
import co.kaype.fc.img.SpriteSheet;

public class TextRenderer
{
	public static SpriteSheet font;
	private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789                      ";
	
	public static void init()
	{
		font = new SpriteSheet("/font.png", 6, 8);
	}
	
	public static int getWidth(String msg)
	{
		int offs = 0;
		
		for (int i = 0; i < msg.length(); i++)
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
			
			if (i > 0)
			{
				if (msg.charAt(i-1) == 'i' || msg.charAt(i-1) == 'l')
					offs += 2;
				if (msg.charAt(i-1) == 't')
					offs += 1;
				if (msg.charAt(i-1) == '.')
					offs += 2;
				if (msg.charAt(i-1) == ':')
					offs += 4;
				if (msg.charAt(i-1) == '\'')
					offs += 3;
				if (msg.charAt(i-1) == 'r')
					offs += 1;
			}
		}
		
		return (msg.length() * 6) - offs;
	}
	
	public static void drawText(Image surface, int scale, String msg, double xm, double ym, int col)
	{
		int offs = 0;
		
		for (int i = 0; i < msg.length(); i++)
		{
			int ch = characters.indexOf(msg.charAt(i));

			int xs = ch % 84;
			
			if (ch < 0)
				continue;

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
			
			if (i > 0)
			{
				if (msg.charAt(i-1) == 'i' || msg.charAt(i-1) == 'l')
					offs += 2;
				if (msg.charAt(i-1) == 't')
					offs += 1;
				if (msg.charAt(i-1) == '.')
					offs += 2;
				if (msg.charAt(i-1) == ':')
					offs += 4;
				if (msg.charAt(i-1) == '\'')
					offs += 3;
				if (msg.charAt(i-1) == 'r')
					offs += 1;
			}
			
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

					if (img.px[(x / scale) + (y / scale) * img.w] == 0xFFFFFFFF) // filtering out
						surface.px[xp + yp * surface.w] = img.px[(x / scale) + (y / scale) * img.w] & col;
				}
			}
		}
	}
	
	public static void draw(Image surface, int scale, String msg, double xm, double ym, int col)
	{
		drawText(surface, scale, msg, xm+scale, ym+scale, 0x000000);
		drawText(surface, scale, msg, xm, ym, col);
	}
	
	public static void draw(Image surface, int scale, String msg, double xm, double ym)
	{
		drawText(surface, scale, msg, xm+scale, ym+scale, 0x000000);
		drawText(surface, scale, msg, xm, ym, 0xFFFFFF);
	}
}