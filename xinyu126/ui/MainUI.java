/**
 * 这就是一个窗口..这里生产出其他的窗口..这个类是所有类的调用中心.
 * 
 * 我用的模式基本上是:
 *    1--实例化 UI();
 *    2--实例化 功能代码(传递UI);
 * 
 */
package xinyu126.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import xinyu126.chat.Chat;
import xinyu126.chat.UserList;
import xinyu126.common.MainP;
import xinyu126.war.WarP;

/**
 * @author xinyu126
 *
 */
public class MainUI extends JFrame {
	private static final long serialVersionUID = 1L;
	public static void setMiddleLocation(Window window) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = window.getSize();
		window.setLocation((screensize.width - windowSize.width) / 2, (screensize.height - windowSize.height) / 2);
	}
	private MenuItem mi;
	private java.awt.TrayIcon ti;
	public String title;
	private PopupMenu popupMenu;
	/**
	 * @param chatSocket
	 * @param warSocket
	 */
	public MainUI(DatagramSocket warSocket, DatagramSocket chatSocket) {
		String title = "";
		setTitle(title);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setMiddleLocation(this);
		mini();
		JTabbedPane jtp = new JTabbedPane();
		{
			ChatUI chatui=new ChatUI();//聊天图形窗口
			WarUI warui=new WarUI();//魔兽主机图形窗口
			UserListUI userlistui=new UserListUI();//用户列表图形界面
			JPanel jp = new JPanel();
			jp.setLayout(new BorderLayout());
			jp.add(chatui);
			jp.add(userlistui, BorderLayout.EAST);
			jtp.add(jp, "聊天");
			jtp.add(warui, "魔兽");
			Chat chat = new Chat(chatui);// 由图形界面做参数,实例化功能代码
			new UserList(userlistui);
			new WarP(warui, chat.msgWriter, warSocket);
			new MainP(chat.msgWriter, chatSocket);
		}
		add(jtp);
		// pack();
	}

	private void jButtonMouseClicked(ActionEvent evt) {
    	String actionCommand = evt.getActionCommand();
		if (actionCommand.equals("regain")) {
			// 恢复主窗口
			this.setVisible(true);
		} else if (actionCommand.equals("out")) {
			System.exit(0);
		}
		// System.out.println(evt);
	}
	private void mini() {
		class MyActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				jButtonMouseClicked(e);
			}

		}
		ActionListener al = new MyActionListener();
		{
			
			title = "魔兽联机程序";
			mi = new MenuItem("退出");
			mi.setActionCommand("out");
			mi.addActionListener(al);
			popupMenu = new PopupMenu("退出");// 设置通知栏右键菜单
			popupMenu.add(mi);
		}
		try {
			if (java.awt.SystemTray.isSupported()) {// 判断当前平台是否支持系统托盘
				java.awt.SystemTray st = java.awt.SystemTray.getSystemTray();
				Image image = Toolkit.getDefaultToolkit().getImage("icon/war.jpg");// 定义托盘图标的图片
				ti = new java.awt.TrayIcon(image);
				ti.setToolTip(title);
				ti.setPopupMenu(this.popupMenu); // 为托盘添加右键菜单
				st.add(ti);
				ti.setActionCommand("regain");// 恢复窗口
				ti.addActionListener(al);
			}
		} catch (Exception e) {
		}

	}


}
