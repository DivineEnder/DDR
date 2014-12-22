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
	Thread loadSong;
	Controls controls;
	HashMap specialInput;
	ArrayList<String> sInput;
	Loading loadingScreen;
	boolean pressAnyKey;
	
	public GameState(Controls control)
	{
		controls = control;
		pressAnyKey = false;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		engine = new Engine(gc);
		loadSong = new Thread();
		specialInput = new HashMap<Integer, String>();
		loadingScreen = new Loading(gc.getScreenWidth(), gc.getScreenHeight());
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		String in = (String) controls.getKeyMapping().get(key);
		if (in != null)
		{
			specialInput.put(key, controls.getKeyMapping().get(key));
			sInput.add((String) specialInput.get(key));
		}
		
		if (pressAnyKey)
		{
			pressAnyKey = false;
			engine.start();
		}
    }
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (specialInput.get(key) != null)
			specialInput.remove(key);
	}
	
	public void update(final GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		sInput = new ArrayList<String>(specialInput.values());
		
		engine.update(gc, sInput);
		
		if (input.isKeyPressed(Input.KEY_W))
		{
			loadSong = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Darude - Sandstorm 8bit Remix.wav", "data/Music/song.txt", gc);
				}
			};
			loadSong.start();
			pressAnyKey = true;
		}
		
		if (input.isKeyPressed(Input.KEY_Q))
		{
			loadSong = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Am I Wrong.wav", "data/Music/Am I Wrong.txt", gc);
				}
			};
			loadSong.start();
			pressAnyKey = true;
		}
		
		if (input.isKeyPressed(Input.KEY_E))
		{
			loadSong = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Mowe - Pump Up The Jam Mixdown.wav", "data/Music/Mowe - Pump Up The Jam Mixdown.txt", gc);
				}
			};
			loadSong.start();
			pressAnyKey = true;
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			engine.reset();
			engine = new Engine(gc);
			state.enterState(0, new FadeOutTransition(Color.black, 0), new FadeInTransition(Color.black, 750));
		}
		
		if (loadSong.isAlive())
			loadingScreen.update();
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		if (loadSong.isAlive())
		{
			loadingScreen.draw(g);
		}
		else if (pressAnyKey)
		{
			g.setColor(Color.white);
			g.drawString("Press Any Key to continue", gc.getScreenWidth()/2, gc.getScreenHeight()/2);
		}
		else
			engine.render(gc, g);
	}

	public int getID()
	{
		return 1;
	}
}
