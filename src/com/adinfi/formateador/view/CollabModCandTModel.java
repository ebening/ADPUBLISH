/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocCollabCandidateBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.util.Utilerias;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author USUARIO
 */
public class CollabModCandTModel extends AbstractTableModel {

    public static int CHECK_COLUMN = 0;

    /**
     * @return the list
     */
    public List<DocCollabCandidateBO> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<DocCollabCandidateBO>  list) {
        this.list = list;
    }

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("6", null),      
        SUBJECT("SUBJECT_NAME", "Tema"),
        DOCUMENT_NAME("DOCUMENT_NAME", "Título"),         
        DOCUMENT_TYPE_NAME("DOCUMENT_TYPE_NAME", "Tipo de documento"),        
        AUTOR("AUTOR", "Autor"),     
        ID("ID", "Id"),  
        CHECK("", "");

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
        ColumNamesConstants.DOCUMENT_NAME.getCaption(),
        ColumNamesConstants.DOCUMENT_TYPE_NAME.getCaption(),         
        ColumNamesConstants.SUBJECT.getCaption(),    
        ColumNamesConstants.AUTOR.getCaption(),            
    };

    private final Class[] classes = new Class[]{   
        Boolean.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class       
        
       
    };

    private List<DocCollabCandidateBO> list;    
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, "", "", null, Boolean.FALSE
    };
    
     public CollabModCandTModel() {
         
     }

    public void setData(List<DocCollabCandidateBO> list) {
        this.list=list;
        refreshData();
    }

    
    public void refreshData() {
        if(this.list==null){
            return;
        }
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < data.length; i++) {
            DocCollabCandidateBO row = list.get(i);
            
            data[i] = new Object[]{    
                 row.isCheck(),
                 row.getName() , 
                 row.getDocSrcName() ,
                 row.getCandTypeName(),
                 row.getCandSubject() ,                               
                 row.getCandAuthor()
    
            };
        }
        this.list = list;
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
            if( columnIndex==CHECK_COLUMN ){
                
            }
            
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        DocCollabCandidateBO collab=this.list.get(row);
        if(collab!=null){
            collab.setCheck((Boolean)value);
        }
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
        return (Boolean) ((data == null || data.length == 0) ? false : data[row][CHECK_COLUMN]);
    }

    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][1]);
    }
    
   @Override
    public boolean isCellEditable(int row, int col) {
        //Verificar si el elemento contiene -1 en idLanguage
        if (data == null || data.length == 0) {
            return false;
        } else {
            //Editar todas las columnas
            if ( col == CollabModCandTModel.CHECK_COLUMN ) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    
    
    
    
}
