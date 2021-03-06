package co.kaype.fc.fwk;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputAssistant implements KeyListener, MouseListener, MouseMotionListener
{
	public static boolean[] keys = new boolean[256 * 256], tkeys = new boolean[256];
	public static int mousePosX, mousePosY;
	public static boolean mouseDown;
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mousePosX = e.getX();
		mousePosY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// ...
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// ...
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// ...
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseDown = false;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}
}
