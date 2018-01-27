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
		// 标识,是否存在新的更新文件
		private boolean isUpdated = false;
		// 保存最新的版本
		String netVersion;
		// 本地版本文件名
		String LocalVerFileName = "xml\\ver.xml";

		// 显示信息
		


		// 复制文件
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
			// 本地版本文件
			File verFile = new File(LocalVerFileName);

			FileReader is = null;
			BufferedReader br = null;

			// 读取本地版本
			try {
				is = new FileReader(verFile);

				br = new BufferedReader(is);
				String ver = br.readLine();

				return ver;
			} catch (FileNotFoundException ex) {
				
			} catch (IOException ex) {
				
			} finally {
				// 释放资源
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
			// 更新文件版本标识URL
			String versionUrl = "http://202.199.155.13/warPuber/version.htm";

			/*
			 * 这里是通过HTTP访问一个页面,以取得网络上的版本号 比如这里就是在这个页面直接打印出 6.19.1.1
			 * 然后把这个版本号比对本地的版本号,如果版本号不同的话,就从网络上下载新的程序并覆盖现有程序
			 */

			URL url = null;
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader netVer = null;

			// 读取网络上的版本号
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
					msg.append("发现新版本,现在开始更新...\n");
				}

			} catch (MalformedURLException ex) {
			} catch (IOException ex) {
			} finally {
				// 释放资源
				try {
					netVer.close();
					isr.close();
					is.close();
				} catch (IOException ex1) {
				}
			}

			// 如果版本不同,下载网络上的文件,更新本地文件
			if (isUpdated) {
				// 本地需要被更新的文件
				File oldFile = new File("warPuber.jar");
				// 缓存网络上下载的文件
				File newFile = new File("temp.jar");

				// 网络上的文件位置
				String updateUrl = "http://202.199.155.13/warPuber/warPuber.jar";

				HttpURLConnection httpUrl = null;
				BufferedInputStream bis = null;
				FileOutputStream fos = null;

				try {
					// 打开URL通道
					url = new URL(updateUrl);
					httpUrl = (HttpURLConnection) url.openConnection();

					httpUrl.connect();

					byte[] buffer = new byte[1024];

					int size = 0;

					is = httpUrl.getInputStream();
					bis = new BufferedInputStream(is);
					fos = new FileOutputStream(newFile);

					msg.append("\n正在从网络上下载新的更新文件\n");

					// 保存文件
					try {
						int flag = 0;
						int flag2 = 0;
						while ((size = bis.read(buffer)) != -1) {
							// 读取并刷新临时保存文件
							fos.write(buffer, 0, size);
							fos.flush();

							// 模拟一个简单的进度条
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

					msg.append("\n文件下载完成\n");

					// 把下载的临时文件替换原有文件
					CopyFile(oldFile, newFile);
					// 把本地版本文件更新为网络同步
					UpdateLocalVerFile();

				} catch (MalformedURLException ex2) {
				} catch (IOException ex) {
					msg.append("\n文件读取错误\n");
				} finally {
					try {
						fos.close();
						bis.close();
						is.close();
						httpUrl.disconnect();
					} catch (IOException ex3) {
					}
				}

				// 启动应用程序
				try {
					msg.append("\n2秒后重启启动应用程序\n");
					MainRun.closeUDP();
					Thread.sleep(1000);
					msg.append("\n1秒后重启启动应用程序\n");
					Thread.sleep(1000);
					msg.append("\n重启启动应用程序\n");
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

				// 退出更新程序
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
			// 把本地版本文件更新为网络同步
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
        //启动更新线程
		new Check().start();
    }

	private void init() {
		jf = new JFrame();
		jf.setLayout(new BorderLayout());
		JLabel title = new JLabel("正在检查网络上的更新资源...\n");
		jf.add(title, BorderLayout.NORTH);
		msg = new JTextArea();
		jf.add(msg, BorderLayout.CENTER);
		process = new JLabel();
		jf.add(process, BorderLayout.SOUTH);
		// 窗体设置
		jf.setTitle("Auto Update");
		jf.setSize(250, 300);
		
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MainUI.setMiddleLocation(jf);
	}
    private void showUpdateWindow() {
		
		jf.setVisible(true);
    }
}




