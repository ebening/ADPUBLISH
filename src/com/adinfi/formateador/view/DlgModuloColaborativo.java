/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocCollabCandidateBO;
import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.SectionColModBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.dao.HeaderColModDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.SectionColModDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.publish.AddSubjectModal;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author ADINFI
 */
public class DlgModuloColaborativo extends javax.swing.JDialog {

    private JXTable jxTable;
    private SearchDocsCollabModel model;
    List<DocumentSearchBO> lstDocsAdd = null;
    private boolean agregado;
    private List<DocumentTypeBO> listDoc = null;
    

    /**
     * Creates new form DlgModuloColaborativo
     */
    public DlgModuloColaborativo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(txtTema);
        loadDocumentType();
        fillCombos();
        this.model = new SearchDocsCollabModel();
        createJXTable();

    }

    public DlgModuloColaborativo(java.awt.Frame parent, boolean modal, boolean habIdMod) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(txtTema);
        loadDocumentType();
        fillCombos();
        txtIdModulo.setEditable(habIdMod);
        this.model = new SearchDocsCollabModel();
        createJXTable();

    }

    public DlgModuloColaborativo(java.awt.Frame parent, boolean modal, boolean habIdMod, int marketId, int docTypeId) {
        super(parent, modal);
        initComponents();
        loadDocumentType();
        textAutoCompleter = new TextAutoCompleter(txtTema);
        selCombMarket(marketId, docTypeId);
        txtIdModulo.setEditable(habIdMod);
        this.model = new SearchDocsCollabModel();
        createJXTable();

    }

    private void createJXTable() {
        if (jxTable == null) {
            jxTable = new JXTable();
            jxTable.setHorizontalScrollEnabled(true);
            jxTable.setColumnControlVisible(true);
            int margin = 5;
            jxTable.packTable(margin);

            //jxTable.addMouseMotionListener(mml);
            jxTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            jxTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    if (me.getClickCount() == 2) {
                        // your valueChanged overridden method 
                        accept();
                    }
                }
            });

            scrollGrid.setViewportView(jxTable);
        }

        jxTable.setModel(model);
        Utilerias.formatTablex(jxTable);
        jxTable.getColumn(0).setPreferredWidth(200);
    }

    protected void accept(int idx) {

        if (this.lstDocsAdd == null || this.lstDocsAdd.size() <= 0) {
            return;
        }
        if (idx < 0) {
            return;
        }
        List<DocumentBO> list = this.model.getList();
        if (list == null || list.size() <= 0) {
            return;
        }

        DocumentBO docBO = list.get(idx);
        // addDocumentsCollab(docBO);   

    }

    private void loadDocumentType() {
        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        listDoc = daoDoc.get(null, -1, 1);//daoDoc.getDocTypeByMarket(1, ((MarketBO)objMrkt).getIdMarket());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblEncadezado = new javax.swing.JLabel();
        cmbEncabezado = new javax.swing.JComboBox();
        lblSeccion = new javax.swing.JLabel();
        cmbSeccion = new javax.swing.JComboBox();
        lblTema = new javax.swing.JLabel();
        txtTema = new javax.swing.JTextField();
        lblIdModulo = new javax.swing.JLabel();
        txtIdModulo = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        edDocName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblMercado = new javax.swing.JLabel();
        cmbMercado = new javax.swing.JComboBox();
        lblTipoDoc = new javax.swing.JLabel();
        cmbTipoDoc = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        scrollGrid = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar a módulo dolaborativo");

        jPanel1.setPreferredSize(new java.awt.Dimension(502, 310));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEncadezado.setText("Encabezado:");
        jPanel1.add(lblEncadezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        cmbEncabezado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEncabezadoItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 300, -1));

        lblSeccion.setText("Sección:");
        jPanel1.add(lblSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, -1, -1));

        jPanel1.add(cmbSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 300, -1));

        lblTema.setText("Tema:");
        jPanel1.add(lblTema, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, -1, -1));

        txtTema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTemaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtTemaMousePressed(evt);
            }
        });
        txtTema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTemaActionPerformed(evt);
            }
        });
        txtTema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTemaFocusLost(evt);
            }
        });
        txtTema.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTemaKeyTyped(evt);
            }
        });
        jPanel1.add(txtTema, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 300, -1));

        lblIdModulo.setText("Id del módulo:");
        jPanel1.add(lblIdModulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, -1, -1));
        jPanel1.add(txtIdModulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 300, -1));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_add_.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 240, -1, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar documento:"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        edDocName.setName("edDocName"); // NOI18N
        edDocName.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                edDocNameHierarchyChanged(evt);
            }
        });
        edDocName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edDocNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edDocNameKeyTyped(evt);
            }
        });
        jPanel4.add(edDocName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre del documento");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        lblMercado.setText("Mercado:");
        jPanel4.add(lblMercado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cmbMercado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMercadoItemStateChanged(evt);
            }
        });
        cmbMercado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMercadoActionPerformed(evt);
            }
        });
        jPanel4.add(cmbMercado, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, -1));

        lblTipoDoc.setText("Tipo de documento:");
        jPanel4.add(lblTipoDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        cmbTipoDoc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoDocItemStateChanged(evt);
            }
        });
        jPanel4.add(cmbTipoDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 110, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, 150));

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setMaximumSize(new java.awt.Dimension(2147483647, 200));
        jPanel2.setPreferredSize(new java.awt.Dimension(576, 200));
        jPanel2.setLayout(new java.awt.BorderLayout());

        scrollGrid.setMaximumSize(new java.awt.Dimension(550, 32767));
        jPanel2.add(scrollGrid, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOK.setText("Aceptar");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        jPanel3.add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel3.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, -1, -1));

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMercadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoActionPerformed

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            builIdModulo();
        }
        fillComboDoc();

    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void cmbEncabezadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEncabezadoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fillComboSeccion();
            builIdModulo();
        }
    }//GEN-LAST:event_cmbEncabezadoItemStateChanged

    private void cmbTipoDocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoDocItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fillComboHead();
            builIdModulo();
        }
    }//GEN-LAST:event_cmbTipoDocItemStateChanged

    private void txtTemaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusLost
        listenerData();
        builIdModulo();
    }//GEN-LAST:event_txtTemaFocusLost

    private void txtTemaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemaKeyTyped
        listenerData();
        builIdModulo();
    }//GEN-LAST:event_txtTemaKeyTyped

    private void txtTemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMouseClicked
        listenerData();
        builIdModulo();
    }//GEN-LAST:event_txtTemaMouseClicked

    private void txtTemaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMousePressed
        listenerData();
        builIdModulo();
    }//GEN-LAST:event_txtTemaMousePressed

    private void txtTemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTemaActionPerformed

    }//GEN-LAST:event_txtTemaActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        AddSubjectModal s = new AddSubjectModal(null, true);

        s.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                initAutoTema();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                initAutoTema();
            }
        });

        s.setLocationRelativeTo(null);
        s.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    protected void searchDocuments() {
        // this.e

        this.model.setData((List<DocumentBO>) null);
        this.model.fireTableDataChanged();

        Object obj = cmbMercado.getSelectedItem();
        int idMarket = 0;
        if (obj != null) {
            MarketBO marketb = (MarketBO) obj;
            idMarket = Integer.parseInt(marketb.getIdMiVector_real());
        }

        Object obj2 = cmbTipoDoc.getSelectedItem();
        int idDocType = 0;
        if (obj2 != null) {
            DocumentTypeBO tipoBO = (DocumentTypeBO) obj2;
            idDocType = tipoBO.getIddocument_type();
        }

//        if ((edDocName.getText() == null || edDocName.getText().isEmpty())
//                && (idMarket <= 0)
//                && (idDocType <= 0)) {
//            return;
//        }
        DocumentDAO docDAO = new DocumentDAO();
        List<DocumentBO> lstDocuments = docDAO.searchCollaboratives(this.edDocName.getText(), idMarket, idDocType, GlobalDefines.COLLAB_TYPE_MOD);

        this.model.setData(lstDocuments);
        this.model.fireTableDataChanged();

    }

    private void accept() {
        if (validateForm()) {
            if (add) {
                agregar();
            } else {
                crear();
            }
            this.setVisible(false);
        }

    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        accept();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchDocuments();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void edDocNameHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_edDocNameHierarchyChanged
        // TODO add your handling code here:
        //    searchDocuments();
    }//GEN-LAST:event_edDocNameHierarchyChanged

    private void edDocNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edDocNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_edDocNameKeyPressed

    private void edDocNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edDocNameKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_edDocNameKeyTyped

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
            java.util.logging.Logger.getLogger(DlgModuloColaborativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgModuloColaborativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgModuloColaborativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgModuloColaborativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgModuloColaborativo dialog = new DlgModuloColaborativo(new javax.swing.JFrame(), true);
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

    private void listenerData() {
        Object o = textAutoCompleter.getItemSelected();
        if (o != null && o instanceof SubjectBO) {
            selectedValueSubject = (SubjectBO) o;
        } else if (o != null) {
            SubjectDAO daoSubject = new SubjectDAO();
            List<SubjectBO> listSubject = daoSubject.get(null);
            for (SubjectBO b : listSubject) {
                if (o.toString().equals(b.getName())) {
                    selectedValueSubject = b;
                }
            }
        }
    }

    private void fillCombos() {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO marketBO = new MarketBO();

        marketBO.setName("Seleccione");
        marketBO.setIdMiVector_real("-1");
        cmbMercado.addItem(marketBO);
        for (MarketBO mBO : list) {
            mBO.setIdMarket(Integer.parseInt(mBO.getIdMiVector_real()));
            cmbMercado.addItem(mBO);
        }

        initAutoTema();
    }

    private void selCombMarket(int marketId, int docTypeId) {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO marketBO = new MarketBO();
        MarketBO mSelBO = new MarketBO();

        marketBO.setName("Seleccione");
        marketBO.setIdMiVector_real("-1");
        cmbMercado.addItem(marketBO);
        for (MarketBO mBO : list) {
            cmbMercado.addItem(mBO);
            if (Integer.parseInt(mBO.getIdMiVector_real()) == marketId) {
                cmbMercado.setSelectedItem(mBO);
                mSelBO = mBO;
            }
        }
        
        
        DocumentTypeBO boDoc = new DocumentTypeBO();
        cmbTipoDoc.removeAllItems();
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipoDoc.addItem(boDoc);

        for (DocumentTypeBO boDoc2 : listDoc) {
            if (boDoc2.getIdMarket() == marketId) {
                cmbTipoDoc.addItem(boDoc2);
                if (docTypeId == boDoc2.getIddocument_type()) {
                    cmbTipoDoc.setSelectedItem(boDoc2);
                }
            }

        }
        fillComboHead();
        initAutoTema();
    }

    private void initAutoTema() {

        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        listDoc = daoDoc.get(null, -1, 1);

        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.get(null);
        textAutoCompleter.removeAllItems();
        for (SubjectBO b : listSubject) {
            textAutoCompleter.addItem(b);
        }
    }

    private void fillComboDoc() {

        cmbTipoDoc.removeAllItems();

        Object obj = cmbMercado.getSelectedItem();
        MarketBO boMarket = (MarketBO) obj;

        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipoDoc.addItem(boDoc);
        
        

        if (boMarket.getIdMiVector_real().equals("-1")) {
            for (DocumentTypeBO boDoc2 : listDoc) {
                cmbTipoDoc.addItem(boDoc2);
            }
        } else {
            for (DocumentTypeBO boDoc2 : listDoc) {
                if (boDoc2.getIdMarket() == Integer.parseInt(boMarket.getIdMiVector_real()) && boDoc2.isCollaborative()) {
                    cmbTipoDoc.addItem(boDoc2);
                }
            }
        }

        if (cmbEncabezado != null) {
            cmbEncabezado.removeAllItems();
        }
    }

    private void fillComboHead() {
        Object objMrkt = cmbMercado.getSelectedItem();
        MarketBO marketbo = (MarketBO) objMrkt;
        int idMarket = Integer.parseInt(marketbo.getIdMiVector_real());

        Object objDoc = cmbTipoDoc.getSelectedItem();
        if (objMrkt != null && idMarket > -1
                && objDoc != null && ((DocumentTypeBO) objDoc).getIddocument_type() > -1) {
            HeaderColModDAO headDAO = new HeaderColModDAO();
            
            List<HeaderColModBO> listHeader = headDAO.get(idMarket, ((DocumentTypeBO) objDoc).getIddocument_type());
            HeaderColModBO headBO = new HeaderColModBO();

            cmbEncabezado.removeAllItems();
            headBO.setName("Seleccione");
            headBO.setIdHeaderColMod(-1);
            cmbEncabezado.addItem(headBO);

            for (HeaderColModBO hBO : listHeader) {
                cmbEncabezado.addItem(hBO);
            }
        }
    }

    private void fillComboSeccion() {
        Object objEnc = cmbEncabezado.getSelectedItem();
        if (objEnc != null && ((HeaderColModBO) objEnc).getIdHeaderColMod() > -1) {
            SectionColModDAO sectionDAO = new SectionColModDAO();
            List<SectionColModBO> listSection = sectionDAO.get(((HeaderColModBO) objEnc).getIdHeaderColMod());

            SectionColModBO sectionBO = new SectionColModBO();
            cmbSeccion.removeAllItems();
            sectionBO.setName("Seleccione");
            sectionBO.setIdHeaderColMod(-1);
            cmbSeccion.addItem(sectionBO);
            for (SectionColModBO sBO : listSection) {
                cmbSeccion.addItem(sBO);
            }
        }
    }

    private void builIdModulo() {

        Object objMrkt = cmbMercado.getSelectedItem();
        MarketBO marketbo = (MarketBO) objMrkt;
        int idMarket = Integer.parseInt(marketbo.getIdMiVector_real());

        StringBuilder strB = new StringBuilder();
        strB.append("M_");
        if (objMrkt != null && idMarket > -1) {
            strB.append(((MarketBO) objMrkt).getNomenclature());
        }
        Object objDoc = cmbTipoDoc.getSelectedItem();
        if (objDoc != null && ((DocumentTypeBO) objDoc).getIddocument_type() > -1) {
            strB.append(((DocumentTypeBO) objDoc).getNomenclature());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        strB.append(sdf.format(new Date()));
        if (selectedValueSubject != null) {
            strB.append(selectedValueSubject.getName());
        }
        txtIdModulo.setText(strB.toString());
    }

    public void crear() {
        Object objMrkt = cmbMercado.getSelectedItem();
        MarketBO marketbo = (MarketBO) objMrkt;
        int idMarket = Integer.parseInt(marketbo.getIdMiVector_real());

        moduleCollab = new DocCollabCandidateBO();
        moduleCollab.setMarketId(idMarket);
        moduleCollab.setDocumentTypeId(((DocumentTypeBO) cmbTipoDoc.getSelectedItem()).getIddocument_type());
        moduleCollab.setIdHeader(((HeaderColModBO) cmbEncabezado.getSelectedItem()).getIdHeaderColMod());
        moduleCollab.setIdSection(((SectionColModBO) cmbSeccion.getSelectedItem()).getIdSectionColMod());
        moduleCollab.setCandSubject(selectedValueSubject.getName());
        moduleCollab.setCandSubjectId(selectedValueSubject.getIdSubject());
        moduleCollab.setCandName(txtIdModulo.getText());
        moduleCollab.setTipo(GlobalDefines.TIPO_CAND_MODULE);

        String nameMod = "";

        for (Integer idDocMod : lDocModCollab.keySet()) {
            moduleCollab.setModuleId(idDocMod);
            nameMod = lDocModCollab.get(idDocMod);
        }

        CollaborativesDAO collDAO = new CollaborativesDAO();
        if (collDAO.validarModulo(moduleCollab.getMarketId(), moduleCollab.getDocumentTypeId(), moduleCollab.getModuleId(), moduleCollab.getDocumentModuleId())) {
            collDAO.addDocumentCollabCand(moduleCollab);
        } else {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El módulo ".concat(nameMod).concat(" no se puede integrar por que ya se encuentra en la bandeja Mercado: ").concat(((MarketBO) cmbMercado.getSelectedItem()).getName()).
                    concat(", Tipo Documento: ").concat(((DocumentTypeBO) cmbTipoDoc.getSelectedItem()).getName()), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregar() {
        agregado = false;
        /*
         if( this.lstDocsAdd==null || this.lstDocsAdd.size()<=0 ){
         return;
         }*/

        int idx = this.jxTable.getSelectedRow();

        if (idx < 0) {
            return;
        }
        List<DocumentBO> list = this.model.getList();
        if (list == null || list.size() <= 0) {
            return;
        }

        DocumentBO docBO = list.get(idx);

        //Validar perfil y doctype
        int perfilID = InstanceContext.getInstance().getUsuario().getPerfilId();
        DocumentTypeProfileDAO dao = new DocumentTypeProfileDAO();
        List<DocumentTypeProfileBO> lstPerfiles = dao.get(docBO.getTipoBO().getIddocument_type());

        boolean valid = false;

        for (DocumentTypeProfileBO lst : lstPerfiles) {
            if (perfilID == lst.getIdprofile()) {
                valid = true;
                break;
            }
        }

        if (valid == false) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Este usuario no esta autorizado para editar este tipo de documento.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object objDoc = cmbTipoDoc.getSelectedItem();
        boolean temaReq = false;
        boolean titReq = false;
        if (objDoc != null) {
            DocumentTypeBO tipoBO = (DocumentTypeBO) objDoc;
            titReq = tipoBO.isTitle();
            temaReq = tipoBO.isSubject();
        }
        
        if(temaReq && selectedValueSubject == null){
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El tema es requerido.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        moduleCollab = new DocCollabCandidateBO();
        moduleCollab.setMarketId(Integer.parseInt(((MarketBO) cmbMercado.getSelectedItem()).getIdMiVector_real()));
        //  moduleCollab.setDocumentTypeId(((DocumentTypeBO)cmbTipoDoc.getSelectedItem()).getIddocument_type());
        moduleCollab.setIdHeader(((HeaderColModBO) cmbEncabezado.getSelectedItem()).getIdHeaderColMod());
        moduleCollab.setIdSection(((SectionColModBO) cmbSeccion.getSelectedItem()).getIdSectionColMod());
        moduleCollab.setCandSubject(selectedValueSubject != null ?  selectedValueSubject.getName() : "");
        moduleCollab.setCandSubjectId(selectedValueSubject != null ? selectedValueSubject.getIdSubject() : 0);
        moduleCollab.setTipo(GlobalDefines.TIPO_CAND_MODULE);
        moduleCollab.setDocVersionTarget(docBO.getDocVersionId());

        CollaborativesDAO collDAO = new CollaborativesDAO();
        int i = 1;
        List<String> modErr = new ArrayList<>();
        List<String> modOk = new ArrayList<>();
        int result = 0;
        for (Integer idDocMod : lDocModCollab.keySet()) {
            if (lDocModCollab.size() > 1) {
                moduleCollab.setCandName(txtIdModulo.getText() + "_" + i);
            } else {
                moduleCollab.setCandName(txtIdModulo.getText());
            }
            moduleCollab.setDocumentModuleId(idDocMod);
            if (collDAO.validarModulo(moduleCollab.getMarketId(), moduleCollab.getDocumentTypeId(), moduleCollab.getModuleId(), moduleCollab.getDocumentModuleId())) {
                result = collDAO.addDocumentCollabCand(moduleCollab);
                if (result == 1) {
                    modErr.add(lDocModCollab.get(idDocMod));
                } else {
                    modOk.add(lDocModCollab.get(idDocMod));
                    i++;
                }
            } else {
                modErr.add(lDocModCollab.get(idDocMod));
            }
        }

        if (modErr.size() > 0) {
            String msgErr = "";
            if (modErr.size() > 1) {
                msgErr = "Los módulos ";
                for (String strMod : modErr) {
                    msgErr = msgErr.concat(strMod).concat(", ");
                }
                msgErr = msgErr.substring(0, msgErr.lastIndexOf(","));
            } else {
                msgErr = "El módulo ".concat(modErr.get(0));
            }
            msgErr = msgErr.concat(" no se puede(n) integrar por que ya se encuentra(n) en la bandeja Mercado: ").concat(((MarketBO) cmbMercado.getSelectedItem()).getName()).
                    concat(", Tipo Documento: ").concat(((DocumentTypeBO) cmbTipoDoc.getSelectedItem()).getName());

            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), msgErr, JOptionPane.ERROR_MESSAGE);
        }

        if (modOk.size() > 0) {
            String msgOk = "";
            if (modOk.size() > 1) {
                msgOk = "Los módulos ";
                for (String strMod : modOk) {
                    msgOk = msgOk.concat(strMod).concat(", ");
                }
                msgOk = msgOk.substring(0, msgOk.lastIndexOf(","));
            } else {
                msgOk = "El módulo ".concat(modOk.get(0));
            }
            msgOk = msgOk.concat(" fue(ron) agregado(s) exitosamente");

            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), msgOk, JOptionPane.INFORMATION_MESSAGE);
            agregado = true;
        }
    }

    public boolean isAgregado() {
        return agregado;
    }

    private boolean validateForm() {
        Object objMrkt = cmbMercado.getSelectedItem();
        MarketBO marketbo = (MarketBO) objMrkt;
        int idMarket = Integer.parseInt(marketbo.getIdMiVector_real());

        if (objMrkt == null || idMarket < 0) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un mercado", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Object docType = cmbTipoDoc.getSelectedItem();

        if (docType == null || ((DocumentTypeBO) docType).getIddocument_type() < 0) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un tipo de documento", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Object objEnc = cmbEncabezado.getSelectedItem();
        if (objEnc != null && ((HeaderColModBO) objEnc).getIdHeaderColMod() < 0) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un encabezado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Object objSec = cmbSeccion.getSelectedItem();
        if (objSec != null && ((SectionColModBO) objSec).getIdSectionColMod() < 0) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione una sección", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (((DocumentTypeBO) docType).isSubject()) {
            if (!txtTema.getText().isEmpty()) {
                if (selectedValueSubject == null) {
                    Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (selectedValueSubject != null && !txtTema.getText().trim().equals(selectedValueSubject.getName())) {
                    Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            } else if (txtTema.getText().trim().isEmpty() && selectedValueSubject != null) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (selectedValueSubject == null) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un tema", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (txtIdModulo == null || (txtIdModulo != null && txtIdModulo.getText().isEmpty())) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Ingrese un Id de Módulo", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (add) {
            int idx = this.jxTable.getSelectedRow();

            if (idx < 0) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione el documento al que desea agregar el módulo", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        }

        return true;
    }

    public void setDataModuleDocument(DocumentBO docBo) {
        for (int i = 0; i < cmbMercado.getItemCount(); i++) {
            if (cmbMercado.getItemAt(i) instanceof MarketBO) {
                MarketBO market = (MarketBO) cmbMercado.getItemAt(i);
                int idMarket = Integer.parseInt(market.getIdMiVector_real());
                if (idMarket == docBo.getIdMarket()) {
                    cmbMercado.setSelectedIndex(i);
                    break;
                }
            }
        }

        for (int i = 0; i < cmbTipoDoc.getItemCount(); i++) {
            if (cmbTipoDoc.getItemAt(i) instanceof DocumentTypeBO) {
                DocumentTypeBO docType = (DocumentTypeBO) cmbTipoDoc.getItemAt(i);

                if (docType.getIddocument_type() == docBo.getIdDocType()) {
                    cmbTipoDoc.setSelectedIndex(i);
                    break;
                }
            }
        }

        searchDocuments();

        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.get(null);
        for (int i = 0; i < listSubject.size(); i++) {
            SubjectBO obj = listSubject.get(i);
            if (obj.getIdSubject() == docBo.getIdSubject()) {
                selectedValueSubject = obj;
                txtTema.setText(obj.getName());
                break;
            }
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox cmbEncabezado;
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbSeccion;
    private javax.swing.JComboBox cmbTipoDoc;
    private javax.swing.JTextField edDocName;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblEncadezado;
    private javax.swing.JLabel lblIdModulo;
    private javax.swing.JLabel lblMercado;
    private javax.swing.JLabel lblSeccion;
    private javax.swing.JLabel lblTema;
    private javax.swing.JLabel lblTipoDoc;
    private javax.swing.JScrollPane scrollGrid;
    private javax.swing.JTextField txtIdModulo;
    private javax.swing.JTextField txtTema;
    // End of variables declaration//GEN-END:variables
    private TextAutoCompleter textAutoCompleter;
    private SubjectBO selectedValueSubject;
    private DocCollabCandidateBO moduleCollab;
    private Map<Integer, String> lDocModCollab;
    private boolean add;

    public DocCollabCandidateBO getModuleCollab() {
        return moduleCollab;
    }

    public void setModuleCollab(DocCollabCandidateBO moduleCollab) {
        this.moduleCollab = moduleCollab;
    }

    public Map<Integer, String> getlDocModCollab() {
        return lDocModCollab;
    }

    public void setlDocModCollab(Map<Integer, String> lDocModCollab) {
        this.lDocModCollab = lDocModCollab;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    class SearchDocsCollabModel extends AbstractTableModel {

        private List<DocumentBO> list;

        /**
         * @return the list
         */
        public List<DocumentBO> getList() {
            return list;
        }

        /**
         * @param list the list to set
         */
        public void setList(List<DocumentBO> list) {
            this.list = list;
        }

        private final String[] columnNames = new String[]{
            "Nombre",
            "Tema",
            "Título",
            "Tipo de documento",
            "Mercado"
        };

        private final Class[] classes = new Class[]{
            String.class,
            String.class,
            String.class,
            String.class,
            String.class
        };

        public final Integer COLUMN_COUNT = 5;

        private Object[][] data = null;

        public SearchDocsCollabModel() {

        }

        public void setData(List<DocumentBO> list) {
            this.list = list;
            refreshData();
        }

        public void refreshData() {
            if (this.list == null) {
                return;
            }
            data = new Object[list == null ? 0 : list.size()][];
            for (int i = 0; i < data.length; i++) {
                DocumentBO row = list.get(i);

                data[i] = new Object[]{
                    row.getFileName(),
                    row.getSubjectBO().getName(),
                    row.getDocumentName(),
                    row.getTipoBO().getName(),
                    row.getMarketBO().getName()

                };
            }

        }

        @Override
        public int getRowCount() {
            return data != null ? data.length : 0;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public String getColumnName(int column) {
            return column >= columnNames.length ? columnNames[columnNames.length - 1] : columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex >= classes.length ? classes[classes.length - 1] : classes[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            if (data == null || data.length == 0) {
                return Object.class;
            }

            Object value = null;
            try {
                //Seleccionar imagen a pintar en base a 

                value = data[rowIndex][columnIndex];

            } catch (Exception e) {
                value = null;
            }
            return value;
        }

        public Object[][] getData() {
            return data;
        }

        public void setData(Object[][] data) {
            this.data = data;
            fireTableDataChanged();
        }

    }

}
