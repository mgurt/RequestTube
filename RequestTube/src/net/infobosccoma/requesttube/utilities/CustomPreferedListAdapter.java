package net.infobosccoma.requesttube.utilities;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import net.infobosccoma.requesttube.R;
import net.infobosccoma.requesttube.model.VistaItem;
import net.infobosccoma.requesttube.model.YoutubeVideo;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPreferedListAdapter extends BaseAdapter {
	
	private Activity context;
	private TreeMap<String, Bitmap> imatgesLlista;
	private Cursor dades;
	
	/**
	 * Constructor
	 * @param context de l'aplicació
	 * @param dades amb les dades de la BBDD
	 */
	public CustomPreferedListAdapter(Activity context, Cursor dades){
		super();
		this.context = context;
		imatgesLlista = new TreeMap<String, Bitmap>();
		this.dades = dades;
	}
	
	/**
	 * Sobreescriptura del mètode getCount que indica quantes dades gestiona
	 * l'adaptador.
	 */
	@Override
	public int getCount() {
		return dades.getCount();
	}
	
	/**
	 * Sobreescriptura del mètode getItemId que retorna l'id de l'objecte
	 * que ocupa la posició indicada amb el paràmetre.
	 */
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}
	
	/**
	 * Sobreescriptura que retorna un element del cursos de la llista
	 * que conté l'adapter
	 */
	@Override
	public YoutubeVideo getItem(int position) {
		YoutubeVideo video = new YoutubeVideo();

		// Moure el cursor en la primera posició
		dades.moveToFirst();
		// Extreure les dades del camp de la BBDD
		if(dades.moveToPosition(position)){
			video.setVideoID(dades.getString(0));
			video.setTitle(dades.getString(1));
			video.setDescription(dades.getString(2));
			video.setPublishedAt(dades.getString(3));
			video.setCategoria(dades.getString(4));
			video.setThumbnail(dades.getString(5));
		}
		
		return video;
	}
	
	
	/**
	 * Sobreescriptura del mètode getView per indicar com s'han de mostrar
	 * les dades d'una fila del ListView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View element = convertView;
		// Obtenir el video de la BBDD
		YoutubeVideo video = (YoutubeVideo) getItem(position);
		VistaItem vista;

		// Si no hi ha la vista guardada, crear-la. Sinó utilitzar una anterior
		if (element == null) {
			LayoutInflater inflater = (context.getLayoutInflater());
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
		/// Assignar la informació als components
		
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
	 * Mètode que assigna el cursor amb les dades
	 * @param dades
	 */
	public void setAdapter(Cursor dades){
		this.dades = dades;
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
