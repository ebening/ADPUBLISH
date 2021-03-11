/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author Desarrollador
 */
public class DocumentVersion {
    
  private int docVersionId;
  private int documentId;
  private int version;
  private Date fechaCreacion;

    public int getDocVersionId() {
        return docVersionId;
    }

    public void setDocVersionId(int docVersionId) {
        this.docVersionId = docVersionId;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    
}
