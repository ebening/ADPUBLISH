package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.usuarioADBO;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfUsuarioAD;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.UsuarioAD;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author Josue Sanchez
 */
public class NewUserDialog extends javax.swing.JDialog {

    private usuarioADBO selectedValue;

    public usuarioADBO getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(usuarioADBO selectedValue) {
        this.selectedValue = selectedValue;
    }

    public NewUserDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(inputUsuers);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        inputUsuers = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscar Usuario");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSave.setText("Aceptar");
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave);

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDelete.setText("Cancelar");
        btnDelete.setName("btnDelete"); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete);

        inputUsuers.setName("inputUsuers"); // NOI18N
        inputUsuers.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                inputUsuersInputMethodTextChanged(evt);
            }
        });
        inputUsuers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputUsuersKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputUsuersKeyReleased(evt);
            }
        });

        jLabel1.setText("Nombre");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputUsuers)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputUsuers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        //loadUsersLDAP();
    }//GEN-LAST:event_formComponentShown

    private void inputUsuersKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputUsuersKeyTyped
        listenerData();
    }//GEN-LAST:event_inputUsuersKeyTyped

    private void inputUsuersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputUsuersMouseClicked
        listenerData();
    }//GEN-LAST:event_inputUsuersMouseClicked

    private void inputUsuersFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputUsuersFocusLost
        listenerData();
    }//GEN-LAST:event_inputUsuersFocusLost

    private void inputUsuersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputUsuersMousePressed
        listenerData();
    }//GEN-LAST:event_inputUsuersMousePressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        inputUsuers.removeAll();
        inputUsuers.setText(null);
        textAutoCompleter.removeAllItems();
        dispose();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void inputUsuersInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_inputUsuersInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_inputUsuersInputMethodTextChanged

    private void inputUsuersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputUsuersKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_inputUsuersKeyReleased

    private void inputUsuersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputUsuersKeyPressed
        // TODO add your handling code here:
        String text = inputUsuers.getText().trim();
        if(text != null && text.length() == 2){
            loadUsersLDAP(text);
        }
    }//GEN-LAST:event_inputUsuersKeyPressed

    public usuarioADBO returnValue() {
        if (!inputUsuers.getText().isEmpty()) {
            if(selectedValue == null || !inputUsuers.getText().equals(selectedValue.getNombre()) ){
                selectedValue = getUserByNameLDAP(inputUsuers.getText());
            }
            
            return selectedValue;
        } else {
            return null;
        }
    }

    private void listenerData() {
        Object o = textAutoCompleter.getItemSelected();
        if (o != null && o instanceof usuarioADBO) {
            selectedValue = (usuarioADBO) o;
        }
    }

    private void loadUsersLDAP() {

        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuarioAD arrayu = stub.busquedaPorNombreLDAP(" ");
            UsuarioAD[] usr = arrayu.getUsuarioAD();

            if (usr == null) {
                return;
            }

            for (UsuarioAD usuarioAD : usr) {
                textAutoCompleter.addItem(new usuarioADBO(usuarioAD.getCorreo(), usuarioAD.getExtension(), usuarioAD.getNombre(), usuarioAD.getUsuarioNT()));
            }
            // textAutoCompleter.addItems(list);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }

    }
    
    private void loadUsersLDAP(String text) {

        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuarioAD arrayu = stub.busquedaPorNombreLDAP(text);
            UsuarioAD[] usr = arrayu.getUsuarioAD();
            textAutoCompleter = new TextAutoCompleter(inputUsuers);
            if (usr == null) {
                return;
            }

            for (UsuarioAD usuarioAD : usr) {
                textAutoCompleter.addItem(new usuarioADBO(usuarioAD.getCorreo(), usuarioAD.getExtension(), usuarioAD.getNombre(), usuarioAD.getUsuarioNT()));
            }
            // textAutoCompleter.addItems(list);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }

    }
    
    private usuarioADBO getUserByNameLDAP(String name){
        usuarioADBO retVal = null;
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuarioAD arrayu = stub.busquedaPorNombreLDAP(name);
            UsuarioAD[] usr = arrayu.getUsuarioAD();

            if (usr == null || usr.length <= 0) {
                return null;
            }

            UsuarioAD usuarioAD = usr[0];
            retVal = new usuarioADBO(usuarioAD.getCorreo(), usuarioAD.getExtension(), usuarioAD.getNombre(), usuarioAD.getUsuarioNT());
            
            // textAutoCompleter.addItems(list);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }
        
        return retVal;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField inputUsuers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
   private TextAutoCompleter textAutoCompleter;

}
