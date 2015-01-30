import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StoryState extends BasicGameState
{
	StoryParser[] story;
	Rhythms[] rhythmList;
	Rhythms engineRhythm;
	TrueTypeFont wordFont;
	int storyPosition;
	
	StoryState(Rhythms rhythm)
	{
		engineRhythm = rhythm;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		Font font = new Font("Verdana", Font.BOLD, 32);
		wordFont = new TrueTypeFont(font, true);
		
		story = new StoryParser[]{new StoryParser("data/Story/story.txt", wordFont, gc.getWidth() - (20 * 4))};
		
		rhythmList = new Rhythms[1];
		for (int i = 0; i < rhythmList.length; i++)
			rhythmList[i] = new Rhythms();
		rhythmList[0].setRhythm("Do I Wanna Know - Arctic Monkeys");
		
		storyPosition = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		g.setColor(Color.white);
		g.fillRect(0, (gc.getHeight() * 2/3) - (2 * 20), gc.getWidth(), gc.getHeight() /2);
		
		g.setColor(Color.black);
		g.fillRoundRect(20, (gc.getHeight() * 2/3) - 20, gc.getWidth() - (20 * 2), gc.getHeight() * 1/3, 20);
		
		story[0].drawText(wordFont, storyPosition, gc);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_J))
		{
			story[0].setDisplay();
			if (storyPosition == story[0].story.length - 1)
			{
				engineRhythm.setRhythm(rhythmList[0].title + " - " + rhythmList[0].artist);
				state.enterState(4);
			}
			else
				storyPosition++;
		}
	}

	@Override
	public int getID()
	{
		return 3;
	}
	
}
