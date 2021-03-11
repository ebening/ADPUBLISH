package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.Item;
import com.adinfi.formateador.bos.ReturnSelectedTemplates;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.PanelPlantillasTodas;
import com.adinfi.formateador.view.PanelThumbTodos;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class SelectTemplate extends javax.swing.JDialog {

    private PanelPlantillasTodas panelPlantillasTodas = null;

    public SelectTemplate(java.awt.Frame parent, boolean modal, List<Integer> selectedLst, List<DocumentTypeBO> typesList) {
        super(parent, modal);
        initComponents();
        intiPanelPlantillasTodas(selectedLst, typesList);
    }

    public SelectTemplate(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        intiPanelPlantillasTodas();
    }

    protected void intiPanelPlantillasTodas() {
        try {
            panelPlantillasTodas = new PanelPlantillasTodas();
            centerPanel.add(panelPlantillasTodas, BorderLayout.CENTER);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    protected void intiPanelPlantillasTodas(List<Integer> selectedLst, List<DocumentTypeBO> typesList) {
        try {
            panelPlantillasTodas = new PanelPlantillasTodas(selectedLst, typesList);
            centerPanel.add(panelPlantillasTodas, BorderLayout.CENTER);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        centerPanel = new javax.swing.JPanel();
        southPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleccionar Plantilla");
        setMinimumSize(new java.awt.Dimension(300, 400));

        centerPanel.setMinimumSize(new java.awt.Dimension(600, 140));
        centerPanel.setName("centerPanel"); // NOI18N
        centerPanel.setPreferredSize(new java.awt.Dimension(600, 140));
        centerPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        southPanel.setName("southPanel"); // NOI18N

        jButton1.setText("Guardar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        southPanel.add(jButton1);

        jButton2.setText("Cancelar");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        southPanel.add(jButton2);

        getContentPane().add(southPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public ReturnSelectedTemplates getSelectedTemplates() {
        List<PanelThumbTodos> panelThumbTodoses = panelPlantillasTodas.getPanelThumbTodoses();
        ReturnSelectedTemplates rst = new ReturnSelectedTemplates();
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < panelThumbTodoses.size(); i++) {
            if (panelThumbTodoses.get(i).isChecked() == true) {
                Item item = new Item(panelThumbTodoses.get(i).getId(), panelThumbTodoses.get(i).getModuleName());
                itemList.add(item);
                rst.setValue(item.getValue());
            }
        }
        rst.setItems(itemList);
        return rst;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel southPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the panelPlantillasTodas
     */
    public PanelPlantillasTodas getPanelPlantillasTodas() {
        return panelPlantillasTodas;
    }

    /**
     * @param panelPlantillasTodas the panelPlantillasTodas to set
     */
    public void setPanelPlantillasTodas(PanelPlantillasTodas panelPlantillasTodas) {
        this.panelPlantillasTodas = panelPlantillasTodas;
    }

}
