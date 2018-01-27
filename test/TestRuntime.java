/**
 * 
 */
package test;

import java.io.IOException;

/**
 * @author xinyu126
 *
 */
public class TestRuntime {

	/**
	 * @param args
	 *2009-9-29
	 */
	public static void main(String[] args) {
		try {
			// String.format("java %s", "xinyu126.TestOut")
			Runtime.getRuntime().exec("warPuber.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
