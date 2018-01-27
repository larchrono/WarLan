/**
 * 
 */
package xinyu126.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @author xinyu126
 *
 */
public class MainP {

	// 定时发送一个包给服务器，表示我在线。
	private class KeepOnline implements Runnable {
		public void run() {
			try {
				while (keepFlag) {
					Thread.sleep(15 * 60 * 1000);
					UDPSender.sendUDP(DataContainer.ME.toByteHasHead(), DataContainer.serverIP);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}

	private class MSGServer implements Runnable {

		/**
		 * @param udpdata
		 *            解析显示消息 消息结构：2+…… =>2:头 + 名字|MSG
		 * 
		 */

		public void run() {
			try {
				while (MSGFlag) {
					udpSocket.receive(udpPacket);
				
					byte[] udpdata = Arrays.copyOf(udpPacket.getData(), udpPacket.getLength());
					// System.out.print('+' == udpdata[0]);
					// System.out.print((char) udpdata[0]);
					if ('m' == udpdata[0] && 's' == udpdata[1]) {
						// 消息
						resolveMSG(udpdata);
					} else if ('+' == udpdata[0] && '+' == udpdata[1]) {

						DataContainer.addNewUser(udpdata, udpPacket.getAddress(), 2);
					} else if ('l' == udpdata[0] && 's' == udpdata[1]) {
						// 在线列表
						msgWriter.insertString("获取在线列表……ok");
						DataContainer.freshList(udpdata);
						byte[] msgByte = DataContainer.ME.toByteHasHead();
						UDPSender.sendToAll(msgByte);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				msgWriter.insertString(e.toString());
			} finally {
				msgWriter.insertSysMSG("对不起,出错啦!你无法在接受消息了.为了尽快改正.请与作者联系!");
			}

		}

	}
	
	private DatagramPacket udpPacket;
	private boolean MSGFlag = true;
	private boolean keepFlag = true;
	private DatagramSocket udpSocket;
	private byte[] databuf;
	private MsgWriter msgWriter;
	public MainP(MsgWriter msgWriter, DatagramSocket chatSocket) {
		this.udpSocket = chatSocket;
		this.msgWriter = msgWriter;
		init();
		new Thread(new MSGServer()).start();
		new Thread(new KeepOnline()).start();
	}
	private void init() {
		databuf = new byte[2048];
		udpPacket = new DatagramPacket(DataContainer.ME.toByteHasHead(), DataContainer.ME.toByteHasHead().length,
				DataContainer.serverIP, 6113);
		try {
			udpSocket.send(udpPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		udpPacket = new DatagramPacket(databuf, 2048);
	}
	
	private void resolveMSG(byte[] udpdata) {
		
		String str = new String(udpdata);
		String[] strArray = str.split("\\|");
		if ("msall".equals(strArray[0])) {
			msgWriter.insertUserMSG(strArray[1], strArray[2].trim());
		}else if("ms".equals(strArray[0])){
			DataContainer.addMSG(udpPacket.getAddress(), strArray[1], strArray[2]);
		}
	} 
}
