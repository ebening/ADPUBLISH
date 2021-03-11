/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view;



import com.adinfi.formateador.util.Utilerias;
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
public class OLEExcel {
    
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
         Variant sheetVar =null;
         OleAutomation varSel=null;
    
    
    
    public void dispose(){
        if(varSel!=null){
            varSel.dispose();
            varSel=null;
        }
        
        if(range!=null){
            range.dispose();
             range=null;        
        }    
                           
        if(arguments_1!=null){
            for(Variant var :arguments_1 ){
                var.dispose();
            }
          // arguments_1[0].dispose();
           // arguments_1[1].dispose();            
            arguments_1=null;                
        }
        
        if(sheet!=null){
            sheet.dispose();
            sheet=null;
        }
        
        if( sheetVar!=null ){
                           sheetVar.dispose();
                           sheetVar=null;
        }                   
        
        if(xls!=null){
                 xls.dispose();              
                 xls=null;
        }         
        
        if(site!=null){
                 site.dispose();  
                 site=null;
        }         
        
        
    }
    
    
    public void createPartControl1(Composite parent, String fileName, String rangoIni, String rangoFin , String sheetName , 
String rango ) {

        /*
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
        Variant intVariant=null;  */
         
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
            
            sheetVar = xls.getProperty(ids[0]);
            
            
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
                   
                       //count.dispose();
                       //intVariant.dispose();
   
                       //count=null;
                       //intVariant=null;
   
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
                    
                    
                                                //sheets.dispose();
                                                //sheets=null;
                                  
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
                
                if(sheets!=null)
                    sheets.dispose();
                sheets=null;
                
                if(count!=null)
                    count.dispose();
                count=null;
                
                if(intVariant!=null)
                    intVariant.dispose();
                intVariant=null;
                
                if(encontro==false){
                 //  Utilerias.logger(getClass()).info("No se encontro el sheet con nombre :"+ sheetName );
                   return;
                }




                //**
                
                
                
            }
            
           // site.dispose();
            
            //****inivio pruebas range
            
                    //       sheets.dispose();
              //  sheet.dispose();
            if(sheetsVariant!=null){
                sheetsVariant.dispose();
                sheetsVariant=null;
            }   
                
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
                        if(names!=null){
                            names.dispose();
                        }
                        names=xls.getProperty(namesId);   
                        if(namedRanges!=null){
                            namedRanges.dispose();
                            namedRanges=null;
                        }
                        namedRanges =  names.getAutomation ();
                        rangeItems= namedRanges.invoke(nameItemId,arguments_4 );                        
                        System.out.println(workbookIds);
                        
                    }
 
                    if( rangeItems==null ){
                        return;
                    }
                    
                    if(namedRange!=null){
                        namedRange.dispose();
                        namedRange=null;
                    }
                    
                    namedRange=rangeItems.getAutomation();  

                    int refersToRangeId=namedRange.getIDsOfNames(new String[]{"RefersToRange"})[0];
                    Variant refersToRange = namedRange.getProperty(refersToRangeId);
                    
                   OleAutomation refRangeChild=refersToRange.getAutomation();
                    int[] copyId1 = refRangeChild.getIDsOfNames(new String[]{"Copy"});
                    refRangeChild.invoke(copyId1[0]);
                     
                    
                  //  refRangeChild.invoke(copyId1[0]);
                    
              /*      
arguments_4
names
namedRanges
refersToRange   */
                    
                   if( refRangeChild!=null ) {
                       refRangeChild.dispose();
                       refRangeChild=null;
                   }
        
                   if(refersToRange!=null){
                       refersToRange.dispose();
                       refersToRange=null;
                   }
                   
                   if(namedRange!=null){
                       namedRange.dispose();
                       namedRange=null;
                   }
                    
                   if( namedRanges!=null ){
                       namedRanges.dispose();
                       namedRanges=null;
                   }
                    
                   if(names!=null){
                       names.dispose();
                       names=null;
                   }
                   
                  if( rangeItems!=null ){
                      rangeItems.dispose();
                      rangeItems=null;
                  }
                   
                   
                   if(arguments_4!=null){
                       for( Variant var :arguments_4 ){
                           var.dispose();
                       }
                   }
                   
                   
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
          //  Utilerias.logger(getClass()).info("RANGE ID =" + rangeId);
            arguments_1 = new Variant[2];
            arguments_1[0] = new Variant(rangoIni);
            arguments_1[1] = new Variant(rangoFin);
            
          
            
            
            
           range = sheet.getProperty(rangeId, arguments_1);
            
        
   
  varSel=range.getAutomation();
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
             
            
            /*
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
            */
            
            
        } catch (SWTError | Exception e) {
           Utilerias.logger(getClass()).info("Unable to open activeX control");
           Utilerias.logger(getClass()).error(e);
        } finally {
            
            /*
                    
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
   */
    
        }
    }
    
}
