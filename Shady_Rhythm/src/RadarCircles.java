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
	int[] lineWidth;
	
	RadarCircles(int screenWidth, int screenHeight)
	{
		center_x = screenWidth/2;
		center_y = screenHeight/2;
		
		radius = (screenHeight/8) - 5;
		
		fillColors = new Color[4];
		for (int i = 0; i < fillColors.length; i++)
			fillColors[i] = Color.black;
		
		outlineColors = new Color[4];
		outlineColors[0] = Color.yellow;
		outlineColors[1] = Color.green;
		outlineColors[2] = Color.blue;
		outlineColors[3] = Color.red;
		
		lineWidth = new int[4];
		for (int i = 0; i < lineWidth.length; i++)
			lineWidth[i] = 3;
	}
	
	public void select(int circle)
	{
		for (int i = 0; i < lineWidth.length; i++)
			lineWidth[i] = 3;
		lineWidth[circle - 1] = 8;
	}
	
	public void keyPressed(String circle)
	{
		fillColors[Integer.parseInt(circle) - 1] = outlineColors[Integer.parseInt(circle) - 1];
	}
	
	public void draw(Graphics g)
	{
		for (int i = 0; i < 4; i++)
		{
			g.setLineWidth(lineWidth[i]);
			g.setColor(outlineColors[i]);
			g.draw(new Circle(center_x + (radius * (i + 1)), center_y, 28));
			g.draw(new Circle(center_x, center_y, radius * (i + 1)));
			g.setColor(fillColors[i]);
			g.fill(new Circle(center_x + (radius * (i + 1)), center_y, 27));
			fillColors[i] = Color.black;
		}
			
	}
}
