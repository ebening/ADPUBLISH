/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.sql.Blob;
import java.sql.Clob;

/**
 *
 * @author Josue
 */
public class PublicityBO {
    
    
    private int idPublicity;
    private String url;
    private String description;
    private Clob text;
    private Blob image;

    public int getIdPublicity() {
        return idPublicity;
    }

    public void setIdPublicity(int idPublicity) {
        this.idPublicity = idPublicity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Clob getText() {
        return text;
    }

    public void setText(Clob text) {
        this.text = text;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
    
    
    
    
    
}
