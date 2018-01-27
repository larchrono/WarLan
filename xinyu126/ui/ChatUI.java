package xinyu126.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

public class ChatUI extends JPanel {
	// 用于在线列表的显示。
	// 重写JLabel

	private static final long serialVersionUID = 1L;

	public JTextPane jtpMSG;
	private JScrollPane jspMSG;
	public JTextPane jtWriteMSG;
	public JButton jbSend;
	

	public ChatUI() {
		initGUI();
	}


	private void initGUI() {
		try {
						
			setLayout(new BorderLayout());
	

			{// 消息区
				jspMSG = new JScrollPane();
				add(jspMSG, BorderLayout.CENTER);
				{
					jtpMSG = new JTextPane();
					jspMSG.setViewportView(jtpMSG);
					jtpMSG.setText("感谢你加入测试，如有问题或建议，请到bbs发帖。\n源代码开放。欢迎一起加入。\n");
					jtpMSG.setEditable(false);
				}
				
			}


			// 发送消息
			{
				JPanel jpSendMSG = new JPanel();
				BoxLayout jpSendMSGLayout = new BoxLayout(jpSendMSG, javax.swing.BoxLayout.X_AXIS);
				jpSendMSG.setLayout(jpSendMSGLayout);
				add(jpSendMSG, BorderLayout.SOUTH);
				jpSendMSG.setPreferredSize(new java.awt.Dimension(429, 64));
				jpSendMSG.setBorder(new LineBorder(new java.awt.Color(0, 0, 200), 1, false));
				{
					JScrollPane jScrollPane1 = new JScrollPane();
					jpSendMSG.add(jScrollPane1);
					{
						jtWriteMSG = new JTextPane();
						jScrollPane1.setViewportView(jtWriteMSG);
					}
				}
				{
					JPanel jp = new JPanel();
					jp.setLayout(new BoxLayout(jp, javax.swing.BoxLayout.Y_AXIS));
					JButton jbClear = new JButton("清空");
					jbClear.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jtpMSG.setText("");
						}
					});
					jbSend = new JButton();					
					jbSend.setText("发送");
					jp.add(jbClear);
					jp.add(jbSend);
					jpSendMSG.add(jp);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	


}
