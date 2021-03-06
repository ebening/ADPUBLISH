/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.util.Utilerias;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Desarrollador
 */
public class PanelModParent extends javax.swing.JPanel {
    ScrDocumentMod scrDoc;
    ModuleBO module;
    
    /**
     * Creates new form PanelModParent
     */
    public PanelModParent(ScrDocumentMod scrDoc , ModuleBO module   ) {
        initComponents();
        this.scrDoc=scrDoc;
        this.module=module;
        
    }
    
    JPanel getContent(){
        return this.content;
    }
    
    protected void removeModule(){
       // scrDoc.deleteModule(module.getRootSectionId());
         //scrDoc.deleteModule(module.getModuleId() , module.getDocumentModuleId() );
         scrDoc.deleteModule(module);
         if (module.getDocumentModuleId() > 0) {
             if (this.scrDoc.scrIntMod.getlIdDocModule() == null) { 
                 this.scrDoc.scrIntMod.setlIdDocModule(new ArrayList<>());
             }
             
             this.scrDoc.scrIntMod.getlIdDocModule().add(module.getDocumentModuleId());
         }
         
         //CollaborativesDAO collabDAO=new CollaborativesDAO();
         //collabDAO.restoreModuleCollabCandLog(module.getDocumentModuleId());
        // this.scrDoc.scrIntMod.loadCandidateDocs();
         
    }

    protected void moveUp(){
        scrDoc.moveUp(module.getRootSection().getSectionId(), module.getDocumentModuleId() + module.getIdHeader() + module.getIdSection());
    }
    
    protected void moveDown(){
        scrDoc.moveDown(module.getRootSection().getSectionId(), module.getDocumentModuleId() + module.getIdHeader() + module.getIdSection());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        content = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(400, 0));
        setLayout(new java.awt.BorderLayout());

        content.setPreferredSize(new java.awt.Dimension(200, 400));
        content.setLayout(new java.awt.BorderLayout());
        add(content, java.awt.BorderLayout.CENTER);

        jPanel10.setMaximumSize(new java.awt.Dimension(45, 32767));
        jPanel10.setPreferredSize(new java.awt.Dimension(45, 302));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/left_back_arrow_blue.png"))); // NOI18N
        btnRemove.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jPanel10.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 40, 40));

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/up_back_arrow_blue.png"))); // NOI18N
        btnUp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });
        jPanel10.add(btnUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 40, 40));

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/down_back_arrow_blue.png"))); // NOI18N
        btnDown.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });
        jPanel10.add(btnDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 40, 40));

        add(jPanel10, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        removeModule();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        moveUp();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        moveDown();
    }//GEN-LAST:event_btnDownActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JPanel content;
    private javax.swing.JPanel jPanel10;
    // End of variables declaration//GEN-END:variables
}
