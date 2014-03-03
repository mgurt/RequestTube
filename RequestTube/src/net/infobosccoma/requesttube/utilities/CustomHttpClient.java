package net.infobosccoma.requesttube.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class CustomHttpClient {
	
	// Atributs de la classe
	public static final int HTTP_TIMEOUT = 30 * 1000; // Milisegons
	private static HttpClient myHttpClient; // Instancia al nostra HttpClient
	
	/**
	  * Agafa una instancia simple del nostre objecte HttpClient.
	  * 
	  * @return un objecte HttpClient amb els seus parametres de configuracio
	  */
	
	private static HttpClient getHttpClient() {
		// Si no hi ha instancia creada a myHttpClient, crear-la i definir els temps d'espera
		if (myHttpClient == null){
			myHttpClient = new DefaultHttpClient();
			
			final HttpParams params = myHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return myHttpClient;
	}
	
	 /**
	  * Realitza una peticio via Post a una URL especifica amb uns parametres especificats
	  * 
	  * @param url
	  *            L'adreça web on s'ha de fer la peticio Post
	  * @param postParameters
	  *            Els parametres a passar amb la sol·licitud
	  * @return El resultat de la peticio
	  * @throws Excepcio
	  */
	
	public static String executarHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		
		try {
			// Crear el client
			HttpClient client = getHttpClient();
			// Crear una solicitud post i afegir els par�metres
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(formEntity);
			// Executar consulta al servidor web
			HttpResponse response = client.execute(request);
			// Llegir del servidor
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			// Construir objectes per tractar la resposta
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String newLine = System.getProperty("line.separator");
			// Llegir i tancar l'Stream
			while((line = in.readLine()) != null){
				sb.append(line + newLine);
			}
			in.close();
			// Convertir a String i retornar la resposta
			String result = sb.toString();
			return result;
		}
		catch(Exception e){
			Log.e("Executar HTTP", e.getMessage() + " | "+ e.getCause());
			return null;
		}
		finally {
			if (in != null){
				try {
					in.close();
				}
				catch (IOException e){
					Log.e("Error en la comunicaci� via HTTP", e.toString());
					e.printStackTrace();
				}
			}
		}
		
	}

	
	 /**
	  * Realitza una petici� HTTP GET a una URL especificada
	  * 
	  * @param url
	  *            L'adre�a web on fer la petici�
	  * @return El resultat de la petici�
	  * @throws Excepci�
	  */
		
	public static String executarHttpGet(String url) throws Exception {
		BufferedReader in = null;
		
		try{
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String newLine = System.getProperty("line.separator");
			
			while((line = in.readLine()) != null){
				sb.append(line + newLine);
			}
			in.close();
			String result = sb.toString();
			return result;
		}
		finally{
			if(in != null){
				try{
					in.close();
				}
				catch(IOException e){
					Log.e("Error de connexi� HTTP",e.getMessage());
				}
			}
		}
	}
}
