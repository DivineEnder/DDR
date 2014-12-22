import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameState extends BasicGameState
{
	Engine engine;
	ArrayList<Rhythms> storyRhythmList;
	
	public void seedStoryRhythms(GameContainer gc)
	{
		storyRhythmList.add(new Rhythms("Am I Wrong", gc));
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		storyRhythmList = new ArrayList<Rhythms>();
		seedStoryRhythms(gc);
		engine = new Engine(gc, storyRhythmList.get(0));
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		
    }
	
	public void update(final GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))
		{
			engine = new Engine(gc, storyRhythmList.get(0));
			state.enterState(0);
		}
		
		if (input.isKeyPressed(Input.KEY_1))
			engine.start();
		
		engine.update(gc);
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		engine.render(gc, g);
	}

	public int getID()
	{
		return 1;
	}
}
