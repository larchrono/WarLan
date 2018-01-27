/**
 * 
 */
package test;


/**
 * @author Administrator
 *
 */
public class TestDll {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestDll();

	}
	TestDll() {
		// System.out.print(new String(vv()));
		vv();
	}
	void vv() {
		byte[] v = { (byte) 0xf7, 0x30 };
		System.out.print(v[0] == (byte) 0xf7);
	}


}
