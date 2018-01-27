/**
 * 
 */
package xinyu126.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;

/**
 * @author Administrator
 *
 */
public class UDPSender {
	public static DatagramSocket udpSocket;

	public static void sendToAll(byte[] msgByte) {
		Iterator<UserNode> it = DataContainer.UserList.iterator();
		DatagramPacket p = new DatagramPacket(msgByte, msgByte.length, null, 6114);
		try {
		while (it.hasNext()) {
			UserNode user = it.next();
			InetAddress ip = user.IP;
			p.setAddress(ip);
			udpSocket.send(p);
		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public static void sendUDP(byte[] b, InetAddress ip) throws IOException {
		DatagramPacket p = new DatagramPacket(b, b.length, ip, 6114);
		
			udpSocket.send(p);
		
	}
	public UDPSender(DatagramSocket socket) {
		udpSocket = socket;
	}
}
