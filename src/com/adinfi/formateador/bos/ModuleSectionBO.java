/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import com.adinfi.formateador.util.GlobalDefines;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Desarrollador
 */
public class ModuleSectionBO {
    int sectionId;
    int moduleId;
    int sectionParentId;
    short width;
    short height;
    String tipo;
    boolean percentage;
    boolean elemEq;
    String name;   
    String contentType;
    String initCoor;
    String endCoor;
    int rows;
    
    private int numElements=0;
    private String type=null;	          
  //  private int parentSection=0;
    private boolean isPercentage=false;
    private boolean childsEqual=false;          
    private int idx=0;
    private String sectionName=null;    
    
    Map<Integer,ModuleSectionBO> childSectionsAsMap;
    List<ModuleSectionBO> childSectionsAsList;
                 
    private List<ObjectInfoBO> lstObjects=null;
    private String htmlClass;
   
    
    
    
    //public addSection()
       
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getSectionParentId() {
        return sectionParentId;
    }

    public void setSectionParentId(int sectionParentId) {
        this.sectionParentId = sectionParentId;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public boolean isElemEq() {
        return elemEq;
    }

    public void setElemEq(boolean elemEq) {
        this.elemEq = elemEq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public Map<Integer, ModuleSectionBO> getChildSectionsAsMap() {
        return childSectionsAsMap;
    }

    public void setChildSectionsAsMap(Map<Integer, ModuleSectionBO> childSectionsAsMap) {
        this.childSectionsAsMap = childSectionsAsMap;
    }

    public List<ModuleSectionBO> getChildSectionsAsList() {
        return childSectionsAsList;
    }

    public void setChildSectionsAsList(List<ModuleSectionBO> childSectionsAsList) {
        this.childSectionsAsList = childSectionsAsList;
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

 

    public boolean isIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public boolean isChildsEqual() {
        return childsEqual;
    }

    public void setChildsEqual(boolean childsEqual) {
        this.childsEqual = childsEqual;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<ObjectInfoBO> getLstObjects() {
        return lstObjects;
    }

    public void setLstObjects(List<ObjectInfoBO> lstObjects) {
        this.lstObjects = lstObjects;
    }
    
    public boolean isText(){
        return ( this.contentType!=null && (this.contentType.equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT)||this.contentType.equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT_BULLET)));
    }

    public String getHtmlClass() {
        return htmlClass;
    }

    public void setHtmlClass(String htmlClass) {
        this.htmlClass = htmlClass;
    }
    
    public String getInitCoor() {
        return initCoor;
    }

    public void setInitCoor(String initCoor) {
        this.initCoor = initCoor;
    }
    
    public String getEndCoor() {
        return endCoor;
    }

    public void setEndCoor(String endCoor) {
        this.endCoor = endCoor;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
}
