package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import protocol.DataPacket;

public class GuiEventProcessor implements ActionListener {
	private int mode;
	private MonitorBuffer monitor;
	private String curMode;

	public GuiEventProcessor(MonitorBuffer monitor) {
		mode = DataPacket.IDLE;
		this.monitor = monitor;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (mode == DataPacket.IDLE) {
			mode = DataPacket.MOVIE;
			curMode = "MOVIE";
		} else {
			mode = DataPacket.IDLE;
			curMode = "IDLE";
		}
		System.out.println("Mode changed to: " + curMode);
		monitor.setMode(mode);
	}
}