/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.hexidec.ekit.EkitCore;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/**
 *
 * @author Desarrollador
 */
public class ScrCellText extends javax.swing.JPanel /*implements DropTargetListener*/ {

    /* Version modificada para formateador */
    private static final Logger LOG = Logger.getLogger(ScrCellText.class.getName());
    protected ModuleSectionBO secInfo;
    //protected DropTarget dropTarget;
    protected boolean acceptableType; // Indicates whether data is acceptable
    //protected DataFlavor targetFlavor; // Flavor to use for transfer     
    protected ScrDocument parentDoc;
    protected ObjectInfoBO objInfo;
    private int objectId = 0;

    private EkitCore ekitCore;
    private JTextPane sourceText;
    private JTextArea jTextArea;
    private String plainText;

    private JButton btnConcat;
    private JButton btnDesconcat;
    private JButton btnIniciaConcat;
    private JButton btnWarnConcat;
    private int id;
    private int maxLetrasXRenglon;
    private int maxRenglones;
    private int numMaxCaracteres;
    private int numCaracActuales;
    private boolean isPasteCtrlV;
    private JPopupMenu popup;
    private ModuleBO moduleBO;

    /**
     * Creates new form ScrCellText
     *
     * @param scrDoc
     * @param secInfo
     */
    public ScrCellText(ScrDocument scrDoc, ModuleSectionBO secInfo, ModuleBO moduleBO) {
        this.parentDoc = scrDoc;
        this.secInfo = secInfo;
        this.moduleBO=moduleBO;
        initComponents();
        initTextEditor();
        initTextPaneListener();

        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<ObjectInfoBO>());
        }

        initScreen();
        addObject(GlobalDefines.OBJ_TYPE_TEXT);
        cboIdiomasActionPerformed(null);
        parentDoc.setContObj(parentDoc.getContObj() + 1);
        
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        for (ScrCellText srcCellText : lista) {
            srcCellText.txtRespaldo = null;
            srcCellText.actExcedente = true;
        }
    }

    private void initTextEditor() {
        try {
            Vector<String> v = Utilerias.getOptionsToEkitcore(Utilerias.EKIT_CORE_TYPE.TEXT);
            ekitCore = Utilerias.getEkitCore();
            
            sourceText = ekitCore.getTextPane();
            jTextArea = ekitCore.getSourcePane();
            add(ekitCore, BorderLayout.CENTER);
            sourceText.addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    sourceTextFocusLost(evt);
                }
            });
            
            sourceText.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (moduleBO.isHeaderOrSection() || moduleBO.isSection()) {
                        return ;
                    }
                    
                    if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                        isPasteCtrlV = true;
                    } else if(e.getKeyCode() == KeyEvent.VK_Z && isPasteCtrlV){
                        Utilerias.undoEkitCore(ekitCore);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (moduleBO.isHeaderOrSection() || moduleBO.isSection()) {
                        return ;
                    }
                    
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL){
                        isPasteCtrlV = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
                        String resVal = sourceText.getText();
                        if(!resVal.contains("</p>")){
                            resVal = resVal.replaceAll("<body>", "<body><p style=\"margin-top: 0\">");
                            resVal = resVal.replaceAll("</body>", "</p></body>");
                            sourceText.setText(resVal);
                            ekitCore.getTextPane().setText(resVal);
                            actualizaText();
                        }
                    }
                }
                
                
            });
            
            sourceText.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    String resVal = sourceText.getText();
                    if(!resVal.contains("</p>")){
                        resVal = resVal.replaceAll("<body>", "<body><p style=\"margin-top: 0\">");
                        resVal = resVal.replaceAll("</body>", "</p></body>");
                        sourceText.setText(resVal);
                        ekitCore.getTextPane().setText(resVal);
                        actualizaText();
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
                                addToCollaborativePop();
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
             
            initButtons();
            cboIdiomas = new JComboBox();
            cboIdiomas.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Sin corrección", "Español", "Inglés"}));
            cboIdiomas.setSelectedIndex(1);
            cboIdiomas.setSize(40, 20);
            cboIdiomas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cboIdiomasActionPerformed(e);
                }
            });
            if (!this.moduleBO.isHeaderOrSection() && !this.moduleBO.isSection()) {
                Utilerias.addEkitcoreLayout(this, ekitCore, v, cboIdiomas, btnWarnConcat, btnIniciaConcat, btnConcat, btnDesconcat);
            } else {
                sourceText.setEditable(false);
                sourceText.setEnabled(false);
            }
