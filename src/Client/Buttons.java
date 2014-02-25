package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import protocol.DataPacket;

public class Buttons {
	private MonitorBuffer monitor;
	private ModeListener idleListener,movieListener;
	private SyncListener syncListener, asyncListener;
	
	
	public Buttons(MonitorBuffer monitor) {
		this.monitor = monitor;
		idleListener = new ModeListener(DataPacket.IDLE);
		movieListener = new ModeListener(DataPacket.MOVIE);
		syncListener = new SyncListener(true);
		asyncListener = new SyncListener(false);
	}

	private class ModeListener implements ActionListener {
		private int mode;

		private ModeListener(int mode) {
			this.mode = mode;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			monitor.setMode(mode);
		}
	}

	private class SyncListener implements ActionListener {
		private boolean sync;
		
		private SyncListener(boolean sync){
		this.sync = sync;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			monitor.setSync(sync);
		}
	}

	
	public ModeListener getIdleListener() {
		return idleListener;
	}

	public ModeListener getMovieListener() {
		return movieListener;
	}
	
	public SyncListener getSyncListener() {
		return syncListener;
	}
	public SyncListener getAsyncListener(){
		return asyncListener;
	}
}