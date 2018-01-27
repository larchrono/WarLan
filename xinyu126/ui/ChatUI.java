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
	// ���������б����ʾ��
	// ��дJLabel

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
	

			{// ��Ϣ��
				jspMSG = new JScrollPane();
				add(jspMSG, BorderLayout.CENTER);
				{
					jtpMSG = new JTextPane();
					jspMSG.setViewportView(jtpMSG);
					jtpMSG.setText("��л�������ԣ�����������飬�뵽bbs������\nԴ���뿪�š���ӭһ����롣\n");
					jtpMSG.setEditable(false);
				}
				
			}


			// ������Ϣ
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
					JButton jbClear = new JButton("���");
					jbClear.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jtpMSG.setText("");
						}
					});
					jbSend = new JButton();					
					jbSend.setText("����");
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
