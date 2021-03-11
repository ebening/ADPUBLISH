package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.seguridad.Perfil;
import com.adinfi.formateador.bos.seguridad.Usuario;
import com.adinfi.formateador.bos.usuarioADBO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.UserModel;
import com.adinfi.formateador.view.resources.CCheckBox;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfPerfil;
import com.adinfi.ws.ArrayOfUsuario;
import com.adinfi.ws.ArrayOfUsuarioEmisora;
import com.adinfi.ws.ArrayOfUsuarioSector;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.UsuarioEmisora;
import com.adinfi.ws.UsuarioSector;
import com.adinfi.ws.UsuarioSeguimiento;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guillermo Trejo
 */
public class Users extends javax.swing.JDialog {

    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    List<Integer> selectedRows;
    boolean isEdit = false;

    /**
     * Creates new form Office1
     *
     * @param parent
     * @param modal
     */
    public Users(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Idiomas = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        inputExtUsr = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        inputNombre = new javax.swing.JTextField();
        inputEmailUsr = new javax.swing.JTextField();
        inputUserNT = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cboProfile = new javax.swing.JComboBox();
        cboIndustry = new JComboCheckBox();
        jButton1 = new javax.swing.JButton();
        cmbEmisoras = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        isAutorCheck = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        directoryCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnDirectrio = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        inputUserSearch = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cboProfileSearch = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Usuarios");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        Idiomas.setName("Idiomas"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("* Campo Requerido");
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "", "Nombre*", "Extensión*", "Correo*", "Usuario de red*", "Perfil*"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        userTable.setName("userTable"); // NOI18N
        jScrollPane9.setViewportView(userTable);

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setName("jPanel15"); // NOI18N

        jLabel10.setText("Nombre*");
        jLabel10.setName("jLabel10"); // NOI18N

        inputExtUsr.setEnabled(false);
        inputExtUsr.setName("inputExtUsr"); // NOI18N

        jLabel11.setText("Extensión");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText("Correo*");
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText("Usuario de red*");
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText("Perfil*");
        jLabel14.setName("jLabel14"); // NOI18N

        inputNombre.setEnabled(false);
        inputNombre.setName("inputNombre"); // NOI18N

        inputEmailUsr.setName("inputEmailUsr"); // NOI18N

        inputUserNT.setEnabled(false);
        inputUserNT.setName("inputUserNT"); // NOI18N

        jLabel15.setText("Empresas a las que sigue ");
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel19.setText("Fecha de actualización");
        jLabel19.setName("jLabel19"); // NOI18N

        jTextField17.setEnabled(false);
        jTextField17.setName("jTextField17"); // NOI18N

        jTextField2.setEnabled(false);
        jTextField2.setName("jTextField2"); // NOI18N

        jLabel20.setText("Fecha de alta");
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel21.setText("Sectores");
        jLabel21.setName("jLabel21"); // NOI18N

        cboProfile.setEnabled(false);
        cboProfile.setName("cboProfile"); // NOI18N

        cboIndustry.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboIndustry.setEnabled(false);
        cboIndustry.setName("cboIndustry"); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton1.setText("+");
        jButton1.setEnabled(false);
        jButton1.setMargin(new java.awt.Insets(1, 7, 1, 7));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cmbEmisoras.setName("cmbEmisoras"); // NOI18N

        jLabel22.setText("Autor*");
        jLabel22.setName("jLabel22"); // NOI18N

        isAutorCheck.setEnabled(false);
        isAutorCheck.setName("isAutorCheck"); // NOI18N

        jLabel23.setText("Directorio*");
        jLabel23.setName("jLabel23"); // NOI18N

        directoryCheckBox.setEnabled(false);
        directoryCheckBox.setName("directoryCheckBox"); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputUserNT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(inputEmailUsr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(inputExtUsr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(inputNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(cboProfile, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(cmbEmisoras, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(cboIndustry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(isAutorCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(directoryCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(inputNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputExtUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputEmailUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputUserNT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(cboIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(isAutorCheck)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(directoryCheckBox))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel15))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(cmbEmisoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1.setName("jPanel1"); // NOI18N

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNew.setText("Nuevo");
        btnNew.setName("btnNew"); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel1.add(btnNew);

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSave.setText("Guardar");
        btnSave.setEnabled(false);
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave);

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDelete.setText("Eliminar");
        btnDelete.setEnabled(false);
        btnDelete.setName("btnDelete"); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete);

        btnDirectrio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDirectrio.setText("Directorio");
        btnDirectrio.setName("btnDirectrio"); // NOI18N
        btnDirectrio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirectrioActionPerformed(evt);
            }
        });
        jPanel1.add(btnDirectrio);

        javax.swing.GroupLayout IdiomasLayout = new javax.swing.GroupLayout(Idiomas);
        Idiomas.setLayout(IdiomasLayout);
        IdiomasLayout.setHorizontalGroup(
            IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IdiomasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(IdiomasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        IdiomasLayout.setVerticalGroup(
            IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IdiomasLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setName("jPanel14"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Nombre");
        jLabel5.setName("jLabel5"); // NOI18N

        inputUserSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputUserSearch.setName("inputUserSearch"); // NOI18N

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Perfil");
        jLabel3.setName("jLabel3"); // NOI18N

        cboProfileSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "Analista", "Sistemas", "Colaborador", "" }));
        cboProfileSearch.setName("cboProfileSearch"); // NOI18N
        cboProfileSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProfileSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputUserSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(cboProfileSearch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(inputUserSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboProfileSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Idiomas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Idiomas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        if (btnSave.getText().equals(SAVE_LABEL)) {
            saveUser();
        } else if (btnSave.getText().equals(EDIT_LABEL)) {
            isEdit = true;
            setEnabledFields(true);
            btnSave.setText(SAVE_LABEL);
            btnDirectrio.setEnabled(false);
            userTable.setEnabled(false);
            btnDelete.setEnabled(false);
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        if (btnNew.getText().equals(NEW_LABEL)) {
            NewUserDialog newD = new NewUserDialog(null, true);
            usuarioADBO r = newD.returnValue();
            if (r != null && !r.getNombre().isEmpty()) {
                inputNombre.setText(r.getNombre());
                inputExtUsr.setText(r.getExtension());
                inputEmailUsr.setText(r.getCorreo());
                inputUserNT.setText(r.getUsuarioNT());
                btnSave.setEnabled(true);
                btnNew.setText(CANCEL_LABEL);
                btnDirectrio.setEnabled(false);
                setEnabledFields(true);
                jTextField2.setText(null);
                jTextField17.setText(null);
                userTable.setEnabled(false);
            }
        } else if (btnNew.getText().equals(CANCEL_LABEL)) {
            cancelAction();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FollowCompanies emp = new FollowCompanies(null, true, usu, idUsuario);
        emp.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadEmisoras();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                loadEmisoras();
            }

        });
        Utilerias.centreDialog(emp, true);
        emp.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        loadProfile();
        loadIndustry();
        addUserModel();
        searchByName(null);
        loadUsers();

    }//GEN-LAST:event_formComponentShown

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteUser();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cboProfileSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProfileSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProfileSearchActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        searchByName(inputUserSearch.getText());
        clearFields();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnDirectrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDirectrioActionPerformed
        DlgDirectory dir = new DlgDirectory(null, true);
        Utilerias.centreDialog(dir, true);
        dir.setVisible(true);
    }//GEN-LAST:event_btnDirectrioActionPerformed

    private void setButtonsUser(UserModel model){
        selectedRows = new ArrayList<>();
        Map<Integer, Integer> slcted = new HashMap<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i)) {
                selectedRows.add(model.getUniqueRowIdentify(i));
                slcted.put(model.getUniqueRowIdentify(i), i);
            }
        }

        if (selectedRows.isEmpty() == true) {
            //Cuando no se han seleccionado elementos en el grid
            btnSave.setText(SAVE_LABEL);
            btnSave.setEnabled(false);
            btnNew.setText(NEW_LABEL);
            btnNew.setEnabled(true);
            btnDirectrio.setEnabled(true);
            btnDelete.setEnabled(false);
            clearFields();
        } else if (selectedRows.size() == 1) {
            btnNew.setText(CANCEL_LABEL);
            btnNew.setEnabled(true);
            btnSave.setEnabled(true);
            btnSave.setText(EDIT_LABEL);
            btnDelete.setEnabled(true);
            btnDirectrio.setEnabled(true);
            setCurrent(slcted.get(selectedRows.get(selectedRows.size() - 1)));
        } else if (selectedRows.size() > 1) {
            btnNew.setText(CANCEL_LABEL);
            btnNew.setEnabled(true);
            btnSave.setEnabled(false);
            btnSave.setText(EDIT_LABEL);
            btnDelete.setEnabled(true);
            btnDirectrio.setEnabled(false);
            
        }

        if (selectedRows.isEmpty()) {
            setCurrent(userTable.getSelectedRow());
        } 
    }
    
    private void addUserModel() {

        UserModel model = userTable.getModel() instanceof DefaultTableModel
                ? new UserModel()
                : (UserModel) userTable.getModel();
        userTable.setModel(model);

        try {
         /*   userTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectedRows = new ArrayList<>();
                    Map<Integer, Integer> slcted = new HashMap<>();
                    int column = userTable.columnAtPoint(e.getPoint());
                    // Habilitar e inhabiliar botones de eliminar guardar
                    UserModel model = (UserModel) userTable.getModel();

                /*    int length = model.getData() != null ? model.getData().length : 0;
                    for (int i = 0; i < length; i++) {
                        if (model.isCheckedRow(i)) {
                            selectedRows.add(model.getUniqueRowIdentify(i));
                            slcted.put(model.getUniqueRowIdentify(i), i);
                        }
                    }
                    if (column == StatementConstant.SC_0.get()) {

                        if (selectedRows.isEmpty() == true) {
                            //Cuando no se han seleccionado elementos en el grid
                            btnSave.setText(SAVE_LABEL);
                            btnSave.setEnabled(false);
                            btnNew.setText(NEW_LABEL);
                            btnNew.setEnabled(true);
                            btnDirectrio.setEnabled(true);
                            btnDelete.setEnabled(false);
                            clearFields();
                        } else if (selectedRows.size() == 1) {
                            btnNew.setText(CANCEL_LABEL);
                            btnNew.setEnabled(true);
                            btnSave.setEnabled(true);
                            btnSave.setText(EDIT_LABEL);
                            btnDelete.setEnabled(true);
                            btnDirectrio.setEnabled(true);
                            setCurrent(slcted.get(selectedRows.get(selectedRows.size() - 1)));
                        } else if (selectedRows.size() > 1) {
                            btnNew.setText(CANCEL_LABEL);
                            btnNew.setEnabled(true);
                            btnSave.setEnabled(false);
                            btnSave.setText(EDIT_LABEL);
                            btnDelete.setEnabled(true);
                            btnDirectrio.setEnabled(false);
                        }
                    }

                    if (selectedRows.isEmpty()) {
                        setCurrent(userTable.getSelectedRow());
                    } 
                } 
            }); */
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            userTable.setDefaultRenderer(Object.class, stcr);
            fitColumnsUsers(userTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

//    private boolean isAnyRowChecked() {
//        boolean checked = false;
//        UserModel model = userTable.getModel() instanceof UserModel
//                ? new UserModel()
//                : (UserModel) userTable.getModel();
//        if (model == null) {
//            return false;
//        } else {
//            for (int i = 0; i < userTable.getModel().getRowCount(); i++) {
//                if (model.isCheckedRow(i));
//                {
//                    checked = true;
//                    break;
//                }
//            }
//            return checked;
//        }
//
//    }
    private void setUserModel(List<com.adinfi.formateador.bos.seguridad.Usuario> list) {
        UserModel userModel = new UserModel(list);
//        try {
//            userTable.addMouseListener(new java.awt.event.MouseAdapter() {
//                @Override
//                public void mouseClicked(java.awt.event.MouseEvent evt) {
//                    int row = userTable.rowAtPoint(evt.getPoint());
//                    int col = userTable.columnAtPoint(evt.getPoint());
//                    if (row >= 0 && col >= 0) {
//                        if (evt.getClickCount() == 1) {
//                            setCurrent(row, col);
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Utilerias.logger(getClass()).info(e);
//        }

        try {
            userModel.addTableModelListener((TableModelEvent e)->{
                setButtonsUser(userModel);
            });
            userTable.setModel(userModel);
            fitColumnsUsers(userTable);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private Object[] usu;
    private int idUsuario;

    private void setCurrent(int row) {

        try {
            UserModel model = (UserModel) userTable.getModel();
            Object[][] data = model.getData();
            idUsuario = (Integer) data[row][StatementConstant.SC_1.get()];
            usu = new Object[]{
                "<Usuarios>\n<Usuario>" + data[row][StatementConstant.SC_1.get()] + "</Usuario>\n</Usuarios>",
                GlobalDefines.WS_INSTANCE,
                data[row][StatementConstant.SC_10.get()],//Perfil 
                data[row][StatementConstant.SC_9.get()],// isAutor
                "", //empresas
                "", //sectores
                data[row][StatementConstant.SC_11.get()], //isDirectorio
                InstanceContext.getInstance().getUsuario().getUsuarioId(),
                Utilerias.getIP(),
                null};

            inputNombre.setText(String.valueOf(data[row][StatementConstant.SC_2.get()]));
            inputExtUsr.setText(String.valueOf(data[row][StatementConstant.SC_3.get()]));
            inputEmailUsr.setText(String.valueOf(data[row][StatementConstant.SC_4.get()]));
            inputUserNT.setText(String.valueOf(data[row][StatementConstant.SC_5.get()]));
            jTextField2.setText(String.valueOf(data[row][StatementConstant.SC_7.get()]));
            jTextField17.setText(String.valueOf(data[row][StatementConstant.SC_8.get()]));
            isAutorCheck.setSelected((boolean) data[row][StatementConstant.SC_9.get()]);
            directoryCheckBox.setSelected((boolean) data[row][StatementConstant.SC_11.get()]);

            int profileID = (int) data[row][StatementConstant.SC_10.get()];
            for (int i = 0; i < cboProfile.getItemCount(); i++) {
                Object obj = cboProfile.getItemAt(i);
                com.adinfi.formateador.bos.seguridad.Perfil bo
                        = (com.adinfi.formateador.bos.seguridad.Perfil) obj;
                if (bo.getPerfilId() == profileID) {
                    cboProfile.setSelectedItem(obj);
                    break;
                }
            }

            loadEmisoras();
            loadIndustry();
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    //private StringBuilder sbEmisoras = new StringBuilder();
    String emisorasLoad = null;
    List<String> lstEmisoras = new ArrayList<>();

    private void loadEmisoras() {
        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.getByName(null, true);

        emisorasLoad = null;
        lstEmisoras.clear();

        IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
        UtileriasWS.setEndpoint(stub);
        if (listSubject != null) {
            try {
                UsuarioSeguimiento usuSeg = stub.getSectorEmisoraPorUsuario(idUsuario);
                ArrayOfUsuarioEmisora aEm = usuSeg.getEmisoras();
                UsuarioEmisora[] ue = aEm.getUsuarioEmisora();
                if (ue != null) {
                    cmbEmisoras.removeAllItems();
                    for (SubjectBO listSubject1 : listSubject) {
                        for (UsuarioEmisora ue1 : ue) {
                            if (listSubject1.getIdSubject() == Integer.parseInt(ue1.getEmisora())) {
                                SubjectBO bo = new SubjectBO();
                                bo.setName(listSubject1.getName());
                                bo.setIdSubject(listSubject1.getIdSubject());
                                cmbEmisoras.addItem(bo);
                                //sbEmisoras.append(",").append(listSubject1.getIdSubject());
                                lstEmisoras.add(String.valueOf(listSubject1.getIdSubject()));
                                //System.out.println(listSubject1.getIdSubject() + "->" + listSubject1.getName());
                                break;
                            }
                        }
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "El servicio de sectores por emisora no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            }
        }
        if (lstEmisoras.isEmpty() == false) {
            emisorasLoad = StringUtils.join(lstEmisoras, ",");
        }
    }

    private void fitColumnsUsers(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_7.get() - 1));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_8.get() - 2));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_10.get() - 3));

        } catch (Exception e) {

        }
    }

    private void loadProfile() {
        List<com.adinfi.formateador.bos.seguridad.Perfil> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfPerfil array = stub.perfiles(GlobalDefines.WS_INSTANCE);
            if (array != null && array.getPerfil() != null) {
                for (com.adinfi.ws.Perfil o : array.getPerfil()) {
                    com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    list.add(p);
                }
                cboProfile.removeAllItems();
                cboProfileSearch.removeAllItems();

                com.adinfi.formateador.bos.seguridad.Perfil bo = new Perfil();
                bo.setNombre("Seleccione:");
                bo.setPerfilId(0);
                cboProfileSearch.addItem(bo);
                cboProfile.addItem(bo);
                for (Perfil o : list) {
                    cboProfile.addItem(o);
                    cboProfileSearch.addItem(o);
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de perfiles no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }

    private Map<String, Integer> mapInd;

    private void loadIndustry() {
        try {
            IndustryDAO dAO = new IndustryDAO();
            List<IndustryBO> list = dAO.get(null);
            if (list != null) {
                IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                UtileriasWS.setEndpoint(stub);

                UsuarioSeguimiento usuSeg = stub.getSectorEmisoraPorUsuario(idUsuario);
                ArrayOfUsuarioSector aUS = usuSeg.getSectores();
                UsuarioSector[] us = aUS.getUsuarioSector();

                mapInd = new HashMap<>();
                cboIndustry.removeAllItems();
                for (int i = 0; i < cboIndustry.getItemCount(); i++) {
                    CCheckBox cb = (CCheckBox) cboIndustry.getItemAt(i);
                    cb.setSelected(false);
                }
                for (IndustryBO indBO : list) {
                    JCheckBox check = new JCheckBox(indBO.getName());
                    boolean res = false;
                    for (UsuarioSector us1 : us) {
                        if (indBO.getIdIndustry() == Integer.parseInt(us1.getSector())) {
                            res = true;
                            break;
                        }
                    }
                    check.setSelected(res);
                    cboIndustry.addItem(check);
                    mapInd.put(indBO.getName(), indBO.getIdIndustry());
                }
            }
        } catch (RemoteException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de sectores por emisora no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void loadUsers() {
        List<com.adinfi.formateador.bos.seguridad.Usuario> list = new ArrayList<>();
        list = UtileriasWS.getAllUsers();
        //setUserModel(list);
    }

    private void searchByName(String name) {

        int idProfile = 0;
        Object obj = cboProfileSearch.getSelectedItem();
        com.adinfi.formateador.bos.seguridad.Perfil bo
                = (com.adinfi.formateador.bos.seguridad.Perfil) obj;

        idProfile = bo.getPerfilId();

        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, idProfile, name);
            List<Usuario> list = new ArrayList<>();
            if (array != null && array.getUsuario() != null) {
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    //com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    Usuario u = new com.adinfi.formateador.bos.seguridad.Usuario(
                            false,
                            o.getUsuarioId(),
                            o.getUsuarioNT(),
                            o.getNombre(),
                            o.getCorreo(),
                            o.getExtension(),
                            o.getFechaAlta(),
                            o.getUltimoAcceso(),
                            o.getIsAutor(),
                            o.getPerfilId(),
                            o.getPerfil(),
                            o.getMiVectorId(),
                            o.getIsDirectorio()
                    );
                    list.add(u);
                }
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No existen coincidencias en la búsqueda");
                } else {
                    setUserModel(list);
                }
            }

            // textAutoCompleter.addItems(list);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void saveUser() {
        if (!isEdit) {
            if (validateUser()) {
                if (!validateUserRepeat(GlobalDefines.WS_INSTANCE, inputUserNT.getText())) {
                    IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                    UtileriasWS.setEndpoint(stub);
                    Integer ext = null;
                    if (!inputExtUsr.getText().isEmpty()) {
                        ext = Integer.valueOf(inputExtUsr.getText());
                    }
                    StringBuilder indus = new StringBuilder();
                    if (cboIndustry != null) {
                        for (int i = 0; i < cboIndustry.getItemCount(); i++) {
                            JCheckBox check = (JCheckBox) cboIndustry.getItemAt(i);
                            if (check.isSelected()) {
                                indus.append(",").append(mapInd.get(check.getText()));
                            }
                        }
                    }

                    try {
                        stub.crearUsuario(
                                GlobalDefines.WS_INSTANCE,
                                inputNombre.getText(),
                                inputUserNT.getText(),
                                inputEmailUsr.getText(),
                                ext,
                                getCurrentSelectedPerfilID(),
                                isAutor(),
                                emisorasLoad, //sbEmisoras.toString().isEmpty() ? "" : sbEmisoras.toString().substring(1), //Empresas
                                indus.toString().isEmpty() ? "" : indus.toString().substring(1), //Sectores
                                directoryCheckBox.isSelected(), //isDirecotrio
                                getCurrentLoggedIDusr(),
                                Utilerias.getIP()
                        );
                        loadUsers();
                        clearFields();
                        btnDirectrio.setEnabled(true);
                        btnSave.setEnabled(false);
                        btnDelete.setEnabled(false);
                        searchByName("");
                        clearFields();
                        JOptionPane.showMessageDialog(this, "El usuario fue creado correctamente");

                        cancelAction();
                    } catch (RemoteException ex) {
                        //JOptionPane.showMessageDialog(this, "Ocurrio un error al guardar");
                        JOptionPane.showMessageDialog(null, "El servicio de crear usuario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                        cancelAction();
                        Utilerias.logger(getClass()).info(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario " + inputUserNT.getText() + " ya existe");
                }
            }
        } else if (isEdit) {
            editUser();
        }
    }

    private void editUser() {
        if (validateUser()) {

            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);

            int userID = checkedRowVal();

            String xml = "<Usuarios>\n"
                    + "<Usuario>" + userID + "</Usuario>\n"
                    + "</Usuarios>";

            Object obj = cboProfile.getSelectedItem();
            com.adinfi.formateador.bos.seguridad.Perfil bo
                    = (com.adinfi.formateador.bos.seguridad.Perfil) obj;

            List<String> lstIndustry = new ArrayList<>();
            String sectores = null;

            if (cboIndustry != null) {
                for (int i = 0; i < cboIndustry.getItemCount(); i++) {
                    JCheckBox check = (JCheckBox) cboIndustry.getItemAt(i);
                    if (check.isSelected()) {
                    //indus.append(",").append(mapInd.get(check.getText()));
                        //StringUtils.j
                        //System.out.println(mapInd.get(check.getText()));
                        lstIndustry.add(mapInd.get(check.getText()).toString());

                    }
                }

                sectores = StringUtils.join(lstIndustry, ",");
            }

            try {
                stub.modificarPermisosUsuario(
                        xml,
                        GlobalDefines.WS_INSTANCE,
                        bo.getPerfilId(),
                        isAutorCheck.isSelected(),
                        emisorasLoad, //empresas
                        sectores, //sectores
                        directoryCheckBox.isSelected(), //isDirectorio
                        InstanceContext.getInstance().getUsuario().getUsuarioId(),
                        Utilerias.getIP(),
                        "");
                btnSave.setEnabled(false);
                btnDelete.setEnabled(false);
                btnNew.setText(NEW_LABEL);
                JOptionPane.showMessageDialog(this, "El usuario fue modificado correctamente");
                isEdit = false;
                searchByName(null);
                setEnabledFields(false);
                clearFields();
                btnDirectrio.setEnabled(true);
                userTable.setEnabled(true);
            } catch (RemoteException ex) {
                //JOptionPane.showMessageDialog(this, "Ocurrio un error al modificar");
                JOptionPane.showMessageDialog(null, "El servicio de modificar permisos de usuario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                cancelAction();
                Utilerias.logger(getClass()).info(ex);
            }
        }
    }

    private boolean validateUser() {
        boolean b = true;
        StringBuilder fields = new StringBuilder();

        if (inputNombre.getText().isEmpty()) {
            b = false;
            fields.append("\nNombre");
        }

        if (inputEmailUsr.getText().isEmpty()) {
            b = false;
            fields.append("\nCorreo");
        }

        if (!inputExtUsr.getText().isEmpty()) {
            if (isNumeric(inputExtUsr.getText()) == false) {
                b = false;
                fields.append("\nExtensión debe ser numerica");
            }
        }

        if (cboProfile.getSelectedIndex() == 0) {
            b = false;
            fields.append("\nPerfil");
        }

        if (inputUserNT.getText().isEmpty()) {
            b = false;
            fields.append("\nUsuario de Red");
        }

        Object object = cboProfile.getSelectedItem();
        if (object == null) {
            b = false;
            fields.append("\nPerfil");
        }

        if (!b) {
            JOptionPane.showMessageDialog(this, "Complete los siguientes campos: " + fields);
        }

        return b;
    }

    private int checkedRowVal() {
        int num = 0;
        UserModel model = (UserModel) userTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {
                num = model.getUniqueRowIdentify(i);
                break;
            }
        }
        return num;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateUserRepeat(String sitio, String usuarioNT) {
        boolean b = false;
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            b = stub.validaUsuario(sitio, usuarioNT);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de validación de usuario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }
        return b;
    }

    private int getCurrentSelectedPerfilID() {
        int id = 0;
        Object o = cboProfile.getSelectedItem();
        com.adinfi.formateador.bos.seguridad.Perfil obj = (com.adinfi.formateador.bos.seguridad.Perfil) o;
        id = obj.getPerfilId();
        return id;
    }

    private boolean isAutor() {
        boolean b = isAutorCheck.isSelected();
        return b;
    }

    private int getCurrentLoggedIDusr() {
        int id = 0;
        id = InstanceContext.getInstance().getUsuario().getUsuarioId();
        return id;
    }

    private void deleteUser() {

        if (selectedRows == null || selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar");
        } else {
            String comment = JOptionPane.showInputDialog(null, "Eliminar Usuario(s) \nEnvía un comentario.", "Eliminar Usuario(s)", JOptionPane.QUESTION_MESSAGE);
            if (!comment.isEmpty()) {
                int id = 0;
                StringBuilder xmlUsuarios = new StringBuilder("<Usuarios>");

                for (int i = 0; i < selectedRows.size(); i++) {
                    xmlUsuarios.append("<Usuario>");
                    xmlUsuarios.append(selectedRows.get(i));
                    xmlUsuarios.append("</Usuario>");
                }
                xmlUsuarios.append("</Usuarios>");

                try {
                    IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                    UtileriasWS.setEndpoint(stub);
                    stub.eliminarUsuario(
                            xmlUsuarios.toString(),
                            GlobalDefines.WS_INSTANCE,
                            getCurrentLoggedIDusr(),
                            Utilerias.getIP(),
                            comment);
                    loadUsers();
                    btnDirectrio.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente");
                    searchByName(null);
                    clearFields();
                    cancelAction();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "El servicio de eliminar usuario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                    Utilerias.logger(getClass()).info(ex);
                }

                clearFields();
                btnDelete.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(null, "Ingrese un comentario para continuar.");
            }
        }
    }

    private void clearFields() {
        inputNombre.setText(null);
        inputExtUsr.setText(null);
        inputEmailUsr.setText(null);
        inputUserNT.setText(null);
        cboProfile.setSelectedIndex(0);
        isAutorCheck.setSelected(false);
        directoryCheckBox.setEnabled(false);
        jTextField2.setText(null);
        jTextField17.setText(null);
    }

    private void setEnabledFields(boolean b) {
        inputExtUsr.setEnabled(b);
        cboProfile.setEnabled(b);
        isAutorCheck.setEnabled(b);
        directoryCheckBox.setEnabled(b);
        jButton1.setEnabled(b);
        cboIndustry.setEnabled(b);
        cmbEmisoras.setEnabled(b);
    }

    private void cancelAction() {
        loadUsers();
        btnNew.setText(NEW_LABEL);
        clearFields();
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        setEnabledFields(false);
        btnDirectrio.setEnabled(true);
        jTextField2.setText(null);
        jTextField17.setText(null);
        userTable.setEnabled(true);
        searchByName(inputUserSearch.getText());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Idiomas;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDirectrio;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cboIndustry;
    private javax.swing.JComboBox cboProfile;
    private javax.swing.JComboBox cboProfileSearch;
    private javax.swing.JComboBox cmbEmisoras;
    private javax.swing.JCheckBox directoryCheckBox;
    private javax.swing.JTextField inputEmailUsr;
    private javax.swing.JTextField inputExtUsr;
    private javax.swing.JTextField inputNombre;
    private javax.swing.JTextField inputUserNT;
    private javax.swing.JTextField inputUserSearch;
    private javax.swing.JCheckBox isAutorCheck;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
