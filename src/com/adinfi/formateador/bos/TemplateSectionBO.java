package com.adinfi.formateador.bos;

import com.adinfi.formateador.util.GlobalDefines;
import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class TemplateSectionBO {
    
    private int sectionId=0;
    private int numElements=0;
    private String type=null;	  
    private short width=0;
    private short height=0;
    private int parentSection=0;
    private boolean isPercentage=false;
    private boolean childsEqual=false;          
    private int idx=0;
    private String sectionName=null;
    private List<TemplateSectionBO> childSections=null;      
    private List<ObjectInfoBO> lstObjects=null;
    private int templateId;
    private String contentType;
    private String htmlClass;
    private int sectionIdModule;

    public int getSectionIdModule() {
        return sectionIdModule;
    }

    public void setSectionIdModule(int sectionIdModule) {
        this.sectionIdModule = sectionIdModule;
    }

    public String getHtmlClass() {
        return htmlClass;
    }

    public void setHtmlClass(String htmlClass) {
        this.htmlClass = htmlClass;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public List<ObjectInfoBO> getLstObjects() {
        return lstObjects;
    }

    public void setLstObjects(List<ObjectInfoBO> lstObjects) {
        this.lstObjects = lstObjects;
    }
          
      
    public int getIdx() {
            return idx;
    }
    public void setIdx(int idx) {
            this.idx = idx;
    }
    public List<TemplateSectionBO> getChildSections() {
            return childSections;
    }
    public void setChildSegments(List<TemplateSectionBO> childSections) {
            this.childSections = childSections;
    }

    public int getNumElements() {
            return numElements;
    }
    public void setNumElements(int numElements) {
            this.numElements = numElements;
    }
    public String getType() {
            return type;
    }
    public void setType(String type) {
            this.type = type;
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
    public boolean isChildsEqual() {
            return childsEqual;
    }
    public void setChildsEqual(boolean childsEqual) {
            this.childsEqual = childsEqual;
    }
    public void setHeight(short height) {
            this.height = height;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    public int getParentSection() {
            return parentSection;
    }
    public void setParentSection(int parentSection) {
            this.parentSection = parentSection;
    }
    public boolean isPercentage() {
            return isPercentage;
    }
    
    public void setPercentage(boolean isPercentage) {
            this.isPercentage = isPercentage;
    }
    
    public boolean isText(){
        return ( this.contentType!=null && (this.contentType.equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT)||this.contentType.equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT_BULLET)));
    }

    
    
}
