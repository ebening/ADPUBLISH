/*
 * MainView.java
 */
package com.adinfi.formateador.main;

import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.DlgSaveCollab;
import com.adinfi.formateador.view.QuickViewPanel;
import com.adinfi.formateador.view.ScrDocument;
import com.adinfi.formateador.view.ScrIntegradorDoc;
import com.adinfi.formateador.view.ScrIntegradorMod;
import com.adinfi.formateador.view.SearchDocByCriteria;
import com.adinfi.formateador.view.administration.JTabbedPane_withoutPaintedTabs;
import com.adinfi.formateador.view.publish.SendPublish;
import com.adinfi.ws.ArrayOfSeccion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;

/**
 * The application's main frame.
 */
public final class MainView extends JRibbonFrame {

    private static final Integer DOCUMENT_ID = -1;
    private static final Integer GAP_SIZE = 5;
    private static final Color MARGIN_COLOR = new Color(240, 255, 255);

    private ScrDocument scrDocument;

    /**
     * Paneles principales
     */
    private final JPanel south = new javax.swing.JPanel();
    private final JPanel north = new javax.swing.JPanel();
    private final JPanel east = new javax.swing.JPanel();
    private final JPanel west = new javax.swing.JPanel();

    private final RibbonMenu ribbonMenu = new RibbonMenu();
    public static MainView main = null;

