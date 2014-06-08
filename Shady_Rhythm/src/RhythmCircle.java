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
		
		color = Color.orange;
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
		return visible;
	}
	
	private void smoothAppear()
	{
		if (radius <= circle_radius)
			radius += .1;
	}
	
	public void keyPressed(char key)
	{
		if (angle >= (360 - startAngle))
		{
			if (key == 'a' && layer == 1)
				color = Color.white;
			else if (key == 'f' && layer == 2)
				color = Color.green;
			else if (key == 'r' && layer == 3)
				color = Color.blue;
			else if (key == 'd' && layer == 4)
				color = Color.red;
			else if (key == 'g' && layer == 5)
				color = Color.yellow;
		}
	}
	
	public void move()
	{
		angle += .75;
		if (angle >= 360)
			angle -= 360;
		rotations++;
		
		x = screenWidth/2 + (float) ((((screenHeight/10) - 5) * layer) * Math.cos(angle * (Math.PI/180)));
		y = screenHeight/2 + (float) ((((screenHeight/10) - 5) * layer) * Math.sin(angle * (Math.PI/180)));
		
		if (angle + .1 >= startAngle && angle - .1 <= startAngle)
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
		}
	}
}