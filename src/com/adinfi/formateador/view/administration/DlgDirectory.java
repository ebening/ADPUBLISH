/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.seguridad.Usuario;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.administration.tablemodel.UserModel;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.DBResult;
import com.adinfi.ws.IAccess_Stub;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author ADINFI
 */
public class DlgDirectory extends javax.swing.JDialog {

    /**
     * Creates new form DlgDirectory
     */
    public DlgDirectory(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadUsuarios();
    }
    
    private void loadUsuarios() {
        List<Usuario> lUsuario = UtileriasWS.getDirectorio();
        Collections.sort(lUsuario, new CustomComparator());
        UserModel userModel = new UserModel(lUsuario);
        try {
            tableUser.setModel(userModel);
           
            fitColumnsUsers(tableUser);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }
    
    private void moveUp() {
        try{
            int rSel = tableUser.getSelectedRow();
            UserModel model = (UserModel) tableUser.getModel();
            int length = model.getData() != null ? model.getData().length : 0;

            if (length > 1 && rSel > 0) {

                Object[][] obj = model.getData();
                Object[] oAux = obj[rSel];
                obj[rSel] = obj[rSel - 1];
                obj[rSel - 1] = oAux;

                model = new UserModel();
                model.setData(obj);
                tableUser.setModel(model);
                fitColumnsUsers(tableUser);
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void moveDown() {
        try{
            int rSel = tableUser.getSelectedRow();
            UserModel model = (UserModel) tableUser.getModel();
            int length = model.getData() != null ? model.getData().length : 0;

            if (length > 1 && rSel < length - 1) {

                Object[][] obj = model.getData();
                Object[] oAux = obj[rSel];
                obj[rSel] = obj[rSel + 1];
                obj[rSel + 1] = oAux;

                model = new UserModel();
                model.setData(obj);
                tableUser.setModel(model);
                fitColumnsUsers(tableUser);
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void save() {
        UserModel model = (UserModel) tableUser.getModel();
        if (model.getData().length > 0) {
            
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);

            StringBuilder sb = new StringBuilder("<Usuarios>");
            for (int i = 0; i < model.getData().length; i++) {
                String formato ="<Usuarios><Usuario UsuarioId=\"1\" Orden=\"1\"/><Usuario UsuarioId=\"2\" Orden=\"2\"/></Usuarios>";
                sb.append("<Usuario UsuarioId=\"").append(model.getData()[i][StatementConstant.SC_1.get()]).append("\" Orden=\"").append(i+1).append("\"/>");
            }
            sb.append("</Usuarios>");
            
            try {
                int us = InstanceContext.getInstance().getUsuario().getUsuarioId();
                DBResult result = stub.modificarDirectorio(sb.toString(), "Formateador", 0, Utilerias.getIP());
                if (result.getResultCd().equals("000")){ 
                    JOptionPane.showMessageDialog(this, "Se ha guardado correctamente", "Vector", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "El servicio de modificar directorio no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            }
        }
        
        
    }
    
    private void fitColumnsUsers(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_10.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_9.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_8.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_7.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_6.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_5.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_4.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_3.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            
            
        } catch (Exception e) {

        }
    }
    
    public class CustomComparator implements Comparator<Usuario> {
        @Override
        public int compare(Usuario o1, Usuario o2) {
            return Integer.valueOf(o1.getOrdenDir()). compareTo(o2.getOrdenDir());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableUser);

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/up2.png"))); // NOI18N
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/down2.png"))); // NOI18N
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        btnSave.setText("Guardar");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnSave)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDown)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        moveUp();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        moveDown();
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DlgDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgDirectory dialog = new DlgDirectory(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableUser;
    // End of variables declaration//GEN-END:variables
}
