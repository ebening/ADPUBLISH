package com.adinfi.formateador.view;
  
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.editor.UtileriasPDF;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.hexidec.ekit.EkitCore;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang3.StringUtils;
 
/**
 *
 * @author Fede
 * 
 * Clase encargada de generar el componente de bullet
 * 
 */
public class ScrCellTextBullet extends javax.swing.JPanel implements DropTargetListener 
{

    /* Version modificada para formateador */  
    private static final Logger LOG = Logger.getLogger(ScrCellTextBullet.class.getName());
    protected ModuleSectionBO secInfo;  
    protected DropTarget dropTarget;
    protected boolean acceptableType; // Indicates whether data is acceptable
    protected DataFlavor targetFlavor; // Flavor to use for transfer     
    protected ScrDocument parentDoc;
    protected ObjectInfoBO objInfo;
    private int objectId = 0;

    private EkitCore ekitCore;
    private JTextPane sourceText;
    private JTextArea jTextArea;
    private String plainText;
    
    // Se declaran dos constantes para el manejo de maximos
    private static final int MAX_CHARACTERS = 200; 
    private static final int MAX_BULLETS = 4;
    private boolean isPasteCtrlV;
    private boolean isCtrl;
    private int totalChar = 0;
    private int longLett = 0;
    private int longRow = 0;
    private int lettBySpace = 0;
    private JPopupMenu popup;
    private ModuleBO moduleBO;

