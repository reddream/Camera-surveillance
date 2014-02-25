package Client;

import javax.swing.JOptionPane;

public class ClientTestMain
{

	public static void main(String[] args)
	{
		int cameras = Integer.parseInt(JOptionPane.showInputDialog(null, "How many servers do you wish to connect to?"));
		MonitorBuffer mon = new MonitorBuffer(cameras);
		Buttons ge = new Buttons(mon);
		GUI gui = new GUI(ge, mon,cameras);
		ImageHandler im = new ImageHandler(mon, gui);
		Connect c = new Connect(cameras,mon, im);
		gui.setVisible(true);
		}
	}
