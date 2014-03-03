package net.infobosccoma.requesttube;

import java.util.ArrayList;

import net.infobosccoma.requesttube.data.PreferedVideosPersistent;
import net.infobosccoma.requesttube.data.PreferedVideosSQLiteHelper;
import net.infobosccoma.requesttube.model.YoutubeRequest;
import net.infobosccoma.requesttube.model.YoutubeVideo;
import net.infobosccoma.requesttube.utilities.CustomHttpClient;
import net.infobosccoma.requesttube.utilities.VideoListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class VideoListActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener{

	private ListView requestList;
	private ArrayList<YoutubeVideo> videos;
	private YoutubeRequest youtubeRequest;
	private PreferedVideosSQLiteHelper helper;
	private SQLiteDatabase db;
	private PreferedVideosPersistent conversor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videolist);
		
		// Assignar el botó UP a l'ActionBar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Recuperar la ListView i assignar els events d'acció
		requestList = (ListView) findViewById(R.id.requestList);
		requestList.setOnItemClickListener(this);
		requestList.setOnItemLongClickListener(this);

		// Connectar-se la BBDD
		helper = new PreferedVideosSQLiteHelper(this, "RequestTube.db", null, 1);
		db = helper.getWritableDatabase();
		conversor = new PreferedVideosPersistent(helper);

		// Capturar la consulta realitzada
		youtubeRequest = (YoutubeRequest) getIntent().getExtras()
				.getSerializable("Request");

		// Fer la petició HTTP per a descarregar els vídeos, llistes de
		// reproducció o canals
		new connexionHTTP().execute(youtubeRequest.getRequest());
		
	}

	/**
	 * Crear les opcions de l'ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	/**
	 * Gestionar les accions
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
	    	case R.id.preferits:
	    		if(conversor.getAll().getCount() == 0){
	    			Toast.makeText(this, "No hi ha videos a preferits", Toast.LENGTH_SHORT).show();
	    		} else {
	    			Intent it = new Intent(this, PreferedVideosListActivity.class);
		    		startActivity(it);
	    		}
    	}
    	return super.onOptionsItemSelected(item);
    }

	/**
	 * Classe asincrona que realitza la connexió als servidors de Youtube
	 * @author marc
	 *
	 */
	private class connexionHTTP extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... urls) {
			String resposta = null;
			for (String url : urls) {
				try {
					resposta = CustomHttpClient.executarHttpGet(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return resposta;
		}

		/**
		 * Crear una llista de videos amb la informació obtinguda de la petició
		 */
		@Override
		protected void onPostExecute(String resposta) {
			
			ArrayList<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
			try {
				JSONObject jsonObject = new JSONObject(resposta);
				if(jsonObject == null){
					Log.i("Video List Activity", "No s'ha trobat videos");
					Toast.makeText(VideoListActivity.this, "No s'han trobat coincidencies.\nTorna-ho intentar.", Toast.LENGTH_LONG).show();
				} else {
					videos = obtenirInformacioVideos(jsonObject);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Assignar l'adaptador
			requestList.setAdapter(new VideoListAdapter(VideoListActivity.this,
					videos));
		}
		
		/**
		 * Extracció de la informació del JSON obtingut de la petició a Youtube,
		 * creació de la llista amb la informació dels videos.
		 * @param json
		 * @return llista de videos
		 */
		private ArrayList<YoutubeVideo> obtenirInformacioVideos(JSONObject json) {
			videos = new ArrayList<YoutubeVideo>();
			try {
				// Catch elements <item> i recorre'ls
				JSONArray jsonItems = json.getJSONArray("items");

				for (int i = 0; i < jsonItems.length(); i++) {
					// Obtenir id
					JSONObject id = jsonItems.getJSONObject(i).getJSONObject("id");
					// Obtenir snippet
					JSONObject snippet = jsonItems.getJSONObject(i).getJSONObject("snippet");
					
					// Afegir els vídeos a la llista
					videos.add(new YoutubeVideo(
							id.getString("videoId"),
							snippet.getString("title"),
							snippet.getString("description"),
							tractarDataHora(snippet.getString("publishedAt")), 
							youtubeRequest.getCategoria(), 
							snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url")));
				}
			} catch (JSONException e) {
				Log.e("JSONException", e.getMessage());
			}
			return videos;
		}

	}
	
	private String tractarDataHora(String dataHora) {
		String dataTractada = new String();
		
		// Any
		String any = dataHora.substring(0, 4);
		// Mes
		String mes = dataHora.substring(5, 7);
		// Dia
		String dia = dataHora.substring(8, 10);
		// Hores
		String hora = dataHora.substring(11, 13);
		// Minuts
		String minuts = dataHora.substring(14, 16);
		
		dataTractada = hora + ":" + minuts + " - " + dia + "/" + mes + "/" + any;
		
		return dataTractada;
	}

	/**
	 * 
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Intent it = new Intent(this, YoutubePlayerActivity.class);		
		Bundle b = new Bundle();
		b.putSerializable("VIDEO", videos.get(position));
		it.putExtras(b);
		startActivity(it);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, final int position, long id) {
		AlertDialog.Builder dialeg = new AlertDialog.Builder(this);
		
		dialeg.setTitle("Afegir a preferits");
		dialeg.setMessage("Vols afegir el vídeo a la teva llista de preferits?");
		
		dialeg.setPositiveButton("Afegir", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Si s'ha establert connexió amb la BBDD
				if(db != null){
					if(!conversor.exist(videos.get(position).getVideoID())){
						conversor.save(videos.get(position));
						Toast.makeText(getApplicationContext(), "Video afegir correctament als preferits", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "El video que intenta introduïr existeix a la teva llista de videos preferits", Toast.LENGTH_SHORT).show();
					}
				}			
			}
		});
		dialeg.setNegativeButton("Cancel·lar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}				
			}
		});
		dialeg.create();
		dialeg.show();
		return true;
	}
}
