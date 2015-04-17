import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Score
{
	//Creates a variable to hold the overall points that you get
	float points;
	//Creates a variable to hold the overall maximum points that you can get
	float maxPoints;
	//Creates a variable to hold the overall accuracy of the player
	float percentage;
	
	//Create array to hold points by color of the circle
	float[] pointsByColor;
	//Creates array to hold the maximum points that can be attained categorized by circle color
	float[] maxPointsByColor;
	//Creates array to hold percent accuracy in each color category
	float[] percentageByColor;
	//Creates array to hold the high scores of the current song
	float[] highScores;
	
	//Creates a variable to determine how many times points were added to consecutively
	int numAdds;
	//Determines what the longest streak of circles was
	int longestStreak;
	
	//Creates a variable to determine the background color of the screen
	float colorVibrance;
	
	float angle;
	float actualAngle;
	float displayAngle;
	
	//Initalizes a new score for the current rhythm
	public void initalize()
	{
		//Initializes overall points
		points = 0;
		//Initializes overall max points
		maxPoints = 0;
		//Initializes percent to 100% at the start of the game
		percentage = 1;
				
		//Initializes points by color to start at 0s
		pointsByColor = new float[]{0, 0, 0, 0, 0};
		//Initializes maximum points by color to start at all 0s
		maxPointsByColor = new float[]{0, 0, 0, 0, 0};
		//Initializes percentage by color to start at all 100%
		percentageByColor = new float[]{1, 1, 1, 1, 1};
		//Initializes the high scores to a new float array that holds the overall percentage and individual color percentages
		highScores = new float[6];
				
		//Initializes the number of addition to the points to zero
		numAdds = 0;
		
		//Starts the color out in the center of the scale
		colorVibrance = .5f;
		
		displayAngle = 0;
	}
	
	//Updates the players points when they hit a circle
	public void updatePoints(boolean add, int layer)
	{
		//Adds to the max overall points
		maxPoints++;
		//Adds to the maximum points of the color of the circle just terminated
		maxPointsByColor[layer - 1]++;
		
		//Checks to see if the player hit the circle and so should get points
		if (add)
		{
			//Adds to the players overall points
			points++;
			//Adds to the players color categorized points
			pointsByColor[layer - 1]++;
			//Increases the number of times that we have consecutively added points
			numAdds++;
			//Checks for longest streak then sets longest streak to consecutive additions if it is greater
			if (numAdds > longestStreak)
				longestStreak = numAdds;
			
			//Checks to see if the color vibrance is lower than 1 and if it is increase the color slightly
			if (colorVibrance < 1)
				colorVibrance += .05;
		}
		else
		{
			//Break the streak of additions if the player did not hit the circle
			numAdds = 0;
			
			//Checks to see if teh color vibrance is greater than 0 then decreases it if it is
			if (colorVibrance > 0)
				colorVibrance -= .05;
		}
		//Calculate the overall point percentage
		percentage = points/maxPoints;
		//Calculate the point percentage of the color of the circle just terminated
		percentageByColor[layer - 1] = pointsByColor[layer - 1]/maxPointsByColor[layer - 1];
	}
	
	//Checks to see whether the player got a new high score and writes the new score to a file if they did
	public boolean checkHighScore(String song) throws IOException
	{
		//Creates a new score file using the song the player just played
		File file = new File("data/Score/" + song + ".txt");
		//Checks to see whether there is already a high score file for this song
		if (file.exists())
		{
			//Creates a new reader in order to read the previous high score from the file
			BufferedReader scoreReader = new BufferedReader(new FileReader("data/Score/" + song + ".txt"));
			//Reads the previous high scores from the file and then splits them by the commas into a string list
			String[] highScoreStrings = scoreReader.readLine().split(",");
			
			//Sets the high score in the score state to be the pervious high scores
			highScores[0] = Float.parseFloat(highScoreStrings[0]);
			for (int i = 1; i < highScores.length; i++)
				highScores[i] = Float.parseFloat(highScoreStrings[i]);
			
			//Checks to see whether the overall percentage of the player received is greater than the previous high score
			if (percentage > Float.parseFloat(highScoreStrings[0]))
			{
				//Creates a new writer to write to a new high score file
				BufferedWriter score = new BufferedWriter(new FileWriter("data/Score/" + song + ".txt"));
				
				//Creates a string that will be written to the file and adds all the scores to that string
				String writeToFile = Float.toString(percentage) + ",";
				for (int i = 0; i < percentageByColor.length; i++)
					writeToFile += Float.toString(percentageByColor[i]) + "0,";
				//Gets rid of the extra comma at the end of string after we have created it
				writeToFile = writeToFile.substring(0, writeToFile.length()-1);
				
				//Writes the high scores to the high score file
				score.write(writeToFile);
				//Closes and saves the high score file
				score.close();
				
				//Returns true back to the score state to let the state know that they player got a new high score
				return true;
			}
			//Otherwise return false back to the score state to let the state know that the player did not get a new high score
			else
				return false;
		}
		//Otherwise your score is the new high score
		else
		{
			//Creates a new buffered writer to write a new high score file
			BufferedWriter score = new BufferedWriter(new FileWriter("data/Score/" + song + ".txt"));
			
			//Sets the high score in the score state to be the players scores
			highScores[0] = percentage;
			for (int i = 1; i < highScores.length; i++)
				highScores[i] = percentageByColor[i-1];
			
			//Creates a string that will be written to the file and adds all the scores to that string
			String writeToFile = Float.toString(percentage) + ",";
			for (int i = 0; i < percentageByColor.length; i++)
				writeToFile += Float.toString(percentageByColor[i]) + ",";
			//Gets rid of the extra comma at the end of string after we have created it
			writeToFile = writeToFile.substring(0, writeToFile.length()-1);
			
			//Writes the string (high scores) to the new high score file
			score.write(writeToFile);
			//Close the file and saves it
			score.close();
			
			//Returns true back to the score state to let the state know that they player got a new high score
			return true;
		}
	}
	
	public void drawScoreSwoop(Graphics g, float windowWidth, float windowHeight)
	{
		g.setLineWidth(5);
		
		//Calculates the angle to fill the arc from
		angle = (float) (Math.acos((windowHeight/2 - windowWidth/8)/(windowHeight/2)) * 180/Math.PI);
		
		g.setColor(new Color(229f/255f - (223f/255f * percentage), 8f/255f + (98f/255f * percentage), 0f/255f + (10f/255f * percentage)));
		g.draw(new Circle(windowWidth/8 - windowHeight/2, windowHeight/2, windowHeight/2));
		
		actualAngle = ((2 * angle) * percentage);
		
		if (displayAngle < actualAngle + .5f && displayAngle > actualAngle - .5f)
			displayAngle = actualAngle;
		else if (displayAngle > actualAngle)
		{
			float velocity = 1 - (.5f * (actualAngle/displayAngle));
			displayAngle -= velocity;
		}
		else if (displayAngle < actualAngle)
		{
			float velocity = 1 - (.5f * (displayAngle/actualAngle));
			displayAngle += velocity;
		}
		g.fillArc(windowWidth/8 - windowHeight, 0, windowHeight, windowHeight, angle - displayAngle, angle);
		
		g.setColor(new Color(84f/255f, 168f/255f * (1 - colorVibrance), 84f/255f));
		g.fill(new Circle((0 + (windowHeight * 2/3)/8) - (windowHeight * 1/3), windowHeight * 2/3, windowHeight * 1/3));
		
		g.setColor(new Color(229f/255f - (223f/255f * percentage), 8f/255f + (98f/255f * percentage), 0f/255f + (10f/255f * percentage)));
		g.draw(new Circle((0 + (windowHeight * 2/3)/8) - (windowHeight * 1/3), windowHeight * 2/3, windowHeight * 1/3));
	}
}
