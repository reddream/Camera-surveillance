package Server;

import java.nio.ByteBuffer;

import protocol.DataPacket;
import se.lth.cs.cameraproxy.Axis211A;
import se.lth.cs.cameraproxy.MotionDetector;

public class Out extends Thread {
	private Monitor mon;
	private Axis211A myCamera;
	private byte[] message;
	private Server server;
	private MotionDetector md;
	private int oldMode;
	
	public Out(Monitor mon, Server ss, Axis211A myCamera, MotionDetector md) {
		this.mon = mon;
		this.server = ss;
		this.myCamera = myCamera; 
		message = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		this.md = md;
		oldMode = -1;
		if (!myCamera.connect()) 
		{
			System.out.println("Failed to connect to camera!");
			System.exit(1);
		}
	}

	public void run() {
		while (true) {
			System.out.println("start of while");
			mon.waitForConnection();
			System.out.println("wait for connection done");
//			if (!mon.isClockSent()) 
//			{
//				sendClock();
//				mon.setClockSent(true);				
//				System.out.println("sent clock");
//			}
			mon.setDetected(md.detect());
			System.out.println("gg");
			if (mon.getMode() == DataPacket.MOVIE && oldMode != DataPacket.MOVIE) {
				sendMode(DataPacket.MOVIE);
			}
			System.out.println("wtf");
			oldMode = mon.getMode();
			sendJPEG();
			mon.delay();
		}
	}

	public void sendMode(int mode)
	{
		DataPacket packet = new DataPacket(0, mode, new byte[0]);
		server.putMessage(packet.byteArray());
	}
	public void sendClock() 
	{
		long time = System.currentTimeMillis();
		byte[] bytes = ByteBuffer.allocate(8).putLong(time).array();	
		DataPacket packet = new DataPacket(bytes.length, DataPacket.CLOCK, bytes);
		server.putMessage(packet.byteArray());
	}
	
	public void sendJPEG() {

		//if (!myCamera.connect()) {
		//	System.out.println("Failed to connect to camera!");
		//	System.exit(1);
		//}
		int len = myCamera.getJPEG(message, 0);
		DataPacket packet = new DataPacket(len, DataPacket.JPEG, message);
		server.putMessage(packet.byteArray());
	}

}
