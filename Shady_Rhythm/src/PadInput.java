import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class PadInput
{
	int input;
	int lastInput;
	
	boolean usePads;
	boolean[] padReleased;
	
	PadInput()
	{
		input = 0;
		lastInput = 0;
		usePads = true;
		padReleased = new boolean[]{false, false, false, false, false};
	}
	
	//Test this
	public boolean isPadReleased(int pad)
	{
		boolean released = false;
		
		//if (padReleased[pad - 1])
		//{
		//	released = true;
		//	padReleased[pad - 1] = false;
		//}
		
		return released;
	}
	
	void connect (String portName) throws Exception
    {
		CommPortIdentifier portIdentifier = null;
		try
		{
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e)
		{
			System.out.println("WARNING: Controller not found, switching to keyboard input mode.");
			usePads = false;
		};
		if (usePads)
		{
			if (portIdentifier.isCurrentlyOwned())
	        {
				System.out.println("Error: Port is currently in use. Please check for external programs that may be tyring to use the same port.");
	            usePads = false;
	        }
	        else
	        {
	            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
	            
	            if (commPort instanceof SerialPort)
	            {
	                SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                
	                (new Thread(new SerialReader(in, this))).start();
	            }
	            else
	            {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }
		}
    }
    
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        PadInput pads;
        
        public SerialReader(InputStream in, PadInput pads)
        {
            this.in = in;
            this.pads = pads;
        }
        
        public void run()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ((len = this.in.read(buffer)) > -1)
                {
                	int recentInput = 0;
                	try
                	{
                		pads.lastInput = pads.input;
                		recentInput = Integer.parseInt(new String(buffer, 0, len).replace("/n", ""));
                		pads.input = recentInput;
                		
                		//if (pads.lastInput != 0 && pads.lastInput != pads.input)
                		//{
                			//pads.padReleased[pads.lastInput - 1] = true;
                		//}
                	} catch(NumberFormatException e) {};
                	
                	
                }
            }catch (IOException e){e.printStackTrace();}
        }
    }

    static void listPorts()
    {
    	java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
    	while (portEnum.hasMoreElements())
    	{
    		CommPortIdentifier portIdentifier = portEnum.nextElement();
    		System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()));
    	}
    }
    
    static String getPortTypeName (int portType)
    {
    	switch (portType)
    	{
    		case CommPortIdentifier.PORT_I2C:
    			return "I2C";
    		case CommPortIdentifier.PORT_PARALLEL:
    			return "Parallel";
    		case CommPortIdentifier.PORT_RAW:
    			return "Raw";
    		case CommPortIdentifier.PORT_RS485:
    			return "RS485";
    		case CommPortIdentifier.PORT_SERIAL:
    			return "Serial";
    		default:
    			return "unknown type";
    	}
    }
}
