package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffect {
	private String path;
	private String fileName;
	
	 public SoundEffect(String fileName) {
       path = "lib/" + fileName;

       this.fileName = fileName;
	 }
	 
	 public URI getSongURI() {
       File file = new File(path);
       return file.toURI();
	 }
	 
	 public void playSound() {
		 Media media = new Media(getSongURI().toString());
		    MediaPlayer mediaPlayer = new MediaPlayer(media);
		    mediaPlayer.play();
	 }
	 
	

//package model;
//
//import java.io.File;
//import java.io.Serializable;
//import java.net.URI;
//
//    public class Song implements Serializable {
//
//        private String path;
//        private String title;
//        private String artist;
//        private String length;
//        private String fileName;

//        public Song(String fileName, String title, String artist, String length) {
//            path = "songfiles/" + fileName;
//
//            this.fileName = fileName;
//            this.title = title;
//            this.artist = artist;
//            this.length = length;
//        }
//
//        public URI getSongURI() {
//            File file = new File(path);
//            return file.toURI();
//        }
//
//        public String getSongPath() {
//            return fileName;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getArtist() {
//            return artist;
//        }
//
//        public String getLength() {
//            return length;
//        }
//    }

}
