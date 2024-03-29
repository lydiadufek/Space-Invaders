/**
 * Purpose: This file holds the sound effect functionality. A media player
 * 			is created when a sound needs to be played and a URI is created
 * 			of the file string at the same time.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import java.io.File;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffect {
	private String path;
	private Media media;
	private MediaPlayer mediaPlayer;
	
	/**
	 * Constructor for the sound effect 
	 * @param fileName the name of the sound file
	 */
	public SoundEffect(String fileName) {
       path = "lib/" + fileName;
	 }
	 
	/**
	 * Returns the song's URI 
	 * @return the song's URI
	 */
	public URI getSongURI() {
       File file = new File(path);
       return file.toURI();
	 }
	 
	/**
	 * Plays the sound 
	 */
	public void playSound() {
		 media = new Media(getSongURI().toString());
		 mediaPlayer = new MediaPlayer(media);
		 mediaPlayer.play();
	 }
	 
	/**
	 * Stops any sound being played 
	 */
	public void stopSound() {
		 if (mediaPlayer != null) {
			 mediaPlayer.stop();
		 }
	 }

}
