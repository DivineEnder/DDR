import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MenuState extends BasicGameState
{
	//Creates variable to hold the window width and height
	int windowWidth;
	int windowHeight;
	
	//Creates a variable for a custom font to draw in
	TrueTypeFont wordFont;
	
	PadInput pads;
	//Creates an instance of the state handler class
	StateHandler stateHandler;
	BackgroundAnimation background;
	
	//Creates an array of slick2d circles that can be drawn on the screen as the different options
	Circle[] menuCircles;
	//Creates an array of strings which can be drawn on the screen as some of the menu options
	String[] menuStrings;
	//Creates an array of points that tell the string array above which string is located where
	Point[] menuStringsCoords;
	//Creates an array of the color scheme to use for drawing
	Color[] colorArray;
	//Creates an array of rounded selector rectangles to draw around the strings when they are selected
	RoundedRectangle[] selectorRectangles;
	
	//Creates a variable to hold the current selector shape (circle or rectangle based on whether you have circle or string selected)
	Shape selector;
	//Creates a variable to hold the timer circle coordinates and radius
	Circle timerCircle;
	
	//Creates a varaible for the background music
	Music backgroundMusic;
	//Creates a sound for the move between two options
	Sound blip;
	//Creates a sound for when you select an option
	Sound select;
	//Creates a sound for a transition as you move from one state to another
	Sound transition;
	
	//Creates a variable to tell what option on the menu is selected
	int selected;
	//Creates a variable to hold the timer
	int timer;
	//Determines whether or not to draw the timer circle
	boolean timerGo = false;
	
	//Constructor
	MenuState(StateHandler sh, PadInput p)
	{
		//Initializes this classes instance of the state handler to be the universal one passed between all states
		stateHandler = sh;
		
		pads = p;
	}

	//Initializes various variables
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Gets the window width and height from the gamecontiner and then sets them to the window width and heighht variables
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
		
		background = new BackgroundAnimation(gc);
		
		//Loads the background music from a file
		backgroundMusic = new Music("data/Sound Effects/Background Music.wav");
		
		//Loads the moving selector sound from a file
		blip = new Sound("data/Sound Effects/Blip.wav");
		//Loads the selected sound from a file
		select = new Sound("data/Sound Effects/Select.wav");
		//Hold the state transition sound from a file
		transition = new Sound("data/Sound Effects/Transition.wav");
		
		//Initializes the circles drawn on the screen to a set position
		menuCircles = new Circle[3];
		menuCircles[0] = new Circle(windowWidth/5, windowHeight/2, 100);
		menuCircles[1] = new Circle(windowWidth/2, windowHeight/2, 100);
		menuCircles[2] = new Circle(windowWidth - windowWidth/5, windowHeight/2, 100);
		
		//Initializes the color array to a predetermined color scheme
		colorArray = new Color[5];
		colorArray[0] = new Color(173, 16, 16); //red
		colorArray[1] = new Color(196, 196, 16); //yellow
		colorArray[2] = new Color(22, 161, 22); //green
		colorArray[3] = new Color(63, 186, 186); //cyan
		colorArray[4] = new Color(10, 29, 145); //blue
		
		//Initializes the menu strings which will be drawn as the options
		menuStrings = new String[5];
		menuStrings[0] = "Arcade";
		menuStrings[1] = "OPTIONS";
		menuStrings[2] = "Story";
		menuStrings[3] = "EXIT";
		menuStrings[4] = "High Scores";
		
		//Initializes the menu strings' coordinates
		menuStringsCoords = new Point[2];
		menuStringsCoords[0] = new Point(windowWidth/2 - (windowWidth/2 - windowWidth/5)/2, windowHeight/2);
		menuStringsCoords[1] = new Point(windowWidth/2 + (windowWidth/2 - windowWidth/5)/2, windowHeight/2);
		
		//Initializes the selector to start on the center circle
		selector = new Circle(menuCircles[1].getCenterX(), menuCircles[1].getCenterY(), menuCircles[1].getRadius() + 10);
		
		//Initializes the selector rectangles so that they aren't null (not set here because need certain variables only found when drawing strings)
		selectorRectangles = new RoundedRectangle[2];
		
		//Initializes the timer circle to a predetermined position on the lower portion of the screen
		timerCircle = new Circle(windowWidth/2, windowHeight/2 + windowHeight/3, 50);
		
		////Initializes selected to be on the middle circle
		selected = 2;
		//Sets the timer to start at zero
		timer = 0;
	}

	//Function that draws a string that you send it downwards on the screen instead of sideways
	public void drawStringDown(float x, float y, String string, Graphics g)
	{
		//Temporary variable that gets the height of the string as a whole
		int height = g.getFont().getHeight(string);
		//Creates an array to hold the strings individual chars as strings (so they can be displayed later)
		String[] stringChars = new String[string.length()];
		//Iterates through the string and gets ever char from the string
		for (int i = 1; i < string.length() + 1; i++)
			stringChars[i - 1] = string.substring(i - 1, i);
		
		//Iterates though the chars and draws them to the screen one on top of another
		for (int i = 0; i < stringChars.length; i++)
		{
			g.drawString(stringChars[i], x - g.getFont().getWidth(stringChars[i])/2, (y - (height * (stringChars.length/2))) + (i * height));
		}
		//Initializes the selector rectangle for the OPTIONS string with variables from this function
		if (string.equals("OPTIONS"))
			selectorRectangles[0] = new RoundedRectangle(x - g.getFont().getWidth(stringChars[0])/2 - 10, y - (height * (stringChars.length/2)) - 5, g.getFont().getWidth(stringChars[0]) + 20, stringChars.length * height + 10, 6);
		//Initializes the selector rectangle for the EXIT string with variables from this function
		else if (string.equals("EXIT"))
			selectorRectangles[1] = new RoundedRectangle(x - g.getFont().getWidth(stringChars[0])/2 - 10, y - (height * (stringChars.length/2)) - 5, g.getFont().getWidth(stringChars[0]) + 20, stringChars.length * height + 10, 6);
	}
	
	//Triggers certain events when the state is entered
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		//Turns off the timer when you enter the state
		timerGo = false;
		//Resets the timer to 0 when you enter the state
		timer = 0;
		//Slowly fades in the background music
		backgroundMusic.fade(750, stateHandler.getMusicVolume(), false);
		//Endlessly loops the background music
		backgroundMusic.loop();
	}
	
	//Triggers certain events upon leaving the menu state
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//Fades out the background music when you leave the state
		backgroundMusic.fade(750, 0, true);
	}
	
	//Renders the to the screen
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Turns on anti alias for smoother drawing
		g.setAntiAlias(true);
		//Sets the graphics to draw with the custom font created above
		g.setFont(wordFont);
		
		background.draw(gc, g);
		
		//g.setColor(Color.white);
		//g.drawString(Integer.toString(pads.input), 0, 0);
		
		//Iterates through the menu options
		for (int i = 0; i < menuStrings.length; i += 2)
		{
			//Sets the graphics color to the corresponding color in the color scheme
			g.setColor(colorArray[i]);
			//Fills the menu circle that corresponds with that color
			g.fill(menuCircles[i/2]);
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the menu string in the specified position on the screen
			g.drawString(menuStrings[i], menuCircles[i/2].getCenterX() - g.getFont().getWidth(menuStrings[i])/2, menuCircles[i/2].getCenterY() - g.getFont().getHeight(menuStrings[i])/2);
		}
		
		//Iterates through the two downward drawn strings
		for (int i = 0; i < menuStringsCoords.length; i++)
		{
			//Sets the graphics color to the corresponding color in the color scheme
			g.setColor(colorArray[(2*i) + 1]);
			//Draws the menu string downwards
			drawStringDown(menuStringsCoords[i].getX(), menuStringsCoords[i].getY(), menuStrings[(2*i) + 1], g);
		}
		
		//Checks to see if the timer is turned on
		if (timerGo)
		{
			//Sets the graphics line width for drawing to 3
			g.setLineWidth(3);
			//Sets the graphics color to the color of the option that is being selected
			g.setColor(colorArray[selected]);
			//Draws the outline of the timer circle
			g.draw(timerCircle);
			
			//Turns off anti alias temporarily for drawing the arc (done because anti alias messes with arc drawing)
			g.setAntiAlias(false);
			//Fill a certain portion of the timer circle depending on how close the timer is to 100
			g.fillArc(timerCircle.getX(), timerCircle.getY(), timerCircle.getRadius() * 2, timerCircle.getRadius() * 2, 270, 270 + (timer * (360f/100f)));
			//Turns anti alias back on once we no longer need it off
			g.setAntiAlias(true);
		}
		
		//Sets the graphics line width to draw with to 10
		g.setLineWidth(10);
		//Sets the graphics color to white
		g.setColor(new Color(255, 255, 255));
		//Draws the selector shape around the option that you have selected
		g.draw(selector);
	}
	
	//Checks to see if a key was pressed
	@Override
	public void keyPressed(int key, char c)
	{
		//Checks for either H, K, or J pressed and sets the timer to true if they were
		if (key == Input.KEY_H || key == Input.KEY_J || key == Input.KEY_K)
		{
			timerGo = true;
		}
	}
	
	//Checks for key releases
	@Override
	public void keyReleased(int key, char c)
	{
		//Checks for either H, J, or K released
		if (key == Input.KEY_H || key == Input.KEY_J || key == Input.KEY_K)
		{
			//Sets the timer to false (will no longer draw)
			timerGo = false;
			//Resets the timer to zero
			timer = 0;
			background.addCircle(timerCircle.getCenterX(), timerCircle.getCenterY(), timerCircle.getRadius(), colorArray[selected]);
		}
	}

	//Updates variables upon certain events happening
	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets the keyboard input from the gamecontainer
		Input input = gc.getInput();
		
		background.update(gc);
		
		//Checks to see whether the timer is complete
		if (timer == 100)
		{
			//Fades out the background music
			backgroundMusic.fade(750, 0, true);
			//Plays the transition sound
			transition.play(1, stateHandler.getSoundVolume());
			//Plays the select option sound
			select.play(1, stateHandler.getSoundVolume());
			//Checks to see whether the exit option was selected and exits the game if it was
			if (selected == 3)
				gc.exit();
			//Otherwise enter the state of selected + 1
			else
				state.enterState(selected + 1, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		}
		
		//Creates a temporary variable to hold what was selected before any changes were made
		int thisIteration = selected;
		
		if (pads.usePads)
		{
			if (pads.input == 0)
			{
				timerGo = false;
				timer = 0;
			}
			else
				timerGo = true;
		}
		
		//TEST THIS
		if (pads.isPadReleased(1) || pads.isPadReleased(2) || pads.isPadReleased(3) || pads.isPadReleased(4) || pads.isPadReleased(5))
			background.addCircle(timerCircle.getCenterX(), timerCircle.getCenterY(), timerCircle.getRadius(), colorArray[selected]);
		
		//Checks to see whether H and J keys are both down
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) || pads.input == 2)
		{
			//Sets the selector to the string selector rectangle
			selector = selectorRectangles[0];
			//Sets selected to first second options
			selected = 1;
		}
		//Checks to see whether the J and K keys are both down
		else if (input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K) || pads.input == 4)
		{
			//Sets the selector to the string selector rectangle
			selector = selectorRectangles[1];
			//Sets the selector to the fourth option
			selected = 3;
		}
		//Checks to see whether the H key was pressed and whether the selector is not already on this option
		else if (input.isKeyPressed(Input.KEY_H) && selected != 0 || pads.input == 1 && selected != 0)
		{
			//Sets the selector shape to the menu circle associated with option with a slightly larger radius
			selector = new Circle(menuCircles[0].getCenterX(), menuCircles[0].getCenterY(), menuCircles[0].getRadius() + 10);
			//Sets the selector to the first option
			selected = 0;
		}
		//Checks to see whether the J key was pressed and whether the selector is not already on this option
		else if (input.isKeyPressed(Input.KEY_J) && selected != 2 || pads.input == 3 && selected != 2)
		{
			//Sets the selector shape to the menu circle associated with option with a slightly larger radius
			selector = new Circle(menuCircles[1].getCenterX(), menuCircles[1].getCenterY(), menuCircles[1].getRadius() + 10);
			//Sets the selector to the third option
			selected = 2;
		}
		//Checks to see whether the K key was pressed and whether the selector is not already on this option
		else if (input.isKeyPressed(Input.KEY_K) && selected != 4 || pads.input == 5 && selected != 4)
		{
			//Sets the selector shape to the menu circle associated with option with a slightly larger radius
			selector = new Circle(menuCircles[2].getCenterX(), menuCircles[2].getCenterY(), menuCircles[2].getRadius() + 10);
			//Sets the selector to the fifth option
			selected = 4;
		}
		
		//Clears the input record
		//This is needed because otherwise key presses can carry across game states
		input.clearKeyPressedRecord();
		
		//Checks to see whether the selected option has changed and plays the switch sound if it has
		if (selected != thisIteration)
			blip.play(1, stateHandler.getSoundVolume());
		
		//Adds to the timer if the timer is running
		if (timerGo)
			timer += 2;
	}
	
	//Identifies the state id
	public int getID()
	{
		return 0;
	}
}