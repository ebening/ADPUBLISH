package com.adinfi.formateador.bos;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Guillermo Trejo
 */
public class TemplateBO {
    
     int templateId;
     String name;
     short  width;
     short  height;
     String status;
     Date   fechaCreacion;
     short  heightTitle;
     boolean firstPage;
     
     int[] modulesAdd;
     Map<Integer,TemplateModuleBO> modulesAsMapByModule;
     List<TemplateModuleBO> modulesAsList;    
     private String templateName = null;     
     private String templatePath;     
    private String layoutType = null;     

    /*
    private int templateId = 0;
    private List<TemplateSectionBO> sections = null;
    private int rootSectionId = 0;
    private String templateName = null;
    private String layoutType = null;
    private short widht = 0;
    private Map<Integer, TemplateSectionBO> mapSectionsByIdx;
    private Map<Integer, List<TemplateSectionBO>> mapSectionsByParent = null;
    private Map<Integer, TemplateSectionBO> mapSectionsById = null;
    private List<TemplateSectionBO> lstSectionsLeave = null;
    private List<ModuleBO> lstModules = null;
    private short height = 0;
    private String status = null;
    private String templatePath;
    public int getTemplateId() {
    return templateId;
    }
    public void setTemplateId(int templateId) {
    this.templateId = templateId;
    }
    public List<TemplateSectionBO> getSections() {
    return sections;
    }
    public void setSections(List<TemplateSectionBO> sections) {
    this.sections = sections;
    }
    public int getRootSectionId() {
    return rootSectionId;
    }
    public void setRootSectionId(int rootSectionId) {
    this.rootSectionId = rootSectionId;
    }
    public String getTemplateName() {
    return templateName;
    }
    public void setTemplateName(String templateName) {
    this.templateName = templateName;
    }
    public String getLayoutType() {
    return layoutType;
    }
    public void setLayoutType(String layoutType) {
    this.layoutType = layoutType;
    }
    public short getWidht() {
    return widht;
    }
    public void setWidht(short widht) {
    this.widht = widht;
    }
    public Map<Integer, TemplateSectionBO> getMapSectionsByIdx() {
    return mapSectionsByIdx;
    }
    public void setMapSectionsByIdx(Map<Integer, TemplateSectionBO> mapSectionsByIdx) {
    this.mapSectionsByIdx = mapSectionsByIdx;
    }
    public Map<Integer, List<TemplateSectionBO>> getMapSectionsByParent() {
    return mapSectionsByParent;
    }
    public void setMapSectionsByParent(Map<Integer, List<TemplateSectionBO>> mapSectionsByParent) {
    this.mapSectionsByParent = mapSectionsByParent;
    }
    public Map<Integer, TemplateSectionBO> getMapSectionsById() {
    return mapSectionsById;
    }
    public void setMapSectionsById(Map<Integer, TemplateSectionBO> mapSectionsById) {
    this.mapSectionsById = mapSectionsById;
    }
    public List<TemplateSectionBO> getLstSectionsLeave() {
    return lstSectionsLeave;
    }
    public void setLstSectionsLeave(List<TemplateSectionBO> lstSectionsLeave) {
    this.lstSectionsLeave = lstSectionsLeave;
    }
    public List<ModuleBO> getLstModules() {
    return lstModules;
    }
    public void setLstModules(List<ModuleBO> lstModules) {
    this.lstModules = lstModules;
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
    public String getTemplatePath() {
    return templatePath;
    }
    public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
    }
     */
    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
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

    public int[] getModulesAdd() { 
        return modulesAdd;
    }

    public void setModulesAdd(int[] modulesAdd) {
        this.modulesAdd = modulesAdd;
    }

    public Map<Integer, TemplateModuleBO> getModulesAsMapByModule() {
        return modulesAsMapByModule;
    }

    public void setModulesAsMapByModule(Map<Integer, TemplateModuleBO> modulesAsMapByModule) {
        this.modulesAsMapByModule = modulesAsMapByModule;
    }

    public List<TemplateModuleBO> getModulesAsList() {
        return modulesAsList;
    }

    public void setModulesAsList(List<TemplateModuleBO> modulesAsList) {
        this.modulesAsList = modulesAsList;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }
    
    public short getHeightTitle() {
        return heightTitle;
    }

    public void setHeightTitle(short value) {
        this.heightTitle = value;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

}
