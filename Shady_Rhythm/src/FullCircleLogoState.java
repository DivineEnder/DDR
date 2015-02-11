import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class FullCircleLogoState extends BasicGameState 
{
	LogoShape[] shapes;
	
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		Color[] colors = new Color[5];
		colors[0] = new Color(0, 255, 255, .05f);
		colors[1] = new Color(0, 0, 255, .05f);
		colors[2] = new Color(255, 0, 0, .05f);
		colors[3] = new Color(255, 255, 0, .05f);
		colors[4] = new Color(0, 255, 0, .05f);
		
		float x = gc.getWidth()/2;
		float y = gc.getHeight()/2;
		float r = gc.getHeight()/4;
		
		Point[] regularPentagonVerticies = new Point[5];
		Point[] tiltedPentagonVerticies = new Point[5];
		for (int i = 0; i < tiltedPentagonVerticies.length; i++)
		{
			tiltedPentagonVerticies[i] = new Point((float) (x + (r-30) * Math.cos(i * 2 * Math.PI / 5)), (float) (y + (r-30) * Math.sin(i * 2 * Math.PI / 5)));
		}
		for (int i = 0; i < regularPentagonVerticies.length; i++)
		{
			regularPentagonVerticies[i] = new Point((float) (x + r * Math.cos((i * 2 * Math.PI / 5) + (270 * Math.PI/180))), (float) (y + r * Math.sin((i * 2 * Math.PI / 5) + (270 * Math.PI/180))));
		}
		
		shapes = new LogoShape[5];
		for (int i = 0; i < shapes.length; i++)
		{
			shapes[i] = new LogoShape(regularPentagonVerticies[i], tiltedPentagonVerticies[i], colors[i], new Circle(gc.getWidth()/2, gc.getHeight()/2, gc.getHeight()/4), i);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setColor(Color.white);
		g.drawString("Full Circle", gc.getWidth()/2 - g.getFont().getWidth("Full Circle")/2, gc.getHeight()/2 - g.getFont().getHeight("Full Circle")/2);
		
		for (int i = 0; i < shapes.length; i++)
			shapes[i].draw(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_J))
		{
			for (int i = 0; i < shapes.length; i++)
				shapes[i].rotate(i);
		}
		
		for (int i = 0; i < shapes.length; i++)
			shapes[i].rotation();
	}
	
	public int getID()
	{
		return 7;
	}
}
