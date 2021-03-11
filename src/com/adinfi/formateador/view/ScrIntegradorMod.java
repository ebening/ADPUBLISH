
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
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.SectionColModBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.HeaderColModDAO;
import com.adinfi.formateador.dao.SectionColModDAO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.administration.tablemodel.SearchTableModel;
import com.adinfi.formateador.view.publish.SendPublish;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXTable;


/**
 *
 * @author Desarrollador
 */
public class ScrIntegradorMod extends javax.swing.JPanel {
    
    private JXTable tableCand;
    private DocumentCollabBO document = null;
    CollabModCandTModel modelTableCand;
    Map<Short, List<ModuleBO>> mapModulesByHoja;
    Map<Integer, String> mapInfoByModule;
    PanelSearchModule panSearchModule;
    List<DocCollabCandidateBO> lstCandidates;
    Map<String, DocCollabCandidateBO> mapOrigCandidates;
    List<DocumentCollabItemBO> lstCollabItems;
    ScrDocumentMod scrDoc;

    Map<String, DocCollabCandidateBO> mapCandDelete;
    Map<String, DocCollabCandidateBO> mapCandAdd;

    
    
    public DocumentBO getDocument() {
        return document;
    }

    public List<Integer> getlIdDocModule() {
        return lIdDocModule;
    }

    public void setlIdDocModule(List<Integer> lIdDocModule) {
        this.lIdDocModule = lIdDocModule;
    }

    private List<Integer> lIdDocModule;
    
    public void newDocument(DocumentCollabBO document) {
        this.document = document;
        try{
           
      //  this.scrDoc.setDocBO(new DocumentBO());    
       // this.scrDoc.initNewDocument();
        
        tableCand.removeAll();       
        lstCollabItems=new ArrayList<>();        
        this.document.setLstDocumentCollab(lstCollabItems);
        this.createScrDoc(document.getDocumentId()<=0);
        this.scrDoc.setLstCollabMod(lstCollabItems);
        this.scrDoc.setDocBO(document);
        //this.modelListDocs.setData(lstCollabItems);               
        loadCandidateDocs();
        initCombos();
        
        edNombre.setText(this.document.getDocumentName());
        
      //  if( this)
        if(this.scrDoc.getDocBO()==null || this.scrDoc.getDocBO().getDocumentId()<=0){
            this.saveDocument();
        }
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }
        
    }
    
    
 public void loadDocument(){
        if(this.document.getDocumentId()<=0){
            return;
        }
        this.scrDoc.setDocumentId(this.document.getDocumentId());
        try{
            this.scrDoc.openDoc();
        }catch(Exception e){
             Utilerias.logger(getClass()).error(e);
        }
        
        
    }    
    
    
    public void initCombos(){
        
        this.cmbMercado.removeAllItems();
        this.cmbTipDoc.removeAllItems();
         
        
        this.cmbMercado.addItem(this.document.getMarketBO());
        this.cmbTipDoc.addItem(this.document.getTipoBO());
            
        
        this.cmbMercado.setEnabled(false);
        this.cmbTipDoc.setEnabled(false);
        
        this.cmbMercado.setSelectedIndex(0);
        this.cmbTipDoc.setSelectedIndex(0);
        
        fillComboEncabezado();
        
        
        if (this.document.getDocumentId() <= 0) {
            btnPublicar.setEnabled(false);
            btnViewHTML.setEnabled(false);
            btnViewPDF.setEnabled(false);
        }
        
    }
    
    private void fillComboEncabezado(){
        Object objMrkt = cmbMercado.getSelectedItem();
        Object objDoc = this.cmbTipDoc.getSelectedItem();
        HeaderColModDAO headDAO = new HeaderColModDAO();
        List<HeaderColModBO> listHeader = headDAO.get(Integer.parseInt(((MarketBO)objMrkt).getIdMiVector_real()), ((DocumentTypeBO)objDoc).getIddocument_type());
        HeaderColModBO headBO = new HeaderColModBO();

        this.cmbEncabezado.removeAllItems();
        headBO.setName("Seleccione");
        headBO.setIdHeaderColMod(-1);
        this.cmbEncabezado.addItem(headBO);

        for (HeaderColModBO hBO : listHeader) {
            this.cmbEncabezado.addItem(hBO);
        }
    }
    
    private void fillComboSeccion(){
        Object objEnc = cmbEncabezado.getSelectedItem();
        if (objEnc != null && ((HeaderColModBO)objEnc).getIdHeaderColMod() > -1) {
            SectionColModDAO sectionDAO = new SectionColModDAO();
            List<SectionColModBO> listSection = sectionDAO.get(((HeaderColModBO)objEnc).getIdHeaderColMod());
            
            SectionColModBO sectionBO = new SectionColModBO();
            cmbSeccion.removeAllItems();
            sectionBO.setName("Seleccione");
            sectionBO.setIdHeaderColMod(-1);
            sectionBO.setIdSectionColMod(-1);
            cmbSeccion.addItem(sectionBO);
            for (SectionColModBO sBO : listSection) {
                cmbSeccion.addItem(sBO);
            }
        }  else {
            cmbSeccion.removeAllItems();
            
        } 
    }
    
    
    /**
     * Creates new form ScrIntegradorDoc
     */
    public ScrIntegradorMod() {
        initComponents();
        createTableDocCand();
      //  createScrDoc();       
        loadDocumentPar();
        panSearchModule=new PanelSearchModule (this);
        //this.jPanel9.add (panSearchModule  );
        this.scrollSearchModule.setViewportView(panSearchModule);
        
        
    }
    
    
    public void loadDocumentPar(){
        
        if( this.document==null || this.document.isCollaborative()==false ){
            return;
        }
        List<String> titulo = Utilerias.getTitleProperties(this.document);
        if(titulo!= null && titulo.size() >= 3){
            MainView.main.initInfoLabel(titulo.get(0), titulo.get(1), titulo.get(2));
        }else{
            MainView.main.initInfoLabel("", "", "");
        }
       
        mapCandDelete=new TreeMap<>();
        mapCandAdd=new TreeMap<>();
        
        
        loadCandidateDocs(); 
        
        
    }
    
    
    private void createScrDoc(boolean isNew){
        
        // boilerplate table-setup; this would be the same for a JTable
        this.scrDoc=new ScrDocumentMod(this);
        try{
            
        if(isNew){    
          this.scrDoc.setDocumentId(-1);
        }
       // this.scrDoc.getDocBO().setDocumentId(-1);
       // this.scrDoc.setDocBO(new DocumentBO());
        this.scrDoc.setDocBO(document);
        this.scrDoc.addHoja(true);
        this.mainPanel.add(this.scrDoc);
        mainPanel.revalidate();
        mainPanel.updateUI();
        }catch(Exception e){
           Utilerias.logger(getClass()).error(e); 
        }
        
    
        
        
    }
    
