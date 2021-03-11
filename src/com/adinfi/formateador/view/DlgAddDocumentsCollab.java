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
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author Desarrollador
 */
public class DlgAddDocumentsCollab extends javax.swing.JDialog {

    private boolean ok = false;
    List<DocumentSearchBO> lstDocsAdd = null;
    private JXTable jxTable;
    private SearchDocsCollabModel model;
    private List<DocumentTypeBO> listDoc = null;

    /**
     * Creates new form AddDocumentsCollab
     */
    public DlgAddDocumentsCollab(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ok = false;
        loadLstDocumentType();
        fillCombos();
        this.model = new SearchDocsCollabModel();
        createJXTable();
    }

    public void setDocumentsAdd(List<DocumentSearchBO> lstDocsAdd) {
        this.lstDocsAdd = lstDocsAdd;
    }

    /*Llenar Combos*/
    private void fillCombos() {

        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo_ = new MarketBO();
        /*Agregando valores por default al combo*/
        bo_.setName("Seleccione");
        bo_.setIdMiVector_real("-1");
        cmbMercado.addItem(bo_);
        for (MarketBO bo : list) {
            bo.setIdMarket(Integer.parseInt(bo.getIdMiVector_real()));
            cmbMercado.addItem(bo);
        }

        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        List<DocumentTypeBO> listDoc = daoDoc.get(null, -1, 0);
        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipo.addItem(boDoc);
        for (DocumentTypeBO boDoc2 : listDoc) {
            cmbTipo.addItem(boDoc2);
        }

    }
    
    private void loadLstDocumentType(){
     DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        listDoc = daoDoc.get(null, -1, 1);
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

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

        Object obj2 = cmbTipo.getSelectedItem();
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
        List<DocumentBO> lstDocuments = docDAO.searchCollaboratives(this.edDocName.getText(), idMarket, idDocType, GlobalDefines.COLLAB_TYPE_DOC);

        this.model.setData(lstDocuments);
        this.model.fireTableDataChanged();

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

    protected void accept() {

        int idx = this.jxTable.getSelectedRow();

        if (idx < 0) {
            return;
        }

        ok = true;
        if (this.lstDocsAdd == null || this.lstDocsAdd.size() <= 0) {
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

        addDocumentsCollab(docBO);

    }

    private void filltipoDoc() {

        cmbTipo.removeAllItems();

        Object obj = cmbMercado.getSelectedItem();
        MarketBO boMarket = (MarketBO) obj;
        int idMarket = Integer.parseInt(boMarket.getIdMiVector_real());

        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipo.addItem(boDoc);

        if (idMarket == -1) {
            for (DocumentTypeBO boDoc2 : listDoc) {
                cmbTipo.addItem(boDoc2);
            }
        } else {
            for (DocumentTypeBO boDoc2 : listDoc) {
                if (boDoc2.getIdMarket() == idMarket && boDoc2.isCollaborative()) {
                    cmbTipo.addItem(boDoc2);
                }
            }
        }

    }

    protected void addDocumentsCollab(DocumentBO docTargetBO) {
        CollaborativesDAO docsCollabDAO = new CollaborativesDAO();
        boolean error = true;
        int response = 0;
        int idMarket = 0;
        int idDocType;
        String docErrors = "";
        String docRep = "";
        if (docTargetBO == null || docTargetBO.getDocumentId() <= 0) {
            return;
        }

        try {

            /*Validacion Combo mercado*/
            Object obj = cmbMercado.getSelectedItem();

            if (obj != null) {
                MarketBO boMarket = (MarketBO) obj;
                idMarket = Integer.parseInt(boMarket.getIdMiVector_real());
            }

            /*
             if( idMarket<=0 ){
             Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Mercado", JOptionPane.ERROR_MESSAGE);
             return;
             }*/
            /* Validación Combo Tipo de Documento*/
            Object obj2 = cmbTipo.getSelectedItem();
            idDocType = 0;
            if (obj2 != null) {
                DocumentTypeBO tipoBO = (DocumentTypeBO) obj2;
                idDocType = tipoBO.getIddocument_type();
            }

            /*
             if( idDocType<=0 ){
             Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Tipo de Documento", JOptionPane.ERROR_MESSAGE);
             return;
             }*/
            error = false;
            docErrors = "";
            docRep = "";
            for (DocumentSearchBO docBO : this.lstDocsAdd) {

                if (docBO.isCollaborative() 
                        && ( docBO.getCollaborative_Type() == null || GlobalDefines.COLLAB_TYPE_DOC.equals( docBO.getCollaborative_Type().trim() ) ) ) {
                    error = true;
                    docErrors += docBO.getDocumentName() + " \n ";
                    continue;
                }

                DocCollabCandidateBO docCandBO = new DocCollabCandidateBO();
                docCandBO.setDocVersionId(docBO.getDocVersionId());
                docCandBO.setMarketId(idMarket);
                docCandBO.setDocumentTypeId(idDocType);
                docCandBO.setTipo("D");
                docCandBO.setDocVersionTarget(docTargetBO.getDocVersionId());

                response = docsCollabDAO.addDocumentCollabCand(docCandBO);

                if (response != 0) {
                    error = true;
                    if(response == 11){
                        docRep += docBO.getDocumentName() + " \n ";
                    }else{
                        docErrors += docBO.getDocumentName() + " \n ";
                    }
                }
            }

            this.lstDocsAdd = null;

        } catch (Exception e) {
            error = true;
        }

        if (error == true) {
            if(docErrors.isEmpty() == false)
                JOptionPane.showMessageDialog(this, "Los siguientes documentos no se pudieron agregar :\n" + docErrors, "Agregar documentos ", JOptionPane.OK_OPTION);
            
            if(docRep.isEmpty() == false)
                JOptionPane.showMessageDialog(this, "Los siguientes documentos ya existen en el integrador :\n" + docRep, "Agregar documentos ", JOptionPane.OK_OPTION);
        } else {
            //  JOptionPane.showMessageDialog(this, "Los documentos se agregaron correctamente ", "Agregar documentos ", JOptionPane.OK_OPTION);            
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Los documentos se guardaron correctamente ", JOptionPane.INFORMATION_MESSAGE);
        }
        this.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbMercado = new javax.swing.JComboBox();
        cmbTipo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        edDocName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        scrollGrid = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(550, 2147483647));
        setPreferredSize(new java.awt.Dimension(550, 594));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        panel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel1.setMaximumSize(new java.awt.Dimension(550, 32767));
        panel1.setPreferredSize(new java.awt.Dimension(550, 134));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Mercado");

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

        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tipo de documento");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre del documento");

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

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edDocName, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edDocName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panel1, java.awt.BorderLayout.NORTH);

