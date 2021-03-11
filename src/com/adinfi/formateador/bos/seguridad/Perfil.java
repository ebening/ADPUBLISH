package com.adinfi.formateador.bos.seguridad;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class Perfil {

    private int perfilId;               //Perfil Id
    private String nombre;		//Nombre del perfil
    private String descripcion;		//Descripción del perfil
    private String fechaAlta;		//Fecha de Creación del perfil
    private boolean isActiv;		//Bandera que indica si esta activa
    private boolean isAdministrable;	//Bandera que indica si es administrable desde la sección de Administración
    private boolean isVisible;		//Bandera que indica si es visible para la sección de administración

    private List<SeccionesPorPerfil> seccionesPorPerfil = new ArrayList<>();

    public Perfil() {
        this(0, null, null, null, false, false, false, null);
    }

    public Perfil(int perfilId, String nombre, String descripcion, String fechaAlta, boolean isActiv, boolean isAdministrable, boolean isVisible, List<SeccionesPorPerfil> seccionesPorPerfil) {
        this.perfilId = perfilId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.isActiv = isActiv;
        this.isAdministrable = isAdministrable;
        this.isVisible = isVisible;
        this.seccionesPorPerfil = seccionesPorPerfil == null
                ? seccionesPorPerfil = new ArrayList<>()
                : seccionesPorPerfil;
    }

    public Perfil(int perfilId, String nombre, String descripcion, String fechaAlta, boolean isActiv, boolean isAdministrable, boolean isVisible) {
        this.perfilId = perfilId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.isActiv = isActiv;
        this.isAdministrable = isAdministrable;
        this.isVisible = isVisible;
        this.seccionesPorPerfil = seccionesPorPerfil == null
                ? seccionesPorPerfil = new ArrayList<>()
                : seccionesPorPerfil;
    }

    /**
     * @return the perfilId
     */
    public int getPerfilId() {
        return perfilId;
    }

    /**
     * @param perfilId the perfilId to set
     */
    public void setPerfilId(int perfilId) {
        this.perfilId = perfilId;
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
     * @return the isVisible
     */
    public boolean isIsVisible() {
        return isVisible;
    }

    /**
     * @param isVisible the isVisible to set
     */
    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * @return the seccionesPorPerfil
     */
    public List<SeccionesPorPerfil> getSeccionesPorPerfils() {
        return seccionesPorPerfil;
    }

    /**
     * @param seccionesPorPerfil the seccionesPorPerfils to set
     */
    public void setSeccionesPorPerfils(List<SeccionesPorPerfil> seccionesPorPerfil) {
        this.seccionesPorPerfil = seccionesPorPerfil;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