    public MainView(SingleFrameApplication app) {
        initComponents();
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap rm = new ResourceMap(null, getClass().getClassLoader(), "com.adinfi.formateador.main.resources.MainView");
        ResourceMap resourceMap = rm;//getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(MainApp.getApplication().getContext());//(getApplication().getContext());
        taskMonitor.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            String propertyName = evt.getPropertyName();
            switch (propertyName) {
                case "started":
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    getProgressBar().setVisible(true);
                    getProgressBar().setIndeterminate(true);
                    break;
                case "done":
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    getProgressBar().setVisible(false);
                    getProgressBar().setValue(0);
                    break;
                case "message":
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                    break;
                case "progress":
                    int value = (Integer) (evt.getNewValue());
                    getProgressBar().setVisible(true);
                    getProgressBar().setIndeterminate(false);
                    getProgressBar().setValue(value);
                    break;
            }
        });
        
        Utilerias.limpiarDirectorioDocumental(Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR));
        Utilerias.limpiarDirectorioDocumental(new File(Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR)).getParentFile().getAbsolutePath() + "\\gen_images");
        
        initFrame();
        addCustomWindowsListener(app);
    }

    protected void addCustomWindowsListener(final SingleFrameApplication app) {
        app.getMainFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        app.getMainFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Boolean bOkToExit = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Está seguro de salir de la aplicación?") == JOptionPane.YES_OPTION;
                if (bOkToExit == true) {
                    ((MainApp) app).closeApplication();
                }
            }
        });
    }

    private void initFrame() {
        main = this;
        initPanelDocument();
        initScreen();
        initFrameConfiguration();
        //addKeyListener();
        addWindowsListener();
        initInfoLabel(null, null, null);
        initMenuFromProfile();
    }

    private void initMenuFromProfile() {
        try {
            ArrayOfSeccion secciones = InstanceContext.getInstance().getProfile();
            Boolean createMenu = ribbonMenu.getMenu(getRibbon(), secciones);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public void initInfoLabel(String tipoDocumento, String tema, String titulo) {
        String user = InstanceContext.getInstance().getUsuario() == null
                ? ""
                : InstanceContext.getInstance().getUsuario().getNombre();

        StringBuilder sb = new StringBuilder("[Versión ")
                .append(Utilerias.getProperty(ApplicationProperties.APLICACION_VERSION))
                .append("] ")
                .append("adPublish");

        if (tipoDocumento != null && tipoDocumento.isEmpty() == false) {
            sb.append(", ").append(tipoDocumento);
        }

        if (tema != null && tema.isEmpty() == false) {
            sb.append(", ").append(tema);
        }

        if (titulo != null && titulo.isEmpty() == false) {
            sb.append(", ").append(titulo);
        }
        infoLabel.setText(sb.toString());
    }

    private void initPanelDocument() {
        mainPanel.setLayout(new java.awt.BorderLayout());
        south.setBackground(MARGIN_COLOR);
        south.setMaximumSize(new java.awt.Dimension(32767, GAP_SIZE));
        south.setPreferredSize(new java.awt.Dimension(711, GAP_SIZE));
        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
                southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 711, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
                southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, GAP_SIZE, Short.MAX_VALUE)
        );
        mainPanel.add(south, java.awt.BorderLayout.SOUTH);
        north.setBackground(MARGIN_COLOR);
        north.setMaximumSize(new java.awt.Dimension(32767, GAP_SIZE));
        north.setPreferredSize(new java.awt.Dimension(711, GAP_SIZE));
        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
                northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 711, Short.MAX_VALUE)
        );
        northLayout.setVerticalGroup(
                northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, GAP_SIZE, Short.MAX_VALUE)
        );
        mainPanel.add(north, java.awt.BorderLayout.NORTH);
        east.setBackground(MARGIN_COLOR);
        east.setMaximumSize(new java.awt.Dimension(GAP_SIZE, 32767));
        east.setPreferredSize(new java.awt.Dimension(GAP_SIZE, 393));
        javax.swing.GroupLayout eastLayout = new javax.swing.GroupLayout(east);
        east.setLayout(eastLayout);
        eastLayout.setHorizontalGroup(
                eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, GAP_SIZE, Short.MAX_VALUE)
        );
        eastLayout.setVerticalGroup(
                eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 393, Short.MAX_VALUE)
        );
        mainPanel.add(east, java.awt.BorderLayout.EAST);
        west.setBackground(MARGIN_COLOR);
        west.setMaximumSize(new java.awt.Dimension(GAP_SIZE, 32767));
        west.setPreferredSize(new java.awt.Dimension(GAP_SIZE, 393));
        javax.swing.GroupLayout westLayout = new javax.swing.GroupLayout(west);
        west.setLayout(westLayout);
        westLayout.setHorizontalGroup(
                westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, GAP_SIZE, Short.MAX_VALUE)
        );
        westLayout.setVerticalGroup(
                westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 393, Short.MAX_VALUE)
        );

        mainPanel.add(west, java.awt.BorderLayout.WEST);
        mainTabbed.addTab("Editor", mainPanel);
        mainTabbed.addTab("Favoritos", new QuickViewPanel());
        mainTabbed.addTab("Busqueda", new SearchDocByCriteria());
        mainTabbed.setSelectedIndex(StatementConstant.SC_1.get());

        getContentPane().add(mainTabbed, BorderLayout.CENTER);
        getContentPane().add(statusPanel, java.awt.BorderLayout.SOUTH);
    }

    public void setSearchPanel() {
        if (getScrDocument() != null) {
            getScrDocument().setDocumentId(-1);
        }
        mainTabbed.setSelectedIndex(StatementConstant.SC_2.get());
    }

    private void initScreen() {
        try {
            scrDocument = new ScrDocument();
            scrDocument.setDocumentId(DOCUMENT_ID);
            scrDocument.init();
        } catch (SQLException e) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Error de conexion", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).error(e);
            MainApp.getApplication().shutdown();
        }
    }

    private void initFrameConfiguration() {
        setTitle(GlobalDefines.TITULO_APP);
        setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GlobalDefines.INITIAL_FORM_SIZE);
        setVisible(true);
    }

    private void addKeyListener() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomKeyEventDispatcher());
    }

    private void addWindowsListener() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Boolean bOkToExit = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Está seguro de salir de la aplicación?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (bOkToExit == true) {
                    MainApp.getApplication().shutdown();
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MainApp.getApplication().getMainFrame();
            aboutBox = new MainAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MainApp.getApplication().show(aboutBox);
    }

    @Override
    public ResizableIcon getApplicationIcon() {
        return Utilerias.getIcon(Utilerias.ICONS.APP_RIBBON_ICON);
    }

    public void setDocument(int documentID, String name) {
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            this.setTitle(GlobalDefines.TITULO_APP + "  " + name);
            scrDocument = new ScrDocument();
            getScrDocument().setDocumentId(documentID);
            getScrDocument().init();
            mainPanel.removeAll();
            mainPanel.add(getScrDocument());
            mainPanel.revalidate();
            mainPanel.updateUI();
        } catch (SQLException ex) {
            Utilerias.showMessage(this, GlobalDefines.ERROR_CONEXION, JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).error(ex);
        }
    }

    public void setDocument(int documentID, String name, boolean openPublish) {
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            this.setTitle(GlobalDefines.TITULO_APP + "  " + name);
            scrDocument = new ScrDocument();
            getScrDocument().setDocumentId(documentID);
            getScrDocument().setOpenPublish(openPublish);
            getScrDocument().init();
            mainPanel.removeAll();
            mainPanel.add(getScrDocument());
            mainPanel.revalidate();
            mainPanel.updateUI();
        } catch (SQLException ex) {
            Utilerias.showMessage(this, GlobalDefines.ERROR_CONEXION, JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).error(ex);
        }
    }

    public void setCustomComponent(JPanel panel) {
        try {
            if (getScrDocument() != null) {
                getScrDocument().setDocumentId(-1);
            }
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            mainPanel.removeAll();
            mainPanel.add(panel);
            mainPanel.revalidate();
            mainPanel.updateUI();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    public void setCollabIntegradorMod(ScrIntegradorMod scrInt) {
        if (scrInt == null) {
            return;
        }
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            // scrDocument = document;
            this.setTitle(GlobalDefines.TITULO_APP + "  " + scrInt.getDocument().getDocumentName());
            mainPanel.removeAll();
            mainPanel.add(scrInt);
            mainPanel.revalidate();
            mainPanel.repaint();
            mainPanel.updateUI();
            scrInt.loadDocumentPar();
            //  scrInt.loadDocument();

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    public void setCollabIntegradorMod(ScrIntegradorMod scrInt, boolean openPublish) {
        if (scrInt == null) {
            return;
        }
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            // scrDocument = document;
            this.setTitle(GlobalDefines.TITULO_APP + "  " + scrInt.getDocument().getDocumentName());
            mainPanel.removeAll();
            mainPanel.add(scrInt);
            mainPanel.revalidate();
            mainPanel.repaint();
            mainPanel.updateUI();
            scrInt.loadDocumentPar();
            //  scrInt.loadDocument();

            if (openPublish) {
                SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
                Utilerias.centreDialog(dt, true);
                dt.setVisible(dt.showDialog);
                MainView.main.getProgressBar().setVisible(false);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    public void setCollabIntegradorDoc(ScrIntegradorDoc scrInt) {
        if (scrInt == null) {
            return;
        }
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            // scrDocument = document;
            mainPanel.removeAll();
            mainPanel.add(scrInt);
            mainPanel.revalidate();
            mainPanel.repaint();
            mainPanel.updateUI();
            scrInt.loadDocument();

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    public void setCollabIntegradorDoc(ScrIntegradorDoc scrInt, boolean openPublish) {
        if (scrInt == null) {
            return;
        }
        try {
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            this.setTitle(GlobalDefines.TITULO_APP + "  " + scrInt.getDocument().getDocumentName());
            // scrDocument = document;
            mainPanel.removeAll();
            mainPanel.add(scrInt);
            mainPanel.revalidate();
            mainPanel.repaint();
            mainPanel.updateUI();
            scrInt.loadDocument();

            if (openPublish) {
                SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
                Utilerias.centreDialog(dt, true);
                dt.setVisible(dt.showDialog);
                MainView.main.getProgressBar().setVisible(false);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    public void addToMainPanel(ScrDocument document) {
        try {
            if (document == null) {
                return;
            }
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            scrDocument = document;
            mainPanel.removeAll();
            mainPanel.add(document);
            mainPanel.revalidate();
            mainPanel.repaint();
            mainPanel.updateUI();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }
    
    
    public  void newCollabDocument(String tipo , boolean saveAs  ) {
        try {
            DlgSaveCollab dlgSave = new DlgSaveCollab(MainApp.getApplication().getMainFrame(), true, tipo);
            dlgSave.setLocationRelativeTo(dlgSave);
            dlgSave.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            //dlgSave.setTitle(GlobalDefines.TITULO_APP);
            dlgSave.setCollborative(true);
            
            if(GlobalDefines.TIPO_CAND_DOC.equals(tipo) && saveAs){
                ScrIntegradorDoc intDoc = (ScrIntegradorDoc) main.getMainPanel().getComponents()[0];
                dlgSave.loadInfoDocument( intDoc.getDocument() );
            } else if (GlobalDefines.TIPO_CAND_MODULE.equals(tipo) && saveAs){
                ScrIntegradorMod intDoc = (ScrIntegradorMod) main.getMainPanel().getComponents()[0];
                dlgSave.loadInfoDocument( intDoc.getDocument() );
            }
            
            dlgSave.setVisible(true);
            if (dlgSave.isOk() == false) {
                return;
            }

            DocumentCollabBO document=null;
             
            document = new DocumentCollabBO();
            document.setCollaborative(true);
             

            int idms;
            Object obj = dlgSave.getCmbMercado().getSelectedItem();
            if (obj != null) {
                MarketBO marketb = (MarketBO) obj;
                idms = Integer.parseInt(marketb.getIdMiVector_real());
                document.setIdMarket(idms);
                document.setMarketBO(marketb);
            } else {
                return;
            }

            int idDocType;
            Object obj2 = dlgSave.getCmbTipo().getSelectedItem();
            DocumentTypeBO tipoBO = null;
            if (obj2 != null) {
                tipoBO = (DocumentTypeBO) obj2;
                idDocType = tipoBO.getIddocument_type();
                document.setIdDocType(idDocType);
                document.setTipoBO(tipoBO);
            } else {
                return;
            }

            int idLanguage;
            Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
            if (obj3 != null) {
                LanguageBO languageBO = (LanguageBO) obj3;
                idLanguage = languageBO.getIdLanguage();
                document.setIdLanguage(idLanguage);
            } else {
                return;
            }

            int idSubject;
            SubjectBO subjectBO = dlgSave.getSelectedValueSubject();//CboSubject().getSelectedItem();
            if (subjectBO != null) {
                idSubject = subjectBO.getIdSubject();
                document.setIdSubject(idSubject);
            }/* else {
                return;
            }*/

            document.setDocumentName(dlgSave.getEdNombredoc().getText());
            document.setFileName(dlgSave.getEdFileName().getText());
            document.setFavorite(dlgSave.isFavorite().isSelected());

            if (GlobalDefines.TIPO_CAND_DOC.equals(tipo)) {

                if( saveAs==false ){
                ScrIntegradorDoc scrIntegradorDoc = new ScrIntegradorDoc();
                scrIntegradorDoc.newDocument(document);
                //scrIntegradorDoc.loadDocument(document);
                MainView.main.setCollabIntegradorDoc(scrIntegradorDoc);
                }else{
                    ScrIntegradorDoc  scrIntDoc=(ScrIntegradorDoc)MainView.main.getMainPanel().getComponents()[0];
                    if( scrIntDoc!=null ){
                        scrIntDoc.saveAs( document  );
                    }
                    
                    
                    
                }
                 

            } else {

                if( saveAs==false ){
                    ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
                    scrIntegradorMod.newDocument(document);
                    //scrIntegradorDoc.loadDocument(document);
                    MainView.main.setCollabIntegradorMod(scrIntegradorMod);
                }else{
                   ScrIntegradorMod  scrIntMod=(ScrIntegradorMod)MainView.main.getMainPanel().getComponents()[0];
                    if( scrIntMod!=null ){
                        scrIntMod.saveAs( document  );
                    }
                    
                                        
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    
    
    

    public void viewQuickViewPanel() {
        if (getScrDocument() != null) {
            getScrDocument().setDocumentId(-1);
        }
        mainTabbed.setSelectedIndex(StatementConstant.SC_1.get());

    }

    public void setDocument(ScrDocument document) {
        try {
            if (document == null) {
                return;
            }
            scrDocument = document;
            mainTabbed.setSelectedIndex(StatementConstant.SC_0.get());
            getScrDocument().initNewDocument();
            mainPanel.removeAll();
            mainPanel.add(getScrDocument());
            mainPanel.revalidate();
            mainPanel.updateUI();
        } catch (SQLException ex) {
            Utilerias.showMessage(this, GlobalDefines.ERROR_CONEXION, JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).error(ex);
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

        mainPanel = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void setComponent(JPanel mainPanel) {
    }

    private void setStatusBar(JPanel statusPanel) {
        //javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        infoLabel = new JLabel();
        infoLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        //infoLabel.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        infoLabel.setName("infoLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                //.addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(statusAnimationLabel)
                                        .addContainerGap())))
        );
        statusPanelLayout.setVerticalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                        //.addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(statusMessageLabel)
                                .addComponent(statusAnimationLabel)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        statusPanel.setBorder(BorderFactory.createEmptyBorder());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private JTabbedPane_withoutPaintedTabs mainTabbed = new JTabbedPane_withoutPaintedTabs();

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private JLabel infoLabel;

    /**
     * @return the progressBar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @param progressBar the progressBar to set
     */
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * @return the mainPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * @return the scrDocument
     */
    public ScrDocument getScrDocument() {
        return scrDocument;
    }

    private class CustomKeyEventDispatcher implements KeyEventDispatcher {

        public CustomKeyEventDispatcher() {
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {

                if (e.isControlDown() && e.getKeyCode() != KeyEvent.VK_CONTROL /*&& e.getKeyCode() == KeyEvent.VK_V*/) {
                    int keyCode = e.getKeyCode();
                    WindowActions.Action action = WindowActions.getInstance().findAction(keyCode);
                    if (action != null) {
                        String actionName = action.name;
                        ribbonMenu.CustomClickAction(actionName);
                    }
                    e.consume();
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {

            } else if (e.getID() == KeyEvent.KEY_TYPED) {

            }

            return false;
        }

    }
}
