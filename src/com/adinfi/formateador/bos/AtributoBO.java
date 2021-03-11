package com.adinfi.formateador.bos;

import java.sql.Clob;

public class AtributoBO {
	
	private String name = "";
	private String valor = "";
        private Clob valorClob;
	private String description = "";
	private short tipo;
	private int longuitud;
	private boolean requerido;
	private boolean representativo;
	
	public AtributoBO(){}
	
	public AtributoBO(String name, String valor){
		this.name = name;
		this.valor = valor;
	}
	
	public AtributoBO(String name, String valor, short tipo){
		this.name = name;
		this.valor = valor;
		this.tipo = tipo;
	}
	
	/**
	 * @return El valor del atributo.
	 */
	public String getValor() {
		return valor;	
	}
	
	/**
	 * @param valor - El valor para
	 * asignar al atributo.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
		
	/**
	 * @return Nombre del Atributo.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name - Nombre para
	 * asignar al Atributo.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return La longitud
	 * del Atributo.
	 */
	public int getLonguitud() {
		return longuitud;
	}
	
	/**
	 * @param longuitud - Longitud
	 * para asignar al atributo.
	 */
	public void setLonguitud(int longuitud) {
		this.longuitud = longuitud;
	}
	
	/**
	 * @return true - Si el
	 * atributo es requerdio.
	 */
	public boolean isRequerido() {
		return requerido;
	}
	
	/**
	 * @param requerido - define si
	 * el atributo es requerido.
	 */
	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}
	
	/**
	 * @return El tipo del Atributo.
	 */
	public short getTipo() {
		return tipo;
	}
	
	/**
	 * @param tipo - El tipo para
	 * asignar al Atributo.
	 */
	public void setTipo(short tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the representativo
	 */
	public boolean isRepresentativo() {
		return representativo;
	}

	/**
	 * @param representativo the representativo to set
	 */
	public void setRepresentativo(boolean representativo) {
		this.representativo = representativo;
	}

        public Clob getValorClob() {
            return valorClob;
        }

        public void setValorClob(Clob valorClob) {
            this.valorClob = valorClob;
        }
        
        
	
	/**
	 * Metodo utilizado para
	 * crear una copia de este
	 * objeto Atributo.
	 */
	public AtributoBO clone(){
		AtributoBO cloeAtributo = new AtributoBO();
		cloeAtributo.setName(this.name);
		cloeAtributo.setValor(this.valor);
		cloeAtributo.setDescription(this.description);
		cloeAtributo.setTipo(this.tipo);
		cloeAtributo.setLonguitud(this.longuitud);
		cloeAtributo.setRequerido(this.requerido);
		cloeAtributo.setRepresentativo(this.representativo);
		
		return cloeAtributo;
	}
	
	public String toString(){
		return "\n     Nombre="+name+
				"\n    Desc="+description;
	}
}
