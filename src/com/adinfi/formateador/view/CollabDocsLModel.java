/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocCollabCandidateBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Desarrollador
 */
 class CollabDocsLModel extends AbstractTableModel  {
        List<DocumentCollabItemBO> list; 
        
        
    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("5", null),      
        NAME("NAME", "Nombre"),
        SUBJECT("SUBJECT_NAME", "Tema"),
        DOCUMENT_NAME("DOCUMENT_NAME", "Título"),         
        DOCUMENT_TYPE_NAME("DOCUMENT_TYPE_NAME", "Tipo de documento"),        
        AUTOR("AUTOR", "Autor");            
        

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
    
    
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;    
    
    
    private final String[] columnNames = new String[]{            
        CollabDocsCandTModel.ColumNamesConstants.NAME.getCaption(),      
        CollabDocsCandTModel.ColumNamesConstants.DOCUMENT_NAME.getCaption(),  
        CollabDocsCandTModel.ColumNamesConstants.DOCUMENT_TYPE_NAME.getCaption(),                          
        CollabDocsCandTModel.ColumNamesConstants.SUBJECT.getCaption(),      
        CollabDocsCandTModel.ColumNamesConstants.AUTOR.getCaption(),       
    };

    private final Class[] classes = new Class[]{           
        String.class,
        String.class,
        String.class,
        String.class,
        String.class            
       
       
    };    
        
        
        
        /*
        void setData(List<DocumentCollabItemBO> data) {
           this.data=data;
           
           if(data!=null){
               for( DocumentCollabItemBO docCollab : data ){
                   super.addElement(docCollab.getDocumentName());
               }
           }
           
        } */

        
    
    
    public void setList(List<DocumentCollabItemBO> list) {
        this.list=list;
        refreshData();
    }        
    
    
    
    public void refreshData() {
        if(this.list==null){
            return;
        }
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < data.length; i++) {
            DocumentCollabItemBO row = list.get(i);
            
            data[i] = new Object[]{    
                
                row.getFileName(),
                row.getDocumentName() ,    
               row.getDocTypeName() ,                
                row.getTema() ,
                row.getAutor()
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
        DocumentCollabItemBO collab=this.list.get(row);       
        fireTableCellUpdated(row, col);
    }

    
    public Object[][] getData() {
        return data;
    }
    
    
        public List<DocumentCollabItemBO> getList(){
            
            return this.list;
        }    

    
    public void setData(Object[][] data) {
        this.data = data;
        fireTableDataChanged();
    }

  

    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row][1]);
    }
    
   @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }
    
    
    public void addElement( DocumentCollabItemBO value  ){
            if( list==null ){
                list=new ArrayList<>();
            }
            list.add(value);
            
            /*Collections.sort(list, new Comparator<DocumentCollabItemBO>() {
                    @Override
                    public int compare(DocumentCollabItemBO p1, DocumentCollabItemBO p2) {
                        final String fileName1 = p1.getFileName();
                        final String fileName2 = p2.getFileName();
                        
                        if (fileName1 == null) {
                            if (fileName2 == null) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                        if (fileName2 == null) {
                            return -1;
                        }
                                             
                        return fileName1.compareTo(fileName2);
                    }
            });*/
            
            refreshData();
            //super.addElement(value.getDocumentName() );
    }
    
    
    
    
    
    
    /*
    
        
        public void setElementAt(DocumentCollabItemBO value , int idx){
            data.set(idx, value);
            super.addElement(value.getDocumentName() );
            
        }
         
        public void addElement( DocumentCollabItemBO value  ){
            if( data==null ){
                data=new ArrayList<>();
            }
            data.add(value);
            super.addElement(value.getDocumentName() );
        }
        
        public List<DocumentCollabItemBO> getData(){
            
            return this.data;
        }
        
        @Override
        public Object getElementAt(int index) {
            DocumentCollabItemBO docCollab=data.get(index);
            if( docCollab==null ){
                return "";
            }
            return docCollab.getDocumentName();
           // return getValueAt(index, 3);
        }

        public int getSize() {
            if(data==null ){
                return 0;
            }
            return data.size();
        }

    */

  
    }