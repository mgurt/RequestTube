package net.infobosccoma.requesttube.utilities;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import net.infobosccoma.requesttube.R;
import net.infobosccoma.requesttube.model.VistaItem;
import net.infobosccoma.requesttube.model.YoutubeVideo;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends ArrayAdapter<YoutubeVideo> {

	private ArrayList<YoutubeVideo> youtubeVideos;
	private TreeMap<String, Bitmap> imatgesLlista;
	private VistaItem vista;

	public VideoListAdapter(Activity context, ArrayList<YoutubeVideo> youtubeVideos) {
		super(context, android.R.layout.simple_spinner_item, youtubeVideos);
		this.youtubeVideos = youtubeVideos;
		imatgesLlista = new TreeMap<String, Bitmap>();
	}

	/**
	 * Carrega les vistes cada vegada que el sistema Android demana per mostrar-les
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View element = convertView;

		// Si no hi ha la vista guardada, crear-la. Sinó utilitzar una anterior
		if (element == null) {
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			element = inflater.inflate(R.layout.item_video_list, null);
			
			 
			vista = new VistaItem(
					(TextView) element.findViewById(R.id.tvTitolVideo),
					(TextView) element.findViewById(R.id.tvDescripcioVideo),
					(TextView) element.findViewById(R.id.tvIdVideo),
					(TextView) element.findViewById(R.id.tvPublicacioVideo),
					(TextView) element.findViewById(R.id.tvCategoriaVideo),
					(TextView) element.findViewById(R.id.tvThumbnailVideo),
					(ImageView) element.findViewById(R.id.imgVideo));

			// setTag permet guardar referencies a qualsevol objecte o
			// col·leccions
			element.setTag(vista);
		} else {
			vista = (VistaItem) element.getTag();
		}
		// Assignar la informació als components
		YoutubeVideo video = youtubeVideos.get(position);
		// Títol
		vista.getTvTitleVideo().setText(video.getTitle());
		// Descripció
		vista.getTvDescripcioVideo().setText(video.getDescription());
		// Identificador video
		vista.getTvVideoId().setText(video.getVideoID());
		// Categoria
		vista.getTvCategoriaVideo().setText(video.getCategoria());
		// Publicació
		vista.getTvPublicacioVideo().setText(video.getPublishedAt());
		// Thumbnail (URL)
		vista.getTvThumbnailVideo().setText(video.getThumbnail());
		// Thumbnail (imatge)
		try {
			// Si la imatge està al TreeMap, carregar-la
			if(imatgesLlista.containsKey(video.getThumbnail())){
				vista.getImgVideo().setImageBitmap(imatgesLlista.get(video.getThumbnail()));
				// Sino descarregar-la d'Internet i guardar-la al TreeMap
			} else {
				Bitmap bitmap = new connexionHTTP().execute(video.getThumbnail().replace("\\/", "/")).get();
				imatgesLlista.put(video.getThumbnail(), bitmap);
				vista.getImgVideo().setImageBitmap(bitmap);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return element;
	}
	
	/**
	 * Classe asincrona que realitza la connexió als servidors de Youtube
	 * @author marc
	 *
	 */
	private class connexionHTTP extends AsyncTask<String, Integer, Bitmap> {
				
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap bitmap = null;
			for (String url : urls) {
				try {
					bitmap = YoutubeVideo.getBitmapFromURL(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return bitmap;
		}
	}
}
