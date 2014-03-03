package net.infobosccoma.requesttube.model;

import java.io.Serializable;
import java.util.TreeMap;

import net.infobosccoma.requesttube.R;

public class YoutubeRequest implements Serializable {
	
	private static final long serialVersionUID = 2758563108236510709L;
	private final String API_KEY = "AIzaSyBlcA8hAnEFyOqCeqjjmigX2klrj5lFrlM";
	private final String PART = "snippet";
	private String consulta, codiRegio, orderBy, categoria;
	private int maxResults;
	private TreeMap<String, String> diccionariOrderBy;
	private TreeMap<String, Integer> diccionariCategoryId;
	
	public YoutubeRequest(String consulta, String codiRegio, String categoria,
			String orderBy, int maxResults) {
		// Omplir els diccionaris
		omplirDiccionariOrderBy();
		omplirDiccionariCategoryId();
		this.consulta = consulta.trim().replace(" ", "+");
		this.codiRegio = codiRegio;
		this.orderBy = diccionariOrderBy.get(orderBy);
		this.maxResults = maxResults;
		
		// Comprovar si s'ha seleccionat categoria
		if(categoria.equals("Qualsevol")){
			this.categoria = "";
		} else {
			this.categoria = "&videoCategoryId="+diccionariCategoryId.get(categoria);
		}
	}
	
	public String getRequest(){
		String request = "https://www.googleapis.com/youtube/v3/search?part="+PART+"&maxResults="+maxResults+categoria+"&order="+orderBy+"&q="+consulta+"&regionCode="+codiRegio+"&type=video&key="+API_KEY;		
		System.out.println(request);
		return request;
	}
	
	private void omplirDiccionariOrderBy(){
		diccionariOrderBy = new TreeMap<String, String>();
		diccionariOrderBy.put("Rellevància Youtube", "relevance");
		diccionariOrderBy.put("Data creació", "date");
		diccionariOrderBy.put("Reproduccions", "viewCount");
		diccionariOrderBy.put("Votació usuaris", "rating");
	}
	
	private void omplirDiccionariCategoryId(){
		diccionariCategoryId = new TreeMap<String, Integer>();
		diccionariCategoryId.put("Animació", 1);
		diccionariCategoryId.put("Automoció", 2);
		diccionariCategoryId.put("Música", 10);
		diccionariCategoryId.put("Animals i Mascotes", 15);
		diccionariCategoryId.put("Esports", 17);
		diccionariCategoryId.put("Viatges", 19);
		diccionariCategoryId.put("Jocs", 20);
		diccionariCategoryId.put("Comedia", 23);
		diccionariCategoryId.put("Entreteniment", 24);
		diccionariCategoryId.put("How to - Com fer-ho", 26);
		diccionariCategoryId.put("Educació", 27);
		diccionariCategoryId.put("Ciència i tecnologia", 28);
		diccionariCategoryId.put("Pel·lícules", 30);
		diccionariCategoryId.put("Comèdia", 34);
		diccionariCategoryId.put("Ciència Ficció", 40);
	}

	@Override
	public String toString() {
		return "YoutubeRequest [consulta=" + consulta + ", codiRegio="
				+ codiRegio + ", orderBy=" + orderBy
				+ ", maxResults=" + maxResults + "]";
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	

}
