package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DsAutor;
import com.adinfi.formateador.bos.DsCanal;
import com.adinfi.formateador.bos.DsInputDoc;
import com.adinfi.formateador.bos.DsInputListaDist;
import com.adinfi.formateador.bos.FilePublishAttach;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.InputDoc;
import com.adinfi.formateador.bos.InputListaDist;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.PublishStatusBO;
import com.adinfi.formateador.bos.RelatedDocsBO;
import com.adinfi.formateador.bos.RelationalFilesBO;
import com.adinfi.formateador.bos.SendPublishAuthorsBO;
import com.adinfi.formateador.bos.SendPublishBO;
import com.adinfi.formateador.bos.SendPublishDistributionListBO;
import com.adinfi.formateador.bos.SendPublishFilesBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.TwitterBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.PublishStatusDAO;
import com.adinfi.formateador.dao.RelatedDocsDAO;
import com.adinfi.formateador.dao.SendPublishAuthorsDAO;
import com.adinfi.formateador.dao.SendPublishDAO;
import com.adinfi.formateador.dao.SendPublishDistributionListDAO;
import com.adinfi.formateador.dao.SendPublishFilesDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.dao.TwitterDAO;
import com.adinfi.formateador.editor.HTMLDocument;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.main.RibbonMenu;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.CustomAction;
import com.adinfi.formateador.view.ScrIntegradorDoc;
import com.adinfi.formateador.view.ScrIntegradorMod;
import com.adinfi.formateador.view.administration.JCheckBoxTree;
import com.adinfi.formateador.view.administration.JComboCheckBox;
import com.adinfi.formateador.view.resources.CCheckBox;
import com.adinfi.ws.analisisws.publicador.CanalDistribucion;
import com.adinfi.ws.publicador.DBResult;
import com.google.common.collect.ListMultimap;
import com.hexidec.ekit.EkitCore;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialClob;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Josue Sanchez
 */
public class SendPublish extends javax.swing.JDialog {
    
