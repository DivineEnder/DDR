import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class Engine
{
	RadarCircles rc;
	Selector selector;
	Music song;
	ArrayList<RhythmCircle> circles;
	RhythmCircle circle;
	int points;
	int maxPoints;
	Image Background;
	int bDeltaX;
	int addition;
	
	Engine(GameContainer gc)
	{
		rc = new RadarCircles(gc.getWidth(), gc.getHeight());
		selector = new Selector();
		song = null;
		circles = new ArrayList();
		points = 0;
		maxPoints = 1;
		try {Background = new Image("/data/Background.png");} catch (SlickException e) {e.printStackTrace();}
		bDeltaX = 500;
		addition = 1;
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
    	maxPoints = data.length;
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
				{
					points = circles.get(i).keyPressed(specialInput.get(j), points);
					System.out.println(points);
				}
				else if (circles.get(i).checkTermination() == 1)
					circles.remove(i);
			}
		}
		
		if (bDeltaX == 1500)
			addition = -1;
		else if (bDeltaX == 0)
			addition = 1;
		bDeltaX += addition;
	}
	
	public void render(GameContainer gc, final Graphics g)
	{
		Background.draw(0, 0, gc.getScreenWidth()/2, gc.getScreenHeight(), 0 + bDeltaX, 0, 500 + bDeltaX, 600);
		Background.getFlippedCopy(true, false).draw(gc.getScreenWidth()/2, 0, gc.getScreenWidth(), gc.getScreenHeight(), 2000 - (500 + bDeltaX), 0, 2000 - bDeltaX, 600);
		
		rc.draw(g);
		g.setColor(Color.cyan);
		
		g.drawString(Float.toString(selector.getAngle()), 0, 0);
		g.drawString(Integer.toString(selector.getRotations()), 0, 20);
		
		g.draw(new Rectangle(gc.getScreenWidth() - 200, 10, 50, 190));
		g.drawString(points/maxPoints * 100 + "%", gc.getScreenWidth() - 185, 10);
		g.fill(new Rectangle(gc.getScreenWidth() - 200, 200 - (points/maxPoints * 200), 50, (points/maxPoints * 200)));
		
		for (int i = 0; i < circles.size(); i++)
			circles.get(i).drawCircle(g, selector);
		
		//g.setColor(Color.red);
		//g.draw(new Rectangle(gc.getScreenWidth() - 200, 200, 50, 190));
		//g.setColor(Color.black);
		//g.fill(new Rectangle(gc.getScreenWidth() - 198, 198, 46, 4));
		
	}
}
