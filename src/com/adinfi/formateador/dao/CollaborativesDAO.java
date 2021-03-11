/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocCollabCandidateBO;
import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentVersion;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.sun.xml.ws.org.objectweb.asm.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Desarrollador
 */
public class CollaborativesDAO {
    
    public boolean saveCollabDocAs(  DocumentCollabBO docOrig , DocumentCollabBO docCopy , List<DocCollabCandidateBO> lstCandidates  ){
       if( docOrig==null || docCopy==null ) {
          return false; 
       }
      
       try{
           
       
       docOrig.setDocumentId(0);
       docOrig.setDocumentName(docCopy.getDocumentName());
       docOrig.setDocument_Name(docCopy.getDocument_Name());
       docOrig.setFileName(docCopy.getFileName());
       docOrig.setFile_Name(docCopy.getFile_Name());
       docOrig.setIdDocType(docCopy.getIdDocType());
       docOrig.setIdDocument_Type(docCopy.getIdDocument_Type());
       docOrig.setIdLanguage(docCopy.getIdLanguage());
       docOrig.setIdMarket(docCopy.getIdMarket());
       docOrig.setIdPublish(0);
       docOrig.setIdSubject(docCopy.getIdSubject() );
       docOrig.setMarketBO(docCopy.getMarketBO());
       docOrig.setPublishName(null);
       docOrig.setSubjectBO(docCopy.getSubjectBO());
       docOrig.setTipo(docCopy.getTipo());
       docOrig.setTipoBO(docCopy.getTipoBO());
        
       List<DocumentCollabItemBO> lstCollabItems=docOrig.getLstDocumentCollab();
       if(lstCollabItems!=null && lstCollabItems.size()>0  ){
           for( DocumentCollabItemBO item :lstCollabItems  ){
               item.setDocumentCollabItemId(0);               
           }
       }
       
       docOrig.setLstDocumentCollabOrig(null);
       docOrig.setDocumentId( this.saveCollaborative(docOrig) );
       
       if(lstCandidates!=null && lstCandidates.size()>0 ){
           for( DocCollabCandidateBO  cand : lstCandidates  ){
               cand.setCollabDocCandidateId(0);
               cand.setDocVersionTarget(docOrig.getDocVersionId());
               this.addDocumentCollabCand(cand);
               
           }
       }
       
       
       
       
       
       return true;
       
       }catch(Exception e){ 
          e.printStackTrace();
          return false;
       }
           
       
       
       
       
       
       
       
       
       
        
        
    }
    
