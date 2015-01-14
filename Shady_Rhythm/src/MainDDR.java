import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
 
public class MainDDR extends StateBasedGame
{
	Controls controls = new Controls();
	Rhythms engineRhythm = new Rhythms();
	
	public MainDDR(String title)
	{
        super(title);
    }
	
    public static void main(String[] args) throws SlickException
    {
    	AppGameContainer app = new AppGameContainer(new MainDDR("Shady Rhythms"));
        
        app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), false);
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        app.setTitle("Full Circle");
        app.setTargetFrameRate(60);
        app.setShowFPS(false);
        app.setVSync(true);
        app.start();
    }
 
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
    	this.addState(new LogoState());
    	this.addState(new MenuState(controls));
    	this.addState(new ArcadeState(engineRhythm));
    	this.addState(new GameState(engineRhythm));
    	this.addState(new TutorialState(controls));
    	this.addState(new OptionsState(controls));
    }
 
}