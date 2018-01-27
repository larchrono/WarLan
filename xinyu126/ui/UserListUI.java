/**
 * 在线列表的图型界面
 */
package xinyu126.ui;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xinyu126.common.DataContainer;

/**
 * @author xinyu126
 *
 */
public class UserListUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public JList jlist;
	UserListUI() {
		jlist = new JList();
		jlist.setCellRenderer(new UserNodeUI());
		jlist.setModel(DataContainer.onlineModel);
		JScrollPane jsp = new JScrollPane(jlist);
		setLayout(new BorderLayout());
		add(jsp);
	}
}
