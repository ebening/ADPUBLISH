/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.util.Utilerias;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Josue Sanchez
 */
public class DocumentTypeTitle extends javax.swing.JDialog {

    /**
     * Creates new form DocumentTypeTitle
     */
    private int currentLenght;

    public enum FieldsConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TEMA("1", "@TEMA", 30),
        TITULO("2", "@TITULO", 120),
        TIPODOCUMENTO("3", "@TIPO_DE_DOCUMENTO", 40),
        MERCADO("4", "@MERCADO", 25),
        NOMENCLATURA("5", "@NOMENCLATURA", 3);

        FieldsConstants(final String value, final String caption, final int maxValue) {
            this.value = value;
            this.caption = caption;
            this.maxValue = maxValue;
        }

        private final String value;
        private final String caption;
        private final int maxValue;

        /* Metodo sobre escrito para obetener el valor de las constantes.
         * @see java.lang.String toString()
         */
        @Override
        public String toString() {
            return value;
        }

        public String getCaption() {
            return caption;
        }

        public int getMaxValue() {
            return maxValue;

        }
    }

    public DocumentTypeTitle(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        btnTexto.setVisible(false);

        titleArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                chkTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                chkTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                chkTitle();
            }
        });

    }

    public String returRegEx() {
        return titleArea.getText();
    }

    public void setRegEx(String regex) {
        this.titleArea.setText(regex);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        temaInput = new javax.swing.JButton();
        btnTitulo5 = new javax.swing.JButton();
        btnTipoDoc = new javax.swing.JButton();
        btnMercado = new javax.swing.JButton();
        btnNomenclatura = new javax.swing.JButton();
        btnTexto = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        display = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        titleArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Título");

        jPanel1.setName("jPanel1"); // NOI18N

        jButton2.setText("Aceptar");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton1.setText("Cancelar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 1, 2));

        temaInput.setText("Tema (30)");
        temaInput.setName("temaInput"); // NOI18N
        temaInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temaInputActionPerformed(evt);
            }
        });
        jPanel2.add(temaInput);

        btnTitulo5.setText("Título (120)");
        btnTitulo5.setName("btnTitulo5"); // NOI18N
        btnTitulo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTitulo5ActionPerformed(evt);
            }
        });
        jPanel2.add(btnTitulo5);

        btnTipoDoc.setText("Tipo de Documento (40)");
        btnTipoDoc.setName("btnTipoDoc"); // NOI18N
        btnTipoDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoDocActionPerformed(evt);
            }
        });
        jPanel2.add(btnTipoDoc);

        btnMercado.setText("Mercado (25)");
        btnMercado.setName("btnMercado"); // NOI18N
        btnMercado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMercadoActionPerformed(evt);
            }
        });
        jPanel2.add(btnMercado);

        btnNomenclatura.setText("Nomenclatura (3)");
        btnNomenclatura.setName("btnNomenclatura"); // NOI18N
        btnNomenclatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNomenclaturaActionPerformed(evt);
            }
        });
        jPanel2.add(btnNomenclatura);

        btnTexto.setText("Texto");
        btnTexto.setName("btnTexto"); // NOI18N
        btnTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTextoActionPerformed(evt);
            }
        });
        jPanel2.add(btnTexto);

        jButton3.setText("Limpiar");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        display.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        display.setName("display"); // NOI18N
        jPanel2.add(display);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        titleArea.setColumns(20);
        titleArea.setLineWrap(true);
        titleArea.setRows(5);
        titleArea.setWrapStyleWord(true);
        titleArea.setName("titleArea"); // NOI18N
        jScrollPane1.setViewportView(titleArea);

        jPanel3.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (currentLenght > 204) {
            Utilerias.showMessage(null, "El titulo del documento debe ser menor o igual a 204 caracteres", 0);
            titleArea.setText(titleArea.getText().substring(0, 204));
        } else {
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void temaInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temaInputActionPerformed
        titleArea.setText(titleArea.getText() + " " + FieldsConstants.TEMA.getCaption());
    }//GEN-LAST:event_temaInputActionPerformed

    private void btnTitulo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTitulo5ActionPerformed
        titleArea.setText(titleArea.getText() + " " + FieldsConstants.TITULO.getCaption());
    }//GEN-LAST:event_btnTitulo5ActionPerformed

    private void btnTipoDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoDocActionPerformed
        titleArea.setText(titleArea.getText() + " " + FieldsConstants.TIPODOCUMENTO.getCaption());
    }//GEN-LAST:event_btnTipoDocActionPerformed

    private void btnMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMercadoActionPerformed
        titleArea.setText(titleArea.getText() + " " + FieldsConstants.MERCADO.getCaption());
    }//GEN-LAST:event_btnMercadoActionPerformed

    private void btnNomenclaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNomenclaturaActionPerformed
        titleArea.setText(titleArea.getText() + " " + FieldsConstants.NOMENCLATURA.getCaption());
    }//GEN-LAST:event_btnNomenclaturaActionPerformed

    private void btnTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTextoActionPerformed
        titleArea.setText(titleArea.getText() + "+' '+");

    }//GEN-LAST:event_btnTextoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        titleArea.setText(null);
        currentLenght = 0;
        display.setText(null);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void chkTitle() {
        String[] txt;
        currentLenght = 0;


        int cnt = StringUtils.countMatches(titleArea.getText(), " ");
        // si es un caracter blanco
        currentLenght = currentLenght + cnt;

        if (titleArea.getText().toUpperCase().contains("@TEMA")) {
            if (cnt == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        titleArea.setText(titleArea.getText().replace("@TEMA", "@TEMA "));
                    }
                });
            }
        }
        if (titleArea.getText().toUpperCase().contains("@MERCADO")) {
            if (cnt == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        titleArea.setText(titleArea.getText().replace("@MERCADO", "@MERCADO "));
                    }
                });
            }
        }
        if (titleArea.getText().toUpperCase().contains("@TIPO_DE_DOCUMENTO")) {
            if (cnt == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        titleArea.setText(titleArea.getText().replace("@TIPO_DE_DOCUMENTO", "@TIPO_DE_DOCUMENTO "));
                    }
                });
            }
        }
        if (titleArea.getText().toUpperCase().contains("@TITULO")) {
            if (cnt == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        titleArea.setText(titleArea.getText().replace("@TITULO", "@TITULO "));
                    }
                });
            }
        }
        if (titleArea.getText().toUpperCase().contains("@NOMENCLATURA")) {
            if (cnt == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        titleArea.setText(titleArea.getText().replace("@NOMENCLATURA", "@NOMENCLATURA "));
                    }
                });
            }
        }

