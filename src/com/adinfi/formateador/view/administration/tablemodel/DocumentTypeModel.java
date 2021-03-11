package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.DocumentTypeBO;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Guillermo Trejo
 */
public class DocumentTypeModel  extends AbstractTableModel {

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("21", null),
        CHECK("", ""),
        ID("ID", "ID"),
        NAME("NAME","Nombre*"),
        MARKET("MARKET","Mercado*"),
        NOMENCLATURA("NOMENCLATURA", "Nomenclatura*"),
        OUTGOINGEMAIL("OUTGOINGEMAIL","Correo Saliente*"),
        OUTGOINGEMAILID("OUTGOINGEMAILID","OUTGOING ID"),
        PUBLISHPROFILE("PUBLISHPROFILE"," Perfil de Pulicación"),
        DISCLOSURE("DISCLOSURE","Disclosure"),
        SENDMEDIA("SENDMEDIA","Enviar a medios"),
        PUBLISH("PUBLISH","Publicación"),
        COLLABORATIVE("COLLABORATIVE","Colaborativo"),
        SENDEMAIL("SENDEMAIL","Send"),
        SUBJECT("SUBJECT","tema"),
        TITLE("TITLE","titulo"),
        SUBTITLE("SUBTITLE","subtitulo"),
        HTML("HTML","html"),
        PDF("PDF","pdf"),
        IDMIVECTOR("IDMIVECTOR","IDMIVECTOR"),
        IDDOCUMENT_TYPE_VECTOR("IDDOCUMENT_TYPE_VECTOR","IDDOCUMENT_TYPE_VECTOR"),
        REGEX("REGEX","Regex");
        
        
        
        
     

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
        ColumNamesConstants.CHECK.getCaption(),
        ColumNamesConstants.ID.getCaption(),
        ColumNamesConstants.NAME.getCaption(),
        ColumNamesConstants.MARKET.getCaption(),
        ColumNamesConstants.NOMENCLATURA.getCaption(),
        ColumNamesConstants.OUTGOINGEMAIL.getCaption(),
        ColumNamesConstants.OUTGOINGEMAILID.getCaption(),
        ColumNamesConstants.PUBLISHPROFILE.getCaption(),
        ColumNamesConstants.DISCLOSURE.getCaption(),
        ColumNamesConstants.SENDMEDIA.getCaption(),
        ColumNamesConstants.PUBLISH.getCaption(),
        ColumNamesConstants.COLLABORATIVE.getCaption(),
        ColumNamesConstants.SENDEMAIL.getCaption(),
        ColumNamesConstants.SUBJECT.getCaption(),
        ColumNamesConstants.TITLE.getCaption(),
        ColumNamesConstants.SUBTITLE.getCaption(),
        ColumNamesConstants.HTML.getCaption(),
        ColumNamesConstants.PDF.getCaption(),
        ColumNamesConstants.IDMIVECTOR.getCaption(),
        ColumNamesConstants.IDDOCUMENT_TYPE_VECTOR.getCaption(),
        ColumNamesConstants.REGEX.getCaption()
        
    };
    private final Class[] classes = new Class[]{
        Boolean.class,
        Integer.class,
        String.class,
        String.class,
        String.class,
        String.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        Boolean.class,
        String.class,
        String.class,
        String.class
    };
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, "", "", null, Boolean.FALSE
    };

    public DocumentTypeModel(List<DocumentTypeBO> list) {
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            DocumentTypeBO row = list.get(i);
            data[i] = new Object[]{
                row.isCheck(),
                row.getIddocument_type(),
                row.getName(),
                row.getName_market(),
                row.getNomenclature(),
                row.getEmail(),
                row.getIdOutgoingEmail(),
                row.getIdPublishProfile(),
                row.getIdDisclosure(),
                row.isSendMedia(),
                row.isPublish(),
                row.isCollaborative(),
                row.isSendEmail(),
                row.isSubject(),
                row.isTitle(),
                row.isSubTitle(),
                row.isHtml(),
                row.isPdf(),
                row.getIdMiVector(),
                row.getIddocument_type_vector(),
                row.getTitle_regex()
                    
            };
        }
    }

    public DocumentTypeModel() {

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
            if (col == 0) {
                return true;
            }
            // habilitar edición en fila seleccionada
            if (Boolean.parseBoolean(String.valueOf(data[row][0])) == true) {
                return true;
            } else {
                return false;
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
        return (Boolean)((data == null || data.length == 0) ? false : data[row][0]);
    }


    public Integer getUniqueRowIdentify(int row) {
        return (Integer)((data == null || data.length == 0) ? -1 : data[row][1]);
    }
    
      public String  getIdMiVector(int row) {
        return (String)((data == null || data.length == 0) ? -1 : data[row][18]);
    }
      
        public String getIdDocumentTypeVector(int row) {
        return (String)((data == null || data.length == 0) ? -1 : data[row][19]);
    }
}
