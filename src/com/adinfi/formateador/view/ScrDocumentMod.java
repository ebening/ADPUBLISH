package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.dnd.ScrCell;
import com.adinfi.formateador.view.resources.DnDTabbedPane;
import img.ImageDummy;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Desarrollador
 */
public class ScrDocumentMod extends ScrDocument {

    private int documentId_;
    private String documentName;
    private DocumentBO docBO;
    int meterText = 0;
    short currentHoja = 1;
    private List<JPanel> listaTextos = new ArrayList<>();
    private List<DocumentCollabItemBO> lstCollabMod=new ArrayList<>();
    ScrIntegradorMod scrIntMod;
    private boolean isMovedPage = false;
    //private List<String> hojasDel = new ArrayList<>();    
    private boolean isOpenDoc;
    private int contObj;
    private short hojaAct;    

    /**
     * Creates new form ScrDocument
     */
    public ScrDocumentMod(ScrIntegradorMod scrIntMod) {
       // super();
        initComponents();
        this.scrIntMod=scrIntMod;
        //**
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
                //tabHojasMouseDragged(evt);
                Utilerias.pasarGarbageCollector();
                isMovedPage = true;
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
        
        
       tabHojas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                Utilerias.pasarGarbageCollector();
                if (Me.isPopupTrigger()) {
                    popHojas.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
        
        
        
        
        //**
        initScreen();

      //  initMenuEmergente();
    }
    
    
    /*
    protected  void initScreen() {
        tabHojas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    popHojas.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
    }    
    */
    

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
               // jMenuItemActionBorrarModulo(evt);
            }
        });

     
        popHojas.addSeparator();
        popHojas.add(mbItemBorrarModulo);
        //popHojas.add(mbItemEnlazar);

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
    
    
    //public void deleteModule(int moduleId, int documentModuleId) {
    public void deleteModule(ModuleBO module) {
      //  if (JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar el módulo ?", "Eliminar Módulo", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
      //      return;
      //  }
        
        
        int numHoja = Integer.parseInt( tabHojas.getTitleAt( tabHojas.getSelectedIndex() ) );
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
        
        if (!module.isHeaderOrSection() && !module.isSection()) {
            for (ModuleBO moduleBO : lstModules) {

                if(module.getDocumentModuleId()>0){
                    if(moduleBO.getDocumentModuleId() == module.getDocumentModuleId() ){

                        lstModules.remove(i);
                        moduleRemove = moduleBO;
                        break;                    

                    }


                }else{

                    if (moduleBO.getModuleId() == module.getModuleId()) {
                        lstModules.remove(i);
                        moduleRemove = moduleBO;
                        break;
                    }


                }


                i++;
            }
        } else {
            for (ModuleBO moduleBO : lstModules) {
                if (module.getIdHeader() < 1 && module.getIdSection() < 1) {
                    if(moduleBO.getDocumentModuleId() == module.getDocumentModuleId() ){
                        lstModules.remove(i);
                        moduleRemove = moduleBO;
                        break;                    
                    }
                } else if(module.isHeaderOrSection()){
                    if ( moduleBO.getIdHeader() == module.getIdHeader() ){
                        lstModules.remove(i);
                        moduleRemove = moduleBO;
                        break;                    
                    }
                }else{
                    if(moduleBO.getIdSection() == module.getIdSection() ){
                        lstModules.remove(i);
                        moduleRemove = moduleBO;
                        break;                    

                    }
                }
                i++;
            }
        }

        if (moduleRemove != null) {
            this.contentPanel.remove(moduleRemove.getPanModule());
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            contentPanel.invalidate();
            contentPanel.repaint();
            contentPanel.getParent().invalidate();
            contentPanel.getParent().repaint();
            scrIntMod.removeModule(moduleRemove);
        }
        

    }
    
    protected void moveUp(int idSection, int idGen){
        int tabSelc = tabHojas.getSelectedIndex();
        Object[] lModulesBO = getAndDeleteAllModules();
        this.getDocBO().getMapHojas().clear();
        
        int numHojas = tabHojas.getTabCount();
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja((short)numHojas);
        this.getDocBO().getMapHojas().put(numHojas, hojaBO);
        
        for (int i = 0; i < lModulesBO.length; i++){
            ModuleBO modBO = (ModuleBO)lModulesBO[i];
            if (modBO.getRootSection().getSectionId() == idSection && idGen == (modBO.getDocumentModuleId() + modBO.getIdHeader() + modBO.getIdSection())){
                if (i == 0) {
                    break;
                }
                
                ModuleBO modAux = (ModuleBO)lModulesBO[i - 1];
                lModulesBO[i-1] = modBO;
                lModulesBO[i] = modAux;
            }
        }
       
        for (Object obj : lModulesBO){
            ModuleBO moduleBO = (ModuleBO) obj;
            //if (moduleBO.isHeaderOrSection()) {
            //moduleBO.setDocumentModuleId(0);
                this.addModule3(moduleBO);
            /*} else {
                this.addModule2(moduleBO.getModuleId(), 
                                moduleBO.getDocumentModuleId() > 0 ? moduleBO.getDocumentModuleId() : moduleBO.getOrigDocModuleId(), 
                                moduleBO.getIdHeader(), 
                                moduleBO.getIdSection());
            }*/
        }
        
        if (tabSelc >= tabHojas.getTabCount()) {
            tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);
        } else {
            tabHojas.setSelectedIndex(tabSelc);
        }
    }
    
    
    protected void moveDown(int idSection, int idGen){
        int tabSelc = tabHojas.getSelectedIndex();
        Object[] lModulesBO = getAndDeleteAllModules();
        this.getDocBO().getMapHojas().clear();
        
        int numHojas = tabHojas.getTabCount();
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja((short)numHojas);
        this.getDocBO().getMapHojas().put(numHojas, hojaBO);
        
        for (int i = 0; i < lModulesBO.length; i++){
            ModuleBO modBO = (ModuleBO)lModulesBO[i];
            if (modBO.getRootSection().getSectionId() == idSection && idGen == (modBO.getDocumentModuleId() + modBO.getIdHeader() + modBO.getIdSection())){
                if (i == lModulesBO.length - 1) {
                    break;
                }
                
                ModuleBO modAux = (ModuleBO)lModulesBO[i + 1];
                lModulesBO[i + 1] = modBO;
                lModulesBO[i] = modAux;
                break;
            }
        }
       
        for (Object obj : lModulesBO){
            ModuleBO moduleBO = (ModuleBO) obj;
            
            //if (moduleBO.isHeaderOrSection()) {
                this.addModule3(moduleBO);
            /*} else {
                this.addModule2(moduleBO.getModuleId(), 
                                moduleBO.getDocumentModuleId() > 0 ? moduleBO.getDocumentModuleId() : moduleBO.getOrigDocModuleId(), 
                                moduleBO.getIdHeader(), 
                                moduleBO.getIdSection());
            }*/
        }
        
        if (tabSelc >= tabHojas.getTabCount()) {
            tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);
        } else {
            tabHojas.setSelectedIndex(tabSelc);
        }
    }
    
    private Object[] getAndDeleteAllModules() {
        Collection<HojaBO> colHojasBO = this.getDocBO().getMapHojas().values();
        Object[] lstModulesCol = new Object[0];
        List<ModuleBO> lModules = new ArrayList<>();
        if (colHojasBO == null) {
            return null;
        }
        
        /*if (hojasDel == null) {
            hojasDel = new ArrayList<>();
        }*/
        
        Iterator<HojaBO> iterHojas = colHojasBO.iterator();
        while (iterHojas.hasNext()){
            HojaBO hojaBO = iterHojas.next();

            List<ModuleBO> lstModules = hojaBO.getLstModules();
            if (lstModules == null) {
                continue;
            }
            
            /*if (!hojasDel.contains(String.valueOf(hojaBO.getHoja()))) {
                hojasDel.add(String.valueOf(hojaBO.getHoja()));
            }*/
            //this.quitModules(lstModules);
            
            lModules.addAll(lstModules);
        }
        this.contentPanel.removeAll();
        this.tabHojas.removeAll();
        this.addHoja(true);
        lstModulesCol = new Object[lModules.size()];
        lstModulesCol = lModules.toArray();
        return lstModulesCol;
    }
    
    public Object[] getAllModules() {
        Collection<HojaBO> colHojasBO = this.getDocBO().getMapHojas().values();
        Object[] lstModulesCol = new Object[0];
        List<ModuleBO> lModules = new ArrayList<>();
        if (colHojasBO == null) {
            return null;
        }
        
        Iterator<HojaBO> iterHojas = colHojasBO.iterator();
        while (iterHojas.hasNext()){
            HojaBO hojaBO = iterHojas.next();

            List<ModuleBO> lstModules = hojaBO.getLstModules();
            if (lstModules == null) {
                continue;
            }
            
            lModules.addAll(lstModules);
        }
        lstModulesCol = new Object[lModules.size()];
        lstModulesCol = lModules.toArray();
        return lstModulesCol;
    }
    
    public void deleteModules(List<ModuleBO> lstModules) {
        for (ModuleBO moduleBO : lstModules) {
            this.contentPanel.remove(moduleBO.getPanModule());
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            contentPanel.invalidate();
            contentPanel.repaint();
            contentPanel.getParent().invalidate();
            contentPanel.getParent().repaint();
            scrIntMod.removeModule(moduleBO);
        }
    }
    
    public void quitModules(List<ModuleBO> lstModules) {
        for (ModuleBO moduleBO : lstModules) {
            this.contentPanel.remove(moduleBO.getPanModule());
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            contentPanel.invalidate();
            contentPanel.repaint();
            contentPanel.getParent().invalidate();
            contentPanel.getParent().repaint();
        }
    }
    
    protected void resetSection( ModuleSectionBO modSection ){
        if( modSection==null ){
            return;
        }
        
        List<ObjectInfoBO> lstObjects;
        List<ModuleSectionBO> lstSecChilds;
        if (GlobalDefines.SEC_TYPE_CELL.equals(modSection.getType())) {
           lstObjects= modSection.getLstObjects();
           if( lstObjects!=null ){
               for( ObjectInfoBO object: lstObjects ){
                   object.setObjectId(0);
               }
           }
        }else{
           if(  modSection.getChildSectionsAsList()!=null ) {
               lstSecChilds=modSection.getChildSectionsAsList();
               for( ModuleSectionBO secChild : lstSecChilds  ){
                  resetSection(secChild);
               }
               
               
           }
            
        }   
        
    }
    
    

    
   public void resetModule( ModuleBO module ) {
       if(module==null){
           return ;
       }
       
       module.setDocumentModuleId(0);
       if( module.getRootSection()!=null ){
          resetSection( module.getRootSection() );
       }               
       
       
       
       
   }
    
   /*
   ModuleBO  addModule2(int moduleId, int documentModuleId, int idHeader, int idSection) throws UnsupportedOperationException {
        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
            
        }
        
        DocumentDAO docDAO=null;
        ModuleBO moduleBO;
        if( documentModuleId>0 ){
             docDAO=new DocumentDAO();
             moduleBO=docDAO.getDocModule(documentModuleId); 
             moduleBO.setDocumentModuleId(0);
             if( moduleBO.getOrigDocModuleId()<=0 ){
                moduleBO.setOrigDocModuleId(documentModuleId);
             }
             resetModule(moduleBO);
            // moduleBO.
                          
        }else{
             TemplateDAO tempDAO = new TemplateDAO();
             moduleBO = tempDAO.getModule(moduleId);
        }
        
        moduleBO.setIdHeader(idHeader);
        moduleBO.setIdSection(idSection);
              
        if (moduleBO == null) {
            return null;
        }

        int numHoja = (tabHojas.getSelectedIndex()<0?0:tabHojas.getSelectedIndex()) + 1;
        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return null;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
        }

        
        int nuevaHoja = 0;
        boolean b = addToNextPage(lstModules, moduleBO, tabHojas.getSelectedIndex() == 0);
        int antHoja = numHoja;

        if (b == true) {
            //checamos si hay espacio en la ultima hoja
            numHoja = tabHojas.getTabCount();
            if (numHoja > antHoja) {
                hojaBO = this.getDocBO().getMapHojas().get(numHoja);
                if (hojaBO == null) {
                    return null;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                b = addToNextPage(lstModules, moduleBO, tabHojas.getTabCount() == 1);
            } else {
                b = true;
            }

            //No hay lugar en la ultima pagina
            if (b == true) {
                // Salto de linea
                // addHojaADocumentoExistente();
                hojaBO = createLastHoja();
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
        contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        
        PanelModParent panParent=new PanelModParent( this , moduleBO  ); 
        contentPanel.add(panParent);
        
        
        
        JPanel panModule = addLayout(panParent.getContent(), moduleBO.getRootSection(), moduleBO);
        
        
        
        
      //  moduleBO.setPanModule(panModule);
        moduleBO.setPanModule(panParent);
        
        

        if (antHoja != numHoja) {
            showHoja((short) numHoja);
            tabHojas.setSelectedIndex(numHoja - 1);
         //   tabHojas.validate();
            tabHojas.repaint();
            tabHojas.updateUI();
          //  contentPanel.validate();
         //   contentPanel.getRootPane().revalidate();
          //  contentPanel.getRootPane().updateUI();
          //  contentPanel.update( contentPanel.getGraphics());
            
          //  panParent.getParent().validate();
          
        }
        
            tabHojas.repaint();
            
           
        
        
        return moduleBO;

    } */
   
   
   
   ModuleBO  addModule2(int moduleId, int documentModuleId, int idHeader, int idSection) throws UnsupportedOperationException {
        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
            
        }
        
        DocumentDAO docDAO=null;
        ModuleBO moduleBO;
        if( documentModuleId>0 ){
             docDAO=new DocumentDAO();
             moduleBO=docDAO.getDocModule(documentModuleId); 
             moduleBO.setDocumentModuleId(0);
             if( moduleBO.getOrigDocModuleId()<=0 ){
                moduleBO.setOrigDocModuleId(documentModuleId);
             }
             resetModule(moduleBO);
            // moduleBO.
                          
        }else{
             TemplateDAO tempDAO = new TemplateDAO();
             moduleBO = tempDAO.getModule(moduleId);
        }
        
        moduleBO.setIdHeader(idHeader);
        moduleBO.setIdSection(idSection);
              
        if (moduleBO == null) {
            return null;
        }
        
        int numHoja = tabHojas.getSelectedIndex();
        numHoja = Integer.parseInt(tabHojas.getTitleAt(numHoja));

        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return null;
        }

        if (hojaBO == null) {
            return null;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
        }

        
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
                    return null;
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
                
                this.hojasDel.remove(""+numHoja);
            }
        }
        // Misma pagina            
        lstModules.add(moduleBO);
        moduleBO.setHoja((short) numHoja);
        contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        
        PanelModParent panParent=new PanelModParent( this , moduleBO  ); 
        contentPanel.add(panParent);
        
        
        
        JPanel panModule = addLayout(panParent.getContent(), moduleBO.getRootSection(), moduleBO);
        
        
        
        
      //  moduleBO.setPanModule(panModule);
        moduleBO.setPanModule(panParent);
        
        

        if (antHoja != numHoja) {
            showHoja((short) numHoja);
        //    tabHojas.setSelectedIndex(numHoja - 1);
         //   tabHojas.validate();
            tabHojas.repaint();
            tabHojas.updateUI();
          //  contentPanel.validate();
         //   contentPanel.getRootPane().revalidate();
          //  contentPanel.getRootPane().updateUI();
          //  contentPanel.update( contentPanel.getGraphics());
            
          //  panParent.getParent().validate();
          
        }
        
            tabHojas.repaint();
            
           
        
        
        return moduleBO;

    }   
   
   ModuleBO  addModule3(ModuleBO moduleBO) throws UnsupportedOperationException {
        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
            
        }
        
        int numHoja = (tabHojas.getSelectedIndex()<0?0:tabHojas.getSelectedIndex()) + 1;
        HojaBO hojaBO = this.getDocBO().getMapHojas().get(numHoja);
        if (hojaBO == null) {
            return null;
        }
        List<ModuleBO> lstModules = hojaBO.getLstModules();
        if (lstModules == null) {
            lstModules = new ArrayList<>();
            hojaBO.setLstModules(lstModules);
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
            numHoja = tabHojas.getTabCount();
            if (numHoja > antHoja) {
                hojaBO = this.getDocBO().getMapHojas().get(numHoja);
                if (hojaBO == null) {
                    return null;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null) {
                    lstModules = new ArrayList<>();
                    hojaBO.setLstModules(lstModules);
                }
                b = addToNextPage(lstModules, moduleBO, tabHojas.getTabCount() == 1);
            } else {
                b = true;
            }

            //No hay lugar en la ultima pagina
            if (b == true) {
                // Salto de linea
                // addHojaADocumentoExistente();
                hojaBO = createLastHoja();
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
        contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        
        PanelModParent panParent=new PanelModParent( this , moduleBO  ); 
        contentPanel.add(panParent);
        
        
        
        JPanel panModule = addLayout(panParent.getContent(), moduleBO.getRootSection(), moduleBO);
        
        
        
        
      //  moduleBO.setPanModule(panModule);
        moduleBO.setPanModule(panParent);
        
        

        if (antHoja != numHoja) {
            showHoja((short) numHoja);
            tabHojas.setSelectedIndex(numHoja - 1);
         //   tabHojas.validate();
            tabHojas.repaint();
            tabHojas.updateUI();
          //  contentPanel.validate();
         //   contentPanel.getRootPane().revalidate();
          //  contentPanel.getRootPane().updateUI();
          //  contentPanel.update( contentPanel.getGraphics());
            
          //  panParent.getParent().validate();
          
        }
        
            tabHojas.repaint();
            
           
        
        
        return moduleBO;

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
        
        /*
        if(!validarMovDelConcat()){
            JOptionPane.showMessageDialog(null, "La hoja no se puede eliminar ya que tiene texto concatenado.");
            return;
        } */

        int result = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar la hoja?.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (getHojasDel() == null) {
                setHojasDel(new ArrayList<>());
            } 
            getHojasDel().add(tabHojas.getTitleAt(tabHojas.getSelectedIndex()));
           // HojaBO hojaBO=this.getDocBO().getMapHojas().get(tabHojas.getSelectedIndex()+1);
             HojaBO hojaBO=this.getDocBO().getMapHojas().get( Integer.parseInt( tabHojas.getTitleAt(tabHojas.getSelectedIndex()))  );
            tabHojas.remove(tabHojas.getSelectedIndex());
            tabHojas.updateUI();
            
          
            List<ModuleBO> lstModules=null;
            if( hojaBO!=null ){
                
                lstModules=hojaBO.getLstModules();
                
                if(lstModules!=null ){
                    for( ModuleBO moduleBO :lstModules  ){
                        this.scrIntMod.removeModule(moduleBO);
                    }
                    
                }
                                                
                
            }
             
            
            
            generarOrdenVisual();
        }
    }
    
   
   /*
       protected HojaBO createLastHoja() {
        int numHojas = tabHojas.getTabCount();
        short nuevaHoja = (short) (numHojas + 1);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int)nuevaHoja, hojaBO);

        JPanel panHoja = new JPanel();
        panHoja.setLayout(new BorderLayout());

        tabHojas.addTab("" + numHojas, panHoja);
        tabHojas.setTitleAt(nuevaHoja - 1, "" + nuevaHoja);

        // showHoja(nuevaHoja);
        tabHojas.setSelectedIndex(numHojas);
        
    
        
        return hojaBO;
       // addModule(moduleId);

    }
   */
    
    
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



    protected int numeroHojaMayor() {
        int retVal = 0;
        int numHojaSig = 0;
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
        return retVal;
    }
    
   
   
   
   /*
   ModuleBO  addModule2(int moduleId, int documentModuleId  ) throws UnsupportedOperationException {
        if (this.getDocBO() == null) {
            throw new UnsupportedOperationException(GlobalDefines.MSG_FALTA_DOCUMENTO_PLANTILLA);
            
        }
        
        DocumentDAO docDAO=null;
        ModuleBO moduleBO;
        if( documentModuleId>0 ){
             docDAO=new DocumentDAO();
             moduleBO=docDAO.getDocModule(documentModuleId); 
             moduleBO.setDocumentModuleId(0);
             resetModule(moduleBO);
            // moduleBO.
                          
        }else{
             TemplateDAO tempDAO = new TemplateDAO();
             moduleBO = tempDAO.getModule(moduleId);
        }
              
        if (moduleBO == null) {
            return null;
        }

        int numHoja = tabHojas.getSelectedIndex() + 1;
        HojaBO hojaBO = this.getDocBO().getMapHojas().get("" + numHoja);
        if (hojaBO == null) {
            return null;
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
        PanelModParent panParent=new PanelModParent( this  );
        contentPanel.add(panParent);
        
        JPanel panModule = addLayout(panParent.getContent()  , moduleBO.getRootSection(), moduleBO);
        
       // panParent.getContent().add(panModule);
        moduleBO.setPanModule(panModule);
        return moduleBO;

    }    
    
    */
       
       
    protected void changeHoja() {
        if(isOpenDoc)
            return;
        
        int numHoja = tabHojas.getSelectedIndex();
        if( numHoja<0 ){
            return;
        }
        numHoja = Integer.parseInt(tabHojas.getTitleAt(numHoja));
        showHoja((short) numHoja);

    }       
       
    protected void addHoja(Boolean nuevo) {
        if (getDocumentId() <= 0 && nuevo == false) {
            return;
        }
        int numHojas = tabHojas.getTabCount();
      //  numHojas = Integer.parseInt(tabHojas.getTitleAt(numHojas));
        short nuevaHoja = (short) (numHojas + 1);
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int)nuevaHoja, hojaBO);

        JPanel panHoja = new JPanel();
        panHoja.setLayout(new BorderLayout());
        // 
        tabHojas.addTab("" + numHojas, panHoja);
        tabHojas.setTitleAt(tabHojas.getTabCount() - 1, "" + nuevaHoja);

        showHoja(nuevaHoja);
        tabHojas.setSelectedIndex(tabHojas.getTabCount() - 1);

    }

    
    
     
   
    @Override
    public void openDoc() throws SQLException {
        if (getDocumentId() <= 0) {
            return;
        }
        
        DocumentDAO docDAO = new DocumentDAO();
        DocumentBO docBO = docDAO.getDocument(getDocumentId(), -1,false);
        
        if (docBO == null) {
            return;
        }        
        
 
        this.setDocBO(docBO);
        int numHojas = docBO.getNumHojas();
        
        if(numHojas<=0){
            
        short nuevaHoja = 1;
        HojaBO hojaBO = new HojaBO();
        hojaBO.setHoja(nuevaHoja);
        this.getDocBO().getMapHojas().put((int)nuevaHoja, hojaBO);
        numHojas=1;    
            
            
         //   return;
        }
        
        tabHojas.setSelectedIndex(-1);
        tabHojas.removeAll();        

        JPanel panHoja = null;
        isOpenDoc = true;
        for (int i = 0; i < numHojas; i++) {
            panHoja = new JPanel();
            panHoja.setLayout(new BorderLayout());
            // 
            // tabHojas.add(panHoja , BorderLayout.CENTER );
            tabHojas.addTab("" + numHojas, panHoja);
            tabHojas.setTitleAt(i, "" + (i + 1));
        }
        isOpenDoc = false;
        this.showHoja((short) 1);

    }
    

    /*
    protected void showHoja(short numHoja) {
         
        if (this.getDocBO() == null) {
            return;
        }
        int numHojas = tabHojas.getTabCount();
        if (numHoja > numHojas) {
            return;
        }
        contentPanel.removeAll();

        if (this.getDocBO().getMapHojas() == null) {
            return;
        }

        HojaBO hojaBO = this.getDocBO().getMapHojas().get((int)numHoja);
        if (hojaBO == null) {
            return;
        }

        List<ModuleBO> lstModules = hojaBO.getLstModules();
        JPanel panTmp = null;

        JPanel panHoja = (JPanel) tabHojas.getComponentAt(numHoja - 1);
        contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        contentPanel.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
        if (panHoja != null) {
            panHoja.removeAll();
            JScrollPane jScroll = new JScrollPane(this.contentPanel);
            // jScroll.setMaximumSize(new Dimension(700,GlobalDefines.MAX_HEIGHT_CELL));
            panHoja.add(jScroll);
        }

        if (lstModules != null) {
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            for (ModuleBO moduleBO : lstModules) {

                
        PanelModParent panParent=new PanelModParent( this , moduleBO  );
        contentPanel.add(panParent);
                
                
                panTmp = addLayout(panParent.getContent(), moduleBO.getRootSection(), moduleBO);
                if (moduleBO.isHasText() == false) {
                    panTmp.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    panTmp.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                }

                moduleBO.setPanModule(panParent);
               // moduleBO.setPanModule(panTmp);
            }
        }

//         tabHojas.add(contentPanel);
        //  showLayout( null , docBO.getRootSectionId() , docBO.getMapSectionsByParent() ,
        //            docBO.getMapSectionsById() ); 
    } */
    
    
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
            //jScroll.setMaximumSize(new Dimension(700,GlobalDefines.MAX_HEIGHT_CELL));
            panHoja.add(jScroll);
        }

        short heightTot = 0;
        if (lstModules != null) {
            contentPanel.setLayout(new GridLayout(lstModules.size(), 1));
            for (ModuleBO moduleBO : lstModules) {
                heightTot += moduleBO.getHeight();
                contentPanel.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));

                PanelModParent panParent=new PanelModParent( this , moduleBO  ); 
                contentPanel.add(panParent);
                panTmp = addLayout(panParent.getContent(), moduleBO.getRootSection(), moduleBO);
                if (moduleBO.isHasText() == false) {
                    panTmp.setPreferredSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                    panTmp.setMaximumSize(new Dimension(700, GlobalDefines.MAX_HEIGHT_CELL));
                }

                moduleBO.setPanModule(panParent);
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
    
    
    
    
    
    
    
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popHojas = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        contentPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
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

        tabHojas.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabHojas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabHojas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabHojasStateChanged(evt);
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
        // TODO add your handling code here:
        deleteHojaADocumentoExistente();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu popHojas;
    private javax.swing.JTabbedPane tabHojas;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    public boolean saveDocumentMod() {
        if (this.getDocBO().getMapHojas() != null && this.getDocBO().getMapHojas().size() > 1) {
           
            for (Map.Entry<Integer, HojaBO> entHoja : this.getDocBO().getMapHojas().entrySet()) {
                if (!entHoja.getValue().getLstModules().isEmpty()) {
                    continue;
                }
                if (getHojasDel() == null) {
                    setHojasDel(new ArrayList<>());
                } 
                getHojasDel().add(tabHojas.getTitleAt(entHoja.getKey() - 1));
                //tabHojas.remove(entHoja.getKey() - 1);
                //tabHojas.updateUI();
            }
        }
        setTabHojas(tabHojas);
        return saveDocument();
    }
    
    
    
    
    
    
    

    
    public List<DocumentCollabItemBO> getLstCollabMod() {
        return lstCollabMod;
    }

    public void setLstCollabMod(List<DocumentCollabItemBO> lstCollabMod) {
        this.lstCollabMod = lstCollabMod;
    }

    
}


