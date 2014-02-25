package Server;

import protocol.DataPacket;
import protocol.PacketParser;

/*
 * Responsible for handling the request sent by clients.
 * Updates the mode in monitor.
 */
public class In extends Thread
{
	private Monitor mon;
	private Server server;
	private int oldType;

	public In(Monitor mon, Server serv)
	{
		this.mon = mon;
		this.server = serv;
		oldType = -1;
	}

	public void run()
	{
		PacketParser parser = new PacketParser();
		while (true)
		{
			byte[] message = server.getMessage();
			
			parser.unWrapHeader(message);
			int type = parser.getType();
			if (oldType == type) continue;
//			System.out.println("Received type: " + type);
			switch (type)
			{
			case DataPacket.IDLE:
				mon.setMode(DataPacket.IDLE);
				break;
			case DataPacket.MOVIE:
				mon.setMode(DataPacket.MOVIE);
				break;
			case DataPacket.CONCLOSE:
				System.out.println("Connection closed with a client!");
				mon.setConnection(false);
				break;
			}
			oldType = type;
		}
	}
}
