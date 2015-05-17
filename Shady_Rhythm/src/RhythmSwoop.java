import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;

public class RhythmSwoop
{
	//Window width and height variables
	float windowWidth;
	float windowHeight;
	
	//Create new instance of Rhythm class
	Rhythms rhythm;
	
	//Creates variables for drawing swoop
	float radius;
	float angle;
	
	//Constructor
	RhythmSwoop(Rhythms r, int width, int height)
	{
		//Initializes Rhythm class instance to universal one passed between states
		rhythm = r;
		
		//Sets window width and height
		windowWidth = width;
		windowHeight = height;
		
		//Sets radius to half of previously set height
		radius = windowHeight/2;
		
		//Sets angle for filling swoop (calculated using trig)
		angle = (float) (Math.acos((radius - windowWidth/8)/(radius)) * 180/Math.PI) + 180 - 10;
	}
	
	//Render function
	public void draw(Graphics g, Score points)
	{
		//Set the drawing line width
		g.setLineWidth(5);
		
		//Checks for where in the song the player is
		float songPosition = 0;
		if (rhythm.currentSong.playing())
			songPosition = rhythm.currentSong.getPosition();
		
		//Fills the swoop with color that varies as it fills more
		g.setColor(new Color(6f/255f - (6f/255f * (songPosition/rhythm.songDuration)), 35f/255f + (77f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration)), 106f/255f + (223f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration))));
		g.fillArc(windowWidth * 7/8, 0, radius * 2, radius * 2, angle - ((2 * (angle - 180)) * (songPosition/rhythm.songDuration)), angle);
		
		//Fills the top half of the container
		g.setColor(new Color(0, 35, 90));
		g.fillArc(windowWidth * 7/8, 0, radius * 2, radius * 2, angle, 360 - angle);
		
		//Fills the bottom half of the container
		g.setColor(new Color(84f/255f, 168f/255f * (1 - points.colorVibrance), 84f/255f));
		g.fill(new Circle((0 + (windowHeight * 2/3)/8) - (windowHeight * 1/3), windowHeight * 2/3, windowHeight * 1/3));
		
		//Draws the black outline container lines
		float x = (float) ((radius) * Math.cos((angle) * (Math.PI/180)));
		float y = (float) ((radius) * Math.sin((angle) * (Math.PI/180)));
		g.setColor(Color.black);
		g.draw(new Line((windowWidth * 7/8) + radius, radius, (windowWidth * 7/8) + radius + x, radius - y));
		g.draw(new Line((windowWidth * 7/8) + radius, radius, (windowWidth * 7/8) + radius + x, radius + y));
		
		//Makes the swoop by filling in a smaller circle in the middle of the arc
		g.setColor(new Color(84f/255f, 168f/255f * (1 - points.colorVibrance), 84f/255f));
		g.fill(new Circle((windowWidth - (radius * 4/3)/8) + (radius * 2/3), radius * 2/3, radius * 2/3));
		
		//Draws the black swoop outline
		g.setColor(Color.black);
		g.draw(new Circle((windowWidth * 7/8) + radius, radius, radius));
		g.draw(new Circle((windowWidth - (radius * 4/3)/8) + (radius * 2/3), radius * 2/3, radius * 2/3));
		
		//Draws the percentage text at the same rotation as the fill that the swoop is currently at
		g.setColor(Color.black);
		g.rotate((windowWidth * 7/8) + radius, radius, (angle - 180) - ((2 * (angle - 180)) * (songPosition/rhythm.songDuration)));
		g.drawString(Integer.toString((int) ((((2 * (angle - 180)) * (songPosition/rhythm.songDuration)) / (2 * (angle - 180))) * 100)) + "%", windowWidth * 7/8 - g.getFont().getWidth(Integer.toString((int) ((((2 * (angle - 180)) * (rhythm.currentSong.getPosition()/rhythm.songDuration)) / (2 * (angle - 180))) * 100)) + "%") - 10, radius - g.getFont().getHeight(Integer.toString((int) ((((2 * (angle - 180)) * (rhythm.currentSong.getPosition()/rhythm.songDuration)) / (2 * (angle - 180))) * 100)) + "%")/2);
		g.resetTransform();
	}
}
