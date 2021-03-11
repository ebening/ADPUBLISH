package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.LinkedExcelBO;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 *
 * @author Josue Sanchez
 */
public class LinkedExcelModel extends AbstractTableModel {

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("11", null),
        CHECK("", ""),
        ID("ID", "Id Excel"),
        ID_CATEGORY("ID_CATEGORY", "Id Cat"),
        CATEGORY("CATEGORY", "Categoría*"),
        NAME("NAME", "Nombre*"),
        PATH("PATH", "Ruta*"),
        SHEET("SHEET", "Hoja*"),
        XY1("XY1", "Celda inicial*"),
        XY2("XY2", "Celda final*"),
        RANGE("RANGE","Rango*"),
        ISCELL("ISCELL", "CELL?");

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
        ColumNamesConstants.ID_CATEGORY.getCaption(),
        ColumNamesConstants.CATEGORY.getCaption(),
        ColumNamesConstants.NAME.getCaption(),
        ColumNamesConstants.PATH.getCaption(),
        ColumNamesConstants.SHEET.getCaption(),
        ColumNamesConstants.XY1.getCaption(),
        ColumNamesConstants.XY2.getCaption(),
        ColumNamesConstants.RANGE.getCaption(),
        ColumNamesConstants.ISCELL.getCaption()
    };
    private final Class[] classes = new Class[]{
        Boolean.class,
        Integer.class,
        Integer.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        Boolean.class
    };
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, "", "", "", "", "", "", "", "", "", ""
    };

    public LinkedExcelModel(List<LinkedExcelBO> list) {
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            LinkedExcelBO row = list.get(i);
            data[i] = new Object[]{
                row.isCheck(),
                row.getIdLinkedExcel(),
                row.getId_category(),
                row.getCategory(),
                row.getName(),
                row.getPath(),
                row.getSheet(),
                row.getXY1(),
                row.getXY2(),
                row.getRange(),
                row.isIsCell()
            };
        }
    }

    public LinkedExcelModel() {

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
        return (Boolean) ((data == null || data.length == 0) ? false : data[row][0]);
    }

    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][1]);
    }
}
