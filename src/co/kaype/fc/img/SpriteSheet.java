package co.kaype.fc.img;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//
// A spritesheet, an image that holds multiple smaller images
//
public class SpriteSheet
{
	// The base image
	private BufferedImage base;
	
	// The width/height of each sprite
	private int w, h;

	//
	// Creates a spritesheet
	//
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
	
	//
	// Creates a spritesheet from an existing image
	//
	public SpriteSheet(Image ref, int w, int h)
	{
		base = ImageLoader.toBufferedImage(ref);
		
		this.w = w;
		this.h = h;
	}
	
	//
	// Gets a sprite from two x/y coordinates
	//
	public Image getImage(int sx, int sy)
	{
		return ImageLoader.loadImage(base.getSubimage(sx * w, sy * h, w, h));
	}
	
	//
	// Gets a sprite from its position
	//
	public Image getImage(int s)
	{
		return ImageLoader.loadImage(base.getSubimage((s % w) * w, (s / h) * h, w, h));
	}
}