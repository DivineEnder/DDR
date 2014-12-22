import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	Score points;
	Music song;
	boolean playingSong = false;
	ArrayList<RhythmCircle> circles;
	ArrayList<Particles> particles;
	RhythmCircle circle;
	
	Engine(GameContainer gc)
	{
		rc = new RadarCircles(gc.getWidth(), gc.getHeight());
		selector = new Selector();
		points = new Score();
		song = null;
		circles = new ArrayList<RhythmCircle>();
		particles = new ArrayList<Particles>();
	}
	
	public void start()
	{
		selector.start();
	}
	
	public void reset()
	{
		song.fade(750, 0, true);
	}
	
	public void selectSong(String musicPath, String rhythmPath, GameContainer gc)
	{
		try{song = new Music(musicPath);} catch (SlickException e) {}
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
		if (!playingSong && selector.getAngle() == 180)
		{
			playingSong = true;
			song.play();
		}
			
		for (int i = 0; i < particles.size(); i++)
		{
			if (particles.get(i).checkDraw())
				particles.get(i).update();
			else
				particles.remove(i);
		}
		
		for (int j = 0; j < specialInput.size(); j++)
		{
			rc.keyPressed(specialInput.get(j));
			for (int i = 0; i < circles.size(); i++)
			{
				if (circles.get(i).checkVisible())
				{
					int pointsAdd = circles.get(i).keyPressed(specialInput.get(j));
					if (pointsAdd != -1)
					{
						particles.add(new Particles(new Random().nextInt((15 - 5) + 1) + 5, circles.get(i).getX(), circles.get(i).getY(), pointsAdd, circles.get(i).getColor()));
						points.modPoints(pointsAdd);
					}
				}
			}
		}
		
		for (int i = 0; i < circles.size(); i++)
		{
			if (circles.get(i).checkTermination())
			{
				points.updateMaxPoints();
				circles.remove(i);
			}
		}
		
		if (input.isKeyPressed(Input.KEY_UP))
		{
			points.modPoints(1);
			points.updateMaxPoints();
		}
		if (input.isKeyPressed(Input.KEY_DOWN))
		{
			points.updateMaxPoints();
		}
	}
	
	public void render(GameContainer gc, final Graphics g)
	{
		g.setColor(Color.white);
		g.fill(new Rectangle(0, 0, gc.getScreenWidth(), gc.getScreenHeight()));
		
		rc.draw(g);
		g.setColor(Color.cyan);
		
		g.drawString(Float.toString(selector.getAngle()), 0, 0);
		g.drawString(Integer.toString(selector.getRotations()), 0, 20);
		
		for (int i = 0; i < circles.size(); i++)
			circles.get(i).drawCircle(g, selector);
		
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).draw(g);
		
		points.draw(g, gc);
	}
}
