package net.infobosccoma.requesttube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BannerActivity extends Activity {
	
	Intent itRequestActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner);
		// Cargar animaciones
		final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
		final ImageView imgBienvenida = (ImageView) findViewById(R.id.imgBenvinguda);
		imgBienvenida.startAnimation(alpha);
		
		 itRequestActivity = new Intent(this, RequestActivity.class);
		
		// Vamos a declarar un nuevo thread
		Thread timer = new Thread() {
			// El nuevo Thread exige el metodo run
			public void run() {
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					// Si no puedo ejecutar el sleep muestro el error
					Log.e("ERROR DE THREAD", "No s'ha executat el Thread");
				} finally {
					startActivity(itRequestActivity);
					finish();
				}
			}
		};
		// ejecuto el thread
		timer.start();
	}
}
