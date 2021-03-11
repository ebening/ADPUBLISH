/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author ADINFI
 */
public class HeaderColModBO {
    private boolean check;
    private int idHeaderColMod;
    private int idMarket;
    private int idDocumentType;
    private String name;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdHeaderColMod() {
        return idHeaderColMod;
    }

    public void setIdHeaderColMod(int idHeaderColMod) {
        this.idHeaderColMod = idHeaderColMod;
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    public int getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(int idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
