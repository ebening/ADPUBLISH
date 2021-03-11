/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Josue Sanchez
 */
public class MarketBO {
    
    private boolean check;
    private int     idMarket;
    private String  nomenclature;
    private String  name;
    private String  idMiVector;
    private boolean status;
    private Date    datemodify;
    private String idMiVector_real;
    
    public MarketBO(){
    
    }

    public String getIdMiVector() {
        return idMiVector;
    }

    public void setIdMiVector(String idMiVector) {
        this.idMiVector = idMiVector;
    }
    
    public MarketBO(int idMarket) {
        this.idMarket = idMarket;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
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

    public String getIdMiVector_real() {
        return idMiVector_real;
    }

    public void setIdMiVector_real(String idMiVector_real) {
        this.idMiVector_real = idMiVector_real;
    }
    
    
    
@Override
public String toString(){
return name;
}
    
    
}
