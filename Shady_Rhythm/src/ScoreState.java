import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ScoreState extends BasicGameState
{
	//Creates integers to hold the window width and height
	int windowWidth;
	int windowHeight;
	
	//Creates a default font to draw with
	TrueTypeFont wordFont;
	//Creates a default font to draw the high score with
	TrueTypeFont highScoreFont;
	
	//Creates a sound when you enter the state
	Sound win;
	
	PadInput pads;
	//Creates an instance of the score class
	Score score;
	//Creates an instance of the state handler class
	StateHandler stateHandler;
	//Creates an instance of the rhythm class
	Rhythms engineRhythm;
	
	//Creates a variable to display the score with (needed for starting animation)
	float displayScore;
	//Creates an array to display the individual color based percentage (needed for starting animation)
	float[] displayColorScores;
	
	//Creates an array to hold the colors for the individual colors
	Color[] accuracyColors;
	
	boolean newHighScore;
	
	//Constructor
	ScoreState(Score s, StateHandler sh, Rhythms rhythm, PadInput p)
	{
		//Sets the ScoreStates instance of Score to the instance shared with the gamestate
		score = s;
		//Sets the stateHandler instance to be the universal one used throughout the game
		stateHandler = sh;
		//Sets the rhythm instance to be the rhythm that was passed to the engine
		engineRhythm = rhythm;
		pads = p;
	}
	
	//Triggers certain processes when the state is entered
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		//Plays the winning sound when you enter the state (sound is of cheering)
		win.play(1, stateHandler.getSoundVolume());
		try {newHighScore = score.checkHighScore(engineRhythm.title);} catch (IOException e) {e.printStackTrace();}
	}
	
	//Triggers certain events when the score state is left
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//Resets the display score to zero to start the animation over again when you next enter
		displayScore = 0;
		//Resets all the color based display scores to start the animation over again when you next enter
		for (int i = 0; i < displayColorScores.length; i++)
			displayColorScores[i] = 0;
		//Checks to see if the winning cheers are playing and then turns them off as you leave the state if they are
		if (win.playing())
			win.stop();
	}
	
	//Basic initializations of variables
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Gets the window width and height from the gamecontainer
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes the font to draw the players recent score in
		Font font = new Font("Courier", Font.BOLD, 35);
		wordFont = new TrueTypeFont(font, false);
		
		//Initializes the font used to draw high scores
		font = new Font("Courier", Font.BOLD, 20);
		highScoreFont = new TrueTypeFont(font, false);
		
		//Reads the winning sound from a wav file (sound is of cheering)
		win = new Sound("data/Sound Effects/Score Screen Sound.wav");
		
		//Sets the score that displays on the screen to be 0 at first (used for animation)
		displayScore = 0;
		//Sets the individual color scores the are displayed to start at 0 (used for animation)
		displayColorScores = new float[]{0, 0, 0, 0, 0};
		
		//Sets the color array to be the predetermined color scheme shades of red, yellow, green, cyan, and blue
		accuracyColors = new Color[5];
		accuracyColors[0] = new Color(173, 16, 16); //red
		accuracyColors[1] = new Color(196, 196, 16); //yellow
		accuracyColors[2] = new Color(22, 161, 22); //green
		accuracyColors[3] = new Color(63, 186, 186); //cyan
		accuracyColors[4] = new Color(10, 29, 145); //blue
	}

	//Renders to the screen
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Sets the graphics font to be the custom font created for the players score
		g.setFont(wordFont);
		
		//Sets the graphics color to a shade of gray
		g.setColor(new Color(84, 84, 84));
		//Draws the background color as gray
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		//Sets the graphics color to black
		g.setColor(Color.black);
		//Draws the accuracy string above the accuracy circle
		g.drawString("Accuracy", windowWidth/4 - g.getFont().getWidth("Accuracy")/2, windowHeight/2 - 325 - g.getFont().getHeight("Accuracy")/2);
		if (newHighScore)
			g.drawString("NEW HIGH SCORE", windowWidth/4 - g.getFont().getWidth("NEW HIGH SCORE")/2, 30 - g.getFont().getHeight("NEW HIGH SCORE")/2);
		if (score.percentage > .7)
			g.drawString("You Passed!!", windowWidth/4 - g.getFont().getWidth("NEW HIGH SCORE")/2, 30 + g.getFont().getHeight("NEW HIGH SCORE")/2);
		
		//Sets the graphics color to white
		g.setColor(Color.white);
		//Draws the accuracy as a percentage of a circle (using angle measures)
		g.fillArc(windowWidth/4 - 150, windowHeight/2 - 150 - 150, 300, 300, 270, 270 + (360 * displayScore));
		
		//Converts the score percentage from a float to a string
		String displayScoreString = Float.toString(displayScore * 100).substring(0, 2) + "%";
		//Checks to see if the percentage is 100 (need a different portion of the sting if that is the case)
		if (displayScore >= 1)
			displayScoreString = Float.toString(displayScore * 100).substring(0, 3) + "%";
		//Sets the graphics color to black
		g.setColor(Color.black);
		//Draws the percentage string in the middle of the percentage circle
		g.drawString(displayScoreString, windowWidth/4 - g.getFont().getWidth(displayScoreString)/2, windowHeight/2 - 150 - g.getFont().getHeight(displayScoreString)/2);
		
		//Draws the color categorized accuracy score as a portion of a circle
		for (int i = 0; i < accuracyColors.length; i++)
		{
			//Sets the color to the accuracy color of the individual circles
			g.setColor(accuracyColors[i]);
			////Draws the accuracy as a percentage of a circle (using angle measures)
			g.fillArc(windowWidth * (1 + (2 * i))/10 - 75, windowHeight * 3/4 - 50, 150, 150, 270, 270 + (360 * displayColorScores[i]));
		}
		
		//Draws the color categorized accuracy strings
		for (int i = 0; i < displayColorScores.length; i++)
		{
			//Converts the color categorized score percentage from a float to a string
			String displayColorScoreString = Float.toString(displayColorScores[i] * 100).substring(0, 2) + "%";
			//Checks to see if the color categorized score percentage is 100 (need a different portion of the sting if that is the case)
			if (displayColorScores[i] >= 1)
					displayColorScoreString = Float.toString(displayColorScores[i] * 100).substring(0, 3) + "%";
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the color categorized percentage strings in the middle of their corresponding percentage circles
			g.drawString(displayColorScoreString, windowWidth * (1 + (2 * i))/10 - g.getFont().getWidth(displayColorScoreString)/2, windowHeight * 3/4 + 25 - g.getFont().getHeight(displayColorScoreString)/2);
		}
		
		//This point on draws the high score portion of the score screen
		//Creates a couple variables to define the small high score window in the top right corner of the screen
		float highScoreWindowX = windowWidth/2 - 150;
		float highScoreWindowY = 60;
		float highScoreWindowWidth = 900;
		float highScoreWindowHeight = 500;
		
		//Sets the graphics drawing color to black
		g.setColor(Color.black);
		//Draws the outline of the high score window
		g.draw(new Rectangle(highScoreWindowX, highScoreWindowY, highScoreWindowWidth, highScoreWindowHeight));
		
		//Checks to see if the player got a new high score and displays previous high score above the window if they did
		if (newHighScore)
			g.drawString("Previous High Score", highScoreWindowX + highScoreWindowWidth/2 - g.getFont().getWidth("Previous High Score")/2, highScoreWindowY - g.getFont().getHeight("Previous High Score"));
		//Otherwise display high score in for the song above the window
		else
			g.drawString("High Score", highScoreWindowX + highScoreWindowWidth/2 - g.getFont().getWidth("High Score")/2, highScoreWindowY - g.getFont().getHeight("High Score"));
		
		//Sets the grpahics font to be the high score font (NOTE::Not done before displaying the above string because the above string should be slightly more emphasized than the strings inside the high score window)
		g.setFont(highScoreFont);
		
		//Sets the graphics color to black
		g.setColor(Color.black);
		//Draws the high score accuracy string above the accuracy circle
		g.drawString("Accuracy",highScoreWindowX + highScoreWindowWidth/2 - g.getFont().getWidth("Accuracy")/2, highScoreWindowY + highScoreWindowHeight/2 - 75 - 150 - g.getFont().getHeight("Accuracy")/2);
		
		//Sets the graphics color to white
		g.setColor(Color.white);
		//Draws the high score accuracy as a percentage of a circle (using angle measures)
		g.fillArc(highScoreWindowX + highScoreWindowWidth/2 - 75, highScoreWindowY + highScoreWindowHeight/2 - 75 - 125, 150, 150, 270, 270 + (360 * score.highScores[0]));
		
		//Converts the high score percentage from a float to a string
		String displayHighScoreString = Float.toString(score.highScores[0] * 100).substring(0, 2) + "%";
		//Checks to see if the high score percentage is 100 (need a different portion of the sting if that is the case)
		if (score.highScores[0] >= 1)
			displayHighScoreString = Float.toString(score.highScores[0] * 100).substring(0, 3) + "%";
		//Sets the graphics color to black
		g.setColor(Color.black);
		//Draws the high score percentage string in the middle of the percentage circle
		g.drawString(displayHighScoreString, highScoreWindowX + highScoreWindowWidth/2 - g.getFont().getWidth(displayHighScoreString)/2, highScoreWindowY + highScoreWindowHeight/2 - 125 - g.getFont().getHeight(displayHighScoreString)/2);
		
		//Draws the color categorized high score accuracy scores as a portion of a circle
		for (int i = 0; i < accuracyColors.length; i++)
		{
			//Sets the color to the accuracy color of the individual circles
			g.setColor(accuracyColors[i]);
			//Draws the high score accuracy as a percentage of a circle (using angle measures)
			g.fillArc(highScoreWindowX + highScoreWindowWidth * (1 + (2 * i))/10 - 50, highScoreWindowY + highScoreWindowHeight * 3/4 - 50, 100, 100, 270, 270 + (360 * score.highScores[i+1]));
		}
		
		//Draws the color categorized high score accuracy strings
		for (int i = 0; i < displayColorScores.length; i++)
		{
			//Converts the color categorized high score percentage from a float to a string
			String displayHighColorScoreString = Float.toString(score.highScores[i+1] * 100).substring(0, 2) + "%";
			//Checks to see if the color categorized high score percentage is 100 (need a different portion of the sting if that is the case)
			if (score.highScores[i+1] >= 1)
					displayHighColorScoreString = Float.toString(score.highScores[i+1] * 100).substring(0, 3) + "%";
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the color categorized high score percentage strings in the middle of their corresponding high score percentage circles
			g.drawString(displayHighColorScoreString, highScoreWindowX + highScoreWindowWidth * (1 + (2 * i))/10 - g.getFont().getWidth(displayHighColorScoreString)/2, highScoreWindowY + highScoreWindowHeight * 3/4 - g.getFont().getHeight(displayHighColorScoreString)/2);
		}
	}

	//Used to check for certain events and then updates variables
	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets the keyboard input from the game container
		Input input = gc.getInput();
		
		//Adds to the display score until it reaches the players percent (creates animation)
		if (displayScore < score.percentage)
			displayScore += .01;
		
		//Adds to the individual colors scores until they reach the actual percent the player achieved (creates animation)
		for (int i = 0; i < displayColorScores.length; i++)
		{
			if (displayColorScores[i] < score.percentageByColor[i])
				displayColorScores[i] += .01;
		}
		
		//Checks to see if the H key is pressed
		if (input.isKeyPressed(Input.KEY_H) || input.isKeyPressed(Input.KEY_J) || input.isKeyPressed(Input.KEY_K) || pads.input == 1 || pads.input == 3 || pads.input == 5)
		{
			//Enters the last state that the game came from (so that can go back to either story or arcade)
			state.enterState(stateHandler.getLastStateID());
		}
		
		input.clearKeyPressedRecord();
	}

	//Identifies state id
	@Override
	public int getID()
	{
		return 8;
	}

}
