/**
 * 
 */
package war3Server;

//udp包的最佳大小是1478 byte
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class Server {
	class CheckOnline implements Runnable{
		final long timeout = 20 * 60 * 1000;
		private void check(Vector<UserNode> online){
			Iterator<UserNode> u = online.iterator();
			while (u.hasNext()) {
				UserNode uu = u.next();
				// System.out.print(uu.id + ":" + uu.name + ":" + uu.IP);
				// 检测在线状态，在线的设置成不在线。不在线的移除！
				// 在线状态需要客户端定时维持,否则剔除
				if (uu.online) {
					uu.online = false;
				} else {
					u.remove();
				}
			}
		}

		public void run() {
			while(flag&&!udpServer.isClosed()){
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				check(online);// 检测在线
				byte[][] b = creatOnline();// 生成在线列表
				sendOnlineListToAll(b);// 发送给所有人
			}			
		}
	}
	class ServerRunner implements Runnable {
		// private String IPFrom;
		private InetAddress IPFrom;
		
		// run方法---------
		public void run() {
			try {
				while (!udpServer.isClosed() && flag) {
					udpServer.receive(udpPacket);
					IPFrom = udpPacket.getAddress();
					
					byte[] udpdatabyte = udpPacket.getData();
					// System.out.print("接到：");
					// 上线消息：'+'(1), version int(1),userName(12)
					if ('+' == (udpdatabyte[0])) {
						// System.out.print(new String(udpdatabyte));
						UserNode user;
						if ((user = findUser(IPFrom)) != null) {
							user = new UserNode(subArray(udpdatabyte, 2, udpPacket.getLength() - 2), IPFrom);
						} else {
							user = new UserNode(subArray(udpdatabyte, 2, udpPacket.getLength() - 2), IPFrom);
							addUser(user);
						}
						// 发送在线列表
						byte[][] b = creatOnline();
						for (int i = 0; i < b.length; i++) {
							DatagramPacket p = new DatagramPacket(b[i], 1404, udpPacket.getAddress(), 6114);
							udpServer.send(p);
						}
					 }
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Server();

	}
	private boolean flag = true;
	
	Vector<UserNode> online;
	DatagramSocket udpServer;
	DatagramPacket udpPacket;
	byte[] udpbuff;
	long id;
	// ---------------------------------------------------
	
	Server(){
		online = new Vector<UserNode>();
		udpbuff = new byte[1024 * 4];
		udpPacket=new DatagramPacket(udpbuff, udpbuff.length);
		try {
			udpServer=new DatagramSocket(6113);
			System.out.println("open 6113");
			new Thread(new ServerRunner()).start();
			new Thread(new CheckOnline()).start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	// -----------------------------------------------
	
	private void addUser(UserNode user) {
		online.add(user);	
	}

	///填充byte[]数组
	private void copyByte(byte[] bigArray,byte[] smallArray,int begain){
		for(int i=0;i<smallArray.length;i++){
			bigArray[i+begain]=smallArray[i];
		}
	}

	// 生成在线列表
	// 每个UDP包大小是1404=20*70+4
	private byte[][] creatOnline() {
		int k = online.size();
		int table = k / 70;
		byte[][] b = new byte[table + 1][1404];
		int i = 0;
		byte[] head = "ls".getBytes();
		Iterator<UserNode> u = online.iterator();
		table = 0;
		copyByte(b[0], head, 0);// 头--用于接收验证
		b[0][2] = 0;
		while (u.hasNext()) {
			if (i >= 70) {
				table++;
				i = 0;
				copyByte(b[table], head, 0);
				b[table][2] = (byte) table;
			}
			UserNode user = u.next();
			copyByte(b[table], user.toByte(), i * 20 + 4);
			i++;
		}
		return b;
	}
	/**
	 * @param userName
	 * @return boolean
	 */
	private UserNode findUser(InetAddress ip) {
		UserNode user;
		for (int i = 0; i < online.size(); i++) {
			user = online.get(i);
			if (user.IP.equals(ip)) {
				return user;
			}
		}
		return null;
	}

	//
	private void sendOnlineListToAll(byte[][] b) {
		for (int i = 0; i < b.length; i++) {
			// 发送给每个在线
			UserNode user;
			Iterator<UserNode> iterator = online.iterator();
			try {
				while (iterator.hasNext()) {
					user = iterator.next();
					DatagramPacket p = new DatagramPacket(b[i], 1364, user.IP, 6113);
					udpServer.send(p);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public byte[] subArray(byte[] bb, int offset, int length) {
		int t = bb.length - offset;
		if (length < t) {
			t = length;
		}
		byte[] b=new byte[t];
			for(int i=0;i<t;i++){
				b[i]=bb[i+offset];
			}
		return b;
	}
}

/**

 */
class UserNode {


	// all 20 byte
	// version 1 byte
	// dormitory 3 byte
	// IP 4 byte
	// name 12 byte

	public byte[] VDN;// version and dormitory and name
	public InetAddress IP;
	public boolean online;
	public String tflag = null;
	public UserNode(byte[] vdn, InetAddress ip) {
		this.VDN = vdn;
		this.IP = ip;
		online = true;
	}
	public byte[] toByte() {
		int i = 0;
		byte[] b = new byte[20];
		// IP
		byte[] ip = IP.getAddress();
		for (; i < 4; i++) {
			b[i] = ip[i];
		}
		for (; i < 4 + VDN.length; i++) {
			b[i] = VDN[i - 4];
		}
		return b;
	}
		
}