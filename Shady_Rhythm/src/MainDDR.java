import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
 
public class MainDDR extends StateBasedGame
{
	Controls controls = new Controls();
	
	public MainDDR(String title)
	{
        super(title);
    }
	
    public static void main(String[] args) throws SlickException
    {
    	AppGameContainer app = new AppGameContainer(new MainDDR("Shady Rhythms"));
        
        app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
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
    	this.addState(new ArcadeState());
    	this.addState(new TutorialState());
    	this.addState(new OptionsState(controls));
    }
 
}

/*import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MainDDR extends BasicGame
{
	int GameState;
	String specialInput;
	HashMap test = new HashMap();
	
    public MainDDR()
    {
        super("Rhythms");
    }
  
    @Override
    public void init(GameContainer gc) throws SlickException 
    {
    	specialInput = "";
    	GameState = 0;
    	test.put(Input.KEY_Q, "IT FUCKING WORKED");
    }
    
    @Override
    public void keyPressed(int key, char c)
    {
        if (c == '`')
            specialInput += "`";
        specialInput = (String) test.get(key);
    }
    
    @Override
    public void update(GameContainer gc, int delta) throws SlickException
    {
    	Input input = gc.getInput();
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
    	g.setAntiAlias(true);
    	//gc.setShowFPS(false);
    }
    
    public static MainDDR getReference()
    {
    	MainDDR Game = new MainDDR();
    	return Game;
    }
  
    public static void main(String[] args) throws SlickException, InterruptedException, BrokenBarrierException
    {
        AppGameContainer app = new AppGameContainer(new MainDDR());
        
        app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
        app.setTargetFrameRate(60);
        app.setVSync(true);
        app.start();
    }
}*/