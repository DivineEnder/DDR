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
	Color[] fillColors;
	
	RadarCircles(int screenWidth, int screenHeight)
	{
		center_x = screenWidth/2;
		center_y = screenHeight/2;
		
		radius = (screenHeight/10) - 5;
		
		fillColors = new Color[5];
		for (int i = 0; i < fillColors.length; i++)
			fillColors[i] = Color.black;
	}
	
	public void keyPressed(char key)
	{
		if (key == 'a')
			fillColors[0] = Color.white;
		if (key == 'f')
			fillColors[1] = Color.green;
		if (key == 'r')
			fillColors[2] = Color.blue;
		if (key == 'd')
			fillColors[3] = Color.red;
		if (key == 'g')
			fillColors[4] = Color.yellow;
	}
	
	public void draw(Graphics g)
	{
		g.setLineWidth(3);
		
		g.setColor(Color.white);
		g.draw(new Circle(center_x + radius, center_y, 28));
		g.draw(new Circle(center_x, center_y, radius));
		g.setColor(Color.green);
		g.draw(new Circle(center_x + (radius * 2), center_y, 28));
		g.draw(new Circle(center_x, center_y, (radius * 2)));
		g.setColor(Color.blue);
		g.draw(new Circle(center_x + (radius * 3), center_y, 28));
		g.draw(new Circle(center_x, center_y, (radius * 3)));
		g.setColor(Color.red);
		g.draw(new Circle(center_x + (radius * 4), center_y, 28));
		g.draw(new Circle(center_x, center_y, (radius * 4)));
		g.setColor(Color.yellow);
		g.draw(new Circle(center_x + (radius * 5), center_y, 28));
		g.draw(new Circle(center_x, center_y, (radius * 5)));
		
		g.setColor(fillColors[0]);
		g.fill(new Circle(center_x + radius, center_y, 27));
		g.setColor(fillColors[1]);
		g.fill(new Circle(center_x + (radius * 2), center_y, 27));
		g.setColor(fillColors[2]);
		g.fill(new Circle(center_x + (radius * 3), center_y, 27));
		g.setColor(fillColors[3]);
		g.fill(new Circle(center_x + (radius * 4), center_y, 27));
		g.setColor(fillColors[4]);
		g.fill(new Circle(center_x + (radius * 5), center_y, 27));
		
		for (int i = 0; i < fillColors.length; i++)
			fillColors[i] = Color.black;
	}
}
