package net.infobosccoma.requesttube.model;

import java.util.ArrayList;

public class PreferedYoutubeVideos {
	
	private static ArrayList<YoutubeVideo> preferedVideos;
	
	public PreferedYoutubeVideos(){
		preferedVideos = new ArrayList<YoutubeVideo>();
	}

	public static ArrayList<YoutubeVideo> getPreferedVideos() {
		return preferedVideos;
	}

	public static void setPreferedVideos(ArrayList<YoutubeVideo> preferedVideos) {
		PreferedYoutubeVideos.preferedVideos = preferedVideos;
	}
	
}
