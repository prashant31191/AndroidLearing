package com.example.xmlparser;

public class Song {

	private String id;
	private String title;
	private String artist;
	private String duration;
	private String thumb_url;
	
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getArtist() {
		return artist;
	}
	public String getDuration() {
		return duration;
	}
	public String getThumb_url() {
		return thumb_url;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}
}
