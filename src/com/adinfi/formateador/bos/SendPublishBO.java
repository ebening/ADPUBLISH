/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

import java.sql.Clob;
import java.util.Date;

/**
 *
 * @author Josue Sanchez
 */
public class SendPublishBO {

    private int idDocument_send;
    private int idStatus_publish;
    private int idDocument;
    private int idSubject;
    private String title;
    private boolean scheduled;
    private Clob   text;
    private String textString;
    private String date;
    private String time;
    private String date_publish;
    private String url;
    private String pid;
    private String pidzip;
    private Clob documentContent;
    private String correoAutor;
    
    public String getDate_publish() {
        return date_publish;
    }

    public void setDate_publish(String date_publish) {
        this.date_publish = date_publish;
    }
   
    public int getIdDocument_send() {
        return idDocument_send;
    }

    public void setIdDocument_send(int idDocument_send) {
        this.idDocument_send = idDocument_send;
    }

    public int getIdStatus_publish() {
        return idStatus_publish;
    }

    public void setIdStatus_publish(int idStatus_publish) {
        this.idStatus_publish = idStatus_publish;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
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
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPidzip() {
        return pidzip;
    }

    public void setPidzip(String pidzip) {
        this.pidzip = pidzip;
    }

    public Clob getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(Clob documentContent) {
        this.documentContent = documentContent;
    }

    public String getCorreoAutor() {
        return correoAutor;
    }

    public void setCorreoAutor(String correoAutor) {
        this.correoAutor = correoAutor;
    }
}
