/**
 * 
 */
package xinyu126.chat;

import java.io.IOException;
import java.net.DatagramPacket;

import xinyu126.common.DataContainer;
import xinyu126.common.UDPSender;
import xinyu126.common.UserNode;
import xinyu126.common.UserNode.Msgs;
import xinyu126.ui.ChatUI;

/**
 * @author xinyu126
 *
 */
public class Chat2P extends AbstractChat {
	private UserNode user;
	/**
	 * @param chatUI
	 * @param dataContainer
	 */
	public Chat2P(ChatUI chatUI, UserNode users) {
		super(chatUI);
		this.user = users;
		init();
	}

	private void init(){
		for(Msgs msgs:user.msgList){
			msgWriter.insertUserMSG(msgs.userName, msgs.msg, msgs.time);
		}
	}


	/* (non-Javadoc)
	 * @see xinyu126.chat.AbstractChat#sendMSG(byte[])
	 */
	@Override
	public void sendMSG(String msg) {
		byte[] msgByte = ("ms|" + DataContainer.ME.userName + "|" + msg).getBytes();
		DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length, null, 6114);
		try {
		
				packet.setAddress(user.IP);
			
				UDPSender.udpSocket.send(packet);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	

}
