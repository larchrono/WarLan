/**
 * 
 */
package xinyu126.ui;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author xinyu126
 *
 */
public class LoginUI extends JPanel {
	private static final long serialVersionUID = 1L;
	public JTextField jtUserName;
	public JComboBox jcbVersion;
	public JComboBox jcbqu;// 南北区
	public JComboBox jcblou;// 楼号
	public JButton jbLogin;
	public JLabel errorInfo;
	public LoginUI() {
		{
			JPanel jp = new JPanel();
			jp.setPreferredSize(new java.awt.Dimension(200, 30));
			jtUserName = new JTextField();

			JLabel jLabel1 = new JLabel("昵称");

			jtUserName.setPreferredSize(new java.awt.Dimension(73, 22));
			jp.add(jLabel1);
			jp.add(jtUserName);
			add(jp);
		}
		{
			JPanel jp = new JPanel();	
			jp.setPreferredSize(new java.awt.Dimension(200, 35));
			JLabel jLabel2 = new JLabel();
			jp.add(jLabel2);
			jLabel2.setText("版本");
			ComboBoxModel jcbVesionModel = new DefaultComboBoxModel(new String[] { "1.20", "1.21", "1.22", "1.23",
					"1.24", "1.25" });
			jcbVersion = new JComboBox();
			jp.add(jcbVersion);
			jcbVersion.setModel(jcbVesionModel);
			add(jp);
		}
		{
			JPanel jp = new JPanel();
			jp.setPreferredSize(new java.awt.Dimension(200, 35));
			JLabel jLabel = new JLabel();
			jp.add(jLabel);
			jLabel.setText("宿舍:");
			int all = 18;
			String[] num = new String[all];
			for (int i = 1; i <= all; i++) {
				num[i - 1] = String.valueOf(i);
			}
			ComboBoxModel jcbVesionModel = new DefaultComboBoxModel(new String[] { " ", "s", "n" });
			ComboBoxModel jcbVesionMode2 = new DefaultComboBoxModel(num);
			jcbqu = new JComboBox();
			jcbqu.setModel(jcbVesionModel);
			jp.add(jcbqu);
			
			JLabel jLabe2 = new JLabel();
			jp.add(jLabe2);
			jLabe2.setText("区");
			jcblou = new JComboBox();
			jcblou.setModel(jcbVesionMode2);
			jp.add(jcblou);
			JLabel jLabe3 = new JLabel();
			jp.add(jLabe3);
			add(jp);
		}
		{
			JPanel jp = new JPanel();	
			jp.setPreferredSize(new java.awt.Dimension(200, 35));
			jbLogin = new JButton("login");
			jp.add(jbLogin);
			add(jp);
		}
		{
			JPanel jp = new JPanel();	
			jp.setPreferredSize(new java.awt.Dimension(300, 30));
			errorInfo = new JLabel("                                                                     ");
			jp.add(errorInfo);
			add(jp);
		}
		// setPreferredSize(new java.awt.Dimension(115, 266));

	}
}
