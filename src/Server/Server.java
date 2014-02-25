package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port;
	private InputStream is;
	private OutputStream os;
	private Socket clientSocket;
	private Monitor mon;

	public Server(Monitor mon, int port) {
		this.mon = mon;
		this.port = port;
	}

	public Server(Monitor mon) {
		this.mon = mon;
		this.port = 6099;
	}

	public void handleRequests() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out
					.println("Server started! Waiting for client to connect...");
			while (true) {
				clientSocket = serverSocket.accept();
				is = clientSocket.getInputStream();
				os = clientSocket.getOutputStream();
				mon.setConnection(true); // streams initiated and client
										// connected
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Client connected!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getMessage() {
		byte[] message = new byte[5];
		try {
			if (is != null) {
				is.read(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public void putMessage(byte[] message) {
		try {
			
			if (clientSocket.isConnected()) {
				os.flush();
				os.write(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