    public DocumentCollabBO getDocument(int documentId ){
        Connection conn=null;
        DocumentCollabBO docBO=null;
        
        Statement stDocCollab=null;
        ResultSet rsDocCollab=null;
        String    sqlDocCollab=null;
        
        Statement stDocCollabItems=null;
        ResultSet rsDocCollabItems=null;
        String    sqlDocCollabItems=null;
        
        try{
            conn = ConnectionDB.getInstance().getConnection();
            sqlDocCollab = "SELECT               "
                    + "  D.DOCUMENT_ID ,   "
                    + "  D.DOCUMENT_NAME , "
                    + "  D.FILE_NAME,  "
                    + "	 D.IDDOCUMENT_TYPE,  "
                    + "	 D.IDMARKET,  "
                    + "	 D.IDLANGUAGE ,  "
                    + "  D.IDSUBJECT  , "
                    + "  DV.DOC_VERSION_ID ,  "
                    + "  MA.NAME AS MARKET , "
                    + "  DT.NAME AS DOCUMENT_TYPE ,  "                    
                    + "  D.COLLABORATIVE ,  "                    
                    + "  D.COLLABORATIVE_TYPE   "
                    + "  FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW  D  , " + GlobalDefines.DB_SCHEMA + "MARKET MA, " + GlobalDefines.DB_SCHEMA + "SUBJECT SU , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT  , " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV    "
                    + "  WHERE " 
                    + "  D.DOCUMENT_ID=" + documentId 
                    + "  AND D.IDMARKET=MA.IDMIVECTOR_REAL  " 
                    + "  AND D.DOCUMENT_ID=DV.DOCUMENT_ID "
                    + "  AND DV.VERSION=1 "
                    + "  AND D.IDDOCUMENT_TYPE=DT.IDDOCUMENT_TYPE  " ;           
            
            stDocCollab = conn.createStatement();
            rsDocCollab = stDocCollab.executeQuery(sqlDocCollab);
            
            
           if (rsDocCollab.next()) {

                docBO = new DocumentCollabBO();
                docBO.setDocumentId(rsDocCollab.getInt("document_id"));
                docBO.setTemplateName(rsDocCollab.getString("document_name"));
                docBO.setFileName(rsDocCollab.getString("file_name"));
                docBO.setIdDocType(rsDocCollab.getInt("idDocument_type"));
                docBO.setIdMarket(rsDocCollab.getInt("idMarket"));
                docBO.setIdLanguage(rsDocCollab.getInt("idLanguage"));
                docBO.setIdSubject(rsDocCollab.getInt("idSubject"));
                docBO.setCollaborative(rsDocCollab.getShort("collaborative")==1   );
                docBO.setCollaborativeType(rsDocCollab.getString("collaborative_type"));
                docBO.setDocVersionId(rsDocCollab.getInt("DOC_VERSION_ID"));
                

                MarketBO ma=new MarketBO();
                ma.setIdMarket(rsDocCollab.getInt("idMarket"));
                ma.setName(rsDocCollab.getString("market"));
                docBO.setMarketBO(ma);
                
                DocumentTypeBO dt=new DocumentTypeBO();
                dt.setIddocument_type(rsDocCollab.getInt("iddocument_type"));
                dt.setName(rsDocCollab.getString("document_type"));
                docBO.setTipoBO(dt);                
                

            } else {

                return null;
            }            
            
            sqlDocCollabItems="  SELECT " +
            "    DOC.DOCUMENT_ID  ," +
            "    DOC.DOCUMENT_NAME , " +
            "    DOC.FILE_NAME ," +
            "    DOC.IDMARKET , " +
            "    DOC.IDDOCUMENT_TYPE , " +
            "    DOC.IDLANGUAGE , " +
            "    DOC.IDSUBJECT , " +
            "    DCI.DOCUMENT_COLLAB_ITEM_ID ," +
            "    DCI.ITEM_DOC_VERSION_ID  ,    " +
            "    DCI.DOCUMENT_MODULE_ID ,   " +      
            "    SU.NAME AS TEMA ,   " +    
            "    DOC.NAME_AUTHOR ,  "        +
            "    DT.NAME AS DOCUMENT_TYPE_NAME , "   +     
            "    DV.VERSION     "  +                  
            " FROM  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM DCI " + 
            " inner join " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV ON (DCI.ITEM_DOC_VERSION_ID=DV.DOC_VERSION_ID  )" +
            " inner join " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC ON (DV.DOCUMENT_ID=DOC.DOCUMENT_ID)" +
            " inner join " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT ON (DOC.IDDOCUMENT_TYPE=DT.IDDOCUMENT_TYPE)" +
            " left join " + GlobalDefines.DB_SCHEMA + "SUBJECT SU ON (DOC.IDSUBJECT=SU.IDSUBJECT)  " +       
            " WHERE DCI.DOCUMENT_ID=" + documentId +
            " ORDER BY DCI.ORDER_ID  ";
            
            stDocCollabItems=conn.createStatement();
            rsDocCollabItems=stDocCollabItems.executeQuery(sqlDocCollabItems);
            List<DocumentCollabItemBO> items=null;
            DocumentCollabItemBO item=null;
            List<Integer> origIds=null;
            while(rsDocCollabItems.next()){
                if(items==null){
                    items=new ArrayList<>();
                    origIds=new ArrayList<>();
                    docBO.setLstDocumentCollab(items);
                    docBO.setLstDocumentCollabOrig(origIds);
                }
                item=new DocumentCollabItemBO();
                item.setDocumentCollabItemId(rsDocCollabItems.getInt("DOCUMENT_COLLAB_ITEM_ID") );
                item.setDocumentId(documentId);
                item.setItemDocVersionId(rsDocCollabItems.getInt("ITEM_DOC_VERSION_ID"));
                item.setDocumentName(rsDocCollabItems.getString("DOCUMENT_NAME") );
                item.setItemVersion( rsDocCollabItems.getInt("VERSION")  );
                item.setItemDocumentId(rsDocCollabItems.getInt("DOCUMENT_ID"));
                item.setDocumentModuleId(rsDocCollabItems.getInt("DOCUMENT_MODULE_ID")  );
                item.setFileName(rsDocCollabItems.getString("FILE_NAME"));
                item.setDocTypeName(rsDocCollabItems.getString("DOCUMENT_TYPE_NAME"));
                item.setTema(rsDocCollabItems.getString("TEMA"));                
                item.setAutor(rsDocCollabItems.getString("NAME_AUTHOR"));
                        
                items.add(item);
                
                origIds.add(item.getDocumentCollabItemId() );
  
            }
            

            
        }catch(Exception e){
            
            
        }finally{
            try{
               if( rsDocCollab!=null ) {
                   rsDocCollab.close();
               }
               
               if( stDocCollab!=null ){
                   stDocCollab.close();
               }
               
               if( rsDocCollabItems!=null ){
                   rsDocCollabItems.close();
               }
               
               if( stDocCollabItems!=null ){
                   stDocCollabItems.close();
               }
               
               if(conn!=null){
                   conn.close();
               }
                
            }catch(Exception e){
                
            }
            
        }
        return docBO;
    }
    
    
    public List<DocCollabCandidateBO> getDocCandidates( String tipo , int docVersionIdTarget , int idHeader , int idSection ){
        List<DocCollabCandidateBO> lst=null;
        Connection conn=null;
        StringBuilder sqlDocCollabCandidates=new StringBuilder();
        Statement stDocCollabCandidates=null;
        ResultSet rsDocCollabCandidates=null;
        
        
        if( GlobalDefines.TIPO_CAND_DOC.equals(tipo) ==false &&  GlobalDefines.TIPO_CAND_MODULE.equals(tipo)==false   ){
            return null;
        } 
        
        
        try{
             conn = ConnectionDB.getInstance().getConnection();
                                 
           if( GlobalDefines.TIPO_CAND_DOC.equals(tipo) ) { 
                sqlDocCollabCandidates.append("  SELECT ");
                sqlDocCollabCandidates.append("    CC.COLLAB_DOC_CANDIDATE_ID ,");
                sqlDocCollabCandidates.append("    CC.DOC_VERSION_ID , ");
                 sqlDocCollabCandidates.append("    CC.DOC_VERSION_ID_TARGET , ");
            //    sqlDocCollabCandidates.append("    CC.DOCUMENT_TYPE_ID , ");   
                 sqlDocCollabCandidates.append("    CC.TIPO ,    ");
                sqlDocCollabCandidates.append("    CC.IDHEADERCOLMOD ,    ");
                sqlDocCollabCandidates.append("    CC.IDSECTIONCOLMOD ,  ");
                sqlDocCollabCandidates.append("    CC.NAME ,  " );
                sqlDocCollabCandidates.append("    DOC.IDMARKET , ");
                sqlDocCollabCandidates.append("    DOC.IDDOCUMENT_TYPE  ,  ");
                sqlDocCollabCandidates.append("    DV.VERSION   , ");
                sqlDocCollabCandidates.append("    DOC.DOCUMENT_ID ,  ");
                sqlDocCollabCandidates.append("    DOC.DOCUMENT_NAME ,");
                sqlDocCollabCandidates.append("    DOC.NAME_AUTHOR , ");
                sqlDocCollabCandidates.append("    DOC.FILE_NAME , ");
                sqlDocCollabCandidates.append("    MA.NAME AS MARKET_NAME , ");
                sqlDocCollabCandidates.append("    DT.NAME AS DOCUMENT_TYPE_NAME  ,     ");
                sqlDocCollabCandidates.append("    SU.NAME AS SUBJECT    ");
                sqlDocCollabCandidates.append("  FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE CC  ");
                sqlDocCollabCandidates.append("  INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV ON (CC.DOC_VERSION_ID=DV.DOC_VERSION_ID) ");
                sqlDocCollabCandidates.append("  INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW DOC ON (DOC.DOCUMENT_ID = DV.DOCUMENT_ID) ");
                sqlDocCollabCandidates.append("  INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT ON (DOC.IDDOCUMENT_TYPE=DT.IDDOCUMENT_TYPE) ");
                sqlDocCollabCandidates.append("  INNER JOIN " + GlobalDefines.DB_SCHEMA + "MARKET MA ON (DOC.IDMARKET=MA.IDMIVECTOR_REAL) ");
                sqlDocCollabCandidates.append("  LEFT JOIN  " + GlobalDefines.DB_SCHEMA + "SUBJECT SU ON (SU.IDSUBJECT=DOC.IDSUBJECT) ");
                sqlDocCollabCandidates.append(" WHERE CC.DOC_VERSION_ID_TARGET=");
                sqlDocCollabCandidates.append(docVersionIdTarget);
                sqlDocCollabCandidates.append(" AND CC.TIPO='D' " );                
            
           }else{
               

                sqlDocCollabCandidates.append("    SELECT     CC.COLLAB_DOC_CANDIDATE_ID ,    CC.DOC_VERSION_ID ,    CC.DOC_VERSION_ID_TARGET ,        CC.IDHEADERCOLMOD ,        CC.IDSECTIONCOLMOD ,      ");
                sqlDocCollabCandidates.append(" CC.NAME ,   CC.IDHEADERCOLMOD , CC.IDSECTIONCOLMOD ,    CC.TIPO  ,       CC.MODULE_ID ,    CC.DOCUMENT_MODULE_ID   , CC.DOCUMENT_MODULE_ID_TARGET ,   SU.NAME AS SUBJECT       ,  CC.SUBJECTID   , ");
                sqlDocCollabCandidates.append( "     DOC.DOCUMENT_NAME , " );
                sqlDocCollabCandidates.append( "     DT.NAME AS DOCUMENT_TYPE_NAME , " );
                sqlDocCollabCandidates.append( "     DOC.NAME_AUTHOR   ");
                
//                sqlDocCollabCandidates.append(" FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE CC  ,  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT , " + GlobalDefines.DB_SCHEMA + "MARKET MA  , " + GlobalDefines.DB_SCHEMA + "SUBJECT SU     ");
                sqlDocCollabCandidates.append(" FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE CC " );
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM ON ( CC.DOCUMENT_MODULE_ID=DM.DOCUMENT_MODULE_ID )  ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV ON ( DM.DOC_VERSION_ID=DV.DOC_VERSION_ID AND DM.DOCUMENT_ID=DV.DOCUMENT_ID ) ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW  DOC ON ( DV.DOCUMENT_ID=DOC.DOCUMENT_ID )   ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT ON ( DOC.IDDOCUMENT_TYPE = DT.IDDOCUMENT_TYPE   )  ");
                                                                                                
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "SUBJECT SU ON ( SU.IDSUBJECT=CC.SUBJECTID ) ");
                sqlDocCollabCandidates.append(" WHERE ");
                sqlDocCollabCandidates.append(" CC.DOC_VERSION_ID_TARGET=");
                sqlDocCollabCandidates.append(docVersionIdTarget);
                
                if (idHeader > 0){
                    sqlDocCollabCandidates.append(" AND CC.IDHEADERCOLMOD=");
                    sqlDocCollabCandidates.append(idHeader);
                }
                
                if (idSection > 0){
                    sqlDocCollabCandidates.append(" AND CC.IDSECTIONCOLMOD=");
                    sqlDocCollabCandidates.append(idSection);
                }
             //   sqlDocCollabCandidates.append(" AND CC.DOCUMENT_TYPE_ID=DT.IDDOCUMENT_TYPE ");
                sqlDocCollabCandidates.append(" AND CC.TIPO='M'  ");
             //   sqlDocCollabCandidates.append(" AND CC.MARKET_ID=MA.IDMARKET ");
             //   sqlDocCollabCandidates.append(" AND SU.IDSUBJECT=CC.SUBJECTID  ");
                  sqlDocCollabCandidates.append(" AND ( CC.ESTATUS IS NULL OR   CC.ESTATUS  <> 'D'   ) ");
                

               
 
               
           }
             
             
             stDocCollabCandidates=conn.createStatement();
             rsDocCollabCandidates=stDocCollabCandidates.executeQuery(sqlDocCollabCandidates.toString());
             DocCollabCandidateBO bo=null;
             while(rsDocCollabCandidates.next()  ){
                 if( lst==null  ){
                     lst=new ArrayList<>();
                 }
                 bo=new DocCollabCandidateBO();
                 bo.setCollabDocCandidateId( rsDocCollabCandidates.getInt("COLLAB_DOC_CANDIDATE_ID") );
                
             //    bo.setDocumentTypeId(rsDocCollabCandidates.getInt("DOCUMENT_TYPE_ID")  );
            //     bo.setMarketId( rsDocCollabCandidates.getInt("MARKET_ID")  );
                 bo.setTipo(rsDocCollabCandidates.getString("TIPO"));
            //     bo.setCandTypeName( rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME")  );
                 bo.setDocVersionTarget( rsDocCollabCandidates.getInt("DOC_VERSION_ID_TARGET") );
                 
                 if( GlobalDefines.TIPO_CAND_DOC.equals(tipo)   ){

                    bo.setDocVersionId( rsDocCollabCandidates.getInt("DOC_VERSION_ID") );
                    bo.setCandName(rsDocCollabCandidates.getString("DOCUMENT_NAME")  );
                    bo.setCandSubject(rsDocCollabCandidates.getString("SUBJECT"));
                    bo.setCandAuthor(rsDocCollabCandidates.getString("NAME_AUTHOR"));
                    bo.setCandId(""+rsDocCollabCandidates.getInt("DOCUMENT_ID"));
                    bo.setFileName(rsDocCollabCandidates.getString("FILE_NAME"));
                    bo.setCandTypeName(rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME"));
                   
                 }else{
                    bo.setDocumentModuleId(rsDocCollabCandidates.getInt("DOCUMENT_MODULE_ID")  );
                    bo.setIdHeader(rsDocCollabCandidates.getInt("IDHEADERCOLMOD"));
                    bo.setIdSection( rsDocCollabCandidates.getInt("IDSECTIONCOLMOD") );
                    bo.setName( rsDocCollabCandidates.getString("NAME") );
                    bo.setCandName(rsDocCollabCandidates.getString("NAME"));
                    bo.setCandSubject(rsDocCollabCandidates.getString("SUBJECT"));
                    bo.setCandSubjectId(rsDocCollabCandidates.getInt("SUBJECTID"));
                    bo.setModuleId(rsDocCollabCandidates.getInt("MODULE_ID"));
                    
                    bo.setCandTypeName(rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME"));
                   // bo.setCandName(rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME"));
                    bo.setDocSrcName(rsDocCollabCandidates.getString("DOCUMENT_NAME"));
                    bo.setCandAuthor(rsDocCollabCandidates.getString("NAME_AUTHOR"));
                    bo.setDocumentModuleIdTarget( rsDocCollabCandidates.getInt("DOCUMENT_MODULE_ID_TARGET")  );
                                          
                 }
                 
                 lst.add(bo);                                  
             }
  
        }catch(Exception e){
             Utilerias.logger(getClass()).error(e);            
        }finally{
            try{
                                                                                                  if( rsDocCollabCandidates!=null ){
                    rsDocCollabCandidates.close();
                }
                
                if( stDocCollabCandidates!=null ){
                    stDocCollabCandidates.close();
                }
                
                if(conn!=null){
                    conn.close();
                }
                
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return lst;
    }
    
    
    
    public  DocCollabCandidateBO geCandidateMod(   int docVersionIdTarget , int documentModuleId ){
        DocCollabCandidateBO bo=null;
        Connection conn=null;
        StringBuilder sqlDocCollabCandidates=new StringBuilder();
        Statement stDocCollabCandidates=null;
        ResultSet rsDocCollabCandidates=null;
        
                              
        try{
             conn = ConnectionDB.getInstance().getConnection();
                                 
          
                sqlDocCollabCandidates.append("    SELECT     CC.COLLAB_DOC_CANDIDATE_ID ,    CC.DOC_VERSION_ID ,    CC.DOC_VERSION_ID_TARGET ,        CC.IDHEADERCOLMOD ,        CC.IDSECTIONCOLMOD ,      ");
                sqlDocCollabCandidates.append(" CC.NAME ,   CC.IDHEADERCOLMOD , CC.IDSECTIONCOLMOD ,    CC.TIPO  ,       CC.MODULE_ID ,    CC.DOCUMENT_MODULE_ID   ,  CC.DOCUMENT_MODULE_ID_TARGET    ,  SU.NAME AS SUBJECT       ,  CC.SUBJECTID   , ");
                sqlDocCollabCandidates.append( "     DOC.DOCUMENT_NAME , " );
                sqlDocCollabCandidates.append( "     DT.NAME AS DOCUMENT_TYPE_NAME , " );
                sqlDocCollabCandidates.append( "     DOC.NAME_AUTHOR   ");
                
//                sqlDocCollabCandidates.append(" FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE CC  ,  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT , " + GlobalDefines.DB_SCHEMA + "MARKET MA  , " + GlobalDefines.DB_SCHEMA + "SUBJECT SU     ");
                sqlDocCollabCandidates.append(" FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE CC " );
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW DM ON ( CC.DOCUMENT_MODULE_ID=DM.DOCUMENT_MODULE_ID )  ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW DV ON ( DM.DOC_VERSION_ID=DV.DOC_VERSION_ID AND DM.DOCUMENT_ID=DV.DOCUMENT_ID ) ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW  DOC ON ( DV.DOCUMENT_ID=DOC.DOCUMENT_ID )   ");
                sqlDocCollabCandidates.append(" LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE DT ON ( DOC.IDDOCUMENT_TYPE = DT.IDDOCUMENT_TYPE   )  ");
                                                                                                
                sqlDocCollabCandidates.append("   , " + GlobalDefines.DB_SCHEMA + "SUBJECT SU     ");
                sqlDocCollabCandidates.append(" WHERE ");
                sqlDocCollabCandidates.append(" CC.DOC_VERSION_ID_TARGET=");
                sqlDocCollabCandidates.append(docVersionIdTarget);
                
                sqlDocCollabCandidates.append(" AND CC.DOCUMENT_MODULE_ID="  );
                sqlDocCollabCandidates.append(documentModuleId);
                
                
  
            
                sqlDocCollabCandidates.append(" AND CC.TIPO='M'  ");
            
                sqlDocCollabCandidates.append(" AND SU.IDSUBJECT=CC.SUBJECTID  ");
              //  sqlDocCollabCandidates.append(" AND ( CC.ESTATUS IS NULL OR   CC.ESTATUS  <> 'D'   ) ");
                

 
             stDocCollabCandidates=conn.createStatement();
             rsDocCollabCandidates=stDocCollabCandidates.executeQuery(sqlDocCollabCandidates.toString());
             bo=null;
             if(rsDocCollabCandidates.next()  ){
                 
                 bo=new DocCollabCandidateBO();
                 bo.setCollabDocCandidateId( rsDocCollabCandidates.getInt("COLLAB_DOC_CANDIDATE_ID") );
                
             //    bo.setDocumentTypeId(rsDocCollabCandidates.getInt("DOCUMENT_TYPE_ID")  );
            //     bo.setMarketId( rsDocCollabCandidates.getInt("MARKET_ID")  );
                 bo.setTipo(rsDocCollabCandidates.getString("TIPO"));
            //     bo.setCandTypeName( rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME")  );
                 bo.setDocVersionTarget( rsDocCollabCandidates.getInt("DOC_VERSION_ID_TARGET") );
                 
 
                    bo.setDocumentModuleId(rsDocCollabCandidates.getInt("DOCUMENT_MODULE_ID")  );
                    bo.setIdHeader(rsDocCollabCandidates.getInt("IDHEADERCOLMOD"));
                    bo.setIdSection( rsDocCollabCandidates.getInt("IDSECTIONCOLMOD") );
                    bo.setName( rsDocCollabCandidates.getString("NAME") );
                    bo.setCandName(rsDocCollabCandidates.getString("NAME"));
                    bo.setCandSubject(rsDocCollabCandidates.getString("SUBJECT"));
                    bo.setCandSubjectId(rsDocCollabCandidates.getInt("SUBJECTID"));
                    bo.setModuleId(rsDocCollabCandidates.getInt("MODULE_ID"));
                    
                    bo.setCandTypeName(rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME"));
                   // bo.setCandName(rsDocCollabCandidates.getString("DOCUMENT_TYPE_NAME"));
                    bo.setDocSrcName(rsDocCollabCandidates.getString("DOCUMENT_NAME"));
                    bo.setCandAuthor(rsDocCollabCandidates.getString("NAME_AUTHOR"));
                    bo.setDocumentModuleIdTarget( rsDocCollabCandidates.getInt("DOCUMENT_MODULE_ID_TARGET")  );
                                          
                 
                 
                                                 
             }
  
        }catch(Exception e){
             Utilerias.logger(getClass()).error(e);            
        }finally{
            try{
                                                                                                  if( rsDocCollabCandidates!=null ){
                    rsDocCollabCandidates.close();
                }
                
                if( stDocCollabCandidates!=null ){
                    stDocCollabCandidates.close();
                }
                
                if(conn!=null){
                    conn.close();
                }
                
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return bo;
    }
    
    
    
    

     public int deleteDocumentCollabCand( int docCollabCandId ){
        int response=0;
        // COLLAB_DOC_CANDIDATE 
        if( docCollabCandId<=0 ){
            return -1;
        }
        
                       
        Connection conn=null;
       
        
        Statement stDeleteCollabCand=null;      
        String sqlDeleteCollabCand=null;
                       
        try{
            sqlDeleteCollabCand=" DELETE FROM  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE  WHERE COLLAB_DOC_CANDIDATE_ID="  + docCollabCandId ;
            conn = ConnectionDB.getInstance().getConnection();            
            stDeleteCollabCand=conn.createStatement();                                                         
            int n=stDeleteCollabCand.executeUpdate(sqlDeleteCollabCand);
            if(n>0){
                Utilerias.logger(getClass()).info("Se elimino el document_collab_id="+ docCollabCandId ); 
            }
            response=0;

            
        }catch(Exception e){
            response=-1;
            e.printStackTrace();                               
        }finally{
             try{
                
                if(stDeleteCollabCand!=null){
                    stDeleteCollabCand.close();
                }
                                               
                if(conn!=null){
                    conn.close();
                }
                
                
             }catch( Exception e ){
                 Utilerias.logger(getClass()).error(e); 
             }
             
        }
        
        return response;
        
    }
     
     
     public int deleteModuleCollabCandLog( int docCollabCandId , int documentModuleIdNew ){
        int response=0;
        // COLLAB_DOC_CANDIDATE 
        if( docCollabCandId<=0 ){
            return -1;
        }
        
                       
        Connection conn=null;
       
        
        Statement stUpdateCollabCand=null;      
        String sqlUpdateCollabCand=null;
                       
        try{
            if( documentModuleIdNew>0){
               sqlUpdateCollabCand=" UPDATE  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE SET ESTATUS='D' , DOCUMENT_MODULE_ID_TARGET="+documentModuleIdNew+"  WHERE COLLAB_DOC_CANDIDATE_ID="  + docCollabCandId ;                
            }else{
               sqlUpdateCollabCand=" UPDATE  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE SET ESTATUS='D'  WHERE COLLAB_DOC_CANDIDATE_ID="  + docCollabCandId ;
            }
            conn = ConnectionDB.getInstance().getConnection();            
            stUpdateCollabCand=conn.createStatement();                                                         
            int n=stUpdateCollabCand.executeUpdate(sqlUpdateCollabCand);
            if(n>0){
                Utilerias.logger(getClass()).info("Se actualizo el estatis a D de document_collab_id="+ docCollabCandId ); 
            }
            response=0;

            
        }catch(Exception e){
            response=-1;
            e.printStackTrace();                               
        }finally{
             try{
                
                if(stUpdateCollabCand!=null){
                    stUpdateCollabCand.close();
                }
                                               
                if(conn!=null){
                    conn.close();
                }
                
                
             }catch( Exception e ){
                 Utilerias.logger(getClass()).error(e); 
             }
             
        }
        
        return response;
        
    }
     
     
     
     
     
     public int restoreModuleCollabCandLog( int documentModuleIdTarget ){
        int response=0;
        // COLLAB_DOC_CANDIDATE 
        if( documentModuleIdTarget<=0 ){
            return -1;
        }
        
                       
        Connection conn=null;
       
        
        Statement stUpdateCollabCand=null;      
        String sqlUpdateCollabCand=null;
                       
        try{
            sqlUpdateCollabCand=" UPDATE  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE SET ESTATUS=NULL  WHERE DOCUMENT_MODULE_ID_TARGET ="  + documentModuleIdTarget ;
            conn = ConnectionDB.getInstance().getConnection();            
            stUpdateCollabCand=conn.createStatement();                                                         
            int n=stUpdateCollabCand.executeUpdate(sqlUpdateCollabCand);
            if(n>0){
                Utilerias.logger(getClass()).info("Se actualizo el estatis a NULL de DOCUMENT_MODULE_ID_TARGET="+ documentModuleIdTarget ); 
            }
            response=0;

            
        }catch(Exception e){
            response=-1;
            e.printStackTrace();                               
        }finally{
             try{
                
                if(stUpdateCollabCand!=null){
                    stUpdateCollabCand.close();
                }
                                               
                if(conn!=null){
                    conn.close();
                }
                
                
             }catch( Exception e ){
                 Utilerias.logger(getClass()).error(e); 
             }
             
        }
        
        return response;
        
    }
     
     
     
     public int restoreModuleCollabCandLogById( int documentCollabCandId ){
        int response=0;
        // COLLAB_DOC_CANDIDATE 
        if( documentCollabCandId<=0 ){
            return -1;
        }
        
                       
        Connection conn=null;
       
        
        Statement stUpdateCollabCand=null;      
        String sqlUpdateCollabCand=null;
                       
        try{
            sqlUpdateCollabCand=" UPDATE  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE SET ESTATUS=NULL  WHERE COLLAB_DOC_CANDIDATE_ID ="  + documentCollabCandId ;
            conn = ConnectionDB.getInstance().getConnection();            
            stUpdateCollabCand=conn.createStatement();                                                         
            int n=stUpdateCollabCand.executeUpdate(sqlUpdateCollabCand);
            if(n>0){
               // Utilerias.logger(getClass()).info("Se actualizo el estatis a NULL de DOCUMENT_MODULE_ID_TARGET="+ documentModuleIdTarget ); 
            }
            response=0;

            
        }catch(Exception e){
            response=-1;
            e.printStackTrace();                               
        }finally{
             try{
                
                if(stUpdateCollabCand!=null){
                    stUpdateCollabCand.close();
                }
                                               
                if(conn!=null){
                    conn.close();
                }
                
                
             }catch( Exception e ){
                 Utilerias.logger(getClass()).error(e); 
             }
             
        }
        
        return response;
        
    }
     
     
     
     
     
     
     
    
    public boolean validarModulo(int idMarket, int idDocType, int idModule, int idDocModule) {
        boolean modValido = true;
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            StringBuilder sqlMod = new StringBuilder();
            sqlMod.append("SELECT COUNT(COLLAB_DOC_CANDIDATE_ID) AS MODULOS ");
            sqlMod.append("FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE ");
            sqlMod.append("WHERE MARKET_ID = ");
            sqlMod.append(idMarket);
            sqlMod.append(" AND DOCUMENT_TYPE_ID = ");
            sqlMod.append(idDocType);
            sqlMod.append(" AND (MODULE_ID = ");
            sqlMod.append(idModule);
            sqlMod.append(" OR DOCUMENT_MODULE_ID = ");
            sqlMod.append(idDocModule);
            sqlMod.append(")UNION ");
            sqlMod.append("SELECT COUNT(COLLAB_DOC_CANDIDATE_ID) AS MODULOS ");
            sqlMod.append("FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE ");
            sqlMod.append("WHERE MARKET_ID = ");
            sqlMod.append(idMarket);
            sqlMod.append(" AND DOCUMENT_TYPE_ID = ");
            sqlMod.append(idDocType);
            sqlMod.append(" AND MODULE_ID = (SELECT MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW WHERE DOCUMENT_MODULE_ID = ");
            sqlMod.append(idDocModule);
            sqlMod.append(")UNION ");
            sqlMod.append("SELECT COUNT(MODULE_ID) AS MODULOS ");
            sqlMod.append("FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW ");
            sqlMod.append("WHERE DOCUMENT_MODULE_ID IN (SELECT DOCUMENT_MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE WHERE DOCUMENT_MODULE_ID IS NOT NULL AND ");
            sqlMod.append("MARKET_ID = ");
            sqlMod.append(idMarket);
            sqlMod.append(" AND DOCUMENT_TYPE_ID = ");
            sqlMod.append(idDocType);
            sqlMod.append(") AND MODULE_ID = (SELECT MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW WHERE DOCUMENT_MODULE_ID = ");
            sqlMod.append(idDocModule);
            sqlMod.append(")UNION ");
            sqlMod.append("SELECT COUNT(MODULE_ID) AS MODULOS ");
            sqlMod.append("FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_MODULE_NEW ");
            sqlMod.append("WHERE DOCUMENT_MODULE_ID IN (SELECT DOCUMENT_MODULE_ID FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE WHERE DOCUMENT_MODULE_ID IS NOT NULL AND ");
            sqlMod.append("MARKET_ID = ");
            sqlMod.append(idMarket);
            sqlMod.append(" AND DOCUMENT_TYPE_ID = ");
            sqlMod.append(idDocType);
            sqlMod.append(") AND MODULE_ID = ");
            sqlMod.append(idModule);

            ResultSet rsMod = st.executeQuery(sqlMod.toString());
            int val1 = 0;
            while (rsMod.next()) {
                val1 = val1 + rsMod.getInt("MODULOS");
            }
            modValido = val1 == 0;
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return modValido;
    }
    
    
    /**
     * Agrega un documento al
     */
  //  public int addDocumentCollabCand( String tipo ,  int docVersionId , int moduleId ,    int marketId , int docTypeId ){
     public int addDocumentCollabCand( DocCollabCandidateBO docCandBO  ){
        int response=0;
        // COLLAB_DOC_CANDIDATE 
        if( docCandBO==null ){
            return -1;
        }
        
        /*
        if( docCandBO.getTipo()==null  ){
            return -1;
        }*/
        
        
        String tipo=docCandBO.getTipo();
        if( !GlobalDefines.TIPO_CAND_DOC.equals(tipo) &&  !GlobalDefines.TIPO_CAND_MODULE.equals(tipo)   ){
            return -1;
        }
        
        Connection conn=null;
        Statement st=null;
        
        Statement stExists=null;
        ResultSet rsExists=null;
        String sqlExists=null;
        String sqlExistsPanelDoc=null;
        Statement stExistsPanelDoc=null;
        ResultSet rsExistsPanelDoc=null;
        
        
        String sqlInsertCollabCand=null;
        try{
            
            if( GlobalDefines.TIPO_CAND_DOC.equals(tipo) ){
                    sqlExists=" SELECT * FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE WHERE "
                            + "  DOC_VERSION_ID="+ docCandBO.getDocVersionId()+" "
                        //    + "  AND MARKET_ID=" + docCandBO.getMarketId() + " "
                        //    + "  AND DOCUMENT_TYPE_ID=" + docCandBO.getDocumentTypeId();
                            + "  AND DOC_VERSION_ID_TARGET=" + docCandBO.getDocVersionTarget() +
                            "  AND TIPO ='"+ tipo+"' ";

                    sqlInsertCollabCand=" INSERT INTO  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE   "+
                    " ( DOC_VERSION_ID , DOC_VERSION_ID_TARGET , TIPO  , FECHA   ) "
                   + "VALUES ( " +  docCandBO.getDocVersionId() + " , " +  docCandBO.getDocVersionTarget() + "  , 'D' ,    CURRENT DATE   ) "  ;
                    
                    
                    sqlExistsPanelDoc= "  select * from " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM CI WHERE CI.ITEM_DOC_VERSION_ID= "+ docCandBO.getDocVersionId() +" "
                            + "  AND CI.DOC_VERSION_ID= " +docCandBO.getDocVersionTarget();
            
            }else{
                    sqlExists=" SELECT * FROM " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE WHERE "
                            + "  DOCUMENT_MODULE_ID="+ docCandBO.getDocumentModuleId()+" "
                           // + "  AND MARKET_ID=" + docCandBO.getMarketId() + " "
                           // + "  AND DOCUMENT_TYPE_ID=" + docCandBO.getDocumentTypeId();
                            + "  AND DOC_VERSION_ID_TARGET=" + docCandBO.getDocVersionTarget() +
                            "  AND TIPO ='"+ tipo+ "' ";
                            

                    sqlInsertCollabCand=" INSERT INTO  " + GlobalDefines.DB_SCHEMA + "COLLAB_DOC_CANDIDATE   "+
                    " ( " + (docCandBO.getDocumentModuleId() > 0 ? "DOCUMENT_MODULE_ID" : "MODULE_ID") + " , DOC_VERSION_ID_TARGET  , FECHA , IDHEADERCOLMOD , IDSECTIONCOLMOD , NAME, TIPO, SUBJECT, SUBJECTID) "
                   + "VALUES ( " +  (docCandBO.getDocumentModuleId() > 0 ? docCandBO.getDocumentModuleId() : docCandBO.getModuleId()) + " , " + docCandBO.getDocVersionTarget() +" , "
                            + "CURRENT DATE , "+ docCandBO.getIdHeader() +" , " + docCandBO.getIdSection()  +" , '"+ docCandBO.getCandName() + "', '" + docCandBO.getTipo() + "', '" + docCandBO.getCandSubject() + "', " + docCandBO.getCandSubjectId() + ") "  ;
                
            }
            
            conn = ConnectionDB.getInstance().getConnection();
            
            stExists=conn.createStatement();
            rsExists=stExists.executeQuery(sqlExists);
            if( rsExists.next() ){
                return 11;//Elemento repetido
            }
            
            
            if( GlobalDefines.TIPO_CAND_DOC.equals(tipo)  ){
                stExistsPanelDoc=conn.createStatement();
                rsExistsPanelDoc=stExistsPanelDoc.executeQuery(sqlExistsPanelDoc);
                if(rsExistsPanelDoc.next()){
                    return 1;
                }                                    
            }
            
            
            st=conn.createStatement();
            
            int n=st.executeUpdate(sqlInsertCollabCand);
            response=0;

            
        }catch(Exception e){
            response=-1;
            e.printStackTrace();                               
        }finally{
             try{
                if(st!=null){
                   st.close();
                } 
                
                if(stExists!=null){
                    stExists.close();
                }
                
                if( rsExists!=null ){
                    rsExists.close();
                }
                
                if(rsExistsPanelDoc!=null){
                    rsExistsPanelDoc.close();
                }
                
                if(stExistsPanelDoc!=null){
                    stExistsPanelDoc.close();
                }
                 
                if(conn!=null){
                    conn.close();
                }
                
                
             }catch( Exception e ){
                 
             }
             
        }
        
        return response;
        
    }
    
            
   public int saveCollaborative(DocumentCollabBO docBO) {
        String sqlInsDoc = new StringBuilder(" INSERT INTO ").append(GlobalDefines.DB_SCHEMA)
                .append("DOCUMENT_NEW ( DOCUMENT_NAME, FECHA_CREACION, FILE_NAME, IDMARKET, IDDOCUMENT_TYPE, IDLANGUAGE, IDSUBJECT, FAVORITE, GUID, ID_AUTHOR, NAME_AUTHOR, COLLABORATIVE, COLLABORATIVE_TYPE ) ")
                .append(" VALUES( ?, CURRENT DATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ").toString();

        int documentId = 0;
        Connection conn = null;
        PreparedStatement stInsDoc = null; 
        Statement stInsDocVersion =null;
        int version=1;
        boolean isNew = false;
                 
        try {
            conn = ConnectionDB.getInstance().getConnection();
            stInsDoc = conn.prepareStatement(sqlInsDoc, Statement.RETURN_GENERATED_KEYS); 

            int favorite = (docBO.isFavorite()) ? 1 : 0;

            if (docBO.getDocumentId() <=0 ) {
                isNew = true;

                int i=1;
                stInsDoc.setString(i++, docBO.getDocumentName());
                stInsDoc.setString(i++, docBO.getFileName());
                stInsDoc.setInt(i++, docBO.getIdMarket());
                stInsDoc.setInt(i++, docBO.getIdDocType());
                stInsDoc.setInt(i++, docBO.getIdLanguage());
                stInsDoc.setInt(i++, docBO.getIdSubject());
                stInsDoc.setInt(i++, favorite);
                stInsDoc.setString(i++, docBO.getGuid());
                stInsDoc.setInt(i++, InstanceContext.getInstance().getUsuario().getUsuarioId());
                stInsDoc.setString(i++, InstanceContext.getInstance().getUsuario().getUsuarioNT());
                stInsDoc.setInt(i++, 1);
                stInsDoc.setString(i++, GlobalDefines.COLLAB_TYPE_DOC);
                
                int n = stInsDoc.executeUpdate();

                ResultSet rs;

                if (n > 0) {
                    System.out.println("Se inserto un elemento ");
                    rs = stInsDoc.getGeneratedKeys();
                    if (rs.next()) {
                        documentId = rs.getInt(1);
                        System.out.println("valor de documentId generada automáticamente = " + documentId);
                    }
                    rs.close();
                    // Cerrar ResultSet
                    docBO.setDocumentId(documentId);
                    //upsertCollaboratives(docBO);
                    // insertamos la version
                    String   sqlInsDocVersion = " INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW ( DOCUMENT_ID , VERSION , FECHA_CREACION ) VALUES ( "
                + "  " + docBO.getDocumentId() + " , "+version+ " , CURRENT DATE )  ";
               int docVersionId = 0;



                stInsDocVersion = conn.createStatement();

                n = stInsDocVersion.executeUpdate(sqlInsDocVersion, Statement.RETURN_GENERATED_KEYS);

               

            if (n > 0) {
                System.out.println("Se inserto una version para el documentId  " + docBO.getDocumentId());
                rs = stInsDocVersion.getGeneratedKeys();
                if (rs.next()) {
                    docVersionId = rs.getInt(1);
                    System.out.println("valor de documentId generada automáticamente = " + docVersionId);
                }
                rs.close();
                // Cerrar ResultSet
                //docBO.setDocumentId(documentId);                          
                docBO.setVersion(version);
                docBO.setDocVersionId(docVersionId);

                DocumentVersion docVersion = new DocumentVersion();
                docVersion.setDocVersionId(docVersionId);
                docVersion.setVersion(version);
            }
            
            DocumentDAO docDAO = new DocumentDAO();
            docDAO.upsertModules(docBO, isNew);
                    
                    
                    
                    
                    
                    
                    

                }

            }
            upsertCollaboratives(docBO);
           

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }finally{
            try{
               if(stInsDoc!=null){
                   stInsDoc.close();
               }
               
               if(stInsDocVersion!=null){
                   stInsDocVersion.close();
               }
               
               if(conn!=null){
                   conn.close();
               }
                
            }catch(Exception e){
                 Utilerias.logger(getClass()).error(e);
            }
        }
        
        
        
        return docBO.getDocumentId();
    }
    
    
    
    public void upsertCollaboratives(DocumentCollabBO docBO) {

        
        PreparedStatement pstInsCollab =null;
        PreparedStatement pstUpdCollab =null;
        PreparedStatement pstDelCollab =null;
        
        //String sqlDelModules;
        String sqlInsCollab = " INSERT INTO  " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM  (  ITEM_DOC_VERSION_ID , DOCUMENT_ID, DOC_VERSION_ID , DOCUMENT_MODULE_ID ,  ORDER_ID  ) "
                + "  VALUES ( ? , "+ docBO.getDocumentId()+", "+ docBO.getDocVersionId() + ", ? , ? ) ";
        
        String sqlUpdCollab= " UPDATE " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM SET ORDER_ID= ? WHERE DOCUMENT_COLLAB_ITEM_ID=?  ";

        String sqlDelCollab = " DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM WHERE  DOCUMENT_COLLAB_ITEM_ID = ? " ;
                
        Connection conn = null;
       
        ResultSet rs;
        Map<Integer,Integer> currentIds=new TreeMap<>();
        int documentId = docBO.getDocumentId();
        int docCollabId = 0;
        try {
            conn = ConnectionDB.getInstance().getConnection();
                //Statement stDelModules = conn.createStatement();
            pstInsCollab = conn.prepareStatement(sqlInsCollab, Statement.RETURN_GENERATED_KEYS);
            pstUpdCollab = conn.prepareStatement(sqlUpdCollab);
            pstDelCollab = conn.prepareStatement(sqlDelCollab);
                                             
            int n = 0;                        
            List<DocumentCollabItemBO> lstCollab = docBO.getLstDocumentCollab();
            if( lstCollab==null ){
                return;
            }
            
             for( DocumentCollabItemBO docCollab :lstCollab  ){
                 if( docCollab==null ){
                     continue;
                 }
                 
                 if( docCollab.getDocumentCollabItemId()>0 ){
                     currentIds.put(docCollab.getDocumentCollabItemId(), docCollab.getDocumentCollabItemId());
                 }
                 
             }
            
            List<Integer> lstIdOrigs=docBO.getLstDocumentCollabOrig();
            Integer idTmp;
            boolean borrar;
            if( lstIdOrigs!=null && lstIdOrigs.size()>0 ){
                for( Integer idOrig :lstIdOrigs  ){ 
                  borrar=false;  
                  if( currentIds==null || currentIds.size()<=0  ){
                      borrar=true;
                  }else{
                      idTmp=currentIds.get(idOrig);
                      if( idTmp==null ){
                         borrar=true; 
                      }
                      
                  }
                  if( borrar==true ){
                      pstDelCollab.setInt(1, idOrig );
                      n=pstDelCollab.executeUpdate();
                      if( n>0 ){
                          System.out.println("Se elimino el doc collab id :"+ idOrig );
                      }
                  }

                }
            
            }
                        
            int i=0;
            lstIdOrigs=new ArrayList<>();
            docBO.setLstDocumentCollabOrig(lstIdOrigs);
            for( DocumentCollabItemBO docCollabItem :lstCollab  ){
                i++;
                if( docCollabItem==null ){
                    continue;
                }
                docCollabItem.setDocumentId(documentId);
                docCollabItem.setDocVersionId(docBO.getDocVersionId());
                
               if( docCollabItem.getDocumentCollabItemId()<=0 ) {
                   if( docCollabItem.getItemDocVersionId()<=0 ){
                     pstInsCollab.setNull(StatementConstant.SC_1.get() , Type.INT  );                       
                   }else{
                     pstInsCollab.setInt(StatementConstant.SC_1.get(), docCollabItem.getItemDocVersionId()  );
                   }  
                   
                   if( docCollabItem.getDocumentModuleId()<=0 ){
                      pstInsCollab.setNull(StatementConstant.SC_2.get(), Type.INT);
                   }else{
                      pstInsCollab.setInt(StatementConstant.SC_2.get(), docCollabItem.getDocumentModuleId()  );
                   } 
                    
                    
                    
                    pstInsCollab.setShort(StatementConstant.SC_3.get(), (short)i);
                    n=pstInsCollab.executeUpdate();
                    if(n>0){
                        System.out.println("Se inserto un colaborativo");
                        rs = pstInsCollab.getGeneratedKeys();
                        if (rs.next()) {
                           docCollabId = rs.getInt(1);
                           System.out.println("valor de docCollabId generada automáticamente = " + docCollabId);
                           docCollabItem.setDocumentCollabItemId(docCollabId);
                        }
                        rs.close();
                    }
                   
                   
               }else{
                   pstUpdCollab.setShort(StatementConstant.SC_1.get(), (short)i );
                   pstUpdCollab.setInt(StatementConstant.SC_2.get(), docCollabItem.getDocumentCollabItemId() );
                   n=pstUpdCollab.executeUpdate();
                   if( n>0 ){
                       System.out.println("Se actualizo el documento con docCollabId="+ docCollabItem.getDocumentCollabItemId() );
                   }
                                                         
               }
               lstIdOrigs.add(docCollabItem.getDocumentCollabItemId());
                
                                                                
            }
            
 
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }finally{
            try{
                if( pstInsCollab!=null  ){
                    pstInsCollab.close();
                }
                
                if( pstUpdCollab!=null ){
                    pstUpdCollab.close();
                }
                
                if( conn!=null ){
                    conn.close();
                }
                
            }catch(Exception e){
                Utilerias.logger(getClass()).error(e);
            }
            
            
            
            
        }
    }
    
    public List<DocumentBO> getDocumentBoCollabByDocument(int documentId) {
        String sqlDocument = null;
        List<DocumentBO> lstDocColl = new ArrayList<>();
        DocumentDAO dao = new DocumentDAO();
        
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement stDocument = conn.createStatement();) {

            sqlDocument = "SELECT DISTINCT(C.DOCUMENT_ID) as ID_DOCCOLLAB "
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_COLLAB_ITEM C "
                    + " INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_VERSION_NEW V ON (V.DOC_VERSION_ID=C.ITEM_DOC_VERSION_ID) "
                    + " INNER JOIN " + GlobalDefines.DB_SCHEMA + "DOCUMENT_NEW D ON (D.DOCUMENT_ID=V.DOCUMENT_ID) "
                    + "	WHERE D.PUBLISH_NAME IS NOT NULL AND D.DOCUMENT_ID = " + documentId;

            ResultSet rsDocument = stDocument.executeQuery(sqlDocument);
            while (rsDocument.next()) {
                DocumentBO document = dao.getDocument( rsDocument.getInt("ID_DOCCOLLAB"), -1 , false );
                lstDocColl.add(document);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            lstDocColl = new ArrayList<>();
        }
        return lstDocColl;
    }
    
    
    
}
