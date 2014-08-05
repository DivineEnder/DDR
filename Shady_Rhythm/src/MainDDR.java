import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
 
public class MainDDR extends StateBasedGame
{
	Controls controls = new Controls();
	Rhythms rhythms = new Rhythms();
	
	public MainDDR(String title)
	{
        super(title);
    }
	
    public static void main(String[] args) throws SlickException
    {
    	AppGameContainer app = new AppGameContainer(new MainDDR("Shady Rhythms"));
        
        app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), false);
        app.setTargetFrameRate(60);
        app.setShowFPS(false);
        app.setVSync(true);
        app.start();
    }
 
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
    	this.addState(new MenuState(controls));
    	this.addState(new GameState(controls));
    	this.addState(new ArcadeState(controls));
    	this.addState(new TutorialState());
    	this.addState(new OptionsState(controls));
    }
 
}