package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.LinkedExcelBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.ExcelDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Desarrollador
 */
public class ScrObjExcel extends javax.swing.JPanel {

    private JFileChooser chooser = new JFileChooser();
    private ObjectInfoBO objInfo;
    private Image imgCurrent;
    private String currentFileImg;
    private NavigableImagePanel navigableImagePanel;
    private BufferedImage image;
    private BufferedImage thumbImage;
    private boolean isCutImage;
    private boolean prepareToCutIamge;
    private LinkedExcelBO categoriaLEBO;
    private LinkedExcelBO nombreLEBO;
    private boolean errorDetectado;
    /**
     * Creates new form ScrObjExcel1
     */
    public ScrObjExcel() {
        initComponents();
        initScreen();
        fillCboIdiomas();
        initComboCategoria();
        //rbAuto.setEnabled(false);
        //rbSelection.setEnabled(false);
        //rbAuto.setVisible(false);
        //rbSelection.setVisible(false);
        btnCut.setEnabled(false);
        //btnCut.setVisible(false);
        categoriaLEBO = null;
        nombreLEBO = null;
        errorDetectado = false;
    }

    private void initScreen() {
        cboIdiomasActionPerformed(null);
    }

    public void initPanImage(BufferedImage image) {
        try {
            navigableImagePanel = new NavigableImagePanel(image);
            navigableImagePanel.setClipSize(0, 0);
            containerPanel.removeAll();
            containerPanel.add(navigableImagePanel, BorderLayout.CENTER);
            containerPanel.revalidate();
            containerPanel.updateUI();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(image);
        }
    }

