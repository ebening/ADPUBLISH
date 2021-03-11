/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.util;

/**
 *
 * @author USUARIO
 */
public enum ApplicationProperties {

    /**
     * Constante que indica la propiedad app.title contenida en el archivo main.properties
     */
    APP_TITLE("app.title"),
    
    /**
     * Constante que indica la propiedad jdbc.driver contenida en el archivo main.properties
     */
    JDBC_DRIVER("jdbc.driver"),

    /**
     * Constante que indica la propiedad url.connection contenida en el archivo main.properties
     */
    URL_CONNECTION("url.connection"),

    /**
     * Constante que indica la propiedad db.user contenida en el archivo main.properties
     */
    DB_USER("db.user"),
    
    /**
     * Constante que indica la propiedad db.pool.minpoolsize contenida en el archivo main.properties
     */
    DB_POOL_MINPOOLSIZE("db.pool.minpoolsize"), 
    
    /**
     * Constante que indica la propiedad db.pool.maxpoolsize contenida en el archivo main.properties
     */
    DB_POOL_MAXPOOLSIZE("db.pool.maxpoolsize"), 
    
    /**
     * Constante que indica la propiedad db.pool.acquireincrement contenida en el archivo main.properties
     */
    DB_POOL_ACQUIREINCREMENT("db.pool.acquireincrement"),
    
    /**
     * Constante que indica la propiedad db.pass contenida en el archivo main.properties
     */
    DB_PASS("db.pass"),
    
    /**
     * Constante que indica la propiedad db.login.timeout contenida en el archivo main.properties
     */
    DB_LOGIN_TIMEOUT("db.login.timeout"),

    /**
     * Constante que indica la propiedad dir.thumbs contenida en el archivo main.properties
     */
    DIR_THUMBS("dir.thumbs"),

    /**
     * Constante que indica la propiedad dir.gen.images contenida en el archivo main.properties
     */
    DIR_GEN_IMAGES("dir.gen.images"),

    /**
     * Constante que indica la propiedad dir.pdfs contenida en el archivo main.properties
     */
    DIR_PDFS("dir.pdfs"),
    
    /**
     * Constante que indica la propiedad dir.audio contenida en el archivo main.properties
     */
    DIR_AUDIO("dir.audio"),
    
    /**
     * Constante que indica la propiedad relative.audio.folder contenida en el archivo main.properties
     */
    RELATIVE_AUDIO_FOLDER("relative.audio.folder"),
    
    
    /**
     * Constante que indica la propiedad dir.html contenida en el archivo main.properties
     */
    DIR_HTML("dir.html"),
    
    /**
     * Constante que indica la propiedad dir.html.generate contenida en el archivo main.properties
     */
    DIR_HTML_GENERATE("dir.html.generate"),
    
    /**
     * Constante que indica la propiedad pref.file.thumb.template contenida en el archivo main.properties
     */
    PREF_FILE_THUMB_TEMPLATE("pref.file.thumb.template"),

    /**
     * Constante que indica la propiedad pref.file.thumbs.modulo contenida en el archivo main.properties
     */
    PREF_FILE_THUMB_MODULO("pref.file.thumbs.modulo"),
    

    /**
     * Constante que indica la propiedad doc.base.html contenida en el archivo main.properties
     */
    DOC_BASE_HTML("doc.base.html"),
    
    /**
     * Constante que indica la propiedad doc.base.directory contenida en el archivo main.properties
     */
    DOC_BASE_DIRECTORY("doc.base.directory"),
    
    /**
     * Constante que indica la propiedad disclosure.html contenida en el archivo main.properties
     */
    DISCLOSURE_HTML("disclosure.html"),
    
    /**
     * Constante que indica la propiedad disclosure.pdf contenida en el archivo main.properties
     */
    DISCLOSURE_PDF("disclosure.pdf"),
    
