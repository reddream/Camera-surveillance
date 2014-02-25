package protocol;

import java.nio.ByteBuffer;

//wrapper class, has info in its attributes after unwrapping data from bytestream
public class PacketParser {
	private static final int SIZE_BYTES = 4;
	private static final int TYPE_INDEX = 4;
	private int size;
	private int type;

	public PacketParser() {
	}

	public void unWrapHeader(byte[] in)
	{
		byte[] msgSize = new byte[4];
		for (int i = 0; i < SIZE_BYTES; i++) {
			msgSize[i] = in[i];
		}
		size = ByteBuffer.wrap(msgSize).getInt();
		type = in[TYPE_INDEX];
	}
	public int getMsgSize() {
		return size;
	}

	public int getType() {
		return type;
	}	
}
