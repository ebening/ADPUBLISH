package com.adinfi.formateador.bos;

import java.sql.Blob;

/**
 *
 * @author Josue Sanchez
 */
public class SendPublishFilesBO {

    private int iddocument_send;
    private int iddocument_send_files;
    private String filename;
    private Blob file;
    private String uid;

    public int getIddocument_send() {
        return iddocument_send;
    }

    public void setIddocument_send(int iddocument_send) {
        this.iddocument_send = iddocument_send;
    }

    public int getIddocument_send_files() {
        return iddocument_send_files;
    }

    public void setIddocument_send_files(int iddocument_send_files) {
        this.iddocument_send_files = iddocument_send_files;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    

}
