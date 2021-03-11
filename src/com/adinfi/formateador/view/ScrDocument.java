package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.main.RibbonMenu;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.dnd.ScrCell;
import com.adinfi.formateador.view.publish.SendPublish;
import com.adinfi.formateador.view.resources.DnDTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Desarrollador
 */
public class ScrDocument extends javax.swing.JPanel {

    private int documentId_;
    private String documentName;
    private DocumentBO docBO;
    int meterText = 0;
    short currentHoja = 1;
    private List<JPanel> listaTextos = new ArrayList<>();
    protected List<String> hojasDel = new ArrayList<>();
    boolean openPublish = false;
    public List<String> idOtrosMas = new ArrayList();
    public List<String> idOtrosMasOtraHoja = new ArrayList();
    public boolean textNvoConcatenado = false;
    private boolean isMovedPage = false;
    private int idxActResp = 0;
    public int keyPressed;
    public StringBuilder strExcReajuste;
    private int numHojaSig = 0;
    private boolean isOpenDoc;
    /**
     * Creates new form ScrDocument
     */
    public ScrDocument() {
        initComponents();
        tabHojas = new DnDTabbedPane();
        tabHojas.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabHojas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabHojas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabHojasStateChanged(evt);
            }
        });
        add(tabHojas, java.awt.BorderLayout.CENTER);
        tabHojas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tabHojasMouseDragged(evt);
            }
        });

        tabHojas.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                if (isMovedPage) {
                    generarOrdenVisual();
                    isMovedPage = false;
                }
            }
        });
        initScreen();
        initMenuEmergente();
    }

    /**
     * Metodo, que inicializa las opciones de Borrar modulo y agregar a modulo
     * colaborativo
     */
    private void initMenuEmergente() {
        JMenuItem mbItemBorrarModulo;
        //JMenuItem mbItemEnlazar;
        mbItemBorrarModulo = new JMenuItem("Eliminar módulo");
        mbItemBorrarModulo.setEnabled(false);
        mbItemBorrarModulo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemActionBorrarModulo(evt);
            }
        });

        /*mbItemEnlazar = new JMenuItem("Agregar a módulo colaborativo");
         mbItemEnlazar.setEnabled(true);
         mbItemEnlazar.addActionListener(new java.awt.event.ActionListener() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt) {
         jMenuItemActionAgregarModuloColaborativo(evt);
         }
         });*/
        popHojas.addSeparator();
        popHojas.add(mbItemBorrarModulo);
        //popHojas.add(mbItemEnlazar);

    }

    /**
     * Metodo que maneja la logica para borrar el modulo
     *
     * @param evt
     */
    private void jMenuItemActionBorrarModulo(java.awt.event.ActionEvent evt) {
        getSelectedModuleID();

        int reply = JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea eliminar el módulo?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.out.println("Se va a borrar el modulo ... ");
            //TODO:Mandar a ejecutar el metodo de eliminar modulo
            //deleteModule(this);
        }
    }

    protected int getSelectedModuleID() {
        int moduleID = 0;
        try {
            if (contentPanel != null) {
                Component[] comps = contentPanel.getComponents();
                for (int i = 0; i < comps.length; i++) {
                    System.out.println(comps[i].getName());
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        return moduleID;
    }

    /**
     * Metodo que maneja la logica para borrar el modulo
     *
     * @param evt
     */
    private void jMenuItemActionAgregarModuloColaborativo(java.awt.event.ActionEvent evt) {

    }

    public void addModuleColaborative(/* int moduleId */  int documentModuleId  ) {
        Utilerias.pasarGarbageCollector();
        int numHoja = tabHojas.getSelectedIndex() + 1;
        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return;
        }

        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            return;
        }

        for (ModuleBO moduleBO : lstModules) {
            if (moduleBO.getDocumentModuleId() == documentModuleId ) {
                if (this.getDocBO().isCollaborative()) {
                    JOptionPane.showMessageDialog(this, "No se puede agregar modulos del documento " + this.getDocBO().getDocument_Name() + " por que fue generado en integrador de documentos", "Agregar módulos ", JOptionPane.OK_OPTION);
                    continue;
                } else {
                    DocumentTypeDAO docTypeDAO = new DocumentTypeDAO();
                    List<Integer> lDocTypes = docTypeDAO.getTemplateByDocumentTypeId(this.getDocBO().getIdDocument_Type());
                    if (lDocTypes != null && lDocTypes.contains(16)) {
                        JOptionPane.showMessageDialog(this, "Los módulos de una Plantilla 16 no puedes ser agregados", "Agregar módulos ", JOptionPane.OK_OPTION);
                        continue;
                    }
                }

                Map<Integer, String> lDocModCo = new HashMap<>();
                List<Integer> modPlan16 = new ArrayList<>();
                modPlan16.add(GlobalDefines.OBJ_TYPE_CALENDAR);
                modPlan16.add(GlobalDefines.OBJ_TYPE_CALENDAR_MONTH);
                modPlan16.add(GlobalDefines.OBJ_TYPE_CALENDAR_WEEK);
                if (moduleBO.getRootSection().getLstObjects() != null) {
                    for (ObjectInfoBO oiBO : moduleBO.getRootSection().getLstObjects()) {
                        if (modPlan16.contains(oiBO.getObjType())) {
                            JOptionPane.showMessageDialog(this, "Los módulos relacionadoss con una Plantilla 16 no puedes ser agregados", "Agregar módulos ", JOptionPane.OK_OPTION);
                            return;
                        }
                    }

                }
                DlgModuloColaborativo dlgModCol = null;
                if (this.getDocBO().getIdMarket() == 0 && this.getDocBO().getIdDocType() == 0) {
                    dlgModCol = new DlgModuloColaborativo(MainApp.getApplication().getMainFrame(), true, true);
                } else {
                    dlgModCol = new DlgModuloColaborativo(MainApp.getApplication().getMainFrame(), true, true, this.getDocBO().getIdMarket(), this.getDocBO().getIdDocType());
                }
                if (moduleBO.getDocumentModuleId() > 0) {
                    TemplateDAO tempDAO = new TemplateDAO();
                    ModuleBO modName = tempDAO.getModule(moduleBO.getModuleId());
                    lDocModCo.put(moduleBO.getDocumentModuleId(), modName.getName());
                    dlgModCol.setAdd(true);
                } else {
                    lDocModCo.put(moduleBO.getModuleId(), moduleBO.getName());
                    dlgModCol.setAdd(false);
                }
                dlgModCol.setlDocModCollab(lDocModCo);
                Utilerias.centreDialog(dlgModCol, true);
                dlgModCol.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
                dlgModCol.setVisible(true);
                break;
            }
        }
    }

    protected final void initScreen() {
        tabHojas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    popHojas.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
    }

    public boolean isOpenPublish() {
        return openPublish;
    }

    public void setOpenPublish(boolean openPublish) {
        this.openPublish = openPublish;
    }

    public int getDocumentId() {
        return documentId_;
    }

    public void setDocumentId(int documentId) {
        this.documentId_ = documentId;
        if (docBO != null) {
            docBO.setDocumentId(documentId);
        }
    }

    public void init() throws SQLException {
        if (getDocumentId() > 0) {
            numHojaSig = 0;
            openDoc();
        }
    }

    public void initNewDocument() throws SQLException {
        if (getDocumentId() != -1) {
            return;
        }

        tabHojas.removeAll();
        int numHojas = 1;
        JPanel panHoja = null;
        for (int i = 0; i < numHojas; i++) {
            panHoja = new JPanel();
            panHoja.setLayout(new BorderLayout());
            tabHojas.addTab("" + numHojas, panHoja);
            tabHojas.setTitleAt(i, "" + (i + 1));
        }

        short numHoja = 1;
        panHoja = (JPanel) tabHojas.getComponentAt(numHoja - 1);
        if (panHoja != null) {
            panHoja.removeAll();
            JScrollPane jScroll = new JScrollPane(this.contentPanel);
            panHoja.add(jScroll);
        }
        
        initConcatObj();
    }

    public void openDoc() throws SQLException {
        if (getDocumentId() <= 0) {
            return;
        }
        tabHojas.removeAll();
        tabHojas.revalidate();
        tabHojas.updateUI();
        //tabHojas.setFocusCycleRoot(false);
        DocumentDAO docDAO = new DocumentDAO();
        DocumentBO docBO = docDAO.getDocument(getDocumentId(), -1,false);
        
        List<String> titulo = Utilerias.getTitleProperties(docBO);
        if(titulo!= null && titulo.size() >= 3){
            MainView.main.initInfoLabel(titulo.get(0), titulo.get(1), titulo.get(2));
        }else{
            MainView.main.initInfoLabel("", "", "");
        }
        
        objetosConc = docDAO.getConcatObjsDocument(getDocumentId());

        if (docBO == null) {
            return;
        }
        this.setDocBO(docBO);
        int numHojas = docBO.getNumHojas();

        JPanel panHoja = null;
        isOpenDoc = true;
        for (int i = 0; i < numHojas; i++) {
            Utilerias.pasarGarbageCollector();
            panHoja = new JPanel();
            panHoja.setLayout(new BorderLayout());
            // 
            // tabHojas.add(panHoja , BorderLayout.CENTER );
            tabHojas.addTab("" + numHojas, panHoja);
            tabHojas.setTitleAt(i, "" + (i + 1));
            tabHojas.revalidate();
            tabHojas.updateUI();
        }
        isOpenDoc = false;
        this.showHoja((short) 1);
        System.out.println("Indice actual de paginas : " + idxActResp);
        if(idxActResp > 0 && tabHojas.getTabCount() > 0){
            tabHojas.setSelectedIndex(idxActResp);
            idxActResp = 0;
        }
        

        if (this.isOpenPublish()) {
            SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
            Utilerias.centreDialog(dt, true);
            dt.setVisible(dt.showDialog);
            MainView.main.getProgressBar().setVisible(false);
        }
    }

    private void generarOrdenVisual() {
        try {
            List<Integer> orden = new ArrayList<>();
            for (int i = 0; i < tabHojas.getTabCount(); i++) {
                orden.add(Integer.parseInt(tabHojas.getTitleAt(i)));
            }
            this.getDocBO().setOrden(orden);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public void deleteModule(int sectionRoot) {
        if (JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar el módulo ?", "Eliminar Módulo", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }
        int numHoja = tabHojas.getSelectedIndex() + 1;
        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return;
        }

        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            return;
        }
        int i = 0;

        ModuleBO moduleRemove = null;
        for (ModuleBO moduleBO : lstModules) {
            if (moduleBO.getRootSectionId() == sectionRoot) {
                lstModules.remove(i);
                moduleRemove = moduleBO;
                break;
            }
            i++;
        }

        if (moduleRemove != null) {
            this.contentPanel.remove(moduleRemove.getPanModule());
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
        }

    }

    public boolean addModule(int moduleId, int templateId) throws UnsupportedOperationException {
        Utilerias.pasarGarbageCollector();
        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
        }

        TemplateDAO tempDAO = new TemplateDAO();
        ModuleBO moduleBO = tempDAO.getModule(moduleId);
        if (moduleBO == null) {
            return false;
        }

        int numHoja = tabHojas.getSelectedIndex();
        numHoja = Integer.parseInt(tabHojas.getTitleAt(numHoja));

        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return false;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            
            if (templateId > 0) {
                TemplateBO templateBO = tempDAO.getTemplate(templateId);

                if (tabHojas.getSelectedIndex() == 0 && !templateBO.isFirstPage()) {
                    JOptionPane.showMessageDialog(null, "La plantilla que intenta agregar no es de primera hoja.");
                    return false;
                }
            }
            
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
        }
        
        if(templateId <= 0 && tabHojas.getSelectedIndex() == 0 && moduleBO.getHeight() > GlobalDefines.DRAWABLE_HEIGHT_PH){
            if (lstModules == null || lstModules.size() <= 0){
                JOptionPane.showMessageDialog(null, "El módulo que intenta agregar excede el tamaño de la hoja actual. Intente ingresar un módulo distinto");
                return false;
            }
        }

        //Verificar si se puede agregar modulo
        if (lstModules.size() > 0 && this.getDocBO().isCollaborative()) {
            JOptionPane.showMessageDialog(null, "Este documento solo puede contener un módulo ya que es de tipo Colaborativo");
            return false;
        }

        /**
         *
         * Verificar el tamaño de la hoja para hacer un salto
         */
        int nuevaHoja = 0;
        boolean b = addToNextPage(lstModules, moduleBO, tabHojas.getSelectedIndex() == 0);
        int antHoja = numHoja;

        if (b == true) {
            //checamos si hay espacio en la ultima hoja
            numHoja = tabHojas.getTabCount() - 1;
            numHoja = Integer.parseInt(tabHojas.getTitleAt(numHoja));
            if (numHoja > antHoja) {
                hojaBO = this.getDocBO().getMapHojas().get(numHoja);
                if (hojaBO == null) {
                    return false;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                b = addToNextPage(lstModules, moduleBO, (tabHojas.getTabCount() - 1) == 0);
            } else {
                b = true;
            }

            //No hay lugar en la ultima pagina
            if (b == true) {
                // Salto de linea
                // addHojaADocumentoExistente();
                hojaBO = createLastHoja();
                generarOrdenVisual();
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                numHoja = hojaBO.getHoja();
            }
        }
        // Misma pagina            
        lstModules.add(moduleBO);
        moduleBO.setHoja((short) numHoja);
        moduleBO.setTemplateId(templateId);
        contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        JPanel panModule = addLayout(contentPanel, moduleBO.getRootSection(), moduleBO);

        moduleBO.setPanModule(panModule);

        /*if (antHoja != numHoja) {
         showHoja((short) numHoja);
         tabHojas.setSelectedIndex(numHoja - 1);
         tabHojas.invalidate();
         tabHojas.repaint();
         }*/
        return true;
    }

    protected boolean addToNextPage(List<ModuleBO> lstModules, ModuleBO currentModule, boolean isFirstPage) {
        boolean b = false;
        short height = 0;
        for (int i = 0; i < lstModules.size(); i++) {
            height += lstModules.get(i).getHeight();
        }
        if ((height + currentModule.getHeight()) > (isFirstPage ? GlobalDefines.DRAWABLE_HEIGHT_PH : GlobalDefines.DRAWABLE_HEIGHT)) {
            b = true;
        } else {
            b = false;
        }
        return b;
    }

    public void addModulesInTemplate(List<Integer> modulesID) throws UnsupportedOperationException {

        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
        }
        addHojaADocumentoExistente();

        for (int i = 0; i < modulesID.size(); i++) {
            int moduleId = modulesID.get(i);
            if (this.getDocBO() == null) {
                throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
            }

            TemplateDAO tempDAO = new TemplateDAO();
            ModuleBO moduleBO = tempDAO.getModule(moduleId);
            if (moduleBO == null) {
                return;
            }

            int numHoja = tabHojas.getSelectedIndex() + 1;
            HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
            if (hojaBO == null) {
                return;
            }

            List<ModuleBO> lstModules = hojaBO.getLstModules();
            if (lstModules == null) {
                lstModules = new ArrayList<>();
                hojaBO.setLstModules(lstModules);
            }
            lstModules.add(moduleBO);
            moduleBO.setHoja((short) numHoja);
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            //contentPanel.setPreferredSize(new Dimension(700, lstModules.size() * 300));
            contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
            JPanel panModule = addLayout(contentPanel, moduleBO.getRootSection(), moduleBO);
            moduleBO.setPanModule(panModule);
        }

    }

    protected HojaBO createLastHoja() {
        int numHojas = numeroHojaMayor();//tabHojas.getTabCount();
        //numHojas = Integer.parseInt(tabHojas.getTitleAt(numHojas-1));
        short nuevaHoja = (short) (numHojas + 1);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int) nuevaHoja, hojaBO);

        JPanel panHoja = new JPanel();
        panHoja.setLayout(new BorderLayout());

        tabHojas.addTab("" + numHojas, panHoja);
        tabHojas.setTitleAt(tabHojas.getTabCount() - 1, "" + nuevaHoja);

        // showHoja(nuevaHoja);
        tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);
        return hojaBO;
        // addModule(moduleId);

    }

    protected void addHojaADocumentoExistente() {

        //Verificar si se puede agregar modulo
        if (this.getDocBO().isCollaborative()) {
            JOptionPane.showMessageDialog(null, "Este documento solo puede contener un módulo ya que es de tipo Colaborativo");
            return;
        }

        int numHojas = numeroHojaMayor();//tabHojas.getTabCount();
        //numHojas = Integer.parseInt(tabHojas.getTitleAt(numHojas-1));
        short nuevaHoja = (short) (numHojas + 1);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int) nuevaHoja, hojaBO);

        JPanel panHoja = new JPanel();
        panHoja.setLayout(new BorderLayout());

        tabHojas.addTab("" + numHojas, panHoja);
        tabHojas.setTitleAt(tabHojas.getTabCount() - 1, "" + nuevaHoja);

        showHoja(nuevaHoja);
        tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);
        
        generarOrdenVisual();
    }

    protected int numeroHojaMayor() {
        int retVal = 0;
        try {
            for (int i = 0; i < tabHojas.getTabCount(); i++) {
                int res = Integer.parseInt(tabHojas.getTitleAt(i));
                if (res > retVal) {
                    retVal = res;
                }
            }
            
            if(retVal < numHojaSig){
                retVal = numHojaSig;
            }else if (retVal > numHojaSig){
                numHojaSig = retVal;
            }
            
            numHojaSig++;
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        
        if(hojasDel != null && hojasDel.contains( String.valueOf( retVal + 1 ) )){
            retVal = numeroHojaMayor();
        }
        
        return retVal;
    }

    protected void deleteHojaADocumentoExistente() {

        if (tabHojas.getTabCount() <= 1) {
            JOptionPane.showMessageDialog(null, "La hoja no se puede eliminar ya que el documento no puede quedar sin hojas.");
            return;
        }

        /*for (int i = 0; i < listaTextos.size(); i++) {
            if (listaTextos.get(i) instanceof ScrCellText) {
                ScrCellText obj = (ScrCellText) listaTextos.get(i);
                if (obj.getSctPadre() != null || obj.getSctHijo() != null) {
                    JOptionPane.showMessageDialog(null, "La hoja no se puede eliminar ya que tiene texto concatenado.");
                    return;
                }
            }
        }*/
        
        if(!validarMovDelConcat()){
            JOptionPane.showMessageDialog(null, "La hoja no se puede eliminar ya que tiene texto concatenado.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar la hoja?.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (hojasDel == null) {
                hojasDel = new ArrayList<>();
            }
            hojasDel.add(tabHojas.getTitleAt(tabHojas.getSelectedIndex()));
            tabHojas.remove(tabHojas.getSelectedIndex());
            tabHojas.updateUI();
            
            generarOrdenVisual();
        }
    }
    
    private boolean validarMovDelConcat(){
        boolean retVal = true;
        if (getObjetosConc() != null) {
            boolean eliminar = true;
            Map<String, Map<String, StringBuilder>> mapConcat = getObjetosConc();
            
            for (Map.Entry<String, Map<String, StringBuilder>> ent : mapConcat.entrySet()){
                String key = ent.getKey();
                if (key.startsWith(getHojaAct() + "")) {
                    eliminar = false;
                    break;
                }
                Map<String, StringBuilder> mapConcatOrig = ent.getValue();
                if (mapConcatOrig != null) {
                    for (Map.Entry<String, StringBuilder> ent2 : mapConcatOrig.entrySet()) {
                        if (ent2.getKey().startsWith(getHojaAct() + "")) {
                            eliminar = false;
                            break;
                        }
                    }
                }
            }
            
            if (!eliminar) {
                retVal = false;
            }
        }
        
        return retVal;
    }

    protected void validDnD() {

        if(!validarMovDelConcat()){
            JOptionPane.showMessageDialog(null, "La hoja no se puede mover ya que tiene texto concatenado.");
            return;
        }
    
        isMovedPage = true;
        
    }

    protected void addHoja(Boolean nuevo) {
        if (getDocumentId() <= 0 && nuevo == false) {
            return;
        }
        int numHojas = numeroHojaMayor();//tabHojas.getTabCount();
        //numHojas = Integer.parseInt(tabHojas.getTitleAt(numHojas-1));
        short nuevaHoja = (short) (numHojas + 1);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int) nuevaHoja, hojaBO);

        JPanel panHoja = new JPanel();
        panHoja.setLayout(new BorderLayout());
        // 
        tabHojas.addTab("" + numHojas, panHoja);
        tabHojas.setTitleAt(tabHojas.getTabCount() - 1, "" + nuevaHoja);

        showHoja(nuevaHoja);
        tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);
        hojaAct = (short) (tabHojas.getTabCount() - 1);
        if (hojaAct == 0) {
            hojaAct = 1;
        }
    }

    protected void changeHoja() {
        if(isOpenDoc)
            return;
        
        int numHoja = tabHojas.getSelectedIndex();
        if (numHoja < 0) {
            return;
        }
        numHoja = Integer.parseInt(tabHojas.getTitleAt(numHoja));
        showHoja((short) numHoja);

    }

    private Map<String, StringBuilder> textConc;

    public Map<String, StringBuilder> getTextConc() {
        return textConc;
    }

    public void setTextConc(Map<String, StringBuilder> textConc) {
        this.textConc = textConc;
    }

    private Map<String, Map<String, StringBuilder>> objetosConc;

    public Map<String, Map<String, StringBuilder>> getObjetosConc() {
        return objetosConc;
    }

    public void setObjetosConc(Map<String, Map<String, StringBuilder>> objetosConc) {
        this.objetosConc = objetosConc;
    }

    private Map<String, StringBuilder> objectosDescConc;

    public Map<String, String> objectosDescConcInt;

    public Map<String, StringBuilder> getObjectosDescConc() {
        return objectosDescConc;
    }

    public void setObjectosDescConc(Map<String, StringBuilder> objectosDescConc) {
        this.objectosDescConc = objectosDescConc;
    }

    private short hojaAct;

    public short getHojaAct() {
        return hojaAct;
    }

    public void setHojaAct(short hojaAct) {
        this.hojaAct = hojaAct;
    }

    public void showFirstPage() {
        if (tabHojas == null || tabHojas.getTabCount() <= 0) {
            return;
        }

        tabHojas.setSelectedIndex(0);
    }

    protected void showHoja(short numHoja) {
        contObj = 0;
        hojaAct = numHoja;
        /*
         if (getDocumentId() <= 0) {
         return;
         }*/
        if (this.getDocBO() == null) {
            return;
        }
        int numHojas = tabHojas.getTabCount();
        //if (numHoja > numHojas) {
        //   return;
        //}
        contentPanel.removeAll();
        //listaTextos.clear();

        if (this.getDocBO().getMapHojas() == null) {
            return;
        }

        HojaBO hojaBO = this.getDocBO().getMapHojas().get((int) numHoja);
        if (hojaBO == null) {
            return;
        }

        List<ModuleBO> lstModules = hojaBO.getLstModules();
        JPanel panTmp = null;

        JPanel panHoja = (JPanel) tabHojas.getComponentAt(tabHojas.getSelectedIndex());
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        contentPanel.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        if (panHoja != null) {
            panHoja.removeAll();
            JScrollPane jScroll = new JScrollPane(this.contentPanel);
            // jScroll.setMaximumSize(new Dimension(700,GlobalDefines.MAX_HEIGHT_CELL));
            panHoja.add(jScroll);
        }

        short heightTot = 0;
        if (lstModules != null) {
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            for (ModuleBO moduleBO : lstModules) {
                Utilerias.pasarGarbageCollector();
                heightTot += moduleBO.getHeight();
                panTmp = addLayout(contentPanel, moduleBO.getRootSection(), moduleBO);
                if (moduleBO.isHasText() == false) {
                    panTmp.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    panTmp.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                }

                moduleBO.setPanModule(panTmp);
            }
        }

        if (tabHojas.getSelectedIndex() == 0 && heightTot > GlobalDefines.DRAWABLE_HEIGHT_PH && this.getDocBO().getMapHojas().size() == tabHojas.getTabCount()) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        JOptionPane.showMessageDialog(null, "La hoja contiene una plantilla que no es de primera hoja o los modulos agregados a esta hoja sobrepasan el tamaño \n"
                                + "configurado para una primera hoja. No podra guardar el documento con esta configuracion de hojas, mueva a esta \n"
                                + "posicion una hoja con plantilla de primera hoja o agrege solo los modulos necesarios para una primera hoja");
                    } catch (Exception ex) {
                        Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        }

