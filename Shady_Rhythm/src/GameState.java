import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameState extends BasicGameState
{
	int windowWidth;
	int windowHeight;
	
	//Creates a variable to hold the custom font used to draw to the screen
	TrueTypeFont wordFont;
	
	PadInput pads;
	
	//Creates an instance of the engine that will run
	Engine engine;
	//Creates an instance of the rhythm class to use to play songs and get rhythm circles
	Rhythms rhythm;
	//Creates an instance of the state handler class which determines what state the game came from
	StateHandler stateHandler;
	//Creates an instance of the loading screen class which draws the loading screen
	Loading loadingScreen;
	//Creates an instace of the score class which holds the player score
	Score score;
	
	//Creates a new thread that will be used for loading the songs
	Thread loading;
	
	//Creates a variable to tell whether or not the game is paused
	boolean paused;
	//Creates a variable to tell whether or not the game is done loading
	boolean pressAnyKey;
	
	//Creates a string that is displayed while the game is reading the rhythm
	String loadingString;
	
	float pauseOpacity;
	int[] timers;
	
	//Constructor
	public GameState(Rhythms engineRhythm, Score s, StateHandler sh, PadInput p)
	{
		//Sets the rhythm instance to be equal to the one that was passed from either arcade or story state
		rhythm = engineRhythm;
		//Sets the score state to an instance that is shared to the score state
		score = s;
		//Sets the state handler to be an instance that is passed universally to all states
		stateHandler = sh;
		
		pads = p;
	}
	
	//Initializes various variables when the game is first loaded
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Creates a new font that is used to draw strings on the screen
		Font font = new Font("Courier", Font.BOLD, 30);
		wordFont = new TrueTypeFont(font, false);
		
		//Creates a new engine that is used to run the basic game play
		engine = new Engine(gc, rhythm, score, stateHandler, pads);
		//Creates a new loading screen that is used to display the loading screen
		loadingScreen = new Loading(gc.getWidth(), gc.getHeight());
		
		pauseOpacity = 0;
		timers = new int[]{0, 0, 0,};
	}
	
	//Triggers certain events when the game state is left
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//Fades out the currently palying song when you leave the state
		rhythm.currentSong.fade(750, 0, true);
		
		pads.clearPadPressedRecord();
	}
	
	//Triggers certain events when the game state is entered
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		pads.clearPadPressedRecord();
		
		//Starts the gameplay as unpaused
		paused = false;
		//Sets the press any key as false
		pressAnyKey = false;
		
		//Starts the string displayed as loading
		loadingString = "Loading";
		
		//Creates a new gamecontainer and sets it to the instance passed by the state
		final GameContainer pass = gc;
		//Sets various engine functions
		engine.setup();
		//Initalizes a new thread that loads reads the rhythms from the text file
		loading = new Thread()
		{
			//Sets a thread based gamecontainer as the passed game container instance above
			GameContainer gc = pass;
			
			//Automatically runs when the thread is started
			public void run()
			{
				//Reads the rhythm circles from the file
				rhythm.readRhythm(gc);
				//Turns on press any key once the circles are read from the file and game play is ready
				pressAnyKey = true;
				//Sets the loading string to let the player know that gameplay is ready
				loadingString = "Press any pad to continue...";
			}
		};
		//Starts the loading process thread
		loading.start();
	}
	
	//Checks for key presses
	@Override
    public void keyPressed(int key, char c)
    {
		//Checks to see if loading is done and gameplay is ready
		if (pressAnyKey)
		{
			//Turns off the loading screen
			pressAnyKey = false;
			//Starts the game play
			engine.start();
		}
    }
	
	//Checks for certain events and then updates variables correspondingly
	public void update(final GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		//Gets keyboard input from the gamecontainer
		Input input = gc.getInput();
		
		if (!paused)
		{
			//Checks to see if the escape key is pressed
			if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_K) || pads.input == 6)
			{
				//Pauses the engine
				engine.pause();
				//Pauses the gameplay and brings up the pause screen
				paused = true;
			}
		}
		
		if (pads.usePads)
		{
			if (pressAnyKey)
			{
				if (pads.input != 0)
				{
					//Turns off the loading screen
					pressAnyKey = false;
					//Starts the game play
					engine.start();
				}
			}
		}
		
		//Checks to see if H, J, and K are all down at the same time
		//if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))
		//{
			//Leaves the game state and enters the aracade state
			//state.enterState(1, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		//}
		
		//Checks to see if the gameplay is loading or if it is and done and waiting for player then updates the loading screen
		if (loading.isAlive() || pressAnyKey)
			loadingScreen.update();
		//Checks to see if the player has responded and then updates the engine for gameplay
		else if (!pressAnyKey && !paused)
			engine.update(gc, state);
		
		if (paused)
		{
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
				state.enterState(stateHandler.getLastStateID(), new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			else if (timers[1] == 100)
			{
				paused = false;
			}
			else if (timers[2] == 100)
				state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		}
		else
		{
			if (pauseOpacity > 0)
				pauseOpacity -= .01f;
			if (pauseOpacity > 0 && pauseOpacity < .01f)
				engine.play();
		}
		
		//Clears the input record
		//This is needed because otherwise key presses can carry across game states
		input.clearKeyPressedRecord();
	}
	
	//Draws to the screen
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		//Sets anti aliasing on for smoother rendering
		g.setAntiAlias(true);
		//Sets the font to be the custom font defined above
		g.setFont(wordFont);
		
		//Checks if the game rhythm is still being read from the file
		if (loading.isAlive())
		{
			//Draws the loading screen
			loadingScreen.draw(g, gc);
			//Draws the loading string
			g.drawString(loadingString, gc.getWidth()/2 - g.getFont().getWidth(loadingString)/2, gc.getHeight() - g.getFont().getHeight(loadingString));
		}
		//Checks to see if the rhythm is done loading and waiting for player
		else if (pressAnyKey)
		{
			//Draws the loading screen
			loadingScreen.draw(g, gc);
			//Draws the waiting for player new loading string
			g.drawString(loadingString, gc.getWidth()/2 - g.getFont().getWidth(loadingString)/2, gc.getHeight() - g.getFont().getHeight(loadingString));
		}
		//Otherwise draw the engine gameplay
		else
			engine.render(gc, g);
		
		g.setColor(new Color(0, 0, 0, pauseOpacity));
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		//Checks to see if the game is paused and draws the pause screen if it is
		if (paused)
		{	
			if (pauseOpacity < .75)
				pauseOpacity += .01;
			//colorArray[0] = new Color(173, 16, 16); //red
			//colorArray[1] = new Color(196, 196, 16); //yellow
			//colorArray[2] = new Color(22, 161, 22); //green
			//colorArray[3] = new Color(63, 186, 186); //cyan
			//colorArray[4] = new Color(10, 29, 145); //blue
			
			g.setColor(Color.white);
			g.drawString("GAME IS PAUSED", gc.getWidth()/2 - g.getFont().getWidth("Game is paused")/2, 20 + g.getFont().getHeight("GAME IS PAUSED"));
			
			g.setColor(new Color(173f/255f, 16f/255f, 16f/255f, pauseOpacity));
			g.draw(new Circle(windowWidth/6, windowHeight/2, windowHeight/8));
			
			g.setAntiAlias(false);
			g.fillArc(windowWidth/6 - windowHeight/8,  windowHeight/2 - windowHeight/8, windowHeight/4, windowHeight/4, 270, 270 + (timers[0] * (360f/100f)));
			g.setAntiAlias(true);
			
			g.setColor(new Color(22f/255f, 161f/255f, 22f/255f, pauseOpacity));
			g.draw(new Circle(windowWidth/2, windowHeight/2, windowHeight/8));
			
			g.setAntiAlias(false);
			g.fillArc(windowWidth/2 - windowHeight/8,  windowHeight/2 - windowHeight/8, windowHeight/4, windowHeight/4, 270, 270 + (timers[1] * (360f/100f)));
			g.setAntiAlias(true);
			
			g.setColor(new Color(10f/255f, 29f/255f, 145f/255f, pauseOpacity));
			g.draw(new Circle(windowWidth * 5/6, windowHeight/2, windowHeight/8));
			
			g.setAntiAlias(false);
			g.fillArc(windowWidth * 5/6 - windowHeight/8,  windowHeight/2 - windowHeight/8, windowHeight/4, windowHeight/4, 270, 270 + (timers[2] * (360f/100f)));
			g.setAntiAlias(true);
			
			g.setColor(Color.black);
			g.drawString("Back", windowWidth/6 - g.getFont().getWidth("Back")/2, windowHeight/2 - g.getFont().getHeight("Back")/2);
			g.drawString("Resume", windowWidth/2 - g.getFont().getWidth("Resume")/2, windowHeight/2 - g.getFont().getHeight("Resume")/2);
			g.drawString("Main Menu", windowWidth * 5/6 - g.getFont().getWidth("Main Menu")/2, windowHeight/2 - g.getFont().getHeight("Main Menu")/2);
		}
	}

	//Identifies state id
	public int getID()
	{
		return 4;
	}
}
