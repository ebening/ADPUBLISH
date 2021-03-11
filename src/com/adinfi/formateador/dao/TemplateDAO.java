/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.bos.TemplateModuleBO;
import com.adinfi.formateador.bos.TemplateSectionBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.ThumbTemplate;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Desarrollador
 */
public class TemplateDAO {

    protected void addSections(ModuleSectionBO section, PreparedStatement pstInsSection) {
        String sqlInsSection;
        ResultSet rs = null;
        int moduleId;
        List<ModuleSectionBO> lstChilds;
        int n;
        int sectionId = 0;

        try (Connection conn = ConnectionDB.getInstance().getConnection();) {

            if (conn == null) {
                Utilerias.logger(getClass()).error("Error al obtener la conexion");
                return;
            }

            if (section == null || section.getChildSectionsAsList() == null || section.getChildSectionsAsList().size() <= 0) {
                return;
            }

            lstChilds = section.getChildSectionsAsList();
            moduleId = section.getModuleId();

            sqlInsSection = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("SECTION_SECTIONS_NEW ")
                    .append("(MODULE_ID , SECTION_PARENT_ID ,WIDTH,HEIGHT,TIPO ) ")
                    .append("VALUES( ")
                    .append(moduleId)
                    .append(" , ")
                    .append(section.getSectionId())
                    .append(" , ? , ? , ? ) ").toString();

            pstInsSection = conn.prepareStatement(sqlInsSection, Statement.RETURN_GENERATED_KEYS);

            for (ModuleSectionBO modSection : lstChilds) {

                pstInsSection.setShort(1, modSection.getWidth());
                pstInsSection.setShort(2, modSection.getHeight());
                pstInsSection.setString(3, modSection.getTipo());

                n = pstInsSection.executeUpdate();
                if (n > 0) {

                    rs = pstInsSection.getGeneratedKeys();
                    if (rs.next()) {
                        sectionId = rs.getInt(1);
                    }
                    modSection.setSectionId(sectionId);
                    addSections(modSection, pstInsSection);

                    if (rs != null) {
                        rs.close();
                    }
                }
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    public List<TemplateBO> getTemplates() {
        TemplateBO templateBO = null;
        String sqlTemplate = null;
        List<TemplateBO> lstTemplates = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stTemplate = conn.createStatement();) {

            sqlTemplate = new StringBuilder()
                    .append("SELECT ")
                    .append(" T.TEMPLATE_ID ,")
                    .append(" T.TEMPLATE_NAME ,")
                    .append(" T.WIDTH AS TEMPLATE_WIDTH ,")
                    .append(" T.HEIGHT AS TEMPLATE_HEIGHT ,")
                    .append(" T.STATUS AS TEMPLATE_STATUS ")
                    .append(" FROM ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_NEW T ")
                    .append(" WHERE T.STATUS = '1'")
                    .append(" ORDER BY T.TEMPLATE_ID ASC ").toString();

            lstTemplates = new ArrayList<>();
            ResultSet rsTemplate = stTemplate.executeQuery(sqlTemplate);
            while (rsTemplate.next()) {

                templateBO = new TemplateBO();
                templateBO.setTemplateId(rsTemplate.getInt("template_id"));
                templateBO.setTemplateName(rsTemplate.getString("template_name"));
                templateBO.setWidth(rsTemplate.getShort("template_width"));
                templateBO.setHeight(rsTemplate.getShort("template_height"));
                lstTemplates.add(templateBO);
            }
            if (rsTemplate != null) {
                rsTemplate.close();
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstTemplates;
    }
    
    public List<TemplateBO> getTemplatesFromDocumentType(int documentTypeID) {
        TemplateBO templateBO = null;
        String sqlTemplate = null;
        List<TemplateBO> lstTemplates = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stTemplate = conn.createStatement();) {

            sqlTemplate = new StringBuilder()
                    .append("SELECT ")
                    .append(" T.TEMPLATE_ID ,")
                    .append(" T.TEMPLATE_NAME ,")
                    .append(" T.WIDTH AS TEMPLATE_WIDTH ,")
                    .append(" T.HEIGHT AS TEMPLATE_HEIGHT ,")
                    .append(" T.STATUS AS TEMPLATE_STATUS ,")
                    .append(" T.FIRST_PAGE AS PRIMER_HOJA ")
                    .append(" FROM ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_NEW T ")
                    .append(" INNER JOIN ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_TEMPLATE AS DT ")
                    .append(" ON T.TEMPLATE_ID = DT.TEMPLATE_ID ")
                    .append(" INNER JOIN ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_TYPE AS DTT ")
                    .append(" ON DT.DOCUMENT_ID = DTT.IDDOCUMENT_TYPE ")
                    .append(" WHERE (T.STATUS = '1' OR T.STATUS IS NULL) AND (DTT.IDDOCUMENT_TYPE = ")
                    .append(documentTypeID)
                    .append(") ")
                    .append(" ORDER BY T.FIRST_PAGE DESC, T.TEMPLATE_ID ASC ").toString();

            lstTemplates = new ArrayList<>();
            ResultSet rsTemplate = stTemplate.executeQuery(sqlTemplate);
            while (rsTemplate.next()) {

                templateBO = new TemplateBO();
                templateBO.setTemplateId(rsTemplate.getInt("template_id"));
                templateBO.setTemplateName(rsTemplate.getString("template_name"));
                templateBO.setWidth(rsTemplate.getShort("template_width"));
                templateBO.setHeight(rsTemplate.getShort("template_height"));
                templateBO.setFirstPage(rsTemplate.getBoolean("PRIMER_HOJA"));
                lstTemplates.add(templateBO);
            }
            if (rsTemplate != null) {
                rsTemplate.close();
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstTemplates;
    }
    
    public Hashtable<Integer, ModuleSectionBO> getModulesSectionsByModuleId(int moduleId) {
        ModuleSectionBO sectionBO = null;
        String sqlModuleSection = null;
        ResultSet rsSection = null;
        Hashtable<Integer, ModuleSectionBO> lstModuleSection = new Hashtable<>();
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stModule = conn.createStatement();) {

            sqlModuleSection = new StringBuilder()
                    .append("SELECT SECTION_ID, MODULE_ID, SECTION_PARENT_ID, WIDTH, HEIGHT, TIPO, CONTENT_TYPE FROM ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("MODULE_SECTIONS_NEW WHERE MODULE_ID = ")
                    .append(moduleId).toString();

            rsSection = stModule.executeQuery(sqlModuleSection);
            while (rsSection.next()) {

                sectionBO = new ModuleSectionBO();
                sectionBO.setSectionId(rsSection.getInt("SECTION_ID"));
                sectionBO.setModuleId(rsSection.getInt("MODULE_ID"));
                sectionBO.setSectionParentId(rsSection.getInt("SECTION_PARENT_ID"));
                sectionBO.setWidth(rsSection.getShort("WIDTH"));
                sectionBO.setHeight(rsSection.getShort("HEIGHT"));
                sectionBO.setTipo(rsSection.getString("TIPO"));
                sectionBO.setType(rsSection.getString("CONTENT_TYPE"));
                lstModuleSection.put(sectionBO.getSectionId(), sectionBO);
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            if (rsSection != null) {
                try {
                    rsSection.close();
                } catch (SQLException ex) {
                    Utilerias.logger(getClass()).error(ex);
                }
            }
        }
        return lstModuleSection;
    }

    public ModuleBO getModule(int moduleId) {
        String sqlModule = null;
        ModuleBO moduleBO = null;
        ResultSet rsModule = null;
        Statement stModule = null;

        try (Connection conn = ConnectionDB.getInstance().getConnection();) {

            StringBuilder sb = new StringBuilder("SELECT ");
            sb.append(" MOD.MODULE_ID, ")
                    .append(" MOD.MODULE_NAME, ")
                    .append(" MOD.WIDTH AS MODULE_WIDTH, ")
                    .append(" MOD.HEIGHT AS MODULE_HEIGHT, ")
                    .append(" SEC.SECTION_ID, ")
                    .append(" SEC.MODULE_ID, ")
                    .append(" SEC.SECTION_PARENT_ID, ")
                    .append(" SEC.WIDTH AS SECTION_WIDTH, ")
                    .append(" SEC.HEIGHT AS SECTION_HEIGHT, ")
                    .append(" SEC.TIPO, ")
                    .append(" SEC.SEC_NAME, ")
                    .append(" CONTENT_TYPE, ")
                    .append(" INIT_COOR, ")
                    .append(" END_COOR, ")
                    .append(" SEC.ROWS ")
                    .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("MODULE_NEW MOD ")
                    .append(" INNER JOIN ").append(GlobalDefines.DB_SCHEMA).append("MODULE_SECTIONS_NEW SEC ")
                    .append(" ON MOD.MODULE_ID=SEC.MODULE_ID ")
                    .append(" WHERE MOD.STATUS = 1 AND MOD.MODULE_ID = ")
                    .append(moduleId)
                    .append(" ORDER BY SEC.SECTION_PARENT_ID ");

            sqlModule = sb.toString();

            stModule = conn.createStatement();
            rsModule = stModule.executeQuery(sqlModule);
            List<ModuleSectionBO> lstChildsSections = null;
            Map<Integer, List<ModuleSectionBO>> mapSectionsByParent = new TreeMap<>();

            lstChildsSections = new ArrayList<>();
            ModuleSectionBO docSecBO = null;

            while (rsModule.next()) {
                if (moduleBO == null) {
                    moduleBO = new ModuleBO();
                    moduleBO.setModuleId(rsModule.getInt("MODULE_ID"));
                    moduleBO.setName(rsModule.getString("MODULE_NAME"));
                    moduleBO.setWidth(rsModule.getShort("MODULE_WIDTH"));
                    moduleBO.setHeight(rsModule.getShort("MODULE_HEIGHT"));
                }

                lstChildsSections = mapSectionsByParent.get(rsModule.getInt("section_parent_id"));

                if (lstChildsSections == null) {
                    lstChildsSections = new ArrayList<>();
                    mapSectionsByParent.put(rsModule.getInt("section_parent_id"), lstChildsSections);
                }

                docSecBO = new ModuleSectionBO();
                docSecBO.setType(rsModule.getString("tipo"));
                docSecBO.setSectionId(rsModule.getInt("section_id"));
                docSecBO.setWidth(rsModule.getShort("SECTION_WIDTH"));
                docSecBO.setHeight(rsModule.getShort("SECTION_HEIGHT"));
                docSecBO.setSectionName(rsModule.getString("sec_name"));
                docSecBO.setSectionParentId(rsModule.getInt("section_parent_id"));
                docSecBO.setModuleId(moduleBO.getModuleId());
                docSecBO.setContentType(rsModule.getString("content_type"));
                docSecBO.setInitCoor(rsModule.getString("INIT_COOR"));
                docSecBO.setEndCoor(rsModule.getString("END_COOR"));
                docSecBO.setRows(rsModule.getInt("ROWS"));
                lstChildsSections.add(docSecBO);

                lstChildsSections = new ArrayList<>();
                mapSectionsByParent.put(rsModule.getInt("section_id"), lstChildsSections);
                docSecBO.setChildSectionsAsList(lstChildsSections);

                if (rsModule.getInt("section_parent_id") == 0) {
                    moduleBO.setRootSection(docSecBO);
                    moduleBO.setRootSectionId(docSecBO.getSectionId());
                }

            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            try {
                if (rsModule != null) {
                    rsModule.close();
                }
                if (stModule != null) {
                    stModule.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }
        return moduleBO;
    }

    public TemplateBO getTemplate(int templateId) {
        TemplateBO templateBO = null;
        String sqlTemplate = null;
        ResultSet rsTemplate = null;
       

        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stTemplate = conn.createStatement();) {

            sqlTemplate = new StringBuilder()
                    .append(" SELECT  TE.TEMPLATE_ID , ")
                    .append("  TE.TEMPLATE_NAME , ")
                    .append("  TE.WIDTH as template_width, ")
                    .append("  TE.HEIGHT as template_height, ")
                    .append("  TE.STATUS , ")
                    .append("  TE.TIPO_LAYOUT  , ")
                    .append("  TE.HEIGHT_TITLE  , ")
                    .append("  TE.FIRST_PAGE  , ")
                    .append("  TM.TEMPLATE_MODULE_ID , ")
                    .append("  TM.ORDER_ID , ")
                    .append("  TM.MODULE_ID , ")                
                    .append("  MN.MODULE_NAME AS MODULE_NAME , ") 
                    .append("  MN.WIDTH AS module_width , ")
                    .append("  MN.HEIGHT AS module_height , ")   
                    .append("  MN.STATUS AS MODULE_STATUS , ")
                    .append("  MN.FECHA_CREACION AS MODULE_FECHA_CREACION  ")
                    .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("TEMPLATE_NEW TE ")
                    .append(" INNER JOIN ").append(GlobalDefines.DB_SCHEMA).append("TEMPLATE_MODULES_NEW TM")
                    .append(" ON TE.TEMPLATE_ID = TM.TEMPLATE_ID ")
                    .append(" INNER JOIN ").append(GlobalDefines.DB_SCHEMA).append("MODULE_NEW AS MN")
                    .append(" ON MN.MODULE_ID = TM.MODULE_ID ")
                    .append(" WHERE (TE.TEMPLATE_ID = ").append(templateId).append(") ")
                    .append(" AND (TE.STATUS IS NULL OR TE.STATUS = '1') ")
                    .append(" ORDER BY TM.ORDER_ID ASC ").toString();

            rsTemplate = stTemplate.executeQuery(sqlTemplate);

            List<TemplateModuleBO> lstModules = null;
            TemplateModuleBO tm;
            
            while (rsTemplate.next()) {
                if (templateBO == null) {
                    templateBO = new TemplateBO();
                    templateBO.setTemplateId(rsTemplate.getInt("template_id"));
                    templateBO.setTemplateName(rsTemplate.getString("template_name"));
                    templateBO.setWidth(rsTemplate.getShort("template_width"));
                    templateBO.setHeight(rsTemplate.getShort("template_height"));
                    templateBO.setLayoutType(rsTemplate.getString("tipo_layout"));
                    templateBO.setHeightTitle(rsTemplate.getShort("HEIGHT_TITLE"));
                    templateBO.setFirstPage(rsTemplate.getBoolean("FIRST_PAGE"));
                    lstModules = new ArrayList<>();
                    templateBO.setModulesAsList(lstModules);
                }
                tm = new TemplateModuleBO();
                tm.setModuleId(rsTemplate.getInt("module_id"));
                tm.setTemplateModuleId(rsTemplate.getInt("template_module_id"));
                tm.setOrderId(rsTemplate.getShort("order_id"));
                
                ModuleBO module = new ModuleBO();
                
                module = this.getModule(tm.getModuleId());
                tm.setModule(module);
                
                /*
                module.setModuleId(rsTemplate.getInt("MODULE_ID"));
                module.setName(rsTemplate.getString("MODULE_NAME"));
                module.setWidth(rsTemplate.getShort("module_width"));
                module.setHeight(rsTemplate.getShort("module_height"));
                module.setStatus(rsTemplate.getString("MODULE_STATUS"));
                module.setFechaCreacion(rsTemplate.getDate("MODULE_FECHA_CREACION"));
                tm.setModule(module);
                */
                lstModules.add(tm);

            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            try {
                if (rsTemplate != null) {
                    rsTemplate.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }

        return templateBO;
    }

    public List<ModuleBO> getModules() {
        List<ModuleBO> lstModules = null;
        String sqlModules = new StringBuilder()
                .append(" SELECT ")
                .append(" MODULE_ID,")
                .append(" MODULE_NAME,")
                .append(" WIDTH,")
                .append(" HEIGHT,")
                .append(" STATUS,")
                .append(" FECHA_CREACION")
                .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("MODULE_NEW ")
                .append(" WHERE STATUS = '1' ").toString();
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stModules = conn.createStatement();
                ResultSet rsModules = stModules.executeQuery(sqlModules);) {

            ModuleBO module;
            while (rsModules.next()) {
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                }
                module = new ModuleBO();
                module.setModuleId(rsModules.getInt("MODULE_ID"));
                module.setName(rsModules.getString("MODULE_NAME"));
                module.setWidth(rsModules.getShort("WIDTH"));
                module.setHeight(rsModules.getShort("HEIGHT"));
                module.setStatus(rsModules.getString("STATUS"));
                module.setFechaCreacion(rsModules.getDate("FECHA_CREACION"));
                lstModules.add(module);
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstModules;
    }
    
    
    public List<ModuleBO> searchModulesByName(String value) {
        List<ModuleBO> lstModules = null;
        String sqlModules = new StringBuilder()
                .append(" SELECT ")
                .append(" MODULE_ID,")
                .append(" MODULE_NAME,")
                .append(" WIDTH,")
                .append(" HEIGHT,")
                .append(" STATUS,")
                .append(" FECHA_CREACION")
                .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("MODULE_NEW ")
                .append(" WHERE STATUS IS NULL OR STATUS = '1' ")               
                .toString();
        if( value != null && value.isEmpty() == false ){
            sqlModules += " AND MODULE_NAME LIKE '%" + value + "%'";
        }
                 
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stModules = conn.createStatement();
                ResultSet rsModules = stModules.executeQuery(sqlModules);) {

            ModuleBO module;
            while (rsModules.next()) {
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                }
                module = new ModuleBO();
                module.setModuleId(rsModules.getInt("MODULE_ID"));
                module.setName(rsModules.getString("MODULE_NAME"));
                module.setWidth(rsModules.getShort("WIDTH"));
                module.setHeight(rsModules.getShort("HEIGHT"));
                module.setStatus(rsModules.getString("STATUS"));
                module.setFechaCreacion(rsModules.getDate("FECHA_CREACION"));
                lstModules.add(module);
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstModules;
    }
    

    public boolean removeModule(int moduleId){
        boolean retVal=true;
        String sqlUpdModule;
        Statement stUpdModule = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlUpdModule = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("MODULE_NEW SET STATUS = '0' WHERE MODULE_ID = ")
                    .append(moduleId).toString();
            
            stUpdModule = conn.createStatement();
            int n = stUpdModule.executeUpdate(sqlUpdModule);
            
            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stUpdModule != null) {
                try {
                    stUpdModule.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    public boolean editModule(int moduleId, String name){
        boolean retVal=true;
        String sqlUpdModule;
        Statement stUpdModule = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlUpdModule = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("MODULE_NEW SET MODULE_NAME = '")
                    .append(name)
                    .append("' WHERE MODULE_ID = ")
                    .append(moduleId).toString();
            
            stUpdModule = conn.createStatement();
            int n = stUpdModule.executeUpdate(sqlUpdModule);
            
            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stUpdModule != null) {
                try {
                    stUpdModule.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    public int validaEliminacionModulo(int moduleId){
        if(existModuleInTemplate(moduleId)){
            return USED_TEMPLATE;
        }else if (validationExistModuleInTemplate(moduleId)){
            return INTO_TEMPLATE;
        }
        
        return DELETE_TEMPLATE;
    }
    
    public boolean existModuleInTemplate(int moduleId){
        boolean retVal = false;
        String sqlTemp;
        Statement stTemp = null;
        ResultSet rsTemp = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return true;
            }
            
            sqlTemp = new StringBuilder()
                    .append(" SELECT * FROM ").append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_MODULES_NEW WHERE MODULE_ID = ")
                    .append(moduleId).toString();
            
            stTemp = conn.createStatement();
            rsTemp = stTemp.executeQuery(sqlTemp);
            
            if(rsTemp.next()){
                retVal = true;
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = true;
        } finally {
            if (stTemp != null) {
                try {
                    stTemp.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = true;
                }
            }
        }
        return retVal;
    }
    
    public static final int DELETE_TEMPLATE = 0;
    public static final int DEFAULT_TEMPLATE = 1;
    public static final int USED_TEMPLATE = 2;
    public static final int INTO_TEMPLATE = 3;
    
    public int validaEliminacionTemplate(int templateId){
        if(validationDefaultTemplate(templateId)){
            return DEFAULT_TEMPLATE;
        }else if(validationUsedTemplate(templateId)){
            return USED_TEMPLATE;
        }
        
        return DELETE_TEMPLATE;
    }
    
    public boolean validationUsedTemplate(int templateId){
        boolean retVal = false;
        String sqlTemp;
        Statement stTemp = null;
        ResultSet rsTemp = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlTemp = new StringBuilder()
                    .append(" SELECT * FROM ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_MODULE_NEW WHERE TEMPLATE_ID = ")
                    .append(templateId).toString();
            
            stTemp = conn.createStatement();
            rsTemp = stTemp.executeQuery(sqlTemp);
            
            if(rsTemp.next()){
                retVal = true;
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stTemp != null) {
                try {
                    stTemp.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }    
    
    public boolean validationDefaultTemplate(int templateId){
        boolean retVal = false;
        String sqlTemp;
        Statement stTemp = null;
        ResultSet rsTemp = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlTemp = new StringBuilder()
                    .append(" SELECT DEFAULT_TEMP FROM  ").append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_NEW WHERE TEMPLATE_ID = ")
                    .append(templateId).toString();
            
            stTemp = conn.createStatement();
            rsTemp = stTemp.executeQuery(sqlTemp);
            
            if(rsTemp.next()){
                retVal = rsTemp.getInt("DEFAULT_TEMP") == 1 ? true : false;
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stTemp != null) {
                try {
                    stTemp.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }    
    
    public boolean validationExistModuleInTemplate(int moduleId){
        boolean retVal = false;
        String sqlTemp;
        Statement stTemp = null;
        ResultSet rsTemp = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlTemp = new StringBuilder()
                    .append("SELECT * FROM ").append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_NEW T INNER JOIN ").append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_MODULES_NEW M ON (M.TEMPLATE_ID = T.TEMPLATE_ID) WHERE T.STATUS <> 0 AND M.MODULE_ID = ")
                    .append(moduleId).toString();
            
            stTemp = conn.createStatement();
            rsTemp = stTemp.executeQuery(sqlTemp);
            
            if(rsTemp.next()){
                retVal = rsTemp.getInt("DEFAULT_TEMP") == 1 ? true : false;
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stTemp != null) {
                try {
                    stTemp.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }    
    
    public void addModule(ModuleBO module) {
        if (module == null) {
            return;
        }
        String sqlInsModule;
        Statement stInsModule;
        ResultSet rs = null;

        PreparedStatement pstInsSection = null;
        String sqlInsSection = null;
        int n;
        int moduleId = 0;

        try (Connection conn = ConnectionDB.getInstance().getConnection();) {

            if (conn == null) {
                return;
            }
            sqlInsModule = new StringBuilder()
                    .append(" INSERT INTO ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("MODULE_NEW ")
                    .append(" ( MODULE_NAME, WIDTH , HEIGHT, STATUS, FECHA_CREACION ) ")
                    .append(" VALUES (")
                    .append("'").append(module.getName()).append("', ")
                    .append(module.getWidth())
                    .append(", ")
                    .append(module.getHeight())
                    .append(", ")
                    .append("'1'")
                    .append(", ")
                    .append(" CURRENT DATE ) ").toString();

            stInsModule = conn.createStatement();
            n = stInsModule.executeUpdate(sqlInsModule, Statement.RETURN_GENERATED_KEYS);
            if (n <= 0) {
                return;
            }

            rs = stInsModule.getGeneratedKeys();

            if (rs.next()) {
                moduleId = rs.getInt(1);
                module.getRootSection().setModuleId(moduleId);
            }
            rs.close();

            if (module.getRootSection() != null) {

                sqlInsSection = new StringBuilder()
                        .append("INSERT INTO ")
                        .append(GlobalDefines.DB_SCHEMA)
                        .append("SECTION_SECTIONS_NEW ")
                        .append(" ( MODULE_ID , SECTION_PARENT_ID,WIDTH,HEIGHT,TIPO ) ")
                        .append(" VALUES (")
                        .append(moduleId)
                        .append(" , 0 , ? , ?, ? ) ").toString();

                pstInsSection = conn.prepareStatement(sqlInsSection);
                pstInsSection.setShort(1, module.getRootSection().getWidth());
                pstInsSection.setShort(2, module.getRootSection().getHeight());
                pstInsSection.setString(3, sqlInsModule);

                rs = pstInsSection.getGeneratedKeys();
                if (rs.next()) {
                    module.getRootSection().setSectionId(rs.getInt(1));
                }
                n = pstInsSection.executeUpdate(sqlInsSection, Statement.RETURN_GENERATED_KEYS);
                if (n > 0) {
                    addSections(module.getRootSection(), pstInsSection);
                }
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            if (pstInsSection != null) {
                try {
                    pstInsSection.close();
                } catch (Exception e) {
                    Utilerias.logger(getClass()).info(e);
                }
            }
        }

    }

    public List<String> addModuleJLGB(ModuleBO module) {
        List<String> res = new ArrayList<>(); 
        String retVal = "El Módulo se guardo satisfactoriamente.";
        if (module == null) {
            res.add("No existen datos para guardar.");
            res.add("0");
            return res;
        }
        String sqlInsModule;
        Statement stInsModule;
        ResultSet rs = null;

        Statement pstInsSection = null;
        String sqlInsSection = null;
        int n;
        int moduleId = 0;

        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                res.add("Fallo la conexión.");
                res.add("0");
                return res;
            }

            sqlInsModule = new StringBuilder()
                    .append(" INSERT INTO ").append(GlobalDefines.DB_SCHEMA).append("MODULE_NEW ")
                    .append(" ( MODULE_NAME, WIDTH , HEIGHT,STATUS,FECHA_CREACION  ) ")
                    .append(" VALUES (")
                    .append("'").append(module.getName()).append("'")
                    .append(" , ")
                    .append(module.getWidth())
                    .append(" , ")
                    .append(module.getHeight())
                    .append(" , '1'")
                    .append(" , CURRENT DATE )").toString();

            stInsModule = conn.createStatement();
            n = stInsModule.executeUpdate(sqlInsModule, Statement.RETURN_GENERATED_KEYS);
            if (n <= 0) {
                res.add("Fallo la inserción del encabezado del módulo.");
                res.add("0");
                return res;
            }
            rs = stInsModule.getGeneratedKeys();
            if (rs.next()) {
                moduleId = rs.getInt(1);
                if (module.getLstSectionsText() == null) {
                    res.add("No existen secciones para guardar.");
                    res.add("0");
                    return res;
                }
                for (ModuleSectionBO msBO : module.getLstSectionsText()) {
                    msBO.setModuleId(moduleId);
                }
            }
            if (rs != null) {
                rs.close();
            }

            if (module.getLstSectionsText() != null) {
                ModuleSectionBO msBOC = module.getLstSectionsText().get(0);

                sqlInsSection = new StringBuilder()
                        .append(" INSERT INTO ")
                        .append(GlobalDefines.DB_SCHEMA)
                        .append(" MODULE_SECTIONS_NEW ")
                        .append(" ( MODULE_ID , SECTION_PARENT_ID, WIDTH, HEIGHT, TIPO ) ")
                        .append(" VALUES ( ")
                        .append(moduleId)
                        .append(" , 0 , ")
                        .append(msBOC.getWidth())
                        .append(" , ")
                        .append(msBOC.getHeight())
                        .append(" , '")
                        .append(msBOC.getTipo())
                        .append("' ) ").toString();
                pstInsSection = conn.createStatement();

                n = pstInsSection.executeUpdate(sqlInsSection, Statement.RETURN_GENERATED_KEYS);

                if (n > 0) {
                    //se inserto el rootsegment
                    //addSections(module.getRootSection(), pstInsSection);
                    rs = pstInsSection.getGeneratedKeys();
                    int moduleSectionId = 0;
                    if (rs.next()) {
                        //module.getRootSection().setSectionId(rs.getInt(1));
                        moduleSectionId = rs.getInt(1);

                        int msRId = 0;
                        for (ModuleSectionBO msBO : module.getLstSectionsText()) {

                            if ("C".equals(msBO.getTipo())) {
                                continue;
                            }

                            ResultSet rss = null;
                            Statement pstInsModSec = null;

                            try {
                                String sqlInsModSec = new StringBuilder()
                                        .append("INSERT INTO ")
                                        .append(GlobalDefines.DB_SCHEMA)
                                        .append(" MODULE_SECTIONS_NEW ( MODULE_ID , SECTION_PARENT_ID,WIDTH,HEIGHT,TIPO, CONTENT_TYPE, INIT_COOR, END_COOR ) ")
                                        .append(" VALUES( ")
                                        .append(moduleId)
                                        .append(", ").toString();

                                pstInsModSec = conn.createStatement();
                                if ("R".equals(msBO.getTipo())) {
                                    sqlInsModSec += moduleSectionId + ", ";
                                } else {
                                    sqlInsModSec += msRId + ", ";
                                }

                                sqlInsModSec += msBO.getWidth() + ", " + msBO.getHeight() + ", '" + msBO.getTipo() + "', ";
                                if ("N".equals(msBO.getTipo())) {
                                    if (msBO.getContentType() == null) {
                                        sqlInsModSec += "null, '" + msBO.getInitCoor() + "', '" + msBO.getEndCoor() + "' )";
                                    } else {
                                        sqlInsModSec += "'" + msBO.getContentType() + "', '" + msBO.getInitCoor() + "', '" + msBO.getEndCoor() + "' )";
                                    }
                                } else {
                                    sqlInsModSec += "null, null, null)";
                                }
                                
                                

                                n = pstInsModSec.executeUpdate(sqlInsModSec, Statement.RETURN_GENERATED_KEYS);

                                if ("R".equals(msBO.getTipo())) {
                                    rss = pstInsModSec.getGeneratedKeys();
                                    if (rss.next()) {
                                        msRId = rss.getInt(1);
                                    }
                                }
                            } catch (Exception e) {
                                Utilerias.logger(getClass()).error(e);
                                retVal = "Ocurrio un error al gurdar las secciones.";
                            } finally {
                                if (pstInsModSec != null) {
                                    try {
                                        pstInsModSec.close();
                                    } catch (Exception e) {
                                        Utilerias.logger(getClass()).info(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            try {
                TemplateDAO templateDAO = new TemplateDAO();
                ModuleBO moduleBO = templateDAO.getModule(moduleId);

                short hres = 30;
                if (moduleBO.getHeight() > 200) {
                    hres = 90;
                }

                ThumbTemplate thTemp = new ThumbTemplate();
                thTemp.genThumbModule(moduleBO.getModuleId(), (short) 90, hres, "");
                thTemp.genThumbModule(moduleBO.getModuleId(), moduleBO.getWidth(), moduleBO.getHeight(), "_B");
            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = "Ocurrio un error al gurdar el Módulo";
        } finally {
            if (pstInsSection != null) {
                try {
                    pstInsSection.close();
                } catch (Exception e) {
                    Utilerias.logger(getClass()).info(e);
                }
            }
        }

        res.add(retVal);
        res.add(moduleId+"");
        return res;
    }

    protected void updateTemplateModules(TemplateBO template) {
        Connection conn = null;
        PreparedStatement pstInsTemplateMod = null;
        PreparedStatement pstUpdTemplateMod = null;

        String sqlInsTemplateMod;
        String sqlUpdTemplateMod;
        int moduleId;
        int orderId;
        int n;
        try {

            conn = ConnectionDB.getInstance().getConnection();
            if (conn == null) {
                return;
            }

            sqlInsTemplateMod = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_MODULES_NEW ( TEMPLATE_ID , MODULE_ID , ORDER_ID ) VALUES (")
                    .append(template.getTemplateId())
                    .append(" , ? , ?  ) ").toString();

            sqlUpdTemplateMod = new StringBuilder()
                    .append("UPDATE ").append(GlobalDefines.DB_SCHEMA).append("TEMPLATE_MODULES_NEW SET ORDER_ID = ? WHERE TEMPLATE_MODULE_ID = ? ").toString();

            pstInsTemplateMod = conn.prepareStatement(sqlInsTemplateMod);
            List<TemplateModuleBO> lstModules = template.getModulesAsList();
            if (lstModules == null || lstModules.size() <= 0) {
                return;
            }

            pstUpdTemplateMod = conn.prepareStatement(sqlUpdTemplateMod);

            short i = 0;
            ModuleBO module;
            for (TemplateModuleBO tempMod : lstModules) {
                i++;
                if (tempMod.getTemplateModuleId() > 0) {
                    //ya existe solo actualizamos orden
                    pstUpdTemplateMod.setShort(1, i);
                    pstUpdTemplateMod.setInt(2, tempMod.getTemplateModuleId());
                    n = pstUpdTemplateMod.executeUpdate();
                } else {
                    module = tempMod.getModule();
                    if (module.getModuleId() <= 0) {
                        addModule(module);
                    }
                    moduleId = module.getModuleId();
                    pstInsTemplateMod.setInt(1, moduleId);
                    pstInsTemplateMod.setShort(2, i);
                    n = pstInsTemplateMod.executeUpdate();
                }

            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            try {
                if (pstInsTemplateMod != null) {
                    pstInsTemplateMod.close();
                }
                if (pstUpdTemplateMod != null) {
                    pstUpdTemplateMod.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
    }

    public int addTemplate(TemplateBO template) {
        
        int templateId = 0;
        
        if (template == null) {
            return 0;
        }
        String sqlInsTemplate = null;

        int n = 0;

        sqlInsTemplate = new StringBuilder()
                .append(" INSERT INTO ").append(GlobalDefines.DB_SCHEMA).append("TEMPLATE_NEW ")
                .append(" ( TEMPLATE_NAME , WIDTH , HEIGHT, STATUS, FECHA_CREACION , TIPO_LAYOUT, HEIGHT_TITLE, FIRST_PAGE ) ")
                .append(" VALUES ( '")
                .append(template.getName())
                .append("' , ")
                .append(template.getWidth())
                .append(" , ")
                .append(template.getHeight())
                .append(" , '1', CURRENT DATE  , 'R', ")
                .append(template.getHeightTitle())
                .append(" , ")
                .append(template.isFirstPage() ? " 1 " : " 0 ").append(")").toString();

        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stInsTemplate = conn.createStatement();) {
            n = stInsTemplate.executeUpdate(sqlInsTemplate, Statement.RETURN_GENERATED_KEYS);

            if (n <= 0) {
                return 0;
            }
            ResultSet rs = stInsTemplate.getGeneratedKeys();
            if (rs.next()) {
                templateId = rs.getInt(1);
                template.setTemplateId(templateId);
            }
            rs.close();

            updateTemplateModules(template);
            
            ThumbTemplate thTemp = new ThumbTemplate();
            thTemp.genThumbTemplate(templateId, (short) 90, (short) 90);
            //thTemp.genThumbTemplate(templateId, (short) 90, (short) 90);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        return templateId;
    }
    
    public boolean updateTemplate(int templateId, TemplateBO tempBO){
        boolean retVal=true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append(" TEMPLATE_NEW SET TEMPLATE_NAME = '").append(tempBO.getTemplateName())
                    .append("', FIRST_PAGE = ").append(tempBO.isFirstPage() ? " 1 " : " 0 ")
                    .append(" WHERE TEMPLATE_ID = ")
                    .append(templateId).toString();
            
            stUpdTemplate = conn.createStatement();
            int n = stUpdTemplate.executeUpdate(sqlUpdTemplate);
            
            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stUpdTemplate != null) {
                try {
                    stUpdTemplate.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    public boolean removeTemplate(int templateId){
        boolean retVal=true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("TEMPLATE_NEW SET STATUS = '0' WHERE TEMPLATE_ID = ")
                    .append(templateId).toString();
            
            stUpdTemplate = conn.createStatement();
            int n = stUpdTemplate.executeUpdate(sqlUpdTemplate);
            
            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stUpdTemplate != null) {
                try {
                    stUpdTemplate.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    public boolean updateSection(int sectionId, int rows){
        boolean retVal = true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }
            
            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("MODULE_SECTIONS_NEW SET ROWS = ")
                    .append(rows)
                    .append(" WHERE SECTION_ID = ")
                    .append(sectionId).toString();
            
            stUpdTemplate = conn.createStatement();
            int n = stUpdTemplate.executeUpdate(sqlUpdTemplate);
            
            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stUpdTemplate != null) {
                try {
                    stUpdTemplate.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
}