//         tabHojas.add(contentPanel);
        //  showLayout( null , docBO.getRootSectionId() , docBO.getMapSectionsByParent() ,
        //            docBO.getMapSectionsById() ); 
    }

    private boolean primeraHojaValida() {
        if (tabHojas.getTabCount() < 0) {
            JOptionPane.showMessageDialog(null, "El documento no contiene hojas.");
            return false;
        }

        int numHoja = this.getDocBO().getCollaborativeType() == GlobalDefines.COLLAB_TYPE_MOD ? 1 : Integer.parseInt(tabHojas.getTitleAt(0));
       //  int numHoja =  GlobalDefines.COLLAB_TYPE_MOD.equals(this.getDocBO().getCollaborativeType())    ? 1 : Integer.parseInt(tabHojas.getTitleAt(0));

        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            JOptionPane.showMessageDialog(null, "La primera hoja esta vacia.");
            return false;
        }

        List<ModuleBO> lstModules = hojaBO.getLstModules();

        short heightTot = 0;
        if (lstModules != null) {
            for (ModuleBO moduleBO : lstModules) {
                heightTot += moduleBO.getHeight();
            }
        }

        if (heightTot > GlobalDefines.DRAWABLE_HEIGHT_PH) {

            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JOptionPane.showMessageDialog(null, "El documento no se puede guardar. La primera hoja contiene una plantilla que no es de primera hoja \n"
                                    + "o los modulos asignados a esta hoja sobrepasan el tamaño configurado para una primera hoja.");
                        } catch (Exception ex) {
                            Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            } catch (InterruptedException ex) {
                Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
            }

            return false;
        } else {
            return true;
        }

    }
    
    private void actualizarIdConcat() {
        int ordenHoja = 1;
        Map<String, Map<String, StringBuilder>> mapDestino = new HashMap<>();
        for (int i = 0; i < tabHojas.getTabCount(); i++) {
            Integer numHoja = Integer.valueOf(tabHojas.getTitleAt(i));
            if (!numHoja.equals(ordenHoja)) {
                Map<String, Map<String, StringBuilder>> mapDest = new HashMap<>();
                mapDest.putAll(getObjetosConc());

                for (Map.Entry<String, Map<String, StringBuilder>> entDestino : mapDest.entrySet()) {
                    if (numHoja.equals(Integer.valueOf(entDestino.getKey().substring(0, 1)))) {
                        String id = entDestino.getKey().substring(1);
                        Map<String, StringBuilder> mDes = entDestino.getValue();
                        getObjetosConc().remove(entDestino.getKey());
                        mapDestino.put(ordenHoja + id, mDes);
                    }
                }
            }
                
            ordenHoja++;
        }
        
        getObjetosConc().putAll(mapDestino);
        mapDestino = new HashMap<>();
        ordenHoja = 1;
        for (int i = 0; i < tabHojas.getTabCount(); i++) {
            Integer numHoja = Integer.valueOf(tabHojas.getTitleAt(i));
            if (!numHoja.equals(ordenHoja)) {
                Map<String, Map<String, StringBuilder>> mapDest = new HashMap<>();
                mapDest.putAll(getObjetosConc());
                
                for (Map.Entry<String, Map<String, StringBuilder>> entDestino : mapDest.entrySet()) {
                    for (Map.Entry<String, StringBuilder> entOrigen : entDestino.getValue().entrySet()) {
                        if (numHoja.equals(Integer.valueOf(entOrigen.getKey().substring(0, 1)))) {
                            String id = entOrigen.getKey().substring(1);
                            StringBuilder texto = entOrigen.getValue();
                            Map<String, StringBuilder> auxMap = new HashMap<>();
                            getObjetosConc().remove(entDestino.getKey());
                            auxMap.put(ordenHoja + id, texto);
                            mapDestino.put(entDestino.getKey(), auxMap);
                        }
                    }
                }
            }
                
            ordenHoja++;
        }
        
        getObjetosConc().putAll(mapDestino);
    }

    public boolean saveDocument() {
        if (this.getDocBO() == null) {
            return false;
        }

        if (!primeraHojaValida()) {
            return false;
        }
        
        if(this.getDocBO().isCollaborative() == false && isEmptyDocument()){
           
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    
                    @Override
                    public void run() {
                        try {
                            JOptionPane.showMessageDialog(null, "El documento no contiene hojas grabables, asegurese de tener una plantilla o modulo agregado a la hoja actual.");
                        } catch (Exception ex) {
                            Utilerias.logger(getClass()).error(ex);
                        }
                    }
                });
                return false;
            } catch (InterruptedException | InvocationTargetException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }
        
        DocumentDAO docDAO = new DocumentDAO();
        eliminarHojas();
        int idDoc = docDAO.saveDocument1(this.getDocBO(), true);
        setDocumentId(idDoc);
        Utilerias.pasarGarbageCollector();
        if (objetosConc != null) {
            actualizarIdConcat();
            docDAO.saveConcatObjsDocument(getDocumentId(), objetosConc);
        }

        if (this.getDocBO().isCollaborative() && (this.getDocBO().getCollaborativeType() == null || !this.getDocBO().getCollaborativeType().equals(GlobalDefines.COLLAB_TYPE_MOD))) {
            showDlgAddModuleColl(getLstModCollabByDoc(this.getDocBO()));
            return true;
        }

        Utilerias.pasarGarbageCollector();
        ordenarHojas();
        //docDAO.actualizarHojas(this.getDocBO());
        
        idxActResp = tabHojas.getSelectedIndex();
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    init();
                } catch (SQLException ex) {
                    Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return true;
    }
    
    public void recargarDocumento(){
        idxActResp = tabHojas.getSelectedIndex();
        MainView.main.getProgressBar().setVisible(true);
        MainView.main.getProgressBar().setIndeterminate(true);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    init();
                    MainView.main.getProgressBar().setVisible(false);
                } catch (SQLException ex) {
                    Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public boolean saveDocumentAs(DlgUpdate dl) {
        if (this.getDocBO() == null) {
            return false;
        }

        if (!primeraHojaValida()) {
            return false;
        }

        if (!overrideDocument(dl)) {
            return false;
        }

        DocumentDAO docDAO = new DocumentDAO();
        this.getDocBO().setDocumentId(0);
        setOpenPublish(false);

        eliminarHojas();
        int idDoc = docDAO.saveDocument1(this.getDocBO(), true);

        setDocumentId(idDoc);
        
        if (objetosConc != null) {
            docDAO.saveConcatObjsDocument(idDoc, objetosConc);
        }

        ordenarHojas();
        //docDAO.actualizarHojas(this.getDocBO());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    init();
                } catch (SQLException ex) {
                    Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        return true;
    }

//    public int saveDocument(boolean id) {
//        int idDoc = 0;
//        if (this.getDocBO() == null) {
//            return idDoc;
//        }
//        DocumentDAO docDAO = new DocumentDAO();
//        idDoc = docDAO.saveDocument(this.getDocBO(), id);
//        return idDoc;
//    }
    private void ordenarHojas() {
        DocumentDAO docDAO = new DocumentDAO();
        Map<Integer, List<Integer>> lstHojas = docDAO.getHojasDocumento(this.getDocBO());

        try {
            int cont = 1;
            for (int i = 0; i < tabHojas.getTabCount(); i++) {
                Integer pHoja = Integer.parseInt(tabHojas.getTitleAt(i));
                List<Integer> modules = lstHojas.get(pHoja);

                if (modules == null) {
                    continue;
                }

                StringBuilder docModIds = new StringBuilder();
                for (Integer module : modules) {
                    if (docModIds.toString().isEmpty()) {
                        docModIds.append(module.intValue());
                    } else {
                        docModIds.append(", ").append(module.intValue());
                    }
                }

                if (!docModIds.toString().isEmpty()) {
                    docDAO.ordenarHoja(docModIds.toString(), cont);
                    cont++;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void eliminarHojas() {
        if (hojasDel == null || hojasDel.size() <= 0) {
            return;
        }

        String ids = "";
        for (String s : hojasDel) {
            docBO.getMapHojas().remove(Integer.parseInt(s));
            if (docBO.getDocumentId() > 0) {
                if (ids.isEmpty()) {
                    ids += s;
                }
                ids += "," + s;
            }
        }

        if (docBO.getDocumentId() > 0 && !ids.isEmpty()) {
            DocumentDAO docDAO = new DocumentDAO();
            docDAO.eliminarHojas(ids, docBO);
        }
        //docDAO.actualizarHojas(docBO.getDocumentId());
        hojasDel = null;
    }
    
    private boolean isEmptyDocument() {
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();

        Integer numHoja = null;
        HojaBO hojaBO = null;
        while (it.hasNext()) {
            numHoja = (Integer) it.next();
            
            hojaBO = docBO.getMapHojas().get(numHoja);

            if (hojaBO != null 
                    && hojasDel.contains(String.valueOf(numHoja)) == false 
                    && hojaBO.getLstModules() != null
                    && hojaBO.getLstModules().size() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean overrideDocument(DlgUpdate dlgSave) {
        try {
            if (dlgSave.isOk() == false) {
                return false;
            }

            this.getDocBO().setDocumentName(dlgSave.getEdNombredoc().getText());

            int idLanguage;
            Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
            if (obj3 != null) {
                LanguageBO languageBO = (LanguageBO) obj3;
                idLanguage = languageBO.getIdLanguage();
                this.getDocBO().setIdLanguage(idLanguage);
            } else {
                return false;
            }

            int idSubject;
            //Object obj4 = dlgSave.getCboSubject().getSelectedItem();
            SubjectBO subjectBO = dlgSave.getSelectedValueSubject();
            if (subjectBO != null) {
                //subjectBO = (SubjectBO) obj4;
                idSubject = subjectBO.getIdSubject();
                this.getDocBO().setIdSubject(idSubject);
            } /*else {
             return false;
             }*/

            setDocumentName(dlgSave.getEdNombredoc().getText());
            this.getDocBO().setFileName(dlgSave.getEdFileName().getText());
            this.getDocBO().setFavorite(dlgSave.isFavorite().isSelected());
            this.getDocBO().setStatus("No enviado");

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        return true;
    }
    
    private void initConcatObj() {
        idOtrosMas = new ArrayList();
        idOtrosMasOtraHoja = new ArrayList();
        textNvoConcatenado = false;
        keyPressed = 0;
        strExcReajuste = new StringBuilder();
        textConc = null;
        objetosConc = null;
        objectosDescConc = null;
        objectosDescConcInt = null;
    }

    public boolean newDocument(boolean isCollaborative) {
        try {
            DlgSave dlgSave = new DlgSave(MainApp.getApplication().getMainFrame(), true);
            dlgSave.setLocationRelativeTo(dlgSave);
            dlgSave.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            //dlgSave.setTitle(GlobalDefines.TITULO_APP);
            dlgSave.setCollborative(isCollaborative);
            dlgSave.setVisible(true);
            if (dlgSave.isOk() == false) {
                return false;
            }

            RibbonMenu.Menu.setMenu(RibbonMenu.Menu.MENU.EDITAR);
            setOpenPublish(false);

            this.setDocBO(new DocumentBO());
            this.getDocBO().setDocumentName(dlgSave.getEdNombredoc().getText());
            this.getDocBO().setCollaborative(isCollaborative);

            int idms;
            Object obj = dlgSave.getCmbMercado().getSelectedItem();
            if (obj != null) {
                MarketBO marketb = (MarketBO) obj;
                idms = Integer.parseInt(marketb.getIdMiVector_real());
                this.getDocBO().setIdMarket(idms);
            } else {
                return false;
            }

            int idDocType;
            Object obj2 = dlgSave.getCmbTipo().getSelectedItem();
            DocumentTypeBO tipoBO = null;
            if (obj2 != null) {
                tipoBO = (DocumentTypeBO) obj2;
                idDocType = tipoBO.getIddocument_type();
                this.getDocBO().setIdDocType(idDocType);
            } else {
                return false;
            }

            int idLanguage;
            Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
            if (obj3 != null) {
                LanguageBO languageBO = (LanguageBO) obj3;
                idLanguage = languageBO.getIdLanguage();
                this.getDocBO().setIdLanguage(idLanguage);
            } else {
                return false;
            }

            int idSubject;
            SubjectBO subjectBO = dlgSave.getSelectedValueSubject();
            if (subjectBO != null) {
                idSubject = subjectBO.getIdSubject();
                this.getDocBO().setIdSubject(idSubject);
            } /*else {
             return false;
             }*/

            setDocumentName(dlgSave.getEdNombredoc().getText());
            this.getDocBO().setFileName(dlgSave.getEdFileName().getText());
            this.contentPanel.removeAll();
            this.tabHojas.removeAll();
            this.addHoja(true);
            this.getDocBO().setFavorite(dlgSave.isFavorite().isSelected());
            initConcatObj();
            MainView.main.initInfoLabel(tipoBO == null ? "" : tipoBO.getName(), subjectBO == null ? "" : subjectBO.getName(), dlgSave.getEdNombredoc().getText());
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        return true;
    }

    public boolean newDocument(int idMarket, int idDocumnetType) {
        try {
            DlgSave dlgSave = new DlgSave(MainApp.getApplication().getMainFrame(), true);
            dlgSave.setLocationRelativeTo(dlgSave);
            dlgSave.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            //dlgSave.setTitle(GlobalDefines.TITULO_APP);
            dlgSave.setCollborative(false);

            dlgSave.setMarketSel(idMarket);
            dlgSave.setDocTypeSel(idDocumnetType);

            dlgSave.setVisible(true);

            if (dlgSave.isOk() == false) {
                return false;
            }

            RibbonMenu.Menu.setMenu(RibbonMenu.Menu.MENU.EDITAR);

            this.setDocBO(new DocumentBO());
            this.getDocBO().setDocumentName(dlgSave.getEdNombredoc().getText());

            int idms;
            Object obj = dlgSave.getCmbMercado().getSelectedItem();
            if (obj != null) {
                MarketBO marketb = (MarketBO) obj;
                idms = Integer.parseInt(marketb.getIdMiVector_real());
                this.getDocBO().setIdMarket(idms);
            } else {
                return false;
            }

            int idDocType;
            Object obj2 = dlgSave.getCmbTipo().getSelectedItem();
            DocumentTypeBO tipoBO = null;
            if (obj2 != null) {
                tipoBO = (DocumentTypeBO) obj2;
                idDocType = tipoBO.getIddocument_type();
                this.getDocBO().setIdDocType(idDocType);
            } else {
                return false;
            }

            int idLanguage;
            Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
            if (obj3 != null) {
                LanguageBO languageBO = (LanguageBO) obj3;
                idLanguage = languageBO.getIdLanguage();
                this.getDocBO().setIdLanguage(idLanguage);
            } else {
                return false;
            }

            int idSubject;
            SubjectBO subjectBO = dlgSave.getSelectedValueSubject();
            if (subjectBO != null) {
                idSubject = subjectBO.getIdSubject();
                this.getDocBO().setIdSubject(idSubject);
            } /*else {
             return false;
             }*/

            setDocumentName(dlgSave.getEdNombredoc().getText());
            this.getDocBO().setFileName(dlgSave.getEdFileName().getText());
            this.contentPanel.removeAll();
            this.tabHojas.removeAll();
            this.addHoja(true);
            this.getDocBO().setFavorite(dlgSave.isFavorite().isSelected());
            initConcatObj();
            MainView.main.initInfoLabel(tipoBO == null ? "" : tipoBO.getName(), subjectBO == null ? "" : subjectBO.getName(), dlgSave.getEdNombredoc().getText());
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        return true;
    }

    protected JPanel addLayout(JPanel panelParent, ModuleSectionBO sectionBO, ModuleBO moduleBO) {
        Utilerias.logger(getClass()).debug("Type=" + sectionBO.getType());
        if (sectionBO == null) {
            return null;
        }

        List<ModuleSectionBO> sectionsChilds = sectionBO.getChildSectionsAsList();

        int width = 0;
        int height = 0;
        meterText++;

        if (GlobalDefines.SEC_TYPE_CELL.equals(sectionBO.getType())) {
            if (panelParent == null) {
                JPanel panelTmp = new JPanel();
                panelTmp.setLayout(new GridLayout());

                if (sectionBO.getContentType() == null || (sectionBO.getContentType().equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT) == false
                        && sectionBO.getContentType().equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT_BULLET) == false)) {
                    panelTmp.add(new ScrCell(this, sectionBO, moduleBO ));
                }

                contentPanel.add(panelTmp);

                //Agregar al nombre del panel el moduleID
                int idModule = moduleBO.getModuleId();
                panelTmp.setName("ModuleID=" + idModule);

                if (moduleBO.isHasText() == false) {
                    panelTmp.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                }

            } else {
                if (sectionBO.getContentType() == null || (sectionBO.getContentType().equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT) == false
                        && sectionBO.getContentType().equals(GlobalDefines.SEC_CONTENT_TYPE_TEXT_BULLET) == false)) {

                    ScrCell scrCell = new ScrCell(this, sectionBO, moduleBO);

                    if (moduleBO.isHasText() == false) {
                        scrCell.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    }

                    panelParent.add(scrCell);

                }
            }
            return null;
        }

        if (null != sectionBO.getType()) {
            switch (sectionBO.getType()) {
                case GlobalDefines.SEC_TYPE_COLUMN:
                    width = sectionBO.getWidth();
                    break;
                case GlobalDefines.SEC_TYPE_SHEET:
                    height = sectionBO.getHeight();
                    if (moduleBO.isHasText() == false) {
                        height = 30;
                    }
                    break;
            }
        }

        JPanel panel = new JPanel();
        panel.setComponentPopupMenu(popHojas);
        if (panelParent != null) {
            panelParent.add(panel);
        } else {
            contentPanel.add(panel);
        }

        // panel.sets
        // GridBagLayout layout = new GridBagLayout();
        GridLayout layout = new GridLayout();

        // GridBagConstraints gbc = new GridBagConstraints();
        if (GlobalDefines.SEC_TYPE_COLUMN.equals(sectionBO.getType())) {
            // gbc.fill = GridBagConstraints.VERTICAL;     
            layout = new GridLayout(sectionsChilds.size(), 1);
        } else if (GlobalDefines.SEC_TYPE_SHEET.equals(sectionBO.getType())) {
            // gbc.fill = GridBagConstraints.HORIZONTAL;       
            layout = new GridLayout(1, sectionsChilds.size());

        }
        panel.setLayout(layout);

        int i = 0;
        if (sectionsChilds != null) {
            for (ModuleSectionBO secTmp : sectionsChilds) {
                Utilerias.logger(getClass()).debug("GetType-secTmp=" + secTmp.getType());
                // Utilerias.logger(getClass()).info("TemplateId=" + secTmp.getTemplateId());
                Utilerias.logger(getClass()).debug("SectionId=" + secTmp.getSectionId());
                Utilerias.logger(getClass()).debug("Width=" + secTmp.getWidth());
                Utilerias.logger(getClass()).debug("Height=" + secTmp.getHeight());
                Utilerias.logger(getClass()).debug("ContentType-secTmp=" + secTmp.getContentType());
                Utilerias.logger(getClass()).debug("\n");
                if (secTmp == null) {
                    continue;
                }
                JPanel content = null;

                if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType()) && GlobalDefines.SEC_CONTENT_TYPE_TEXT.equals(secTmp.getContentType())) {
                    content = new ScrCellText(this, secTmp, moduleBO);
                    //content = new ScrCellTextBullet(this, secTmp);        
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    listaTextos.add(content);
                } else if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType()) && GlobalDefines.SEC_CONTENT_TYPE_TEXT_BULLET.equals(secTmp.getContentType())) {
                    //content = new ScrCellText(this, secTmp);
                    content = new ScrCellTextBullet(this, secTmp, moduleBO);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    listaTextos.add(content);
                } else if (GlobalDefines.SEC_TYPE_CELL.equals(sectionBO.getType())) {
                    content = new ScrCell(this, null,moduleBO);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    content.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    content.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                } else if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType()) && GlobalDefines.SEC_CONTENT_TYPE_CALENDAR.equals(secTmp.getContentType())) {
                    content = new ScrCalendarTotal(this, secTmp);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    listaTextos.add(content);
                } else if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType()) && GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_WEEK.equals(secTmp.getContentType())) {
                    content = new ScrCalendarWeek(this, secTmp);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    listaTextos.add(content);
                } else if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType()) && GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_MONTH.equals(secTmp.getContentType())) {
                    content = new ScrCalendarMonth(this, secTmp);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    listaTextos.add(content);
                } else {
                    content = new JPanel(new GridLayout(1, 1));
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                    content.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    content.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                }

                if (GlobalDefines.SEC_TYPE_COLUMN.equals(sectionBO.getType())) {
                    panel.add(content);
                } else if (GlobalDefines.SEC_TYPE_SHEET.equals(sectionBO.getType())) {
                    panel.add(content);
                }
                addLayout(content, secTmp, moduleBO);
                i++;
            }
        }
        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popHojas = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        contentPanel = new javax.swing.JPanel();
        tabHojas = new javax.swing.JTabbedPane();

        jMenuItem1.setText("Agregar hoja");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        popHojas.add(jMenuItem1);

        jMenuItem2.setText("Eliminar hoja");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        popHojas.add(jMenuItem2);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        contentPanel.setRequestFocusEnabled(false);
        contentPanel.setLayout(new java.awt.GridLayout(1, 0));

        setLayout(new java.awt.BorderLayout());

        tabHojas.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabHojas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabHojas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabHojasStateChanged(evt);
            }
        });
        tabHojas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tabHojasMouseDragged(evt);
            }
        });
        add(tabHojas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        addHojaADocumentoExistente();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tabHojasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabHojasStateChanged
        Utilerias.pasarGarbageCollector();
        changeHoja();
    }//GEN-LAST:event_tabHojasStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        deleteHojaADocumentoExistente();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void tabHojasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabHojasMouseDragged
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        validDnD();
    }//GEN-LAST:event_tabHojasMouseDragged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu popHojas;
    private javax.swing.JTabbedPane tabHojas;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void saveTexts() {
        try {

            List<Integer> idGuardados = new ArrayList<>();

            for (int i = 0; i < listaTextos.size(); i++) {
                int obJectID_ = 0;
                if (listaTextos.get(i) instanceof ScrCellText) {
                    ScrCellText obj = (ScrCellText) listaTextos.get(i);
                    obJectID_ = obj.getObjectId();
                    //Agregar a lista 
                    idGuardados.add(obJectID_);
                    obj.actualizaText();
                } else if (listaTextos.get(i) instanceof ScrCellTextBullet) {
                    ScrCellTextBullet obj = (ScrCellTextBullet) listaTextos.get(i);
                    obJectID_ = obj.getObjectId();
                    //Agregar a lista 
                    idGuardados.add(obJectID_);
                    obj.actualizaText();
                }
                Utilerias.logger(getClass()).info("Objeto con ID : " + obJectID_);
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);

        }
    }

    /**
     * @return the docBO
     */
    public DocumentBO getDocBO() {
        return docBO;
    }

    /**
     * @param docBO the docBO to set
     */
    public void setDocBO(DocumentBO docBO) {
        this.docBO = docBO;
    }

    private int contObj;

    public int getContObj() {
        return contObj;
    }

    public void setContObj(int contObj) {
        this.contObj = contObj;
    }

    private void showDlgAddModuleColl(Map<Integer, String> lDocModCo) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    DlgModuloColaborativo dlgModCol = new DlgModuloColaborativo(null, true, lDocModCo.size() == 1);
                    dlgModCol.setlDocModCollab(lDocModCo);
                    dlgModCol.setAdd(true);
                    dlgModCol.setDataModuleDocument(getDocBO());
                    Utilerias.centreDialog(dlgModCol, true);
                    dlgModCol.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
                    dlgModCol.setVisible(true);

                    if (dlgModCol.isAgregado()) {
                        MainView.main.viewQuickViewPanel();
                        MainView.main.getScrDocument().setDocBO(null);
                        MainView.main.getMainPanel().removeAll();
                    }

                } catch (Exception ex) {
                    Logger.getLogger(ScrDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private Map<Integer, String> getLstModCollabByDoc(DocumentBO docBO) {
        Map<Integer, String> retVal = new HashMap<>();;
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();
        TemplateDAO tempDAO = new TemplateDAO();

        try {
            Integer numHoja = null;
            HojaBO hojaBO = null;
            List<ModuleBO> lstModules = null;
            if (it.hasNext()) {
                numHoja = (Integer) it.next();
                hojaBO = docBO.getMapHojas().get(numHoja);

                if (hojaBO != null) {
                    lstModules = hojaBO.getLstModules();
                    for (ModuleBO moduleBO : lstModules) {
                        ModuleBO modName = tempDAO.getModule(moduleBO.getModuleId());
                        retVal.put(moduleBO.getDocumentModuleId(), modName.getName());
                    }
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        return retVal;
    }

    public void setTabHojas(JTabbedPane tabHojas) {
        this.tabHojas = tabHojas;
    }

    public List<String> getHojasDel() {
        return hojasDel;
    }

    public void setHojasDel(List<String> hojasDel) {
        this.hojasDel = hojasDel;
    }
    
    
}
