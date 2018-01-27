/**
 * 
 */
package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import xinyu126.common.DataContainer;

/**
 * @author xinyu126
 *
 */
public class TestWar {

	class Server implements Runnable{
		public void run(){
			while (p == null) {
				try {
					server.receive(packet);
					
					byte[] data = packet.getData();
					byte[] mm = "à¸à¸à¸à¸à¸à¸dota".getBytes();
					mm = new String(mm).getBytes("utf-8");
					int len = mm.length > 31 ? 31 : mm.length;
					for (int i = 0; i < len; i++) {
						 data[20 + i] = mm[i];
					}
					p = new DatagramPacket(data, packet.getLength(), ip, 6112);
					System.out.println("shoudao" + new String(data));
				} catch (IOException e) {
				}
				
			}
			while (p != null) {
				try {
					Thread.sleep(3000);
					server.send(pub);
					// server.receive(packet);
					Thread.sleep(1000);
					server.send(p);
					
				} catch (InterruptedException e) {
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("·¢ËÍ!!");
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestWar();

	}

	DatagramSocket server;
	DatagramPacket packet, pub, p;
	byte[] data;
	InetAddress ip;
	TestWar() {
		try {
			server = new DatagramSocket(6113);
			ip = InetAddress.getLocalHost();
			packet = new DatagramPacket(DataContainer.warSearch, 16, ip, 6112);
			pub = new DatagramPacket(DataContainer.warPublish, 16, ip, 6112);
			server.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		data = new byte[1024];
		packet = new DatagramPacket(data, data.length);
		p = null;
		new Thread(new Server()).start();
	}

}
