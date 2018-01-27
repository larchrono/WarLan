/**
 * 
 */
package xinyu126.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author xinyu126
 *
 */
public class AudioPlay {
	public static void full(){
		play("au\\wenzi.au");
	}
	public static void gameStart(){
		play("au\\gamestart.au");
	}
	public static void newgame() {
		play("au\\newgame.au");
	}
	/**
	 * @param fileName
	 *            play audio
	 */
	private static void play(String fileName) {
		try {
			FileInputStream aufile = new FileInputStream(fileName);
			AudioStream audiofile = new AudioStream(aufile);
			AudioPlayer.player.start(audiofile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
