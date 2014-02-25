package Test;

import java.util.Random;

import protocol.DataPacket;
import se.lth.cs.cameraproxy.Axis211A;

public class PacketTest {
	public static void main(String[] args) {
		Random rand = new Random();
		byte[] ens = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		for (int i = 0; i < ens.length; i++) {
			ens[i] = (byte) rand.nextInt();
		}
		long start = System.currentTimeMillis();
		DataPacket dp = new DataPacket(ens.length, DataPacket.IDLE, ens);
		long elapsed = System.currentTimeMillis() - start;
		dp.byteArray();
		System.out.println(elapsed);
		
	
	}
}
