/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.DlgAddDocumentsCollab.SearchDocsCollabModel;
import com.adinfi.formateador.view.publish.AddSubjectModal;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author Desarrollador
 */
public class DlgSaveCollab extends javax.swing.JDialog {

    private boolean ok;
    private boolean collborative = false;
    private boolean titleRequired = false;

    List<DocumentSearchBO> lstDocsAdd = null;
    private JXTable jxTable;
    private SearchDocsCollabModel model;
    private String tipo = null;
    private TextAutoCompleter textAutoCompleter;
    private SubjectBO selectedValueSubject;
    private List<DocumentTypeBO> listDoc = null;

    public SubjectBO getSelectedValueSubject() {
        return selectedValueSubject;
    }

    public void setSelectedValueSubject(SubjectBO selectedValueSubject) {
        this.selectedValueSubject = selectedValueSubject;
    }

    public DlgSaveCollab(java.awt.Frame parent, boolean modal, String tipo) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(txtTema);
        initAutoTema();
        Utilerias.addEscapeListener(this);
        //Utilerias.loadBinaryLookup(cboSubject);
        this.tipo = tipo;
        this.model = new SearchDocsCollabModel();
        createJXTable();
        cboSubject.setVisible(false);
        if (tipo.equals(GlobalDefines.TIPO_CAND_DOC)) {
            this.setTitle("Integrador de Documentos");
        } else {
            this.setTitle("Integrador de Módulos");
        }
        enableNuevo(false);

    }

    private void initAutoTema() {

        DocumentTypeDAO daoDoc = new DocumentTypeDAO();

        if (this.isCollborative()) {
            listDoc = daoDoc.get(null, -1, 1);
        } else {
            listDoc = daoDoc.get(null, -1, 0);
        }

        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.get(null);
        textAutoCompleter.removeAllItems();
        for (SubjectBO b : listSubject) {
            textAutoCompleter.addItem(b);
        }
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
    
    public void loadInfoDocument(DocumentBO document){
        fillCombos();
        
        for(int i = 0; i < cmbMercado.getItemCount(); i++){
            MarketBO bo = (MarketBO) cmbMercado.getItemAt(i);
            if(bo.getIdMiVector_real().equals( String.valueOf( document.getIdMarket() )) ){
                cmbMercado.setSelectedIndex(i);
                break;
            }
        }
        
        for(int i = 0; i < cmbTipo.getItemCount(); i++){
            DocumentTypeBO bo = (DocumentTypeBO) cmbTipo.getItemAt(i);
            if(bo.getIddocument_type() == document.getIdDocType() ){
                cmbTipo.setSelectedIndex(i);
                break;
            }
        }
        
        btnBuscar.setEnabled(false);
        nuevoDocumento();
        btnNuevo.setEnabled(false);
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isCollborative() {
        return collborative;
    }

    public void setCollborative(boolean collborative) {
        this.collborative = collborative;
    }

    public boolean isTitleRequired() {
        return titleRequired;
    }

    public void setTitleRequired(boolean titleRequired) {
        this.titleRequired = titleRequired;
    }

    public JComboBox getCmbMercado() {
        return cmbMercado;
    }

    public JComboBox getcboIdioma() {
        return cboIdioma;
    }

    public void setCmbMercado(JComboBox cmbMercado) {
        this.cmbMercado = cmbMercado;
    }

    public JComboBox getCmbTipo() {
        return cmbTipo;
    }

    public void setCmbTipo(JComboBox cmbTipo) {
        this.cmbTipo = cmbTipo;
    }

    public JComboBox getCboSubject() {
        return cboSubject;
    }

    public void setCboSubject(JComboBox cboSubject) {
        this.cboSubject = cboSubject;
    }

    public JCheckBox isFavorite() {
        return favorite;
    }

    public void setFavorite() {
        this.favorite = favorite;
    }

    public JTextField getEdNombredoc() {
        return edNombredoc;
    }

    public void setEdNombredoc(JTextField edNombredoc) {
        this.edNombredoc = edNombredoc;
    }

    public void setEdFileName(JTextField edFileName) {
        this.edFileName = edFileName;
    }

    public JTextField getEdFileName() {
        return this.edFileName;
    }

    public boolean validateInputs() {

        /*Validaciones de combos Mercado, Tipo de documento e Idiomas, se valida que este seleccionado*/
        int idms = -1;
        int idDocType = -1;
        int idLanguage = -1;
        StringBuilder fields = new StringBuilder();
        boolean b = true;
        boolean title = false;
        boolean tema = false;

        /*Validacion Combo mercado*/
        Object obj = cmbMercado.getSelectedItem();
        if (obj != null) {
            MarketBO marketb = (MarketBO) obj;
            idms = Integer.parseInt(marketb.getIdMiVector_real());
        }

        if (idms == -1) {
            //Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Mercado", JOptionPane.ERROR_MESSAGE);
            fields.append("\nMercado");
            b = false;
        }

        /* Validación Combo Tipo de Documento*/
        Object obj2 = cmbTipo.getSelectedItem();
        if (obj2 != null) {
            DocumentTypeBO tipoBO = (DocumentTypeBO) obj2;
            idDocType = tipoBO.getIddocument_type();
            title = tipoBO.isTitle();
            tema = tipoBO.isSubject();
        }

        if (idDocType == -1) {
            fields.append("\nTipo de documento");
            b = false;
        }

        /*Validación Combo Idiomas*/
        Object obj3 = cboIdioma.getSelectedItem();
        if (obj3 != null) {
            LanguageBO languageb = (LanguageBO) obj3;
            idLanguage = languageb.getIdLanguage();
        }

        if (idLanguage == -1) {
//            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Idioma", JOptionPane.ERROR_MESSAGE);
//            return false;
            fields.append("\nIdioma");
            b = false;
        }

        /*Validacion Combo tema*/
        if (tema) {
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
                fields.append("\nTema");
                b = false;
            }
        }

        if (title) {
            if (edNombredoc.getText().isEmpty()) {
                fields.append("\nTitulo requerido");
                b = false;
            }
        }

        if (edFileName.getText() == null || edFileName.getText().isEmpty()) {
            //Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre del archivo es obligatorio", JOptionPane.ERROR_MESSAGE);
            fields.append("\nNombre del archivo");
            b = false;

        }

        /*
         if (edNombredoc.getText() == null || edNombredoc.getText().isEmpty()) {
         Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Debe ingresar un tipo de documento", JOptionPane.ERROR_MESSAGE);
         return false;
         }
         */
        //Pattern p = Pattern.compile(GlobalDefines.ALPHA_NUMERIC_REGEX);
        //Matcher m = p.matcher(edFileName.getText());
        if (!validStringFile(edFileName.getText())) {//(false == m.matches()) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre de archivo no debe contener caracteres especiales", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!b) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Campos Requeridos. " + fields, JOptionPane.ERROR_MESSAGE);
        }

        return b;
    }

    private boolean validStringFile(String filename) {
        char[] com = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'á', 'é', 'í', 'ó', 'ú', 'Á',
            'É', 'Í', 'Ó', 'Ú', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        boolean retVal = false;
        for (char s : filename.toCharArray()) {
            retVal = false;
            for (char c : com) {
                if (c == s) {
                    retVal = true;
                    break;
                }
            }
            if (!retVal) {
                break;
            }
        }
        return retVal;
    }

    protected void searchDocuments() {
       // this.e

        this.btnOk.setEnabled(false);

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

        /*
         if( ( edDocName.getText()==null || edDocName.getText().isEmpty())
         && ( idMarket<=0   ) 
         && ( idDocType<=0  ) ){
         return;
         }      */
        DocumentDAO docDAO = new DocumentDAO();
        List<DocumentBO> lstDocuments = docDAO.searchCollaboratives(null, idMarket, idDocType, this.tipo);

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
        jxTable.getColumn(1).setPreferredWidth(200);
    }

    /*Llenar Combos*/
    private void fillCombos() {

        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo2_ = new MarketBO();
        /*Agregando valores por default al combo*/
        bo2_.setName("Seleccione");
        bo2_.setIdMiVector_real("-1");
        cmbMercado.addItem(bo2_);
        for (MarketBO bo : list) {
            cmbMercado.addItem(bo);
        }

        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        List<DocumentTypeBO> listDoc = null;
        if (this.isCollborative()) {
            listDoc = daoDoc.get(null, -1, 1);
        } else {
            listDoc = daoDoc.get(null, -1, 0);
        }

        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipo.addItem(boDoc);
        for (DocumentTypeBO boDoc2 : listDoc) {
            cmbTipo.addItem(boDoc2);
        }

        LanguageDAO lanDao = new LanguageDAO();
        List<LanguageBO> listLan = lanDao.get(null);
        LanguageBO lanBO = new LanguageBO();
        /*Agregando valores por default al combo*/
        lanBO.setName("Seleccione");
        lanBO.setIdLanguage(-1);
        cboIdioma.addItem(lanBO);
        for (LanguageBO lanBO2 : listLan) {
            cboIdioma.addItem(lanBO2);
            if(GlobalDefines.LANGUAGE_NOMENCLATURE_DEFAULT.equals( lanBO2.getNomenclature() )){
                cboIdioma.setSelectedItem(lanBO2);
            }
        }

    }

    private void filltipoDoc() {

        cmbTipo.removeAllItems();

        Object obj = cmbMercado.getSelectedItem();
        MarketBO boMarket = (MarketBO) obj;

        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipo.addItem(boDoc);
        
        
        if(boMarket.getIdMiVector_real().equals("-1")){
          for (DocumentTypeBO boDoc2 : listDoc) {
                cmbTipo.addItem(boDoc2);
        }
        }else{
          for (DocumentTypeBO boDoc2 : listDoc) {
            if (boDoc2.getIdMarket() == Integer.parseInt(boMarket.getIdMiVector_real()) && boDoc2.isCollaborative()) {
                cmbTipo.addItem(boDoc2);
            }
        }
        }
        
      
    }

    protected void nuevoDocumento() {
      // this.jxTable.re

        this.model.setData((List<DocumentBO>) null);
        this.model.fireTableDataChanged();

        this.btnOk.setEnabled(true);
        enableNuevo(true);
     //  this.btnBuscar.setEnabled(false);

    }

    protected void enableNuevo(boolean flag) {
        cboIdioma.setEnabled(flag);
        txtTema.setEnabled(flag);
        edNombredoc.setEnabled(flag);
        edFileName.setEnabled(flag);
        btnNewSubject.setEnabled(flag);
    }

    protected void accept() {

        int idx = this.jxTable.getSelectedRow();

        if (idx < 0) {
            return;
        }

        List<DocumentBO> list = this.model.getList();
        if (list == null || list.size() <= 0) {
            return;
        }

        DocumentBO docBO = list.get(idx);
        if (GlobalDefines.COLLAB_TYPE_DOC.equals(this.tipo)) {
            openDocumentCollabDoc(docBO.getDocumentId());
        } else {
            openDocumentCollabMod(docBO.getDocumentId());
        }

        ok = false;
        this.setVisible(false);

    }

    protected void openDocumentCollabMod(int documentId) {
        DocumentDAO docDAO = new DocumentDAO();
        try {
            DocumentCollabBO document = (DocumentCollabBO) docDAO.getDocument(documentId, 1,false);
            ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
            scrIntegradorMod.newDocument(document);
            scrIntegradorMod.loadDocument();
            //scrIntegradorDoc.loadDocument(document);
            MainView.main.setCollabIntegradorMod(scrIntegradorMod);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    protected void openDocumentCollabDoc(int documentId) {
        try {
            CollaborativesDAO collabDAO = new CollaborativesDAO();

            DocumentCollabBO document = collabDAO.getDocument(documentId);
            ScrIntegradorDoc scrIntegradorDoc = new ScrIntegradorDoc();
            scrIntegradorDoc.openDocument(document);
            //scrIntegradorDoc.loadDocument(document);
            MainView.main.setCollabIntegradorDoc(scrIntegradorDoc);

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
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

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        panel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbMercado = new javax.swing.JComboBox();
        cmbTipo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        favorite = new javax.swing.JCheckBox();
        btnBuscar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cboIdioma = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        edNombredoc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edFileName = new javax.swing.JTextField();
        btnNewSubject = new javax.swing.JButton();
        cboSubject = new javax.swing.JComboBox();
        txtTema = new javax.swing.JTextField();
        scrollGrid = new javax.swing.JScrollPane();
        btnNuevo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        btnOk.setText("Guardar");
        btnOk.setEnabled(false);
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        panel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        favorite.setText("Favorito");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(favorite, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(190, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(favorite)
                    .addComponent(btnBuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Idioma");

        cboIdioma.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboIdiomaItemStateChanged(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Tema");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Título del documento");

        edNombredoc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        edNombredoc.setPreferredSize(new java.awt.Dimension(440, 22));
        edNombredoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edNombredocActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre del archivo");

        edFileName.setPreferredSize(new java.awt.Dimension(440, 22));
        edFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edFileNameActionPerformed(evt);
            }
        });

        btnNewSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_add_.png"))); // NOI18N
        btnNewSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSubjectActionPerformed(evt);
            }
        });

        cboSubject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSubjectItemStateChanged(evt);
            }
        });
        cboSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubjectActionPerformed(evt);
            }
        });

        txtTema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTemaFocusLost(evt);
            }
        });
        txtTema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTemaMouseClicked(evt);
            }
        });
        txtTema.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTemaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(edNombredoc, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addComponent(edFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(122, 122, 122))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edNombredoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollGrid.setMaximumSize(new java.awt.Dimension(550, 32767));

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(5, 5, 5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 696, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel)
                    .addComponent(btnNuevo)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(327, Short.MAX_VALUE)
                    .addComponent(scrollGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(35, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        if (validateInputs() == false) {
            return;
        }
        ok = true;
        this.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Se perderán todos los datos, ¿Desea continuar?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            ok = false;
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cboIdiomaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboIdiomaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
    }//GEN-LAST:event_cboIdiomaItemStateChanged

    private void edNombredocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edNombredocActionPerformed

    }//GEN-LAST:event_edNombredocActionPerformed

    private void edFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edFileNameActionPerformed

    }//GEN-LAST:event_edFileNameActionPerformed

    private void btnNewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSubjectActionPerformed
        AddSubjectModal s = new AddSubjectModal(null, true);

        s.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                //initCboSubject();
                initAutoTema();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //initCboSubject();
                initAutoTema();
            }
        });

        s.setLocationRelativeTo(null);
        s.setVisible(true);
    }//GEN-LAST:event_btnNewSubjectActionPerformed

    private void cboSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSubjectItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
    }//GEN-LAST:event_cboSubjectItemStateChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        fillCombos();
    }//GEN-LAST:event_formComponentShown

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
            setIfisTitleRequired();
        }
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void cmbMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMercadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoActionPerformed

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
        filltipoDoc();

    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        searchDocuments();

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        nuevoDocumento();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtTemaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemaKeyTyped
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaKeyTyped

    private void txtTemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMouseClicked
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaMouseClicked

    private void txtTemaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusLost
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaFocusLost

    private void cboSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSubjectActionPerformed

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
            java.util.logging.Logger.getLogger(DlgSaveCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgSaveCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgSaveCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgSaveCollab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgSaveCollab dialog = new DlgSaveCollab(new javax.swing.JFrame(), true, "M");
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

    private void createFileName() {
        String nomenclaturaIdioma = "";
        String nomenclaturaMercado = "";
        String nomenclaturaTipoDocumento = "";
        String fecha = Utilerias.getCurrentTimeStamp(); /*yyyy/MM/dd*/

        String tema = "";

        MarketBO boMarket = (MarketBO) cmbMercado.getSelectedItem();
        if (boMarket != null && boMarket.getIdMarket() != -1) {
            nomenclaturaMercado = boMarket.getNomenclature();
        } else {
            nomenclaturaMercado = "";
        }

        DocumentTypeBO boDocType = (DocumentTypeBO) cmbTipo.getSelectedItem();
        if (boDocType != null && boDocType.getIddocument_type() != -1) {
            nomenclaturaTipoDocumento = boDocType.getNomenclature();
        } else {
            nomenclaturaTipoDocumento = "";
        }

        LanguageBO boLanguage = (LanguageBO) cboIdioma.getSelectedItem();
        if (boLanguage != null && boLanguage.getIdLanguage() != -1) {
            nomenclaturaIdioma = boLanguage.getNomenclature();
        } else {
            nomenclaturaIdioma = "";
        }

        SubjectBO boSubject = selectedValueSubject;//(SubjectBO) cboSubject.getSelectedItem();
        if (boSubject != null && boSubject.getIdSubject() != -1) {
            tema = boSubject.getName();
        } else {
            tema = "";
        }

        String fileName = String.format("%s%s%s%s%s",
                nomenclaturaIdioma == null ? "" : nomenclaturaIdioma,
                nomenclaturaMercado == null ? "" : nomenclaturaMercado,
                nomenclaturaTipoDocumento == null ? "" : nomenclaturaTipoDocumento,
                fecha,
                tema);

        fileName = fileName.replaceAll("\\s", "");
        edFileName.setText(fileName.toUpperCase(Locale.getDefault()));
    }

    private void setIfisTitleRequired() {
        Object obj = cmbTipo.getSelectedItem();
        DocumentTypeBO bo = (DocumentTypeBO) obj;
        if (bo.isTitle()) {
            this.setTitleRequired(true);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNewSubject;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cboIdioma;
    private javax.swing.JComboBox cboSubject;
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JTextField edFileName;
    private javax.swing.JTextField edNombredoc;
    private javax.swing.JCheckBox favorite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panel1;
    private javax.swing.JScrollPane scrollGrid;
    private javax.swing.JTextField txtTema;
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
            "Título",
            "Tipo de documento",
            "Mercado"
        };

        private final Class[] classes = new Class[]{
            String.class,
            String.class,
            String.class,
            String.class
        };

        public final Integer COLUMN_COUNT = 4;

        private Object[][] data = null;

        public SearchDocsCollabModel() {

        }

        public void setData(List<DocumentBO> list) {
            this.list = list;
            refreshData();
        }

        public void refreshData() {
            if (this.list == null) {
                data = null;
                return;
            }
            data = new Object[list == null ? 0 : list.size()][];
            for (int i = 0; i < data.length; i++) {
                DocumentBO row = list.get(i);

                data[i] = new Object[]{
                    row.getFileName(),
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
