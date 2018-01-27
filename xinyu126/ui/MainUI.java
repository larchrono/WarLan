/**
 * �����һ������..���������������Ĵ���..�������������ĵ�������.
 * 
 * ���õ�ģʽ��������:
 *    1--ʵ���� UI();
 *    2--ʵ���� ���ܴ���(����UI);
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
			ChatUI chatui=new ChatUI();//����ͼ�δ���
			WarUI warui=new WarUI();//ħ������ͼ�δ���
			UserListUI userlistui=new UserListUI();//�û��б�ͼ�ν���
			JPanel jp = new JPanel();
			jp.setLayout(new BorderLayout());
			jp.add(chatui);
			jp.add(userlistui, BorderLayout.EAST);
			jtp.add(jp, "����");
			jtp.add(warui, "ħ��");
			Chat chat = new Chat(chatui);// ��ͼ�ν���������,ʵ�������ܴ���
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
			// �ָ�������
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
			
			title = "ħ����������";
			mi = new MenuItem("�˳�");
			mi.setActionCommand("out");
			mi.addActionListener(al);
			popupMenu = new PopupMenu("�˳�");// ����֪ͨ���Ҽ��˵�
			popupMenu.add(mi);
		}
		try {
			if (java.awt.SystemTray.isSupported()) {// �жϵ�ǰƽ̨�Ƿ�֧��ϵͳ����
				java.awt.SystemTray st = java.awt.SystemTray.getSystemTray();
				Image image = Toolkit.getDefaultToolkit().getImage("icon/war.jpg");// ��������ͼ���ͼƬ
				ti = new java.awt.TrayIcon(image);
				ti.setToolTip(title);
				ti.setPopupMenu(this.popupMenu); // Ϊ��������Ҽ��˵�
				st.add(ti);
				ti.setActionCommand("regain");// �ָ�����
				ti.addActionListener(al);
			}
		} catch (Exception e) {
		}

	}


}
