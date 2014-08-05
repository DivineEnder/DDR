import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TutorialState extends BasicGameState
{
	//ArrayList<Integer> xData = new ArrayList<Integer>();
	//ArrayList<Integer> yData = new ArrayList<Integer>();
	//int[] data = readAudio(new File("data/Music/testy.wav"));
	//BufferedWriter points;
	RadarCircles radar;
	Image proof;
	Image yes;
	int selected = 0;
	int lineWidth = 3;
	boolean transition = false;
	int angle = 180;
	int angle2 = 180;
	float x;
	float y;
	float x2;
	float y2;
	
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		radar = new RadarCircles(gc.getScreenWidth(), gc.getScreenHeight());
		proof = new Image("data/Proof.png");
		yes = new Image("data/yes.png");
		yes.rotate(-90);
		proof.rotate(-90);
		x = gc.getScreenWidth()/2 - ((gc.getScreenHeight()/10 + 5) * 5) - proof.getWidth()/2;
		y = gc.getScreenHeight()/2;
		x2 = gc.getScreenWidth()/2 - ((gc.getScreenHeight()/10 + 5) * 4) - yes.getWidth()/2;
		y2 = gc.getScreenHeight()/2;
		
		/*try {points = new BufferedWriter(new FileWriter("data/data.txt"));} catch (IOException e1) {e1.printStackTrace();}
		
		int bytes, cursor, unsigned;
	      try {
	        FileInputStream s = new FileInputStream("C:/Users/David/Desktop/Am I Wrong.wav");
	        BufferedInputStream b = new BufferedInputStream(s);
	        byte[] data = new byte[128];
	        b.skip(44);
	        cursor = 0;
	        while ((bytes = b.read(data)) > 0) {
	          // do something
	          for(int i=0; i<bytes; i++) {
	                  unsigned = data[i] & 0xFF; // Java..
	                  points.write(cursor + " " + unsigned);
	                  points.newLine();
	                  //System.out.println(cursor + " " + unsigned);
	                  if (cursor > 10000)
	                	  break;
	                  cursor++;
	          }
	        }
	        //System.out.println(cursor);
	        
	        b.read(data);
	        b.close();
	        //for (int i = 0; i < data.length; i++)
	        	//System.out.println(data[i]);
	      } catch(Exception e) {
	        e.printStackTrace();
	      }
	      try {
			points.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*int xSpace = 2;
		//data = readAudio(new File("data/testy.wav"));
		BufferedImage off_Image = new BufferedImage(xSpace * data.length, 512 * 5, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = off_Image.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle(xSpace * data.length, 512 * 5));
		g2d.setColor(Color.ORANGE);
		//g2d.setStroke(new BasicStroke(5));
		g2d.drawLine(0, off_Image.getHeight()/2, xSpace, off_Image.getHeight()/2 - data[0] * 5);
		for (int i = 1; i < data.length - 1; i++)
		{
			g2d.drawLine(xSpace * i, off_Image.getHeight()/2 - (data[i] * 5), xSpace * (i + 1), off_Image.getHeight()/2 - (data[i + 1] * 5));
			//g.drawLine(, arg1, arg2, arg3)
		}
		try {
		    // retrieve image
		    File outputfile = new File("saved.png");
		    ImageIO.write(off_Image, "png", outputfile);
		} catch (IOException e) {
		}*/
	}
	
	/*public int[] readAudio(File file)
	{
		int[] audioValues = null;
	    int totalFramesRead = 0;
	    
	    try
	    {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
	        
	        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
	        
	        if(bytesPerFrame == AudioSystem.NOT_SPECIFIED)
	        {
	            //some audio formats may have unspecified frame size
	            //in that case we may read any amount of bytes
	            bytesPerFrame = 1;
	            System.out.println("Unspecified amount of frames in file");
	        }

	        //Set an arbitrary buffer size of 1024 frames.
	        int numBytes = 1024 * bytesPerFrame;
	        byte[] audioBytes = new byte[numBytes];
	        
	        try
	        {
	            int numBytesRead = 0;
	            int numFramesRead = 0;
	            
	            //Try to read #numBytes bytes from the file
	            while((numBytesRead = audioInputStream.read(audioBytes)) != -1)
	            {
	                //Calculate the number of frames actually read.
	                numFramesRead = numBytesRead / bytesPerFrame;
	                totalFramesRead += numFramesRead;
	            }
	            
	            //convert bytes to integers, because they can be negative
	            audioValues = new int[audioBytes.length];
	            for(int i = 0; i < audioBytes.length; i++)
	            {
	            	audioValues[i] = audioBytes[i];
	                //if(audioBytes[i] < 0)
	                //{
	                //    audioValues[i] = audioBytes[i] + 256;
	                //}
	                //System.out.print(audioBytes[i] + " ");
	                //System.out.print(audioValues[i] + " ");
	                //System.out.println();
	            }


	        } catch (Exception ex){System.out.println("Error! Problem with audio data");}
	    } catch (Exception e){System.out.println("Error! Audio not compatible");}
	    
	    return audioValues; 
	}*/
	
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_SPACE))
		{
			if (selected != 1)
			{
				selected = 1;
				radar.select(1);
			}
			else
			{
				transition = true;
			}
		}
		
		if (input.isKeyPressed(Input.KEY_J))
		{
			if (selected != 2)
			{
				selected = 2;
				radar.select(2);
			}
			else
			{
				transition = true;
			}
		}
		
		if (input.isKeyPressed(Input.KEY_K))
		{
			if (selected != 3)
			{
				selected = 3;
				radar.select(3);
			}
			else
			{
				transition = true;
			}
		}
		
		if (input.isKeyPressed(Input.KEY_L))
		{
			if (selected != 4)
			{
				selected = 4;
				radar.select(4);
			}
			else
			{
				transition = true;
			}
		}
		
		if (input.isKeyPressed(Input.KEY_SEMICOLON))
		{
			if (selected != 5)
			{
				selected = 5;
				radar.select(5);
			}
			else
			{
				transition = true;
			}
		}
		
		if (transition)
		{
			if (selected == 5)
			{
				x = gc.getScreenWidth()/2 + (float) (((gc.getScreenHeight()/10 + 5) * selected) * Math.cos(angle * (Math.PI/180))) - proof.getWidth()/2;
				y = gc.getScreenHeight()/2 + (float) (((gc.getScreenHeight()/10 + 5) * selected) * Math.sin(angle * (Math.PI/180)));
				proof.rotate(-1);
				angle++;
				if (angle == 360)
					transition = false;
			}
			if (selected == 4)
			{
				x2 = gc.getScreenWidth()/2 + (float) (((gc.getScreenHeight()/10 + 5) * selected) * Math.cos(angle2 * (Math.PI/180))) - yes.getWidth()/2;
				y2 = gc.getScreenHeight()/2 + (float) (((gc.getScreenHeight()/10 + 5) * selected) * Math.sin(angle2 * (Math.PI/180)));
				yes.rotate(-1);
				angle2++;
				if (angle2 == 360)
					transition = false;
			}
			
			
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(0, new FadeOutTransition(Color.black, 750), new FadeInTransition(Color.black, 750));
	}
	
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		radar.draw(g);
		
		g.drawImage(proof, x, y);
		g.drawImage(yes, x2, y2);
		
		//g.setColor(Color.cyan);
		
		//for (int i = 1; i < xData.size() - 1; i++)
		//{
		//	g.drawLine(xData.get(i - 1), gc.getScreenHeight() - (yData.get(i - 1)), xData.get(i), gc.getScreenHeight() - (yData.get(i)));
		//}
		/*int origin = gc.getScreenHeight()/2;
		int xSpace = 1;
		
		g.setAntiAlias(true);
			
		g.drawLine(0, origin, xSpace, origin - data[0] * 5);
		for (int i = 1; i < data.length - 1; i++)
		{
			g.drawLine(xSpace * i, origin - (data[i] * 5), xSpace * (i + 1), origin - (data[i + 1] * 5));
			//g.drawLine(, arg1, arg2, arg3)
		}*/
		
		//g.drawString("Tutorial State", 0, 0);
	}

	public int getID()
	{
		return 3;
	}
}