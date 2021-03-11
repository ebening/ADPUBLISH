package com.adinfi.formateador.bos;

/**
 *
 * @author Josue Sanchez
 */
public class CategoriaVideoBO {
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
