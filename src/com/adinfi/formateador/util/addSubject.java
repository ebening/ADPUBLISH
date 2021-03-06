/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.view.administration.tablemodel.SubjectModel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Josu? Sanchez
 */
public class addSubject extends javax.swing.JDialog {

    private Boolean subjectFirst = false;
    private Boolean languagesFirst = false;
    private Boolean industryFirst = false;
    private Boolean marketFirst = false;
    private Boolean imageLoadFirst = false;
    private Boolean outgoingEmailFirst = false;
    private String pathFile = "";

    /**
     * Creates new form Catalogo
     */
    // Etiquetas que cambian entre si la etiqueta
    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    // --------------------------------------------

    // Etiquetas que cambian entre si la etiqueta
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    // --------------------------------------------

    private final String DELETE_MESSAGE = "?Desea eliminar?";

    public addSubject(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        subject = new javax.swing.JPanel();
        tableSubjectPanel = new javax.swing.JPanel();
        labelSubject = new javax.swing.JLabel();
        inputSubject = new javax.swing.JTextField();
        ScrollTableSubject = new javax.swing.JScrollPane();
        subjectTable = new javax.swing.JTable();
        searchSubject = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnNewSubject = new javax.swing.JButton();
        btnSaveSubject = new javax.swing.JButton();
        btnDeleteSubject = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        subject.setName("subject"); // NOI18N
        subject.setPreferredSize(new java.awt.Dimension(648, 670));
        subject.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                subjectComponentShown(evt);
            }
        });

        tableSubjectPanel.setName("tableSubjectPanel"); // NOI18N

        labelSubject.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelSubject.setText("B?squeda");
        labelSubject.setName("labelSubject"); // NOI18N

        inputSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputSubject.setName("inputSubject"); // NOI18N
        inputSubject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputSubjectKeyReleased(evt);
            }
        });

        ScrollTableSubject.setName("ScrollTableSubject"); // NOI18N

        subjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        subjectTable.setName("subjectTable"); // NOI18N
        subjectTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                subjectTableComponentShown(evt);
            }
        });
        ScrollTableSubject.setViewportView(subjectTable);

        searchSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        searchSubject.setName("searchSubject"); // NOI18N
        searchSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSubjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableSubjectPanelLayout = new javax.swing.GroupLayout(tableSubjectPanel);
        tableSubjectPanel.setLayout(tableSubjectPanelLayout);
        tableSubjectPanelLayout.setHorizontalGroup(
            tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                        .addComponent(labelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(ScrollTableSubject, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        tableSubjectPanelLayout.setVerticalGroup(
            tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchSubject, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelSubject)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollTableSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("*Campo Requerido");
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        btnNewSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewSubject.setText("Nuevo");
        btnNewSubject.setName("btnNewSubject"); // NOI18N
        btnNewSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnNewSubject);

        btnSaveSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveSubject.setText("Guardar");
        btnSaveSubject.setEnabled(false);
        btnSaveSubject.setName("btnSaveSubject"); // NOI18N
        btnSaveSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnSaveSubject);

        btnDeleteSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteSubject.setText("Eliminar");
        btnDeleteSubject.setEnabled(false);
        btnDeleteSubject.setName("btnDeleteSubject"); // NOI18N
        btnDeleteSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnDeleteSubject);

        javax.swing.GroupLayout subjectLayout = new javax.swing.GroupLayout(subject);
        subject.setLayout(subjectLayout);
        subjectLayout.setHorizontalGroup(
            subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectLayout.createSequentialGroup()
                .addGroup(subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(subjectLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(subjectLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE))))
                .addContainerGap())
        );
        subjectLayout.setVerticalGroup(
            subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableSubjectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        addSubjectModel();
        findSubject(null);
          try {
            IndustryDAO dAO = new IndustryDAO();
            listIndustry = dAO.get("");
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }//GEN-LAST:event_formComponentShown

    private void btnDeleteSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSubjectActionPerformed
        deleteSubject();
    }//GEN-LAST:event_btnDeleteSubjectActionPerformed

    private void btnSaveSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSubjectActionPerformed
        insertUpdateSubject();
    }//GEN-LAST:event_btnSaveSubjectActionPerformed

    private void btnNewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSubjectActionPerformed
        if (btnNewSubject.getText().equals(CANCEL_LABEL)) {
            btnNewSubject.setText(NEW_LABEL);
            btnSaveSubject.setEnabled(false);
            cancelNewSubject();
        } else if (btnNewSubject.getText().equals(NEW_LABEL)) {
            btnNewSubject.setText(CANCEL_LABEL);
            btnSaveSubject.setEnabled(true);
            newSubject();

        }
    }//GEN-LAST:event_btnNewSubjectActionPerformed

    private void searchSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSubjectActionPerformed
        findSubject(inputSubject.getText());
    }//GEN-LAST:event_searchSubjectActionPerformed

    private void subjectTableComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_subjectTableComponentShown

    }//GEN-LAST:event_subjectTableComponentShown

    private void inputSubjectKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputSubjectKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findSubject(inputSubject.getText());
        }
    }//GEN-LAST:event_inputSubjectKeyReleased

    private void subjectComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_subjectComponentShown
