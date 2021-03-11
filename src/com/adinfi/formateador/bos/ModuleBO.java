/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import com.adinfi.formateador.util.GlobalDefines;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JPanel;

/**
 *
 * @author Desarrollador
 */
public class ModuleBO {
   int moduleId  ;
   String name;
   short width;
   short height;
   String status;
   Date fechaCreacion;
   ModuleSectionBO rootSection;
   private Map<Short,ModuleSectionBO> allSectionsByIndex=new TreeMap<Short,ModuleSectionBO>();
   List<ModuleSectionBO> lstSectionsText;
   List<ModuleSectionBO> lstSectionsTextandBullet;
        
   private short order;
   private int templateId;
   private int rootSectionId;
   private boolean delete;
   private short hoja;
  
   private JPanel panModule;
   private boolean hasText = false;
   private String templatePath;
   private int documentVersionId;
   private int version;   
   private int documentModuleId;
   private int origDocModuleId;
   private int idHeader;
   private int idSection;
   
   private boolean headerOrSection;
   private boolean section;

    public boolean isHeaderOrSection() {
        return headerOrSection;
    }

    public void setHeaderOrSection(boolean headerOrSection) {
        this.headerOrSection = headerOrSection;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ModuleSectionBO getRootSection() {
        return rootSection;
    }

    public void setRootSection(ModuleSectionBO rootSection) {
        this.rootSection = rootSection;
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

 
    
 
            
            
/**
 * Agrega una seccion al módulo 
 * @param parentIndex. Índice de la sección padre
 * @param tipo. Tipo de sección si es fila (R) o (C) columna 
 * @param section 
 */            
public void  addSection(  short parentIndex , String tipo , short width , short height ){
    
   ModuleSectionBO parentSec=allSectionsByIndex.get(parentIndex);
   if( parentSec==null ){
       return;
   }
   
   if(tipo==null || tipo.isEmpty() ){
       return ;
   }
   
   if( GlobalDefines.SEC_TYPE_SHEET.equals(tipo) ==false && 
       GlobalDefines.SEC_TYPE_COLUMN.equals(tipo)==false){
       return;
   }
   
   if( parentSec.getTipo()==null || parentSec.getTipo().isEmpty() ){
       parentSec.setTipo(tipo);       
   }else{
       if( parentSec.getTipo().equals(tipo)==false ){
          //padre es de tipo row y me dan col o visceversa 
          return;
       }
   }
   
   List<ModuleSectionBO> childSections=parentSec.getChildSectionsAsList();
   if( childSections==null ){
       childSections=new ArrayList<ModuleSectionBO>();
   }
   
   ModuleSectionBO newSection=new ModuleSectionBO();
   newSection.setWidth(width);
   newSection.setHeight(height);
       
   childSections.add(newSection);
   
   allSectionsByIndex.put(height, parentSec);
    
        
}


/**
 * Regresa una sección del módulo dado su índice
 * @param index
 * @return 
 */    
ModuleSectionBO getSectionByIndex (short  index ){
    ModuleSectionBO section=null;
    
    return section;
}
        
/**
 * Regresa una sección del módulo dado su Id
 * @param sectionId
 * @return 
 */
ModuleSectionBO getSectionById(int  sectionId ){
    ModuleSectionBO section=null;
    
    return section;
    
}


/**
 * Regresa la lista de las secciones hijas de una sección dado su índice
 * @param index
 * @return 
 */
public List<ModuleSectionBO> getLstSectionChildsByIndex( short index ){
  List<ModuleSectionBO>  lstChilds=null;
  return lstChilds;
}
        
        
/**
 * Regresa la lista de las secciones hijas de una sección dado su Id.
 * @param sectionId de la sección que se regresarán sus hijos
 * @return 
 */        
public List<ModuleSectionBO> getLstSectionChildsByParentId(int sectionId ){
   List<ModuleSectionBO>  lstChilds=null;
  return lstChilds;   
}

/**
 * Remueve una sección del módulo dado su índice. 
 * @param index 
 */
public void removeSectionByIndex( short index ){
    
}

/**
 * Remueve una sección del módulo dado su id. 
 * Si la sección tiene secciones hijas , serán también removidas.
 * @param sectionId 
 */
public void removeSectionById ( int sectionId ){
    
}

/**
 * Establece el tipo de contenido para la sección con el índice dado.
 * @param index
 * @param contentType 
 */
public void setContentTypeByIndex( short index , String contentType ){
    
}

/**
 * Establece el tipo de contenido para la sección con el id dado.
 * @param sectionId
 * @param contentType 
 */
public void setConentTypeById ( int sectionId , String contentType ){
    
}

    public Map<Short, ModuleSectionBO> getAllSectionsByIndex() {
        return allSectionsByIndex;
    }

    public void setAllSectionsByIndex(Map<Short, ModuleSectionBO> allSectionsByIndex) {
        this.allSectionsByIndex = allSectionsByIndex;
    }

    public short getOrder() {
        return order;
    }

    public void setOrder(short order) {
        this.order = order;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getRootSectionId() {
        return rootSectionId;
    }

    public void setRootSectionId(int rootSectionId) {
        this.rootSectionId = rootSectionId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public short getHoja() {
        return hoja;
    }

    public void setHoja(short hoja) {
        this.hoja = hoja;
    }

    public JPanel getPanModule() {
        return panModule;
    }

    public void setPanModule(JPanel panModule) {
        this.panModule = panModule;
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getDocumentVersionId() {
        return documentVersionId;
    }

    public void setDocumentVersionId(int documentVersionId) {
        this.documentVersionId = documentVersionId;
    }

    public int getDocumentModuleId() {
        return documentModuleId;
    }

    public void setDocumentModuleId(int documentModuleId) {
        this.documentModuleId = documentModuleId;
    }

    public List<ModuleSectionBO> getLstSectionsText() {
        return lstSectionsText;
    }

    public void setLstSectionsText(List<ModuleSectionBO> lstSectionsText) {
        this.lstSectionsText = lstSectionsText;
    }

    public List<ModuleSectionBO> getLstSectionsTextandBullet() {
        return lstSectionsTextandBullet;
    }

    public void setLstSectionsTextandBullet(List<ModuleSectionBO> lstSectionsTextandBullet) {
        this.lstSectionsTextandBullet = lstSectionsTextandBullet;
    }
    
    

    public int getOrigDocModuleId() {
        return origDocModuleId;
    }

    public void setOrigDocModuleId(int origDocModuleId) {
        this.origDocModuleId = origDocModuleId;
    }
 

   
   
}
