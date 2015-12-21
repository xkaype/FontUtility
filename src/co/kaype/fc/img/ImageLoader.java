package co.kaype.fc.img;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader
{
	public static Image loadImage(String ref)
	{
		try
		{
			return loadImage(ImageIO.read(ImageLoader.class.getResourceAsStream(ref)));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Image loadImageFromSystem(String ref)
	{
		try
		{
			return loadImage(ImageIO.read(new File(ref)));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Image loadImage(BufferedImage image)
	{
		try
		{
			Image result = new Image(image.getWidth(), image.getHeight());
			
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), result.px, 0, image.getWidth());
			
			return result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static BufferedImage toBufferedImage(Image image)
	{
		try
		{
			BufferedImage result = new BufferedImage(image.w, image.h, BufferedImage.TYPE_INT_ARGB);
			
			result.setRGB(0, 0, image.w, image.h, image.px, 0, image.w);
			
			return result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}