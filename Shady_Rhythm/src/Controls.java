import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Input;

public class Controls
{
	private HashMap<Integer, String> keyMap;
	private HashMap<Integer, String> sKeyMap;
	private ArrayList<String> controlKeys;
	private String[] keyString;
	private int[] currentControls;
	
	public Controls()
	{
		keyMap = new HashMap<Integer, String>();
		sKeyMap = new HashMap<Integer, String>();
		controlKeys = new ArrayList<String>();
		keyString = new String[5];
		currentControls = new int[5];
		
		sKeyMap.put(Input.KEY_Q, "Q");
		sKeyMap.put(Input.KEY_W, "W");
		sKeyMap.put(Input.KEY_E, "E");
		sKeyMap.put(Input.KEY_R, "R");
		sKeyMap.put(Input.KEY_T, "T");
		sKeyMap.put(Input.KEY_Y, "Y");
		sKeyMap.put(Input.KEY_U, "U");
		sKeyMap.put(Input.KEY_I, "I");
		sKeyMap.put(Input.KEY_O, "O");
		sKeyMap.put(Input.KEY_P, "P");
		sKeyMap.put(Input.KEY_A, "A");
		sKeyMap.put(Input.KEY_S, "S");
		sKeyMap.put(Input.KEY_D, "D");
		sKeyMap.put(Input.KEY_F, "F");
		sKeyMap.put(Input.KEY_G, "G");
		sKeyMap.put(Input.KEY_H, "H");
		sKeyMap.put(Input.KEY_J, "J");
		sKeyMap.put(Input.KEY_K, "K");
		sKeyMap.put(Input.KEY_L, "L");
		sKeyMap.put(Input.KEY_SEMICOLON, "SEMICOLON");
		sKeyMap.put(Input.KEY_Z, "Z");
		sKeyMap.put(Input.KEY_X, "X");
		sKeyMap.put(Input.KEY_C, "C");
		sKeyMap.put(Input.KEY_V, "V");
		sKeyMap.put(Input.KEY_B, "B");
		sKeyMap.put(Input.KEY_N, "N");
		sKeyMap.put(Input.KEY_M, "M");
		sKeyMap.put(Input.KEY_SPACE, "SPACE");
		
		keyMap.put(Input.KEY_A, "1");
		keyMap.put(Input.KEY_F, "2");
		keyMap.put(Input.KEY_R, "3");
		keyMap.put(Input.KEY_D, "4");
		keyMap.put(Input.KEY_G, "5");
		
		keyString[0] = sKeyMap.get(Input.KEY_A);
		keyString[1] = sKeyMap.get(Input.KEY_F);
		keyString[2] = sKeyMap.get(Input.KEY_R);
		keyString[3] = sKeyMap.get(Input.KEY_D);
		keyString[4] = sKeyMap.get(Input.KEY_G);
		
		currentControls[0] = Input.KEY_A;
		currentControls[1] = Input.KEY_F;
		currentControls[2] = Input.KEY_R;
		currentControls[3] = Input.KEY_D;
		currentControls[4] = Input.KEY_G;
	}
	
	public void setControls(int key, int oldKey)
	{
		if (keyMap.get(key) == null)
		{
			keyMap.remove(currentControls[oldKey]);
			keyMap.put(key, "" + (oldKey + 1));
			keyString[oldKey] = sKeyMap.get(key);
			currentControls[oldKey] = key;
		}
	}
	
	public HashMap getKeyMapping()
	{
		return keyMap;
	}
	
	public String[] getKeyString()
	{	
		return keyString;
	}
}
