package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Josue Sanchez
 */
public class ImageInfoBO {
    
    public ImageInfoBO(){
    
    }
    
    
    
    boolean check;
    int idImageInfo;
    String category;
    String name;
    String description;
    String autor;
    String source;
    Date dateModify;
    boolean status;
    
     public ImageInfoBO(int idImageInfo){
     this.idImageInfo = idImageInfo;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdImageInfo() {
        return idImageInfo;
    }

    public void setIdImageInfo(int idImageInfo) {
        this.idImageInfo = idImageInfo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
    
    
    
    
    
    
}
