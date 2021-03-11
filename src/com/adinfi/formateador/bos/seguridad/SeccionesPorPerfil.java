package com.adinfi.formateador.bos.seguridad;

/**
 *
 * @author Guillermo Trejo
 */
public class SeccionesPorPerfil {
    
    private int seccionId;              //Id de Sección.
    private String nombre;              //Nombre  de la sección.
    private boolean isActiv;            //Indica si esta activo o no.
    private boolean isAdministrable;	//Indica si es Administrable desde la seción Admon.
    private String descripcion;		//Descripción de la sección.
    private String descripcionCorta;	//Descripción corta de la sección.
    private int parentSeccion;		//ID de la Seccion padre, en caso que esta tenga.
    private String fechaAlta;		//Fecha de Alta

    public SeccionesPorPerfil() {
    
    }

    public SeccionesPorPerfil(int seccionId, String nombre, boolean isActiv, boolean isAdministrable, String descripcion, String descripcionCorta, int parentSeccion, String fechaAlta) {
        this.seccionId = seccionId;
        this.nombre = nombre;
        this.isActiv = isActiv;
        this.isAdministrable = isAdministrable;
        this.descripcion = descripcion;
        this.descripcionCorta = descripcionCorta;
        this.parentSeccion = parentSeccion;
        this.fechaAlta = fechaAlta;
    }

    
    
    /**
     * @return the seccionId
     */
    public int getSeccionId() {
        return seccionId;
    }

    /**
     * @param seccionId the seccionId to set
     */
    public void setSeccionId(int seccionId) {
        this.seccionId = seccionId;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the isActiv
     */
    public boolean isIsActiv() {
        return isActiv;
    }

    /**
     * @param isActiv the isActiv to set
     */
    public void setIsActiv(boolean isActiv) {
        this.isActiv = isActiv;
    }

    /**
     * @return the isAdministrable
     */
    public boolean isIsAdministrable() {
        return isAdministrable;
    }

    /**
     * @param isAdministrable the isAdministrable to set
     */
    public void setIsAdministrable(boolean isAdministrable) {
        this.isAdministrable = isAdministrable;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the descripcionCorta
     */
    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    /**
     * @param descripcionCorta the descripcionCorta to set
     */
    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    /**
     * @return the parentSeccion
     */
    public int getParentSeccion() {
        return parentSeccion;
    }

    /**
     * @param parentSeccion the parentSeccion to set
     */
    public void setParentSeccion(int parentSeccion) {
        this.parentSeccion = parentSeccion;
    }

    /**
     * @return the fechaAlta
     */
    public String getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return parentSeccion != 0 
                ? sb.append(">").append(nombre).toString()
                : nombre;
    }
}