    /**
     * Creates new form ScrCellText
     *
     * @param scrDoc
     * @param secInfo
     */
    public ScrCellTextBullet(ScrDocument scrDoc, ModuleSectionBO secInfo, ModuleBO moduleBO) 
    {
              
        initComponents();
        initTextEditor();
        this.secInfo = secInfo;
        this.parentDoc = scrDoc;
        this.moduleBO=moduleBO;
        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<ObjectInfoBO>());
        }
        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
                this, true, null);
        this.setDropTarget(dropTarget);
        if (secInfo != null) {
            //  this.lblSectionName.setText(secInfo.getSectionName());
        }

        initScreen();
        addObject(GlobalDefines.OBJ_TYPE_TEXT);
        cboIdiomasActionPerformed(null);
        
        longLett = Utilerias.letrasPorRenglon(secInfo.getWidth());
        longRow = Utilerias.renglonesPorEspacioEnSeccion(secInfo);
        lettBySpace = Utilerias.letrasPorEspacio(secInfo);
        resPar = new Hashtable<>();
    }

    private void initTextEditor() 
    {
        System.out.println("En initTextEditor ... "); 
        try {
            /*  Show ekit custom buttons */
//            String sDocument = null;
//            String sStyleSheet = null;
//            String sRawDocument = null;
//            URL urlStyleSheet = null;
//            boolean includeToolBar = true;
//            boolean base64 = false;
//            boolean enterBreak = false;
//            boolean showViewSource = false;
//            boolean showMenuIcons = false;
//            boolean editModeExclusive = true;
//            String sLanguage = null;
//            String sCountry = null;
//            boolean debugMode = false;
//            boolean multiBar = true;
//
//            Vector<String> v = new Vector<>();
//            v.add(EkitCore.KEY_TOOL_COPY);
//            v.add(EkitCore.KEY_TOOL_PASTEX);
//            v.add(EkitCore.KEY_TOOL_PASTE);
//            v.add(EkitCore.KEY_TOOL_CUT);
//
//            v.add(EkitCore.KEY_TOOL_BOLD);
//            v.add(EkitCore.KEY_TOOL_ITALIC);
//            v.add(EkitCore.KEY_TOOL_UNDERLINE);
//
//            v.add(EkitCore.KEY_TOOL_UNDO);
//            v.add(EkitCore.KEY_TOOL_REDO);
//            // -- Se agrega al vector elemento para agregar Unsorted List
//            v.add(EkitCore.KEY_TOOL_ULIST);
//
//            ekitCore = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
//            sourceText = ekitCore.getTextPane();
//            jTextArea = ekitCore.getSourcePane();   
//            
//            add(ekitCore, BorderLayout.CENTER);
            
            Vector<String> v = Utilerias.getOptionsToEkitcore(Utilerias.EKIT_CORE_TYPE.BULLET);
            ekitCore = Utilerias.getEkitCore();
            
            sourceText = ekitCore.getTextPane();
            jTextArea = ekitCore.getSourcePane();
            add(ekitCore, BorderLayout.CENTER);
            
            /*
            cboIdiomas = new JComboBox();
            cboIdiomas.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Sin corrección", "Español", "Inglés"}));
            cboIdiomas.setSelectedIndex(1);
            cboIdiomas.setSize(50, 20);
            cboIdiomas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cboIdiomasActionPerformed(e);
                }
            });

            addToolBars(v);
            */
            //sourceText.requestFocusInWindow();
         
            
            
            sourceText.addFocusListener(new java.awt.event.FocusAdapter() 
            {
                @Override
                public void focusLost(FocusEvent evt) 
                {
                    sourceTextFocusLost(evt);
                }
                
                @Override
                public void focusGained(FocusEvent evt) 
                {
                    ;
                    
                }
            });

            
            sourceText.addKeyListener(new KeyListener() 
            {
                @Override
                public void keyPressed(KeyEvent e) 
                {
                    if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                        isCtrl = true;
                    }else if(e.getKeyCode() == KeyEvent.VK_V && isCtrl){
                        isPasteCtrlV = true;
                        String text = delCodeBulletCopy(ekitCore.getTextPane().getText());
                        sourceText.setText(text);
                        ekitCore.getTextPane().setText(text);
                        actualizaText();
                    } else if(e.getKeyCode() == KeyEvent.VK_Z && isCtrl){
                        Utilerias.undoEkitCore(ekitCore);
                    } 
                }

                @Override
                public void keyTyped(KeyEvent e) 
                {
                    ;
                }

                @Override
                public void keyReleased(KeyEvent e) 
                {
                    if(!e.isActionKey() && !e.isControlDown() && isNoActionChar(e)){
                        if(e.getKeyCode() == KeyEvent.VK_ENTER)
                            validaParrafos(false);
                        else
                            validaParrafos(true);
                    }
                    
                    if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                        isCtrl = false;
                    } else if(e.getKeyCode() == KeyEvent.VK_V){
                        isPasteCtrlV = false;
                    }
                    
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
                        String resVal = sourceText.getText();
                        if(!resVal.contains("</p>")){
                            resVal = resVal.replaceAll("<body>", "<body><p style=\"margin-top: 0\">");
                            resVal = resVal.replaceAll("</body>", "</p></body>");
                            sourceText.setText(resVal);
                            ekitCore.getTextPane().setText(resVal);
                            actualizaText();
                            resPar = new Hashtable<>();
                        }
                    }
                }
                
            });
            
            sourceText.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    validaParrafos(false);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    String resVal = sourceText.getText();
                    if(!resVal.contains("</p>")){
                        resVal = resVal.replaceAll("<body>", "<body><p style=\"margin-top: 0\">");
                        resVal = resVal.replaceAll("</body>", "</p></body>");
                        sourceText.setText(resVal);
                        ekitCore.getTextPane().setText(resVal);
                        actualizaText();
                        resPar = new Hashtable<>();
                    }
                    
                    if( e.isPopupTrigger() && InstanceContext.getInstance().getUsuario().getIsAdmin() ){
                        JMenuItem menuItem = new JMenuItem("Renglones por Módulo");
                        menuItem.setName("R_P_M");
                        menuItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                DlgRenglonModulo ren = new DlgRenglonModulo(MainApp.getApplication().getMainFrame(), true, secInfo);
                                ren.setLocationRelativeTo(ren);
                                ren.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
                                ren.setVisible(true);
                            }
                        });
                        
                        JMenuItem menuItemAmc = new JMenuItem("Agregar módulo a colaborativo");
                        menuItemAmc.setName("A_M_C");
                        menuItemAmc.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                addToCollaborative();
                            }
                        });
                        
                        if( popup.getComponentCount() <= 0 ||
                            "R_P_M".equals(popup.getComponent(popup.getComponentCount() -1).getName()) == false ){
                            popup.add(menuItemAmc);
                            popup.add(menuItem);
                        }
                    }
                }
                
            });

            cboIdiomas = new JComboBox();
            cboIdiomas.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Sin corrección", "Español", "Inglés"}));
            cboIdiomas.setSelectedIndex(1);
            cboIdiomas.setSize(50, 20);
            cboIdiomas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cboIdiomasActionPerformed(e);
                }
            });          

            /* Add tool bars to component */
            if (v == null || v.isEmpty()) {
                addAllToolBars();
            } else {
                addToolBars(v);
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }
    
    protected void addToCollaborative() {
        //this.parentDoc.addModuleColaborative(this.secInfo.getModuleId());
        if( this.moduleBO.getDocumentModuleId()<=0 ) {       
            JOptionPane.showMessageDialog(this, "El módulo debe ser guardado para que se pueda agregar al documento colaborativo");
            return;         
        }         
        this.parentDoc.addModuleColaborative(this.moduleBO.getDocumentModuleId() );
    }
    
    private boolean isNoActionChar(KeyEvent e){
        switch (e.getKeyCode()) {
          case KeyEvent.VK_BACK_SPACE:
          case KeyEvent.VK_DELETE:
          case KeyEvent.VK_SHIFT:
          case KeyEvent.VK_CONTROL:
              return false;
        }
        return true;
    }

    public static String toPlainText(String s) 
    {
        System.out.println("En toPlainText ... "+s); 
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }

    private void sourceTextFocusLost(java.awt.event.FocusEvent evt) 
    {
        actualizaText();
    }

    private void addAllToolBars() 
    {
        System.out.println("En sourceTextFocusLost ... "); 
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridx = 1;

            gbc.gridy = 1;
            add(ekitCore.getToolBarMain(true), gbc);

            gbc.gridy = 2;
            add(ekitCore.getToolBarFormat(true), gbc);

            gbc.gridy = 3;
            add(ekitCore.getToolBarStyles(true), gbc);

            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.gridy = 4;
            add(ekitCore, gbc);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }

    private void addToolBars(Vector<String> v) 
    {
        System.out.println("En addToolBars ... "); 
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.gridx = 1;
            JToolBar customBar = ekitCore.customizeToolBar(EkitCore.TOOLBAR_MAIN, v, true);
            add(customBar, gbc);
            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.gridy = 4;
            add(ekitCore, gbc);

            customBar.add(cboIdiomas);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }

    private void cboIdiomasActionPerformed(java.awt.event.ActionEvent evt) 
    {
        System.out.println("En cboIdiomasActionPerformed ... "); 
        int i = cboIdiomas.getSelectedIndex();
        String language;
        switch (i) {
            case 1:
                language = "es";
                break;
            case 2:
                language = "en";
                break;
            default:
                try {
                    SpellChecker.unregister(sourceText);
                } catch (Exception ex) {
                    LOG.log(Level.FINE, ex.getMessage());
                }
                language = null;
                break;
        }

        if (language == null) {
            return;
        }

        try {
            // Create user dictionary in the current working directory of your application
            SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
            // Load the configuration from the file dictionaries.cnf and 
            // use the current locale or the first language as default
            // You can download the dictionary files from 
            // http://sourceforge.net/projects/jortho/files/Dictionaries/                        
            URL is = new URL(Utilerias.rutaDiccionario());
            SpellChecker.registerDictionaries(is, language);
            // enable the spell checking on the text component with all features
            SpellChecker.register(sourceText);
            SpellCheckerOptions sco = new SpellCheckerOptions();
            sco.setCaseSensitive(true);
            sco.setSuggestionsLimitMenu(10);
            sco.setLanguageDisableVisible(false);
            sco.setIgnoreWordsWithNumbers(true);
            popup = SpellChecker.createCheckerPopup(sco);
            sourceText.addMouseListener(new PopupListener(popup));

        } catch (NullPointerException | MalformedURLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    public void initScreen() 
    {
        System.out.println("En initScreen ... "); 
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    popSection.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
        refreshObjects();
    }

    public void setDrag() 
    {
        System.out.println("En setDrag ... ");
        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
                this, true, null);
        this.setDropTarget(dropTarget);
    }

    public void deleteModule() 
    {
        System.out.println("En deleteModule ... ");

        this.parentDoc.deleteModule(this.secInfo.getSectionParentId());
    }

    //**inicio dnd
    // Implementation of the DropTargetListener interface
    @Override
    public void dragEnter(DropTargetDragEvent dtde) 
    {
        System.out.println("En dragEnter ... ");
        Utilerias.logger(getClass()).info("dragEnter, drop action = " + dtde.getDropAction());
        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);

        // Accept or reject the drag.
        acceptOrRejectDrag(dtde);
    }

    public void deletObject(int objectId) 
    {
        System.out.println("En deletObject ... ");
        if (JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el objeto ?", "Eliminar Objeto", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
            return;
        }

        if (this.secInfo.getLstObjects() == null) {
            return;
        }

        for (ObjectInfoBO OnjInfoBOLocal : this.secInfo.getLstObjects()) {
            if (OnjInfoBOLocal.getObjectId() == objectId) {
                // this.secInfo.getLstObjects().remove(i);  
                OnjInfoBOLocal.setDelete(true);
                this.refreshObjects();
                break;
            }
        }
    }

    protected void addObject(int objectType) 
    {
        System.out.println("En addObject ... ");
        objInfo = new ObjectInfoBO();
        boolean existe = false;
        for (ObjectInfoBO objInfo : this.secInfo.getLstObjects()) {
            objectId = objInfo.getObjectId();
            //setObjectId(objInfo.getObjectId());
            objInfo.setObjectId(objInfo.getObjectId());
            objInfo.setPlain_text(objInfo.getPlain_text());
            objInfo.setData(objInfo.getData());
            objInfo.setObjType(objInfo.getObjType());
            objInfo.setSectionId(objInfo.getSectionId());
            objInfo.setTemplateId(objInfo.getTemplateId());
            //objInfo.setSectionRootId(objInfo.getSectionRootId());
            //  objInfo.setSectionRootId(this.secInfo.getSectionIdModule());
            existe = true;
            break;
        }

        if (!existe) {
            objInfo.setObjType(GlobalDefines.OBJ_TYPE_TEXT);
            objInfo.setSectionId(this.secInfo.getSectionId());
            //objInfo.setTemplateId(this.secInfo.getTemplateId());
            //objInfo.setSectionRootId(this.secInfo.getParentSection());
            //  objInfo.setSectionRootId(this.secInfo.getSectionIdModule());

            //TODO NUEVO para errores
            objInfo.setData(objInfo.getData());
            objInfo.setPlain_text(objInfo.getPlain_text());

            this.secInfo.getLstObjects().add(objInfo);
        }
        refreshObjects();
    }

    protected void refreshObjects() {
        System.out.println("En refreshObjects ... ");
        for (final ObjectInfoBO objInfo : secInfo.getLstObjects()) {
            sourceText.setText(objInfo.getData());
            this.objInfo = objInfo;
            break;
        }
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        Utilerias.logger(getClass()).info("DropTarget dragExit");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        Utilerias.logger(getClass()).info("DropTarget dragOver, drop action = " + dtde.getDropAction());
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        Utilerias.logger(getClass()).info("DropTarget dropActionChanged, drop action = " + dtde.getDropAction());
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Utilerias.logger(getClass()).info("DropTarget drop, drop action = " + dtde.getDropAction());
        // Check the drop action
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            // Accept the drop and get the transfer data
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();
            try {
                boolean result = dropComponent(transferable);
                dtde.dropComplete(result);
                Utilerias.logger(getClass()).info("Drop completed, success: " + result);
            } catch (UnsupportedFlavorException | IOException e) {
                Utilerias.logger(getClass()).info("Exception while handling drop " + e);
                dtde.dropComplete(false);
            }
        } else {
            Utilerias.logger(getClass()).info("Drop target rejected drop");
            dtde.rejectDrop();
        }
    }

    // Internal methods start here
    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        Utilerias.logger(getClass()).info("\tSource actions are "
                + sourceActions + ", drop action is "
                + dropAction);
        return true;
    }

    protected void checkTransferType(DropTargetDragEvent dtde) {
        // Only accept a flavor that returns a Component
        acceptableType = false;
        DataFlavor[] fl = dtde.getCurrentDataFlavors();
        for (int i = 0; i < fl.length; i++) {
            Class dataClass = fl[i].getRepresentationClass();

            if (Component.class.isAssignableFrom(dataClass)) {
                // This flavor returns a Component - accept it.
                targetFlavor = fl[i];
                acceptableType = true;
                break;
            }
        }
        Utilerias.logger(getClass()).info("File type acceptable - " + acceptableType);
    }

    protected boolean dropComponent(Transferable transferable)
            throws IOException, UnsupportedFlavorException {
        Object o = transferable.getTransferData(targetFlavor);
        if (o instanceof Component) {
            Utilerias.logger(getClass()).info("Dragged component class is "
                    + o.getClass().getName());
            this.add((Component) o);
            this.validate();
            return true;
        }
        return false;
    }

   

    public void actualizaText() 
    {
        //System.out.println("refreshObjects");
        for (ObjectInfoBO objInfo : this.secInfo.getLstObjects()) {
            if (objInfo.getObjectId() == objectId) {
                objInfo.setData(sourceText.getText());
                objInfo.setPlain_text(Utilerias.html2text(sourceText.getText()));
                objInfo.setObjType(GlobalDefines.OBJ_TYPE_BULLET);
                objInfo.setSectionId(this.secInfo.getSectionId());
              //  objInfo.setTemplateId(this.secInfo.getTemplateId());
                //  objInfo.setSectionRootId(this.secInfo.getSectionIdModule());
                break;  
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        popSection = new javax.swing.JPopupMenu();
        popObject = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
                jMenuItem1ActionPerformed(evt);
            }
        });
        popObject.add(jMenuItem1);

        jMenu1.setText("jMenu1");
        popObject.add(jMenu1);

        jMenuItem2.setText("jMenuItem2");
        popObject.add(jMenuItem2);

        jMenuItem3.setText("jMenuItem3");

        jScrollPane1.setViewportView(jTextPane1);

        jScrollPane2.setViewportView(jTextPane2);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>                        

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    // Variables declaration - do not modify                     
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JPopupMenu popObject;
    private javax.swing.JPopupMenu popSection;
    // End of variables declaration                   
    private JComboBox cboIdiomas;
    private boolean reconstruir;

    /**
     * @return the objectId
     */
    public int getObjectId() {
        return objectId;  
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
    
    /**
     * Metodo que maneja las reglas de:
     * Numero maximo de bullets
     * Numero maximo de caracteres por item de la lista
     * 
     */
    public void bulletUtil()
    {
        List<String> uList;
        String text = ekitCore.getTextPane().getText();//sourceText.getText();
        String stringList = "";
        /*reconstruir = false;
        
        
        if(isThereULists(text))
        {
            //System.out.println("Si hay una lista");
            uList =  parseHtmlCode(text, e);//getUList(text);
            //System.out.println("Los elementos son: "+uList.length);
            if(uList.size() > MAX_BULLETS)
            {
                JOptionPane.showMessageDialog(this, "Se alcanzo el número maximo de bullets.", "Atención.", JOptionPane.WARNING_MESSAGE);
                //manager.undo();
                String resVal = "<html><head></head><body><p style=\"margin-top: 0\"></p><ul>";
                for (int i = 0; i < MAX_BULLETS; i++) {
                    if(uList.size() <= i)
                        break;
                    
                    String liElem = uList.get(i);
                    if(liElem.length()>MAX_CHARACTERS)
                        liElem = liElem.substring(0, MAX_CHARACTERS);

                    resVal += "<li>" + liElem + "</li>";
                }
                resVal+="</ul><p style=\"margin-top: 0\">&#160;</p></body></html>";

                sourceText.setText(resVal);
                ekitCore.getTextPane().setText(resVal);
                actualizaText();
            }
            else
            {
                List<String> decodeList = new ArrayList<>();
                for(int i=0 ; i < uList.size() ; i++)
                {  
                    if(null != uList.get(i))
                    {
                        stringList = uList.get(i);
                        stringList = toOnlyText(stringList);   
                        
                        if(stringList.length()>MAX_CHARACTERS)
                        {
                            stringList = stringList.substring(0, MAX_CHARACTERS);
                            decodeList.add(stringList);
                            reconstruir = true;
                            //sourceText.setText(text);
                            
                        }else{
                            decodeList.add(stringList);
                        }
                    }  
                }
                
                if(reconstruir){
                    String resVal = "<html><head></head><body><p style=\"margin-top: 0\"></p><ul>";
                    for (int i = 0; i < MAX_BULLETS; i++) {
                        if(decodeList.size() <= i)
                            break;

                        resVal += "<li>"+decodeList.get(i)+"</li>";
                    }
                    resVal+="</ul><p style=\"margin-top: 0\"></p></body></html>";

                    sourceText.setText(resVal);
                    ekitCore.getTextPane().setText(resVal);
                    actualizaText();
                }
            }
        }
        else
        {*/
        //if(!isThereULists(text)){
            //if(e.getKeyCode() == KeyEvent.VK_ENTER){
            uList =  UtileriasPDF.parseToList_P_NoFormat(delCodeBulletCopy(text));//parseHtmlCode(delCodeBulletCopy(text));//getUList(text);
            String resVal = "<html><head></head><body>";
            for (int i = 0; i < uList.size(); i++) {

                String liElem = uList.get(i);
                /*if(liElem == null || liElem.trim().isEmpty())
                    continue;*/

                resVal += "<p style=\"margin-top: 0\">" + liElem + "</p>";
            }
            resVal+="</body></html>";

            sourceText.setText(resVal);
            ekitCore.getTextPane().setText(resVal);
            actualizaText();
            //}
        //}
    }
    
    private String delCodeBulletCopy(String bulletText){
        String regx[] = {"&#8226;", "&#61607;", "&#61656;", "&#61692;", "&#61558;"};

        for (String rx : regx) {
            bulletText = bulletText.replaceAll(rx, "");
        }
        
        return bulletText;
    }
    
    /**
     * ´Devuelve la lista de elementos de los bullets
     */
    public List<String> parseHtmlCode(String htmlCode){
        List<String> liElement = new ArrayList<>();
        try {
            StringReader srHCode = new StringReader(htmlCode);
            List<Element> lstElement = HTMLWorker.parseToList(srHCode, new StyleSheet());
            
            for(int k = 0; k < lstElement.size(); ++k){
                if (lstElement.get(k) instanceof com.itextpdf.text.List) {
                    com.itextpdf.text.List listaHTML = (com.itextpdf.text.List) lstElement.get(k);
                    if (listaHTML.isEmpty()) {
                        continue;
                    }
                    
                    for (Element li : listaHTML.getItems()) {
                        if(li.getChunks().isEmpty())
                            continue;
                        Chunk chuckItem = li.getChunks().get(0);
                        liElement.add( chuckItem.getContent() );
                    }
                }else{
                    Paragraph paragraph = new Paragraph((Paragraph) (Element) lstElement.get(k));
                    if(paragraph.isEmpty())
                        continue;

                    liElement.add( paragraph.getContent() );
                }
            }
        } catch (IOException e) {
            Utilerias.logger(getClass()).error(e);
            liElement.clear();
        }
        
        return liElement;
    }
    
    /**
     * Devuelve el numero de Items contenidos en una unsorted list de un html
     * 
     * @param html
     * @return 
     */
    public int howMamyItemsUListhas(String html)
    {
        int countItems = 0;

        String[] lis = StringUtils.substringsBetween(html, "<li>", "</li>");
            for (String li : lis) 
            {
                    System.out.println("li value:" + li); // good
                    countItems++;
            }

            return countItems;
    }
    
    public String[] getUList(String html)
    {
        String[] lis = StringUtils.substringsBetween(html, "<li>", "</li>");
        return lis;
    }
    
    public static boolean isThereULists(String html)
    {
        boolean isThere = false;

        String[] uls = StringUtils.substringsBetween(html, "<ul>", "</ul>");

        if(null != uls && uls.length > 0)  
        {
            isThere = true;
        }
        else
        {
            isThere = false;
        }

        return isThere;
    }
    
    /**
     * Devuelve el texto sin saltos de linea
     * @param s
     * @return 
     */
    public static String toOnlyText(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    //builder.append("&nbsp;");
                    builder.append("");
                    previousWasASpace = false;   
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }
    
    private Hashtable<Integer, List<String>> resPar;
    private boolean msjVisible;
    private void validaParrafos(boolean notificar){
        try{
            String text = delCodeBulletCopy(ekitCore.getTextPane().getText());
            List<String> uList = UtileriasPDF.parseToList_P_NoFormat(text);
            Hashtable<Integer, List<String>> respRP = new Hashtable<>(); 
            
            for(Integer key : resPar.keySet()){
                if(resPar.get(key) == null)
                    break;
                
                respRP.put(key, resPar.get(key));
            }
            resPar.clear();
            //longLett = 80 + longRow = 9 + lettBySpace = 661;
                   
            int noPara = 0;
            int idxPar = 0;
            for (String element : uList) {
                                
                if(element==null || element.trim().isEmpty()){
                    List<String> lstRen = new ArrayList<>();
                    lstRen.add("0");
                    lstRen.add("");
                    resPar.put(idxPar, lstRen);
                    noPara++;
                }else{
                    int longPara = element.trim().length();
                    int renPorPara = (int) longPara > longLett ? longPara / longLett : 1;
                    int residPara = longPara > longLett ? longPara % longLett : 0;

                    if(residPara > 0){
                        renPorPara++;
                    }
                    
                    noPara += renPorPara;
                    List<String> lstRen = new ArrayList<>();
                    lstRen.add(longPara + "");
                    lstRen.add(element.trim());
                    resPar.put(idxPar, lstRen);
                }
                 
                if(noPara > longRow){
                    //if(!msjVisible){
                        JOptionPane.showMessageDialog(null, "El texto excede los " + lettBySpace + " caracteres del espacio de bullets.");
                    /*    msjVisible = true;
                    }else{
                        msjVisible=false;
                    }*/
                        
                    //if(isPasteCtrlV){
                        String resVal = "<html><head></head><body>";
                        
                        if(respRP == null || respRP.size() <= 0)
                            resVal += "<p style=\"margin-top: 0\"> </p>";
                        
                        for(int i=0; i<respRP.size(); i++){
                            if(respRP.get(i) == null){
                                break;
                            }
                            
                            resVal += "<p style=\"margin-top: 0\">" + respRP.get(i).get(1) + "</p>";   
                        }
                        
                        resVal+="</body></html>";
                        resPar = respRP;
                        sourceText.setText(resVal);
                        ekitCore.getTextPane().setText(resVal);
                        actualizaText();
                    /*}else{
                        int i = 0;
                        String resVal = "<html><head></head><body>";
                        for (String resp : uList) {
                            try{
                                if( resp.trim().length() > longLett && i < respRP.size() && resp.trim().length() > Integer.parseInt(respRP.get(i).get(0)) ){
                                    //resVal += "<p style=\"margin-top: 0\">" + respRP.get(i).get(1) + "</p>"; //--original
                                    resVal += "<p style=\"margin-top: 0\">" + resp.substring(0, Integer.parseInt(respRP.get(i).get(0))) + "</p>";
                                    //Utilerias.undoEkitCore(ekitCore);
                                    //resPar = respRP;
                                    break;
                                }else{
                                    if(i >= respRP.size())
                                        resVal += "<p style=\"margin-top: 0\">" + resp.substring(0, longLett) + "</p>";
                                    else
                                        resVal += "<p style=\"margin-top: 0\">" + resp + "</p>";
                                }
                            }catch(Exception ei){
                                Utilerias.logger(getClass()).debug(ei);
                                resVal += "<p style=\"margin-top: 0\">" + resp + "</p>";
                            }
                            i++;
                        }
                        resVal+="</body></html>";
                        resPar = respRP;
                        sourceText.setText(resVal);
                        ekitCore.getTextPane().setText(resVal);
                        actualizaText();
                    }*/
                    
                    break;
                }
                
                idxPar++;
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }
    }
}
