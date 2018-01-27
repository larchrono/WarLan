/**
 * 
 */
package xinyu126.common;

import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;




/**
 * @author Administrator
 *
 */
public class UserNode {


	// all 18 byte
	// head 2 byte
	// version 1 byte
	// 楼 3 byte
	// name 12 byte
	public class Msgs {
		public Date time;
		public String userName;
		public String msg;

		Msgs(String msg, String userName) {
			this.msg = msg;
			this.userName = userName;
			this.time = new Date();
		}
	}
	public String userName;
	public InetAddress IP;
	public byte version;
	public String dormitory;
	long id;
	public LinkedList<Msgs> msgList;
	/**
	 * @param userName
	 * @param ip
	 * @param version
	 */
	public UserNode(String userName, InetAddress ip, byte version, String dormitory) {
		// TODO Auto-generated constructor stub
		this.userName = userName;
		this.IP = ip;
		this.version = version;
		this.dormitory = dormitory;
		msgList = new LinkedList<Msgs>();
	}

	/**
	 * @param userName
	 * @param msg
	 */
	public void addMSG(String userName, String msg) {
		// TODO Auto-generated method stub
		
	}

	public String getIPstr() {
		return IP.getHostAddress();
	}

	/**
	 * 生成带头消息的byte型用户信息 "++" 2 byte version 1 byte dormitory 3 byte userName 12
	 * byte all=18
	 * */
	public byte[] toByteHasHead() {		
		int i = 0;
		int k;
		byte[] temp;
		byte[] b = new byte[14];
		temp = "++".getBytes();
		for (k = 0; k < 2; k++, i++) {
			b[i] = temp[k];
		}
		b[2] = version;
		i++;
		temp = (dormitory + " ").getBytes();
		for (k = 0; k < 3; k++, i++) {
			b[i] = temp[k];
		}
		byte[] un = userName.getBytes();
		int len = un.length < 12 ? un.length : 12;
		for (k = 0; k < len; k++, i++) {
			b[i] = un[k];
		}
		return b;
	}
}