    private void createTableDocCand() {
             
                tableCand = new JXTable();
                tableCand.setHorizontalScrollEnabled(true);
                tableCand.setColumnControlVisible(true);
                tableCand.setColumnControlVisible(false);
                int margin = 5;
                tableCand.packTable(margin);
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
            
            modelTableCand=new CollabModCandTModel();
            tableCand.setModel(modelTableCand);
            tableCand.getColumn(0).setPreferredWidth(25);
            tableCand.getColumn(1).setPreferredWidth(160);
            tableCand.getColumn(2).setPreferredWidth(380);
            tableCand.getColumn(3).setPreferredWidth(200);
            tableCand.getColumn(4).setPreferredWidth(100);
            tableCand.getColumn(5).setPreferredWidth(100);
           // Utilerias.formatTablex(tableCand);
        }    
    
    
   private String armaClave( int moduleId , int documentModuleId ) {
       if( documentModuleId>0 ){
           return "0-"+ documentModuleId;
       }else{
           return moduleId+"-0"; 
       }
       
   }    
    
    
    protected void loadCandidateDocs(){
        DocumentSearchBO docSearchBO = new DocumentSearchBO();
        docSearchBO.setIdMarket(this.document.getIdMarket());
        docSearchBO.setIdDocType(this.document.getIdDocType());
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        ScrIntegradorMod.SearchDocumentWorker worker = new ScrIntegradorMod.SearchDocumentWorker(MainView.main.getProgressBar(), docSearchBO);
        worker.execute();        
        
        
    }
    
    
    public void saveAs(DocumentCollabBO document) {
        CollaborativesDAO collaborativesDAO=new CollaborativesDAO();
        
        try{
            this.scrDoc.getDocBO().setDocVersionId(0);
            this.scrDoc.getDocBO().setDocumentId(0);
            this.scrDoc.getDocBO().setDocumentName(document.getDocumentName());
            this.scrDoc.getDocBO().setDocument_Name(document.getDocument_Name());
            this.scrDoc.getDocBO().setFileName(document.getFileName());
            this.scrDoc.getDocBO().setFile_Name(document.getFile_Name());
            this.scrDoc.getDocBO().setIdDocument_Type(document.getIdDocument_Type());
            this.scrDoc.getDocBO().setIdLanguage(document.getIdLanguage());
            this.scrDoc.getDocBO().setIdDocType(document.getIdDocType());
            this.scrDoc.getDocBO().setIdLanguage(document.getIdLanguage());
            this.scrDoc.getDocBO().setIdMarket(document.getIdMarket());
            this.scrDoc.getDocBO().setIdSubject(document.getIdSubject());
            this.scrDoc.getDocBO().setVersion(0);
            this.scrDoc.getDocBO().setIdPublish(0);
                 scrDoc.getDocBO().setCollaborative(true);                
                 scrDoc.getDocBO().setCollaborativeType(GlobalDefines.COLLAB_TYPE_MOD );


            Map<Integer,HojaBO>mapHojasOrig= this.scrDoc.getDocBO().getMapHojas();
            //this.scrDoc.getDocBO().setMapHojas(null);
            //guardamos el documento vacio

            //
            Set<Integer> keySet=mapHojasOrig.keySet();
            Iterator<Integer> it=keySet.iterator();
            Integer nHoja;
            HojaBO hoja;
            List<ModuleBO> lstModTmp=null;
            while( it.hasNext() ){
                nHoja=it.next();
                hoja=mapHojasOrig.get(nHoja);
                if( hoja==null ){
                    continue;
                }

                lstModTmp=hoja.getLstModules();
                if(lstModTmp==null || lstModTmp.size()<=0 ){
                    continue;
                }

                for(  ModuleBO mod : lstModTmp ){

                    mod.setDocumentModuleId(0);
                    this.scrDoc.resetModule(mod);                   
                    mod.setDocumentVersionId(this.scrDoc.getDocBO().getDocVersionId());


                }
            }

            this.scrDoc.saveDocument();
            //this.initCombos();
            //MainView.main.setCollabIntegradorMod(this);



            CollaborativesDAO collabDAO = new CollaborativesDAO();
            CollabModCandTModel  modelCand = ( CollabModCandTModel ) tableCand.getModel();
            List<DocCollabCandidateBO> lst= modelCand.getList();

            if( lst!=null && lst.size()>0 ){
                for( DocCollabCandidateBO collabBO : lst  ){
                     collabBO.setCollabDocCandidateId(0);
                     collabBO.setDocVersionTarget(this.scrDoc.getDocBO().getDocVersionId());
                     collabDAO.addDocumentCollabCand(collabBO);
                }
            }

            List<DocumentCollabItemBO> lstCollabItems= document.getLstDocumentCollab();
            if( lstCollabItems!=null && lstCollabItems.size()>0 ){
                for( DocumentCollabItemBO  collabItem : lstCollabItems    ){
                    collabItem.setDocumentCollabItemId(0);
                    collabItem.setDocVersionIdTarget(this.scrDoc.getDocBO().getDocVersionId());
                }

            }
            collabDAO.upsertCollaboratives(document);
            //this.loadCandidateDocs();

            //MainView.main.viewQuickViewPanel();
            MainView.main.getScrDocument().setDocBO(null);
            MainView.main.getMainPanel().removeAll();
            MainView.main.setTitle(GlobalDefines.TITULO_APP);
            MainView.main.initInfoLabel("", "", "");
           
            DocumentDAO docDAO = new DocumentDAO();
            DocumentCollabBO documentNew = (DocumentCollabBO) docDAO.getDocument(this.scrDoc.getDocumentId(), 1,false);
            ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
            scrIntegradorMod.newDocument(documentNew);
            scrIntegradorMod.loadDocument();
            

            OpenPublishColabModWorker modWorker = new OpenPublishColabModWorker(scrIntegradorMod, false);
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            modWorker.execute();
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Se guardo el documento correctamente ", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }
           
    }    
    
    
    protected void saveDocument(){
       
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if (lIdDocModule != null && !lIdDocModule.isEmpty()) {
            CollaborativesDAO collabDAO=new CollaborativesDAO();
            lIdDocModule.stream().forEach((idDocMod) -> {
                collabDAO.restoreModuleCollabCandLog(idDocMod);
            });
        }
      //  this.loadCandidateDocs();
        ScrIntegradorMod.SaveDocumentWorker worker = new ScrIntegradorMod.SaveDocumentWorker(MainView.main.getProgressBar());
        worker.execute();        
    }
    
    
    protected void removeModule(ModuleBO module){
      
                
                //DocCollabCandidateBO docCand=this.mapOrigCandidates.get( module.getModuleId()+"-"+ module.getDocumentModuleId() );
        
                /*
                if(this.mapOrigCandidates==null   || this.mapOrigCandidates.size()<=0  ){
                    return;
                } */
                    
        
                DocCollabCandidateBO docCand=null;
                if( this.mapOrigCandidates!=null  ){
                        
                    docCand= this.mapOrigCandidates.get( armaClave( module.getModuleId() , module.getOrigDocModuleId()   ) );
                }        
                
                CollabModCandTModel model= (CollabModCandTModel) tableCand.getModel();
                List<DocCollabCandidateBO> list=model.getList();
                
                if( docCand!=null ){
                      
                     if(list!=null){
                         docCand.setCheck(false);
                         list.add(docCand);
                         model.refreshData();
                         model.fireTableDataChanged();
                         this.mapCandDelete.remove( armaClave( docCand.getModuleId() , docCand.getDocumentModuleId()  ) );
                     }
                    
                }else{
                    
                    //Primero checamos si existe el collabcandidate
                    CollaborativesDAO collabDAO=new CollaborativesDAO();
                    docCand=collabDAO.geCandidateMod(   this.document.getDocVersionId()     ,  module.getOrigDocModuleId() );
                    
 
                    
                    if( docCand==null   ){                        
                        //Es un modulo guardado
                        docCand=new DocCollabCandidateBO ();
                        docCand.setModuleId(module.getModuleId());
                        docCand.setCandAuthor( this.document.getAuthorName() );
                        docCand.setTipo(GlobalDefines.COLLAB_TYPE_MOD);
                        docCand.setDocumentModuleId( module.getDocumentModuleId() );         
                        docCand.setCandSubjectId(this.document.getIdSubject());
                        docCand.setCandSubject(this.document.getSubjectBO()!= null ? this.document.getSubjectBO().getName():null);
                        
                        docCand.setCandTypeName(this.document.getTipoBO().getName());
                        docCand.setDocumentTypeId(this.document.getIdDocument_Type());
                        
                        docCand.setFileName(this.document.getFile_Name());
                        docCand.setCandName("MOD_" + module.getDocumentModuleId()  );
                        docCand.setName("MOD_" + module.getDocumentModuleId());
                        docCand.setDocSrcName(this.document.getDocumentName());
                        docCand.setDocVersionTarget( this.document.getDocVersionId() );
                        
                    }   
                    
                    String cve=armaClave( module.getModuleId() ,module.getDocumentModuleId() );              
                    
                    if(!module.isHeaderOrSection() && !module.isSection())
                        this.mapCandAdd.put(cve, docCand);
                    
                    if(list!=null && !module.isHeaderOrSection() && !module.isSection()){
                         docCand.setCheck(false);
                         list.add(docCand);
                         model.refreshData();
                         model.fireTableDataChanged();
                     }
                }
        
        
       
        /*
        CollabDocsLModel lModel=(CollabDocsLModel)this.listDocuments.getModel();
        List<DocumentCollabItemBO> data= lModel.getData();
        DocumentCollabItemBO docCollBO=data.get(idx);
        data.remove(idx);
        lModel.remove(idx);
        
        
        if( docCollBO!=null ){
            if( this.mapOrigCandidates!=null ){
                DocCollabCandidateBO docCand=this.mapOrigCandidates.get( docCollBO.getItemDocVersionId() );
                if( docCand!=null ){
                     CollabDocsCandTModel model= (CollabDocsCandTModel) tableCand.getModel();
                     List<DocCollabCandidateBO> list=model.getList();
                     if(list!=null){
                         docCand.setCheck(false);
                         list.add(docCand);
                         model.refreshData();
                         model.fireTableDataChanged();
                     }
                    
                }
            }
            
            
            
        }
        
        */
        
        
        
        
    }
    
    protected void sendPublish() {
        SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }
    
    protected void htmlPreview(){
        if(this.document.getDocumentId()<=0){
            return;
        }
        try{
            HTMLViewWorkerModCol workerHTML = new HTMLViewWorkerModCol(this.scrDoc.getDocBO());
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            workerHTML.execute();
            /*PdfDocument pdfDoc = new PdfDocument();
            pdfDoc.createPdfDocument(this.scrDoc.getDocBO());
            
            HTMLDocument html=new HTMLDocument();
            String fileName="file:///" + html.createHTMLDocument(this.scrDoc.getDocBO(), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY, pdfDoc.getFileName(), pdfDoc.getFilePath());
            
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.openURLinBrowser(fileName);*/
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }



        
    }
    
    
    
    protected void pdfPreview() {
        
            if(this.document.getDocumentId()<=0){
                return;
            }        
        
            try {
                
                MainView.main.getProgressBar().setVisible(true);
                MainView.main.getProgressBar().setIndeterminate(true);
                PDFViewerWorker pDFViewerWorker = new PDFViewerWorker(this.scrDoc.getDocBO());
                pDFViewerWorker.execute();
                
                /*PdfDocument pdfDoc = new PdfDocument();
                pdfDoc.createPdfDocument(this.scrDoc.getDocBO());
                String fileName = pdfDoc.getFilePath();
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
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser("file://" + fileName);*/
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }  
        
    }
    
    
    
    
    
    protected void selectAll(){
        
       if( tableCand.getRowCount()<=0 ){
            return;
        }
                        
        CollabModCandTModel model= (CollabModCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list=model.getList();
        if( list==null ){
            return;
        }
         
        for( DocCollabCandidateBO docCand : list ){
            docCand.setCheck(chkSelectAll.isSelected() );
           
        }
            
         model.refreshData();
         model.fireTableDataChanged();
        
    }
    
    
   protected void borrarSelected(){
        if( tableCand.getRowCount()<=0 ){
            return;
        }
        
        
        int reply = JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea eliminar el módulo  ?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (reply != JOptionPane.YES_OPTION) {
            System.out.println("Se va a borrar el modulo de la bandeja  ... ");
            //TODO:Mandar a ejecutar el metodo de eliminar modulo
            //deleteModule(this);
            return;
        }
        
        CollabModCandTModel model= (CollabModCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list=model.getList();
        if( list==null ){
            return;
        }
        
        DocumentCollabItemBO docCollabItem=null;
        List<DocCollabCandidateBO> lstDel=new ArrayList<>();
      //  CollabDocsLModel  listModel=(CollabDocsLModel) lstCandidates.getModel();    
        
        CollaborativesDAO collabDAO=new CollaborativesDAO();
        String cve;
        for( DocCollabCandidateBO collabCandBO :  list  ){
            if( collabCandBO.isCheck() ){               
                collabDAO.deleteDocumentCollabCand( collabCandBO.getCollabDocCandidateId()  );
                //listModel.addElement(docCollabItem);   
                if( collabCandBO.getCollabDocCandidateId()<=0 ){
                    cve=armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId() );              
                    this.mapCandAdd.remove(cve);                    
                }
                lstDel.add(collabCandBO);
            }
        }
           
        for( DocCollabCandidateBO collabCandBO :  lstDel  ){
            list.remove(collabCandBO);
        }                        
        model.refreshData();
        model.fireTableDataChanged();
    }
    
    
    public void addModuleFromSearch(int moduleId){
         ModuleBO moduleBO=this.scrDoc.addModule2(moduleId, 0, 0, 0);
        
    }
    
    private static final String MOD_HEADER = "Modulo Encabezado";
        
    protected void addHeader() {
        int idHeader = 0;
        String strHeader = "";
        Object objEnc = cmbEncabezado.getSelectedItem();
        if (objEnc != null && ((HeaderColModBO)objEnc).getIdHeaderColMod() > -1) {
            idHeader = ((HeaderColModBO)objEnc).getIdHeaderColMod();
            strHeader = ((HeaderColModBO)objEnc).getName();
        } else {
            return;
        }
        
        TemplateDAO temDAO = new TemplateDAO();
        List<ModuleBO> lModBO = temDAO.searchModulesByName(MOD_HEADER);
        if (lModBO == null || lModBO.isEmpty()) {
            return;
        }
        
        ModuleBO modBO = lModBO.get(0);
        DocumentCollabItemBO docCollabItem=new DocumentCollabItemBO();     
        docCollabItem.setDocumentModuleId(0);
        docCollabItem.setModuleId(modBO.getModuleId());
        docCollabItem.setDocumentName(null);
        docCollabItem.setIdHeader(idHeader);
        docCollabItem.setIdSection(0);

        this.lstCollabItems.add(docCollabItem);
        
        TemplateDAO tempDAO = new TemplateDAO();
        ModuleBO moduleBO = tempDAO.getModule(modBO.getModuleId());
        
        if (moduleBO == null) {
            return;
        }
        
        String strHeaderHTML = "<html><head></head><body><p style=\"margin-top: 0\">\n</p><p style=\"margin-top: 0\">" + strHeader + "</p><p style=\"margin-top: 0\">\n</p></body></html>";
        
        moduleBO.setIdHeader(idHeader);
        moduleBO.setIdSection(0);
        
        Object[] lModulesBO = this.scrDoc.getAllModules();
        
        for (Object obj : lModulesBO){
            ModuleBO mBO = (ModuleBO) obj;
            if (!mBO.isHeaderOrSection()) {
                continue;
            }
            String strHe = mBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().get(0).getData();
            strHe = strHe.substring(strHe.indexOf("</p><p style=\"margin-top: 0\">") + 29);
            strHe = strHe.substring(0, strHe.indexOf("</p>"));
            if (mBO.isHeaderOrSection() && strHe.equals(strHeader)) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Ya existe el encabezado en el documento", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        ModuleSectionBO modSecBO = new ModuleSectionBO();
        modSecBO.setModuleId(moduleBO.getModuleId());
        modSecBO.setContentType("T");
        modSecBO.setHeight(moduleBO.getHeight());
        modSecBO.setLstObjects(new ArrayList<>());
        modSecBO.setType("N");
        ObjectInfoBO objInfoBO = new ObjectInfoBO();
        objInfoBO.setHeight(moduleBO.getHeight());
        objInfoBO.setData(strHeaderHTML);
        objInfoBO.setPlain_text(strHeaderHTML);
        objInfoBO.setObjType(4);
        objInfoBO.setWidth(moduleBO.getWidth());
        modSecBO.getLstObjects().add(objInfoBO);
        moduleBO.setLstSectionsText(new ArrayList<>());
        moduleBO.getLstSectionsText().add(modSecBO);
        moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).setLstObjects(new ArrayList<>());
        objInfoBO.setSectionId(moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getSectionId());
        moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().add(objInfoBO);
        moduleBO.setHeaderOrSection(true);

        this.scrDoc.addModule3(moduleBO);
        
    }
    
    private static final String MOD_SECTION = "Modulo Seccion";
    
    protected void addSection() {
        int idSection = 0;
        String strSection = "";
        Object objEnc = cmbSeccion.getSelectedItem();
        if (objEnc != null && ((SectionColModBO)objEnc).getIdSectionColMod() > -1) {
            idSection = ((SectionColModBO)objEnc).getIdSectionColMod();
            strSection = ((SectionColModBO)objEnc).getName();
        } else {
            return;
        }
        
        TemplateDAO temDAO = new TemplateDAO();
        List<ModuleBO> lModBO = temDAO.searchModulesByName(MOD_SECTION);
        if (lModBO == null || lModBO.isEmpty()) {
            return;
        }
        
        ModuleBO modBO = lModBO.get(0);
        DocumentCollabItemBO docCollabItem=new DocumentCollabItemBO();     
        docCollabItem.setDocumentModuleId(0);
        docCollabItem.setModuleId(modBO.getModuleId());
        docCollabItem.setDocumentName(null);
        docCollabItem.setIdHeader(0);
        docCollabItem.setIdSection(idSection);

        this.lstCollabItems.add(docCollabItem);
        
        TemplateDAO tempDAO = new TemplateDAO();
        ModuleBO moduleBO = tempDAO.getModule(modBO.getModuleId());
        
        if (moduleBO == null) {
            return;
        }
        
        String strSectionHTML = "<html><head></head><body><p style=\"margin-top: 0\">\n</p><p style=\"margin-top: 0\">" + strSection + "</p></body></html>";
        
        moduleBO.setIdHeader(0);
        moduleBO.setIdSection(idSection);
        
        Object[] lModulesBO = this.scrDoc.getAllModules();
        
        for (Object obj : lModulesBO){
            ModuleBO mBO = (ModuleBO) obj;
            if (!mBO.isSection()) {
                continue;
            }
            String strSe = mBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().get(0).getData();
            strSe = strSe.substring(strSe.indexOf("</p><p style=\"margin-top: 0\">") + 29);
            strSe = strSe.substring(0, strSe.indexOf("</p>"));
            if (mBO.isSection() && strSe.equals(strSection)) {
                Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Ya existe la sección en el documento", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        ModuleSectionBO modSecBO = new ModuleSectionBO();
        modSecBO.setModuleId(moduleBO.getModuleId());
        modSecBO.setContentType("T");
        modSecBO.setHeight(moduleBO.getHeight());
        modSecBO.setLstObjects(new ArrayList<>());
        modSecBO.setType("N");
        ObjectInfoBO objInfoBO = new ObjectInfoBO();
        objInfoBO.setHeight(moduleBO.getHeight());
        objInfoBO.setData(strSectionHTML);
        objInfoBO.setPlain_text(strSectionHTML);
        objInfoBO.setObjType(4);
        objInfoBO.setWidth(moduleBO.getWidth());
        modSecBO.getLstObjects().add(objInfoBO);
        moduleBO.setLstSectionsText(new ArrayList<>());
        moduleBO.getLstSectionsText().add(modSecBO);
        moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).setLstObjects(new ArrayList<>());
        objInfoBO.setSectionId(moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getSectionId());
        moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().add(objInfoBO);
        moduleBO.setSection(true);

        this.scrDoc.addModule3(moduleBO);
        
    }
   
    protected void addModules(){
        if( tableCand.getRowCount()<=0 ){
            return;
        }
        
        int  id=0;
        
        int index=tableCand.getSelectedRow();
        CollabModCandTModel model= (CollabModCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list=model.getList();
        if( list==null ){
            return;
        }
        
        List<DocCollabCandidateBO> lstCandSel = new ArrayList<>();
        
        for (int i = 0; i < tableCand.getRowCount(); i++) {
            lstCandSel.add(list.get(tableCand.convertRowIndexToModel(i)));
        }
        
        DocumentCollabItemBO docCollabItem=null;
        Map<String , DocCollabCandidateBO> lstDel=new TreeMap<>();
        //CollabDocsLModel  listModel=(CollabDocsLModel) listDocuments.getModel();               
        ModuleBO moduleBO;
        for( DocCollabCandidateBO collabCandBO :  lstCandSel  ){
            if( collabCandBO.isCheck() ){
                docCollabItem=new DocumentCollabItemBO();     
                //docCollabItem.setItemDocVersionId( collabCandBO.getDocVersionId() );
                docCollabItem.setDocumentModuleId( collabCandBO.getDocumentModuleId() );
                docCollabItem.setModuleId( collabCandBO.getModuleId() );
                docCollabItem.setDocumentName(collabCandBO.getCandName() );
                docCollabItem.setIdHeader(collabCandBO.getIdHeader());
                docCollabItem.setIdSection(collabCandBO.getIdSection());
                
                this.lstCollabItems.add(docCollabItem);
                
                  
                id=collabCandBO.getDocumentModuleIdTarget() >0 ? collabCandBO.getDocumentModuleIdTarget() :   collabCandBO.getDocumentModuleId();
                moduleBO=this.scrDoc.addModule2(collabCandBO.getModuleId(),
                     id , collabCandBO.getIdHeader(), collabCandBO.getIdSection());
               // if( this.  )
               
                lstDel.put( armaClave( collabCandBO.getModuleId() ,id  )  , collabCandBO);
                
                
                if( collabCandBO.getCollabDocCandidateId()>0 ){
                    this.mapCandDelete.put(armaClave( collabCandBO.getModuleId() ,id   ) , collabCandBO );                     
                    this.mapCandAdd.remove( armaClave( collabCandBO.getModuleId() ,id   )  );
                }else{
                    this.mapCandAdd.remove( armaClave( collabCandBO.getModuleId() ,id   )  );
                }
                
                
                
                
            }
        }

        Set<String> set=lstDel.keySet();
        Iterator<String> it =set.iterator();
        String cve;
        while( it.hasNext() ){
           cve=it.next();
           if(cve==null){
               continue;
           }
           
            for( DocCollabCandidateBO collabCandBO :  list  ){
                 id=collabCandBO.getDocumentModuleIdTarget() >0 ? collabCandBO.getDocumentModuleIdTarget() :   collabCandBO.getDocumentModuleId();
                if(  (armaClave( collabCandBO.getModuleId() ,id   )).equals(cve)   ){
                    list.remove(collabCandBO);
                    break;
                }
            }                        
        }
        model.refreshData();
        model.fireTableDataChanged();

        
        //listDocs.add(docCollab);
 
        
    }
    
    
    
    /*
    
    
    protected void addModules(){
        if( tableCand.getRowCount()<=0 ){
            return;
        }
        
        int index=tableCand.getSelectedRow();
        CollabModCandTModel model= (CollabModCandTModel) tableCand.getModel();
        List<DocCollabCandidateBO> list=model.getList();
        if( list==null ){
            return;
        }
        
        DocumentCollabItemBO docCollabItem=null;
        Map<String , DocCollabCandidateBO> lstDel=new TreeMap<>();
        //CollabDocsLModel  listModel=(CollabDocsLModel) listDocuments.getModel();               
        ModuleBO moduleBO;
        for( DocCollabCandidateBO collabCandBO :  list  ){
            if( collabCandBO.isCheck() ){
                docCollabItem=new DocumentCollabItemBO();     
                //docCollabItem.setItemDocVersionId( collabCandBO.getDocVersionId() );
                docCollabItem.setDocumentModuleId( collabCandBO.getDocumentModuleId() );
                docCollabItem.setModuleId( collabCandBO.getModuleId() );
                docCollabItem.setDocumentName(collabCandBO.getCandName() );
                docCollabItem.setIdHeader(collabCandBO.getIdHeader());
                docCollabItem.setIdSection(collabCandBO.getIdSection());
                
                this.lstCollabItems.add(docCollabItem);
                
                  
                
                moduleBO=this.scrDoc.addModule2(collabCandBO.getModuleId(),
                     collabCandBO.getDocumentModuleIdTarget() >0 ? collabCandBO.getDocumentModuleIdTarget() :   collabCandBO.getDocumentModuleId(), collabCandBO.getIdHeader(), collabCandBO.getIdSection());
               // if( this.  )
               
                lstDel.put( armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId()   )  , collabCandBO);
                
                
                if( collabCandBO.getCollabDocCandidateId()>0 ){
                    this.mapCandDelete.put(armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId()   ) , collabCandBO );                     
                    this.mapCandAdd.remove( armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId()   )  );
                }else{
                    this.mapCandAdd.remove( armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId()   )  );
                }
                
                
                
                
            }
        }

        Set<String> set=lstDel.keySet();
        Iterator<String> it =set.iterator();
        String cve;
        while( it.hasNext() ){
           cve=it.next();
           if(cve==null){
               continue;
           }
           
            for( DocCollabCandidateBO collabCandBO :  list  ){
                 
                if(  (armaClave( collabCandBO.getModuleId() ,collabCandBO.getDocumentModuleId()   )).equals(cve)   ){
                    list.remove(collabCandBO);
                    break;
                }
            }                        
        }
        model.refreshData();
        model.fireTableDataChanged();
        
        //listDocs.add(docCollab);
 
        
    }
    
    
    */
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
                int idHeader = 0;
                int idSection = 0;
                
                Object objEnc = cmbEncabezado.getSelectedItem();
                if (objEnc != null) {
                    idHeader = ((HeaderColModBO)objEnc).getIdHeaderColMod();
                }
                
                Object objSec = cmbSeccion.getSelectedItem();
                if (objSec != null) {
                    idSection = ((SectionColModBO)objSec).getIdSectionColMod();
                }
                
                CollaborativesDAO dao = new CollaborativesDAO();
                list = dao.getDocCandidates( GlobalDefines.TIPO_CAND_MODULE ,  document.getDocVersionId()  , idHeader, idSection);
                if(list!=null){
                    for( DocCollabCandidateBO cand: list ){
                       if( mapOrigCandidates ==null ){
                           mapOrigCandidates=new TreeMap<>();
                       }
                       
                       mapOrigCandidates.put( armaClave(  cand.getModuleId() , cand.getDocumentModuleId()   ) , cand);
                       
                       
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
                lstCandidates=new ArrayList<>();
                if( lstTmp!=null ){
                    for( DocCollabCandidateBO candBO : lstTmp  ){
                        if( mapCandDelete.get(armaClave( candBO.getModuleId() ,candBO.getDocumentModuleId()   )) ==null ){
                            //no lo encontro por lo que  hay que agregarlo
                            if( lstCandidates==null ){
                                lstCandidates=new ArrayList<>();
                            }
                            lstCandidates.add(candBO);
                        }
                        
                    }
                }
                                                                                
                modelTableCand.setData(lstCandidates);
                modelTableCand.fireTableDataChanged();
                
                  
                
            } catch (Exception ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {
                
                
              //  setRenderEditorToColumns(jxTable);
                bar.setVisible(false);               
            }
            
            
            
            
            
            
            
            /*
            try {
                lstCandidates = get();
                modelTableCand.setData(lstCandidates);
                modelTableCand.fireTableDataChanged();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {
                
                
              //  setRenderEditorToColumns(jxTable);
                bar.setVisible(false);               
            } */
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
            
            List<String> listRsp=new ArrayList<>();
            try {
                
                scrDoc.getDocBO().setCollaborative(true);                
                scrDoc.getDocBO().setCollaborativeType(GlobalDefines.COLLAB_TYPE_MOD );
                if (scrDoc.saveDocumentMod()){
                    //MainView.main.getScrDocument().setDocBO(scrDoc.getDocBO());
                    Object objDoc = cmbTipDoc.getSelectedItem();
                    DocumentTypeBO docTypeBO = (DocumentTypeBO)objDoc;
                    if (docTypeBO.isSendEmail()) {
                        btnPublicar.setEnabled(true);
                    }
                    btnViewPDF.setEnabled(true);
                    btnViewHTML.setEnabled(true);
                }
                
                CollaborativesDAO dao = new CollaborativesDAO();
                dao.upsertCollaboratives(document);
                                                
                listRsp.add( "0"); 
                listRsp.add("");
  
                
            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
                listRsp.set(0, "1");
                listRsp.set(1, ex.getMessage()  );
                
                
            }
            return listRsp;
        }

        @Override
        protected void done() {
            List<String> listRsp = null;
            Map<Integer,Integer> mapRelModules=new TreeMap<Integer,Integer>();
            try {
                listRsp = get();
                if( listRsp!=null && listRsp.size()>1 ){
                    
                    if( listRsp.get(0).equals("0") ){
                        
                        
                        //****** inicio
                        DocumentBO docBO=scrDoc.getDocBO();
                        Map<Integer,HojaBO>  mapHojas=docBO.getMapHojas();
                        HojaBO  hojaBO;
                        Set<Integer> keySetRel;
                        Iterator<Integer> itRel;
                        List<ModuleBO> lstModules;
                        if(mapHojas!=null&&mapHojas.size()>0){
                            keySetRel=mapHojas.keySet();
                            itRel=keySetRel.iterator();
                            while( itRel.hasNext() ){
                                hojaBO=mapHojas.get(itRel.next());
                                if(hojaBO==null){
                                    continue;
                                }
                                lstModules=hojaBO.getLstModules();
                                if(lstModules==null|| lstModules.size()<=0){
                                    continue;
                                }
                                
                                for( ModuleBO moduleBO : lstModules ){
                                    mapRelModules.put( moduleBO.getOrigDocModuleId()  ,  moduleBO.getDocumentModuleId() );                                    
                                }
                                
                                
                                
                                
                            }
                            
                        }
                        
                                                                                                
  //Eliminamos los que estaban en la lista de candidatos
                          Set<String> set=mapCandDelete.keySet();
                          CollaborativesDAO collabDAO=new CollaborativesDAO();
                          Iterator<String> it =set.iterator();
                          String cve;
                          Integer documentModuleIdNew;
                          int iDocumentModuleIdNew;
                          DocCollabCandidateBO collabCandBO;
                          while( it.hasNext() ){
                             iDocumentModuleIdNew=0; 
                             documentModuleIdNew=null;
                             cve=it.next();
                             if(cve==null){
                                 continue;
                             }
                             collabCandBO=mapCandDelete.get(cve);
                             
                             if( collabCandBO.getCollabDocCandidateId()>0  ){
                                 documentModuleIdNew=null;
                                 if( collabCandBO.getDocumentModuleId()>0 ){
                                     documentModuleIdNew=mapRelModules.get(collabCandBO.getDocumentModuleId()  );
                                     if( documentModuleIdNew!=null ){
                                         iDocumentModuleIdNew=documentModuleIdNew;
                                     }
                                 }
                                  
                                  collabDAO.deleteModuleCollabCandLog(collabCandBO.getCollabDocCandidateId() , iDocumentModuleIdNew  );
                              }                                                                        
                          }

                          mapCandDelete=new TreeMap<>();                      
                          
                          
                          //Agregamos los nuevos
                          
                          set=mapCandAdd.keySet();                           
                          it =set.iterator();                          
                          while( it.hasNext() ){
                             cve=it.next();
                             if(cve==null){
                                 continue;
                             }
                             collabCandBO=mapCandAdd.get(cve);
                             if( collabCandBO.getCollabDocCandidateId()<=0  ){
                                  collabDAO.addDocumentCollabCand(collabCandBO);
                              }else{
                                 collabDAO.restoreModuleCollabCandLogById(collabCandBO.getCollabDocCandidateId()  );
                             }                                                                        
                          }

                          mapCandAdd=new TreeMap<>();                      
                          
                                                  
                        
                        
                        
                        //***** fin
                        
                        Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Se guardo el documento correctamente ", JOptionPane.INFORMATION_MESSAGE);
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
      
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbMercado = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbTipDoc = new javax.swing.JComboBox();
        btnActualizar = new javax.swing.JButton();
        chkSelectAll = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        cmbSeccion = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cmbEncabezado = new javax.swing.JComboBox();
        cmbAddSection = new javax.swing.JButton();
        btnAddHeader = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        edNombre = new javax.swing.JTextField();
        addHeader = new javax.swing.JButton();
        addSection = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        scrollTableCand = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        mainPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        scrollSearchModule = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        btnPublicar = new javax.swing.JButton();
        btnViewHTML = new javax.swing.JButton();
        btnViewPDF = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 15));
        jPanel1.setMinimumSize(new java.awt.Dimension(460, 309));
        jPanel1.setPreferredSize(new java.awt.Dimension(580, 428));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(0, 260));
        jPanel3.setPreferredSize(new java.awt.Dimension(346, 230));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Tipo de documento:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        cmbMercado.setPreferredSize(new java.awt.Dimension(250, 22));
        cmbMercado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMercadoItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbMercado, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Mercado:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        cmbTipDoc.setPreferredSize(new java.awt.Dimension(250, 22));
        jPanel3.add(cmbTipDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, -1, -1));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel3.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        chkSelectAll.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        chkSelectAll.setText("Seleccionar Todos");
        chkSelectAll.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        jPanel3.add(chkSelectAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, 150, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 100, 20));

        cmbSeccion.setPreferredSize(new java.awt.Dimension(250, 22));
        jPanel3.add(cmbSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 190, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Encabezado:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 20));

        cmbEncabezado.setAutoscrolls(true);
        cmbEncabezado.setMinimumSize(new java.awt.Dimension(50, 22));
        cmbEncabezado.setPreferredSize(new java.awt.Dimension(190, 22));
        cmbEncabezado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEncabezadoItemStateChanged(evt);
            }
        });
        cmbEncabezado.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                cmbEncabezadoInputMethodTextChanged(evt);
            }
        });
        jPanel3.add(cmbEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        cmbAddSection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_add_.png"))); // NOI18N
        cmbAddSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAddSectionActionPerformed(evt);
            }
        });
        jPanel3.add(cmbAddSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 30, 20));

        btnAddHeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_add_.png"))); // NOI18N
        btnAddHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHeaderActionPerformed(evt);
            }
        });
        jPanel3.add(btnAddHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 30, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Sección:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 60, 20));

        edNombre.setEditable(false);
        jPanel3.add(edNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 250, -1));

        addHeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_right_back_arrow_blue.png"))); // NOI18N
        addHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHeaderActionPerformed(evt);
            }
        });
        jPanel3.add(addHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 30, 20));

        addSection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/small_right_back_arrow_blue.png"))); // NOI18N
        addSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSectionActionPerformed(evt);
            }
        });
        jPanel3.add(addSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 30, 20));

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

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
        jButton6.setText("Agregar Módulos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton6);

        jPanel5.add(jPanel12, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_END);

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

        jSplitPane1.setDividerLocation(1140);

        mainPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        mainPanel.setMinimumSize(new java.awt.Dimension(300, 4));
        mainPanel.setPreferredSize(new java.awt.Dimension(300, 4));
        mainPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(mainPanel);

        jPanel9.setMaximumSize(new java.awt.Dimension(130, 2147483647));
        jPanel9.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel9.setPreferredSize(new java.awt.Dimension(130, 10));
        jPanel9.setVerifyInputWhenFocusTarget(false);
        jPanel9.setLayout(new java.awt.BorderLayout());

        scrollSearchModule.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        scrollSearchModule.setMaximumSize(new java.awt.Dimension(80, 32767));
        jPanel9.add(scrollSearchModule, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel9);

        jPanel7.add(jSplitPane1, java.awt.BorderLayout.CENTER);

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

        btnPublicar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPublicar.setText("Publicar");
        btnPublicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPublicarActionPerformed(evt);
            }
        });
        jPanel8.add(btnPublicar);

        btnViewHTML.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnViewHTML.setText("Ver HTML");
        btnViewHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewHTMLActionPerformed(evt);
            }
        });
        jPanel8.add(btnViewHTML);

        btnViewPDF.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnViewPDF.setText("Ver PDF");
        btnViewPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPDFActionPerformed(evt);
            }
        });
        jPanel8.add(btnViewPDF);

        jPanel2.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        addModules();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        saveDocument();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnViewHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewHTMLActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        htmlPreview();
    }//GEN-LAST:event_btnViewHTMLActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        loadCandidateDocs();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnViewPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPDFActionPerformed
        // TODO add your handling code here:
        Utilerias.pasarGarbageCollector();
        pdfPreview();
    }//GEN-LAST:event_btnViewPDFActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        borrarSelected();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void cmbEncabezadoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_cmbEncabezadoInputMethodTextChanged
        
    }//GEN-LAST:event_cmbEncabezadoInputMethodTextChanged

    private void cmbEncabezadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEncabezadoItemStateChanged
        fillComboSeccion();
    }//GEN-LAST:event_cmbEncabezadoItemStateChanged

    private void cmbAddSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAddSectionActionPerformed
        DlgSectionColMod dlgSect = new DlgSectionColMod(null, true);
       
        MarketBO market=(MarketBO)this.cmbMercado.getSelectedItem();
        DocumentTypeBO type= (DocumentTypeBO)this.cmbTipDoc.getSelectedItem();
        
        Object objEncabezado = cmbEncabezado.getSelectedItem();
        HeaderColModBO hbo = (HeaderColModBO) objEncabezado;
        
        dlgSect.setFields( market.getIdMarket(), type.getIddocument_type(), hbo.getIdHeaderColMod());
        
        dlgSect.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        dlgSect.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                fillComboSeccion();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                fillComboSeccion();
            }
        });

        dlgSect.setLocationRelativeTo(null);
        dlgSect.setVisible(true);
    }//GEN-LAST:event_cmbAddSectionActionPerformed

    private void btnAddHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddHeaderActionPerformed
        DlgHeaderColMod dlgHead = new DlgHeaderColMod(null, true);
        dlgHead.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        MarketBO market=(MarketBO)this.cmbMercado.getSelectedItem();
        DocumentTypeBO type= (DocumentTypeBO)this.cmbTipDoc.getSelectedItem();
        
        dlgHead.setFields( market.getIdMarket()  , type.getIddocument_type() );
        dlgHead.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                fillComboEncabezado();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                fillComboEncabezado();
            }
        });

        dlgHead.setLocationRelativeTo(null);
        dlgHead.setVisible(true);
    }//GEN-LAST:event_btnAddHeaderActionPerformed

    private void btnPublicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPublicarActionPerformed
        Utilerias.pasarGarbageCollector();
        sendPublish();
    }//GEN-LAST:event_btnPublicarActionPerformed

    private void addHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHeaderActionPerformed
        Utilerias.pasarGarbageCollector();
        addHeader();
    }//GEN-LAST:event_addHeaderActionPerformed

    private void addSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSectionActionPerformed
        Utilerias.pasarGarbageCollector();
        addSection();
    }//GEN-LAST:event_addSectionActionPerformed

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMercadoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addHeader;
    private javax.swing.JButton addSection;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAddHeader;
    private javax.swing.JButton btnPublicar;
    private javax.swing.JButton btnViewHTML;
    private javax.swing.JButton btnViewPDF;
    private javax.swing.JCheckBox chkSelectAll;
    private javax.swing.JButton cmbAddSection;
    private javax.swing.JComboBox cmbEncabezado;
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbSeccion;
    private javax.swing.JComboBox cmbTipDoc;
    private javax.swing.JTextField edNombre;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollSearchModule;
    private javax.swing.JScrollPane scrollTableCand;
    // End of variables declaration//GEN-END:variables






}


class OpenPublishColabModWorker extends SwingWorker<String, String> {

    ScrIntegradorMod scrIntegradorMod;
    boolean openPublish;

    public OpenPublishColabModWorker(ScrIntegradorMod scrIntegradorMod, boolean openPublish) {
        this.openPublish = openPublish;
        this.scrIntegradorMod = scrIntegradorMod;
    }

    @Override
    protected String doInBackground() throws Exception {
        publish("");
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        try {
            MainView.main.setCollabIntegradorMod(scrIntegradorMod, openPublish);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    @Override
    protected void done() {
        MainView.main.getProgressBar().setVisible(false);
    }

}
 
