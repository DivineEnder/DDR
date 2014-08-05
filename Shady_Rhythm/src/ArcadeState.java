import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ArcadeState extends BasicGameState
{
	Controls controls;
	String controlInput;
	boolean songSelected;
	
	ArcadeState(Controls c)
	{
		controls = c;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		controlInput = "";
		songSelected = false;
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		String in = (String) controls.getKeyMapping().get(key);
		if (in != null)
			controlInput = in;
    }
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		
		if (songSelected)
			state.enterState(1);
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		g.drawString("Arcade State", 0, 0);
	}

	public int getID()
	{
		return 2;
	}
}
