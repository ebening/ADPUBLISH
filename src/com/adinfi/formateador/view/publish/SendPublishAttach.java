package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.view.publish.*;
import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.PublishStatusBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.PublishStatusDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.Utilerias;
import com.hexidec.ekit.EkitCore;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Josue Sanchez
 */
public class SendPublishAttach extends javax.swing.JDialog {
    
    public SendPublishAttach(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        addTextEditor();

        /* botones para enviar inmediatamente o programar envio */
        groupEnviar.setSelected(true);
        buttonGroup1.add(groupEnviar);
        buttonGroup1.add(groupProgramar);

        setProgramerInputs();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cboMarket = new javax.swing.JComboBox();
        cboIndustry = new javax.swing.JComboBox();
        cboDocumentType = new javax.swing.JComboBox();
        cboSubject = new javax.swing.JComboBox();
        cboStatus = new javax.swing.JComboBox();
        cboLanguage = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        groupEnviar = new javax.swing.JRadioButton();
        groupProgramar = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        inputDate = new javax.swing.JFormattedTextField();
        inputTime = new javax.swing.JFormattedTextField();
        jButton10 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        inputTitulo = new javax.swing.JTextField();
        textEditorContainer = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        jLabel12.setText("jLabel9");
        jLabel12.setName("jLabel12"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enviar documento con adjunto");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        cboMarket.setEnabled(false);
        cboMarket.setName("cboMarket"); // NOI18N

        cboIndustry.setEnabled(false);
        cboIndustry.setName("cboIndustry"); // NOI18N

        cboDocumentType.setEnabled(false);
        cboDocumentType.setName("cboDocumentType"); // NOI18N

        cboSubject.setName("cboSubject"); // NOI18N
        cboSubject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSubjectItemStateChanged(evt);
            }
        });

        cboStatus.setName("cboStatus"); // NOI18N

        cboLanguage.setEnabled(false);
        cboLanguage.setName("cboLanguage"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setText("Hora");
        jLabel2.setName("jLabel2"); // NOI18N

        groupEnviar.setText("Enviar Indmediatamente");
        groupEnviar.setName("groupEnviar"); // NOI18N
        groupEnviar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                groupEnviarItemStateChanged(evt);
            }
        });

        groupProgramar.setText("Programar Envío");
        groupProgramar.setName("groupProgramar"); // NOI18N
        groupProgramar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                groupProgramarItemStateChanged(evt);
            }
        });

        jLabel1.setText("Fecha");
        jLabel1.setName("jLabel1"); // NOI18N

        inputDate.setEnabled(false);
        inputDate.setName("inputDate"); // NOI18N

        inputTime.setEnabled(false);
        inputTime.setName("inputTime"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(groupEnviar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupProgramar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputDate, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTime, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupEnviar)
                    .addComponent(groupProgramar)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(inputDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton10.setText("+");
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPanel8.setName("jPanel8"); // NOI18N

        jLabel9.setText("Tema");
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText("Sector");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText("Status");
        jLabel11.setName("jLabel11"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(216, 216, 216)
                .addComponent(jLabel11)
                .addGap(200, 200, 200))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSubject, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMarket, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboIndustry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboDocumentType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboLanguage, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboDocumentType)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboMarket)
                        .addComponent(cboLanguage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboSubject)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(cboIndustry))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(cboStatus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setName("jPanel3"); // NOI18N

        inputTitulo.setText("Título");
        inputTitulo.setName("inputTitulo"); // NOI18N
        inputTitulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inputTituloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputTituloFocusLost(evt);
            }
        });
        inputTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputTituloMouseClicked(evt);
            }
        });

        textEditorContainer.setName("textEditorContainer"); // NOI18N
        textEditorContainer.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textEditorContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(inputTitulo)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textEditorContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setName("jPanel4"); // NOI18N

        jTextField4.setName("jTextField4"); // NOI18N

        jLabel3.setText("Archivos adjuntos");
        jLabel3.setName("jLabel3"); // NOI18N

        jButton1.setText("Examinar");
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Alcance de publicación"));
        jPanel6.setName("jPanel6"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 181, Short.MAX_VALUE)
        );

        jPanel7.setName("jPanel7"); // NOI18N

        jLabel4.setText("Twitter");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("140");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText("#Vector");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel6.setName("jLabel6"); // NOI18N

        jButton7.setText("Examinar");
        jButton7.setName("jButton7"); // NOI18N

        jComboBox8.setName("jComboBox8"); // NOI18N

        jLabel8.setText("Autor(es)");
        jLabel8.setName("jLabel8"); // NOI18N

        jButton9.setText("+");
        jButton9.setName("jButton9"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jButton3.setText("Enviar");
        jButton3.setName("jButton3"); // NOI18N

        jButton2.setText("Guardar");
        jButton2.setName("jButton2"); // NOI18N

        jButton6.setText("Cancelar");
        jButton6.setName("jButton6"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton6))
                .addContainerGap())
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(385, 385, 385)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7)))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setName("jPanel9"); // NOI18N

        jLabel14.setText("Tipo de documento");
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText("Idioma");
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel13.setText("Mercado");
        jLabel13.setName("jLabel13"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(156, 156, 156)
                .addComponent(jLabel15)
                .addGap(211, 211, 211))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel13)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void setProgramerInputs(){
    /* Se inician los valores por default de la fecha y la hora */
        inputDate.setHorizontalAlignment(JFormattedTextField.CENTER);
        inputTime.setHorizontalAlignment(JFormattedTextField.CENTER);
        inputDate.setValue(new Date());
        DateFormat df = new SimpleDateFormat("hh:mm/a");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        inputTime.setValue(reportDate);
    }
    
    /* Se agrega el componente editor de texto */
    private void addTextEditor() {
        String sDocument = null;
        String sStyleSheet = null;
        String sRawDocument = null;
        URL urlStyleSheet = null;
        boolean includeToolBar = true;
        boolean base64 = false;
        boolean enterBreak = false;
        boolean showViewSource = false;
        boolean showMenuIcons = false;
        boolean editModeExclusive = true;
        String sLanguage = null;
        String sCountry = null;
        boolean debugMode = false;
        boolean multiBar = true;
        
        Vector<String> v = new Vector<>();
        v.add(EkitCore.KEY_TOOL_COPY);
        v.add(EkitCore.KEY_TOOL_PASTEX);
        v.add(EkitCore.KEY_TOOL_PASTE);
        v.add(EkitCore.KEY_TOOL_CUT);
        
        v.add(EkitCore.KEY_TOOL_BOLD);
        v.add(EkitCore.KEY_TOOL_ITALIC);
        v.add(EkitCore.KEY_TOOL_UNDERLINE);
        
        v.add(EkitCore.KEY_TOOL_UNDO);
        v.add(EkitCore.KEY_TOOL_REDO);
        
        EkitCore ekitCore1 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
        EkitCore ekitCore2 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
        
        try {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridx = 1;
            JToolBar customBar = ekitCore1.customizeToolBar(EkitCore.TOOLBAR_MAIN, v, true);
            textEditorContainer.removeAll();
            textEditorContainer.add(customBar, gbc);
            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.gridy = 4;
            textEditorContainer.add(ekitCore1, gbc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void cboSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSubjectItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object obj = cboSubject.getSelectedItem();
            SubjectBO bo = (SubjectBO) obj;
            int id = bo.getIndustry();
            if (id > 0) {
                cboIndustry.setSelectedIndex(id);
            } else {
                cboIndustry.setSelectedIndex(-1);
            }
            
        }

    }//GEN-LAST:event_cboSubjectItemStateChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
    }//GEN-LAST:event_formComponentShown

    private void groupEnviarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_groupEnviarItemStateChanged

    }//GEN-LAST:event_groupEnviarItemStateChanged

    private void groupProgramarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_groupProgramarItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            
            inputDate.setValue(null);
            inputTime.setValue(null);
            inputDate.setEnabled(true);
            inputTime.setEnabled(true);
            
            try {
                MaskFormatter dateMask = new MaskFormatter("##/##/####");
                dateMask.install(inputDate);
            } catch (ParseException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            
            try {
                MaskFormatter dateMask2 = new MaskFormatter("##:##/UU");
                dateMask2.install(inputTime);
            } catch (ParseException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        } else {
            inputDate.setEnabled(false);
            inputTime.setEnabled(false);
            setProgramerInputs();
        }
    }//GEN-LAST:event_groupProgramarItemStateChanged

    private void inputTituloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTituloFocusGained
        if (inputTitulo.getText().equals("Título")) {
            inputTitulo.setText(" ");
        }
    }//GEN-LAST:event_inputTituloFocusGained

    private void inputTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTituloMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTituloMouseClicked

    private void inputTituloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTituloFocusLost
        if (inputTitulo.getText().isEmpty() || inputTitulo.getText().equals("")) {
            inputTitulo.setText("Título");
        }
    }//GEN-LAST:event_inputTituloFocusLost

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
       addSubjectComponent();
    }//GEN-LAST:event_jButton10ActionPerformed
    
   
    /* Lógica de acceso a esta ventana */
    private void init() {

        /*  Documento activo  */
        DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        
        if (docBO == null) {
            // si no hay documento activo para enviar publicacion: error y cerrar
            JOptionPane.showMessageDialog(null, "No hay documento activo para enviar", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info("The software does not have documents to show");
            this.dispose();
        } else if (docBO.getDocumentId() > 0) {
            // si hay un documento y existe un idDocumento: abrir y llenar campos 
            fillDocumentInfo(docBO.getDocumentId());
        } else {
            /* si hay documento en sesion pero no esta guardado y no tiene un idDoc:
             Guardar documento en sesion, abrir ventana y llenar los campos */

            //Boolean para indicar que se regrese el id insertado
            boolean r = true;
            MainView.main.getScrDocument().saveTexts();
            int idDoc = 0;//MainView.main.getScrDocument().saveDocument();
            if (idDoc > 0) {
                // si guardo correctamente el id debe ser mayor a cero: abrir ventana y llenar campos
                fillDocumentInfo(idDoc);
            } else {
                // si no se guardo correctamente: error y cerrar
                JOptionPane.showMessageDialog(null, "No hay documento activo para enviar", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info("The software does not have documents to show");
                this.dispose();
            }
        }
        
    }
    
    /* Invocación de metodos para llenar info en pantalla */
    private void fillDocumentInfo(int idDocument) {
        
        try {
            DocumentDAO dao = new DocumentDAO();
            DocumentBO docBO = dao.getDocument(idDocument);

            /*Mostrar los campos del documento activo en la pantalla de enviar publicación*/
            /*Inicializar combos*/
            initComboBoxMarket(docBO.getIdMarket());
            initComboBoxDocumentType(docBO.getIdDocType());
            initComboBoxLanguage(docBO.getIdLanguage());
            initComboBoxStatus();
            initComboBoxSubject();
            initComboBoxIndustry();
            
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
        }
        
    }

    /*Metodo para llenar el combo mercado y seleccionar el valor almacenado
     en base de datos para el documento actual */
    private void initComboBoxMarket(int id) {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo_ = new MarketBO();
        bo_.setName("Seleccione");
        bo_.setIdMiVector_real("-1");
        cboMarket.addItem(bo_);
        for (MarketBO bo : list) {
            cboMarket.addItem(bo);
        }
        
        int n = cboMarket.getItemCount();
        for (int i = 0; i < n; i++) {
            MarketBO obj = (MarketBO) cboMarket.getItemAt(i);
            if (obj.getIdMarket() == id) {
                cboMarket.setSelectedIndex(i);
                break;
            }
        }
    }

    /*Metodo para llenar el combo Tipo de documento y seleccionar el valor almacenado
     en base de datos para el documento actual */
    private void initComboBoxDocumentType(int id) {
        DocumentTypeDAO dao = new DocumentTypeDAO();
        List<DocumentTypeBO> list = dao.get(null, -1, 0);
        DocumentTypeBO docBO = new DocumentTypeBO();
        docBO.setName("Seleccione:");
        docBO.setIddocument_type(-1);
        cboDocumentType.addItem(docBO);
        for (DocumentTypeBO docBO_ : list) {
            cboDocumentType.addItem(docBO_);
        }
        
        int n = cboDocumentType.getItemCount();
        for (int i = 0; i < n; i++) {
            DocumentTypeBO obj = (DocumentTypeBO) cboDocumentType.getItemAt(i);
            if (obj.getIddocument_type() == id) {
                cboDocumentType.setSelectedIndex(i);
                break;
            }
        }
    }

    /*Metodo para llenar el combo idiomas y seleccionar el valor almacenado
     en base de datos para el documento actual */
    private void initComboBoxLanguage(int id) {
        LanguageDAO dao = new LanguageDAO();
        List<LanguageBO> list = dao.get(null);
        LanguageBO bo = new LanguageBO();
        bo.setName("Seleccione:");
        bo.setIdLanguage(-1);
        cboLanguage.addItem(bo);
        for (LanguageBO bo_ : list) {
            cboLanguage.addItem(bo_);
        }
        
        int n = cboLanguage.getItemCount();
        for (int i = 0; i < n; i++) {
            LanguageBO obj = (LanguageBO) cboLanguage.getItemAt(i);
            if (obj.getIdLanguage() == id) {
                cboLanguage.setSelectedIndex(i);
                break;
            }
        }
        
    }

    /* Metodo para llenar el combo Status */
    private void initComboBoxStatus() {
        PublishStatusDAO dao = new PublishStatusDAO();
        List<PublishStatusBO> list = dao.get();
        PublishStatusBO bo = new PublishStatusBO();
        bo.setName("Seleccione:");
        bo.setIdpublish_status(-1);
        cboStatus.addItem(bo);
        for (PublishStatusBO bo_ : list) {
            cboStatus.addItem(bo_);
        }
    }

    /* Metodo para llenar el combo Tema */
    private void initComboBoxSubject() {
        SubjectDAO dao = new SubjectDAO();
        List<SubjectBO> list = dao.get(null);
        SubjectBO bo = new SubjectBO();
        bo.setName("Seleccione");
        bo.setIdSubject(-1);
        cboSubject.addItem(bo);
        for (SubjectBO bo_ : list) {
            cboSubject.addItem(bo_);
        }
        
    }

    /* Metodo para llenar el combo sector*/
    private void initComboBoxIndustry() {
        IndustryDAO dao = new IndustryDAO();
        List<IndustryBO> list = dao.get(null);
        IndustryBO bo = new IndustryBO();
        bo.setName("Seleccione:");
        bo.setIdIndustry(-1);
        cboIndustry.addItem(bo);
        for (IndustryBO bo_ : list) {
            cboIndustry.addItem(bo_);
        }
    }

    /* Ventana para guardar temas   */
    private void addSubjectComponent(){
      
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboDocumentType;
    private javax.swing.JComboBox cboIndustry;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboMarket;
    private javax.swing.JComboBox cboStatus;
    private javax.swing.JComboBox cboSubject;
    private javax.swing.JRadioButton groupEnviar;
    private javax.swing.JRadioButton groupProgramar;
    private javax.swing.JFormattedTextField inputDate;
    private javax.swing.JFormattedTextField inputTime;
    private javax.swing.JTextField inputTitulo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPanel textEditorContainer;
    // End of variables declaration//GEN-END:variables
}
