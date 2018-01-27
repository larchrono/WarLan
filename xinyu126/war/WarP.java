/**
 * 
 */
package xinyu126.war;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextField;

import xinyu126.common.AudioPlay;
import xinyu126.common.DataContainer;
import xinyu126.common.MsgWriter;
import xinyu126.common.UserNode;
import xinyu126.common.XMLParser;
import xinyu126.ui.WarUI;

/**
 * @author xinyu126
 * 
 */
public class WarP {

	class SearchMe implements Runnable {
		private DatagramPacket packet;
		

		SearchMe(byte[] udps) {
			packet = new DatagramPacket(udps, 16, myIP, 6112);
		}
		public void run() {
			
			try {
				while (searchFlag) {
					Thread.sleep(5 * 1000);
					
						
						udpServer.send(packet);
						if (timedelay < 5) {
						timedelay++;
					} else if (ICHost) {
						// 如果我建主了.并且5秒钟的延迟时间,说明游戏开始,或者取消
						hosttime1 = 1;
						hosttime2 = 0;
						ICHost = false;
						AudioPlay.gameStart();
					}
					
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
	class SearchWar implements Runnable {

		public void run() {
			
			try {
				while (!udpServer.isClosed() && searchFlag) {
					searchWar();
					Thread.sleep(5 * 1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	class Server implements Runnable {

		public void run() {
			byte[] mapdata = null;
			byte[] newmapdata = null;
			long begain = 0L;
			while (searchFlag) {
				try {
					udpServer.receive(udpPacket);
					recvIP = udpPacket.getAddress();
					int length = udpPacket.getLength();
					int port = udpPacket.getPort();
					byte[] udpdata = Arrays.copyOf(udpPacket.getData(), length);
					// 自己机器发来的，是我建了主机
					if (recvIP.equals(myIP)) {
						// 判断是不是游戏地图信息
						if (udpdata[1] == (byte) 0x30) {
						// System.out.println("我建主了~~");
						timedelay = 3;
						if (newmapdata == null) {
							// 初始化替换数据(魔兽信息显示)
							UserNode me = DataContainer.ME;
							byte[] temp = (me.dormitory + "-" + me.userName + DataContainer.ISAY + "\0\0\0\0\0\0\0\0\0\0\0\0\0")
									.getBytes("utf-8");
							newmapdata = new byte[31];
							for (int i = 0; i < 31; i++) {
								newmapdata[i] = temp[i];
							}
						}
							mapdata = udpdata;
							// 替换魔兽显示信息
							for (int i = 0; i < 31; i++) {
								mapdata[20 + i] = newmapdata[i];
							}
						WarNode newHost = new WarNode(mapdata, myIP, 0);
						
						// 看看主机列表中的第一个是不是自己

						if (vWarHosts.size() > 0) {
							WarNode oldHost = vWarHosts.get(0);
							if (oldHost.IP.equals(myIP)) {
								// oldHost = newHost;
								vWarHosts.remove(0);
								vWarHosts.add(newHost);
							} else {
								vWarHosts.add(0, newHost);
							}
						
						} else {
							vWarHosts.add(newHost);
						}
							
						//
						if (!hostRun.isAlive()) {
							hostRun = new Thread(new Host(vWarHosts, warui.df));
							hostRun.start();
						}
						// 发布
							publishWar();
						// 音乐
							if (mapdata[mapdata.length - 14] == mapdata[mapdata.length - 10]) {
								if (hosttime2 < hosttime1) {
								AudioPlay.full();
								hosttime2++;
							}
						}
						}
					}
					else {
						// 别人机器来的。

						// 如果是从端口6113来的消息
						DatagramPacket packet;
						if (port == 6113) {
							if ('m' == udpdata[0] && 'e' == udpdata[1]) {
								// 他说他建立了主机。
								// 开始计时
								begain = System.currentTimeMillis();
								// 我就说 have a look
								packet = new DatagramPacket("lk".getBytes(), 2, recvIP, 6113);
								udpServer.send(packet);
							} else if ('l' == udpdata[0] && 'k' == udpdata[1]) {
								packet = new DatagramPacket(mapdata, mapdata.length, recvIP, port);
								udpServer.send(packet);
							} else if (udpdata[0] == (byte) 0xf7 && udpdata[1] == (byte) 0x30) {
								// 收到了地图信息。解析
								int delay = (int) (System.currentTimeMillis() - begain);
								WarNode newHost = new WarNode(udpdata, recvIP, delay);
								addAndUpdateHost(newHost);
							}
						} else if (port == 6112) {
							if (udpdata.length < 100 && mapdata != null) {
								packet = new DatagramPacket(mapdata, mapdata.length, recvIP, port);
								udpServer.send(packet);
							} 
						}
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	private static final long serialVersionUID = 1L;
	private DatagramSocket udpServer;
	private boolean searchFlag = true;
	private int timedelay = 5;
	private int hosttime1 = 1, hosttime2 = 0;
	private boolean ICHost = false;
	private InetAddress myIP, recvIP;
	private DatagramPacket udpPacket;
	private byte[] databuf;
	// private DefaultTableModel dftm;
	private Vector<WarNode> vWarHosts;
	private Thread hostRun;
	private MsgWriter msgWriter;
	private WarUI warui;
	private LinkedList<InetAddress> pubips;
	private ActionListener al;
	private JButton jbPuber;
	private JTextField jtIsay;
	private boolean puberflag = false;
	public WarP(final WarUI warui, MsgWriter msgWriter, DatagramSocket socket) {
		this.warui = warui;
		this.msgWriter = msgWriter;
		udpServer = socket;
		vWarHosts = new Vector<WarNode>();
		databuf = new byte[1024];
		udpPacket = new DatagramPacket(databuf, 1024);
		jbPuber = warui.jbPuber;
		jtIsay = warui.jtfISay;
		al = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("puber".equals(cmd)) {
					if (puberflag == true) {
						puberflag = false;
						jbPuber.setText("人够多了,发布取消了");
					} else {
						puberflag = true;
						jbPuber.setText("正在发布中````");
					}
					
					pubips = new XMLParser().ParseIP(new File("xml\\IP.xml"));
				} else if ("info".equals(cmd)) {
					DataContainer.ISAY = jtIsay.getText();
				}
			}

		};
		warui.jbPuber.addActionListener(al);
		warui.jbSave.addActionListener(al);
		starUDP();		
	}

	// 添加，或者更新主机信息，并且启动显示主机的线程
	private void addAndUpdateHost(WarNode newHost) {
		
		if (newHost.IP != null) {
			// 找到了，则更新
			int i;
			if ((i = findHost(newHost)) > -1) {
				vWarHosts.remove(i);
				vWarHosts.insertElementAt(newHost, i);
			// 找不到，则添加
			} else {
				vWarHosts.add(newHost);
				msgWriter.insertString("刚有人建了主机~~~~", MsgWriter.SysAttr);
				AudioPlay.newgame();
			}
			if (!hostRun.isAlive()) {
				hostRun = new Thread(new Host(vWarHosts, warui.df));
				hostRun.start();
			}
		}
	}

	// broadcast UDP
	private void broadcastWar(byte[] bytes, int port) {
		Iterator<UserNode> it = DataContainer.UserList.iterator();
		try {
		while (it.hasNext()) {
			InetAddress ip = it.next().IP;
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, port);
			
				udpServer.send(packet);
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
	}

	private int findHost(WarNode newHost){
		Iterator<WarNode> it = vWarHosts.iterator();
		int i = 0;
		while (it.hasNext()) {
			WarNode warHost = it.next();
			if (newHost.IP.equals(warHost.IP)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private void publishWar() {
		broadcastWar("me".getBytes(), 6113);
		broadcastWar(DataContainer.warPublish, 6112);
		if (puberflag && pubips != null) {
			try {
				DatagramPacket packet = new DatagramPacket(DataContainer.warPublish, 16, null, 6112);
				for (InetAddress ip : pubips) {
					packet.setAddress(ip);

					udpServer.send(packet);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void searchWar(){
		broadcastWar(DataContainer.warSearch, 6112);
	}

	private void starUDP() {
		try {
			myIP = InetAddress.getLocalHost();
			new Thread(new SearchMe(DataContainer.warSearch)).start();
			new Thread(new Server()).start();			
			hostRun = new Thread(new Host(vWarHosts, warui.df));
			hostRun.start();
		} catch (UnknownHostException e) {
			// TODO
			e.printStackTrace();
		}

	}



	
}
