import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Rhythms
{
	//Creates a variable for the title of the song
	String title;
	//Creates a variable for the artist of the song (currently not displayed in game anywhere)
	String artist;
	//Creates a variable to hold the wav file of the song
	Music currentSong;
	//Creates an array to hold the circles for the song
	ArrayList<RhythmCircle> circleList;
	
	//Creates a variable to hold the fist rhythmCircles angle (used to calculate music start)
	float firstCircleAngle;
	
	//Sets file path to the current song
	public void setRhythm(String filename)
	{
		//Initializes the list of circles to be a new arraylist
		circleList = new ArrayList<RhythmCircle>();

		//Initializes the title of the song by parsing it from the file name
    	title = filename.substring(0, filename.indexOf('-') - 1);
    	//Initializes the artist of the song by parsing it from the file name
    	artist = filename.substring(filename.indexOf('-') + 2);
    	//Song filenames in the form of [title - artist.wav]
    	
    	//Checks to make sure this rhythm isn't a placeholder for the story that has no rhythm
    	if (!title.equals("NONE"))
    	{
	    	//Initializes the currentSong to a wav file from the file
	    	try{currentSong = new Music("data/Music/" + filename + ".wav");} catch (SlickException e) {}
    	}
	}
	
	//Sets up the rhythm for gameplay
	public void readRhythm(GameContainer gc)
	{
		//Creates a temporary variable to reference the file
		String filename = title + " - " + artist;
		
		//Creates a new array to hold the data that is going to be read from the text file
		float[][] data = null;
		//Creates a new class that reads from the textfile with the rhythm
    	ReadSong read = new ReadSong("data/Music/" + filename + ".txt");
    	//Converts the data from the file to a list of integers
    	try {data = read.toIntList(read.OpenFile());} catch (IOException e) {System.out.println(e);}
    	//Reinitializes the circle list to make sure it is empty
    	circleList = new ArrayList<RhythmCircle>();
    	//Sets the first circles angle from the file
    	firstCircleAngle = data[0][0];
    	//Creates new rhythm circles from the file data and then add them to the circle list
    	for (int i = 0; i < data.length; i++)
    		circleList.add(new RhythmCircle(data[i][0], data[i][1], (int) data[i][2], (int) data[i][3], gc));
	}
	
	//Updates the circle list by removing circles that have passed
	public void updateRhythmCircleList(Selector selector, Score score)
	{
		//Iterates through the circle list
		for (int i = 0; i < circleList.size(); i++)
		{
			//Check to see whether the circle is visible on the screen
			if (!circleList.get(i).checkVisible(selector))
			{
				//Checks to see if the circle finished without the player hitting it
				if (circleList.get(i).checkTermination() == 1)
				{
					//Modifies the score to reflect the player missing the circle
					score.updatePoints(false, circleList.get(i).getLayer());
					//Removes the circle from the list so that we no longer have to iterate over it
					circleList.remove(i);
				}
				//Checks to see if the circle finished with the player hitting it
				else if (circleList.get(i).checkTermination() == 2)
				{
					//Modifies the score to reflect the player hitting the circle
					score.updatePoints(true, circleList.get(i).getLayer());
					//Removes the circle from the list so that we no longer have to iterate over it
					circleList.remove(i);
				}
				//Otherwise break outside of the loop
				else
					break;
				//The break is there for efficiency sake
				//Otherwise it would iterate through the entire list
				//Circles will only be hit one at a time so one circle termination will happen at a time
				//Once we find that termination, there should be no other terminations
				//So running through the list after the termination is pointless and wastes time
			}
		}
	}
}
