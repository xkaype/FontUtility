package co.kaype.fc.img;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet
{
	private BufferedImage base;
	private int w, h;

	public SpriteSheet(String ref, int w, int h)
	{
		try
		{
			base = ImageIO.read(SpriteSheet.class.getResourceAsStream(ref));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.w = w;
		this.h = h;
	}
	
	public SpriteSheet(Image ref, int w, int h)
	{
		base = ImageLoader.toBufferedImage(ref);
		
		this.w = w;
		this.h = h;
	}
	
	public Image getImage(int sx, int sy)
	{
		return ImageLoader.loadImage(base.getSubimage(sx * w, sy * h, w, h));
	}
	
	public Image getImage(int s)
	{
		return ImageLoader.loadImage(base.getSubimage((s % w) * w, (s / h) * h, w, h));
	}
}