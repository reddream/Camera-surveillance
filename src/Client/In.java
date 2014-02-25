package Client;

import java.nio.ByteBuffer;

import protocol.DataPacket;
import protocol.ImageContainer;
import protocol.PacketParser;

public class In extends Thread
{
	private MonitorBuffer monitor;
	private Client client;
	private int id;

	public In(Client client, MonitorBuffer monitor, int id)
	{
		this.id = id;
		this.monitor = monitor;
		this.client = client;
		client.connect();
	}

	// trycka ut fakebilder kontinuerligt.
	public void run()
	{
		while (true)
		{
			PacketParser packet = new PacketParser();
			if (client.isConnected())
			{
				byte[] header = client.getHeader();
				packet.unWrapHeader(header);
				int size = packet.getMsgSize();
				int mode = packet.getType();
				switch (mode) 
				{
					case DataPacket.JPEG:
						System.out.println(size);
						byte[] networkImg = client.getImage(size);
						ImageContainer image = new ImageContainer(networkImg);
						monitor.setJPEG(image, id);
						break;
					case DataPacket.CLOCK:
						byte[] clock = client.getClock();
						long time = ByteBuffer.wrap(clock).getLong(); //hoppas detta funkar
						monitor.setClock(id,time);
						break;
					case DataPacket.MOVIE:
						monitor.setMode(DataPacket.MOVIE);
						break;
					default: break;
				}
			}
		}
	}
}
