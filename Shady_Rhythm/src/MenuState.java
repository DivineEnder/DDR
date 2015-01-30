import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;

public class MenuState extends BasicGameState
{
	Circle[] menuCircles;
	String[] menuStrings;
	Point[] menuStringsCoords;
	Shape selector;
	Music backgroundMusic;
	Sound blip;
	Sound select;
	Sound transition;
	Font font = new Font("Verdana", Font.BOLD, 12);
	TrueTypeFont trueFont = new TrueTypeFont(font, false);
	int selected;
	int timer;
	boolean timerGo = false;
	Circle timerCircle;
	Color[] colorArray;

	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		backgroundMusic = new Music("/data/Sound Effects/background.wav");
		backgroundMusic.loop();
		
		blip = new Sound("/data/Sound Effects/Blip.wav");
		select = new Sound("/data/Sound Testing/Select or Level.wav");
		transition = new Sound("/data/Sound Testing/Transition Laser.wav");
		
		float goldenRatio = (float) ((1 + Math.sqrt(5))/2);
		
		menuCircles = new Circle[3];
		menuCircles[0] = new Circle(gc.getWidth()/5, gc.getHeight()/2, 100);//gc.getHeight()/(3 * goldenRatio));
		menuCircles[1] = new Circle(gc.getWidth()/2, gc.getHeight()/2, 100);//gc.getHeight()/(3 * goldenRatio));
		menuCircles[2] = new Circle(gc.getWidth() - gc.getWidth()/5, gc.getHeight()/2, 100);//gc.getHeight()/(3 * goldenRatio));
		
		colorArray = new Color[]{new Color(255, 0, 0), new Color(255, 255, 0), new Color(0, 255, 0), new Color(0, 255, 255), new Color(0, 0, 255)};
		
		menuStrings = new String[5];
		menuStrings[0] = "Arcade";
		menuStrings[1] = "OPTIONS";
		menuStrings[2] = "Story";
		menuStrings[3] = "EXIT";
		menuStrings[4] = "Tutorial";
		
		menuStringsCoords = new Point[]{new Point(gc.getWidth()/2 - (gc.getWidth()/2 - gc.getWidth()/5)/2, gc.getHeight()/2), new Point(gc.getWidth()/2 + (gc.getWidth()/2 - gc.getWidth()/5)/2, gc.getHeight()/2)};
		
		selector = new Circle(menuCircles[1].getCenterX(), menuCircles[1].getCenterY(), menuCircles[1].getRadius() + 10);
		
		timerCircle = new Circle(gc.getWidth()/2, gc.getHeight()/2 + gc.getHeight()/3, 50);
		
		selected = 2;
		timer = 0;
	}

	public void drawStringDown(float x, float y, String string, Graphics g)
	{
		int height = g.getFont().getHeight(string);
		String[] stringChars = new String[string.length()];
		for (int i = 1; i < string.length() + 1; i++)
			stringChars[i - 1] = string.substring(i - 1, i);
		
		for (int i = 0; i < stringChars.length; i++)
		{
			g.drawString(stringChars[i], x - g.getFont().getWidth(stringChars[i])/2, (y - (height * (stringChars.length/2))) + (i * height));
		}
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		timerGo = false;
		timer = 0;
		if (!backgroundMusic.playing())
			backgroundMusic.loop();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		g.setFont(trueFont);
		
		g.setColor(colorArray[0]);
		g.fill(menuCircles[0]);
		g.setColor(Color.black);
		g.drawString(menuStrings[0], menuCircles[0].getCenterX() - g.getFont().getWidth(menuStrings[0])/2, menuCircles[0].getCenterY() - g.getFont().getHeight(menuStrings[0])/2);
		
		g.setColor(colorArray[1]);
		drawStringDown(menuStringsCoords[0].getX(), menuStringsCoords[0].getY(), menuStrings[1], g);
		
		g.setColor(colorArray[2]);
		g.fill(menuCircles[1]);
		g.setColor(Color.black);
		g.drawString(menuStrings[2], menuCircles[1].getCenterX() - g.getFont().getWidth(menuStrings[2])/2, menuCircles[1].getCenterY() - g.getFont().getHeight(menuStrings[2])/2);
		
		g.setColor(colorArray[3]);
		drawStringDown(menuStringsCoords[1].getX(), menuStringsCoords[1].getY(), menuStrings[3], g);
		
		g.setColor(colorArray[4]);
		g.fill(menuCircles[2]);
		g.setColor(Color.black);
		g.drawString(menuStrings[4], menuCircles[2].getCenterX() - g.getFont().getWidth(menuStrings[4])/2, menuCircles[2].getCenterY() - g.getFont().getHeight(menuStrings[4])/2);
		
		if (timerGo)
		{
			g.setLineWidth(3);
			g.setColor(colorArray[selected]);
			g.draw(timerCircle);
			
			g.setAntiAlias(false);
			g.fillArc(timerCircle.getX(), timerCircle.getY(), timerCircle.getRadius() * 2, timerCircle.getRadius() * 2, 270, 270 + (timer * (360f/100f)));
			g.setAntiAlias(true);
		}
		
		g.setLineWidth(10);
		g.setColor(new Color(255, 255, 255));
		g.draw(selector);
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (key == Input.KEY_H)
		{
			timerGo = true;
		}
		else if (key == Input.KEY_J)
		{
			timerGo = true;
		}
		else if (key == Input.KEY_K)
		{
			timerGo = true;
		}
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_H)
		{
			timerGo = false;
			timer = 0;
		}
		else if (key == Input.KEY_J)
		{
			timerGo = false;
			timer = 0;
		}
		else if (key == Input.KEY_K)
		{
			timerGo = false;
			timer = 0;
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (timer == 100)
		{
			backgroundMusic.fade(750, 0, true);
			transition.play();
			select.play();
			if (selected == 3)
				gc.exit();
			else
				state.enterState(selected + 1, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		}
		
		int thisIteration = selected;
		
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J))
		{
			selector = new Rectangle(menuStringsCoords[0].getX() - 20, menuStringsCoords[0].getY() - 51, 40, 120);
			selected = 1;
		}
		else if (input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))
		{
			selector = new Rectangle(menuStringsCoords[1].getX() - 20, menuStringsCoords[1].getY() - 37, 40, 74);
			selected = 3;
		}
		else if (input.isKeyPressed(Input.KEY_H) && selected != 0)
		{
			selector = new Circle(menuCircles[0].getCenterX(), menuCircles[0].getCenterY(), menuCircles[0].getRadius() + 10);
			selected = 0;
		}
		else if (input.isKeyPressed(Input.KEY_J) && selected != 2)
		{
			selector = new Circle(menuCircles[1].getCenterX(), menuCircles[1].getCenterY(), menuCircles[1].getRadius() + 10);
			selected = 2;
		}
		else if (input.isKeyPressed(Input.KEY_K) && selected != 4)
		{
			selector = new Circle(menuCircles[2].getCenterX(), menuCircles[2].getCenterY(), menuCircles[2].getRadius() + 10);
			selected = 4;
		}
		
		if (selected != thisIteration)
		{
			blip.play();
		}
		
		if (timerGo)
			timer++;
			
	}
	
	public int getID()
	{
		return 0;
	}
}