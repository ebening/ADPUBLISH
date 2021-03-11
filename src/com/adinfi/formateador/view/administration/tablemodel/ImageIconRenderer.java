/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author USUARIO
 */
public class ImageIconRenderer extends DefaultTableCellRenderer  {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        ImageIcon imageIcon = (ImageIcon) value;
        label.setIcon(imageIcon);
        label.setText("");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        
        if (hasFocus) {
            setBackground(UIManager.getDefaults().getColor("Table.focusCellBackground"));
        } else if (isSelected) {
            setBackground(UIManager.getDefaults().getColor("Table.selectionBackground"));
        } else {            
            setBackground(UIManager.getDefaults().getColor("Table.background"));
            //setBackground(Color.YELLOW);
        } 

        return label;
    }
}
