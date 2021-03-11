/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.main;

import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.StringEncrypter;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Usuario;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Guillermo Trejo
 */
public class LoginBackgroundWorker {

    private Exception exception;
    private final String user;
    private final String pass;
    private Usuario usuario_;

    LoginBackgroundWorker(final String user, final String pass) {
        this.user = user;
        this.pass = pass;
        this.usuario_ = null;
    }


    public boolean doInBackground() {
        boolean success = false;
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            this.usuario_ = stub.autentificarPlain(GlobalDefines.WS_INSTANCE, user, pass);
            if (usuario_ == null) {
                Utilerias.logger(getClass()).info("Error de conexion");
            } else {
                success = true;
                rememberPassword(user, pass);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de autentificación no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            setException(ex);
            Utilerias.logger(getClass()).info(ex);
        }
        return success;
    }

    private void rememberPassword(String u, String p) {
        try {
            Properties properties = Utilerias.getPasswordFile();
            /*
            String encryptUser = StringEncrypter.encryptDES(u);
            String encryptPass = StringEncrypter.encryptDES(p);
            */
            
            String encryptUser = new String(StringEncrypter.encryptAES(u));
            String encryptPass = new String(StringEncrypter.encryptAES(p));
                        
            properties.put(GlobalDefines.LOGIN_COOKIE_SAVED_PASS, true);
            properties.put(GlobalDefines.LOGIN_COOKIE_USER, encryptUser);
            properties.put(GlobalDefines.LOGIN_COOKIE_PASS, encryptPass);
            Utilerias.savePasswordFile(properties);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    public Usuario getUser() {
        return usuario_;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
