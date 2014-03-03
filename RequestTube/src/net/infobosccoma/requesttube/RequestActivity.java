package net.infobosccoma.requesttube;

import net.infobosccoma.requesttube.data.PreferedVideosPersistent;
import net.infobosccoma.requesttube.data.PreferedVideosSQLiteHelper;
import net.infobosccoma.requesttube.model.YoutubeRequest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RequestActivity extends ActionBarActivity {

	private EditText consulta;
	private Spinner spinnerCategoriaId, spinnerNumItems, spinnerOrderBy;
	private ArrayAdapter<CharSequence> adapterCategoriaId, adapterOrderBy;
	private ArrayAdapter<Integer> adapterNumItems;
	private PreferedVideosSQLiteHelper helper;
	private PreferedVideosPersistent conversor;
	private SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Crear la interficie, omplir i inicialitzar components
		setContentView(R.layout.activity_request);
		consulta = (EditText) findViewById(R.id.etConsulta);
		omplirSpinners();

		// Connectar-se amb la BBDD
		helper = new PreferedVideosSQLiteHelper(this, "RequestTube.db", null, 1);
		if (db == null) {
			db = helper.getWritableDatabase();
			conversor = new PreferedVideosPersistent(helper);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.prefered:
			if (conversor.getAll().getCount() == 0) {
				Toast.makeText(this, "No hi ha videos a preferits",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent it = new Intent(this, PreferedVideosListActivity.class);
				startActivity(it);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Executar la consulta i enviar la consulta a la pròxima activitat
	 * 
	 * @param view
	 */
	public void requestTube(View view) {
		if (consulta.getText().toString().trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "Escrigui una consulta",
					Toast.LENGTH_SHORT).show();
			;
		} else {
			startActivity(crearRequest());
		}
	}

	/**
	 * Omplir els spinners del formulari
	 */
	private void omplirSpinners() {
		Integer[] numItems = new Integer[50];
		for (Integer i = 0; i < 50; i++) {
			numItems[i] = i + 1;
		}

		// Crear adaptadors
		adapterCategoriaId = ArrayAdapter.createFromResource(this,
				R.array.categoryId, android.R.layout.simple_spinner_item);
		adapterCategoriaId
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapterOrderBy = ArrayAdapter.createFromResource(this, R.array.orderBy,
				android.R.layout.simple_spinner_item);
		adapterOrderBy
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapterNumItems = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, numItems);
		adapterNumItems
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Capturar spinners
		spinnerCategoriaId = (Spinner) findViewById(R.id.spinnerCategoriaId);
		spinnerNumItems = (Spinner) findViewById(R.id.spinnerNumItems);
		spinnerOrderBy = (Spinner) findViewById(R.id.spinnerOrderBy);

		// Instanciar adaptadors
		spinnerCategoriaId.setAdapter(adapterCategoriaId);
		spinnerOrderBy.setAdapter(adapterOrderBy);
		spinnerNumItems.setAdapter(adapterNumItems);
		
		// Posar valor per defecte
		spinnerOrderBy.setSelection(adapterOrderBy.getPosition("Rellevància Youtube"));
		spinnerNumItems.setSelection(adapterNumItems.getPosition(20));
	}

	/**
	 * Crear la intenció amb les dades de la consulta a Youtube
	 * 
	 * @return
	 */
	private Intent crearRequest() {
		// Crear instancia
		YoutubeRequest ytRequest = new YoutubeRequest(consulta.getText()
				.toString(), "ES", spinnerCategoriaId.getSelectedItem()
				.toString(), spinnerOrderBy.getSelectedItem().toString(),
				Integer.valueOf(spinnerNumItems.getSelectedItem().toString()));

		Intent it = new Intent(this, VideoListActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("Request", ytRequest);
		it.putExtras(b);
		return it;
	}

}
