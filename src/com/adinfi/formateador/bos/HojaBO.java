package com.adinfi.formateador.bos;

import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class HojaBO {
    short hoja;
    List<ModuleBO> lstModules=null; 
    boolean delete;

    public short getHoja() {
        return hoja;
    }

    public void setHoja(short hoja) {
        this.hoja = hoja;
    }

    public List<ModuleBO> getLstModules() {
        return lstModules;
    }

    public void setLstModules(List<ModuleBO> lstModules) {
        this.lstModules = lstModules;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    
}
