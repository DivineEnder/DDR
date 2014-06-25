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
	Thread thread;
	Controls controls;
	HashMap specialInput;
	ArrayList<String> sInput;
	
	public GameState(Controls control)
	{
		controls = control;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		engine = new Engine(gc);
		thread = new Thread();
		specialInput = new HashMap<Integer, String>();
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
			thread = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Rather Be.wav", "data/Music/Rather Be.txt", gc);
				}
			};
			thread.start();
		}
		
		if (input.isKeyPressed(Input.KEY_Q))
		{
			thread = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Am I Wrong.wav", "data/Music/Am I Wrong.txt", gc);
				}
			};
			thread.start();
		}
		
		if (input.isKeyPressed(Input.KEY_E))
		{
			thread = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/Music/Mowe - Pump Up The Jam Mixdown.wav", "data/Music/Mowe - Pump Up The Jam Mixdown.txt", gc);
				}
			};
			thread.start();
		}
		
		//if (input.isKeyPressed(Input.KEY_P))
		//{
		//	thread = new Thread()
		//	{
		//		public void run()
		//		{
		//			engine.selectSong("data/Music/Technotronic - Pump Up The Jam.wav", "data/Music/Technotronic - Pump Up The Jam.txt", gc);
		//		}
		//	};
		//	thread.start();
		//}
		
		if (input.isKeyDown(Input.KEY_R) && input.isKeyDown(Input.KEY_O) && input.isKeyDown(Input.KEY_L))
		{
			thread = new Thread()
			{
				public void run()
				{
					engine.selectSong("data/default.wav", "data/Am I Wrong.txt", gc);
				}
			};
			thread.start();
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			engine.reset();
			engine = new Engine(gc);
			state.enterState(0, new FadeOutTransition(Color.black, 0), new FadeInTransition(Color.black, 750));
		}
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		if (thread.isAlive())
			g.drawString("Loading", 0, 0);
		else
			engine.render(gc, g);
	}

	public int getID()
	{
		return 1;
	}
}
