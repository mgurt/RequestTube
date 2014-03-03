package net.infobosccoma.requesttube.data;

import net.infobosccoma.requesttube.model.YoutubeVideo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Classe conversora d'objectes YoutubeVideo a BD
 * @author Marc Gurt Dot
 */
public class PreferedVideosPersistent {
	
	private PreferedVideosSQLiteHelper helper;
	 
	/**
	 * Consructor per defecte
	 */
	public PreferedVideosPersistent() {
	}
 
	/**
	 * Constructor amb paràmetres
	 * @param helper l'ajudant de la BD de Titulars
	 */
	public PreferedVideosPersistent(PreferedVideosSQLiteHelper helper) {
		this.helper = helper;
	}
 
	/**
	 * Desa un nou titular a la taula
	 * @param titular l'objecte a desar
	 * @return l'id del nou titular desat
	 */
	public long save(YoutubeVideo video) {		
		long index = -1;
		// Agafar l'objecte base de dades en mode escriptura
		SQLiteDatabase db = helper.getWritableDatabase();
		// Es crea un nou objecte diccionari per indicar els valors a afegir
		ContentValues dades = new ContentValues();
		
		// Afegir els valors de l'element a guardar al diccionari
		dades.put("videoID", video.getVideoID());
		dades.put("title", video.getTitle());
		dades.put("description", video.getDescription());
		dades.put("publishedAt", video.getPublishedAt());
		dades.put("category", video.getCategoria());
		dades.put("thumbnail", video.getThumbnail());
		
		try{
			index = db.insertOrThrow(PreferedVideosSQLiteHelper.NOM_TAULA, 
					null, 
					dades);
			
			Log.i(PreferedVideosSQLiteHelper.NOM_TAULA, "Valor afegit correctament");
		} catch(Exception e){
			Log.e(PreferedVideosSQLiteHelper.NOM_TAULA, e.getMessage());
		}
    	return index;
	}	
	
	public boolean exist(String id){
		boolean resp = false;
		SQLiteDatabase db = helper.getWritableDatabase();

		String selectQuery = "SELECT  * FROM " + PreferedVideosSQLiteHelper.NOM_TAULA 
				 + " WHERE videoID = '" + id + "'";
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c.getCount() > 0){
			resp = true;
		}
		c.close();
		return resp;
	}
 
	/**
	 * Retorna un cursor amb totes les dades de la taula
	 * @return
	 */
	public Cursor getAll() {
		// Obtenir un objecte per llegir la taula de la BBDD
		SQLiteDatabase db = helper.getReadableDatabase();
		
		 
		
		return db.query(true, 
				PreferedVideosSQLiteHelper.NOM_TAULA, 
				new String[]{"videoID","title","description","publishedAt","category","thumbnail"}, // Prepared Query WHERE --> Ex. "WHERE title = ?"
				null, // Valors de la Prepared query --> new String = {"titol_video"}
				null, // Group By (String)
				null, // Having (String)
				null, // Order By (String)
				null, // Limit (String)
				null); // No es fa servir...
	}
 
	/**
	 * Esborra el titular passat per paràmetre
	 * @param t el titular a esborrar
	 * @return la quantitat de titulars eliminats
	 */
	public boolean remove(YoutubeVideo video) {		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		return db.delete(
				PreferedVideosSQLiteHelper.NOM_TAULA, 
				"videoID = ?", // Prepared Statement --> "WHERE title = ?"
				new String[]{video.getVideoID()} ) > 0; // new String[] = {"titol_video"}
	}
	
	/**
	 * Esborra tots els titulars de la taula
	 * @return 
	 */
	public boolean removeAll() {		
		// obtenir l'objecte BD en mode escriptura
				SQLiteDatabase db = helper.getWritableDatabase();
		 
				return db.delete(PreferedVideosSQLiteHelper.NOM_TAULA,
						null, 
						null ) > 0;
	}
	
}
