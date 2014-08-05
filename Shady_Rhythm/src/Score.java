import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Score
{
	float points;
	float maxPoints;
	float percentage;
	
	Score()
	{
		points = 0;
		maxPoints = 0;
		percentage = 1;
	}
	
	public void updateMaxPoints()
	{
		maxPoints++;
		percentage = points/maxPoints;
	}
	
	public void modPoints(int value)
	{
		points += value;
	}
	
	public void draw(Graphics g, GameContainer gc)
	{
		g.setColor(Color.cyan);
		g.draw(new Rectangle(gc.getScreenWidth() - 100, 20, 50, 200));
		g.fill((new Rectangle(gc.getScreenWidth() - 100, 20 + (200 * (1 - percentage)), 50, 200 * (percentage))));
		g.setColor(Color.white);
		g.drawString(Integer.toString((int)(percentage * 100)) + "%", gc.getScreenWidth() - 95, 30);
	}
}
