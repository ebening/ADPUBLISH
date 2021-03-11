package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.administration.tablemodel.SubjectModel;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfUsuarioEmisora;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.UsuarioEmisora;
import com.adinfi.ws.UsuarioSeguimiento;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class FollowCompanies extends javax.swing.JDialog {

    private Object[] usu;
    private int idUsuario;
    
    public FollowCompanies(java.awt.Frame parent, boolean modal, Object[] usu, int idUsuario) {
        super(parent, modal);
        initComponents();
        this.usu = usu;
        this.idUsuario = idUsuario;
        loadModel();
    }
    
    private StringBuilder sbEmisoras = new StringBuilder();
    
    private void loadModel() {
        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.getByName(null, true);
        
        IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
        UtileriasWS.setEndpoint(stub);
        if (listSubject != null) {
            try {
                UsuarioSeguimiento usuSeg = stub.getSectorEmisoraPorUsuario(idUsuario);
                ArrayOfUsuarioEmisora aEm = usuSeg.getEmisoras();
                UsuarioEmisora[] ue = aEm.getUsuarioEmisora();
                if (ue != null) {
                    for (SubjectBO listSubject1 : listSubject) {
                        for (UsuarioEmisora ue1 : ue) {
                            if (listSubject1.getIdSubject() == Integer.parseInt(ue1.getEmisora())) {
                                listSubject1.setCheck(true);
                                sbEmisoras.append(",").append(listSubject1.getIdSubject());
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
        
        SubjectModel subMod = new SubjectModel(listSubject);
        jTable1.setModel(subMod);
        fitColumns(jTable1);
    }
    
    private void save() {
        IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
        UtileriasWS.setEndpoint(stub);
        try {
             SubjectModel model = (SubjectModel) jTable1.getModel();
             StringBuilder sb = new StringBuilder();
            if (model.getData().length > 0) {

                for (int i = 0; i < model.getData().length; i++) {
                    if ((boolean)model.getData()[i][StatementConstant.SC_0.get()]) {
                        sb.append(",").append(model.getData()[i][StatementConstant.SC_1.get()]);
                    }
                }
            }
            stub.modificarPermisosUsuario(
                    (String)usu[0],
                    (String)usu[1],
                    (int)usu[2], 
                    (boolean)usu[3],
                    sb.toString().isEmpty() ? "" : sb.toString().substring(1), //empresas
                    (String)usu[5], //sectores
                    (boolean)usu[6], //isDirectorio
                    (int)usu[7],
                    (String)usu[8],
                    null);
            JOptionPane.showMessageDialog(this, "Se ha guardado correctamente", "Vector", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de modificar permisos de usuario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(FollowCompanies.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void searchCompany() {
        SubjectDAO daoSubject = new SubjectDAO();
        String name = jTextField8.getText();
        if (name != null && !name.isEmpty()) {
            List<SubjectBO> listSubject = daoSubject.getByName(name, true);
            SubjectModel subMod = new SubjectModel(listSubject);
            jTable1.setModel(subMod);
            fitColumns(jTable1);
        } else {
            
        }
    }
    
    private void fitColumns(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_3.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_4.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_3.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));

            
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField8 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Empresas a las que sigue");

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "Empresa 1"},
                {null, "Empresa 2"},
                {null, "Empresa 3"},
                {null, "Empresa 4"}
            },
            new String [] {
                "", "Empresa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);

        jTextField8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField8.setName("jTextField8"); // NOI18N

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Búsqueda");
        jLabel10.setName("jLabel10"); // NOI18N

        btnCancel.setText("Cancelar");
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnAccept.setText("Aceptar");
        btnAccept.setName("btnAccept"); // NOI18N
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(btnAccept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnAccept))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        searchCompany();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        save();
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
