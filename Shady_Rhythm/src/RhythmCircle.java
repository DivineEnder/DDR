import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;

public class RhythmCircle
{
	//Creates static variables to hold the windows width and height
	private static int windowWidth;
	private static int windowHeight;
	
	//Creates a variable to hold the x position of the circle on the screen
	private float x;
	//Creates a variable to hold the y position of the circle on the screen
	private float y;
	//Creates a variable to hold the number of rotations that the circle starts at
	private float rotations;
	//Creates a variable to hold the angle at which the circle is drawn
	private float angle;
	
	//Creates a variable to hold the angle at which the circle the circle will start drawing on the screen
	private float startDrawingAngle;
	//Creates a variable to hold the circles radius as an angle measure
	private float circleRadiusAsAngle;
	
	//Creates a variable to hold the circles current drawing radius (used for growing animation)
	private float radius;
	//Creates a immutable variable to hold the target or ending circle radius
	private static float circle_radius;
	
	//Creates a variable to determine whether the circle is visible on the screen or not
	private boolean visible;
	
	//Creates a variable to hold the layer on which the circle travels
	private int layer;
	//Creates a variable to determine whether the circle has finished its rotation and how it finished
	private int termination;
	
	//Creates a sound to be played when you miss the circle
	Sound miss;
	
	//Creates a variable to hold the color that the circle is drawn in (determined by the layer)
	private Color color;
	
	//Constructor
	RhythmCircle(float a, int rot, int lay, GameContainer gc)
	{
		//Gets the width and height of the game and sets it equal to the windows width and height variables
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		//Sets the selector drawing angle equal to the angle that was passed to the circle
		startDrawingAngle = a;
		//Sets the circle's rotations equal to the rotations that was passed to the circle
		rotations = rot;
		//Sets the circle's layer equal to the the layer that was passed to the circle
		layer = lay;
		
		//Sets the end drawing radius to a static value
		circle_radius = 25;
		
		//Does the law of cosines to determine what the circle's radius as an angle is
		double csquared = (circle_radius*2) * (circle_radius*2);
		double ab = 2 * ((((windowHeight/10) - 5) * layer) * (((windowHeight/10) - 5) * layer));
		circleRadiusAsAngle = (float) (Math.acos((csquared - ab) / (-1 * ab)) * (180/Math.PI)) + (5/layer);
		
		//Sets the graphical angle that the circle will start drawing at on the screen
		angle = 45;
		
		//Determines the x and and y coordinates for that angle on the screen
		x = windowWidth/2 + (float) (((windowHeight/10) * layer) * Math.cos(angle * (Math.PI/180)));
		y = windowHeight/2 + (float) (((windowHeight/10) * layer) * Math.sin(angle * (Math.PI/180)));
		
		//Checks to see what layer the circle is on and sets it to a specific color for each layer
		if (layer == 1)
			color = new Color(173, 16, 16); //Red
		else if (layer == 2)
			color = new Color(196, 196, 16); //Yellow
		else if (layer == 3)
			color = new Color(22, 161, 22); //Green
		else if (layer == 4)
			color = new Color(63, 186, 186); //Cyan
		else if (layer == 5)
			color = new Color(10, 29, 145); //Blue
		
		//Sets the starting drawn radius of the circle as zero (for growing animation)
		radius = 0;
		//Initializes the circle to start out not visible
		visible = false;
		
		//Sets termination to be negative 1 so that we can tell that the circle has not yet been drawn
		termination = -1;
		
		//Loads the miss sound effect from file
		try {
			miss = new Sound("data/Sound Effects/Miss Sound.wav");
		} catch (SlickException e) {e.printStackTrace();}
	}
	
	//Gets the circles radius as an angle
	public float getRadiusAsAngle()
	{
		return circleRadiusAsAngle;
	}
	
	//Print out the values of the circle that it got from the file (Debuggin purposes)
	public void printCircle()
	{
		System.out.print("(" + startDrawingAngle + "," + rotations + "," + layer + ")");
	}
	
	//Gets circles layer (which ring it is on)
	public int getLayer()
	{
		return layer;
	}
	
	//Checks to see whether the circle has finished, been hit, or missed
	public int checkTermination()
	{
		return termination;
	}
	
	//Changes whether the circle is visible or not
	public void toggleVisible()
	{
		//Switches visible from true to false or from false to true
		visible = !visible;
		//Increases termination by 1 (used to determine whether the circle has been drawn yet)
		termination++;
	}
	
	//Checks for to see whether the circle should become visible
	public boolean checkVisible(Selector selector)
	{
		//Checks to see if the selector is at the correct rotations and angle then turns the circle on if it is
		if (selector.getAngle() == startDrawingAngle && selector.getRotations() == rotations)
			toggleVisible();
		
		//Returns whether or not the circle is visible
		return visible;
	}
	
	//Checks to see if the key that was pressed by the player corresponds to the circle
	public void keyPressed(int layerPressed)
	{
		//Checks to see if the key pressed was on the corresponding layer
		if (layer == layerPressed && termination != 1)
		{
			//Checks to see whether the key was pressed within the threashold for hitting the circle
			if (angle > (360 - circleRadiusAsAngle/3) && angle < (360 + circleRadiusAsAngle/3))
			{
				//Turns off the circle if the circle was hit
				visible = false;
				//Sets the termination to 2 to tell others that the circle was hit
				termination = 2;
			}
		}
	}
	
	//Creates the growing smooth appear animation when the circle first draws
	private void smoothAppear()
	{
		//Checks to see if the drawing radius is less than the desired radius and increments the drawing radius until it is equal to the desired radius
		if (radius <= circle_radius)
			radius += .1;
	}
	
	//Moves the circle around the radar circle layer that it is on
	public void move()
	{
		//Increases the angle that the circle is drawn at
		angle += .5;
		
		//Recalculates the x and y coordinates based on the new angle
		x = windowWidth/2 + (float) ((((windowHeight/10) - 5) * layer) * Math.cos(angle * (Math.PI/180)));
		y = windowHeight/2 + (float) ((((windowHeight/10) - 5) * layer) * Math.sin(angle * (Math.PI/180)));
	}
	
	//Checks to see whether the circle has gone 360 degrees without being hit
	private void checkFullCircle()
	{
		//Checks to see whether the circle has gone 360 degrees
		//The plus and minus .2 are because the circle can sometimes be missed if you serach for the exact angle
		if (angle + .5 >= circleRadiusAsAngle + 360 && angle - .5 <= circleRadiusAsAngle + 360)
		{
			//Turns the circle off
			toggleVisible();
			//Plays the miss sound
			miss.play();
		}
	}
	
	//Updates certain aspects of the circle as you draw it
	public void updateCircle()
	{
		//Creates the smooth appear effect of the circle as it draws
		smoothAppear();
		//Moves the circle around the radar circle layer
		move();
		//Checks to see whether the circle has completed 360 degrees without being hit by the player
		checkFullCircle();
	}
	
	//Draws the circle to the screen
	public void draw(Graphics g, Selector selector)
	{
		//Checks to see whether the circle is visible
		if (visible)
		{
			//Checks to see whether the selector is running then updates the circle if it is (in case the game is paused)
			if (selector.checkRunning())
				updateCircle();
			//Sets the graphics color to the be circles layer color
			g.setColor(color);
			//Draws the circle
			g.fill(new Circle(x, y, radius));
			//Sets the graphics color to black
			g.setColor(Color.black);
			//Draws the black outline of the circle to make it look nicer
			g.draw(new Circle(x, y, radius));
		}
	}
}