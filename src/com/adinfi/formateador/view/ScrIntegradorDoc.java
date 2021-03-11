
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocCollabCandidateBO;
import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.editor.HTMLDocument;
import com.adinfi.formateador.editor.PdfDocument;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.MergePDF;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.view.administration.tablemodel.SearchTableModel;
import com.adinfi.formateador.view.publish.SendPublish;
import com.itextpdf.text.DocumentException;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.kohsuke.rngom.digested.Main;

/**
 *
 * @author Desarrollador
 */
public class ScrIntegradorDoc extends javax.swing.JPanel {

    private JXTable tableCand;
    private DocumentCollabBO document = null;
    CollabDocsCandTModel modelTableCand;

    JXTable listDocuments;
    CollabDocsLModel modelListDocs;

    List<DocCollabCandidateBO> lstCandidates;
    Map<Integer, DocCollabCandidateBO> mapOrigCandidates;
    List<DocumentCollabItemBO> lstCollabItems;

    Map<Integer, DocCollabCandidateBO> mapCandDelete;
    Map<Integer, DocCollabCandidateBO> mapCandAdd;

    public DocumentBO getDocument() {
        
        return document;
        
    }

    public void newDocument(DocumentCollabBO document) {
        this.document = document;
        listDocuments.removeAll();
        tableCand.removeAll();
        lstCollabItems = new ArrayList<>();
        this.document.setLstDocumentCollab(lstCollabItems);
        this.modelListDocs.setList(lstCollabItems);

        loadCandidateDocs();
        initCombos();
        jButton3.setEnabled(false);
        saveDocument();
    }

    private int numHojas = 0;

    ModuleBO addModule2(int moduleId, int documentModuleId) throws UnsupportedOperationException , Exception {
        if (this.document == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);

        }

        DocumentDAO docDAO = null;
        ModuleBO moduleBO;
        if (documentModuleId > 0) {
            docDAO = new DocumentDAO();
            moduleBO = docDAO.getDocModule(documentModuleId);
            moduleBO.setDocumentModuleId(0);
            moduleBO.setOrigDocModuleId(documentModuleId);
            resetModule(moduleBO);

        } else {
            TemplateDAO tempDAO = new TemplateDAO();
            moduleBO = tempDAO.getModule(moduleId);
        }

        if (moduleBO == null) {
            return null;
        }

        if (this.document.getMapHojas() == null) {
            this.document.setMapHojas(new HashMap<Integer, HojaBO>());
            createLastHoja();
        }

        if (this.document.getMapHojas().size() == 0) {
            createLastHoja();
        }

        HojaBO hojaBO = this.document.getMapHojas().get(numHojas);
        if (hojaBO == null) {
            return null;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
        }

        boolean b = addToNextPage(lstModules, moduleBO);
        int antHoja = numHojas - 1;

        if (b == true) {
            //checamos si hay espacio en la ultima hoja
            if (numHojas > antHoja) {
                hojaBO = this.document.getMapHojas().get(numHojas);
                if (hojaBO == null) {
                    return null;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                b = addToNextPage(lstModules, moduleBO);
            } else {
                b = true;
            }

            //No hay lugar en la ultima pagina
            if (b == true) {
                // Salto de linea
                hojaBO = createLastHoja();
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
            }
        }
        // Misma pagina          
        moduleBO.setHoja((short) numHojas);
        lstModules.add(moduleBO);
        hojaBO.setLstModules(lstModules);
        if (document.getMapHojas().containsKey(numHojas)) {
            document.getMapHojas().replace(numHojas, hojaBO);
        } else {
            document.getMapHojas().put(numHojas, hojaBO);
        }

        return moduleBO;

    }

    ModuleBO addModule3(ModuleBO bo) throws UnsupportedOperationException {
        if (this.document == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);

        }

        DocumentDAO docDAO = null;
        ModuleBO moduleBO;
        if (bo.getDocumentModuleId() > 0) {
            docDAO = new DocumentDAO();
            //moduleBO=docDAO.getDocModule(documentModuleId); 
            moduleBO = bo;
            moduleBO.setDocumentModuleId(0);
            moduleBO.setOrigDocModuleId(bo.getDocumentModuleId());
            resetModule(moduleBO);

        } else {
            TemplateDAO tempDAO = new TemplateDAO();
            moduleBO = tempDAO.getModule(bo.getDocumentModuleId());

        }

        if (moduleBO == null) {
            return null;
        }

        if (this.document.getMapHojas() == null) {
            this.document.setMapHojas(new HashMap<Integer, HojaBO>());
            createLastHoja();
        }

        if (this.document.getMapHojas().size() == 0) {
            createLastHoja();
        }

        HojaBO hojaBO = this.document.getMapHojas().get(numHojas);
        if (hojaBO == null) {
            return null;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
        }

        boolean b = addToNextPage(lstModules, moduleBO);
        int antHoja = numHojas - 1;

        if (b == true) {
            //checamos si hay espacio en la ultima hoja
            if (numHojas > antHoja) {
                hojaBO = this.document.getMapHojas().get(numHojas);
                if (hojaBO == null) {
                    return null;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                b = addToNextPage(lstModules, moduleBO);
            } else {
                b = true;
            }

            //No hay lugar en la ultima pagina
            if (b == true) {
                // Salto de linea
                hojaBO = createLastHoja();
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
            }
        }
        // Misma pagina          
        moduleBO.setHoja((short) numHojas);
        lstModules.add(moduleBO);
        hojaBO.setLstModules(lstModules);
        if (document.getMapHojas().containsKey(numHojas)) {
            document.getMapHojas().replace(numHojas, hojaBO);
        } else {
            document.getMapHojas().put(numHojas, hojaBO);
        }

        return moduleBO;

    }

