/**
 * 
 */
package xinyu126.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import xinyu126.common.UserNode;

/**
 * @author xinyu126
 *
 */
public class UserNodeUI extends JComponent implements ListCellRenderer {
	private static final long serialVersionUID = 1L;
	UserNode user;
	String ip;
	private Color background, foreground;
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		user = (UserNode) value;
		foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
		background = isSelected ? list.getSelectionBackground() : list.getBackground();
		return this;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(150, 40);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(background);
		g.fillRect(2, 2, getWidth() - 2, getHeight() - 2);
		g.setColor(new Color(0xdd, 0x00, 0x00));
		g.drawString(user.userName, 10, 17);
		int l = user.userName.getBytes().length;
		g.setColor(new Color(0x00, 0x00, 0xdd));
		g.drawString("(" + 1.2 + user.version % 10 + ")", 15 + l * 6, 17);
		g.setColor(foreground);
		g.drawString("(" + user.dormitory + ")" + user.getIPstr(), 10, 34);
		g.dispose();
	}
}
