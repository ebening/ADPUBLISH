package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Josue Sanchez
 */
public class AddSubjectModal extends javax.swing.JDialog {

    public AddSubjectModal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initIndustryCbo();
    }

    private void initIndustryCbo() {
        IndustryDAO dao = new IndustryDAO();
        List<IndustryBO> list = dao.get(null);
        IndustryBO bo = new IndustryBO();
        bo.setIdIndustry(-1);
        bo.setName("Seleccione");
        cboIndustry.addItem(bo);
        for (IndustryBO b : list) {
            cboIndustry.addItem(b);
        }

    }

    private void saveSubject() {

        int idinsert = 0;

        boolean b = true;

        StringBuilder fields = new StringBuilder();

        String subject = inputSubject.getText();
        SubjectBO bo = new SubjectBO();

        if (subject.isEmpty()) {
            b = false;
            fields.append("\nNombre del tema");
          
        } else {
            if (Utilerias.isRepeated("SUBJECT", "NAME", subject, "IDSUBJECT", bo.getIdSubject())) {
               JOptionPane.showMessageDialog(null, "El nombre del tema ya existe en la base de datos");
                return;
            } else {
                bo.setName(subject);
            }

        }

        if (isIssuing.isSelected()) {

            bo.setIssuing(true);

            Object obj = cboIndustry.getSelectedItem();
            IndustryBO bo2 = (IndustryBO) obj;
            if (bo2.getIdIndustry() == -1) {
                b = false;
                fields.append("\nSe debe seleccionar un sector");
            } else {
                bo.setIndustry(bo2.getIdIndustry());
            }
            bo.setIndustry(bo.getIndustry());
        } else {
            bo.setIssuing(false);
        }

        if (b) {
            SubjectDAO dao = new SubjectDAO();
            idinsert = dao.insertUpdate(bo);
        } else {
            JOptionPane.showMessageDialog(this, "Complete " + fields);
        }

        if (idinsert > 0) {
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboIndustry = new javax.swing.JComboBox();
        inputSubject = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnSaveSubject = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        isIssuing = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Tema");

        cboIndustry.setEnabled(false);
        cboIndustry.setName("cboIndustry"); // NOI18N

        inputSubject.setName("inputSubject"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        btnSaveSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveSubject.setText("Agregar");
        btnSaveSubject.setName("btnSaveSubject"); // NOI18N
        btnSaveSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSubjectActionPerformed(evt);
            }
        });
        jPanel1.add(btnSaveSubject);

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancel);

        jLabel1.setText("Nombre");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Sector");
        jLabel2.setName("jLabel2"); // NOI18N

        isIssuing.setText("Emisora");
        isIssuing.setName("isIssuing"); // NOI18N
        isIssuing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isIssuingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputSubject)
                    .addComponent(cboIndustry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(isIssuing))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(isIssuing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSubjectActionPerformed
        saveSubject();
    }//GEN-LAST:event_btnSaveSubjectActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void isIssuingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isIssuingActionPerformed
        if (isIssuing.isSelected()) {
            cboIndustry.setEnabled(true);
        } else {
            cboIndustry.setEnabled(false);
        }
    }//GEN-LAST:event_isIssuingActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSaveSubject;
    private javax.swing.JComboBox cboIndustry;
    private javax.swing.JTextField inputSubject;
    private javax.swing.JCheckBox isIssuing;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