    private String pathFile = "";
    private String name2 = "";
    private EkitCore ekitCore1;
    private final DateFormat TWELVE_TF = new SimpleDateFormat("hh:mma");
    private final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");
    private final JFileChooser chooser = new JFileChooser();
    private boolean ok = true;
    private StringBuilder reqFields = new StringBuilder();
    private String url = "";
    private String documentTypeID = "";
    private int LIMIT_CHARS = 140;
    private JCheckBoxTree jCheckBoxTree = new JCheckBoxTree();
    private Map<String, String> valTree = new HashMap<>();
    private Map<String, String> nodos = new HashMap<>();
    private Map<String, String> hijos = new HashMap<>();
    DocumentBO docBO = null;
    public boolean showDialog = true;
    private TextAutoCompleter textAutoCompleter;
    private SubjectBO selectedValueSubject;
    private String twitterInmediatamenteMedios = null;
    private String tweetXMLAttach = "";
    private String fecha_guardado_publicado;
    private final int ATTACH_SIZE = 23;
    private boolean selectedFile = false;
    private boolean validadoTw = true;
    
    ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.TWITTER_HASHTAG_VALUE);
    
    UtilDateModel publicationDate = new UtilDateModel();
    
    List<String> names = new ArrayList();
    List<String> paths = new ArrayList();
    
    private Map<String, Blob> filesAttch = new HashMap<>();
    private Map<String, String> filesAttachName = new HashMap<>();
    private Map<String, String> filesAttachPath = new HashMap<>();
    
    private Map<String, Blob> twitterAttach = new HashMap<>();
    private Map<String, Clob> twitterAttachEncoded = new HashMap<>();
    
    private Hashtable<String, com.adinfi.ws.analisisws.publicador.CanalDistribucion> table;
    JCheckBoxTree cbt = new JCheckBoxTree();
    
    private String respTextoClob;
    private File fileTwitter;
    
    private List<String> codeNodeDefault;

    public SendPublish(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        removeAttchTW.setVisible(false);
        loadDatePicker();
        addTextEditor();
        init();
        loadUsers();
        List<String> hashtag = list.get("twitter.hashtag.value");
        theHashtag.setText(hashtag.get(0));
        
        countChar();
        publishProgressBar.setVisible(false);
        textAutoCompleter = new TextAutoCompleter(txtTema);
        initAutoTema();
        cboSubject.setVisible(false);
        /* Hashtag Editable para Administradores  */
        allowHashtagModify();
        
        try {
            table = new Hashtable<>();
            setProgramerInputs();
            loadPublishTree();
            jCheckBoxTree.updateUI();
            /*Filtro de archivos*/
            loadFileFilter();
            
            for (int i = 0; i < jCheckBoxTree.getRowCount(); i++) {
                jCheckBoxTree.expandRow(i);
            }
            
            inputTwitter.setLineWrap(true);
            inputTwitter.setWrapStyleWord(true);
            
            inputTwitter.getDocument().addDocumentListener(new DocumentListener() {
                
                @Override
                public void insertUpdate(DocumentEvent e) {
                    countChar();
                }
                
                @Override
                public void removeUpdate(DocumentEvent e) {
                    countChar();
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) {
                    countChar();
                }
            });
            
            inputTwitter.setLineWrap(true);
            inputTwitter.setWrapStyleWord(true);
            
            theHashtag.getDocument().addDocumentListener(new DocumentListener() {
                
                @Override
                public void insertUpdate(DocumentEvent e) {
                    countChar();
                }
                
                @Override
                public void removeUpdate(DocumentEvent e) {
                    countChar();
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) {
                    countChar();
                }
            });
            
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }

        // cargar el arbol de alcance de publicación
        checkInicialTree();
    }
    
    @SuppressWarnings("unchecked")

    /**
     * ********************************************************************
     */
    /* Lógica de acceso a esta ventana */
    private void init() {

        /*  Documento activo  */
        docBO = MainView.main.getScrDocument().getDocBO();
        
        DocumentBO docBOCollab = null;
        int nComponentes = MainView.main.getMainPanel().getComponents().length;
        
        if (nComponentes > 0) {
            if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorDoc) {
                docBOCollab = ((ScrIntegradorDoc) MainView.main.getMainPanel().getComponent(0)).getDocument();
            }
            
            if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorMod) {
                try {
                    
                    docBOCollab = ((ScrIntegradorMod) MainView.main.getMainPanel().getComponent(0)).getDocument();
                    DocumentDAO docDAO = new DocumentDAO();
                    docBOCollab = docDAO.getDocument(docBOCollab.getDocumentId(), -1, false);
                    
                } catch (SQLException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            }
        }

        // si es un documento colaborativo cargamos el docBO con el objeto.
        if (docBOCollab != null) {
            if (docBOCollab.isCollaborative()) {
                docBO = docBOCollab;
            }
        }

        // revisar si el collab
        MainView.main.getProgressBar().setVisible(false);
        
        if (docBO == null || docBO.getDocumentId() == 0) {

            // si no hay documento activo para enviar publicacion: error y cerrar
            JOptionPane.showMessageDialog(null, "No hay documento activo para enviar", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info("The software does not have documents to show");
            showDialog = false;
            
        } else if (docBO.getDocumentId() > 0) {

            /* revisamos si el tipo de documento de este documetno tiene habilitado la opción de permitir
             Envio de correo de lo contrario no es posible publicar para este tipo de documento */
            DocumentTypeDAO dType = new DocumentTypeDAO();
            List<DocumentTypeBO> dTypeBO = dType.get(docBO.getIdDocType());
            
            if (dTypeBO.get(0).isSendEmail() == false) {
                //Solo se bloquea el boton de envio
                btnSendPublish.setEnabled(false);
                
                /*JOptionPane.showMessageDialog(null, "No es posible publicar este Tipo de Documento", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info("No es posible publicar este Tipo de Documento: permisos");
                //docBO = null;
                //this.dispose();
                showDialog = false;
                return;*/
            }

            // titulo del documento por default
            inputTitulo.setText(docBO.getDocumentName());

            /*Si tiene enviar a medios el tipo de documento se precarga el texto en la publicacion */
            if (dTypeBO.get(0).isPublish() == true) {
                setTextinPublishFromDocument();
            }

            /*
             Si tiene habilidato el tipo de documento: Enviar inmediatamente a medios
             se agrega el tema y el titulo en el cuerpo del twitter.
             */
            if (dTypeBO.get(0).isSendMedia() == true) {
                SubjectDAO daoSj = new SubjectDAO();
                List<SubjectBO> lstS = daoSj.get(null);
                String subjecName = "";
                
                for (SubjectBO subjectBO : lstS) {
                    if (subjectBO.getIdSubject() == docBO.getIdSubject()) {
                        subjecName = subjectBO.getName();
                        break;
                    }
                }
                
                twitterInmediatamenteMedios = subjecName + ":" + docBO.getDocumentName();
                
            }

            // si hay un documento y existe un idDocumento: abrir y llenar campos 
            fillDocumentInfo(docBO.getDocumentId());

            /*comportamiento de los botones guardar y enviar*/
            int idStatus = checkPublishStatus();
            
        } else {

            // si no se guardo correctamente: error y cerrar
            JOptionPane.showMessageDialog(null, "No hay documento activo para enviar", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info("The software does not have documents to show");
            this.dispose();
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
    
    private void fillOtherValuesBySubject() {
        //Object obj = cboSubject.getSelectedItem();
        SubjectBO bo = selectedValueSubject;//(SubjectBO) obj;
        int id = bo.getIndustry();
        if (id > 0) {
            
            for (int i = 0; i < cboIndustry.getItemCount(); i++) {
                Object object = cboIndustry.getItemAt(i);
                IndustryBO bo_ = (IndustryBO) object;
                int idIndustry = bo_.getIdIndustry();
                if (idIndustry == id) {
                    cboIndustry.setSelectedIndex(i);
                }
            }
            
        } else {
            cboIndustry.setSelectedIndex(-1);
        }
    }

    /* Invocación de metodos para llenar info en pantalla */
    private void fillDocumentInfo(int idDocument) {
        
        DocumentDAO dao = new DocumentDAO();
        DocumentBO docBO2 = docBO;
        if (docBO2 != null) {
            /*Mostrar los campos del documento activo en la pantalla de enviar publicación*/
            /*Inicializar combos*/
            initComboBoxMarket(docBO2.getIdMarket());
            initComboBoxDocumentType(docBO2.getIdDocType());
            initComboBoxLanguage(docBO2.getIdLanguage());
            initComboBoxStatus(docBO2.getDocumentId());
            initComboBoxSubject();
            initComboBoxIndustry();

            /* Seleccionar tema  */
            SubjectDAO daoSubject = new SubjectDAO();
            List<SubjectBO> listSubject = daoSubject.get(null);
            int n = listSubject.size();
            for (int i = 0; i < n; i++) {
                SubjectBO obj = (SubjectBO) listSubject.get(i);
                if (obj.getIdSubject() == docBO2.getIdSubject()) {
                    //cboSubject.setSelectedIndex(i);
                    selectedValueSubject = obj;
                    txtTema.setText(obj.getName());
                    fillOtherValuesBySubject();
                    
                    break;
                }
            }
            
        }
        
        if (checkIfHasPublish(idDocument)) {
            
            groupEnviar.setEnabled(false);
            groupProgramar.setEnabled(false);
            inputTime.setEnabled(false);
            datePublishPicker.setVisible(false);
            txtFechaPublica.setLocation(datePublishPicker.getLocation());
            jLabel2.setVisible(false);
            inputTime.setVisible(false);
            jLabel1.setText("Fecha de Publicación");

            /* Existe una publicacion para este documento  */
            String valor;
            SendPublishDAO daoB = new SendPublishDAO();
            List<SendPublishBO> list = daoB.get(idDocument);
            
            for (SendPublishBO b : list) {
                
                txtFechaPublica.setText(b.getDate_publish() == null ? b.getDate() : b.getDate_publish());

                /* Seleccionar tema en el combo */
                SubjectDAO daoSubject = new SubjectDAO();
                List<SubjectBO> listSubject = daoSubject.get(null);
                int n = listSubject.size();
                for (int i = 0; i < n; i++) {
                    SubjectBO obj = (SubjectBO) listSubject.get(i);
                    if (obj.getIdSubject() == b.getIdSubject()) {
                        //cboSubject.setSelectedIndex(i);
                        selectedValueSubject = obj;
                        txtTema.setText(obj.getName());
                        fillOtherValuesBySubject();
                        
                        break;
                    }
                }

                /* Seleccionar status */
                int n1 = cboStatus.getItemCount();
                for (int i1 = 0; i1 < n1; i1++) {
                    PublishStatusBO obj1 = (PublishStatusBO) cboStatus.getItemAt(i1);
                    if (obj1.getIdpublish_status() == b.getIdStatus_publish()) {
                        cboStatus.setSelectedIndex(i1);
                        break;
                    }
                }

                /* Si esta programado el envio mostrar fecha y hora (modificar formatos)  */
                if (b.isScheduled()) {
                    
                    groupProgramar.setSelected(b.isScheduled());
                    String d = b.getDate();
                    Date date = null;
                    
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
                    String dateInString = d;
                    
                    try {
                        date = formatter.parse(dateInString);
                        fecha_guardado_publicado = formatter2.format(date);
                    } catch (ParseException ex) {
                        Utilerias.logger(getClass()).info(ex);
                    }
                    
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    
                    int year = c.get(c.YEAR);
                    int month = c.get(c.MONTH);
                    int day = c.get(c.DAY_OF_MONTH);
                    
                    publicationDate.setDate(year, month, day);
                    publicationDate.setSelected(true);
                    
                    try {
                        inputTime.setText(convertTo12HoursFormat(b.getTime()));
                    } catch (ParseException ex) {
                        Utilerias.logger(getClass()).info(ex);
                    }
                    
                } else {
                    String d = b.getDate_publish();
                    if (b.getDate_publish() != null) {
                        
                        fecha_guardado_publicado = b.getDate_publish();
                        
                        Date date = null;
                        
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                        String dateInString = d;
                        
                        try {
                            date = formatter.parse(dateInString);
                        } catch (ParseException ex) {
                            Utilerias.logger(getClass()).info(ex);
                        }
                        
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        
                        int year = c.get(c.YEAR);
                        int month = c.get(c.MONTH);
                        int day = c.get(c.DAY_OF_MONTH);
                        
                        publicationDate.setDate(year, month, day);
                        publicationDate.setSelected(false);
                    }
                }

                /* precargar titulo. */
                inputTitulo.setText(b.getTitle());

                /* precargar cuerpo. Insertar texto en el editor  */
                String text = ekitCore1.getTextPane().getText();
                String textPlain = Utilerias.html2text(text);
                if (b.getText() != null){// && textPlain.trim().isEmpty()) {
                    try {
                        valor = CLOBToString(b.getText());
                        valor = valor.replace("#8A0808", "#000000");
                        if (valor.length() > getMaxCharMail()) {
                            ekitCore1.getTextPane().setText(valor.substring(0, getMaxCharMail()));
                        } else {
                            ekitCore1.getTextPane().setText(valor);
                        }
                        ekitCore1.getTextPane().setForeground(Color.BLACK);
                    } catch (SQLException ex) {
                        Utilerias.logger(getClass()).info(ex);
                    } catch (IOException ex) {
                        Utilerias.logger(getClass()).info(ex);
                    }
                }

                /*Si tiene documentos relacionados preecargarlos*/
                RelatedDocsDAO daoRelated = new RelatedDocsDAO();
                List<RelatedDocsBO> listRelated = daoRelated.get(idDocument);
                
                if (listRelated != null && listRelated.size() > 0) {
                    for (int i = 0; i < listRelated.size(); i++) {
                        RelationalFilesBO bo = new RelationalFilesBO();
                        bo.setIdDocSend(listRelated.get(i).getRelated_document_id());
                        bo.setDocument_name(listRelated.get(i).getDocument_name());
                        cboRelatedDocs.addItem(bo);
                    }
                }
            }
            
        } else {
            txtFechaPublica.setVisible(false);
        }
        /*Retrive Tweet if exist */
        String tw = getTweet(idDocument);
        if (tw == null) {
            tw = twitterInmediatamenteMedios;
        }
        // revisar si tiene el hashtag no mostrarlo en la caja de texto del twitt
        if (tw != null && tw.toUpperCase().contains(theHashtag.getText().trim().toUpperCase())) {
            tw = tw.replace(theHashtag.getText(), "").trim();
            inputTwitter.setText(tw);
        } else {
            inputTwitter.setText(tw);
        }

        // cargar usuarios en multicombo.
        loadUsers();
    }
    
    private int numMaxCharMail;
    
    private int getMaxCharMail() {
        if (numMaxCharMail > 0) {
            return numMaxCharMail;
        }
        
        try {
            numMaxCharMail = 2000;
            List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_MAX_CHAR_MAIL) != null
                    ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_MAX_CHAR_MAIL).get(Utilerias.ALLOWED_KEY.PUBLISH_MAX_CHAR_MAIL.toString())
                    : null;
            if (list != null && list.isEmpty() == false) {
                numMaxCharMail = Integer.parseInt(list.get(StatementConstant.SC_0.get()));
            }
        } catch (Exception e) {
            numMaxCharMail = 2000;
        }
        
        return numMaxCharMail;
    }

    /**
     * ********************************************************************
     *
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cboMarket = new javax.swing.JComboBox();
        cboIndustry = new javax.swing.JComboBox();
        cboDocumentType = new javax.swing.JComboBox();
        cboStatus = new javax.swing.JComboBox();
        cboLanguage = new javax.swing.JComboBox();
        jButton10 = new javax.swing.JButton();
        txtTema = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        inputTitulo = new javax.swing.JTextField();
        textEditorContainer = new javax.swing.JPanel();
        FilesContainer = new javax.swing.JScrollPane();
        container = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        inputFilesarray = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        openFile = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        treeContainer = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputTwitter = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        counterTwitteer = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        removeAttchTW = new javax.swing.JButton();
        imageInLabel = new javax.swing.JLabel();
        theHashtag = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cboRelatedDocs = new javax.swing.JComboBox();
        cboAutores = new JComboCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnPDF = new javax.swing.JButton();
        btnSendPublish = new javax.swing.JButton();
        btnSaveSend = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnHTML = new javax.swing.JButton();
        publishProgressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        groupEnviar = new javax.swing.JRadioButton();
        groupProgramar = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        inputTime = new javax.swing.JFormattedTextField();
        datePublishPicker = new javax.swing.JPanel();
        cboSubject = new javax.swing.JComboBox();
        txtFechaPublica = new javax.swing.JTextField();

        jLabel12.setText("jLabel9");
        jLabel12.setName("jLabel12"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enviar Publicación");

        jPanel1.setName("jPanel1"); // NOI18N

        cboMarket.setEnabled(false);
        cboMarket.setName("cboMarket"); // NOI18N

        cboIndustry.setEnabled(false);
        cboIndustry.setName("cboIndustry"); // NOI18N

        cboDocumentType.setEnabled(false);
        cboDocumentType.setName("cboDocumentType"); // NOI18N

        cboStatus.setEnabled(false);
        cboStatus.setName("cboStatus"); // NOI18N

        cboLanguage.setEnabled(false);
        cboLanguage.setName("cboLanguage"); // NOI18N

        jButton10.setText("+");
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        txtTema.setName("txtTema"); // NOI18N
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

        jLabel5.setText("Mercado:");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText("Tipo de Documento:");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel9.setText("Lenguaje:");
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText("Tema:");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText("Sector:");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel13.setText("Estado:");
        jLabel13.setName("jLabel13"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cboMarket, 0, 222, Short.MAX_VALUE)
                                .addComponent(txtTema))
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(cboDocumentType, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboIndustry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboStatus, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboLanguage, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMarket)
                    .addComponent(cboDocumentType)
                    .addComponent(cboLanguage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboIndustry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10))))
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

        FilesContainer.setBorder(null);
        FilesContainer.setName("FilesContainer"); // NOI18N

        container.setName("container"); // NOI18N
        container.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));
        FilesContainer.setViewportView(container);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textEditorContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(FilesContainer)
            .addComponent(inputTitulo)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(inputTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textEditorContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilesContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel4.setName("jPanel4"); // NOI18N

        inputFilesarray.setName("inputFilesarray"); // NOI18N

        jLabel3.setText("Archivos adjuntos");
        jLabel3.setName("jLabel3"); // NOI18N

        openFile.setText("Adjunto");
        openFile.setName("openFile"); // NOI18N
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputFilesarray)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openFile, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(inputFilesarray, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openFile))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Alcance de publicación"));
        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());

        treeContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        treeContainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        treeContainer.setName("treeContainer"); // NOI18N
        jPanel6.add(treeContainer, java.awt.BorderLayout.CENTER);

        jPanel7.setName("jPanel7"); // NOI18N

        jLabel4.setText("Twitter");
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        inputTwitter.setColumns(20);
        inputTwitter.setRows(5);
        inputTwitter.setName("inputTwitter"); // NOI18N
        inputTwitter.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                inputTwitterInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(inputTwitter);

        jPanel8.setName("jPanel8"); // NOI18N

        jButton7.setText("Img. Twitter");
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        counterTwitteer.setBackground(new java.awt.Color(204, 204, 204));
        counterTwitteer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        counterTwitteer.setText("140");
        counterTwitteer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        counterTwitteer.setName("counterTwitteer"); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N

        removeAttchTW.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        removeAttchTW.setForeground(new java.awt.Color(255, 0, 51));
        removeAttchTW.setText("X");
        removeAttchTW.setName("removeAttchTW"); // NOI18N
        removeAttchTW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAttchTWActionPerformed(evt);
            }
        });

        imageInLabel.setName("imageInLabel"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(imageInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeAttchTW, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeAttchTW))
                .addGap(22, 22, 22))
        );

        theHashtag.setText("#Vector");
        theHashtag.setEnabled(false);
        theHashtag.setName("theHashtag"); // NOI18N
        theHashtag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                theHashtagActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(counterTwitteer, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                    .addComponent(theHashtag))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(counterTwitteer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(theHashtag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );

        jPanel9.setName("jPanel9"); // NOI18N

        jLabel8.setText("Autor(es)");
        jLabel8.setName("jLabel8"); // NOI18N

        cboRelatedDocs.setName("cboRelatedDocs"); // NOI18N
        cboRelatedDocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRelatedDocsActionPerformed(evt);
            }
        });

        cboAutores.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        cboAutores.setName("cboAutores"); // NOI18N
        cboAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAutoresActionPerformed(evt);
            }
        });

        jLabel7.setText("Archivos relacionados");
        jLabel7.setName("jLabel7"); // NOI18N

        jButton8.setText("+");
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton11.setText("-");
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jPanel5.setName("jPanel5"); // NOI18N

        btnPDF.setText("PDF");
        btnPDF.setName("btnPDF"); // NOI18N
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });

        btnSendPublish.setText("Enviar");
        btnSendPublish.setName("btnSendPublish"); // NOI18N
        btnSendPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendPublishActionPerformed(evt);
            }
        });

        btnSaveSend.setText("Guardar");
        btnSaveSend.setName("btnSaveSend"); // NOI18N
        btnSaveSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSendActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnHTML.setText("HTML");
        btnHTML.setName("btnHTML"); // NOI18N
        btnHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHTMLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveSend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSendPublish, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHTML, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveSend)
                    .addComponent(btnSendPublish)
                    .addComponent(btnPDF)
                    .addComponent(btnHTML)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel7))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(cboRelatedDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(cboAutores, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboRelatedDocs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        publishProgressBar.setIndeterminate(true);
        publishProgressBar.setName("publishProgressBar"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(publishProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(publishProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setText("Hora");
        jLabel2.setName("jLabel2"); // NOI18N

        buttonGroup1.add(groupEnviar);
        groupEnviar.setSelected(true);
        groupEnviar.setText("Enviar Inmediatamente");
        groupEnviar.setName("groupEnviar"); // NOI18N
        groupEnviar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                groupEnviarItemStateChanged(evt);
            }
        });
        groupEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupEnviarActionPerformed(evt);
            }
        });

        buttonGroup1.add(groupProgramar);
        groupProgramar.setText("Programar Envío");
        groupProgramar.setName("groupProgramar"); // NOI18N
        groupProgramar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                groupProgramarItemStateChanged(evt);
            }
        });
        groupProgramar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupProgramarActionPerformed(evt);
            }
        });

        jLabel1.setText("Fecha");
        jLabel1.setName("jLabel1"); // NOI18N

        inputTime.setEnabled(false);
        inputTime.setName("inputTime"); // NOI18N

        datePublishPicker.setEnabled(false);
        datePublishPicker.setName("datePublishPicker"); // NOI18N
        datePublishPicker.setPreferredSize(new java.awt.Dimension(0, 0));
        datePublishPicker.setLayout(new java.awt.BorderLayout());

        cboSubject.setName("cboSubject"); // NOI18N
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

        txtFechaPublica.setEnabled(false);
        txtFechaPublica.setName("txtFechaPublica"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(groupEnviar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupProgramar)
                .addGap(70, 70, 70)
                .addComponent(cboSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFechaPublica, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(datePublishPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTime, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(groupEnviar)
                        .addComponent(groupProgramar)
                        .addComponent(jLabel2)
                        .addComponent(inputTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(cboSubject)
                        .addComponent(txtFechaPublica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datePublishPicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /* Revisa el status de la publicación */
    private int checkPublishStatus() {
        
        int idPublishStatus = 0;
        //DocumentBO boDoc = MainView.main.getScrDocument().getDocBO();
        DocumentBO boDoc = docBO;
        
        if (boDoc.getDocumentId() > 0) {
            PublishStatusDAO dao = new PublishStatusDAO();
            List<PublishStatusBO> bo = dao.getStatusPublish(boDoc.getDocumentId());
            if (bo.size() > 0) {
                idPublishStatus = bo.get(0).getIdstatus_publish();
            }
        }
        return idPublishStatus;
    }

    /* Se inician los valores por default de la fecha y la hora */
    private void setProgramerInputs() {
        try {
            DateFormat df = new SimpleDateFormat("hh:mm/a");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            loadDatePicker();
            inputTime.setValue(reportDate);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }
    
    private boolean countChar() {

        // limite menos el hashtag menos un espacio.
        boolean validado = true;
        
        String currentHashtag = theHashtag.getText();
        int limit = (LIMIT_CHARS - currentHashtag.length()) - 1;
        int currentTextLenght = inputTwitter.getText().length();
        
        if (chooser.getSelectedFile() == null) {
            
            int n = limit - currentTextLenght;
            
            if (n < 0) {
                counterTwitteer.setForeground(Color.RED);
                validado = false;
            } else {
                counterTwitteer.setForeground(Color.BLACK);
                validado = true;
            }
            
            counterTwitteer.setText(String.valueOf(n));
            
        } else {
            int n = (limit - currentTextLenght) - ATTACH_SIZE;
            
            if (n < 0) {
                counterTwitteer.setForeground(Color.RED);
                validado = false;
            } else {
                counterTwitteer.setForeground(Color.BLACK);
                validado = true;
            }
            
            counterTwitteer.setText(String.valueOf(n));
            
        }
        
        int totalCaracteres = Integer.valueOf(counterTwitteer.getText());
        
        if (totalCaracteres < 0) {
            counterTwitteer.setForeground(Color.RED);
            validado = false;
            
        } else {
            counterTwitteer.setForeground(Color.BLACK);
            validado = true;
        }
        
        return validado;
        
    }

    /* Carga el arbol de alcance de publicación  */
    private void loadPublishTree() {
        
        try {
            com.adinfi.ws.analisisws.publicador.IPublicador_Stub stub = (com.adinfi.ws.analisisws.publicador.IPublicador_Stub) new com.adinfi.ws.analisisws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
            UtileriasWS.setEndpoint(stub);
            com.adinfi.ws.analisisws.publicador.ArrayOfCanalDistribucion array = stub.listaDistribucion(GlobalDefines.WS_INSTANCE);
            com.adinfi.ws.analisisws.publicador.CanalDistribucion[] canal = array.getCanalDistribucion();
            
            int x1 = 0;
            List<com.adinfi.ws.analisisws.publicador.CanalDistribucion> list = new ArrayList<>();
            
            codeNodeDefault = new ArrayList<>();

            for (com.adinfi.ws.analisisws.publicador.CanalDistribucion o2 : canal) {
                com.adinfi.ws.analisisws.publicador.CanalDistribucion o1 = new CanalDistribucion();
                o1.setCanalId(o2.getCanalId());
                
                o1.setChildren(o2.getChildren());
                
                if (o2.getDescripcion().equals("Pruebas Formateador")) {
                    o1.setDescripcion("Pruebas");
                }else{
                    o1.setDescripcion(o2.getDescripcion());
                }
                
                o1.setIsDefault(o2.getIsDefault());
                o1.setIsRequerido(o2.getIsRequerido());
                list.add(o1);
                
                if(o2.getChildren() != null && o2.getChildren().getCanalDistribucion() != null){
                    for(com.adinfi.ws.analisisws.publicador.CanalDistribucion child : o2.getChildren().getCanalDistribucion()){
                        if(child.getIsDefault()){
                            String chanel = "";
                            if(child.getCanalId().intValue() < 10)
                                chanel = "0" + child.getCanalId();
                            else
                                chanel = child.getCanalId().toString();
                            codeNodeDefault.add(o2.getCanalId().toString() + chanel);
                        }
                    }
                }
            }
            
            Collections.reverse(list);
            
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Todos");
            DefaultTreeModel modelo = new DefaultTreeModel(root);
            
            int x = 0;
            for (com.adinfi.ws.analisisws.publicador.CanalDistribucion o : list) {
                
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(o.getDescripcion());
                com.adinfi.ws.analisisws.publicador.ArrayOfCanalDistribucion arrayOfCanalDistribucion_child = o.getChildren();

                // Guardar los valores en un mapa para recogerlos al guardar (Valores node root)
                //System.out.println(o.getDescripcion() + " -> " + o.getCanalId().toString());
                valTree.put(o.getDescripcion(), o.getCanalId().toString());
                nodos.put(o.getCanalId().toString(), o.getDescripcion());
                addNode(arrayOfCanalDistribucion_child, node);
                modelo.insertNodeInto(node, root, x);
            }
            jCheckBoxTree.setModel(modelo);
            treeContainer.setViewportView(jCheckBoxTree);
        } catch (Exception e) {
            btnSendPublish.setEnabled(false);
            JOptionPane.showMessageDialog(null, "El servicio de alcance de la publicación no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void addNode(com.adinfi.ws.analisisws.publicador.ArrayOfCanalDistribucion child, DefaultMutableTreeNode parent) {
        
        if (child.getCanalDistribucion() == null || child.getCanalDistribucion().length == 0) {
            return;
        }
        
        com.adinfi.ws.analisisws.publicador.CanalDistribucion[] canalDistribucions = child.getCanalDistribucion();
        for (int i = 0; i < canalDistribucions.length; i++) {
            String descripcion = canalDistribucions[i].getDescripcion();
            int canal = canalDistribucions[i].getCanalId();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(descripcion);
            parent.add(node);
            com.adinfi.ws.analisisws.publicador.ArrayOfCanalDistribucion arrayofCanalChild = canalDistribucions[i].getChildren();
            addNode(arrayofCanalChild, node);
            valTree.put(node.toString(), String.valueOf(canal));
            hijos.put(String.valueOf(canal), node.toString());
            
        }
    }
    
    private String getPublishTreeData() {

        StringBuilder listaDistribucion = new StringBuilder();
        String canal = "";
        String node = "";
        listaDistribucion.append("<ListaDistribucion>\n");

        try{
            TreePath[] aTree = jCheckBoxTree.getCheckedPaths();
            for (TreePath path : aTree) {
                if (!path.getLastPathComponent().toString().equals("Todos")) {
                    //System.out.println(path);
                    if (path.getParentPath() != null && !path.getParentPath().toString().equals("[Todos]")) {

                        node = valTree.get(path.getParentPath().getLastPathComponent().toString());
                        canal = valTree.get(path.getLastPathComponent().toString());

                        if (canal.toCharArray().length == 1) {
                            canal = "0" + canal;
                        }

                        listaDistribucion.append("<CanalId>");
                        listaDistribucion.append(node).append(canal);
                        listaDistribucion.append("</CanalId>\n");
                    }
                }
            }

            listaDistribucion.append("</ListaDistribucion>");
            //System.out.println(listaDistribucion);
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
        return listaDistribucion.toString();
    }
    
    private InputListaDist getPublishTreeDataJson() {
        InputListaDist inputListaDist = new InputListaDist();
        String canal = "";
        String node = "";
        
        try{
            List<DsCanal> tdsInputListaDist = new ArrayList<>();
            TreePath[] aTree = jCheckBoxTree.getCheckedPaths();
            for (TreePath path : aTree) {
                if (!path.getLastPathComponent().toString().equals("Todos")) {
                    //System.out.println(path);
                    if (path.getParentPath() != null && !path.getParentPath().toString().equals("[Todos]")) {

                        node = valTree.get(path.getParentPath().getLastPathComponent().toString());
                        canal = valTree.get(path.getLastPathComponent().toString());

                        if (canal.toCharArray().length == 1) {
                            canal = "0" + canal;
                        }
                        
                        DsCanal dsCanal = new DsCanal();
                        dsCanal.CanalId = Integer.valueOf(node.concat(canal));
                        tdsInputListaDist.add(dsCanal);
                    }
                }
            }
            DsInputListaDist dsInputListaDist = new DsInputListaDist();
            dsInputListaDist.tdsInputListaDist = tdsInputListaDist;
            inputListaDist.dsInputListaDist = dsInputListaDist;
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
        return inputListaDist;
    }
    
    public final void loadFileFilter() {
        ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.TWITTER_IMAGE_FORMAT);
        FileFilter fileFilter = Utilerias.getFileFilter("Archivo de imágenes", list);
        chooser.setFileFilter(fileFilter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
        //v.add(EkitCore.KEY_TOOL_PASTE);
        v.add(EkitCore.KEY_TOOL_CUT);
        v.add(EkitCore.KEY_TOOL_BOLD);
        v.add(EkitCore.KEY_TOOL_ITALIC);
        v.add(EkitCore.KEY_TOOL_UNDERLINE);
        v.add(EkitCore.KEY_TOOL_UNDO);
        v.add(EkitCore.KEY_TOOL_REDO);
        
        ekitCore1 = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
        
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
            Utilerias.logger(getClass()).info(e);
        }
        
    }

    /* Agregar archivos al contenedor */
    private void addFilesToContainer() {
        myFileChooser = new JFileChooser();
        
        chooser.setAcceptAllFileFilterUsed(true);
        int returnVal = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathFile = file.getAbsolutePath();
            String name = file.getName();
            
            names.add(name);
            paths.add(pathFile);
            
            Blob blob = null;
            
            String uid = Utilerias.getUniqueID();
            
            byte[] data;
            try {
                data = Files.readAllBytes(file.toPath());
                blob = new javax.sql.rowset.serial.SerialBlob(data);
                
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            
            filesAttch.put(uid, blob);
            filesAttachName.put(uid, name);
            filesAttachPath.put(uid, pathFile);
            
            if (!names.isEmpty()) {
                for (String name1 : names) {
                    name2 = name1;
                }
                
                inputFilesarray.setText(pathFile);
                ItemPanel panel = new ItemPanel(name2, uid, this, filesAttachPath.get(uid));
                container.add(panel);
                container.updateUI();
            }
        } else {
            pathFile = "";
        }
    }

    /* imagen adjunta del twitter */
    private void addTwitterAttach(boolean saved, String entryKey) {
        
        File file = null;
        int returnVal = 0;
        BufferedImage image = null;

        /*Si esta guardado un registro en la bd lo mostramos directamente*/
        if (saved) {
            removeAttchTW.setVisible(true);
            try {
                image = ImageIO.read(twitterAttach.get(entryKey).getBinaryStream());
                BufferedImage resizedImage = resize(image, 50, 50);
                ImageIcon ii = new ImageIcon(resizedImage);
                imageInLabel.setVisible(true);
                imageInLabel.setIcon(ii);
            } catch (SQLException | IOException e) {
                Utilerias.logger(getClass()).info(e);
            }
        } else {
            /*de otra forma lo tomamos del file chooser*/
            /* Convertimos a blob para manejar un estandar */
            try {
                chooser.setAcceptAllFileFilterUsed(false);
                returnVal = chooser.showOpenDialog(null);
                file = chooser.getSelectedFile();
                fileTwitter = file;
                byte[] data = Files.readAllBytes(file.toPath());
                String uid = Utilerias.getUniqueID();
                twitterAttach.clear();
                twitterAttach.put(uid, new javax.sql.rowset.serial.SerialBlob(data));
                image = ImageIO.read(twitterAttach.get(uid).getBinaryStream());
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    BufferedImage resizedImage = resize(image, 50, 50);
                    ImageIcon ii = new ImageIcon(resizedImage);
                    imageInLabel.setVisible(true);
                    imageInLabel.setIcon(ii);
                    removeAttchTW.setVisible(true);
                    countChar();
                }
                
            } catch (IOException | SQLException e) {
                Utilerias.logger(getClass()).info(e);
            }
        }
        
    }

    /* IMAGE DATA RESIZE */
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

    /* Remueve el elemento de archivo generado por add files to container */
    public void removeElementfromContainer(String path) {
        
        Component[] comp = container.getComponents();
        for (Component comp_ : comp) {
            if (comp_ instanceof ItemPanel) {
                if (((ItemPanel) comp_).getPath().equals(path)) {
                    
                    container.remove(comp_);
                    container.updateUI();
                    
                    filesAttachPath.remove(path);

                    // se actualiza la barra de paths visualmente.
                    if (filesAttachPath.isEmpty()) {
                        inputFilesarray.setText(null);
                    } else {
                        for (Map.Entry e : filesAttachPath.entrySet()) {
                            inputFilesarray.setText(e.getValue().toString());
                            break;
                        }
                    }
                    
                    filesAttachName.remove(path);
                    filesAttch.remove(path);
                    
                }
            }
        }
    }

    /*Revisa si la extensión es diferente a .exe si es asi devuelve un true
     de lo contrario un false */
    private boolean checkValidData(String f) {
        
        boolean b = true;
        
        String extension = checkExtension(f);
        
        if (extension.equals("exe")) {
            b = false;
        }
        
        return b;
    }

    /* Regresa la extensión del archivo*/
    private String checkExtension(String s) {
        String ext = null;
        int i = s.lastIndexOf('.');
        
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /* Metodo para verificar el peso de los archivos: devuelve true si el
     archivo es menor a 20MB y false si es mayor a 20MB*/
    private boolean checkFileSize(String path) {
        
        boolean b = true;
        double bytes = 0;
        double maxBytes = 20971520;
        
        File file = new File(path);
        
        if (file.exists()) {
            bytes = file.length();
            if (bytes > maxBytes) {
                b = false;
            }
        } else {
            b = false;
        }
        return b;
    }

    /* Revisa si existe una plublicacion ya creada para este documento si es asi devuelve true */
    private boolean checkIfHasPublish(int idDoc) {
        boolean b = false;
        SendPublishDAO dao = new SendPublishDAO();
        List<SendPublishBO> list = dao.get(idDoc);
        if (list != null) {
            b = list.size() > 0;
        }
        return b;
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
            if (Integer.parseInt(bo.getIdMiVector_real()) == id) {
                cboMarket.setSelectedItem(bo);
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
    private void initComboBoxStatus(int id) {
        
        PublishStatusDAO dao = new PublishStatusDAO();
        List<PublishStatusBO> list = dao.get();
        List<PublishStatusBO> idStatusSaved = dao.getStatusPublish(id);
        int idStatusSavedInt = -1;
        if (idStatusSaved != null && idStatusSaved.size() > 0) {
            idStatusSavedInt = idStatusSaved.get(0).getIdstatus_publish();
        }
        for (PublishStatusBO bo_ : list) {
            cboStatus.addItem(bo_);
        }
        
        int n = cboStatus.getItemCount();
        for (int i = 0; i < n; i++) {
            PublishStatusBO obj = (PublishStatusBO) cboStatus.getItemAt(i);
            if (obj.getIdpublish_status() == idStatusSavedInt) {
                cboStatus.setSelectedIndex(i);
                break;
            }
        }
        
    }

    /* Metodo para llenar el combo Tema */
    private void initComboBoxSubject() {
        cboSubject.removeAllItems();
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
    private void addSubjectComponent() {
        AddSubjectModal s = new AddSubjectModal(null, true);
        
        s.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                initComboBoxSubject();
                initAutoTema();
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                initComboBoxSubject();
                initAutoTema();
            }
        });
        
        s.setLocationRelativeTo(null);
        s.setVisible(true);
    }

    /* Guardar publicación  */
    private int saveSendPublish(SendPublishBO bo) {
        int id = 0;
        
        SendPublishDAO dao = new SendPublishDAO();
        id = dao.insertUpdate(bo);

        //Validar y guardar twitter:
        return id;
    }

    /*Carga los usuarios para agregarlos al combo autores */
    private void loadUsers() {
        cboAutores.removeAllItems();
        try {
            com.adinfi.ws.IAccess_Stub stub = (com.adinfi.ws.IAccess_Stub) new com.adinfi.ws.Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            com.adinfi.ws.ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);
            
            if (array != null && array.getUsuario() != null) {
                com.adinfi.ws.Usuario[] users = array.getUsuario();
                
                cboAutores.removeAllItems();
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    //com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    com.adinfi.formateador.bos.seguridad.Usuario u
                            = new com.adinfi.formateador.bos.seguridad.Usuario(
                                    false,
                                    o.getUsuarioId(),
                                    o.getUsuarioNT(),
                                    o.getNombre(),
                                    o.getCorreo(),
                                    o.getExtension(),
                                    o.getFechaAlta(),
                                    o.getUltimoAcceso(),
                                    o.getIsAutor(),
                                    o.getPerfilId(),
                                    o.getPerfil(),
                                    o.getMiVectorId(),
                                    o.getIsDirectorio()
                            );
                    
                    cboAutores.addItem(new CCheckBox(u.toString(), u));
                }

                //Obtenemos el id de la publicacion acutal
                //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
                List<SendPublishBO> listPub = null;
                if (docBO != null) {
                    SendPublishDAO daoSend = new SendPublishDAO();
                    listPub = daoSend.get(docBO.getDocumentId());
                }
                
                if (listPub.isEmpty()) {
                    //no hay publicación guardada
                    for (int i = 0; i < cboAutores.getItemCount(); i++) {
                        CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                        com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();
                        if (doc.getUsuarioNT().equals(InstanceContext.getInstance().getUsuario().getUsuarioNT())) {
                            cb.setSelected(true);
                        } else {
                            cb.setSelected(false);
                        }
                    }
                    
                } else {
                    int idPublish = listPub.get(0).getIdDocument_send();
                    SendPublishAuthorsDAO daoAuthors = new SendPublishAuthorsDAO();
                    List<SendPublishAuthorsBO> listAuthors = daoAuthors.get(idPublish);
                    
                    if (listAuthors.isEmpty()) {

                        //si no hay autores guardados previamente deseleccionamos todos
                        for (int i = 0; i < cboAutores.getItemCount(); i++) {
                            CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                            cb.setSelected(false);
                        }
                        
                    } else {
                    //Precargamos los autores posteriormente seleccionados

                        // creamos una lista auxiliar con los ids de los autores seleccionados guardados
                        List<Integer> auxList = new ArrayList<>();
                        for (SendPublishAuthorsBO listAuthor : listAuthors) {
                            auxList.add(listAuthor.getId_author());
                        }

                        // seleccionamos
                        for (int i = 0; i < cboAutores.getItemCount(); i++) {
                            CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                            com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();
                            boolean b = false;
                            if (auxList.contains(doc.getUsuarioId())) {
                                b = true;
                            }
                            cb.setSelected(b);
                        }
                    }
                    
                }
                
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }
    /* Validar no exceda los 140 caracteres de twitter */
    
    private boolean validateTwitter() {
        return countChar();
    }

    /* Revisar si existe una publicacion del documento actual para evitar duplicidad */
    private int checkForSavedDocSend() {
        int i = 0;

        //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        int id = docBO.getDocumentId();
        
        SendPublishDAO dao = new SendPublishDAO();
        List<SendPublishBO> bo = dao.get(id);
        if (bo.size() > 0) {
            i = bo.get(0).getIdDocument_send();
        }
        return i;
    }

    /* Revisar si existe una publicación del twitter para evitar duplicidad */
    private int checkForSavedTweetSend() {
        int i = 0;
        //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        int id = docBO.getDocumentId();
        
        TwitterDAO dao = new TwitterDAO();
        List<TwitterBO> bo = dao.get(id);
        if (bo != null && bo.size() > 0) {
            i = bo.get(0).getIdTwitter();
            if (bo.get(0).getEncodedTw() != null) {
                try {
                    tweetXMLAttach = CLOBToString(bo.get(0).getEncodedTw());
                } catch (IOException | SQLException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            }
            
        }
        
        return i;
    }

    /* Validar antes de guardar  */
    private void validateSendPublish(boolean send) {
        
        ok = true;
        reqFields.setLength(0);
        
        String date_publish = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //Date datePublish = sdf.parse(date_publish);
        boolean enviado = false;
        
        SendPublishBO boSend = new SendPublishBO();
        
        try {
            SendPublishDAO daoB = new SendPublishDAO();
            List<SendPublishBO> list = daoB.get(docBO.getDocumentId());
            if (list != null && list.size() > 0) {
                boSend = list.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, e);
        }

        /* si hay un status valido se guarda el valor del status en el objecto */
        boSend.setIdStatus_publish(getIDStatusPublish());

        /* se guarda el valor del idTema  */
        boolean validaTema = true;
        boolean validaTitulo = true;
        DocumentTypeDAO daoDoc = new DocumentTypeDAO();
        List<DocumentTypeBO> listDoc = daoDoc.get(null, -1, 0);
        for (DocumentTypeBO doct : listDoc) {
            if (doct.getIddocument_type() == docBO.getIdDocType()) {
                validaTema = doct.isSubject();
                validaTitulo = doct.isTitle();
                break;
            }
        }
        
        if (validaTema) {
            if (!txtTema.getText().isEmpty()) {
                if (selectedValueSubject == null) {
                    Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (selectedValueSubject != null && !txtTema.getText().trim().equals(selectedValueSubject.getName())) {
                    Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            } else if (txtTema.getText().trim().isEmpty() && selectedValueSubject != null) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El Tema no es Valido.", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boSend.setIdSubject(getIDSubject());
        } else {
            boSend.setIdSubject(0);
        }

        /* Seleccionamos el id del documento activo  */
        //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        boSend.setIdDocument(docBO.getDocumentId());

        /* Seleccionamos el titulo de la publicación */
        if (validaTitulo) {
            if ((inputTitulo.getText() != null) && (!inputTitulo.getText().trim().isEmpty()) && (!inputTitulo.getText().equals("Título"))) {
                boSend.setTitle(inputTitulo.getText());
            } else {
                /* sin titulo */
                ok = false;
                reqFields.append("\nTitulo Valido");
                
            }
        } else {
            boSend.setTitle(inputTitulo.getText());
        }
        
        if (isAlcanceSelected() == false) {
            /* No selecciono  */
            ok = false;
            reqFields.append("\nNo ha seleccionado el alcance de la publicación");
        }

        /* Seleccionamos el cuerpo de la publicación del editor de texto  */
        String text = ekitCore1.getTextPane().getText();
        String textPlain = Utilerias.html2text(text);
        if (!textPlain.trim().isEmpty()) {
            try {
                Clob cl = new SerialClob(text.toCharArray());
                boSend.setText(cl);
                boSend.setTextString(textPlain);
                respTextoClob = Utilerias.html2text(respTextoClob).toUpperCase();
                cl = new SerialClob(respTextoClob.toCharArray());
                boSend.setDocumentContent(cl);
            } catch (SQLException ex) {
                Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ok = false;
            reqFields.append("\nContenido de la publicación");
        }
        
        if (groupProgramar.isSelected()) {
            
            boSend.setScheduled(true);
            
            if (publicationDate.isSelected()) {
                
                try {
                    int statusAct = getIDStatusPublish();
                    //String d = convertDateFormat(publicationDate.getValue().toString(), "dd/MM/yyyy", "yyyy-MM-dd");

                    String day = String.valueOf(publicationDate.getDay());
                    String month = String.valueOf(publicationDate.getMonth() + 1);
                    String year = String.valueOf(publicationDate.getYear());
                    
                    String theDate = day + "/" + month + "/" + year;
                    String d = null;
                    if (statusAct == 2 || statusAct == 4) {
                        d = convertDateFormat(fecha_guardado_publicado, "yyyy/MM/dd", "yyyy-MM-dd");
                    } else {
                        d = convertDateFormat(theDate, "dd/MM/yyyy", "yyyy-MM-dd");
                    }
                    
                    String t = "";
                    
                    if (inputTime.getText() != null && !inputTime.getText().isEmpty() && !inputTime.getText().equals("  :  /  ")) {
                        try {
                            String vh = inputTime.getText().trim().split(":")[0];
                            int nh = Integer.parseInt(vh);
                            if (nh > 12) {
                                JOptionPane.showMessageDialog(this, "El formato de fecha debe ser de 12 horas.");
                                ok = false;
                                return;
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Ocurrio un error al validar la fecha verifique que este en fomrato de 12 horas.");
                            ok = false;
                            return;
                        }
                        
                        t = convertTo24HoursFormat(inputTime.getText().trim().replace("/", ""));
                    } else {
                        JOptionPane.showMessageDialog(this, "Ingrese una hora para programar la publicación");
                        ok = false;
                        return;
                    }
                    
                    boSend.setTime(t);
                    Date dateScheduled = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(d + " " + t);
                    date_publish = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(dateScheduled);
                    Date dateNow = new Date();
                    
                    if (statusAct != 2 && statusAct != 4) {
                        if (dateScheduled.after(dateNow)) {
                            boSend.setDate(d);
                        } else {
                            JOptionPane.showMessageDialog(this, "La fecha programada no es una fecha valida");
                            return;
                        }
                    } else {
                        boSend.setDate(d);
                    }
                    
                } catch (ParseException e) {
                    Utilerias.logger(getClass()).info(e);
                    JOptionPane.showMessageDialog(this, "Ingrese una hora valida para programar la publicación");
                    return;
                    
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha de publicación");
                return;
            }
            
        } else {
            
            boSend.setScheduled(false);
            // si ya se envio la fecha de publicacion ya no se actualiza, converva la guardada.
            if (getIDStatusPublish() == 2) {
                date_publish = fecha_guardado_publicado;
            }
            
        }

        // Set fecha de publicacion.
        boSend.setDate_publish(date_publish);
        if (inputTwitter.getText().length() > 0 || chooser.getSelectedFile() != null) {
            if (!validateTwitter()) {
                ok = false;
                JOptionPane.showMessageDialog(null, "El tweet excede los 140 caracteres", "Inane Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        /* Validaciones de archivos adjuntos no .exe y menores a 20MB */
        if (!filesAttachPath.isEmpty()) {
            for (Map.Entry e : filesAttachPath.entrySet()) {
                //System.out.println(e.getKey() + " " + e.getValue());

                if (!checkValidData((String) e.getValue())) {
                    ok = false;
                    //JOptionPane.showMessageDialog(null, "Extensión .exe no valida", "Inane error", JOptionPane.ERROR_MESSAGE);
                    reqFields.append("\nExtensión .exe no valida");
                    
                }
                
                if (!checkFileSize((String) e.getValue())) {
                    ok = false;
                    //JOptionPane.showMessageDialog(null, "El tamaño del archivo no debe exceder de 20MB", "Inane error", JOptionPane.ERROR_MESSAGE);
                    reqFields.append("\nEl tamaño del archivo no debe exceder de 20MB");
                    
                }
            }
        }
        
        if (!isSelectedAutor()) {
            ok = false;
            //JOptionPane.showMessageDialog(null, "El tamaño del archivo no debe exceder de 20MB", "Inane error", JOptionPane.ERROR_MESSAGE);
            reqFields.append("\nSeleccione Autor");
        }

        /*Si no hay errores > guardar*/
        int id = 0;
        if (ok) {
            /* Seteando el valor del status a guardado */
            int statusAct = getIDStatusPublish();
            if (statusAct != 2 && statusAct != 4) {
                boSend.setIdStatus_publish(StatementConstant.SC_3.get());
            }
            boSend.setIdDocument_send(checkForSavedDocSend());

            /* Guardamos */
            id = saveSendPublish(boSend);
            boSend.setIdDocument_send(id);

            //SI HAY DOCUMENTOS RELACIONADOS -> GUARDAMOS
            if (cboRelatedDocs.getItemCount() > 0) {
                
                RelatedDocsDAO dao = new RelatedDocsDAO();
                dao.delete(docBO.getDocumentId());
                
                for (int i = 0; i < cboRelatedDocs.getItemCount(); i++) {
                    Object obj = cboRelatedDocs.getItemAt(i);
                    RelationalFilesBO boS = (RelationalFilesBO) obj;
                    RelatedDocsBO bo = new RelatedDocsBO();
                    bo.setDocument_id(docBO.getDocumentId());
                    bo.setRelated_document_id(boS.getIdDocSend());
                    RelatedDocsDAO dao2 = new RelatedDocsDAO();
                    dao2.insertUpdate(bo);
                    
                }
                
            }

            //Guardamos authores
            boSend.setCorreoAutor(saveAuthors(id));

            //Guardamos Lista de distribucion
            saveDistributionList(id);
            
            if (!inputTwitter.getText().isEmpty()) {
                saveTweet(false);
            }

            /*Si guardo la informacion guardamos los archivos
             vinculandolo con el id que regreso*/
            if (id > 0) {
                if (!saveFiles(id)) {
                    Utilerias.logger(getClass()).info("No se guardo");
                    ok = false;
                } else {
                    //fillDocumentInfo(id);
                }
            }

            /*Si guardo correctamente y el usuario presiono enviar: Enviamos*/
            if (send && id > 0) {
                
                publishProgressBar.setVisible(true);
                
                btnSaveSend.setEnabled(false);
                btnSendPublish.setEnabled(false);
                btnPDF.setEnabled(false);
                btnHTML.setEnabled(false);
                btnCancel.setEnabled(false);
                
                int idStatusPublish_ = getIDStatusPublish();
                String inputTwitterTxt = inputTwitter.getText();
                String theHashtagTxt = theHashtag.getText();
                UtilDateModel publicationDate_ = publicationDate;
                int idPublish = id;
                String authors_ = getAuthors();
                String subjectPublishName = getSubjectPublishName();
                String docsRelacionados = "";
                
                String dateSend;
                if (boSend.isScheduled()) {
                    dateSend = boSend.getDate() + " " + boSend.getTime();
                } else {
                    //dateSend = boSend.getDate_publish();
                    /* sin fecha cuando es inmediato */
                    dateSend = "";
                }
                
                if (!filesAttachPath.isEmpty()) {
                    List<File> lstFiles = new ArrayList<>();
                    for (Map.Entry e : filesAttachPath.entrySet()) {
                        String rutaArchivo = (String) e.getValue();
                        lstFiles.add(new File(rutaArchivo));
                    }
                    String strDate = null;
                    try {
                        strDate = convertDateFormat(date_publish, "yyyy/MM/dd HH:mm", "dd/MM/yyyy");
                    } catch (Exception e) {
                        Utilerias.logger(getClass()).info(e);
                    }
                    
                    int numSec = 0;
                    if (filesAttch != null) {
                        numSec = filesAttch.size();
                    }
                    
                    List<FilePublishAttach> lstURLs = UtileriasSSH.getInstance().sendFilesAttachSsh(boSend, docBO, strDate, lstFiles, false, numSec);
                    docsRelacionados = Utilerias.encodeAttachedFiles(lstURLs);//(paths);
                }
                
                PublishWorker worker = new PublishWorker(docBO, boSend, idStatusPublish_, inputTwitterTxt, theHashtagTxt, publicationDate_, idPublish, authors_,
                        subjectPublishName, dateSend, tweetXMLAttach, docsRelacionados, publishProgressBar
                );
                
                worker.execute();

                //enviado = setPublish(boSend, id, docBO);
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Debe de completar la informacón requerida " + reqFields, "Inane error", JOptionPane.ERROR_MESSAGE);
        }

        //update(null);
        if (ok) {
            
            if (!send) {
                
                //if (docBO.getPublishName() != null) {
                    
                String nStrDate = null;
                try {
                    Date dd = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(boSend.getDate_publish() == null ? boSend.getDate() : boSend.getDate_publish());
                    nStrDate = new SimpleDateFormat("dd/MM/yyyy").format(dd);
                } catch (ParseException ex) {
                    Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
                }

                String dateSend;
                if (boSend.isScheduled()) {
                    dateSend = boSend.getDate() + " " + boSend.getTime();
                } else {
                    //dateSend = boSend.getDate_publish();
                    /* sin fecha cuando es inmediato */
                    dateSend = "";
                }

                String urlHTML = UtileriasSSH.getInstance().sendFilesSsh(boSend, docBO, nStrDate, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY, getAuthorsJson(), getPublishTreeDataJson(), dateSend.replace("-", "/"));

                if(id > 0 && urlHTML != null){

                    SendPublishDAO SendPublisDao = new SendPublishDAO();
                    SendPublisDao.UpdateURL(id, urlHTML);

                    try {
                        DocumentDAO dDAO = new DocumentDAO();
                        docBO.setPublishName(dDAO.getPublishDocumentName(docBO.getDocumentId()));
                    } catch (Exception e) {
                        Utilerias.logger(getClass()).error(e);
                    }

                }

                CollaborativesDAO collabDAO = new CollaborativesDAO();
                List<DocumentBO> lstDocCollab = collabDAO.getDocumentBoCollabByDocument(docBO.getDocumentId());

                SendPublishDAO daoPub = new SendPublishDAO();

                for (DocumentBO docCollab : lstDocCollab) {

                    if (docCollab.getPublishName() == null) {
                        continue;
                    }

                    List<SendPublishBO> lstDocSendPub = daoPub.get(docCollab.getDocumentId());
                    if (lstDocSendPub != null && lstDocSendPub.size() > 0) {
                        SendPublishBO docSPubBO = lstDocSendPub.get(0);
                        nStrDate = null;
                        try {
                            Date dd = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(docSPubBO.getDate_publish() == null ? docSPubBO.getDate() : docSPubBO.getDate_publish());
                            nStrDate = new SimpleDateFormat("dd/MM/yyyy").format(dd);
                        } catch (ParseException ex) {
                            Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        UtileriasSSH.getInstance().sendFilesSsh(docSPubBO, docCollab, nStrDate, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
                    }
                }
                    
                /*} else {
                    CollaborativesDAO collabDAO = new CollaborativesDAO();
                    List<DocumentBO> lstDocCollab = collabDAO.getDocumentBoCollabByDocument(docBO.getDocumentId());
                    
                    SendPublishDAO daoPub = new SendPublishDAO();
                    
                    for (DocumentBO docCollab : lstDocCollab) {
                        
                        if (docCollab.getPublishName() == null) {
                            continue;
                        }
                        
                        List<SendPublishBO> lstDocSendPub = daoPub.get(docCollab.getDocumentId());
                        if (lstDocSendPub != null && lstDocSendPub.size() > 0) {
                            SendPublishBO docSPubBO = lstDocSendPub.get(0);
                            String nStrDate = null;
                            try {
                                Date dd = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(docSPubBO.getDate_publish() == null ? docSPubBO.getDate() : docSPubBO.getDate_publish());
                                nStrDate = new SimpleDateFormat("dd/MM/yyyy").format(dd);
                            } catch (ParseException ex) {
                                Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            UtileriasSSH.getInstance().sendFilesSsh(docSPubBO, docCollab, nStrDate, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
                        }
                    }
                }*/
                
                JOptionPane.showMessageDialog(this, "Se ha guardado correctamente");
                cboStatus.setSelectedIndex(StatementConstant.SC_2.get());
                
            } else {
                try {
                    DocumentDAO dDAO = new DocumentDAO();
                    docBO.setPublishName(dDAO.getPublishDocumentName(docBO.getDocumentId()));
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }
            }
            
        } else {
            Utilerias.logger(getClass()).info("No se guardo.");
        }
        
    }
    
    private int getIDStatusPublish() {
        
        int idStatus = 0;

        /* Recoger info del combo status */
        Object objStatus = cboStatus.getSelectedItem();
        PublishStatusBO boStatus = (PublishStatusBO) objStatus;
        if (boStatus.getIdpublish_status() > 0) {
            /* si hay un status valido se guarda el valor del status en el objecto */
            idStatus = boStatus.getIdpublish_status();
        } else {
            ok = false;
            reqFields.append("Status de la publicación");
        }
        
        return idStatus;
    }
    
    private int getIDSubject() {
        
        int idSubject = 0;

        //Object objSubject = cboSubject.getSelectedItem();
        SubjectBO boSubject = selectedValueSubject;//(SubjectBO) objSubject;
        if (boSubject != null && boSubject.getIdSubject() > 0) {
            /* se guarda el valor del idTema  */
            idSubject = boSubject.getIdSubject();
        } else {
            /* No hay un id tema valido */
            ok = false;
            reqFields.append("\nTema requerido");
        }
        
        return idSubject;
    }
    
    /**
     * Regresa el arreglo de envio de autores
     * 
     * @return List donde valor 0 autores y valor 1 autoresJson
     */
    private String getAuthors() {
        
        String authors = "";
        try{
            if (cboAutores.getItemCount() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("<AUTORES>");      
                for (int i = 0; i < cboAutores.getItemCount(); i++) {
                    CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                    if (cb.isSelected()) {
                        com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();
                        sb.append("<AUTOR_ID>");
                        sb.append(doc.getUsuarioId());
                        sb.append("</AUTOR_ID>");
                    }
                }
                sb.append("</AUTORES>");
                authors = sb.toString();
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
        
        return authors;
    }
    
    private InputDoc getAuthorsJson() {
        InputDoc inputDoc = new InputDoc();
        try{
            if (cboAutores.getItemCount() > 0) {
                DsInputDoc dsInputDoc = new DsInputDoc();
                List<DsAutor> tdsInputDocAutores = new ArrayList<>();
                for (int i = 0; i < cboAutores.getItemCount(); i++) {
                    CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                    if (cb.isSelected()) {
                        com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();

                        DsAutor dsAutor = new DsAutor();
                        dsAutor.AutorId = doc.getUsuarioId();
                        dsAutor.Autor = doc.getNombre();
                        dsAutor.AutorEmail = doc.getCorreo();
                        tdsInputDocAutores.add(dsAutor);
                    }
                }
                dsInputDoc.tdsInputDocAutores = tdsInputDocAutores;
                inputDoc.dsInputDoc = dsInputDoc;
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).info(e);
        }
        
        return inputDoc;
    }

    /* Cambia el status de la publicación  */
    private void setStatus(int idDocSend, int idStatus) {
        
        SendPublishDAO dao = new SendPublishDAO();
        SendPublishBO bo = new SendPublishBO();
        
        bo.setIdDocument_send(idDocSend);
        bo.setIdStatus_publish(idStatus);
        
    }

    /* Guardar archivos adjuntos de la publicación */
    private boolean saveFiles(int idDocument_send) {
        
        boolean b = true;
        
        SendPublishFilesDAO dao = new SendPublishFilesDAO();
        SendPublishFilesBO bo = new SendPublishFilesBO();

        /* Limpiamos registros para mantener actualizada la tabla. */
        dao.clean(idDocument_send);
        
        if (filesAttch.size() > 0) {
            for (Map.Entry e : filesAttch.entrySet()) {
                //System.out.println(e.getKey() + " " + e.getValue());
                Blob blobFile = (Blob) e.getValue();
                
                bo.setIddocument_send(idDocument_send);
                bo.setFile(blobFile);
                bo.setFilename(filesAttachName.get(e.getKey().toString()));
                bo.setUid(e.getKey().toString());
                
                dao.insertUpdate(bo);
                
            }
        }
        
        return b;
    }
    
    private String saveAuthors(int id) {
        String correoAut = "";
        SendPublishAuthorsDAO dao = new SendPublishAuthorsDAO();
        SendPublishAuthorsBO bo = new SendPublishAuthorsBO();
        bo.setId_send_publish(id);
        dao.delete(bo);
        
        if (cboAutores.getItemCount() > 0) {
            for (int i = 0; i < cboAutores.getItemCount(); i++) {
                CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                if (cb.isSelected()) {
                    com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();
                    bo.setId_author(doc.getUsuarioId());
                    dao.insertUpdate(bo);
                    if (correoAut.isEmpty()) {
                        correoAut = doc.getCorreo();
                    }
                }
            }
        }
        
        return correoAut;
    }
    
    private boolean isAlcanceSelected() {
        
        boolean b = true;
        
        TreePath[] aTree = jCheckBoxTree.getCheckedPaths();
        if (aTree.length == 0) {
            b = false;
        }
        
        return b;
    }
    
    private void saveDistributionList(int id) {
        
        String canal;
        String node;
        
        SendPublishDistributionListDAO dao = new SendPublishDistributionListDAO();
        SendPublishDistributionListBO bo = new SendPublishDistributionListBO();
        bo.setId_publish(id);
        dao.delete(bo);
        
        TreePath[] aTree = jCheckBoxTree.getCheckedPaths();
        for (TreePath path : aTree) {
            if (!path.getLastPathComponent().toString().equals("Todos")) {
                //System.out.println(path);
                if (path.getParentPath() != null && !path.getParentPath().toString().equals("[Todos]")) {
                    
                    node = valTree.get(path.getParentPath().getLastPathComponent().toString());
                    canal = valTree.get(path.getLastPathComponent().toString());
                    
                    if (canal.toCharArray().length == 1) {
                        canal = "0" + canal;
                    }
                    
                    if (node != null && canal != null) {
                        dao = new SendPublishDistributionListDAO();
                        bo = new SendPublishDistributionListBO();
                        bo.setId_publish(id);
                        bo.setId_channel(Integer.valueOf(node + canal));
                        
                        List<SendPublishDistributionListBO> listDist = dao.get(id);
                        if (listDist.isEmpty()) {
                            dao.insertUpdate(bo);
                        } else {
                            dao.insertUpdate(bo);
                        }
                    }
                }
            }
        }
        
    }

    /* Regresa el tweet de la publicación */
    private void saveTweet(boolean isDocSend) {

        // Save or update tweet if not send
        TwitterDAO tDao = new TwitterDAO();
        TwitterBO bo = new TwitterBO();
        //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        int idDocument = docBO.getDocumentId();
        int idUsuario = -1;
        if (idDocument > 0) {
            bo.setIdDocument(idDocument);
            bo.setHasDocument(true);
        } else {
            bo.setHasDocument(false);
        }
        
        if (inputTwitter.getText().toUpperCase().contains(theHashtag.getText().trim().toUpperCase())) {
            bo.setTweet(inputTwitter.getText());
        } else {
            bo.setTweet(inputTwitter.getText() + " " + theHashtag.getText().trim());
        }
        /* Si hay un archivo adjunto en el twitter lo tomamos y lo codificamos para su envio a la bd*/
        String key = null;
        if (twitterAttach != null) {
            for (Map.Entry e : twitterAttach.entrySet()) {
                bo.setAttachment((Blob) e.getValue());
                bo.setPath((String) e.getKey());
                key = (String) e.getKey();
            }
            
            File attach = fileTwitter;//chooser.getSelectedFile();
            if (attach != null) {
                try {
                    List<String> attch = new ArrayList<String>();
                    attch.add(attach.toPath().toString());
                    tweetXMLAttach = Utilerias.encodeTwitterFiles(attch);
                    Clob cl = new SerialClob(tweetXMLAttach.toCharArray());
                    bo.setEncodedTw(cl);
                } catch (SQLException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            } else {
                bo.setEncodedTw(twitterAttachEncoded.get(key));
            }
        }
        
        bo.setIdTwitter(checkForSavedTweetSend());
        bo.setIdUsuario(idUsuario);
        bo.setIsSend(isDocSend);
        tDao.insertUpdate(bo);
    }

    /* Regresa el tweet guardo  */
    private String getTweet(int idDoc) {
        // validar twitter vacio
        String tw = null;
        try {
            TwitterDAO dao = new TwitterDAO();
            List<TwitterBO> bo = dao.get(idDoc);
            if (bo != null && bo.size() > 0) {
                String twi = bo.get(0).getTweet();
                tw = (twi.isEmpty()) ? twitterInmediatamenteMedios : twi;

                //Buscar si tiene adjunto.
                if (bo.get(0).getAttachment() != null) {
                    twitterAttach.put(bo.get(0).getPath(), bo.get(0).getAttachment());
                    addTwitterAttach(true, bo.get(0).getPath());
                    twitterAttachEncoded.put(bo.get(0).getPath(), bo.get(0).getEncodedTw());
                    
                }
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return tw;
    }

    /* Convierte una hora en formato de 24hrs Ejemplo: (4:00PM a 16:00)*/
    private String convertTo24HoursFormat(String twelveHourTime)
            throws ParseException {
        return TWENTY_FOUR_TF.format(TWELVE_TF.parse(twelveHourTime));
    }
    
    private boolean sendPublish(int envioID, int docID, int idioma, String autorID, String url, String tema, String titulo,
            int statusID, String contenidoEmail, boolean isEnvioInmediato, String fechaEnvio, String tweet, String tweetAttach, String docsRelacionados, String listaDistribucion, boolean isDocAdjunto, boolean enviarEmail) {
        
        boolean b = false;
        
        try {
            com.adinfi.ws.publicador.IPublicador_Stub stub = (com.adinfi.ws.publicador.IPublicador_Stub) new com.adinfi.ws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
            UtileriasWS.setEndpoint(stub);
            DBResult res = stub.publicarDocumentoFormateador(
                    envioID,
                    docID,
                    idioma,
                    autorID,
                    url,
                    tema,
                    titulo,
                    statusID,
                    contenidoEmail,
                    isEnvioInmediato,
                    fechaEnvio,
                    tweet,
                    tweetAttach,
                    docsRelacionados,
                    getPublishTreeData(),
                    isDocAdjunto,
                    enviarEmail
            );
            
            String code = res.getResultCd();
            String msg = res.getResultMsg();
            
            b = code.equals("000") && msg.equals("0");

            /*Modificar el estatus a publicado*/
            if (b && isEnvioInmediato) {
                // enviado e imediato = Publicado    
                SendPublishDAO dao = new SendPublishDAO();
                dao.UpdateStatus(envioID, StatementConstant.SC_4.get());
            } else if (b && !isEnvioInmediato) {
                // enviado y no es imediato = Programado    
                SendPublishDAO dao = new SendPublishDAO();
                dao.UpdateStatus(envioID, StatementConstant.SC_2.get());
            }
            
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de publicación no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }
        
        return b;
    }

    /* Convierte una hora en formato de 12hrs Ejemplo: (16:00 a 4:00PM )*/
    private String convertTo12HoursFormat(String twentyHourTime)
            throws ParseException {
        return TWELVE_TF.format(TWENTY_FOUR_TF.parse(twentyHourTime));
    }
    
    private void updateURL(int idDocSend, String url) {
        
        SendPublishDAO dao = new SendPublishDAO();
        dao.UpdateURL(idDocSend, url);
        
    }
    
    public String convertDateFormat(String date, String formatIn, String formatOut) throws ParseException {
        
        String outFormat;
        SimpleDateFormat inFormat = new SimpleDateFormat(formatIn);
        Date d = inFormat.parse(date);
        inFormat.applyPattern(formatOut);
        outFormat = inFormat.format(d);
        
        return outFormat;
    }
    
    private boolean documentIsNotAdded(int idDocSend) {
        boolean b = true;
        if (cboRelatedDocs.getItemCount() > 0) {
            for (int i = 0; i < cboRelatedDocs.getItemCount(); i++) {
                Object obj = cboRelatedDocs.getItemAt(i);
                RelationalFilesBO bo = (RelationalFilesBO) obj;
                if (bo.getIdDocSend() == idDocSend) {
                    b = false;
                }
                
            }
            
        } else {
            b = false;
        }
        
        return b;
    }
    
    private String CLOBToString(Clob cl) throws IOException, SQLException {
        if (cl == null) {
            return "";
        }
        StringBuffer strOut = new StringBuffer();
        String aux;
        BufferedReader br = new BufferedReader(cl.getCharacterStream());
        while ((aux = br.readLine()) != null) {
            strOut.append(aux);
        }
        return strOut.toString();
    }
    
    private void loadDatePicker() {
        JDatePanelImpl datePanel1 = new JDatePanelImpl(publicationDate);
        JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
        publicationDate.setDate(year, month, day);
        publicationDate.setSelected(true);
        
        datePublishPicker.add(BorderLayout.CENTER, datePicker1);
    }
    
    private boolean isSelectedAutor() {
        boolean selected = false;
        
        for (int i = 0; i < cboAutores.getItemCount(); i++) {
            CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
            if (cb.isSelected()) {
                selected = true;
            }
        }
        return selected;
    }
    
    private void setTextinPublishFromDocument() {
        StringBuilder html = new StringBuilder();
        
        HTMLDocument doc = new HTMLDocument();
        List<String> documentsTxt = doc.getTextFromDocument(docBO);
        if (documentsTxt != null && documentsTxt.isEmpty() == false) {
            html.append("<html>"
                    + "<head> </head>"
                    + "<body>");
            for (String docs : documentsTxt) {
                html.append(removeHTMLheaderTags(docs.trim()));
            }
            html.append("</body>"
                    + "</html>");
            String texto = html.toString();
            texto = texto.replace("#8A0808", "#000000");
            respTextoClob = texto;
            if (texto.length() > getMaxCharMail()) {
                ekitCore1.getTextPane().setText(texto.substring(0, getMaxCharMail()));
            } else {
                ekitCore1.getTextPane().setText(texto);
            }
        }
    }
    
    private void allowHashtagModify() {
        
        String perfil = InstanceContext.getInstance().getUsuario().getPerfil();
        
        if (perfil.equals("Administrador")) {
            theHashtag.setEnabled(true);
        }
        
    }
    
    private String removeHTMLheaderTags(String html) {
        String result = null;
        
        if (html != null && !html.isEmpty()) {
            result = html.replaceAll("<html>", "");
            result = result.replaceAll("</html>", "");
            result = result.replaceAll("<head>", "");
            result = result.replaceAll("</head>", "");
            result = result.replaceAll("<body>", "");
            result = result.replaceAll("</body>", "");
            result = result.replaceAll("<title>", "");
            result = result.replaceAll("</title>", "");
            result = result.replaceAll("</html", "");
        }
        
        return result;
    }
    
    private String getSubjectPublishName() {
        String name = "";
        if (selectedValueSubject != null) {
            //Object obj = cboSubject.getSelectedItem();
            SubjectBO bo = selectedValueSubject;//(SubjectBO) obj;
            name = bo.getName();
        }
        
        return name;
    }
    
    private void checkInicialTree() {
        
        int n = jCheckBoxTree.getRowCount();
        List<String> listSaved = new ArrayList<>();
        String canalValue = null;
        
        SendPublishDistributionListDAO dao = new SendPublishDistributionListDAO();
        SendPublishDistributionListBO bo = new SendPublishDistributionListBO();
        SendPublishDAO daoSend = new SendPublishDAO();
        List<SendPublishBO> listPub = daoSend.get(docBO.getDocumentId());
        List<SendPublishDistributionListBO> listSavedDB = null;
        
        if (!listPub.isEmpty()) {
            listSavedDB = dao.get(listPub.get(0).getIdDocument_send());

            /* Carga inicial los archivos adjuntos de la publicacion */
            loadAttachmentFiles(listPub.get(0).getIdDocument_send());
        }
        
        if (listSavedDB != null && !listSavedDB.isEmpty()) {
            
            for (int i = 0; i < listSavedDB.size(); i++) {
                listSaved.add(String.valueOf(listSavedDB.get(i).getId_channel()));
            }
            
            List<String> savedValues = new ArrayList<>();
            Map<String, String> distributionMapParent = new HashMap<>();
            
            for (String canal : listSaved) {
                char[] arrayCanal = canal.toCharArray();

                // si en un momento el canal es > 9 toma dos caracteres hasta 99
                if (Character.getNumericValue(arrayCanal[1]) == 0) {
                    canalValue = String.valueOf(arrayCanal[2]);
                } else if (Character.getNumericValue(arrayCanal[1]) > 0) {
                    canalValue = String.valueOf(arrayCanal[1] + arrayCanal[2]);
                }
                
                savedValues.add(hijos.get(canalValue));
                distributionMapParent.put(hijos.get(canalValue), nodos.get(String.valueOf(arrayCanal[0])));
            }
            
            for (int i = 1; i < n; i++) {
                TreePath path = jCheckBoxTree.getPathForRow(i);
                
                System.out.println(path + "->" + i);
                System.out.println(path.getLastPathComponent().toString());
                System.out.println(path.getParentPath().getLastPathComponent().toString());
                System.out.println(distributionMapParent.get(path.getLastPathComponent().toString()));
                
                if (savedValues.contains(path.getLastPathComponent().toString())
                        && path.getParentPath().getLastPathComponent().toString().equals(distributionMapParent.get(path.getLastPathComponent().toString()))) {
                    //System.out.println(path.getParentPath().getLastPathComponent().toString() + "  -  " + path.getLastPathComponent().toString());
                    jCheckBoxTree.checkNode(path, true);
                    jCheckBoxTree.checkNode(path.getParentPath(), ok);
                }
            }
        } else {
            //listSaved.add("202");
            //listSaved.add("201");
            //listSaved.add("105");
            listSaved = codeNodeDefault;
            
            List<String> savedValues = new ArrayList<>();
            Map<String, String> distributionMapParent = new HashMap<>();
            List<Map<String, String>> distribution = new ArrayList<>();

            for (String canal : listSaved) {
                char[] arrayCanal = canal.toCharArray();

                // si en un momento el canal es > 9 toma dos caracteres hasta 99
                if (Character.getNumericValue(arrayCanal[1]) == 0) {
                    canalValue = String.valueOf(arrayCanal[2]);
                } else if (Character.getNumericValue(arrayCanal[1]) > 0) {
                    canalValue = String.valueOf(arrayCanal[1] + arrayCanal[2]);
                }
                
                savedValues.add(hijos.get(canalValue));
                distributionMapParent.put(hijos.get(canalValue), nodos.get(String.valueOf(arrayCanal[0])));
                
                Map<String, String> dist = new HashMap<>();
                dist.put(hijos.get(canalValue), nodos.get(String.valueOf(arrayCanal[0])));
                distribution.add(dist);
            }
            
            for (int i = 1; i < n; i++) {
                TreePath path = jCheckBoxTree.getPathForRow(i);
                if (   savedValues.contains( path.getLastPathComponent().toString() ) 
                       //&& path.getParentPath().getLastPathComponent().toString().equals(  distributionMapParent.get(path.getLastPathComponent().toString())  )  
                       && getPathComparator(distribution, path.getLastPathComponent().toString(), path.getParentPath().getLastPathComponent().toString())  ) {
                    //System.out.println(path.getParentPath().getLastPathComponent().toString() + "  -  " + path.getLastPathComponent().toString());
                    jCheckBoxTree.checkNode(path, true);
                    jCheckBoxTree.checkNode(path.getParentPath(), ok);
                }
            }
            
        }
    }
    
    private boolean getPathComparator(List<Map<String, String>> dist, String key, String comp){
        if(dist == null)
            return false;
        
        for (Map<String, String> d : dist) {
            if( comp.equals( d.get(key) ) ){
                return true;
            }
        }
        
        return false;
    }

    private void loadAttachmentFiles(int idPublish) {
        
        SendPublishFilesDAO dao = new SendPublishFilesDAO();
        List<SendPublishFilesBO> listAttachmentFiles = dao.get(idPublish);
        
        if (listAttachmentFiles != null && !listAttachmentFiles.isEmpty()) {
            
            for (SendPublishFilesBO ls : listAttachmentFiles) {
                
                ItemPanel panel = new ItemPanel(ls.getFilename(), ls.getUid(), this, filesAttachPath.get(ls.getUid()));
                container.add(panel);
                container.updateUI();
                
                filesAttch.put(ls.getUid(), ls.getFile());
                filesAttachName.put(ls.getUid(), ls.getFilename());
            }
            
        }
    }
    

    private void inputTituloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTituloFocusGained
        if (inputTitulo.getText().equals("Título")) {
            inputTitulo.setText(" ");
        }
    }//GEN-LAST:event_inputTituloFocusGained

    private void inputTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTituloMouseClicked

    }//GEN-LAST:event_inputTituloMouseClicked

    private void inputTituloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTituloFocusLost
        if (inputTitulo.getText().isEmpty() || inputTitulo.getText().equals("")) {
            inputTitulo.setText("Título");
        }
    }//GEN-LAST:event_inputTituloFocusLost

    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        addFilesToContainer();
    }//GEN-LAST:event_openFileActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        addSubjectComponent();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void groupProgramarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_groupProgramarItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            
            publicationDate.setValue(null);
            inputTime.setValue(null);
            datePublishPicker.setEnabled(true);
            inputTime.setEnabled(true);
            
            try {
                MaskFormatter dateMask2 = new MaskFormatter("##:##/UU");
                dateMask2.install(inputTime);
            } catch (ParseException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        } else {
            datePublishPicker.setEnabled(false);
            inputTime.setEnabled(false);
            setProgramerInputs();
        }
    }//GEN-LAST:event_groupProgramarItemStateChanged

    private void groupEnviarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_groupEnviarItemStateChanged

    }//GEN-LAST:event_groupEnviarItemStateChanged

    private void cboSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSubjectActionPerformed

    private void cboSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSubjectItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object obj = cboSubject.getSelectedItem();
            SubjectBO bo = (SubjectBO) obj;
            int id = bo.getIndustry();
            if (id > 0) {
                
                for (int i = 0; i < cboIndustry.getItemCount(); i++) {
                    Object object = cboIndustry.getItemAt(i);
                    IndustryBO bo_ = (IndustryBO) object;
                    int idIndustry = bo_.getIdIndustry();
                    if (idIndustry == id) {
                        cboIndustry.setSelectedIndex(i);
                    }
                }
                
            } else {
                cboIndustry.setSelectedIndex(-1);
            }
        }
    }//GEN-LAST:event_cboSubjectItemStateChanged

    private void btnSaveSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSendActionPerformed
        validateSendPublish(false);
    }//GEN-LAST:event_btnSaveSendActionPerformed

    private void btnSendPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendPublishActionPerformed
        validateSendPublish(true);
    }//GEN-LAST:event_btnSendPublishActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        CustomAction customAction = new CustomAction();
        customAction.executeAction(RibbonMenu.PDF_VIEWER);
    }//GEN-LAST:event_btnPDFActionPerformed

    private void btnHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHTMLActionPerformed
        CustomAction customAction = new CustomAction();
        customAction.executeAction(RibbonMenu.HTML_VIEWER);
    }//GEN-LAST:event_btnHTMLActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int result = JOptionPane.showConfirmDialog(
                MainApp.getApplication().getMainFrame(), "La información capturada se perderá, ¿Desea Continuar?.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        addTwitterAttach(false, null);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void cboAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAutoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAutoresActionPerformed

    private void removeAttchTWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAttchTWActionPerformed
        imageInLabel.setVisible(false);
        removeAttchTW.setVisible(false);
        chooser.setSelectedFile(null);
        selectedFile = false;

        //Regresar los 23 caracteres de la imagen.
        int chaI = (23 + Integer.valueOf(counterTwitteer.getText()));
        counterTwitteer.setText(String.valueOf(chaI));
        if (chaI < 0) {
            counterTwitteer.setForeground(Color.RED);
            validadoTw = false;
        } else {
            counterTwitteer.setForeground(Color.BLACK);
            validadoTw = false;
        }

    }//GEN-LAST:event_removeAttchTWActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        RelationalFiles rf = new RelationalFiles(null, true);
        
        rf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Map<Integer, String> map = rf.selectedRows();
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    Integer key = (Integer) it.next();
                    //System.out.println("Clave: " + key + " -> Valor: " + map.get(key));
                    RelationalFilesBO bo = new RelationalFilesBO();
                    bo.setIdDocSend(key);
                    bo.setDocument_name(map.get(key));
                    
                    if (cboRelatedDocs.getItemCount() == 0 || documentIsNotAdded(key)) {
                        cboRelatedDocs.addItem(bo);
                    }
                    
                }
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                
            }
        });
        
        rf.setLocationRelativeTo(null);
        rf.setVisible(true);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        
        if (cboRelatedDocs.getItemCount() > 0) {
            cboRelatedDocs.removeItem(cboRelatedDocs.getSelectedItem());
            cboRelatedDocs.updateUI();
        }
        

    }//GEN-LAST:event_jButton11ActionPerformed

    private void cboRelatedDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRelatedDocsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRelatedDocsActionPerformed

    private void groupEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupEnviarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_groupEnviarActionPerformed

    private void groupProgramarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupProgramarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_groupProgramarActionPerformed

    private void inputTwitterInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_inputTwitterInputMethodTextChanged
    }//GEN-LAST:event_inputTwitterInputMethodTextChanged

    private void txtTemaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusLost
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            fillOtherValuesBySubject();
        }
    }//GEN-LAST:event_txtTemaFocusLost

    private void txtTemaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemaKeyTyped
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            fillOtherValuesBySubject();
        }
    }//GEN-LAST:event_txtTemaKeyTyped

    private void txtTemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTemaMouseClicked
        // TODO add your handling code here:
        listenerData();
        if (selectedValueSubject != null) {
            fillOtherValuesBySubject();
        }
    }//GEN-LAST:event_txtTemaMouseClicked

    private void theHashtagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_theHashtagActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_theHashtagActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane FilesContainer;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnHTML;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnSaveSend;
    private javax.swing.JButton btnSendPublish;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboAutores;
    private javax.swing.JComboBox cboDocumentType;
    private javax.swing.JComboBox cboIndustry;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboMarket;
    private javax.swing.JComboBox cboRelatedDocs;
    private javax.swing.JComboBox cboStatus;
    private javax.swing.JComboBox cboSubject;
    private javax.swing.JPanel container;
    private javax.swing.JLabel counterTwitteer;
    private javax.swing.JPanel datePublishPicker;
    private javax.swing.JRadioButton groupEnviar;
    private javax.swing.JRadioButton groupProgramar;
    private javax.swing.JLabel imageInLabel;
    private javax.swing.JTextField inputFilesarray;
    private javax.swing.JFormattedTextField inputTime;
    private javax.swing.JTextField inputTitulo;
    private javax.swing.JTextArea inputTwitter;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton openFile;
    private javax.swing.JProgressBar publishProgressBar;
    private javax.swing.JButton removeAttchTW;
    private javax.swing.JPanel textEditorContainer;
    private javax.swing.JTextField theHashtag;
    private javax.swing.JScrollPane treeContainer;
    private javax.swing.JTextField txtFechaPublica;
    private javax.swing.JTextField txtTema;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JFileChooser myFileChooser;
    private javax.swing.JFileChooser myFileChooser2;
    
    class TreeProfile {
        
        private int canalId;
        private boolean requerido;
        private boolean default_;
        private String descripcion;
        
        public TreeProfile() {
            
        }
        
        public TreeProfile(int canalId, boolean requerido, boolean default_, String descripcion) {
            this.canalId = canalId;
            this.requerido = requerido;
            this.default_ = default_;
            this.descripcion = descripcion;
            
        }

        /**
         * @return the canalId
         */
        public int getCanalId() {
            return canalId;
        }

        /**
         * @param canalId the canalId to set
         */
        public void setCanalId(int canalId) {
            this.canalId = canalId;
        }

        /**
         * @return the requerido
         */
        public boolean isRequerido() {
            return requerido;
        }

        /**
         * @param requerido the requerido to set
         */
        public void setRequerido(boolean requerido) {
            this.requerido = requerido;
        }

        /**
         * @return the default_
         */
        public boolean isDefault_() {
            return default_;
        }

        /**
         * @param default_ the default_ to set
         */
        public void setDefault_(boolean default_) {
            this.default_ = default_;
        }

        /**
         * @return the descripcion
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * @param descripcion the descripcion to set
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        
    }
    
    class PublishWorker extends SwingWorker<Boolean, Boolean> {
        
        private final DocumentBO docBO;
        private final SendPublishBO boSend;
        private final int idStatusPublish_;
        private final String inputTwitterTxt;
        private final String theHashtagTxt;
        private final UtilDateModel publicationDate_;
        private final int idPublish;
        private final String authors_;
        private final String subjectPublishName;
        private final String dateSend;
        private final String tweetXMLAttach;
        private final String docsRelacionados;
        private final JProgressBar bar;
        
        PublishWorker(DocumentBO docBO, SendPublishBO boSend, int idStatusPublish_, String inputTwitterTxt, String theHashtagTxt, UtilDateModel publicationDate_, int idPublish, String authors_, String subjectPublishName, String dateSend, String tweetXMLAttach, String docsRelacionados, JProgressBar bar) {
            this.docBO = docBO;
            this.boSend = boSend;
            this.idStatusPublish_ = idStatusPublish_;
            this.inputTwitterTxt = inputTwitterTxt;
            this.theHashtagTxt = theHashtagTxt;
            this.publicationDate_ = publicationDate_;
            this.idPublish = idPublish;
            this.authors_ = authors_;
            this.subjectPublishName = subjectPublishName;
            this.dateSend = dateSend;
            this.tweetXMLAttach = tweetXMLAttach;
            this.docsRelacionados = docsRelacionados;
            this.bar = bar;
        }
        
        @Override
        protected Boolean doInBackground() {
            
            boolean b = false;
            
            String emailText = "";
            try {
                emailText = removeHTMLheaderTags(CLOBToString(boSend.getText()));
                
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }

            //Enviar en el constructor del worker
            /* Enviamos el id del tipo de documento de vector */
            Object selectedDocType = cboDocumentType.getSelectedItem();
            DocumentTypeBO selectedDocTypeBO = (DocumentTypeBO) selectedDocType;
            //validación idMiVector.
            if ((selectedDocTypeBO.getIddocument_type_vector() != null) && (!selectedDocTypeBO.getIddocument_type_vector().isEmpty())) {
                documentTypeID = selectedDocTypeBO.getIddocument_type_vector();
            } else {
                documentTypeID = "0";
            }

            // --- parametro
            String tweet = "";
            if (!inputTwitterTxt.isEmpty()) {
                if (inputTwitterTxt.toUpperCase().contains(theHashtagTxt.trim())) {
                    tweet = inputTwitterTxt;
                } else {
                    tweet = inputTwitterTxt + " " + theHashtagTxt.trim();
                }
            }
            
            String day = String.valueOf(publicationDate_.getDay());
            String month = String.valueOf(publicationDate_.getMonth() + 1);
            String year = String.valueOf(publicationDate_.getYear());
            
            String fechaSSH = day + "/" + month + "/" + year;
            try {
                fechaSSH = convertDateFormat(fechaSSH, "dd/MM/yyyy", "dd/MM/yyyy");
            } catch (ParseException ex) {
                Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            docBO.setIdPublish(idPublish);
            url = UtileriasSSH.getInstance().sendFilesSsh(boSend, docBO, fechaSSH, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY, getAuthorsJson(), getPublishTreeDataJson(), dateSend.replace("-", "/"));
            
            boolean isEnvioInmediato = !boSend.isScheduled();
            
            try {
                com.adinfi.ws.publicador.IPublicador_Stub stub = (com.adinfi.ws.publicador.IPublicador_Stub) new com.adinfi.ws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
                UtileriasWS.setEndpoint(stub);
                DBResult res = stub.publicarDocumentoFormateador(
                        idPublish,
                        Integer.valueOf(documentTypeID),
                        docBO.getIdLanguage(),
                        authors_,
                        url,
                        subjectPublishName,
                        boSend.getTitle(),
                        idStatusPublish_,
                        emailText,
                        isEnvioInmediato,
                        dateSend.replace("-", "/"),
                        tweet,
                        tweetXMLAttach,
                        docsRelacionados,
                        getPublishTreeData(),
                        false, // es documento adjunto?
                        true // se enviara el email?
                );
                
                String code = res.getResultCd();
                String msg = res.getResultMsg();
                
                b = code.equals("000") && msg.equals("0");
                
                if (b) {

                    /* Actualiza el URL en la tabla */
                    updateURL(idPublish, url);

                    /*Modificar el estatus a publicado*/
                    if (boSend.getIdStatus_publish() != 2) {
                        if (b && boSend.isScheduled()) {
                            // enviado y no es imediato = Programado   
                            SendPublishDAO dao = new SendPublishDAO();
                            dao.UpdateStatus(idPublish, StatementConstant.SC_4.get());
                        } else if (b && !boSend.isScheduled()) {
                            // enviado e imediato = Publicado 
                            SendPublishDAO dao = new SendPublishDAO();
                            dao.UpdateStatus(idPublish, StatementConstant.SC_2.get());
                        }
                    }

                    //Empieza actualizacion de Collaborativos si los contiene
                    CollaborativesDAO collabDAO = new CollaborativesDAO();
                    List<DocumentBO> lstDocCollab = collabDAO.getDocumentBoCollabByDocument(docBO.getDocumentId());
                    
                    SendPublishDAO daoPub = new SendPublishDAO();
                    
                    for (DocumentBO docCollab : lstDocCollab) {
                        
                        if (docCollab.getPublishName() == null) {
                            continue;
                        }
                        
                        List<SendPublishBO> lstDocSendPub = daoPub.get(docCollab.getDocumentId());
                        if (lstDocSendPub != null && lstDocSendPub.size() > 0) {
                            SendPublishBO docSPubBO = lstDocSendPub.get(0);
                            String nStrDate = null;
                            try {
                                Date dd = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(docSPubBO.getDate_publish() == null ? docSPubBO.getDate() : docSPubBO.getDate_publish());
                                nStrDate = new SimpleDateFormat("dd/MM/yyyy").format(dd);
                            } catch (ParseException ex) {
                                Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            UtileriasSSH.getInstance().sendFilesSsh(docSPubBO, docCollab, nStrDate, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
                        }
                    }
                    //Termina actualizacion de Collaborativos
                }
                
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            
            return b;
        }
        
        @Override
        protected void done() {
            boolean b = false;
            try {
                b = get();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
            try {
                if (b) {
                    JOptionPane.showMessageDialog(null, "Se ha guardado y enviado correctamente");
                    cboStatus.setSelectedIndex(StatementConstant.SC_3.get());
                    setStatus(idPublish, StatementConstant.SC_2.get());
                    dispose();
                } else {
                    cboStatus.setSelectedIndex(StatementConstant.SC_2.get());
                    setStatus(idPublish, StatementConstant.SC_3.get());
                    JOptionPane.showMessageDialog(null, "Se ha guardado correctamente pero no se envio");
                }
                
            } finally {
                bar.setVisible(false);
                btnSaveSend.setEnabled(true);
                btnSendPublish.setEnabled(true);
                btnPDF.setEnabled(true);
                btnHTML.setEnabled(true);
                btnCancel.setEnabled(true);
            }
        }
        
    }
}
