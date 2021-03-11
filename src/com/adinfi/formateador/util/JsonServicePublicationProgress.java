/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import com.adinfi.formateador.bos.DocumentFormatBO;
import com.adinfi.formateador.bos.InputDoc;
import com.adinfi.formateador.bos.InputListaDist;
import com.adinfi.formateador.bos.ResponseData;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.main.MainApp;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author vectoran
 */
public class JsonServicePublicationProgress {
    private Client client;
    private WebResource target;
    
    public JsonServicePublicationProgress(boolean iniciarCliente){
        try{
            if(iniciarCliente){
            
                try {
                    KeyStore keystore;
                    try (InputStream caInput = JsonServicePublicationProgress.class.getResourceAsStream("cacerts")) {
                        keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                        keystore.load(caInput, "changeit".toCharArray());
                    }
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(keystore);
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);
                    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

                    ClientConfig config=new DefaultClientConfig();
                    config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,context));
                    client = Client.create(config);

                } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException ex) {
                    Utilerias.logger(getClass()).error(ex);
                }

                String urlService = Utilerias.getProperty(ApplicationProperties.URL_WS_REG_PUBLISH_PROGRESS); //"http://192.168.1.208:8080/vcbapi";
                String versus = Utilerias.getProperty(ApplicationProperties.URL_WS_REG_PUBLISH_PROGRESS_VERSUS);
                target = client.resource(urlService).path(versus).path("Intranet").path("DocumentoAnalisis");
            
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }
    }
    
    public String getPublicationProgressResponse(String action, DocumentFormatBO attrib, InputDoc autores, InputListaDist listaDistribucion){
        String aut = getJson( autores );
        String dist = getJson( listaDistribucion );
        
        return getPublicationProgressResponse(action, attrib, aut, dist);
    }

    public String getPublicationProgressResponse(String action, DocumentFormatBO attrib, String autores, String listaDistribucion) {
        String retVal = "Ok";
        
        Form formNvoDoc = new Form();
        formNvoDoc.add("Accion", action);
        formNvoDoc.add("DocumentoId", String.valueOf( attrib.getDocument_id() ));
        formNvoDoc.add("EnvioId", String.valueOf( attrib.getIdDocument_send() ));
        formNvoDoc.add("TipoDocumentoId", String.valueOf( attrib.getIdDocument_type() ));
        formNvoDoc.add("TipoDocumento", attrib.getDocument_type_name());
        formNvoDoc.add("FechaEnvio", attrib.getDate_publish());
        formNvoDoc.add("Estatus", attrib.getPublish_status_name());
        formNvoDoc.add("Tema", attrib.getSubject_name());
        formNvoDoc.add("EsEmisora", String.valueOf( attrib.getIsEmisora() == 1 ? true : false ));
        formNvoDoc.add("SectorId", String.valueOf( attrib.getIdIndustry() ));
        formNvoDoc.add("Sector", attrib.getIndustry_name());
        formNvoDoc.add("Titulo", attrib.getDocumentTitle());
        formNvoDoc.add("IdiomaId", String.valueOf( attrib.getIdLanguage() ));
        formNvoDoc.add("Idioma", attrib.getLanguage_name());
        formNvoDoc.add("AnoReporte", "0");//Valor default;
        formNvoDoc.add("TrimReporte", "0");//Valor default;
        formNvoDoc.add("UrlDocumento", attrib.getUrlHtmlForm());
        formNvoDoc.add("UrlDocumentoPdf", attrib.getPdfUrl());
        formNvoDoc.add("ContenidoCorreo", attrib.getMailContent());
        formNvoDoc.add("Contenido", CLOBToString(attrib.getDocumentContent() ));
        formNvoDoc.add("Autores", autores);
        formNvoDoc.add("ListaDistribucion", listaDistribucion);
        formNvoDoc.add("EsDocumentoAdj", String.valueOf(attrib.isPublishAttach()));

        try{            
            ClientResponse responseFormDoc = target.getRequestBuilder()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(ClientResponse.class, formNvoDoc);

            String res = responseFormDoc.getEntity(String.class);
            Utilerias.logger(getClass()).info("Envio Progress - No Publicacion: " + attrib.getIdDocument_send());
            Utilerias.logger(getClass()).info(res);
            
            ResponseData response = getResponseData(res);
            
            if(response != null &&
               response.Data != null &&
               response.Data.dsAnalisisForm != null &&
               response.Data.dsAnalisisForm.tdsRespuesta != null && response.Data.dsAnalisisForm.tdsRespuesta.size() > 0){

               if ( response.Data.dsAnalisisForm.tdsRespuesta.get(0).EsExitoso ){
                   eliminarPublicacionTmp( attrib.getIdDocument_send() );
               } else {
                   guardarPublicacionFallida(attrib, autores, listaDistribucion, response.Data.dsAnalisisForm.tdsRespuesta.get(0).Mensaje);
                   Utilerias.logger(getClass()).info(" No Publicacion: " + attrib.getIdDocument_send() + " Msj: " +response.Data.dsAnalisisForm.tdsRespuesta.get(0).Mensaje);
               }

            }else{
                guardarPublicacionFallida(attrib, autores, listaDistribucion, res);
            }
        }catch(Exception e){
            guardarPublicacionFallida(attrib, autores, listaDistribucion, "El servicio no responde.");
        }
        //System.out.println(formDocResponse);
        return retVal;//response.toString();

    }
    
    private String getJson(Object obj){
        Gson gson = new Gson();
 
	return gson.toJson(obj);
    }
    
    private ResponseData getResponseData(String json){
        Gson gson = new Gson();
        
        return gson.fromJson(json, ResponseData.class);
    }
    
    private String CLOBToString(Clob cl){
        if (cl == null) {
            return "";
        }
        
        StringBuilder strOut = new StringBuilder();
        try{
            String aux;
            BufferedReader br = new BufferedReader(cl.getCharacterStream());
            while ((aux = br.readLine()) != null) {
                strOut.append(aux);
            }
        } catch (SQLException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        
        return strOut.toString();
    }
    
    private boolean guardarPublicacionFallida(DocumentFormatBO attrib, String autores, String listaDistribucion, String error){
        sendMail(attrib, error);
        boolean retVal = true;
        String sqlDelDoc = new StringBuilder()
                .append("INSERT INTO ").append(GlobalDefines.DB_SCHEMA)
                .append("TMP_SEND_TO_PROGRESS(FOR_IDDOCUMENT_SEND")
                .append(", FOR_DOCUMENT_ID")
                .append(", FOR_IDDOCUMENT_TYPE")
                .append(", FOR_DOCUMENT_TYPE_NAME")
                .append(", FOR_DATE_PUBLISH")
                .append(", FOR_PUBLISH_STATUS_NAME")
                .append(", FOR_SUBJECT_NAME")
                .append(", FOR_ISEMISORA")
                .append(", FOR_IDINDUSTRY")
                .append(", FOR_INDUSTRY_NAME")
                .append(", FOR_DOCUMENT_TITTLE")
                .append(", FOR_IDLANGUAGE")
                .append(", FOR_LANGUAGE_NAME")
                .append(", FOR_REPORT_YEAR")
                .append(", FOR_REPORT_TRIM")
                .append(", FOR_URL")
                .append(", FOR_PDF_URL")
                .append(", FOR_MAIL_CONTENT")
                .append(", FOR_DOCUMENT_CONTENT")
                .append(", FOR_AUTHOR_LIST")
                .append(", FOR_DIST_LIST")
                .append(", FOR_IS_DOC_ADJ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").toString();
        
        try ( Connection conn = ConnectionDB.getInstance().getConnection();
              PreparedStatement pstInsObject = conn.prepareStatement(sqlDelDoc); ) {
            
            if (conn == null) {
                return false;
            }

            eliminarPublicacionTmp( attrib.getIdDocument_send() );
            
            int i = 1;
            pstInsObject.setInt(i++, attrib.getIdDocument_send() );
            pstInsObject.setInt(i++, attrib.getDocument_id()  );
            pstInsObject.setInt(i++, attrib.getIdDocument_type() );
            pstInsObject.setString(i++, attrib.getDocument_type_name());
            pstInsObject.setString(i++, attrib.getDate_publish());
            pstInsObject.setString(i++, attrib.getPublish_status_name());
            pstInsObject.setString(i++, attrib.getSubject_name());
            pstInsObject.setString(i++, attrib.getIsEmisora() == 1 ? "Y" : "N" );
            pstInsObject.setInt(i++, attrib.getIdIndustry() );
            pstInsObject.setString(i++, attrib.getIndustry_name());
            pstInsObject.setString(i++, attrib.getDocumentTitle());
            pstInsObject.setInt(i++, attrib.getIdLanguage() );
            pstInsObject.setString(i++, attrib.getLanguage_name());
            pstInsObject.setString(i++, "0");//Valor default;
            pstInsObject.setString(i++, "0");//Valor default;
            pstInsObject.setString(i++, attrib.getUrlHtmlForm());
            pstInsObject.setString(i++, attrib.getPdfUrl());
            pstInsObject.setString(i++, attrib.getMailContent());
            pstInsObject.setClob(i++, attrib.getDocumentContent() );
            pstInsObject.setString(i++, autores );
            pstInsObject.setString(i++, listaDistribucion);
            pstInsObject.setString(i++, attrib.isPublishAttach() ? "Y" : "N" );
            
            int n = pstInsObject.executeUpdate();
            if (n > 0) {
                retVal = true;
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = false;
        }
        
        return retVal;
    }
    
    public boolean eliminarPublicacionTmp(int envioId) {
        boolean retVal = true;
        String sqlDelDoc;
        Statement stDelDoc = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            if (conn == null) {
                return false;
            }

            sqlDelDoc = new StringBuilder()
                    .append("DELETE ").append(GlobalDefines.DB_SCHEMA)
                    .append("TMP_SEND_TO_PROGRESS WHERE FOR_IDDOCUMENT_SEND = ").append(envioId).toString();

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
    
    private void sendMail(DocumentFormatBO attrib, String error){
        try {
            ListMultimap<String, String> listMargin = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.MAILS_ERROR_PROGRESS);

            String mails = listMargin != null && listMargin.isEmpty() == false 
                               ? listMargin.get(Utilerias.ALLOWED_KEY.MAILS_ERROR_PROGRESS.toString()).get(StatementConstant.SC_0.get()) 
                               : "advecfin@vector.com.mx";
            
            String mensaje = "Ocurrio un error al enviar la informacion de publicacion a progress, el mensaje de error es el siguiente: \n"
                    + " No Publicacion: " + attrib.getIdDocument_send() + "\n"
                    + " Msj: " + error;
            
            String subject = "Formateador - Envio Progress";
            
            TaskEMail tmail = new TaskEMail();
            tmail.enviarMail(mails, mensaje, subject);
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
}
