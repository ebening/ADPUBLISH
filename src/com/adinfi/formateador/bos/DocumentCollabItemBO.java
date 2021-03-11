/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author Desarrollador
 */
public class DocumentCollabItemBO {
             
   private int documentCollabItemId ;
   private int documentId;
   private int docVersionId;
   private int docVersionIdTarget;
   private int itemDocVersionId;
   private short orderId;
   private boolean delete;
   private String  documentName;
   private int  itemVersion;
   private int  itemDocumentId;
   private int  documentModuleId;
   private int  moduleId;
   private int  moduleBO;
   private int idHeader;
   private int idSection;
   private String fileName;
   private String docTypeName;
   private String autor;

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
   private String tema;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getModuleBO() {
        return moduleBO;
    }

    public void setModuleBO(int moduleBO) {
        this.moduleBO = moduleBO;
    }



    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }


    public short getOrderId() {
        return orderId;
    }

    public void setOrderId(short orderId) {
        this.orderId = orderId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentCollabItemId() {
        return documentCollabItemId;
    }

    public void setDocumentCollabItemId(int documentCollabItemId) {
        this.documentCollabItemId = documentCollabItemId;
    }

    public int getItemDocVersionId() {
        return itemDocVersionId;
    }

    public void setItemDocVersionId(int itemDocVersionId) {
        this.itemDocVersionId = itemDocVersionId;
    }

    public int getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(int itemVersion) {
        this.itemVersion = itemVersion;
    }

    public int getItemDocumentId() {
        return itemDocumentId;
    }

    public void setItemDocumentId(int itemDocumentId) {
        this.itemDocumentId = itemDocumentId;
    }

    public int getDocumentModuleId() {
        return documentModuleId;
    }

    public void setDocumentModuleId(int documentModuleId) {
        this.documentModuleId = documentModuleId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getDocVersionId() {
        return docVersionId;
    }

    public void setDocVersionId(int docVersionId) {
        this.docVersionId = docVersionId;
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    public int getDocVersionIdTarget() {
        return docVersionIdTarget;
    }

    public void setDocVersionIdTarget(int docVersionIdTarget) {
        this.docVersionIdTarget = docVersionIdTarget;
    }
    
    
    
    
    
    
}
