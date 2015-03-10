import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TutorialState extends BasicGameState
{
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		
	}
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setColor(new Color(84, 84, 84));
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
	}

	public int getID()
	{
		return 5;
	}
}