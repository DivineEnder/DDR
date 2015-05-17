import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

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

public class HighScoreState extends BasicGameState
{
	//Creates integers to hold the window width and height
	int windowWidth;
	int windowHeight;
		
	//Creates a default font to draw with
	TrueTypeFont wordFont;
	//Creates a default font to draw with
	TrueTypeFont songFont;
	
	PadInput pads;
	Loading loadingScreen;
	
	Thread loading;
	
	String loadingString;
	boolean pressAnyKey;
	
	int selected;
	
	Color[] accuracyColors;
	Color[] containerAccuracyColors;
	
	ArrayList<String> songs;
	ArrayList<float[]> highScores;
	ArrayList<Rhythms> rhythmsList;
	
	int[] timer;
	
	
	HighScoreState(PadInput p)
	{
		pads = p;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Gets the window width and height from the gamecontainer
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes a new java awt font from a file
		Font font = null;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("data/Fonts/belerenbold.ttf"));
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (FontFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		wordFont = new TrueTypeFont(font, false);
		
		//Initializes the font to draw the players recent score in
		font = new Font("Courier", Font.BOLD, 50);
		songFont = new TrueTypeFont(font, false);
		
		loadingScreen = new Loading(windowWidth, windowHeight);
		
		selected = 0;
		
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
		
		songs = new ArrayList<String>();
		highScores = new ArrayList<float[]>();
		
		//Creates a new thread that runs through the songs in the data/Music/ directory of the project
		//This is put inside a thread so as to speed up the initial loading phase of the game
		Thread thread = new Thread()
		{
			//Basic function that runs each time the thread is run
			public void run()
			{
				System.out.println("LOADING: Loading songs for high score state");
				
				//Creates a new file that is a directory within the project
				File dir = new File("data/Score/");
				//Gets a list of all the files that are a wav file within the directory defined above
				File[] songs = dir.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String filename)
					{
						return filename.endsWith(".txt");
					}
				});
				
				//Initializes the rhythmList to a new array of Rhythms
				rhythmsList = new ArrayList<Rhythms>();
				//Iterates through and adds indexes to the rhythmList equal to the number of wav files found above
				for (int i = 0; i < songs.length; i++)
					rhythmsList.add(new Rhythms());
				//Iterates through the recently initialized rhythms in the rhythm list and sets them to the wav files read from the directory above
				for (int i = 0; i < songs.length; i++)
					rhythmsList.get(i).setRhythm(songs[i].getName().substring(0, songs[i].getName().length() - 4));
				
				System.out.println("FINISHED: Loaded songs for high score state");
			}
		};
		//Starts the thread
		thread.start();
		
		timer = new int[]{0, 0};
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		timer[0] = 0;
		timer[1] = 0;
		
		loadingString = "Loading High Scores";
		
		loading = new Thread()
		{	
			//Automatically runs when the thread is started
			public void run()
			{
				File dir = new File("data/Score/");
				File[] files = dir.listFiles();
				
				for (int i = 0; i < files.length; i++)
				{
					BufferedReader scoreReader = null;
					songs.add(files[i].getName().substring(0, files[i].getName().length() - 4));
					try
					{
						scoreReader = new BufferedReader(new FileReader(files[i]));
					} catch (FileNotFoundException e) {e.printStackTrace();}
					try
					{
						String[] highScoreStrings = new String[6];
						float[] highScoreFloats = new float[6];
						
						highScoreStrings = scoreReader.readLine().split(",");
						
						for (int j = 0; j < highScoreStrings.length; j++)
							highScoreFloats[j] = Float.parseFloat(highScoreStrings[j]);
						
						highScores.add(highScoreFloats);
					} catch (IOException e) {e.printStackTrace();}
				}
				
				//Sets the loading string to let the player know that gameplay is ready
				loadingString = "Press any pad to continue...";
			}
		};
		
		loading.start();
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (pressAnyKey)
			pressAnyKey = false;
	}
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		if (pressAnyKey)
		{
			if (pads.input != 0)
				pressAnyKey = false;
		}
		else
		{	
			Input input = gc.getInput();
				
			if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_K) || pads.input == 6)
			{
				state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
			}
			
			
			if (input.isKeyDown(Input.KEY_H) || pads.input == 1)
			{
				timer[0]++;
				timer[1] = 0;
			}
			else if (input.isKeyDown(Input.KEY_K) || pads.input == 5)
			{
				timer[1]++;
				timer[0] = 0;
			}
			else
			{
				timer[0] = 0;
				timer[1] = 0;
			}
			
			if (timer[0] == 50)
			{
				timer[0] = 0;
				timer[1] = 0;
				
				selected--;
				if (selected < 0)
					selected += songs.size();
			}
			else if (timer[1] == 50)
			{
				timer[0] = 0;
				timer[1] = 0;
				
				selected++;
				if (selected > songs.size() - 1)
					selected -= songs.size();
			}
			
		}
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
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		if (loading.isAlive())
		{
			//Draws the loading screen
			loadingScreen.draw(g, gc);
			//Draws the loading string
			g.drawString(loadingString, gc.getWidth()/2 - g.getFont().getWidth(loadingString)/2, gc.getHeight() - g.getFont().getHeight(loadingString));
		}
		else
		{
			g.setAntiAlias(true);
			
			g.setColor(new Color(84, 84, 84));
			g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
			
			g.setFont(songFont);
			
			g.setColor(Color.white);
			g.drawString(songs.get(selected).split("-")[0], windowWidth/2 - g.getFont().getWidth(songs.get(selected).split("-")[0])/2, g.getFont().getHeight(songs.get(selected).split("-")[0])/2);
			
			g.setFont(wordFont);
			
			float[] scores = new float[5];
			for (int i = 1; i < highScores.get(selected).length; i++)
				scores[i - 1] = highScores.get(selected)[i];
			
			drawScoreCircle(windowWidth/2, windowHeight * 5/8, scores, Integer.toString((int) (highScores.get(selected)[0] * 100)), windowHeight/3, rhythmsList.get(selected).ringNum, "High Score", g);
			
			g.setColor(new Color(173, 16, 16));
			g.setLineWidth(3);
			g.draw(new Circle(100, 100, 50));
			g.fillArc(100 - 50, 100 - 50, 100, 100, 270, 270 + (timer[0] * (360f/50f)));
			g.setColor(Color.white);
			g.drawString("Left", 100 - g.getFont().getWidth("Left")/2, 100 - g.getFont().getHeight("Left")/2);
			
			g.setColor(new Color(10, 29, 145));
			g.setLineWidth(3);
			g.draw(new Circle(windowWidth - 100, 100, 50));
			g.fillArc(windowWidth - 100 - 50, 100 - 50, 100, 100, 270, 270 + (timer[1] * (360f/50f)));
			g.setColor(Color.white);
			g.drawString("Right", windowWidth - 100 - g.getFont().getWidth("Right")/2, 100 - g.getFont().getHeight("Right")/2);
		}
	}

	public int getID()
	{
		return 5;
	}
}