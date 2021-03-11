/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

/**
 *
 * @author USUARIO
 */
public class EmisoraBO {

    private int corto;
    private String descripcion;
    private String emisoraId;
    private Double _float;
    private Double PO_Mercado;
    private String recomendacionMercado;
    private String recomendacionMercadoDesc;
    private String recomendacionTecnica;
    private String recomendacionTecnicaDesc;
    private int recompra;
    private String sector;
    private String sectorId;
    private String serie;
    private String tipoEmisora;
    private String tipoEmisoraId;
    private Boolean isNueva;
    private Object __equalsCalc;
    private boolean __hashCodeCalc;

    public EmisoraBO() {
    }

    public EmisoraBO(int corto, String descripcion, String emisoraId, Double _float, Double PO_Mercado, String recomendacionMercado, String recomendacionMercadoDesc, String recomendacionTecnica, String recomendacionTecnicaDesc, int recompra, String sector, String sectorId, String serie, String tipoEmisora, String tipoEmisoraId, Boolean isNueva, Object __equalsCalc, boolean __hashCodeCalc) {
        this.corto = corto;
        this.descripcion = descripcion;
        this.emisoraId = emisoraId;
        this._float = _float;
        this.PO_Mercado = PO_Mercado;
        this.recomendacionMercado = recomendacionMercado;
        this.recomendacionMercadoDesc = recomendacionMercadoDesc;
        this.recomendacionTecnica = recomendacionTecnica;
        this.recomendacionTecnicaDesc = recomendacionTecnicaDesc;
        this.recompra = recompra;
        this.sector = sector;
        this.sectorId = sectorId;
        this.serie = serie;
        this.tipoEmisora = tipoEmisora;
        this.tipoEmisoraId = tipoEmisoraId;
        this.isNueva = isNueva;
        this.__equalsCalc = __equalsCalc;
        this.__hashCodeCalc = __hashCodeCalc;
    }

    /**
     * @return the corto
     */
    public int getCorto() {
        return corto;
    }

    /**
     * @param corto the corto to set
     */
    public void setCorto(int corto) {
        this.corto = corto;
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
     * @return the emisoraId
     */
    public String getEmisoraId() {
        return emisoraId;
    }

    /**
     * @param emisoraId the emisoraId to set
     */
    public void setEmisoraId(String emisoraId) {
        this.emisoraId = emisoraId;
    }

    /**
     * @return the _float
     */
    public Double getFloat() {
        return _float;
    }

    /**
     * @param _float the _float to set
     */
    public void setFloat(Double _float) {
        this._float = _float;
    }

    /**
     * @return the PO_Mercado
     */
    public Double getPO_Mercado() {
        return PO_Mercado;
    }

    /**
     * @param PO_Mercado the PO_Mercado to set
     */
    public void setPO_Mercado(Double PO_Mercado) {
        this.PO_Mercado = PO_Mercado;
    }

    /**
     * @return the recomendacionMercado
     */
    public String getRecomendacionMercado() {
        return recomendacionMercado;
    }

    /**
     * @param recomendacionMercado the recomendacionMercado to set
     */
    public void setRecomendacionMercado(String recomendacionMercado) {
        this.recomendacionMercado = recomendacionMercado;
    }

    /**
     * @return the recomendacionMercadoDesc
     */
    public String getRecomendacionMercadoDesc() {
        return recomendacionMercadoDesc;
    }

    /**
     * @param recomendacionMercadoDesc the recomendacionMercadoDesc to set
     */
    public void setRecomendacionMercadoDesc(String recomendacionMercadoDesc) {
        this.recomendacionMercadoDesc = recomendacionMercadoDesc;
    }

    /**
     * @return the recomendacionTecnica
     */
    public String getRecomendacionTecnica() {
        return recomendacionTecnica;
    }

    /**
     * @param recomendacionTecnica the recomendacionTecnica to set
     */
    public void setRecomendacionTecnica(String recomendacionTecnica) {
        this.recomendacionTecnica = recomendacionTecnica;
    }

    /**
     * @return the recomendacionTecnicaDesc
     */
    public String getRecomendacionTecnicaDesc() {
        return recomendacionTecnicaDesc;
    }

    /**
     * @param recomendacionTecnicaDesc the recomendacionTecnicaDesc to set
     */
    public void setRecomendacionTecnicaDesc(String recomendacionTecnicaDesc) {
        this.recomendacionTecnicaDesc = recomendacionTecnicaDesc;
    }

    /**
     * @return the recompra
     */
    public int getRecompra() {
        return recompra;
    }

    /**
     * @param recompra the recompra to set
     */
    public void setRecompra(int recompra) {
        this.recompra = recompra;
    }

    /**
     * @return the sector
     */
    public String getSector() {
        return sector;
    }

    /**
     * @param sector the sector to set
     */
    public void setSector(String sector) {
        this.sector = sector;
    }

    /**
     * @return the sectorId
     */
    public String getSectorId() {
        return sectorId;
    }

    /**
     * @param sectorId the sectorId to set
     */
    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    /**
     * @return the serie
     */
    public String getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /**
     * @return the tipoEmisora
     */
    public String getTipoEmisora() {
        return tipoEmisora;
    }

    /**
     * @param tipoEmisora the tipoEmisora to set
     */
    public void setTipoEmisora(String tipoEmisora) {
        this.tipoEmisora = tipoEmisora;
    }

    /**
     * @return the tipoEmisoraId
     */
    public String getTipoEmisoraId() {
        return tipoEmisoraId;
    }

    /**
     * @param tipoEmisoraId the tipoEmisoraId to set
     */
    public void setTipoEmisoraId(String tipoEmisoraId) {
        this.tipoEmisoraId = tipoEmisoraId;
    }

    /**
     * @return the isNueva
     */
    public Boolean getIsNueva() {
        return isNueva;
    }

    /**
     * @param isNueva the isNueva to set
     */
    public void setIsNueva(Boolean isNueva) {
        this.isNueva = isNueva;
    }

    /**
     * @return the __equalsCalc
     */
    public Object getEqualsCalc() {
        return __equalsCalc;
    }

    /**
     * @param __equalsCalc the __equalsCalc to set
     */
    public void setEqualsCalc(Object __equalsCalc) {
        this.__equalsCalc = __equalsCalc;
    }

    /**
     * @return the __hashCodeCalc
     */
    public boolean isHashCodeCalc() {
        return __hashCodeCalc;
    }

    /**
     * @param __hashCodeCalc the __hashCodeCalc to set
     */
    public void setHashCodeCalc(boolean __hashCodeCalc) {
        this.__hashCodeCalc = __hashCodeCalc;
    }

}
