/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author USUARIO
 */
public class SearchTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);

        if (hasFocus) {
            c.setBackground(UIManager.getDefaults().getColor("Table.focusCellBackground"));
        } 
        
        if (isSelected) {
            c.setBackground(UIManager.getDefaults().getColor("Table.selectionBackground"));
        } else {            
            c.setBackground(UIManager.getDefaults().getColor("Table.background"));
            //c.setBackground(Color.YELLOW);
        }
 
        return c;

    }
//    @Override
//    public Component getTableCellRendererComponent(final JTable table,
//            final Object value, final boolean isSelected,
//            final boolean hasFocus, final int row, final int column) {
//            // customize second column
//        if (0 == column && value != null) {
//            // TODO: fix darker highlighting here
//            return new JLabel(value.toString()) {
//                @Override
//                public Color getBackground() {
//                    if (isSelected) {
//                        //System.out.println("isSelected");
//                        return UIManager.getDefaults().getColor("Table.selectionBackground");
//                        
//                    } else {
//                        //System.out.println("else");
//                        return UIManager.getDefaults().getColor("Table.background");
//                        
//                    }
//                }
//            };
//        }
//        return super.getTableCellRendererComponent(table, value,
//                isSelected, hasFocus, row, column);
//    }

}
