package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.BulletsEditorBO;
import com.adinfi.formateador.bos.HeaderfooterBO;
import com.adinfi.formateador.bos.ImageDataBO;
import com.adinfi.formateador.bos.ImageInfoBO;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.OutgoingEmailBO;
import com.adinfi.formateador.bos.PublicityBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.TextEditorBO;
import com.adinfi.formateador.dao.BulletsEditorDAO;
import com.adinfi.formateador.dao.HeaderfooterDAO;
import com.adinfi.formateador.dao.ImageDataDAO;
import com.adinfi.formateador.dao.ImageInfoDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.OutgoingEmailDAO;
import com.adinfi.formateador.dao.PublicityDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.dao.TextEditorDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.EditableCellFocusAction;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.ImageInfoModel;
import com.adinfi.formateador.view.administration.tablemodel.IndustryModel;
import com.adinfi.formateador.view.administration.tablemodel.LanguageModel;
import com.adinfi.formateador.view.administration.tablemodel.MarketModel;
import com.adinfi.formateador.view.administration.tablemodel.OutgoingEmailModel;
import com.adinfi.formateador.view.administration.tablemodel.SubjectModel;
import com.adinfi.ws.data.DBResult;
import com.adinfi.ws.data.Data_Impl;
import com.adinfi.ws.data.IData_Stub;
import com.hexidec.ekit.EkitCore;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialClob;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Josué Sanchez,Guillermo Trejo
 */
public class Catalogs extends javax.swing.JDialog {

    String idForm = "";
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
    ButtonGroup selectPublicity = new ButtonGroup();

    private final Boolean subjectFirst = false;
    private Boolean languagesFirst = false;
    private Boolean industryFirst = false;
    private Boolean marketFirst = false;
    private Boolean imageLoadFirst = false;
    private Boolean outgoingEmailFirst = false;
    private String pathFile = "";

    private boolean isDeleteHeader = false;
    private boolean isDeleteFooter = false;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final Pattern pattern;
    private Matcher matcher;

    private EkitCore ekitCore3;

    /**
     * Creates new form Catalogo
     */
    // Etiquetas que cambian entre si la etiqueta
    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    // --------------------------------------------

    // Etiquetas que cambian entre si la etiqueta
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    // --------------------------------------------

    private final String DELETE_MESSAGE = "¿Desea eliminar?";

    public Catalogs(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        TabCatalogos.setSelectedIndex(-1);
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        //Utilerias.logger(getClass()).info(data);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuCatalogo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        TabCatalogos = new JTabbedPane_withoutPaintedTabs();
        subject = new javax.swing.JPanel();
        tableSubjectPanel = new javax.swing.JPanel();
        labelSubject = new javax.swing.JLabel();
        inputSubject = new javax.swing.JTextField();
        ScrollTableSubject = new javax.swing.JScrollPane();
        subjectTable = new javax.swing.JTable();
        searchSubject = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnNewSubject = new javax.swing.JButton();
        btnSaveSubject = new javax.swing.JButton();
        btnDeleteSubject = new javax.swing.JButton();
        languages = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        inputLanguage = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        languageTable = new javax.swing.JTable();
        btnFindLanguage = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnNewLanguage = new javax.swing.JButton();
        btnSaveLanguage = new javax.swing.JButton();
        btnDeleteLanguage = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        industry = new javax.swing.JPanel();
        industryScroll = new javax.swing.JScrollPane();
        industryTable = new javax.swing.JTable();
        inputIndustry = new javax.swing.JTextField();
        industryLabel = new javax.swing.JLabel();
        btnFindIndustry = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnNewIndustry = new javax.swing.JButton();
        btnSaveIndustry = new javax.swing.JButton();
        btnDeleteIndustry = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        Markets = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        inputMarket = new javax.swing.JTextField();
        marketLabel = new javax.swing.JLabel();
        industryScroll1 = new javax.swing.JScrollPane();
        marketTable = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnNewMarket = new javax.swing.JButton();
        btnSaveMarket = new javax.swing.JButton();
        btnDeleteMarket = new javax.swing.JButton();
        btnFindMarket = new javax.swing.JButton();
        imagesLoad = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        inputImageInfo = new javax.swing.JTextField();
        btnImageInfoSearch = new javax.swing.JButton();
        imageDataField = new javax.swing.JPanel();
        imageInLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        industryScroll3 = new javax.swing.JScrollPane();
        imageInfoTable = new javax.swing.JTable();
        imageButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnNewImageInfo = new javax.swing.JButton();
        btnSaveImageInfo = new javax.swing.JButton();
        btnDeleteImageInfo = new javax.swing.JButton();
        publicidad = new javax.swing.JPanel();
        panelPublicity = new javax.swing.JPanel();
        tabPublicity = new JTabbedPane_withoutPaintedTabs();
        textPublicity = new javax.swing.JPanel();
        textEditorCointainer2 = new javax.swing.JPanel();
        imagesPublicity = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        publicityVisor = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        descripcion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        urlImagePublicity1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        publicityImage = new javax.swing.JRadioButton();
        publicityText = new javax.swing.JRadioButton();
        jPanel17 = new javax.swing.JPanel();
        btnDeleteImageInfo1 = new javax.swing.JButton();
        btnSaveImageInfo1 = new javax.swing.JButton();
        textEditor = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnSaveTextEditor = new javax.swing.JButton();
        btnEditEditor = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        textEditorCointainer = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        mathSymbols = new javax.swing.JRadioButton();
        unorder = new javax.swing.JRadioButton();
        editorItalic = new javax.swing.JRadioButton();
        leftAlign = new javax.swing.JRadioButton();
        editorCut = new javax.swing.JRadioButton();
        editorCopy = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        centerAlign = new javax.swing.JRadioButton();
        strikeThrough = new javax.swing.JRadioButton();
        editorUndo = new javax.swing.JRadioButton();
        order = new javax.swing.JRadioButton();
        editorPaste = new javax.swing.JRadioButton();
        editorBold = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        editorUnderline = new javax.swing.JRadioButton();
        editorRedo = new javax.swing.JRadioButton();
        rightAlign = new javax.swing.JRadioButton();
        unicode = new javax.swing.JRadioButton();
        editorPastenoFormat = new javax.swing.JRadioButton();
        bulletsEditor = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        btnSaveTextEditor1 = new javax.swing.JButton();
        btnEditEditor1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        textEditorCointainer1 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        mathSymbols1 = new javax.swing.JRadioButton();
        unorder1 = new javax.swing.JRadioButton();
        editorItalic1 = new javax.swing.JRadioButton();
        leftAlign1 = new javax.swing.JRadioButton();
        editorCut1 = new javax.swing.JRadioButton();
        editorCopy1 = new javax.swing.JRadioButton();
        jPanel15 = new javax.swing.JPanel();
        centerAlign1 = new javax.swing.JRadioButton();
        strikeThrough1 = new javax.swing.JRadioButton();
        editorUndo1 = new javax.swing.JRadioButton();
        order1 = new javax.swing.JRadioButton();
        editorPaste1 = new javax.swing.JRadioButton();
        editorBold1 = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        editorUnderline1 = new javax.swing.JRadioButton();
        editorRedo1 = new javax.swing.JRadioButton();
        rightAlign1 = new javax.swing.JRadioButton();
        unicode1 = new javax.swing.JRadioButton();
        editorPastenoFormat1 = new javax.swing.JRadioButton();
        outgoingEmail = new javax.swing.JPanel();
        industry1 = new javax.swing.JPanel();
        industryScroll2 = new javax.swing.JScrollPane();
        OETable = new javax.swing.JTable();
        inputOE = new javax.swing.JTextField();
        OELabel = new javax.swing.JLabel();
        btnFindOE = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnNewOE = new javax.swing.JButton();
        btnSaveOE = new javax.swing.JButton();
        btnDeleteOE = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        headerFooter = new javax.swing.JPanel();
        panelPublicity1 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        footerVisor = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        headerVisor = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnDeleteImageInfo3 = new javax.swing.JButton();
        btnDeleteImageInfo4 = new javax.swing.JButton();
        Menu = new javax.swing.JPanel();
        btnSaveImageInfo2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Catálogos");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        menuCatalogo.setName("menuCatalogo"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Temas ", "Idiomas ", "Sectores ", "Mercados ", "Imágenes", "Publicidad", "Editor de texto", "Editor de bullets", "Correos Salientes", "Header & Footer" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setName("jList1"); // NOI18N
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout menuCatalogoLayout = new javax.swing.GroupLayout(menuCatalogo);
        menuCatalogo.setLayout(menuCatalogoLayout);
        menuCatalogoLayout.setHorizontalGroup(
            menuCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuCatalogoLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menuCatalogoLayout.setVerticalGroup(
            menuCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        TabCatalogos.setName("TabCatalogos"); // NOI18N
        TabCatalogos.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                TabCatalogosComponentShown(evt);
            }
        });

