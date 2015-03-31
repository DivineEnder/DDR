import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ArcadeState extends BasicGameState
{
	float windowWidth;
	float windowHeight;
	
	//Creates a variable for a custom font to draw in
	TrueTypeFont[] circleFonts;
	
	PadInput pads;
	
	Engine engine;
	Rhythms engineRhythm;
	StateHandler stateHandler;
	
	ArrayList<Rhythms> rhythmsList;
	
	int[] displayedIndexes;
	Circle[] displayCircles;
	Color[] displayColors;
	
	Music backgroundMusic;
	
	Color[] timerColors;
	int[] timers;
	boolean[] timersGo;
	
	public ArcadeState(Rhythms rhythm, StateHandler sh, PadInput p)
	{
		engineRhythm = rhythm;
		stateHandler = sh;
		
		pads = p;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes a new java awt font from a file
		Font font = null;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("data/Fonts/belerenbold.ttf"));
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (FontFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		circleFonts = new TrueTypeFont[5];
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 5);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		circleFonts[0] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 20);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		circleFonts[1] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		circleFonts[2] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 20);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		circleFonts[3] = new TrueTypeFont(font, false);
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 5);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		circleFonts[4] = new TrueTypeFont(font, false);
		
		backgroundMusic = new Music("data/Sound Testing/Dubstep Filler or Starter.wav");
		
		File dir = new File("data/Music/");
		File[] songs = dir.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String filename)
			{
				return filename.endsWith(".wav");
			}
		});
		
		rhythmsList = new ArrayList<Rhythms>();
		for (int i = 0; i < songs.length; i++)
			rhythmsList.add(new Rhythms());
		for (int i = 0; i < songs.length; i++)
			rhythmsList.get(i).setRhythm(songs[i].getName().substring(0, songs[i].getName().length() - 4));
		
		displayCircles = new Circle[5];
		displayCircles[0] = new Circle(windowHeight/9, windowHeight/3, windowHeight/9);
		displayCircles[1] = new Circle(windowWidth/4, windowHeight/3, windowHeight/6);
		displayCircles[2] = new Circle(windowWidth/2, windowHeight/3, windowHeight/4);
		displayCircles[3] = new Circle(windowWidth * 3/4, windowHeight/3, windowHeight/6);
		displayCircles[4] = new Circle(windowWidth - windowHeight/9, windowHeight/3, windowHeight/9);
		
		displayColors = new Color[5];
		displayColors[0] = new Color(1.0f, 1.0f, 1.0f, .2f);
		displayColors[1] = new Color(1.0f, 1.0f, 1.0f, .7f);
		displayColors[2] = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		displayColors[3] = new Color(1.0f, 1.0f, 1.0f, .7f);
		displayColors[4] = new Color(1.0f, 1.0f, 1.0f, .2f);
		
		displayedIndexes = new int[5];
		for (int i = 0; i < displayedIndexes.length; i++)
			displayedIndexes[i] = i;
		
		timerColors = new Color[3];
		timerColors[0] = new Color(173f/255f, 16f/255f, 16f/255f);
		timerColors[1] = new Color(22f/255f, 161f/255f, 22f/255f);
		timerColors[2] = new Color(10f/255f, 29f/255f, 145f/255f);
		timers = new int[]{0, 0, 0};
		timersGo = new boolean[]{false, false, false};
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		//backgroundMusic.loop();
		//backgroundMusic.setVolume(stateHandler.getMusicVolume());
		
		pads.clearPadPressedRecord();
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		//backgroundMusic.stop();
		stateHandler.leavingState(state.getCurrentStateID());
		
		for (int i = 0; i < timers.length; i++)
		{
			timers[i] = 0;
			timersGo[i] = false;
		}
		
		pads.clearPadPressedRecord();
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (key == Input.KEY_H)
		{
			timersGo[0] = true;
			rhythmsList.get(displayedIndexes[1]).currentSong.play();
		}
		else if (key == Input.KEY_J)
		{
			timersGo[1] = true;
			rhythmsList.get(displayedIndexes[2]).currentSong.play();
		}
		else if (key == Input.KEY_K)
		{
			timersGo[2] = true;
			rhythmsList.get(displayedIndexes[3]).currentSong.play();
		}
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_H)
		{
			timersGo[0] = false;
			timers[0] = 0;
			rhythmsList.get(displayedIndexes[1]).currentSong.stop();
		}
		else if (key == Input.KEY_J)
		{
			timersGo[1] = false;
			timers[1] = 0;
			rhythmsList.get(displayedIndexes[2]).currentSong.stop();
		}
		else if (key == Input.KEY_K)
		{
			timersGo[2] = false;
			timers[2] = 0;
			rhythmsList.get(displayedIndexes[3]).currentSong.stop();
		}
	}
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		if (pads.usePads)
		{
			if (pads.input == 1)
				timersGo[0] = true;
			else if (pads.input == 3)
				timersGo[1] = true;
			else if (pads.input == 5)
				timersGo[2] = true;
			else
			{
				for (int i = 0; i < timersGo.length; i++)
					timersGo[i] = false;
			}
		}
		
		if (timers[0] == 100)
		{
			rhythmsList.get(displayedIndexes[1]).currentSong.stop();
			
			for (int i = 0; i < displayedIndexes.length; i++)
			{
				displayedIndexes[i]--;
				if (displayedIndexes[i] < 0)
					displayedIndexes[i] += rhythmsList.size();
			}
			
			timersGo[0] = false;
			timers[0] = 0;
		}
		else if (timers[1] == 100)
		{
			rhythmsList.get(displayedIndexes[2]).currentSong.stop();
			
			engineRhythm.setRhythm(rhythmsList.get(displayedIndexes[2]).title + " - " + rhythmsList.get(displayedIndexes[2]).artist);
			state.enterState(4);
		}
		else if (timers[2] == 100)
		{
			rhythmsList.get(displayedIndexes[3]).currentSong.stop();
			
			for (int i = 0; i < displayedIndexes.length; i++)
			{
				displayedIndexes[i]++;
				if (displayedIndexes[i] > rhythmsList.size() - 1)
					displayedIndexes[i] -= rhythmsList.size() - 1;
			}
			
			timersGo[2] = false;
			timers[2] = 0;
		}
		
		for (int i = 0; i < timers.length; i++)
		{
			if (timersGo[i])
				timers[i]++;
		}
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		g.setLineWidth(5);
		
		g.setColor(new Color(84, 84, 84));
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
		
		g.setColor(Color.white);
		for (int i = 0; i < displayCircles.length; i++)
		{
			g.setColor(displayColors[i]);
			g.fill(displayCircles[i]);
			g.setFont(circleFonts[i]);
			g.setColor(Color.black);
			g.drawString(rhythmsList.get(displayedIndexes[i]).title, displayCircles[i].getCenterX() - g.getFont().getWidth(rhythmsList.get(displayedIndexes[i]).title)/2, displayCircles[i].getCenterY() - g.getFont().getHeight(rhythmsList.get(displayedIndexes[i]).title)/2);
		}
		
		for (int i = 0; i < timers.length; i++)
		{
			g.setColor(new Color(timerColors[i].r, timerColors[i].g, timerColors[i].b, .7f));
			g.draw(displayCircles[i + 1]);
		}
		for (int i = 0; i < timers.length; i++)
		{
			g.setColor(timerColors[i]);
			g.drawArc(displayCircles[i + 1].getX(), displayCircles[i + 1].getY(), displayCircles[i + 1].getRadius() * 2, displayCircles[i + 1].getRadius() * 2, 270, 270 + (timers[i] * (360f/100f)));
			//g.drawArc(displayCircles[i + 1].getX(), displayCircles[i + 1].getY(), displayCircles[i + 1].getRadius(), displayCircles[i + 1].getRadius(), 0, 270, 270);
		}
	}

	public int getID()
	{
		return 1;
	}
}
