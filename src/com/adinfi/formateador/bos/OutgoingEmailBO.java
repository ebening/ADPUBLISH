package com.adinfi.formateador.bos;

import java.util.Date;

/**
 *
 * @author USUARIO
 */

public class OutgoingEmailBO {
    
    private boolean check;
    private int idOutgoingEmail;
    private String email;
    private boolean status;
    private Date datemodify;
    
     public OutgoingEmailBO() {
    }

    public OutgoingEmailBO(int idOutgoingEmail) {
        this.idOutgoingEmail = idOutgoingEmail;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdOutgoingEmail() {
        return idOutgoingEmail;
    }

    public void setIdOutgoingEmail(int idOutgoingEmail) {
        this.idOutgoingEmail = idOutgoingEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDatemodify() {
        return datemodify;
    }

    public void setDatemodify(Date datemodify) {
        this.datemodify = datemodify;
    }
    
    @Override
    public String toString(){
    return email;
    }

    public int getIdMarket() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
