package net.infobosccoma.requesttube.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Classe que manté els components de la vista encapsulats per tal que la ListView gasti menys 
 * recursos del dispositiu
 * @author Marc Gurt
 *
 */
public class VistaItem {
	
	private TextView tvTitleVideo, tvDescripcioVideo, tvVideoId, tvPublicacioVideo, tvCategoriaVideo, tvThumbnailVideo;
	private ImageView imgVideo;
	
	/**
	 * Constructor
	 * @param tvTitle - Titol del video
	 * @param tvDescripcio - Descripcio del video
	 * @param tvVideoId - Identificador del video
	 * @param tvPublicacioVideo - Data i hora de publicació del video
	 * @param tvCategoriaVideo - Categoria del video
	 * @param tvThumbnailVideo - Adreça URL amb la imatge amb miniatura del videos
	 * @param imgVideo - Direcció URL que apunta a la miniatura del video
	 */
	public VistaItem(TextView tvTitle, TextView tvDescripcio, TextView tvVideoId, TextView tvPublicacioVideo, TextView tvCategoriaVideo, TextView tvThumbnailVideo, ImageView imgVideo) {
		this.tvTitleVideo = tvTitle;
		this.tvDescripcioVideo = tvDescripcio;
		this.tvVideoId = tvVideoId;
		this.tvPublicacioVideo = tvPublicacioVideo;
		this.tvCategoriaVideo = tvCategoriaVideo;
		this.tvThumbnailVideo = tvThumbnailVideo;
		this.imgVideo = imgVideo;
	}
	
	/* GETTERS I SETTERS */
	
	public TextView getTvVideoId() {
		return tvVideoId;
	}

	public void setTvVideoId(TextView tvVideoId) {
		this.tvVideoId = tvVideoId;
	}
	
	public TextView getTvPublicacioVideo() {
		return tvPublicacioVideo;
	}

	public void setTvPublicacioVideo(TextView tvPublicacioVideo) {
		this.tvPublicacioVideo = tvPublicacioVideo;
	}
	
	public ImageView getImgVideo() {
		return imgVideo;
	}

	public void setImgVideo(ImageView imgVideo) {
		this.imgVideo = imgVideo;
	}

	public TextView getTvTitleVideo() {
		return tvTitleVideo;
	}

	public void setTvTitleVideo(TextView tvTitleVideo) {
		this.tvTitleVideo = tvTitleVideo;
	}

	public TextView getTvDescripcioVideo() {
		return tvDescripcioVideo;
	}

	public void setTvDescripcioVideo(TextView tvDescripcioVideo) {
		this.tvDescripcioVideo = tvDescripcioVideo;
	}

	public TextView getTvCategoriaVideo() {
		return tvCategoriaVideo;
	}

	public void setTvCategoriaVideo(TextView tvCategoriaVideo) {
		this.tvCategoriaVideo = tvCategoriaVideo;
	}

	public TextView getTvThumbnailVideo() {
		return tvThumbnailVideo;
	}

	public void setTvThumbnailVideo(TextView tvThumbnailVideo) {
		this.tvThumbnailVideo = tvThumbnailVideo;
	}
	
	
}
