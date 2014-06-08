import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class OptionsState extends BasicGameState
{
	int keyChanging;
	ArrayList<String> controlKeys;
	Controls controls;
	String[] keyString;
	Color[] keyColor;
	
	public OptionsState(Controls control)
	{
		controls = control;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		controlKeys = new ArrayList<String>();
		keyString = new String[5];
		keyColor = new Color[5];
		keyChanging = -1;
		
		keyColor[0] = Color.white;
		keyColor[1] = Color.green;
		keyColor[2] = Color.blue;
		keyColor[3] = Color.red;
		keyColor[4] = Color.yellow;
	}
	
	private void colorReset()
	{
		keyColor[0] = Color.white;
		keyColor[1] = Color.green;
		keyColor[2] = Color.blue;
		keyColor[3] = Color.red;
		keyColor[4] = Color.yellow;
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		if (keyChanging != -1 && key != Input.KEY_ESCAPE)
		{
			controls.setControls(key, keyChanging);
			keyChanging = -1;
			colorReset();
		}
    }
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		int rectPosX = gc.getScreenWidth()/6;
		int rectPosY = gc.getScreenHeight()/10 * 3;
		
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		
		if (xpos > rectPosX * 1 && xpos < rectPosX + 100 && ypos > rectPosY && ypos < ypos + 100)
		{
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				colorReset();
				keyChanging = 0;
			}
		}
		
		else if (xpos > rectPosX * 2 && xpos < rectPosX * 2 + 100 && ypos > rectPosY && ypos < ypos + 100)
		{
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				colorReset();
				keyChanging = 1;
			}
		}
		
		if (xpos > rectPosX * 3 && xpos < rectPosX * 3 + 100 && ypos > rectPosY && ypos < ypos + 100)
		{
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				colorReset();
				keyChanging = 2;
			}
		}
		
		if (xpos > rectPosX * 4 && xpos < rectPosX * 4 + 100 && ypos > rectPosY && ypos < ypos + 100)
		{
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				colorReset();
				keyChanging = 3;
			}
		}
		
		if (xpos > rectPosX * 5 && xpos < rectPosX * 5 + 100 && ypos > rectPosY && ypos < ypos + 100)
		{
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			{
				colorReset();
				keyChanging = 4;
			}
		}
		
		if (keyChanging != -1)
			keyColor[keyChanging] = Color.cyan;
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			keyChanging = -1;
			state.enterState(0, new FadeOutTransition(Color.black, 0), new FadeInTransition(Color.black, 750));
			colorReset();
		}
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		keyString = controls.getKeyString();
		
		g.setColor(keyColor[0]);
		g.fill(new Rectangle(gc.getScreenWidth()/6 * 1, gc.getScreenHeight()/10 * 3, 100, 100));
		g.drawString(keyString[0], gc.getScreenWidth()/6 * 1, gc.getScreenHeight()/10 * 3 + 110);
		g.setColor(keyColor[1]);
		g.fill(new Rectangle(gc.getScreenWidth()/6 * 2, gc.getScreenHeight()/10 * 3, 100, 100));
		g.drawString(keyString[1], gc.getScreenWidth()/6 * 2, gc.getScreenHeight()/10 * 3 + 110);
		g.setColor(keyColor[2]);
		g.fill(new Rectangle(gc.getScreenWidth()/6 * 3, gc.getScreenHeight()/10 * 3, 100, 100));
		g.drawString(keyString[2], gc.getScreenWidth()/6 * 3, gc.getScreenHeight()/10 * 3 + 110);
		g.setColor(keyColor[3]);
		g.fill(new Rectangle(gc.getScreenWidth()/6 * 4, gc.getScreenHeight()/10 * 3, 100, 100));
		g.drawString(keyString[3], gc.getScreenWidth()/6 * 4, gc.getScreenHeight()/10 * 3 + 110);
		g.setColor(keyColor[4]);
		g.fill(new Rectangle(gc.getScreenWidth()/6 * 5, gc.getScreenHeight()/10 * 3, 100, 100));
		g.drawString(keyString[4], gc.getScreenWidth()/6 * 5, gc.getScreenHeight()/10 * 3 + 110);
		
		g.drawString("Options State", 0, 0);
	}

	public int getID()
	{
		return 4;
	}
}