import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;    
import java.io.*;  

import javax.swing.JOptionPane;

public class KleineServer {
	private int port;
	
	public KleineServer(){
		port = 6077;
	}
	
	public void handleRequest() throws IOException{
		ServerSocket serverS = new ServerSocket(port);
		int i = 1;
		
		while(true){
			Socket clientSocket = serverS.accept();
			InputStream is = clientSocket.getInputStream();
			OutputStream os = clientSocket.getOutputStream();
			String message = getLine(is);
			String hello = JOptionPane.showInputDialog("Skicka shit till Axel:");
			if (hello == null) hello = "Fredde bryr sig inte";
			System.out.println("Axel: " + message);
			System.out.println("Jag: " + hello);
			os.write(hello.getBytes(), 0, hello.length());
			os.flush();
			clientSocket.close();
		}
	}
	
	private static String getLine(InputStream s)
		    throws IOException {
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
		                result += (char) ch;
		            }
		        }
		        return result;
		    }
	
	private static final byte[] CRLF      = { 13, 10 };
	
    private static void putLine(OutputStream s, String str)
    throws IOException {
        s.write(str.getBytes());
        s.write(CRLF); //CRLF
    }
    public static void main(String[] args){
    	KleineServer theServer = new KleineServer();
        try {
            theServer.handleRequest();
        } catch(IOException e) {
            System.out.println("Error!");
            System.exit(1);
        }
    }
}
