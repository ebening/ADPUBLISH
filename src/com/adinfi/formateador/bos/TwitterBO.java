/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;

/**
 *
 * @author USUARIO
 */
public class TwitterBO {
    
    private int idTwitter;
    boolean hasDocument;
    private int idDocument;
    private int idUsuario;
    private String tweet;
    boolean isSend;
    private Timestamp date_modify;
    boolean status;
    Blob attachment;
    String path;
    int id_publish_attach;
    String filename;
    String ext;
    Clob encodedTw;

    public int getIdTwitter() {
        return idTwitter;
    }

    public void setIdTwitter(int idTwitter) {
        this.idTwitter = idTwitter;
    }

    public boolean isHasDocument() {
        return hasDocument;
    }

    public void setHasDocument(boolean hasDocument) {
        this.hasDocument = hasDocument;
    }

    public boolean isIsSend() {
        return isSend;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }  

    public Blob getAttachment() {
        return attachment;
    }

    public void setAttachment(Blob attachment) {
        this.attachment = attachment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public Timestamp getDate_modify() {
        return date_modify;
    }

    public void setDate_modify(Timestamp date_modify) {
        this.date_modify = date_modify;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId_publish_attach() {
        return id_publish_attach;
    }

    public void setId_publish_attach(int id_publish_attach) {
        this.id_publish_attach = id_publish_attach;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Clob getEncodedTw() {
        return encodedTw;
    }

    public void setEncodedTw(Clob encodedTw) {
        this.encodedTw = encodedTw;
    }
    
    
    
    
    

}
