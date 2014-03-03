package net.infobosccoma.requesttube.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PreferedVideosSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String NOM_TAULA = "prefered_videos";
	
	/**
	 * Consulta que crea la taula de la base de dades
	 */
	private final String CREATE_PREFERED_TABLE = 
			"CREATE TABLE "+ NOM_TAULA 
			+ " ("
			+ "videoID TEXT PRIMARY KEY, "
			+ "title TEXT, "
			+ "description TEXT, "
			+ "publishedAt TEXT, "
			+ "category TEXT, "
			+ "thumbnail TEXT)";

	/**
	 * Constructor de la classe
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public PreferedVideosSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * Mètode que s'executa la primera vegada que s'instancia la classe
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PREFERED_TABLE);
	}

	/**
	 * Mètode que s'executa cada vegada que s'actualitza la versio de la BBDD
	 * Cal tenir en compte que si es fa un canvi significatiu, cal guardar la dades
	 * abans de eliminar l'estructura anterior.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE prefered_videos");
		
		db.execSQL(CREATE_PREFERED_TABLE);
	}

}
