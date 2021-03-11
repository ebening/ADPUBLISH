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
public class FilePublishAttach {
    
    private String fileName;
    private String fileExt;
    private String fileUrl;
    
    public FilePublishAttach(String fileName, String fileExt, String fileUrl){
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
}
