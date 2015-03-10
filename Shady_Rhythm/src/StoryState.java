import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class StoryState extends BasicGameState
{
	//Creates variables to store the width and height of the game
	int windowWidth;
	int windowHeight;
	
	//Creates an instance of the StoryParser that will parse and display the story
	StoryParser[] story;
	//Creates a list of rhythms that are consecutively played as you reach the end of a story section
	Rhythms[] rhythmList;
	//Creates instance of the state handler class
	StateHandler stateHandler;
	//Creates an instance of the rhythm class
	Rhythms engineRhythm;
	//Creates a custom font that can be used to draw text in
	TrueTypeFont wordFont;
	
	//Creates a rectangle with rounded edges to draw text in
	RoundedRectangle textBox;
	
	//Creates a variable to keep track of what scene the state is currently on
	int scene;
	//Creates a variable to determine where in the current scene we are
	int scenePosition;
	
	//Constructor
	StoryState(Rhythms rhythm, StateHandler sh)
	{
		//Sets the instance of rhythm to the rhythm being accessed by the gamestate
		engineRhythm = rhythm;
		//Sets the instance of the state handler tjjjjjo the universal one passed between all states
		stateHandler = sh;
	}
	
	//Triggers certain events when leaving the story state
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//Moves to the next scene
		scene++;
		//Initializes a scenes position to start at the beginning
		scenePosition = 0;
		//Tells the state handler that the game just left the story state
		stateHandler.leavingState(state.getCurrentStateID());
	}
	
	//Initializes various variables
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Creates a new custom font used to draw text on the screen
		Font font = new Font("Verdana", Font.PLAIN, 32);
		wordFont = new TrueTypeFont(font, true);
		
		//Sets the window width and height to be the width and height gotten from the gamecontainer
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Creates a new story parser list populated with the different story files
		story = new StoryParser[5];
		for (int i = 0; i < story.length; i++)
			story[i] = new StoryParser("data/Story/" + i + ".txt", wordFont, windowWidth - (20 * 4));
		
		//Creates a new list of rhythms that can be accessed in order of the story position
		rhythmList = new Rhythms[5];
		for (int i = 0; i < rhythmList.length; i++)
			rhythmList[i] = new Rhythms();
		for (int i = 0; i < rhythmList.length; i++)
			rhythmList[i].setRhythm(story[i].rhythm);
		
		//Sets the textbox to be the specified dimensions on the screen
		textBox = new RoundedRectangle(20, (windowHeight * 3/4 + (wordFont.getHeight() - 10)) - 20, windowWidth - (20 * 2), windowHeight * 1/4 - (wordFont.getHeight() - 10), 20);
		
		//Initializes the scenes to start at the beginning
		scene = 0;
		//Initializes a scenes position to start at the beginning
		scenePosition = 0;
		
		
	}
	
	//Draws to the screen
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Turns anti alias on for smoother drawing
		g.setAntiAlias(true);
		
		//Sets the graphics color to black
		g.setColor(Color.black);
		//Draws the background color as black
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		//Sets the graphics color to gray
		g.setColor(new Color(84, 84, 84));
		//Fills the textbox with the color set above
		g.fill(textBox);
		
		//Draws the strings from the file with the incremental animation
		story[scene].drawText(wordFont, scenePosition, textBox, gc, g);
	}
	
	//Checks for events and updates variables and states accordingly
	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets the keyboard input from the gamecontainer
		Input input = gc.getInput();
		
		//Checks to see if the the J key was pressed
		if (input.isKeyPressed(Input.KEY_J))
		{
			//Reinitializes the stories display to start again
			story[scene].setDisplay();
			//Checks to make sure the player has not reached the end of the string
			if (scenePosition != story[scene].story.length - 1)
			{
				//Moves to the next line in the scene
				scenePosition++;
			}
			//Otherwise move to the rhythm and the next scene
			//This is below because when the state is entered after you leave it it continues from where it left off
			//If the statements are switched it immediately increases scene position upon enter the state again
			else
			{
				if (rhythmList[scene].title.equals("NONE"))
				{
					//Creates a transition effect from scene to scene
					state.enterState(3, new FadeOutTransition(Color.white, 750), new FadeInTransition(Color.white, 750));
				}
				else
				{
					//Sets the universal rhythm to be equal to the next rhythm in the sequence
					engineRhythm.setRhythm(rhythmList[scene].title + " - " + rhythmList[scene].artist);
					//Starts gameplay of the next rhythm by entering the game state
					state.enterState(4);
				}
			}
		}
		
		//Clears the input record
		//This is needed because otherwise key presses can carry across game states
		input.clearKeyPressedRecord();
	}
	
	//Identifies state ID
	@Override
	public int getID()
	{
		return 3;
	}
	
}
