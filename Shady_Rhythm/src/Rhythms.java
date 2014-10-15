public class Rhythms
{
	private int selected;
	private String[] songs;
	private String[] artists;
	private int[] durations; // in seconds
	private String[] difficulty;
	
	
	Rhythms()
	{
		selected = 0;
		songs = new String[]{"Am I Wrong", "Pump Up The Jam Mixdown", "Rather Be"};
		artists = new String[]{"Nico & Vinz", "Mowe", "Clean Bandit"};
		durations = new int[]{(4*60 + 7), (4*60 + 45), (3*60 + 47)};
		difficulty = new String[]{"Easy", "Hard", "Medium"};
	}
}
