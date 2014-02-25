import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;


public class KleineClient {
	private static final int PORT_FOR_EXAMPLE = 6077;
	private int port;
	private String host;
	
	public KleineClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public KleineClient(String host) {
		this(host, PORT_FOR_EXAMPLE);
	}
	
	public KleineClient() throws UnknownHostException {
		this(InetAddress.getLocalHost().toString());
	}
	
	public void sendToAndGetFromServer() {
		while (true) {
			try {
				Socket socket = new Socket(host, port);
				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();
				String message = JOptionPane.showInputDialog("Skicka shit till Fredde:");
				if (message == null) {
					message = "Skrev inget";
					break;
				}
				System.out.println("Jag: " + message);
				byte[] toRead = message.getBytes();
				out.write(toRead,0, toRead.length);
				out.write(10);
				System.out.println("Fredde: " + getLine(in));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void GetFromServer() {
		
	}
	
	public static void main(String[] args) {
		KleineClient kc = new KleineClient("130.235.235.84");
		kc.sendToAndGetFromServer();
	}
	
	 private static String getLine(InputStream s) throws IOException {
	        boolean done = false;
	        String result = "";

	        while(!done) {
	            int ch = s.read();        // Read
	            if (ch <= 0 || ch == 10) {
	                // Something < 0 means end of data (closed socket)
	                // ASCII 10 (line feed) means end of line
	                done = true;
	            }
	            else if (ch >= ' ') {
	                result += (char)ch;
	            }
	        }

	        return result;
	    }
}
