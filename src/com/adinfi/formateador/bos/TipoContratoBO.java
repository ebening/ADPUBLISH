package com.adinfi.formateador.bos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USUARIO
 */
public class TipoContratoBO {

    private String nombre;
    private String tipoContratoId;
    private boolean esActivo;

    public TipoContratoBO() {
    }

    public TipoContratoBO(String nombre, String tipoContratoId, Boolean esActivo) {
        this.nombre = nombre;
        this.tipoContratoId = tipoContratoId;
        this.esActivo = esActivo;
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
     * @return the tipoContratoId
     */
    public String getTipoContratoId() {
        return tipoContratoId;
    }

    /**
     * @param tipoContratoId the tipoContratoId to set
     */
    public void setTipoContratoId(String tipoContratoId) {
        this.tipoContratoId = tipoContratoId;
    }

    /**
     * @return the esActivo
     */
    public boolean isEsActivo() {
        return esActivo;
    }

    /**
     * @param esActivo the esActivo to set
     */
    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }
    
    @Override
    public String toString(){
        return nombre;
    }

}
