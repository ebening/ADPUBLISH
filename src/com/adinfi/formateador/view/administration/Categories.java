/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.CatalogExcelBOCat;
import com.adinfi.formateador.dao.ExcelDAOCat;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Categories extends javax.swing.JDialog {

    /**
     * Creates new form Categorias
     */
    public Categories(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    private void saveCategory() {

        String cat = inputCat.getText();
        
        if (cat == null || cat.isEmpty() == true || cat.trim().isEmpty() == true) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una categor?a.");
            return;
        }
        
        CatalogExcelBOCat bo = new CatalogExcelBOCat();
        bo.setCategory(cat);

        if (!validateName(bo)) {
            ExcelDAOCat dao = new ExcelDAOCat();
            int response = dao.insertCatalogExcel(bo);
            if (response > 0) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar categor?a");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de la Categor?a ya existe, ingrese un nombre diferente.");
        }
    }

    private boolean validateName(CatalogExcelBOCat bo) {
       ExcelDAOCat dao = new ExcelDAOCat();
       return dao.checkRepeat(bo);
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
        inputCat = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Categor?a");

        jLabel1.setText("Categor?a");
        jLabel1.setName("jLabel1"); // NOI18N

        inputCat.setName("inputCat"); // NOI18N

        jToggleButton1.setText("Guardar");
        jToggleButton1.setName("jToggleButton1"); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setText("Cancelar");
        jToggleButton2.setName("jToggleButton2"); // NOI18N
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputCat))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 131, Short.MAX_VALUE)
                        .addComponent(jToggleButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(inputCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        saveCategory();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jToggleButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inputCat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    // End of variables declaration//GEN-END:variables
}
