package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Guillermo Trejo
 */
public class LanguageBO {
    
    private boolean check;
    private int idLanguage;
    private String nomenclature;
    private String name;
    private boolean status;
    private Date datemodify;

    public LanguageBO() {
    }
    
   

    public LanguageBO(int idLanguage) {
        this.idLanguage = idLanguage;
    }
    
    public LanguageBO(boolean check, int idLanguage, String nomenclature, String name, boolean status, Date datemodify) {
        this.check = check;
        this.idLanguage = idLanguage;
        this.nomenclature = nomenclature;
        this.name = name;
        this.status = status;
        this.datemodify = datemodify;
    }

    
    
    public int getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDatemodify() {
        return datemodify;
    }

    public void setDatemodify(Date datemodify) {
        this.datemodify = datemodify;
    }
    
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
//    @Override
//    public String toString() {
//        return String.valueOf(idLanguage);
//    }
    
     @Override
    public String toString(){
    return name;
    }
    
}
