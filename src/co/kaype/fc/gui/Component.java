package co.kaype.fc.gui;

import java.awt.Rectangle;

import co.kaype.fc.App;
import co.kaype.fc.fwk.InputHandler;
import co.kaype.fc.img.Image;

//
// A GUI Component
//
public class Component
{
	// The contents
	public String msg;
	
	// The boundaries
	public int x, y, w, h;
	
	// The boundaries stored in an AWT Rectangle
	public Rectangle bounds;
	
	//
	// Creates the component
	//
	public Component(String msg, int x, int y, int w, int h)
	{
		this.msg = msg;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		bounds = new Rectangle(x, y, w, h);
	}
	
	//
	// This function gets called whenever the component gets activated, usually by a mouse click
	//
	public void onAction()
	{
		// ...
	}
	
	//
	// Returns whether or not the user is hovering over the component
	//
	public boolean isHovered()
	{
		Rectangle mouseAsRect = new Rectangle(InputHandler.mousePosX / App.SCALE, InputHandler.mousePosY / App.SCALE, 1, 1);
		
		if (mouseAsRect.intersects(bounds))
			return true;
		
		return false;
	}

	
	//
	// Returns whether or not the user has clicked the component
	//
	public boolean isClicked()
	{
		if (isHovered() && InputHandler.mouseDown)
		{
			InputHandler.mouseDown = false;
			return true;
		}
		
		return false;
	}
	
	//
	// Updates the component
	//
	public void update()
	{
		if (isClicked())
			onAction();
		
		bounds = new Rectangle(x, y, w, h);
	}
	
	//
	// Renders the component
	//
	public void render(Image surface)
	{
		surface.drawRect(0x545454, x, y, w, h);
	}
}
