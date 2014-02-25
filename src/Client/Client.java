package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import se.lth.cs.fakecamera.Axis211A;

public class Client {
	private static final int CLOCK_PACKET_SIZE = 8;
	private int port;
	private InputStream is;
	private OutputStream os;
	private Socket socket;
	private String ip;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void connect() {
		try {
			socket = new Socket(ip, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			System.out.println("Server connected!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getHeader() {
		byte[] header = new byte[5];
		try {
			if (is != null) {
				is.read(header);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return header;
	}
	public byte[] getClock()
	{
		byte[] clock = new byte[CLOCK_PACKET_SIZE];
		try{
			is.read(clock, 0, CLOCK_PACKET_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clock;
	}
	public byte[] getImage(int size)
	{
		byte[] img = new byte[size];
		int totalRead = 0;
		try	{
			while (totalRead < size) {
				int actRead = is.read(img, totalRead, size-totalRead);
				totalRead += actRead;
			}
			int leftToRead = Axis211A.IMAGE_BUFFER_SIZE - totalRead;
			for (int i = 0; i < leftToRead; i++) {
				is.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	public void putMessage(byte[] message) {
		try {
//			os.flush();
	//		System.out.println("Sent something to server!");
			os.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean isConnected() {
		return socket.isConnected();
	}
}
