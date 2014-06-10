import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Engine
{
	RadarCircles rc;
	Selector selector;
	Music song;
	ArrayList<RhythmCircle> circles;
	RhythmCircle circle;
	
	Engine(GameContainer gc)
	{
		rc = new RadarCircles(gc.getWidth(), gc.getHeight());
		selector = new Selector();
		song = null;
		circles = new ArrayList();
	}
	
	public void reset()
	{
		song.fade(750, 0, true);
	}
	
	public void selectSong(String musicPath, String rhythmPath, GameContainer gc)
	{
		try{song = new Music(musicPath);} catch (SlickException e) {}
		song.play();
		selector.start();
		float[][] data = null;
    	ReadSong read = new ReadSong(rhythmPath);
    	try {data = read.OpenFile();} catch (IOException e) {}
    	for (int i = 0; i < data.length; i++)
    	{
    		circles.add(new RhythmCircle(data[i][0], (int) data[i][1], (int) data[i][2], gc));
    	}
	}
	
	public void update(GameContainer gc, ArrayList<String> specialInput)
	{
		Input input = gc.getInput();
		
		selector.updateSelector();
		
		for (int j = 0; j < specialInput.size(); j++)
		{
			rc.keyPressed(specialInput.get(j));
			for (int i = 0; i < circles.size(); i++)
			{
				if (circles.get(i).checkVisible())
					circles.get(i).keyPressed(specialInput.get(j));
				else if (circles.get(i).checkTermination() == 1)
					circles.remove(i);
			}
		}
	}
	
	public void render(GameContainer gc, final Graphics g)
	{
		rc.draw(g);
		g.setColor(Color.cyan);
		
		g.drawString(Float.toString(selector.getAngle()), 0, 0);
		g.drawString(Integer.toString(selector.getRotations()), 0, 20);
		
		for (int i = 0; i < circles.size(); i++)
			circles.get(i).drawCircle(g, selector);
		
		//g.draw(new Rectangle(gc.getScreenWidth() - 200, 10, 50, 190));
		//g.setColor(Color.red);
		//g.draw(new Rectangle(gc.getScreenWidth() - 200, 200, 50, 190));
		//g.setColor(Color.black);
		//g.fill(new Rectangle(gc.getScreenWidth() - 198, 198, 46, 4));
		
	}
}
