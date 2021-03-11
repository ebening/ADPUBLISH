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
public class IndustryBO {
    
    private boolean check;
    private int idIndustry;
    private String name;
    private boolean status;
    private Date datemodify;
    
    public IndustryBO(){
    
    }
    
    public IndustryBO(int idIndustry) {
        this.idIndustry = idIndustry;
    }
    
    public Boolean isCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getIdIndustry() {
        return idIndustry;
    }

    public void setIdIndustry(int idIndustry) {
        this.idIndustry = idIndustry;
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
 
    @Override
    public String toString() {
        return name;
    }
    
}
