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
public class DocCollabCandidateBO {
        
   private int collabDocCandidateId=0;
   private int docVersionId=0;
   private int docVersionTarget=0;
   private int marketId=0;
   private int documentTypeId=0;
   private int documentModuleId=0;
   private int moduleId=0;
   private String tipo;
   private int idHeader;
   private int idSection;   
   private String candTypeName;
   private String candSubject;
   private int candSubjectId;
   private String candName;
   private String candAuthor;
   private String candId;
   private boolean check;
   private Date fecha;
   private String estatus;
   private String name;
   private ModuleBO module;
   private String fileName;
   private String docSrcName;
   private int documentModuleIdTarget;

    public int getCollabDocCandidateId() {
        return collabDocCandidateId;
    }

    public void setCollabDocCandidateId(int collabDocCandidateId) {
        this.collabDocCandidateId = collabDocCandidateId;
    }

    public int getDocVersionId() {
        return docVersionId;
    }

    public void setDocVersionId(int docVersionId) {
        this.docVersionId = docVersionId;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getCandTypeName() {
        return candTypeName;
    }

    public void setCandTypeName(String candTypeName) {
        this.candTypeName = candTypeName;
    }

    public String getCandSubject() {
        return candSubject;
    }

    public void setCandSubject(String candSubject) {
        this.candSubject = candSubject;
    }

    public String getCandName() {
        return candName;
    }

    public void setCandName(String candName) {
        this.candName = candName;
    }

    public String getCandAuthor() {
        return candAuthor;
    }

    public void setCandAuthor(String candAuthor) {
        this.candAuthor = candAuthor;
    }

    public String getCandId() {
        return candId;
    }

    public void setCandId(String candId) {
        this.candId = candId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getDocumentModuleId() {
        return documentModuleId;
    }

    public void setDocumentModuleId(int documentModuleId) {
        this.documentModuleId = documentModuleId;
    }

 

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public ModuleBO getModule() {
        return module;
    }

    public void setModule(ModuleBO module) {
        this.module = module;
    }

    public int getCandSubjectId() {
        return candSubjectId;
    }

    public void setCandSubjectId(int candSubjectId) {
        this.candSubjectId = candSubjectId;
    }

    public int getDocVersionTarget() {
        return docVersionTarget;
    }

    public void setDocVersionTarget(int docVersionTarget) {
        this.docVersionTarget = docVersionTarget;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocSrcName() {
        return docSrcName;
    }

    public void setDocSrcName(String docSrcName) {
        this.docSrcName = docSrcName;
    }

    public int getDocumentModuleIdTarget() {
        return documentModuleIdTarget;
    }

    public void setDocumentModuleIdTarget(int documentModuleIdTarget) {
        this.documentModuleIdTarget = documentModuleIdTarget;
    }

    
 
   
     
    
    
}
