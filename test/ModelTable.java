/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

abstract class ModelTable extends AbstractTableModel implements TableModelListener {

    public Object[][] data;
    public String[] columNames;

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public String[] getColumNames() {
        return columNames;
    }

    public void setColumNames(String[] columNames) {
        this.columNames = columNames;
    }

   
    public void addRow() {
//       this.data = data;
//       this.fireTableRowsInserted(0,0);
        
//        String[] data = new String[getColumNames().length];
//        for (int i = 0; i < getColumNames().length; i++) {
//            data[i] = "";
//        }
//       
        
        fireTableChanged(null);
    }

    public int getColumnCount() {
        return getColumNames().length;
    }

    @Override
    public int getRowCount() {
        return getData().length;
    }

    @Override
    public String getColumnName(int col) {
        return getColumNames()[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return getData()[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
//            if (col < 2) {
//                return false;
//            } else {
//                return true;
//            }

        return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        getData()[row][col] = value;
        fireTableCellUpdated(row, col);
    }

}
