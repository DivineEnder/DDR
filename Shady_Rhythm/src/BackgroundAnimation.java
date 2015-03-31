import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class BackgroundAnimation
{
	ArrayList<Circle> backgroundCircles;
	Color selectedColor;
	Color backgroundColor;
	ArrayList<Float> radii;
	ArrayList<Color> backgroundCirclesColor;
	
	float colorChange;
	
	BackgroundAnimation(GameContainer gc)
	{
		selectedColor = new Color(Color.red);
		backgroundColor = new Color(84, 84, 84);
		
		radii = new ArrayList<Float>();
		
		backgroundCircles = new ArrayList<Circle>();
		backgroundCirclesColor = new ArrayList<Color>();
		
		colorChange = 50f/255f;
	}
	
	public void addCircle(float x, float y, float startingRadius, Color color)
	{
		radii.add(startingRadius);
		backgroundCircles.add(new Circle(x, y, startingRadius));
		backgroundCirclesColor.add(color);
	}
	
	public char getHighestColorValue(Color color)
	{
		if (color.r > color.g && color.r > color.b)
			return 'r';
		else if (color.g > color.r && color.g > color.b)
			return 'g';
		else if (color.b > color.r && color.b > color.g)
			return 'b';
		else
			return 'n';
	}
	
	public float changingColorValue(Color circleColor, Color color, char colorValue)
	{
		float tempColorChange;
		
		if (getHighestColorValue(circleColor) == colorValue)
			tempColorChange = colorChange;
		else
			tempColorChange = -colorChange;
		
		
		
		if (colorValue == 'r')
		{
			if (color.r + tempColorChange < 1 && color.r + tempColorChange > 0)
				return tempColorChange;
			else
				return 0;
		}
		else if (colorValue == 'g')
		{
			if (color.g + tempColorChange < 1 && color.g + tempColorChange > 0)
				return tempColorChange;
			else
				return 0;
		}
		else
		{
			if (color.b + tempColorChange < 1 && color.b + tempColorChange > 0)
				return tempColorChange;
			else
				return 0;
		}
	}
	
	public Color getCircleFillColor(Color circleColor, int index)
	{
		Color color = backgroundColor;
		
		color = new Color(color.r + changingColorValue(circleColor, color, 'r')*index, color.g + changingColorValue(circleColor, color, 'g')*index, color.b + changingColorValue(circleColor, color, 'b')*index);
		
		return color;
	}
	
	public void update(GameContainer gc)
	{
		for (int i = 0; i < radii.size(); i++)
		{
			radii.set(i, radii.get(i) + 10);
			backgroundCircles.set(i, new Circle(backgroundCircles.get(i).getCenterX(), backgroundCircles.get(i).getCenterY(), radii.get(i)));
			if (backgroundCircles.get(i).contains(0, 0))
			{
				backgroundColor = getCircleFillColor(backgroundCirclesColor.get(i), i+1);
				radii.remove(i);
				backgroundCircles.remove(i);
				backgroundCirclesColor.remove(i);
			}
		}
	}
	
	public void draw(GameContainer gc, Graphics g)
	{
		//GradientFill fill = new GradientFill(0, 0, Color.white, backgroundCircles[0].getRadius()*2, backgroundCircles[0].getRadius()*2, Color.red, true);
		
		//Sets the graphics color to gray
		g.setColor(backgroundColor);
		//Fills a the background with the gray color
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
		
		for (int i = 0; i < backgroundCircles.size(); i++)
		{
			g.setColor(getCircleFillColor(backgroundCirclesColor.get(i), i+1));
			g.fill(backgroundCircles.get(i));
		}
		
		g.setLineWidth(10);
		for (int i = 0; i < backgroundCircles.size(); i++)
		{
			g.setColor(backgroundCirclesColor.get(i));
			g.draw(backgroundCircles.get(i));
		}
		
		//g.setColor(Color.white);
		//g.drawString(Float.toString(backgroundColor.r) + "," + Float.toString(backgroundColor.g) + "," + Float.toString(backgroundColor.b), 0, 0);
	}
}