    public void genExcellPaste(OLEExcel oleExcel) {
        try {
            //No mover
            Transferable transferable = Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getContents(null);
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

            BufferedImage img = (BufferedImage) clip.getContents(null).getTransferData(DataFlavor.imageFlavor);
            this.getObjInfo().setImage(img);

            String fileName = Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.EXCEL);
            this.setCurrentFileImg(fileName);
            this.getObjInfo().setFile(getCurrentFileImg());
            Utilerias.logger(getClass()).info(this.getCurrentFileImg());

            File f = new File(this.getCurrentFileImg());
            if (f.canExecute()) {
                f.delete();
            }
            f.createNewFile();
            ImageIO.write(img, GlobalDefines.png, f);
        } catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
            setErrorDetectado(true);
        }
        refreshFile();
        setIsCutImage(false);
       // oleExcel.dispose();
    }

    public void refreshFile() {
        try {
            if (this.getCurrentFileImg() == null || this.getCurrentFileImg().isEmpty()) {
                return;
            }
            if (this.getImgCurrent() != null) {
                this.getImgCurrent().flush();
            }
            File file = new File(this.getCurrentFileImg());
            displayImage(file);

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
            setErrorDetectado(true);
        }
    }

    private void fillCboIdiomas() {
        LanguageDAO lanDao = new LanguageDAO();
        List<LanguageBO> listLan = lanDao.get(null);
        LanguageBO lanBO = new LanguageBO();
        /*Agregando valores por default al combo*/
        lanBO.setName("Sin corrección");
        lanBO.setIdLanguage(-1);
        getCboIdiomas().addItem(lanBO);
        for (LanguageBO lanBO2 : listLan) {
            if("es".equals(lanBO2.getNomenclature().toLowerCase().trim()) || "en".equals(lanBO2.getNomenclature().toLowerCase().trim()))
                getCboIdiomas().addItem(lanBO2);
        }
        getCboIdiomas().setSelectedIndex(1);
        activarIdioma();
    }

    public void displayImage(File file) {
        try {
            BufferedImage bi = ImageIO.read(file);
            if (bi != null) {
                image = bi;
                displayImage();
            } else {
                image = null;
            }
        } catch (IOException e) {
            Utilerias.logger(getClass()).info(e);
            setErrorDetectado(true);
        }
    }
    
    public void displayImage(BufferedImage bi) {
        if (bi != null) {
            thumbImage = image;
            image = bi;
            displayImage();
        } else {
            image = null;
        }
    }

    public void displayImage() {
        try {
            if (image == null) {
                return;
            }
            //short width = objInfo.getWidth();
            //short height = objInfo.getHeight();

            initPanImage(image);
            //Estos valores se leen del modulo para ajustarlo 
            navigableImagePanel.setImage(image);

           
            rbAuto.setSelected(true);
            setClipper();

            navigableImagePanel.setNavigationImageEnabled(false);
            navigableImagePanel.repaint();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    protected void linkedExcelProcess() {
        String fileName = this.getEdPath().getText();
        String rangoIni = this.getEdRangoIni().getText();
        String rangoFin = this.getEdRangoFin().getText();
        String hoja=this.getEdHoja().getText();
        String rango=this.getEdRango().getText();
        
        if(fileName==null || fileName.isEmpty() ){
            return;
        }

        if (  rangoIni == null || rangoFin == null || rangoIni.isEmpty() || rangoFin.isEmpty()) {
            if( rango==null || rango.isEmpty() ){
               return;
            }
        }

        //1. Create shell
        Shell shell = null;
        OLEExcel oleExcel=null;
        try {
            shell = new Shell();
            shell.setSize(600, 400);
            shell.setText("Excel Window");
            shell.setLayout(new FillLayout());
            shell.setMenuBar(new Menu(shell, SWT.BAR));
           // createPartControl1(shell, fileName, rangoIni, rangoFin , hoja , rango );
            oleExcel=new OLEExcel();
            oleExcel.createPartControl1(shell, fileName, rangoIni, rangoFin , hoja , rango );
            
            shell.open();
            shell.setVisible(false);

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
            setErrorDetectado(true);
        } finally {
            shell.close();
            if(shell != null){
                while (!shell.isDisposed()) { 
                    try { 
                        if(!shell.getDisplay().readAndDispatch()){
                            shell.getDisplay().sleep();
                        }
                    } catch (Exception ex) {
                        Utilerias.logger(getClass()).error(ex);
                    }
                }

                try{
                    shell.getDisplay().dispose();
                    shell.dispose();
                }catch(Exception e){}

                shell = null;
            }
        }
        Utilerias.pasarGarbageCollector();
        //2. Get generated image from Excel
        genExcellPaste(oleExcel);
        if(oleExcel!=null){
            oleExcel.dispose();
        }
        
    }

    public void pasteExcel() {
        linkedExcelProcess();
    }

    public void createPartControl1(Composite parent, String fileName, String rangoIni, String rangoFin , String sheetName , 
String rango ) {

        OleClientSite site = null;
        OleAutomation xls = null;
        OleFrame frame = null;
        OleAutomation sheet = null;
        OleAutomation sheets = null;
        OleAutomation namedRanges = null;
        OleAutomation namedRange=null;
        
        Variant sheetsVariant=null;
        Variant name=null;
        Variant[] arguments_1 =null;
        Variant range=null;
        Variant count=null;
        Variant sheetVariant=null ;        
        Variant intVariant=null;
         
        try {
            frame = new OleFrame(parent, SWT.NONE);
            //   File f = new File("c:\\jmpa\\test_macro.xlsm");
            File f = new File(fileName);
            site = new OleClientSite(frame, SWT.NONE, f);
            
           // site.dispose();
             
            
            xls = new OleAutomation(site);

            int[] ids = xls.getIDsOfNames(new String[]{"ActiveSheet"});
            //int[] ids = xls.getIDsOfNames(new String[]{"Sheets(SALIDA)"});

            //OleAutomation sheet;
            Utilerias.logger(getClass()).info(ids[0]);
            Variant sheetVar = xls.getProperty(ids[0]);
            
            
         //    sheetVar.dispose();
         //   xls.dispose();
           
         
       //     site.dispose();
            
            sheet=null;
            if(sheetName==null || sheetName.isEmpty()){  
                if( sheet!=null ){
                    sheet.dispose();
                }
                sheet=sheetVar.getAutomation();            
            }else{
                
                //**
                
              


                int[] workbookIds = xls.getIDsOfNames (new String[] {"Worksheets"}); 

                sheetsVariant = xls.getProperty(workbookIds[0]);    
                if(sheets!=null){
                    sheets.dispose();
                }
                sheets = sheetsVariant.getAutomation (); 

                int[] sheetsIds = sheets.getIDsOfNames(new String[] {"Item"});   
                
                /*
                sheets.dispose();
                sheetsVariant.dispose();
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();       */            
                

                
             
                

                

                 intVariant = new Variant(1); // but can be 2 or 3 ... 
               // Variant[] itemArgs = new Variant[] {intVariant}; 
                  Variant[] itemArgs = null; 
                // get the sheet ..................... :( 
                sheetVariant=null ; // if(sheetVariant == null)   
                
                /*
                intVariant.dispose();
               sheets.dispose();
                sheetsVariant.dispose();
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();     */               
                

                sheetsIds = sheets.getIDsOfNames(new String[] {"Count"}); 
                count=sheets.getProperty(sheetsIds[0]);
                
                int[] sheetsIdsMain = sheets.getIDsOfNames(new String[] {"Item"}); 

                boolean encontro=false;
                
               
                
                
                
                for( int i=1;i<= count.getInt();i++ ){
                   if(intVariant!=null) {
                       intVariant.dispose();
                   }
                   intVariant = new Variant(i); // but can be 2 or 3 ... 
                   itemArgs = new Variant[] {intVariant};   
                   
                   for(Variant var : itemArgs){
                       var.dispose();
                   }
                   
                       count.dispose();
   intVariant.dispose();
   
   count=null;
   intVariant=null;
   
   /*
               sheets.dispose();
                sheetsVariant.dispose();
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();   */
                   
                   
                   
                   if(sheetVariant!=null){
                       sheetVariant.dispose();
                   }
                   
                   sheetVariant = sheets.getProperty(sheetsIdsMain[0], itemArgs); 
                if( sheet!=null ){
                    sheet.dispose();
                }                   
                   sheet=sheetVariant.getAutomation();

                    sheetsIds = sheet.getIDsOfNames(new String[] {"name"});  
                    if(name!=null ){
                        name.dispose();
                        name=null;
                    }
                    name=sheet.getProperty(sheetsIds[0]);

                    if(  name.getString().equals(sheetName) ){
                        encontro=true;
                      //  break;
                    }
                    
                     if( itemArgs!=null ){
                         for( Variant var : itemArgs ){
                             var.dispose();
                         }
                     }
                    
                     if(name!=null){
                         name.dispose();
                         name=null;
                     }
                     
                     /*
                     if(sheet!=null){
                         sheet.dispose();
                         sheet=null;
                     } */
                    
                    if(intVariant!=null) {
                       intVariant.dispose();
                      intVariant=null;
                    }
                    
                    if(itemArgs!=null ){
                        for( Variant var :itemArgs ){
                            var.dispose();
                        }
                    }
                    
                    
                                                sheets.dispose();
                                                sheets=null;
                                  
                   /*                             
                sheetsVariant.dispose();
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();  
                    */
                 if(sheetVariant!=null){
                    sheetVariant.dispose();
                    sheetVariant=null;    
                 }   
                    
                                       
                    
       /*
                            sheets.dispose();
                sheetsVariant.dispose();
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();  */
                    if(encontro==true){
                        break;
                    }
                    
                   

                }

                if(encontro==false){
                   Utilerias.logger(getClass()).info("No se encontro el sheet con nombre :"+ sheetName );
                   return;
                }




                //**
                
                
                
            }
            
           // site.dispose();
            
            //****inivio pruebas range
            
                    //       sheets.dispose();
              //  sheet.dispose();
                sheetsVariant.dispose();
                sheetsVariant=null;
                
                /*
                sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();         */    
            
            
            
            if( rango!=null && !rango.trim().isEmpty() ){
            
                    int namesId=sheet.getIDsOfNames(new String[]{"Names"})[0];

                    Variant[] arguments_4 = new Variant[1];
                    arguments_4[0] = new Variant(rango);

                    Variant names=sheet.getProperty(namesId);

                    namedRanges =  names.getAutomation ();

                    int nameItemId=   namedRanges.getIDsOfNames(new String[]{"Item"})[0]; 

                    Variant rangeItems= namedRanges.invoke(nameItemId,arguments_4 );

                    //OleAutomation namedRange=null;
                    if( rangeItems==null ){                    
                         
                        //Lo buscamos en ambito global
                        int[] workbookIds = xls.getIDsOfNames (new String[] {"Names"}); 
                        names=xls.getProperty(namesId);                        
                        namedRanges =  names.getAutomation ();
                        rangeItems= namedRanges.invoke(nameItemId,arguments_4 );                        
                        System.out.println(workbookIds);
                        
                    }
 
                    if( rangeItems==null ){
                        return;
                    }
                    namedRange=rangeItems.getAutomation();  

                    int refersToRangeId=namedRange.getIDsOfNames(new String[]{"RefersToRange"})[0];
                    Variant refersToRange = namedRange.getProperty(refersToRangeId);

                    int[] copyId1 = refersToRange.getAutomation().getIDsOfNames(new String[]{"Copy"});
                    refersToRange.getAutomation().invoke(copyId1[0]);
                    
                    
                    
                    
                    
                    return;

            
            }
            
            /*
            sheet.dispose();
                           sheetVar.dispose();
                 xls.dispose();                 
                 site.dispose();   
            */
            //*** fin pruebas range

            // Den Range erstellen
            int rangeId = sheet.getIDsOfNames(new String[]{"Range"})[0];
            Utilerias.logger(getClass()).info("RANGE ID =" + rangeId);
            arguments_1 = new Variant[2];
            arguments_1[0] = new Variant(rangoIni);
            arguments_1[1] = new Variant(rangoFin);
            
          
            
            
            
           range = sheet.getProperty(rangeId, arguments_1);
            
        
   
 OleAutomation varSel=range.getAutomation();
            int[] selectId = varSel.getIDsOfNames(new String[]{"Select"});
           
            varSel.invoke(selectId[0]);
            
            /*
            varSel.dispose();
            range.dispose();
            
            arguments_1[0].dispose();
            arguments_1[1].dispose();
                       sheet.dispose();
                      // sheet=null;
                           sheetVar.dispose();
                       //    sheetVar=null;
                 xls.dispose();              
              //   xls=null;
                 site.dispose();                   
    */
            
            int[] copyId = varSel.getIDsOfNames(new String[]{"Copy"});
            varSel.invoke(copyId[0]);
            
            
         //  range.getAutomation().dispose(); 
            for(int i=0;i<=100000;i++ ){
                System.out.println("");
                
            }
            
            varSel.dispose();
            range.dispose();
            range=null;                        
           arguments_1[0].dispose();
            arguments_1[1].dispose();            
            arguments_1=null;                                    
                       sheet.dispose();
                       sheet=null;
                           sheetVar.dispose();
                           sheetVar=null;
                 xls.dispose();              
                 xls=null;
                 site.dispose();  
                 site=null;
            
            
            
        } catch (SWTError | Exception e) {
            Utilerias.logger(getClass()).info("Unable to open activeX control");
        } finally {
            
            
                    
            try{
                if (intVariant != null) {
                    intVariant.dispose();
                }
            }catch(Exception ex){}                      
            
            
                    
            try{
                if (sheetVariant != null) {
                    sheetVariant.dispose();
                }
            }catch(Exception ex){}                    
            
            try{
                if (sheetsVariant != null) {
                    sheetsVariant.dispose();
                }
            }catch(Exception ex){}
            
            



            
           try{
                if (count != null) {
                    count.dispose();
                }
            }catch(Exception ex){}        
            
           try{
                if (name != null) {
                    name.dispose();
                }
            }catch(Exception ex){}         
           
           /*
           try{
                if (arguments_1 != null) {
                    arguments_1.dispose();
                }
            }catch(Exception ex){}    */    
           
           try{
                if (range != null) {
                    range.dispose();
                }
            }catch(Exception ex){}              
            
            
            try{
                if (namedRanges != null) {
                    namedRanges.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (namedRange != null) {
                    
                    namedRange.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (sheet != null) {
                    sheet.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (sheets != null) {
                    
                    sheets.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (xls != null) {
                    
                    xls.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (site != null) {
                    
                    site.dispose();
                }
            }catch(Exception ex){}
            
            try{
                if (frame != null) {
                    
                    frame.dispose();
                }
            }catch(Exception ex){}

            site = null;
            xls = null;
            frame = null;
            sheet = null;
            sheets = null;
            namedRanges = null;
            namedRange=null;
        }
    }

    void disposeClient() {

    }

    public void selectFile() {
        int state = getChooser().showOpenDialog(null);
        File file = getChooser().getSelectedFile();
        if (file != null && state == JFileChooser.APPROVE_OPTION) {
            getEdPath().setText(file.getAbsolutePath());
        }
    }
    
    private void activarIdioma(){
        int i = getCboIdiomas().getSelectedIndex();
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
                    SpellChecker.unregister(getEdTitulo());
                    SpellChecker.unregister(getEdSubtitulo());
                    SpellChecker.unregister(getEdDescripcion());
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).error(ex);
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

            SpellChecker.register(getEdTitulo());
            SpellChecker.register(getEdSubtitulo());
            SpellChecker.register(getEdDescripcion());

            SpellCheckerOptions sco = new SpellCheckerOptions();
            sco.setCaseSensitive(true);
            sco.setSuggestionsLimitMenu(10);
            sco.setLanguageDisableVisible(false);
            sco.setIgnoreWordsWithNumbers(true);
            JPopupMenu popup = SpellChecker.createCheckerPopup(sco);

            getEdTitulo().addMouseListener(new PopupListener(popup));
            getEdSubtitulo().addMouseListener(new PopupListener(popup));
            getEdDescripcion().addMouseListener(new PopupListener(popup));

        } catch (NullPointerException | MalformedURLException ex) {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        topPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cboName = new javax.swing.JComboBox();
        rbAuto = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox();
        edPath = new javax.swing.JTextField();
        rbSelection = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        edDescripcion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cboIdiomas = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        edRangoIni = new javax.swing.JTextField();
        edTitulo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        edRango = new javax.swing.JTextField();
        edRangoFin = new javax.swing.JTextField();
        edHoja = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        edSubtitulo = new javax.swing.JTextField();
        mainPanelImage = new javax.swing.JPanel();
        cutPanel = new javax.swing.JPanel();
        btnCut = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        btnZoomIn = new javax.swing.JButton();
        containerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        topPanel.setPreferredSize(new java.awt.Dimension(352, 275));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cboName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNameItemStateChanged(evt);
            }
        });
        jPanel1.add(cboName, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 31, 202, -1));

        buttonGroup1.add(rbAuto);
        rbAuto.setSelected(true);
        rbAuto.setText("Autoajustable");
        rbAuto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbAutoItemStateChanged(evt);
            }
        });
        jPanel1.add(rbAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 238, -1, -1));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Corrector ortográfico");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 219, -1, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Ruta");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 16));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 57, 25, 20));

        cboCategoria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCategoriaItemStateChanged(evt);
            }
        });
        jPanel1.add(cboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 11, 202, -1));

        edPath.setEditable(false);
        edPath.setMinimumSize(new java.awt.Dimension(120, 22));
        edPath.setPreferredSize(new java.awt.Dimension(110, 22));
        jPanel1.add(edPath, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 57, 202, -1));

        buttonGroup1.add(rbSelection);
        rbSelection.setText("Cortar selección");
        rbSelection.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbSelectionItemStateChanged(evt);
            }
        });
        jPanel1.add(rbSelection, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 238, -1, -1));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Rango");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 85, -1, 20));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Nombre");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 33, 100, 17));
        jPanel1.add(edDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 190, 202, -1));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Fuente*");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 193, 101, -1));

        cboIdiomas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIdiomasActionPerformed(evt);
            }
        });
        jPanel1.add(cboIdiomas, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 216, 202, -1));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Título*");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 140, 101, -1));

        edRangoIni.setEditable(false);
        jPanel1.add(edRangoIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 85, 50, -1));
        jPanel1.add(edTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 137, 200, -1));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Categoría");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 11, 102, 20));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Celda final");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 114, 102, -1));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Subtítulo");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 167, 101, -1));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Hoja");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 111, -1, 20));

        edRango.setEditable(false);
        jPanel1.add(edRango, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 85, 45, -1));

        edRangoFin.setEditable(false);
        jPanel1.add(edRangoFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 111, 50, -1));

        edHoja.setEditable(false);
        jPanel1.add(edHoja, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 111, 45, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Celda inicial");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 88, 102, -1));
        jPanel1.add(edSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 164, 200, -1));

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(227, Short.MAX_VALUE))
        );

        add(topPanel, java.awt.BorderLayout.WEST);

        mainPanelImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanelImage.setLayout(new java.awt.BorderLayout());

        btnCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cut.png"))); // NOI18N
        btnCut.setEnabled(false);
        btnCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCutActionPerformed(evt);
            }
        });

        btnZoomOut.setText("|« »|");
        btnZoomOut.setEnabled(false);
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        btnZoomIn.setText("»| |«");
        btnZoomIn.setEnabled(false);
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cutPanelLayout = new javax.swing.GroupLayout(cutPanel);
        cutPanel.setLayout(cutPanelLayout);
        cutPanelLayout.setHorizontalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cutPanelLayout.createSequentialGroup()
                .addContainerGap(463, Short.MAX_VALUE)
                .addComponent(btnCut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnZoomOut)
                .addGap(2, 2, 2)
                .addComponent(btnZoomIn))
        );
        cutPanelLayout.setVerticalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cutPanelLayout.createSequentialGroup()
                .addGroup(cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnZoomOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnZoomIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mainPanelImage.add(cutPanel, java.awt.BorderLayout.NORTH);

        containerPanel.setLayout(new java.awt.BorderLayout());
        mainPanelImage.add(containerPanel, java.awt.BorderLayout.CENTER);

        add(mainPanelImage, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cboIdiomasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIdiomasActionPerformed
        activarIdioma();
    }//GEN-LAST:event_cboIdiomasActionPerformed

    private void cboCategoriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCategoriaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            getNombreExcel();
        }
    }//GEN-LAST:event_cboCategoriaItemStateChanged

    private void cboNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNameItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            getLinkedExcelInfo();
        }
    }//GEN-LAST:event_cboNameItemStateChanged

    private void btnCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCutActionPerformed
        try{    
            final BufferedImage bi = ImageClipperNavigablePanel.getInstance().getCutImage();
            
            if(bi==null){
                return;
            }

            containerPanel.removeAll();
            containerPanel.revalidate();
            containerPanel.updateUI();
            mainPanelImage.revalidate();
            mainPanelImage.updateUI();
            /*getContainerPanel().removeAll();
            JPanel p = new JPanel() {

                @Override
                public void paint(Graphics g) {
                    // POSITION OF THE PICTURE

                    int parentWidth = getMainPanelImage().getWidth();
                    int parentHeight = getMainPanelImage().getHeight();

                    int imageWidth = bi.getWidth();
                    int imageHeight = bi.getHeight();

                    int a1 = parentWidth / 2;
                    int a2 = imageWidth / 2;

                    int b1 = parentHeight / 2;
                    int b2 = imageHeight / 2;

                    g.drawImage(bi, a1 - a2, b1 - b2, imageWidth, imageHeight, null);
                }
            };
            p.setDoubleBuffered(true);
            getContainerPanel().add(p, BorderLayout.CENTER);
            getContainerPanel().revalidate();
            getContainerPanel().repaint();*/

            displayImage(bi);
    //        try{
    //            File f = new File(getCurrentFileImg());
    //            if (f.canExecute()) {
    //                f.delete();
    //            }
    //            f.createNewFile();
    //            ImageIO.write(bi, "png", f);
    //        } catch (HeadlessException | IOException ex) {
    //            Utilerias.logger(Utilerias.class).error(ex);
    //        }
            setIsCutImage(true);
            rbAuto.setSelected(true);
        }catch(IllegalArgumentException ex){
            Utilerias.logger(getClass()).error(ex);
            JOptionPane.showMessageDialog(null, "No se pudo generar el corte de la imagen, asegurese que el margen \n"
                                              + "de corte en color rojo este dentro de los bordes de la imagen.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
            JOptionPane.showMessageDialog(null, "No se pudo generar el corte de la imagen, asegurese que el margen \n"
                                              + "de corte en color rojo este dentro de los bordes de la imagen.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnCutActionPerformed

    private void rbAutoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbAutoItemStateChanged
        if (rbAuto.isSelected() == true) {
            btnCut.setEnabled(false);
            btnZoomIn.setEnabled(false);
            btnZoomOut.setEnabled(false);
            prepareToCutIamge =  false;
            setClipper();
        } else {
            btnCut.setEnabled(true);
            btnZoomIn.setEnabled(true);
            btnZoomOut.setEnabled(true);
            prepareToCutIamge =  true;
            setClipper();
        }

    }//GEN-LAST:event_rbAutoItemStateChanged

    private void rbSelectionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbSelectionItemStateChanged
        if (rbAuto.isSelected() == true) {
            btnCut.setEnabled(false);
            btnZoomIn.setEnabled(false);
            btnZoomOut.setEnabled(false);
            prepareToCutIamge =  false;
            setClipper();
        } else {
            btnCut.setEnabled(true);
            btnZoomIn.setEnabled(true);
            btnZoomOut.setEnabled(true);
            prepareToCutIamge =  true;
            setClipper();
        }
        
    }//GEN-LAST:event_rbSelectionItemStateChanged

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        // TODO add your handling code here:
        ImageClipperNavigablePanel.getInstance().zoomInOut(true);
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        // TODO add your handling code here:
        ImageClipperNavigablePanel.getInstance().zoomInOut(false);
    }//GEN-LAST:event_btnZoomInActionPerformed

    protected void setClipper() {
        //short width = objInfo.getWidth();
        //short height = objInfo.getHeight();

        if (rbSelection.isSelected() == true) {
            final short panelContentWidth = (short) navigableImagePanel.getImage().getWidth();//500;
            final short panelContentHeight = (short) navigableImagePanel.getImage().getHeight();//560;
            short clipW = objInfo.getWidth();
            short clipH = objInfo.getHeight();
            if(objInfo.getWidth() >= panelContentWidth){
                double dif = (objInfo.getWidth() - panelContentWidth);
                double por = (dif*100/objInfo.getWidth());
                double porH = (objInfo.getHeight()*(por/100));
                clipH = (short)(objInfo.getHeight()-porH);
                clipW = panelContentWidth;
            }
            if(objInfo.getHeight() >= panelContentHeight){
                double dif = (objInfo.getHeight() - panelContentHeight);
                double por = (dif*100/objInfo.getHeight());
                double porW = (objInfo.getWidth()*(por/100));
                clipW = (short)(objInfo.getWidth()-porW);
                clipH = panelContentHeight;
            }

            double scale = navigableImagePanel.getScale();
            if(scale > 1)
                scale = 1;
            short width = (short)(clipW * scale);
            short height = (short)(clipH * scale);

            navigableImagePanel.setSizeClipByScale(width, height);
            //navigableImagePanel.setClipSize(100, 100);
        }else{
            navigableImagePanel.setClipSize(0, 0);
        }
    }

    public void previewImage() {
        if (getEdPath().getText() == null || getEdPath().getText().isEmpty() == true) {
            Utilerias.showMessage(MainApp.getApplication().getMainFrame(), "Campo archivo de excel obligatorio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        pasteExcel();
    }

    private void initComboCategoria() {
        try {
            getCboCategoria().removeAllItems();
            ExcelDAO excelDAO = new ExcelDAO();
            List<LinkedExcelBO> list = excelDAO.getAllCtegories();
            LinkedExcelBO emptyBO = new LinkedExcelBO();
            emptyBO.setCategory(GlobalDefines.COMBOBOX_EMPTY_MESSAGE);
            emptyBO.display(LinkedExcelBO.DISPLAY_CATEGORY);
            getCboCategoria().addItem(emptyBO);
            for (LinkedExcelBO o : list) {
                o.display(LinkedExcelBO.DISPLAY_CATEGORY);
                getCboCategoria().addItem(o);
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public void getNombreExcel() {
        Object obj = getCboCategoria().getSelectedItem();
        if (obj == null || String.valueOf(obj).equals(GlobalDefines.COMBOBOX_EMPTY_MESSAGE)) {
            return;
        }
        getCboName().removeAllItems();
        LinkedExcelBO linkedExcelBO = (LinkedExcelBO) obj;
        categoriaLEBO = linkedExcelBO;
        int id_category = linkedExcelBO.getId_category();
        ExcelDAO excelDAO = new ExcelDAO();
        List<LinkedExcelBO> list = excelDAO.getFromCtegory(id_category);

        for (LinkedExcelBO o : list) {
            o.display(LinkedExcelBO.DISPLAY_NAME);
            getCboName().addItem(o);
        }
    }

    public void getLinkedExcelInfo() {
        LinkedExcelBO selectedItem = (LinkedExcelBO) getCboName().getSelectedItem();
        if (selectedItem == null || String.valueOf(selectedItem).equals(GlobalDefines.COMBOBOX_EMPTY_MESSAGE)) {
            return;
        }

        nombreLEBO = selectedItem;
        getEdPath().setText(selectedItem.getPath());
        getEdRangoIni().setText(selectedItem.getXY1());
        getEdRangoFin().setText(selectedItem.getXY2());
        getEdHoja().setText(selectedItem.getSheet());
        getEdRango().setText(selectedItem.getRange());
        rbAuto.setSelected(true);
    }

    public Boolean documentoVacio() {
        if (getEdPath().getText() == null || getEdPath().getText().isEmpty() == true
                || getEdRangoIni().getText() == null || getEdRangoIni().getText().isEmpty() == true
                || getEdRangoFin().getText() == null || getEdRangoFin().getText().isEmpty() == true) {
            return true;
        } else {
            return false;
        }
    }

    public void cargarNombreCategoria(int idLinkedExcel){
        ExcelDAO excelDAO = new ExcelDAO();
        LinkedExcelBO nombreLEBOtmp = excelDAO.getLinkedExcelBO(idLinkedExcel);
        
        for(int i = 0; i < getCboCategoria().getItemCount(); i++){
            LinkedExcelBO lebo = (LinkedExcelBO) getCboCategoria().getItemAt(i);
            if(lebo.getId_category() == nombreLEBOtmp.getId_category()){
                categoriaLEBO = lebo;
                getCboCategoria().setSelectedItem(lebo);
                getNombreExcel();
                break;
            }
        }
        
        for(int i = 0; i < getCboName().getItemCount(); i++){
            LinkedExcelBO lebo = (LinkedExcelBO) getCboName().getItemAt(i);
            if(lebo.getIdLinkedExcel() == nombreLEBOtmp.getIdLinkedExcel()){
                getCboName().setSelectedItem(lebo);
                getLinkedExcelInfo();
                break;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCut;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboCategoria;
    private javax.swing.JComboBox cboIdiomas;
    private javax.swing.JComboBox cboName;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JPanel cutPanel;
    private javax.swing.JTextField edDescripcion;
    private javax.swing.JTextField edHoja;
    private javax.swing.JTextField edPath;
    private javax.swing.JTextField edRango;
    private javax.swing.JTextField edRangoFin;
    private javax.swing.JTextField edRangoIni;
    private javax.swing.JTextField edSubtitulo;
    private javax.swing.JTextField edTitulo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel mainPanelImage;
    private javax.swing.JRadioButton rbAuto;
    private javax.swing.JRadioButton rbSelection;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the chooser
     */
    public JFileChooser getChooser() {
        return chooser;
    }

    /**
     * @return the objInfo
     */
    public ObjectInfoBO getObjInfo() {
        return objInfo;
    }

    /**
     * @param objInfo the objInfo to set
     */
    public void setObjInfo(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
        rbAuto.setSelected(true);
    }

    /**
     * @return the imgCurrent
     */
    public Image getImgCurrent() {
        return imgCurrent;
    }

    /**
     * @param imgCurrent the imgCurrent to set
     */
    public void setImgCurrent(Image imgCurrent) {
        this.imgCurrent = imgCurrent;
    }

    /**
     * @return the currentFileImg
     */
    public String getCurrentFileImg() {
        return currentFileImg;
    }

    /**
     * @param currentFileImg the currentFileImg to set
     */
    public void setCurrentFileImg(String currentFileImg) {
        this.currentFileImg = currentFileImg;
    }

    /**
     * @return the navigableImagePanel
     */
    public NavigableImagePanel getNavigableImagePanel() {
        return navigableImagePanel;
    }

    /**
     * @param navigableImagePanel the navigableImagePanel to set
     */
    public void setNavigableImagePanel(NavigableImagePanel navigableImagePanel) {
        this.navigableImagePanel = navigableImagePanel;
    }

    /**
     * @param chooser the chooser to set
     */
    public void setChooser(JFileChooser chooser) {
        this.chooser = chooser;
    }

    /**
     * @return the btnCut
     */
    public javax.swing.JButton getBtnCut() {
        return btnCut;
    }

    /**
     * @param btnCut the btnCut to set
     */
    public void setBtnCut(javax.swing.JButton btnCut) {
        this.btnCut = btnCut;
    }

    /**
     * @return the buttonGroup1
     */
    public javax.swing.ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    /**
     * @param buttonGroup1 the buttonGroup1 to set
     */
    public void setButtonGroup1(javax.swing.ButtonGroup buttonGroup1) {
        this.buttonGroup1 = buttonGroup1;
    }

    /**
     * @return the cboCategoria
     */
    public javax.swing.JComboBox getCboCategoria() {
        return cboCategoria;
    }

    /**
     * @param cboCategoria the cboCategoria to set
     */
    public void setCboCategoria(javax.swing.JComboBox cboCategoria) {
        this.cboCategoria = cboCategoria;
    }

    /**
     * @return the cboIdiomas
     */
    public javax.swing.JComboBox getCboIdiomas() {
        return cboIdiomas;
    }

    /**
     * @param cboIdiomas the cboIdiomas to set
     */
    public void setCboIdiomas(javax.swing.JComboBox cboIdiomas) {
        this.cboIdiomas = cboIdiomas;
    }

    /**
     * @return the cboName
     */
    public javax.swing.JComboBox getCboName() {
        return cboName;
    }

    /**
     * @param cboName the cboName to set
     */
    public void setCboName(javax.swing.JComboBox cboName) {
        this.cboName = cboName;
    }

    /**
     * @return the containerPanel
     */
    public javax.swing.JPanel getContainerPanel() {
        return containerPanel;
    }

    /**
     * @param containerPanel the containerPanel to set
     */
    public void setContainerPanel(javax.swing.JPanel containerPanel) {
        this.containerPanel = containerPanel;
    }

    /**
     * @return the cutPanel
     */
    public javax.swing.JPanel getCutPanel() {
        return cutPanel;
    }

    /**
     * @param cutPanel the cutPanel to set
     */
    public void setCutPanel(javax.swing.JPanel cutPanel) {
        this.cutPanel = cutPanel;
    }

    /**
     * @return the edDescripcion
     */
    public javax.swing.JTextField getEdDescripcion() {
        return edDescripcion;
    }

    /**
     * @param edDescripcion the edDescripcion to set
     */
    public void setEdDescripcion(javax.swing.JTextField edDescripcion) {
        this.edDescripcion = edDescripcion;
    }

    /**
     * @return the edHoja
     */
    public javax.swing.JTextField getEdHoja() {
        return edHoja;
    }

    /**
     * @param edHoja the edHoja to set
     */
    public void setEdHoja(javax.swing.JTextField edHoja) {
        this.edHoja = edHoja;
    }

    /**
     * @return the edPath
     */
    public javax.swing.JTextField getEdPath() {
        return edPath;
    }

    /**
     * @param edPath the edPath to set
     */
    public void setEdPath(javax.swing.JTextField edPath) {
        this.edPath = edPath;
    }

    /**
     * @return the edRango
     */
    public javax.swing.JTextField getEdRango() {
        return edRango;
    }

    /**
     * @param edRango the edRango to set
     */
    public void setEdRango(javax.swing.JTextField edRango) {
        this.edRango = edRango;
    }

    /**
     * @return the edRangoFin
     */
    public javax.swing.JTextField getEdRangoFin() {
        return edRangoFin;
    }

    /**
     * @param edRangoFin the edRangoFin to set
     */
    public void setEdRangoFin(javax.swing.JTextField edRangoFin) {
        this.edRangoFin = edRangoFin;
    }

    /**
     * @return the edRangoIni
     */
    public javax.swing.JTextField getEdRangoIni() {
        return edRangoIni;
    }

    /**
     * @param edRangoIni the edRangoIni to set
     */
    public void setEdRangoIni(javax.swing.JTextField edRangoIni) {
        this.edRangoIni = edRangoIni;
    }

    /**
     * @return the edSubtitulo
     */
    public javax.swing.JTextField getEdSubtitulo() {
        return edSubtitulo;
    }

    /**
     * @param edSubtitulo the edSubtitulo to set
     */
    public void setEdSubtitulo(javax.swing.JTextField edSubtitulo) {
        this.edSubtitulo = edSubtitulo;
    }

    /**
     * @return the edTitulo
     */
    public javax.swing.JTextField getEdTitulo() {
        return edTitulo;
    }

    /**
     * @param edTitulo the edTitulo to set
     */
    public void setEdTitulo(javax.swing.JTextField edTitulo) {
        this.edTitulo = edTitulo;
    }

    /**
     * @return the jLabel10
     */
    public javax.swing.JLabel getjLabel10() {
        return jLabel10;
    }

    /**
     * @param jLabel10 the jLabel10 to set
     */
    public void setjLabel10(javax.swing.JLabel jLabel10) {
        this.jLabel10 = jLabel10;
    }

    /**
     * @return the jLabel11
     */
    public javax.swing.JLabel getjLabel11() {
        return jLabel11;
    }

    /**
     * @param jLabel11 the jLabel11 to set
     */
    public void setjLabel11(javax.swing.JLabel jLabel11) {
        this.jLabel11 = jLabel11;
    }

    /**
     * @return the jLabel12
     */
    public javax.swing.JLabel getjLabel12() {
        return jLabel12;
    }

    /**
     * @param jLabel12 the jLabel12 to set
     */
    public void setjLabel12(javax.swing.JLabel jLabel12) {
        this.jLabel12 = jLabel12;
    }

    /**
     * @return the jLabel2
     */
    public javax.swing.JLabel getjLabel2() {
        return jLabel2;
    }

    /**
     * @param jLabel2 the jLabel2 to set
     */
    public void setjLabel2(javax.swing.JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    /**
     * @return the jLabel3
     */
    public javax.swing.JLabel getjLabel3() {
        return jLabel3;
    }

    /**
     * @param jLabel3 the jLabel3 to set
     */
    public void setjLabel3(javax.swing.JLabel jLabel3) {
        this.jLabel3 = jLabel3;
    }

    /**
     * @return the jLabel4
     */
    public javax.swing.JLabel getjLabel4() {
        return jLabel4;
    }

    /**
     * @param jLabel4 the jLabel4 to set
     */
    public void setjLabel4(javax.swing.JLabel jLabel4) {
        this.jLabel4 = jLabel4;
    }

    /**
     * @return the jLabel5
     */
    public javax.swing.JLabel getjLabel5() {
        return jLabel5;
    }

    /**
     * @param jLabel5 the jLabel5 to set
     */
    public void setjLabel5(javax.swing.JLabel jLabel5) {
        this.jLabel5 = jLabel5;
    }

    /**
     * @return the jLabel6
     */
    public javax.swing.JLabel getjLabel6() {
        return jLabel6;
    }

    /**
     * @param jLabel6 the jLabel6 to set
     */
    public void setjLabel6(javax.swing.JLabel jLabel6) {
        this.jLabel6 = jLabel6;
    }

    /**
     * @return the jLabel7
     */
    public javax.swing.JLabel getjLabel7() {
        return jLabel7;
    }

    /**
     * @param jLabel7 the jLabel7 to set
     */
    public void setjLabel7(javax.swing.JLabel jLabel7) {
        this.jLabel7 = jLabel7;
    }

    /**
     * @return the jLabel8
     */
    public javax.swing.JLabel getjLabel8() {
        return jLabel8;
    }

    /**
     * @param jLabel8 the jLabel8 to set
     */
    public void setjLabel8(javax.swing.JLabel jLabel8) {
        this.jLabel8 = jLabel8;
    }

    /**
     * @return the jLabel9
     */
    public javax.swing.JLabel getjLabel9() {
        return jLabel9;
    }

    /**
     * @param jLabel9 the jLabel9 to set
     */
    public void setjLabel9(javax.swing.JLabel jLabel9) {
        this.jLabel9 = jLabel9;
    }

    /**
     * @return the jPanel2
     */
    public javax.swing.JPanel getjPanel2() {
        return jPanel2;
    }

    /**
     * @param jPanel2 the jPanel2 to set
     */
    public void setjPanel2(javax.swing.JPanel jPanel2) {
        this.jPanel2 = jPanel2;
    }

    /**
     * @return the jRadioButton1
     */
    public javax.swing.JRadioButton getjRadioButton1() {
        return rbAuto;
    }

    /**
     * @param jRadioButton1 the jRadioButton1 to set
     */
    public void setjRadioButton1(javax.swing.JRadioButton jRadioButton1) {
        this.rbAuto = jRadioButton1;
    }

    /**
     * @return the jRadioButton2
     */
    public javax.swing.JRadioButton getjRadioButton2() {
        return rbSelection;
    }

    /**
     * @param jRadioButton2 the jRadioButton2 to set
     */
    public void setjRadioButton2(javax.swing.JRadioButton jRadioButton2) {
        this.rbSelection = jRadioButton2;
    }

    /**
     * @return the mainPanelImage
     */
    public javax.swing.JPanel getMainPanelImage() {
        return mainPanelImage;
    }

    /**
     * @param mainPanelImage the mainPanelImage to set
     */
    public void setMainPanelImage(javax.swing.JPanel mainPanelImage) {
        this.mainPanelImage = mainPanelImage;
    }

    /**
     * @return the topPanel
     */
    public javax.swing.JPanel getTopPanel() {
        return topPanel;
    }

    /**
     * @param topPanel the topPanel to set
     */
    public void setTopPanel(javax.swing.JPanel topPanel) {
        this.topPanel = topPanel;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the thumbImage
     */
    public BufferedImage getThumbImage() {
        return thumbImage;
    }

    /**
     * @param thumbImage the thumbImage to set
     */
    public void setThumbImage(BufferedImage thumbImage) {
        this.thumbImage = thumbImage;
    }

    public boolean isCutImage() {
        return isCutImage;
    }

    public void setIsCutImage(boolean isCutImage) {
        this.isCutImage = isCutImage;
    }
    
    public boolean isPrepareToCutIamge() {
        return prepareToCutIamge;
    }

    public LinkedExcelBO getCategoriaLEBO() {
        return categoriaLEBO;
    }

    public void setCategoriaLEBO(LinkedExcelBO categoriaLEBO) {
        this.categoriaLEBO = categoriaLEBO;
    }

    public LinkedExcelBO getNombreLEBO() {
        return nombreLEBO;
    }

    public void setNombreLEBO(LinkedExcelBO nombreLEBO) {
        this.nombreLEBO = nombreLEBO;
    }

    public boolean isErrorDetectado() {
        return errorDetectado;
    }

    public void setErrorDetectado(boolean errorDetectado) {
        this.errorDetectado = errorDetectado;
    }
    
}
