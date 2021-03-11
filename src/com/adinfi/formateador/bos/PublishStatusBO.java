/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

/**
 *
 * @author Josue Sanchez
 */
public class PublishStatusBO {

    int idstatus_publish;
    int idpublish_status;
    String name;
    int idDocument;

    public int getIdpublish_status() {
        return idpublish_status;
    }

    public void setIdpublish_status(int idpublish_status) {
        this.idpublish_status = idpublish_status;
    }
    
    
    public int getIdstatus_publish() {
        return idstatus_publish;
    }

    public void setIdstatus_publish(int idstatus_publish) {
        this.idstatus_publish = idstatus_publish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

}
