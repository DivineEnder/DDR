import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Circle;

public class RadarCircles
{
	static int center_x;
	static int center_y;
	int radius;
	Color[] outlineColors;
	Color[] fillColors;
	
	RadarCircles(int screenWidth, int screenHeight)
	{
		center_x = screenWidth/2;
		center_y = screenHeight/2;
		
		radius = (screenHeight/10) - 5;
		
		fillColors = new Color[5];
		for (int i = 0; i < fillColors.length; i++)
			fillColors[i] = Color.black;
		
		outlineColors = new Color[5];
		outlineColors[0] = Color.white;
		outlineColors[1] = Color.green;
		outlineColors[2] = Color.blue;
		outlineColors[3] = Color.red;
		outlineColors[4] = Color.yellow;
	}
	
	public void keyPressed(String circle)
	{
		fillColors[Integer.parseInt(circle) - 1] = outlineColors[Integer.parseInt(circle) - 1];
	}
	
	public void draw(Graphics g)
	{
		g.setLineWidth(3);
		
		for (int i = 0; i < 5; i++)
		{

			g.setColor(outlineColors[i]);
			g.draw(new Circle(center_x + (radius * (i + 1)), center_y, 28));
			g.draw(new Circle(center_x, center_y, radius * (i + 1)));
			g.setColor(fillColors[i]);
			g.fill(new Circle(center_x + (radius * (i + 1)), center_y, 27));
			fillColors[i] = Color.black;
		}
			
	}
}
