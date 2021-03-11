package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author USUARIO
 */
public class SearchTableModel extends AbstractTableModel {

    public static int HAND_CURSOR_COLUMN = 15;

    /**
     * @return the list
     */
    public List<DocumentSearchBO> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<DocumentSearchBO> list) {
        this.list = list;
    }

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("17", null),
        ICON("", ""),
        DOCUMENT_NAME("DOCUMENT_NAME", "Título"),
        FECHA_PUBLICACION("FECHA_PUBLICACION", "Fecha"),
        FECHA_CREACION("FECHA_CREACION", "Fecha"),
        FILENAME("Nombre del Archivo", "Nombre del Archivo"),
        DOCUMENT_TYPE_NAME("DOCUMENT_TYPE_NAME", "Tipo de documento"),
        //SUBJECT("SUBJECT_NAME", "Tema"),
        AUTOR("AUTOR", "Autor"),
        MARKET_NAME("MARKET_NAME", "Mercado"),
        STATUS("STATUS", "Estado"),
        INDUSTRY("INDUSTRY_NAME", "Sector"),
        //LANGUAGE_NAME("LANGUAGE_NAME", "Idioma"),
        /* Botónes
         *  - PDF
         *  - HTML
         *  - Editar documento
         *  - Editar publicación
         */
        BTN_PDF("PDF", "PDF"),
        BTN_HTML("HTML", "HTML"),
        BTN_EDIT_DOC("Editar", "Editar documento"),
        BTN_EDIT_PUBLISH("Publicación", "Editar Publicación"),
        BTN_NEW("Crear Nuevo Documento", "Crear Nuevo Documento"),
        CHECK("", ""),
        ID_PUBLISH("ID Publicación", "ID Publicación");
        //ISSELECCION("Seleccionable","Seleccionable");

        ColumNamesConstants(final String value, final String caption) {
            this.value = value;
            this.caption = caption;
        }

        private final String value;
        private final String caption;

        /* Metodo sobre escrito para obetener el valor de las constantes.
         * @see java.lang.String toString()
         */
        @Override
        public String toString() {
            return value;
        }

        public String getCaption() {
            return caption;
        }
    }

    private final String[] columnNames = new String[]{
        ColumNamesConstants.ICON.getCaption(),
        ColumNamesConstants.FECHA_PUBLICACION.getCaption(),
        ColumNamesConstants.FECHA_CREACION.getCaption(),
        ColumNamesConstants.FILENAME.getCaption(),
        ColumNamesConstants.DOCUMENT_TYPE_NAME.getCaption(),
        ColumNamesConstants.DOCUMENT_NAME.getCaption(),
        //ColumNamesConstants.SUBJECT.getCaption(),
        ColumNamesConstants.AUTOR.getCaption(),
        ColumNamesConstants.MARKET_NAME.getCaption(),
        ColumNamesConstants.STATUS.getCaption(),
        ColumNamesConstants.INDUSTRY.getCaption(),
        //ColumNamesConstants.LANGUAGE_NAME.getCaption(),
        /* Botónes
         *  - PDF
         *  - HTML
         *  - Editar documento
         *  - Editar publicación
         */
        ColumNamesConstants.BTN_PDF.getCaption(),
        ColumNamesConstants.BTN_HTML.getCaption(),
        ColumNamesConstants.BTN_EDIT_DOC.getCaption(),
        ColumNamesConstants.BTN_EDIT_PUBLISH.getCaption(),
        ColumNamesConstants.BTN_NEW.getCaption(),
        ColumNamesConstants.CHECK.getCaption(),
        ColumNamesConstants.ID_PUBLISH.getCaption()
    //ColumNamesConstants.ISSELECCION.getCaption()
    };

    private final Class[] classes = new Class[]{
        ImageIcon.class,
        Date.class,
        Date.class,
        String.class,
        String.class,
        String.class,
        //String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        //String.class,
        //Botones
        Object.class,
        String.class,
        String.class,
        String.class,
        String.class,
        //Check
        Boolean.class,
        String.class
    };

    public final String convertDateFormat(String date, String formatIn, String formatOut) throws ParseException {

        String outFormat;
        SimpleDateFormat inFormat = new SimpleDateFormat(formatIn);
        Date d = inFormat.parse(date);
        inFormat.applyPattern(formatOut);
        outFormat = inFormat.format(d);

        return outFormat;
    }
    
    public final Date convertStringToDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date retValue = null;
        try {
            retValue = format.parse(dateStr);
        } catch (ParseException ex) {
            Utilerias.logger(getClass()).debug(ex);
        } catch (Exception e){
            Utilerias.logger(getClass()).debug(e);
        }
        return retValue;
    }

    private List<DocumentSearchBO> list;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, "", "", null, Boolean.FALSE
    };

    public SearchTableModel(List<DocumentSearchBO> list) {

        data = new Object[list == null ? 0 : list.size()][];
        String datePublishDocument = " ";
        String dateCreateDocument = "";

        for (int i = 0; i < data.length; i++) {
            DocumentSearchBO row = list.get(i);

            if (row == null) {
                return;
            }

            try {
                // si esta programado para publicacion la fecha se toma  de la tabla document_send campo date
                if (row.isScheduled()) {
                    datePublishDocument = row.getDocPublicationDate();
                    if (datePublishDocument != null && !datePublishDocument.isEmpty()) {
                        datePublishDocument = convertDateFormat(datePublishDocument, "yyyy-MM-dd", "dd-MM-yyyy");
                    }

                } else {
                    // si no esta programada es envio inmediato se toma de la tabla document_send del campo date_publish
                    if (row.getIdStatus_publish() == 2 || row.getIdStatus() == 3 || row.getIdStatus() == 4) {
                        datePublishDocument = row.getDate_publish();
                        if (datePublishDocument != null && !datePublishDocument.isEmpty()) {
                            datePublishDocument = datePublishDocument.substring(0, 10);
                            datePublishDocument = convertDateFormat(datePublishDocument, "yyyy/MM/dd", "dd/MM/yyyy");
                        }
                    } else {
                        datePublishDocument = row.getDocCreationDate();
                        if (datePublishDocument != null && !datePublishDocument.isEmpty()) {
                            datePublishDocument = convertDateFormat(datePublishDocument, "yyyy-MM-dd", "dd/MM/yyyy");
                        }
                    }
                }

                if (row.getDocCreationDate() != null) {
                    dateCreateDocument = convertDateFormat(row.getDocCreationDate(), "yyyy-MM-dd", "dd/MM/yyyy");
                }

            } catch (ParseException ex) {
                Utilerias.logger(getClass()).info(ex);
            }

            ImageIcon icon = getImageIcon(row.isCollaborative(), row.getDocumentId(), row.getCollaborative_Type());
            
            data[i] = new Object[]{
                icon,
                datePublishDocument != null ? convertStringToDate(datePublishDocument) : "",
                dateCreateDocument != null ? convertStringToDate(dateCreateDocument) : "",
                row.getFileName(),
                row.getDocTypeName(),
                tituloCodificado(row),//row.getSubjectName()==null ? "" : new StringBuilder(row.getSubjectName()).append(" : ").append(row.getDocumentName()).toString(),
                //row.getAuthor(),
                "adPublish",
                row.getMarketName(),
                row.getDocument_status(),
                row.getIndustryName(),
                //row.getLanguageName(),
                ColumNamesConstants.BTN_PDF.getCaption(), //PDF
                ColumNamesConstants.BTN_HTML.getCaption(),
                ColumNamesConstants.BTN_EDIT_DOC.getCaption(),
                ColumNamesConstants.BTN_EDIT_PUBLISH.getCaption(),
                ColumNamesConstants.BTN_NEW.getCaption(),
                row.isCheck(),
                row.getIdPublish()
            //row.getEnableForDocumentTypeProfile()

            };
        }
        this.list = list;
    }

    private String tituloCodificado(DocumentSearchBO row) {
        if (row.getTitle_regex() == null || row.getTitle_regex().isEmpty()) {
            if(row.getSubjectName() == null){
                if(row.getDocumentName() == null || row.getDocumentName().trim().isEmpty()){
                    return "";
                }else{
                    return row.getDocumentName();
                }
            }else{
                if(row.getDocumentName() == null || row.getDocumentName().trim().isEmpty()){
                    return row.getSubjectName();
                }else{
                    return row.getSubjectName() + " : " + row.getDocumentName();
                }
            }

//            String tituloOut;
//            if (titulo.length() > 100) {
//                tituloOut = titulo.substring(0, 100);
//            } else {
//                tituloOut = titulo;
//            }
//            return tituloOut;
        }

        String codigo = row.getTitle_regex();

        Map<String, String> precod = new HashMap<>();
        precod.put("@TEMA", row.getSubjectName() == null || row.getSubjectName().isEmpty() ? "" : row.getSubjectName());
        precod.put("@TITULO", row.getDocumentName() == null || row.getDocumentName().isEmpty() ? "" : row.getDocumentName());
        precod.put("@TIPO_DE_DOCUMENTO", row.getDocTypeName() == null || row.getDocTypeName().isEmpty() ? "" : row.getDocTypeName());
        precod.put("@MERCADO", row.getMarketName() == null || row.getMarketName().isEmpty() ? "" : row.getMarketName());
        precod.put("@NOMENCLATURA", row.getNomenclature() == null || row.getNomenclature().isEmpty() ? "" : row.getNomenclature());

        codigo = Utilerias.armarTitulo(precod, codigo);

//        String codigoOut;
//        if (row.getDocTypeName().length() > 100) {
//            codigoOut = codigo.substring(0, 100);
//        } else {
//            codigoOut = codigo;
//        }
        return codigo;
    }

    private ImageIcon getImageIconByStatus(int status) {
        ImageIcon icon = null;
        switch (status) {
            case 0:
            case 1: //No enviado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SEARCH_NEW_DOCUMENT);
                break;
            case 2: //Enviado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SEND_32);
                break;
            case 3: //Guardado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SAVE_32);
                break;
            case 4: //Programado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SCHEDULED);
                break;

            case 5: //cancelado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.EXIT_32);
                break;
        }
        return icon;
    }

    private ImageIcon getImageIcon(boolean collab, int idDoc, String collabType) {
        ImageIcon icon = null;

        if (collab) {
            //collaborativo
            if(collabType != null && collabType.equals("M")){
             icon = Utilerias.getImageIcon(Utilerias.ICONS.COLLAB);
            }else{
             icon = Utilerias.getImageIcon(Utilerias.ICONS.COLLAB_DOC);
            }
        } else if (idDoc > 0 && idDoc < 3000000) {
            // documento con id    
            icon = Utilerias.getImageIcon(Utilerias.ICONS.DOCUMENT);
        } else {
            // pub attachment
            icon = Utilerias.getImageIcon(Utilerias.ICONS.DOCUMENT_ATTACH);
        }

        return icon;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.length : 0;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        return column >= columnNames.length ? columnNames[columnNames.length - 1] : columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex >= classes.length ? classes[classes.length - 1] : classes[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (data == null || data.length == 0) {
            return Object.class;
        }

        Object value = null;
        try {
            //Seleccionar imagen a pintar en base a 

            value = data[rowIndex][columnIndex];
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Verificar si el elemento contiene -1 en idLanguage
        if (data == null || data.length == 0) {
            return false;
        } else {
            //Editar todas las columnas
            if (col == StatementConstant.SC_10.get()
                    || col == StatementConstant.SC_11.get()
                    || col == StatementConstant.SC_12.get()
                    || col == StatementConstant.SC_13.get()
                    || col == StatementConstant.SC_14.get()) {
                return true;
            } else {

                boolean b = false;
                if (col == SearchTableModel.HAND_CURSOR_COLUMN) {

                    String tipoDoc = (String) data[row][4];
                    int tipoDocID = 0;
                    
                    List<DocumentSearchBO> list = getList();
                    for(DocumentSearchBO bo : list){
                      if(bo.getDocTypeName().equals(tipoDoc)){
                          tipoDocID = bo.getIdDocType();
                          break;
                      }
                    }
                    
                    int perfilID = InstanceContext.getInstance().getUsuario().getPerfilId();
                    DocumentTypeProfileDAO dao = new DocumentTypeProfileDAO();
                    List<DocumentTypeProfileBO> lstPerfiles = dao.get(tipoDocID);

                    b = false;

                    for (DocumentTypeProfileBO lst : lstPerfiles) {
                        if (perfilID == lst.getIdprofile()) {
                            b = true;
                            break;
                        }
                    }

                }
                return b;
            }
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
        fireTableDataChanged();
    }

    public Object[] getEmptyRow() {
        return emptyRow;
    }

    public void setEmptyRow(Object[] emptyRow) {
        this.emptyRow = emptyRow;
    }

    public Boolean isCheckedRow(int row) {
        return (Boolean) ((data == null || data.length == 0) ? false : data[row][0]);
    }

    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][1]);
    }

    public Integer getRowIdPublish(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][16]);
    }
}
