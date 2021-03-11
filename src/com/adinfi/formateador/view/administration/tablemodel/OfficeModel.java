package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.OfficeBO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Guillermo Trejo
 */
public class OfficeModel extends AbstractTableModel {

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("17", null),
        CHECK("", ""),
        ID("ID", "ID"),
        ORDER("ORDER", "Orden*"),
        BRANCH("BRANCH", "Sucursal*"),
        ADDRESS("ADDRESS", "Dirección*"),
        DISTRICT("DISTRICT", "Colonia*"),
        ZIPCODE("ZIPCODE","Código Postal*"),
        CITY("CITY","Municipio*"),
        STATE("STATE","Estado*"),
        COUNTRY("COUNTRY","País*"),
        NATIONAL("NATIONAL","Nacional"),
        PHONE1("PHONE1","Teléfono 1*"),
        PHONE2("PHONE1","Teléfono 2"),
        PHONE3("PHONE1","Teléfono 3"),
        PHONE4("PHONE1","Teléfono 4"),
        DATEMODIFY("DATEMODIFY","Fecha de Modificación"),
        STATUS("STATUS","Estado");
        

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
        ColumNamesConstants.ORDER.getCaption(),//2
        ColumNamesConstants.BRANCH.getCaption(),//3
        ColumNamesConstants.ADDRESS.getCaption(),//4
        ColumNamesConstants.DISTRICT.getCaption(),//5
        ColumNamesConstants.ZIPCODE.getCaption(),//6
        ColumNamesConstants.CITY.getCaption(),//7
        ColumNamesConstants.STATE.getCaption(),//8
        ColumNamesConstants.COUNTRY.getCaption(),//9
        ColumNamesConstants.NATIONAL.getCaption(),//10
        ColumNamesConstants.PHONE1.getCaption(),//11
        ColumNamesConstants.PHONE2.getCaption(),//12
        ColumNamesConstants.PHONE3.getCaption(),//13
        ColumNamesConstants.PHONE4.getCaption(),//14
        ColumNamesConstants.DATEMODIFY.getCaption(),//15
        ColumNamesConstants.STATUS.getCaption(),//16
    };
    private final Class[] classes = new Class[]{
      Boolean.class,
          Integer.class,
          Integer.class, 
        String.class,
        String.class,
        String.class,
          Integer.class,
        String.class, 
        String.class,
        String.class,
      Boolean.class,
        String.class,
        String.class,
        String.class,
        String.class,
           Date.class,
      Boolean.class
    };
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, 0,"","","",0,"","","",0,"","","","", null, Boolean.FALSE
    };

    public OfficeModel(List<OfficeBO> list) {
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            OfficeBO row = list.get(i);
            data[i] = new Object[]{
                row.isCheck(),      //o
                row.getIdOffice(),   //1
                row.getOrder(),//2
                row.getBranch(),//3
                row.getAddress(),//4
                row.getDistrict(),//5
                row.getZipCode(),//6
                row.getCity(),//7
                row.getState(),//8
                row.getCountry(),//9
                row.isNational(),//10
                row.getPhone1(),//11
                row.getPhone2(),//12
                row.getPhone3(),//13
                row.getPhone4(),//14
                row.getDateModify(),//15
                row.isStatus()//16
            };
        }
    }

    public OfficeModel() {
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
        return (Boolean)((getData() == null || getData().length == 0) ? false : getData()[row][0]);
    }
    
    public Integer getUniqueRowIdentify(int row) {
        return (Integer)((getData() == null || getData().length == 0) ? -1 : getData()[row][1]);
    }
}
