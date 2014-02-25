package Server;

import se.lth.cs.realtime.PeriodicThread;

public class TimerThread extends PeriodicThread {
	private Monitor mon;
	
	public TimerThread(long period, Monitor mon) {
		super(period);
		this.mon = mon;
	}
	
	public void perform() {
		mon.timeToSend();
	}
}
