package Client;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Connect extends JFrame {
	private JLabel ip, port;
	private JButton connect;
	private boolean pressedButton = false;
	private int imSize;
	private MonitorBuffer mon;
	private JTextField[] ips;
	private JTextField[] ports;
	private ImageHandler im;

	public Connect(int imSize, MonitorBuffer mon, ImageHandler im){
		super();
		this.imSize = imSize;
		this.im = im;
		this.mon = mon;
		this.getContentPane().setLayout(new GridLayout(imSize+1,0));
		ips = new JTextField[imSize];
		ports = new JTextField[imSize];
		for(int i=0; i<imSize; i++){
			Container c1 = new Container();
			c1.setLayout(new FlowLayout());
			ip = new JLabel("IP:");
			ips[i] = new JTextField("127.0.0.1");
			port = new JLabel("Port:");
			ports[i] = new JTextField("" + (6011+i));
			c1.add(ip);
			c1.add(ips[i]);
			c1.add(port);
			c1.add(ports[i]);			
			this.getContentPane().add(c1);
		}
		connect = new JButton("Connect");
		connect.addActionListener(new ConnectListener());
		this.getContentPane().add(connect);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}
	
	public String[] getIps(){
		String[] sIps = new String[imSize];
		for(int i=0; i<imSize; i++){
			sIps[i] = ips[i].getText();
		}
		return sIps;
	}
	public class ConnectListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] ips = getIps();
				int[] ports = getPorts();
				Client clients[] = new Client[imSize];
				for(int i=0; i<imSize; i++){
				clients[i] = new Client(ips[i], ports[i]);
				System.out.println("Connected to: "+ips[i] + ":" +ports[i]);
				In in = new In(clients[i], mon, i);
				in.start();
				}
				im.start();
				Ut ut = new Ut(clients,mon);
				ut.start();
				setVisible(false);
			}
		}
	
	public int[] getPorts(){
		int[] iPorts = new int[imSize];
		for(int i=0; i<imSize; i++){
			iPorts[i] = Integer.parseInt(ports[i].getText());
		}
		return iPorts;
	}
	
	public boolean isPressed(){
		return pressedButton;
	}
}