        subject.setName("subject"); // NOI18N
        subject.setPreferredSize(new java.awt.Dimension(648, 670));
        subject.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                subjectComponentShown(evt);
            }
        });

        tableSubjectPanel.setName("tableSubjectPanel"); // NOI18N

        labelSubject.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelSubject.setText("Búsqueda");
        labelSubject.setName("labelSubject"); // NOI18N

        inputSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputSubject.setName("inputSubject"); // NOI18N
        inputSubject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputSubjectKeyReleased(evt);
            }
        });

        ScrollTableSubject.setName("ScrollTableSubject"); // NOI18N

        subjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        subjectTable.setName("subjectTable"); // NOI18N
        subjectTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                subjectTableComponentShown(evt);
            }
        });
        ScrollTableSubject.setViewportView(subjectTable);

        searchSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        searchSubject.setName("searchSubject"); // NOI18N
        searchSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSubjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableSubjectPanelLayout = new javax.swing.GroupLayout(tableSubjectPanel);
        tableSubjectPanel.setLayout(tableSubjectPanelLayout);
        tableSubjectPanelLayout.setHorizontalGroup(
            tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(ScrollTableSubject))
                    .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                        .addComponent(labelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 415, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tableSubjectPanelLayout.setVerticalGroup(
            tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchSubject, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tableSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelSubject)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollTableSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("*Campo Requerido");
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        btnNewSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewSubject.setText("Nuevo");
        btnNewSubject.setName("btnNewSubject"); // NOI18N
        btnNewSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnNewSubject);

        btnSaveSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveSubject.setText("Guardar");
        btnSaveSubject.setEnabled(false);
        btnSaveSubject.setName("btnSaveSubject"); // NOI18N
        btnSaveSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnSaveSubject);

        btnDeleteSubject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteSubject.setText("Eliminar");
        btnDeleteSubject.setEnabled(false);
        btnDeleteSubject.setName("btnDeleteSubject"); // NOI18N
        btnDeleteSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSubjectActionPerformed(evt);
            }
        });
        jPanel4.add(btnDeleteSubject);

        javax.swing.GroupLayout subjectLayout = new javax.swing.GroupLayout(subject);
        subject.setLayout(subjectLayout);
        subjectLayout.setHorizontalGroup(
            subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectLayout.createSequentialGroup()
                .addGroup(subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(subjectLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(subjectLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE))))
                .addContainerGap())
        );
        subjectLayout.setVerticalGroup(
            subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableSubjectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        TabCatalogos.addTab("Temas", subject);

        languages.setName("languages"); // NOI18N
        languages.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                languagesComponentShown(evt);
            }
        });

        jPanel18.setName("jPanel18"); // NOI18N
        jPanel18.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel18ComponentShown(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Búsqueda");
        jLabel9.setName("jLabel9"); // NOI18N

        inputLanguage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputLanguage.setName("inputLanguage"); // NOI18N
        inputLanguage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputLanguageKeyReleased(evt);
            }
        });

        jScrollPane13.setName("jScrollPane13"); // NOI18N

        languageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        languageTable.getModel().addTableModelListener(languageTable);
        languageTable.setName("languageTable"); // NOI18N
        languageTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                languageTableComponentShown(evt);
            }
        });
        jScrollPane13.setViewportView(languageTable);

        btnFindLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnFindLanguage.setName("btnFindLanguage"); // NOI18N
        btnFindLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindLanguageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFindLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFindLanguage, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
        );

        jPanel2.setName("jPanel2"); // NOI18N

        btnNewLanguage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewLanguage.setText(NEW_LABEL);
        btnNewLanguage.setName("btnNewLanguage"); // NOI18N
        btnNewLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewLanguageActionPerformed(evt);
            }
        });
        jPanel2.add(btnNewLanguage);

        btnSaveLanguage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveLanguage.setText("Guardar");
        btnSaveLanguage.setEnabled(false);
        btnSaveLanguage.setName("btnSaveLanguage"); // NOI18N
        btnSaveLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveLanguageActionPerformed(evt);
            }
        });
        jPanel2.add(btnSaveLanguage);

        btnDeleteLanguage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteLanguage.setText("Eliminar");
        btnDeleteLanguage.setEnabled(false);
        btnDeleteLanguage.setName("btnDeleteLanguage"); // NOI18N
        btnDeleteLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLanguageActionPerformed(evt);
            }
        });
        jPanel2.add(btnDeleteLanguage);

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel1.setText("*Campo Requerido");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout languagesLayout = new javax.swing.GroupLayout(languages);
        languages.setLayout(languagesLayout);
        languagesLayout.setHorizontalGroup(
            languagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(languagesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(languagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(languagesLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        languagesLayout.setVerticalGroup(
            languagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(languagesLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );

        TabCatalogos.addTab("Idiomas", languages);

        industry.setName("industry"); // NOI18N
        industry.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                industryComponentShown(evt);
            }
        });

        industryScroll.setName("industryScroll"); // NOI18N

        industryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        industryTable.setName("industryTable"); // NOI18N
        industryScroll.setViewportView(industryTable);

        inputIndustry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputIndustry.setName("inputIndustry"); // NOI18N
        inputIndustry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputIndustryKeyReleased(evt);
            }
        });

        industryLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        industryLabel.setText("Búsqueda");
        industryLabel.setName("industryLabel"); // NOI18N

        btnFindIndustry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnFindIndustry.setName("btnFindIndustry"); // NOI18N
        btnFindIndustry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindIndustryActionPerformed(evt);
            }
        });

        jPanel7.setName("jPanel7"); // NOI18N

        btnNewIndustry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewIndustry.setText(NEW_LABEL);
        btnNewIndustry.setName("btnNewIndustry"); // NOI18N
        btnNewIndustry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewIndustryActionPerformed(evt);
            }
        });
        jPanel7.add(btnNewIndustry);

        btnSaveIndustry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveIndustry.setText("Guardar");
        btnSaveIndustry.setEnabled(false);
        btnSaveIndustry.setName("btnSaveIndustry"); // NOI18N
        btnSaveIndustry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveIndustryActionPerformed(evt);
            }
        });
        jPanel7.add(btnSaveIndustry);

        btnDeleteIndustry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteIndustry.setText("Eliminar");
        btnDeleteIndustry.setEnabled(false);
        btnDeleteIndustry.setName("btnDeleteIndustry"); // NOI18N
        btnDeleteIndustry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteIndustryActionPerformed(evt);
            }
        });
        jPanel7.add(btnDeleteIndustry);

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel8.setText("*Campo Requerido");
        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout industryLayout = new javax.swing.GroupLayout(industry);
        industry.setLayout(industryLayout);
        industryLayout.setHorizontalGroup(
            industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(industryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(industryScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(industryLayout.createSequentialGroup()
                        .addGroup(industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(industryLayout.createSequentialGroup()
                                .addComponent(industryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFindIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        industryLayout.setVerticalGroup(
            industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(industryLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFindIndustry, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(industryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(industryLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(industryScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        TabCatalogos.addTab("Sectores", industry);

        Markets.setName("Markets"); // NOI18N
        Markets.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                MarketsComponentShown(evt);
            }
        });

        jPanel21.setName("jPanel21"); // NOI18N

        inputMarket.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputMarket.setName("inputMarket"); // NOI18N
        inputMarket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputMarketKeyReleased(evt);
            }
        });

        marketLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        marketLabel.setText("Búsqueda");
        marketLabel.setName("marketLabel"); // NOI18N

        industryScroll1.setName("industryScroll1"); // NOI18N

        marketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        marketTable.setName("marketTable"); // NOI18N
        industryScroll1.setViewportView(marketTable);

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel10.setText("*Campo Requerido");
        jLabel10.setName("jLabel10"); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N

        btnNewMarket.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewMarket.setText(NEW_LABEL);
        btnNewMarket.setName("btnNewMarket"); // NOI18N
        btnNewMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewMarketActionPerformed(evt);
            }
        });
        jPanel8.add(btnNewMarket);

        btnSaveMarket.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveMarket.setText("Guardar");
        btnSaveMarket.setEnabled(false);
        btnSaveMarket.setName("btnSaveMarket"); // NOI18N
        btnSaveMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMarketActionPerformed(evt);
            }
        });
        jPanel8.add(btnSaveMarket);

        btnDeleteMarket.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteMarket.setText("Eliminar");
        btnDeleteMarket.setEnabled(false);
        btnDeleteMarket.setName("btnDeleteMarket"); // NOI18N
        btnDeleteMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMarketActionPerformed(evt);
            }
        });
        jPanel8.add(btnDeleteMarket);

        btnFindMarket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnFindMarket.setName("btnFindMarket"); // NOI18N
        btnFindMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindMarketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(industryScroll1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(marketLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFindMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFindMarket, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(marketLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(industryScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout MarketsLayout = new javax.swing.GroupLayout(Markets);
        Markets.setLayout(MarketsLayout);
        MarketsLayout.setHorizontalGroup(
            MarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MarketsLayout.setVerticalGroup(
            MarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TabCatalogos.addTab("Mercados", Markets);

        imagesLoad.setName("imagesLoad"); // NOI18N
        imagesLoad.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                imagesLoadComponentShown(evt);
            }
        });

        jPanel22.setName("jPanel22"); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Búsqueda");
        jLabel13.setName("jLabel13"); // NOI18N

        inputImageInfo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputImageInfo.setName("inputImageInfo"); // NOI18N
        inputImageInfo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputImageInfoKeyReleased(evt);
            }
        });

        btnImageInfoSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnImageInfoSearch.setName("btnImageInfoSearch"); // NOI18N
        btnImageInfoSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageInfoSearchActionPerformed(evt);
            }
        });

        imageDataField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        imageDataField.setName("imageDataField"); // NOI18N
        imageDataField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageDataFieldMouseClicked(evt);
            }
        });
        imageDataField.setLayout(new java.awt.BorderLayout());

        imageInLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        imageInLabel.setName("imageInLabel"); // NOI18N
        imageDataField.add(imageInLabel, java.awt.BorderLayout.CENTER);

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setText("*Campo Requerido");
        jLabel6.setName("jLabel6"); // NOI18N

        industryScroll3.setName("industryScroll3"); // NOI18N

        imageInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        imageInfoTable.setName("imageInfoTable"); // NOI18N
        industryScroll3.setViewportView(imageInfoTable);

        imageButton.setText("Seleccionar Imagen");
        imageButton.setEnabled(false);
        imageButton.setName("imageButton"); // NOI18N
        imageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputImageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImageInfoSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addContainerGap(435, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(imageButton))
                    .addComponent(imageDataField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(industryScroll3, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addGap(165, 165, 165)))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImageInfoSearch, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputImageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addGap(24, 24, 24)
                .addComponent(imageDataField, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(imageButton))
                .addContainerGap())
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addGap(81, 81, 81)
                    .addComponent(industryScroll3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );

        jPanel1.setName("jPanel1"); // NOI18N

        btnNewImageInfo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewImageInfo.setText("Nuevo");
        btnNewImageInfo.setName("btnNewImageInfo"); // NOI18N
        btnNewImageInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewImageInfoActionPerformed(evt);
            }
        });
        jPanel1.add(btnNewImageInfo);

        btnSaveImageInfo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveImageInfo.setText("Guardar");
        btnSaveImageInfo.setEnabled(false);
        btnSaveImageInfo.setName("btnSaveImageInfo"); // NOI18N
        btnSaveImageInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveImageInfoActionPerformed(evt);
            }
        });
        jPanel1.add(btnSaveImageInfo);

        btnDeleteImageInfo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteImageInfo.setText("Eliminar");
        btnDeleteImageInfo.setEnabled(false);
        btnDeleteImageInfo.setName("btnDeleteImageInfo"); // NOI18N
        btnDeleteImageInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteImageInfoActionPerformed(evt);
            }
        });
        jPanel1.add(btnDeleteImageInfo);

        javax.swing.GroupLayout imagesLoadLayout = new javax.swing.GroupLayout(imagesLoad);
        imagesLoad.setLayout(imagesLoadLayout);
        imagesLoadLayout.setHorizontalGroup(
            imagesLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(imagesLoadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        imagesLoadLayout.setVerticalGroup(
            imagesLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagesLoadLayout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        TabCatalogos.addTab("Imágenes", imagesLoad);

        publicidad.setName("publicidad"); // NOI18N
        publicidad.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                publicidadComponentShown(evt);
            }
        });

        panelPublicity.setName("panelPublicity"); // NOI18N

        tabPublicity.setName("tabPublicity"); // NOI18N

        textPublicity.setName("textPublicity"); // NOI18N

        textEditorCointainer2.setName("textEditorCointainer2"); // NOI18N
        textEditorCointainer2.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout textPublicityLayout = new javax.swing.GroupLayout(textPublicity);
        textPublicity.setLayout(textPublicityLayout);
        textPublicityLayout.setHorizontalGroup(
            textPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
            .addGroup(textPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(textEditorCointainer2, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
        );
        textPublicityLayout.setVerticalGroup(
            textPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(textPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(textPublicityLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(textEditorCointainer2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        tabPublicity.addTab("Text", textPublicity);

        imagesPublicity.setName("imagesPublicity"); // NOI18N

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setName("jPanel19"); // NOI18N

        publicityVisor.setBackground(new java.awt.Color(204, 102, 0));
        publicityVisor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        publicityVisor.setText("No Image");
        publicityVisor.setName("publicityVisor"); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(publicityVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(publicityVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );

        jButton1.setText("Examinar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel23.setName("jPanel23"); // NOI18N

        descripcion.setName("descripcion"); // NOI18N

        jLabel5.setText("URL");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel7.setText("Descripción");
        jLabel7.setName("jLabel7"); // NOI18N

        urlImagePublicity1.setName("urlImagePublicity1"); // NOI18N
        urlImagePublicity1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                urlImagePublicity1FocusLost(evt);
            }
        });
        urlImagePublicity1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                urlImagePublicity1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(urlImagePublicity1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(urlImagePublicity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jLabel15.setText("La imagen debe tener una resolución de 900x180px");
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout imagesPublicityLayout = new javax.swing.GroupLayout(imagesPublicity);
        imagesPublicity.setLayout(imagesPublicityLayout);
        imagesPublicityLayout.setHorizontalGroup(
            imagesPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagesPublicityLayout.createSequentialGroup()
                .addGroup(imagesPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addGroup(imagesPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(imagesPublicityLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1))
                        .addGroup(imagesPublicityLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        imagesPublicityLayout.setVerticalGroup(
            imagesPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagesPublicityLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagesPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabPublicity.addTab("Image", imagesPublicity);

        jPanel20.setName("jPanel20"); // NOI18N

        selectPublicity.add(publicityImage);
        publicityImage.setText("Imagen");
        publicityImage.setName("publicityImage"); // NOI18N
        publicityImage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                publicityImageItemStateChanged(evt);
            }
        });
        publicityImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicityImageActionPerformed(evt);
            }
        });

        selectPublicity.add(publicityText);
        publicityText.setSelected(true);
        publicityText.setText("Texto");
        publicityText.setName("publicityText"); // NOI18N
        publicityText.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                publicityTextItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(publicityImage)
                    .addComponent(publicityText))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(publicityText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(publicityImage)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPublicityLayout = new javax.swing.GroupLayout(panelPublicity);
        panelPublicity.setLayout(panelPublicityLayout);
        panelPublicityLayout.setHorizontalGroup(
            panelPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPublicityLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(tabPublicity)
                .addGap(35, 35, 35))
        );
        panelPublicityLayout.setVerticalGroup(
            panelPublicityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPublicityLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPublicity)
                .addContainerGap())
            .addGroup(panelPublicityLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(230, 230, 230))
        );

        jPanel17.setName("jPanel17"); // NOI18N

        btnDeleteImageInfo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteImageInfo1.setText("Eliminar");
        btnDeleteImageInfo1.setName("btnDeleteImageInfo1"); // NOI18N
        btnDeleteImageInfo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteImageInfo1ActionPerformed(evt);
            }
        });
        jPanel17.add(btnDeleteImageInfo1);

        btnSaveImageInfo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveImageInfo1.setText("Guardar");
        btnSaveImageInfo1.setName("btnSaveImageInfo1"); // NOI18N
        btnSaveImageInfo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveImageInfo1ActionPerformed(evt);
            }
        });
        jPanel17.add(btnSaveImageInfo1);

        javax.swing.GroupLayout publicidadLayout = new javax.swing.GroupLayout(publicidad);
        publicidad.setLayout(publicidadLayout);
        publicidadLayout.setHorizontalGroup(
            publicidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(publicidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(publicidadLayout.createSequentialGroup()
                .addComponent(panelPublicity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
        publicidadLayout.setVerticalGroup(
            publicidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(publicidadLayout.createSequentialGroup()
                .addComponent(panelPublicity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        TabCatalogos.addTab("Publicidad", publicidad);

        textEditor.setName("textEditor"); // NOI18N
        textEditor.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                textEditorComponentShown(evt);
            }
        });

        jPanel10.setName("jPanel10"); // NOI18N

        btnSaveTextEditor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveTextEditor.setText("Guardar");
        btnSaveTextEditor.setEnabled(false);
        btnSaveTextEditor.setName("btnSaveTextEditor"); // NOI18N
        btnSaveTextEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTextEditorActionPerformed(evt);
            }
        });
        jPanel10.add(btnSaveTextEditor);

        btnEditEditor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditEditor.setText("Editar");
        btnEditEditor.setName("btnEditEditor"); // NOI18N
        btnEditEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEditorActionPerformed(evt);
            }
        });
        jPanel10.add(btnEditEditor);

        jPanel6.setName("jPanel6"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Opciones del editor de texto");
        jLabel3.setName("jLabel3"); // NOI18N

        textEditorCointainer.setName("textEditorCointainer"); // NOI18N
        textEditorCointainer.setLayout(new java.awt.GridBagLayout());

        jPanel3.setName("jPanel3"); // NOI18N

        mathSymbols.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mathSymbols.setText("Símbolos matemáticos");
        mathSymbols.setEnabled(false);
        mathSymbols.setName("mathSymbols"); // NOI18N

        unorder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        unorder.setText("Lista no ordenada");
        unorder.setEnabled(false);
        unorder.setName("unorder"); // NOI18N

        editorItalic.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorItalic.setText("Itálica");
        editorItalic.setEnabled(false);
        editorItalic.setName("editorItalic"); // NOI18N

        leftAlign.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        leftAlign.setText("Alinear texto izquierda");
        leftAlign.setEnabled(false);
        leftAlign.setName("leftAlign"); // NOI18N

        editorCut.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorCut.setText("Cortar");
        editorCut.setEnabled(false);
        editorCut.setName("editorCut"); // NOI18N

        editorCopy.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorCopy.setText("Copiar");
        editorCopy.setEnabled(false);
        editorCopy.setName("editorCopy"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editorCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorCut, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorItalic, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leftAlign, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unorder, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mathSymbols, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(editorCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorCut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorItalic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leftAlign)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unorder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mathSymbols)
                .addContainerGap())
        );

        jPanel5.setName("jPanel5"); // NOI18N

        centerAlign.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        centerAlign.setText("Alinear texto centro");
        centerAlign.setEnabled(false);
        centerAlign.setName("centerAlign"); // NOI18N

        strikeThrough.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        strikeThrough.setText("Subrayado");
        strikeThrough.setEnabled(false);
        strikeThrough.setName("strikeThrough"); // NOI18N

        editorUndo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorUndo.setText("Deshacer");
        editorUndo.setEnabled(false);
        editorUndo.setName("editorUndo"); // NOI18N

        order.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        order.setText("Lista ordenada");
        order.setEnabled(false);
        order.setName("order"); // NOI18N

        editorPaste.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorPaste.setText("Pegar");
        editorPaste.setEnabled(false);
        editorPaste.setName("editorPaste"); // NOI18N

        editorBold.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorBold.setText("Negrita");
        editorBold.setEnabled(false);
        editorBold.setName("editorBold"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editorPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorBold, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(centerAlign)
                    .addComponent(order, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strikeThrough, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(editorPaste)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorBold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorUndo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(centerAlign)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(order)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(strikeThrough)
                .addGap(324, 324, 324))
        );

        jPanel11.setName("jPanel11"); // NOI18N

        editorUnderline.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorUnderline.setText("Subrayado");
        editorUnderline.setEnabled(false);
        editorUnderline.setName("editorUnderline"); // NOI18N

        editorRedo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorRedo.setText("Rehacer");
        editorRedo.setEnabled(false);
        editorRedo.setName("editorRedo"); // NOI18N

        rightAlign.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rightAlign.setText("Alinear texto derecha");
        rightAlign.setEnabled(false);
        rightAlign.setName("rightAlign"); // NOI18N

        unicode.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        unicode.setText("Caracteres unicode");
        unicode.setEnabled(false);
        unicode.setName("unicode"); // NOI18N

        editorPastenoFormat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorPastenoFormat.setText("Pegar sin formato");
        editorPastenoFormat.setEnabled(false);
        editorPastenoFormat.setName("editorPastenoFormat"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(editorPastenoFormat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editorUnderline, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editorRedo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rightAlign, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(unicode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 18, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(editorPastenoFormat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorUnderline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorRedo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightAlign)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unicode)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(437, 437, 437))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE))
                            .addComponent(textEditorCointainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textEditorCointainer, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout textEditorLayout = new javax.swing.GroupLayout(textEditor);
        textEditor.setLayout(textEditorLayout);
        textEditorLayout.setHorizontalGroup(
            textEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        textEditorLayout.setVerticalGroup(
            textEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, textEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        TabCatalogos.addTab("Editor de Texto", textEditor);

        bulletsEditor.setName("bulletsEditor"); // NOI18N
        bulletsEditor.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bulletsEditorComponentShown(evt);
            }
        });

        jPanel12.setName("jPanel12"); // NOI18N

        btnSaveTextEditor1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveTextEditor1.setText("Guardar");
        btnSaveTextEditor1.setEnabled(false);
        btnSaveTextEditor1.setName("btnSaveTextEditor1"); // NOI18N
        btnSaveTextEditor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTextEditor1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnSaveTextEditor1);

        btnEditEditor1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditEditor1.setText("Editar");
        btnEditEditor1.setName("btnEditEditor1"); // NOI18N
        btnEditEditor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEditor1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnEditEditor1);

        jPanel13.setName("jPanel13"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Opciones del editor de bullets");
        jLabel4.setName("jLabel4"); // NOI18N

        textEditorCointainer1.setName("textEditorCointainer1"); // NOI18N
        textEditorCointainer1.setLayout(new java.awt.GridBagLayout());

        jPanel14.setName("jPanel14"); // NOI18N

        mathSymbols1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mathSymbols1.setText("Símbolos matemáticos");
        mathSymbols1.setEnabled(false);
        mathSymbols1.setName("mathSymbols1"); // NOI18N

        unorder1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        unorder1.setText("Lista no ordenada");
        unorder1.setEnabled(false);
        unorder1.setName("unorder1"); // NOI18N

        editorItalic1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorItalic1.setText("Itálica");
        editorItalic1.setEnabled(false);
        editorItalic1.setName("editorItalic1"); // NOI18N

        leftAlign1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        leftAlign1.setText("Alinear texto izquierda");
        leftAlign1.setEnabled(false);
        leftAlign1.setName("leftAlign1"); // NOI18N

        editorCut1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorCut1.setText("Cortar");
        editorCut1.setEnabled(false);
        editorCut1.setName("editorCut1"); // NOI18N

        editorCopy1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorCopy1.setText("Copiar");
        editorCopy1.setEnabled(false);
        editorCopy1.setName("editorCopy1"); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editorCopy1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorCut1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorItalic1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leftAlign1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unorder1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mathSymbols1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(editorCopy1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorCut1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorItalic1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leftAlign1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unorder1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mathSymbols1)
                .addContainerGap())
        );

        jPanel15.setName("jPanel15"); // NOI18N

        centerAlign1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        centerAlign1.setText("Alinear texto centro");
        centerAlign1.setEnabled(false);
        centerAlign1.setName("centerAlign1"); // NOI18N

        strikeThrough1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        strikeThrough1.setText("Subrayado");
        strikeThrough1.setEnabled(false);
        strikeThrough1.setName("strikeThrough1"); // NOI18N

        editorUndo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorUndo1.setText("Deshacer");
        editorUndo1.setEnabled(false);
        editorUndo1.setName("editorUndo1"); // NOI18N

        order1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        order1.setText("Lista ordenada");
        order1.setEnabled(false);
        order1.setName("order1"); // NOI18N

        editorPaste1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorPaste1.setText("Pegar");
        editorPaste1.setEnabled(false);
        editorPaste1.setName("editorPaste1"); // NOI18N

        editorBold1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorBold1.setText("Negrita");
        editorBold1.setEnabled(false);
        editorBold1.setName("editorBold1"); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editorPaste1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorBold1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editorUndo1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(centerAlign1)
                    .addComponent(order1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strikeThrough1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(editorPaste1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorBold1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorUndo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(centerAlign1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(order1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(strikeThrough1)
                .addGap(324, 324, 324))
        );

        jPanel16.setName("jPanel16"); // NOI18N

        editorUnderline1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorUnderline1.setText("Subrayado");
        editorUnderline1.setEnabled(false);
        editorUnderline1.setName("editorUnderline1"); // NOI18N

        editorRedo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorRedo1.setText("Rehacer");
        editorRedo1.setEnabled(false);
        editorRedo1.setName("editorRedo1"); // NOI18N

        rightAlign1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rightAlign1.setText("Alinear texto derecha");
        rightAlign1.setEnabled(false);
        rightAlign1.setName("rightAlign1"); // NOI18N

        unicode1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        unicode1.setText("Caracteres unicode");
        unicode1.setEnabled(false);
        unicode1.setName("unicode1"); // NOI18N

        editorPastenoFormat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        editorPastenoFormat1.setText("Pegar sin formato");
        editorPastenoFormat1.setEnabled(false);
        editorPastenoFormat1.setName("editorPastenoFormat1"); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(editorPastenoFormat1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editorUnderline1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editorRedo1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rightAlign1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(unicode1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 18, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(editorPastenoFormat1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorUnderline1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorRedo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightAlign1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unicode1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(437, 437, 437))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE))
                            .addComponent(textEditorCointainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textEditorCointainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bulletsEditorLayout = new javax.swing.GroupLayout(bulletsEditor);
        bulletsEditor.setLayout(bulletsEditorLayout);
        bulletsEditorLayout.setHorizontalGroup(
            bulletsEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bulletsEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bulletsEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        bulletsEditorLayout.setVerticalGroup(
            bulletsEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bulletsEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        TabCatalogos.addTab("Editor de Bullets", bulletsEditor);

        outgoingEmail.setName("outgoingEmail"); // NOI18N
        outgoingEmail.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                outgoingEmailComponentShown(evt);
            }
        });

        industry1.setName("industry1"); // NOI18N
        industry1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                industry1ComponentShown(evt);
            }
        });

        industryScroll2.setName("industryScroll2"); // NOI18N

        OETable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        OETable.setName("OETable"); // NOI18N
        industryScroll2.setViewportView(OETable);

        inputOE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputOE.setName("inputOE"); // NOI18N
        inputOE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputOEKeyReleased(evt);
            }
        });

        OELabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        OELabel.setText("Búsqueda");
        OELabel.setName("OELabel"); // NOI18N

        btnFindOE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        btnFindOE.setName("btnFindOE"); // NOI18N
        btnFindOE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindOEActionPerformed(evt);
            }
        });

        jPanel9.setName("jPanel9"); // NOI18N

        btnNewOE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewOE.setText(NEW_LABEL);
        btnNewOE.setName("btnNewOE"); // NOI18N
        btnNewOE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOEActionPerformed(evt);
            }
        });
        jPanel9.add(btnNewOE);

        btnSaveOE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveOE.setText("Guardar");
        btnSaveOE.setEnabled(false);
        btnSaveOE.setName("btnSaveOE"); // NOI18N
        btnSaveOE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOEActionPerformed(evt);
            }
        });
        jPanel9.add(btnSaveOE);

        btnDeleteOE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteOE.setText("Eliminar");
        btnDeleteOE.setEnabled(false);
        btnDeleteOE.setName("btnDeleteOE"); // NOI18N
        btnDeleteOE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteOEActionPerformed(evt);
            }
        });
        jPanel9.add(btnDeleteOE);

        jLabel11.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel11.setText("*Campo Requerido");
        jLabel11.setName("jLabel11"); // NOI18N

        javax.swing.GroupLayout industry1Layout = new javax.swing.GroupLayout(industry1);
        industry1.setLayout(industry1Layout);
        industry1Layout.setHorizontalGroup(
            industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(industry1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(industryScroll2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(industry1Layout.createSequentialGroup()
                        .addGroup(industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(industry1Layout.createSequentialGroup()
                                .addComponent(OELabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputOE, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFindOE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        industry1Layout.setVerticalGroup(
            industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(industry1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFindOE, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(industry1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputOE, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(OELabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(industryScroll2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout outgoingEmailLayout = new javax.swing.GroupLayout(outgoingEmail);
        outgoingEmail.setLayout(outgoingEmailLayout);
        outgoingEmailLayout.setHorizontalGroup(
            outgoingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
            .addGroup(outgoingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(outgoingEmailLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(industry1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        outgoingEmailLayout.setVerticalGroup(
            outgoingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
            .addGroup(outgoingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(outgoingEmailLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(industry1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        TabCatalogos.addTab("Correos Salientes", outgoingEmail);

        headerFooter.setName("headerFooter"); // NOI18N
        headerFooter.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                headerFooterComponentShown(evt);
            }
        });

        panelPublicity1.setName("panelPublicity1"); // NOI18N

        jPanel24.setName("jPanel24"); // NOI18N

        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel25.setName("jPanel25"); // NOI18N

        footerVisor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        footerVisor.setText("No Footer");
        footerVisor.setName("footerVisor"); // NOI18N

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footerVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footerVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel26.setName("jPanel26"); // NOI18N

        headerVisor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerVisor.setText("No Header");
        headerVisor.setName("headerVisor"); // NOI18N

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerVisor, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );

        jButton2.setText("Examinar");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Examinar");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel12.setText("Header:");
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel14.setText("Footer:");
        jLabel14.setName("jLabel14"); // NOI18N

        btnDeleteImageInfo3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteImageInfo3.setText("X");
        btnDeleteImageInfo3.setName("btnDeleteImageInfo3"); // NOI18N
        btnDeleteImageInfo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteImageInfo3ActionPerformed(evt);
            }
        });

        btnDeleteImageInfo4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteImageInfo4.setText("X");
        btnDeleteImageInfo4.setName("btnDeleteImageInfo4"); // NOI18N
        btnDeleteImageInfo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteImageInfo4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDeleteImageInfo3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteImageInfo4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jLabel12))
                        .addGap(87, 87, 87)
                        .addComponent(btnDeleteImageInfo3))
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 35, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteImageInfo4))
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout panelPublicity1Layout = new javax.swing.GroupLayout(panelPublicity1);
        panelPublicity1.setLayout(panelPublicity1Layout);
        panelPublicity1Layout.setHorizontalGroup(
            panelPublicity1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPublicity1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelPublicity1Layout.setVerticalGroup(
            panelPublicity1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPublicity1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Menu.setName("Menu"); // NOI18N

        btnSaveImageInfo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveImageInfo2.setText("Guardar");
        btnSaveImageInfo2.setName("btnSaveImageInfo2"); // NOI18N
        btnSaveImageInfo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveImageInfo2ActionPerformed(evt);
            }
        });
        Menu.add(btnSaveImageInfo2);

        javax.swing.GroupLayout headerFooterLayout = new javax.swing.GroupLayout(headerFooter);
        headerFooter.setLayout(headerFooterLayout);
        headerFooterLayout.setHorizontalGroup(
            headerFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(headerFooterLayout.createSequentialGroup()
                .addComponent(panelPublicity1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
        headerFooterLayout.setVerticalGroup(
            headerFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerFooterLayout.createSequentialGroup()
                .addComponent(panelPublicity1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        TabCatalogos.addTab("Header & Footer", headerFooter);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuCatalogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TabCatalogos)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabCatalogos, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addComponent(menuCatalogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        addIndustryModel();
        addLanguageModel();
        addSubjectModel();
        addMarketModel();
        addOutgoingEmailModel();
        addImageInfoModel();
        getPublicityEditorValues();
        initHeaderFooter();
        initPublicity();

    }//GEN-LAST:event_formComponentShown

    private void TabCatalogosComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_TabCatalogosComponentShown
    }//GEN-LAST:event_TabCatalogosComponentShown

    private void industry1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_industry1ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_industry1ComponentShown

    private void btnDeleteOEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteOEActionPerformed
        deleteOutgoingEmail();
    }//GEN-LAST:event_btnDeleteOEActionPerformed

    private void btnSaveOEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOEActionPerformed
        insertUpdateOutgoingEmail();
    }//GEN-LAST:event_btnSaveOEActionPerformed

    private void btnNewOEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOEActionPerformed
        if (btnNewOE.getText().equals(CANCEL_LABEL)) {
            btnNewOE.setText(NEW_LABEL);
            btnSaveOE.setEnabled(false);
            cancelNewOutgoingEmail();
        } else if (btnNewOE.getText().equals(NEW_LABEL)) {
            btnNewOE.setText(CANCEL_LABEL);
            btnSaveOE.setEnabled(true);
            newOutgoingEmail();
        }
    }//GEN-LAST:event_btnNewOEActionPerformed

    private void btnFindOEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindOEActionPerformed
        findOutgoingEmail(inputOE.getText());
    }//GEN-LAST:event_btnFindOEActionPerformed

    private void inputOEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputOEKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findOutgoingEmail(inputOE.getText());
        }
    }//GEN-LAST:event_inputOEKeyReleased

    private void textEditorComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_textEditorComponentShown
        getEditorValues();
    }//GEN-LAST:event_textEditorComponentShown

    private void btnEditEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEditorActionPerformed
        if (btnEditEditor.getText().equals(EDIT_LABEL)) {
            setEnabledEditor(true);
            btnEditEditor.setText(CANCEL_LABEL);
            btnSaveTextEditor.setEnabled(true);
        } else {
            setEnabledEditor(false);
            btnEditEditor.setText(EDIT_LABEL);
            btnSaveTextEditor.setEnabled(false);
        }
    }//GEN-LAST:event_btnEditEditorActionPerformed

    private void btnSaveTextEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTextEditorActionPerformed
        saveTextEditor();
    }//GEN-LAST:event_btnSaveTextEditorActionPerformed

    private void btnNewImageInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewImageInfoActionPerformed
        if (btnNewImageInfo.getText().equals(CANCEL_LABEL)) {
            btnNewImageInfo.setText(NEW_LABEL);
            btnSaveImageInfo.setText(SAVE_LABEL);
            btnDeleteImageInfo.setEnabled(false);
            btnSaveImageInfo.setEnabled(false);
            cancelNewImageInfo();
        } else if (btnNewImageInfo.getText().equals(NEW_LABEL)) {
            btnNewImageInfo.setText(CANCEL_LABEL);
            btnSaveImageInfo.setEnabled(true);
            imageInLabel.setIcon(null);
            newImageInfo();
        }
    }//GEN-LAST:event_btnNewImageInfoActionPerformed

    private void btnSaveImageInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveImageInfoActionPerformed
        insertUpdateImageInfo();
    }//GEN-LAST:event_btnSaveImageInfoActionPerformed

    private void btnDeleteImageInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteImageInfoActionPerformed
        deleteImageInfo();
    }//GEN-LAST:event_btnDeleteImageInfoActionPerformed

    private void imageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageButtonActionPerformed
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage resizedImage = resize(image, 150, 160);
                ImageIcon ii = new ImageIcon(resizedImage);
                imageInLabel.setIcon(ii);
            } catch (IOException ex) {
                Logger.getLogger(Catalogs.class.getName()).log(Level.SEVERE, null, ex);
            }
            pathFile = file.getAbsolutePath();
        } else {
            //imageInLabel.setText("");
            pathFile = "";
        }
    }//GEN-LAST:event_imageButtonActionPerformed

    private void imageDataFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageDataFieldMouseClicked

        if (imageInfoTable.getRowCount() > 0) {
            ImageVisor visor = new ImageVisor(null, true);
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - visor.getWidth()) / 2;
            final int y = (screenSize.height - visor.getHeight()) / 2;
            visor.setLocation(x, y);
            visor.setVisible(true);
        }
    }//GEN-LAST:event_imageDataFieldMouseClicked

    private void btnImageInfoSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageInfoSearchActionPerformed
        findImageInfo(inputImageInfo.getText());
    }//GEN-LAST:event_btnImageInfoSearchActionPerformed

    private void btnFindMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindMarketActionPerformed
        findMarket(inputMarket.getText());
    }//GEN-LAST:event_btnFindMarketActionPerformed

    private void btnDeleteMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMarketActionPerformed
        deleteMarket();
    }//GEN-LAST:event_btnDeleteMarketActionPerformed

    private void btnSaveMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMarketActionPerformed
        MarketModel model = (MarketModel) marketTable.getModel();
        model.setEditable0(true);
        insertUpdateMarket();
    }//GEN-LAST:event_btnSaveMarketActionPerformed

    private void btnNewMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewMarketActionPerformed
        if (btnNewMarket.getText().equals(CANCEL_LABEL)) {
            btnNewMarket.setText(NEW_LABEL);
            btnSaveMarket.setEnabled(false);
            cancelNewMarket();
        } else if (btnNewMarket.getText().equals(NEW_LABEL)) {
            btnNewMarket.setText(CANCEL_LABEL);
            btnSaveMarket.setEnabled(true);
            newMarket();
        }
    }//GEN-LAST:event_btnNewMarketActionPerformed

    private void inputMarketKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputMarketKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findMarket(inputMarket.getText());
        }
    }//GEN-LAST:event_inputMarketKeyReleased

    private void industryComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_industryComponentShown
        if (!industryFirst) {
            findIndustry(inputIndustry.getText());
            industryFirst = true;
        }
        btnSaveIndustry.setFocusable(true);
        industryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_industryComponentShown

    private void btnDeleteIndustryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteIndustryActionPerformed
        deleteIndustry();
    }//GEN-LAST:event_btnDeleteIndustryActionPerformed

    private void btnSaveIndustryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveIndustryActionPerformed
        insertUpdateIndustry();
    }//GEN-LAST:event_btnSaveIndustryActionPerformed

    private void btnNewIndustryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewIndustryActionPerformed
        switch (btnNewIndustry.getText()) {
            case CANCEL_LABEL:
                btnNewIndustry.setText(NEW_LABEL);
                btnSaveIndustry.setEnabled(false);
                cancelNewIndustry();
                break;
            case NEW_LABEL:
                btnNewIndustry.setText(CANCEL_LABEL);
                btnSaveIndustry.setEnabled(true);
                newIndustry();
                break;
        }
    }//GEN-LAST:event_btnNewIndustryActionPerformed

    private void btnFindIndustryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindIndustryActionPerformed
        findIndustry(inputIndustry.getText());
    }//GEN-LAST:event_btnFindIndustryActionPerformed

    private void inputIndustryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputIndustryKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findIndustry(inputIndustry.getText());
        }
    }//GEN-LAST:event_inputIndustryKeyReleased

    private void languagesComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_languagesComponentShown
        if (!languagesFirst) {
            findLanguage(inputLanguage.getText());
            languagesFirst = true;
        }
        btnSaveLanguage.setFocusable(true);
        languageTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_languagesComponentShown

    private void btnDeleteLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLanguageActionPerformed
        deleteLanguage();
    }//GEN-LAST:event_btnDeleteLanguageActionPerformed

    private void btnSaveLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveLanguageActionPerformed
        LanguageModel model = (LanguageModel) languageTable.getModel();
        model.setEditable0(true);
        insertUpdateLanguage();
    }//GEN-LAST:event_btnSaveLanguageActionPerformed

    private void btnNewLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewLanguageActionPerformed
        if (btnNewLanguage.getText().equals(CANCEL_LABEL)) {
            btnNewLanguage.setText(NEW_LABEL);
            btnSaveLanguage.setEnabled(false);
            cancelNewLanguage();
        } else if (btnNewLanguage.getText().equals(NEW_LABEL)) {
            btnNewLanguage.setText(CANCEL_LABEL);
            btnSaveLanguage.setEnabled(true);
            newLanguage();
        }
    }//GEN-LAST:event_btnNewLanguageActionPerformed

    private void jPanel18ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel18ComponentShown

    }//GEN-LAST:event_jPanel18ComponentShown

    private void btnFindLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindLanguageActionPerformed
        findLanguage(null);
        if (btnNewLanguage.getText().equals(CANCEL_LABEL)) {
            btnNewLanguage.setText(NEW_LABEL);
            btnSaveLanguage.setEnabled(false);
        }
    }//GEN-LAST:event_btnFindLanguageActionPerformed

    private void languageTableComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_languageTableComponentShown

    }//GEN-LAST:event_languageTableComponentShown

    private void inputLanguageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputLanguageKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findLanguage(inputLanguage.getText());
        }
    }//GEN-LAST:event_inputLanguageKeyReleased

    private void btnDeleteSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSubjectActionPerformed
        deleteSubject();
    }//GEN-LAST:event_btnDeleteSubjectActionPerformed

    private void btnSaveSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSubjectActionPerformed
        insertUpdateSubject();
    }//GEN-LAST:event_btnSaveSubjectActionPerformed

    private void btnNewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSubjectActionPerformed
        if (btnNewSubject.getText().equals(CANCEL_LABEL)) {
            btnNewSubject.setText(NEW_LABEL);
            btnSaveSubject.setEnabled(false);
            cancelNewSubject();
        } else if (btnNewSubject.getText().equals(NEW_LABEL)) {
            btnNewSubject.setText(CANCEL_LABEL);
            btnSaveSubject.setEnabled(true);
            newSubject();
        }
    }//GEN-LAST:event_btnNewSubjectActionPerformed

    private void searchSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSubjectActionPerformed
        findSubject(inputSubject.getText());
    }//GEN-LAST:event_searchSubjectActionPerformed

    private void subjectTableComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_subjectTableComponentShown

    }//GEN-LAST:event_subjectTableComponentShown

    private void inputSubjectKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputSubjectKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findSubject(inputSubject.getText());
        }
    }//GEN-LAST:event_inputSubjectKeyReleased

    private void inputImageInfoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputImageInfoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findImageInfo(inputImageInfo.getText());
        }
    }//GEN-LAST:event_inputImageInfoKeyReleased

    private void subjectComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_subjectComponentShown
