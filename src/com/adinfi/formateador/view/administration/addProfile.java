package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Response;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class addProfile extends javax.swing.JDialog {

    /**
     * Creates new form addProfile
     * @param parent
     * @param modal
     */
    public addProfile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        inputProfileName = new javax.swing.JTextField();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        bttnAceptar = new javax.swing.JButton();
        bttnCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        chkisAdministrable = new javax.swing.JCheckBox();
        chkisVisible = new javax.swing.JCheckBox();
        inputProfileDesc1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Perfil");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Nombre del Perfil*");
        jLabel1.setName("jLabel1"); // NOI18N

        inputProfileName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputProfileName.setName("inputProfileName"); // NOI18N

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N
        jLayeredPane1.setLayout(new java.awt.FlowLayout());

        bttnAceptar.setText("Aceptar");
        bttnAceptar.setName("bttnAceptar"); // NOI18N
        bttnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnAceptarActionPerformed(evt);
            }
        });
        jLayeredPane1.add(bttnAceptar);

        bttnCancelar.setText("Cancelar");
        bttnCancelar.setName("bttnCancelar"); // NOI18N
        bttnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnCancelarActionPerformed(evt);
            }
        });
        jLayeredPane1.add(bttnCancelar);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Descripci?n*");
        jLabel2.setName("jLabel2"); // NOI18N

        chkisAdministrable.setSelected(true);
        chkisAdministrable.setText("Administrable");
        chkisAdministrable.setEnabled(false);
        chkisAdministrable.setName("chkisAdministrable"); // NOI18N

        chkisVisible.setSelected(true);
        chkisVisible.setText("Visible");
        chkisVisible.setEnabled(false);
        chkisVisible.setName("chkisVisible"); // NOI18N

        inputProfileDesc1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputProfileDesc1.setName("inputProfileDesc1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkisAdministrable)
                                    .addComponent(chkisVisible))
                                .addGap(134, 134, 134))
                            .addComponent(inputProfileDesc1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addComponent(inputProfileName, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLayeredPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(inputProfileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(inputProfileDesc1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(chkisAdministrable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkisVisible)
                .addGap(27, 27, 27)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bttnCancelarActionPerformed

    private void bttnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnAceptarActionPerformed
        String datosObligatorios="";
        String desc = " ";
        if(!inputProfileDesc1.getText().isEmpty()){
            desc = inputProfileDesc1.getText();
        }else{
            datosObligatorios ="\nDescripci?n";
        } 
        
        if (!inputProfileName.getText().isEmpty() && datosObligatorios.isEmpty()) {
            try {
                String ip = Utilerias.getIP();
                int userID = InstanceContext.getInstance().getUsuario().getUsuarioId();
                IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                UtileriasWS.setEndpoint(stub);
                Response res = stub.crearPerfil(
                        GlobalDefines.WS_INSTANCE,
                        inputProfileName.getText(),
                        desc,
                        chkisAdministrable.isSelected(),
                        chkisVisible.isSelected(),
                        userID,
                        Utilerias.getIP(),
                        " ");

                
                switch (res.getMensajeCd()) {
                    case "999":
                        JOptionPane.showMessageDialog(this,res.getMensaje());
                        break;
                    case "000":
                        JOptionPane.showMessageDialog(this,"Se guard? correctamente");
                        this.dispose();
                        break;
                    default:
                        JOptionPane.showMessageDialog(this,"No se recibio informacion v?lida");
                        break;
                }
                
                
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "El servicio de crear perfil no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(e);
            }
        }else{
            if (inputProfileName.getText().isEmpty())
                datosObligatorios += "\nNombre del Perfil";
            JOptionPane.showMessageDialog(this,"Complete campos requeridos*"+datosObligatorios);
        }
    }//GEN-LAST:event_bttnAceptarActionPerformed

 
    
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
            java.util.logging.Logger.getLogger(addProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            addProfile dialog = new addProfile(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttnAceptar;
    private javax.swing.JButton bttnCancelar;
    private javax.swing.JCheckBox chkisAdministrable;
    private javax.swing.JCheckBox chkisVisible;
    private javax.swing.JTextField inputProfileDesc1;
    private javax.swing.JTextField inputProfileName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables
}

