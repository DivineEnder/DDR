import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

public class StoryParser
{
	String[][] story;
	String[] currentDisplay;
	String name;
	
	String rhythm;
	
	int textSpeed;
	int textCounter;
	int displayLineCounter;
	
	StoryParser(String filePath, TrueTypeFont wordFont, float maxLength)
	{	
		ReadSong read = new ReadSong(filePath);
		
		String[] fullStory = null;
		try {fullStory = read.openFile();} catch (IOException e) {System.out.println(e);}
		
		rhythm = fullStory[0];
		name = fullStory[1];
		
		story = new String[fullStory.length - 2][4];
		
		for (int i = 2; i < story.length + 2; i++)
			story[i - 2] = split(fullStory[i], wordFont, maxLength);
		
		setDisplay();
	}
	
	public String[] split(String fullLines, TrueTypeFont wordFont, float maxLength)
	{
		String[] displayLines = new String[]{"", "", "", ""};
		int timesSplit = 0;
		int lastSplit = 0;
		
		for (int i = 1; i < fullLines.length(); i++)
		{
			String partial = fullLines.substring(lastSplit, i);;
			
			if (i == fullLines.length() - 1)
			{
				partial = fullLines.substring(lastSplit, i + 1);
				displayLines[timesSplit] = partial;
				lastSplit = i;
				timesSplit++;
			}
			else if (wordFont.getWidth(partial) >= maxLength)
			{
				if (fullLines.charAt(i) == ' ')
				{
					displayLines[timesSplit] = partial;
					lastSplit = i;
				}
				else if (partial.charAt(0) == ' ')
				{
					displayLines[timesSplit] = partial.substring(1);
					lastSplit = i;
				}
				else
				{
					int lastSpace = partial.lastIndexOf(' ');
					partial = partial.substring(0, lastSpace);
					displayLines[timesSplit] = partial;
					lastSplit = lastSplit + partial.length() + 1;
				}
				timesSplit++;
			}
		}
		
		return displayLines;
	}
	
	public void setDisplay()
	{
		currentDisplay = new String[]{"", "", "", ""};
		textSpeed = 1;
		textCounter = 0;
		displayLineCounter = 0;
	}
	
	private void addCurrentDisplay(int storyPosition)
	{
		textCounter++;
		
		if (textCounter % textSpeed == 0)
		{
			if (currentDisplay[displayLineCounter].equals(story[storyPosition][displayLineCounter]))
			{
				displayLineCounter++;
				textCounter = 1;
			}
			else
				currentDisplay[displayLineCounter] = story[storyPosition][displayLineCounter].substring(0, textCounter / textSpeed);
		}
	}
	
	public void drawText(TrueTypeFont wordFont, int storyPosition, Rectangle textBox, GameContainer gc, Graphics g)
	{
		//g.setColor(new Color(84, 84, 84));
		g.fill(new RoundedRectangle(textBox.getX(), textBox.getY() - (wordFont.getHeight(name) + 20), wordFont.getWidth(name) + 20, wordFont.getHeight(name) + 20, 20));
		wordFont.drawString(textBox.getX() + 10, textBox.getY() - (wordFont.getHeight(name) + 20) + 10, name);
		
		if (displayLineCounter < currentDisplay.length)
			addCurrentDisplay(storyPosition);
		
		for (int i = 0; i < currentDisplay.length; i++)
		{
			wordFont.drawString(textBox.getX() + 10, textBox.getY() + 10 + (wordFont.getHeight(currentDisplay[i]) * i) + (5 * i), currentDisplay[i]);
		}
	}
}
