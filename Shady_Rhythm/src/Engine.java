import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.BasicGame;
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
	boolean playingSong = false;
	ArrayList<Particles> particles;
	RhythmCircle circle;
	Rhythms rhythm;
	
	Engine(GameContainer gc, Rhythms r)
	{
		rc = new RadarCircles(gc.getWidth(), gc.getHeight());
		rhythm = r;
		selector = new Selector();
		points = new Score();
		particles = new ArrayList<Particles>();
	}
	
	public void start()
	{
		selector.start();
	}
	
	public void pause()
	{
		selector.pause();
		if (rhythm.currentSong.playing())
			rhythm.currentSong.pause();
	}
	
	public void play()
	{
		selector.start();
		rhythm.currentSong.resume();
	}
	
	public void reset()
	{
		selector = new Selector();
		rhythm.currentSong.stop();
	}
	
	public void update(GameContainer gc)
	{
		Input input = gc.getInput();
		
		if (selector.getRotations() == 0 && selector.getAngle() == (270 - (int) rhythm.circleList.get(0).getStartingAngle()))
			rhythm.currentSong.play();
		
		selector.updateSelector();
		
		rhythm.updateRhythmCircleList(selector);
		
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J))
		{
			rc.keyPressed("2");
		}
		
		else if (input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K))	
		{
			rc.keyPressed("4");
		}
		
		else if (input.isKeyDown(Input.KEY_H))
		{
			rc.keyPressed("1");
		}
		
		else if (input.isKeyDown(Input.KEY_J))
		{
			rc.keyPressed("3");
		}
		
		else if (input.isKeyDown(Input.KEY_K))
		{
			rc.keyPressed("5");
		}
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		g.setColor(Color.white);
		g.fill(new Rectangle(0, 0, gc.getScreenWidth(), gc.getScreenHeight()));
		
		rc.draw(g);
		g.setColor(Color.cyan);
		
		g.drawString(Float.toString(selector.getAngle()), 0, 0);
		g.drawString(Integer.toString(selector.getRotations()), 0, 20);
		
		for (int i = 0; i < rhythm.circleList.size(); i++)
		{
			rhythm.circleList.get(i).draw(g, selector);
			//if (!rhythm.circleList.get(i).draw(g))
			//	if (i == rhythm.circleList.size()-1 || !rhythm.circleList.get(i + 1).getVisible() )
					//break;
		}
	}
}
