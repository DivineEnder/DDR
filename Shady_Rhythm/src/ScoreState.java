import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
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
	
	float colorScoreRadius;
	float colorScoreCircleX;
	float colorScoreCircleY;
	float colorHighScoreCircleX;
	float colorHighScoreCircleY;
	
	//Creates an array to hold the colors for the individual colors
	Color[] accuracyColors;
	Color[] containerAccuracyColors;
	
	boolean newHighScore;
	
	int overallTransition;
	
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
		
		containerAccuracyColors = new Color[5];
		containerAccuracyColors[0] = new Color(73, 16, 16); //red
		containerAccuracyColors[1] = new Color(96, 96, 16); //yellow
		containerAccuracyColors[2] = new Color(22, 61, 22); //green
		containerAccuracyColors[3] = new Color(63, 86, 86); //cyan
		containerAccuracyColors[4] = new Color(10, 29, 45); //blue
		
		overallTransition = 0;
	}
	
	//Triggers certain processes when the state is entered
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		//Plays the winning sound when you enter the state (sound is of cheering)
		win.play(1, stateHandler.getSoundVolume());
		
		colorScoreRadius = windowHeight/3;
		colorScoreCircleX = windowWidth/2;
		colorScoreCircleY = windowHeight/2;
		colorHighScoreCircleX = windowWidth/2;
		colorHighScoreCircleY = windowHeight/2;
		
		try {newHighScore = score.checkHighScore(engineRhythm.title + " - " + engineRhythm.artist);} catch (IOException e) {e.printStackTrace();}
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
		//Resets the transition effect when you leave the score state
		overallTransition = 0;
		//Checks to see if the winning cheers are playing and then turns them off as you leave the state if they are
		if (win.playing())
			win.stop();
	}
	
	public int getIndex(int i, int numColors)
	{
		if (numColors == 3)
			return i * 2;
		else
			return i;
	}
	
	public void drawScoreCircle(float centerX, float centerY, float[] scores, String overallPercent, float radius, int numColors, String scoreString, Graphics g)
	{
		g.setLineWidth(7);
		g.setAntiAlias(false);
		
		float angle = 360f/numColors;
		
		for (int i = 0; i < numColors; i++)
		{
			g.setColor(containerAccuracyColors[getIndex(i, numColors)]);
			g.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, (270 - angle/2) + (angle * i), (270 - angle/2) + (angle * (i + 1)));
			g.setColor(accuracyColors[getIndex(i, numColors)]);
			g.fillArc(centerX - (radius/2 * scores[getIndex(i, numColors)]) - radius/2, centerY - (radius/2 * scores[getIndex(i, numColors)]) - radius/2, radius + (radius) * scores[getIndex(i, numColors)], radius + (radius) * scores[getIndex(i, numColors)], (270 - angle/2) + (angle * i), (270 - angle/2) + (angle * (i + 1)));
		}
		
		g.setColor(Color.black);
		for (int i = 0; i < numColors; i++)
			g.drawLine(centerX, centerY, centerX + (float) (radius * Math.cos(((angle * (i + 1)) - (90 - angle/2)) * Math.PI/180)), centerY + (float) (radius * Math.sin(((angle * (i + 1)) - (90 - angle/2)) * Math.PI/180)));
		
		for (int i = 0; i < numColors; i++)
		{
			g.drawString(Integer.toString((int) (scores[getIndex(i, numColors)] * 100)) + "%", centerX - g.getFont().getWidth(Integer.toString((int) (scores[getIndex(i, numColors)] * 100)) + "%")/2, centerY - radius - g.getFont().getHeight(Integer.toString((int) (scores[getIndex(i, numColors)] * 100)) + "%"));
			g.rotate(centerX, centerY, angle);
		}
		g.resetTransform();
		
		g.setAntiAlias(true);
		
		g.setColor(Color.black);
		g.draw(new Circle(centerX, centerY, radius + 3.5f));
		
		g.setColor(new Color(54, 54, 54));
		g.fill(new Circle(centerX, centerY, radius/2));
		
		g.setColor(new Color(184, 184, 184));
		g.fill(new Circle(centerX, centerY, (radius/2) * Integer.parseInt(overallPercent)/100));
		
		g.setColor(Color.black);
		g.draw(new Circle(centerX, centerY, radius/2 + 3.5f));
		
		g.setColor(Color.black);
		g.drawString(overallPercent + "%", centerX - g.getFont().getWidth(overallPercent + "%")/2, centerY - g.getFont().getHeight(overallPercent + "%")/2);
		
		g.setColor(new Color(184, 184, 184));
		if (overallTransition == 2)
			g.drawString(scoreString, centerX - g.getFont().getWidth(scoreString)/2, centerY - radius - (g.getFont().getHeight(scoreString) * 2));
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
		
		drawScoreCircle(colorHighScoreCircleX, colorHighScoreCircleY, score.highScores, Integer.toString((int) (score.highScores[0] * 100)), colorScoreRadius - g.getFont().getHeight("100"), engineRhythm.ringNum, "High Score", g);
		drawScoreCircle(colorScoreCircleX, colorScoreCircleY, displayColorScores, Integer.toString((int) (displayScore * 100)), colorScoreRadius, engineRhythm.ringNum, "Score", g);
		
		g.drawString("Press any pad to continue...", windowWidth/2 - g.getFont().getWidth("Press any pad to continue...")/2, windowHeight/2 - g.getFont().getHeight("Press any pad to continue..."));
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
		
		if (displayColorScores[4] >= score.percentageByColor[4] && overallTransition != 1 && overallTransition != 2)
			overallTransition++;
		
		if (overallTransition == 1)
		{
			if (colorScoreCircleX > windowWidth/4)
				colorScoreCircleX -= 1;
			if (colorHighScoreCircleX < windowWidth * 3/4)
				colorHighScoreCircleX += 1;
			
			if (colorHighScoreCircleX >= windowWidth * 3/4 && colorScoreCircleX <= windowWidth/4)
				overallTransition++;
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
