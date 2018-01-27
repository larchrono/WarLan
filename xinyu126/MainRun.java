/**
 * 
 */
package xinyu126;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import xinyu126.common.DataContainer;
import xinyu126.common.UDPSender;
import xinyu126.common.UserNode;
import xinyu126.common.XMLParser;
import xinyu126.common.XMLWriter;
import xinyu126.ui.LoginUI;
import xinyu126.ui.MainUI;


/**
 * @author xinyu126
 * 
 */
public class MainRun {
	class bListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			login();
		}
	}
	public static void main(String[] args) {
		
		new MainRun().init();
		new CheckUpdate();
		
	}
	LoginUI loginui;
	JFrame jframe;
	private static DatagramSocket chatSocket;
	private static DatagramSocket warSocket;
	public static void closeUDP(){
		chatSocket.close();
		warSocket.close();
	}
	private InetAddress myip;

	MainRun() {
		jframe = new JFrame();
		loginui = new LoginUI();
		jframe.add(loginui);
		jframe.setSize(300, 250);
		 MainUI.setMiddleLocation(jframe);
		// loginui.setVisible(true);
		
		jframe.setVisible(true);
		File file = new File("xml\\user.xml");
		if (file.exists()) {
			XMLParser xml = new XMLParser();
			xml.Parser(file);
			loginui.jtUserName.setText(xml.getuserName());
			loginui.jcbVersion.setName(xml.getVersion());
			loginui.jcbqu.setSelectedItem(xml.getqu());
			loginui.jcblou.setSelectedItem(xml.getlou());
		}
		try {
			chatSocket = new DatagramSocket(6114);
			new UDPSender(chatSocket);
			warSocket = new DatagramSocket(6113);
			myip = InetAddress.getLocalHost();
		} catch (SocketException e) {
			loginui.errorInfo.setText("你已经运行了一个程序！");
			loginui.jbLogin.setEnabled(false);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	void init() {
		loginui.jbLogin.addActionListener(new bListener());
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		

	}
	private void login() {
		String userName = loginui.jtUserName.getText().trim();
		String version = loginui.jcbVersion.getSelectedItem().toString();
		String lou = loginui.jcblou.getSelectedItem().toString().trim();
		String qu = loginui.jcbqu.getSelectedItem().toString().trim();
		if (userName == null || "".equals(userName)) {
			loginui.errorInfo.setText("名字不能为空，或者只有空格");
			return;
		}
		if (userName.getBytes().length < 3 || userName.getBytes().length > 12) {
			loginui.errorInfo.setText("名字的长度是大于2个字母小于12个字母，中文减半");
			return;
		}
		if (qu == "") {
			loginui.errorInfo.setText("请选择宿舍(南区?北区?)");
			return;
		}
		if (lou == "") {
			loginui.errorInfo.setText("请选择宿舍楼");
			return;
		}

		
		int t = Integer.parseInt(version.substring(3));
		 DataContainer.warSearch[8] = (byte) (0x14 + t);
		byte bversion = (byte) t;
		loginui.errorInfo.setText("登陆中……");
		DataContainer.ME = new UserNode(userName, myip, bversion, qu + lou);
		new DataContainer();		 
		saveUserInfo(userName, version, qu, lou);
		jframe.setVisible(false);
		jframe.dispose();
		new MainUI(warSocket, chatSocket).setVisible(true);
	}

	

	private void saveUserInfo(String userName, String version, String qu, String lou) {

		File file = new File("xml\\user.xml");
		if (file.exists()) {
			XMLWriter xml = new XMLWriter();
			Document doc = xml.getRootElement();
			Element root = doc.createElement("war");
			Element user = doc.createElement("user");
			Text username = doc.createTextNode(userName);
			user.appendChild(username);
			root.appendChild(user);

			Element E = doc.createElement("version");
			Text T = doc.createTextNode(version);
			E.appendChild(T);
			root.appendChild(E);

			E = doc.createElement("qu");
			T = doc.createTextNode(qu);
			E.appendChild(T);
			root.appendChild(E);

			E = doc.createElement("lou");
			T = doc.createTextNode(lou);
			E.appendChild(T);
			root.appendChild(E);

			doc.appendChild(root);
			xml.save2File(doc, file);
		}
	}
	// Process proc = Runtime.getRuntime().exec(String.format("java %s",
	// "FilteredJList"));


}