    protected HojaBO createLastHoja() {
        numHojas++;
        short nuevaHoja = (short) (numHojas);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.document.getMapHojas().put((int)nuevaHoja, hojaBO);

        return hojaBO;

    }

    protected boolean addToNextPage(List<ModuleBO> lstModules, ModuleBO currentModule) {
        boolean b = false;
        short height = 0;
        for (int i = 0; i < lstModules.size(); i++) {
            height += lstModules.get(i).getHeight();
        }
        if ((height + currentModule.getHeight()) > GlobalDefines.DRAWABLE_HEIGHT) {
            b = true;
        } else {
            b = false;
        }
        return b;
    }

    protected void resetModule(ModuleBO module) {
        if (module == null) {
            return;
        }

        module.setDocumentModuleId(0);
        if (module.getRootSection() != null) {
            resetSection(module.getRootSection());
        }
    }

    protected void resetSection(ModuleSectionBO modSection) {
        if (modSection == null) {
            return;
        }

        List<ObjectInfoBO> lstObjects;
        List<ModuleSectionBO> lstSecChilds;
        if (GlobalDefines.SEC_TYPE_CELL.equals(modSection.getType())) {
            lstObjects = modSection.getLstObjects();
            if (lstObjects != null) {
                for (ObjectInfoBO object : lstObjects) {
                    object.setObjectId(0);
                }
            }
        } else {
            if (modSection.getChildSectionsAsList() != null) {
                lstSecChilds = modSection.getChildSectionsAsList();
                for (ModuleSectionBO secChild : lstSecChilds) {
                    resetSection(secChild);
                }
            }

        }

    }

    
    public void saveAs(DocumentCollabBO document) {
        CollaborativesDAO collaborativesDAO=new CollaborativesDAO();
        
        try{
           CollabDocsCandTModel  modelCand = ( CollabDocsCandTModel ) tableCand.getModel();
           List<DocCollabCandidateBO> lst= modelCand.getList();
           boolean salvo= collaborativesDAO.saveCollabDocAs(this.document, document , lst  );
            
           if(salvo==true){
               this.openDocument(this.document);
               this.loadDocument();
               MainView.main.setTitle(GlobalDefines.TITULO_APP + "  " + this.document.getDocumentName());
               Utilerias.showMessage(MainView.main, "Documento guardado correctamente", JOptionPane.INFORMATION_MESSAGE);
           }
        }catch(Exception e){
            e.printStackTrace();
        }
           
    }
    
    public void openDocument(DocumentCollabBO document) {

        if (document == null || document.isCollaborative() == false) {
            return;
        }

        mapCandDelete = new TreeMap<>();
        mapCandAdd = new TreeMap<>();

        this.document = document;
        listDocuments.removeAll();
        tableCand.removeAll();
        lstCollabItems = document.getLstDocumentCollab();
        if (lstCollabItems == null) {
            lstCollabItems = new ArrayList<>();
            document.setLstDocumentCollab(lstCollabItems);
        }
        // this.document.setLstDocumentCollab(lstCollabItems);
        this.modelListDocs.setList(lstCollabItems);
        
       

        loadCandidateDocs();
        initCombos();
        try {
            AddModulesWorker workerModules = new AddModulesWorker();
            MainView.main.getProgressBar().setIndeterminate(true);
            MainView.main.getProgressBar().setVisible(true);
            workerModules.execute();
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        jButton3.setEnabled(true);
        this.modelListDocs.fireTableDataChanged();
    }

    public void initCombos() {

        this.cmbMercado.removeAllItems();
        this.cmbTipDoc.removeAllItems();

        this.cmbMercado.addItem(this.document.getMarketBO());
        this.cmbTipDoc.addItem(this.document.getTipoBO());

        this.cmbMercado.setEnabled(false);
        this.cmbTipDoc.setEnabled(false);

        this.cmbMercado.setSelectedIndex(0);
        this.cmbTipDoc.setSelectedIndex(0);

    }

    /**
     * Creates new form ScrIntegradorDoc
     */
    public ScrIntegradorDoc() {
        initComponents();
        createTableDocCand();
        createListDocs();
        //  loadDocument();
    }

    public void loadDocument() {

        if (this.document == null || this.document.isCollaborative() == false) {
            return;
        }
        
        List<String> titulo = Utilerias.getTitleProperties(this.document);
        if(titulo!= null && titulo.size() >= 3){
            MainView.main.initInfoLabel(titulo.get(0), titulo.get(1), titulo.get(2));
        }else{
            MainView.main.initInfoLabel("", "", "");
        }

        mapCandDelete = new TreeMap<>();
        mapCandAdd = new TreeMap<>();

        loadCandidateDocs();

    }

    protected void loadDocsCollab() {
        if (this.document == null) {
            return;
        }

        if (this.document.getLstDocumentCollab() == null || this.document.getLstDocumentCollab().size() <= 0) {
            return;
        }

        this.lstCollabItems = document.getLstDocumentCollab();

    }
    
    
    
    
/*
    private void createListDocs() {

        // boilerplate table-setup; this would be the same for a JTable
        listDocuments = new JXList();
        listDocuments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDocuments.setVisibleRowCount(25);
        modelListDocs = new CollabDocsLModel();
        //model.loadData();
        listDocuments.setModel(modelListDocs);
        scrollDocuments.setViewportView(listDocuments);

    } */
    
    
    private void createListDocs() {

        listDocuments = new JXTable();
        listDocuments.setHorizontalScrollEnabled(true);
        listDocuments.setColumnControlVisible(true);
        int margin = 5;
        listDocuments.packTable(margin);
        listDocuments.setColumnControlVisible(false);
        MouseMotionAdapter mml = new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (listDocuments.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                    listDocuments.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    listDocuments.setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        listDocuments.addMouseMotionListener(mml);
        listDocuments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        listDocuments.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //jxTableMouseClicked(evt);
            }
        });

