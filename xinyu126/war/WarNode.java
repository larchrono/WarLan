/**
 * 魔兽的主机节点（游戏中的“建主”了，也就是建立了什么地图）
 * 解析主机信息
 * 生成一个主机节点，包括地图信息,版本，人数，等等
 */
package xinyu126.war;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Vector;


/**
 * @author xinyu126
 * 
 */
public class WarNode {
	String serverName;
	String serverInfo;
	String mapStr;
	int all;
	int besidescp;
	int people;
	int unknow;
	int version;
	int timeDelay;
	public InetAddress IP = null;
	public boolean checked = true;
	public WarNode(byte[] map, InetAddress IP, int delay) {
		int i = 0;
		if (map[0] != (byte) 0xf7 || map[1] != (byte) 0x30) {
			// 这个不是地图信息！
			return;
		}
		this.IP = IP;
		this.timeDelay = delay;
		// 第八个是版本，1.20(20) 1.21(21)什么的
		version = map[8];
		// 从第二十个开始,32个
		// 游戏信息，大部分是：“当地局域网内游戏（X……”
		byte[] t = new byte[32];
		for (i = 0; i < 32; i++) {
			t[i] = map[i + 20];
		}
		try {
			serverInfo = new String(t, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// 现在开始解析地图路径和主机的名字
		i = 69;
		byte[] mapStr = new byte[64];
		t = new byte[32];
		int v = 0;
		int n = 0;
		boolean flag = true;
		do {
			String mm = "00000000" + Integer.toBinaryString(~map[i]);
			// System.out.println(mm);
			for (int j = 1; j < 8; j++) {
				int c = map[i + j];
				if (c == 0) {
					// 如果遇到0x00表示已经结束了
					break;
				}
				int k = '1' == mm.charAt(mm.length() - j - 1) ? 1 : 0;
				// System.out.println(k);
				
				if (c == 1) {
					// 当遇到第一个 0x01的时候表示地图路径完事了，开始解析主机的名字
					flag = false;
				}

				if (flag) {
					// 加到地图上
					mapStr[v] = (byte) (c - k);
					v++;
				} else {
					// 加到名字上
					// 注意这里：如果名字不够长，这8个字节中不能放下01 01 00这三个字节，则会在后面添上“争霸”这个两个中文“d5
					// f9 b1 d5”，然后另起8个字节，放下01 01 00.这个时候要注意去掉这个“争霸”。防止乱码

					// 
					// TODO 暂时这样处理。知道还有很多问题
					if (c != (byte) 0xd5 && c != (byte) 0xf9 && c != (byte) 0xb1) {
						t[n] = (byte) (c - k);
						n++;
					}
				}				
			}
			i += 8;
		} while (map[i] != 1 && i < map.length - 25);
		this.mapStr = "M" + new String(Arrays.copyOf(mapStr, v));
		serverName = new String(Arrays.copyOf(t, n));
		all = map[map.length - 22];
		unknow = map[map.length - 18];
		people = map[map.length - 14];//
		besidescp = map[map.length - 10];// 这个数是这样计算的：除电脑外的所有位置，包括已经有人的位置，例如总数是10个，加了3个电脑，则这个数就是7，不关那7个位置是否有无人。
		// System.out.println(serverInfo + "  " + serverName + " " + this.mapStr
		// + all + " " + computer);
	}
	public Vector<String> getInfo() {
		Vector<String> vv=new Vector<String>();
		vv.add("1." + version);
		vv.add(IP.getHostAddress());
		vv.add(serverName);
		vv.add(serverInfo);
		vv.add(mapStr);
		vv.add("" + (all - besidescp + people) + "|" + all);
		vv.add(String.valueOf(all - besidescp));
		vv.add("" + timeDelay);
		return vv;
	}
	public void setTimeDelay(int delay) {
		this.timeDelay = delay;
	}
}
