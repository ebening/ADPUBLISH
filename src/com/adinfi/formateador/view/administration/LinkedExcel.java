package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.CatalogExcelBOCat;
import com.adinfi.formateador.bos.LinkedExcelBO;
import com.adinfi.formateador.dao.ExcelDAO;
import com.adinfi.formateador.dao.ExcelDAOCat;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.LinkedExcelModel;
import com.google.common.collect.ListMultimap;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class LinkedExcel extends javax.swing.JDialog {

    /**
     * Creates new form CatalogoExcelDialog
     */
    private String pathFile;
    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    private final String DELETE_MESSAGE = "¿Desea eliminar?";
    private boolean isUpdate = false;
    private int idExcel = 0;

    public LinkedExcel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Utilerias.addEscapeListener(this);
        loadFileFilter();
        loadJTableProperties();
    }
    
    private void loadJTableProperties() {
        linkedExcelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /* Metodo para llenar el combo Status */
    private void initComboBoxStatus() {

        cboCatExcel.removeAllItems();
        CatalogExcelBOCat bo = new CatalogExcelBOCat();
        bo.setCategory("Seleccione");
        bo.setId_category(-1);
        cboCatExcel.addItem(bo);
        ExcelDAOCat dao = new ExcelDAOCat();
        List<CatalogExcelBOCat> list = dao.getCatalogExcel(null, 0);
        if (list == null) {
            return;
        }
        for (CatalogExcelBOCat bo_ : list) {
            cboCatExcel.addItem(bo_);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        northPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtCeldaInicial = new javax.swing.JTextField();
        lblCeldaFinal = new javax.swing.JLabel();
        lblHoja = new javax.swing.JLabel();
        lblCeldaInicial = new javax.swing.JLabel();
        txtCeldaFinal = new javax.swing.JTextField();
        txtHoja = new javax.swing.JTextField();
        rbCeldas = new javax.swing.JRadioButton();
        rbRango = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        cboCatExcel = new javax.swing.JComboBox();
        inputPathExcel = new javax.swing.JTextField();
        openFileExcel = new javax.swing.JButton();
        inputNameExcel = new javax.swing.JTextField();
        addCategory = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        centerPanel = new javax.swing.JPanel();
        centerScroll = new javax.swing.JScrollPane();
        linkedExcelTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        southPanel = new javax.swing.JPanel();
        btnNewLinkedExcel = new javax.swing.JButton();
        btnSaveLinkedExcel = new javax.swing.JButton();
        btnDeleteLinkedExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Excel vinculado");
        setMinimumSize(new java.awt.Dimension(400, 400));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        txtCeldaInicial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCeldaInicial.setEnabled(false);

        lblCeldaFinal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCeldaFinal.setText("Celda final*");

        lblHoja.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHoja.setText("Hoja*");

        lblCeldaInicial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCeldaInicial.setText("Celda inicial*");

        txtCeldaFinal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCeldaFinal.setEnabled(false);

        txtHoja.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtHoja.setEnabled(false);

        buttonGroup1.add(rbCeldas);
        rbCeldas.setSelected(true);
        rbCeldas.setText("Celdas");
        rbCeldas.setEnabled(false);
        rbCeldas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbCeldasStateChanged(evt);
            }
        });
        rbCeldas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbCeldasMouseClicked(evt);
            }
        });

        buttonGroup1.add(rbRango);
        rbRango.setText("Rango");
        rbRango.setEnabled(false);
        rbRango.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbRangoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(rbCeldas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbRango))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCeldaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCeldaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCeldaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(txtCeldaInicial))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblHoja, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHoja, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(184, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbCeldas)
                    .addComponent(rbRango))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCeldaInicial)
                        .addComponent(lblHoja)
                        .addComponent(txtHoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCeldaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCeldaFinal)
                    .addComponent(txtCeldaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cboCatExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboCatExcel.setEnabled(false);
        cboCatExcel.setMaximumSize(new java.awt.Dimension(322, 21));
        cboCatExcel.setMinimumSize(new java.awt.Dimension(322, 21));
        cboCatExcel.setPreferredSize(new java.awt.Dimension(322, 21));

        inputPathExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputPathExcel.setEnabled(false);
        inputPathExcel.setMaximumSize(new java.awt.Dimension(322, 21));
        inputPathExcel.setMinimumSize(new java.awt.Dimension(322, 21));
        inputPathExcel.setPreferredSize(new java.awt.Dimension(322, 21));

        openFileExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        openFileExcel.setText("Abrir");
        openFileExcel.setEnabled(false);
        openFileExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileExcelActionPerformed(evt);
            }
        });

        inputNameExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputNameExcel.setEnabled(false);
        inputNameExcel.setMaximumSize(new java.awt.Dimension(322, 21));
        inputNameExcel.setMinimumSize(new java.awt.Dimension(322, 21));
        inputNameExcel.setPreferredSize(new java.awt.Dimension(322, 21));

        addCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_add_.png"))); // NOI18N
        addCategory.setEnabled(false);
        addCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Categoría*");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Nombre*");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Ruta de Excel*");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cboCatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(inputNameExcel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                        .addComponent(inputPathExcel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openFileExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboCatExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(addCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(openFileExcel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(inputNameExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputPathExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        northPanel.add(jPanel1);

        jPanel4.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        northPanel.add(jPanel4);

        getContentPane().add(northPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setLayout(new java.awt.BorderLayout());

        linkedExcelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "Categoría*", "Nombre*", "Ruta de Excel*", "Hoja"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        centerScroll.setViewportView(linkedExcelTable);

        centerPanel.add(centerScroll, java.awt.BorderLayout.CENTER);

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel7.setText("*Campo Requerido");
        centerPanel.add(jLabel7, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        btnNewLinkedExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewLinkedExcel.setText("Nuevo");
        btnNewLinkedExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewLinkedExcelActionPerformed(evt);
            }
        });
        southPanel.add(btnNewLinkedExcel);

        btnSaveLinkedExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveLinkedExcel.setText("Guardar");
        btnSaveLinkedExcel.setEnabled(false);
        btnSaveLinkedExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveLinkedExcelActionPerformed(evt);
            }
        });
        southPanel.add(btnSaveLinkedExcel);

        btnDeleteLinkedExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteLinkedExcel.setText("Eliminar");
        btnDeleteLinkedExcel.setEnabled(false);
        btnDeleteLinkedExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLinkedExcelActionPerformed(evt);
            }
        });
        southPanel.add(btnDeleteLinkedExcel);

        getContentPane().add(southPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnNewLinkedExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewLinkedExcelActionPerformed
        if (btnNewLinkedExcel.getText().equals("Nuevo")) {
            enablefields(true);
            limpiar();
            addLinkedExcelModel();
            getLinkedExcel();
            btnSaveLinkedExcel.setEnabled(true);
            btnNewLinkedExcel.setText("Cancelar");

        } else if (btnNewLinkedExcel.getText().equals("Cancelar")) {
            limpiar();
            enablefields(false);
            btnSaveLinkedExcel.setEnabled(false);
            btnSaveLinkedExcel.setText(SAVE_LABEL);
            btnDeleteLinkedExcel.setEnabled(false);
            btnNewLinkedExcel.setText(NEW_LABEL);
            addLinkedExcelModel();
            getLinkedExcel();

        }

//        if (inputPathExcel.getText().isEmpty() == false
//                || inputPathExcel.getText().isEmpty() == false
//                || txtHoja.getText().isEmpty() == false
//                || txtCeldaInicial.getText().isEmpty() == false
//                || txtCeldaFinal.getText().isEmpty() == false) {
//            int response = JOptionPane.showConfirmDialog(this, "¿Limpiar información?", GlobalDefines.TITULO_APP, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (response == JOptionPane.YES_OPTION) {
//                limpiar();
//            }
//        }

    }//GEN-LAST:event_btnNewLinkedExcelActionPerformed

    private void btnSaveLinkedExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveLinkedExcelActionPerformed
        switch (btnSaveLinkedExcel.getText()) {
            case SAVE_LABEL:
                saveLinkedExcel();
                break;
            case EDIT_LABEL:
                enablefields(true);
                btnSaveLinkedExcel.setText(SAVE_LABEL);
                isUpdate = true;
                break;
        }
    }//GEN-LAST:event_btnSaveLinkedExcelActionPerformed

    private void addCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryActionPerformed
        Categories cat = new Categories(this, true);
        cat.setLocationRelativeTo(this);
        cat.setVisible(true);

        cat.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                initComboBoxStatus();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                initComboBoxStatus();
            }
        });

    }//GEN-LAST:event_addCategoryActionPerformed

    private void rbCeldasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbCeldasStateChanged
        if (rbCeldas.isSelected() == true) {
            //mostra celdas
            lblCeldaInicial.setText("Celda inicial");
            lblCeldaInicial.setVisible(true);
            lblCeldaFinal.setVisible(true);
            lblHoja.setVisible(true);
            txtCeldaFinal.setVisible(true);
            txtHoja.setVisible(true);
        } else {
            //mostra rango
            lblCeldaInicial.setText("Nombre");
            lblCeldaInicial.setVisible(true);
            lblCeldaFinal.setVisible(false);
            lblHoja.setVisible(false);
            txtCeldaInicial.setPreferredSize(new Dimension(127, 21));
            txtCeldaFinal.setVisible(false);
            txtHoja.setVisible(false);
        }
    }//GEN-LAST:event_rbCeldasStateChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        initComboBoxStatus();
        addLinkedExcelModel();
        getLinkedExcel();
    }//GEN-LAST:event_formComponentShown

    private void openFileExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileExcelActionPerformed
        openExcelFile();
    }//GEN-LAST:event_openFileExcelActionPerformed

    private void btnDeleteLinkedExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLinkedExcelActionPerformed
        deleteLinkedExcel();
    }//GEN-LAST:event_btnDeleteLinkedExcelActionPerformed

    private void rbCeldasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbCeldasMouseClicked
        if (rbCeldas.isSelected()) {
            txtCeldaInicial.setText("");
            txtCeldaFinal.setText("");
            txtHoja.setText("");
        } else {
            txtCeldaInicial.setText("");
            txtCeldaFinal.setText("");
            txtHoja.setText("");
        }
    }//GEN-LAST:event_rbCeldasMouseClicked

    private void rbRangoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbRangoMouseClicked
        if (rbRango.isSelected()) {
            txtCeldaInicial.setText("");
        } else {
            txtCeldaInicial.setText("");
            txtCeldaFinal.setText("");
            txtHoja.setText("");
        }
    }//GEN-LAST:event_rbRangoMouseClicked

    private void limpiar() {
        inputPathExcel.setText(null);
        inputPathExcel.setText(null);
        txtHoja.setText(null);
        txtCeldaInicial.setText(null);
        txtCeldaFinal.setText(null);
        inputNameExcel.setText(null);
        cboCatExcel.setSelectedIndex(0);
    }

    private void openExcelFile() {
        int returnVal = myFileChooser.showOpenDialog(null);
        File file = myFileChooser.getSelectedFile();

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathFile = file.getAbsolutePath();
            String name = file.getName();
            inputPathExcel.setText(pathFile);
        }

    }

    private final void loadFileFilter() {
        myFileChooser = new JFileChooser();
        ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.EXCEL_FORMAT);
        FileFilter fileFilter = Utilerias.getFileFilter("Archivo de Excel", list);
        //myFileChooser.setFileFilter(fileFilter);
        myFileChooser.addChoosableFileFilter(fileFilter);
        //myFileChooser.setAcceptAllFileFilterUsed(false);
        myFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private void enablefields(boolean b) {
        cboCatExcel.setEnabled(b);
        inputNameExcel.setEnabled(b);
        //inputPathExcel.setEnabled(b);
        txtCeldaInicial.setEnabled(b);
        txtCeldaFinal.setEnabled(b);
        txtHoja.setEnabled(b);
        openFileExcel.setEnabled(b);
        rbRango.setEnabled(b);
        rbCeldas.setEnabled(b);
        addCategory.setEnabled(b);
    }

    private void addLinkedExcelModel() {
        LinkedExcelModel model = linkedExcelTable.getModel() instanceof DefaultTableModel
                ? new LinkedExcelModel()
                : (LinkedExcelModel) linkedExcelTable.getModel();
        linkedExcelTable.setModel(model);

        try {
          /*  linkedExcelTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    //int row = languageTable.rowAtPoint(e.getPoint());
                    int column = linkedExcelTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        LinkedExcelModel model = (LinkedExcelModel) linkedExcelTable.getModel();
                        List<Integer> selectedRows = new ArrayList<>();
                        int length = model.getData() != null ? model.getData().length : 0;
                        for (int i = 0; i < length; i++) {
                            if (model.isCheckedRow(i) == true) {
                                selectedRows.add(model.getUniqueRowIdentify(i));
                            }
                        }
                        if (selectedRows.isEmpty() == true) {
                            
                            btnSaveLinkedExcel.setText(SAVE_LABEL);
                            btnSaveLinkedExcel.setEnabled(false);
                            btnNewLinkedExcel.setText(NEW_LABEL);
                            btnNewLinkedExcel.setEnabled(true);
                            btnDeleteLinkedExcel.setEnabled(false);
                            enablefields(false);
                        } else if (selectedRows.size() == 1) {
                            btnNewLinkedExcel.setText(CANCEL_LABEL);
                            btnNewLinkedExcel.setEnabled(true);
                            btnSaveLinkedExcel.setEnabled(true);
                            btnSaveLinkedExcel.setText(EDIT_LABEL);
                            btnDeleteLinkedExcel.setEnabled(true);
                        } else if (selectedRows.size() > 1) {
                            btnNewLinkedExcel.setText(CANCEL_LABEL);
                            btnNewLinkedExcel.setEnabled(true);
                            btnSaveLinkedExcel.setEnabled(false);
                            btnSaveLinkedExcel.setText(EDIT_LABEL);
                            btnDeleteLinkedExcel.setEnabled(true);
                        }
                    }
                }
            }); */
            fitColumns(linkedExcelTable);
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            linkedExcelTable.setDefaultRenderer(Object.class, stcr);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void setButtonsLinkedExcell(int lastRow){
        LinkedExcelModel model = (LinkedExcelModel) linkedExcelTable.getModel();
        List<Integer> selectedRows = new ArrayList<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {
                selectedRows.add(model.getUniqueRowIdentify(i));
            }
        }
        if (selectedRows.isEmpty() == true) {
            /*Cuando no se han seleccionado elementos en el grid*/
            btnSaveLinkedExcel.setText(SAVE_LABEL);
            btnSaveLinkedExcel.setEnabled(false);
            btnNewLinkedExcel.setText(NEW_LABEL);
            btnNewLinkedExcel.setEnabled(true);
            btnDeleteLinkedExcel.setEnabled(false);
            enablefields(false);
        } else if (selectedRows.size() == 1) {
            btnNewLinkedExcel.setText(CANCEL_LABEL);
            btnNewLinkedExcel.setEnabled(true);
            btnSaveLinkedExcel.setEnabled(true);
            btnSaveLinkedExcel.setText(EDIT_LABEL);
            btnDeleteLinkedExcel.setEnabled(true);
        } else if (selectedRows.size() > 1) {
            btnNewLinkedExcel.setText(CANCEL_LABEL);
            btnNewLinkedExcel.setEnabled(true);
            btnSaveLinkedExcel.setEnabled(false);
            btnSaveLinkedExcel.setText(EDIT_LABEL);
            btnDeleteLinkedExcel.setEnabled(true);
        }
        setCurrent(lastRow, 0);
    }

    private void getLinkedExcel() {
        try {
            ExcelDAO dAO = new ExcelDAO();
            List<LinkedExcelBO> list = dAO.getExcel();
            setLinkedExcelModel(list);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setLinkedExcelModel(List<LinkedExcelBO> list) {
        LinkedExcelModel model = new LinkedExcelModel(list);
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                setButtonsLinkedExcell(e.getLastRow());
                
            }
        });
        linkedExcelTable.setModel(model);
        fitColumns(linkedExcelTable);

        try {
            linkedExcelTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = linkedExcelTable.rowAtPoint(evt.getPoint());
                    int col = linkedExcelTable.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col > 0) {
                        setCurrent(row, col);
                    }
                }
            });
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void setCurrent(int row, int col) {

        try {

            LinkedExcelModel model = (LinkedExcelModel) linkedExcelTable.getModel();
            Object[][] data = model.getData();

            idExcel = (int) data[row][StatementConstant.SC_1.get()];
            int cat = (int) data[row][StatementConstant.SC_2.get()];

            for (int i = 0; i < cboCatExcel.getItemCount(); i++) {

                Object o = cboCatExcel.getItemAt(i);
                CatalogExcelBOCat bo = (CatalogExcelBOCat) o;
                if (cat == bo.getId_category()) {
                    cboCatExcel.setSelectedIndex(i);
                    break;
                }

            }

            inputNameExcel.setText(String.valueOf(data[row][StatementConstant.SC_4.get()]));
            inputPathExcel.setText(String.valueOf(data[row][StatementConstant.SC_5.get()])); // si

            if (!(boolean) data[row][StatementConstant.SC_10.get()]) {
                rbRango.setSelected(true);
                txtCeldaInicial.setText(String.valueOf(data[row][StatementConstant.SC_9.get()]));
            } else {
                rbCeldas.setSelected(true);
                txtHoja.setText(String.valueOf(data[row][StatementConstant.SC_6.get()]));
                txtCeldaInicial.setText(String.valueOf(data[row][StatementConstant.SC_7.get()]));
                txtCeldaFinal.setText(String.valueOf(data[row][StatementConstant.SC_8.get()]));
            }

//            inputDistrict.setText(String.valueOf(data[row][StatementConstant.SC_5.get()]));
//            inputZipCode.setText(String.valueOf(data[row][StatementConstant.SC_6.get()]));
//            inputCity.setText(String.valueOf(data[row][StatementConstant.SC_7.get()]));
//            inputState.setText(String.valueOf(data[row][StatementConstant.SC_8.get()]));
//            inputCountry.setText(String.valueOf(data[row][StatementConstant.SC_9.get()]));
//
//            checkNational.setSelected((boolean) data[row][StatementConstant.SC_10.get()]);
//
//            //Get Phones
//            inputPhone1.setText(String.valueOf(data[row][StatementConstant.SC_11.get()]));
//            inputPhone2.setText(String.valueOf(data[row][StatementConstant.SC_12.get()]));
//            inputPhone3.setText(String.valueOf(data[row][StatementConstant.SC_13.get()]));
//            inputPhone4.setText(String.valueOf(data[row][StatementConstant.SC_14.get()]));
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void fitColumns(JTable table) {
        /* Ajustar columnas de la tabla */
        Utilerias.adjustJTableRowSizes(table);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Utilerias.adjustColumnSizes(table, i, 4);
        }
        // hide some columns 
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get() - StatementConstant.SC_1.get()));
//        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_13.get() - StatementConstant.SC_2.get()));
//        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_14.get() - StatementConstant.SC_3.get()));
//        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_15.get() - StatementConstant.SC_4.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_8.get()));
    }

    private void saveLinkedExcel() {
        if (validateLinkedExcel()) {

            LinkedExcelBO bo = new LinkedExcelBO();

            Object obj = cboCatExcel.getSelectedItem();
            CatalogExcelBOCat bo2 = (CatalogExcelBOCat) obj;
            int id_category = bo2.getId_category();
            bo.setId_category(id_category);

            bo.setName(inputNameExcel.getText());
            bo.setPath(inputPathExcel.getText());

            if (rbCeldas.isSelected()) {
                bo.setIsCell(true);
                bo.setXY1(txtCeldaInicial.getText());
                bo.setXY2(txtCeldaFinal.getText());
                bo.setSheet(txtHoja.getText());
            } else {
                bo.setIsCell(false);
                bo.setRange(txtCeldaInicial.getText());
            }

            ExcelDAO dao = new ExcelDAO();

            if (isUpdate == false) {

                if (Utilerias.isRepeated(Utilerias.TablesValueNames.LINKEDEXCEL.name(),
                        Utilerias.TablesValueNames.LINKEDEXCEL.getCaption(),
                        bo.getName(), "IDLINKEDEXCEL", bo.getIdLinkedExcel())) {
                    JOptionPane.showMessageDialog(null, "El nombre ya existe, ingrese un nombre diferente.");
                    return;
                }

                int id = dao.insertCatalogExcelDAO(bo);

                if (id > 0) {
                    limpiar();
                    enablefields(false);
                    btnSaveLinkedExcel.setEnabled(false);
                    btnSaveLinkedExcel.setText(SAVE_LABEL);
                    btnDeleteLinkedExcel.setEnabled(false);
                    btnNewLinkedExcel.setText(NEW_LABEL);
                    addLinkedExcelModel();
                    getLinkedExcel();
                    JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente.");
                }
            } else if (isUpdate == true) {

                bo.setIdLinkedExcel(idExcel);

                if (Utilerias.isRepeated(Utilerias.TablesValueNames.LINKEDEXCEL.name(),
                        Utilerias.TablesValueNames.LINKEDEXCEL.getCaption(),
                        bo.getName(), "IDLINKEDEXCEL", bo.getIdLinkedExcel())) {
                    JOptionPane.showMessageDialog(null, "El nombre ya existe, ingrese un nombre diferente.");
                    return;
                }

                dao.insertCatalogExcelDAO(bo);
                limpiar();
                enablefields(false);
                btnSaveLinkedExcel.setEnabled(false);
                btnSaveLinkedExcel.setText(SAVE_LABEL);
                btnDeleteLinkedExcel.setEnabled(false);
                btnNewLinkedExcel.setText(NEW_LABEL);
                addLinkedExcelModel();
                getLinkedExcel();
                isUpdate = false;
                idExcel = 0;
                JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente.");
            }
        }

    }

    private boolean validateLinkedExcel() {
        boolean b = true;
        StringBuilder fields = new StringBuilder();

        Object obj = cboCatExcel.getSelectedItem();
        CatalogExcelBOCat bo = (CatalogExcelBOCat) obj;
        int id_category = bo.getId_category();

        if (id_category == -1) {
            b = false;
            fields.append("\nCategoría");
        }

        if (inputNameExcel.getText().isEmpty()) {
            b = false;
            fields.append("\nNombre");
        }

        if (inputPathExcel.getText().isEmpty()) {
            b = false;
            fields.append("\nRuta del Excel");
        }

        if (rbCeldas.isSelected()) {

            if (txtCeldaInicial.getText().isEmpty()) {
                b = false;
                fields.append("\nCelda inicial");
            }
            if (txtCeldaFinal.getText().isEmpty()) {
                b = false;
                fields.append("\nCelda final");
            }
            if (txtHoja.getText().isEmpty()) {
                b = false;
                fields.append("\nHoja");
            }
        } else {
            if (txtCeldaInicial.getText().isEmpty()) {
                b = false;
                fields.append("\nRango");
            }
        }

        if (!b) {
            JOptionPane.showMessageDialog(null, "Favor de ingresar los datos: " + fields);
        }
        return b;
    }

    private void deleteLinkedExcel() {
        LinkedExcelModel model = linkedExcelTable.getModel() instanceof DefaultTableModel
                ? new LinkedExcelModel()
                : (LinkedExcelModel) linkedExcelTable.getModel();
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
                    ExcelDAO dao = new ExcelDAO();
                    for (int i = 0; i < ids.size(); i++) {
                        dao.deleteLinkedExcel(ids.get(i));
                    }
                    addLinkedExcelModel();
                    getLinkedExcel();
                    btnNewLinkedExcel.setText(NEW_LABEL);
                    btnSaveLinkedExcel.setText(SAVE_LABEL);
                    btnSaveLinkedExcel.setEnabled(false);
                    btnDeleteLinkedExcel.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Registros Eliminados");
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCategory;
    private javax.swing.JButton btnDeleteLinkedExcel;
    private javax.swing.JButton btnNewLinkedExcel;
    private javax.swing.JButton btnSaveLinkedExcel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboCatExcel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JScrollPane centerScroll;
    private javax.swing.JTextField inputNameExcel;
    private javax.swing.JTextField inputPathExcel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblCeldaFinal;
    private javax.swing.JLabel lblCeldaInicial;
    private javax.swing.JLabel lblHoja;
    private javax.swing.JTable linkedExcelTable;
    private javax.swing.JPanel northPanel;
    private javax.swing.JButton openFileExcel;
    private javax.swing.JRadioButton rbCeldas;
    private javax.swing.JRadioButton rbRango;
    private javax.swing.JPanel southPanel;
    private javax.swing.JTextField txtCeldaFinal;
    private javax.swing.JTextField txtCeldaInicial;
    private javax.swing.JTextField txtHoja;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JFileChooser myFileChooser;
}

