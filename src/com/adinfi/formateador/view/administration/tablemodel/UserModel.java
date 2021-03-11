package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.seguridad.Usuario;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Guillermo Trejo
 */
public class UserModel extends AbstractTableModel {

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("12", null),
        CHECK("", ""),
        IDUSER("IDUSSER", "IdUsuario*"),
        NAME("NAME", "Nombre*"),
        EXTENSION("EXTENSION", "Extensión*"),
        EMAIL("EMAIL", "Correo*"),
        NTUSER("NTUSER", "Usuario de Red*"),
        PROFILE("PROFILE", "Perfil*"),
        DATECREATE("DATECREATE", "Fecha de alta"),
        DATEMODIFY("DATEMODIFY", "Fecha de Modificación"),
        ISAUTHOR("ISAUTHOR", "Autor"),
        PROFILEID("PROFILEID", "idProfile"),
        ISDIRECTORY("ISDIRECTORY","Directorio");

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
        ColumNamesConstants.IDUSER.getCaption(),
        ColumNamesConstants.NAME.getCaption(),
        ColumNamesConstants.EXTENSION.getCaption(),
        ColumNamesConstants.EMAIL.getCaption(),
        ColumNamesConstants.NTUSER.getCaption(),
        ColumNamesConstants.PROFILE.getCaption(),
        ColumNamesConstants.DATECREATE.getCaption(),
        ColumNamesConstants.DATEMODIFY.getCaption(),
        ColumNamesConstants.ISAUTHOR.getCaption(),
        ColumNamesConstants.PROFILEID.getCaption(),
        ColumNamesConstants.ISDIRECTORY.getCaption()
    };
    private final Class[] classes = new Class[]{
        Boolean.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class,Boolean.class,Integer.class,Boolean.class
    };
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());
    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, "", "", "", "", "", "", "", Boolean.FALSE,"",Boolean.FALSE
    };

    public UserModel(List<Usuario> list) {
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            
          
            Usuario row = list.get(i);
            data[i] = new Object[]{
                row.isCheck(),
                row.getUsuarioId(),
                row.getNombre(),
                row.getExtension(),
                row.getCorreo(),
                row.getUsuarioNT(),
                row.getPerfil(),
                row.getFechaAlta(),
                row.getUltimoAcceso(),
                row.isIsAutor(),
                row.getPerfilId(),
                row.isDirectorio()
            };
        }
    }

    public UserModel() {

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
            //return Boolean.parseBoolean(String.valueOf(data[row][0])) == true;
            return false;
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
        return (boolean) ((data == null || data.length == 0) ? false : data[row][0]);
    }

    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][1]);
    }
}
