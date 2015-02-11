import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;

public class LogoShape
{
	float startAngle;
	float endAngle;
	float radius;
	float centerX;
	float centerY;
	float lineLength;
	float rotateCenterX;
	float rotateCenterY;
	float rotateRadius;
	float rotateCounter;
	float arcDiffX;
	float arcDiffY;
	float lineDiffX1;
	float lineDiffY1;
	float lineDiffX2;
	float lineDiffY2;
	float opacity;
	boolean rotate;
	boolean rotated;
	Line line;
	Color color;
	
	LogoShape(Point regularPentagon, Point tiltedPentagon, Color c, Circle circle, int index)
	{
		centerX = tiltedPentagon.getX();
		centerY = tiltedPentagon.getY();
		radius = new Line(centerX, centerY, regularPentagon.getX(), regularPentagon.getY()).length();
		endAngle = (float) Math.atan((centerY - regularPentagon.getY())/(centerX - regularPentagon.getX()));
		if (index == 0 || index == 4)
			endAngle = (float) (endAngle * (180/Math.PI)) + 180;
		else
			endAngle = (float) (endAngle * (180/Math.PI));
		startAngle = endAngle - 84;
		
		
		Point nextVerticie = new Point((float) (circle.getCenterX() + circle.radius * Math.cos(((index+1) * 2 * Math.PI / 5) + (270 * Math.PI/180))), (float) (circle.getCenterY() + circle.radius * Math.sin(((index+1) * 2 * Math.PI / 5) + (270 * Math.PI/180))));
		line = new Line(regularPentagon.getX(), regularPentagon.getY(), nextVerticie.getX(), nextVerticie.getY());
		
		rotateCenterX = circle.getCenterX();
		rotateCenterY = circle.getCenterY();
		rotateRadius = circle.radius;
		rotateCounter = 0;
		rotate = false;
		rotated = false;
		
		color = c;
		opacity = 1;
	}
	
	public void rotation()
	{
		if (rotate && rotateCounter != 100)
		{
			centerX += arcDiffX/100;
			centerY += arcDiffY/100;
			startAngle += 15;//+= diffX/2;
			endAngle += 15;//+= diffY/2;
			
			line = new Line(line.getX1() + lineDiffX1/100, line.getY1() + lineDiffY1/100, line.getX2() + lineDiffX2/100, line.getY2() + lineDiffY2/100);
			
			rotateCounter++;
		}
		else if (rotateCounter == 100)
		{
			rotate = false;
			rotated = true;
		}
		
		//startAngle += 10;//+= velocity; //+= diffX/100;
		//endAngle += 10;//+= velocity; //+= diffY/100;
	}
	
	public void rotate(int index)
	{
		rotate = true;
		arcDiffX = rotateCenterX - centerX;
		arcDiffY = rotateCenterY - centerY;
		lineDiffX1 = ((((float) (Math.cos(((index * 72) + 270) * Math.PI/180))) * radius) + rotateCenterX) - line.getX1();
		lineDiffY1 = ((((float) (Math.sin(((index * 72) + 270) * Math.PI/180))) * radius) + rotateCenterY) - line.getY1();
		lineDiffX2 = rotateCenterX - line.getX2();
		lineDiffY2 = rotateCenterY - line.getY2();
	}
	
	public void draw(Graphics g)
	{	
		g.setAntiAlias(true);
		g.setLineWidth(5);
		
		g.setColor(new Color(color.r, color.g, color.b, color.a + .15f));
		g.draw(line);
		
		for (int i = 1; i <= 5; i++)
		{
			g.setColor(new Color(color.r, color.g, color.b, color.a * i));
			float factor = 15;
			for (int j = 6 - i; j > 0; j--)
				factor -= j;
			if (rotated)
				g.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle + (factor * 80/15), endAngle);
			else
				g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle + (factor * 80/15), endAngle);
		}
		
		if (rotated)
		{
			g.setColor(new Color(0, 0, 0, opacity));
			g.fill(new Circle(centerX, centerY, radius - 5));
			if (opacity > 0)
				opacity -= .01;
		}
		
		if (!rotated)
		{
			g.setColor(new Color(color.r, color.g, color.b));
			g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 4, endAngle);
		}
			
		/*g.setColor(new Color(color.r, color.g, color.b));
		g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 4, endAngle);
			
		g.setColor(new Color(color.r, color.g, color.b, color.a + .7f));
		g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 10, endAngle - 4);
			
		g.setColor(new Color(color.r, color.g, color.b, color.a + .5f));
		g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 20, endAngle - 10);
			
		g.setColor(new Color(color.r, color.g, color.b, color.a + .3f));
		g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 40, endAngle - 20);
			
		g.setColor(new Color(color.r, color.g, color.b, color.a + .1f));
		g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, endAngle - 65, endAngle - 40);*/
	}
}