//        if (!subjectFirst) {
//            findSubject(inputSubject.getText());
//            subjectFirst = true;
//        }

        try {
            IndustryDAO dAO = new IndustryDAO();
            listIndustry = dAO.get("");
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        searchSubject.doClick();
        btnSaveSubject.setFocusable(true);
        subjectTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_subjectComponentShown

    private void MarketsComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_MarketsComponentShown
        if (!marketFirst) {
            findMarket(inputMarket.getText());
            marketFirst = true;
        }
        btnSaveMarket.setFocusable(true);
        marketTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_MarketsComponentShown

    private void imagesLoadComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_imagesLoadComponentShown
        if (!imageLoadFirst) {
            findImageInfo(inputImageInfo.getText());
            imageLoadFirst = true;
        }
        btnSaveImageInfo.setFocusable(true);
        imageInfoTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_imagesLoadComponentShown

    private void outgoingEmailComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_outgoingEmailComponentShown
        if (!outgoingEmailFirst) {
            findOutgoingEmail(inputOE.getText());
            outgoingEmailFirst = true;
        }
        btnSaveOE.setFocusable(true);
        OETable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_outgoingEmailComponentShown

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

    }//GEN-LAST:event_jList1ValueChanged

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        String value = (String) jList1.getSelectedValue();
        if (value == null) {
            return;
        }
        value = value.trim();
        switch (value) {
            case "Temas":
                TabCatalogos.setSelectedIndex(0);
                break;
            case "Idiomas":
                TabCatalogos.setSelectedIndex(1);
                break;
            case "Sectores":
                TabCatalogos.setSelectedIndex(2);
                break;
            case "Mercados":
                TabCatalogos.setSelectedIndex(3);
                break;
            case "Imágenes":
                TabCatalogos.setSelectedIndex(4);
                break;
            case "Publicidad":
                TabCatalogos.setSelectedIndex(5);
                break;
            case "Editor de texto":
                TabCatalogos.setSelectedIndex(6);
                break;
            case "Editor de bullets":
                TabCatalogos.setSelectedIndex(7);
                break;
            case "Correos Salientes":
                TabCatalogos.setSelectedIndex(8);
                break;
            case "Header & Footer":
                TabCatalogos.setSelectedIndex(9);
                break;
            default:
                TabCatalogos.setSelectedIndex(-1);
                break;
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void btnSaveTextEditor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTextEditor1ActionPerformed
        saveBulletsEditor();
    }//GEN-LAST:event_btnSaveTextEditor1ActionPerformed

    private void btnEditEditor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEditor1ActionPerformed
        if (btnEditEditor1.getText().equals(EDIT_LABEL)) {
            setEnabledBulletsEditor(true);
            btnEditEditor1.setText(CANCEL_LABEL);
            btnSaveTextEditor1.setEnabled(true);
        } else {
            setEnabledBulletsEditor(false);
            btnEditEditor1.setText(EDIT_LABEL);
            btnSaveTextEditor1.setEnabled(false);
        }
    }//GEN-LAST:event_btnEditEditor1ActionPerformed

    private void bulletsEditorComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bulletsEditorComponentShown
        getBulletsEditorValues();
    }//GEN-LAST:event_bulletsEditorComponentShown

    private void btnDeleteImageInfo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteImageInfo1ActionPerformed
        deletePublicity();
    }//GEN-LAST:event_btnDeleteImageInfo1ActionPerformed

    private void btnSaveImageInfo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveImageInfo1ActionPerformed
        savePublicity();
    }//GEN-LAST:event_btnSaveImageInfo1ActionPerformed

    private void publicidadComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_publicidadComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_publicidadComponentShown

    private void publicityImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publicityImageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_publicityImageActionPerformed

    private void publicityImageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_publicityImageItemStateChanged
        if (publicityImage.isSelected()) {
            tabPublicity.setSelectedIndex(1);
        }

    }//GEN-LAST:event_publicityImageItemStateChanged

    private void publicityTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_publicityTextItemStateChanged
        if (publicityText.isSelected()) {
            tabPublicity.setSelectedIndex(0);
        }

    }//GEN-LAST:event_publicityTextItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectImage();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSaveImageInfo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveImageInfo2ActionPerformed
        if (isDeleteHeader) {
            cleanHeader();
        }

        if (isDeleteFooter) {
            cleanFooter();
        }

        if (isDeleteFooter == false && isDeleteHeader == false) {
            saveHeaderFooter();
        }

        isDeleteHeader = false;
        isDeleteFooter = false;
    }//GEN-LAST:event_btnSaveImageInfo2ActionPerformed

    private void headerFooterComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_headerFooterComponentShown
        initHeaderFooter();
    }//GEN-LAST:event_headerFooterComponentShown

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addHeader();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addFooter();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnDeleteImageInfo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteImageInfo3ActionPerformed
        isDeleteHeader = true;
        headerVisor.setIcon(null);
        headerVisor.setText("No Header");
    }//GEN-LAST:event_btnDeleteImageInfo3ActionPerformed

    private void btnDeleteImageInfo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteImageInfo4ActionPerformed
        isDeleteFooter = true;
        footerVisor.setIcon(null);
        footerVisor.setText("No Footer");
    }//GEN-LAST:event_btnDeleteImageInfo4ActionPerformed

    private void urlImagePublicity1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_urlImagePublicity1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!urlImagePublicity1.getText().isEmpty()) {
                try {
                    BufferedImage image = ImageIO.read(new URL(urlImagePublicity1.getText()));
                    BufferedImage resizedImage = resize(image, 195, 206);
                    ImageIcon ii = new ImageIcon(resizedImage);
                    publicityVisor.setText(null);
                    publicityVisor.setIcon(ii);
                    filePublicity.setSelectedFile(null);
                    //ii = new ImageIcon(new URL(urlImagePublicity1.getText()));
                } catch (MalformedURLException ex) {
                    Utilerias.logger(getClass()).info(ex);
                    JOptionPane.showMessageDialog(null, "La URL no es valida.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(Catalogs.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_urlImagePublicity1KeyPressed

    private void urlImagePublicity1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_urlImagePublicity1FocusLost
        // TODO add your handling code here:
        if (!urlImagePublicity1.getText().isEmpty()) {
            try {
                BufferedImage images = ImageIO.read(new URL(urlImagePublicity1.getText()));
                BufferedImage resizedImage = resize(images, 195, 206);
                ImageIcon ii = new ImageIcon(resizedImage);
                publicityVisor.setText(null);
                publicityVisor.setIcon(ii);
                filePublicity.setSelectedFile(null);
                //ii = new ImageIcon(new URL(urlImagePublicity1.getText()));
            } catch (MalformedURLException ex) {
                Utilerias.logger(getClass()).info(ex);
                JOptionPane.showMessageDialog(null, "La URL no es valida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                Logger.getLogger(Catalogs.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_urlImagePublicity1FocusLost

    private void deletePublicity() {
        try {
            int result = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la Publicidad?.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                PublicityDAO dao = new PublicityDAO();
                PublicityBO bo = new PublicityBO();
                bo.setIdPublicity(idPublicity);
                dao.delete(bo);
                publicityVisor.setIcon(null);
                publicityVisor.setText("No Image");
                urlImagePublicity1.setText(null);
                descripcion.setText(null);
                ekitCore3.getTextPane().setText(null);
                initPublicity();
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

/* *************************************** Subjects ******************************** */
    private void addSubjectModel() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        subjectTable.setModel(model);

        try {

            fitColumnsSubject(subjectTable);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void setButtonsSubject(SubjectModel model){
        List<Integer> selectedRows = new ArrayList<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {
                selectedRows.add(model.getUniqueRowIdentify(i));
            }
        }

        if (selectedRows.isEmpty() == true) {
            /*Cuando no se han seleccionado elementos en el grid*/
            btnSaveSubject.setText(SAVE_LABEL);
            btnSaveSubject.setEnabled(false);
            btnNewSubject.setText(NEW_LABEL);
            btnNewSubject.setEnabled(true);
            btnDeleteSubject.setEnabled(false);
        } else if (selectedRows.size() == 1) {
            btnNewSubject.setText(CANCEL_LABEL);
            btnNewSubject.setEnabled(true);
            btnSaveSubject.setEnabled(true);
            btnSaveSubject.setText(EDIT_LABEL);
            btnDeleteSubject.setEnabled(true);
        } else if (selectedRows.size() > 1) {
            btnNewSubject.setText(CANCEL_LABEL);
            btnNewSubject.setEnabled(true);
            btnSaveSubject.setEnabled(false);
            btnSaveSubject.setText(EDIT_LABEL);
            btnDeleteSubject.setEnabled(true);
        }
    }

    private void setSubjectModel(List<SubjectBO> list) {
        SubjectModel subjectModel = new SubjectModel(list);
        subjectModel.addTableModelListener((TableModelEvent e)->{
            setButtonsSubject(subjectModel);
        });
        subjectTable.setModel(subjectModel);

        try {
            /* Agregar combo*/
//            TableColumn zeroColumn = subjectTable.getColumnModel().getColumn(StatementConstant.SC_0.get());
//            zeroColumn.setPreferredWidth(10);

            TableColumn column = subjectTable.getColumnModel().getColumn(StatementConstant.SC_3.get());
            column.setCellEditor(new DefaultCellEditor(generateIndustryBox()));
            subjectTable.setColumnSelectionAllowed(true);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        new EditableCellFocusAction(subjectTable, KeyStroke.getKeyStroke("TAB"));
        fitColumnsSubject(subjectTable);
    }

    private void findSubject(String pattern) {
        try {
            SubjectDAO dAO = new SubjectDAO();
            List<SubjectBO> list = dAO.get(pattern);
            setSubjectModel(list);
            btnSaveSubject.setText(SAVE_LABEL);
            btnSaveSubject.setEnabled(false);
            btnNewSubject.setText(NEW_LABEL);
            btnNewSubject.setEnabled(true);
            btnDeleteSubject.setEnabled(false);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newSubject() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        if (model == null) {
            return;
        }
        model.setEditable0(false);
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            subjectTable.editCellAt(0, 1);
            Component editor = subjectTable.getEditorComponent();
            editor.requestFocusInWindow();
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            subjectTable.editCellAt(0, 1);
            Component editor = subjectTable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewSubject() {

        findSubject(null);
        /* 
         
         try {
         SubjectModel model = (SubjectModel) subjectTable.getModel();
         if (model.getData().length == 0) {
         return;
         }
         Object[][] data2 = new Object[model.getData().length - 1][];
         if (data2.length == 0) {
         model.setData(data2);
         subjectTable.setModel(model);
         fitColumnsSubject(subjectTable);
         } else {
         System.arraycopy(model.getData(), 1, data2, 0, data2.length);
         model = new SubjectModel();
         model.setData(data2);

         subjectTable.setModel(model);
         fitColumnsSubject(subjectTable);
         }
         } catch (Exception ex) {
         Utilerias.logger(getClass()).info(ex);
         }
         
         */
    }

    private void insertUpdateSubject() {

        if (btnSaveSubject.getText().equals(SAVE_LABEL)) {
            /* 
             * Validar que esté seleccionado un registro del grid para guardar/actualizar
             */
            StringBuilder fields = new StringBuilder();

            //btnSaveSubject.setFocusable(true);
            subjectTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            SubjectModel model = (SubjectModel) subjectTable.getModel();
            model.setEditable0(true);
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;

                SubjectDAO dAO = new SubjectDAO();
                SubjectBO bo = new SubjectBO();
                bo.setIdSubject(model.getUniqueRowIdentify(index));
                if (subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null 
                        || subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nTema");
                } else {
                    bo.setName((String) subjectTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                }
                if ((subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) == null
                        || subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()).toString().isEmpty())
                        & (Boolean.parseBoolean(subjectTable.getModel().getValueAt(index, StatementConstant.SC_4.get()).toString()) == true)) {
                    required = false;
                    fields.append("\nSector");
                } else {
                    if (subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) instanceof IndustryBO) {
                        IndustryBO industryBO = (IndustryBO) subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get());
                        bo.setIndustry(industryBO.getIdIndustry());
                    } else {
                        String str = (String) subjectTable.getModel().getValueAt(index, StatementConstant.SC_3.get());
                        //buscarPorNombre
                        int id = getIdFromComboBox(str);
                        bo.setIndustry(id);
                    }
                }

                //limitar titulo 
                if (bo.getName().length() > 30) {
                    fields.append("\nTitulo de tema debe ser menor igual a 30 caracteres.");
                    required = false;
                }

                bo.setIssuing((boolean) subjectTable.getModel().getValueAt(index, StatementConstant.SC_4.get()));

                if (required == true) {

                    if (Utilerias.isRepeated(
                            Utilerias.TablesValueNames.SUBJECT.name(),
                            Utilerias.TablesValueNames.SUBJECT.getCaption(),
                            bo.getName(), "IDSUBJECT", bo.getIdSubject())) {
                        JOptionPane.showMessageDialog(null, "Este tema ya se encuentra guardado");
                        findSubject(null);
                        return;
                    }

                    int i = dAO.insertUpdate(bo);
                    findSubject(inputSubject.getText());

                    if (bo.getIdSubject() > 0) {
                        JOptionPane.showMessageDialog(this, "Se han modificado los datos del catálogo");
                    } else {
                        JOptionPane.showMessageDialog(this, "Los datos se guardaron exitosamente");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Campos requeridos* " + fields);
                }

            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }

        } else if (btnSaveSubject.getText().equals(EDIT_LABEL)) {

            //obtener la fila del elemento seleccionado 
            int row = subjectTable.getSelectedRow();
            btnSaveSubject.setText(SAVE_LABEL);
            subjectTable.requestFocus();
            subjectTable.editCellAt(row, 1);   
        }
    }

    private void deleteSubject() {
        SubjectModel model = subjectTable.getModel() instanceof DefaultTableModel
                ? new SubjectModel()
                : (SubjectModel) subjectTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    SubjectDAO dao = new SubjectDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findSubject(inputSubject.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }

    }

    private void fitColumnsSubject(JTable table) {
        try {
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }

            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));

            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.LEFT);
            table.getColumnModel().getColumn(2).setCellRenderer(render);

            //Utilerias.formatTable(table);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }


/* ********************************* Languages **************************************** */
    private void addLanguageModel() {
        LanguageModel model = languageTable.getModel() instanceof DefaultTableModel
                ? new LanguageModel()
                : (LanguageModel) languageTable.getModel();
        languageTable.setModel(model);

        try {
           /* languageTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    //int row = languageTable.rowAtPoint(e.getPoint());
                    int column = languageTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        LanguageModel model = (LanguageModel) languageTable.getModel();
                        
                    }
                }
            }); */

            fitColumnsLanguage(languageTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void setButtonsLenguage(LanguageModel model){
        List<Integer> selectedRows = new ArrayList<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {
                selectedRows.add(model.getUniqueRowIdentify(i));
            }
        }
        if (selectedRows.isEmpty() == true) {
            /*Cuando no se han seleccionado elementos en el grid*/
            btnSaveLanguage.setText(SAVE_LABEL);
            btnSaveLanguage.setEnabled(false);
            btnNewLanguage.setText(NEW_LABEL);
            btnNewLanguage.setEnabled(true);
            btnDeleteLanguage.setEnabled(false);
        } else if (selectedRows.size() == 1) {
            btnNewLanguage.setText(CANCEL_LABEL);
            btnNewLanguage.setEnabled(true);
            btnSaveLanguage.setEnabled(true);
            btnSaveLanguage.setText(EDIT_LABEL);
            btnDeleteLanguage.setEnabled(true);
        } else if (selectedRows.size() > 1) {
            btnNewLanguage.setText(CANCEL_LABEL);
            btnNewLanguage.setEnabled(true);
            btnSaveLanguage.setEnabled(false);
            btnSaveLanguage.setText(EDIT_LABEL);
            btnDeleteLanguage.setEnabled(true);
        }
    }

    private void setLanguageModel(List<LanguageBO> list) {
        LanguageModel languageModel = new LanguageModel(list);
        languageModel.addTableModelListener((TableModelEvent e)->{
            setButtonsLenguage(languageModel);
        });
        languageTable.setModel(languageModel);
        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        languageTable.setDefaultRenderer(Object.class, stcr);
        
        new EditableCellFocusAction(languageTable, KeyStroke.getKeyStroke("TAB"));
        
        fitColumnsLanguage(languageTable);
    }

    private void findLanguage(String pattern) {
        try {
            LanguageDAO dAO = new LanguageDAO();
            List<LanguageBO> list = dAO.get(pattern);
            setLanguageModel(list);
            btnSaveLanguage.setText(SAVE_LABEL);
            btnSaveLanguage.setEnabled(false);
            btnNewLanguage.setText(NEW_LABEL);
            btnNewLanguage.setEnabled(true);
            btnDeleteLanguage.setEnabled(false);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void newLanguage() {

        LanguageModel model = languageTable.getModel() instanceof DefaultTableModel
                ? new LanguageModel()
                : (LanguageModel) languageTable.getModel();
        if (model == null) {
            return;
        }
        model.setEditable0(false);
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            languageTable.editCellAt(0, 1);
            Component editor = languageTable.getEditorComponent();
            editor.requestFocusInWindow();
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            languageTable.editCellAt(0, 1);
            Component editor = languageTable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewLanguage() {
        findLanguage(null);
        LanguageModel model = (LanguageModel) languageTable.getModel();
        model.setEditable0(true);
//        try {
//            LanguageModel model = (LanguageModel) languageTable.getModel();
//            if (model.getData().length == 0) {
//                return;
//            }
//            Object[][] data2 = new Object[model.getData().length - 1][];
//            if (data2.length == 0) {
//                model.setData(data2);
//                languageTable.setModel(model);
//                fitColumnsLanguage(languageTable);
//            } else {
//                System.arraycopy(model.getData(), 1, data2, 0, data2.length);
//                //model.setData(data2);
//                model = new LanguageModel();
//                model.setData(data2);
//                languageTable.setModel(model);
//                fitColumnsLanguage(languageTable);
//            }
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info(ex);
//        }
    }

    private void insertUpdateLanguage() {
        if (btnSaveLanguage.getText().equals(SAVE_LABEL)) {

            btnSaveLanguage.setFocusable(true);
            languageTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);


            /* 
             * Validar que esté seleccionado un registro del grid para guardar/actualizar
             */
            LanguageModel model = (LanguageModel) languageTable.getModel();
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;
                LanguageDAO dAO = new LanguageDAO();
                LanguageBO bo = new LanguageBO();
                bo.setIdLanguage(model.getUniqueRowIdentify(index));
                StringBuilder fields = new StringBuilder();

                if (languageTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || languageTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nIdioma");
                } else {
                    bo.setName((String) languageTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                }
                if (languageTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) == null || languageTable.getModel().getValueAt(index, StatementConstant.SC_3.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nNomenclatura");
                } else {
                    bo.setNomenclature((String) languageTable.getModel().getValueAt(index, StatementConstant.SC_3.get()));
                }
                if (required == true) {
                    if (Utilerias.isRepeated(
                            Utilerias.TablesValueNames.LANGUAGE.name(),
                            Utilerias.TablesValueNames.LANGUAGE.getCaption(),
                            bo.getName(), "IDLANGUAGE", bo.getIdLanguage())) {
                        JOptionPane.showMessageDialog(null, "El idioma ya existe, favor de ingresar un idioma distinto");
                        findLanguage(null);
                        return;
                    }

                    dAO.insertUpdate(bo);
                    findLanguage(inputLanguage.getText());
                    if (bo.getIdLanguage() > 0) {
                        JOptionPane.showMessageDialog(null, "Se han modificado los datos del catálogo.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Los datos se guardaron exitosamente");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Campos requeridos*" + fields);
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }
        } else if (btnSaveLanguage.getText().equals(EDIT_LABEL)) {

            //obtener la fila del elemento seleccionado 
            int row = languageTable.getSelectedRow();

            LanguageModel model = (LanguageModel) languageTable.getModel();
            if (model.getData().length == 0) {
                return;
            }

            if ("es".equals(model.getData()[row][3].toString().toLowerCase().trim()) || "en".equals(model.getData()[row][3].toString().toLowerCase().trim())) {
                JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), "Los idiomas Ingles y Español no pueden ser editados", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            btnSaveLanguage.setText(SAVE_LABEL);
            languageTable.requestFocus();
            languageTable.editCellAt(row, 1);
            

        }
    }

    private void deleteLanguage() {
        LanguageModel model = languageTable.getModel() instanceof DefaultTableModel
                ? new LanguageModel()
                : (LanguageModel) languageTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            boolean langNotDel = false;
            for (int row = 0; row < a2.length; row++) {
                if ((boolean) a2[row][0]) {
                    if ("es".equals(a2[row][3].toString().toLowerCase().trim()) || "en".equals(a2[row][3].toString().toLowerCase().trim())) {
                        langNotDel = true;
                        break;
                    }

                    ids.add(model.getUniqueRowIdentify(row));
                }
            }

            if (langNotDel) {
                JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), "Los idiomas Ingles y Español no pueden ser eliminados", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    LanguageDAO dao = new LanguageDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findLanguage(inputLanguage.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }
    }

    private void fitColumnsLanguage(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }

            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
        } catch (Exception e) {

        }
    }

/* ***************************** Industry/Sectores ******************************** */
    private void addIndustryModel() {
        IndustryModel model = industryTable.getModel() instanceof DefaultTableModel
                ? new IndustryModel()
                : (IndustryModel) industryTable.getModel();
        industryTable.setModel(model);

        try {
           /* industryTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = industryTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                    // Habilitar e inhabiliar botones de eliminar guardar
                        sectoresActionSelectRow((IndustryModel) industryTable.getModel());
                    }
                }
            }); */
            fitColumnsIndustry(industryTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void sectoresActionSelectRow(IndustryModel model){
        List<Integer> selectedRows = new ArrayList<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i)) {
                selectedRows.add(model.getUniqueRowIdentify(i));
            }
        }
        if(!(btnSaveIndustry.getText().equals(SAVE_LABEL) && btnSaveIndustry.isEnabled())){
            if (selectedRows.isEmpty() == true) {
                /*Cuando no se han seleccionado elementos en el grid*/
                btnSaveIndustry.setText(SAVE_LABEL);
                btnSaveIndustry.setEnabled(false);
                btnNewIndustry.setText(NEW_LABEL);
                btnNewIndustry.setEnabled(true);
                btnDeleteIndustry.setEnabled(false);
            } else if (selectedRows.size() == 1) {
                btnNewIndustry.setText(CANCEL_LABEL);
                btnNewIndustry.setEnabled(true);
                btnSaveIndustry.setEnabled(true);
                btnSaveIndustry.setText(EDIT_LABEL);
                btnDeleteIndustry.setEnabled(true);
            } else if (selectedRows.size() > 1) {
                btnNewIndustry.setText(CANCEL_LABEL);
                btnNewIndustry.setEnabled(true);
                btnSaveIndustry.setEnabled(false);
                btnSaveIndustry.setText(EDIT_LABEL);
                btnDeleteIndustry.setEnabled(true);
            }
        }
        
    }

    private void setIndustryModel(List<IndustryBO> list) {
        IndustryModel industryModel = new IndustryModel(list);
        industryModel.addTableModelListener((TableModelEvent e) -> {
            sectoresActionSelectRow(industryModel);
        });
        industryTable.setModel(industryModel);
        fitColumnsIndustry(industryTable);
    }

    private void findIndustry(String pattern) {

        try {
            IndustryDAO dAO = new IndustryDAO();
            List<IndustryBO> list = dAO.get(pattern);
            setIndustryModel(list);
            btnSaveIndustry.setText(SAVE_LABEL);
            btnSaveIndustry.setEnabled(false);
            btnNewIndustry.setText(NEW_LABEL);
            btnNewIndustry.setEnabled(true);
            btnDeleteIndustry.setEnabled(false);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newIndustry() {

        IndustryModel model = industryTable.getModel() instanceof DefaultTableModel
                ? new IndustryModel()
                : (IndustryModel) industryTable.getModel();
        model.setEditable0(false);
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            industryTable.editCellAt(0, 1);
            Component editor = industryTable.getEditorComponent();
            editor.requestFocusInWindow();
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            industryTable.editCellAt(0, 1);
            Component editor = industryTable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewIndustry() {
        IndustryModel model = (IndustryModel) industryTable.getModel();
        model.setEditable0(true);
        findIndustry(null);

//        try {
//            IndustryModel model = (IndustryModel) industryTable.getModel();
//            if (model.getData().length == 0) {
//                return;
//            }
//            Object[][] data2 = new Object[model.getData().length - 1][];
//            if (data2.length == 0) {
//                model.setData(data2);
//                industryTable.setModel(model);
//                fitColumnsIndustry(industryTable);
//            } else {
//                System.arraycopy(model.getData(), 1, data2, 0, data2.length);
//                //model.setData(data2);
//                model = new IndustryModel();
//                model.setData(data2);
//                industryTable.setModel(model);
//                fitColumnsIndustry(industryTable);
//            }
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info(ex);
//        }
    }

    private void insertUpdateIndustry() {

        StringBuilder fields = new StringBuilder();

        if (btnSaveIndustry.getText().equals(SAVE_LABEL)) {
            /* 
             * Validar que esté seleccionado un registro del grid para guardar/actualizar
             */
            btnSaveIndustry.setFocusable(true);
            industryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            IndustryModel model = (IndustryModel) industryTable.getModel();
            model.setEditable0(true);
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;
                IndustryDAO dao = new IndustryDAO();
                IndustryBO bo = new IndustryBO();
                bo.setIdIndustry(model.getUniqueRowIdentify(index));

                if (industryTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || industryTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nSector");
                } else {
                    bo.setName((String) industryTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                }
                if (required == true) {
                    if (Utilerias.isRepeated(
                            Utilerias.TablesValueNames.INDUSTRY.name(),
                            Utilerias.TablesValueNames.INDUSTRY.getCaption(),
                            bo.getName(), "IDINDUSTRY", bo.getIdIndustry())) {
                        JOptionPane.showMessageDialog(null, "Este sector ya se encuentra guardado");
                        findIndustry(null);
                        return;
                    }

                    int i = dao.insertUpdate(bo);
                    findIndustry(inputIndustry.getText());
                    if (bo.getIdIndustry() > 0) {
                        JOptionPane.showMessageDialog(null, "Se han modificado los datos del catálogo.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Los datos se guardaron exitosamente");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Campos requeridos*" + fields);
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }
        } else if (btnSaveIndustry.getText().equals(EDIT_LABEL)) {
            //obtener la fila del elemento seleccionado 
            int row = industryTable.getSelectedRow();
            industryTable.editCellAt(row, 1);
            btnSaveIndustry.setText(SAVE_LABEL);
            btnDeleteIndustry.setEnabled(false);
        }
    }

    private void deleteIndustry() {
        IndustryModel model = industryTable.getModel() instanceof DefaultTableModel
                ? new IndustryModel()
                : (IndustryModel) industryTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    IndustryDAO dao = new IndustryDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findIndustry(inputIndustry.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    findIndustry(null);
                }
            }
        }
    }

    private void fitColumnsIndustry(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
        } catch (Exception e) {

        }

    }

/* ************************************ Market ********************************** */
    private void addMarketModel() {
        MarketModel model = marketTable.getModel() instanceof DefaultTableModel
                ? new MarketModel()
                : (MarketModel) marketTable.getModel();
        marketTable.setModel(model);

        try {
        /*    marketTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = marketTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        MarketModel model = (MarketModel) marketTable.getModel();
                        
                    }
                }
            }); */

            fitColumnsMarket(marketTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setButtonsMarket(MarketModel model){
        if(!(btnSaveMarket.getText().equals(SAVE_LABEL) && btnSaveMarket.isEnabled())){
            List<Integer> selectedRows = new ArrayList<>();
            int length = model.getData() != null ? model.getData().length : 0;
            for (int i = 0; i < length; i++) {
                if (model.isCheckedRow(i)) {
                    selectedRows.add(model.getUniqueRowIdentify(i));
                }
            }
            if (selectedRows.isEmpty() == true) {
                /*Cuando no se han seleccionado elementos en el grid*/
                btnSaveMarket.setText(SAVE_LABEL);
                btnSaveMarket.setEnabled(false);
                btnNewMarket.setText(NEW_LABEL);
                btnNewMarket.setEnabled(true);
                btnDeleteMarket.setEnabled(false);
            } else if (selectedRows.size() == 1) {
                btnNewMarket.setText(CANCEL_LABEL);
                btnNewMarket.setEnabled(true);
                btnSaveMarket.setEnabled(true);
                btnSaveMarket.setText(EDIT_LABEL);
                btnDeleteMarket.setEnabled(true);
            } else if (selectedRows.size() > 1) {
                btnNewMarket.setText(CANCEL_LABEL);
                btnNewMarket.setEnabled(true);
                btnSaveMarket.setEnabled(false);
                btnSaveMarket.setText(EDIT_LABEL);
                btnDeleteMarket.setEnabled(true);
            }
        }
        
    }
    
    private void setMarketModel(List<MarketBO> list) {
        MarketModel marketModel = new MarketModel(list);
        marketModel.addTableModelListener((TableModelEvent e) -> {
            setButtonsMarket(marketModel);
        });
        marketTable.setModel(marketModel);
        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        marketTable.setDefaultRenderer(Object.class, stcr);
        
        new EditableCellFocusAction(marketTable, KeyStroke.getKeyStroke("TAB"));
        
        fitColumnsMarket(marketTable);
    }

    private void findMarket(String pattern) {

        try {
            MarketDAO dao = new MarketDAO();
            List<MarketBO> list = dao.get(pattern);
            setMarketModel(list);
            btnSaveMarket.setText(SAVE_LABEL);
            btnSaveMarket.setEnabled(false);
            btnNewMarket.setText(NEW_LABEL);
            btnNewMarket.setEnabled(true);
            btnDeleteMarket.setEnabled(false);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newMarket() {
        MarketModel model = marketTable.getModel() instanceof DefaultTableModel
                ? new MarketModel()
                : (MarketModel) marketTable.getModel();
        if (model == null) {
            return;
        }
        model.setEditable0(false);
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            marketTable.editCellAt(0, 1);
            
            Component editor = marketTable.getEditorComponent();
            editor.requestFocusInWindow();

        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            marketTable.editCellAt(0, 1);
            Component editor = marketTable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewMarket() {

        MarketModel model = (MarketModel) marketTable.getModel();
        model.setEditable0(true);
        findMarket(null);

//        try {
//            MarketModel model = (MarketModel) marketTable.getModel();
//            if (model.getData().length == 0) {
//                return;
//            }
//            Object[][] data2 = new Object[model.getData().length - 1][];
//            if (data2.length == 0) {
//                model.setData(data2);
//                marketTable.setModel(model);
//                fitColumnsIndustry(marketTable);
//            } else {
//                System.arraycopy(model.getData(), 1, data2, 0, data2.length);
//                //model.setData(data2);
//                model = new MarketModel();
//                model.setData(data2);
//                marketTable.setModel(model);
//                fitColumnsMarket(marketTable);
//            }
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info(ex);
//        }
    }

    private void insertUpdateMarket() {

        StringBuilder fields = new StringBuilder();

        if (btnSaveMarket.getText().equals(SAVE_LABEL)) {
            btnSaveMarket.setFocusable(true);
            marketTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            /* 
             * Validar que esté seleccionado un registro del grid para guardar/actualizar
             */
            MarketModel model = (MarketModel) marketTable.getModel();
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;
                MarketDAO dao = new MarketDAO();
                MarketBO bo = new MarketBO();
                bo.setIdMarket(model.getUniqueRowIdentify(index));
                bo.setIdMiVector_real(String.valueOf(model.getUniqueRowIdentify(index)));
                if (marketTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || marketTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    fields.append("\nMercado");
                    required = false;
                } else {
                    bo.setName((String) marketTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                }

                if (marketTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) == null || marketTable.getModel().getValueAt(index, StatementConstant.SC_3.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nNomenclatura");
                } else {
                    bo.setNomenclature((String) marketTable.getModel().getValueAt(index, StatementConstant.SC_3.get()));
                }

                /*if (marketTable.getModel().getValueAt(index, StatementConstant.SC_4.get()) == null || marketTable.getModel().getValueAt(index, StatementConstant.SC_4.get()).toString().isEmpty()) {
                 required = false;
                 } else {*/
                bo.setIdMiVector((String) marketTable.getModel().getValueAt(index, StatementConstant.SC_4.get()));
                //}

                // revisar el tamaño del titulo del mercado y limitarlo a 25 caracteres
                if (bo.getName().length() > 25) {
                    fields.append("\nTitulo del mercado debe ser menor igual a 25 caracteres");
                    required = false;
                }

                if (required == true) {
                    if (Utilerias.isRepeated(
                            Utilerias.TablesValueNames.MARKET.name(),
                            Utilerias.TablesValueNames.MARKET.getCaption(),
                            bo.getName(), "IDMARKET", bo.getIdMarket())) {
                        JOptionPane.showMessageDialog(null, "Este mercado ya esta guardado");
                        findMarket(null);
                        return;
                    }

                    String[] res = getIdMiVector(bo.getName(), bo.getIdMiVector());

                    if (validaId(res[0]) && validaId(res[1])) {
                        bo.setIdMiVector(res[1]);
                        bo.setIdMiVector_real(res[0]);
                    }

                    dao.insertUpdate(bo);
                    findMarket(inputMarket.getText());
                    if (bo.getIdMarket() > 0) {
                        JOptionPane.showMessageDialog(this, "Se han modificado los datos del catálogo");
                    } else {
                        JOptionPane.showMessageDialog(this, "Los datos se guardaron exitosamente");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Campos requeridos*" + fields);
                }

            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }
        } else if (btnSaveMarket.getText().equals(EDIT_LABEL)) {

            //obtener la fila del elemento seleccionado 
            btnSaveMarket.setText(SAVE_LABEL);
            int row = marketTable.getSelectedRow();
            marketTable.editCellAt(row, 1);
            marketTable.requestFocus();

        }
    }

    private String[] getIdMiVector(String mercado, String mercadoId) {
        String[] ids = new String[2];
        IData_Stub stub = (IData_Stub) new Data_Impl().getBasicHttpBinding_IData();
        UtileriasWS.setEndpoint(stub);
        try {
            int us = InstanceContext.getInstance().getUsuario().getUsuarioId();
            DBResult message = null;//message = stub.nuevoCategoria(mercado, us, Utilerias.getIP());
            if (mercadoId == null || mercadoId.isEmpty()) {
                message = stub.nuevoCategoria(mercado, us, Utilerias.getIP());
            } else {
                message = stub.actualizaCategoria(Integer.parseInt(mercadoId), mercado, us, Utilerias.getIP());
            }
            ids[0] = UtileriasWS.getIDSMarketMiVector(message.getResultMsg())[0];
            ids[1] = UtileriasWS.getIDSMarketMiVector(message.getResultMsg())[1];
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de categoria no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
            ids[0] = "error";
        }
        return ids;
    }

    private boolean validaId(String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(id);
            return false;
        }
        return true;
    }

    private void deleteMarket() {
        MarketModel model = marketTable.getModel() instanceof DefaultTableModel
                ? new MarketModel()
                : (MarketModel) marketTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    MarketDAO dao = new MarketDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findMarket(inputMarket.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    findMarket(null);
                }
            }
        }
    }

    private void fitColumnsMarket(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
        } catch (Exception e) {

        }

    }

/* ************************************ Outgoing Email ********************************* */
    private void addOutgoingEmailModel() {
        OutgoingEmailModel model = OETable.getModel() instanceof DefaultTableModel
                ? new OutgoingEmailModel()
                : (OutgoingEmailModel) OETable.getModel();
        OETable.setModel(model);

        try {
         /*   OETable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = OETable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        OutgoingEmailModel model = (OutgoingEmailModel) OETable.getModel();
                        
                    }
                }
            }); */

            fitColumnsOutgoingEmail(OETable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void setButtonsOutGoingMail(OutgoingEmailModel model){
        if(!(btnSaveOE.getText().equals(SAVE_LABEL) && btnSaveOE.isEnabled())){
            List<Integer> selectedRows = new ArrayList<>();
            int length = model.getData() != null ? model.getData().length : 0;
            for (int i = 0; i < length; i++) {
                if (model.isCheckedRow(i)) {
                    selectedRows.add(model.getUniqueRowIdentify(i));
                }
            }
            if (selectedRows.isEmpty() == true) {
                /*Cuando no se han seleccionado elementos en el grid*/
                btnSaveOE.setText(SAVE_LABEL);
                btnSaveOE.setEnabled(false);
                btnNewOE.setText(NEW_LABEL);
                btnNewOE.setEnabled(true);
                btnDeleteOE.setEnabled(false);
            } else if (selectedRows.size() == 1) {
                btnNewOE.setText(CANCEL_LABEL);
                btnNewOE.setEnabled(true);
                btnSaveOE.setEnabled(true);
                btnSaveOE.setText(EDIT_LABEL);
                btnDeleteOE.setEnabled(true);
            } else if (selectedRows.size() > 1) {
                btnNewOE.setText(CANCEL_LABEL);
                btnNewOE.setEnabled(true);
                btnSaveOE.setEnabled(false);
                btnSaveOE.setText(EDIT_LABEL);
                btnDeleteOE.setEnabled(true);
            }
        } 
    }

    private void setOutgoingEmailModel(List<OutgoingEmailBO> list) {
        OutgoingEmailModel outgoingEmailModel = new OutgoingEmailModel(list);
        outgoingEmailModel.addTableModelListener((TableModelEvent e) -> {
            setButtonsOutGoingMail(outgoingEmailModel);
        });
        OETable.setModel(outgoingEmailModel);
        fitColumnsOutgoingEmail(OETable);
    }

    private void findOutgoingEmail(String pattern) {
        try {
            OutgoingEmailDAO dao = new OutgoingEmailDAO();
            List<OutgoingEmailBO> list = dao.get(pattern);
            setOutgoingEmailModel(list);
            btnSaveOE.setText(SAVE_LABEL);
            btnSaveOE.setEnabled(false);
            btnNewOE.setText(NEW_LABEL);
            btnNewOE.setEnabled(true);
            btnDeleteOE.setEnabled(false);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newOutgoingEmail() {
        OutgoingEmailModel model = OETable.getModel() instanceof DefaultTableModel
                ? new OutgoingEmailModel()
                : (OutgoingEmailModel) OETable.getModel();
        if (model == null) {
            return;
        }
        model.setEditable0(false);
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            //Validar que no se tenga un objeto "Nuevo" en pantalla
            if (Integer.valueOf(String.valueOf(a2[0][1])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            OETable.editCellAt(0, 1);
            Component editor = OETable.getEditorComponent();
            editor.requestFocusInWindow();
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            OETable.editCellAt(0, 1);
            Component editor = OETable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewOutgoingEmail() {
        try {
            OutgoingEmailModel model = (OutgoingEmailModel) OETable.getModel();
            if (model.getData().length == 0) {
                return;
            }

            findOutgoingEmail(inputOE.getText());

            /*Object[][] data2 = new Object[model.getData().length - 1][];
             if (data2.length == 0) {
             model.setData(data2);
             OETable.setModel(model);
             fitColumnsOutgoingEmail(OETable);
             } else {
             System.arraycopy(model.getData(), 1, data2, 0, data2.length);
             model = new OutgoingEmailModel();
             model.setData(data2);
             OETable.setModel(model);
             fitColumnsOutgoingEmail(OETable);
             }*/
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private boolean validatEmail(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    private void insertUpdateOutgoingEmail() {

        StringBuilder fields = new StringBuilder();

        if (btnSaveOE.getText().equals(SAVE_LABEL)) {
            btnSaveOE.setFocusable(true);
            OETable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            /* 
             * Validar que esté seleccionado un registro del grid para guardar/actualizar
             */
            OutgoingEmailModel model = (OutgoingEmailModel) OETable.getModel();
            if (model.getData().length == 0) {
                return;
            }
            int index = -1;
            for (int i = 0; i < model.getData().length; i++) {
                if (model.isCheckedRow(i) == true) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }
            try {
                boolean required = true;
                OutgoingEmailDAO dao = new OutgoingEmailDAO();
                OutgoingEmailBO bo = new OutgoingEmailBO();
                bo.setIdOutgoingEmail(model.getUniqueRowIdentify(index));
                if (OETable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || OETable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                    required = false;
                    fields.append("\nEmail");

                } else if (validatEmail((String) OETable.getModel().getValueAt(index, StatementConstant.SC_2.get()))) {
                    bo.setEmail((String) OETable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                } else {
                    JOptionPane.showMessageDialog(null, "Email no válido.");
                    findOutgoingEmail(null);
                    return;
                }
                if (required == true) {
                    if (Utilerias.isRepeated(
                            Utilerias.TablesValueNames.OUTGOINGEMAIL.name(),
                            Utilerias.TablesValueNames.OUTGOINGEMAIL.getCaption(),
                            bo.getEmail(), "IDOUTGOINGEMAIL", bo.getIdOutgoingEmail())) {
                        JOptionPane.showMessageDialog(null, "Este e-mail ya esta guardado");
                        findOutgoingEmail(null);
                        return;
                    }

                    dao.insertUpdate(bo);
                    findOutgoingEmail(inputOE.getText());
                    if (bo.getIdOutgoingEmail() > 0) {
                        JOptionPane.showMessageDialog(this, "Se han modificado los datos del catálogo");
                    } else {
                        JOptionPane.showMessageDialog(this, "Los datos se guardaron exitosamente");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Campos requeridos*" + fields);
                    findOutgoingEmail(null);
                    return;
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).info(e);
            }
        } else if (btnSaveOE.getText().equals(EDIT_LABEL)) {

            //obtener la fila del elemento seleccionado 
            int row = OETable.getSelectedRow();
            OETable.editCellAt(row, 1);
            btnSaveOE.setText(SAVE_LABEL);

        }
    }

    private void deleteOutgoingEmail() {
        OutgoingEmailModel model = OETable.getModel() instanceof DefaultTableModel
                ? new OutgoingEmailModel()
                : (OutgoingEmailModel) OETable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    OutgoingEmailDAO dao = new OutgoingEmailDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findOutgoingEmail(inputOE.getText());
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void fitColumnsOutgoingEmail(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
        } catch (Exception e) {

        }
    }

/* **********************************IMAGES INFO TABLE ******************************** */
    private void addImageInfoModel() {
        ImageInfoModel model = imageInfoTable.getModel() instanceof DefaultTableModel
                ? new ImageInfoModel()
                : (ImageInfoModel) imageInfoTable.getModel();
        imageInfoTable.setModel(model);

        try {
         /*   imageInfoTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = imageInfoTable.columnAtPoint(e.getPoint());
                    int row = imageInfoTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        ImageInfoModel model = (ImageInfoModel) imageInfoTable.getModel();
                        getImageData(model.getUniqueRowIdentify(row));
                    }

                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        ImageInfoModel model = (ImageInfoModel) imageInfoTable.getModel();
                        
                    }

                } 
            }); */

            fitColumnsImageInfo(imageInfoTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setButtonsImage(ImageInfoModel model){
        if(!(btnNewImageInfo.getText().equals(SAVE_LABEL) && btnNewImageInfo.isEnabled())){
            List<Integer> selectedRows = new ArrayList<>();
            int length = model.getData() != null ? model.getData().length : 0;
            for (int i = 0; i < length; i++) {
                if (model.isCheckedRow(i)) {
                    selectedRows.add(model.getUniqueRowIdentify(i));
                }
            }
            if (selectedRows.isEmpty() == true) {
                /*Cuando no se han seleccionado elementos en el grid*/
                btnSaveImageInfo.setText(SAVE_LABEL);
                btnSaveImageInfo.setEnabled(false);
                btnNewImageInfo.setText(NEW_LABEL);
                btnNewImageInfo.setEnabled(true);
                btnDeleteImageInfo.setEnabled(false);
            } else if (selectedRows.size() == 1) {
                btnNewImageInfo.setText(CANCEL_LABEL);
                btnNewImageInfo.setEnabled(true);
                btnSaveImageInfo.setEnabled(true);
                btnSaveImageInfo.setText(EDIT_LABEL);
                btnDeleteImageInfo.setEnabled(true);
            } else if (selectedRows.size() == 2){
                btnNewImageInfo.setText(CANCEL_LABEL);
                btnNewImageInfo.setEnabled(true);
                btnSaveImageInfo.setEnabled(false);
                btnSaveImageInfo.setText(EDIT_LABEL);
                btnDeleteImageInfo.setEnabled(true);
            } 
        }
    }
    
    private void setImageInfoModel(List<ImageInfoBO> list) {
        ImageInfoModel imageInfoModel = new ImageInfoModel(list);
        imageInfoModel.addTableModelListener((TableModelEvent e) -> {
            setButtonsImage(imageInfoModel);
        });
        imageInfoTable.setModel(imageInfoModel);
        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        imageInfoTable.setDefaultRenderer(Object.class, stcr);
        
        new EditableCellFocusAction(imageInfoTable, KeyStroke.getKeyStroke("TAB"));
        
        fitColumnsImageInfo(imageInfoTable);
    }

    private void whichKeyPressed(java.awt.event.KeyEvent evt) {
        int column = imageInfoTable.getSelectedColumn();
        int row = imageInfoTable.getSelectedRow();
        int rows = imageInfoTable.getRowCount();

        if (evt.getKeyCode() == KeyEvent.VK_ENTER
                || evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (column == 0) {
                imageInfoTable.changeSelection(row, 2, false, false);
                imageInfoTable.editCellAt(row, 2);
            } else if (column == 1) {
                imageInfoTable.changeSelection(row, 2, false, false);
                imageInfoTable.editCellAt(row, 2);
            } else if (column == 2) {
                imageInfoTable.changeSelection(row, 3, false, false);
                imageInfoTable.editCellAt(row, 3);
            } else if (column == 3) {
                imageInfoTable.changeSelection(row, 4, false, false);
                imageInfoTable.editCellAt(row, 4);
            } else if (column == 4) {
                imageInfoTable.changeSelection(row, 5, false, false);
                imageInfoTable.editCellAt(row, 5);
            } else if (column == 5) {
                imageButton.setSelected(true);
            }
            evt.consume();
        }
    }

    private void findImageInfo(String pattern) {
        try {
            ImageInfoDAO dao = new ImageInfoDAO();
            List<ImageInfoBO> list = dao.get(pattern);
            setImageInfoModel(list);
            int f = 0;
            btnSaveImageInfo.setText(SAVE_LABEL);
            btnSaveImageInfo.setEnabled(false);
            btnNewImageInfo.setText(NEW_LABEL);
            btnNewImageInfo.setEnabled(true);
            btnDeleteImageInfo.setEnabled(false);
            imageInLabel.setIcon(null);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void newImageInfo() {
        ImageInfoModel model = imageInfoTable.getModel() instanceof DefaultTableModel
                ? new ImageInfoModel()
                : (ImageInfoModel) imageInfoTable.getModel();
        if (model == null) {
            return;
        }
        model.setEditable0(false);
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            /* Validar que no se tenga un objeto "Nuevo" en pantalla */
            if (Integer.valueOf(String.valueOf(a2[StatementConstant.SC_0.get()][StatementConstant.SC_1.get()])) == -1) {
                return;
            }

            // Agregamos el nuevo registro vacio al inicio de la tabla 
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            Object[][] result = new Object[a2.length + 1][];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
            model.setData(result);
            imageDatafileDialog(true);
            imageInfoTable.editCellAt(0, 1);
            Component editor = imageInfoTable.getEditorComponent();
            editor.requestFocusInWindow();
        } else {
            Object[][] a1 = new Object[1][];
            a1[0] = model.getEmptyRow();
            model.setData(a1);
            imageDatafileDialog(true);
            imageInfoTable.editCellAt(0, 1);
            Component editor = imageInfoTable.getEditorComponent();
            editor.requestFocusInWindow();
        }
    }

    private void cancelNewImageInfo() {
        try {
            ImageInfoModel model = (ImageInfoModel) imageInfoTable.getModel();
            model.setEditable0(true);
            if (model.getData().length == 0) {
                return;
            }
            Object[][] data2 = new Object[model.getData().length - 1][];

            int uid = model.getUniqueRowIdentify(0);

            if (uid == -1) {
                if (data2.length == 0) {
                    model.setData(data2);
                    imageInfoTable.setModel(model);
                    fitColumnsOutgoingEmail(imageInfoTable);
                    imageDatafileDialog(false);
                } else {
                    System.arraycopy(model.getData(), 1, data2, 0, data2.length);
                    model = new ImageInfoModel();
                    model.setData(data2);
                    imageInfoTable.setModel(model);
                    fitColumnsImageInfo(imageInfoTable);
                    imageDatafileDialog(false);
                    deselectAll(model);
                }
            } else {
                deselectAll(model);
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }

    }

    private void deselectAll(TableModel model) {
        int columns = imageInfoTable.getRowCount();
        for (int i = 0; i < columns; i++) {
            model.setValueAt(false, i, 0);
        }
    }

    private void insertUpdateImageInfo() {
        if (btnSaveImageInfo.getText().equals(SAVE_LABEL)) {
            if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
                ImageInfoDAO dao = new ImageInfoDAO();
                boolean delete = dao.delete(imagesToDelete);
                String message = delete == true ? "Registros eliminados permanentemente" : "Ocurrió un error al eliminar\nintente nuevamente.";
                if (delete) {
                    findImageInfo(inputImageInfo.getText());
                }
                JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
            } else {
                btnSaveImageInfo.setFocusable(true);
                imageInfoTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

                /* 
                 * Validar que esté seleccionado un registro del grid para guardar/actualizar
                 */
                ImageInfoModel model = (ImageInfoModel) imageInfoTable.getModel();
                model.setEditable0(true);
                if (model.getData().length == 0) {
                    return;
                }

                int index = -1;
                for (int i = 0; i < model.getData().length; i++) {
                    if (model.isCheckedRow(i) == true) {
                        index = i;
                        break;
                    }
                }

                if (index < 0) {
                    return;
                }
                try {
                    boolean required = true;
                    ImageInfoDAO dao = new ImageInfoDAO();
                    ImageInfoBO bo = new ImageInfoBO();
                    bo.setIdImageInfo(model.getUniqueRowIdentify(index));
                    StringBuilder fields = new StringBuilder();

                    if (imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_3.get()) == null || imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_3.get()).toString().isEmpty()) {
                        required = false;
                        fields.append("\nCategoría");
                    } else {
                        bo.setCategory((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_3.get()));
                    }

                    if (imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_2.get()) == null || imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_2.get()).toString().isEmpty()) {
                        required = false;
                        fields.append("\nNombre");
                    } else {
                        bo.setName((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_2.get()));
                    }

                    bo.setDescription((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_4.get()));
                    bo.setAutor((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_5.get()));

                    if ((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_6.get()) == null || imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_6.get()).toString().isEmpty()) {
                        fields.append("\nFuente");
                        required = false;
                    } else {
                        bo.setSource((String) imageInfoTable.getModel().getValueAt(index, StatementConstant.SC_6.get()));
                    }

                    File file = fc.getSelectedFile();
                    if (file != null || bo.getIdImageInfo() > 0) {
                        int idImageData = 0;

                        if (required == true) {
                            if (Utilerias.isRepeated(
                                    Utilerias.TablesValueNames.IMAGEINFO.name(),
                                    Utilerias.TablesValueNames.IMAGEINFO.getCaption(),
                                    bo.getName(), "IDIMAGEINFO", bo.getIdImageInfo())) {
                                JOptionPane.showMessageDialog(null, "Este nombre de Imagen ya se encuentra guardado");
                                findImageInfo(null);
                                return;
                            }
                            int i = dao.insertUpdate(bo);
                            if (bo.getIdImageInfo() > 0) {
                                idImageData = getIdImageData(bo.getIdImageInfo());
                                //JOptionPane.showMessageDialog(this, "Se han modificado los datos del catálogo");
                            }

                            if (file != null && checkValidImageData(file)) {
                                if (SaveImageData(file, i, idImageData)) {
                                    JOptionPane.showMessageDialog(null, " Los datos se guardaron exitosamente ");
                                    fc.setSelectedFile(null);
                                } else {
                                    return;
                                }
                                findImageInfo(inputImageInfo.getText());
                                btnSaveImageInfo.setText(SAVE_LABEL);
                                btnSaveImageInfo.setEnabled(false);
                                btnNewImageInfo.setText(NEW_LABEL);
                                btnNewImageInfo.setEnabled(true);
                                btnDeleteImageInfo.setEnabled(false);
                            } else {
                                int IdImageInfo = model.getUniqueRowIdentify(index);
                                ImageDataDAO daoData = new ImageDataDAO();
                                List<ImageDataBO> list = daoData.get(IdImageInfo);
                                if (list != null && !list.isEmpty()
                                        && list.get(0) != null
                                        && list.get(0).getImage() != null
                                        && list.get(0).getImage().length() > 0) {
                                    JOptionPane.showMessageDialog(null, " Los datos se guardaron exitosamente ");
                                    fc.setSelectedFile(null);
                                    findImageInfo(inputImageInfo.getText());
                                    btnSaveImageInfo.setText(SAVE_LABEL);
                                    btnSaveImageInfo.setEnabled(false);
                                    btnNewImageInfo.setText(NEW_LABEL);
                                    btnNewImageInfo.setEnabled(true);
                                    btnDeleteImageInfo.setEnabled(false);
                                } else {
                                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(),
                                            "Selecciona una Imagen Valida",
                                            "Inane warning",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            }
                            if (i > 0 || i == -1) {
                                findSubject(inputSubject.getText());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Complete campos requeridos*" + fields);
                        }
                    } else {
                        JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(),
                                "Selecciona una Imagen Valida",
                                "Inane warning",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    Utilerias.logger(getClass()).info(e);
                }
            }
        } else if (btnSaveImageInfo.getText().equals(EDIT_LABEL)) {
            //obtener la fila del elemento seleccionado 
            

            imageInfoTable.setCursor(null);
            btnSaveImageInfo.setText(SAVE_LABEL);

            /*Controles de imagen*/
            btnSaveImageInfo.setText(SAVE_LABEL);
            btnSaveImageInfo.setEnabled(true);
            btnNewImageInfo.setText(CANCEL_LABEL);
            imageButton.setEnabled(true);
            
            int row = imageInfoTable.getSelectedRow();
            imageInfoTable.editCellAt(row, 1);
            imageInfoTable.requestFocus();
        }
    }

    private List<Integer> imagesToDelete;

    private void deleteImageInfo() {
        ImageInfoModel model = imageInfoTable.getModel() instanceof DefaultTableModel
                ? new ImageInfoModel()
                : (ImageInfoModel) imageInfoTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            int result = JOptionPane.showConfirmDialog(
                    MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                imagesToDelete = new ArrayList<>();
                for (int row = 0; row < a2.length; row++) {
                    if (model.isCheckedRow(row) == true) {
                        imagesToDelete.add(model.getUniqueRowIdentify(row));

                    }
                }
                if (!imagesToDelete.isEmpty()) {
                    Object[][] data2 = new Object[a2.length - imagesToDelete.size()][];
                    int cont = 0;
                    for (int row = 0; row < a2.length; row++) {
                        if (!model.isCheckedRow(row)) {
                            data2[cont] = model.getData()[row];
                            cont++;
                        }
                    }
                    model = new ImageInfoModel();
                    model.setData(data2);
                    imageInfoTable.setModel(model);
                    fitColumnsImageInfo(imageInfoTable);
                }

                if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
                    btnSaveImageInfo.setText(SAVE_LABEL);
                    btnSaveImageInfo.setEnabled(true);
                    btnNewImageInfo.setText(CANCEL_LABEL);
                    imageButton.setEnabled(true);
                }
            } else {
                fc.setSelectedFile(null);
                findImageInfo(inputImageInfo.getText());
            }
            //Confirmar eliminacion 
            /*if (ids.isEmpty() == false) {
             int result = JOptionPane.showConfirmDialog(
             MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
             if (result == JOptionPane.YES_OPTION) {
             ImageInfoDAO dao = new ImageInfoDAO();
             boolean delete = dao.delete(ids);
             String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
             if (delete) {
             findImageInfo(inputImageInfo.getText());
             }
             JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
             }
             }*/
        }
    }

    private void fitColumnsImageInfo(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));

            Utilerias.adjustJTableRowSizes(table);

        } catch (Exception e) {

        }
    }

/* ************************ IMAGE DATA ******************************************* */
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return bi;
    }

    private void getImageData(int IdImageInfo) {
        ImageDataDAO dao = new ImageDataDAO();
        List<ImageDataBO> list = dao.get(IdImageInfo);
        try {
            if (list == null || list.isEmpty()) {
                return;
            }
            //list.get(StatementConstant.SC_0.get()).getImage().
            BufferedImage image = ImageIO.read(list.get(StatementConstant.SC_0.get()).getImage().getBinaryStream());
            BufferedImage resizedImage = resize(image, 150, 160);
            imageInLabel.setIcon(new ImageIcon(resizedImage));

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Catalogs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getIdImageData(int idImageInfo) {
        ImageDataDAO dao = new ImageDataDAO();
        List<ImageDataBO> list = dao.get(idImageInfo);
        int idImageData = list.get(StatementConstant.SC_0.get()).getIdImageData();
        return idImageData;
    }

    private void imageDatafileDialog(boolean b) {
        if (b) {
            imageButton.setEnabled(b);

        } else {
            imageButton.setEnabled(b);

        }
    }

    private boolean SaveImageData(File file, int idImageInfo, int idImageData) {
        boolean b = false;
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            ImageDataDAO dao = new ImageDataDAO();
            ImageDataBO bo = new ImageDataBO();

            bo.setIdImageInfo(idImageInfo);
            bo.setIdImageData(idImageData);
            Blob blob = new javax.sql.rowset.serial.SerialBlob(data);
            bo.setImage(blob);
            dao.insertUpdate(bo);
            b = true;

        } catch (IOException | SQLException ex) {
            Utilerias.logger(getClass()).info(ex);
        }

        return b;
    }

    private boolean checkValidImageData(File f) {

        boolean b = true;

        String extension = checkExtension(f);

        if (extension != null) {
            return extension.equals(GlobalDefines.jpeg)
                    || extension.equals(GlobalDefines.jpg)
                    || extension.equals(GlobalDefines.gif)
                    || extension.equals(GlobalDefines.tiff)
                    || extension.equals(GlobalDefines.tif)
                    || extension.equals(GlobalDefines.png);
        } else {
            b = false;
        }

        return b;
    }

    private String checkExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

/* ******************************* PUBLICITY **********************************/
    private void selectImage() {
        int returnVal = filePublicity.showOpenDialog(null);
        File file = filePublicity.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage images = ImageIO.read(file);
                BufferedImage resizedImage = resize(images, 195, 206);
                ImageIcon ii = new ImageIcon(resizedImage);
                publicityVisor.setText(null);
                publicityVisor.setIcon(ii);
                urlImagePublicity1.setText("");
            } catch (IOException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            pathFile = file.getAbsolutePath();
        } else {
            //imageInLabel.setText("");
            pathFile = "";
            filePublicity.setSelectedFile(null);
        }
    }

    private void getPublicityEditorValues() {
        TextEditorDAO dao = new TextEditorDAO();
        List<TextEditorBO> list = dao.get();

        if (list != null && list.size() > 0) {

            Vector<String> v3 = new Vector<>();


            /*  Show ekit custom buttons */
            if (list.get(StatementConstant.SC_0.get()).isCopy()) {
                editorCopy1.setSelected(list.get(StatementConstant.SC_0.get()).isCopy());
                v3.add(EkitCore.KEY_TOOL_COPY);
            }
            if (list.get(StatementConstant.SC_0.get()).isPasteNoFormat()) {
                editorPastenoFormat1.setSelected(list.get(StatementConstant.SC_0.get()).isPasteNoFormat());
                v3.add(EkitCore.KEY_TOOL_PASTEX);
            }
            if (list.get(StatementConstant.SC_0.get()).isPaste()) {
                editorPaste1.setSelected(list.get(StatementConstant.SC_0.get()).isPaste());
                v3.add(EkitCore.KEY_TOOL_PASTE);
            }
            if (list.get(StatementConstant.SC_0.get()).isCut()) {
                editorCut1.setSelected(list.get(StatementConstant.SC_0.get()).isCut());
                v3.add(EkitCore.KEY_TOOL_CUT);
            }
            if (list.get(StatementConstant.SC_0.get()).isBold()) {
                editorBold1.setSelected(list.get(StatementConstant.SC_0.get()).isBold());
                v3.add(EkitCore.KEY_TOOL_BOLD);
            }
            if (list.get(StatementConstant.SC_0.get()).isItalic()) {
                editorItalic1.setSelected(list.get(StatementConstant.SC_0.get()).isItalic());
                v3.add(EkitCore.KEY_TOOL_ITALIC);
            }
            if (list.get(StatementConstant.SC_0.get()).isUnderline()) {
                editorUnderline1.setSelected(list.get(StatementConstant.SC_0.get()).isUnderline());
                v3.add(EkitCore.KEY_TOOL_UNDERLINE);
            }
            if (list.get(StatementConstant.SC_0.get()).isUndo()) {
                editorUndo1.setSelected(list.get(StatementConstant.SC_0.get()).isUndo());
                v3.add(EkitCore.KEY_TOOL_UNDO);
            }
            if (list.get(StatementConstant.SC_0.get()).isRedo()) {
                editorRedo1.setSelected(list.get(StatementConstant.SC_0.get()).isRedo());
                v3.add(EkitCore.KEY_TOOL_REDO);
            }

            if (list.get(StatementConstant.SC_0.get()).isLeftalign()) {
                leftAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isLeftalign());
                v3.add(EkitCore.KEY_TOOL_ALIGNL);
            }

            if (list.get(StatementConstant.SC_0.get()).isCenteralign()) {
                centerAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isCenteralign());
                v3.add(EkitCore.KEY_TOOL_ALIGNC);
            }

            if (list.get(StatementConstant.SC_0.get()).isRightalign()) {
                rightAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isRightalign());
                v3.add(EkitCore.KEY_TOOL_ALIGNR);
            }

            if (list.get(StatementConstant.SC_0.get()).isUnorderlist()) {
                unorder1.setSelected(list.get(StatementConstant.SC_0.get()).isUnorderlist());
                v3.add(EkitCore.KEY_TOOL_ULIST);
            }

            if (list.get(StatementConstant.SC_0.get()).isOrderlist()) {
                order1.setSelected(list.get(StatementConstant.SC_0.get()).isOrderlist());
                v3.add(EkitCore.KEY_TOOL_OLIST);
            }

            /*
             if (list.get(StatementConstant.SC_0.get()).isUnicodecharacter()) {
             unicode1.setSelected(list.get(StatementConstant.SC_0.get()).isUnicodecharacter());
             v3.add(EkitCore.KEY_TOOL_UNICODE);
             }

             if (list.get(StatementConstant.SC_0.get()).isMathsymbols()) {
             mathSymbols1.setSelected(list.get(StatementConstant.SC_0.get()).isMathsymbols());
             v3.add(EkitCore.KEY_TOOL_UNIMATH);
             }

             if (list.get(StatementConstant.SC_0.get()).isStrikethrough()) {
             strikeThrough1.setSelected(list.get(StatementConstant.SC_0.get()).isStrikethrough());
             v3.add(EkitCore.KEY_TOOL_STRIKE);
             }
             */
            ekitCore3 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
            try {
                //setLayout(new GridBagLayout());
                GridBagConstraints gbc1 = new GridBagConstraints();
                gbc1.fill = GridBagConstraints.HORIZONTAL;
                gbc1.anchor = GridBagConstraints.NORTH;
                gbc1.gridheight = 1;
                gbc1.gridwidth = 1;
                gbc1.weightx = 1.0;
                gbc1.weighty = 0.0;
                gbc1.gridx = 1;
                JToolBar customBar1 = ekitCore3.customizeToolBar(EkitCore.TOOLBAR_MAIN, v3, true);
                textEditorCointainer2.removeAll();
                textEditorCointainer2.add(customBar1, gbc1);

                gbc1.anchor = GridBagConstraints.SOUTH;
                gbc1.fill = GridBagConstraints.BOTH;
                gbc1.weighty = 1.0;
                gbc1.gridy = 4;
                textEditorCointainer2.add(ekitCore3, gbc1);
                textEditorCointainer2.updateUI();
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

    }

    private void savePublicity() {

        PublicityBO bo = new PublicityBO();
        bo.setIdPublicity(idPublicity);
        boolean b = true;
        String text;

        if (publicityText.isSelected()) {

            try {
                text = ekitCore3.getTextPane().getText();
                Clob cl = new SerialClob(text.toCharArray());
                if (text != null) {
                    bo.setText(cl);
                } else {
                    JOptionPane.showMessageDialog(null, "El Texto no puede ir vacío.", "Inane error", JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        } else if (publicityImage.isSelected()) {
            try {

                File file = filePublicity.getSelectedFile();
                Icon icon = publicityVisor.getIcon();
                if (file != null) {
                    if (checkSize()) {
                        byte[] data = Files.readAllBytes(file.toPath());
                        Blob blob = new javax.sql.rowset.serial.SerialBlob(data);
                        bo.setImage(blob);
                        bo.setUrl(urlImagePublicity1.getText().trim());
                        bo.setDescription(descripcion.getText().trim());
                    } else {
                        JOptionPane.showMessageDialog(this, "La imagen debe tener una resolución de 900x180px");
                        return;
                    }
                } else if (icon != null) {
                    BufferedImage image = ImageIO.read(new URL(urlImagePublicity1.getText()));
                    if (image != null) {
                        byte[] by = Utilerias.imageToByteArray(image);
                        Blob blob = new javax.sql.rowset.serial.SerialBlob(by);
                        bo.setImage(blob);
                        bo.setDescription(descripcion.getText().trim());
                        bo.setUrl(urlImagePublicity1.getText().trim());
                    }
                }
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        // si no hay errores > guardamos
        if (b) {

            PublicityDAO dao = new PublicityDAO();
            int id = dao.insertUpdate(bo);
            JOptionPane.showMessageDialog(null, "Se guardó correctamente");
        }

        initPublicity();
    }
    BufferedImage image = null;
    private int idPublicity;

    private boolean checkSize() {
        return true;
    }

    private void initPublicity() {
        idPublicity = 0;
        PublicityDAO dao = new PublicityDAO();
        List<PublicityBO> list = dao.get();
        try {
            if (list == null || list.isEmpty()) {
                return;
            }

            idPublicity = list.get(StatementConstant.SC_0.get()).getIdPublicity();
            if (list.get(StatementConstant.SC_0.get()).getImage() != null) {

                ekitCore3.getTextPane().setText(null);

                image = ImageIO.read(list.get(StatementConstant.SC_0.get()).getImage().getBinaryStream());
                BufferedImage resizedImage = resize(image, 194, 205);
                publicityVisor.setText(null);
                publicityVisor.setIcon(new ImageIcon(resizedImage));
                descripcion.setText(list.get(StatementConstant.SC_0.get()).getDescription().trim());
                urlImagePublicity1.setText(list.get(StatementConstant.SC_0.get()).getUrl());
            }

            if (list.get(StatementConstant.SC_0.get()).getText() != null) {

                descripcion.setText(null);
                urlImagePublicity1.setText(null);
                publicityVisor.setIcon(null);
                publicityVisor.setText("No Image");

                try {
                    String valor = list.get(StatementConstant.SC_0.get()).getText().getSubString(1, (int) list.get(StatementConstant.SC_0.get()).getText().length() - 1);
                    ekitCore3.getTextPane().setText(valor);
                } catch (SQLException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }

            }

        } catch (IOException | SQLException ex) {
            Utilerias.logger(getClass()).info(ex);
            idPublicity = 0;
        }
    }

 /******************************* Editor de Texto ******************************/
    private void getEditorValues() {
        TextEditorDAO dao = new TextEditorDAO();
        List<TextEditorBO> list = dao.get();

        if (list != null && list.size() > 0) {

            Vector<String> v = new Vector<>();


            /*  Show ekit custom buttons */
            if (list.get(StatementConstant.SC_0.get()).isCopy()) {
                editorCopy.setSelected(list.get(StatementConstant.SC_0.get()).isCopy());
                v.add(EkitCore.KEY_TOOL_COPY);
            }
            if (list.get(StatementConstant.SC_0.get()).isPasteNoFormat()) {
                editorPastenoFormat.setSelected(list.get(StatementConstant.SC_0.get()).isPasteNoFormat());
                v.add(EkitCore.KEY_TOOL_PASTEX);
            }
            if (list.get(StatementConstant.SC_0.get()).isPaste()) {
                editorPaste.setSelected(list.get(StatementConstant.SC_0.get()).isPaste());
                v.add(EkitCore.KEY_TOOL_PASTE);
            }
            if (list.get(StatementConstant.SC_0.get()).isCut()) {
                editorCut.setSelected(list.get(StatementConstant.SC_0.get()).isCut());
                v.add(EkitCore.KEY_TOOL_CUT);
            }
            if (list.get(StatementConstant.SC_0.get()).isBold()) {
                editorBold.setSelected(list.get(StatementConstant.SC_0.get()).isBold());
                v.add(EkitCore.KEY_TOOL_BOLD);
            }
            if (list.get(StatementConstant.SC_0.get()).isItalic()) {
                editorItalic.setSelected(list.get(StatementConstant.SC_0.get()).isItalic());
                v.add(EkitCore.KEY_TOOL_ITALIC);
            }
            if (list.get(StatementConstant.SC_0.get()).isUnderline()) {
                editorUnderline.setSelected(list.get(StatementConstant.SC_0.get()).isUnderline());
                v.add(EkitCore.KEY_TOOL_UNDERLINE);
            }
            if (list.get(StatementConstant.SC_0.get()).isUndo()) {
                editorUndo.setSelected(list.get(StatementConstant.SC_0.get()).isUndo());
                v.add(EkitCore.KEY_TOOL_UNDO);
            }
            if (list.get(StatementConstant.SC_0.get()).isRedo()) {
                editorRedo.setSelected(list.get(StatementConstant.SC_0.get()).isRedo());
                v.add(EkitCore.KEY_TOOL_REDO);
            }

            if (list.get(StatementConstant.SC_0.get()).isLeftalign()) {
                leftAlign.setSelected(list.get(StatementConstant.SC_0.get()).isLeftalign());
                v.add(EkitCore.KEY_TOOL_ALIGNL);
            }

            if (list.get(StatementConstant.SC_0.get()).isCenteralign()) {
                centerAlign.setSelected(list.get(StatementConstant.SC_0.get()).isCenteralign());
                v.add(EkitCore.KEY_TOOL_ALIGNC);
            }

            if (list.get(StatementConstant.SC_0.get()).isRightalign()) {
                rightAlign.setSelected(list.get(StatementConstant.SC_0.get()).isRightalign());
                v.add(EkitCore.KEY_TOOL_ALIGNR);
            }

            if (list.get(StatementConstant.SC_0.get()).isUnorderlist()) {
                unorder.setSelected(list.get(StatementConstant.SC_0.get()).isUnorderlist());
                v.add(EkitCore.KEY_TOOL_ULIST);
            }

            if (list.get(StatementConstant.SC_0.get()).isOrderlist()) {
                order.setSelected(list.get(StatementConstant.SC_0.get()).isOrderlist());
                v.add(EkitCore.KEY_TOOL_OLIST);
            }

            if (list.get(StatementConstant.SC_0.get()).isUnicodecharacter()) {
                unicode.setSelected(list.get(StatementConstant.SC_0.get()).isUnicodecharacter());
                v.add(EkitCore.KEY_TOOL_UNICODE);
            }

            if (list.get(StatementConstant.SC_0.get()).isMathsymbols()) {
                mathSymbols.setSelected(list.get(StatementConstant.SC_0.get()).isMathsymbols());
                v.add(EkitCore.KEY_TOOL_UNIMATH);
            }

            if (list.get(StatementConstant.SC_0.get()).isStrikethrough()) {
                strikeThrough.setSelected(list.get(StatementConstant.SC_0.get()).isStrikethrough());
                v.add(EkitCore.KEY_TOOL_STRIKE);
            }

            ekitCore3 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
            try {
                //setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.gridheight = 1;
                gbc.gridwidth = 1;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.gridx = 1;
                JToolBar customBar = ekitCore3.customizeToolBar(EkitCore.TOOLBAR_MAIN, v, true);
                textEditorCointainer.removeAll();
                textEditorCointainer.add(customBar, gbc);

                gbc.anchor = GridBagConstraints.SOUTH;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1.0;
                gbc.gridy = 4;
                textEditorCointainer.add(ekitCore3, gbc);
                textEditorCointainer.updateUI();
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

    }

    private void setEnabledEditor(boolean b) {
        editorCopy.setEnabled(b);
        editorPaste.setEnabled(b);
        editorPastenoFormat.setEnabled(b);
        editorCut.setEnabled(b);
        editorBold.setEnabled(b);
        editorUnderline.setEnabled(b);
        editorItalic.setEnabled(b);
        editorUndo.setEnabled(b);
        editorRedo.setEnabled(b);
        leftAlign.setEnabled(b);
        centerAlign.setEnabled(b);
        rightAlign.setEnabled(b);
        order.setEnabled(b);
        unorder.setEnabled(b);
        unicode.setEnabled(b);
        mathSymbols.setEnabled(b);
        strikeThrough.setEnabled(b);

    }

    private void saveTextEditor() {

        TextEditorBO bo = new TextEditorBO();
        TextEditorDAO dao = new TextEditorDAO();

        bo.setCopy(editorCopy.isSelected());
        bo.setPaste(editorPaste.isSelected());
        bo.setPasteNoFormat(editorPastenoFormat.isSelected());
        bo.setCut(editorCut.isSelected());
        bo.setBold(editorBold.isSelected());
        bo.setUnderline(editorUnderline.isSelected());
        bo.setItalic(editorItalic.isSelected());
        bo.setUndo(editorUndo.isSelected());
        bo.setRedo(editorRedo.isSelected());
        bo.setLeftalign(leftAlign.isSelected());
        bo.setCenteralign(centerAlign.isSelected());
        bo.setRightalign(rightAlign.isSelected());
        bo.setUnorderlist(unorder.isSelected());
        bo.setOrderlist(order.isSelected());
        bo.setUnicodecharacter(unicode.isSelected());
        bo.setMathsymbols(mathSymbols.isSelected());
        bo.setStrikethrough(strikeThrough.isSelected());

        dao.Update(bo);
        setEnabledEditor(false);
        btnEditEditor.setText(EDIT_LABEL);
        btnSaveTextEditor.setEnabled(false);
        getEditorValues();
    }

 /********************************* Editor de Bullets **************************/
    private void getBulletsEditorValues() {
        BulletsEditorDAO dao = new BulletsEditorDAO();
        List<BulletsEditorBO> list = dao.get();

        if (list != null && list.size() > 0) {

            Vector<String> v2 = new Vector<>();

            editorCopy1.setSelected(false);
            editorPastenoFormat1.setSelected(false);
            editorPaste1.setSelected(false);
            editorCut1.setSelected(false);
            editorBold1.setSelected(false);
            editorPastenoFormat1.setSelected(false);
            editorPaste1.setSelected(false);
            editorCut1.setSelected(false);
            editorBold1.setSelected(false);
            editorItalic1.setSelected(false);
            editorUnderline1.setSelected(false);
            editorUndo1.setSelected(false);
            editorRedo1.setSelected(false);
            leftAlign1.setSelected(false);
            centerAlign1.setSelected(false);
            rightAlign1.setSelected(false);
            unorder1.setSelected(false);
            order1.setSelected(false);
            unicode1.setSelected(false);
            mathSymbols1.setSelected(false);
            strikeThrough1.setSelected(false);

            /*  Show ekit custom buttons */
            if (list.get(StatementConstant.SC_0.get()).isCopy()) {
                editorCopy1.setSelected(list.get(StatementConstant.SC_0.get()).isCopy());
                v2.add(EkitCore.KEY_TOOL_COPY);
            }
            if (list.get(StatementConstant.SC_0.get()).isPasteNoFormat()) {
                editorPastenoFormat1.setSelected(list.get(StatementConstant.SC_0.get()).isPasteNoFormat());
                v2.add(EkitCore.KEY_TOOL_PASTEX);
            }
            if (list.get(StatementConstant.SC_0.get()).isPaste()) {
                editorPaste1.setSelected(list.get(StatementConstant.SC_0.get()).isPaste());
                v2.add(EkitCore.KEY_TOOL_PASTE);
            }
            if (list.get(StatementConstant.SC_0.get()).isCut()) {
                editorCut1.setSelected(list.get(StatementConstant.SC_0.get()).isCut());
                v2.add(EkitCore.KEY_TOOL_CUT);
            }
            if (list.get(StatementConstant.SC_0.get()).isBold()) {
                editorBold1.setSelected(list.get(StatementConstant.SC_0.get()).isBold());
                v2.add(EkitCore.KEY_TOOL_BOLD);
            }
            if (list.get(StatementConstant.SC_0.get()).isItalic()) {
                editorItalic1.setSelected(list.get(StatementConstant.SC_0.get()).isItalic());
                v2.add(EkitCore.KEY_TOOL_ITALIC);
            }
            if (list.get(StatementConstant.SC_0.get()).isUnderline()) {
                editorUnderline1.setSelected(list.get(StatementConstant.SC_0.get()).isUnderline());
                v2.add(EkitCore.KEY_TOOL_UNDERLINE);
            }
            if (list.get(StatementConstant.SC_0.get()).isUndo()) {
                editorUndo1.setSelected(list.get(StatementConstant.SC_0.get()).isUndo());
                v2.add(EkitCore.KEY_TOOL_UNDO);
            }
            if (list.get(StatementConstant.SC_0.get()).isRedo()) {
                editorRedo1.setSelected(list.get(StatementConstant.SC_0.get()).isRedo());
                v2.add(EkitCore.KEY_TOOL_REDO);
            }

            if (list.get(StatementConstant.SC_0.get()).isLeftalign()) {
                leftAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isLeftalign());
                v2.add(EkitCore.KEY_TOOL_ALIGNL);
            }

            if (list.get(StatementConstant.SC_0.get()).isCenteralign()) {
                centerAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isCenteralign());
                v2.add(EkitCore.KEY_TOOL_ALIGNC);
            }

            if (list.get(StatementConstant.SC_0.get()).isRightalign()) {
                rightAlign1.setSelected(list.get(StatementConstant.SC_0.get()).isRightalign());
                v2.add(EkitCore.KEY_TOOL_ALIGNR);
            }

            if (list.get(StatementConstant.SC_0.get()).isUnorderlist()) {
                unorder1.setSelected(list.get(StatementConstant.SC_0.get()).isUnorderlist());
                v2.add(EkitCore.KEY_TOOL_ULIST);
            }

            if (list.get(StatementConstant.SC_0.get()).isOrderlist()) {
                order1.setSelected(list.get(StatementConstant.SC_0.get()).isOrderlist());
                v2.add(EkitCore.KEY_TOOL_OLIST);
            }

            if (list.get(StatementConstant.SC_0.get()).isUnicodecharacter()) {
                unicode1.setSelected(list.get(StatementConstant.SC_0.get()).isUnicodecharacter());
                v2.add(EkitCore.KEY_TOOL_UNICODE);
            }

            if (list.get(StatementConstant.SC_0.get()).isMathsymbols()) {
                mathSymbols1.setSelected(list.get(StatementConstant.SC_0.get()).isMathsymbols());
                v2.add(EkitCore.KEY_TOOL_UNIMATH);
            }

            if (list.get(StatementConstant.SC_0.get()).isStrikethrough()) {
                strikeThrough1.setSelected(list.get(StatementConstant.SC_0.get()).isStrikethrough());
                v2.add(EkitCore.KEY_TOOL_STRIKE);
            }

            EkitCore ekitCore1 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
            try {
                //setLayout(new GridBagLayout());
                GridBagConstraints gbc1 = new GridBagConstraints();
                gbc1.fill = GridBagConstraints.HORIZONTAL;
                gbc1.anchor = GridBagConstraints.NORTH;
                gbc1.gridheight = 1;
                gbc1.gridwidth = 1;
                gbc1.weightx = 1.0;
                gbc1.weighty = 0.0;
                gbc1.gridx = 1;
                JToolBar customBar1 = ekitCore1.customizeToolBar(EkitCore.TOOLBAR_MAIN, v2, true);
                textEditorCointainer1.removeAll();
                textEditorCointainer1.add(customBar1, gbc1);

                gbc1.anchor = GridBagConstraints.SOUTH;
                gbc1.fill = GridBagConstraints.BOTH;
                gbc1.weighty = 1.0;
                gbc1.gridy = 4;
                textEditorCointainer1.add(ekitCore1, gbc1);
                textEditorCointainer1.updateUI();
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

    }

    private void setEnabledBulletsEditor(boolean b) {
        editorCopy1.setEnabled(b);
        editorPaste1.setEnabled(b);
        editorPastenoFormat1.setEnabled(b);
        editorCut1.setEnabled(b);
        editorBold1.setEnabled(b);
        editorUnderline1.setEnabled(b);
        editorItalic1.setEnabled(b);
        editorUndo1.setEnabled(b);
        editorRedo1.setEnabled(b);
        leftAlign1.setEnabled(b);
        centerAlign1.setEnabled(b);
        rightAlign1.setEnabled(b);
//        order1.setEnabled(b);
//        unorder1.setEnabled(b);
        unicode1.setEnabled(b);
        mathSymbols1.setEnabled(b);
        strikeThrough1.setEnabled(b);

    }

    private void saveBulletsEditor() {

        BulletsEditorBO bo = new BulletsEditorBO();
        BulletsEditorDAO dao = new BulletsEditorDAO();

        bo.setCopy(editorCopy1.isSelected());
        bo.setPaste(editorPaste1.isSelected());
        bo.setPasteNoFormat(editorPastenoFormat1.isSelected());
        bo.setCut(editorCut1.isSelected());
        bo.setBold(editorBold1.isSelected());
        bo.setUnderline(editorUnderline1.isSelected());
        bo.setItalic(editorItalic1.isSelected());
        bo.setUndo(editorUndo1.isSelected());
        bo.setRedo(editorRedo1.isSelected());
        bo.setLeftalign(leftAlign1.isSelected());
        bo.setCenteralign(centerAlign1.isSelected());
        bo.setRightalign(rightAlign1.isSelected());
        bo.setUnorderlist(unorder1.isSelected());
        bo.setOrderlist(order1.isSelected());
        bo.setUnicodecharacter(unicode1.isSelected());
        bo.setMathsymbols(mathSymbols1.isSelected());
        bo.setStrikethrough(strikeThrough1.isSelected());
        dao.Update(bo);
        setEnabledBulletsEditor(false);
        btnEditEditor1.setText(EDIT_LABEL);
        btnSaveTextEditor1.setEnabled(false);
        getBulletsEditorValues();
    }

 /******************************** Header & Footer *****************************/
    private void addHeader() {
        int returnVal = fileHeader.showOpenDialog(null);
        File file = fileHeader.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage images = ImageIO.read(file);
                BufferedImage resizedImage = resize(images, 530, 128);
                ImageIcon ii = new ImageIcon(resizedImage);
                headerVisor.setText(null);
                headerVisor.setIcon(ii);
            } catch (IOException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            pathFile = file.getAbsolutePath();
        } else {
            //imageInLabel.setText("");
            pathFile = "";
        }
    }

    private void addFooter() {
        int returnVal = fileFooter.showOpenDialog(null);
        File file = fileFooter.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage resizedImage = resize(image, 530, 128);
                ImageIcon ii = new ImageIcon(resizedImage);
                footerVisor.setText(null);
                footerVisor.setIcon(ii);
            } catch (IOException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            pathFile = file.getAbsolutePath();
        } else {
            //imageInLabel.setText("");
            pathFile = "";
        }
    }

    private void saveHeaderFooter() {

        boolean b = true;
        HeaderfooterBO bo = new HeaderfooterBO();
        bo.setId(1);

        Icon i = headerVisor.getIcon();
        File file = fileHeader.getSelectedFile();
        if (i != null) {
            try {
                if (file != null) {
                    byte[] data = Files.readAllBytes(file.toPath());
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(data);
                    bo.setHeader(blob);
                }
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }
        Icon i2 = footerVisor.getIcon();
        File file2 = fileFooter.getSelectedFile();
        if (i2 != null) {
            try {
                if (file2 != null) {
                    byte[] data = Files.readAllBytes(file2.toPath());
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(data);
                    bo.setFooter(blob);
                }
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }

        }

        if (i != null || i2 != null) {
            HeaderfooterDAO dao = new HeaderfooterDAO();
            int id = dao.insertUpdate(bo);

//            if (file != null && !file.getName().isEmpty())//Se agrega el header
//            {
//                UtileriasSSH.getInstance().sendHeaderOrFooter(file, true);
//            }
//            if (file2 != null && !file2.getName().isEmpty())//Se agrega el footer
//            {
//                UtileriasSSH.getInstance().sendHeaderOrFooter(file2, false);
//            }

            JOptionPane.showMessageDialog(null, "Se guardó correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Debe insertar una imágen", "Inane error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initHeaderFooter() {
        HeaderfooterDAO dao = new HeaderfooterDAO();
        List<HeaderfooterBO> list = dao.get();

        try {

            if (list == null || list.isEmpty()) {
                return;
            }

            if (list.get(0).getHeader() != null) {
                BufferedImage image = ImageIO.read(list.get(StatementConstant.SC_0.get()).getHeader().getBinaryStream());
                BufferedImage resizedImage = resize(image, 530, 128);
                headerVisor.setText(null);
                headerVisor.setIcon(new ImageIcon(resizedImage));
            }
            if (list.get(0).getFooter() != null) {
                BufferedImage image2 = ImageIO.read(list.get(StatementConstant.SC_0.get()).getFooter().getBinaryStream());
                BufferedImage resizedImage2 = resize(image2, 530, 128);
                footerVisor.setText(null);
                footerVisor.setIcon(new ImageIcon(resizedImage2));
            }

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Catalogs.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cleanHeader() {
        HeaderfooterBO bo = new HeaderfooterBO();
        bo.setId(1);
        bo.setHeader(null);
        HeaderfooterDAO dao = new HeaderfooterDAO();
        if (dao.deleteHeader(bo)) {
            JOptionPane.showMessageDialog(this, "Se ha eliminado correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    private void cleanFooter() {
        HeaderfooterBO bo = new HeaderfooterBO();
        bo.setId(1);
        bo.setHeader(null);
        HeaderfooterDAO dao = new HeaderfooterDAO();
        if (dao.deleteFooter(bo)) {
            if (!isDeleteHeader) {
                JOptionPane.showMessageDialog(this, "Se ha eliminado correctamente");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Markets;
    private javax.swing.JPanel Menu;
    private javax.swing.JLabel OELabel;
    private javax.swing.JTable OETable;
    private javax.swing.JScrollPane ScrollTableSubject;
    private javax.swing.JTabbedPane TabCatalogos;
    private javax.swing.JButton btnDeleteImageInfo;
    private javax.swing.JButton btnDeleteImageInfo1;
    private javax.swing.JButton btnDeleteImageInfo3;
    private javax.swing.JButton btnDeleteImageInfo4;
    private javax.swing.JButton btnDeleteIndustry;
    private javax.swing.JButton btnDeleteLanguage;
    private javax.swing.JButton btnDeleteMarket;
    private javax.swing.JButton btnDeleteOE;
    private javax.swing.JButton btnDeleteSubject;
    private javax.swing.JButton btnEditEditor;
    private javax.swing.JButton btnEditEditor1;
    private javax.swing.JButton btnFindIndustry;
    private javax.swing.JButton btnFindLanguage;
    private javax.swing.JButton btnFindMarket;
    private javax.swing.JButton btnFindOE;
    private javax.swing.JButton btnImageInfoSearch;
    private javax.swing.JButton btnNewImageInfo;
    private javax.swing.JButton btnNewIndustry;
    private javax.swing.JButton btnNewLanguage;
    private javax.swing.JButton btnNewMarket;
    private javax.swing.JButton btnNewOE;
    private javax.swing.JButton btnNewSubject;
    private javax.swing.JButton btnSaveImageInfo;
    private javax.swing.JButton btnSaveImageInfo1;
    private javax.swing.JButton btnSaveImageInfo2;
    private javax.swing.JButton btnSaveIndustry;
    private javax.swing.JButton btnSaveLanguage;
    private javax.swing.JButton btnSaveMarket;
    private javax.swing.JButton btnSaveOE;
    private javax.swing.JButton btnSaveSubject;
    private javax.swing.JButton btnSaveTextEditor;
    private javax.swing.JButton btnSaveTextEditor1;
    private javax.swing.JPanel bulletsEditor;
    private javax.swing.JRadioButton centerAlign;
    private javax.swing.JRadioButton centerAlign1;
    private javax.swing.JTextField descripcion;
    private javax.swing.JRadioButton editorBold;
    private javax.swing.JRadioButton editorBold1;
    private javax.swing.JRadioButton editorCopy;
    private javax.swing.JRadioButton editorCopy1;
    private javax.swing.JRadioButton editorCut;
    private javax.swing.JRadioButton editorCut1;
    private javax.swing.JRadioButton editorItalic;
    private javax.swing.JRadioButton editorItalic1;
    private javax.swing.JRadioButton editorPaste;
    private javax.swing.JRadioButton editorPaste1;
    private javax.swing.JRadioButton editorPastenoFormat;
    private javax.swing.JRadioButton editorPastenoFormat1;
    private javax.swing.JRadioButton editorRedo;
    private javax.swing.JRadioButton editorRedo1;
    private javax.swing.JRadioButton editorUnderline;
    private javax.swing.JRadioButton editorUnderline1;
    private javax.swing.JRadioButton editorUndo;
    private javax.swing.JRadioButton editorUndo1;
    private javax.swing.JLabel footerVisor;
    private javax.swing.JPanel headerFooter;
    private javax.swing.JLabel headerVisor;
    private javax.swing.JButton imageButton;
    private javax.swing.JPanel imageDataField;
    private javax.swing.JLabel imageInLabel;
    private javax.swing.JTable imageInfoTable;
    private javax.swing.JPanel imagesLoad;
    private javax.swing.JPanel imagesPublicity;
    private javax.swing.JPanel industry;
    private javax.swing.JPanel industry1;
    private javax.swing.JLabel industryLabel;
    private javax.swing.JScrollPane industryScroll;
    private javax.swing.JScrollPane industryScroll1;
    private javax.swing.JScrollPane industryScroll2;
    private javax.swing.JScrollPane industryScroll3;
    private javax.swing.JTable industryTable;
    private javax.swing.JTextField inputImageInfo;
    private javax.swing.JTextField inputIndustry;
    private javax.swing.JTextField inputLanguage;
    private javax.swing.JTextField inputMarket;
    private javax.swing.JTextField inputOE;
    private javax.swing.JTextField inputSubject;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JLabel labelSubject;
    private javax.swing.JTable languageTable;
    private javax.swing.JPanel languages;
    private javax.swing.JRadioButton leftAlign;
    private javax.swing.JRadioButton leftAlign1;
    private javax.swing.JLabel marketLabel;
    private javax.swing.JTable marketTable;
    private javax.swing.JRadioButton mathSymbols;
    private javax.swing.JRadioButton mathSymbols1;
    private javax.swing.JPanel menuCatalogo;
    private javax.swing.JRadioButton order;
    private javax.swing.JRadioButton order1;
    private javax.swing.JPanel outgoingEmail;
    private javax.swing.JPanel panelPublicity;
    private javax.swing.JPanel panelPublicity1;
    private javax.swing.JPanel publicidad;
    private javax.swing.JRadioButton publicityImage;
    private javax.swing.JRadioButton publicityText;
    private javax.swing.JLabel publicityVisor;
    private javax.swing.JRadioButton rightAlign;
    private javax.swing.JRadioButton rightAlign1;
    private javax.swing.JButton searchSubject;
    private javax.swing.JRadioButton strikeThrough;
    private javax.swing.JRadioButton strikeThrough1;
    private javax.swing.JPanel subject;
    private javax.swing.JTable subjectTable;
    private javax.swing.JTabbedPane tabPublicity;
    private javax.swing.JPanel tableSubjectPanel;
    private javax.swing.JPanel textEditor;
    private javax.swing.JPanel textEditorCointainer;
    private javax.swing.JPanel textEditorCointainer1;
    private javax.swing.JPanel textEditorCointainer2;
    private javax.swing.JPanel textPublicity;
    private javax.swing.JRadioButton unicode;
    private javax.swing.JRadioButton unicode1;
    private javax.swing.JRadioButton unorder;
    private javax.swing.JRadioButton unorder1;
    private javax.swing.JTextField urlImagePublicity1;
    // End of variables declaration//GEN-END:variables
    JFileChooser fc = new JFileChooser();
    JFileChooser filePublicity = new JFileChooser();

    JFileChooser fileHeader = new JFileChooser();
    JFileChooser fileFooter = new JFileChooser();

    private JComboBox generateIndustryBox() {
        JComboBox bx = new JComboBox();
//        Connection con = CPool.getConnection();
        try {
            IndustryDAO dAO = new IndustryDAO();
            List<IndustryBO> list = dAO.get("");
            for (IndustryBO o : list) {
                bx.addItem(o);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        return bx;

    }

    private List<IndustryBO> listIndustry;

    public int getIdFromComboBox(String text) {
        int s = 0;
        for (IndustryBO o : listIndustry) {
            if (o.getName().equals(text)) {
                s = o.getIdIndustry();
                break;
            }
        }
        return s;
    }
}
