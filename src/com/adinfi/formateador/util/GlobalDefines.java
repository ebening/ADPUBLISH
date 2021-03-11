package com.adinfi.formateador.util;

import java.awt.Dimension;

/**
 *
 * @author Desarrollador
 */
public class GlobalDefines {

    public static final String SEC_TYPE_COLUMN = "R";
    public static final String SEC_TYPE_SHEET = "C";
    public static final String SEC_TYPE_CELL = "N";
    
    // -- Tipo de objeto Calendario
    public static final String SEC_CONTENT_TYPE_CALENDAR = "L";
    public static final String SEC_CONTENT_TYPE_CALENDAR_WEEK = "W";
    public static final String SEC_CONTENT_TYPE_CALENDAR_MONTH = "M";
    
    // -- Tipo de Calendario
    public static final int CALENDAR_TOTAL = 1;
    public static final int CALENDAR_WEEK = 2;
    public static final int CALENDAR_MONTH = 3;
    
    public static final int CALENDAR_INDICADORES= 1;
    public static final int CALENDAR_EVENTOS= 2;
    public static final int CALENDAR_REPORTES_FINANCIEROS_EU= 3;
    public static final int CALENDAR_ASAMBLEAS_DIVIDENDOS= 4;
    public static final int CALENDAR_REPORTES_FINANCIEROS= 5;

    public static final String SEC_CONTENT_TYPE_TEXT = "T";
    
    // -- Tipo de objeto bullet
    public static final String SEC_CONTENT_TYPE_TEXT_BULLET = "B";

    //Tipo objetos
    public static final int OBJ_TYPE_EXCEL = 1;
    public static final int OBJ_TYPE_IMAGE = 2;
    public static final int OBJ_TYPE_VIDEO = 3;
    public static final int OBJ_TYPE_TEXT = 4;
    public static final int OBJ_TYPE_BULLET = 5;
    public static final int OBJ_TYPE_CALENDAR = 6;
    public static final int OBJ_TYPE_CALENDAR_WEEK = 7;
    public static final int OBJ_TYPE_CALENDAR_MONTH = 8;
    
    public static final short DRAWABLE_WIDTH = 580;
    public static final short DRAWABLE_HEIGHT = 734;
    public static final short DRAWABLE_HEIGHT_PH = 570;
    
    public static final String DB_SCHEMA = Utilerias.getProperty(ApplicationProperties.DB2_SCHEMA);

    public static final Dimension INITIAL_FORM_SIZE = new Dimension(1000, 750);
    public static final String TITULO_APP = "AdPublish";
    public static final String ERROR_CONEXION = "Error de conexión en base de datos";

    public static final String MSG_FALTA_DOCUMENTO_PLANTILLA = "Es necesario crear un nuevo documento para agregar plantillas";

    public static final int MAX_HEIGHT_CELL = 70;

    public static final String ALPHA_NUMERIC_REGEX = "^[a-zA-Z0-9-_]*$";
    public static final String NUMERIC_REGEX = "^[0-9]*$";

    public static final int DEFAULT_DB_POOL_MINPOOLSIZE = 2;
    public static final int DEFAULT_DB_POOL_ACQUIREINCREMENT = 1;
    public static final int DEFAULT_DB_POOL_MAXPOOLSIZE = 5;
    public static final int DEFAULT_DB_LOGIN_TIMEOUT = 10;

    public static final short DEFAULT_CLICK_COUNT = 1;

    public static final short RIBBON_IMAGE_WIDTH = 40;
    public static final short RIBBON_IMAGE_HEIGHT = 40;

    public static final short DEFAULT_MENU_IMAGE_WIDTH = 48;
    public static final short DEFAULT_MENU_IMAGE_HEIGHT = 48;

    public static final int DOCUMENT_CM_STATUS_DISPATCHED = 1;
    
    public static final String WS_INSTANCE = "Formateador";

    /* Constantes para acciones en base de datos. */
    public static final int SELECT_ACTION = 0;
    public static final int INSERT_UPDATE_ACTION = 1;
    public static final int DELETE_ACTION = 2;
    public static final int DELETE_ACTION_HARD = -2;
    public static final int SELECT_ACTION_EMISOR_BY_NAME = 3;

    /*Constantes para guardar el nombre de las variables user and pass en settings.xml */
    public static final String LOGIN_COOKIE_SAVED_PASS = "XYZ789";
    public static final String LOGIN_COOKIE_USER = "ABC123";
    public static final String LOGIN_COOKIE_PASS = "DEF456";

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    public static final String JNI_FOLDER_NAME = "JNI";

    public static final String COMBOBOX_EMPTY_MESSAGE = "Seleccionar";

    public static final String ALLOWED_TWITTER_IMAGE_FORMAT = "twitter.image.format";
    
    public static String DIR_THUMBS="c:/jmpa/thumbs";    
    public static String DIR_GEN_IMAGES="c:/formateador/gen_images/";
    
    
    public static final String PREF_FILE_THUMB_TEMPLATE="thumb_templ_";
    public static final String PREF_FILE_THUMB_MODULO="thumb_mod_";        

    public static final String ALLOWED_EXCEL_FORMAT = "excel.format";

    public static final int SSH_DEFAULT_PORT = 21;
    public static final int SSH_DEFAULT_TIMEOUT = 20000;
    
    public static final String TIPO_CAND_MODULE="M";
    public static final String TIPO_CAND_DOC="D";
    
    public static final String COLLAB_TYPE_DOC = "D";    
    public static final String COLLAB_TYPE_MOD = "M";
    
    public static final int INDICADORES = 70;
    public static final int ASAMBLEAS_DIVIDENDOS = 43;
    public static final int REPORTES = 33;
    public static final int EVENTOS_PROYECCIONES = 82;
    public static final int FINANCIEROS_EUA = 94;
    
    public static final String AGENDA = "16";
    public static final String AGENDA_SEMANAL = "17";
    public static final String AGENDA_MENSUAL = "18";
    
    public static final String PDF_IMAGE_DESCRIPTION = "triangle.png";
    public static final String PDF_IMAGE_TWITTER = "twitter-t.png";
    public static final String PDF_IMAGE_BULLET = "agenda-closed.png";
    
    
    public static final double CONSTANTE_ALTURA_TEXTOS = 2.15;
    public static final double CONSTANTE_ALTURA_VACIO = 2.02;
    
    public static final String LANGUAGE_NOMENCLATURE_DEFAULT = "ES";
}
