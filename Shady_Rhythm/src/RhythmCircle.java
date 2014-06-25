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
	private int layer;
	private float radius;
	private static float circle_radius;
	private static int screenWidth;
	private static int screenHeight;
	private Color color;
	private int termination;
	
	RhythmCircle(float angle, int rotations, int layer, GameContainer gc)
	{
		screenWidth = gc.getWidth();
		screenHeight = gc.getHeight();
		
		angleStamp = angle;
		this.rotations = rotations;
		this.layer = layer;
		
		double csquared = 50 * 50;
		double ab = 2 * ((((screenHeight/10) - 5) * layer) * (((screenHeight/10) - 5) * layer));
		
		this.angle = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer); 
		startAngle = this.angle;
		
		x = screenWidth/2 + (float) (((screenHeight/10) * layer) * Math.cos(this.angle * (Math.PI/180)));
		y = screenHeight/2 + (float) (((screenHeight/10) * layer) * Math.sin(this.angle * (Math.PI/180)));
		
		if (layer == 1)
			color = Color.white;
		else if (layer == 2)
			color = Color.green;
		else if (layer == 3)
			color = Color.blue;
		else if (layer == 4)
			color = Color.red;
		else if (layer == 5)
			color = Color.yellow;
		
		circle_radius = 25;
		radius = 0;
		visible = false;
		
		termination = -1;
	}
	
	public int checkTermination()
	{
		return termination;
	}
	
	public void toggleVisible()
	{
		visible = !visible;
		termination++;
	}
	
	public boolean checkVisible()
	{
		if (color == Color.cyan)
			return false;
		else
			return visible;
	}
	
	private void smoothAppear()
	{
		if (radius <= circle_radius)
			radius += .1;
	}
	
	public int keyPressed(String circle, int points)
	{
		if (angle >= (360 - startAngle) && layer == Integer.parseInt(circle) && termination != 1)
		{
			color = Color.cyan;
			points++;
		}
		
		return points;
	}
	
	public void move()
	{
		angle += .75;
		
		x = screenWidth/2 + (float) ((((screenHeight/10) - 5) * layer) * Math.cos(angle * (Math.PI/180)));
		y = screenHeight/2 + (float) ((((screenHeight/10) - 5) * layer) * Math.sin(angle * (Math.PI/180)));
		
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
			g.setColor(Color.white);
			g.draw(new Circle(x, y, radius));
		}
	}
}