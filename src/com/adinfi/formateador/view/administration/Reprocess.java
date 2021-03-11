/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.DocumentFormatBO;
import com.adinfi.formateador.dao.SendProgressDAO;
import com.adinfi.formateador.util.JsonServicePublicationProgress;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.SendProgressModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author vectoran
 */
public class Reprocess extends javax.swing.JDialog {

    /**
     * Creates new form Reprocess
     */
    public Reprocess(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillDocuemnts();
        this.setTitle("Reenvió a progress");
    }
    
    private void fillDocuemnts(){
        SendProgressDAO sdao = new SendProgressDAO();
        List<DocumentFormatBO> list = sdao.get(0);
        SendProgressModel spModel = new SendProgressModel(list);
        tblEnvios.setModel(spModel);
        fitColumns(tblEnvios);
    }
    
    private void fitColumns(JTable table) {
        /* Ajustar columnas de la tabla */
        Utilerias.adjustJTableRowSizes(table);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Utilerias.adjustColumnSizes(table, i, 4);
        }
        
        //Not to display colums id,fatemodify,status and optional phones
        table.removeColumn(table.getColumnModel().getColumn(1));

        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        table.setDefaultRenderer(Object.class, stcr);
        
    }
    
    private void reprocesar(){
    
        try {
            
            SendProgressModel model = (SendProgressModel) tblEnvios.getModel();
            Object[][] data = model.getData();
            
            List<DocumentFormatBO> selChecks = new ArrayList<>();
            SendProgressDAO sdao = new SendProgressDAO();
            
            for (int row = 0; row < data.length; row++) {
                if( (boolean) data[row][0] ){
                    
                    int idEnvio = (int) data[row][1];
                    List<DocumentFormatBO> sp = sdao.get(idEnvio);
                    
                    if(sp != null && sp .size() > 0 ){
                        selChecks.add(sp.get(0));
                    }
                }
            }
            
            if(selChecks == null || selChecks.size() < 0){
                JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un Documento para procesar.");
                return;
            }
            
            JsonServicePublicationProgress serviceProgress = new JsonServicePublicationProgress(true);
            for (DocumentFormatBO item : selChecks) {
                
                String s = serviceProgress.getPublicationProgressResponse("nuevo", item, item.getArregloAutores(), item.getArregloDistribucion());
                Utilerias.logger(getClass()).info(s);
                
            }
            
            fillDocuemnts();
            
            JOptionPane.showMessageDialog(null, "Reproceso de Documentos Terminado.");
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            fillDocuemnts();
            JOptionPane.showMessageDialog(null, "Ocurrio un error al procesar los Documentos.");
        }
        
    }

    private void eliminar(){
    
        try {
            
            SendProgressModel model = (SendProgressModel) tblEnvios.getModel();
            Object[][] data = model.getData();
            
            List<DocumentFormatBO> selChecks = new ArrayList<>();
            
            for (int row = 0; row < data.length; row++) {
                if( (boolean) data[row][0] ){
                    DocumentFormatBO df = new DocumentFormatBO();
                    int idEnvio = (int) data[row][1];
                    df.setIdDocument_send(idEnvio);
                    selChecks.add(df);
                }
            }
            
            if(selChecks == null || selChecks.size() < 0){
                JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un Documento para eliminar.");
                return;
            } else {
            
                JsonServicePublicationProgress serviceProgress = new JsonServicePublicationProgress(false);
                
                for (DocumentFormatBO item : selChecks) {

                    serviceProgress.eliminarPublicacionTmp( item.getIdDocument_send() );

                }
                
                fillDocuemnts();
            
                JOptionPane.showMessageDialog(null, "Se eliminaron satisfactoriamente los Documentos seleccionados.");
            
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            fillDocuemnts();
            JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar los Documentos.");
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
        tblEnvios = new javax.swing.JTable();
        btnReprocesar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tblEnvios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Fecha", "Mercado", "Tipo de Documento", "Titulo", "Sector", "Autor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblEnvios.setName("tblEnvios"); // NOI18N
        jScrollPane1.setViewportView(tblEnvios);
        if (tblEnvios.getColumnModel().getColumnCount() > 0) {
            tblEnvios.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        btnReprocesar.setText("Reprocesar");
        btnReprocesar.setName("btnReprocesar"); // NOI18N
        btnReprocesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprocesarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.setName("btnEliminar"); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(263, 263, 263)
                .addComponent(btnReprocesar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReprocesar)
                    .addComponent(btnEliminar))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReprocesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprocesarActionPerformed
        reprocesar();
    }//GEN-LAST:event_btnReprocesarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(Reprocess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reprocess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reprocess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reprocess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Reprocess dialog = new Reprocess(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnReprocesar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEnvios;
    // End of variables declaration//GEN-END:variables
}
