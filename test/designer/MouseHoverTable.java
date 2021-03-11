/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package designer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MouseHoverTable extends JFrame {

    int itsRow = 0;
    int itsColumn = 0;
    JTable itsTable;

    MouseHoverTable(String framename) {
        super(framename);

        itsTable = new JTable(5, 3);
        //make the table transparent
        itsTable.setOpaque(false);
        itsTable.setDefaultRenderer(Object.class, new AttributiveCellRenderer());
        MyMouseAdapter aMouseAda = new MyMouseAdapter();
        itsTable.addMouseMotionListener(aMouseAda);
        JScrollPane jsp = new JScrollPane(itsTable);
        this.getContentPane().add(itsTable);//jsp);
    }

    public static void main(String[] args) {
        JFrame frame = new MouseHoverTable("Table Example");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Window w = e.getWindow();
                w.setVisible(false);
                w.dispose();
                System.exit(0);
            }
        });
        frame.pack();
        frame.show();
    }

    public class MyMouseAdapter extends MouseMotionAdapter //extends MouseAdapter
    {

        public void mouseMoved(MouseEvent e) {
            JTable aTable = (JTable) e.getSource();
            itsRow = aTable.rowAtPoint(e.getPoint());
            itsColumn = aTable.columnAtPoint(e.getPoint());
            aTable.repaint();
        }
    }

    public class AttributiveCellRenderer extends JLabel implements
            TableCellRenderer {

        public AttributiveCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row == itsRow && column == itsColumn) {
                this.setBackground(Color.red);
                this.setForeground(Color.blue);
            } else {
                this.setBackground(Color.cyan);
                this.setForeground(Color.darkGray);
            }
            String aStr = "Row " + row + "Column" + column;
            this.setText(aStr);
            return this;
        }
    }
}