package Server;

import javax.swing.JOptionPane;

import se.lth.cs.cameraproxy.Axis211A;
import se.lth.cs.cameraproxy.MotionDetector;

public class ServerMain {

	public static void main(String[] args) 
	{
		Monitor mon = new Monitor();		
		ConnectPane cpane = new ConnectPane(1, mon);
		//Threads started in cpane
		
	}
}
