/**
 * ħ�޵������ڵ㣨��Ϸ�еġ��������ˣ�Ҳ���ǽ�����ʲô��ͼ��
 * ����������Ϣ
 * ����һ�������ڵ㣬������ͼ��Ϣ,�汾���������ȵ�
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
			// ������ǵ�ͼ��Ϣ��
			return;
		}
		this.IP = IP;
		this.timeDelay = delay;
		// �ڰ˸��ǰ汾��1.20(20) 1.21(21)ʲô��
		version = map[8];
		// �ӵڶ�ʮ����ʼ,32��
		// ��Ϸ��Ϣ���󲿷��ǣ������ؾ���������Ϸ��X������
		byte[] t = new byte[32];
		for (i = 0; i < 32; i++) {
			t[i] = map[i + 20];
		}
		try {
			serverInfo = new String(t, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// ���ڿ�ʼ������ͼ·��������������
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
					// �������0x00��ʾ�Ѿ�������
					break;
				}
				int k = '1' == mm.charAt(mm.length() - j - 1) ? 1 : 0;
				// System.out.println(k);
				
				if (c == 1) {
					// ��������һ�� 0x01��ʱ���ʾ��ͼ·�������ˣ���ʼ��������������
					flag = false;
				}

				if (flag) {
					// �ӵ���ͼ��
					mapStr[v] = (byte) (c - k);
					v++;
				} else {
					// �ӵ�������
					// ע�����������ֲ���������8���ֽ��в��ܷ���01 01 00�������ֽڣ�����ں������ϡ����ԡ�����������ġ�d5
					// f9 b1 d5����Ȼ������8���ֽڣ�����01 01 00.���ʱ��Ҫע��ȥ����������ԡ�����ֹ����

					// 
					// TODO ��ʱ��������֪�����кܶ�����
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
		besidescp = map[map.length - 10];// ���������������ģ��������������λ�ã������Ѿ����˵�λ�ã�����������10��������3�����ԣ������������7��������7��λ���Ƿ������ˡ�
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
