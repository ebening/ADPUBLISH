/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.sql.Clob;

/**
 *
 * @author vectoran
 */
public class SendPublishAttachBO {
    
    
    private int idDocument_send_attach;
    private int status;
    private int idSubject;
    private String title;
    private boolean scheduled;
    private Clob   text;
    private String textString;
    private String date;
    private String time;
    private String date_publish;
    private String url;
    private int idDocumentType;
    private int idMarket;
    private int idLanguage;
    private boolean isCollaborative;
    private int idAuthor;
    private String author;
    private int idDocument_type;

    public int getIdDocument_type() {
        return idDocument_type;
    }

    public void setIdDocument_type(int idDocument_type) {
        this.idDocument_type = idDocument_type;
    }
    
    

    public int getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(int idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    public int getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

    public boolean isIsCollaborative() {
        return isCollaborative;
    }

    public void setIsCollaborative(boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }
    
    public int getIdDocument_send_attach() {
        return idDocument_send_attach;
    }

    public void setIdDocument_send_attach(int idDocument_send_attach) {
        this.idDocument_send_attach = idDocument_send_attach;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Clob getText() {
        return text;
    }

    public void setText(Clob text) {
        this.text = text;
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate_publish() {
        return date_publish;
    }

    public void setDate_publish(String date_publish) {
        this.date_publish = date_publish;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

       
}
