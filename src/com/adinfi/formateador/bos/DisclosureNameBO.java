/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author USUARIO
 */
public class DisclosureNameBO {
    
    int idDisclosure_name;
    String name;
    String indentifier;
    Date dateModify;
    boolean status; 
    
    public DisclosureNameBO(){
    
    }

    public int getIdDisclosure_name() {
        return idDisclosure_name;
    }

    public void setIdDisclosure_name(int idDisclosure_name) {
        this.idDisclosure_name = idDisclosure_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndentifier() {
        return indentifier;
    }

    public void setIndentifier(String indentifier) {
        this.indentifier = indentifier;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    @Override
    public String toString(){
    return name;
    }
    
    
    
    
}
