import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class OptionsState extends BasicGameState
{
	//Creates variables to hold the widow width and height off the game
	int windowWidth;
	int windowHeight;
	
	//Creates a variable for a custom font use while drawing
	TrueTypeFont wordFont;
	
	PadInput pads;
	//Creates an instance of the StateHandler class
	StateHandler stateHandler;
	
	Circle[] optionCircles;
	String[] optionCircleStrings;
	
	Color[] optionCircleColors;
	Color[] selectorColors;
	
	int selected;
	
	//Creates a boolean to tell whether or not we are have selected something
	boolean selectedMode;
	float selectedOpacity;
	
	int[] timers;
	
	//Creates variables for the music and sound volumes
	float musicVolume;
	float soundVolume;
	
	//Constructor
	public OptionsState(StateHandler sh, PadInput p)
	{
		//Initializes the stateHandler instance to a universal class acorss all states
		stateHandler = sh;
		pads = p;
	}
	
	//Initializes various variables
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Gets the games width and height and then sets variables equal to each
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes a new java awt font from a file
		Font font = null;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("data/Fonts/belerenbold.ttf"));
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (FontFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		wordFont = new TrueTypeFont(font, false);
		
		//Initializes selectedMode to start off
		selectedMode = false;
		
		optionCircleStrings = new String[]{"Music Volume", "Sound FX Volume", "Exit"};
		
		optionCircles = new Circle[3];
		optionCircles[0] = new Circle(windowWidth/6, windowHeight/5, windowHeight/6);
		optionCircles[1] = new Circle(windowWidth/2, windowHeight/5, windowHeight/6);
		optionCircles[2] = new Circle(windowWidth * 5/6, windowHeight/5, windowHeight/6);
		
		optionCircleColors = new Color[]{Color.white, Color.white, Color.white};
		
		selectorColors = new Color[3];
		selectorColors[0] = new Color(173, 16, 16);
		selectorColors[1] = new Color(22, 161, 22);
		selectorColors[2] = new Color(10, 29, 145);
		
		selectedOpacity = 0;
		
		timers = new int[]{0, 0, 0};
		
		//Gets the current music and sound FX volume from the universal statehandler and then sets class variables to those values
		musicVolume = stateHandler.getMusicVolume();
		soundVolume = stateHandler.getSoundVolume();
	}
	
	//Checks for certain events and then updates variables accordingly
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets the input from the keyboard and puts it into a variable
		Input input = gc.getInput();
		
		if (selectedMode)
		{
			if (selectedOpacity < .75f)
				selectedOpacity += .01f;
			
			if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_K) || pads.input == 3)
			{
				stateHandler.setMusicVolume(musicVolume);
				stateHandler.setSoundVolume(soundVolume);
				selectedMode = false;
				for (int i = 0; i < optionCircleColors.length; i++)
					optionCircleColors[i] = Color.white;
			}
			
			if (input.isKeyDown(Input.KEY_H))
			{
				if (selected == 0)
				{
					if (musicVolume > .01f)
						musicVolume -= .01f;
				}
				else
				{
					if (soundVolume > .01f)
						soundVolume -= .01f;
				}
			}
			else if (input.isKeyDown(Input.KEY_K))
			{
				if (selected == 0)
				{
					if (musicVolume < 1)
						musicVolume += .01f;
				}
				else
				{
					if (soundVolume < 1)
						soundVolume += .01f;
				}
			}
		}
		else
		{
			if (selectedOpacity > 0)
				selectedOpacity -= .01f;
			
			if (input.isKeyDown(Input.KEY_H) || pads.input == 1)
			{
				timers[0]++;
				timers[1] = 0;
				timers[2] = 0;
			}
			else if (input.isKeyDown(Input.KEY_J) || pads.input == 2)
			{
				timers[1]++;
				timers[0] = 0;
				timers[2] = 0;
			}
			else if (input.isKeyDown(Input.KEY_K) || pads.input == 3)
			{
				timers[2]++;
				timers[0] = 0;
				timers[1] = 0;
			}
			else
			{
				for (int i = 0; i < timers.length; i++)
					timers[i] = 0;
			}
			
			if (timers[0] == 100)
			{
				selected = 0;
				optionCircleColors[0] = selectorColors[0];
				selectedMode = true;
				for (int i = 0; i < timers.length; i++)
					timers[i] = 0;
				
			}
			else if (timers[1] == 100)
			{
				selected = 1;
				optionCircleColors[1] = selectorColors[1];
				selectedMode = true;
				for (int i = 0; i < timers.length; i++)
					timers[i] = 0;
			}
			else if (timers[2] == 100)
			{
				for (int i = 0; i < timers.length; i++)
					timers[i] = 0;
				state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		
		//Clears the keyboard input record
		//Needed so that key presses do not get accidently passed between states
		input.clearKeyPressedRecord();
	}
	
	//Renders to the string
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Sets smoother rendering
		g.setAntiAlias(true);
		//Sets the graphics to draw using the font created above
		g.setFont(wordFont);
		//Sets the line width that the graphics should draw with
		g.setLineWidth(7);
		
		//Sets the color to gray
		g.setColor(new Color(84, 84, 84));
		//Draws the background gray color
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		for (int i = 0; i < optionCircles.length; i++)
		{
			g.setColor(optionCircleColors[i]);
			g.fill(optionCircles[i]);
			g.setColor(Color.black);
			g.drawString(optionCircleStrings[i], optionCircles[i].getCenterX() - g.getFont().getWidth(optionCircleStrings[i])/2, optionCircles[i].getCenterY() - g.getFont().getHeight(optionCircleStrings[i])/2);
			g.setColor(new Color(selectorColors[i].r, selectorColors[i].g, selectorColors[i].b, .25f));
			g.draw(optionCircles[i]);
			g.setColor(selectorColors[i]);
			g.drawArc(optionCircles[i].getX(), optionCircles[i].getY(), optionCircles[i].getWidth(), optionCircles[i].getHeight(), 270, 270 + (timers[i] * (360f/100f)));
		}
		
		g.setColor(new Color(0, 0, 0, selectedOpacity));
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		if (selectedMode)
		{
			g.setAntiAlias(false);
			
			g.setColor(new Color(selectorColors[selected].r, selectorColors[selected].g, selectorColors[selected].b, .25f + selectedOpacity));
			if (selected == 0)
			{
				g.fillArc(windowWidth/2 - windowHeight/5, windowHeight * 3/4 - windowHeight/5, windowHeight/5 * 2, windowHeight/5 * 2, 270, 270 + (musicVolume * 360));
			}
			else
			{
				g.fillArc(windowWidth/2 - windowHeight/5, windowHeight * 3/4 - windowHeight/5, windowHeight/5 * 2, windowHeight/5 * 2, 270, 270 + (soundVolume * 360));
			}
		}
	}

	//Identifies state id
	public int getID()
	{
		return 2;
	}
}