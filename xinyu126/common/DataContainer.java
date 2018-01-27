/**
 * static data
 * 所有的公用数据都在这摆放着,都是静态的!
 * 像在线列表.本机IP,魔兽搜索包等~~~~~
 */
package xinyu126.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultListModel;

import xinyu126.ui.ChatP2PUI;

/**
 * @author xinyu126
 *
 */
public class DataContainer {
	public static UserNode ME;
	public static InetAddress myIP, serverIP;
	public static LinkedList<UserNode> UserList;
	public static byte[] warSearch = { (byte) 0xf7, 0x2f, 0x10, 0x00, 0x50, 0x58, 0x33, 0x57, 0x14, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00 };
	public static byte[] warPublish = { (byte) 0xF7, 0x32, 0x10, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
			0x0A, 0x00, 0x00, 0x00 };
	private final static int nodelength = 20;
	public static String ISAY = "海天白云 WarPuber";
	public static DefaultListModel onlineModel;
	private static Vector<ChatP2PUI> chatWindows;
	/**
	 * @param ip
	 * @param userName
	 * @param msg
	 * @注释: 是直接写入到聊天窗口还是保存,取决于窗口是否打开
	 */
	public static void addMSG(InetAddress ip, String userName, String msg) { 
		ChatP2PUI chatWindow;
		if ((chatWindow = getOpenWindow(ip)) != null) {
			chatWindow.chat.msgWriter.insertUserMSG(userName, msg);
		}else{
			UserNode user;
			if ((user = getUserNode(ip)) != null) {
				user.addMSG(userName, msg);
			}
		}
	}

	/**
	 * @param udpdata
	 */
	public static void addNewUser(byte[] udpdata, InetAddress ip, int offset) {
		int k = 0;
		byte version = udpdata[k + offset];
		k++;
		String dormitory = new String(udpdata, k + offset, 3);
		k += 3;
		
		// Arrays.copyOf(original, newLength)
		String userName = new String(udpdata, k + offset, udpdata.length - offset - k);
		if (getUserNode(ip) == null) {
			UserNode user = new UserNode(userName.trim(), ip, version, dormitory);
			UserList.add(user);
			onlineModel.addElement(user);
		}
	}

	public static void freshList(byte[] udpdata) throws UnknownHostException {
		if (udpdata[2] == 0) {
			// 刷新的列表
			DataContainer.UserList.clear();
		}
		for (int i = 0; i < udpdata.length; i++) {
			int k = i * nodelength + 4;
			byte[] ip = { udpdata[k], udpdata[k + 1], udpdata[k + 2], udpdata[k + 3] };
			if (ip[0] + ip[1] != 0) {
				InetAddress inetip = InetAddress.getByAddress(ip);
				if (!inetip.equals(myIP)) {
					byte[] VDN = Arrays.copyOfRange(udpdata, k + 4, k + 20);
					addNewUser(VDN, inetip, 0);
				}
			} else {
				return;
			}
		}
	}
	
	public static ChatP2PUI getOpenWindow(InetAddress  ip){
		for (ChatP2PUI ui : chatWindows) {
			if (ui.equals(ip)) {
				return ui;
			}
		}
		return null;
	}

	public static UserNode getUserNode(InetAddress id) {
		if (id.equals(ME.IP)) {
			return ME;
		}
		synchronized (UserList) {
			for (UserNode idp : DataContainer.UserList) {
				if (id.equals(idp.IP)) {
					return idp;
				}
			}
		}
		return null;
		
	}

	public DataContainer() {
		UserList = new LinkedList<UserNode>();
		onlineModel = new DefaultListModel();
		// UserList.add(ME);
		onlineModel.addElement(ME);
		chatWindows = new Vector<ChatP2PUI>();
		try {
			myIP = InetAddress.getLocalHost();
			// 服务器地址，这是海天白云的服务器地址
			// 这个IP地址就是server所在的地址.自己修改....
			serverIP = InetAddress.getByName("202.199.155.13");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void addWindow(ChatP2PUI newWindow) {
		chatWindows.add(newWindow);
	}
	public void deleteWindow(ChatP2PUI newWindow) {
		if (getOpenWindow(newWindow.user.IP) != null) {
			chatWindows.add(newWindow);
		}
	}
}
