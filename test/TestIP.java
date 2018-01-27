/**
 * 
 */
package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xinyu126
 * 
 */
public class TestIP {
	public static void main(String[] args) {
		try {
			InetAddress ip1 = InetAddress.getByName("10.10.177.10");
			System.out.println(ip1.toString());
			System.out.println(ip1.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
