package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.seguridad.Perfil;
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
 * @author Josue Sanchez
 */
public class editProfile extends javax.swing.JDialog {

    private int idProfile = -1;

    public editProfile(java.awt.Frame parent, boolean modal, Perfil perfil) {
        super(parent, modal);

        initComponents();
        inputProfileName.setText(perfil.getNombre());
        inputProfileDesc.setText(perfil.getDescripcion());
        chkisAdministrable.setSelected(perfil.isIsAdministrable());
        chkisVisible.setSelected(perfil.isIsVisible());
        //int idProfile = perfil.getPerfilId();
        this.idProfile = perfil.getPerfilId();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        inputProfileName = new javax.swing.JTextField();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        chkisAdministrable = new javax.swing.JCheckBox();
        chkisVisible = new javax.swing.JCheckBox();
        inputProfileDesc = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Perfil");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Nombre del Perfil");
        jLabel1.setName("jLabel1"); // NOI18N

        inputProfileName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputProfileName.setName("inputProfileName"); // NOI18N

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N
        jLayeredPane1.setLayout(new java.awt.FlowLayout());

        jButton1.setText("Aceptar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jLayeredPane1.add(jButton1);

        jButton2.setText("Cancelar");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jLayeredPane1.add(jButton2);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Descripción");
        jLabel2.setName("jLabel2"); // NOI18N

        chkisAdministrable.setSelected(true);
        chkisAdministrable.setText("Administrable");
        chkisAdministrable.setEnabled(false);
        chkisAdministrable.setName("chkisAdministrable"); // NOI18N

        chkisVisible.setSelected(true);
        chkisVisible.setText("Visible");
        chkisVisible.setEnabled(false);
        chkisVisible.setName("chkisVisible"); // NOI18N

        inputProfileDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputProfileDesc.setEnabled(false);
        inputProfileDesc.setName("inputProfileDesc"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(inputProfileName, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkisAdministrable)
                            .addComponent(chkisVisible))
                        .addGap(134, 134, 134))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputProfileDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(inputProfileDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(chkisAdministrable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkisVisible)
                .addGap(27, 27, 27)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        idProfile = -1;
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        editProfile();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void editProfile() {

        int idProfile = this.idProfile;
        String name = inputProfileName.getText();
        String description = inputProfileDesc.getText();
        boolean isAdministrable = chkisAdministrable.isSelected();
        boolean isVisible = chkisVisible.isVisible();
        int usuarioModId = InstanceContext.getInstance().getUsuario().getUsuarioId();
        String IP = Utilerias.getIP();
        String comentario = " ";
        Response res = null;

        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            res = stub.modificarPerfil(GlobalDefines.WS_INSTANCE,
                    idProfile,
                    name,
                    description,
                    isAdministrable,
                    isVisible,
                    usuarioModId,
                    IP,
                    comentario);

        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de modificar perfil no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }

        if (res.getMensajeCd().equals("999")) {
            JOptionPane.showMessageDialog(this, res.getMensaje());
        }

        if (res.getMensajeCd().equals("000")) {
            JOptionPane.showMessageDialog(this, "Se guardó correctamente");
            this.dispose();
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkisAdministrable;
    private javax.swing.JCheckBox chkisVisible;
    private javax.swing.JTextField inputProfileDesc;
    private javax.swing.JTextField inputProfileName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the idProfile
     */
    public int getIdProfile() {
        return idProfile;
    }

    /**
     * @param idProfile the idProfile to set
     */
    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }
}
