package co.kaype.fc.img;

public class Image
{
	public int w, h;
	public int[] px;

	public Image(int w, int h)
	{
		this.w = w;
		this.h = h;

		px = new int[w * h];
	}

	public void clear()
	{
		clear(0);
	}

	public void clear(int v)
	{
		for (int i = 0; i < px.length; i++)
			px[i] = v;
	}
	
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

	public void drawPoint(int v, double x, double y)
	{
		int ix = (int) (x);
		int iy = (int) (y);

		if ((ix >= 0 && ix < w) && (iy >= 0 && iy < h))
			px[ix + iy * w] = v;
	}

	public void drawImage(Image img, double xo, double yo)
	{
		drawImage(img, 1, xo, yo);
	}

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
