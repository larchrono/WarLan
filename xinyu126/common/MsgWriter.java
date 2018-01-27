/**
 * 
 */
package xinyu126.common;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * @author xinyu126
 *
 */
public class MsgWriter {
	private JTextPane jtpMSG;
	private String myName = DataContainer.ME.userName;
	public static SimpleAttributeSet attr;
	public static SimpleAttributeSet SysAttr, MeAttr, UserAttr;
	public MsgWriter(JTextPane jtp) {
		this.jtpMSG = jtp;
		attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, new Color(0x006311));
		SysAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(SysAttr, new Color(0xff0000));
		MeAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(MeAttr, new Color(0xcc33cc));
		UserAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(UserAttr, new Color(0x0033cc));		
	}

	public void insertMyMSG(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		String date = sdf.format(new Date());
		insertString(myName + " " + date + ": ˵", MeAttr);
		insertString(str, attr);
	}
	public void insertString(String str) {
		insertString(str, attr);
	}

	public void insertString(String str, AttributeSet attr) {
		Document doc = jtpMSG.getDocument();
		try {
			doc.insertString(doc.getLength(), str + "\n", attr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		jtpMSG.setCaretPosition(doc.getLength());
	}
	public void insertSysMSG(String str) {
		insertString(str,SysAttr);
	}
	public void insertUserMSG(String userName, String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		String date = sdf.format(new Date());
		insertString(userName + " " + date + ": ˵", UserAttr);
		insertString(str, attr);
	}
	public void insertUserMSG(String userName, String str, Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		String date = sdf.format(time);
		insertString(userName + " " + date + ": ˵", UserAttr);
		insertString(str, attr);
	}
}
