package Client;

import protocol.DataPacket;

public class Ut extends Thread{
	private MonitorBuffer monitor;
	private int mode;
	private Client[] clients;


	public Ut(Client[] clients,MonitorBuffer monitor){
		this.monitor = monitor;
		this.clients = clients;
	}

	public void run(){
		while(true){
			byte[] b = new byte[0];
			mode = monitor.getMode();
			DataPacket dp = new DataPacket(0,mode,b);
			for(Client client: clients){
			client.putMessage((dp.byteArray()));
			}
		}
	}
}
