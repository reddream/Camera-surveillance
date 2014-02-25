package Client;

import protocol.ImageContainer;

public class ImageHandler extends Thread {
	private MonitorBuffer monitor;
	private GUI gui;
	private ImageContainer[] images;
	
	public ImageHandler(MonitorBuffer monitor, GUI gui) {
		this.monitor = monitor;
		this.gui = gui;
	}

	public void run() {
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (true) {
			try {

				sleep(36);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			images = monitor.getImage();
			gui.refreshImage(images);
			try {
				sleep(monitor.getSleepTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
