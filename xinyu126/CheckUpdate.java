/**
 * 
 */
package xinyu126;
import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import xinyu126.ui.MainUI;
/**
 * @author xinyu126
 *
 */
public class CheckUpdate {
    private class Check extends Thread {
		// ��ʶ,�Ƿ�����µĸ����ļ�
		private boolean isUpdated = false;
		// �������µİ汾
		String netVersion;
		// ���ذ汾�ļ���
		String LocalVerFileName = "xml\\ver.xml";

		// ��ʾ��Ϣ
		


		// �����ļ�
		private void CopyFile(File oldFile, File newFile) {
			FileInputStream in = null;
			FileOutputStream out = null;

			try {
				if (oldFile.exists()) {
					oldFile.delete();
				}
				in = new FileInputStream(newFile);
				out = new FileOutputStream(oldFile);

				byte[] buffer = new byte[1024 * 5];
				int size;
				while ((size = in.read(buffer)) != -1) {
					out.write(buffer, 0, size);
					out.flush();
				}
			} catch (FileNotFoundException ex) {
			} catch (IOException ex) {
			} finally {
				try {
					out.close();
					in.close();
				} catch (IOException ex1) {
				}
			}

		}

		private String getNowVer() {
			// ���ذ汾�ļ�
			File verFile = new File(LocalVerFileName);

			FileReader is = null;
			BufferedReader br = null;

			// ��ȡ���ذ汾
			try {
				is = new FileReader(verFile);

				br = new BufferedReader(is);
				String ver = br.readLine();

				return ver;
			} catch (FileNotFoundException ex) {
				
			} catch (IOException ex) {
				
			} finally {
				// �ͷ���Դ
				try {
					br.close();
					is.close();
				} catch (IOException ex1) {
				}
			}
			return "";
		}

		@Override
		public void run() {
			// �����ļ��汾��ʶURL
			String versionUrl = "http://202.199.155.13/warPuber/version.htm";

			/*
			 * ������ͨ��HTTP����һ��ҳ��,��ȡ�������ϵİ汾�� ����������������ҳ��ֱ�Ӵ�ӡ�� 6.19.1.1
			 * Ȼ�������汾�űȶԱ��صİ汾��,����汾�Ų�ͬ�Ļ�,�ʹ������������µĳ��򲢸������г���
			 */

			URL url = null;
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader netVer = null;

			// ��ȡ�����ϵİ汾��
			try {
				url = new URL(versionUrl);
				is = url.openStream();
				isr = new InputStreamReader(is);

				netVer = new BufferedReader(isr);
				String netVerStr = netVer.readLine();
				String localVerStr = getNowVer();

				if (netVerStr.equals(localVerStr)) {
					isUpdated = false;
				} else {
					showUpdateWindow();					
					isUpdated = true;
					netVersion = netVerStr;
					msg.append("�����°汾,���ڿ�ʼ����...\n");
				}

			} catch (MalformedURLException ex) {
			} catch (IOException ex) {
			} finally {
				// �ͷ���Դ
				try {
					netVer.close();
					isr.close();
					is.close();
				} catch (IOException ex1) {
				}
			}

			// ����汾��ͬ,���������ϵ��ļ�,���±����ļ�
			if (isUpdated) {
				// ������Ҫ�����µ��ļ�
				File oldFile = new File("warPuber.jar");
				// �������������ص��ļ�
				File newFile = new File("temp.jar");

				// �����ϵ��ļ�λ��
				String updateUrl = "http://202.199.155.13/warPuber/warPuber.jar";

				HttpURLConnection httpUrl = null;
				BufferedInputStream bis = null;
				FileOutputStream fos = null;

				try {
					// ��URLͨ��
					url = new URL(updateUrl);
					httpUrl = (HttpURLConnection) url.openConnection();

					httpUrl.connect();

					byte[] buffer = new byte[1024];

					int size = 0;

					is = httpUrl.getInputStream();
					bis = new BufferedInputStream(is);
					fos = new FileOutputStream(newFile);

					msg.append("\n���ڴ������������µĸ����ļ�\n");

					// �����ļ�
					try {
						int flag = 0;
						int flag2 = 0;
						while ((size = bis.read(buffer)) != -1) {
							// ��ȡ��ˢ����ʱ�����ļ�
							fos.write(buffer, 0, size);
							fos.flush();

							// ģ��һ���򵥵Ľ�����
							if (flag2 == 99) {
								flag2 = 0;
								process.setText(process.getText() + ".");
							}
							flag2++;
							flag++;
							if (flag > 99 * 50) {
								flag = 0;
								process.setText("");
							}
						}
					} catch (Exception ex4) {
						System.out.println(ex4.getMessage());
					}

					msg.append("\n�ļ��������\n");

					// �����ص���ʱ�ļ��滻ԭ���ļ�
					CopyFile(oldFile, newFile);
					// �ѱ��ذ汾�ļ�����Ϊ����ͬ��
					UpdateLocalVerFile();

				} catch (MalformedURLException ex2) {
				} catch (IOException ex) {
					msg.append("\n�ļ���ȡ����\n");
				} finally {
					try {
						fos.close();
						bis.close();
						is.close();
						httpUrl.disconnect();
					} catch (IOException ex3) {
					}
				}

				// ����Ӧ�ó���
				try {
					msg.append("\n2�����������Ӧ�ó���\n");
					MainRun.closeUDP();
					Thread.sleep(1000);
					msg.append("\n1�����������Ӧ�ó���\n");
					Thread.sleep(1000);
					msg.append("\n��������Ӧ�ó���\n");
					// Process p =
					File file = new File("temp.jar");
					if (file.exists()) {
						file.delete();
					}
					Runtime.getRuntime().exec("warPuber.exe");

				} catch (InterruptedException ex) {
				} catch (IOException e) {

					e.printStackTrace();
				}

				// �˳����³���
				System.exit(0);

			} else {

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				jf.dispose();
			}
			
		
		}

		private void UpdateLocalVerFile() {
			// �ѱ��ذ汾�ļ�����Ϊ����ͬ��
			FileWriter verOS = null;
			BufferedWriter bw = null;
			try {
				verOS = new FileWriter(LocalVerFileName);

				bw = new BufferedWriter(verOS);
				bw.write(netVersion);
				bw.flush();

			} catch (IOException ex) {
			} finally {
				try {
					bw.close();
					verOS.close();
				} catch (IOException ex1) {
				}
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame jf;
	private JTextArea msg;
	private JLabel process;
    public CheckUpdate() {
    	
        init();
        //���������߳�
		new Check().start();
    }

	private void init() {
		jf = new JFrame();
		jf.setLayout(new BorderLayout());
		JLabel title = new JLabel("���ڼ�������ϵĸ�����Դ...\n");
		jf.add(title, BorderLayout.NORTH);
		msg = new JTextArea();
		jf.add(msg, BorderLayout.CENTER);
		process = new JLabel();
		jf.add(process, BorderLayout.SOUTH);
		// ��������
		jf.setTitle("Auto Update");
		jf.setSize(250, 300);
		
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MainUI.setMiddleLocation(jf);
	}
    private void showUpdateWindow() {
		
		jf.setVisible(true);
    }
}




