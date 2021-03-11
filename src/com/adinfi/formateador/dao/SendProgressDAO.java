/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentFormatBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vectoran
 */
public class SendProgressDAO {
    
    public List<DocumentFormatBO> get(int sendProgressId) {

        String sqlDocument = null;
        ResultSet rsDocument = null;
        Connection conn = null;
        Statement stDocument = null;
        List<DocumentFormatBO> retVal = new ArrayList<>();
        
        try {
            conn = ConnectionDB.getInstance().getConnection();
            stDocument = conn.createStatement();

            sqlDocument = " SELECT * FROM " + GlobalDefines.DB_SCHEMA + "TMP_SEND_TO_PROGRESS SP "
                    + "  WHERE ( SP.FOR_IDDOCUMENT_SEND = " + sendProgressId 
                    + "  OR 0 = " + sendProgressId + " ) ";

            rsDocument = stDocument.executeQuery(sqlDocument);

            while (rsDocument.next()) {
                DocumentFormatBO attrib = new DocumentFormatBO();
                
                attrib.setIdDocument_send(rsDocument.getInt("FOR_IDDOCUMENT_SEND"));
                attrib.setDocument_id(rsDocument.getInt("FOR_DOCUMENT_ID"));
                attrib.setIdDocument_type(rsDocument.getInt("FOR_IDDOCUMENT_TYPE"));
                attrib.setDocument_type_name(rsDocument.getString("FOR_DOCUMENT_TYPE_NAME"));
                attrib.setDate_publish(rsDocument.getString("FOR_DATE_PUBLISH"));
                attrib.setPublish_status_name(rsDocument.getString("FOR_PUBLISH_STATUS_NAME"));
                attrib.setSubject_name(rsDocument.getString("FOR_SUBJECT_NAME"));
                attrib.setIsEmisora("Y".equals(rsDocument.getString("FOR_ISEMISORA")) ? 1 : 0);
                attrib.setIdIndustry(rsDocument.getInt("FOR_IDINDUSTRY"));
                attrib.setIndustry_name(rsDocument.getString("FOR_INDUSTRY_NAME"));
                attrib.setDocumentTitle(rsDocument.getString("FOR_DOCUMENT_TITTLE"));
                attrib.setIdLanguage(rsDocument.getInt("FOR_IDLANGUAGE"));
                attrib.setLanguage_name(rsDocument.getString("FOR_LANGUAGE_NAME"));
                attrib.setReportYear(rsDocument.getString("FOR_REPORT_YEAR"));
                attrib.setReportTrim(rsDocument.getString("FOR_REPORT_TRIM"));
                attrib.setUrlHtmlForm(rsDocument.getString("FOR_URL"));
                attrib.setPdfUrl(rsDocument.getString("FOR_PDF_URL"));
                attrib.setMailContent(rsDocument.getString("FOR_MAIL_CONTENT"));
                attrib.setDocumentContent(rsDocument.getClob("FOR_DOCUMENT_CONTENT"));
                attrib.setArregloAutores(rsDocument.getString("FOR_AUTHOR_LIST"));
                attrib.setArregloDistribucion(rsDocument.getString("FOR_DIST_LIST"));
                attrib.setPublishAttach( "Y".equals( rsDocument.getString("FOR_IS_DOC_ADJ") ) ? true : false ); 
                
                retVal.add(attrib);
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
        
        return retVal;
    }  
}
