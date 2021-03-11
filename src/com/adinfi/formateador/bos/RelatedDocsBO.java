/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author vectoran
 */
public class RelatedDocsBO {
    private int idRelatedDoc;
    private int document_id;
    private int related_document_id;
    private String document_name;
    private String file_name;
    private String url;
    private boolean status;
    
    

    public RelatedDocsBO(){

    }
    
    public RelatedDocsBO(int document_id){
    this.document_id = document_id;
    }
    
    
    public int getIdRelatedDoc() {
        return idRelatedDoc;
    }

    public void setIdRelatedDoc(int idRelatedDoc) {
        this.idRelatedDoc = idRelatedDoc;
    }
   
    public int getDocument_id() {
        return document_id;
    }

    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }

    public int getRelated_document_id() {
        return related_document_id;
    }

    public void setRelated_document_id(int related_document_id) {
        this.related_document_id = related_document_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
    
    
    
    

    
}
