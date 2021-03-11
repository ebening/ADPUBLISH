/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author 
 */
public class RelationalFilesBO {
    
    private boolean check;
    private Integer idMarket;
    private String marketName;
    private Integer idDocType;
    private String docTypeName;
    private Integer idLanguage;
    private String languageName;
    private Integer idSubject;
    private String subjectName;
    private Integer idIndustry;
    private String industryName;
    private int idAuthor;
    private String author;
    private String docCreationDate;
    private String docPublicationDate;
    private int version;
    private String criteria;
    private Date docPublicationDateFrom;
    private Date docPublicationDateTo;
    private int idDocSend;
    private String url;
    private String document_name;

    public Integer getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(Integer idMarket) {
        this.idMarket = idMarket;
    }

    public Integer getIdDocType() {
        return idDocType;
    }

    public void setIdDocType(Integer idDocType) {
        this.idDocType = idDocType;
    }

    public Integer getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Integer idLanguage) {
        this.idLanguage = idLanguage;
    }

    public Integer getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Integer idSubject) {
        this.idSubject = idSubject;
    }

    public Integer getIdIndustry() {
        return idIndustry;
    }

    public void setIdIndustry(Integer idIndustry) {
        this.idIndustry = idIndustry;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

   

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDocCreationDate() {
        return docCreationDate;
    }

    public void setDocCreationDate(String docCreationDate) {
        this.docCreationDate = docCreationDate;
    }

    public String getDocPublicationDate() {
        return docPublicationDate;
    }

    public void setDocPublicationDate(String docPublicationDate) {
        this.docPublicationDate = docPublicationDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Date getDocPublicationDateFrom() {
        return docPublicationDateFrom;
    }

    public void setDocPublicationDateFrom(Date docPublicationDateFrom) {
        this.docPublicationDateFrom = docPublicationDateFrom;
    }

    public Date getDocPublicationDateTo() {
        return docPublicationDateTo;
    }

    public void setDocPublicationDateTo(Date docPublicationDateTo) {
        this.docPublicationDateTo = docPublicationDateTo;
    }

    

    public int getIdDocSend() {
        return idDocSend;
    }

    public void setIdDocSend(int idDocSend) {
        this.idDocSend = idDocSend;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }
    
    
    @Override
    public String toString(){
    return document_name;
    }
    
    
    
}
