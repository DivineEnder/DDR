import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class CreditState extends BasicGameState
{
	float windowWidth;
	float windowHeight;
	
	//Creates a variable for a custom font to draw in
	TrueTypeFont wordFont;
	
	PadInput pads;
	
	Music backgroundMusic;
	
	String[] credits;
	
	float creditsY;
	float creditsX;
	
	CreditState(PadInput p)
	{
		pads = p;
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		creditsY = windowHeight;
		creditsX = windowWidth/2;
		
		backgroundMusic.play();
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame state)
	{
		backgroundMusic.stop();
	}
	
	@Override
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
		//Sets the font to be plain and have a size of 30
		font = font.deriveFont(Font.PLAIN, 30);
		//Initializes the font to a truetypefont which can be used to draw strings on the screen in a custom font
		wordFont = new TrueTypeFont(font, false);
		
		Thread thread = new Thread()
		{
			//Basic function that runs each time the thread is run
			public void run()
			{
				System.out.println("LOADING: Loading credits from file");
				ReadSong read = new ReadSong("data/Credits/Credits.txt");
				
				try {credits = read.openFile();} catch (IOException e) {System.out.println(e);}
				System.out.println("FINISHED: Loaded credits from file");
				
				System.out.println("LOADING: Loading credits music");
				try {backgroundMusic = new Music("data/Credits/Never Gonna Give You Up - Rick Astley.wav");} catch (SlickException e) {e.printStackTrace();}
				System.out.println("FINISHED: Loaded credits music");
			}
		};
		thread.start();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		g.setFont(wordFont);
		
		g.setColor(new Color(84, 84, 84));
		g.fill(new Rectangle(0, 0, windowWidth, windowHeight));
		
		g.setColor(Color.black);
		float addedHeight = 0;
		for (int i = 0; i < credits.length; i++)
		{
			g.drawString(credits[i], creditsX - g.getFont().getWidth(credits[i])/2, creditsY + addedHeight);
			addedHeight += g.getFont().getHeight(credits[i]) + 10;
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		creditsY--;
		
		if (!backgroundMusic.playing() || input.isKeyPressed(Input.KEY_S) || pads.input == 6)
			state.enterState(0, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.white, 1500));
	}

	@Override
	public int getID()
	{
		return 9;
	}

}
