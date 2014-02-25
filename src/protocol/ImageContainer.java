package protocol;

public class ImageContainer {
	private byte[] image;
	private long delay, timeStamp;

	public ImageContainer(byte[] image){
		 timeStamp = ((image[25] & 255L) << 24 | (image[26] & 255L) << 16 | (image[27] & 255L) << 8 | image[28] & 255L) * 1000 + (image[29] & 255L) * 10;
		 delay = System.currentTimeMillis() - timeStamp; //needs to be declared asap after network transfer
		 this.image = image;
	}
	public long getDelay(){
		return delay;
	}
	public byte[] getImage(){
		return image;
	}
	public long getTimeStamp(){
		return timeStamp;
	}
}
