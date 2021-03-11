/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author Josue Sanchez
 */
public class SendPublishAuthorsAttachBO {
    
   private int id_authors_attach;
   private int id_send_publish_attach;
   private int id_author;

    public int getId_authors_attach() {
        return id_authors_attach;
    }

    public void setId_authors_attach(int id_authors_attach) {
        this.id_authors_attach = id_authors_attach;
    }

    public int getId_send_publish_attach() {
        return id_send_publish_attach;
    }

    public void setId_send_publish_attach(int id_send_publish_attach) {
        this.id_send_publish_attach = id_send_publish_attach;
    }

    public int getId_author() {
        return id_author;
    }

    public void setId_author(int id_author) {
        this.id_author = id_author;
    }
   
}
