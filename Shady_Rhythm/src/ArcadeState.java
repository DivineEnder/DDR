import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ArcadeState extends BasicGameState
{
	Controls controls;
	String controlInput;
	int radius;
	int numSongs;
	String[] songs;
	Point[] songP;
	int[] songA;
	int transition;
	int selected;
	int phase;
	int sideLength;
	int angleLength;
	int endLength;
	int opacityForTest = 0;
	
	ArcadeState(Controls c)
	{
		controls = c;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		controlInput = "";
		radius = gc.getScreenWidth()/3;
		numSongs = 10;
		transition = 0;
		songs = new String[10];
		selected = 0;
		phase = 0;
		sideLength = 0;
		angleLength = 0;
		endLength = 0;
		
		songs[0] = "Am I Wrong";
		songs[1] = "Rather Be";
		songs[2] = "Humming Bird";
		songs[3] = "Human";
		songs[4] = "Stay with Me";
		songs[5] = "Rude";
		songs[6] = "Burn";
		songs[7] = "Handy";
		songs[8] = "I'm Ready";
		songs[9] = "Hope";
		
		songP = new Point[numSongs];
		songA = new int[numSongs];
		
		
		for (int i = 0; i < songP.length; i++)
		{
			songA[i] = i * (360 / numSongs);
			songP[i] = new Point((float) (radius * Math.cos((Math.PI / 180) * songA[i])), (gc.getScreenHeight() / 2) - (float) (radius * Math.sin((Math.PI / 180) * songA[i])));
		}
	}
	
	@Override
    public void keyPressed(int key, char c)
    {
		String in = (String) controls.getKeyMapping().get(key);
		if (in != null)
			controlInput = in;
    }
	
	private void transitionPhase(GameContainer gc)
	{
		for (int i = 0; i < songA.length; i++)
		{
			songA[i] += transition;
		}
		
		for (int i = 0; i < songP.length; i++)
		{
			songP[i] = new Point((float) (radius * Math.cos((Math.PI / 180) * songA[i])), (gc.getScreenHeight() / 2) - (float) (radius * Math.sin((Math.PI / 180) * songA[i])));
		}
		
		if (songA[0] % (360 / numSongs) == 0)
		{
			if (transition > 0)
				transition--;
			else
				transition++;
		}
	}
	
	private void transitionEffect()
	{
		opacityForTest += 10;
		if (endLength <= 70 && endLength >= 0)
			endLength += phase;
		if (angleLength != 90 && angleLength != 0)
			angleLength += phase;
		sideLength += 5 * phase;
		if (sideLength >= 600 || sideLength <= 0)
			phase = 0;
	}
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (controlInput.equals("1"))
		{
			if (sideLength == 600)
			{
				phase = -3;
				endLength += phase;
				angleLength += phase;
				sideLength += 5 * phase;
				opacityForTest = 0;
			}
			transition = 2;
		}
		
		if (controlInput.equals("2"))
		{
			if (sideLength == 600)
			{
				phase = -3;
				endLength += phase;
				angleLength += phase;
				sideLength += 5 * phase;
				opacityForTest = 0;
			}
			transition = 1;
		}
		
		if (controlInput.equals("3"))
		{
			if (sideLength == 0)
			{
				phase = 3;
				endLength += phase;
				angleLength += phase;
				sideLength += 5 * phase;
			}
		}
		
		if (controlInput.equals("4"))
		{
			if (sideLength == 600)
			{
				phase = -3;
				endLength += phase;
				angleLength += phase;
				sideLength += 5 * phase;
				opacityForTest = 0;
			}
			transition = -1;
		}
		
		if (controlInput.equals("5"))
		{
			if (sideLength == 600)
			{
				phase = -3;
				endLength += phase;
				angleLength += phase;
				sideLength += 5 * phase;
				opacityForTest = 0;
			}
			transition = -2;
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		
		if (transition != 0)
			transitionPhase(gc);
		
		if (phase != 0)
			transitionEffect();
		
		controlInput = "";
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		g.setColor(Color.cyan);
		g.draw(new Circle(0, gc.getScreenHeight()/2, radius));
		
		for (int i = 0; i < numSongs; i++)
		{
			g.setColor(Color.cyan);
			g.fill(new Circle(songP[i].getX(), songP[i].getY(), 50));
			g.setColor(Color.white);
			g.drawString(songs[i], songP[i].getX(), songP[i].getY());
		}
		
		Color opacityTest = new Color(255,46,0,opacityForTest);
		g.setColor(opacityTest);
		g.draw(new Circle((float) (radius * Math.cos(0)), (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)), 55));
		
		g.drawArc((float) (radius * Math.cos(0)) - 70, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) - 70, 140, 140, 0, 0 + angleLength);
		g.drawArc((float) (radius * Math.cos(0)) - 70, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) - 70, 140, 140, 360 - angleLength, 0);
		g.drawLine((float) (radius * Math.cos(0)), (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) - 70, (float) (radius * Math.cos(0)) + sideLength, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) - 70);
		g.drawLine((float) (radius * Math.cos(0)), (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) + 70, (float) (radius * Math.cos(0)) + sideLength, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) + 70);
		g.drawLine((float) (radius * Math.cos(0)) + 600, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)), (float) (radius * Math.cos(0)) + 600, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) - endLength);
		g.drawLine((float) (radius * Math.cos(0)) + 600, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)), (float) (radius * Math.cos(0)) + 600, (gc.getScreenHeight() / 2) - (float) (radius * Math.sin(0)) + endLength);
		
		
		//White Green Blue Red Yellow
		
		/*ShapeFill fillWhite = new GradientFill(0, gc.getScreenHeight()/4 * 3, Color.white, 0, 0, Color.black);
		ShapeFill fillGreen = new GradientFill(0, gc.getScreenHeight()/5 * 4, Color.green, 0, 0, Color.black);
		//ShapeFill fillBlue = new GradientFill(0, gc.getScreenHeight()/2, Color.blue, 0, 0, Color.black);
		ShapeFill fillRed = new GradientFill(0, gc.getScreenHeight()/2, Color.red, 0, gc.getScreenHeight(), Color.black);
		ShapeFill fillYellow = new GradientFill(0, gc.getScreenHeight()/2, Color.yellow, 0, gc.getScreenHeight(), Color.black);
		
		float rectWidth = gc.getScreenWidth() * .25f;
		float rectHeight = 150;
		
		g.setColor(Color.blue);
		g.setLineWidth(5);
		g.drawArc(210 + (rectWidth/(4/3)), 10 + rectHeight + 30 + rectHeight + 30 - rectHeight - rectHeight, 1000, 750, 180, 360);
		
		g.fill(new Rectangle(10, 10, rectWidth, rectHeight), fillWhite);
		g.fill(new Rectangle(110, 10 + rectHeight + 30, rectWidth, rectHeight), fillGreen);
		g.fill(new Rectangle(210, 10 + rectHeight + 30 + rectHeight + 30, rectWidth, rectHeight));
		g.fill(new Rectangle(110, 10 + rectHeight + 30 + rectHeight + 30 + rectHeight + 30, rectWidth, rectHeight), fillRed);
		g.fill(new Rectangle(10, 10 + rectHeight + 30 + rectHeight + 30 + rectHeight + 30 + rectHeight + 30, rectWidth, rectHeight), fillYellow);*/
		
		//g.drawString("Arcade State", 0, 0);
	}

	public int getID()
	{
		return 2;
	}
}
