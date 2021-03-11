package com.adinfi.formateador.view;

import com.adinfi.formateador.util.Utilerias;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author USUARIO
 */
public class QuickViewPanel_respaldo extends javax.swing.JPanel {

    /**
     * Creates new form QuickViewPanel
     */
    public QuickViewPanel_respaldo() {
        initComponents();

        List<Document> list = new ArrayList<>();
        list.add(new Document("01/01/2014", "Tipo documento", "Tema #1", "Titulo #1"));
        list.add(new Document("02/01/2014", "Tipo documento", "Tema #2", "Titulo #2"));
        list.add(new Document("03/01/2014", "Tipo documento", "Tema #3", "Titulo #3"));
        list.add(new Document("04/01/2014", "Tipo documento", "Tema #4", "Titulo #4"));
        list.add(new Document("05/01/2014", "Tipo documento", "Tema #5", "Titulo #5"));

        RecentFiles rf = new RecentFiles(list);

        jTable1.setModel(rf);
        jTable1.setRowHeight(35);

        TableColumn tc1 = jTable1.getColumn("PDF");
        TableColumn tc2 = jTable1.getColumn("HTML");
        TableColumn tc3 = jTable1.getColumn("Editar");
      
         TableCellRenderer buttonRenderer = new JTableButtonRenderer();
         tc1.setCellRenderer(buttonRenderer);
         tc2.setCellRenderer(buttonRenderer);
         tc3.setCellRenderer(buttonRenderer);
         
         //tc.setCellRenderer(new ButtonRenderer());
        //jTable1.getColumn("PDF").setCellEditor(
//                new ButtonEditor(new JCheckBox()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tab2 = new javax.swing.JPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(QuickViewPanel_respaldo.class);
        tabbedPane.setFont(resourceMap.getFont("tabbedPane.font")); // NOI18N
        tabbedPane.setName("tabbedPane"); // NOI18N

        tab1.setFont(resourceMap.getFont("tab1.font")); // NOI18N
        tab1.setName("tab1"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(resourceMap.getString("tab1.TabConstraints.tabTitle"), tab1); // NOI18N

        tab2.setName("tab2"); // NOI18N
        tab2.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab(resourceMap.getString("tab2.TabConstraints.tabTitle"), tab2); // NOI18N

        add(tabbedPane, java.awt.BorderLayout.CENTER);
        tabbedPane.getAccessibleContext().setAccessibleName(resourceMap.getString("tabbedPane.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    class RecentFiles extends AbstractTableModel {

        private static final String PDF = "PDF";
        private static final String HTML = "HTML";
        private static final String EDITAR = "Editar";
        private List<Document> list;
        final Integer COLUMN_COUNT = 8;

        public RecentFiles(List<Document> list) {
            this.list = list;
        }

        @Override
        public int getRowCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public String getColumnName(int column) {
            String name = "??";
            switch (column) {
                case 0:
                    name = "";
                    break;
                case 1:
                    name = "Fecha";
                    break;
                case 2:
                    name = "Tipo documento";
                    break;
                case 3:
                    name = "Tema";
                    break;
                case 4:
                    name = "Titulo";
                    break;
                case 5:
                    name = PDF;
                    break;
                case 6:
                    name = HTML;
                    break;
                case 7:
                    name = EDITAR;
                    break;
            }
            return name;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class type;
            switch (columnIndex) {
                case 0:
                    type = ImageIcon.class;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    type = String.class;
                    break;
                case 5:
                case 6:
                case 7:
                    type = JButton.class;//new ButtonEditor(new JCheckBox());
                    break;
                default:
                    type = String.class;
            }
            return type;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Document m = list.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    value = new ImageIcon(Utilerias.getImage(rowIndex % 2 == 0 ? Utilerias.ICONS.SMALL_NEW_DOCUMENT : Utilerias.ICONS.SMALL_DOCUMENT_ATTACHED));
                    break;
                case 1:
                    value = m.getFecha();
                    break;
                case 2:
                    value = m.getTipoDocumento();
                    break;
                case 3:
                    value = m.getTema();
                    break;
                case 4:
                    value = m.getTitulo();
                    break;
                case 5:
                    /*Adding button and creating click listener*/
                    final JButton button = new JButton(PDF);
                        button.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(button), 
                                        "Button clicked for row "+PDF);
                            }
                        });
                        return button;
                case 6:
                    value = new JButton(HTML);
                    break;
                case 7:
                    value = new JButton(EDITAR);
                    break;
                default:
                    value = Object.class;
            }
            return value;
        }

    }

    class Document {

        private String fecha;
        private String tipoDocumento;
        private String tema;
        private String titulo;

        public Document() {

        }

        public Document(String fecha, String tipoDocumento, String tema, String titulo) {
            this.fecha = fecha;
            this.tipoDocumento = tipoDocumento;
            this.tema = tema;
            this.titulo = titulo;
        }

        /**
         * @return the fecha
         */
        public String getFecha() {
            return fecha;
        }

        /**
         * @param fecha the fecha to set
         */
        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        /**
         * @return the tipoDocumento
         */
        public String getTipoDocumento() {
            return tipoDocumento;
        }

        /**
         * @param tipoDocumento the tipoDocumento to set
         */
        public void setTipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }

        /**
         * @return the tema
         */
        public String getTema() {
            return tema;
        }

        /**
         * @param tema the tema to set
         */
        public void setTema(String tema) {
            this.tema = tema;
        }

        /**
         * @return the titulo
         */
        public String getTitulo() {
            return titulo;
        }

        /**
         * @param titulo the titulo to set
         */
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

    }

    /**
     * @version 1.0 11/09/98
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    private class JTableButtonRenderer implements TableCellRenderer {        
        @Override 
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            
            return button;  
        }
    }

    /**
     * @version 1.0 11/09/98
     */
    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

}