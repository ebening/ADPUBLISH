package com.adinfi.formateador.bos;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class SemanarioDocsBO {

    private String nombreSemanario;
    private int idSemanario;
    private Date fecha;

    private List<DocumentBO> documentsBO;

    public SemanarioDocsBO() {

    }

    public SemanarioDocsBO(String nombreSemanario, int idSemanario, Date fecha, List<DocumentBO> documentsBO) {
        this.nombreSemanario = nombreSemanario;
        this.idSemanario = idSemanario;
        this.fecha = fecha;
        this.documentsBO = documentsBO;
    }

    public String getNombreSemanario() {
        return nombreSemanario;
    }

    public void setNombreSemanario(String nombreSemanario) {
        this.nombreSemanario = nombreSemanario;
    }

    public int getIdSemanario() {
        return idSemanario;
    }

    public void setIdSemanario(int idSemanario) {
        this.idSemanario = idSemanario;
    }

    public List<DocumentBO> getDocumentsBO() {
        return documentsBO;
    }

    public void setDocumentsBO(List<DocumentBO> documentsBO) {
        this.documentsBO = documentsBO;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
