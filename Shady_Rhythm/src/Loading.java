import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Loading
{
	private int[][] angles;
	private int x;
	private int y;
	private int radius;
	
	Loading(int screenWidth, int screenHeight)
	{
		angles = new int[4][4];
		angles[0][0] = 0;
		angles[0][1] = 90;
		angles[1][0] = 90;
		angles[1][1] = 180;
		angles[2][0] = 180;
		angles[2][1] = 270;
		angles[3][0] = 270;
		angles[3][1] = 0;
		radius = screenHeight/2;
		x = screenWidth/2 - radius/2;
		y = screenHeight/2 - radius/2;
	}
	
	public void update()
	{
		int mod = 1;
		for (int i = 0; i < angles.length; i++)
		{
			for (int j = 0; j < angles[i].length; j++)
			{
				angles[i][j] += mod;
			}
			if (mod == 1)
				mod = -1;
			else
				mod = 1;
		}
	}
	
	public void draw(Graphics g)
	{
		g.setLineWidth(5);
		
		g.setColor(Color.yellow);
		g.drawArc(x, y, radius, radius, angles[0][0], angles[0][1]);
		g.setColor(Color.green);
		g.drawArc(x, y, radius, radius, angles[1][0], angles[1][1]);
		g.setColor(Color.blue);
		g.drawArc(x, y, radius, radius, angles[2][0], angles[2][1]);
		g.setColor(Color.red);
		g.drawArc(x, y, radius, radius, angles[3][0], angles[3][1]);
	}
}
