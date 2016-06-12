package co.kaype.fc.img;

//
// Very basic image class
//
public class Image
{
	// Width / Height
	public int w, h;
	
	// Pixels
	public int[] px;
	
	//
	// Creates a new image with a specified width and height
	//
	public Image(int w, int h)
	{
		this.w = w;
		this.h = h;

		px = new int[w * h];
	}
	
	//
	// Clears the image
	//
	public void clear()
	{
		clear(0);
	}

	//
	// Clears an image with a specified color
	//
	public void clear(int v)
	{
		for (int i = 0; i < px.length; i++)
			px[i] = v;
	}
	
	//
	// Draws a rectangle
	//
	public void drawRect(int v, double xo, double yo, int wo, int ho)
	{
		for (int x = 0; x < wo; x++)
		{
			int xp = (int) (x + xo);

			if (xp < 0 || xp >= w)
				continue;

			for (int y = 0; y < ho; y++)
			{
				int yp = (int) (y + yo);

				if (yp < 0 || yp >= h)
					continue;

				px[xp + yp * w] = v;
			}
		}
	}
	
	//
	// Draws a point
	//
	public void drawPoint(int v, double x, double y)
	{
		int ix = (int) (x);
		int iy = (int) (y);

		if ((ix >= 0 && ix < w) && (iy >= 0 && iy < h))
			px[ix + iy * w] = v;
	}
	
	//
	// Draws an image
	//
	public void drawImage(Image img, double xo, double yo)
	{
		drawImage(img, 1, xo, yo);
	}
	
	//
	// Draws a scaled image
	//
	public void drawImage(Image img, int scale, double xo, double yo)
	{
		for (int y = 0; y < img.h * scale; y++)
		{
			int yp = (int) (y + yo);

			if (yp < 0 || yp >= h)
				continue;

			for (int x = 0; x < img.w * scale; x++)
			{
				int xp = (int) (x + xo);

				if (xp < 0 || xp >= w)
					continue;

				int res = img.px[(x / scale) + (y / scale) * img.w];
				
				if (((res >> 24) & 0xff) != 0)
					px[xp + yp * w] = res;
			}
		}
	}
}
