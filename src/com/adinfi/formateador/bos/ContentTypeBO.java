
package com.adinfi.formateador.bos;

/**
 *
 * @author Josue Sanchez
 */
public class ContentTypeBO {
    private int index;
    private String descripcion;
    private String tipoMultimediaId;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoMultimediaId() {
        return tipoMultimediaId;
    }

    public void setTipoMultimediaId(String tipoMultimediaId) {
        this.tipoMultimediaId = tipoMultimediaId;
    }
    
    @Override
    public String toString(){
    return descripcion;
    }
    
}
