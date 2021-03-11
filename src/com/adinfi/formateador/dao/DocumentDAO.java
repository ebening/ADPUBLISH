package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.bos.DocumentVersion;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.SemanarioDocsBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.TemplateSectionBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author Guillermo Trejo
 */
public class DocumentDAO {

    public List<DocumentBO> getDocuments(boolean allVersions) {
        DocumentBO documentBO = null;
        String sqlDocument = null;
        List<DocumentBO> lstDocuments = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            if (allVersions == false) {
                sqlDocument = sqlSelectSpecificVersionDocument;
            } else {
                sqlDocument = sqlSelectAllVersionDocument;
            }

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);
            lstDocuments = new ArrayList<>();
            while (rsDocument.next()) {
                documentBO = new DocumentBO();
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setWidht(rsDocument.getShort("document_width"));
                documentBO.setHeight(rsDocument.getShort("document_height"));
                documentBO.setDocVersionId(rsDocument.getInt("doc_version_id"));
                documentBO.setVersion(rsDocument.getInt("version"));

                lstDocuments.add(documentBO);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstDocuments;
    }
    
    
    
    protected void checkModulesForDeleteCollab(int documentId, int docVersionId, Map<Integer, HojaBO> mapHojas, Connection conn) {

        String sqlUpdateModule = "UPDATE   " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW SET ESTATUS='D' WHERE  DOCUMENT_MODULE_ID  = ? ";
        

        PreparedStatement pstUpdateModule = null;
        

        ResultSet rsCurrents = null;
        Statement stCurrents = null;
        String sqlCurrents = " SELECT DOCUMENT_MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW WHERE DOC_VERSION_ID= " + docVersionId;

        Map<Integer, ModuleBO> mapNewModules = new TreeMap<>();
        List<ModuleBO> lstModules;
        int n;
        try {

            Integer hoja = null;
            HojaBO hojaBO = null;
            Set set = mapHojas.keySet();
            Iterator it = set.iterator();

            short numHoja = 0;
            while (it.hasNext()) {
                numHoja++;
                hoja = (Integer) it.next();
                hojaBO = (HojaBO) mapHojas.get(hoja);
                if (hojaBO == null) {
                    continue;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    continue;
                }

                for (ModuleBO module : lstModules) {
                    if (module == null) {
                        continue;
                    }
                    mapNewModules.put(module.getDocumentModuleId(), module);
                }

            }

            stCurrents = conn.createStatement();
            rsCurrents = stCurrents.executeQuery(sqlCurrents);
            ModuleBO moduleTmp;
            while (rsCurrents.next()) {

                moduleTmp = mapNewModules.get(rsCurrents.getInt("document_module_id"));
                if (moduleTmp != null) {
                    //Esta en la lista de actuales documentos , no lo borramos
                    continue;
                }

                if (pstUpdateModule == null) {
                    pstUpdateModule = conn.prepareStatement(sqlUpdateModule);                  
                }

                

                pstUpdateModule.setInt(1, rsCurrents.getInt("document_module_id"));
                n = pstUpdateModule.executeUpdate();
                if (n > 0) {
                    System.out.println("Se actualizo  el modulo del documentModuleId=" + rsCurrents.getInt("document_module_id"));
                }

            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            try {
                if (rsCurrents != null) {
                    rsCurrents.close();
                }

                if (stCurrents != null) {
                    stCurrents.close();
                }

                if (pstUpdateModule != null) {
                    pstUpdateModule.close();
                }

          

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    
    
    
    
    
    

    protected void checkModulesForDelete(int documentId, int docVersionId, Map<Integer, HojaBO> mapHojas, Connection conn) {

        String sqlDeleteModule = "DELETE FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW WHERE  DOCUMENT_MODULE_ID  = ? ";
        String sqlDeleteObjecs = "DELETE FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW WHERE DOCUMENT_MODULE_ID= ? ";
        String sqlDeleteCollab = "DELETE FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM WHERE  DOCUMENT_MODULE_ID  = ? ";
        String sqlDeleteCand = "DELETE FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE WHERE  DOCUMENT_MODULE_ID   = ? ";

        PreparedStatement pstDeleteModule = null;
        PreparedStatement pstDelObjects = null;
        PreparedStatement pstDelCollab = null;
        PreparedStatement pstDelCand = null;

        ResultSet rsCurrents = null;
        Statement stCurrents = null;
        String sqlCurrents = " SELECT DOCUMENT_MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW WHERE DOC_VERSION_ID= " + docVersionId;

        Map<Integer, ModuleBO> mapNewModules = new TreeMap<>();
        List<ModuleBO> lstModules;
        int n;
        try {

            Integer hoja = null;
            HojaBO hojaBO = null;
            Set set = mapHojas.keySet();
            Iterator it = set.iterator();

            short numHoja = 0;
            while (it.hasNext()) {
                numHoja++;
                hoja = (Integer) it.next();
                hojaBO = (HojaBO) mapHojas.get(hoja);
                if (hojaBO == null) {
                    continue;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    continue;
                }

                for (ModuleBO module : lstModules) {
                    if (module == null) {
                        continue;
                    }
                    mapNewModules.put(module.getDocumentModuleId(), module);
                }

            }

            stCurrents = conn.createStatement();
            rsCurrents = stCurrents.executeQuery(sqlCurrents);
            ModuleBO moduleTmp;
            while (rsCurrents.next()) {

                moduleTmp = mapNewModules.get(rsCurrents.getInt("document_module_id"));
                if (moduleTmp != null) {
                    //Esta en la lista de actuales documentos , no lo borramos
                    continue;
                }

                if (pstDeleteModule == null) {
                    pstDeleteModule = conn.prepareStatement(sqlDeleteModule);
                    pstDelObjects = conn.prepareStatement(sqlDeleteObjecs);
                    pstDelCollab = conn.prepareStatement(sqlDeleteCollab);
                    pstDelCand = conn.prepareStatement(sqlDeleteCand);
                }

                pstDelObjects.setInt(1, rsCurrents.getInt("document_module_id"));
                n = pstDelObjects.executeUpdate();
                if (n > 0) {
                    System.out.println("Se elimino los objetos del documentModuleId=" + rsCurrents.getInt("document_module_id"));
                }

                pstDelCollab.setInt(1, rsCurrents.getInt("document_module_id"));
                n = pstDelCollab.executeUpdate();
                if (n > 0) {
                    System.out.println("Se elimino el modulo del documentModuleId=" + rsCurrents.getInt("document_module_id"));
                }

                pstDelCand.setInt(1, rsCurrents.getInt("document_module_id"));
                n = pstDelCand.executeUpdate();
                if (n > 0) {
                    System.out.println("Se elimino el modulo del documentModuleId=" + rsCurrents.getInt("document_module_id"));
                }

                pstDeleteModule.setInt(1, rsCurrents.getInt("document_module_id"));
                n = pstDeleteModule.executeUpdate();
                if (n > 0) {
                    System.out.println("Se elimino el modulo del documentModuleId=" + rsCurrents.getInt("document_module_id"));
                }

            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            try {
                if (rsCurrents != null) {
                    rsCurrents.close();
                }

                if (stCurrents != null) {
                    stCurrents.close();
                }

                if (pstDeleteModule != null) {
                    pstDeleteModule.close();
                }

                if (pstDelObjects != null) {
                    pstDelObjects.close();
                }

                if (pstDelCollab != null) {
                    pstDelCollab.close();
                }

                if (pstDelCand != null) {
                    pstDelCand.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public List<DocumentBO> getDocuments1() {
        DocumentBO documentBO = null;
        String sqlDocument = null;
        List<DocumentBO> lstDocuments = null;
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            sqlDocument = "SELECT                 "
                    + "	D.DOCUMENT_ID ,             "
                    + "	D.DOCUMENT_NAME ,           "
                    + " D.FILE_NAME     ,  "
                    + "	D.WIDTH AS DOCUMENT_WIDTH ,   "
                    + "	D.HEIGHT AS DOCUMENT_HEIGHT ,  "
                    + "	D.STATUS AS DOCUMENT_STATUS   "
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT D    "
                    + "	ORDER BY  D.DOCUMENT_NAME ";

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);
            lstDocuments = new ArrayList<>();
            while (rsDocument.next()) {
                documentBO = new DocumentBO();
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setWidht(rsDocument.getShort("document_width"));
                documentBO.setHeight(rsDocument.getShort("document_height"));

                lstDocuments.add(documentBO);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstDocuments;
    }
    
    public List<String> getDocumentCollabContentText(String lstDocIds) {
        String sqlDocument = null;
        List<String> retVal = null;
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            sqlDocument = "SELECT O.\"DATA\" AS CONTENIDO FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW O"
                    + "	INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW N"
                    + "	ON (N.DOCUMENT_MODULE_ID = O.DOCUMENT_MODULE_ID)"
                    + " WHERE O.TIPO IN (4,5) AND N.DOCUMENT_ID IN (" + lstDocIds 
                    + ") ORDER BY N.DOCUMENT_ID";

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);
            retVal = new ArrayList<>();
            while (rsDocument.next()) {
                String textContent = rsDocument.getString("CONTENIDO");
                
                if(textContent == null || textContent.trim().isEmpty())
                    continue;
                
                retVal.add( textContent.trim() );
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return retVal;
    }

    public String getPublishDocumentName(int documentId) {
        String sqlDocument = null;
        String retVal = null;
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            sqlDocument = "SELECT * "
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW D    "
                    + "	WHERE  D.DOCUMENT_ID = " + documentId;

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);
            if (rsDocument.next()) {
                retVal = rsDocument.getString("PUBLISH_NAME");
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            retVal = null;
        }
        return retVal;
    }

    public ModuleBO getDocModule(int docModuleId) {
        DocumentBO documentBO = null;
        String sqlDocument = null;
        ResultSet rsDocument = null;
        Connection conn = null;
        Statement stDocument = null;

        try {
            conn = ConnectionDB.getInstance().getConnection();
            stDocument = conn.createStatement();

            sqlDocument = " SELECT \n"
                    + "  DV.DOCUMENT_ID , \n"
                    + "  DV.VERSION\n"
                    + "  FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV\n"
                    + "  WHERE DM.DOC_VERSION_ID=DV.DOC_VERSION_ID \n"
                    + "  AND DM.DOCUMENT_MODULE_ID=" + docModuleId;

            rsDocument = stDocument.executeQuery(sqlDocument);
            int documentId;
            int version;
            Integer cve;
            HojaBO hojaBO;
            Set<Integer> keySet;
            Iterator<Integer> it;
            List<ModuleBO> lstModules;
            if (rsDocument.next()) {

                documentId = rsDocument.getInt("document_Id");
                version = rsDocument.getInt("version");
                documentBO = this.getDocument(documentId, version,true);

                if (documentBO != null) {
                    if (documentBO.getMapHojas() != null) {

                        keySet = documentBO.getMapHojas().keySet();
                        it = keySet.iterator();
                        while (it.hasNext()) {
                            cve = it.next();
                            hojaBO = documentBO.getMapHojas().get(cve);
                            lstModules = hojaBO.getLstModules();
                            if (lstModules != null) {
                                for (ModuleBO module : lstModules) {

                                    if (module.getDocumentModuleId() == docModuleId) {
                                        return module;
                                    }

                                }
                            }

                        }

                    }

                }

            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {

            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }

                if (stDocument != null) {
                    stDocument.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;

    }

//    public void saveDocument(DocumentBO docBO) {
//
//        /*SI NO EXISTE UN DOCUMENTO */
//        if (docBO == null) {
//            return;
//        }
//
//        /* SI EXISTE UN DOCUMENTO ACTIVO Y TIENE UN ID MENOR IGUAL A CERO
//         ES TOMADO EN CUENTA COMO UN NUEVO DOCUMENTO.*/
//        if (docBO.getDocumentId() <= 0) {
//            //SI ES NUEVO GENERAR guid PARA USARLO COMO IDENTIFICADOR DE VERSIONAMIENTO
//            String idu = Utilerias.getUniqueObjectID();
//            docBO.setGuid(idu);
//            docBO.setVersion(1);
//            insertDocument(docBO);
//
//        } else {
//            /* GUARDAR VERSIÓN */
//            //pro                                                                                                                                                                                                                                     ceso de busqueda para version modificando docBO
//            //Select version n+1 
//            //insertar nueva version nuevo doc.
//            DocumentBO bo = getDocGUIDandCurrentVersion(docBO.getDocumentId());
//            int version = bo.getVersion();
//            docBO.setGuid(bo.getGuid());
//            docBO.setVersion(version + 1);
//            insertDocument(docBO);
//            updateDocument(docBO);
//        }
//    }
//
//    public int saveDocument(DocumentBO docBO, boolean r) {
//        int idDoc = 0;
//        if (docBO == null) {
//            return idDoc;
//        }
//        if (docBO.getDocumentId() <= 0) {
//            String idu = Utilerias.getUniqueObjectID();
//            docBO.setGuid(idu);
//            docBO.setVersion(1);
//            idDoc = insertDocument(docBO);
//
//        } else {
//            /* GUARDAR VERSIÓN */
//
//            //proceso de busqueda para version modificando docBO
//            //Select version n+1 
//            //insertar nueva version nuevo doc.
//            DocumentBO bo = getDocGUIDandCurrentVersion(docBO.getDocumentId());
//            int version = bo.getVersion();
//            docBO.setGuid(bo.getGuid());
//            docBO.setVersion(version + 1);
//            idDoc = insertDocument(docBO);
//
//            //updateDocument(docBO);
//        }
//        return idDoc;
//    }
    public DocumentBO getDocGUIDandCurrentVersion(int documentId) {
        DocumentBO bo = new DocumentBO();
        String sqlDocument = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            System.out.println(documentId);

            sqlDocument = " SELECT            "
                    + "	   D.DOCUMENT_ID ,    "
                    + "	   D.GUID ,           "
                    + "	   D.VERSION          "
                    + "	   FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW D WHERE DOCUMENT_ID =  " + documentId;

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);

            while (rsDocument.next()) {
                bo.setDocumentId(rsDocument.getInt("document_id"));
                bo.setGuid(rsDocument.getString("guid"));
                bo.setVersion(rsDocument.getInt("version"));
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return bo;
    }

    public int saveDocument1(DocumentBO docBO, boolean keepVersion) {
        String sqlInsDoc = new StringBuilder(" INSERT INTO ").append(GlobalDefines.DB_SCHEMA)
            .append("DOCUMENT_NEW ( DOCUMENT_NAME, FECHA_CREACION, FILE_NAME, IDMARKET, IDDOCUMENT_TYPE, IDLANGUAGE, IDSUBJECT, FAVORITE, GUID, ID_AUTHOR, NAME_AUTHOR, COLLABORATIVE , COLLABORATIVE_TYPE, VISIBLE ) ")
            .append("VALUES ( ?, CURRENT DATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )").toString();
        
        String sqlUpdateDoc = new StringBuilder(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
            .append("DOCUMENT_NEW SET DOCUMENT_NAME = ?, FILE_NAME = ?, IDLANGUAGE = ?, IDSUBJECT = ?, FAVORITE = ? ")
            .append("WHERE DOCUMENT_ID = ?").toString();

        int documentId = 0;
        boolean isNew = false;
        if (docBO.getDocumentId() <= 0) {
            isNew = true;
        }

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement stInsDoc = conn.prepareStatement(sqlInsDoc, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement stUpdateDoc = conn.prepareStatement(sqlUpdateDoc);) {

            int favorite = (docBO.isFavorite()) ? 1 : 0;

            if (docBO.getDocumentId() <= 0 || docBO.getVersion() <= 0) {
                
                int i = 1;
                stInsDoc.setString(i++, docBO.getDocumentName());
                stInsDoc.setString(i++, docBO.getFileName());
                stInsDoc.setInt(i++, docBO.getIdMarket());
                stInsDoc.setInt(i++, docBO.getIdDocType());
                stInsDoc.setInt(i++, docBO.getIdLanguage());
                stInsDoc.setInt(i++, docBO.getIdSubject());
                stInsDoc.setString(i++, String.valueOf(favorite));
                stInsDoc.setString(i++, docBO.getGuid());
                stInsDoc.setString(i++, String.valueOf( InstanceContext.getInstance().getUsuario().getUsuarioId() ) );
                stInsDoc.setString(i++, InstanceContext.getInstance().getUsuario().getUsuarioNT());
                stInsDoc.setString(i++, (docBO.isCollaborative() ? " 1 " : null ));
                stInsDoc.setString(i++, docBO.getCollaborativeType() );
                stInsDoc.setInt(i++, (docBO.getCollaborativeType() == null && docBO.isCollaborative() ? 0 : 1) );
                
                int n = stInsDoc.executeUpdate();

                ResultSet rs;

                if (n > 0) {
                    Utilerias.logger(getClass()).info("Se inserto un elemento ");
                    rs = stInsDoc.getGeneratedKeys();
                    if (rs.next()) {
                        documentId = rs.getInt(1);
                        Utilerias.logger(getClass()).info("valor de documentId generada automáticamente = " + documentId);
                    }
                    rs.close();
                    // Cerrar ResultSet
                    docBO.setDocumentId(documentId);

                }

            } else {
                
                int i = 1;
                stUpdateDoc.setString(i++, docBO.getDocumentName());
                stUpdateDoc.setString(i++, docBO.getFileName());
                stUpdateDoc.setInt(i++, docBO.getIdLanguage());
                stUpdateDoc.setInt(i++, docBO.getIdSubject());
                stUpdateDoc.setInt(i++, docBO.isFavorite() ? 1 : 0);
                stUpdateDoc.setInt(i++, docBO.getDocumentId());

                int n = stUpdateDoc.executeUpdate();

                if (n > 0) {
                    Utilerias.logger(getClass()).info("Se actualizo un documento ID " + docBO.getDocumentId());
                }
            }

            boolean isNewVersion = (keepVersion == false || isNew );
            if (isNewVersion) {
                setNextVersion(docBO);
            }
            upsertModules(docBO, isNewVersion);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return docBO.getDocumentId();
    }

    public DocumentBO savePublishDocumentName(DocumentBO docBO, String documentNamePublish) {
        String sqlInsDoc = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stInsDoc = conn.createStatement();) {

            sqlInsDoc = " UPDATE " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW SET"
                    + " PUBLISH_NAME = '" + documentNamePublish
                    + "' WHERE DOCUMENT_ID = " + docBO.getDocumentId();

            int n = stInsDoc.executeUpdate(sqlInsDoc);

            if (n > 0) {
                Utilerias.logger(getClass()).info("Se actualizo un documento ID " + docBO.getDocumentId());
            }

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }

        return docBO;
    }

    protected DocumentVersion setNextVersion(DocumentBO docBO) {
        DocumentVersion docVersion = null;
        String sqlInsDocVersion = null;
        /* QUERY PARA GENERAR EL ID POR MEDIO DE UNA SECUENCIA  */
        // String sqlNextDocId = "VALUES  NEXT VALUE   FOR DOCUMENT_SEQ ";
        int newVersion;
        if (docBO.getVersion() <= 0) {
            newVersion = 1;
        } else {
            newVersion = docBO.getVersion() + 1;
        }

        sqlInsDocVersion = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW ( DOCUMENT_ID , VERSION , FECHA_CREACION ) VALUES ( "
                + "  " + docBO.getDocumentId() + " , " + newVersion + " , CURRENT DATE )  ";
        int docVersionId = 0;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stInsDocVersion = conn.createStatement();) {

            int n = stInsDocVersion.executeUpdate(sqlInsDocVersion, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs;

            if (n > 0) {
                System.out.println("Se inserto una version para el documentId  " + docBO.getDocumentId());
                rs = stInsDocVersion.getGeneratedKeys();
                if (rs.next()) {
                    docVersionId = rs.getInt(1);
                    System.out.println("valor de version generada automáticamente = " + docVersionId);
                }
                rs.close();
                // Cerrar ResultSet
                //docBO.setDocumentId(documentId);                          
                docBO.setVersion(newVersion);
                docBO.setDocVersionId(docVersionId);

                docVersion = new DocumentVersion();
                docVersion.setDocVersionId(docVersionId);
                docVersion.setVersion(newVersion);

            }

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
            Utilerias.logger(getClass()).info(e);
        }
        return docVersion;

    }

    public int insertDocument(DocumentBO docBO) {
        String sqlInsDoc = null;
        /* QUERY PARA GENERAR EL ID POR MEDIO DE UNA SECUENCIA  */
        String sqlNextDocId = "VALUES  NEXT VALUE   FOR " + GlobalDefines.DB_SCHEMA + "DOCUMENT_SEQ ";
        int documentId = 0;
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stNextDocId = conn.createStatement();
                ResultSet rsNextDocId = stNextDocId.executeQuery(sqlNextDocId);
                Statement stInsDoc = conn.createStatement();) {

            if (rsNextDocId.next()) {
                documentId = rsNextDocId.getInt(1);
                docBO.setDocumentId(documentId);
            }

            int favorite = (docBO.isFavorite()) ? 1 : 0;

            sqlInsDoc = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT ( "
                    + "DOCUMENT_ID ,"
                    + "DOCUMENT_NAME,"
                    + "STATUS, "
                    + "FECHA_CREACION,"
                    + "FILE_NAME, "
                    + "IDMARKET,"
                    + "IDDOCUMENT_TYPE,"
                    + "IDLANGUAGE,"
                    + "IDSUBJECT,"
                    + "VERSION,"
                    + "FAVORITE,"
                    + "GUID  ) VALUES ( "
                    + " "
                    + documentId + " , '"
                    + docBO.getDocumentName() + "' ,  "
                    + "NULL , "
                    + "CURRENT DATE ,  '"
                    + docBO.getFileName() + "',  '"
                    + docBO.getIdMarket() + "',  '"
                    + docBO.getIdDocType() + "',  '"
                    + docBO.getIdLanguage() + "',  '"
                    + docBO.getIdSubject() + "',  '"
                    + docBO.getVersion() + "',  '"
                    + favorite + "',  '"
                    + docBO.getGuid() + "'   )";

            stInsDoc.executeUpdate(sqlInsDoc);
            // upsertModules(docBO);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
            Utilerias.logger(getClass()).info(e);
        }
        return documentId;
    }

    //TODO posiblemente no se utiliza este metodo
    public void collectDocObjects(List<ObjectInfoBO> lstObjectsInfo, int secId, Map<Integer, List<TemplateSectionBO>> sectionsByParent,
            Map<Integer, TemplateSectionBO> sectionsById) {

        List<TemplateSectionBO> sectionsChilds = sectionsByParent.get(secId);
        TemplateSectionBO section = sectionsById.get(secId);
        List<ObjectInfoBO> lstObjects = null;
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType())) {
            lstObjects = section.getLstObjects();
            if (lstObjects == null || lstObjects.size() <= 0) {
                return;
            }
            for (ObjectInfoBO objectInfo : lstObjects) {
                lstObjectsInfo.add(objectInfo);
            }
            return;
        }

        if (sectionsChilds != null) {
            for (TemplateSectionBO secTmp : sectionsChilds) {
                if (secTmp == null) {
                    continue;
                }
                collectDocObjects(lstObjectsInfo, secTmp.getSectionId(), sectionsByParent,
                        sectionsById);
            }
        }

    }

    protected void upsertObjectsSection(int documentId, ModuleBO moduleBO, ModuleSectionBO parentSection, boolean isNewVersion) {
        String sqlInsObject = null;
        String sqlUpdObject = null;
        ResultSet rs = null;

        //String sqlNextObjectId = "VALUES  NEXT VALUE  FOR OBJECT_SEQ  ";
        sqlInsObject = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW "
                + "( "
                + " DOCUMENT_MODULE_ID,"
                + " SECTION_ID, "
                + " WIDTH,"
                + " HEIGHT, "
                + " TIPO, "
                + " FILE, "
                + " URL,"
                + " ORDER_ID,"
                + " DATA,"
                + " PLAIN_TEXT,"
                + " TITULO, "
                + " SUBTITULO, "
                + " DESCRIPCION, "
                + " HTML_VIDEO, "
                + " THUMBNAIL_URL , "
                + " RANGO_INI , "
                + " RANGO_FIN ,"
                + " FILE2, "
                + " IMAGE, "
                + " IMAGE_THUMB, "
                + " TIPOVIDEO, "
                + " IDVIDEO, "
                + " IDVIDEODB, "
                + " IDLINKEDEXCEL "
                + ")  "
                + " VALUES "
                + "( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        sqlUpdObject = "  UPDATE " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW "
                + "     SET WIDTH=? , "
                + "     HEIGHT=? , "
                + "     FILE=?   , "
                + "     URL=?    ,"
                + "     ORDER_ID=? ,"
                + "     DATA=? , "
                + "     PLAIN_TEXT=? , "
                + "     TITULO=? , "
                + "     SUBTITULO=? ,  "
                + "     DESCRIPCION=?, "
                + "     HTML_VIDEO=?, "
                + "     THUMBNAIL_URL=? , "
                + "     RANGO_INI=? ,  "
                + "     RANGO_FIN=? ,  "
                + "     FILE2 = ?,"
                + "     IMAGE = ?,"
                + "     IMAGE_THUMB = ?, "
                + "     TIPOVIDEO = ?, "
                + "     IDVIDEO = ?, "
                + "     IDVIDEODB = ?, "
                + "     IDLINKEDEXCEL = ? "
                + "  WHERE OBJECT_ID=?    ";
        String sqlDelObject = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW WHERE OBJECT_ID=? ";

        ResultSet rsNextObjectId = null;

        int objectId = 0;
        int n = 0;

        if (parentSection == null) {
            return;
        }

        if (parentSection.getChildSectionsAsList() == null || parentSection.getChildSectionsAsList().size() <= 0) {
            return;
        }

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pstInsObject = conn.prepareStatement(sqlInsObject, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstUpdObject = conn.prepareStatement(sqlUpdObject);
                PreparedStatement pstDeleteObject = conn.prepareStatement(sqlDelObject);) {
            if (conn == null) {
                return;
            }

            pstInsObject.setInt(StatementConstant.SC_1.get(), moduleBO.getDocumentModuleId());

            for (ModuleSectionBO sectionBO : parentSection.getChildSectionsAsList()) {

                if (sectionBO.getLstObjects() != null) {
                    for (ObjectInfoBO objectBO : sectionBO.getLstObjects()) {
                        if (isNewVersion || objectBO.getObjectId() <= 0) {

                            if (objectBO.isDelete()) {
                                continue;
                            }

                            pstInsObject.setInt(StatementConstant.SC_2.get(), objectBO.getSectionId());
                            pstInsObject.setShort(StatementConstant.SC_3.get(), (short) objectBO.getWidth());
                            pstInsObject.setShort(StatementConstant.SC_4.get(), (short) objectBO.getHeight());
                            pstInsObject.setInt(StatementConstant.SC_5.get(), objectBO.getObjType());
                            pstInsObject.setString(StatementConstant.SC_6.get(), objectBO.getFile());
                            pstInsObject.setString(StatementConstant.SC_7.get(), objectBO.getUrl());
                            pstInsObject.setShort(StatementConstant.SC_8.get(), (short) objectBO.getOrderId());

                            pstInsObject.setObject(StatementConstant.SC_9.get(), objectBO.getData(), java.sql.Types.CLOB);
                            pstInsObject.setObject(StatementConstant.SC_10.get(), objectBO.getPlain_text(), java.sql.Types.CLOB);
                            pstInsObject.setString(StatementConstant.SC_11.get(), objectBO.getTitulo());
                            pstInsObject.setString(StatementConstant.SC_12.get(), objectBO.getSubTitulo());
                            pstInsObject.setString(StatementConstant.SC_13.get(), objectBO.getComentarios());
                            pstInsObject.setString(StatementConstant.SC_14.get(), objectBO.getHtmlVideo());
                            pstInsObject.setString(StatementConstant.SC_15.get(), objectBO.getThumbnailUrl());
                            pstInsObject.setString(StatementConstant.SC_16.get(), objectBO.getRangoIni());
                            pstInsObject.setString(StatementConstant.SC_17.get(), objectBO.getRangoFin());

                            pstInsObject.setString(StatementConstant.SC_18.get(), objectBO.getFile2());

                            byte imageArray[] = Utilerias.imageToByteArray(objectBO.getImage());
                            byte thumbImageArray[] = Utilerias.imageToByteArray(objectBO.getImageThumb());

                            if (imageArray == null) {
                                pstInsObject.setNull(StatementConstant.SC_19.get(), java.sql.Types.BLOB);
                            } else {
                                pstInsObject.setBlob(StatementConstant.SC_19.get(), new SerialBlob(imageArray));
                            }

                            if (thumbImageArray == null) {
                                pstInsObject.setNull(StatementConstant.SC_20.get(), java.sql.Types.BLOB);
                            } else {
                                pstInsObject.setBlob(StatementConstant.SC_20.get(), new SerialBlob(thumbImageArray));
                            }

                            pstInsObject.setString(StatementConstant.SC_21.get(), objectBO.getTipoVideo());
                            pstInsObject.setString(StatementConstant.SC_22.get(), objectBO.getIdVideo());
                            pstInsObject.setString(StatementConstant.SC_23.get(), objectBO.getIdVideoDb());
                            pstInsObject.setInt(StatementConstant.SC_24.get(), objectBO.getIdLinkedExcel());

                            n = pstInsObject.executeUpdate();
                            if (n > 0) {

                                rs = pstInsObject.getGeneratedKeys();
                                if (rs.next()) {
                                    objectId = rs.getInt(StatementConstant.SC_1.get());
                                    objectBO.setObjectId(objectId);
                                }
                                rs.close();

                            }

                        } else {
                            if (objectBO.isDelete()) {
                                pstDeleteObject.setInt(1, objectBO.getObjectId());
                                n = pstDeleteObject.executeUpdate();
                            } else {
                                pstUpdObject.setShort(1, objectBO.getWidth());
                                pstUpdObject.setShort(2, objectBO.getHeight());
                                pstUpdObject.setString(3, objectBO.getFile());
                                pstUpdObject.setString(4, objectBO.getUrl());
                                pstUpdObject.setShort(5, (short) objectBO.getOrderId());

                                if (objectBO.getData() != null && objectBO.getData().toCharArray() != null) {
                                    pstUpdObject.setClob(6, new javax.sql.rowset.serial.SerialClob(objectBO.getData().toCharArray()));
                                } else {
                                    pstUpdObject.setClob(6, new javax.sql.rowset.serial.SerialClob("".toCharArray()));
                                }

                                if (objectBO.getPlain_text() != null && objectBO.getPlain_text().toCharArray() != null) {
                                    pstUpdObject.setClob(7, new javax.sql.rowset.serial.SerialClob(objectBO.getPlain_text().toCharArray()));
                                } else {
                                    pstUpdObject.setClob(7, new javax.sql.rowset.serial.SerialClob("".toCharArray()));
                                }

                                pstUpdObject.setString(8, objectBO.getTitulo());
                                pstUpdObject.setString(9, objectBO.getSubTitulo());
                                pstUpdObject.setString(10, objectBO.getComentarios());

                                pstUpdObject.setString(11, objectBO.getHtmlVideo() != null ? objectBO.getHtmlVideo().replace("{HEIGHT}", String.valueOf(objectBO.getHeight())).replace("{WIDTH}", String.valueOf(objectBO.getWidth())) : objectBO.getHtmlVideo());
                                pstUpdObject.setString(12, objectBO.getThumbnailUrl());

                                pstUpdObject.setString(13, objectBO.getRangoIni());
                                pstUpdObject.setString(14, objectBO.getRangoFin());
                                pstUpdObject.setString(15, objectBO.getFile2());

                                //Nuevos parametros para insertar imagen e imagen_thumb
                                byte imageArray[] = Utilerias.imageToByteArray(objectBO.getImage());
                                byte thumbImageArray[] = Utilerias.imageToByteArray(objectBO.getImageThumb());

                                if (imageArray == null) {
                                    pstUpdObject.setNull(StatementConstant.SC_16.get(), java.sql.Types.BLOB);
                                } else {
                                    pstUpdObject.setBlob(StatementConstant.SC_16.get(), new SerialBlob(imageArray));
                                }

                                if (thumbImageArray == null) {
                                    pstUpdObject.setNull(StatementConstant.SC_17.get(), java.sql.Types.BLOB);
                                } else {
                                    pstUpdObject.setBlob(StatementConstant.SC_17.get(), new SerialBlob(thumbImageArray));
                                }

                                pstUpdObject.setString(18, objectBO.getTipoVideo());
                                pstUpdObject.setString(19, objectBO.getIdVideo());
                                pstUpdObject.setString(20, objectBO.getIdVideoDb());
                                pstUpdObject.setInt(21, objectBO.getIdLinkedExcel());

                                pstUpdObject.setInt(22, objectBO.getObjectId());
                                n = pstUpdObject.executeUpdate();
                            }
                        }
                    }
                } else {
                    if (sectionBO.getChildSectionsAsList() != null) {
                        upsertObjectsSection(documentId, moduleBO, sectionBO, isNewVersion);
                    }
                }
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            try {
                if (rsNextObjectId != null) {
                    rsNextObjectId.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }
    }

    protected void upsertObjects(int documentId, ModuleBO moduleBO, boolean isNewVersion) {
        if (moduleBO.getRootSection() == null || moduleBO.getRootSection().getChildSectionsAsList() == null) {
            return;
        }
        upsertObjectsSection(documentId, moduleBO, moduleBO.getRootSection(), isNewVersion);
    }

    protected void upsertModules(DocumentBO docBO, boolean isNewVersion) {

        //String sqlDelModules;
        String sqlInsDocModule = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW (  DOC_VERSION_ID , DOCUMENT_ID, MODULE_ID, HOJA , ORDER_ID, TEMPLATE_ID , DOCUMENT_MODULE_ID_ORIG , HEADER_OR_SECTION , IS_SECTION) "
                + "  VALUES ( ? , ?, ?, ?, ?, ? , ? , ?, ?) ";

        String sqlUpdDocModule = " UPDATE " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW  SET  ORDER_ID= ? , HOJA = ? "
                + "WHERE DOCUMENT_MODULE_ID= ? ";

        Map<Integer, HojaBO> mapHojas;
        ResultSet rs;

        int documentId = 0;
        int documentModuleId = 0;
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                //Statement stDelModules = conn.createStatement();
                PreparedStatement pstInsDocModule = conn.prepareStatement(sqlInsDocModule, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstUpdDocModule = conn.prepareStatement(sqlUpdDocModule);) {

            if (docBO == null || conn == null) {
                return;
            }

            mapHojas = docBO.getMapHojas();
            documentId = docBO.getDocumentId();
            if( docBO.isCollaborative() ){
                checkModulesForDeleteCollab(documentId, docBO.getDocVersionId(), mapHojas, conn); 
            }else{
                checkModulesForDelete(documentId, docBO.getDocVersionId(), mapHojas, conn);
            }

            //sqlDelModules = " DELETE FROM DOCUMENT_MODULE_NEW  WHERE DOCUMENT_MODULE_ID= ? ";
            int n = 0;
            //stDelModules.executeUpdate(sqlDelModules);

            pstInsDocModule.setInt(StatementConstant.SC_1.get(), docBO.getDocVersionId());
            pstInsDocModule.setInt(StatementConstant.SC_2.get(), documentId);
            

            List<ModuleBO> lstModules = null;
            Set set = mapHojas.keySet();
            Iterator it = set.iterator();
            Integer hoja = null;
            HojaBO hojaBO = null;
            short numHoja = 0;
            while (it.hasNext()) {
                //numHoja++;
                hoja = (Integer) it.next();
                hojaBO = (HojaBO) mapHojas.get(hoja);
                if (hojaBO == null) {
                    continue;
                }
                numHoja = hojaBO.getHoja();
                lstModules = hojaBO.getLstModules();

                if (lstModules != null) {
                    short i = 1;
                    for (ModuleBO moduleBO : lstModules) {

                        /* TODO if anterior con doble ampersan */
                        // if (isNewVersion == false & moduleBO.getDocumentModuleId() > 0) {
                        if (isNewVersion == false && moduleBO.getDocumentModuleId() > 0) {
                            pstUpdDocModule.setShort(1, i);
                            pstUpdDocModule.setShort(2, moduleBO.getHoja());
                            pstUpdDocModule.setInt(3, moduleBO.getDocumentModuleId());
                            n = pstUpdDocModule.executeUpdate();
                            if (n > 0) {
                                System.out.println("Se actualizo el document_module_id " + moduleBO.getDocumentModuleId());
                                this.upsertObjects(documentId, moduleBO, isNewVersion);
                            }

                        } else {
                            //  pstInsDocModule.setInt(1, moduleBO.getDocumentVersionId() );
                            pstInsDocModule.setInt(StatementConstant.SC_3.get(), moduleBO.getModuleId());
                            pstInsDocModule.setShort(StatementConstant.SC_4.get(), numHoja);
                            pstInsDocModule.setShort(StatementConstant.SC_5.get(), i);
                            pstInsDocModule.setInt(StatementConstant.SC_6.get(), moduleBO.getTemplateId());
                            pstInsDocModule.setInt(StatementConstant.SC_7.get(), moduleBO.getOrigDocModuleId() );
                            pstInsDocModule.setInt(StatementConstant.SC_8.get(), moduleBO.isHeaderOrSection() ? 1 : 0);
                            pstInsDocModule.setInt(StatementConstant.SC_9.get(), moduleBO.isSection() ? 1 : 0);
                            n = pstInsDocModule.executeUpdate();
                            moduleBO.setOrder(i);
                            moduleBO.setHoja(numHoja);
                            if (n > 0) {

                                rs = pstInsDocModule.getGeneratedKeys();
                                if (rs.next()) {
                                    documentModuleId = rs.getInt(StatementConstant.SC_1.get());
                                    moduleBO.setDocumentModuleId(documentModuleId);
                                }
                                rs.close();

                                this.upsertObjects(documentId, moduleBO, isNewVersion);
                            }
                        }
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }

    public void updateDocument(DocumentBO docBO) {
        try {
//            upsertModules(docBO);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {

        }
    }

    public DocumentBO getDocument(int documentId) throws SQLException {

        DocumentBO documentBO = null;
        ModuleSectionBO sectionBO = null;
        Statement stDocument = null;
        String sqlDocument = null;
        ResultSet rsDocument = null;

        Statement stHojas = null;
        ResultSet rsHojas = null;

        PreparedStatement pstObjects = null;
        ResultSet rsObjects = null;
        String sqlObjects;

        Statement stModules = null;
        ResultSet rsModules = null;
        String sqlModules = null;
        List<ModuleBO> lstModules = null;
        int rootSectionId = 0;
        List<ObjectInfoBO> lstObjectsInfo = new ArrayList<>();
        List<ObjectInfoBO> lstObjects = null;

        Map<Integer, HojaBO> mapHojas = new TreeMap<>();

        PreparedStatement pstModule = null;
        ResultSet rsModule = null;

        Connection conn = ConnectionDB.getInstance().getConnection();

        try {
            sqlDocument = "SELECT               "
                    + "  D.DOCUMENT_ID ,   "
                    + "  D.DOCUMENT_NAME , "
                    + "  D.FILE_NAME,  "
                    + "	 D.IDDOCUMENT_TYPE,  "
                    + "	 D.IDMARKET,  "
                    + "	 D.IDLANGUAGE  "
                    + "  FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT D "
                    + "  WHERE "
                    + "  D.DOCUMENT_ID=" + documentId;

            sqlObjects = "  SELECT * FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECTS DO "
                    + "  WHERE DO.DOCUMENT_ID=" + documentId + " "
                    + "  AND DO.SECTION_ID=? "
                    + "  AND DO.SEGMENT_ROOT=? "
                    + "  AND DO.MODULE_ORDER_ID=? "
                    + "  AND DO.HOJA=?  "
                    + "  ORDER BY DO.ORDER_ID ";

            stDocument = conn.createStatement();
            rsDocument = stDocument.executeQuery(sqlDocument);

            stHojas = conn.createStatement();
            rsHojas = stHojas.executeQuery("SELECT MAX( HOJA) FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE WHERE DOCUMENT_ID=" + documentId + "   ");
            HojaBO hojaBO = null;
            int numHojas = 0;
            if (rsHojas.next()) {
                numHojas = rsHojas.getInt(1);
            }
            for (int i = 1; i <= numHojas; i++) {
                hojaBO = new HojaBO();
                hojaBO.setHoja((short) i);
                if (mapHojas == null) {
                    mapHojas = new TreeMap<>();
                }
                mapHojas.put(i, hojaBO);
            }

            if (rsDocument.next()) {

                documentBO = new DocumentBO();
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setIdDocType(rsDocument.getInt("idDocument_type"));
                documentBO.setIdMarket(rsDocument.getInt("idMarket"));
                documentBO.setIdLanguage(rsDocument.getInt("idLanguage"));
                //  documentBO.setWidht( rsDocument.getShort("document_width"));
                //  documentBO.setHeight(rsDocument.getShort("document_height"));
                //  documentBO.setLayoutType(rsDocument.getString("tipo_layout"));

            } else {

                return null;
            }

            pstModule = conn.prepareStatement("SELECT * FROM " + GlobalDefines.DB_SCHEMA + "TEMPLATE_SECTIONS WHERE SECTION_ID=? ");

            /*
             sqlModules = " SELECT * FROM TEMPLATE_SECTIONS TS , DOCUMENT_MODULE DM "
             + " WHERE TS.SECTION_PARENT_ID=DM.SEGMENT_ROOT "
             + " AND DM.DOCUMENT_ID=" + documentId + "  "
             + " ORDER BY DM.HOJA ,  DM.ORDER_ID, DM.SEGMENT_ROOT  "; */
            sqlModules = "   SELECT * FROM " + GlobalDefines.DB_SCHEMA + "TEMPLATE_SECTIONS TS , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE DM  "
                    + "   WHERE TS.SECTION_ID_MODULE=DM.SEGMENT_ROOT  "
                    + "  AND DM.DOCUMENT_ID=" + documentId + "                       "
                    + "  ORDER BY DM.HOJA ,  DM.ORDER_ID, DM.SEGMENT_ROOT , TS.SECTION_PARENT_ID  ";

            stModules = conn.createStatement();

            pstObjects = conn.prepareStatement(sqlObjects);
            ModuleBO moduleBO = null;
            rsModules = stModules.executeQuery(sqlModules);
            ModuleSectionBO docSecBO = null;

            int moduleAnt = -1;
            short hojaAnt = -1;
            List<ModuleSectionBO> lstChildsSections = null;
            Map<Integer, List<ModuleSectionBO>> mapSectByPar = null;
            while (rsModules.next()) {
                if (moduleAnt != rsModules.getInt("segment_root") || hojaAnt != rsModules.getShort("hoja")) {
                    moduleBO = new ModuleBO();
                    mapSectByPar = new TreeMap<>();
                    moduleBO.setOrder(rsModules.getShort("order_id"));
                    rootSectionId = rsModules.getInt("segment_root");
                    moduleBO.setRootSectionId(rootSectionId);
                    moduleBO.setTemplateId(rsModules.getInt("template_id"));
                    moduleBO.setHoja(rsModules.getShort("hoja"));

                    //pstModule.setInt(1, rsModules.getInt("segment_root"));
                    //rsModule = pstModule.executeQuery();
                    //if (rsModule.next()) {
                    sectionBO = new ModuleSectionBO();
                    sectionBO.setSectionId(rsModules.getInt("section_id"));
                    sectionBO.setType(rsModules.getString("tipo"));
                    sectionBO.setWidth(rsModules.getShort("width"));
                    sectionBO.setHeight(rsModules.getShort("height"));
                    //  sectionBO.setTemplateId(rsModules.getInt("template_id"));
                    sectionBO.setContentType(rsModules.getString("content_type"));
                    moduleBO.setRootSection(sectionBO);
                    lstChildsSections = new ArrayList<>();

                    sectionBO.setChildSectionsAsList(lstChildsSections);
                    mapSectByPar.put(new Integer(rsModules.getInt("section_id")), lstChildsSections);
                    //}
                    //rsModule.close();

                    hojaBO = mapHojas.get("" + rsModules.getShort("hoja"));
                    lstModules = hojaBO.getLstModules();

                    if (lstModules == null) {
                        lstModules = new ArrayList<>();
                        hojaBO.setLstModules(lstModules);
                    }
                    lstModules.add(moduleBO);
                    moduleAnt = rsModules.getInt("segment_root");
                    hojaAnt = rsModules.getShort("hoja");
                    continue;

                }

                docSecBO = new ModuleSectionBO();
                docSecBO.setType(rsModules.getString("tipo"));
                docSecBO.setSectionId(rsModules.getInt("section_id"));
                docSecBO.setWidth(rsModules.getShort("width"));
                docSecBO.setHeight(rsModules.getShort("height"));
                docSecBO.setSectionName(rsModules.getString("sec_name"));
                // docSecBO.setParentSection(rsModules.getInt("section_parent_id"));
                docSecBO.setSectionParentId(rsModules.getInt("section_parent_id"));
                // docSecBO.setTemplateId(rsModules.getInt("template_id"));
                docSecBO.setContentType(rsModules.getString("content_type"));
                // docSecBO.setSectionIdModule(rsModules.getInt("section_id_module"));

                lstChildsSections = mapSectByPar.get(rsModules.getInt("section_parent_id"));
                if (lstChildsSections != null) {
                    lstChildsSections.add(docSecBO);
                }

                lstChildsSections = new ArrayList<>();

                docSecBO.setChildSectionsAsList(lstChildsSections);
                mapSectByPar.put(rsModules.getInt("section_id"), lstChildsSections);

                if (GlobalDefines.SEC_TYPE_CELL.equals(docSecBO.getType())) {
                    pstObjects.setInt(1, docSecBO.getSectionId());
                    pstObjects.setInt(2, moduleBO.getRootSectionId());
                    pstObjects.setShort(3, moduleBO.getOrder());
                    pstObjects.setShort(4, moduleBO.getHoja());

                    rsObjects = pstObjects.executeQuery();

                    lstObjects = new ArrayList<>();
                    ObjectInfoBO object = null;
                    docSecBO.setLstObjects(lstObjects);

                    while (rsObjects.next()) {
                        object = new ObjectInfoBO();
                        object.setFile(rsObjects.getString("file"));
                        object.setUrl(rsObjects.getString("url"));
                        object.setSectionId(rsObjects.getInt("section_id"));
                        object.setObjType(rsObjects.getInt("tipo"));
                        object.setObjectId(rsObjects.getInt("object_id"));
                        object.setFile2(rsObjects.getString("file2"));

                        if (object.getObjType() == GlobalDefines.OBJ_TYPE_TEXT) {
                            moduleBO.setHasText(true);
                        }

                        object.setWidth(rsObjects.getShort("width"));
                        object.setHeight(rsObjects.getShort("height"));
                        object.setTitulo(rsObjects.getString("titulo"));
                        object.setSubTitulo(rsObjects.getString("subtitulo"));
                        object.setComentarios(rsObjects.getString("descripcion"));
                        object.setTipoVideo(rsObjects.getString("tipovideo"));
                        object.setIdVideo(rsObjects.getString("idvideo"));
                        object.setIdVideoDb(rsObjects.getString("idvideodb"));
                        object.setRangoIni(rsObjects.getString("rango_ini"));
                        object.setRangoFin(rsObjects.getString("rango_fin"));

                        String valor = rsObjects.getString("data");
                        /*if (rsObjects.getClob("data") != null && rsObjects.getClob("data").length() > 0) {
                            valor = rsObjects.getClob("data").getSubString(1, (int) rsObjects.getClob("data").length() - 1);

                        }*/
                        object.setHtmlVideo(rsObjects.getString("html_video"));
                        object.setThumbnailUrl(rsObjects.getString("THUMBNAIL_URL"));

                        object.setData(valor);
                        lstObjects.add(object);
                    }
                }
            }
            documentBO.setMapHojas(mapHojas);

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            throw ex;
        } finally {
            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }

                if (stDocument != null) {
                    stDocument.close();
                }

                if (rsModules != null) {
                    rsModules.close();
                    rsModules = null;
                }

                if (stModules != null) {
                    stModules.close();
                    stModules = null;
                }

                if (rsObjects != null) {
                    rsObjects.close();
                }

                if (rsModule != null) {
                    rsModule.close();
                }

                if (pstObjects != null) {
                    pstObjects.close();
                }

                if (pstModule != null) {
                    pstModule.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }

        return documentBO;
    }

    public Map<String, Map<String, StringBuilder>> getConcatObjsDocument(int documentId) {
        Connection conn = null;
        Statement stConcDoc = null;
        ResultSet rsConcDoc = null;
        try {
            Map<String, Map<String, StringBuilder>> mObjConc = new HashMap();
            String sqlConcDoc = " SELECT ID_DESTINO, ID_ORIGEN, TEXTO FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONCAT DC WHERE DC.DOCUMENT_ID=" + documentId;
            conn = ConnectionDB.getInstance().getConnection();
            stConcDoc = conn.createStatement();
            rsConcDoc = stConcDoc.executeQuery(sqlConcDoc);

            if (rsConcDoc != null) {
                while (rsConcDoc.next()) {
                    String idDestino = rsConcDoc.getString("ID_DESTINO");
                    String idOrigen = rsConcDoc.getString("ID_ORIGEN");
                    String texto = rsConcDoc.getString("TEXTO");
                    Map<String, StringBuilder> mOrigen = new HashMap();
                    mOrigen.put(idOrigen, new StringBuilder(texto));
                    mObjConc.put(idDestino, mOrigen);
                }

                if (!mObjConc.isEmpty()) {
                    return mObjConc;
                }
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {

            try {
                if (rsConcDoc != null) {
                    rsConcDoc.close();
                }

                if (stConcDoc != null) {
                    stConcDoc.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }

        }
        return null;
    }

    public void saveConcatObjsDocument(int documentId, Map<String, Map<String, StringBuilder>> mObjConc) {

        Connection conn = null;
        Statement stConcDoc = null;
        ResultSet rsConcDoc = null;
        try {
            conn = ConnectionDB.getInstance().getConnection();
            deleteConcatObjsDocument(documentId);

            stConcDoc = conn.createStatement();
            for (Map.Entry<String, Map<String, StringBuilder>> entDest : mObjConc.entrySet()) {
                String strIdOrig = "";
                StringBuilder strTexto = new StringBuilder();
                for (Map.Entry<String, StringBuilder> entOrig : entDest.getValue().entrySet()) {
                    strIdOrig = entOrig.getKey();
                    strTexto = entOrig.getValue();
                }

                String sqlConcDoc = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONCAT (DOCUMENT_ID, ID_DESTINO, ID_ORIGEN, TEXTO) "
                        + "VALUES (" + documentId + ", '" + entDest.getKey() + "', '" + strIdOrig + "', '" + strTexto.toString() + "')";

                stConcDoc.execute(sqlConcDoc);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {

            try {
                if (rsConcDoc != null) {
                    rsConcDoc.close();
                }

                if (stConcDoc != null) {
                    stConcDoc.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                Utilerias.logger(getClass()).error(e);
            }

        }
    }

    public void deleteConcatObjsDocument(int documentId) {
        Connection conn = null;
        Statement stConcDoc = null;
        ResultSet rsConcDoc = null;
        try {
            conn = ConnectionDB.getInstance().getConnection();
            stConcDoc = conn.createStatement();

            String sqlConcDoc = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONCAT DC WHERE DC.DOCUMENT_ID = " + documentId;
            stConcDoc.execute(sqlConcDoc);
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {

            try {
                if (rsConcDoc != null) {
                    rsConcDoc.close();
                }

                if (stConcDoc != null) {
                    stConcDoc.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                Utilerias.logger(getClass()).error(e);
            }

        }
    }

    public DocumentBO getDocument(int documentId, int version  , boolean deleted ) throws SQLException {

        DocumentBO documentBO = null;
        ModuleSectionBO sectionBO = null;
        Statement stDocument = null;
        String sqlDocument = null;
        ResultSet rsDocument = null;

        Statement stLastVersion = null;
        ResultSet rsLastVersion = null;

        Statement stHojas = null;
        ResultSet rsHojas = null;

        PreparedStatement pstObjects = null;
        ResultSet rsObjects = null;
        String sqlObjects;

        Statement stModules = null;
        ResultSet rsModules = null;
        String sqlModules = null;
        List<ModuleBO> lstModules = null;
        //  int rootSectionId = 0;
        List<ObjectInfoBO> lstObjectsInfo = new ArrayList<>();
        List<ObjectInfoBO> lstObjects = null;

        Map<Integer, HojaBO> mapHojas = new TreeMap<>();

        PreparedStatement pstModule = null;
        ResultSet rsModule = null;

        Connection conn = ConnectionDB.getInstance().getConnection();
        boolean isLastVersion = (version == -1);
        int versionGet = 0;

        try {

            String sqlLastVersion = " SELECT MAX(VERSION) FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV WHERE DV.DOCUMENT_ID=" + documentId;
            if (isLastVersion) {

                stLastVersion = conn.createStatement();
                rsLastVersion = stLastVersion.executeQuery(sqlLastVersion);
                if (rsLastVersion.next()) {
                    versionGet = rsLastVersion.getInt(1);
                }

            } else {
                versionGet = version;
            }

            sqlDocument = "SELECT               "
                    + "  D.DOCUMENT_ID ,   "
                    + "  D.DOCUMENT_NAME , "
                    + "  D.FILE_NAME,  "
                    + "	 D.IDDOCUMENT_TYPE,  "
                    + "	 D.IDMARKET,  "
                    + "  D.IDSUBJECT ,  "
                    + "	 D.IDLANGUAGE , "
                    + "	 D.FAVORITE, "
                    + "  D.COLLABORATIVE ,  "
                    + "  D.COLLABORATIVE_TYPE , "
                    + "  D.PUBLISH_NAME , "
                    + "  DV.DOC_VERSION_ID ,  "
                    + "  DV.VERSION  ,      "
                    + "  MA.NAME AS MARKET , "
                    + "  DT.NAME AS DOCUMENT_TYPE ,  "
                    //+ "  SU.NAME AS SUBJECT , "
                    + "  D.NAME_AUTHOR, D.ID_AUTHOR "
                    + "  FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW D , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV , " + GlobalDefines.DB_SCHEMA + "MARKET MA, "
                    //+ GlobalDefines.DB_SCHEMA + "SUBJECT SU , " 
                    + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT "
                    + "  WHERE "
                    + "  D.DOCUMENT_ID=" + documentId
                    + "  AND D.DOCUMENT_ID=DV.DOCUMENT_ID "
                    + "  AND DV.VERSION=  " + versionGet
                    + "  AND D.IDMARKET=MA.IDMIVECTOR_REAL  "
                    + "  AND D.IDDOCUMENT_TYPE=DT.IDDOCUMENT_TYPE  ";
            //+ "  AND D.IDSUBJECT=SU.IDSUBJECT  ";

            sqlObjects = "  SELECT DO.* FROM "
                    + GlobalDefines.DB_SCHEMA + "DOCUMENT_OBJECT_NEW DO ,  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV  "
                    + "  WHERE DV.DOCUMENT_ID=" + documentId + "                  "
                    + "  AND DV.DOC_VERSION_ID=DM.DOC_VERSION_ID  "
                    + "  AND DO.DOCUMENT_MODULE_ID=DM.DOCUMENT_MODULE_ID "
                    + "   AND DV.VERSION=  " + versionGet
                    + "  AND DM.DOCUMENT_MODULE_ID= ? "
                    + "  AND DO.SECTION_ID=?  ";
                    
            if( deleted==false ){
                sqlObjects+= "  AND ( DM.ESTATUS IS NULL OR DM.ESTATUS<>'D' ) " ;
            } 
                    
                sqlObjects+= "  ORDER BY DM.ORDER_ID , DO.ORDER_ID ";

            stDocument = conn.createStatement();
            rsDocument = stDocument.executeQuery(sqlDocument);

            stHojas = conn.createStatement();
            //  rsHojas = stHojas.executeQuery("SELECT MAX( HOJA) FROM DOCUMENT_MODULE WHERE DOCUMENT_ID=" + documentId + "   ");
            String sqlHojas = " SELECT MAX(HOJA) FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM  , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV "
                    + " WHERE DM.DOC_VERSION_ID=DV.DOC_VERSION_ID "
                    + " AND DV.DOCUMENT_ID=" + documentId
                    + " AND DV.VERSION=" + versionGet;
            
            
            if( deleted==false ){
                sqlHojas+= "  AND ( DM.ESTATUS IS NULL OR DM.ESTATUS<>'D' ) " ;
            }             
            
            
            
            
            rsHojas = stHojas.executeQuery(sqlHojas);

            HojaBO hojaBO = null;
            int numHojas = 0;
            if (rsHojas.next()) {
                numHojas = rsHojas.getInt(1);
            }
            for (int i = 1; i <= numHojas; i++) {
                hojaBO = new HojaBO();
                hojaBO.setHoja((short) i);
                if (mapHojas == null) {
                    mapHojas = new TreeMap<>();
                }
                mapHojas.put(i, hojaBO);
            }

            if (rsDocument.next()) {

                if (rsDocument.getShort("collaborative") == 1) {
                    documentBO = new DocumentCollabBO();
                    documentBO.setCollaborative(true);
                    documentBO.setCollaborativeType(rsDocument.getString("collaborative_type"));
                } else {
                    documentBO = new DocumentBO();
                }
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setIdDocType(rsDocument.getInt("idDocument_type"));
                documentBO.setIdMarket(rsDocument.getInt("idMarket"));
                documentBO.setIdLanguage(rsDocument.getInt("idLanguage"));
                documentBO.setDocVersionId(rsDocument.getInt("doc_version_id"));
                documentBO.setVersion(rsDocument.getInt("version"));
                documentBO.setPublishName(rsDocument.getString("PUBLISH_NAME"));

                documentBO.setAuthorName(rsDocument.getString("name_author"));
                documentBO.setIdAuthor(rsDocument.getInt("ID_AUTHOR"));

                documentBO.setFavorite(rsDocument.getInt("favorite") > 0);

                MarketBO ma = new MarketBO();
                ma.setIdMarket(rsDocument.getInt("idMarket"));
                ma.setIdMiVector_real(String.valueOf(rsDocument.getInt("idMarket")));
                ma.setName(rsDocument.getString("market"));
                documentBO.setMarketBO(ma);

                DocumentTypeBO dt = new DocumentTypeBO();
                dt.setIddocument_type(rsDocument.getInt("iddocument_type"));
                dt.setName(rsDocument.getString("document_type"));
                documentBO.setTipoBO(dt);

                int idSubject = rsDocument.getInt("IDSUBJECT");
                documentBO.setIdSubject(idSubject);

                if (idSubject > 0) {

                    SubjectDAO daoSubject = new SubjectDAO();
                    List<SubjectBO> listSubject = daoSubject.get(null);
                    SubjectBO su = null;

                    for (SubjectBO subBO : listSubject) {
                        if (subBO.getIdSubject() == idSubject) {
                            su = subBO;
                            break;
                        }
                    }

                    //su.setIdSubject(rsDocument.getInt("idsubject"));
                    //su.setName(rsDocument.getString("subject"));
                    documentBO.setSubjectBO(su);

                }

                //  documentBO.setWidht( rsDocument.getShort("document_width"));
                //  documentBO.setHeight(rsDocument.getShort("document_height"));
                //  documentBO.setLayoutType(rsDocument.getString("tipo_layout"));
            } else {

                return null;
            }

            pstModule = conn.prepareStatement("SELECT * FROM " + GlobalDefines.DB_SCHEMA + "TEMPLATE_SECTIONS WHERE SECTION_ID=? ");

            /*
             sqlModules = " SELECT * FROM TEMPLATE_SECTIONS TS , DOCUMENT_MODULE DM "
             + " WHERE TS.SECTION_PARENT_ID=DM.SEGMENT_ROOT "
             + " AND DM.DOCUMENT_ID=" + documentId + "  "
             + " ORDER BY DM.HOJA ,  DM.ORDER_ID, DM.SEGMENT_ROOT  "; */
            sqlModules = "   SELECT * FROM " + GlobalDefines.DB_SCHEMA + "MODULE_SECTIONS_NEW MS , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV  "
                    + "   WHERE MS.MODULE_ID=DM.MODULE_ID  "
                    + "AND  DV.DOCUMENT_ID=" + documentId + "                  "
                    + "  AND DV.DOC_VERSION_ID=DM.DOC_VERSION_ID  "
                    + //   "  AND DO.DOCUMENT_MODULE_ID=DM.DOCUMENT_MODULE_ID " +
                    "   AND DV.VERSION=  " + versionGet;
            
            if( deleted==false ){
                sqlModules+= "  AND ( DM.ESTATUS IS NULL OR DM.ESTATUS<>'D' ) " ;
            }              
                    
                    
            sqlModules+="  ORDER BY DM.HOJA ,  DM.ORDER_ID ,  DM.DOCUMENT_MODULE_ID ,     MS.SECTION_PARENT_ID  ";

            stModules = conn.createStatement();

            pstObjects = conn.prepareStatement(sqlObjects);
            ModuleBO moduleBO = null;
            rsModules = stModules.executeQuery(sqlModules);
            ModuleSectionBO docSecBO = null;

            int moduleAnt = -1;
            int docModuleIdAnt=-1;
            short hojaAnt = -1;
            int order = -1;
            List<ModuleSectionBO> lstChildsSections = null;
            Map<Integer, List<ModuleSectionBO>> mapSectByPar = null;
            while (rsModules.next()) {
                if (moduleAnt != rsModules.getInt("module_id") || hojaAnt != rsModules.getShort("hoja") || order != rsModules.getInt("order_id") 
                        ||  docModuleIdAnt != rsModules.getInt("document_module_id")     ) {
                    moduleBO = new ModuleBO();
                    mapSectByPar = new TreeMap<>();
                    moduleBO.setOrder(rsModules.getShort("order_id"));
                    moduleBO.setModuleId(rsModules.getInt("module_id"));
                    moduleBO.setOrigDocModuleId(rsModules.getInt("DOCUMENT_MODULE_ID_ORIG"));
                    // rootSectionId = rsModules.getInt("segment_root");
                    // moduleBO.setRootSectionId(rootSectionId);
                    moduleBO.setTemplateId(rsModules.getInt("template_id"));
                    moduleBO.setHoja(rsModules.getShort("hoja"));
                    moduleBO.setDocumentModuleId(rsModules.getInt("document_module_id"));

                    moduleBO.setWidth(rsModules.getShort("width"));
                    moduleBO.setHeight(rsModules.getShort("height"));
                    moduleBO.setHeaderOrSection(rsModules.getInt("HEADER_OR_SECTION") == 1);
                    moduleBO.setSection(rsModules.getInt("IS_SECTION") == 1);
                    //pstModule.setInt(1, rsModules.getInt("segment_root"));
                    //rsModule = pstModule.executeQuery();
                    //if (rsModule.next()) {
                    sectionBO = new ModuleSectionBO();
                    sectionBO.setSectionId(rsModules.getInt("section_id"));
                    sectionBO.setType(rsModules.getString("tipo"));
                    sectionBO.setWidth(rsModules.getShort("width"));
                    sectionBO.setHeight(rsModules.getShort("height"));
                    //  sectionBO.setTemplateId(rsModules.getInt("template_id"));
                    sectionBO.setContentType(rsModules.getString("content_type"));
                    moduleBO.setRootSection(sectionBO);
                    lstChildsSections = new ArrayList<>();

                    sectionBO.setChildSectionsAsList(lstChildsSections);
                    mapSectByPar.put(new Integer(rsModules.getInt("section_id")), lstChildsSections);
                    //}
                    //rsModule.close();

                    hojaBO = mapHojas.get(rsModules.getInt("hoja"));
                    lstModules = hojaBO.getLstModules();

                    if (lstModules == null) {
                        lstModules = new ArrayList<>();
                        hojaBO.setLstModules(lstModules);
                    }
                    lstModules.add(moduleBO);
                    moduleAnt = rsModules.getInt("module_id");
                    hojaAnt = rsModules.getShort("hoja");
                    order = rsModules.getInt("order_id");
                    docModuleIdAnt = rsModules.getInt("document_module_id");
                    
                    continue;

                }

                docSecBO = new ModuleSectionBO();
                docSecBO.setType(rsModules.getString("tipo"));
                docSecBO.setSectionId(rsModules.getInt("section_id"));
                docSecBO.setWidth(rsModules.getShort("width"));
                docSecBO.setHeight(rsModules.getShort("height"));
                docSecBO.setSectionName(rsModules.getString("sec_name"));
                // docSecBO.setParentSection(rsModules.getInt("section_parent_id"));
                docSecBO.setSectionParentId(rsModules.getInt("section_parent_id"));
                // docSecBO.setTemplateId(rsModules.getInt("template_id"));
                docSecBO.setContentType(rsModules.getString("content_type"));
                // docSecBO.setSectionIdModule(rsModules.getInt("section_id_module"));
                docSecBO.setModuleId(rsModules.getInt("module_id"));
                docSecBO.setRows(rsModules.getInt("rows"));

                lstChildsSections = mapSectByPar.get(rsModules.getInt("section_parent_id"));
                if (lstChildsSections != null) {
                    lstChildsSections.add(docSecBO);
                }

                lstChildsSections = new ArrayList<>();

                docSecBO.setChildSectionsAsList(lstChildsSections);
                mapSectByPar.put(rsModules.getInt("section_id"), lstChildsSections);

                if (GlobalDefines.SEC_TYPE_CELL.equals(docSecBO.getType())) {

                    pstObjects.setInt(1, moduleBO.getDocumentModuleId());
                    pstObjects.setInt(2, docSecBO.getSectionId());

                    //  pstObjects.setShort(3, moduleBO.getOrder());
                    rsObjects = pstObjects.executeQuery();

                    lstObjects = new ArrayList<>();
                    ObjectInfoBO object = null;
                    docSecBO.setLstObjects(lstObjects);

                    ModuleSectionBO sectionText = null;
                    ModuleSectionBO sectionTextAndBullet = null;

                    while (rsObjects.next()) {
                        sectionText = null;
                        object = new ObjectInfoBO();
                        object.setFile(rsObjects.getString("file"));
                        object.setUrl(rsObjects.getString("url"));
                        object.setSectionId(rsObjects.getInt("section_id"));
                        object.setObjType(rsObjects.getInt("tipo"));
                        object.setObjectId(rsObjects.getInt("object_id"));
                        object.setFile2(rsObjects.getString("file2"));

                        if (object.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE
                                || object.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
                            if (documentBO.getFirstImage() == null) {
                                documentBO.setFirstImage(object);
                            }
                        }

                        if (object.getObjType() == GlobalDefines.OBJ_TYPE_TEXT) {
                            moduleBO.setHasText(true);

                            if (sectionText == null) {
                                sectionText = docSecBO;
                            }

                        }

                        if ((object.getObjType() == GlobalDefines.OBJ_TYPE_BULLET) || (object.getObjType() == GlobalDefines.OBJ_TYPE_TEXT)) {

                            if (sectionTextAndBullet == null) {
                                sectionTextAndBullet = docSecBO;
                            }

                        }

                        /* Modificacion para obtener WIDTH Y HEIGHT, de los módulos */
                        object.setWidth(docSecBO.getWidth());
                        object.setHeight(docSecBO.getHeight());
                        //object.setWidth(rsObjects.getShort("width"));
                        //object.setHeight(rsObjects.getShort("height"));
                        /* -------------------------------------------- */

                        object.setTitulo(rsObjects.getString("titulo"));
                        object.setSubTitulo(rsObjects.getString("subtitulo"));
                        object.setComentarios(rsObjects.getString("descripcion"));
                        object.setTipoVideo(rsObjects.getString("tipovideo"));
                        object.setIdVideo(rsObjects.getString("idvideo"));
                        object.setIdVideoDb(rsObjects.getString("idvideodb"));
                        object.setRangoIni(rsObjects.getString("rango_ini"));
                        object.setRangoFin(rsObjects.getString("rango_fin"));
                        object.setOrderId(rsObjects.getInt("order_id"));

                        String valor = rsObjects.getString("data");
                        /*if (rsObjects.getClob("data") != null && rsObjects.getClob("data").length() > 0) {
                            valor = rsObjects.getClob("data").getSubString(1, (int) rsObjects.getClob("data").length() - 1);

                        }*/
                        object.setHtmlVideo(rsObjects.getString("html_video"));
                        object.setThumbnailUrl(rsObjects.getString("THUMBNAIL_URL"));
                        object.setPlain_text(rsObjects.getString("PLAIN_TEXT"));
                        object.setIdLinkedExcel(rsObjects.getInt("IDLINKEDEXCEL"));

                        byte[] imageArray = rsObjects.getBytes("IMAGE");
                        byte[] imageThumbArray = rsObjects.getBytes("IMAGE_THUMB");

                        object.setImage(Utilerias.byteArrayToImage(imageArray));
                        object.setImageThumb(Utilerias.byteArrayToImage(imageThumbArray));

                        object.setData(valor);

                        lstObjects.add(object);
                    }

                    if (sectionText != null) {
                        if (moduleBO.getLstSectionsText() == null) {
                            moduleBO.setLstSectionsText(new ArrayList<>());
                        }
                        moduleBO.getLstSectionsText().add(sectionText);
                    }

                    if (sectionTextAndBullet != null) {
                        if (moduleBO.getLstSectionsTextandBullet() == null) {
                            moduleBO.setLstSectionsTextandBullet(new ArrayList<>());
                        }
                        moduleBO.getLstSectionsTextandBullet().add(sectionTextAndBullet);
                    }

                }
            }
            documentBO.setMapHojas(mapHojas);

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            throw ex;
        } finally {
            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }

                if (stDocument != null) {
                    stDocument.close();
                }

                if (rsModules != null) {
                    rsModules.close();
                    rsModules = null;
                }

                if (stModules != null) {
                    stModules.close();
                    stModules = null;
                }

                if (rsObjects != null) {
                    rsObjects.close();
                }

                if (rsModule != null) {
                    rsModule.close();
                }

                if (pstObjects != null) {
                    pstObjects.close();
                }

                if (pstModule != null) {
                    pstModule.close();
                }

                if (rsLastVersion != null) {
                    rsLastVersion.close();
                }

                if (stLastVersion != null) {
                    stLastVersion.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }

        return documentBO;
    }

    public List<DocumentBO> searchCollaboratives(String name, int marketId, int docTypeId, String collabType) {

        DocumentBO documentBO = null;
        PreparedStatement stDocument = null;
        String sqlDocument = null;
        ResultSet rsDocument = null;
        List<DocumentBO> lstDocuments = null;
        Connection conn = null;

        try {
            conn = ConnectionDB.getInstance().getConnection();

            sqlDocument = "SELECT  "
                    + "	DOC.DOCUMENT_ID ,             "
                    + "	DOC.DOCUMENT_NAME ,           "
                    + "     DOC.FILE_NAME     ,  "
                    + "	DOC.WIDTH AS DOCUMENT_WIDTH ,   "
                    + "	DOC.HEIGHT AS DOCUMENT_HEIGHT ,  "
                    + "     DOC.FECHA_CREACION            ,  "
                    + "	DOC.STATUS AS DOCUMENT_STATUS , DOC.IDSUBJECT , SU.NAME AS SUBJECT_NAME   "
                    + "     , A.VERSION , DV2.DOC_VERSION_ID , MA.IDMIVECTOR_REAL , MA.NAME AS MARKET_NAME , DT.IDDOCUMENT_TYPE , DT.NAME AS DOCUMENT_TYPE_NAME "
                    + " FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC  " +
                    " inner join " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV2 on (DOC.DOCUMENT_ID=DV2.DOCUMENT_ID) " +
                    " inner join ( SELECT MAX ( DV1.VERSION ) AS VERSION," +
                    "             DV1.DOCUMENT_ID         " +
                    "    FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV1                 " +
                    "    GROUP BY   DV1.DOCUMENT_ID         )  A  on (DV2.DOCUMENT_ID=A.DOCUMENT_ID)" +
                    " inner join " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT on (DOC.IDDOCUMENT_TYPE=DT.IDDOCUMENT_TYPE) " +
                    " inner join " + GlobalDefines.DB_SCHEMA + "MARKET MA on (DOC.IDMARKET=MA.IDMIVECTOR_REAL)" +
                    " left join " + GlobalDefines.DB_SCHEMA + "SUBJECT SU on (DOC.IDSUBJECT=SU.IDSUBJECT )"
                    + " WHERE DOC.COLLABORATIVE_TYPE='" + collabType + "'  ";

            if (name != null && name.isEmpty() == false) {
                sqlDocument += " AND ( UPPER(DOC.DOCUMENT_NAME) LIKE UPPER(?) OR UPPER(DOC.FILE_NAME) LIKE UPPER(?) ) ";
            }
            sqlDocument += "  AND   DOC.COLLABORATIVE= 1    ";

            if (marketId > 0) {
                sqlDocument += "  AND DOC.IDMARKET=" + marketId;
            }

            if (docTypeId > 0) {
                sqlDocument += "  AND DOC.IDDOCUMENT_TYPE= " + docTypeId;
            }

            //  sqlDocument += " ORDER BY DOC.FECHA_CREACION DESC  ";
            sqlDocument += " ORDER BY DOC.DOCUMENT_ID DESC  ";

            stDocument = conn.prepareStatement(sqlDocument);

            if (name != null && name.isEmpty() == false) {
                stDocument.setString(1, "%" + name + "%");
                stDocument.setString(2, "%" + name + "%");
            }

            rsDocument = stDocument.executeQuery();
            lstDocuments = new ArrayList<>();

            MarketBO ma;
            DocumentTypeBO dt;
            SubjectBO su;

            while (rsDocument.next()) {
                documentBO = new DocumentBO();
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setStatus(rsDocument.getString("DOCUMENT_STATUS"));
                documentBO.setFecha(rsDocument.getDate("FECHA_CREACION"));
                documentBO.setDocVersionId(rsDocument.getInt("doc_version_id"));
                documentBO.setVersion(rsDocument.getInt("version"));

                ma = new MarketBO();
                ma.setIdMarket(rsDocument.getInt("IDMIVECTOR_REAL"));
                ma.setName(rsDocument.getString("MARKET_NAME"));
                documentBO.setMarketBO(ma);

                dt = new DocumentTypeBO();
                dt.setIddocument_type(rsDocument.getInt("IDDOCUMENT_TYPE"));
                dt.setName(rsDocument.getString("DOCUMENT_TYPE_NAME"));
                documentBO.setTipoBO(dt);

                su = new SubjectBO();
                su.setIdSubject(rsDocument.getInt("IDSUBJECT"));
                su.setName(rsDocument.getString("SUBJECT_NAME"));
                documentBO.setSubjectBO(su);

                lstDocuments.add(documentBO);

            }

        } catch (SQLException sqlException) {
            Utilerias.logger(getClass()).info(sqlException);
        } finally {
            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }

                if (stDocument != null) {
                    stDocument.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        return lstDocuments;
    }

    public List<DocumentBO> getDocumentsByName(String name, boolean allVersions) {

        DocumentBO documentBO = null;
        PreparedStatement stDocument = null;
        String sqlDocument = null;
        ResultSet rsDocument = null;
        List<DocumentBO> lstDocuments = null;
        Connection conn = null;

        try {
            conn = ConnectionDB.getInstance().getConnection();
            if (allVersions == false) {
                sqlDocument = "SELECT  "
                        + "	DOC.DOCUMENT_ID ,             "
                        + "	DOC.DOCUMENT_NAME ,           "
                        + "     DOC.FILE_NAME     ,  "
                        + "	DOC.WIDTH AS DOCUMENT_WIDTH ,   "
                        + "	DOC.HEIGHT AS DOCUMENT_HEIGHT ,  "
                        + "     DOC.FECHA_CREACION            ,  "
                        + "	DOC.STATUS AS DOCUMENT_STATUS   "
                        + "     , A.VERSION , DV2.DOC_VERSION_ID "
                        + " FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV2 , ( SELECT MAX ( DV1.VERSION ) AS VERSION  , DV1.DOCUMENT_ID "
                        + "        FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV1         "
                        + "        GROUP BY   DV1.DOCUMENT_ID "
                        + "       "
                        + " )  A"
                        + " WHERE DOC.DOCUMENT_ID=A.DOCUMENT_ID "
                        + " AND   DOC.DOCUMENT_ID=DV2.DOCUMENT_ID "
                        + " AND   DV2.VERSION=A.VERSION "
                        + " AND ( UPPER(DOC.DOCUMENT_NAME) LIKE UPPER(?) OR UPPER(DOC.FILE_NAME) LIKE UPPER(?) ) "
                        + " ORDER BY DOC.FECHA_CREACION DESC  ";
            } else {

                sqlDocument = "SELECT                 "
                        + "	DOC.DOCUMENT_ID ,             "
                        + "	DOC.DOCUMENT_NAME ,           "
                        + " DOC.FILE_NAME     ,  "
                        + "	DOC.WIDTH AS DOCUMENT_WIDTH ,   "
                        + "     DOC.FECHA_CREACION          ,   "
                        + "	DOC.HEIGHT AS DOCUMENT_HEIGHT ,  "
                        + "	DOC.STATUS AS DOCUMENT_STATUS ,  "
                        + "     DV.DOC_VERSION_ID ,   "
                        + "     DV.VERSION   "
                        + "	FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV   "
                        + "     WHERE DOC.DOCUMENT_ID= DV.DOCUMENT_ID  "
                        + "     AND ( UPPER(DOC.DOCUMENT_NAME) LIKE UPPER(?) OR UPPER(DOC.FILE_NAME) LIKE UPPER(?) ) "
                        + "     ORDER BY DOC.FECHA_CREACION DESC  ";
            }

            stDocument = conn.prepareStatement(sqlDocument);
            stDocument.setString(1, "%" + name + "%");
            stDocument.setString(2, "%" + name + "%");

            rsDocument = stDocument.executeQuery();
            lstDocuments = new ArrayList<>();

            while (rsDocument.next()) {
                documentBO = new DocumentBO();
                documentBO.setDocumentId(rsDocument.getInt("document_id"));
                documentBO.setTemplateName(rsDocument.getString("document_name"));
                documentBO.setFileName(rsDocument.getString("file_name"));
                documentBO.setStatus(rsDocument.getString("DOCUMENT_STATUS"));
                documentBO.setFecha(rsDocument.getDate("FECHA_CREACION"));
                documentBO.setDocVersionId(rsDocument.getInt("doc_version_id"));
                documentBO.setVersion(rsDocument.getInt("version"));

                lstDocuments.add(documentBO);
            }

        } catch (SQLException sqlException) {
            Utilerias.logger(getClass()).info(sqlException);
        } finally {
            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }

                if (stDocument != null) {
                    stDocument.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        return lstDocuments;
    }

    public SemanarioDocsBO getSemanarioDocs(int idSemanario) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        SemanarioDocsBO semanarioDocsBO = null;
        try {
            conn = ConnectionDB.getInstance().getConnection();
            pst = conn.prepareStatement(
                    "SELECT s.nombre_semanario AS nombre_semanario, "
                    + "sd.id_documento AS id_documento "
                    + "FROM " + GlobalDefines.DB_SCHEMA + "semanario_docs AS sd, " + GlobalDefines.DB_SCHEMA + "semanario AS s "
                    + "WHERE s.id_semanario = sd.id_semanario "
                    + "AND s.id_semanario = ? "
                    + "ORDER BY ORDER_ID");
            pst.setInt(1, idSemanario);
            rs = pst.executeQuery();
            semanarioDocsBO = new SemanarioDocsBO();
            List<DocumentBO> lDocumentsBO = new ArrayList<>();
            semanarioDocsBO.setDocumentsBO(lDocumentsBO);
            DocumentDAO docDAO = new DocumentDAO();

            while (rs.next()) {

                semanarioDocsBO.setNombreSemanario(rs.getString("nombre_semanario"));
                semanarioDocsBO.setIdSemanario(idSemanario);

                DocumentBO docBO = docDAO.getDocument(rs.getInt("id_documento"));
                semanarioDocsBO.getDocumentsBO().add(docBO);
            }

        } catch (Exception e) {
            System.err.println("**** Error en getSemanarioDocs: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

                if (rs != null) {
                    rs.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("**** Error cerrando statements en getSemanarioDocs(): " + ex.getMessage());
            }
        }
        return semanarioDocsBO;
    }

    public List<SemanarioDocsBO> getSemanariosByName(String name) {
        PreparedStatement pstmtSemanario = null;
        ResultSet rs = null;
        List<SemanarioDocsBO> lstSemanario = null;
        Connection conn = null;

        try {
            conn = ConnectionDB.getInstance().getConnection();
            pstmtSemanario = conn.prepareStatement(strGetSemanariosByName);
            pstmtSemanario.setString(1, "%" + name + "%");

            rs = pstmtSemanario.executeQuery();//executeQuery(sqlDocument);
            lstSemanario = new ArrayList<>();

            while (rs.next()) {
                SemanarioDocsBO o = new SemanarioDocsBO();
                o.setIdSemanario(rs.getInt("ID_SEMANARIO"));
                o.setNombreSemanario(rs.getString("NOMBRE_SEMANARIO"));
                o.setFecha(rs.getDate("FECHA"));
                lstSemanario.add(o);
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            try {
                if (pstmtSemanario != null) {
                    pstmtSemanario.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }
        return lstSemanario;
    }

    public boolean insertarSemanario(String nameSemanario, List<Integer> idDocuments) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtSemanario = null;
        ResultSet rs = null;
        boolean insertado = false;
        int idSemanario = 0;
        try {
            /* Primero insertar semanario y recuperar su ID_SEMANARIO */
            conn = ConnectionDB.getInstance().getConnection();
            pstmtSemanario = conn.prepareStatement(strInsertarSemanario, Statement.RETURN_GENERATED_KEYS);
            pstmtSemanario.setString(1, nameSemanario);

            pstmtSemanario.executeUpdate();
            rs = pstmtSemanario.getGeneratedKeys();

            if (rs.next()) {
                idSemanario = rs.getInt(1);
            }
            if (idSemanario != 0) {
                insertado = true;
                pstmt = conn.prepareStatement(strInsertarSemanarioDocs);
                int i = 0;
                for (int o : idDocuments) {
                    pstmt.clearParameters();
                    pstmt.setInt(1, idSemanario);
                    pstmt.setInt(2, o);
                    pstmt.setShort(3, (short) (i + 1));
                    int n = pstmt.executeUpdate();
                    i++;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (pstmtSemanario != null) {
                    pstmtSemanario.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException e) {
                Utilerias.logger(getClass()).info(e);
            }
        }
        return insertado;
    }

    public List<DocumentSearchBO> getDocumentByCriteria(DocumentSearchBO docSearchBO) {

        DocumentSearchBO documentSearchBO = null;
        PreparedStatement stDocument = null;
        ResultSet rsDoc = null;
        List<DocumentSearchBO> lstDocuments = null;
        String criteriaEnabled = "";
        String criteriaEnabled2 = "";
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {

            StringBuilder sb = new StringBuilder("");
            StringBuilder sb2 = new StringBuilder("");

            if (docSearchBO.getIdMarket() != -1) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ")) {
                    sb.append(" DOC.IDMARKET = ").append(docSearchBO.getIdMarket());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ")) {
                    sb2.append(" DOCX.IDMARKET = ").append(docSearchBO.getIdMarket());
                }

            }
            if (docSearchBO.getIdSubject() != -1) {

                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" DOC.IDSUBJECT = ").append(docSearchBO.getIdSubject());
                } else {
                    sb.append(" AND ").append(" DOC.IDSUBJECT = ").append(docSearchBO.getIdSubject());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" DOCX.IDSUBJECT = ").append(docSearchBO.getIdSubject());
                } else {
                    sb2.append(" AND ").append(" DOCX.IDSUBJECT = ").append(docSearchBO.getIdSubject());
                }
            }

            if (docSearchBO.getIdIndustry() != -1) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" S.INDUSTRY = ").append(docSearchBO.getIdIndustry());
                } else {
                    sb.append(" AND ").append(" S.INDUSTRY = ").append(docSearchBO.getIdIndustry());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" S2.INDUSTRY = ").append(docSearchBO.getIdIndustry());
                } else {
                    sb2.append(" AND ").append(" S2.INDUSTRY = ").append(docSearchBO.getIdIndustry());
                }
            }

            if (docSearchBO.getIdAuthor() != -1) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" DOC.ID_AUTHOR = ").append(docSearchBO.getIdAuthor());
                } else {
                    sb.append(" AND ").append("DOC.ID_AUTHOR = ").append(docSearchBO.getIdAuthor());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" DOCX.IDAUTHOR = ").append(docSearchBO.getIdAuthor());
                } else {
                    sb2.append(" AND ").append("DOCX.IDAUTHOR = ").append(docSearchBO.getIdAuthor());
                }
            }

            if (docSearchBO.getIdDocType() != -1) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" DOC.IDDOCUMENT_TYPE = ").append(docSearchBO.getIdDocType());
                } else {
                    sb.append(" AND ").append(" DOC.IDDOCUMENT_TYPE = ").append(docSearchBO.getIdDocType());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" DOCX.IDDOCUMENT_TYPE = ").append(docSearchBO.getIdDocType());
                } else {
                    sb2.append(" AND ").append(" DOCX.IDDOCUMENT_TYPE = ").append(docSearchBO.getIdDocType());
                }
            }

            if (docSearchBO.getIdLanguage() != -1) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" DOC.IDLANGUAGE = ").append(docSearchBO.getIdLanguage());
                } else {
                    sb.append(" AND ").append(" DOC.IDLANGUAGE = ").append(docSearchBO.getIdLanguage());
                }

                //Publicaciones con adjunto
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" DOCX.IDLANGUAGE = ").append(docSearchBO.getIdLanguage());
                } else {
                    sb2.append(" AND ").append(" DOCX.IDLANGUAGE = ").append(docSearchBO.getIdLanguage());
                }
            }

            if (docSearchBO.getIdStatus() != -1) {

                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (docSearchBO.getIdStatus() == 1) {
                    if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                        //sb.append(" DOC.DOCUMENT_ID NOT EXISTS(SELECT DS.DOCUMENT_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_SEND WHERE  ) ");
                        sb.append(" NOT EXISTS (SELECT DSN.IDDOCUMENT FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_SEND AS DSN WHERE DSN.IDDOCUMENT = DOC.DOCUMENT_ID) ");
                    } else {
                        sb.append(" AND ").append(" NOT EXISTS (SELECT DSN.IDDOCUMENT FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_SEND AS DSN WHERE DSN.IDDOCUMENT = DOC.DOCUMENT_ID) ");
                    }
                } else {
                    if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                        sb.append(" DS.IDSTATUS_PUBLISH = ").append(docSearchBO.getIdStatus());
                    } else {
                        sb.append(" AND ").append(" DS.IDSTATUS_PUBLISH = ").append(docSearchBO.getIdStatus());
                    }
                }

                //Publicaciones con adjunto
                if (docSearchBO.getIdStatus() == 1) {
                    if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                        sb2.append(" DOCX.IDSTATUS_PUBLISH  = 1 ");
                    } else {
                        sb2.append(" AND ").append(" DOCX.IDSTATUS_PUBLISH  = 1");
                    }
                } else {
                    if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                        sb2.append(" DOCX.IDSTATUS_PUBLISH  = ").append(docSearchBO.getIdStatus());
                    } else {
                        sb2.append(" AND ").append(" DOCX.IDSTATUS_PUBLISH  = ").append(docSearchBO.getIdStatus());
                    }
                }

            }

            if (docSearchBO.getDocPublicationDateFrom() != null
                    && !docSearchBO.getDocPublicationDateFrom().isEmpty()
                    && docSearchBO.getDocPublicationDateTo() != null
                    && !docSearchBO.getDocPublicationDateTo().isEmpty()) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom());
                    sb.append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                    sb.append("OR DOC.FECHA_CREACION BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("'))");
                } else {
                    sb.append(" AND (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom());
                    sb.append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                    sb.append("OR DOC.FECHA_CREACION BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("'))");
                }
            } else if (docSearchBO.getDocPublicationDateFrom() != null
                    && !docSearchBO.getDocPublicationDateFrom().isEmpty()) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') ");
                    sb.append("OR DOC.FECHA_CREACION >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("'))");
                } else {
                    sb.append(" AND (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') ");
                    sb.append("OR DOC.FECHA_CREACION >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("'))");
                    

                }
            } else if (docSearchBO.getDocPublicationDateTo() != null
                    && !docSearchBO.getDocPublicationDateTo().isEmpty()) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;
                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("')) ");
                    sb.append("OR (DOC.FECHA_CREACION <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("')");
                    sb.append(")");
                } else {
                    sb.append(" AND (date(to_date(DS.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("')) ");
                    sb.append("OR (DOC.FECHA_CREACION <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("')");
                    sb.append(")");
                    
                }
            }

            //Publicaciones con adjunto
            if (docSearchBO.getDocPublicationDateFrom() != null
                    && !docSearchBO.getDocPublicationDateFrom().isEmpty()
                    && docSearchBO.getDocPublicationDateTo() != null
                    && !docSearchBO.getDocPublicationDateTo().isEmpty()) {
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom());
                    sb2.append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                } else {
                    sb2.append(" AND date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) BETWEEN DATE('").append(docSearchBO.getDocPublicationDateFrom());
                    sb2.append("') AND DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                }
            } else if (docSearchBO.getDocPublicationDateFrom() != null
                    && !docSearchBO.getDocPublicationDateFrom().isEmpty()) {
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') ");
                } else {
                    sb2.append(" AND date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) >= DATE('").append(docSearchBO.getDocPublicationDateFrom()).append("') ");
                }
            } else if (docSearchBO.getDocPublicationDateTo() != null
                    && !docSearchBO.getDocPublicationDateTo().isEmpty()) {
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;
                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                } else {
                    sb2.append(" AND date(to_date(DOCX.DATE_PUBLISH,'YYYY/MM/DD HH24:MI')) <= DATE('").append(docSearchBO.getDocPublicationDateTo()).append("') ");
                }
            }

            if (docSearchBO.getCriteria() != null && !docSearchBO.getCriteria().isEmpty()) {
                criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" (DOC.VISIBLE = 1 AND DOC.STATUS = '1') AND (")
                            .append(" (UPPER(DS.TITLE) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(" OR (UPPER(S.NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(SUBSTR((DS.TEXT),1,254)) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(DOC.DOCUMENT_NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(DOC.FILE_NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(docSearchBO.isSearchByContent() ? getIdDocumentFilterByCriteria(docSearchBO.getCriteria()) : "")
                            .append(" )");

                } else {
                    sb.append(" AND (DOC.VISIBLE = 1 AND  DOC.STATUS = '1') AND (")
                            .append(" (UPPER(DS.TITLE) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(" OR (UPPER(S.NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(SUBSTR((DS.TEXT),1,254)) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(DOC.DOCUMENT_NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            .append(" OR (UPPER(DOC.FILE_NAME) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(docSearchBO.isSearchByContent() ? getIdDocumentFilterByCriteria(docSearchBO.getCriteria()) : "")
                            .append(" )");
                }
            }else{
                
                    criteriaEnabled = criteriaEnabled.isEmpty() == true ? " WHERE " : criteriaEnabled;

                if (criteriaEnabled.equals(" WHERE ") && sb.toString().isEmpty()) {
                    sb.append(" (DOC.VISIBLE = 1 AND DOC.STATUS = '1')");

                } else {
                    sb.append(" AND (DOC.VISIBLE = 1 AND  DOC.STATUS = '1')");
                }
                
                
            
            }

            //Publicaciones con adjunto
            if (docSearchBO.getCriteria() != null && !docSearchBO.getCriteria().isEmpty()) {
                criteriaEnabled2 = criteriaEnabled2.isEmpty() == true ? " WHERE " : criteriaEnabled2;

                if (criteriaEnabled2.equals(" WHERE ") && sb2.toString().isEmpty()) {
                    sb2.append(" (DOCX.STATUS = '1') AND (")
                            .append(" (UPPER(DOCX.TITLE) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(" OR (UPPER(SUBSTR((DOCX.TEXT),1,254)) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            //.append(docSearchBO.isSearchByContent() ? getIdDocumentFilterByCriteria(docSearchBO.getCriteria()) : "")
                            .append(" )");

                } else {
                    sb2.append(" AND (DOCX.STATUS = '1') AND (")
                            .append(" (UPPER(DOCX.TITLE) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%') ")
                            .append(" OR (UPPER(SUBSTR((DOCX.TEXT),1,254)) LIKE '%").append(docSearchBO.getCriteria().toUpperCase().trim()).append("%')")
                            //.append(docSearchBO.isSearchByContent() ? getIdDocumentFilterByCriteria(docSearchBO.getCriteria()) : "")
                            .append(" )");
                }
            }

            sqlGetDocByCriteria.append(criteriaEnabled).append(sb);

            sqlGetDocByCriteria.append(" \n UNION ALL \n  ").append(sqlGetDocByCriteriaUnionAttach.append(criteriaEnabled2).append(sb2));

            sqlGetDocByCriteria.append(" ORDER BY FECHA_ORDEN DESC");

            stDocument = conn.prepareStatement(sqlGetDocByCriteria.toString());

            rsDoc = stDocument.executeQuery();
            lstDocuments = new ArrayList<>();
            int perfilID = InstanceContext.getInstance().getUsuario().getPerfilId();
            
            DocumentTypeProfileDAO dao = new DocumentTypeProfileDAO();
            List<DocumentTypeProfileBO> lstPerfiles = dao.get(0);
            

            while (rsDoc.next()) {
                documentSearchBO = new DocumentSearchBO();
                documentSearchBO.setDocumentId(rsDoc.getInt("document_id"));
                documentSearchBO.setDocumentName(rsDoc.getString("DOCUMENT_NAME"));
                documentSearchBO.setDocument_status(rsDoc.getString("DOCUMENT_STATUS"));
                documentSearchBO.setIdMarket(rsDoc.getInt("IDMARKET"));
                documentSearchBO.setMarketName(rsDoc.getString("MARKET"));
                documentSearchBO.setIdDocType(rsDoc.getInt("IDDOCUMENT_TYPE"));
                documentSearchBO.setDocTypeName(rsDoc.getString("DOC_TYPE_NAME"));
                documentSearchBO.setIdLanguage(rsDoc.getInt("IDLANGUAGE"));
                documentSearchBO.setLanguageName(rsDoc.getString("LANGUAGE"));
                documentSearchBO.setIdSubject(rsDoc.getInt("IDSUBJECT"));
                documentSearchBO.setSubjectName(rsDoc.getString("SUBJECT"));
                documentSearchBO.setIdIndustry(rsDoc.getInt("ID_INDUSTRY"));
                documentSearchBO.setIndustryName(rsDoc.getString("INDUSTRY_NAME"));
                documentSearchBO.setIdAuthor(rsDoc.getInt("ID_AUTHOR"));
                documentSearchBO.setAuthor(rsDoc.getString("NAME_AUTHOR"));
                documentSearchBO.setDocCreationDate(rsDoc.getString("FECHA_CREACION"));
                documentSearchBO.setDocPublicationDate(rsDoc.getString("SPDATE"));
                documentSearchBO.setScheduled(rsDoc.getInt("SCHEDULED") == 1);
                documentSearchBO.setDate_publish(rsDoc.getString("PUBLISH_DATE"));
                documentSearchBO.setVersion(rsDoc.getInt("VERSION"));
                documentSearchBO.setDocVersionId(rsDoc.getInt("doc_version_id"));
                documentSearchBO.setCollaborative(rsDoc.getShort("collaborative") == 1);
                documentSearchBO.setCollaborative_Type(rsDoc.getString("collaborative_type"));
                documentSearchBO.setIdStatus_publish(rsDoc.getInt("IDSTATUS_PUBLISH"));
                documentSearchBO.setTitle_regex(rsDoc.getString("TITLE_REGEX"));
                documentSearchBO.setNomenclature(rsDoc.getString("NOMENCLATURE"));
                documentSearchBO.setIdPublish(rsDoc.getInt("ID_PUBLISH"));
                documentSearchBO.setFileName(rsDoc.getString("FILENAME"));
                documentSearchBO.setUrl(rsDoc.getString("URL"));
                
                String codeHist = rsDoc.getString("CODE_HIST");
                if(codeHist == null || codeHist.trim().isEmpty()){
                    documentSearchBO.setHistory(false);
                }else{
                    try {
                        String[] val = codeHist.split("-");
                        if("HIST".equals( val[0] ))
                            documentSearchBO.setHistory(true);
                        else
                            documentSearchBO.setHistory(false);
                    } catch (Exception e) {
                         documentSearchBO.setHistory(false);
                    }
                    
                }

                /*Cargar el permiso del tipo de documento con el perfil del usuario */

                for (DocumentTypeProfileBO lst : lstPerfiles) {
                    if (documentSearchBO.getIdDocType() == lst.getDocument_id() && perfilID == lst.getIdprofile()) {
                        documentSearchBO.setEnableForDocumentTypeProfile(1);
                        break;
                    }
                }

                lstDocuments.add(documentSearchBO);
            }

        } catch (SQLException sqlException) {
            Utilerias.logger(getClass()).info(sqlException);
        } finally {
            try {
                if (rsDoc != null) {
                    rsDoc.close();
                }
                if (stDocument != null) {
                    stDocument.close();
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        return lstDocuments;
    }

    private String getIdDocumentFilterByCriteria(String searchLike) {
        String retVal = "";
        String sql = sqlGetDocIdByContent.toString().replaceAll("@like", searchLike.toUpperCase());
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);) {

            while (rs.next()) {
                retVal += "," + rs.getString("DOCUMENT_ID");
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            return "";
        }

        if (!retVal.isEmpty()) {
            retVal = " OR DOC.DOCUMENT_ID IN (" + retVal.substring(1) + ") ";
        }

        return retVal;
    }

    public Integer[] getDocumentIdByVersion(int versionId) {
        Integer[] documentId = new Integer[2];
        String sqlDocument = null;
        ResultSet rsDocument = null;
        Connection conn = null;
        Statement stDocument = null;

        try {
            conn = ConnectionDB.getInstance().getConnection();
            stDocument = conn.createStatement();

            sqlDocument = " SELECT \n"
                    + "  DV.DOCUMENT_ID, \n"
                    + "  DV.VERSION \n"
                    + "  FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV\n"
                    + "  WHERE DV.DOC_VERSION_ID=" + versionId;
            rsDocument = stDocument.executeQuery(sqlDocument);

            if (rsDocument.next()) {
                documentId[0] = rsDocument.getInt("document_Id");
                documentId[1] = rsDocument.getInt("version");
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            try {
                if (rsDocument != null) {
                    rsDocument.close();
                }
                if (stDocument != null) {
                    stDocument.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
        return documentId;

    }

    public boolean eliminarHojas(String ids, DocumentBO document) {
        boolean retVal = true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            if( document.isCollaborative() ){
            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append(" DOCUMENT_MODULE_NEW  ") 
                    .append(" SET ESTATUS='D'  ") 
                    .append(" WHERE DOCUMENT_ID = ").append(document.getDocumentId())
                    .append(" AND DOC_VERSION_ID = ").append(document.getDocVersionId())
                    .append(" AND HOJA IN (").append(ids).append(")").toString();
                
                
            }else{
            
            sqlUpdTemplate = new StringBuilder()
                    .append(" DELETE ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_MODULE_NEW WHERE DOCUMENT_ID = ").append(document.getDocumentId())
                    .append(" AND DOC_VERSION_ID = ").append(document.getDocVersionId())
                    .append(" AND HOJA IN (").append(ids).append(")").toString();
            
            }

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

    public boolean eliminarDocumento(int documentId) {
        boolean retVal = true;
        String sqlDelDoc;
        Statement stDelDoc = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            sqlDelDoc = new StringBuilder()
                    .append("DELETE ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_NEW WHERE DOCUMENT_ID = ").append(documentId).toString();

            stDelDoc = conn.createStatement();
            int n = stDelDoc.executeUpdate(sqlDelDoc);

            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stDelDoc != null) {
                try {
                    stDelDoc.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    public boolean eliminarDocAttach(int attachId) {
        boolean retVal = true;
        String sqlDelDoc;
        Statement stDelDoc = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            sqlDelDoc = new StringBuilder()
                    .append("DELETE ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_SEND_ATTACH WHERE IDDOCUMENT_SEND_ATTACH = ").append(attachId).toString();

            stDelDoc = conn.createStatement();
            int n = stDelDoc.executeUpdate(sqlDelDoc);

            if (n <= 0) {
                retVal = false;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        } finally {
            if (stDelDoc != null) {
                try {
                    stDelDoc.close();
                } catch (SQLException e) {
                    Utilerias.logger(getClass()).info(e);
                    retVal = false;
                }
            }
        }
        return retVal;
    }

    public void actualizarHojas(DocumentBO document) {
        List<String> hojas = getHojasRestantes(document);
        int h = 1;
        for (String hoja : hojas) {
            if (!hoja.equals(h)) {
                acomodarHoja(hoja, h, document);
            }
            h++;
        }
    }

    private boolean acomodarHoja(String hoja_a, int hoja_n, DocumentBO document) {
        boolean retVal = true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_MODULE_NEW SET HOJA = ").append(hoja_n)
                    .append(" WHERE DOCUMENT_ID = ").append(document.getDocumentId())
                    .append(" AND DOC_VERSION_ID = ").append(document.getDocVersionId())
                    .append(" AND HOJA = ").append(hoja_a).toString();

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

    public boolean ordenarHoja(String docModIds, int hojaId) {
        boolean retVal = true;
        String sqlUpdTemplate;
        Statement stUpdTemplate = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            sqlUpdTemplate = new StringBuilder()
                    .append(" UPDATE ").append(GlobalDefines.DB_SCHEMA)
                    .append("DOCUMENT_MODULE_NEW SET HOJA = ").append(hojaId)
                    .append(" WHERE DOCUMENT_MODULE_ID IN (").append(docModIds)
                    .append(") ").toString();

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

    private List<String> getHojasRestantes(DocumentBO document) {
        List<String> lstHojas = new ArrayList<>();
        String sqlModules = new StringBuilder()
                .append(" SELECT ")
                .append(" DISTINCT(HOJA) AS HOJA")
                .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_MODULE_NEW ")
                .append(" WHERE DOCUMENT_ID = ").append(document.getDocumentId())
                .append(" AND DOC_VERSION_ID = ").append(document.getDocVersionId())
                .append(" ORDER BY HOJA")
                .toString();
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stModules = conn.createStatement();
                ResultSet rsModules = stModules.executeQuery(sqlModules);) {

            while (rsModules.next()) {
                lstHojas.add(rsModules.getString("HOJA"));
            }

        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstHojas;
    }

    public Map<Integer, List<Integer>> getHojasDocumento(DocumentBO document) {
        Map<Integer, List<Integer>> lstHojas = new HashMap<>();
        String sqlModules = new StringBuilder()
                .append(" SELECT HOJA, DOCUMENT_MODULE_ID ")
                .append(" FROM ").append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_MODULE_NEW ")
                .append(" WHERE DOCUMENT_ID = ").append(document.getDocumentId())
                .append(" AND DOC_VERSION_ID = ").append(document.getDocVersionId())
                .append(" ORDER BY HOJA ")
                .toString();
        try (Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stModules = conn.createStatement();
                ResultSet rsModules = stModules.executeQuery(sqlModules);) {

            int hoja = 0;
            List<Integer> res = null;
            while (rsModules.next()) {
                if (hoja != rsModules.getInt("HOJA")) {
                    if (hoja > 0) {
                        List<Integer> copy = res;
                        lstHojas.put(hoja, copy);
                    }
                    hoja = rsModules.getInt("HOJA");
                    res = new ArrayList<>();
                    res.add(rsModules.getInt("DOCUMENT_MODULE_ID"));
                } else {
                    res.add(rsModules.getInt("DOCUMENT_MODULE_ID"));
                }

            }
            List<Integer> copy = res;
            lstHojas.put(hoja, copy);
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return lstHojas;
    }

    private static final String strGetSemanariosByName
            = " SELECT "
            + " ID_SEMANARIO, "
            + " NOMBRE_SEMANARIO, "
            + " FECHA "
            + " FROM " + GlobalDefines.DB_SCHEMA + "SEMANARIO "
            + " WHERE UPPER(NOMBRE_SEMANARIO) LIKE UPPER(?) "
            + " ORDER BY FECHA DESC ";

    private static final String strInsertarSemanarioDocs
            = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "SEMANARIO_DOCS (ID_SEMANARIO, ID_DOCUMENTO, ORDER_ID) "
            + " VALUES (?, ?, ?)";

    private static final String strInsertarSemanario
            = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "SEMANARIO (NOMBRE_SEMANARIO) VALUES (?) ";

    private static final String sqlSelectDocumentByID
            = "SELECT "
            + "D.DOCUMENT_ID ,"
            + "D.DOCUMENT_NAME , "
            + "D.FILE_NAME ,  "
            + "D.IDMARKET     ,  "
            + "D.IDDOCUMENT_TYPE    , "
            + "D.IDLANGUAGE    ,  "
            + "D.IDSUBJECT     ,  "
            + "D.STATUS AS DOCUMENT_STATUS  "
            + "FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT D WHERE D.DOCUMENT_ID = ? "
            + "ORDER BY  D.DOCUMENT_NAME ";

    private static final String sqlSelectSpecificVersionDocument
            = "SELECT  "
            + "	DOC.DOCUMENT_ID ,             "
            + "	DOC.DOCUMENT_NAME ,           "
            + " DOC.FILE_NAME     ,  "
            + "	DOC.WIDTH AS DOCUMENT_WIDTH ,   "
            + "	DOC.HEIGHT AS DOCUMENT_HEIGHT ,  "
            + "	DOC.STATUS AS DOCUMENT_STATUS   "
            + ", A.VERSION , DV2.DOC_VERSION_ID "
            + " FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV2 , ( SELECT MAX ( DV1.VERSION ) AS VERSION  , DV1.DOCUMENT_ID "
            + "        FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV1         "
            + "        GROUP BY   DV1.DOCUMENT_ID "
            + "       "
            + " )  A "
            + " WHERE DOC.DOCUMENT_ID=A.DOCUMENT_ID "
            + " AND   DOC.DOCUMENT_ID=DV2.DOCUMENT_ID "
            + " AND   DV2.VERSION=A.VERSION "
            + " ORDER BY DOC.DOCUMENT_NAME ";

    private static final String sqlSelectAllVersionDocument
            = "SELECT                 "
            + "	DOC.DOCUMENT_ID ,             "
            + "	DOC.DOCUMENT_NAME ,           "
            + " DOC.FILE_NAME     ,  "
            + "	DOC.WIDTH AS DOCUMENT_WIDTH ,   "
            + "	DOC.HEIGHT AS DOCUMENT_HEIGHT ,  "
            + "	DOC.STATUS AS DOCUMENT_STATUS ,  "
            + "     DV.DOC_VERSION_ID ,   "
            + "     DV.VERSION   "
            + "	FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV   "
            + "     WHERE DOC.DOCUMENT_ID= DV.DOCUMENT_ID  "
            + "	ORDER BY  DOC.DOCUMENT_NAME , DV.VERSION ";

    public StringBuilder sqlGetDocIdByContent = new StringBuilder("SELECT DISTINCT(DOC.DOCUMENT_ID) FROM ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_NEW DOC INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_MODULE_NEW MN ON (DOC.DOCUMENT_ID = MN.DOCUMENT_ID) INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_OBJECT_NEW OB ON (MN.DOCUMENT_MODULE_ID = OB.DOCUMENT_MODULE_ID) ")
            .append("WHERE UPPER(SUBSTR(OB.PLAIN_TEXT, 1,254))  LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 254,508))   LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 508,762))   LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 762,1016))  LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 1016,1270)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 1270,1524)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 1524,1778)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 1778,2032)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 2032,2286)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 2286,2540)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 2540,2794)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 2794,3048)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 3048,3302)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 3302,3556)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 3556,3810)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 3810,4064)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 4064,4318)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 4318,4572)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 4572,4826)) LIKE UPPER('%@like%')")
            .append("OR UPPER(SUBSTR(OB.PLAIN_TEXT, 4826,5080)) LIKE UPPER('%@like%')");

    public StringBuilder sqlGetDocByCriteria = new StringBuilder("SELECT \n")
            .append("  DOC.DOCUMENT_ID \n")
            .append(", DOC.DOCUMENT_NAME \n")
            .append(", DOC.IDMARKET \n")
            .append(", M.NAME AS MARKET \n")
            .append(", DOC.IDSUBJECT \n")
            .append(", S.NAME AS SUBJECT \n")
            .append(", DOC.IDLANGUAGE \n")
            .append(", L.NAME AS LANGUAGE \n")
            .append(", S.INDUSTRY AS ID_INDUSTRY \n")
            .append(", I.NAME AS INDUSTRY_NAME \n")
            .append(", DOC.FECHA_CREACION \n")
            .append(", DOC.ID_AUTHOR \n")
            .append(", DOC.NAME_AUTHOR \n")
            .append(", DOC.COLLABORATIVE \n")
            .append(", DOC.COLLABORATIVE_TYPE \n")
            .append(", SP.NAME AS DOCUMENT_STATUS \n")
            .append(", DOC.IDDOCUMENT_TYPE \n")
            .append(", DT.NAME AS DOC_TYPE_NAME \n")
            .append(", DT.TITLE_REGEX \n")
            .append(", DT.NOMENCLATURE ")
            .append(", DS.IDSTATUS_PUBLISH \n")
            .append(", DS.DATE_PUBLISH PUBLISH_DATE, DS.SCHEDULED, DS.DATE SPDATE \n")
            .append(", A.VERSION , A.DOC_VERSION_ID \n")
            .append(", NULL AS ID_PUBLISH \n")
            .append(", DOC.FILE_NAME AS fileName \n")
            .append(", CASE WHEN DS.DATE IS NULL THEN (CASE WHEN DS.DATE_PUBLISH IS NULL THEN VARCHAR_FORMAT(DOC.FECHA_CREACION, 'YYYY/MM/DD') ELSE DS.DATE_PUBLISH END) ELSE DS.DATE END AS FECHA_ORDEN \n")
            .append(", null AS CODE_HIST, DS.URL")
            .append(" FROM ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_NEW AS DOC \n")
            .append("INNER JOIN \n")
            .append("(SELECT ")
            .append("MAX ( DV1.VERSION ) AS VERSION ")
            .append(", DV1.DOCUMENT_ID AS DOCUMENT_ID , DV1.DOC_VERSION_ID    ")
            .append("FROM ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_VERSION_NEW DV1 ")
            .append("GROUP BY DV1.DOCUMENT_ID , DV1.DOC_VERSION_ID  ) AS A ON DOC.DOCUMENT_ID=A.DOCUMENT_ID \n")
            .append("INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("MARKET AS M ON M.IDMIVECTOR_REAL = DOC.IDMARKET \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("SUBJECT AS S ON S.IDSUBJECT = DOC.IDSUBJECT \n")
            .append("INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_TYPE AS DT ON DT.IDDOCUMENT_TYPE = DOC.IDDOCUMENT_TYPE \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("INDUSTRY AS I ON I.IDINDUSTRY = S.INDUSTRY \n")
            .append("INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("LANGUAGE AS L ON L.IDLANGUAGE = DOC.IDLANGUAGE \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_SEND AS DS ON DS.IDDOCUMENT = DOC.DOCUMENT_ID \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("PUBLISH_STATUS AS SP ON SP.IDPUBLISH_STATUS = DS.IDSTATUS_PUBLISH \n");

    public StringBuilder sqlGetDocByCriteriaUnionAttach = new StringBuilder("SELECT ")
            .append("  -1 AS DOCUMENT_ID \n")
            .append(", DOCX.TITLE AS DOCUMENT_NAME \n")
            .append(", DOCX.IDMARKET \n")
            .append(", M2.NAME AS MARKET \n")
            .append(", DOCX.IDSUBJECT \n")
            .append(", S2.NAME AS SUBJECT \n")
            .append(", DOCX.IDLANGUAGE \n")
            .append(", L2.NAME AS LANGUAGE \n")
            .append(", S2.INDUSTRY AS ID_INDUSTRY \n")
            .append(", I2.NAME AS INDUSTRY_NAME \n")
            .append(", DOCX.FECHA_CREACION \n")
            .append(", DOCX.IDAUTHOR AS ID_AUTHOR \n")
            .append(", DOCX.AUTHOR AS NAME_AUTHOR \n")
            .append(", NULL AS COLLABORATIVE \n")
            .append(", NULL AS COLLABORATIVE_TYPE \n")
            .append(", SP2.NAME AS DOCUMENT_STATUS \n")
            .append(", DOCX.IDDOCUMENT_TYPE \n")
            .append(", DT2.NAME AS DOC_TYPE_NAME \n")
            .append(", DT2.TITLE_REGEX \n")
            .append(", DT2.NOMENCLATURE \n")
            .append(", DOCX.IDSTATUS_PUBLISH \n")
            .append(", DOCX.DATE_PUBLISH AS PUBLISH_DATE, DOCX.SCHEDULED, DOCX.DATE SPDATE \n")
            .append(", '-5' AS VERSION , NULL AS DOC_VERSION_ID \n")
            .append(", DOCX.IDDOCUMENT_SEND_ATTACH AS ID_PUBLISH \n")
            .append(", NULL AS fileName ")
            .append(", CASE WHEN DOCX.DATE IS NULL THEN (CASE WHEN DOCX.DATE_PUBLISH IS NULL THEN VARCHAR_FORMAT(DOCX.FECHA_CREACION, 'YYYY/MM/DD') ELSE DOCX.DATE_PUBLISH END) ELSE DOCX.DATE END AS FECHA_ORDEN \n")
            .append(", DOCX.COLUMN1 AS CODE_HIST, DOCX.URL")
            .append(" FROM ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_SEND_ATTACH AS DOCX")
            .append(" INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("MARKET         AS M2  ON M2.IDMIVECTOR_REAL          =  DOCX.IDMARKET \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("SUBJECT        AS S2  ON S2.IDSUBJECT         =  DOCX.IDSUBJECT \n")
            .append("INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("LANGUAGE       AS L2  ON L2.IDLANGUAGE        =  DOCX.IDLANGUAGE \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("INDUSTRY       AS I2  ON I2.IDINDUSTRY        =  S2.INDUSTRY \n")
            .append("INNER JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("PUBLISH_STATUS AS SP2 ON SP2.IDPUBLISH_STATUS =  DOCX.IDSTATUS_PUBLISH \n")
            .append("LEFT JOIN ")
            .append(GlobalDefines.DB_SCHEMA).append("DOCUMENT_TYPE  AS DT2 ON DT2.IDDOCUMENT_TYPE  = DOCX.IDDOCUMENT_TYPE \n");

}