//class CustomExcelModel extends AbstractTableModel {
//
//    static final Integer COLUMN_COUNT = 5;
//
//    List<LinkedExcelBO> list;
//
//    public CustomExcelModel(List<LinkedExcelBO> list) {
//        this.list = list;
//    }
//
//    @Override
//    public int getRowCount() {
//        return list != null ? list.size() : 0;
//    }
//
//    @Override
//    public int getColumnCount() {
//        return COLUMN_COUNT;
//    }
//
//    @Override
//    public String getColumnName(int column) {
//        String name = "??";
//        switch (column) {
//            case 0:
//                name = "id";
//                break;
//            case 1:
//                name = "Categoria";
//                break;
//            case 2:
//                name = "Nombre";
//                break;
//            case 3:
//                name = "Ruta de Excel";
//                break;
//            case 4:
//                name = "Hoja";
//                break;
//            case 5:
//                name = "Celda final rango";
//                break;
//        }
//        return name;
//    }
//
//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//        Class type = String.class;
//        switch (columnIndex) {
//            case 0:
//                type = Integer.class;
//                break;
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//                type = String.class;
//                break;
//            case 6:
//                type = Date.class;
//                break;
//        }
//        return type;
//    }
//
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        LinkedExcelBO doc = list.get(rowIndex);
//        Object value = null;
//        switch (columnIndex) {
//            case 0:
//                value = doc.getIdLinkedExcel();
//                break;
//            case 1:
//            //value = doc.getCategory();
//            //break;
//            case 2:
//            //value = doc.getExcelName();
//            //break;
//            case 3:
//            ///value = doc.getHoja();
//            //break;
//            case 4:
//            //value = doc.getXY1();
//            //break;
//            case 5:
//                value = ""; //doc.getXY2();
//                break;
//            case 6:
//                value = doc.getDateModify();
//                break;
//
//        }
//        return value;
//    }
//
//}
