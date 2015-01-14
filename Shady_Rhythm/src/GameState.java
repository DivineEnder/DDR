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
	Rhythms rhythm;
	Thread loading;
	Loading loadingScreen;
	boolean paused;
	boolean pressAnyKey;
	
	public GameState(Rhythms engineRhythm)
	{
		rhythm = engineRhythm;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		engine = new Engine(gc, rhythm);
		loadingScreen = new Loading(gc.getScreenWidth(), gc.getScreenHeight());
		
		paused = false;
		pressAnyKey = false;
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		if (pressAnyKey)
		{
			pressAnyKey = false;
			engine.start();
		}
    }
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		final GameContainer pass = gc;
		loading = new Thread()
		{
			GameContainer gc = pass;
			public void run()
			{
				rhythm.readRhythm(gc);
			}
		};
		pressAnyKey = true;
		loading.start();
	}
	
	public void update(final GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			if (paused)
			{
				paused = false;
				engine.play();
			}
			else
			{
				engine.pause();
				paused = true;
			}
		}
		
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))
		{
			engine.reset();
			state.enterState(2);
		}
		
		if (loading.isAlive())
			loadingScreen.update();
		else if (!pressAnyKey)
			engine.update(gc);
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		if (loading.isAlive())
			loadingScreen.draw(g);
		else if (pressAnyKey)
			g.drawString("Press any key to continue", gc.getScreenWidth()/2 - g.getFont().getWidth("Press any key to continue"), gc.getScreenHeight()/2 - g.getFont().getHeight("Press any key to continue"));
		else
			engine.render(gc, g);
		
		if (paused)
			g.drawString("GAME IS PAUSED", gc.getScreenWidth()/2 - g.getFont().getWidth("GAME IS PAUSED"), gc.getScreenHeight()/2 - g.getFont().getHeight("GAME IS PAUSED"));
	}

	public int getID()
	{
		return 1;
	}
}
