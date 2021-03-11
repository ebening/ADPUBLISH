package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.publish.AddSubjectModal;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DlgSave extends javax.swing.JDialog {

    private boolean ok;
    private boolean collborative = false;
    private boolean titleRequired = false;
    private TextAutoCompleter textAutoCompleter;
    private SubjectBO selectedValueSubject;
    private List<DocumentTypeBO> listDocx;

    public SubjectBO getSelectedValueSubject() {
        return selectedValueSubject;
    }

    public void setSelectedValueSubject(SubjectBO selectedValueSubject) {
        this.selectedValueSubject = selectedValueSubject;
    }

    public static String remove1(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
        // Cadena de caracteres ASCII que reemplazar·n los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }//remove1

    public DlgSave(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        textAutoCompleter = new TextAutoCompleter(txtTema) {

            @Override
            protected boolean updateListData() {
                JList lista = getList();
                Object[] misItems = getItems();
                List<Object> itemsFiltrados = new ArrayList<>();
                String filtro = remove1(getTextComponent().getText().toLowerCase());

                if (filtro.length() > 0) {
                    for (Object object : misItems) {
                        if (remove1(object.toString().toLowerCase()).startsWith(filtro)) {
                            itemsFiltrados.add(object);
                        }
                    }
                }

                lista.setListData(itemsFiltrados.toArray());

                return true;
            }

        };
        initAutoTema();
        cboSubject.setVisible(false);
        Utilerias.addEscapeListener(this);
        Utilerias.loadBinaryLookup(cboSubject);
        this.setTitle("Nuevo Documento");
        initComboDocumentType();
    }

    private void initAutoTema() {
        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.get(null);
        textAutoCompleter.removeAllItems();
        for (SubjectBO b : listSubject) {
//            SubjectBO subjectBO = new SubjectBO();
//            subjectBO.setName(b.getName());
//            textAutoCompleter.addItem(subjectBO);
//            subjectBO.setName(remove1(b.getName()));
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

        if (txtTema.getText().trim().isEmpty()) {
            selectedValueSubject = null;
        }
    }

    private void initComboDocumentType() {
        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        if (this.isCollborative()) {
            listDocx = daoDoc.get(null, -1, 1);
        } else {
            listDocx = daoDoc.get(null, -1, 0);
        }
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

        /* ValidaciÛn Combo Tipo de Documento*/
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

        if (edNombredoc.getText().length() > 120) {
            fields.append("\nEl titulo del documento debe ser menor o igual a 120 caracteres. ");
            b = false;
        }

        /*ValidaciÛn Combo Idiomas*/
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

            int ids = -1;
            Object objSubject = selectedValueSubject;//cboSubject.getSelectedItem();
            if (objSubject != null) {
                SubjectBO sub = (SubjectBO) objSubject;
                ids = sub.getIdSubject();
            }

            if (ids == -1) {
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
        int idDocTypeOB = 0;
        Object objeto = cmbTipo.getSelectedItem();
        if (obj2 != null) {
            DocumentTypeBO tipoBO = (DocumentTypeBO) objeto;
            idDocTypeOB = tipoBO.getIddocument_type();
        }

        int perfilID = InstanceContext.getInstance().getUsuario().getPerfilId();
        DocumentTypeProfileDAO dao = new DocumentTypeProfileDAO();
        List<DocumentTypeProfileBO> lstPerfiles = dao.get(idDocTypeOB);

        Boolean bb = false;

        for (DocumentTypeProfileBO lst : lstPerfiles) {
            if (perfilID == lst.getIdprofile()) {
                bb = true;
                break;
            }
        }
        if (bb == false) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Este usuario no autorizado para editar este tipo de documento", JOptionPane.ERROR_MESSAGE);
            return false;
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
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El nombre de archivo no debe contener ninguno de los siguientes caracteres especiales:\n \\ / : * ? \" | = > < ,  ", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!b) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Campos Requeridos. " + fields, JOptionPane.ERROR_MESSAGE);
        }

        return b;
    }

    private boolean validStringFile(String filename) {
        char[] com = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', '—', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'Ò', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '·', 'È', 'Ì', 'Û', '˙', '¡',
            '…', 'Õ', '”', '⁄', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_', '%', 'ø', '!', '°',
            ',', '.', '@', '$'};
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

    private int marketSel;
    private int docTypeSel;

    /*Llenar Combos*/
    private void fillCombos() {

        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo_ = new MarketBO();
        bo_.setName("Seleccione");
        bo_.setIdMiVector_real("-1");
        cmbMercado.addItem(bo_);
        for (MarketBO bo : list) {
            cmbMercado.addItem(bo);
            if (bo.getIdMiVector_real().equals(String.valueOf(marketSel))) {
                bo_ = bo;
            }
        }
        if (bo_ != null) {
            cmbMercado.setSelectedItem(bo_);
        }

//        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
//        List<DocumentTypeBO> listDoc = null;
//        if (this.isCollborative()) {
//            listDoc = daoDoc.get(null, -1, 1);
//        } else {
//            listDoc = daoDoc.get(null, -1, 0);
//        }
        DocumentTypeBO boDoc = new DocumentTypeBO();
//        /*Agregando valores por default al combo*/
//        boDoc.setName("Seleccione");
//        boDoc.setIddocument_type(-1);
//        cmbTipo.addItem(boDoc);
//        boDoc = null;
//        for (DocumentTypeBO boDoc2 : listDoc) {
//            if (boDoc2.getIddocument_type() == docTypeSel) {
//                boDoc = boDoc2;
//            }
//            cmbTipo.addItem(boDoc2);
//        }

//        if (boDoc != null) {
//            cmbTipo.setSelectedItem(boDoc);
//        }
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
        
//        SubjectDAO daoSubject = new SubjectDAO();
//        List<SubjectBO> listSubject = daoSubject.get(null);
//        SubjectBO suBO = new SubjectBO();
//        suBO.setName("Seleccione");
//        suBO.setIdSubject(-1);
//        cboSubject.addItem(suBO);
//        for (SubjectBO b : listSubject) {
//            cboSubject.addItem(b);
//        }
    }

    private void filltipoDoc() {

        cmbTipo.removeAllItems();

        DocumentTypeBO boDoc = new DocumentTypeBO();
        /*Agregando valores por default al combo*/
        boDoc.setName("Seleccione");
        boDoc.setIddocument_type(-1);
        cmbTipo.addItem(boDoc);

        Object obj = cmbMercado.getSelectedItem();
        MarketBO boMarket = (MarketBO) obj;

        if (Integer.valueOf(boMarket.getIdMiVector_real()) == -1) {
            for (DocumentTypeBO boDoc2 : listDocx) {
                cmbTipo.addItem(boDoc2);
            }
        }

        if (boMarket.getIdMiVector_real() != null && Integer.valueOf(boMarket.getIdMiVector_real()) != -1) {
            DocumentTypeBO selDocType = null;
            for (DocumentTypeBO boDoc2 : listDocx) {
                if (boDoc2.getIdMarket() == Integer.valueOf(boMarket.getIdMiVector_real())) {
                    cmbTipo.addItem(boDoc2);
                    if(boDoc2.getIddocument_type() == docTypeSel){
                        selDocType = boDoc2;
                    }
                }
            }
            
            if(selDocType != null)
                cmbTipo.setSelectedItem(selDocType);
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

        btnOk.setText("Aceptar");
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
                    .addComponent(favorite, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jLabel1.setText("TÌtulo del documento");

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

        txtTema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTemaFocusLost(evt);
            }
        });
        txtTema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTemaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtTemaMousePressed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edNombredoc, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(edFileName, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(cboIdioma, 0, 269, Short.MAX_VALUE))
                .addGap(358, 358, 358))
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
                        .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel))
                .addGap(16, 16, 16))
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
        int option = JOptionPane.showConfirmDialog(null, "Se perder·n todos los datos, øDesea continuar?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            ok = false;
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        ok = false;
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

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

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        fillCombos();
    }//GEN-LAST:event_formComponentShown

    private void cboIdiomaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboIdiomaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
    }//GEN-LAST:event_cboIdiomaItemStateChanged

    private void cboSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSubjectItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
    }//GEN-LAST:event_cboSubjectItemStateChanged

    private void cmbMercadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMercadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoActionPerformed

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
        }
        filltipoDoc();
    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            createFileName();
            setIfisTitleRequired();
        }
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void txtTemaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemaKeyTyped
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaKeyTyped

    private void txtTemaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusLost
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
        createFileName();
    }//GEN-LAST:event_txtTemaFocusLost

    private void txtTemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMouseClicked
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaMouseClicked

    private void txtTemaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMousePressed
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            createFileName();
        }
    }//GEN-LAST:event_txtTemaMousePressed

    private void initCboSubject() {
        cboSubject.removeAllItems();
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

    public int getMarketSel() {
        return marketSel;
    }

    public void setMarketSel(int marketSel) {
        this.marketSel = marketSel;
    }

    public int getDocTypeSel() {
        return docTypeSel;
    }

    public void setDocTypeSel(int docTypeSel) {
        this.docTypeSel = docTypeSel;
    }


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
