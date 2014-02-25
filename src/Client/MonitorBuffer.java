package Client;

import java.util.ArrayDeque;
import java.util.Queue;

import protocol.ImageContainer;
import se.lth.cs.cameraproxy.Axis211A;

public class MonitorBuffer {

	// modestate
	private int mode;
	private boolean modeChanged;
	private boolean synchronous;

	// imagestate
	private boolean newImage;
	private int imSize;
	private ArrayDeque[] images;
	private long[] clocks;
	private long serverDiff;
	private long sleep;

	public MonitorBuffer(int imSize) {
		this.imSize = imSize;
		images = new ArrayDeque[imSize];
		for (int i = 0; i < imSize; i++) {
			images[i] = new ArrayDeque<ImageContainer>();
		}
		newImage = false;
		clocks = new long[imSize];
	}

	public synchronized void setJPEG(ImageContainer img, int id) {
		images[id].add(img);
		System.out.println("Set" + id);
		newImage = true;
		notifyAll();
	}

	public synchronized ImageContainer[] getImage() {
		while (!newImage) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ImageContainer[] returnImages = new ImageContainer[imSize];
		if(!synchronous){
			for (int i = 0; i < imSize; i++) {
				if (images[i].size() != 0) {
					returnImages[i] = (ImageContainer) images[i].poll();
				}
			}
			newImage = false;
			System.out.println("Get no sync");
			return returnImages;
		}else{
			boolean bothExist = true;
			for (int i = 0; i < imSize; i++) {
				if (images[i].size() != 0) {
					returnImages[i] = (ImageContainer) images[i].peek();
				}else{
					bothExist = false;
				}
			}
			if(!bothExist){
				sleep = 0;
				System.out.println("Get only 1 image exist, synced");
				return returnImages;
			}
			sleep = returnImages[0].getTimeStamp() - returnImages[1].getTimeStamp();
			long possibleSleep; //check second element in queue for new sleep
			if(sleep>0){
				//returnImages[0] ska ej uppdateras, sleepas
				//måste kolla andra elementet i [1]-kön
				ImageContainer firstInQueue = (ImageContainer) images[1].poll();
				if(images[1].size() != 0){
					ImageContainer secondInQueue = (ImageContainer) images[1].peek();
					possibleSleep = firstInQueue.getTimeStamp() - secondInQueue.getTimeStamp();
					if(sleep > possibleSleep){
						sleep = possibleSleep;
					}
				}
				returnImages[0] = null;
			}else{
				//måste kolla andra elementet i [0]-kön
				//returnImages[1] ska ej uppdateras, sleepas. Glöm ej negativ sleep
				sleep = Math.abs(sleep);
				ImageContainer firstInQueue = (ImageContainer) images[0].poll();
				if(images[0].size() != 0){
					ImageContainer secondInQueue = (ImageContainer) images[0].peek();
					possibleSleep = firstInQueue.getTimeStamp() - secondInQueue.getTimeStamp();
					if(sleep > possibleSleep){
						sleep = possibleSleep;
					}
				}
				returnImages[1] = null; 
			}
			System.out.println("Get synced");
			return returnImages;
		}
	}

	public synchronized long getSleepTime() {
		if(!synchronous){
			sleep = 0;
		}
		return sleep;
	}

	public synchronized void setClock(int id, long clock) {
		clocks[id] = clock;
		if (imSize >= 2) {
			if (clocks[0] > 0 && clocks[1] > 0) {
				serverDiff = clocks[0] - clocks[1];
			}
		}
	}

	public synchronized void setSync(boolean sync) {
		synchronous = sync;
	}

	public synchronized void setMode(int mode) {
		this.mode = mode;
		modeChanged = true;
		notifyAll();
	}

	public synchronized int getMode() {
		while (!modeChanged) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		modeChanged = false;
		return mode;
	}

}