//        if (!subjectFirst) {
//            findSubject(inputSubject.getText());
//            subjectFirst = true;
//        }
        
        try {
            IndustryDAO dAO = new IndustryDAO();
            listIndustry = dAO.get("");
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        
        searchSubject.doClick();
    }//GEN-LAST:event_subjectComponentShown

    /* Subject */
    private void addSubjectModel() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        subjectTable.setModel(model);

        try {
            subjectTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    //int row = languageTable.rowAtPoint(e.getPoint());
                    int column = subjectTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        SubjectModel model = (SubjectModel) subjectTable.getModel();
                        List<Integer> selectedRows = new ArrayList<>();
                        int length = model.getData() != null ? model.getData().length : 0;
                        for (int i = 0; i < length; i++) {
                            if (model.isCheckedRow(i) == true) {
                                selectedRows.add(model.getUniqueRowIdentify(i));
                            }
                        }

                        if (selectedRows.isEmpty() == true) {
                            /*Cuando no se han seleccionado elementos en el grid*/
                            btnSaveSubject.setText(SAVE_LABEL);
                            btnSaveSubject.setEnabled(false);
                            btnNewSubject.setText(NEW_LABEL);
                            btnNewSubject.setEnabled(true);
                            btnDeleteSubject.setEnabled(false);
                        } else if (selectedRows.size() == 1) {
                            btnNewSubject.setText(CANCEL_LABEL);
                            btnNewSubject.setEnabled(true);
                            btnSaveSubject.setEnabled(true);
                            btnSaveSubject.setText(EDIT_LABEL);
                            btnDeleteSubject.setEnabled(true);
                        } else if (selectedRows.size() > 1) {
                            btnNewSubject.setText(CANCEL_LABEL);
                            btnNewSubject.setEnabled(true);
                            btnSaveSubject.setEnabled(false);
                            btnSaveSubject.setText(EDIT_LABEL);
                            btnDeleteSubject.setEnabled(true);
                        }

                    }
                }
            });

            fitColumnsSubject(subjectTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setSubjectModel(List<SubjectBO> list) {
        SubjectModel subjectModel = new SubjectModel(list);
        subjectTable.setModel(subjectModel);

        try {
            /* Agregar combo*/
            TableColumn column = subjectTable.getColumnModel().getColumn(StatementConstant.SC_3.get());
            column.setCellEditor(new DefaultCellEditor(generateIndustryBox()));
            subjectTable.setColumnSelectionAllowed(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fitColumnsSubject(subjectTable);
    }

    private void findSubject(String pattern) {
        try {
            SubjectDAO dAO = new SubjectDAO();
            List<SubjectBO> list = dAO.get(pattern);
            setSubjectModel(list);
            btnSaveSubject.setText(SAVE_LABEL);
            btnSaveSubject.setEnabled(false);
            btnNewSubject.setText(NEW_LABEL);
            btnNewSubject.setEnabled(true);
            btnDeleteSubject.setEnabled(false);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newSubject() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        if (model == null) {
            return;
        }

        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            subjectTable.editCellAt(0, 1);
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            subjectTable.editCellAt(0, 1);

        }
    }

    private void cancelNewSubject() {
        try {
            SubjectModel model = (SubjectModel) subjectTable.getModel();
            if (model.getData().length == 0) {
                return;
            }
            Object[][] data2 = new Object[model.getData().length - 1][];
            if (data2.length == 0) {
                model.setData(data2);
                subjectTable.setModel(model);
                fitColumnsSubject(subjectTable);
            } else {
                System.arraycopy(model.getData(), 1, data2, 0, data2.length);
                model = new SubjectModel();
                model.setData(data2);

                subjectTable.setModel(model);
                fitColumnsSubject(subjectTable);
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void insertUpdateSubject() {
        if (btnSaveSubject.getText().equals(SAVE_LABEL)) {
            /* 
             * Validar que est? seleccionado un registro del grid para guardar/actualizar
             */
            btnSaveSubject.setFocusable(true);
            subjectTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            SubjectModel model = (SubjectModel) subjectTable.getModel();
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;
                SubjectDAO dAO = new SubjectDAO();
                SubjectBO bo = new SubjectBO();
                bo.setIdSubject(model.getUniqueRowIdentify(index));
                if (subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    required = false;
                } else {
                    bo.setName((String) subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                }
                if (subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) == null || subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()).toString().isEmpty()) {
                    required = false;
                } else {
                    if (subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) instanceof IndustryBO) {
                        IndustryBO industryBO = (IndustryBO) subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get());
                        bo.setIndustry(industryBO.getIdIndustry());
                    } else {
                        String str = (String)subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get());
                        //buscarPorNombre
                        int id = getIdFromComboBox(str);
                        bo.setIndustry(id);
                    }
                }
                
                bo.setIssuing((boolean) subjectTable.getModel().getValueAt(index, StatementConstant.SC_4.get()));

                if (required == true) {
                    int i = dAO.insertUpdate(bo);
                    if (i > 0) {
                        findSubject(inputSubject.getText());
                    }
                    if (i == -1) {
                        findSubject(inputSubject.getText());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Complete campos requeridos*");
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }

        } else if (btnSaveSubject.getText().equals(EDIT_LABEL)) {

            //obtener la fila del elemento seleccionado 
            int row = subjectTable.getSelectedRow();
            subjectTable.editCellAt(row, 1);
            btnSaveSubject.setText(SAVE_LABEL);
        }
    }

    private void deleteSubject() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    SubjectDAO dao = new SubjectDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurri? un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findSubject(inputSubject.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }

    }

    private void fitColumnsSubject(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                if (i == 0) {
                    Utilerias.adjustColumnSizes(table, i, 1);
                } else {
                    Utilerias.adjustColumnSizes(table, i, 4);
                }
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
        } catch (Exception e) {

        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollTableSubject;
    private javax.swing.JButton btnDeleteSubject;
    private javax.swing.JButton btnNewSubject;
    private javax.swing.JButton btnSaveSubject;
    private javax.swing.JTextField inputSubject;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel labelSubject;
    private javax.swing.JButton searchSubject;
    private javax.swing.JPanel subject;
    private javax.swing.JTable subjectTable;
    private javax.swing.JPanel tableSubjectPanel;
    // End of variables declaration//GEN-END:variables
            JFileChooser fc = new JFileChooser();

    private JComboBox generateIndustryBox() {
        JComboBox bx = new JComboBox();
//        Connection con = CPool.getConnection();
        try {
            IndustryDAO dAO = new IndustryDAO();
            List<IndustryBO> list = dAO.get("");
            for (IndustryBO o : list) {
                bx.addItem(o);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        return bx;

    }
    
    private List<IndustryBO> listIndustry;
    
    public int getIdFromComboBox(String text) {
        int s = 0;
        for (IndustryBO o : listIndustry) {
            int j = 0;
            if (o.getName().equals(text)) {
                s = o.getIdIndustry();
                break;
            }
        }
        return s;
    }
}
