/**
 * 
 */
package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author xinyu126
 *
 */
public class TestAU {
	public static void main(String[] args) {
		try {
			FileInputStream fileau = new FileInputStream("au\\newgame.au");
			AudioStream audio = new AudioStream(fileau);
			AudioPlayer.player.start(audio);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
