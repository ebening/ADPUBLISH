package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Guillermo Trejo , Josue Sanchez
 */
public class LinkedExcelBO {

    private boolean check;
    private int idLinkedExcel;
    private int id_category;
    private String category;
    private String name;
    private String path;
    private boolean isCell;
    private String sheet;
    private String XY1;
    private String XY2;
    private String range;
    private Date dateModify;
    private boolean status;
    private int display;
    public static final int DISPLAY_CATEGORY = 0;
    public static final int DISPLAY_NAME = 1;
   

   
    

    public LinkedExcelBO() {

    }

    public LinkedExcelBO(int idLinkedExcel, String category, String name, String path, boolean isCell, String sheet, String XY1, String XY2) {
        this.idLinkedExcel = idLinkedExcel;
        this.category = category;
        this.name = name;
        this.path = path;
        this.isCell = isCell;
        this.sheet = sheet;
        this.XY1 = XY1;
        this.XY2 = XY2;
    }
    
        public LinkedExcelBO(int idLinkedExcel, int id_category, String category, String name, String path, boolean isCell, String sheet, String XY1, String XY2,String range) {
        this.idLinkedExcel = idLinkedExcel;
        this.id_category = id_category;
        this.category = category;
        this.name = name;
        this.path = path;
        this.isCell = isCell;
        this.sheet = sheet;
        this.XY1 = XY1;
        this.XY2 = XY2;
        this.range = range;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
        

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
    

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }
    
    

    public void display(int display) {
        this.display = display;
    }

    /**
     * @return the idLinkedExcel
     */
    public int getIdLinkedExcel() {
        return idLinkedExcel;
    }

    /**
     * @param idLinkedExcel the idLinkedExcel to set
     */
    public void setIdLinkedExcel(int idLinkedExcel) {
        this.idLinkedExcel = idLinkedExcel;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isCell
     */
    public boolean isIsCell() {
        return isCell;
    }

    /**
     * @param isCell the isCell to set
     */
    public void setIsCell(boolean isCell) {
        this.isCell = isCell;
    }

    /**
     * @return the sheet
     */
    public String getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(String sheet) {
        this.sheet = sheet;
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
     * @return the dateModify
     */
    public Date getDateModify() {
        return dateModify;
    }

    /**
     * @param dateModify the dateModify to set
     */
    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (display == DISPLAY_CATEGORY) {
            return category;
        } else if (display == DISPLAY_NAME) {
            return name;
        }
        return "";
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
