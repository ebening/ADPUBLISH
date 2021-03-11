
package com.adinfi.formateador.bos;

import java.sql.Blob;

/**
 *
 * @author Josue Sanchez
 */
public class ImageDataBO {
    
    int idImageData;
    int idImageInfo;
    Blob image;

    public int getIdImageData() {
        return idImageData;
    }

    public void setIdImageData(int idImageData) {
        this.idImageData = idImageData;
    }

    public int getIdImageInfo() {
        return idImageInfo;
    }

    public void setIdImageInfo(int idImageInfo) {
        this.idImageInfo = idImageInfo;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

  
    
    
    
}
