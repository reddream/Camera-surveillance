package Server;

import protocol.DataPacket;

public class Monitor {
	private int curMode; // IDLE OR MOVIE
	private boolean connected;
	private boolean detected;
	private boolean clockSent;
	private boolean timeToSend;
	private boolean connectPanePressed;
	
	public Monitor() {
		connected = false;
		detected = false;
		clockSent = false;
		timeToSend = false;
		connectPanePressed = false;
		curMode = DataPacket.IDLE;
	}

	public synchronized void setMode(int m) {
		if (curMode != m) {
			curMode = m;
			System.out.println("Mode changed to:" + m);
//			notifyAll();
		}
	}

	public synchronized int getMode() {
		return curMode;
	}

	public synchronized void setConnection(boolean open) {
		connected = open;
		if (!open) clockSent = false; //reset clockSent if the client disconnects
		notifyAll();
	}

	public synchronized void waitForConnection() {
		while (!connected) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized boolean isClockSent()
	{
		return clockSent;
	}
	
	public synchronized void setClockSent(boolean sent)
	{
		clockSent = sent;
//		notifyAll();
	}
	
	public synchronized void delay() 
	{
		switch (curMode) {
			case DataPacket.IDLE:
				while (!detected && !timeToSend)
				{
					try{
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(detected) setMode(DataPacket.MOVIE);
				break;
			case DataPacket.MOVIE: break;
			default: break;
		}
		timeToSend = false;
//		notifyAll();
	}

	public synchronized void setDetected(boolean newValue) {
		if (detected != newValue) {
			detected = newValue;
			notifyAll();
		}
	}

	public synchronized void timeToSend() {
		timeToSend = true;
		notifyAll();
	}
}
