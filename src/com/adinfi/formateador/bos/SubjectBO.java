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
public class SubjectBO {

    boolean check;
    int idSubject;
    String name;
    int industry;
    boolean issuing;
    Date dateModify;
    boolean status;

    public SubjectBO(int idSubject) {
        this.idSubject = idSubject;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public SubjectBO() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public boolean isIssuing() {
        return issuing;
    }

    public void setIssuing(boolean issuing) {
        this.issuing = issuing;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }

}
