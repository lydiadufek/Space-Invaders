/**
 * Purpose: This file holds the sound effect functionality. A media player
 * 			is created when a sound needs to be played and an URI is created
 * 			of the file string at the same time.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffect {
	private String path;
	private String fileName;
	private Media media;
	private MediaPlayer mediaPlayer;
	
	 public SoundEffect(String fileName) {
       path = "lib/" + fileName;
       this.fileName = fileName;
	 }
	 
	 public URI getSongURI() {
       File file = new File(path);
       return file.toURI();
	 }
	 
	 public void playSound() {
		 media = new Media(getSongURI().toString());
		    mediaPlayer = new MediaPlayer(media);
		    mediaPlayer.play();
	 }
	 
	 public void stopSound() {
		 if (mediaPlayer != null) {
			 mediaPlayer.stop();
		 }
	 }

}
