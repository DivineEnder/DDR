import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class LogoState extends BasicGameState
{
	int radius;
	int x;
	int y;
	boolean transition;
	float counter = 0;
	int[][] angles;
	int[][] regularAngles;
	int change;
	int lineChangeY;
	int lineChangeX;
	float opacity;
	float opacity2;
	Music testSound;
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		radius = 250;
		x = gc.getWidth()/2 - radius;
		y = gc.getHeight()/2 - radius;
		change = radius/2;
		lineChangeY = 0;
		lineChangeX = radius/2;
		transition = false;
		angles = new int[2][2];
		angles[0][0] = 180;
		angles[0][1] = 270;
		angles[1][0] = 0;
		angles[1][1] = 90;
		regularAngles = new int[2][2];
		regularAngles[0][0] = 180;
		regularAngles[0][1] = 270;
		regularAngles[1][0] = 0;
		regularAngles[1][1] = 90;
		opacity = .5f;
		opacity2 = .5f;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (counter == 100)
			transition = true;
			
		if (transition)
		{
			if (change >= 0)
				change -= radius/2/45;
			if (lineChangeY <= radius)
				lineChangeY += radius/2/30;
			if (lineChangeX <= radius)
				lineChangeX += radius/2/30;
			for (int i = 0; i < angles.length; i++)
				for (int j = 0; j < angles[i].length; j++)
					angles[i][j]--;
			for (int i = 0; i < regularAngles.length; i++)
				for (int j = 0; j < regularAngles[i].length; j++)
					regularAngles[i][j] += 2;
			if (angles[0][0] == 90)
				transition = false;
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(0, new FadeOutTransition(Color.black, 0), new FadeInTransition(Color.black, 750));
		if (counter == 300)
			state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		
		counter++;
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		g.setLineWidth(5);
		
		g.setColor(Color.blue);
		g.drawArc(gc.getWidth()/2 - radius, gc.getHeight()/2 - radius, radius*2, radius*2, regularAngles[0][0], regularAngles[0][1]);
		g.setColor(Color.yellow);
		g.drawArc(gc.getWidth()/2 - radius, gc.getHeight()/2 - radius, radius*2, radius*2, regularAngles[1][0], regularAngles[1][1]);
		
		g.setColor(Color.red);
		g.drawArc(x + change, y, radius*2, radius*2, angles[0][0], angles[0][1]);
		g.setColor(Color.green);
		g.drawArc(x - change, y, radius*2, radius*2, angles[1][0], angles[1][1]);
		
		Color color = new Color(255, 255, 255, opacity);
		g.setColor(color);
		g.drawLine(x + radius, y, x + radius, y + radius*2);
		Color color2 = new Color(255, 255, 255, opacity2);
		g.setColor(color2);
		g.drawLine(x, y + radius, x + radius/2, y + radius);
		g.drawLine(x + radius + radius/2, y + radius, x + radius*2, y + radius);
		
		if (lineChangeY >= radius/2)
			opacity2 -= .05f;
		if (lineChangeY >= radius)
			opacity -= .05f;
		color = new Color(255, 255, 255, opacity);
		g.setColor(color);
		g.drawLine(x + radius + lineChangeX, y + lineChangeY, x + change, y + radius*2 - lineChangeY);
		
		//g.setColor(new Color(0, 0, 0, 1 - (counter/100 + .1f)));
		//g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
	}

	public int getID()
	{
		return 6;
	}
	
}
