/**
 * 
 */
package xinyu126.ui;

import java.net.InetAddress;

import javax.swing.JFrame;

import xinyu126.chat.Chat2P;
import xinyu126.common.UserNode;

/**
 * @author xinyu126
 *
 */
public class ChatP2PUI extends JFrame { 
	private static final long serialVersionUID = 1L;
	public Chat2P chat;
	public UserNode user;
	public ChatP2PUI(UserNode user) {
		setTitle("”Î" + user.userName + "(" + user.getIPstr() + "¡ƒÃÏ÷–)");
		ChatUI chatui = new ChatUI();
		add(chatui);
		setSize(400, 300);
		MainUI.setMiddleLocation(this);
		setVisible(true);
		chat = new Chat2P(chatui, user);
	}

	public boolean equals(InetAddress IP) {
		if (IP.equals(this.user.IP)) {
				return true;
			}		
		return false;
	}
	
}
