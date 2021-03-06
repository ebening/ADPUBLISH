/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.SemanarioDocsBO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.editor.HTMLDocument;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class SemanarioDialog extends javax.swing.JDialog {

    private MainView main;
    private DefaultListModel<ModelBean> model;
    private Logger LOG = Logger.getLogger(SemanarioDialog.class.getName());

    /**
     * Creates new form SemanarioDialog
     */
    public SemanarioDialog(java.awt.Frame parent, boolean modal, MainView main) {
        super(parent, modal);
        this.model = new DefaultListModel<>();
        this.main = main;
        initComponents();
        setLocationRelativeTo(parent);
        Utilerias.addEscapeListener(this);
        setTitle(GlobalDefines.TITULO_APP);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelBusqueda = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        panelNuevo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        eastPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        upLevel = new javax.swing.JButton();
        downLevel = new javax.swing.JButton();
        removeDocument = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelBusqueda.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        panelBusqueda.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Nombre del semanario");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("PDF");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("HTML");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBusqueda.add(jPanel2, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Busqueda semanario", panelBusqueda);

        panelNuevo.setLayout(new java.awt.BorderLayout());

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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);

        panelNuevo.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Nombre del documento");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Agregar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNuevo.add(jPanel1, java.awt.BorderLayout.NORTH);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Documentos de semanario");

        upLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/up_.png"))); // NOI18N
        upLevel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        upLevel.setIconTextGap(0);
        upLevel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        upLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upLevelActionPerformed(evt);
            }
        });

        downLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/down_.png"))); // NOI18N
        downLevel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        downLevel.setIconTextGap(0);
        downLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downLevelActionPerformed(evt);
            }
        });

        removeDocument.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        removeDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDocumentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eastPanelLayout = new javax.swing.GroupLayout(eastPanel);
        eastPanel.setLayout(eastPanelLayout);
        eastPanelLayout.setHorizontalGroup(
            eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eastPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addGroup(eastPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(upLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(downLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(removeDocument, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))))
                .addContainerGap())
        );
        eastPanelLayout.setVerticalGroup(
            eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eastPanelLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(eastPanelLayout.createSequentialGroup()
                        .addComponent(upLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeDocument, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelNuevo.add(eastPanel, java.awt.BorderLayout.EAST);

        jTabbedPane1.addTab("Nuevo semanario", panelNuevo);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            buscarDocumentos();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        buscarDocumentos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //guardar documento

        if (model.size() == 0) {
            return;
        }

        //TODO modificar para nueva version cambiar null
        String name = JOptionPane.showInputDialog(null, "??Nombre de semanario?", GlobalDefines.TITULO_APP, JOptionPane.QUESTION_MESSAGE);
        if (name != null && name.isEmpty() == false) {
            //Guardar semanario
            guardarSemanario(name);
        } else {
            //Utilerias.showMessage(main, "Especifique un nombre de semanario", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        int row = jTable1.getSelectedRow();
        int idDocument = jTable1.getModel().getValueAt(row, 0) == null ? -1
                : Integer.parseInt(String.valueOf(jTable1.getModel().getValueAt(row, 0)));
        String nombre = jTable1.getModel().getValueAt(row, 0) == null ? ""
                : String.valueOf(jTable1.getModel().getValueAt(row, 1));

        if (model.isEmpty() == true) {
            model.addElement(new ModelBean(idDocument, nombre));
            jList1.setModel(model);
        } else {
            Enumeration<ModelBean> elements = model.elements();
            ModelBean o = elements.nextElement();
            boolean existe = false;
            while (o != null) {
                if (o.getId() == idDocument) {
                    existe = true;
                    break;
                }
                o = (elements.hasMoreElements() == true) ? elements.nextElement() : null;
            }
            if (existe == false) {
                model.addElement(new ModelBean(idDocument, nombre));
                jList1.setModel(model);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void removeDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDocumentActionPerformed
        if (model.isEmpty() == true) {
            return;
        }
        ModelBean b = (ModelBean) jList1.getSelectedValue();
        if (b == null) {
            return;
        }
        int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), String.format("??Remover %s de la lista de semanario?", b.getName()), GlobalDefines.TITULO_APP, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            model.removeElement(b);
        }
    }//GEN-LAST:event_removeDocumentActionPerformed

    private void upLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upLevelActionPerformed
        if (model.isEmpty() == true) {
            return;
        }
        int index = jList1.getSelectedIndex();
        if (index > 0) {
            ModelBean m1 = model.getElementAt(index);
            ModelBean m2 = model.getElementAt(index - 1);
            model.setElementAt(m1, index - 1);
            model.setElementAt(m2, index);
            jList1.setSelectedIndex(index - 1);
        }
    }//GEN-LAST:event_upLevelActionPerformed

    private void downLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downLevelActionPerformed
        if (model.isEmpty() == true) {
            return;
        }
        int index = jList1.getSelectedIndex();
        if (index < model.size() - 1) {
            ModelBean m1 = model.getElementAt(index);
            ModelBean m2 = model.getElementAt(index + 1);
            model.setElementAt(m1, index + 1);
            model.setElementAt(m2, index);
            jList1.setSelectedIndex(index + 1);
        }
    }//GEN-LAST:event_downLevelActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            buscarSemanario();
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        buscarSemanario();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTextField2.setText("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        enableGuardar();
    }//GEN-LAST:event_jList1ValueChanged

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
//        JTable table = (JTable) evt.getSource();
//        Point p = evt.getPoint();
//        int row = table.rowAtPoint(p);
//        if (evt.getClickCount() == 2) {
//            int idSemanario = jTable2.getModel().getValueAt(row, 0) == null ? -1
//                    : Integer.parseInt(String.valueOf(jTable2.getModel().getValueAt(row, 0)));
//            String nombre = jTable2.getModel().getValueAt(row, 0) == null ? ""
//                    : String.valueOf(jTable2.getModel().getValueAt(row, 1));
//
//            int result = JOptionPane.showConfirmDialog(this, "??Visualizar semanario PDF?\n" + nombre, GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                try {
//                    main.openSemanario(idSemanario, nombre);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                } finally {
//                    this.setCursor(Cursor.getDefaultCursor());
//                    dispose();
//                }
//            }
//        }

    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //PDF
        int row = jTable2.getSelectedRow();
        if (row < 0) return;
        int idSemanario = jTable2.getModel().getValueAt(row, 0) == null ? -1
                : Integer.parseInt(String.valueOf(jTable2.getModel().getValueAt(row, 0)));
        String nombre = jTable2.getModel().getValueAt(row, 0) == null ? ""
                : String.valueOf(jTable2.getModel().getValueAt(row, 1));

        int result = JOptionPane.showConfirmDialog(this, "??Visualizar semanario PDF?\n" + nombre, GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                //TODO modificar para nueva version
                //main.openSemanario(idSemanario, nombre);
                

                //dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.setCursor(Cursor.getDefaultCursor());
                
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //HTML
        int row = jTable2.getSelectedRow();
        if (row < 0) return;
        int idSemanario = jTable2.getModel().getValueAt(row, 0) == null ? -1
                : Integer.parseInt(String.valueOf(jTable2.getModel().getValueAt(row, 0)));
        String nombre = jTable2.getModel().getValueAt(row, 0) == null ? ""
                : String.valueOf(jTable2.getModel().getValueAt(row, 1));

        int result = JOptionPane.showConfirmDialog(this, "??Visualizar semanario HTML?\n" + nombre, GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                //main.openSemanario(idSemanario, nombre);
                HTMLDocument htmlDoc=new HTMLDocument();
                String fileName = htmlDoc.createHTMLSemanario(idSemanario, null, null, null);
                //Set your page url in this string. For eg, I m using URL for Google Search engine
                String url = "file:///" + fileName;
                //  String encodedurl = URLEncoder.encode(url.toString()); 
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.setCursor(Cursor.getDefaultCursor());
                //dispose();
            }
        }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void enableGuardar() {
        jButton2.setEnabled(model.isEmpty() == true ? false : true);
    }

    private void buscarSemanario() {
        try {
            String text = jTextField2.getText();
            if (text == null || text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto a buscar", GlobalDefines.TITULO_APP, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DocumentDAO dao = new DocumentDAO();
            List<SemanarioDocsBO> list = dao.getSemanariosByName(text);

            CustomSearchModelSemanario customModel = new CustomSearchModelSemanario(list);
            jTable2.setModel(customModel);
            jTable2.removeColumn(jTable2.getColumnModel().getColumn(0));
            
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(jTable2);
            for (int i = 0; i < jTable2.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(jTable2, i, 4);
            }
        } catch (HeadlessException ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    private void buscarDocumentos() {
        try {
            String text = jTextField1.getText();
            if (text == null || text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto a buscar", GlobalDefines.TITULO_APP, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DocumentDAO dao = new DocumentDAO();
            List<DocumentBO> list = dao.getDocumentsByName(text,false);

            CustomSearchModelDocumento customModel = new CustomSearchModelDocumento(list);
            jTable1.setModel(customModel);
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
            
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(jTable1);
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(jTable1, i, 4);
            }
        } catch (HeadlessException ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    private void guardarSemanario(String name) {
        try {
            DocumentDAO dao = new DocumentDAO();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                list.add(model.get(i).getId());
            }
            Boolean b = dao.insertarSemanario(name, list);
            if (b == true) {
                
                //TODO modificar para nueva version
                //Utilerias.showMessage(main, "Semanario guardado satisfactoriamente", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downLevel;
    private javax.swing.JPanel eastPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel panelBusqueda;
    private javax.swing.JPanel panelNuevo;
    private javax.swing.JButton removeDocument;
    private javax.swing.JButton upLevel;
    // End of variables declaration//GEN-END:variables
}

class ModelBean {

    private int id;
    private String name;

    public ModelBean() {

    }

    public ModelBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
