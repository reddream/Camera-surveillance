package protocol;

import java.nio.ByteBuffer;

import Server.Out;

//This works

public class DataPacket {
	public static final int IDLE = 1;
	public static final int MOVIE = 2;
	public static final int JPEG = 3;
	public static final int CONCLOSE = 4;
	public static final int CLOCK = 5;

	private int msgSize;
	private int type;
	private byte[] message;
	private byte[] header;
	private byte[] completeMsg;

	public DataPacket(int msgSize, int type, byte[] message) {
		this.msgSize = msgSize;
		this.type = type;
		this.message = message;
		this.header = assembleHeader(this.msgSize, type);
		this.completeMsg = completeMessage();
	}

	private byte[] assembleHeader(int msgSize, int type) {
		byte[] header = new byte[5];
		byte[] size = ByteBuffer.allocate(4).putInt(msgSize).array();
		for (int i = 0; i < 4; i++) {
			header[i] = size[i];
		}
		header[4] = (byte) type;
		return header;
	}

	private byte[] completeMessage() {
		byte[] complete = new byte[header.length + message.length];
		int index = 0;
		for (byte b : header) {
			
			complete[index++] = b;
		}
		for (byte b : message) {
			complete[index++] = b;
		}
		return complete;
	}
	
	public byte[] byteArray() {
		return completeMsg;
	}

}
