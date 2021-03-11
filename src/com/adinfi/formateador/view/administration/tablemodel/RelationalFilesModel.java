/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.OfficeBO;
import com.adinfi.formateador.bos.RelationalFilesBO;
import com.adinfi.formateador.util.Utilerias;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author USUARIO
 */
public class RelationalFilesModel extends AbstractTableModel{
    
    String dummy;
    
    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("11", null),
        IMAGEN("", ""),
        FECHA("FECHA", "Fecha de Envio"),
        TIPODOC("TIPODOC", "Tipo Documento"),
        TEMA("TEMA", "Tema"),
        TITULO("TITULO","Titulo"),
        AUTOR("AUTOR", "Autor"),
        MERCADO("MERCADO","Mercado"),
        SECTOR("SECTOR","Sector"),
        IDIOMA("IDIOMA","Idioma"),
        CHECK("",""),
        IDPUBLISH("ID Publicación","ID Publicación");
        

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
        ColumNamesConstants.IMAGEN.getCaption(), //0
        ColumNamesConstants.FECHA.getCaption(),    //1
        ColumNamesConstants.TIPODOC.getCaption(),//2
        ColumNamesConstants.TEMA.getCaption(),//3
        ColumNamesConstants.TITULO.getCaption(),//4
        ColumNamesConstants.AUTOR.getCaption(),//5
        ColumNamesConstants.MERCADO.getCaption(),//6
        ColumNamesConstants.SECTOR.getCaption(),//7
        ColumNamesConstants.IDIOMA.getCaption(),//8
        ColumNamesConstants.CHECK.getCaption(),//9
        ColumNamesConstants.IDPUBLISH.getCaption(),//10
    };
    private final Class[] classes = new Class[]{
        ImageIcon.class,
        String.class,
        String.class, 
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        Boolean.class,
        Integer.class,
    };
     private List<RelationalFilesBO> list;
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private Object[][] data = null;
    private Object[] emptyRow = new Object[]{
        Boolean.TRUE, -1, 0,"","","",0,"","","",0,"","","","", null, Boolean.FALSE,0,null
    };
    
    
        private ImageIcon getImageIcon(int status) {
        ImageIcon icon = null;
        switch (status) {
            case 0:
            case 1: //No enviado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SEARCH_NEW_DOCUMENT);
                break;
            case 2: //Enviado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SMALL_SEND);
                break;
            case 3: //Guardado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SMALL_SAVE);
                break;
            case 4: //Programado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SMALL_NOTIFY);
                break;

            case 5: //cancelado
                icon = Utilerias.getImageIcon(Utilerias.ICONS.SMALL_EXIT);
                break;
        }
        return icon;
    }
    
    
    public RelationalFilesModel(List<RelationalFilesBO>  list) {
        data = new Object[list == null ? 0 : list.size()][];
        for (int i = 0; i < list.size(); i++) {
            
             RelationalFilesBO row = list.get(i);
             ImageIcon icon = getImageIcon(1);
             
            data[i] = new Object[]{
                icon,                             //0
                row.getDocPublicationDate(),      //1
                row.getDocTypeName(),             //2
                row.getSubjectName(),             //3
                row.getDocument_name(),             //3
                row.getAuthor(),                  //4
                row.getMarketName(),              //5
                row.getIndustryName(),              //5
                row.getLanguageName(),              //5
                row.isCheck(),                    //6
                row.getIdDocSend()
            };
        }
                this.list = list;
    }

    public RelationalFilesModel() {
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
            if (col == 9) {
                return true;
            }
            return Integer.parseInt(String.valueOf(getData()[row][10])) == -1;
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
        return (Boolean)((getData() == null || getData().length == 0) ? false : getData()[row][9]);
    }
    
    public Integer getUniqueRowIdentify(int row) {
        return (Integer)((getData() == null || getData().length == 0) ? -1 : getData()[row][10]);
        
    }
    
     public String getTitleofDoc(int row) {
        return (String)((getData() == null || getData().length == 0) ? -1 : getData()[row][4]);
    }
}
