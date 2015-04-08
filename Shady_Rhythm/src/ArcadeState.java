import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ArcadeState extends BasicGameState
{
	//Creates variables to hold the window width and height
	float windowWidth;
	float windowHeight;
	
	//Creates an array to hold the different fonts used to draw the song names
	TrueTypeFont[] circleFonts;
	
	//Creates an instance of the PadInput class to get input from the dance pad
	PadInput pads;
	
	//Creates a class specific instance of the Rhythms class for the arcade state
	Rhythms engineRhythm;
	//Creates a class specific instance of the stateHandler class for the arcade state
	StateHandler stateHandler;
	
	//Creates an array of rhythms to choose from
	ArrayList<Rhythms> rhythmsList;
	
	//Creates a list of integers that determine what 5 songs to display on screen
	int[] displayedIndexes;
	//Creates a list of circles that will be drawn to the screen as backgrounds for each song
	Circle[] displayCircles;
	//Creates a list of colors to draw the displayCircles with
	Color[] displayColors;
	
	//Creates a list of colors to draw the selector arcs with (not all 5 colors represented, only the game's shade of r, g, and b)
	Color[] timerColors;
	//Creates a list of integers to determine how long to hold the different keys for
	int[] timers;
	//Creates a list of booleans that determine whether or not a key is held down
	boolean[] timersGo;
	
	//Constructor
	public ArcadeState(Rhythms rhythm, StateHandler sh, PadInput p)
	{
		//Initializes the arcade states engineRhythm instance of the Rhythms class to the one that is passed to the game state
		engineRhythm = rhythm;
		//Initializes the arcade states StateHandler instance to the universal one passed between all classes
		stateHandler = sh;
		
		//Initializes the arcade states pad input instance to the universal pad input passed between all controllable classes
		pads = p;
	}
	
	//Initializes various variables when game starts (runs before graphics drawn to screen)
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Initializes the window width and height variables to the window width and height from the gamecontainer
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes a new java awt font from a file
		Font font = null;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("data/Fonts/belerenbold.ttf"));
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (FontFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		//Initializes the font list to a new list of TrueTypeFonts
		circleFonts = new TrueTypeFont[5];
		//Sets the font to be plain and have a size of 5
		font = font.deriveFont(Font.PLAIN, 5);
		//Initializes the lists indexes to a TrueTypeFont which can be used to draw strings on the screen in a custom font
		circleFonts[0] = new TrueTypeFont(font, false);
		circleFonts[4] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 20
		font = font.deriveFont(Font.PLAIN, 20);
		//Initializes the lists indexes to a TrueTypeFont which can be used to draw strings on the screen in a custom font
		circleFonts[1] = new TrueTypeFont(font, false);
		circleFonts[3] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		//Initializes the lists indexes to a TrueTypeFont which can be used to draw strings on the screen in a custom font
		circleFonts[2] = new TrueTypeFont(font, false);
		
		//Creates a new thread that runs through the songs in the data/Music/ directory of the project
		//This is put inside a thread so as to speed up the initial loading phase of the game
		Thread thread = new Thread()
		{
			//Basic function that runs each time the thread is run
			public void run()
			{
				//Creates a new file that is a directory within the project
				File dir = new File("data/Music/");
				//Gets a list of all the files that are a wav file within the directory defined above
				File[] songs = dir.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String filename)
					{
						return filename.endsWith(".wav");
					}
				});
				
				//Initializes the rhythmList to a new array of Rhythms
				rhythmsList = new ArrayList<Rhythms>();
				//Iterates through and adds indexes to the rhythmList equal to the number of wav files found above
				for (int i = 0; i < songs.length; i++)
					rhythmsList.add(new Rhythms());
				//Iterates through the recently initialized rhythms in the rhythm list and sets them to the wav files read from the directory above
				for (int i = 0; i < songs.length; i++)
					rhythmsList.get(i).setRhythm(songs[i].getName().substring(0, songs[i].getName().length() - 4));
			}
		};
		//Starts the thread
		thread.start();
		
		//Initializes the display circles to predetermined positions on the screen based upon the window width and height
		displayCircles = new Circle[5];
		displayCircles[0] = new Circle(windowHeight/9, windowHeight/3, windowHeight/9);
		displayCircles[1] = new Circle(windowWidth/4, windowHeight/3, windowHeight/6);
		displayCircles[2] = new Circle(windowWidth/2, windowHeight/3, windowHeight/4);
		displayCircles[3] = new Circle(windowWidth * 3/4, windowHeight/3, windowHeight/6);
		displayCircles[4] = new Circle(windowWidth - windowHeight/9, windowHeight/3, windowHeight/9);
		
		//Initializes the display colors
		//Colors change in opacity so that they cause the fading almost 3-D look
		displayColors = new Color[5];
		displayColors[0] = new Color(1.0f, 1.0f, 1.0f, .2f);
		displayColors[1] = new Color(1.0f, 1.0f, 1.0f, .7f);
		displayColors[2] = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		displayColors[3] = new Color(1.0f, 1.0f, 1.0f, .7f);
		displayColors[4] = new Color(1.0f, 1.0f, 1.0f, .2f);
		
		//Initializes the displayed indexes to the first 5 indexes of the rhythmList
		displayedIndexes = new int[5];
		for (int i = 0; i < displayedIndexes.length; i++)
			displayedIndexes[i] = i;
		
		//Initializes the three timer colors to Full Circle's shade of r, b, and g
		timerColors = new Color[3];
		timerColors[0] = new Color(173f/255f, 16f/255f, 16f/255f);
		timerColors[1] = new Color(22f/255f, 161f/255f, 22f/255f);
		timerColors[2] = new Color(10f/255f, 29f/255f, 145f/255f);
		
		//Initializes the timer list to all start at 0
		timers = new int[]{0, 0, 0};
		//Initializes the timer booleans to all start as false
		timersGo = new boolean[]{false, false, false};
	}
	
	//Triggers certain events when the arcade state is entered
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		//Clears the recent pad input
		//This keeps key presses from crossing states
		pads.clearPadPressedRecord();
	}
	
	//Triggers certain events when the player leaves the arcade state
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//Lets the stateHandler know the state that was just left
		stateHandler.leavingState(state.getCurrentStateID());
		
		//Resets the timers for each key
		for (int i = 0; i < timers.length; i++)
		{
			//Sets the timer ints to zero
			timers[i] = 0;
			//Sets the timer booleans to false
			timersGo[i] = false;
		}
		
		//Clears the recent pad input
		//This keeps key presses from crossing states
		//Probably not needed here as well as in the enter state, but I include them in both because you never know how ticky users can get
		pads.clearPadPressedRecord();
	}
	
	//Triggers certain events when keys are pressed
	@Override
	public void keyPressed(int key, char c)
	{
		//Checks to see whether the key that was pressed was the key H
		if (key == Input.KEY_H)
		{
			//Turns on the selector timer by setting its corresponding boolean value to true
			timersGo[0] = true;
			//Plays a sample of the song that is being selected by the key begin pressed
			rhythmsList.get(displayedIndexes[1]).currentSong.play();
		}
		//Checks to see whether the key that was pressed was the key J
		else if (key == Input.KEY_J)
		{
			//Turns on the selector timer by setting its corresponding boolean value to true
			timersGo[1] = true;
			//Plays a sample of the song that is being selected by the key begin pressed
			rhythmsList.get(displayedIndexes[2]).currentSong.play();
		}
		//Checks to see whether the key that was pressed was the key K
		else if (key == Input.KEY_K)
		{
			//Turns on the selector timer by setting its corresponding boolean value to true
			timersGo[2] = true;
			//Plays a sample of the song that is being selected by the key begin pressed
			rhythmsList.get(displayedIndexes[3]).currentSong.play();
		}
	}
	
	//Triggers certain events when keys are released
	@Override
	public void keyReleased(int key, char c)
	{
		//Checks to see whether the key that was released was the key H
		if (key == Input.KEY_H)
		{
			//Turns off the selector timer by setting its corresponding boolean value to false
			timersGo[0] = false;
			//Resets the corresponding timer's value
			timers[0] = 0;
			//Stops playing the sample of the song that was selected
			rhythmsList.get(displayedIndexes[1]).currentSong.stop();
		}
		//Checks to see whether the key that was released was the key J
		else if (key == Input.KEY_J)
		{
			//Turns off the selector timer by setting its corresponding boolean value to false
			timersGo[1] = false;
			//Resets the corresponding timer's value
			timers[1] = 0;
			//Stops playing the sample of the song that was selected
			rhythmsList.get(displayedIndexes[2]).currentSong.stop();
		}
		//Checks to see whether the key that was released was the key K
		else if (key == Input.KEY_K)
		{
			//Turns off the selector timer by setting its corresponding boolean value to false
			timersGo[2] = false;
			//Resets the corresponding timer's value
			timers[2] = 0;
			//Stops playing the sample of the song that was selected
			rhythmsList.get(displayedIndexes[3]).currentSong.stop();
		}
	}
	
	//Checks for certain events and then updates variables accordingly
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		//Checks to see whether the dance pad is connected to the game
		if (pads.usePads)
		{
			if (pads.input == 6)
				state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			
			//Checks to see whether the left-most pad was pressed
			if (pads.input == 1)
				timersGo[0] = true;
			//Checks to see whether the middle pad was pressed
			else if (pads.input == 3)
				timersGo[1] = true;
			//Checks to see whether the right-most pad was pressed
			else if (pads.input == 5)
				timersGo[2] = true;
			//If none of the pads were pressed reset all the timers
			else
			{
				//Turns off all the timers by setting their boolean values to false
				for (int i = 0; i < timersGo.length; i++)
					timersGo[i] = false;
				//Resets all the timers to zero value
				for (int i = 0; i < timersGo.length; i++)
					timers[i] = 0;
			}
		}
		
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_K))
			state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		
		//Checks to see whether the left selection timer is at its max value
		if (timers[0] == 100)
		{
			//Stops playing the song that was being selected
			rhythmsList.get(displayedIndexes[1]).currentSong.stop();
			
			//Iterates through the displayedIndexes list and shifts them all to the left by one
			for (int i = 0; i < displayedIndexes.length; i++)
			{
				//Moves the indexes to the left
				displayedIndexes[i]--;
				//Circles the indexes back around so that they do not reference indexes outside of the rhythmList
				if (displayedIndexes[i] < 0)
					displayedIndexes[i] += rhythmsList.size();
			}
			
			//Turns off the corresponding timer by setting its boolean value to false
			timersGo[0] = false;
			//Resets the corresponding timer's value
			timers[0] = 0;
		}
		else if (timers[1] == 100)
		{
			//Stops playing the song that was being selected
			rhythmsList.get(displayedIndexes[2]).currentSong.stop();
			
			engineRhythm.setRhythm(rhythmsList.get(displayedIndexes[2]).title + " - " + rhythmsList.get(displayedIndexes[2]).artist);
			state.enterState(4);
		}
		//Checks to see whether the right selection timer is at its max value
		else if (timers[2] == 100)
		{
			//Stops playing the song that was being selected
			rhythmsList.get(displayedIndexes[3]).currentSong.stop();
			
			//Iterates through the displayedIndexes list and shifts them all to the right by one
			for (int i = 0; i < displayedIndexes.length; i++)
			{
				//Moves the indexes to the right
				displayedIndexes[i]++;
				//Circles the indexes back around so that they do not reference indexes outside of the rhythmList
				if (displayedIndexes[i] > rhythmsList.size() - 1)
					displayedIndexes[i] -= rhythmsList.size() - 1;
			}
			
			//Turns off the corresponding timer by setting its boolean value to false
			timersGo[2] = false;
			//Resets the corresponding timer's value
			timers[2] = 0;
		}
		
		//Iterates through and adds to the selectors that are currently running
		for (int i = 0; i < timers.length; i++)
		{
			if (timersGo[i])
				timers[i]++;
		}
	}
	
	//Draws stuff to the screen
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Turns on Anti Aliasing which makes drawing more smooth
		g.setAntiAlias(true);
		//Sets the graphics line width to draw with
		g.setLineWidth(5);
		
		//Sets the color to the Full Circle shade of gray
		g.setColor(new Color(84, 84, 84));
		//Fills the background rectangle to draw the background as completely gray
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
		
		//Iterates through and draws the five different song selections
		for (int i = 0; i < displayCircles.length; i++)
		{
			//Sets the graphics color to the display circle colors
			g.setColor(displayColors[i]);
			//Draws a filled display circle to the screen
			g.fill(displayCircles[i]);
			//Sets the graphics font to the circle fonts defined above
			g.setFont(circleFonts[i]);
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the song title in the middle of the corresponding display circle
			g.drawString(rhythmsList.get(displayedIndexes[i]).title, displayCircles[i].getCenterX() - g.getFont().getWidth(rhythmsList.get(displayedIndexes[i]).title)/2, displayCircles[i].getCenterY() - g.getFont().getHeight(rhythmsList.get(displayedIndexes[i]).title)/2);
			//Sets the graphics to color to the selector color if the song isn't on the far left or right
			if (i > 0 && i < 4)
				g.setColor(timerColors[i-1]);
			//Creates a string to display the artist of the song
			String artist = "Artist: " + rhythmsList.get(displayedIndexes[i]).artist;
			//Draws the artist of the song under the display circle
			g.drawString(artist, displayCircles[i].getCenterX() - g.getFont().getWidth("Difficulty: "), windowHeight * 5/8);
			//Creates a string to display the diffculty of the song
			String difficulty = "Difficulty: " + rhythmsList.get(displayedIndexes[i]).displayDifficulty;
			//Draws the difficulty of the song under the display circle
			g.drawString(difficulty, displayCircles[i].getCenterX() - g.getFont().getWidth("Difficulty: "), windowHeight * 5/8 + g.getFont().getHeight(artist));
		}
		
		//Draws the opaque timer outlines on top of the display circles
		for (int i = 0; i < timers.length; i++)
		{
			//Sets the graphics color to a more opaque version of the timer color
			g.setColor(new Color(timerColors[i].r, timerColors[i].g, timerColors[i].b, .5f));
			//Draws the outline of the corresponding display circle
			g.draw(displayCircles[i + 1]);
		}
		
		//Draws the selector timers as arcs on the edge of a display circle
		for (int i = 0; i < timers.length; i++)
		{
			//Sets the graphics color to the timer colors
			g.setColor(timerColors[i]);
			//Draws an arc along the edge of the display circle whose angle is determined by what percentage of full the timer is at
			g.drawArc(displayCircles[i + 1].getX(), displayCircles[i + 1].getY(), displayCircles[i + 1].getRadius() * 2, displayCircles[i + 1].getRadius() * 2, 270, 270 + (timers[i] * (360f/100f)));
		}
	}
	
	//Identifies state id
	public int getID()
	{
		return 1;
	}
}
