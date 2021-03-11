package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.publish.AddSubjectModal;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgUpdate extends javax.swing.JDialog {

    private boolean ok;
    private boolean collborative = false;
    private boolean titleRequired = false;
    private TextAutoCompleter textAutoCompleter;
    private SubjectBO selectedValueSubject;

    public DlgUpdate(java.awt.Frame parent, boolean modal, boolean edicion) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(txtTema);
        initAutoTema();
        cboSubject.setVisible(false);
        Utilerias.addEscapeListener(this);
        //Utilerias.loadBinaryLookup(cboSubject);
        this.setTitle(edicion ? "Editar Documento" : "Nuevo Documento");
        if(edicion){
            DocumentBO bo = MainView.main.getScrDocument().getDocBO();
            if(bo != null && Utilerias.checkIfHasPublish(bo.getDocumentId())){
                cboIdioma.setEnabled(false);
            }
            /*btnNewSubject.setEnabled(false);
            txtTema.setEnabled(false);
            edNombredoc.setEnabled(false);
            edFileName.setEnabled(false);
            btnOk.setVisible(false);
            favorite.setEnabled(false);*/
        }
    }

    private void initAutoTema() {
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

    public SubjectBO getSelectedValueSubject() {
        return selectedValueSubject;
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
        if (edFileName.getText() == null || edFileName.getText().isEmpty()) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre del archivo es obligatorio", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Pattern p = Pattern.compile(GlobalDefines.ALPHA_NUMERIC_REGEX);
        Matcher m = p.matcher(edFileName.getText());
        if (false == m.matches()) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre de archivo no debe contener caracteres especiales", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (titleRequired) {
            if (edNombredoc.getText() == null || edNombredoc.getText().isEmpty()) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El titulo del documento es obligatorio", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        boolean title = false;
        boolean tema = false;

        /*Validaciones de combos Mercado, Tipo de documento e Idiomas, se valida que este seleccionado*/
        int idms;
        int idDocType;
        int idLanguage;

        /*Validacion Combo mercado*/
        Object obj = cmbMercado.getSelectedItem();
        if (obj != null) {
            MarketBO marketb = (MarketBO) obj;
            idms = Integer.parseInt(marketb.getIdMiVector_real());
        } else {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Mercado", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* Validación Combo Tipo de Documento*/
        Object obj2 = cmbTipo.getSelectedItem();
        if (obj2 != null) {
            DocumentTypeBO tipoBO = (DocumentTypeBO) obj2;
            idDocType = tipoBO.getIddocument_type();
            title = tipoBO.isTitle();
            tema = tipoBO.isSubject();
        } else {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Tipo de Documento", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (idDocType == -1) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Tipo de Documento", JOptionPane.ERROR_MESSAGE);
            return false;
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

            int ids;
            Object objSubject = selectedValueSubject;//cboSubject.getSelectedItem();
            if (objSubject != null) {
                SubjectBO sub = (SubjectBO) objSubject;
                ids = sub.getIdSubject();
            } else {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Tema", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (ids == -1) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione un Tema", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (txtTema.getText().trim().isEmpty() && selectedValueSubject != null) {
            selectedValueSubject = null;
        }

        if (title) {
            if (edNombredoc.getText().isEmpty()) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Introdusca un Titulo.", JOptionPane.ERROR_MESSAGE);
            }

            if (edNombredoc.getText().length() > 120) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El titulo del documento debe ser menor o igual a 120 caracteres.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        /*Validación Combo Idiomas*/
        Object obj3 = cboIdioma.getSelectedItem();
        if (obj3 != null) {
            LanguageBO languageb = (LanguageBO) obj3;
            idLanguage = languageb.getIdLanguage();
        } else {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Idioma", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (idLanguage == -1) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Seleccione Idioma", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
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
            cmbMercado.addItem(bo);
        }

        filltipoDoc();

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
        initAutoTema();

    }

    private void filltipoDoc() {

        cmbTipo.removeAllItems();
        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        List<DocumentTypeBO> listDoc;

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
        int id = 0;
        for (DocumentTypeBO boDoc2 : listDoc) {
            cmbTipo.addItem(boDoc2);
        }
    }

    private void initDatatoUpdate() {

        try {
            DocumentBO bo = MainView.main.getScrDocument().getDocBO();

            // Seleccionar el elemento en el combo para su edición (Idioma)
            for (int i = 0; i < cboIdioma.getItemCount(); i++) {
                LanguageBO obj = (LanguageBO) cboIdioma.getItemAt(i);
                if (obj.getIdLanguage() == bo.getIdLanguage()) {
                    cboIdioma.setSelectedIndex(i);
                    break;
                }
            }

            // Seleccionar el elemento en el combo para su edición (Mercado)
            for (int i = 0; i < cmbMercado.getItemCount(); i++) {
                MarketBO obj = (MarketBO) cmbMercado.getItemAt(i);
                if (Integer.valueOf(obj.getIdMiVector_real()) == bo.getIdMarket()) {
                    cmbMercado.setSelectedIndex(i);
                    break;
                }
            }

            // Seleccionar el elemento en el combo para su edición (Tipo de Documento)
            for (int i = 0; i < cmbTipo.getItemCount(); i++) {
                DocumentTypeBO obj = (DocumentTypeBO) cmbTipo.getItemAt(i);
                if (obj.getIddocument_type() == bo.getIdDocType()) {
                    cmbTipo.setSelectedIndex(i);
                    break;
                }
            }

            // Seleccionar el elemento en el combo para su edición (Tema)
            SubjectDAO daoSubject = new SubjectDAO();
            List<SubjectBO> listSubject = daoSubject.get(null);
            for (int i = 0; i < listSubject.size(); i++) {
                SubjectBO obj = listSubject.get(i);
                if (obj.getIdSubject() == bo.getIdSubject()) {
                    selectedValueSubject = obj;
                    txtTema.setText(obj.getName());
                    break;
                }
            }

            favorite.setSelected(bo.isFavorite());
            //falta favorito
            // Inicializar titulo
            edNombredoc.setText(bo.getDocumentName());
            // Inicializar nombre del archivo
            edFileName.setText(bo.getFileName());

            // es favorito?
            favorite.setSelected(bo.isFavorite());

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void initCboSubject() {
        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.get(null);
        SubjectBO suBO = new SubjectBO();
        suBO.setName("Seleccione");
        suBO.setIdSubject(-1);
        cboSubject.addItem(suBO);
        for (SubjectBO b : listSubject) {
            cboSubject.addItem(b);
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(456, 235));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnOk.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnOk.setText("Aceptar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        panel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Mercado");

        cmbMercado.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbMercado.setEnabled(false);
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

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTipo.setEnabled(false);
        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tipo de documento");

        favorite.setText("Favorito");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(favorite, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(favorite)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Idioma");

        cboIdioma.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cboIdioma.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboIdiomaItemStateChanged(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Tema");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Título del documento");

        edNombredoc.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        edNombredoc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        edNombredoc.setPreferredSize(new java.awt.Dimension(440, 22));
        edNombredoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edNombredocActionPerformed(evt);
            }
        });
        edNombredoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                edNombredocKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre del archivo");

        edFileName.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
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

        cboSubject.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cboSubject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSubjectItemStateChanged(evt);
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
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cboIdioma, 0, 269, Short.MAX_VALUE)
                        .addComponent(edNombredoc, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addComponent(edFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewSubject)))
                .addGap(177, 177, 177))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel))
                .addContainerGap())
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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        ok = false;
    }//GEN-LAST:event_formWindowActivated

    private void edNombredocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edNombredocActionPerformed

    }//GEN-LAST:event_edNombredocActionPerformed

    private void edFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edFileNameActionPerformed

    }//GEN-LAST:event_edFileNameActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        ok = false;
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ok = false;
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnNewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSubjectActionPerformed
        AddSubjectModal s = new AddSubjectModal(null, true);

        s.addWindowListener(new WindowAdapter() {

            @Override
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

    private void cmbMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMercadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        fillCombos();
        initDatatoUpdate();
        edNombredoc.requestFocusInWindow();
    }//GEN-LAST:event_formComponentShown

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        setIfisTitleRequired();
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void cboIdiomaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboIdiomaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
    }//GEN-LAST:event_cboIdiomaItemStateChanged

    private void cboSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSubjectItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }

    }//GEN-LAST:event_cboSubjectItemStateChanged

    private void txtTemaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusLost
        // TODO add your handling code here:
        listenerData();
        if(selectedValueSubject != null){
            createFileName();
        }
    }//GEN-LAST:event_txtTemaFocusLost

    private void txtTemaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemaKeyTyped
        // TODO add your handling code here:
        listenerData();
        if(selectedValueSubject != null){
            createFileName();
        }
//        System.out.println("typed");
    }//GEN-LAST:event_txtTemaKeyTyped

    private void txtTemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMouseClicked
//        // TODO add your handling code here:
        listenerData();
        if(selectedValueSubject != null){
            createFileName();
        }
    }//GEN-LAST:event_txtTemaMouseClicked

    private void edNombredocKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edNombredocKeyReleased

    }//GEN-LAST:event_edNombredocKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNewSubject;
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
    private javax.swing.JTextField txtTema;
    // End of variables declaration//GEN-END:variables
}
