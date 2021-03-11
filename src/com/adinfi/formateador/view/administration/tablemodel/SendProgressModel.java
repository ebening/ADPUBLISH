/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.DocumentFormatBO;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vectoran
 */
public class SendProgressModel extends AbstractTableModel {
    
    public SendProgressModel(){}
    
    public SendProgressModel(List<DocumentFormatBO> list){
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            DocumentFormatBO row = list.get(i);
            data[i] = new Object[]{
                row.isCheck(),      //o
                row.getIdDocument_send(),   //1
                row.getDate_publish(),//2
                row.getDocument_type_name(),//3
                row.getDocumentTitle(),//4
                row.getIndustry_name()//5
            };
        }
    }

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("6", null),
        CHECK("", ""),
        ID("ID", "ID"),
        FECHA("FECHA", "Fecha"),
        TIPODOCUMENTO("TIPODOCUMENTO", "Tipo de Documento"),
        TITULO("TITULO", "Titulo"),
        SECTOR("SECTOR", "Sector");
        

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
        ColumNamesConstants.CHECK.getCaption(), //0
        ColumNamesConstants.ID.getCaption(),    //1
        ColumNamesConstants.FECHA.getCaption(),//2
        ColumNamesConstants.TIPODOCUMENTO.getCaption(),//3
        ColumNamesConstants.TITULO.getCaption(),//4
        ColumNamesConstants.SECTOR.getCaption()//5
    };
    
    private final Class[] classes = new Class[]{
        Boolean.class,
        Integer.class,
        String.class,
        String.class,
        String.class,
        String.class
    };
    
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, "", "", "", ""
    };
    
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
        return (Boolean)((getData() == null || getData().length == 0) ? false : getData()[row][0]);
    }
    
    public Integer getUniqueRowIdentify(int row) {
        return (Integer)((getData() == null || getData().length == 0) ? -1 : getData()[row][1]);
    }
    
    @Override
    public int getRowCount() {
        return getData() != null ? getData().length : 0;
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

        if (getData() == null || getData().length == 0) {
            return Object.class;
        }
        
        Object value = null;
        try {
            value = getData()[rowIndex][columnIndex];
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        getData()[rowIndex][columnIndex] = aValue;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Verificar si el elemento contiene -1 en idLanguage
        if (getData() == null || getData().length == 0) {
            return false;
        } else {
            if (col == 0) {
                return true;
            }
            return Integer.parseInt(String.valueOf(getData()[row][1])) == -1;
        }
    }
    
}