        scrollDocuments.setViewportView(listDocuments);

        modelListDocs = new CollabDocsLModel();
       // modelTableCand = new CollabDocsCandTModel();
        listDocuments.setModel(modelListDocs);
        listDocuments.getColumn(0).setPreferredWidth(260);
        listDocuments.getColumn(1).setPreferredWidth(480);
        listDocuments.getColumn(2).setPreferredWidth(300);
        listDocuments.setSortable(false);
        // Utilerias.formatTablex(tableCand);
    }
    
    
    
    
    
    

    private void createTableDocCand() {

        tableCand = new JXTable();
        tableCand.setHorizontalScrollEnabled(true);
        tableCand.setColumnControlVisible(true);
        int margin = 5;
        tableCand.packTable(margin);
        tableCand.setColumnControlVisible(false);
        MouseMotionAdapter mml = new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (tableCand.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                    tableCand.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tableCand.setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        tableCand.addMouseMotionListener(mml);
        tableCand.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableCand.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //jxTableMouseClicked(evt);
            }
        });

        scrollTableCand.setViewportView(tableCand);

        modelTableCand = new CollabDocsCandTModel();
        tableCand.setModel(modelTableCand);
        tableCand.getColumn(0).setPreferredWidth(25);
         tableCand.getColumn(1).setPreferredWidth(200);
          tableCand.getColumn(2).setPreferredWidth(450);
           tableCand.getColumn(3).setPreferredWidth(200);
         tableCand.getColumn(4).setPreferredWidth(100);          
         tableCand.getColumn(5).setPreferredWidth(100);                   
        
        // Utilerias.formatTablex(tableCand);
    }

    protected void loadCandidateDocs() {
        DocumentSearchBO docSearchBO = new DocumentSearchBO();
        docSearchBO.setIdMarket(this.document.getIdMarket());
        docSearchBO.setIdDocType(this.document.getIdDocType());
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        ScrIntegradorDoc.SearchDocumentWorker worker = new ScrIntegradorDoc.SearchDocumentWorker(MainView.main.getProgressBar(), docSearchBO);
        worker.execute();

    }

    protected void sendPublish() {
        
        System.out.println(this.listDocuments);
        List<DocumentCollabItemBO> lDocColItemBO = document.getLstDocumentCollab();
        int iddoc = lDocColItemBO.get(0).getDocumentId();
        System.out.println(lDocColItemBO.get(0).getDocumentName());
        
        if (MainView.main.getScrDocument() == null) {
            MainView.main.setDocument(new ScrDocument());
        }
        DocumentDAO docDAO = new DocumentDAO();
        document.setPublishName(docDAO.getPublishDocumentName(document.getDocumentId()));
        
        if(listDocuments != null){
            document.setLstDocumentCollab(getLstDocCollabItems());
        }
        
        MainView.main.getScrDocument().setDocBO(document);
        SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }
    
    private List<DocumentCollabItemBO> getLstDocCollabItems(){
        CollabDocsLModel lModel = (CollabDocsLModel) listDocuments.getModel();
        List<DocumentCollabItemBO> lstItemBO = lModel.getList();
        DocumentDAO dao = new DocumentDAO();
        
        for (DocumentCollabItemBO itemBO : lstItemBO) {
            if(itemBO.getItemDocumentId() == 0){
                Integer[] res = dao.getDocumentIdByVersion(itemBO.getItemDocVersionId());
                if(res != null && res.length > 1 && res[0].intValue() > 0){
                    itemBO.setItemDocumentId(res[0].intValue());
                    itemBO.setItemVersion(res[1].intValue());
                }
            }
        }
        
        return lstItemBO;
    }
    

    protected void saveDocument() {

        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        ScrIntegradorDoc.SaveDocumentWorker worker = new ScrIntegradorDoc.SaveDocumentWorker(MainView.main.getProgressBar());
        worker.execute();

    }
    
    
    
    protected void saveAs() {

        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        ScrIntegradorDoc.SaveDocumentWorker worker = new ScrIntegradorDoc.SaveDocumentWorker(MainView.main.getProgressBar());
        worker.execute();

    }    
    
    
    

    protected void removeDocument() {
        if (this.listDocuments.getRowCount() <= 0) {
            return;
        }

        if (this.listDocuments.getSelectedRowCount() <=0 ) {
            return;
        }

        int idx = this.listDocuments.getSelectedRow() ;
        CollabDocsLModel lModel = (CollabDocsLModel) this.listDocuments.getModel();
        List<DocumentCollabItemBO> data = lModel.getList();
        DocumentCollabItemBO docCollBO = data.get(idx);
        data.remove(idx);
        lModel.refreshData();
       // lModel.remove(idx);
        lModel.fireTableDataChanged();

        if (docCollBO != null) {
            if (this.mapOrigCandidates != null) {
                DocCollabCandidateBO docCand = this.mapOrigCandidates.get(docCollBO.getItemDocVersionId());
                CollabDocsCandTModel model = (CollabDocsCandTModel) tableCand.getModel();
                List<DocCollabCandidateBO> list = model.getList();

                if (docCand != null) {
                    if (list != null) {
                        docCand.setCheck(false);
                        list.add(docCand);
                        model.refreshData();
                        model.fireTableDataChanged();
                        this.mapCandDelete.remove(docCand.getDocVersionId());
                        // this.mapCandAdd.put(docCand.getDocumentTypeId()   , docCand);
                    }

                } else {
                    docCand = new DocCollabCandidateBO();
                    docCand.setDocVersionId(docCollBO.getItemDocVersionId());
                    docCand.setCandName(docCollBO.getDocumentName());
                    docCand.setTipo(GlobalDefines.COLLAB_TYPE_DOC);
                    this.mapCandAdd.put(docCand.getDocVersionId(), docCand);
                    docCand.setCandName(docCollBO.getDocumentName());
                    docCand.setDocumentTypeId(this.document.getTipoBO().getIddocument_type());
                    docCand.setMarketId(this.document.getMarketBO().getIdMiVector_real() != null ? Integer.parseInt(this.document.getMarketBO().getIdMiVector_real()) : this.document.getMarketBO().getIdMarket());
                    docCand.setDocVersionId(docCollBO.getItemDocVersionId());
                    docCand.setCandSubject(docCollBO.getTema());
                    docCand.setCandAuthor(docCollBO.getAutor());
                    docCand.setCandTypeName(docCollBO.getDocTypeName());
                    docCand.setFileName(docCollBO.getFileName());
                    docCand.setDocVersionTarget( this.document.getDocVersionId() );

                    docCand.setCheck(false);
                    list.add(docCand);
                    model.refreshData();
                    model.fireTableDataChanged();

                    //  docCand.setDocumentTypeId( docCollBO. );
                }

            }

        }

    }

    protected void moveUp() {

        if (this.listDocuments.getRowCount() <= 0) {
            return;
        }

        if (this.listDocuments.getSelectedRowCount() <= 0) {
            return;
        }

        /*
        if (this.listDocuments.getSelectedValue() == null) {
            return;
        } */

        int idx = this.listDocuments.getSelectedRow();

        CollabDocsLModel lModel = (CollabDocsLModel) this.listDocuments.getModel();
        List<DocumentCollabItemBO> data = lModel.getList();
        DocumentCollabItemBO docCollBO1 = data.get(idx - 1);
        DocumentCollabItemBO docCollBO2 = data.get(idx);
        data.set(idx - 1, docCollBO2);
        data.set(idx, docCollBO1);
        lModel.refreshData();
        
        /*
        lModel.setValueAt(data, idx, idx);
        lModel.setElementAt(docCollBO2, idx - 1);
        lModel.setElementAt(docCollBO1, idx);
        this.listDocuments.setSelectedIndex(idx - 1); */
        lModel.fireTableDataChanged();
        this.listDocuments.setRowSelectionInterval(idx-1, idx-1);

    }

    protected void moveDown() {

        if (this.listDocuments.getRowCount() <= 0) {
            return;
        }

        if (this.listDocuments.getSelectedRowCount() <=0) {
            return;
        }

        if (this.listDocuments.getSelectedRow() >= this.listDocuments.getRowCount() - 1) {
            return;
        }

        int idx = this.listDocuments.getSelectedRow();

        CollabDocsLModel lModel = (CollabDocsLModel) this.listDocuments.getModel();
        List<DocumentCollabItemBO> data = lModel.getList();
        DocumentCollabItemBO docCollBO1 = data.get(idx);
        DocumentCollabItemBO docCollBO2 = data.get(idx + 1);
        
        data.set(idx, docCollBO2);
        data.set( idx + 1, docCollBO1);

        /*
        lModel.setElementAt(docCollBO2, idx);
        lModel.setElementAt(docCollBO1, idx + 1);
        this.listDocuments.setSelectedIndex(idx + 1); */
        lModel.refreshData();
        
        lModel.fireTableDataChanged();
        this.listDocuments.setRowSelectionInterval(idx + 1, idx + 1);

    }

    protected void htmlPreview() {
        try {
            //URI uri = URI.create(s);
            if (this.document.getDocumentId() <= 0) {
                return;
            }

            HTMLViewWorker workerHTML = new HTMLViewWorker(this.document.getDocumentId(), listDocuments);
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            workerHTML.execute();
            /*HTMLDocument html = new HTMLDocument();
            String fileName = html.createHTMLSemanario(this.document.getDocumentId(), null);
            fileName = "file:///" + fileName;
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.openURLinBrowser(fileName);*/

        } catch (Exception e) {

        }

    }
    
    protected void pdfPreview() {

        if (this.document.getDocumentId() <= 0) {
            return;
        }
        
        PDFCreatorCollaborativeWorker collabWorker = new PDFCreatorCollaborativeWorker(this.document.getDocumentId(), this.document, listDocuments);
        MainView.main.getProgressBar().setVisible(true);
        MainView.main.getProgressBar().setIndeterminate(true);
        collabWorker.execute();
/*
        try {
            PdfDocument pdfDoc = new PdfDocument();
            String fileName = pdfDoc.createSemanarioPdfDocument(this.document.getDocumentId());

            InputStream inputStream = new FileInputStream(fileName);
            SwingController controller = new SwingController();
            SwingViewBuilder factory = new SwingViewBuilder(controller);
            JPanel panel = factory.buildViewerPanel();

            PDFViewer viewer = new PDFViewer();
            viewer.setViewer(panel);
            controller.openDocument(inputStream, "", "");
            controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false);

            viewer.setBounds(new Rectangle(600, 400));
            Utilerias.centreFrame(viewer, true);
            viewer.setVisible(true);

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }*/

    }

    protected void selectAll() {

        if (tableCand.getRowCount() <= 0) {
            return;
        }

        CollabDocsCandTModel model = (CollabDocsCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list = model.getList();
        if (list == null) {
            return;
        }

        for (DocCollabCandidateBO docCand : list) {
            docCand.setCheck(chkSelectAll.isSelected());

        }

        model.refreshData();
        model.fireTableDataChanged();

    }

    protected void borrarSelected() {
        if (tableCand.getRowCount() <= 0) {
            return;
        }
        
        int reply = JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea eliminar el documento  ?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (reply != JOptionPane.YES_OPTION) {            
            //TODO:Mandar a ejecutar el metodo de eliminar modulo
            //deleteModule(this);
            return;
        }
        
        
        

        int index = tableCand.getSelectedRow();
        CollabDocsCandTModel model = (CollabDocsCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list = model.getList();
        if (list == null) {
            return;
        }

        DocumentCollabItemBO docCollabItem = null;
        Map<Integer, DocCollabCandidateBO> lstDel = new TreeMap<>();
        CollabDocsLModel listModel = (CollabDocsLModel) listDocuments.getModel();
        CollaborativesDAO collabDAO = new CollaborativesDAO();
        for (DocCollabCandidateBO collabCandBO : list) {
            if (collabCandBO.isCheck()) {
                collabDAO.deleteDocumentCollabCand(collabCandBO.getCollabDocCandidateId());
                //listModel.addElement(docCollabItem);                
                lstDel.put(collabCandBO.getDocVersionId(), collabCandBO);
            }
        }

        Set<Integer> set = lstDel.keySet();
        Iterator<Integer> it = set.iterator();
        Integer cve;
        while (it.hasNext()) {
            cve = it.next();
            if (cve == null) {
                continue;
            }

            for (DocCollabCandidateBO collabCandBO : list) {
                if (collabCandBO.getDocVersionId() == cve) {
                    list.remove(collabCandBO);
                    break;
                }
            }
        }
        model.refreshData();
        model.fireTableDataChanged();

    }

    protected void addDocuments() {
        if (tableCand.getRowCount() <= 0) {
            return;
        }

        int index = tableCand.getSelectedRow();
        CollabDocsCandTModel model = (CollabDocsCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list = model.getList();
        if (list == null) {
            return;
        }
        List<DocCollabCandidateBO> lstCandSel = new ArrayList<>();
        
        for (int i = 0; i < tableCand.getRowCount(); i++) {
            lstCandSel.add(list.get(tableCand.convertRowIndexToModel(i)));
        }

        DocumentCollabItemBO docCollabItem = null;
        Map<Integer, DocCollabCandidateBO> lstDel = new TreeMap<>();
        CollabDocsLModel listModel = (CollabDocsLModel) listDocuments.getModel();
        for (DocCollabCandidateBO collabCandBO : lstCandSel) {
            if (collabCandBO.isCheck()) {
                docCollabItem = new DocumentCollabItemBO();
                docCollabItem.setItemDocVersionId(collabCandBO.getDocVersionId());
                docCollabItem.setDocumentName(collabCandBO.getCandName());
                docCollabItem.setFileName(collabCandBO.getFileName());
                docCollabItem.setDocTypeName(collabCandBO.getCandTypeName());
                docCollabItem.setAutor(collabCandBO.getCandAuthor());
                docCollabItem.setTema(collabCandBO.getCandSubject());
                listModel.addElement(docCollabItem);
                
                System.out.println("Doc - " + collabCandBO.getCandName());

                lstDel.put(collabCandBO.getDocVersionId(), collabCandBO);
                if (collabCandBO.getCollabDocCandidateId() > 0) {
                    this.mapCandDelete.put(collabCandBO.getDocVersionId(), collabCandBO);
                } else {
                    this.mapCandAdd.remove(collabCandBO.getDocVersionId());
                }
            }
        }

        Set<Integer> set = lstDel.keySet();
        Iterator<Integer> it = set.iterator();
        Integer cve;
        while (it.hasNext()) {
            cve = it.next();
            if (cve == null) {
                continue;
            }

            for (DocCollabCandidateBO collabCandBO : list) {
                if (collabCandBO.getDocVersionId() == cve) {
                    list.remove(collabCandBO);
                    break;
                }
            }
        }
        model.refreshData();
        model.fireTableDataChanged();
        listModel.fireTableDataChanged();

        //listDocs.add(docCollab);
    }

    class SearchDocumentWorker extends SwingWorker<List<DocCollabCandidateBO>, List<DocCollabCandidateBO>> {

        private Exception exception;
        private final JProgressBar bar;
        private final DocumentSearchBO docSearchBO;

        SearchDocumentWorker(final JProgressBar bar, DocumentSearchBO docSearchBO) {
            this.bar = bar;
            this.docSearchBO = docSearchBO;
        }

        @Override
        protected List<DocCollabCandidateBO> doInBackground() {
            List<DocCollabCandidateBO> list = null;

            try {
                CollaborativesDAO dao = new CollaborativesDAO();

                list = dao.getDocCandidates(GlobalDefines.TIPO_CAND_DOC, document.getDocVersionId(), 0, 0);
                if (mapOrigCandidates == null) {
                    mapOrigCandidates = new TreeMap<>();
                }

                if (list != null) {
                    for (DocCollabCandidateBO cand : list) {
                        mapOrigCandidates.put(cand.getDocVersionId(), cand);
                    }
                }

            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
            }
            return list;
        }

        @Override
        protected void done() {

            try {

                List<DocCollabCandidateBO> lstTmp;
                lstTmp = get();
                lstCandidates = new ArrayList<>();
                if (lstTmp != null) {
                    for (DocCollabCandidateBO candBO : lstTmp) {
                        if (mapCandDelete.get(candBO.getDocVersionId()) == null) {
                            //no lo encontro por lo que  hay que agregarlo
                            if (lstCandidates == null) {
                                lstCandidates = new ArrayList<>();
                            }
                            lstCandidates.add(candBO);
                        }

                    }
                }

                
                List<DocCollabCandidateBO> list =get();
                
                
                
                
                modelTableCand.setData(lstCandidates);
                modelTableCand.fireTableDataChanged();

            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {

                //  setRenderEditorToColumns(jxTable);
                bar.setVisible(false);
            }
        }

    }

    class AddModulesWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {

            try {
                agregarModulos();
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            return null;
        }

        @Override
        protected void done() {

            MainView.main.getProgressBar().setIndeterminate(false);
            MainView.main.getProgressBar().setVisible(false);

        }

    }

    public void agregarModulos() throws Exception {

        List<DocumentCollabItemBO> lDocColItemBO = document.getLstDocumentCollab();
        DocumentDAO docDAO = new DocumentDAO();
        if (lDocColItemBO != null) {
            for (DocumentCollabItemBO docColItemBO : lDocColItemBO) {
                Integer[] docId = docDAO.getDocumentIdByVersion(docColItemBO.getItemDocVersionId());
                if (docId == null || docId[0] == null || docId[1] == null) {
                    continue;
                }
                DocumentBO docBO = docDAO.getDocument(docId[0], docId[1],false);
                if (docBO != null && docBO.getMapHojas() != null) {
                    for (Map.Entry<Integer, HojaBO> entryHoja : docBO.getMapHojas().entrySet()) {
                        if (entryHoja.getValue() != null) {
                            for (ModuleBO modBO : entryHoja.getValue().getLstModules()) {
                                try{
                                addModule2(modBO.getModuleId(), modBO.getDocumentModuleId());
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                //addModule3(modBO);
                            }
                        }
                    }
                }
            }
        }
    }

    class SaveDocumentWorker extends SwingWorker<List<String>, Object> {

        private Exception exception;
        private final JProgressBar bar;

        SaveDocumentWorker(final JProgressBar bar) {
            this.bar = bar;
        }

        @Override
        protected List<String> doInBackground() {
            List<DocCollabCandidateBO> list = new ArrayList<>();
            List<String> listRsp = new ArrayList<>();
            try {
                CollaborativesDAO dao = new CollaborativesDAO();

                //agregarModulos();
                int idDoc = dao.saveCollaborative(document);
                document.setDocumentId(idDoc);
                //  CollabDocsLModel  listModel=(CollabDocsLModel) listDocuments.getModel();
                //   List<DocumentCollabItemBO> listDocs= listModel.getData();
                listRsp.add("0");
                listRsp.add("");

            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
                listRsp.add( "1");
                listRsp.add( ex.getMessage());

            }
            return listRsp;
        }

        @Override
        protected void done() {
            List<String> listRsp = null;
            try {
                listRsp = get();
                if (listRsp != null && listRsp.size() > 1) {

                    if (listRsp.get(0).equals("0")) {

                        //Eliminamos los que estaban en la lista de candidatos
                        Set<Integer> set = mapCandDelete.keySet();
                        CollaborativesDAO collabDAO = new CollaborativesDAO();
                        Iterator<Integer> it = set.iterator();
                        Integer cve;
                        DocCollabCandidateBO collabCandBO;
                        while (it.hasNext()) {
                            cve = it.next();
                            if (cve == null) {
                                continue;
                            }
                            collabCandBO = mapCandDelete.get(cve);
                            if (collabCandBO.getCollabDocCandidateId() > 0) {
                                collabDAO.deleteDocumentCollabCand(collabCandBO.getCollabDocCandidateId());
                            }
                        }

                        mapCandDelete = new TreeMap<>();

                        //Agregamos los nuevos
                        set = mapCandAdd.keySet();
                        it = set.iterator();
                        while (it.hasNext()) {
                            cve = it.next();
                            if (cve == null) {
                                continue;
                            }
                            collabCandBO = mapCandAdd.get(cve);
                            if (collabCandBO.getCollabDocCandidateId() <= 0) {
                                collabDAO.addDocumentCollabCand(collabCandBO);
                            }
                        }

                        mapCandAdd = new TreeMap<>();

                        // JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), "Se guardo el documento correctamente ", "Agregar documentos ", JOptionPane.OK_OPTION);      
                        Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "El documento se guardo correctamente ", JOptionPane.INFORMATION_MESSAGE);
                        jButton3.setEnabled(true);

                    }

                }

            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {

                //  setRenderEditorToColumns(jxTable);
                bar.setVisible(false);
            }
        }

    }

    public JXTable getListDocuments() {
        return listDocuments;
    }

    public void setListDocuments(JXTable listDocuments) {
        this.listDocuments = listDocuments;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        scrollTableCand = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbMercado = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbTipDoc = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        chkSelectAll = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        scrollDocuments = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 15));
        jPanel1.setPreferredSize(new java.awt.Dimension(675, 428));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel4.add(scrollTableCand, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(434, 50));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/borrar_1.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jPanel5.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 1, 12, 1));
        jPanel12.setMaximumSize(new java.awt.Dimension(170, 32767));
        jPanel12.setPreferredSize(new java.awt.Dimension(180, 50));
        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton6.setText("Agregar Documentos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton6);

        jPanel5.add(jPanel12, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jPanel3.setMinimumSize(new java.awt.Dimension(0, 230));
        jPanel3.setPreferredSize(new java.awt.Dimension(346, 140));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Tipo de documento:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        cmbMercado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cmbMercado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Mercado:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cmbTipDoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cmbTipDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jButton1.setText("Actualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        chkSelectAll.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        chkSelectAll.setText("Seleccionar Todos");
        chkSelectAll.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        jPanel3.add(chkSelectAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 150, -1));

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel6.setPreferredSize(new java.awt.Dimension(350, 35));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Lista de documentos");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, -1));

        jPanel2.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());
        jPanel9.add(scrollDocuments, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setMaximumSize(new java.awt.Dimension(45, 32767));
        jPanel10.setPreferredSize(new java.awt.Dimension(45, 302));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/left_back_arrow_blue.png"))); // NOI18N
        btnRemove.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jPanel10.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 40, 40));

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/up_back_arrow_blue.png"))); // NOI18N
        btnUp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });
        jPanel10.add(btnUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 40, 40));

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/down_back_arrow_blue.png"))); // NOI18N
        btnDown.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });
        jPanel10.add(btnDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 40, 40));

        jPanel7.add(jPanel10, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 1, 12, 1));
        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel8.setPreferredSize(new java.awt.Dimension(350, 50));
        jPanel8.setLayout(new java.awt.GridLayout(1, 4));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton2);

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setText("Publicar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton3);

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton4.setText("Ver HTML");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton4);

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setText("Ver PDF");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton5);

        jPanel2.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        addDocuments();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        saveDocument();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        htmlPreview();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        loadCandidateDocs();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        pdfPreview();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        removeDocument();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
        moveUp();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        // TODO add your handling code here:
        moveDown();
    }//GEN-LAST:event_btnDownActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        borrarSelected();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        sendPublish();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JCheckBox chkSelectAll;
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbTipDoc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane scrollDocuments;
    private javax.swing.JScrollPane scrollTableCand;
    // End of variables declaration//GEN-END:variables

}

