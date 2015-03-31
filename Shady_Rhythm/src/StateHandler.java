public class StateHandler
{
	private int lastStateID;
	
	private float musicVolume;
	private float soundVolume;
	
	StateHandler()
	{
		lastStateID = 0;
		
		musicVolume = 1;
		soundVolume = 1;
	}
	
	public void setMusicVolume(float v)
	{
		musicVolume = v;
	}
	
	public void setSoundVolume(float v)
	{
		soundVolume = v;
	}
	
	public float getMusicVolume()
	{
		return musicVolume;
	}
	
	public float getSoundVolume()
	{
		return soundVolume;
	}
	
	public void leavingState(int ID)
	{
		lastStateID = ID;
	}
	
	public int getLastStateID()
	{
		return lastStateID;
	}
}
