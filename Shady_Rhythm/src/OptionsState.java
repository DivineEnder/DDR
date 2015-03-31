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
	
	//Creates a variable to tell what is selected
	int selected;
	//Creates a boolean to tell whether or not we are have selected something
	boolean selectedMode;
	
	//Creates variables for the strings to display to the screen
	String music;
	String sound;
	String exit;
	
	//Creates an array to hold the individual colors for the strings
	Color[] stringColors;
	
	//Creates variables for the sound FX and music bars on the screen
	RoundedRectangle musicVolumeLine;
	RoundedRectangle soundVolumeLine;
	
	//Creates variables for the sound FX and music bar selectors on the screen
	RoundedRectangle musicVolumeSelector;
	RoundedRectangle soundVolumeSelector;
	
	//Creates the two selector circles
	Circle[] selector;
	//Creates boolean that determines whether or not to fill the selector circles
	boolean[] fill;
	
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
		
		//Initializes a new font to use for drawing to the screen
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("data/Fonts/belerenbold.ttf"));
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (FontFormatException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		wordFont = new TrueTypeFont(font, false);
		
		//Initializes selected to start at the top
		selected = 0;
		//Initializes selectedMode to start off
		selectedMode = false;
		
		//Initializes the strings drawn
		music = "Music Volume";
		sound = "Sound FX Volume";
		exit = "Exit";
		
		//Initializes the colors of the strings to be white at first
		stringColors = new Color[]{Color.white, Color.white, Color.white};
		
		//Initializes the volume bars drawn
		musicVolumeLine = new RoundedRectangle(windowWidth/7 + wordFont.getWidth(music), windowHeight/10 + wordFont.getHeight(music)/2, windowWidth * 3/5, 5, 6);
		soundVolumeLine = new RoundedRectangle(windowWidth/7 + wordFont.getWidth(music), windowHeight/5 + wordFont.getHeight(sound)/2, windowWidth * 3/5, 5, 6);
		
		//Initializes the two circles used as a selector
		selector = new Circle[2];
		selector[0] = new Circle(windowWidth/20 - 30, windowHeight/10 + wordFont.getHeight(sound)/2, 15);
		selector[1] = new Circle(musicVolumeLine.getMaxX() + 30, windowHeight/10 + wordFont.getHeight(sound)/2, 15);
		
		//Initializes the fill component of the two selector circles to be false
		fill = new boolean[]{false, false};
		
		//Gets the current music and sound FX volume from the universal statehandler and then sets class variables to those values
		musicVolume = stateHandler.getMusicVolume();
		soundVolume = stateHandler.getSoundVolume();
	}
	
	//Checks for certain events and then updates variables accordingly
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets the input from the keyboard and puts it into a variable
		Input input = gc.getInput();
		
		//Checks to make sure you don't have one of the options selected
		if (!selectedMode)
		{
			//Checks for J being pressed on the keyboard
			if (input.isKeyPressed(Input.KEY_J))
			{	
				//Moves the selector down
				selected++;
				//Moves the selector up to the top if it is at the bottom of the list
				if (selected > 2)
					selected -= 3;
				
				//Changes where the selector circles are based on what is selected
				if (selected == 0)
				{
					selector[0] = new Circle(windowWidth/20 - 30, windowHeight/10 + wordFont.getHeight(music)/2, 15);
					selector[1] = new Circle(musicVolumeLine.getMaxX() + 30, windowHeight/10 + wordFont.getHeight(music)/2, 15);
				}
				else if (selected == 1)
				{
					selector[0] = new Circle(windowWidth/20 - 30, windowHeight/5 + wordFont.getHeight(sound)/2, 15);
					selector[1] = new Circle(musicVolumeLine.getMaxX() + 30, windowHeight/5 + wordFont.getHeight(sound)/2, 15);
				}
				else
				{
					selector[0] = new Circle(windowWidth/20 - 30, windowHeight * 9/10 + wordFont.getHeight(exit)/2, 15);
					selector[1] = new Circle(windowWidth/20 + wordFont.getWidth(exit) + 30, windowHeight * 9/10 + wordFont.getHeight(exit)/2, 15);
				}
			}
			
			//Checks for the H key pressed
			if (input.isKeyDown(Input.KEY_H))
			{
				//Fills the left hand circle
				fill[0] = true;
				//Sets the color of the currently selected item to be the filled circles color
				stringColors[selected] = new Color(173, 16, 16);
			}
			//Otherwise do not fill the left circle and return the currently selected string to its white color
			else
			{
				fill[0] = false;
				//Makes sure that the other fill is not changing the color before it sets it to white
				if (!fill[1])
					stringColors[selected] = Color.white;
			}
			//Checks for the K key pressed
			if (input.isKeyDown(Input.KEY_K))
			{
				//Fills the right hand circle
				fill[1] = true;
				//Sets the color of the currently selected item to be the filled circles color
				stringColors[selected] = new Color(10, 29, 145);
			}
			//Otherwise do not fill the right circle and return the currently selected string to its white color
			else
			{
				fill[1] = false;
				//Makes sure that the other fill is not changing the color before it sets it to white
				if (!fill[0])
					stringColors[selected] = Color.white;
			}
			
			//Checks if both the H and K keys are down by checking their fills
			if (fill[0] && fill[1])
			{
				if (selected == 0)
				{
					//Turns on selected mode
					selectedMode = true;
					//Sets the color of the currently selected item to be the green mix in our color scheme
					stringColors[selected] = new Color(22, 161, 22);
				}
				else if (selected == 1)
				{
					//Turns on selected mode
					selectedMode = true;
					//Sets the color of the currently selected item to be the green mix in our color scheme
					stringColors[selected] = new Color(22, 161, 22);
				}
				//Leaves the state if exit is selected
				else
					state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
		}
		//Deals with when you have an option selected
		else
		{
			//Turns off selected mode if you press J
			if (input.isKeyPressed(Input.KEY_J))
			{
				selectedMode = false;
				//Sets the stateHandlers volume to be equal to the changed volumes here
				stateHandler.setMusicVolume(musicVolume);
				stateHandler.setSoundVolume(soundVolume);
			}
			
			//Checks to see if H was pressed
			if (input.isKeyPressed(Input.KEY_H))
			{
				//Checks to see if volume is selected
				if (selected == 0)
				{
					//Decreases the music volume if it is greater than zero
					if (musicVolume > 0)
						musicVolume -= .05f;
					System.out.println(musicVolume);
					
				}
				//Checks to see if sound FX is selected
				else
				{
					//Decreases the sound FX volume if it is greater than zero
					if (soundVolume > 0)
						soundVolume -= .05f;
					System.out.println(soundVolume);
				}
			}
			
			//Checks to see if the K key was pressed
			if (input.isKeyPressed(Input.KEY_K))
			{
				//Checks to see if the music was selected
				if (selected == 0)
				{
					//Increases the music volume if it is less than 1
					if (musicVolume < 1)
						musicVolume += .05f;
				}
				//Checks to see if the sound FX was selected
				else
				{
					//Increases the sound volume if it is less than 1
					if (soundVolume < 1)
						soundVolume += .05f;
				}
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
		g.setLineWidth(1);
		
		//Sets the color to gray
		g.setColor(new Color(84, 84, 84));
		//Draws the background gray color
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		//Sets the color to the strings color from the array
		g.setColor(stringColors[0]);
		//Draws the string to the screen
		g.drawString(music, windowWidth/20, windowHeight/10);
		//Sets the color to the strings color from the array
		g.setColor(stringColors[1]);
		//Draws the string to the screen
		g.drawString(sound, windowWidth/20, windowHeight * 2/10);
		//Sets the color to the strings color from the array
		g.setColor(stringColors[2]);
		//Draws the string to the screen
		g.drawString(exit, windowWidth/20, windowHeight * 9/10);
		
		//Sets the color of the bar to be the same as the string
		g.setColor(stringColors[0]);
		//Draws the volume bar for music
		g.draw(musicVolumeLine);
		//Fills the music bar to a percentage of the max volume
		g.fill(new RoundedRectangle(musicVolumeLine.getX(), musicVolumeLine.getY(), musicVolumeLine.getWidth() * musicVolume, musicVolumeLine.getHeight(), musicVolumeLine.getBoundingCircleRadius()));
		//Sets the color of the bar to be the same as the string
		g.setColor(stringColors[1]);
		//Draws the volume bar for sound
		g.draw(soundVolumeLine);
		//Fills the sound FX bar to a percentage of the max volume
		g.fill(new RoundedRectangle(soundVolumeLine.getX(), soundVolumeLine.getY(), soundVolumeLine.getWidth() * soundVolume, soundVolumeLine.getHeight(), soundVolumeLine.getBoundingCircleRadius()));
		
		//colorArray[0] = new Color(173, 16, 16); //red
		//colorArray[1] = new Color(196, 196, 16); //yellow
		//colorArray[2] = new Color(22, 161, 22); //green
		//colorArray[3] = new Color(63, 186, 186); //cyan
		//colorArray[4] = new Color(10, 29, 145); //blue
		
		//Sets the line width to be drawn with a lot thicker
		g.setLineWidth(5);
		//Sets the color to our own color schemes red
		g.setColor(new Color(173, 16, 16));
		//Fills the left hand selector circle if H is pressed
		if (fill[0])
			g.fill(selector[0]);
		//Draws the outline of the left hand selector circle if H is not pressed
		else
			g.draw(selector[0]);
		//Sets the color to our own color schemes blue
		g.setColor(new Color(10, 29, 145));
		//Fills the right hand selector circle if K is pressed
		if (fill[1])
			g.fill(selector[1]);
		//Draws the outline of the right hand selector circle if K is not pressed
		else
			g.draw(selector[1]);
	}

	//Identifies state id
	public int getID()
	{
		return 2;
	}
}