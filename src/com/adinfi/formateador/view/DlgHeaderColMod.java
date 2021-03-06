/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.HeaderColModDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.Utilerias;
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author ADINFI
 */
public class DlgHeaderColMod extends javax.swing.JDialog {

    /**
     * Creates new form DlgHeaderColMod
     */
    public DlgHeaderColMod(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillCombos();
    }
    
    public void setFields(int mercadoId , int tipoDocumento){
        if( mercadoId <=0 || tipoDocumento<=0 ){
            return;
        }
        
        if( cmbMarket.getItemCount()<=0 ){
            return;
        }
        
        MarketBO market=null;
        DocumentTypeBO type=null;
        for( int i=0;i<cmbMarket.getItemCount();i++ ){
           market=(MarketBO) cmbMarket.getItemAt(i);
           if( market.getIdMarket()==mercadoId ){
               cmbMarket.setSelectedIndex(i);
               this.fillComboDoc();
               if( this.cmbDocType.getItemCount()>0 ){
                 for( int j=0;j<cmbDocType.getItemCount();j++ )  {
                     type=(DocumentTypeBO) cmbDocType.getItemAt(j);
                     if(type.getIddocument_type()==tipoDocumento){
                         cmbDocType.setSelectedIndex(j);
                         break;
                     }
                     
                 }
                                                         
               }
               break;
               
               
           }
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

        lblMarket = new javax.swing.JLabel();
        cmbMarket = new javax.swing.JComboBox();
        lblDocType = new javax.swing.JLabel();
        cmbDocType = new javax.swing.JComboBox();
        lblNameHeader = new javax.swing.JLabel();
        txtNameHeader = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Encabezado");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblMarket.setText("Mercado:");

        cmbMarket.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMarketItemStateChanged(evt);
            }
        });

        lblDocType.setText("Tipo de Documento:");

        cmbDocType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDocTypeItemStateChanged(evt);
            }
        });

        lblNameHeader.setText("Nombre del Encabezado:");

        btnNew.setText("Agregar");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMarket)
                            .addComponent(cmbMarket, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDocType)
                            .addComponent(cmbDocType, 0, 309, Short.MAX_VALUE)
                            .addComponent(lblNameHeader)
                            .addComponent(txtNameHeader)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnNew)
                        .addGap(88, 88, 88)
                        .addComponent(btnCancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMarket)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMarket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDocType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDocType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNameHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew)
                    .addComponent(btnCancel))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        if (validateForm()){
            save();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbDocTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDocTypeItemStateChanged
        
    }//GEN-LAST:event_cmbDocTypeItemStateChanged

    private void cmbMarketItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMarketItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fillComboDoc();
        }
    }//GEN-LAST:event_cmbMarketItemStateChanged
    
    private boolean validateForm() {
        Object mercado = cmbMarket.getSelectedItem();
        if (mercado == null || ((MarketBO)mercado).getIdMarket() < 0){
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un mercado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        Object docType = cmbDocType.getSelectedItem();
        if (docType == null || ((DocumentTypeBO)docType).getIddocument_type() < 0){
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un tipo de documento", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (txtNameHeader == null || txtNameHeader.getText().trim().isEmpty()) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Ingrese un nombre para el encabezado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        HeaderColModDAO hcmDAO = new HeaderColModDAO();
        if (!hcmDAO.validateName(((MarketBO)mercado).getIdMarket(), ((DocumentTypeBO)docType).getIddocument_type(), txtNameHeader.getText().trim())){
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre para el encabezado ya existe", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void save() {
        Object mObj = cmbMarket.getSelectedItem();
        Object dObj = cmbDocType.getSelectedItem();
        
        HeaderColModBO hcmBO = new HeaderColModBO();
        hcmBO.setIdMarket(Integer.parseInt(((MarketBO)mObj).getIdMiVector_real()));
        hcmBO.setIdDocumentType(((DocumentTypeBO)dObj).getIddocument_type());
        hcmBO.setName(txtNameHeader.getText());
        try{
            HeaderColModDAO hcmDAO = new HeaderColModDAO();
            hcmDAO.insert(hcmBO);
            dispose();
        } catch(Exception e) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Ocurrio un error al agregar encabezado", JOptionPane.ERROR_MESSAGE);
        }  
    }
    
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
            java.util.logging.Logger.getLogger(DlgHeaderColMod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgHeaderColMod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgHeaderColMod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgHeaderColMod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgHeaderColMod dialog = new DlgHeaderColMod(new javax.swing.JFrame(), true);
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
    
    private void fillCombos() {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO marketBO = new MarketBO();
        
        marketBO.setName("Seleccione");
        marketBO.setIdMiVector_real("-1");
        cmbMarket.addItem(marketBO);
        for (MarketBO mBO : list) {
            mBO.setIdMarket(Integer.parseInt(mBO.getIdMiVector_real()));
            cmbMarket.addItem(mBO);
        }
    }
    
    private void fillComboDoc() {
        Object objMrkt = cmbMarket.getSelectedItem();
        
        
        if (objMrkt != null && ((MarketBO)objMrkt).getIdMarket() > -1) {
            DocumentTypeDAO daoDoc = new DocumentTypeDAO();
            List<DocumentTypeBO> listDoc = daoDoc.getDocTypeByMarket(1, Integer.parseInt(((MarketBO)objMrkt).getIdMiVector_real()));
            
            cmbDocType.removeAllItems();
            
            DocumentTypeBO boDoc = new DocumentTypeBO();
            boDoc.setName("Seleccione");
            boDoc.setIddocument_type(-1);
            cmbDocType.addItem(boDoc);
            
            for (DocumentTypeBO boDoc2 : listDoc) {
                cmbDocType.addItem(boDoc2);
            }
            cmbDocType.setSelectedItem(boDoc);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox cmbDocType;
    private javax.swing.JComboBox cmbMarket;
    private javax.swing.JLabel lblDocType;
    private javax.swing.JLabel lblMarket;
    private javax.swing.JLabel lblNameHeader;
    private javax.swing.JTextField txtNameHeader;
    // End of variables declaration//GEN-END:variables
}
