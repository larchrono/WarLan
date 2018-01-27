/**
 * 
 */
package xinyu126.chat;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;

import xinyu126.common.DataContainer;
import xinyu126.common.UserNode;
import xinyu126.ui.ChatP2PUI;
import xinyu126.ui.UserListUI;

/**
 * @author xinyu126
 *
 */
public class UserList {
	private JList list;
	private MouseListener mouseListener;
	public UserList(UserListUI listui) {
		this.list = listui.jlist;
		mouseListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				System.out.print("click");
				if (e.getClickCount() > 1) {
					ChatTo(e.getPoint());
				}
			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		};
		list.addMouseListener(mouseListener);
	}
	/**
	 * @param point
	 */
	protected void ChatTo(Point point) {
	int index = list.locationToIndex(point);
		UserNode user = (UserNode) DataContainer.onlineModel.get(index);
		System.out.print(index);
		if (DataContainer.getOpenWindow(user.IP) == null) {
			new ChatP2PUI(user);
		}
	}
}
