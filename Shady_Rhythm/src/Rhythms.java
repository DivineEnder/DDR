import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Rhythms
{
	Music currentSong;
	ArrayList<RhythmCircle> circleList;
	
	Rhythms(String filename, GameContainer gc)
	{
		circleList = new ArrayList<RhythmCircle>();
		try{currentSong = new Music("data/Music/" + filename + ".wav");} catch (SlickException e) {}
		float[][] data = null;
    	ReadSong read = new ReadSong("data/Music/" + filename + ".txt");
    	try {data = read.OpenFile();} catch (IOException e) {}
    	for (int i = 0; i < data.length; i++)
    	{
    		circleList.add(new RhythmCircle(data[i][0], (int) data[i][1], (int) data[i][2], gc));
    		circleList.get(i).printCircle();
    	}
	}
	
	public void updateRhythmCircleList(Selector selector)
	{
		for (int i = 0; i < circleList.size(); i++)
		{
			if (!circleList.get(i).checkVisible(selector))
			{
				if (circleList.get(i).checkTermination())
					circleList.remove(i);
				else
					break;
			}
		}
	}
}
