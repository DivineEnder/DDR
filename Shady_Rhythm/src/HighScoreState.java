import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
	
	ArrayList<String> songs;
	ArrayList<float[]> highScores;
	
	HighScoreState(PadInput p)
	{
		pads = p;
	}
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		//Gets the window width and height from the gamecontainer
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Initializes the font to draw the players recent score in
		Font font = new Font("Courier", Font.BOLD, 35);
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
		
		songs = new ArrayList<String>();
		highScores = new ArrayList<float[]>();
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
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
			if (pads.usePads)
			{
				if (pads.input == 15)
				{
					state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
				}
				else if (pads.input == 1)
				{
					
				}
			}
			else
			{
				Input input = gc.getInput();
				
				if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))
				{
					state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
				}
				
				if (input.isKeyPressed(Input.KEY_H))
				{
					selected--;
					if (selected < 0)
						selected += songs.size();
				}
				else if (input.isKeyPressed(Input.KEY_K))
				{
					selected++;
					if (selected > songs.size() - 1)
						selected -= songs.size();
				}
			}
		}
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
			g.drawString(songs.get(selected), windowWidth/2 - g.getFont().getWidth(songs.get(selected))/2, g.getFont().getHeight(songs.get(selected))/2);
			
			g.setFont(wordFont);
			
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the accuracy string above the accuracy circle
			g.drawString("Accuracy", windowWidth/2 - g.getFont().getWidth("Accuracy")/2, windowHeight/2 - 325 - g.getFont().getHeight("Accuracy")/2);
			
			//Sets the graphics color to white
			g.setColor(Color.white);
			//Draws the accuracy as a percentage of a circle (using angle measures)
			g.fillArc(windowWidth/2 - 150, windowHeight/2 - 150 - 150, 300, 300, 270, 270 + (360 * highScores.get(selected)[0]));
			
			//Converts the score percentage from a float to a string
			String displayScoreString = Float.toString(highScores.get(selected)[0] * 100).substring(0, 2) + "%";
			//Checks to see if the percentage is 100 (need a different portion of the sting if that is the case)
			if (highScores.get(selected)[0] >= 1)
				displayScoreString = Float.toString(highScores.get(selected)[0] * 100).substring(0, 3) + "%";
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the percentage string in the middle of the percentage circle
			g.drawString(displayScoreString, windowWidth/2 - g.getFont().getWidth(displayScoreString)/2, windowHeight/2 - 150 - g.getFont().getHeight(displayScoreString)/2);
			
			//Draws the color categorized accuracy score as a portion of a circle
			for (int i = 0; i < accuracyColors.length; i++)
			{
				//Sets the color to the accuracy color of the individual circles
				g.setColor(accuracyColors[i]);
				////Draws the accuracy as a percentage of a circle (using angle measures)
				g.fillArc(windowWidth * (1 + (2 * i))/10 - 75, windowHeight * 3/4 - 50, 150, 150, 270, 270 + (360 * highScores.get(selected)[i+1]));
			}
			
			//Draws the color categorized accuracy strings
			for (int i = 0; i < highScores.get(selected).length; i++)
			{
				//Converts the color categorized score percentage from a float to a string
				String displayColorScoreString = Float.toString(highScores.get(selected)[i] * 100).substring(0, 2) + "%";
				//Checks to see if the color categorized score percentage is 100 (need a different portion of the sting if that is the case)
				if (highScores.get(selected)[i] >= 1)
					displayColorScoreString = Float.toString(highScores.get(selected)[i] * 100).substring(0, 3) + "%";
				//Sets the graphics color to black
				g.setColor(Color.black);
				//Draws the color categorized percentage strings in the middle of their corresponding percentage circles
				g.drawString(displayColorScoreString, windowWidth * (1 + (2 * i))/10 - g.getFont().getWidth(displayColorScoreString)/2, windowHeight * 3/4 + 25 - g.getFont().getHeight(displayColorScoreString)/2);
			}
		}
	}

	public int getID()
	{
		return 5;
	}
}