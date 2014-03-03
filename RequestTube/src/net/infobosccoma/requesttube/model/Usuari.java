package net.infobosccoma.requesttube.model;

/**
 * Classe que encapsula la informació d'un usuari de RequestTube
 * @author Marc Gurt (marcgurt@hotmail.com)
 *
 */
public class Usuari {
	
	private int id;
	private String nomUsuari, contrasenya;
	
	public Usuari(){
	}
	
	public Usuari(int id, String nomUsuari, String contrasenya) {
		this.id = id;
		this.nomUsuari = nomUsuari;
		this.contrasenya = contrasenya;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomUsuari() {
		return nomUsuari;
	}

	public void setNomUsuari(String nomUsuari) {
		this.nomUsuari = nomUsuari;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

}
