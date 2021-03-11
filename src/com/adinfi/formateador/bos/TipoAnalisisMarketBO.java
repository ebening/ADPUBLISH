/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author USUARIO
 */
public class TipoAnalisisMarketBO {
    
     private int categoriaVideo;
     private String descripcion;

    public int getCategoriaVideo() {
        return categoriaVideo;
    }

    public void setCategoriaVideo(int categoriaVideo) {
        this.categoriaVideo = categoriaVideo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
     
      @Override
    public String toString(){
    return descripcion;
    }
    
}
