import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class RhythmCircle
{
	private float x;
	private float y;
	private boolean visible;
	private float rotations;
	private float angleStamp;
	private float angle;
	private float startAngle;
	private float[] pointAngles;
	private int layer;
	private float radius;
	private static float circle_radius;
	private static int screenWidth;
	private static int screenHeight;
	private Color color;
	private int termination;
	private boolean hit;
	
	RhythmCircle(float angle, int rotations, int layer, GameContainer gc)
	{
		screenWidth = gc.getWidth();
		screenHeight = gc.getHeight();
		
		angleStamp = angle;
		this.rotations = rotations;
		this.layer = layer;
		
		circle_radius = 25;
		
		double csquared = (circle_radius*2) * (circle_radius*2);
		double ab = 2 * ((((screenHeight/8) - 5) * layer) * (((screenHeight/8) - 5) * layer));
		
		this.angle = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer); 
		startAngle = this.angle;
		
		pointAngles = new float[3];
		csquared = circle_radius * circle_radius;
		pointAngles[0] = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer);
		csquared = (circle_radius*.5) * (circle_radius*.5);
		pointAngles[1] = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer);
		csquared = (circle_radius*.25) * (circle_radius*.25);
		pointAngles[2] = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer);
		
		x = screenWidth/2 + (float) (((screenHeight/8) * layer) * Math.cos(this.angle * (Math.PI/180)));
		y = screenHeight/2 + (float) (((screenHeight/8) * layer) * Math.sin(this.angle * (Math.PI/180)));
		
		if (layer == 1)
			color = Color.yellow;
		else if (layer == 2)
			color = Color.green;
		else if (layer == 3)
			color = Color.blue;
		else if (layer == 4)
			color = Color.red;
		
		radius = 0;
		visible = false;
		hit = false;
		
		termination = -1;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public boolean checkTermination()
	{
		if (termination == 1)
			return true;
		
		return false;
	}
	
	public void toggleVisible()
	{
		visible = !visible;
		termination++;
	}
	
	public boolean checkVisible()
	{
		return visible;
	}
	
	public boolean checkHit()
	{
		return hit;
	}
	
	private void smoothAppear()
	{
		if (radius <= circle_radius)
			radius += .1;
	}
	
	public int keyPressed(String circle)
	{
		int points = -1;
		
		if (layer == Integer.parseInt(circle) && termination != 1)
		{
			if (angle >= (360 - startAngle))
			{
				points = 0;
				hit = true;
				visible = false;
				termination = 1;
				if (angle >= (360 - pointAngles[0]))
				{
					points = 100;
					if (angle >= (360 - pointAngles[1]))
					{
						points = 250;
						if (angle >= (360 - pointAngles[2]))
						{
							points = 500;
						}
					}
				}
			}
		}
		
		return points;
	}
	
	public void move()
	{
		angle += .75;
		
		x = screenWidth/2 + (float) ((((screenHeight/8) - 5) * layer) * Math.cos(angle * (Math.PI/180)));
		y = screenHeight/2 + (float) ((((screenHeight/8) - 5) * layer) * Math.sin(angle * (Math.PI/180)));
		
		if (angle + .1 >= startAngle + 360 && angle - .1 <= startAngle + 360)
		{
			toggleVisible();
		}
	}
	
	public void drawCircle(Graphics g, Selector selector)
	{
		if (selector.getAngle() == angleStamp && selector.getRotations() == rotations)
			toggleVisible();
		if (visible)  
		{
			smoothAppear();
			move();
			g.setColor(color);
			g.fill(new Circle(x, y, radius));
			g.setColor(Color.black);
			g.draw(new Circle(x, y, radius));
		}
	}
}