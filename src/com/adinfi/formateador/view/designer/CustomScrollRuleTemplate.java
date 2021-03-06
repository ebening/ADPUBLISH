/*
 * Copyright (c) 1995, 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.adinfi.formateador.view.designer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import com.adinfi.formateador.view.designer.*;

public class CustomScrollRuleTemplate extends JPanel
        implements ItemListener {

    private Rule columnView;
    private Rule rowView;
    private JToggleButton isMetric;
    private ScrollablePanel picture;
    private CellPanelTemplate cellPanelTemplate;

    public CustomScrollRuleTemplate(int heightModule) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        //Create the row and column headers.
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);
        

        columnView.setPreferredWidth(320);
        rowView.setPreferredHeight(480);

        //Create the corners.
        JPanel buttonCorner = new JPanel(); //use FlowLayout
        isMetric = new JToggleButton("cm", true);
        isMetric.setFont(new Font("SansSerif", Font.PLAIN, 11));
        isMetric.setMargin(new Insets(2, 2, 2, 2));
        isMetric.addItemListener(this);
        buttonCorner.add(isMetric);

        //Set up the scroll pane.
        picture = new ScrollablePanel(columnView.getIncrement());
        
        picture.setLayout(new BorderLayout());
        
        cellPanelTemplate = new CellPanelTemplate(heightModule);
        
        picture.add(cellPanelTemplate, BorderLayout.CENTER);
        
      
        JScrollPane pictureScrollPane = new JScrollPane(picture);
        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
        pictureScrollPane.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));

        pictureScrollPane.setColumnHeaderView(columnView);
        pictureScrollPane.setRowHeaderView(rowView);

        //Set the corners.
        //In theory, to support internationalization you would change
        //UPPER_LEFT_CORNER to UPPER_LEADING_CORNER,
        //LOWER_LEFT_CORNER to LOWER_LEADING_CORNER, and
        //UPPER_RIGHT_CORNER to UPPER_TRAILING_CORNER.  In practice,
        //bug #4467063 makes that impossible (in 1.4, at least).
        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                buttonCorner);
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER,
                new Corner());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER,
                new Corner());

        //Put it in this panel.
        add(pictureScrollPane);
        setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 10));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            //Turn it to metric.
            rowView.setIsMetric(true);
            columnView.setIsMetric(true);
        } else {
            //Turn it to inches.
            rowView.setIsMetric(false);
            columnView.setIsMetric(false);
        }
        picture.setMaxUnitIncrement(rowView.getIncrement());
    }

    /**
     * @return the cellPanel
     */
    public CellPanelTemplate getCellPanelTemplate() {
        return cellPanelTemplate;
    }

    /**
     * @param cellPanel the cellPanel to set
     */
    public void setCellPanelTemplate(CellPanelTemplate cellPanelTemplate) {
        this.cellPanelTemplate = cellPanelTemplate;
    }

    class ModulesModel extends AbstractTableModel {

        public String[] colNames = {"A", "B", "C", "D", "E"};

        public Class[] colTypes = {
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class
        };
        Object[][] data;
        final Integer COLUMN_COUNT = 5;

        public ModulesModel(Object[][] data) {
            this.data = data;
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
            return colNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (data != null
                    && data[rowIndex][columnIndex] != null) {
                return data[rowIndex][columnIndex];
            } else {
                return String.class;
            }
        }
    }
}
