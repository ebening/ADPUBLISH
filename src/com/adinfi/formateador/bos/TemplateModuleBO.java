/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

/**
 *
 * @author Desarrollador
 */
public class TemplateModuleBO {    
       int templateModuleId;
       int templateId;
       int moduleId;
       short orderId;
       ModuleBO module;  

    public int getTemplateModuleId() {
        return templateModuleId;
    }

    public void setTemplateModuleId(int templateModuleId) {
        this.templateModuleId = templateModuleId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public short getOrderId() {
        return orderId;
    }

    public void setOrderId(short orderId) {
        this.orderId = orderId;
    }

    public ModuleBO getModule() {
        return module;
    }

    public void setModule(ModuleBO module) {
        this.module = module;
    }
       
       
    
}
