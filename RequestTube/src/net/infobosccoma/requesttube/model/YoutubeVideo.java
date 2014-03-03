package net.infobosccoma.requesttube.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class YoutubeVideo implements Serializable {

	private static final long serialVersionUID = 1548335592184202417L;
	private String videoID, title, description, publishedAt, categoria,
			thumbnail;

	public YoutubeVideo(String videoID, String title, String description,
			String publishedAt, String categoria, String thumbnail) {
		this.videoID = videoID;
		this.title = title;
		this.description = description;
		this.publishedAt = publishedAt;
		this.categoria = categoria;
		this.thumbnail = thumbnail;
	}

	public YoutubeVideo() {
	}

	public String getVideoID() {
		return videoID;
	}

	public void setVideoID(String videoID) {
		this.videoID = videoID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * Creació d'un bitmap obtingut d'una font URL
	 * @param src
	 * @return
	 */
	public static Bitmap getBitmapFromURL(String src) {
		try {
			System.out.println(src);
			URL url = new URL(src);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
