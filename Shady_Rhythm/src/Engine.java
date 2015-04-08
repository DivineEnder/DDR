import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Engine
{
	int windowWidth;
	int windowHeight;
	
	PadInput pads;
	
	//Creates an instance of the State Handler class
	StateHandler stateHandler;
	
	//Creates an instance of the radar circles to draw in the background
	RadarCircles rc;
	//Creates an instance of the selector to keep track of the angle and rotations of a song
	Selector selector;
	//Creates an instance of the score class to keep track of the score as you play the song
	Score points;
	//Creates an instance of the rhythm class to hold the song and RhythmCircle list
	Rhythms rhythm;
	
	//Constructor
	Engine(GameContainer gc, Rhythms r, Score s, StateHandler sh, PadInput p)
	{
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Sets the rhythm to the universal rhythm passed between game states
		rhythm = r;
		//Sets the points (score class) to the score that is passed between the scorestate and gamestate
		points = s;
		//Sets the state handler to the universal statehandler that is passed to all states
		stateHandler = sh;
		
		pads = p;
	}
	
	//Sets up the engine to start a new song
	public void setup()
	{
		//Creates a new selector to start from the beginning
		selector = new Selector();
		//Initializes the score to start at zero
		points.initalize();
		
		//Creates a new radar circles that are created from the window width and height
		rc = new RadarCircles(windowWidth, windowHeight, rhythm.ringNum);
	}
	
	//Starts the selector and the engine
	public void start()
	{
		selector.start();
	}
	
	//Pauses the song and gameplay
	public void pause()
	{
		//Pauses the selector
		selector.pause();
		//Pauses the song
		if (rhythm.currentSong.playing())
			rhythm.currentSong.pause();
	}
	
	//Starts the game after it has been paused
	public void play()
	{
		//Starts the selector again
		selector.start();
		//Makes the song resume from where it was paused
		if (selector.getRotations() > 0 || selector.getAngle() > (360f - 45 + (int) rhythm.circleList.get(0).getRadiusAsAngle() - rhythm.firstCircleAngle))
			rhythm.currentSong.resume();
	}
	
	//Checks for certain events and updates variables based on those events
	public void update(GameContainer gc, StateBasedGame state)
	{
		//Gets the keyboard input from the game container
		Input input = gc.getInput();
		
		//Checks to see if the rhythm is over and then moves to the score state if it is
		if (rhythm.circleList.size() == 0 && !rhythm.currentSong.playing() || input.isKeyPressed(Input.KEY_S))
			state.enterState(8, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
		
		//Waits until the first circle is in the correct position before it starts playing the song
		if (selector.getRotations() == 0 && selector.getAngle() == (360f - 45 + (int) rhythm.circleList.get(0).getRadiusAsAngle() - rhythm.firstCircleAngle))
			rhythm.currentSong.play(1, stateHandler.getMusicVolume());
		
		//Updates the selector (increasing angle measure)
		selector.updateSelector();
		
		//Updates the rhythm circle list by removing already hit or missed circles
		rhythm.updateRhythmCircleList(selector, points);
		
		//Checks to see if both the H and J key are held down
		if (input.isKeyDown(Input.KEY_H) && input.isKeyDown(Input.KEY_J) || pads.input == 2)
		{
			//Triggers the radar circles corresponding circle fill
			rc.keyPressed(2);
			//Iterates through the rhythm circle list
			for (int i = 0; i < rhythm.circleList.size(); i++)
			{
				//Checks to see if the key the player just pressed corresponds to a rhythm circle
				//If a circle was detected then don't detect another circle
				if (rhythm.circleList.get(i).keyPressed(2))
					break;
				//You only need to check 5 circles into the list
				//Only 5 circles maximum should be playable at a time
				//If you have reached the 5th circle break out of the loop so that you don't waste time
				if (i == 4)
					break;
			}
		}
		
		else if (input.isKeyDown(Input.KEY_J) && input.isKeyDown(Input.KEY_K) || pads.input == 4)	
		{
			//Triggers the radar circles corresponding circle fill
			rc.keyPressed(4);
			//Iterates through the rhythm circle list
			for (int i = 0; i < rhythm.circleList.size(); i++)
			{
				//Checks to see if the key the player just pressed corresponds to a rhythm circle
				//If a circle was detected then don't detect another circle
				if (rhythm.circleList.get(i).keyPressed(4))
					break;
				//You only need to check 5 circles into the list
				//Only 5 circles maximum should be playable at a time
				//If you have reached the 5th circle break out of the loop so that you don't waste time
				if (i == 4)
					break;
			}
		}
		
		else if (input.isKeyDown(Input.KEY_H) || pads.input == 1)
		{
			//Triggers the radar circles corresponding circle fill
			rc.keyPressed(1);
			//Iterates through the rhythm circle list
			for (int i = 0; i < rhythm.circleList.size(); i++)
			{
				//Checks to see if the key the player just pressed corresponds to a rhythm circle
				//If a circle was detected then don't detect another circle
				if (rhythm.circleList.get(i).keyPressed(1))
					break;
				//You only need to check 5 circles into the list
				//Only 5 circles maximum should be playable at a time
				//If you have reached the 5th circle break out of the loop so that you don't waste time
				if (i == 4)
					break;
			}
		}
		
		else if (input.isKeyDown(Input.KEY_J) || pads.input == 3)
		{
			//Triggers the radar circles corresponding circle fill
			rc.keyPressed(3);
			//Iterates through the rhythm circle list
			for (int i = 0; i < rhythm.circleList.size(); i++)
			{
				//Checks to see if the key the player just pressed corresponds to a rhythm circle
				//If a circle was detected then don't detect another circle
				if (rhythm.circleList.get(i).keyPressed(3))
					break;
				//You only need to check 5 circles into the list
				//Only 5 circles maximum should be playable at a time
				//If you have reached the 5th circle break out of the loop so that you don't waste time
				if (i == 4)
					break;
			}
		}
		
		else if (input.isKeyDown(Input.KEY_K) || pads.input == 5)
		{
			//Triggers the radar circles corresponding circle fill
			rc.keyPressed(5);
			//Iterates through the rhythm circle list
			for (int i = 0; i < rhythm.circleList.size(); i++)
			{
				//Checks to see if the key the player just pressed corresponds to a rhythm circle
				//If a circle was detected then don't detect another circle
				if (rhythm.circleList.get(i).keyPressed(5))
					break;
				//You only need to check 5 circles into the list
				//Only 5 circles maximum should be playable at a time
				//If you have reached the 5th circle break out of the loop so that you don't waste time
				if (i == 4)
					break;
			}
		}
	}
	
	//Draws to the screen
	public void render(GameContainer gc, Graphics g)
	{
		//Sets the graphics color to our color schemes gray
		g.setColor(new Color(84f/255f, 168f/255f * points.colorVibrance, 84f/255f));//new Color(255f/255f, 84f/255f, 84f/255f, points.opacity));
		//Fills the background with the gray color
		g.fill(new Rectangle(0, 0, gc.getScreenWidth(), gc.getScreenHeight()));
		
		//Draws the radar circle (rings that the circles travel on)
		rc.draw(g);
		
		//Sets the graphics color to cyan
		g.setColor(Color.cyan);
		//Draws the selectors current angle to the top left corner of the string (debugging purposes)
		g.drawString(Float.toString(selector.getAngle()), 0, 0);
		//Draws the selectors current rotations under the angle (debuggin purposes)
		g.drawString(Integer.toString(selector.getRotations()), 0, 20);
		
		//Iterates through the rhythm circle list and draws the rhythm circles to the screen if they are visible
		for (int i = 0; i < rhythm.circleList.size(); i++)
			rhythm.circleList.get(i).draw(g, selector);
	}
}
