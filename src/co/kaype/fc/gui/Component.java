package co.kaype.fc.gui;

import java.awt.Rectangle;

import co.kaype.fc.App;
import co.kaype.fc.fwk.InputAssistant;
import co.kaype.fc.img.Image;

public class Component
{
	public String msg;
	public int x, y, w, h;
	
	public Rectangle bounds;
	
	public Component(String msg, int x, int y, int w, int h)
	{
		this.msg = msg;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		bounds = new Rectangle(x, y, w, h);
	}
	
	public void onAction()
	{
		// ...
	}
	
	public boolean isHovered()
	{
		Rectangle mouseAsRect = new Rectangle(InputAssistant.mousePosX / App.SCALE, InputAssistant.mousePosY / App.SCALE, 1, 1);
		
		if (mouseAsRect.intersects(bounds))
			return true;
		
		return false;
	}
	
	public boolean isClicked()
	{
		if (isHovered() && InputAssistant.mouseDown)
		{
			InputAssistant.mouseDown = false;
			return true;
		}
		
		return false;
	}
	
	public void update()
	{
		if (isClicked())
			onAction();
		
		bounds = new Rectangle(x, y, w, h);
	}
	
	public void render(Image surface)
	{
		surface.drawRect(0x545454, x, y, w, h);
	}
}
