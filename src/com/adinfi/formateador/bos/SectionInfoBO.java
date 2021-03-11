package com.adinfi.formateador.bos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class SectionInfoBO {

    private String sectionName;
    private List<ObjectInfoBO> lstObjects = new ArrayList<>();

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

}