// sino es blanco contamos 
        txt = titleArea.getText().split(" ");
        for (String t : txt) {

            System.out.println(t);

            switch (t) {
                case ("@TEMA"):
                    System.out.println(t);
                    currentLenght = currentLenght + FieldsConstants.TEMA.getMaxValue();
                    break;
                case ("@MERCADO"):
                    System.out.println(t);
                    currentLenght = currentLenght + FieldsConstants.MERCADO.getMaxValue();
                    break;
                case ("@TIPO_DE_DOCUMENTO"):
                    System.out.println(t);
                    currentLenght = currentLenght + FieldsConstants.TIPODOCUMENTO.getMaxValue();
                    break;
                case ("@TITULO"):
                    System.out.println(t);
                    currentLenght = currentLenght + FieldsConstants.TITULO.getMaxValue();
                    break;
                case ("@NOMENCLATURA"):
                    System.out.println(t);
                    currentLenght = currentLenght + FieldsConstants.NOMENCLATURA.getMaxValue();
                    break;
                default:
                    if (t.isEmpty() == false && t != null) {
                        System.out.println(t);
                        currentLenght = currentLenght + t.length();
                    }
                    break;
            }

        }

        display.setText("Caracteres actuales: " + String.valueOf(currentLenght));
        if (currentLenght > 204) {
            display.setForeground(Color.RED);
        } else {
            display.setForeground(Color.GREEN);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMercado;
    private javax.swing.JButton btnNomenclatura;
    private javax.swing.JButton btnTexto;
    private javax.swing.JButton btnTipoDoc;
    private javax.swing.JButton btnTitulo5;
    private javax.swing.JLabel display;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton temaInput;
    private javax.swing.JTextArea titleArea;
    // End of variables declaration//GEN-END:variables
}