class PDFCreatorCollaborativeWorker extends SwingWorker<List<InputStream>, List<InputStream>> {

    int idDocument;
    DocumentBO docBO;
    JXTable listDocuments;

    public PDFCreatorCollaborativeWorker(int idDocument, DocumentBO docBO) {

        this.idDocument = idDocument;
        this.docBO = docBO;
    }
    
    public PDFCreatorCollaborativeWorker(int idDocument, DocumentBO docBO, JXTable listDocuments) {

        this.idDocument = idDocument;
        this.docBO = docBO;
        this.listDocuments = listDocuments;
    }

    @Override
    protected List<InputStream> doInBackground() {

        CollaborativesDAO collabDAO = new CollaborativesDAO();
        DocumentCollabBO document = collabDAO.getDocument(idDocument);
        List<InputStream> lstCollabPdf = new ArrayList<>();

        if(listDocuments != null){
            document.setLstDocumentCollab(getLstDocCollabItems());
        }
        
        //int cont = 1;
        for (DocumentCollabItemBO coll : document.getLstDocumentCollab()) {
            /*DocumentDAO dao = new DocumentDAO();
            DocumentBO bo = null;
            FileInputStream inputStream = null;*/
            try {
                //bo = dao.getDocument(coll.getDocumentId(), -1);
                /*bo = dao.getDocument(coll.getItemDocumentId(), -1,false);
                PdfDocument pdfDoc = new PdfDocument();*/
                
                lstCollabPdf.add( UtileriasSSH.getInstance().crearPDFCollab( coll.getItemDocumentId(), null ) );
                
                
                
                
                /*if(cont < document.getLstDocumentCollab().size()){
                    pdfDoc.setEliminarDisplosure(true);
                }else{
                    pdfDoc.setEliminarDisplosure(false);
                    DocumentBO boColl = dao.getDocument(docBO.getDocumentId(), -1,false);
                    pdfDoc.setDocBODisplosure(boColl);
                    pdfDoc.setAgregarDirectorio(true);
                }
                
                if(pdfDoc.createPdfDocument(bo, false) != null){
                    String fileName = pdfDoc.getFilePath();
                    Utilerias.logger(getClass()).info("PDF Individual = " + fileName);
                //inputStream = new FileInputStream(fileName);
                    //lstCollabPdf.add(fileName);
                }*/
            }catch(Exception e){
                Utilerias.logger(getClass()).info(e);
            }
            /*} catch (SQLException e) {
                Utilerias.logger(getClass()).info(e);
            } catch (DocumentException | IOException ex) {
                Utilerias.logger(getClass()).info(ex);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                        Utilerias.logger(getClass()).info(ex);
                    }
                }
            }*/
            //cont++;
        }
        
        try {//Se genera el Directorio con el Disclosure del semanario
            PdfDocument pdfDoc = new PdfDocument();
            
            pdfDoc.createPdfDocumentDisclosureAndDirOnly(docBO);
            String fileName = pdfDoc.getFilePath();
            lstCollabPdf.add( new FileInputStream(fileName) );
            
        } catch (DocumentException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        
        return lstCollabPdf;
    }

    @Override
    protected void done() {
        List<InputStream> lstCollabPdf = null;
        try {
            lstCollabPdf = get();
        } catch (Exception e) {
        }
        if (lstCollabPdf != null && !lstCollabPdf.isEmpty()) {
            OutputStream output = null;
            try {

                String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
                String fileName = dirPDF + docBO.getDocumentId() + "_" + docBO.getFileName() + "_" + Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.PDF) + ".pdf";
                output = new FileOutputStream(fileName);
                Utilerias.logger(getClass()).info(fileName);
                MergePDF.concatPDFs(lstCollabPdf, output, true);
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser("file://" + fileName);
            } catch (FileNotFoundException | BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
                Logger.getLogger(PDFCreatorCollaborativeWorker.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PDFCreatorCollaborativeWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo generar el PDF del documento Colaborativo.");
        }
        MainView.main.getProgressBar().setVisible(false);
    }

    private List<DocumentCollabItemBO> getLstDocCollabItems(){
        CollabDocsLModel lModel = (CollabDocsLModel) listDocuments.getModel();
        List<DocumentCollabItemBO> lstItemBO = lModel.getList();
        DocumentDAO dao = new DocumentDAO();
        
        for (DocumentCollabItemBO itemBO : lstItemBO) {
            if(itemBO.getItemDocumentId() == 0){
                Integer[] res = dao.getDocumentIdByVersion(itemBO.getItemDocVersionId());
                if(res != null && res.length > 1 && res[0].intValue() > 0){
                    itemBO.setItemDocumentId(res[0].intValue());
                    itemBO.setItemVersion(res[1].intValue());
                }
            }
        }
        
        return lstItemBO;
    }
}

class HTMLViewWorker extends SwingWorker<String, String> {

    int idDocument;
    JXTable listDocuments;
    
    public HTMLViewWorker(int idDocument) {
        this.idDocument = idDocument;
    }
    
    public HTMLViewWorker(int idDocument, JXTable listDocuments) {
        this.idDocument = idDocument;
        this.listDocuments = listDocuments;
    }

    @Override
    protected String doInBackground() throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = new Date();

        DocumentDAO dao = new DocumentDAO();
        DocumentBO bo = dao.getDocument(idDocument, -1,false);

        if(listDocuments != null){
            bo.setLstDocumentCollab(getLstDocCollabItems());
        }
        
        String ruta = UtileriasSSH.getInstance().sendFilesSsh(null, bo, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);

        return ruta;
    }

    @Override
    protected void done() {
        String ruta = null;

        try {
            ruta = get();
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).info(ex);
        }

        try {
            if(ruta != null){
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser(ruta);
            }
        } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }

    }
    
    private List<DocumentCollabItemBO> getLstDocCollabItems(){
        CollabDocsLModel lModel = (CollabDocsLModel) listDocuments.getModel();
        List<DocumentCollabItemBO> lstItemBO = lModel.getList();
        DocumentDAO dao = new DocumentDAO();
        
        for (DocumentCollabItemBO itemBO : lstItemBO) {
            if(itemBO.getItemDocumentId() == 0){
                Integer[] res = dao.getDocumentIdByVersion(itemBO.getItemDocVersionId());
                if(res != null && res.length > 1 && res[0].intValue() > 0){
                    itemBO.setItemDocumentId(res[0].intValue());
                    itemBO.setItemVersion(res[1].intValue());
                }
            }
        }
        
        return lstItemBO;
    }

}

class HTMLViewWorkerModCol extends SwingWorker<String, String> {

    DocumentBO docBO;
    
    public HTMLViewWorkerModCol(DocumentBO bo) {
        this.docBO = bo;
    }

    @Override
    protected String doInBackground() throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = new Date();

        String ruta = UtileriasSSH.getInstance().sendFilesSsh(null, docBO, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);

        return ruta;
    }

    @Override
    protected void done() {
        String ruta = null;

        try {
            ruta = get();
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).info(ex);
        }

        try {
            if(ruta != null){
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser(ruta);
            }
        } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }

    }

}