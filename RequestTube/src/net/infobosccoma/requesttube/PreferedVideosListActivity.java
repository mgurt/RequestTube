package net.infobosccoma.requesttube;

import net.infobosccoma.requesttube.data.PreferedVideosPersistent;
import net.infobosccoma.requesttube.data.PreferedVideosSQLiteHelper;
import net.infobosccoma.requesttube.model.YoutubeVideo;
import net.infobosccoma.requesttube.utilities.CustomPreferedListAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PreferedVideosListActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener{
	
	private int position;
	private ListView preferedVideosList;
	private PreferedVideosSQLiteHelper helper;
	private Cursor cursorPreferedVideos;
	private PreferedVideosPersistent conversor;
	private CustomPreferedListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefered_list);
		
		// Crear l'ActionBar
		ActionBar action = getSupportActionBar();
		action.setDisplayHomeAsUpEnabled(true);
		
		// Obtenir ListView
		preferedVideosList = (ListView) findViewById(R.id.preferedList);
		// Crear un objecte que crea la connexió amb la BBDD
		helper = new PreferedVideosSQLiteHelper(this, "RequestTube.db", null, 1);
		// Obtenir la BBDD en un objecte
		SQLiteDatabase db = helper.getWritableDatabase();
		conversor = new PreferedVideosPersistent(helper);
		
		// Si la connexió amb la BBDD s'ha establert correctament
		if(db != null){
			// actualitzar la llista
			refreshData();
			// Tancar la connexió amb la BBDD
			db.close();
		}
		
		// Intanciar events als elements de la llista
		preferedVideosList.setOnItemClickListener(this);
		preferedVideosList.setOnItemLongClickListener(this);
	}
	
	/**
     * Torna a executar la consulta i a enllaçar les dades
     */
	private void refreshData() {
		cursorPreferedVideos = conversor.getAll();
		adapter = new CustomPreferedListAdapter(this, cursorPreferedVideos);
		adapter.notifyDataSetChanged();
		
		preferedVideosList.setAdapter(adapter);
	}
	
	/**
	 * Crear el menú d'opcions
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.prefered_list, menu);

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Intent it = new Intent(this, YoutubePlayerActivity.class);
		
		// Crear un nou video amb les dades del video seleccionat
		YoutubeVideo video = new YoutubeVideo(
				((TextView) view.findViewById(R.id.tvIdVideo)).getText().toString(),
				((TextView) view.findViewById(R.id.tvTitolVideo)).getText().toString(),
				((TextView) view.findViewById(R.id.tvDescripcioVideo)).getText().toString(),
				((TextView) view.findViewById(R.id.tvPublicacioVideo)).getText().toString(),
				((TextView) view.findViewById(R.id.tvCategoriaVideo)).getText().toString(),
				((TextView) view.findViewById(R.id.tvThumbnailVideo)).getText().toString()
		);
		
		// Crear un nou bundle, posar l'objecte dins un Intent i iniciar l'activitat YoutubePlayer
		Bundle b = new Bundle();
		b.putSerializable("VIDEO", video);		
		it.putExtras(b);
		startActivity(it);		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
		this.position = position;
		
		crearDialegEliminarPreferit();
		return true;
	}
	
	/**
	 * Creació del dialeg que apareix quan es realitza un LongClick a un element de la 
	 * llista, donant la posibilitat d'eliminar un element.
	 */
	private void crearDialegEliminarPreferit() {
		AlertDialog.Builder dialeg = new AlertDialog.Builder(this);
		
		dialeg.setTitle("Eliminar video");
		dialeg.setMessage("Estas segur que vols eliminar el video "+ 
		adapter.getItem(position).getTitle() +"?");
		dialeg.setPositiveButton("Eliminar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Eliminar element
				if(conversor.remove(adapter.getItem(position))){
					
					// Tornar a obtenir els elements del cursor i actualitzar elements de la llista
					adapter.setAdapter(conversor.getAll());
					adapter.notifyDataSetChanged();
					
					// Informar
					Toast.makeText(PreferedVideosListActivity.this, "Video eliminat correctament", Toast.LENGTH_SHORT).show();
				} else {
					Log.e("PREFERED PREFERED VIDEOS", "Error al eliminar el video" + adapter.getItem(which).getTitle());
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
	}

}