        jPanel3.setMaximumSize(new java.awt.Dimension(550, 2147483647));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 400));
        jPanel3.setPreferredSize(new java.awt.Dimension(550, 400));
        jPanel3.setLayout(new java.awt.BorderLayout());

        scrollGrid.setMaximumSize(new java.awt.Dimension(550, 32767));
        jPanel3.add(scrollGrid, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.setMaximumSize(new java.awt.Dimension(550, 60));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 45));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("Seleccionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 100, -1));

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
      filltipoDoc();
    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void cmbMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMercadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoActionPerformed

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        /*  if (evt.getStateChange() == ItemEvent.SELECTED) {
         createFileName();
         } */
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        this.ok = false;
    }//GEN-LAST:event_formWindowActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchDocuments();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //this.clo
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void edDocNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edDocNameKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_edDocNameKeyTyped

    private void edDocNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edDocNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_edDocNameKeyPressed

    private void edDocNameHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_edDocNameHierarchyChanged
        // TODO add your handling code here:
        //    searchDocuments();

    }//GEN-LAST:event_edDocNameHierarchyChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        accept();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
       
    }//GEN-LAST:event_formComponentShown

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
            java.util.logging.Logger.getLogger(DlgAddDocumentsCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgAddDocumentsCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgAddDocumentsCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgAddDocumentsCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgAddDocumentsCollab dialog = new DlgAddDocumentsCollab(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JTextField edDocName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panel1;
    private javax.swing.JScrollPane scrollGrid;
    // End of variables declaration//GEN-END:variables

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
