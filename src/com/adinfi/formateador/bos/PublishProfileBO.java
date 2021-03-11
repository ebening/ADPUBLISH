package com.adinfi.formateador.bos;

/**
 *
 * @author USUARIO
 */
public class PublishProfileBO {
    
    int idPublishProfile;
    String name;

    public int getIdPublishProfile() {
        return idPublishProfile;
    }

    public void setIdPublishProfile(int idPublishProfile) {
        this.idPublishProfile = idPublishProfile;
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
