package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Guillermo Trejo
 */
public class CatalogExcelBOCat {
    
    private int catalogExcelID;
    private String category;
    private String excelName;
    private String hoja;
    private String XY1;
    private String XY2;
    private Date fechaCreacion;
    private int id_category;

    public CatalogExcelBOCat() {
    
    }
    
    public CatalogExcelBOCat(String category ) {
        this.category = category;
        this.fechaCreacion = fechaCreacion != null ? (Date)fechaCreacion.clone() : null;
    }
    
     public CatalogExcelBOCat( int id_category , String category) {
        this.category = category;
        this.id_category = id_category;
        this.fechaCreacion = fechaCreacion != null ? (Date)fechaCreacion.clone() : null;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }
    
     
     
     
    
    /**
     * @return the catalogExcelID
     */
    public int getCatalogExcelID() {
        return catalogExcelID;
    }

    /**
     * @param catalogExcelID the catalogExcelID to set
     */
    public void setCatalogExcelID(int catalogExcelID) {
        this.catalogExcelID = catalogExcelID;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the excelName
     */
    public String getExcelName() {
        return excelName;
    }

    /**
     * @param excelName the excelName to set
     */
    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    /**
     * @return the XY1
     */
    public String getXY1() {
        return XY1;
    }

    /**
     * @param XY1 the XY1 to set
     */
    public void setXY1(String XY1) {
        this.XY1 = XY1;
    }

    /**
     * @return the XY2
     */
    public String getXY2() {
        return XY2;
    }

    /**
     * @param XY2 the XY2 to set
     */
    public void setXY2(String XY2) {
        this.XY2 = XY2;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the hoja
     */
    public String getHoja() {
        return hoja;
    }

    /**
     * @param hoja the hoja to set
     */
    public void setHoja(String hoja) {
        this.hoja = hoja;
    }
    
    @Override 
    public String toString() {
        return getCategory(); 
    }
}