    /**
     * Constante que indica la propiedad index.base.html contenida en el archivo main.properties
     */
    INDEX_BASE_HTML("index.base.html"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.analisisWS contenida en el archivo main.properties
     */
    ANALISIS_WS("ws.url.analisis.analisisWS"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.emisorasWS contenida en el archivo main.properties
     */
    EMISORAS_WS("ws.url.analisis.emisorasWS"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.searchCM contenida en el archivo main.properties
     */
    SEARCHCM_WS("ws.url.analisis.searchCM"),

    /**
     * Constante que indica la propiedad ws.url.analisis.vectormedia contenida en el archivo main.properties
     */
    URL_WS_VECTORMEDIA("ws.url.analisis.vectormedia"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.vectormedia contenida en el archivo main.properties
     */
    URL_WS_PUBLICADOR_PUBLICADOR("ws.url.publicador.publicador"),
    
     /**
     * Constante que indica la propiedad ws.url.analisis.vectormedia contenida en el archivo main.properties
     */
    URL_WS_ANALISIS_DATA("ws.url.analisis.analisisWS"), 
    
    /**
     * Constante que indica la propiedad ws.url.analisis.vectormedia contenida en el archivo main.properties
     */
    URL_WS_ANALISIS_PUBLICADOR("ws.url.analisis.publicador"),

    /**
     * Constante que indica la propiedad ws.url.analisis.vectormedia contenida en el archivo main.properties
     */
    URL_WS_VCBAUTH_ACCESS("ws.url.vcbauth.access"),
    
     /**
     * Constante que indica la propiedad icm.admin contenida en el archivo main.properties
     */
    ICM_ADMIN("icm.admin"),
    
      /**
     * Constante que indica la propiedad icm.password contenida en el archivo main.properties
     */
    ICM_PASSWORD("icm.password"),
        
      /**
     * Constante que indica la propiedad icm.schema contenida en el archivo main.properties
     */
    ICM_SCHEMA("icm.schema"),
    
     /**
     * Constante que indica la propiedad icm.server.file contenida en el archivo main.properties
     */
    ICM_SERVER_FILE("icm.server.file"),
    
      /**
     * Constante que indica el valor constante del esquema de la base de datos de db2
     */
    DB2_SCHEMA("db.schema"),
    
     /**
     * Constante que indica la propiedad icm.item.type contenida en el archivo main.properties
     */
    ICM_ITEM_TYPE("icm.item.type"),
    /**
     * Constante que indica la propiedad icm.library contenida en el archivo main.properties
     */
    ICM_LIBRARY("icm.library"),
    
    /*configuracion de proxy*/
    
    PROXY_CONFIG("proxy.config"),
    PROXY_CONFIG_HOST("proxy.config.host"),
    PROXY_CONFIG_PORT("proxy.confi.port"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.regprogress contenida en el archivo main.properties
     */
    URL_WS_REG_PUBLISH_PROGRESS("ws.url.analisis.regprogress"),
    
    /**
     * Constante que indica la propiedad ws.url.analisis.regprogress.versus contenida en el archivo main.properties
     */
    URL_WS_REG_PUBLISH_PROGRESS_VERSUS("ws.url.analisis.regprogress.versus"),
    
    /**
     * Bloque de constantes para configurar el envio de mail
     */
    EMAIL_CUENTA_CORREO_COMPLETA("email.cuenta.correo.completa"),
    EMAIL_CUENTA("email.cuenta"),
    EMAIL_PASSWORD("email.password"),
    EMAIL_SSL_TLS_PROPERTIES("email.ssl.tls.properties"),
    EMAIL_MAIL_AUTHENTICATION("email.mail.authentification"),
    EMAIL_PUERTO("email.puerto"),
    EMAIL_STARTTLS("email.starttls"),
    EMAIL_HOSTNAME("email.hostname"),
    EMAIL_FALLBACK("email.fallback"),
    EMAIL_DOMAIN_MAIL("email.domain.mail"),
    
    /**
     * Version de la aplicacion
     */
    APLICACION_VERSION("aplicacion.version")
    ;
    
        
    /**
     * @param text
     */
    ApplicationProperties(final String text) {
        this.text = text;
    }

    private final String text;

    /* Método sobre escrito para obetener el valor de las constantes.
     * @see java.lang.String toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
