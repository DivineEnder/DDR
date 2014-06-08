import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MenuState extends BasicGameState
{
	int selector;  // This means whether to go to story, arcade selector
	Animation backgroundAnimation;
	boolean arrow;
	Controls controls;
	String specialInput;
	
	public MenuState(Controls control)
	{
		controls = control;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		selector = -1;
		backgroundAnimation = new Animation();
		specialInput = "";
		arrow = false;
		
		for (int i = 1; i <= 6; i++)
			backgroundAnimation.addFrame(new Image("data/Background_" + i + ".png"), 250);
		for (int i = 6; i >= 1; i--)
			backgroundAnimation.addFrame(new Image("data/Background_" + i + ".png"), 250);
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		String in = (String) controls.getKeyMapping().get(key);
		if (in != null)
			specialInput = (String) controls.getKeyMapping().get(key);
    }
	
	public void update(GameContainer gc, StateBasedGame state, int delta)throws SlickException
	{
		Input input = gc.getInput();
	
		int screenWidth = gc.getWidth();
		int screenHeight = gc.getHeight();
		
		int xpos = Mouse.getX();
		int ypos = gc.getHeight() - Mouse.getY();
		
		if (xpos > (screenWidth/5) && xpos < (screenWidth/5 + screenWidth * 3/5) && ypos > (screenHeight * 2/5) && ypos < (screenHeight * 2/5 + screenHeight * 3/25))
		{
			selector = 0;
			
			if(input.isMouseButtonDown(0))
			{
				arrow = false;
				state.enterState(1, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		else if (xpos > (screenWidth/5) && xpos < (screenWidth/5 + screenWidth * 3/5) && ypos > (screenHeight * .4 +  screenHeight * 3/25) && ypos < (screenHeight * 2/5 + screenHeight * 6/25))
		{
			selector = 1;
			
			if(input.isMouseButtonDown(0))
			{
				arrow = false;
				state.enterState(2, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		else if (xpos > (screenWidth/5) && xpos < (screenWidth/5 + screenWidth * 3/5) && ypos > (screenHeight * .4 +  screenHeight * 6/25) && ypos < (screenHeight * 2/5 + screenHeight * 9/25))
		{
			selector = 2;
			
			if(input.isMouseButtonDown(0))
			{
				arrow = false;
				state.enterState(3, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		else if (xpos > (screenWidth/5) && xpos < (screenWidth/5 + screenWidth * 3/10) && ypos > (screenHeight * .4 +  screenHeight * 9/25) && ypos < (screenHeight * 2/5 + screenHeight * 12/25))
		{
			selector = 3;
			
			if(input.isMouseButtonDown(0))
			{
				arrow = false;
				state.enterState(4, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		else if (xpos > (screenWidth/5 + screenWidth * 3/10) && xpos < (screenWidth/5 + screenWidth * 3/5) && ypos > (screenHeight * .4 +  screenHeight * 9/25) && ypos < (screenHeight * 2/5 + screenHeight * 12/25))
		{
			selector = 4;
			
			if(input.isMouseButtonDown(0))
				System.exit(0);
		}
		else
		{
			if(!arrow)
				selector = -1;
		}
		if(input.isKeyPressed(Input.KEY_UP))
		{
			selector--;
			if (selector == -1)
				selector = 4;
			
			arrow = true;
		}
		if(input.isKeyPressed(Input.KEY_DOWN))
		{
			selector++;
			if (selector == 5)
				selector = 0;
			
			arrow = true;
		}
		if(input.isKeyPressed(Input.KEY_RIGHT))
		{
			if (selector == 3)
				selector = 4;
			
			arrow = true;
		}
		if(input.isKeyPressed(Input.KEY_LEFT))
		{
			if(selector == 4)
				selector = 3;
			
			arrow = true;
		}
		if (specialInput.equals("1"))
		{
			if (selector != 0)
			{
				selector = 0;
				arrow = true;
			}
			else
			{
				arrow = false;
				state.enterState(1, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
			
			specialInput = "";
		}
		if (specialInput.equals("2"))
		{
			if (selector != 1)
			{
				selector = 1;
				arrow = true;
			}
			else
			{
				arrow = false;
				state.enterState(2, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
			
			specialInput = "";
		}
		if (specialInput.equals("3"))
		{
			if (selector != 2)
			{
				selector = 2;
				arrow = true;
			}
			else
			{
				arrow = false;
				state.enterState(3, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
			
			specialInput = "";
		}
		if (specialInput.equals("4"))
		{
			if (selector != 3)
			{
				selector = 3;
				arrow = true;
			}
			else
			{
				arrow = false;
				state.enterState(4, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
			
			specialInput = "";
		}
		if (specialInput.equals("5"))
		{
			if (selector != 4)
				selector = 4;
			else
				System.exit(0);
			
			arrow = true;
			specialInput = "";
		}

		
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		//background = background.getScaledCopy(gc.getWidth(),gc.getWidth());
		g.drawAnimation(backgroundAnimation, 0, 0);
		
		int screenWidth = gc.getWidth();
		int screenHeight = gc.getHeight();
		
		float rectx = screenWidth/5; // The two is for the extra bezel to put a box behind it
		float height = screenHeight * 3/25;
		float width = screenWidth * 3/5;
		
		Color white = new Color(255, 255, 255, 64);
		Color green = new Color(23, 110, 24, 64);
		Color blue = new Color(0, 10, 255, 64);
		Color red = new Color(255, 0, 18, 64);
		Color yellow = new Color(255, 239, 0, 64);

		g.setColor(white);
		g.fillRect(rectx, screenHeight*2/5, width, height);
		g.setColor(green);
		g.fillRect(rectx, screenHeight*2/5 + height, width, height);
		g.setColor(blue);
		g.fillRect(rectx, screenHeight*2/5 + 2 * height, width, height);
		g.setColor(red);
		g.fillRect(rectx, screenHeight*2/5 + 3 * height, width/2, height);
		g.setColor(yellow);
		g.fillRect(rectx + width/2, screenHeight*2/5 + 3 * height, width/2 , height);
		
		Color whiteMod = new Color(255, 255, 255, 128);
		Color greenMod = new Color(23, 110, 24, 200);
		Color blueMod = new Color(0, 10, 255, 128);
		Color redMod = new Color(255, 0, 18, 128);
		Color yellowMod = new Color(255, 239, 0, 128);
		
		if (selector == 0)
		{
			g.setColor(whiteMod);
			g.fillRect(rectx, screenHeight*2/5, width, height);
		}
		else if (selector == 1)
		{
			g.setColor(greenMod);
			g.fillRect(rectx, screenHeight*2/5 + height, width, height);
		}
		else if (selector == 2)
		{
			g.setColor(blueMod);
			g.fillRect(rectx, screenHeight*2/5 + 2* height, width, height);
		}
		else if (selector == 3)
		{
			g.setColor(redMod);
			g.fillRect(rectx, screenHeight*2/5 + 3 * height, width/2, height);
		}
		else if (selector == 4)
		{
			g.setColor(yellowMod);
			g.fillRect(rectx + width/2, screenHeight*2/5 + 3 * height, width/2 , height);
		}
		
			
	}
	
	public int getID(){
		return 0;
	}
}