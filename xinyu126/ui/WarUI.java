/**
 * 
 */
package xinyu126.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import xinyu126.common.DataContainer;

/**
 * @author Administrator
 *
 */
public class WarUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable jtable;
	public static boolean SHOWHOST = true;
	public JTextField jtfISay;
	public JButton jbSave;
	public DefaultTableModel df;
	public JButton jbPuber;
	WarUI() {

		initUI();
	}

	private void initUI() {
		df = new DefaultTableModel();
		df.addColumn("ID");// 0
		df.addColumn("版本");// 1
		df.addColumn("主机IP");// 2
		df.addColumn("主机名");// 3
		df.addColumn("游戏名");// 4
		df.addColumn("地图");// 5
		df.addColumn("人数");// 6
		df.addColumn("电脑数");// 7
		df.addColumn("延时");// 8
		jtable = new JTable(df);		
		TableColumnModel cm = jtable.getColumnModel();
		cm.getColumn(0).setPreferredWidth(20);
		cm.getColumn(1).setPreferredWidth(20);
		cm.getColumn(2).setPreferredWidth(75);
		cm.getColumn(3).setPreferredWidth(40);
		cm.getColumn(4).setPreferredWidth(120);
		cm.getColumn(5).setPreferredWidth(140);
		cm.getColumn(6).setPreferredWidth(30);
		cm.getColumn(7).setPreferredWidth(20);
		cm.getColumn(8).setPreferredWidth(20);
		JScrollPane jsp = new JScrollPane(jtable);
		
		jtfISay = new JTextField(DataContainer.ISAY);
		jtfISay.setPreferredSize(new java.awt.Dimension(150, 20));
		jbSave = new JButton("修改");
		jbSave.setActionCommand("info");
		jbPuber = new JButton("向外界发布我的主机");
		jbPuber.setActionCommand("puber");
		JPanel jp = new JPanel();
		jp.add(new JLabel("我的主机我做主:"));
		jp.add(jtfISay);
		jp.add(jbSave);
		jp.add(new JLabel("这里修改的信息可以在建主机的时候看到."));
		jp.add(jbPuber);
		jtable.setFillsViewportHeight(true);
		this.setLayout(new BorderLayout());
		this.add(jsp, BorderLayout.CENTER);
		add(jp, BorderLayout.SOUTH);
	}
}
