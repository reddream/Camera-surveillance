package Server;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import se.lth.cs.cameraproxy.Axis211A;
import se.lth.cs.cameraproxy.MotionDetector;

public class ConnectPane extends JFrame
{
	//GUI fields
	private JLabel ip_label, cameraPort_label, serverPort_label;
	private JButton startButton;
	private JTextField ip_field;
	private JTextField cameraPort_field;
	private JTextField serverPort_field;
	
	private int nrServers;

	//threads and monitors etc
	private Monitor mon;
	private Server serv;
	private Out out;
	private Axis211A cam;
	private MotionDetector md;
	private In in;
	private TimerThread tt;
	

	public ConnectPane(int nrServers, Monitor mon)
	{
		super();
		
		this.nrServers = nrServers;
		this.mon = mon;	
		this.getContentPane().setLayout(new GridLayout(nrServers + 1, 3));
	
		Container c1 = new Container();
		c1.setLayout(new FlowLayout());
		ip_label = new JLabel("IP of Camera:");
		ip_field = new JTextField("argus-1.student.lth.se");
		cameraPort_label = new JLabel("Port of Camera");
		cameraPort_field = new JTextField("" + (7000));
		serverPort_label = new JLabel("Port of Server");
		serverPort_field = new JTextField("" + (6011));

		c1.add(ip_label);
		c1.add(ip_field);
		c1.add(cameraPort_label);
		c1.add(cameraPort_field);
		c1.add(serverPort_label);
		c1.add(serverPort_field);
		this.getContentPane().add(c1);


		startButton = new JButton("Start Server!");
		startButton.addActionListener(new StartServerListener());
		this.getContentPane().add(startButton);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private class StartServerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			setVisible(false);
			String ip = ip_field.getText();
			int servPort = Integer.parseInt(serverPort_field.getText());
			int camPort = Integer.parseInt(cameraPort_field.getText());
			
			System.out.println(ip + "   " + servPort + "     " + camPort);
			//Thread creation and start, monitor already created in main
			serv = new Server(mon, servPort);
			System.out.println("server created");
			cam = new Axis211A(ip, camPort);
			System.out.println("camera created");
			md = new MotionDetector(ip, camPort);
			System.out.println("md created");
			out = new Out(mon, serv, cam, md);
			System.out.println("out created");
			in = new In(mon, serv);
			System.out.println("in created");
			tt = new TimerThread(5000, mon);
			System.out.println("tt created");
			out.start();
			System.out.println("out started");
			in.start();
			System.out.println("in started");
			tt.start();
			System.out.println("tt started");
			serv.handleRequests();
		}
		
	}
	
}
