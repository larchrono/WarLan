/**
 * 
 */
package xinyu126.chat;

import xinyu126.common.DataContainer;
import xinyu126.common.UDPSender;
import xinyu126.ui.ChatUI;

/**
 * @author xinyu126
 *
 */
public class Chat extends AbstractChat {
	UDPSender udpSender;
	/**
	 * @param chatUI
	 * @param dataContainer
	 */
	public Chat(ChatUI chatUI) {
		super(chatUI);
		
	}
	@Override
	public void sendMSG(String msg) {
		byte[] msgByte = ("msall|" + DataContainer.ME.userName + "|" + msg).getBytes();
		UDPSender.sendToAll(msgByte);

	}

}
