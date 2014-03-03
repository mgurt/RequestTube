package net.infobosccoma.requesttube;

import net.infobosccoma.requesttube.data.PreferedVideosPersistent;
import net.infobosccoma.requesttube.data.PreferedVideosSQLiteHelper;
import net.infobosccoma.requesttube.model.YoutubeVideo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {

	static private final String DEVELOPER_KEY = "AIzaSyAxAobP3CDDdS-lWqThPZv1_His8ctJhTs";
	private YoutubeVideo video;
	private PreferedVideosSQLiteHelper helper;
	private PreferedVideosPersistent conversor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		// Obtenir video
		video = (YoutubeVideo) getIntent().getExtras().get("VIDEO");
		
		// Inicialitzar el reproductor
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(DEVELOPER_KEY, this);
		
		// Connectar a BBDD
		helper = new PreferedVideosSQLiteHelper(this, "RequestTube.db", null, 1);
		conversor = new PreferedVideosPersistent(helper);
	}

	/**
	 * Inicialitzar la reproducció del vídeo seleccionaty
	 */
	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			// Carregar video i posar en pantalla completa
			player.loadVideo(video.getVideoID());
		}
	}
	
	/**
	 * Informar en cas de falla en la reproducció
	 */
	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult error) {

		Toast.makeText(this, "Oh no! " + error.toString(), Toast.LENGTH_LONG)
				.show();
	}
	
	/**
	 * Creació del menú
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}
	
	/**
	 * Accions dels elements del menú
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
		    		finish();
	    		}
	    		break;
	    	case R.id.afegir_preferits:
	    		// Comprovar que hi hagi connexio amb la BBDD
	    		if(helper.getReadableDatabase() != null){
	    			
	    			// Guardar el video i informar
	    			if(!conversor.exist(video.getVideoID())){
	    				conversor.save(video);
			    		Toast.makeText(getApplicationContext(), "Video afegir correctament als preferits", Toast.LENGTH_SHORT).show();
	    			} else{
	    				Toast.makeText(getApplicationContext(), "El video que intenta introduïr existeix a la teva llista de videos preferits", Toast.LENGTH_SHORT).show();
	    			}
	    			
	    		} else {
	    			Log.e("Youtube Player Activity", "Error al guardar l'element a la BBDD");
	    		}
	    		break;
	    	default:
	    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }

}
