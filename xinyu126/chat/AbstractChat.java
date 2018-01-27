/**
 * 
 */
package xinyu126.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.JTextPane;

import xinyu126.common.MsgWriter;
import xinyu126.ui.ChatUI;



/**
 * @author Administrator
 *
 */
public abstract class AbstractChat {

	class sendBListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			send();
		}
	}	


	
	private static final long serialVersionUID = 1L;



	
	public MsgWriter msgWriter;
	private JTextPane jtWriteMSG;


	public AbstractChat(ChatUI chatUI) {

		jtWriteMSG = chatUI.jtWriteMSG;

		msgWriter = new MsgWriter(chatUI.jtpMSG);
		chatUI.jbSend.addActionListener(new sendBListener());
		chatUI.jtWriteMSG.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == 10) {
					send();
				}
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
	};
	/**
	 * @param userName
	 * @param msg
	 * @param time
	 *            2009-9-27
	 */
	public void insertMSG(String userName, String msg, Date time) {
		// TODO Auto-generated method stub

	}

	private void send() {
		String msg = jtWriteMSG.getText();
		if (msg == null || "".equals(msg)) {
			msgWriter.insertSysMSG("不能发生空白");
			return;
		}
		msgWriter.insertMyMSG(msg.trim());
		jtWriteMSG.setText("");		
		sendMSG(msg);
		jtWriteMSG.requestFocus();
	}
	public abstract void sendMSG(String msg);


}
