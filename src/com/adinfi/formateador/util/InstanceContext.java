/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.util;

import com.adinfi.formateador.util.*;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfSeccion;
import com.adinfi.ws.IAccess_Stub;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class InstanceContext {
    
    private com.adinfi.ws.Usuario usuario;
    private ArrayOfSeccion profile;
    private static InstanceContext instance;
    private boolean activePDF;
    private boolean activeHTML;
    private boolean activeEditDocument;
    private boolean activePublish;
    
    
    
    public static synchronized InstanceContext getInstance() {
        if (instance == null) {
            instance = new InstanceContext();
        }
        return instance;
    }

    /**
     * @return the usuario
     */
    public com.adinfi.ws.Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(com.adinfi.ws.Usuario usuario) {
        if (usuario != null) {
            int idPerfil = usuario.getPerfilId();
            setIdPerfil(idPerfil);
        }
        this.usuario = usuario;
    }
    
    public void setIdPerfil(int idPerfil) {
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            profile = stub.secionesPorPerfil(GlobalDefines.WS_INSTANCE, idPerfil);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de secciones por perfil no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }

    /**
     * @return the profile
     */
    public ArrayOfSeccion getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(ArrayOfSeccion profile) {
        this.profile = profile;
    }

    public boolean isActivePDF() {
        return activePDF;
    }

    public void setActivePDF(boolean activePDF) {
        this.activePDF = activePDF;
    }

    public boolean isActiveHTML() {
        return activeHTML;
    }

    public void setActiveHTML(boolean activeHTML) {
        this.activeHTML = activeHTML;
    }

    public boolean isActiveEditDocument() {
        return activeEditDocument;
    }

    public void setActiveEditDocument(boolean activeEditDocument) {
        this.activeEditDocument = activeEditDocument;
    }

    public boolean isActivePublish() {
        return activePublish;
    }

    public void setActivePublish(boolean activePublish) {
        this.activePublish = activePublish;
    }
    
    
}
