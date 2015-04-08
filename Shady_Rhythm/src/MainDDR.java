import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
 
public class MainDDR extends StateBasedGame
{
	//Initializes some classes that need to passed between game states
	//Creates a universal padInput class instance used to get input from the dance pad
	static PadInput pads = new PadInput();
	//Creates a universal rhythm to be passed between states (used for picking songs)
	Rhythms engineRhythm = new Rhythms();
	//Creates a state handler which can tell you which state you just left
	StateHandler stateHandler = new StateHandler();
	//Creates a score to pass between the game state and the score state
	Score score = new Score();
	
	//Constructor
	public MainDDR(String title)
	{
        super(title);
    }
	
    public static void main(String[] args) throws SlickException
    {
    	//Tries to connect to the serial port COM13 to get input from the dance pad
    	try
        {
            pads.connect("COM13");
        }catch ( Exception e ){e.printStackTrace();}
    	
    	//Creates a new application container
    	AppGameContainer app = new AppGameContainer(new MainDDR("Full Circle"));
        
    	//Sets the applications display size
        app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);//app.getScreenWidth() - 150, app.getScreenHeight() - 100, false);
        //Gets rid of the window boarders
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        //Sets the window title to be Full Circle
        app.setTitle("Full Circle");
        //Sets the fps that the game should run on
        app.setTargetFrameRate(60);
        //Removes the fps display in the applications top left corner
        app.setShowFPS(false);
        //Helps with tearing issues, makes the game run smoothly
        app.setVSync(true);
        //Starts the application
        app.start();
    }
 
    //Initiates all the states of the game
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
    	//Initiates the Team Henry Logo state first (first state initialized is first one game goes into)
    	this.addState(new LogoState());
    	//Adds the Full Circle Logo state (displays full circle logo and transition)
    	this.addState(new FullCircleLogoState());
    	//Adds the Menu state (displays main menu)
    	this.addState(new MenuState(stateHandler, pads));
    	//Adds the Arcade state (song selection here)
    	this.addState(new ArcadeState(engineRhythm, stateHandler, pads));
    	//Adds the Game state (gameplay in this state)
    	this.addState(new GameState(engineRhythm, score, stateHandler, pads));
    	//Adds the Score state (scores displayed after gameplay here)
    	this.addState(new ScoreState(score, stateHandler, engineRhythm, pads));
    	//Adds the High Score state (work in progress)
    	this.addState(new HighScoreState(pads));
    	//Adds the Options state (work in progress)
    	this.addState(new OptionsState(stateHandler, pads));
    	//Adds the Story state (work in progress)
    	//this.addState(new StoryState(engineRhythm, stateHandler, pads));
    	
    	/*State ID list
    	
    		MenuState			[ID:0]
    		ArcadeState 		[ID:1]
    		OptionsState		[ID:2]
    		StoryState			[ID:3]
    		GameState 			[ID:4]
    		HighScoreState		[ID:5]
    		LogoState			[ID:6]
    		FullCircleLogoState	[ID:7]
    		ScoreState			[ID:8]
    	*/
    }
 
}