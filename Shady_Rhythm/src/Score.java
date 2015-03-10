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
	
	//Creates a variable to determine how many times points were added to consecutively
	int numAdds;
	//Determines what the longest streak of circles was
	int longestStreak;
	
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
				
		//Initializes the number of addition to the points to zero
		numAdds = 0;
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
		}
		//Break the streak of additions if the player did not hit the circle
		else
			numAdds = 0;
		//Calculate the overall point percentage
		percentage = points/maxPoints;
		//Calculate the point percentage of the color of the circle just terminated
		percentageByColor[layer - 1] = pointsByColor[layer - 1]/maxPointsByColor[layer - 1];
	}
}
