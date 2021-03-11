package com.adinfi.formateador.cm;

import com.adinfi.formateador.bos.AtributoBO;
import com.adinfi.formateador.bos.DocumentFormatBO;
import com.adinfi.formateador.bos.DocumentoBO;
import com.ibm.mm.sdk.common.DKAttrDefICM;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKConstantICM;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKNVPair;
import com.ibm.mm.sdk.common.DKParts;
import com.ibm.mm.sdk.common.DKRetrieveOptionsICM;
import com.ibm.mm.sdk.common.DKSequentialCollection;
import com.ibm.mm.sdk.common.DKTextICM;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.common.dkIterator;
import com.ibm.mm.sdk.common.dkResultSetCursor;
import com.ibm.mm.sdk.server.DKDatastoreICM;
import java.io.File;
import java.util.ArrayList;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.MimeType;
import com.adinfi.formateador.util.Utilities;
import com.adinfi.formateador.util.GlobalDefines;
import com.ibm.mm.sdk.common.DKPidICM;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportCM {

    private static final int CONNECTION_PERCENT = 20;
    private CMConnection connection = null;
    //private Exception exception = null;

    public ExportCM() {
        connection = new CMConnection();
    }

    public synchronized int connectToServer() {

        int percent = -1;
        boolean connect;
        try {
            connect = connection.connectCM();
            if (connect == true) {
                percent = CONNECTION_PERCENT;
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info("Error en conexion a CM " + ex.getMessage());
        }
        return percent;
    }

    public synchronized void disconnect() {
        if (connection != null) {
            connection.disconectCM();
        }
    }

    public synchronized List<String> importDocument(DocumentFormatBO p) {
        
        List<String> pid = new ArrayList<>();
        
        if (p == null) {
            Utilerias.logger(getClass()).info("Documento nulo");
            return null;
        }
        DKDatastoreICM dataStore = null;
        try {
            dataStore = connection.getDataStoreCM();
        } catch (Exception e) {            
            Utilerias.logger(getClass()).info("Error en conexion a CM");
            return null;
        }

        for (Object o : p.getUrlTypes()) {

            if (o.toString().equals("ZIP")) {
                p.setUrlType(o.toString());
                p.setDoc_Url(p.getUrlHtmlVector());
            } else if (o.toString().equals("PDF")) {
                p.setUrlType(o.toString());
                p.setDoc_Url(p.getUrlPdfVector());
            }

            DocumentoBO doc = new DocumentoBO();
            /**
             * String pdfPath = getFileName(p);
             *
             * if (fileExists(pdfPath)) {
             *
             * doc.setUrl(pdfPath);
             *
             * }else{ Util.logger(getClass()).info("--> Documento PDF no existe
             * = " + pdfPath); }
        *
             */

            doc.setUrl(p.getDoc_Url());

            String itemType = Utilerias.getProperty(ApplicationProperties.ICM_ITEM_TYPE);
            doc.setNombre(itemType);

            ArrayList<AtributoBO> lista = null;
            try {
                lista = getAtributoBOsListByItemType(itemType, dataStore);
            } catch (Exception ex) {                
                Utilerias.logger(getClass()).info(ex);
                return null;
            }

            lista = fillAttributes(lista, p);
            try {
                doc.setAtributos(lista);
            } catch (Exception e) {
                Utilerias.logger(getClass()).info("Error al agregar la lista de atributos al documento CM");
                return null;
            }

              if(o.toString().equals("ZIP")) {
               if (p.getPidZip()!= null) {
                if (deleteDocument(dataStore,p.getPid())) {
                    Utilerias.logger(getClass()).info("Se Elimino el pdf " + p.getDocument_id());    
                    } else {
                    Utilerias.logger(getClass()).info("No se elimino el documento pdf " + p.getDocument_id());    
                    }
                }
            } else if (o.toString().equals("PDF")) {
                if (p.getPid() != null) {
                  if (deleteDocument(dataStore,p.getPidZip())) {
                        Utilerias.logger(getClass()).info("Se Elimino el zip " + p.getDocument_id());    
                     } else {
                        Utilerias.logger(getClass()).info("No se elimino el documento zip " + p.getDocument_id());    
                     }
                }   
            }
            
            
                   
            
            DKDDO documento = importDocument(doc, dataStore, itemType, p);

            if (documento == null) {
                Utilerias.logger(getClass()).info("Error al importar documento CM");
                //return null;
                pid.add("");
            } else {
                pid.add( documento.getPidObject().pidString());
            }

        //String borrar = Util.getProperty(ApplicationProperties.DELETE_ENABLED);
        //if (borrar.equals("1")) {
            // deletePDF(p.getUrlDocPDF());
            }
        
        return pid;
    }

    /**
     * public synchronized boolean fileExists(String path) { File file = new
     * File(path); return file.exists() && !file.isDirectory(); }        
    *
     */

    public synchronized void deletePDF(String url) {
        try {

            File file = new File(url);

            if (file.delete()) {
                //Utilerias.logger(getClass()).info(file.getName() + " is deleted!");
            } else {
                Utilerias.logger(getClass()).info("Error al borrar el archivo pdf");
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).info("Error al borrar el documento");
        }
    }

    public synchronized ArrayList<AtributoBO> fillAttributes(ArrayList<AtributoBO> list, DocumentFormatBO p) {

        try {
            for (AtributoBO a : list) {

                if (a.getName().equals("FOR_IDDOCUMENT_SEND") == true) {
                    String idDocumentSend = String.valueOf(p.getIdDocument_send());
                    a.setValor(idDocumentSend);
                } else if (a.getName().equals("FOR_DOCUMENT_ID") == true) {
                    String documentId = String.valueOf(p.getDocument_id());
                    a.setValor(documentId);
                } else if (a.getName().equals("FOR_IDDOCUMENT_TYPE") == true) {
                    String idDocumentType = String.valueOf(p.getIdDocument_type());
                    a.setValor(idDocumentType);
                } else if (a.getName().equals("FOR_DOCUMENT_TYPE_NAME") == true) {
                    String documentTypeName = String.valueOf(p.getDocument_type_name());
                    a.setValor(documentTypeName);
                } else if (a.getName().equals("FOR_IDMARKET") == true) {
                    String idMarket = String.valueOf(p.getIdMarket());
                    a.setValor(idMarket);
                } else if (a.getName().equals("FOR_MARKET_NAME") == true) {
                    String marketName = String.valueOf(p.getMarket_name());
                    a.setValor(marketName);
                } else if (a.getName().equals("FOR_DATE_PUBLISH") == true) {
                    String datePublish = String.valueOf(p.getDate_publish());
                    a.setValor(datePublish);
                } else if (a.getName().equals("FOR_SUBJECT_NAME") == true) {
                    String subjectName = String.valueOf(p.getSubject_name());
                    a.setValor(subjectName);
                } else if (a.getName().equals("FOR_IDINDUSTRY") == true) {
                    String idIndustry = String.valueOf(p.getIdIndustry());
                    a.setValor(idIndustry);
                } else if (a.getName().equals("FOR_INDUSTRY_NAME") == true) {
                    String industryName = String.valueOf(p.getIndustry_name());
                    a.setValor(industryName);
                } else if (a.getName().equals("FOR_IDLANGUAGE") == true) {
                    String idLanguage = String.valueOf(p.getIdLanguage());
                    a.setValor(idLanguage);
                } else if (a.getName().equals("FOR_LANGUAGE_NAME") == true) {
                    String languageName = String.valueOf(p.getLanguage_name());
                    a.setValor(languageName);
                } else if (a.getName().equals("FOR_IDSTATUS_PUBLISH") == true) {
                    String idStatusPublish = String.valueOf(p.getIdStatus_publish());
                    a.setValor(idStatusPublish);
                } else if (a.getName().equals("FOR_PUBLISH_STATUS_NAME") == true) {
                    String publishStatusName = String.valueOf(p.getPublish_status_name());
                    a.setValor(publishStatusName);
                } else if (a.getName().equals("FOR_ISEMISORA") == true) {
                    String isEmisora = String.valueOf(p.getIsEmisora());
                    a.setValor(isEmisora);
                } else if (a.getName().equals("FOR_AUTHOR_NAME") == true) {
                    String authorName = String.valueOf(p.getAuthor_name());
                    a.setValor(authorName);
                } else if (a.getName().equals("FOR_URL") == true) {
                    String urlVector = String.valueOf(p.getUrlHtmlForm());
                    a.setValor(urlVector);
                } else if (a.getName().equals("FOR_TIPO") == true) {
                    String urlType = String.valueOf(p.getUrlType());
                    a.setValor(urlType);
                } else if (a.getName().equals("FOR_TITULO") == true) {
                    String urlType = "ATRIBUTONODESEADO";
                    a.setValor(urlType);
                } else if (a.getName().equals("FOR_DOCUMENT_TITLE") == true) {
                    String value = p.getDocumentTitle();
                    a.setValor(value);
                } else if (a.getName().equals("FOR_MAIL_CONTENT") == true) {
                    String value = p.getMailContent();
                    a.setValor(value);
                } else if (a.getName().equals("FOR_AUTHOR_MAIL") == true) {
                    String value = p.getAuthorMail();
                    a.setValor(value);
                } else if (a.getName().equals("FOR_FLAG_HISTORY") == true) {
                    String value = p.getFlagHistory();
                    a.setValor(value);
                } else if (a.getName().equals("FOR_PDF_URL") == true) {
                    String value = p.getPdfUrl();
                    a.setValor(value);
                } else if (a.getName().equals("FOR_DOCUMENT_CONTENT") == true) {
                    Clob value = p.getDocumentContent();
                    a.setValorClob(value);
                } else {
                    Utilerias.logger(getClass()).info("Tipo no soportado en AtributoBO de CM = " + a.getName());
                }
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return list;
    }

    public synchronized ArrayList<AtributoBO> getAtributoBOsListByItemType(String itemType, DKDatastoreICM connCM)
            throws DKException, Exception {
        ArrayList<AtributoBO> atributos = new ArrayList<>();

        DKSequentialCollection attrColl = (DKSequentialCollection) connCM.listEntityAttrs(itemType);

        if (attrColl != null) {
            dkIterator iter = attrColl.createIterator();

            while (iter.more()) {
                DKAttrDefICM attr = (DKAttrDefICM) iter.next();
                AtributoBO atributo = new AtributoBO();
                atributo.setName(attr.getName());
                atributo.setDescription(attr.getDescription());
                atributo.setTipo(attr.getType());
                atributo.setLonguitud(attr.getMax());
                atributo.setRequerido(!attr.isNullable());
                atributo.setRepresentativo(attr.isRepresentative());
                atributos.add(atributo);

            }
        }
        return atributos;
    }

    public synchronized DKDDO importDocument(DocumentoBO item, DKDatastoreICM connCM, String itemTypeStr, DocumentFormatBO p) {
        String newPid = null;
        DKDDO ddoDocument = null;
        try {
            String nombreItemType = item.getNombre();
            short itemType = item.getItemType();
            if (itemType == DKConstant.DK_CM_DOCUMENT) {

                if (existsDocument(connCM, p) == false) {
                    ddoDocument = connCM.createDDO(nombreItemType, itemType);

                    ddoDocument = fillDocumentAttrs(item.getAtributos(), ddoDocument);

                    ddoDocument = fillParts(ddoDocument, item, connCM);

                    ddoDocument.add();

                    newPid = ddoDocument.getPidObject().pidString();

                } else {
                    Utilerias.logger(getClass()).info(" Ya existe el documento ");
                }

            }
        } catch (DKException e) {
            printException(e);
        } catch (Exception e) {
            printException(e);
        }
        return ddoDocument;
    }
    
          public synchronized boolean deleteDocument(DKDatastoreICM connCM,String pid) {
           boolean b = false;
            try {                                              
                DKDDO doc = connCM.createDDOFromPID(pid);
                if (doc!=null) {
                doc.del();    
                }
                b = true;    
            } catch (Exception ex) {                
                Utilerias.logger(getClass()).info("Error al borrar el documento de CM " + ex.getMessage() );               
            }            
            return b;
          }   

    private synchronized DKDDO fillParts(DKDDO ddoDocument, DocumentoBO doc, DKDatastoreICM dsICM) {
        try {
            DKTextICM base = (DKTextICM) dsICM.createDDO("ICMBASETEXT", DKConstantICM.DK_ICM_SEMANTIC_TYPE_BASE);
            ((DKTextICM) base).setFormat("");
            ((DKTextICM) base).setLanguageCode("");

            String mimeType = new MimeType().getCMMimeTypeByUrl(doc.getUrl());

            if (mimeType == null) {
                Utilerias.logger(getClass()).info("Mimetype = null " + doc.getPid());
                return ddoDocument;
            }

            base.setMimeType(mimeType);

            if (doc.getUrl() != null && doc.getUrl().isEmpty() == false) {
                base.setContentFromClientFile(doc.getUrl());
                DKParts dkParts = (DKParts) ddoDocument.getData(ddoDocument.dataId(
                        DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS));
                base.setOrgFileName(null);
                dkParts.addElement(base);
            }

        } catch (DKUsageError ex) {
            printException(ex);
        } catch (DKException ex) {
            printException(ex);
        } catch (Exception ex) {
            printException(ex);
        }
        return ddoDocument;
    }

    private synchronized DKDDO fillDocumentAttrs(ArrayList<AtributoBO> atributos, DKDDO ddoDocument) {

        for (AtributoBO attr : atributos) {
            String attrName = attr.getName();
            Short attrType = attr.getTipo();
            String attrValue = attr.getValor();

            boolean addAttr = false;
            if (attrValue != null) {
                if (!attrValue.isEmpty()) {
                    addAttr = true;
                }
            }
            
            if (attrType.equals(DKConstant.DK_CM_CLOB) || attrType.equals(DKConstant.DK_CM_DBCLOB)){
                if(attr.getValorClob()!= null){
                    attrValue = CLOBToString(attr.getValorClob());
                    addAttr = true;
                }
            }

            try {
                if (addAttr) {
                    if (attrType.equals(DKConstant.DK_DATE)) {
                        ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()),
                                Utilities.parserSqlDate(attr.getValor()));
                    } else if (attrType.equals(DKConstant.DK_LONG)) {
                        ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()),
                                Utilities.convertToInteger(attr.getValor()));
                    } else if (attrType.equals(DKConstant.DK_DECIMAL)) {
                        ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()),
                                Utilities.convertToBigDecimal(attr.getValor()));
                    } else if (attrType.equals(DKConstant.DK_FSTRING) || attrType.equals(DKConstant.DK_VSTRING)) {
                        ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()), attr.getValor());
                    } else if (attrType.equals(DKConstant.DK_CM_CLOB) || attrType.equals(DKConstant.DK_CM_DBCLOB)) {
                        ddoDocument.setData(ddoDocument.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()), attrValue);
                    } else {
                        throw new Exception("No se ha identificado el tipo de dato para el atributo " + attrName);
                    }
                }
            } catch (DKUsageError ex) {
                printException(ex);
            } catch (Exception ex) {
                printException(ex);
            }
        }
        return ddoDocument;
    }
    
    private String CLOBToString(Clob cl){
        StringBuffer strOut = new StringBuffer();
        try {
            if (cl == null) {
                return "";
            }
            
            String aux;
            BufferedReader br = new BufferedReader(cl.getCharacterStream());
            while ((aux = br.readLine()) != null) {
                strOut.append(aux);
            }
            
        } catch (IOException ex) {
        } catch (SQLException ex) {
        }
        return strOut.toString();
    }

    public synchronized void printException(DKException e) {
        Utilerias.logger(getClass()).error(" Name: " + e.name());
        Utilerias.logger(getClass()).error(" Message: " + e.getMessage());
        Utilerias.logger(getClass()).error(" Message ID: " + e.getErrorId());
        Utilerias.logger(getClass()).error(" Error State: " + e.errorState());
        Utilerias.logger(getClass()).error(" Error Code: " + e.errorCode());
        Utilerias.logger(getClass()).error(e);
    }

    public synchronized void printException(Exception e) {
        Utilerias.logger(getClass()).error(" Name: " + e.getClass().getName());
        Utilerias.logger(getClass()).error(" Message: " + e.getMessage());
    }

    private synchronized boolean existsDocument(DKDatastoreICM dsICM, DocumentFormatBO p) {
        boolean exists = false;
        dkResultSetCursor cursor = null;
        try {
            String query = getDocumentQuery(p);

            DKRetrieveOptionsICM dkRetrieveOptions = DKRetrieveOptionsICM.createInstance(dsICM);
            dkRetrieveOptions.baseAttributes(true);

            DKNVPair options[] = new DKNVPair[3];
            options[0] = new DKNVPair(DKConstant.DK_CM_PARM_MAX_RESULTS, "0"); // No Maximum (Default) // Specify max using a string value.
            options[1] = new DKNVPair(DKConstant.DK_CM_PARM_RETRIEVE, dkRetrieveOptions);           // Always specify desired Retrieve Options.
            options[2] = new DKNVPair(DKConstant.DK_CM_PARM_END, null);                        // Must mark the end of the NVPair

            cursor = dsICM.execute(query, DKConstantICM.DK_CM_XQPE_QL_TYPE, options);
            //exists = cursor.fetchNext()!=null;                                     

            DKDDO ddo;
            String itemId = "";

            while ((ddo = cursor.fetchNext()) != null) { // Get the next ddo & stop when ddo == null.
                itemId = ((DKPidICM) ddo.getPidObject()).getItemId();
                Utilerias.logger(getClass()).info("     - Item ID:  " + itemId + "  (" + ddo.getPidObject().getObjectType() + ")");  // Print Item ID & Object Type
                //list =  printFolderContents_V2(ddo, dsICM, p);  
                exists = true;
            }

        } catch (DKUsageError ex) {
            printException(ex);
        } catch (DKException ex) {
            printException(ex);
        } catch (Exception ex) {
            printException(ex);
        } finally {
            if (cursor != null) {
                try {
                    cursor.destroy();
                } catch (DKException ex) {
                    printException(ex);
                } catch (Exception ex) {
                    printException(ex);
                }
            }
        }
        return exists;
    }

    private String getDocumentQuery(DocumentFormatBO p) {

        StringBuilder sb = new StringBuilder();
        sb.append("/VEC_FORMATEADOR");

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdDocument_send() != 0 ? "[@FOR_IDDOCUMENT_SEND =" + p.getIdDocument_send() : "");
        } else {
            sb.append(p.getIdDocument_send() != 0 ? " AND @FOR_IDDOCUMENT_SEND =" + p.getIdDocument_send() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getDocument_id() != 0 ? "[@FOR_DOCUMENT_ID =" + p.getDocument_id() : "");
        } else {
            sb.append(p.getDocument_id() != 0 ? " AND @FOR_DOCUMENT_ID =" + p.getDocument_id() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdDocument_type() != 0 ? "[@FOR_IDDOCUMENT_TYPE =" + p.getIdDocument_type() : "");
        } else {
            sb.append(p.getIdDocument_type() != 0 ? " AND @FOR_IDDOCUMENT_TYPE =" + p.getIdDocument_type() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getDocument_type_name() != null ? "[@FOR_DOCUMENT_TYPE_NAME =\"" + p.getDocument_type_name() + "\"" : "");
        } else {
            sb.append(p.getDocument_type_name() != null ? " AND @FOR_DOCUMENT_TYPE_NAME =\"" + p.getDocument_type_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdMarket() != 0 ? "[@FOR_IDMARKET =" + p.getIdMarket() : "");
        } else {
            sb.append(p.getIdMarket() != 0 ? " AND @FOR_IDMARKET =" + p.getIdMarket() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getMarket_name() != null ? "[@FOR_MARKET_NAME =\"" + p.getMarket_name() + "\"" : "");
        } else {
            sb.append(p.getMarket_name() != null ? " AND @FOR_MARKET_NAME =\"" + p.getMarket_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getDate_publish() != null ? "[@FOR_DATE_PUBLISH=\"" + Utilities.parserSqlDate(p.getDate_publish()) + "\"" : "");
        } else {
            sb.append(p.getDate_publish() != null ? " AND @FOR_DATE_PUBLISH=\"" + Utilities.parserSqlDate(p.getDate_publish()) + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getSubject_name() != null ? "[@FOR_SUBJECT_NAME =\"" + p.getSubject_name() + "\"" : "");
        } else {
            sb.append(p.getSubject_name() != null ? " AND @FOR_SUBJECT_NAME =\"" + p.getSubject_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdIndustry() != 0 ? "[@FOR_IDINDUSTRY =" + p.getIdIndustry() : "");
        } else {
            sb.append(p.getIdIndustry() != 0 ? " AND @FOR_IDINDUSTRY =" + p.getIdIndustry() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIndustry_name() != null ? "[@FOR_INDUSTRY_NAME =\"" + p.getIndustry_name() + "\"" : "");
        } else {
            sb.append(p.getIndustry_name() != null ? " AND @FOR_INDUSTRY_NAME =\"" + p.getIndustry_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdLanguage() != 0 ? "[@FOR_IDLANGUAGE =" + p.getIdLanguage() : "");
        } else {
            sb.append(p.getIdLanguage() != 0 ? " AND @FOR_IDLANGUAGE =" + p.getIdLanguage() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getLanguage_name() != null ? "[@FOR_LANGUAGE_NAME =\"" + p.getLanguage_name() + "\"" : "");
        } else {
            sb.append(p.getLanguage_name() != null ? " AND @FOR_LANGUAGE_NAME =\"" + p.getLanguage_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getIdStatus_publish() != 0 ? "[@FOR_IDSTATUS_PUBLISH =" + p.getIdStatus_publish() : "");
        } else {
            sb.append(p.getIdStatus_publish() != 0 ? " AND @FOR_IDSTATUS_PUBLISH =" + p.getIdStatus_publish() : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getPublish_status_name() != null ? "[@FOR_PUBLISH_STATUS_NAME =\"" + p.getPublish_status_name() + "\"" : "");
        } else {
            sb.append(p.getPublish_status_name() != null ? " AND @FOR_PUBLISH_STATUS_NAME =\"" + p.getPublish_status_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getAuthor_name() != null ? "[@FOR_AUTHOR_NAME =\"" + p.getAuthor_name() + "\"" : "");
        } else {
            sb.append(p.getAuthor_name() != null ? " AND @FOR_AUTHOR_NAME =\"" + p.getAuthor_name() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getDoc_Url() != null ? "[@FOR_URL =\"" + p.getDoc_Url() + "\"" : "");
        } else {
            sb.append(p.getDoc_Url() != null ? " AND @FOR_URL =\"" + p.getDoc_Url() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append(p.getUrlType() != null ? "[@FOR_TIPO =\"" + p.getUrlType() + "\"" : "");
        } else {
            sb.append(p.getUrlType() != null ? " AND @FOR_TIPO =\"" + p.getUrlType() + "\"" : "");
        }

        if (sb.toString().equals("/VEC_FORMATEADOR")) {
            sb.append("");
        } else {
            sb.append("]");
        }

        Utilerias.logger(getClass()).info(" Query: " + sb.toString());

        return sb.toString();
    }
}