/**
 * 
 */
package xinyu126.war;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import xinyu126.ui.WarUI;

/**
 * @author Administrator
 *
 */
public class Host implements Runnable {
	private Vector<WarNode> vHosts;
	private DefaultTableModel dftm;
	Host(Vector<WarNode> vHosts, DefaultTableModel dftm) {
		this.vHosts = vHosts;
		this.dftm = dftm;
	}

	public void run() {
		boolean clearflag = false;		
		while (WarUI.SHOWHOST && vHosts.size() > 0) {
			int i = 0;
			Vector<String> v;

			
			Iterator<WarNode> it; 
			if (clearflag) {
				it = vHosts.iterator();
				while (it.hasNext()) {
					WarNode warhost = it.next();
					if (warhost.checked) {			
						warhost.checked = false;
					} else {
						it.remove();
					}
				}
				clearflag = false;
			} else {
				clearflag = true;
			}
			
			for (; i < dftm.getRowCount(); i++) {
				dftm.removeRow(0);
			}
			i = 0;
			it = vHosts.iterator();
			while (it.hasNext()) {
				WarNode warhost = it.next();
				v = warhost.getInfo();
				v.add(0, String.valueOf(i));
				dftm.addRow(v);
				i++;
			}
					
			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