//            if (v == null || v.isEmpty()) {
//                addAllToolBars();
//            } else {
//                addToolBars(v);
//            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    protected void addToCollaborativePop() {
        //this.parentDoc.addModuleColaborative(this.secInfo.getModuleId());
        if( this.moduleBO.getDocumentModuleId()<=0 ) {       
            JOptionPane.showMessageDialog(this, "El módulo debe ser guardado para que se pueda agregar al documento colaborativo");
            return;         
        }         
        this.parentDoc.addModuleColaborative(this.moduleBO.getDocumentModuleId() );
    }

    public static synchronized String toPlainText(String s) {
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
    
    private String txt2HTML(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasEnter = false;
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\n':
                    builder.append("</p><p style=\"margin-top: 0\">");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("   ");
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

    private void sourceTextFocusLost(java.awt.event.FocusEvent evt) {
        actualizaText();
    }

    private void cboIdiomasActionPerformed(java.awt.event.ActionEvent evt) {
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
            LOG.log(Level.FINE, ex.getMessage());
        }
    }

    
    public final void initScreen() {
        popSection = new JPopupMenu();
        popSection.setBorder(new BevelBorder(BevelBorder.RAISED));
        JMenuItem menuItem = new JMenuItem("Eliminar módulo");
        popSection.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteModule();
            }
        });

        //popSection.addSeparator();

        JMenuItem menuItemCol = new JMenuItem("Agregar módulo a colaborativo");
        popSection.add(menuItemCol);
        menuItemCol.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addToCollaborative();
            }
        });

        /*
         menuItem = new JMenuItem("Pegar");
         popSection.add(menuItem);
         */
        jTextArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    popSection.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
        //initButtons();
        refreshObjects();
    }
    
    public boolean actDesc = false;

    public void addToCollaborative() {
        if (JOptionPane.showConfirmDialog(
                this, "Agregar modulo a colaborativo? ", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            int idSelectedModule = this.secInfo.getModuleId();
            JOptionPane.showConfirmDialog(this, idSelectedModule);
        }
    }
    

//    public void setDrag() {
//        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
//                this, true, null);
//        this.setDropTarget(dropTarget);
//    }

    public void deleteModule() {

        this.parentDoc.deleteModule(this.secInfo.getSectionParentId());
    }

    //**inicio dnd
    // Implementation of the DropTargetListener interface
//    @Override
//    public void dragEnter(DropTargetDragEvent dtde) {
//        Utilerias.logger(getClass()).info("dragEnter, drop action = " + dtde.getDropAction());
//        // Get the type of object being transferred and determine
//        // whether it is appropriate.
//        checkTransferType(dtde);
//
//        // Accept or reject the drag.
//        acceptOrRejectDrag(dtde);
//    }

    public void deletObject(int objectId) {
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

    protected void addObject(int objectType) {
        objInfo = new ObjectInfoBO();
        boolean existe = false;
        for (ObjectInfoBO objInfo : this.secInfo.getLstObjects()) {
            objectId = objInfo.getObjectId();
            //setObjectId(objInfo.getObjectId());
            objInfo.setObjectId(objInfo.getObjectId());
            //String txtSinHtml = Utilerias.html2text(objInfo.getPlain_text());
            objInfo.setPlain_text(objInfo.getPlain_text());
            //String txtSinHtml = Utilerias.html2text(objInfo.getData());
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
            //objInfo.setData(objInfo.getData() != null ? Utilerias.html2text(objInfo.getData()) : objInfo.getData());
            objInfo.setData(objInfo.getData());
            objInfo.setPlain_text(objInfo.getPlain_text());

            this.secInfo.getLstObjects().add(objInfo);
        }
        actDesc = true;
        refreshObjects();
    }
    
    private boolean recargaTexto = true;

    protected void refreshObjects() {
        for (final ObjectInfoBO objInfo : secInfo.getLstObjects()) {
            //try {
                if (recargaTexto) {
                    sourceText.setText(objInfo.getData());
                    //sourceText.getDocument().insertString(0, objInfo.getData(), null);
                    
                }
            //} catch (BadLocationException ex) {
            //    Logger.getLogger(ScrCellText.class.getName()).log(Level.SEVERE, null, ex);
            //}
            this.objInfo = objInfo;
            break;
        }
        detectarDesconc();
    }

//    @Override
//    public void dragExit(DropTargetEvent dte) {
//        Utilerias.logger(getClass()).info("DropTarget dragExit");
//    }
//
//    @Override
//    public void dragOver(DropTargetDragEvent dtde) {
//        Utilerias.logger(getClass()).info("DropTarget dragOver, drop action = " + dtde.getDropAction());
//        // Accept or reject the drag
//        acceptOrRejectDrag(dtde);
//    }
//
//    @Override
//    public void dropActionChanged(DropTargetDragEvent dtde) {
//        Utilerias.logger(getClass()).info("DropTarget dropActionChanged, drop action = " + dtde.getDropAction());
//        // Accept or reject the drag
//        acceptOrRejectDrag(dtde);
//    }
//
//    @Override
//    public void drop(DropTargetDropEvent dtde) {
//        Utilerias.logger(getClass()).info("DropTarget drop, drop action = " + dtde.getDropAction());
//        // Check the drop action
//        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
//            // Accept the drop and get the transfer data
//            dtde.acceptDrop(dtde.getDropAction());
//            Transferable transferable = dtde.getTransferable();
//            try {
//                boolean result = dropComponent(transferable);
//                dtde.dropComplete(result);
//                Utilerias.logger(getClass()).info("Drop completed, success: " + result);
//            } catch (UnsupportedFlavorException | IOException e) {
//                Utilerias.logger(getClass()).info("Exception while handling drop " + e);
//                dtde.dropComplete(false);
//            }
//        } else {
//            Utilerias.logger(getClass()).info("Drop target rejected drop");
//            dtde.rejectDrop();
//        }
//    }

    // Internal methods start here
    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        Utilerias.logger(getClass()).info("\tSource actions are "
                + sourceActions + ", drop action is "
                + dropAction);
        return true;
    }

//    protected void checkTransferType(DropTargetDragEvent dtde) {
//        // Only accept a flavor that returns a Component
//        acceptableType = false;
//        DataFlavor[] fl = dtde.getCurrentDataFlavors();
//        for (int i = 0; i < fl.length; i++) {
//            Class dataClass = fl[i].getRepresentationClass();
//
//            if (Component.class.isAssignableFrom(dataClass)) {
//                // This flavor returns a Component - accept it.
//                targetFlavor = fl[i];
//                acceptableType = true;
//                break;
//            }
//        }
//        Utilerias.logger(getClass()).info("File type acceptable - " + acceptableType);
//    }
//
//    protected boolean dropComponent(Transferable transferable)
//            throws IOException, UnsupportedFlavorException {
//        Object o = transferable.getTransferData(targetFlavor);
//        if (o instanceof Component) {
//            Utilerias.logger(getClass()).info("Dragged component class is "
//                    + o.getClass().getName());
//            this.add((Component) o);
//            this.validate();
//            return true;
//        }
//        return false;
//    }

    public void actualizaText() {
        for (ObjectInfoBO objInfoActText : this.secInfo.getLstObjects()) {
            if (objInfoActText.getObjectId() == objectId) {
                //try {
                    //objInfoActText.setData(sourceText.getDocument().getText(0, sourceText.getDocument().getLength()));
                    objInfoActText.setData(sourceText.getText());
                    //String txtSinHtml = Utilerias.html2text(sourceText.getText());
                    objInfoActText.setPlain_text(sourceText.getText());
                    objInfoActText.setObjType(GlobalDefines.OBJ_TYPE_TEXT);
                    objInfoActText.setSectionId(this.secInfo.getSectionId());
                    //  objInfo.setTemplateId(this.secInfo.getTemplateId());
                    //  objInfo.setSectionRootId(this.secInfo.getSectionIdModule());
                    break;
                //} catch (BadLocationException ex) {
                //    Logger.getLogger(ScrCellText.class.getName()).log(Level.SEVERE, null, ex);
                //}
            }
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
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
    private JComboBox cboIdiomas;

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

    private void initButtons() {

        btnConcat = new JButton();
        btnConcat.setRequestFocusEnabled(false);
        btnConcat.setBackground(new java.awt.Color(255, 255, 255));
        btnConcat.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnConcat.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.CONCAT));
        btnConcat.setToolTipText(Utilerias.ICONS.CONCAT.getCaption());
        btnConcat.setEnabled(false);

        btnDesconcat = new JButton();
        btnDesconcat.setRequestFocusEnabled(false);
        btnDesconcat.setBackground(new java.awt.Color(255, 255, 255));
        btnDesconcat.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnDesconcat.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.DESCONCAT));
        btnDesconcat.setToolTipText(Utilerias.ICONS.DESCONCAT.getCaption());
        btnDesconcat.setEnabled(false);

        btnIniciaConcat = new JButton();
        btnIniciaConcat.setRequestFocusEnabled(false);
        btnIniciaConcat.setBackground(new java.awt.Color(255, 255, 255));
        btnIniciaConcat.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnIniciaConcat.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.INICIA_CONCAT));
        btnIniciaConcat.setToolTipText(Utilerias.ICONS.INICIA_CONCAT.getCaption());
        btnIniciaConcat.setEnabled(false);
        
        btnWarnConcat = new JButton();
        btnWarnConcat.setRequestFocusEnabled(false);
        btnWarnConcat.setBackground(new java.awt.Color(255, 255, 255));
        btnWarnConcat.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnWarnConcat.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.CONCAT_WARN));
        btnWarnConcat.setToolTipText(Utilerias.ICONS.CONCAT_WARN.getCaption());
        btnWarnConcat.setEnabled(false);
        
    }

    private StringBuilder sbTextOK;
    private StringBuilder sbTextExced;
    public boolean actExcedente = true;

    public boolean isActExcedente() {
        return actExcedente;
    }

    public void setActExcedente(boolean actExcedente) {
        this.actExcedente = actExcedente;
    }
    final Clipboard clipboard = getToolkit().getSystemClipboard();

    private List<ScrCellText> lista = new ArrayList<>();
    
    private int maxScroll;
    public int posCursor;
    public int numEnters;
    public boolean actScroll = true;
    private boolean reajustar = false;
    private boolean precedeEnter;
    private boolean dobleEnter;
    public boolean seguirAct;
    

    private void initTextPaneListener() {
        sbTextOK = new StringBuilder();
        sbTextExced = new StringBuilder();

        JViewport viewport = (JViewport) sourceText.getParent();
        javax.swing.JScrollPane scroll = (javax.swing.JScrollPane) viewport.getParent();
        
        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (maxScroll != e.getAdjustable().getMaximum()){
                    maxScroll = e.getAdjustable().getMaximum();
                    actExcedente = true;
                }
            }
        });

        sourceText.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent event) {
                parentDoc.keyPressed = event.getKeyChar();
                posCursor = sourceText.getCaretPosition();
                if (parentDoc.keyPressed == 65535 ||
                    parentDoc.keyPressed == 32) {
                    return;
                }
                
                if (parentDoc.keyPressed == 10) {
                    numEnters++;
                }
                
                if ((parentDoc.keyPressed == 8 || parentDoc.keyPressed == 127) && sourceText.getDocument().getLength() < 2){
                    sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                    numEnters = 0;
                    actScroll = true;
                    txtRespaldo = null;
                    reajustarConcat();
                    vieneDeReajuste = true;
                    detectarExcedente();
                    vieneDeReajuste = false;
                    if (parentDoc.idOtrosMas != null && !parentDoc.idOtrosMas.isEmpty() && sbTextExced != null) {
                        parentDoc.strExcReajuste = new StringBuilder();
                        lista = new ArrayList<>();
                        obtenScrCellText(parentDoc);
                        iniciaConcatListener(null);
                        while (parentDoc.idOtrosMas != null && !parentDoc.idOtrosMas.isEmpty()){
                            String idConReaj = parentDoc.idOtrosMas.get(0);
                            int i = 0;
                            boolean encontrado = false;
                            for (ScrCellText srcCellText : lista) {
                                String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                                if (idConReaj.equals(parentDoc.getHojaAct() + "-" + idGen2)) {
                                    srcCellText.sourceText.setText("");
                                    srcCellText.sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                                    if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty()) {
                                        boolean borrar = true;
                                        for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                                            if (conc.getValue() != null) {
                                                borrar = false;
                                                break;
                                            }
                                        }
                                        if (borrar) {
                                            parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen2);
                                            srcCellText.btnDesconcat.setEnabled(false);
                                        }
                                    } else {
                                        parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen2);
                                        srcCellText.btnDesconcat.setEnabled(false);
                                    }
                                    srcCellText.concatListener(null);
                                    srcCellText.vieneDeReajuste = true;
                                    srcCellText.detectarExcedente();
                                    srcCellText.vieneDeReajuste = false;
                                    if (srcCellText.sbTextExced != null && !srcCellText.sbTextExced.toString().isEmpty() &&
                                            (parentDoc.idOtrosMas.size() > 1 || (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()))) {
                                        parentDoc.strExcReajuste = srcCellText.sbTextExced;
                                        srcCellText.iniciaConcatListener(null);
                                    } else {
                                        parentDoc.strExcReajuste = new StringBuilder();
                                    }
                                    encontrado = true;
                                    break;
                                }
                                i++;
                            }
                            
                            if (encontrado) {
                                parentDoc.idOtrosMas.remove(parentDoc.idOtrosMas.get(0));
                            } else {
                                actualizaConcatTodos();
                                break;
                            }
                            
                        }
                    } else {
                        parentDoc.idOtrosMas = new ArrayList();
                    }
                    desactivaBtnsConcat();
                    habilitarTodosDesc();
                } else {
                    reajustarConcat();
                    vieneDeReajuste = true;
                    detectarExcedente();
                    //vieneDeReajuste = false;
                    if (parentDoc.idOtrosMas != null && !parentDoc.idOtrosMas.isEmpty() && sbTextExced != null) {
                        parentDoc.strExcReajuste = new StringBuilder();
                        lista = new ArrayList<>();
                        obtenScrCellText(parentDoc);
                        iniciaConcatListener(null);
                        while (parentDoc.idOtrosMas != null && !parentDoc.idOtrosMas.isEmpty()){
                            String idConReaj = parentDoc.idOtrosMas.get(0);
                            int i = 0;
                            boolean encontrado = false;
                            for (ScrCellText srcCellText : lista) {
                                String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                                if (idConReaj.equals(parentDoc.getHojaAct() + "-" + idGen2)) {
                                    srcCellText.sourceText.setText("");
                                    srcCellText.sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                                    if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty()) {
                                        boolean borrar = true;
                                        for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                                            if (conc.getValue() != null) {
                                                borrar = false;
                                                break;
                                            }
                                        }
                                        if (borrar) {
                                            parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen2);
                                            srcCellText.btnDesconcat.setEnabled(false);
                                        }
                                    } else {
                                        parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen2);
                                        srcCellText.btnDesconcat.setEnabled(false);
                                    }
                                    srcCellText.concatListener(null);
                                    srcCellText.vieneDeReajuste = true;
                                    srcCellText.detectarExcedente();
                                    srcCellText.vieneDeReajuste = false;
                                    if (srcCellText.sbTextExced != null && !srcCellText.sbTextExced.toString().isEmpty() &&
                                            (parentDoc.idOtrosMas.size() > 1 || (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()))) {
                                        parentDoc.strExcReajuste = srcCellText.sbTextExced;
                                        srcCellText.iniciaConcatListener(null);
                                    } else {
                                        parentDoc.strExcReajuste = new StringBuilder();
                                    }
                                    encontrado = true;
                                    break;
                                }
                                i++;
                            }
                            
                            if (encontrado) {
                                parentDoc.idOtrosMas.remove(parentDoc.idOtrosMas.get(0));
                            } else {
                                actualizaConcatTodos();
                                break;
                            }
                            
                        }
                    } else {
                        parentDoc.idOtrosMas = new ArrayList();
                        if (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()) {
                            if (sbTextExced != null && !sbTextExced.toString().isEmpty()) {
                                parentDoc.strExcReajuste = sbTextExced;
                                lista = new ArrayList<>();
                                obtenScrCellText(parentDoc);
                                vieneDeReajuste = true;
                                iniciaConcatListener(null);
                            } else {
                                parentDoc.strExcReajuste = new StringBuilder();
                            }
                        }
                    }
                    desactivaBtnsConcat();
                    habilitarTodosDesc();
                } /*else {
                    numEnters = 0;
                    actScroll = true;
                    detectarExcedente();
                }*/
                actExcedente = true;

                try {
                    int numCar = sourceText.getDocument().getText(0, sourceText.getDocument().getLength()).length();
                    if (numCar > posCursor) {
                        sourceText.setCaretPosition(posCursor);
                    }
                    
                } catch (BadLocationException ex) {
                    Logger.getLogger(ScrCellText.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void keyTyped(java.awt.event.KeyEvent event) {
                parentDoc.keyPressed = event.getKeyChar();
                if (parentDoc.keyPressed == 10) {
                    actExcedente = true;
                    actScroll = false;
                }
            }
        });

        btnIniciaConcat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciaConcatListener(e);
                actualizaConcat();
            }
        });

        btnConcat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concatListener(e);
                detectarExcedente();
            }
        });

        btnDesconcat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desconcatListener(e);
            }
        });
    }
    
    public boolean vieneDeReajuste;
    public boolean reajustado;
    
    private void reajustarConcat() {
        actualizaConcatTodos();
        int i = 0;
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        for (ScrCellText srcCellText : lista) {
            if (this == srcCellText) {
                break;
            }
            i++;
        }

        String idGen = parentDoc.getHojaAct() + "-" + secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + i;

        @SuppressWarnings("element-type-mismatch")
        Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();

        if (mapConcat == null) {
            return;
        }
        
        reajustado = true;

        String txtRegresar = "";
        String idDestino = "";
        for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
            Map<String, StringBuilder> mapConcatOrig = ent.getValue();
            if (mapConcatOrig.containsKey(idGen)) {
                idDestino = ent.getKey();
                txtRegresar = mapConcatOrig.get(idGen).toString();
                break;
            }
        }
        
        if (idDestino.isEmpty()) {
            if (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()) {
                idDestino = parentDoc.idOtrosMasOtraHoja.get(0);
            } else {
                return;
            }
        }
        
        String strEntExt = "";
        
        while (txtRegresar.startsWith("\n")) {
            txtRegresar = txtRegresar.substring(1);
            if (txtRegresar.startsWith("&")) {
                txtRegresar = txtRegresar.substring(1);
                if (txtRegresar.startsWith("&")) {
                    strEntExt = strEntExt + "</p><p style=\"margin-top: 0\">";
                } else {
                    String align = txtRegresar.substring(0, txtRegresar.indexOf("&"));
                    strEntExt = strEntExt + "</p><p align=\"" + align + "\" style=\"margin-top: 0\">";
                }
                txtRegresar = txtRegresar.substring(txtRegresar.indexOf("&")+1);
            } else {
                strEntExt = strEntExt + "</p><p style=\"margin-top: 0\">";
            }
        }

        if (!strEntExt.isEmpty()){
            txtRegresar = strEntExt + txtRegresar;
        }
        
        boolean buscar = true;
        parentDoc.idOtrosMas = new ArrayList();
        while (buscar) {
            buscar = false;
            for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                if (mapConcatOrig.containsKey(idDestino)) {
                    String strEnt = "";
                    String aux = mapConcatOrig.get(idDestino).toString();
                    parentDoc.idOtrosMas.add(idDestino);
                    while (aux.startsWith("\n")) {
                        aux = aux.substring(1);
                        if (aux.startsWith("&")) {
                            aux = aux.substring(1);
                            if (aux.startsWith("&")) {
                                strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                            } else {
                                String align = aux.substring(0, aux.indexOf("&"));
                                strEnt = strEnt + "</p><p align=\"" + align + "\" style=\"margin-top: 0\">";
                            }
                            aux = aux.substring(aux.indexOf("&")+1);
                        } else {
                            strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                        }
                    }
                    
                    if (strEnt.isEmpty()){
                        aux = " ".concat(aux);
                    } else {
                        aux = strEnt + aux;
                    }
                    
                    idDestino = ent.getKey();
                    buscar = true;
                    txtRegresar = txtRegresar + aux;
                    break;
                }
            }
        }
        
        if (parentDoc.strExcReajuste != null && !parentDoc.strExcReajuste.toString().isEmpty()) {
            String strEnt = "";
            String aux = parentDoc.strExcReajuste.toString();
            while (aux.startsWith("\n")) {
                aux = aux.substring(1);
                if (aux.startsWith("&")) {
                    aux = aux.substring(1);
                    if (aux.startsWith("&")) {
                        strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                    } else {
                        String align = aux.substring(0, aux.indexOf("&"));
                        strEnt = strEnt + "</p><p align=\"" + align + "\" style=\"margin-top: 0\">";
                    }
                    aux = aux.substring(aux.indexOf("&")+1);
                } else {
                    strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                }
            }

            if (strEnt.isEmpty()){
                aux = " ".concat(aux);
            } else {
                aux = strEnt + aux;
            }
            
            txtRegresar = txtRegresar + aux;
            parentDoc.strExcReajuste = null;
        }

        for (String idMas : parentDoc.idOtrosMas) {
            i = 0;
            boolean encontrado = false;
            for (ScrCellText srcCellText : lista) {
                String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                if (idMas.equals(parentDoc.getHojaAct() + "-" + idGen2)) {
                    srcCellText.getSourceText().setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                    srcCellText.btnIniciaConcat.setEnabled(false);
                    srcCellText.btnWarnConcat.setEnabled(false);
                    srcCellText.btnDesconcat.setEnabled(false);
                    srcCellText.sbTextExced = null;
                    srcCellText.reajustado = true;
                    srcCellText.actualizaText();
                    encontrado = true;
                    break;
                }
                i++;
            }
            
            if (!encontrado) {
                if (parentDoc.idOtrosMasOtraHoja == null) {
                    parentDoc.idOtrosMasOtraHoja = new ArrayList<>();
                }
                
                if (!parentDoc.idOtrosMasOtraHoja.contains(idMas)) {
                    parentDoc.idOtrosMasOtraHoja.add(idMas);
                }
            }
            
            parentDoc.getObjetosConc().remove(idMas);
            
        }
        
        if (txtRegresar == null || txtRegresar.isEmpty()) {
            return;
        }
        
        String allTExt = sourceText.getText();
        int posIns = 0;
        
        i = 0;
        
        boolean existe = false;
        for (ScrCellText srcCellText : lista) {
            String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
            if (idDestino.equals(parentDoc.getHojaAct() + "-" + idGen2)) {
                srcCellText.getSourceText().setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                srcCellText.btnDesconcat.setEnabled(false);
                srcCellText.btnIniciaConcat.setEnabled(false);
                srcCellText.btnWarnConcat.setEnabled(false);
                srcCellText.sbTextExced = null;
                srcCellText.actualizaText();
                existe = true;
                break;
            }
            i++;
        }
        
        if (!existe) {
            if (parentDoc.idOtrosMasOtraHoja ==  null) {
                parentDoc.idOtrosMasOtraHoja = new ArrayList<>();
            }
            if (!parentDoc.idOtrosMasOtraHoja.contains(idDestino)) {
                parentDoc.idOtrosMasOtraHoja.add(idDestino);
            }
        } else {
            parentDoc.idOtrosMas.add(idDestino);
        }
        
        txtRespaldo = null;
        
        posIns = allTExt.lastIndexOf("</p>");
        
        if (posIns < 0) {
            allTExt = allTExt.replace("<body>", "<body><p style=\"margin-top: 0\">");
            allTExt = allTExt.replace("</body>", "</p></body>");
            posIns = allTExt.lastIndexOf("</p>");
        }
        
        sourceText.setText(allTExt.substring(0, posIns) + txtRegresar + allTExt.substring(posIns));
        actExcedente = true;
        actScroll = true;
        parentDoc.keyPressed = 0;
        
        if (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()) {
            parentDoc.idOtrosMas.removeAll(parentDoc.idOtrosMasOtraHoja);
        }
        
        parentDoc.getObjetosConc().remove(idDestino);
        //parentDoc.getObjetosConc().remove("POST");
        actExcedente = true;
        reajustar = true;

        //detectarDesconc();
        actualizaText();
    }

    private void iniciaConcatListener(ActionEvent evt) {
        parentDoc.keyPressed = 0;
        txtRespaldo = null;
        if (!vieneDeReajuste) {
            detectarExcedente();
        } else {
            vieneDeReajuste = false;
        }
        activaDesactivaBntConcat();
        desactivaBtnsInicia();
        detectarExcedente();
        btnConcat.setEnabled(false);
        actualizaText();
    }

    private void concatListener(ActionEvent evt) {
        String textoConcatenar = "";
        String idOrigen = "";
        if (parentDoc != null && parentDoc.getTextConc() != null) {
            String toRemove = "";
            for (Map.Entry<String, StringBuilder> ent : parentDoc.getTextConc().entrySet()){
                idOrigen = ent.getKey();
                if (ent.getValue() != null && !idOrigen.equals("POST-ENTER") && !idOrigen.equals("ALIGN")){
                    textoConcatenar = ent.getValue().toString();
                    break;
                } else if (!idOrigen.equals("PRE-ENTER") && !idOrigen.equals("PRE-DOBLE-ENTER") && !idOrigen.equals("POST-ENTER") && !idOrigen.equals("ALIGN")){
                    toRemove = idOrigen;
                }
            }

            parentDoc.getTextConc().remove(toRemove);
        }
        String alignHtml = "";
        String align = "";
        if (parentDoc.getTextConc().containsKey("ALIGN")) {
            alignHtml = " align=\"" + parentDoc.getTextConc().get("ALIGN") + "\"";
            align = parentDoc.getTextConc().get("ALIGN").toString();
            parentDoc.getTextConc().remove("ALIGN");
        }

        String allText = sourceText.getText();
        sourceText.setText("");
        sourceText.setText(allText.replace("<body>", "<body><p" + alignHtml + " style=\"margin-top: 0\">" + textoConcatenar + "</p>"));

        desactivaBtnsConcat();
        desactivaBtnsDesconcat();

        btnConcat.setEnabled(false);
        if (idOrigen != null && !idOrigen.isEmpty()) {
            btnDesconcat.setEnabled(true);
        }

        if (parentDoc.getObjetosConc() == null) {
            parentDoc.setObjetosConc(new HashMap<>());
        }
        int i = 0;
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);

        for (ScrCellText srcCellText : lista) {
            if (this == srcCellText) {
                break;
            }
            i++;
        }

        String idGen = secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + i;

        Map<String, StringBuilder> mOrigen = new HashMap<>();
        int numPost = 0;
        if (parentDoc.getTextConc().containsKey("POST-ENTER")) {
            numPost = Integer.valueOf(parentDoc.getTextConc().get("POST-ENTER").toString());
            parentDoc.getTextConc().remove("POST-ENTER");
        }
        if (parentDoc.getTextConc().containsKey("PRE-DOBLE-ENTER")) {
            mOrigen.put(idOrigen, new StringBuilder().append("\n\n").append("&").append(align).append("&").append(textoConcatenar));
            parentDoc.getTextConc().remove("PRE-DOBLE-ENTER");
        } else if (parentDoc.getTextConc().containsKey("PRE-ENTER")) {
            if (numPost > 0) {
                mOrigen.put(idOrigen, new StringBuilder().append("\n\n").append("&").append(align).append("&").append(textoConcatenar));
            } else {
                mOrigen.put(idOrigen, new StringBuilder().append("\n").append("&").append(align).append("&").append(textoConcatenar));
            }
            parentDoc.getTextConc().remove("PRE-ENTER");
        } else {
            if (numPost > 0) {
                mOrigen.put(idOrigen, new StringBuilder().append("\n\n").append("&").append(align).append("&").append(textoConcatenar));
            } else {
               mOrigen.put(idOrigen, new StringBuilder().append(textoConcatenar));
            }
        }

        if (idOrigen != null && !idOrigen.isEmpty()){
            parentDoc.getObjetosConc().put(parentDoc.getHojaAct() + "-" + idGen, mOrigen);
            parentDoc.getTextConc().remove(idOrigen);
            parentDoc.getTextConc().remove("PRE");
            //parentDoc.getTextConc().put("POST", null);
        }
        
        actualizaText();
        actualizaText();
        habilitarTodosDesc();
        activaBtnsInicia();
    }
     
    private void habilitarTodosDesc() {
        if (parentDoc.getObjetosConc() != null) {
            lista = new ArrayList<>();
            obtenScrCellText(parentDoc);
            int i = 0;
            for (ScrCellText srcCellText : lista) {
                String idGen = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                if (parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                    boolean ultimo = true;
                    Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();
                    for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                        Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                        if (mapConcatOrig.containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                            ultimo = false;
                            break;
                        }
                    }
                    
                    if (ultimo) {
                        if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty() && 
                            parentDoc.idOtrosMasOtraHoja != null && parentDoc.idOtrosMasOtraHoja.contains(parentDoc.getHojaAct() + "-" + idGen)) {
                            for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                                if (conc.getValue() != null) {
                                    ultimo = true;
                                    break;
                                }
                            }
                        }
                    }
                    srcCellText.btnDesconcat.setEnabled(ultimo);
                }
                i++;
            }
        }
    }

    private void desconcatListener(ActionEvent evt) {
            txtRespaldo = null;
            int i = 0;
            lista = new ArrayList<>();
            obtenScrCellText(parentDoc);
            for (ScrCellText srcCellText : lista) {
                if (this == srcCellText) {
                    break;
                } 
                i++;
            }

            String idGen = parentDoc.getHojaAct() + "-" + secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + i;
            
            @SuppressWarnings("element-type-mismatch")
            Map<String, StringBuilder> mapOrigen = parentDoc.getObjetosConc().get(idGen);
            
            if (mapOrigen == null) {
                return;
            }
            
            String txtRegresar = "";
            String idOrigen = "";
            String toRemove = "";
            for (Map.Entry<String, StringBuilder> ent :mapOrigen.entrySet()){
                idOrigen = ent.getKey();
                if (ent.getValue() != null){
                    txtRegresar = ent.getValue().toString();
                    break;
                } else {
                    toRemove = idOrigen;
                }
                
                mapOrigen.remove(toRemove);
            }
            
            
            String strNvoEx = "";
            
            /*if (reajustado) {
                String txtObjAct = quitarEntTxt(sourceText.getText());
                txtObjAct = txtObjAct.replace("<b></b>", "");
                txtRegresar = txtObjAct.substring(txtObjAct.indexOf("<p style=\"margin-top: 0\">"), txtObjAct.indexOf("</body>"));
                sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                reajustado = false;
            } else {
               
            }*/
            
            i = 0;
            boolean origenEncontrado = false;
            for (ScrCellText srcCellText : lista) {
                String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                if (idOrigen.equals(parentDoc.getHojaAct() + "-" + idGen2)) {
                    srcCellText.txtRespaldo = null;
                    String allTExt = quitarEntTxt(srcCellText.getSourceText().getText());
                    

                    String txtRegAux = "";
                    String strEnt = "";
                    txtRegAux = txtRegresar;
                    while (txtRegAux.startsWith("\n")) {
                        txtRegAux = txtRegAux.substring(1);
                        if (txtRegAux.startsWith("&")) {
                            txtRegAux = txtRegAux.substring(1);
                            if (txtRegAux.startsWith("&")) {
                                strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                            } else {
                                String align = txtRegAux.substring(0, txtRegAux.indexOf("&"));
                                strEnt = strEnt + "</p><p align=\"" + align + "\" style=\"margin-top: 0\">";
                            }
                            txtRegAux = txtRegAux.substring(txtRegAux.indexOf("&")+1);
                        } else {
                            strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                        }
                    }

                    txtRegAux = quitarEntTxt(sourceText.getText());
                    if (txtRegAux.indexOf("style=\"margin-top: 0\">") > 0) {
                        txtRegAux = strEnt + txtRegAux.substring(txtRegAux.indexOf("style=\"margin-top: 0\">") + 22, txtRegAux.lastIndexOf("</p>"));
                    } else {
                        txtRegAux = strEnt + txtRegAux.substring(txtRegAux.indexOf("<body>") + 6, txtRegAux.lastIndexOf("</body>"));
                    }
                    
                    if (strEnt.isEmpty()) {
                        txtRegAux = " ".concat(txtRegAux);
                    }
                    
                    int posIns = allTExt.lastIndexOf("</p>");
                    if (posIns < 0) {
                        allTExt = allTExt.replace("<body>", "<body><p style=\"margin-top: 0\">");
                        allTExt = allTExt.replace("</body>", "</p></body>");
                        posIns = allTExt.lastIndexOf("</p>");
                    }
                    srcCellText.getSourceText().setText(allTExt.substring(0, posIns) + txtRegAux + allTExt.substring(posIns));
                    srcCellText.setActExcedente(true);
                    srcCellText.actScroll = true;
                    parentDoc.keyPressed = 0;
                    srcCellText.btnIniciaConcat.setEnabled(true);
                    srcCellText.btnWarnConcat.setEnabled(true);
                    //srcCellText.btnDesconcat.setEnabled(true);
                    origenEncontrado = true;
                    srcCellText.detectarExcedente();
                    srcCellText.actualizaText();
                    break;
                }
                i++;
            }
            
            if (!origenEncontrado) {
                if (parentDoc.getObjectosDescConc() == null) {
                    parentDoc.setObjectosDescConc(new HashMap<>());
                }
                String strEnt = "";
                String txtRegAux = txtRegresar;
                while (txtRegAux.startsWith("\n")) {
                    txtRegAux = txtRegAux.substring(1);
                    if (txtRegAux.startsWith("&")) {
                        txtRegAux = txtRegAux.substring(1);
                        if (txtRegAux.startsWith("&")) {
                            strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                        } else {
                            String align = txtRegAux.substring(0, txtRegAux.indexOf("&"));
                            strEnt = strEnt + "</p><p align=\"" + align + "\" style=\"margin-top: 0\">";
                        }
                        txtRegAux = txtRegAux.substring(txtRegAux.indexOf("&")+1);
                    } else {
                        strEnt = strEnt + "</p><p style=\"margin-top: 0\">";
                    }
                }

                txtRegAux = quitarEntTxt(sourceText.getText());
                txtRegAux = strEnt + txtRegAux.substring(txtRegAux.indexOf("style=\"margin-top: 0\">") + 22, txtRegAux.lastIndexOf("</p>"));
                
                parentDoc.getObjectosDescConc().put(idOrigen, new StringBuilder().append(txtRegAux));
            }
            
            sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
            desactivaBtnsConcat();
            btnDesconcat.setEnabled(false);
            parentDoc.getObjetosConc().remove(idGen);
            //parentDoc.getObjetosConc().remove("POST");
            actExcedente = true;

        detectarDesconc();
        actualizaText();
        detectarExcedente();
        habilitarTodosDesc();
    }
    
    private boolean hayReajusteHoja;
    private boolean actDEsCont;
    public void detectarDesconc() {
        String idGen = secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + parentDoc.getContObj();
        
        if (parentDoc.objectosDescConcInt != null) {
            if (parentDoc.objectosDescConcInt.containsKey(parentDoc.getHojaAct() + "-" + idGen) && actDesc) {
                sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                
                if (parentDoc.getObjetosConc() != null){
                    parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen);
                }
                parentDoc.objectosDescConcInt.remove(parentDoc.getHojaAct() + "-" + idGen);
                btnDesconcat.setEnabled(false);
                actDesc = false;
            }
        }
        
        if (parentDoc.getObjectosDescConc() != null && actDesc) {
            lista = new ArrayList<>();
            StringBuilder sbTxtRegresar = parentDoc.getObjectosDescConc().get(parentDoc.getHojaAct() + "-" + idGen);
            if (sbTxtRegresar != null && !sbTxtRegresar.toString().isEmpty()){
                String allTExt = sourceText.getText();
                String txtReg = sbTxtRegresar.toString();
                String strEnt = "";
                while (txtReg.startsWith("\n")) {
                    strEnt = "</p><p style=\"margin-top: 0\">" + strEnt;
                    txtReg = txtReg.substring(1);
                }
                int posIns = allTExt.lastIndexOf("</p>");
                if (posIns < 0) {
                    allTExt = allTExt.replace("<body>", "<body><p style=\"margin-top: 0\">");
                    allTExt = allTExt.replace("</body>", "</p></body>");
                    posIns = allTExt.lastIndexOf("</p>");
                }
                sourceText.setText(allTExt.substring(0, posIns) + strEnt + txtReg + allTExt.substring(posIns));
                setActExcedente(true);
                parentDoc.getObjectosDescConc().remove(parentDoc.getHojaAct() + "-" + idGen);
                recargaTexto = false;
                actDEsCont = true;
                actualizaText();
            }
        }
        
        if (parentDoc.idOtrosMasOtraHoja != null && !parentDoc.idOtrosMasOtraHoja.isEmpty()) {
            lista = new ArrayList<>();
            obtenScrCellText(parentDoc);
            for (String idConReaj : parentDoc.idOtrosMasOtraHoja) {

                if (idConReaj.equals(parentDoc.getHojaAct() + "-" + idGen)) {
                    parentDoc.strExcReajuste = null;
                    sourceText.setText("<html><head></head><body><p style=\"margin-top: 0\"></p></body></html>");
                    if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty()) {
                        boolean borrar = true;
                        for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                            if (conc.getValue() != null) {
                                borrar = false;
                                break;
                            }
                        }
                        if (borrar) {
                            parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen);
                            btnDesconcat.setEnabled(false);
                        }
                    } else {
                        parentDoc.getObjetosConc().remove(parentDoc.getHojaAct() + "-" + idGen);
                        btnDesconcat.setEnabled(false);
                    }

                    concatListener(null);
                    detectarExcedente();
                    if (sbTextExced != null && !sbTextExced.toString().isEmpty() && parentDoc.idOtrosMasOtraHoja.size() > 1) {
                        actualizarLista = false;

                        String allText = quitarEntTxt(sourceText.getText());
                        allText = quitarFormatosBlanco(allText);
                        
                        int posInitFormato = iniciaFomato(sbTextExced.toString());

                        int startExce = allText.lastIndexOf(sbTextExced.toString().substring(posInitFormato));

                        if (startExce < 0) {
                            continue;
                        }

                        String txtFinal = allText.substring(0, startExce) + allText.substring(startExce + (sbTextExced.toString().length() - posInitFormato));
                        int post = 0;
                        while (txtFinal.endsWith("<p style=\"margin-top: 0\"></p></body></html>")) {
                            txtFinal = txtFinal.replace("<p style=\"margin-top: 0\"></p></body></html>", "</body></html>");
                            post++;
                        }

                        sourceText.setText(txtFinal);

                        actualizaText();

                        Map<String, StringBuilder> textConc = new HashMap<>();
                        String auxExce = sbTextExced.toString();
                        if (auxExce.endsWith("style=\"margin-top: 0\">")) {
                            auxExce = auxExce.substring(0, auxExce.lastIndexOf("style=\"margin-top: 0\">") - 3);
                        }
                        parentDoc.keyPressed = 0;

                        textConc.put(idConReaj, new StringBuilder().append(auxExce));

                        textConc.put("PRE", null);
                        if (precedeEnter) {
                            textConc.put("PRE-ENTER", null);
                        } else if (dobleEnter) {
                            textConc.put("PRE-DOBLE-ENTER", null);
                        }
                        if (post > 0) {
                            textConc.put("POST-ENTER", new StringBuilder().append(post));
                        }
                        parentDoc.setTextConc(textConc);

                        parentDoc.strExcReajuste = sbTextExced;
                        btnIniciaConcat.setEnabled(false);
                        btnWarnConcat.setEnabled(false);
                        desactivaBtnsInicia();
                        detectarExcedente();
                        btnConcat.setEnabled(false);
                        actualizaText();

                        if (parentDoc.getObjetosConc() != null && parentDoc.getObjetosConc().containsKey(idConReaj)) {
                            Map<String, StringBuilder> mapOrigen = parentDoc.getObjetosConc().get(idConReaj);

                            for (Map.Entry<String, StringBuilder> ent :mapOrigen.entrySet()){
                                String idOrigen = ent.getKey();
                                if (ent.getValue() != null){
                                    String enters = "";
                                    if (ent.getValue().toString().startsWith("\n")) {
                                        enters = ent.getValue().toString().substring(0, ent.getValue().toString().lastIndexOf("\n") + 1);
                                    }
                                    allText = sourceText.getText();
                                    allText = quitarEntTxt(allText);
                                    allText = quitarFormatosBlanco(allText);
                                    allText = allText.substring(allText.indexOf("style=\"margin-top: 0\">") + 22, allText.lastIndexOf("</p></body>"));

                                    mapOrigen.replace(idOrigen, new StringBuilder().append(enters).append(allText));
                                    break;
                                }
                            }
                        }
                    } else {
                        parentDoc.strExcReajuste = new StringBuilder();
                    }

                    parentDoc.idOtrosMasOtraHoja.remove(parentDoc.idOtrosMasOtraHoja.get(0));
                    break;
                }
            }
        }
        
        if (parentDoc.getTextConc() != null && parentDoc.getTextConc().size() > 0) {
            if (parentDoc.getTextConc() != null && parentDoc.getTextConc().size() > 0 && parentDoc.getTextConc().containsKey("PRE")) {
                boolean activar = true;
                Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();
                if (mapConcat != null) {
                    if (parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                        activar = false;
                    } else {
                        for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                            Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                            if (mapConcatOrig.containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                                activar = false;
                                break;
                            }
                        }
                    }
                }
                btnConcat.setEnabled(activar);
            }
            
            if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty()) {
                for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                    if (conc.getValue() != null) {
                        desactivaBtnsInicia();
                        break;
                    }
                }
            }
        }
        
        if (parentDoc.getObjetosConc() != null && parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
            boolean ultimo = true;
            Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();
            for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                if (mapConcatOrig.containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                    ultimo = false;
                    break;
                }
            }
            btnDesconcat.setEnabled(ultimo);
        }
        detectarExcedente();
    }
    
    private String txtRespaldo;
    
    private int caracteresExcedenteEnter(String texto) {
        int lengthText = texto.length();
        String textoAux = texto;
        int numCaracExce = maxLetrasXRenglon;

        while (lengthText > maxLetrasXRenglon) {
            textoAux = textoAux.substring(maxLetrasXRenglon);
            lengthText = textoAux.length();
        }
        
        numCaracExce = maxLetrasXRenglon - textoAux.length();
         
        return numCaracExce;
    }
    
    private void actualizaConcat() {
        int i = 0;
        for (ScrCellText srcCellText : lista) {
            if (this == srcCellText) {
                break;
            }
            i++;
        }

        String idGen = parentDoc.getHojaAct() + "-" + secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + i;
        
        if (parentDoc.getObjetosConc() != null && parentDoc.getObjetosConc().containsKey(idGen)) {
            Map<String, StringBuilder> mapOrigen = parentDoc.getObjetosConc().get(idGen);

            for (Map.Entry<String, StringBuilder> ent :mapOrigen.entrySet()){
                String idOrigen = ent.getKey();
                if (ent.getValue() != null){
                    String enters = "";
                    if (ent.getValue().toString().startsWith("\n")) {
                        enters = ent.getValue().toString().substring(0, ent.getValue().toString().lastIndexOf("\n") + 1);
                    }
                    String allText = sourceText.getText();
                    allText = quitarEntTxt(allText);
                    allText = quitarFormatosBlanco(allText);
                    allText = allText.substring(allText.indexOf("style=\"margin-top: 0\">") + 22, allText.lastIndexOf("</p></body>"));
                    
                    mapOrigen.replace(idOrigen, new StringBuilder().append(enters).append(allText));
                    break;
                }
            }
        }
    }
    
    private void actualizaConcatTodos() {
        if (parentDoc.getObjetosConc() != null) {
            int i = 0;
            for (ScrCellText srcCellText : lista) {
                String idGen = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
                if (parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                    Map<String, StringBuilder> mapOrigen = parentDoc.getObjetosConc().get(parentDoc.getHojaAct() + "-" + idGen);

                    for (Map.Entry<String, StringBuilder> ent :mapOrigen.entrySet()){
                        String idOrigen = ent.getKey();
                        if (ent.getValue() != null){
                            String enters = "";
                            if (ent.getValue().toString().startsWith("\n")) {
                                enters = ent.getValue().toString().substring(0, ent.getValue().toString().lastIndexOf("\n") + 1);
                            }
                            String allText = srcCellText.sourceText.getText();
                            allText = quitarEntTxt(allText);
                            allText = quitarFormatosBlanco(allText);
                            allText = allText.substring(allText.indexOf("<body>") + 6, allText.lastIndexOf("</body>"));
                            if (allText.startsWith("<p ")) {
                                allText = allText.substring(allText.indexOf("style=\"margin-top: 0\">") + 22, allText.lastIndexOf("</p>"));
                            }

                            mapOrigen.replace(idOrigen, new StringBuilder().append(enters).append(allText));
                            break;
                        }
                    }
                }
                i++;
            }
        }
    }
    
    private int numCarGen;
    
    private void detectarExcedente() {
        try {
            if (moduleBO.isHeaderOrSection() || moduleBO.isSection()) {
                return ;
            }
            maxLetrasXRenglon = Utilerias.letrasPorRenglon((int) secInfo.getWidth());
            maxRenglones = Utilerias.renglonesPorEspacioEnSeccion(secInfo);
            numMaxCaracteres = maxLetrasXRenglon * maxRenglones;
            sbTextExced = null;
            String[] arrRenglones = new String[maxRenglones];
            
            String allText = sourceText.getDocument().getText(0, sourceText.getDocument().getLength());
            numCarGen = allText.length();
            
            if (allText.startsWith("\n")) {
                allText = allText.substring(1);
            }
            allText = allText.replace("&", "&amp;");
            allText = allText.replace("\"", "&quot;");
            allText = allText.replace("<", "&lt;");
            allText = allText.replace(">", "&gt;");
            int numCar = allText.length();
            numCaracActuales = numCar;
            String allTextAux = allText;
            
            for (int i = 0; i < maxRenglones; i++) {
                if (allTextAux.length() > maxLetrasXRenglon || allTextAux.contains("\n")) {
                    String strAux = "";
                    
                    if (allTextAux.contains("\n") && allTextAux.indexOf("\n") < maxLetrasXRenglon){
                        strAux = allTextAux.substring(0, allTextAux.indexOf("\n") + 1);
                        allTextAux = allTextAux.substring(allTextAux.indexOf("\n") + 1);
                    } else {
                        strAux = allTextAux.substring(0, maxLetrasXRenglon);
                        allTextAux = allTextAux.substring(maxLetrasXRenglon);
                    }
                    arrRenglones[i] = strAux;
                } else {
                    arrRenglones[i] = allTextAux;
                    break;
                }
            }
            
            int numCaracExceEnter = 0;
            
            for (String str : arrRenglones) {
                if (str != null) {
                    if (str.contains("\n")){
                        numCaracExceEnter = numCaracExceEnter + (maxLetrasXRenglon - str.length());
                    }
                } else {
                    break;
                }
            }
            
            numMaxCaracteres = numMaxCaracteres - numCaracExceEnter;
            
            if (numCaracActuales > numMaxCaracteres) {
                precedeEnter = allText.substring(numMaxCaracteres - 1, numCaracActuales).startsWith("\n");
                dobleEnter = false;

                String textExced = allText.substring(numMaxCaracteres, numCaracActuales);
                String textCorrecto = allText.substring(0, numMaxCaracteres);
                
                String x = allText.substring(numMaxCaracteres, numMaxCaracteres + 1);
                if (!textCorrecto.endsWith(" ") && !textCorrecto.endsWith("\n") && !x.equals(" ") && !x.equals("\n")) {
                    textCorrecto = textCorrecto.substring(0, textCorrecto.lastIndexOf(" ") + 1);
                    textExced = allText.substring(textCorrecto.length(), numCaracActuales);
                }
                /*textExced = textExced.replace("&", "&amp;");
                textExced = textExced.replace("\"", "&quot;");
                textExced = textExced.replace("<", "&lt;");
                textExced = textExced.replace(">", "&gt;");*/
                if (textExced.startsWith("\n")) {
                    textExced = textExced.substring(1);
                    if (precedeEnter) {
                        dobleEnter = true;
                        precedeEnter = false;
                    } else {
                        precedeEnter = true;
                    }
                }
                
                if (textExced.endsWith("\n")) {
                    textExced = textExced.substring(0, textExced.length() - 1);
                }
                
                textExced = txt2HTML(textExced);
                textExced = quitarFormatosBlanco(textExced);
                String srcText = sourceText.getText();
                
                if (!srcText.contains("</p>")) {
                    srcText = srcText.replace("<body>", "<body><p style=\"margin-top: 0\">");
                    srcText = srcText.replace("</body>", "</p></body>");
                }
                
                String allsinEnt = quitarEntTxt(srcText);
                String auxPos = allsinEnt;
                //Map<Integer, String> mPosAlig = getPosAlig(auxPos);
//                auxPos = auxPos.replace("<p align=\"center\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
//                auxPos = auxPos.replace("<p align=\"left\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
//                auxPos = auxPos.replace("<p align=\"right\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
                Map<Integer, String> mPosFor = getPosFormato(auxPos);
//                List<Integer> lPosNeg = getPosNeg(auxPos);
//                List<Integer> lPosCur = getPosCur(auxPos);
//                List<Integer> lPosSubra = getPosSubra(auxPos);

                String allSinNeg = allsinEnt.replace("<b>","");
                allSinNeg = allSinNeg.replace("</b>","");
                allSinNeg = allSinNeg.replace("<i>","");
                allSinNeg = allSinNeg.replace("</i>","");
                allSinNeg = allSinNeg.replace("<u>","");
                allSinNeg = allSinNeg.replace("</u>","");
                //allSinNeg = allSinNeg.replace("&lt;", "<");
                //allSinNeg = allSinNeg.replace("&gt;", ">");
                allSinNeg = allSinNeg.replace("<p align=\"center\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
                allSinNeg = allSinNeg.replace("<p align=\"left\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
                allSinNeg = allSinNeg.replace("<p align=\"right\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");
                allSinNeg = allSinNeg.replace("<p align=\"\" style=\"margin-top: 0\">", "<p style=\"margin-top: 0\">");

                int posTextExc = allSinNeg.lastIndexOf(textExced);
                
                //posTextExc = sumPosAlig(mPosAlig, posTextExc);
                Object[] posAbi = sumPosFormato(mPosFor, posTextExc);
                
                //Object[] posAbi = sumPosNeg(lPosNeg, posTextExc);
                posTextExc = (Integer) posAbi[0];
                boolean abiertoNeg = (Boolean) posAbi[1];
                
                //posAbi = sumPosCur(lPosCur, posTextExc);
                //posTextExc = (Integer) posAbi[0];
                boolean abiertoCur = (Boolean) posAbi[2];
                
                //posAbi = sumPosSubra(lPosSubra, posTextExc);
                //posTextExc = (Integer) posAbi[0];
                boolean abiertoSubra = (Boolean) posAbi[3];
                
                if (posTextExc > -1 && textExced != null && !textExced.trim().isEmpty()) {
                    String auxFin = (abiertoNeg ? "<b>" : "") + (abiertoCur ? "<i>" : "") + (abiertoSubra ? "<u>" : "") + allsinEnt.substring(posTextExc, allsinEnt.contains("</p>") ? allsinEnt.lastIndexOf("</p>") :  allsinEnt.indexOf("</body>"));
                    sbTextExced = new StringBuilder(auxFin);
                    if (actExcedente) {
                        sourceText.setText(resaltarTexto(allsinEnt, posTextExc));
                        if (numCar > posCursor) {
                            sourceText.setCaretPosition(posCursor);
                        }
                        //actExcedente = false;

                    }
                }
            } else {
                String str = sourceText.getText();
                if (str.contains("<span><font color=\"#8A0808\">")) {
                    str = str.replace("<span><font color=\"#8A0808\">", "");
                    str = str.replace("</font></span>", "");
                    sourceText.setText(str);
                    if (numCar > posCursor) {
                        sourceText.setCaretPosition(posCursor);
                    }
                }
            }
            
            if (sbTextExced == null || sbTextExced.toString().isEmpty()) {
                btnIniciaConcat.setEnabled(false);
                btnWarnConcat.setEnabled(false);
            } else {
                if (parentDoc.getTextConc() != null && !parentDoc.getTextConc().isEmpty()) {
                    boolean activa = true;
                    if (!vieneDeReajuste) {
                        for (Map.Entry<String, StringBuilder> conc : parentDoc.getTextConc().entrySet()) {
                            if (conc.getValue() != null && !conc.getKey().equals("ALIGN")) {
                                activa = false;
                                break;
                            }
                        }
                    }
                    btnIniciaConcat.setEnabled(activa);
                } else {
                    btnIniciaConcat.setEnabled(true);
                }
                btnWarnConcat.setEnabled(true);
            }

        } catch (BadLocationException ex) {
            Logger.getLogger(ScrCellText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Map<Integer, String> getPosFormato(String txt) {
        Map<Integer, String> mPos = new LinkedHashMap<>();
        String auxTxt = txt;
        while (auxTxt.contains("<b>") || auxTxt.contains("</b>") ||
               auxTxt.contains("<i>") || auxTxt.contains("</i>") ||
               auxTxt.contains("<u>") || auxTxt.contains("</u>") ||
               auxTxt.contains("<p align=\"")) {
            
            Map<Integer, String> mAux = new LinkedHashMap<>();
            mAux.put(auxTxt.lastIndexOf("<b>"), "<b>");
            mAux.put(auxTxt.lastIndexOf("</b>"), "</b>");
            mAux.put(auxTxt.lastIndexOf("<i>"), "<i>");
            mAux.put(auxTxt.lastIndexOf("</i>"), "</i>");
            mAux.put(auxTxt.lastIndexOf("<u>"), "<u>");
            mAux.put(auxTxt.lastIndexOf("</u>"), "</u>");
            mAux.put(auxTxt.lastIndexOf("<p align=\""), auxTxt.lastIndexOf("<p align=\"") < 0 ? "" :
                                                        auxTxt.substring(auxTxt.lastIndexOf("<p align=\"") + 2, auxTxt.substring(auxTxt.lastIndexOf("<p align=\"")).indexOf("\" ") + 1 + auxTxt.lastIndexOf("<p align=\"")));
            
            List<Integer> lKeys = new ArrayList<>(mAux.keySet());
            Collections.sort(lKeys, Collections.reverseOrder());
            mPos.put(lKeys.get(0), mAux.get(lKeys.get(0)));
            auxTxt = auxTxt.substring(0, lKeys.get(0));
        }
        
        return mPos;
    }
    
    private Object[] sumPosFormato(Map<Integer, String> mPosForm, Integer pos) {
        Boolean abiertoNeg = false;
        Boolean abiertoCur = false;
        Boolean abiertoSub = false;
        Integer posTextExc = pos;
        Object[] posAbi = new Object[4];
        List<Integer> lKeys = new ArrayList<>(mPosForm.keySet());
        Collections.sort(lKeys);
        
        int tagNeg = 0;
        int tagCur = 0;
        int tagSub = 0;
        
        for (Integer iKey : lKeys) {
            if (iKey > posTextExc) {
                break;
            }
            
            String tag = mPosForm.get(iKey);
            posTextExc += tag.length();
            
            switch (tag) {
                case "<b>" :
                    tagNeg++;
                    break;
                case "</b>" :
                    tagNeg++;
                    break;
                case "<i>" :
                    tagCur++;
                    break;
                case "</i>" :
                    tagCur++;
                    break;
                case "<u>" :
                    tagSub++;
                    break;
                case "</u>" :
                    tagSub++;
                    break;
            }
        }
        
        abiertoNeg = (tagNeg % 2) == 1;
        abiertoCur = (tagCur % 2) == 1;
        abiertoSub = (tagSub % 2) == 1;
        
        posAbi[0] = posTextExc;
        posAbi[1] = abiertoNeg;
        posAbi[2] = abiertoCur;
        posAbi[3] = abiertoSub;
        
        return posAbi;
    }
    
    private String resaltarTexto(String txtIn, int posTextExc) {
        String txtOrig = txtIn;
        String txtFin = txtOrig.substring(posTextExc);
        String txtToLight = txtOrig.substring(posTextExc, txtOrig.lastIndexOf("</body>"));
        String txtToLight2 = txtToLight.replace("style=\"margin-top: 0\">", "style=\"margin-top: 0\"><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("</p>", "</span></p>");
        txtToLight2 = txtToLight2.replace("<b>", "</span><b><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("</b>", "</span></b><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("<i>", "</span><i><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("</i>", "</span></i><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("<u>", "</span><u><span style=\"color:#8A0808\">");
        txtToLight2 = txtToLight2.replace("</u>", "</span></u><span style=\"color:#8A0808\">");
        txtOrig = txtOrig.substring(0, posTextExc) + "<span style=\"color:#8A0808\">" + txtToLight2 + txtOrig.substring(txtOrig.lastIndexOf("</body>"));
        if (parentDoc.keyPressed == 10 && numCarGen == posCursor) {
            String enters = "<p style=\"margin-top: 0\"></p>";
            txtOrig = txtOrig.replace("</body>", enters + "</body>");
        }
        return txtOrig;
    }

    private String quitarEntTxt(String txtOrig) {
        String allsinEnt = txtOrig.replace("\r\n       ", "");
        allsinEnt = allsinEnt.replace("\r\n      ", "");
        allsinEnt = allsinEnt.replace("\r\n     ", "");
        allsinEnt = allsinEnt.replace("\r\n    ", "");
        allsinEnt = allsinEnt.replace("\r\n   ", "");
        allsinEnt = allsinEnt.replace("\r\n  ", "");
        allsinEnt = allsinEnt.replace("\r\n ", "");
        allsinEnt = allsinEnt.replace("\r\n", "");
        allsinEnt = allsinEnt.replace("\n       ", "");
        allsinEnt = allsinEnt.replace("\n      ", "");
        allsinEnt = allsinEnt.replace("\n     ", "");
        allsinEnt = allsinEnt.replace("\n    ", "");
        allsinEnt = allsinEnt.replace("\n   ", "");
        allsinEnt = allsinEnt.replace("\n  ", "");
        allsinEnt = allsinEnt.replace("\n ", "");
        allsinEnt = allsinEnt.replace("\n", "");
        allsinEnt = allsinEnt.replace("<b>    ", "<b>");
        allsinEnt = allsinEnt.replace("</b>    ", "</b>");
        allsinEnt = allsinEnt.replace("<b></b>", "");
        allsinEnt = allsinEnt.replace("<i>    ", "<i>");
        allsinEnt = allsinEnt.replace("</i>    ", "</i>");
        allsinEnt = allsinEnt.replace("<i></i>", "");
        allsinEnt = allsinEnt.replace("<u>    ", "<u>");
        allsinEnt = allsinEnt.replace("</u>    ", "</u>");
        allsinEnt = allsinEnt.replace("<u></u>", "");
        allsinEnt = allsinEnt.replace("<font color=\"#8A0808\">", "");
        allsinEnt = allsinEnt.replace("<span>", "");
        allsinEnt = allsinEnt.replace("</font>", "");
        allsinEnt = allsinEnt.replace("</span>", "");
        allsinEnt = allsinEnt.replace("    </p>", "</p>");
        /*if (parentDoc.keyPressed == 10) {
            String enters = "<p style=\"margin-top: 0\"></p>";

            allsinEnt = allsinEnt.replace("</body>", enters + "</body>");
        }*/
        return allsinEnt;
    }
    
    private void desactivaBtnsConcat() {
        obtenScrCellText(parentDoc);
        for (ScrCellText cellText : lista) {
            cellText.getBtnConcat().setEnabled(false);
        }
    }
    
    private void desactivaBtnsDesconcat() {
        obtenScrCellText(parentDoc);
        for (ScrCellText cellText : lista) {
            cellText.getBtnDesconcat().setEnabled(false);
        }
    }
    
    private void desactivaBtnsInicia() {
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        lista.stream().forEach((cellText) -> {
            cellText.getBtnIniciaConcat().setEnabled(false);
        });
    }
    
    private void activaBtnsInicia() {
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        lista.stream().forEach((cellText) -> {
            if (cellText.sbTextExced != null && !cellText.sbTextExced.toString().isEmpty()) {
                cellText.getBtnIniciaConcat().setEnabled(true);
            } else {
                cellText.getBtnIniciaConcat().setEnabled(false);
            }
        });
    }
    
    private void actviBtnsDesconcat() {
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        int i = 0;
        for (ScrCellText srcCellText : lista) {
            String idGen2 = srcCellText.secInfo.getModuleId() + "" + srcCellText.secInfo.getSectionId() + "" + srcCellText.secInfo.getSectionParentId() + "" + srcCellText.secInfo.getIdx() + "" + i;
            if (parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen2)) {
                boolean ultimo = true;
                Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();
                for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                    Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                    if (mapConcatOrig.containsKey(parentDoc.getHojaAct() + "-" + idGen2)) {
                        ultimo = false;
                        break;
                    }
                }
                
                srcCellText.btnDesconcat.setEnabled(ultimo);
            }
            i++;
        }
    }
    
    private boolean actualizarLista = true;
            
    private void activaDesactivaBntConcat() {
        
        lista = new ArrayList<>();
        obtenScrCellText(parentDoc);
        

        for (ScrCellText scrCellTextP : lista) {
            boolean hayTextoExcedente = (scrCellTextP.getSbTextExced() != null && !scrCellTextP.getSbTextExced().toString().isEmpty());

            if (hayTextoExcedente) {
                for (int i = 0; i < lista.size(); i++) {
                    ScrCellText scrCellTextH = lista.get(i);
                    if (seguirAct && sbTextExced != null) {
                        String idGen = ""; 
                        int j = 0;
                        for (ScrCellText scrCellText : lista) { 
                            if (sbTextExced != null && scrCellText.getSbTextExced() != null && sbTextExced.toString().equals(scrCellText.getSbTextExced().toString())) {
                                idGen = secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + j;
                                break;
                            }
                            j++;
                        }
                        String allText = quitarEntTxt(sourceText.getText());
                        allText = quitarFormatosBlanco(allText);
                        int posInitFormato = iniciaFomato(sbTextExced.toString());

                        int startExce = allText.lastIndexOf(sbTextExced.toString().substring(posInitFormato));

                        if (startExce < 0) {
                            continue;
                        }

                        String txtFinal = allText.substring(0, startExce) + allText.substring(startExce + (sbTextExced.toString().length() - posInitFormato));
                        int post = 0;
                        while (txtFinal.endsWith("style=\"margin-top: 0\"></p></body></html>")) {
                            txtFinal = txtFinal.substring(0, txtFinal.lastIndexOf("<p ")) + "</body></html>";
                            post++;
                        }
                        sourceText.setText(txtFinal);
                        
                        String align = txtFinal.substring(txtFinal.lastIndexOf("<p "));
                        if (align.contains("align")) {
                            align = align.substring(10, align.indexOf("\" "));
                        } else {
                            align = "";
                        }

                        actualizaText();

                        Map<String, StringBuilder> textConc = new HashMap<>();
                        String auxExce = sbTextExced.toString();
                        //auxExce = auxExce.replace("<p style=\"margin-top: 0\"></p>", "");
                       
                        if (auxExce.endsWith("style=\"margin-top: 0\">")) {
                            auxExce = auxExce.substring(0, auxExce.lastIndexOf("<p "));
                        }
                        parentDoc.keyPressed = 0;

                        textConc.put(parentDoc.getHojaAct() + "-" + idGen, new StringBuilder().append(auxExce));
                        textConc.put("PRE", null);
                        textConc.put("ALIGN", new StringBuilder().append(align));
                        if (precedeEnter) {
                            textConc.put("PRE-ENTER", null);
                        } else if (dobleEnter) {
                            textConc.put("PRE-DOBLE-ENTER", null);
                        }
                        if (post > 0) {
                            textConc.put("POST-ENTER", new StringBuilder().append(post));
                        }
                        parentDoc.setTextConc(textConc);
                        break;
                    } else {
                        String idGen = scrCellTextH.secInfo.getModuleId() + "" + scrCellTextH.secInfo.getSectionId() + "" + scrCellTextH.secInfo.getSectionParentId() + "" + scrCellTextH.secInfo.getIdx() + "" + i;
                        boolean activar = true;
                        if (parentDoc.getObjetosConc() != null) {
                            Map<String, Map<String, StringBuilder>> mapConcat = parentDoc.getObjetosConc();
                            if (parentDoc.getObjetosConc().containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                                activar = false;
                            } else {
                                for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                                    Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                                    if (mapConcatOrig.containsKey(parentDoc.getHojaAct() + "-" + idGen)) {
                                        activar = false;
                                        break;
                                    }
                                }
                            }
                        }

                        scrCellTextH.getBtnConcat().setEnabled(activar);
                        if (scrCellTextH.getBtnIniciaConcat().isEnabled() && scrCellTextH.sbTextExced != null && scrCellTextH.sbTextExced.toString().equals(sbTextExced.toString())) {
                            idGen = secInfo.getModuleId() + "" + secInfo.getSectionId() + "" + secInfo.getSectionParentId() + "" + secInfo.getIdx() + "" + i;

                            String allText = quitarEntTxt(sourceText.getText());
                            allText = quitarFormatosBlanco(allText);
                            
                            int posInitFormato = iniciaFomato(sbTextExced.toString());

                            int startExce = allText.lastIndexOf(sbTextExced.toString().substring(posInitFormato));

                            if (startExce < 0) {
                                continue;
                            }

                            String txtFinal = allText.substring(0, startExce) + allText.substring(startExce + (sbTextExced.toString().length() - posInitFormato));
                            int post = 0;
                            while (txtFinal.endsWith("style=\"margin-top: 0\"></p></body></html>")) {
                                txtFinal = txtFinal.substring(0, txtFinal.lastIndexOf("<p ")) + "</body></html>";
                                post++;
                            }
                            
                            sourceText.setText(txtFinal);
                            
                            String align = txtFinal.substring(txtFinal.lastIndexOf("<p "));
                            if (align.contains("align")) {
                                align = align.substring(10, align.indexOf("\" "));
                            } else {
                                align = "";
                            }

                            actualizaText();

                            Map<String, StringBuilder> textConc = new HashMap<>();
                            String auxExce = sbTextExced.toString();
                            //auxExce = auxExce.replace("<p style=\"margin-top: 0\"></p>", "");
                            if (auxExce.endsWith("style=\"margin-top: 0\">")) {
                                auxExce = auxExce.substring(0, auxExce.lastIndexOf("style=\"margin-top: 0\">"));
                            }
                            parentDoc.keyPressed = 0;
                            textConc.put(parentDoc.getHojaAct() + "-" + idGen, new StringBuilder().append(auxExce));
                            textConc.put("PRE", null);
                            textConc.put("ALIGN", new StringBuilder().append(align));
                            if (precedeEnter) {
                                textConc.put("PRE-ENTER", null);
                            } else if (dobleEnter) {
                                textConc.put("PRE-DOBLE-ENTER", null);
                            }
                            if (post > 0) {
                                textConc.put("POST-ENTER", new StringBuilder().append(post));
                            }
                            parentDoc.setTextConc(textConc);
                        }
                    }
                }
                break;
            }
        }
    }
    
    private int iniciaFomato(String str) {
        int pos = 0;
        String strAux = str;
        
        while (strAux.startsWith("<b>") || strAux.startsWith("<i>") || strAux.startsWith("<u>")) {
            pos = pos + 3;
            strAux = strAux.substring(3);
        }
        
        return pos;
    }
    
    private String quitarFormatosBlanco(String conFormato) {
        String sinFormato = conFormato;
        sinFormato = sinFormato.replace("<b></b>", "");
        sinFormato = sinFormato.replace("<i></i>", "");
        return sinFormato;
    }

    private void obtenScrCellText(Container container) {
        if (container != null && container.getComponents() != null) {
            for (Component c : container.getComponents()) {
                if (c instanceof ScrCellText) {
                    lista.add((ScrCellText) c);
                } else {
                    obtenScrCellText((Container) c);
                }
            }
        }
    }

    public JButton getBtnConcat() {
        return btnConcat;
    }

    public JButton getBtnDesconcat() {
        return btnDesconcat;
    }

    public JButton getBtnIniciaConcat() {
        return btnIniciaConcat;
    }

    public StringBuilder getSbTextExced() {
        return sbTextExced;
    }

    public void setSbTextExced(StringBuilder sbTextExced) {
        this.sbTextExced = sbTextExced;
    }

    public StringBuilder getSbTextOK() {
        return sbTextOK;
    }

    public void setSbTextOK(StringBuilder sbTextOK) {
        this.sbTextOK = sbTextOK;
    }

    public JTextPane getSourceText() {
        return sourceText;
    }

    public void setSourceText(JTextPane sourceText) {
        this.sourceText = sourceText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
