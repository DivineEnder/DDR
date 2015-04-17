import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class RhythmSwoop
{
	float windowWidth;
	float windowHeight;
	
	Rhythms rhythm;
	
	float radius;
	float angle;
	
	RhythmSwoop(Rhythms r, int width, int height)
	{
		rhythm = r;
		
		windowWidth = width;
		windowHeight = height;
		
		radius = windowHeight/2;
		
		angle = (float) (Math.acos((radius - windowWidth/8)/(radius)) * 180/Math.PI) + 180;
	}
	
	public void draw(Graphics g, Score points)
	{
		g.setLineWidth(5);
		
		g.setColor(new Color(6f/255f - (6f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration)), 35f/255f + (77f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration)), 106f/255f + (223f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration))));
		g.draw(new Circle((windowWidth * 7/8) + radius, radius, radius));
		g.fillArc(windowWidth * 7/8, 0, radius * 2, radius * 2, angle - ((2 * (angle - 180)) * (rhythm.currentSong.getPosition()/rhythm.songDuration)), angle);
		
		g.setColor(new Color(84f/255f, 168f/255f * (1 - points.colorVibrance), 84f/255f));
		g.fill(new Circle((windowWidth - (radius * 4/3)/8) + (radius * 2/3), radius * 2/3, radius * 2/3));
		
		g.setColor(new Color(6f/255f - (6f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration)), 35f/255f + (77f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration)), 106f/255f + (223f/255f * (rhythm.currentSong.getPosition()/rhythm.songDuration))));
		g.draw(new Circle((windowWidth - (radius * 4/3)/8) + (radius * 2/3), radius * 2/3, radius * 2/3));
	}
}
