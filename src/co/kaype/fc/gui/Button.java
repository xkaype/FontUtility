package co.kaype.fc.gui;

import co.kaype.fc.img.Image;

//
// A GUI Button
//
public class Button extends Component
{
	//
	// Creates the button
	//
	public Button(String msg, int x, int y)
	{
		super(msg, x, y, TextRenderer.getWidth(msg) + 8, 12);
	}
	
	//
	// Updates the button
	//
	public void update()
	{
		super.update();
	}
	
	//
	// Renders the button
	//
	public void render(Image surface)
	{
		int col = (!isHovered() ? 0x545454 : 0x646464);
		int colDark = (!isHovered() ? 0x444444 : 0x545454);

		surface.drawRect(0, x + 2, y, w - 2, h + 2);
		surface.drawRect(0, x + 1, y + 1, w, h);

		surface.drawRect(colDark, x + 1, y - 1, w - 2, (h / 2) + 3);
		surface.drawRect(colDark, x, y, w, (h / 2) + 1);
		
		surface.drawRect(col, x + 1, y + (h / 2), w - 2, (h / 2) + 1);
		surface.drawRect(col, x, y + (h / 2) + 1, w, (h / 2) - 1);
		
		TextRenderer.draw(surface, 1, msg, x + (w / 2) - (TextRenderer.getWidth(msg) / 2) + 1, y + 2, 0xFFFFFF);
	}
}
