package com.adinfi.formateador.bos;
import com.ibm.mm.sdk.common.DKConstant;
import java.util.ArrayList;

public class DocumentoBO {
	    
	private String nombre;
	private String descripcion;
	private short itemType = DKConstant.DK_CM_DOCUMENT; 
	private String packagePid;
	private String pid;
	private String url = "";
	private int nivel = 1;
	private ArrayList<AtributoBO> atributos;
	private String estado;
	
	
	public DocumentoBO(){        
        }
	
	public DocumentoBO(String pid){
		this.atributos = new ArrayList<AtributoBO>();
		this.pid = pid;
		this.nivel = 1;
	}
	
	public DocumentoBO(String pid, String url, ArrayList<AtributoBO> atributos){
		this.pid = pid;
		this.url = url;
		this.atributos = atributos;
		this.nivel = 1;
	}
	
	/**
	 * @return El nombre del documento          
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre - Nombre para asignar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return La descripcion del documento.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion - Descripcion para asignar.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
	 * @return El itemType del documento.
	 */
        
	public short getItemType() {
            return itemType;
	}

	/**	 
	 * @param itemType -
	 */
        
	public void setItemType(short itemType) {
            this.itemType = itemType;
	}
	
	/**
	 * @return Pid	 
	 */
        
	public String getPackagePid() {
		return packagePid;
	}

	/**
	 * @param packagePid - Pid
	 */
	public void setPackagePid(String packagePid) {
		this.packagePid = packagePid;
	}
	
	/**
	 * @return El pid del documento.
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid - El pid para asignar.
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}


	/**
	 * @return La url del documento.
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url - La url para asignar.
	 */
	public void setUrl(String url) {
		
		if(url != null && !url.isEmpty()){
			this.url = url;
			//this.itemType = DKConstant.DK_CM_DOCUMENT;
		}
	}


	/**
	 * @return El nivel del documento.
	 */
	public int getNivel() {
		return nivel;
	}


	/**
	 * @param nivel - Nivel para asignar.
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	/**
	 * @return Estado del documento en el flujo.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado - Estado del documento para asignar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return Los atributos del documento.
	 */
	public ArrayList<AtributoBO> getAtributos() {
		return atributos;
	}

	/**
	 * @param atributos - Atributos pàra asignar.
	 */
	public void setAtributos(ArrayList<AtributoBO> atributos) {
		this.atributos = atributos;
	}
	
	
	/**
	 * @param attrName - Nombre del atributo buscado.
	 * @return Valor del atributo buscado o null
	 * si no existe un atributo con ese nombre.
	 */
	public String getValorAtributo(String attrName){
		AtributoBO atributo = this.getAtributo(attrName);
		
		if(atributo != null){
			return atributo.getValor();
		}
			
		return null;
	}
	
	/**
	 * @param attrName - Nombre del atributo.
	 * @return Objeto Attr o null si no existe
	 * un atributo con ese nombre.
	 */
	public AtributoBO getAtributo(String attrName){
		for (AtributoBO attr : atributos) {
			if(attr.getName().equals(attrName)){
				return attr;
			}
		}
		return null;
	}

	/**
	 * @param name - Nombre del Atributo.
	 * @param value - Valor del atributo.
	 * @return false - Si el atributo ya existe.
	 */
	public boolean addAtributo(String name, String value){
		if(this.atributos == null){
			this.atributos = new ArrayList<AtributoBO>();
		}
		
		for(AtributoBO attr : atributos) {
			if(attr.getName().equals(name)){
				return false;
			}
		}
		
		AtributoBO newAttr = new AtributoBO(name, value);
		this.atributos.add(newAttr);
		return true;
	}
	
	/**
	 * @param atributo - Objeto Atributo.
	 * @return false - Si el atributo ya existe.
	 */
	public boolean addAtributo(AtributoBO atributo){
		if(this.atributos == null){
			this.atributos = new ArrayList<AtributoBO>();
		}
		
		for(AtributoBO attr : atributos) {
			if(attr.getName().equals(atributo.getName())){
				return false;
			}
		}
		
		this.atributos.add(atributo);
		return true;
	}
	
	/**
	 * 
	 */
	public DocumentoBO clone(){
		DocumentoBO docClone = new DocumentoBO();
		docClone.setNombre(nombre);
		docClone.setDescripcion(descripcion);
		//docClone.setItemType(itemType);
		docClone.setEstado(estado);
		docClone.setNivel(nivel);
		docClone.setUrl(url);
		docClone.setPid(packagePid);
		docClone.setPackagePid(packagePid);
		
		for(AtributoBO attr : atributos) {
			docClone.addAtributo(attr.clone());
		}
		
		return docClone;
	}
	

        @Override
	public String toString(){
		String docString = "\nDocumento \nPid = "+ pid;	
                
		if(atributos != null && !atributos.isEmpty()){
			docString += "\nAtributos";
			
			for (AtributoBO atributo : atributos) {
				docString += "\n" + atributo.toString();
			}
		}				
		return docString;
	}
}
