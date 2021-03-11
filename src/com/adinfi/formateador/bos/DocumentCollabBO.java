/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.util.List;

/**
 *
 * @author Desarrollador
 */
public class DocumentCollabBO extends DocumentBO{
    
    
    private int tipo;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
            
        //Si se trata de un documento colaborativo la lista de los doc.que lo integran
    List<Integer> lstDocumentCollabOrig=null;
    List<DocumentCollabItemBO> lstDocumentCollab=null;

    public List<Integer> getLstDocumentCollabOrig() {
        return lstDocumentCollabOrig;
    }

    public void setLstDocumentCollabOrig(List<Integer> lstDocumentCollabOrig) {
        this.lstDocumentCollabOrig = lstDocumentCollabOrig;
    }


    public List<DocumentCollabItemBO> getLstDocumentCollab() {
        return lstDocumentCollab;
    }

    public void setLstDocumentCollab(List<DocumentCollabItemBO> lstDocumentCollab) {
        this.lstDocumentCollab = lstDocumentCollab;
    }
    
    

    
}
