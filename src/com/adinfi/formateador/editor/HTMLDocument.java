package com.adinfi.formateador.editor;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.EventBO;
import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.OfficeBO;
import com.adinfi.formateador.bos.RelatedDocsBO;
import com.adinfi.formateador.bos.SectionColModBO;
import com.adinfi.formateador.bos.SectionInfoBO;
import com.adinfi.formateador.bos.SemanarioDocsBO;
import com.adinfi.formateador.bos.SendPublishAuthorsBO;
import com.adinfi.formateador.bos.SendPublishBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.seguridad.Usuario;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.HeaderColModDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.OfficeDAO;
import com.adinfi.formateador.dao.RelatedDocsDAO;
import com.adinfi.formateador.dao.SectionColModDAO;
import com.adinfi.formateador.dao.SendPublishAuthorsDAO;
import com.adinfi.formateador.dao.SendPublishDAO;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.agendas.AgendaWS;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfUsuarioEmisora;
import com.adinfi.ws.ArrayOfUsuarioSector;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.UsuarioEmisora;
import com.adinfi.ws.UsuarioSector;
import com.adinfi.ws.UsuarioSeguimiento;
import com.darkprograms.speech.synthesiser.Synthesiser;
import com.itextpdf.text.DocumentException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

/**
 *
 * @author Desarrollador
 */
public class HTMLDocument {

    String fileNamePDF = null;
    DocumentBO docTemp = null;
    private List<String> sshFiles;
    boolean proxy = Boolean.parseBoolean(Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG));
    Utilerias.ALLOWED_KEY type_view;
    private final int TITULO_COD = 0;
    private final int TIPO_DOC = 1;
    private boolean isCalendar;
    private DocumentBO docBODisplosure;
    private Hashtable<Integer, ModuleSectionBO> moduleSectionResp;
    private boolean messageSent = false;
    private int iteracion = 0;

    public static void main(String[] args) {
        HTMLDocument html = new HTMLDocument();
        html.createHTMLSemanario(166, null, null, null);

    }

    public String createHTMLSemanario(int documentId, Map<Integer, String> urlDoc, DocumentBO docObj, String pdfGen) {
        String fileName = null;

        try {
            Utilerias.pasarGarbageCollector();
            CollaborativesDAO collabDAO = new CollaborativesDAO();
            DocumentCollabBO collabDocBO = collabDAO.getDocument(documentId);
            if (collabDocBO == null) {
                return null;
            }

            sshFiles = new ArrayList<>();

            SubjectBO subjectName = null;

            SubjectDAO dao = new SubjectDAO();
            List<SubjectBO> list = dao.get(null);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getIdSubject() == collabDocBO.getIdSubject()) {
                    subjectName = list.get(i);
                    break;
                }
            }

            List<DocumentCollabItemBO> lstDocs = collabDocBO.getLstDocumentCollab();
            if(docObj.getLstDocumentCollab() != null && docObj.getLstDocumentCollab().size() > 0){
                lstDocs = docObj.getLstDocumentCollab();
            }
            
            DocumentDAO documentDAO = new DocumentDAO();

            String htmlDir = Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR);
            String dirGenImages = new File(htmlDir).getParentFile().getAbsolutePath() + "/gen_images/";

            fileName = docObj != null && docObj.getPublishName() != null
                    ? Utilerias.getGeneratedHTMLName(docObj.getPublishName())
                    : Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.SEMANARIO);

            String fileBase = Utilerias.getFilePath(Utilerias.PATH_TYPE.INDEX_HTML_PATH);
            String allHTML = readBaseDocHTML(fileBase);
            StringBuilder strBuff = new StringBuilder("");

            strBuff.append("<div class=\"main-container\">\n");
            strBuff.append("<div class='wrap'>\n");
            strBuff.append("<div class='content'>\n");
            strBuff.append("<div class='card-display'>\n");
            strBuff.append("<div class=\"filtros\">\n");

            String fileNameDoc = null;
            String fileTmp = null;
            ObjectInfoBO objectBO = null;
            ObjectInfoBO imageBO = null;
            DocumentBO docBO = null;
            List<MarketBO> markets = new ArrayList<>();
            Map<Integer, MarketBO> map_markets = new TreeMap<>();
            MarketBO ma = null;
            MarketBO maTmp;
            DocumentTypeBO dt;
            String fileImage;
            List<SubjectBO> lstSubject = new ArrayList<>();
            if (lstDocs != null) {
                for (DocumentCollabItemBO docCollabBO : lstDocs) {

                    docBO = documentDAO.getDocument(docCollabBO.getItemDocumentId(), docCollabBO.getItemVersion(),false);
                    if (docBO == null) {
                        continue;
                    }
                    ma = docBO.getMarketBO();
                    if (ma != null) {
                        maTmp = map_markets.get(ma.getIdMarket());
                        if (maTmp == null) {
                            markets.add(ma);
                            map_markets.put(ma.getIdMarket(), ma);
                        }
                    }

                    dt = docBO.getTipoBO();

                    objectBO = this.getObjectWithText(docBO);
                    imageBO = docBO.getFirstImage();
                    fileImage = null;
                    if (imageBO != null) {
                        try {
                            fileImage = dirGenImages + "sem_img_" + imageBO.getObjectId() + "_" + new Date().getTime() + ".png";
                            File f = new File(fileImage);
                            ImageIO.write(imageBO.getImage(), "PNG", f);
                            addSshFile(fileImage);
                        } catch (Exception e) {
                            Utilerias.logger(getClass()).error(e);
                        }
                    }

                    fileTmp = docBO.getFileName();
                    fileTmp = fileTmp.replaceAll(" ", "_");

                    //  fileNameDoc = GlobalDefines.DIR_HTML + docBO.getDocumentId() + "_" + fileTmp + ".htm";
                    fileNameDoc = htmlDir + fileTmp + ".htm";

                    strBuff.append("<div class=\"").append(ma.getName()).append("\"> <span class=\"card\">  ");
                    if (fileImage != null) {
                        strBuff.append(" <img alt=\"\" width='90%' height='30%'  src='IMG/" + Utilerias.getFileName(fileImage) + "'  ></img> ");
                    }
                    strBuff.append(" <h2>" + dt.getName() + "</h2> ");

                    strBuff.append(" <p>");
                    if (docBO.getSubjectBO() != null && docBO.getSubjectBO().getName() != null) {
                        strBuff.append(docBO.getSubjectBO().getName());
                        strBuff.append(":");
                    }

                    strBuff.append(docBO.getDocumentName());
                    strBuff.append(".");
                    strBuff.append(" </p>");

                    strBuff.append(" <a href='" + getUrlDocumento(docBO.getDocumentId(), urlDoc) + "' target='_blank' class=\"btn\">ir a documento</a> </span> </div>");
                    //strBuff.append("</div>\n");

                    SubjectBO subjectNameItem = null;

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getIdSubject() == docBO.getIdSubject()) {
                            subjectNameItem = list.get(i);
                            break;
                        }
                    }
                    
                    if (subjectNameItem != null && subjectNameItem.isIssuing()) {
                        boolean agregar=true;
                        for (SubjectBO sub : lstSubject) {
                            if(sub.getIdSubject() == subjectNameItem.getIdSubject()){
                                agregar = false;
                               break;
                            }
                        }
                        
                        if(agregar)
                            lstSubject.add(subjectNameItem);
                    }
                }

            }

            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            //   strBuff.append("</html>");

            java.util.Date today = new java.util.Date();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(today);

            String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            int year = gc.get(GregorianCalendar.YEAR);
            int month = gc.get(GregorianCalendar.MONTH);
            int day = gc.get(GregorianCalendar.DAY_OF_MONTH);
            String fecha = "" + year + " " + months[month] + " " + day;

            String marketsHTML = "";
            if (markets.size() > 0) {
                for (MarketBO m : markets) {
                    marketsHTML += " <li><a href='#'>" + m.getName() + "</a></li> ";
                }
            }

            allHTML = allHTML.replace("@FECHA", fecha);
            allHTML = allHTML.replace("@MARKETS", marketsHTML);
            allHTML = allHTML.replace("@BODY", strBuff.toString());
            allHTML = allHTML.replace("@DIRECTORY", createDirectory());
            allHTML = allHTML.replace("@NombreAutor", InstanceContext.getInstance().getUsuario().getNombre());
            
            if (pdfGen == null) {
                allHTML = allHTML.replace("@href", " style=\"cursor:pointer;\"  onclick=\"javascript:alert('No es posible generar Documento PDF para este tipo de Documento')\"");
            } else {
                allHTML = allHTML.replace("@PDF", pdfGen);
                allHTML = allHTML.replace("@href", "href=" + pdfGen + "");
            }

            DisclosureDocument disclosure = new DisclosureDocument(sshFiles);
            allHTML = allHTML.replace("@DISCLOSURE", disclosure.getDisclosure(collabDocBO.getIdDocType(), subjectName, lstSubject));

//            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName)))) {
//                bwr.write(allHTML);
//                //flush the stream
//                bwr.flush();
//            }
            try (BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
                bwr.write(allHTML);
                //flush the stream
                bwr.flush();
            }

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return fileName;
    }

    private String getUrlDocumento(Integer idDocumento, Map<Integer, String> urlDoc) {
        if (urlDoc == null) {
            return "";
        }

        String url = urlDoc.get(idDocumento);

        if (url == null || url.isEmpty()) {
            return "";
        }

        return url;
    }

    public String createHTMLSemanario1(int semanarioId) {
        String fileName = null;

        try {

            DocumentDAO documentDAO = new DocumentDAO();
            SemanarioDocsBO semDocsBO = documentDAO.getSemanarioDocs(semanarioId);
            String htmlDir = Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR);
            fileName = htmlDir + "SEMANARIO_" + semanarioId + ".htm";
            //fileName = GlobalDefines.DIR_HTML + "SEMANARIO_" + semanarioId + ".htm";
            // step 1
            //Document document = new Document(PageSize.LETTER  );
            String indexHtmlDir = Utilerias.getFilePath(Utilerias.PATH_TYPE.INDEX_HTML_PATH);
            //String fileBase = GlobalDefines.DIR_HTML + "/" + GlobalDefines.INDEX_BASE_HTML;
            String fileBase = htmlDir + "/" + indexHtmlDir;
            // step 1
            //Document document = new Document(PageSize.LETTER  );
            String allHTML = readBaseDocHTML(fileBase);

            StringBuilder strBuff = new StringBuilder("");

            strBuff.append("<div class=\"main-container\">\n");
            strBuff.append("<div class='wrap'>\n");
            strBuff.append("<div class='content'>\n");
            strBuff.append("<div class='card-display'>\n");

            String fileNameDoc = null;
            String fileTmp = null;
            ObjectInfoBO objectBO = null;
            if (semDocsBO != null && semDocsBO.getDocumentsBO() != null) {
                for (DocumentBO docBO : semDocsBO.getDocumentsBO()) {
                    objectBO = this.getObjectWithText(docBO);
                    fileTmp = docBO.getFileName();
                    fileTmp = fileTmp.replaceAll(" ", "_");

                    //  fileNameDoc = GlobalDefines.DIR_HTML + docBO.getDocumentId() + "_" + fileTmp + ".htm";
                    fileNameDoc = htmlDir + fileTmp + ".htm";

                    strBuff.append(" <div class=\"card\">  ");
                    strBuff.append(" <h2>Econom&iacute;a</h2> ");
                    if (objectBO != null) {
                        // strBuff.append(" <p>"+ objectBO.getData() +"</p> ");

                        if (objectBO != null && objectBO.getData() != null) {
                            if (objectBO.getData().length() <= 50) {

                            } else {
                                strBuff.append(" <p>" + objectBO.getData() + " ...</p> ");
                            }
                        }
                        strBuff.append(" <a href='file:///" + fileNameDoc + "' target='_blank' class=\"btn\">ir a documento</a> </div>  ");
                    }
                }

            }

            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            strBuff.append("</div>\n");
            //   strBuff.append("</html>");

            allHTML = allHTML.replace("@BODY", strBuff.toString());
            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName)))) {
                bwr.write(allHTML);
                //flush the stream
                bwr.flush();
            }

        } catch (IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return fileName;
    }

    public ObjectInfoBO getObjectWithText(DocumentBO docBO) {

        if (docBO == null) {
            return null;
        }
        if (docBO.getNumHojas() <= 0) {
            return null;
        }
        int numHojas = docBO.getNumHojas();

        HojaBO hojaBO = null;
        List<ModuleBO> lstModules = null;
        List<ModuleSectionBO> lstSections = null;
        for (int i = 0; i < numHojas; i++) {
            hojaBO = docBO.getMapHojas().get((i + 1));
            if (hojaBO == null) {
                continue;
            }
            lstModules = hojaBO.getLstModules();
            if (lstModules == null || lstModules.size() <= 0) {
                continue;
            }

            for (ModuleBO moduleBO : lstModules) {
                if (moduleBO.isHasText() == false) {
                    continue;
                }
                //lstSections = moduleBO.getRootSection().getChildSectionsAsList();
                lstSections = moduleBO.getLstSectionsText();
                if (lstSections == null || lstSections.size() <= 0) {
                    continue;
                }
                for (ModuleSectionBO sectionBO : lstSections) {
                    if (sectionBO.getLstObjects() == null || sectionBO.getLstObjects().size() <= 0) {
                        continue;
                    }

                    for (ObjectInfoBO objectBO : sectionBO.getLstObjects()) {
                        if (objectBO.getObjType() != GlobalDefines.OBJ_TYPE_TEXT) {

                            if (sectionBO.getChildSectionsAsList() != null) {
                                for (ModuleSectionBO sectionBOChld : sectionBO.getChildSectionsAsList()) {
                                    if (sectionBOChld.getLstObjects() == null || sectionBOChld.getLstObjects().size() <= 0) {
                                        continue;
                                    }

                                    for (ObjectInfoBO objectBOTmp : sectionBOChld.getLstObjects()) {
                                        if (objectBOTmp.getObjType() != GlobalDefines.OBJ_TYPE_TEXT) {
                                            continue;
                                        }

                                        if (objectBOTmp.getData() == null || objectBOTmp.getPlain_text().isEmpty() == true) {
                                            continue;
                                        }

                                        return objectBOTmp;

                                    }

                                }
                            }

                        }

                        if (objectBO.getPlain_text() == null || objectBO.getPlain_text().isEmpty()) {
                            continue;
                        }

                        return objectBO;
                    }

                }

            }

        }

        return null;

    }

    public List<String> getTextFromDocument(DocumentBO docBO) {

        List<String> textInDoc = new ArrayList<>();

        if (docBO == null) {
            return null;
        }
        if (docBO.getNumHojas() <= 0) {
            return null;
        }
        
        if( GlobalDefines.COLLAB_TYPE_DOC.equals( docBO.getCollaborativeType() ) ){
            
            String lstDocId = null;
            for(DocumentCollabItemBO coll : docBO.getLstDocumentCollab()){
                if(lstDocId == null){
                    lstDocId = String.valueOf( coll.getItemDocumentId() );
                    continue;
                }
                lstDocId += "," + coll.getItemDocumentId();
            }
            
            if(lstDocId == null)
                return textInDoc;
            
            DocumentDAO docDAO = new DocumentDAO();
            textInDoc = docDAO.getDocumentCollabContentText(lstDocId);
            
        } else {

            int numHojas = docBO.getNumHojas();

            HojaBO hojaBO = null;
            List<ModuleBO> lstModules = null;
            List<ModuleSectionBO> lstSections = null;
            for (int i = 0; i < numHojas; i++) {
                hojaBO = docBO.getMapHojas().get((i + 1));
                if (hojaBO == null) {
                    continue;
                }
                lstModules = hojaBO.getLstModules();
                if (lstModules == null || lstModules.size() <= 0) {
                    return null;
                }

                for (ModuleBO modules : lstModules) {
                    lstSections = modules.getLstSectionsTextandBullet();
                    if (lstSections != null && lstSections.isEmpty() == false) {
                        for (ModuleSectionBO sectionBO : lstSections) {
                            List<ObjectInfoBO> objectsInfo = sectionBO.getLstObjects();
                            if (objectsInfo != null && objectsInfo.isEmpty() == false) {
                                for (ObjectInfoBO infoBO : objectsInfo) {
                                    textInDoc.add(infoBO.getData());
                                }
                            }
                        }
                    }

                    int c = 6;
                }
            }
        
        }

        return textInDoc;
    }

    private void addSshFile(String filePath) {
        if (filePath.isEmpty()) {
            return;
        }

        if (sshFiles == null) {
            sshFiles = new ArrayList<>();
        }

        sshFiles.add(filePath);
    }

    public List<String> getSshFiles() {
        return sshFiles;
    }

    public DocumentBO getDocBODisplosure() {
        return docBODisplosure;
    }

    public void setDocBODisplosure(DocumentBO docBODisplosure) {
        this.docBODisplosure = docBODisplosure;
    }

    public String createHTMLDocument(DocumentBO docBO, Utilerias.ALLOWED_KEY type, String pdfName, String pdfPath) throws IOException,
            DocumentException {

        if (docBO == null) {
            return null;
        }

        sshFiles = new ArrayList<>();
        type_view = type;

        String outputFileName = docBO.getPublishName() == null
                ? Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.HTML)
                : Utilerias.getGeneratedHTMLName(docBO.getPublishName());
        String documentBasePath = Utilerias.getFilePath(Utilerias.PATH_TYPE.DOCUMENT_BASE_PATH);
        String fileName = outputFileName;
        docTemp = docBO;
        String pdfGen = pdfName;
        fileNamePDF = pdfPath;

        String allHTML = readBaseDocHTML(documentBasePath);
        SubjectBO subjectName = null;

        SubjectDAO dao = new SubjectDAO();
        List<SubjectBO> list = dao.get(null);

        if (docBODisplosure == null) {
            docBODisplosure = docBO;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdSubject() == docBODisplosure.getIdSubject()) {
                subjectName = list.get(i);
                break;
            }
        }

        List<String> titulos = Utilerias.getTitulo(docBO);
        StringBuffer strBuff = new StringBuffer("");
        String docTitulo = "<h1>" + titulos.get(TIPO_DOC) + "</h1>";
        String docSubTit = "<h2>" + titulos.get(TITULO_COD) + "</h2>";
        strBuff.append(docTitulo);
        strBuff.append(docSubTit);
        isCalendar = false;

        if (docBO.getMapHojas() == null || docBO.getMapHojas().size() <= 0) {
            return null;
        }
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();

        Integer numHoja = null;
        HojaBO hojaBO = null;
        List<ModuleBO> lstModules = null;
        int orden = 0;
        TemplateDAO tmpDAO = new TemplateDAO();
        iteracion = 0;
        boolean calHojaUno = false;
        while (it.hasNext()) {
            Utilerias.pasarGarbageCollector();
            if (docBO.getOrden() != null && docBO.getOrden().size() > 0) {
                numHoja = docBO.getOrden().get(orden);
                orden++;
                it.next();
                if (orden >= docBO.getOrden().size()) {
                    while (it.hasNext()) {
                        it.next();
                    }
                }
            } else {
                numHoja = (Integer) it.next();
            }
            hojaBO = docBO.getMapHojas().get(numHoja);
            if (hojaBO == null) {
                continue;
            }

            lstModules = hojaBO.getLstModules();
            if (lstModules != null) {
                for (ModuleBO moduleBO : lstModules) {
                    moduleSectionResp = tmpDAO.getModulesSectionsByModuleId(moduleBO.getModuleId());
                    if (docTemp.isCollaborative()) {
                        if (moduleBO.isHeaderOrSection()) {
                            try {
                                moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().get(0).setHeader(true);
                            } catch (Exception e) {
                                Utilerias.logger(getClass()).info(e);
                            }
                        }

                        if (moduleBO.isSection()) {
                            try {
                                moduleBO.getRootSection().getChildSectionsAsList().get(0).getChildSectionsAsList().get(0).getLstObjects().get(0).setSection(true);
                            } catch (Exception e) {
                                Utilerias.logger(getClass()).info(e);
                            }
                        }
                        /*if (moduleBO.getIdHeader() > 0) {
                            HeaderColModBO headerBO = new HeaderColModDAO().getHeader(moduleBO.getIdHeader());
                            strBuff.append("<h2>");
                            strBuff.append(headerBO.getName());
                            strBuff.append("</h2>");
                        }

                        if (moduleBO.getIdSection() > 0) {
                            SectionColModBO sectionBO = new SectionColModDAO().getSection(moduleBO.getIdSection());
                            strBuff.append("<div class='sectionModCol'>");
                            strBuff.append(sectionBO.getName());
                            strBuff.append("</div>");
                        }*/
                    }
                    addLayout(strBuff, moduleBO.getRootSection(), null);
                    // para delimitar plantillas
                    strBuff.append("<div class=\"mobile-hidden\" id=\"corte\" style=\"  width: 100%; height: 3px; background-color: #FFF; float: left;\"></div>");

                }
            }
            if (iteracion == 0 && isCalendar) {
                calHojaUno = true;
            }
            iteracion++;
        }

        Date today = new Date();
        String fechaHoy = Utilerias.formatDate(today, "dd/MM/yyyy");
        DisclosureDocument disclosure = new DisclosureDocument(sshFiles);
        if (calHojaUno) {
            strBuff = new StringBuffer(strBuff.toString().replaceFirst(docTitulo, " "));
            strBuff = new StringBuffer(strBuff.toString().replaceFirst(docSubTit, " "));
        }
        allHTML = allHTML.replace("@BODY", strBuff.toString());
        allHTML = allHTML.replace("@RELFILE", displayRelationalFiles(docBO));
        allHTML = getAutor(docBO, allHTML);
        allHTML = allHTML.replace("@DISCLOSURE", disclosure.getDisclosure(docBODisplosure.getIdDocType(), subjectName, null));
        allHTML = allHTML.replace("@FECHA_HOY", fechaHoy);
        allHTML = allHTML.replace("@SCRIPT_ADICIONAL", "<script src=\"../../../../../../../js/calend.js\"></script>");
        allHTML = allHTML.replace("@DIRECTORY", createDirectory());
        
        if (pdfGen == null) {
            allHTML = allHTML.replace("@href", " style=\"cursor:pointer;\"  onclick=\"javascript:alert('No es posible generar Documento PDF para este tipo de Documento')\"");
        } else {
            allHTML = allHTML.replace("@PDF", pdfGen);
            allHTML = allHTML.replace("@href", "href=" + pdfGen + "");
        }
        allHTML = allHTML.replace("@TAB_BOTTOM", "");
        try (BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            bwr.write(allHTML);
            //flush the stream
            bwr.flush();
        }
        addSshFile(fileName);
        return fileName;
    }

    private String getAutor(DocumentBO docBO, String allHTML) {
        SendPublishDAO spDAO = new SendPublishDAO();
        SendPublishAuthorsDAO spaDAO = new SendPublishAuthorsDAO();
        List<SendPublishBO> spBOLst = spDAO.get(docBO.getDocumentId());
        if (spBOLst == null || spBOLst.size() <= 0 || docBO.getDocumentId() == 0) {
            String retNamesMailMobile = "<div class=\"nombre\">" + InstanceContext.getInstance().getUsuario().getNombre() + "</div>";
            retNamesMailMobile += "<div class=\"email\">" + InstanceContext.getInstance().getUsuario().getCorreo() + " x" + InstanceContext.getInstance().getUsuario().getExtension() + "</div>";

            allHTML = allHTML.replace("@autorNombreEmailMobile", retNamesMailMobile);
            allHTML = allHTML.replace("@NombreAutor", InstanceContext.getInstance().getUsuario().getNombre());
            allHTML = allHTML.replace("@emailvector", InstanceContext.getInstance().getUsuario().getCorreo() + " x" + InstanceContext.getInstance().getUsuario().getExtension());

            return allHTML;
        }

        SendPublishBO spBO = spBOLst.get(0);
        List<SendPublishAuthorsBO> spaBOLst = spaDAO.get(spBO.getIdDocument_send());
        if (spaBOLst == null || spaBOLst.size() <= 0) {
            allHTML = allHTML.replace("@autorNombreEmailMobile", "");
            allHTML = allHTML.replace("@NombreAutor", "");
            allHTML = allHTML.replace("@emailvector", "");
            return allHTML;
        }

        List<com.adinfi.formateador.bos.seguridad.Usuario> list = new ArrayList<>();
        list = UtileriasWS.getAllUsers();
        List<com.adinfi.formateador.bos.seguridad.Usuario> result = new ArrayList<>();
        for (SendPublishAuthorsBO autSend : spaBOLst) {
            for (Usuario us : list) {
                if (us.getUsuarioId() == autSend.getId_author()) {
                    result.add(us);
                    break;
                }
            }
        }

        if (result.size() <= 0) {
            allHTML = allHTML.replace("@autorNombreEmailMobile", "");
            allHTML = allHTML.replace("@NombreAutor", "");
            allHTML = allHTML.replace("@emailvector", "");
            return allHTML;
        }

        Collections.sort(result, new Comparator<com.adinfi.formateador.bos.seguridad.Usuario>() {

            @Override
            public int compare(Usuario o1, Usuario o2) {
                int ordenP1 = o1.getOrdenDir();
                int ordenP2 = o2.getOrdenDir();

                if (ordenP1 == 0) {
                    ordenP1 = Integer.MAX_VALUE;
                }
                if (ordenP2 == 0) {
                    ordenP2 = Integer.MAX_VALUE;
                }
                
                return new Integer( ordenP1 ).compareTo(new Integer( ordenP2 ));
            }
        });
        
        String retNamesMailMobile = "";
        String retValNames = "<ol style=\"list-style-type: none;\">";
        String retValMails = "<ol style=\"list-style-type: none;\">";
        for (Usuario user : result) {
            retNamesMailMobile += "<div class=\"nombre\">" + user.getNombre() + "</div>";
            retNamesMailMobile += "<div class=\"email\">" + user.getCorreo() + " x" + user.getExtension() + "</div>";

            retValNames += "<li>" + user.getNombre() + "</li>";
            retValMails += "<li>" + user.getCorreo() + " x" + user.getExtension() + "</li>";
        }
        retValNames += "</ol>";
        retValMails += "</ol>";

        allHTML = allHTML.replace("@autorNombreEmailMobile", retNamesMailMobile);
        allHTML = allHTML.replace("@NombreAutor", retValNames);
        allHTML = allHTML.replace("@emailvector", retValMails);
        return allHTML;
    }

    private String displayRelationalFiles(DocumentBO docBO) {
        String retVal = "";

        RelatedDocsDAO rDAO = new RelatedDocsDAO();
        List<RelatedDocsBO> lstRBO = rDAO.get(docBO.getDocumentId());

        if (lstRBO == null || lstRBO.size() <= 0) {
            return retVal;
        }

        retVal = "<h3>Documentos Relacionados</h3><ul>";
        for (RelatedDocsBO rel : lstRBO) {
            retVal += "<li><a target='_blank' href='" + rel.getUrl() + "'>" + rel.getDocument_name() + "</a></li>";
        }

        retVal += "</ul>";

        return retVal;
    }

    private DocumentTypeBO getIdMiVectorByDocumentType() {
        DocumentTypeBO docTypeBO = null;

        try {
            DocumentTypeDAO dDAO = new DocumentTypeDAO();
            List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);

            for (DocumentTypeBO d : dBO) {
                if (d.getIddocument_type() == docTemp.getIdDocType()) {
                    docTypeBO = d;
                    break;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

        return docTypeBO;
    }

    private String displayCalendar(String contType) {
        StringBuffer strBuff = new StringBuffer("");
        final String[] diasSem = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        List<EventBO> lEvent = new ArrayList<>();
        final List<String> prodid = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.ICS_PRODID) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.ICS_PRODID).get(Utilerias.ALLOWED_KEY.ICS_PRODID.toString()) : null;
        final List<String> calname = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.ICS_CALNAME) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.ICS_CALNAME).get(Utilerias.ALLOWED_KEY.ICS_CALNAME.toString()) : null;

        DocumentTypeBO docTypeBO = getIdMiVectorByDocumentType();

        try {
            //new PdfDocument().createPdfDocument(docTemp);
            switch (contType) {
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR:
                    isCalendar = true;
                    final String[] mesDesc = {"Ene.", "Feb.", "Mar.", "Abr.", "May.", "Jun.", "Jul.", "Ago.", "Sep.", "Oct.", "Nov.", "Dic."};
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sdfDay = new SimpleDateFormat("d");
                    strBuff = new StringBuffer("");

                    if (iteracion == 0) {
                        strBuff.append("<h1>").append(docTypeBO.getName()).append("</h1>");
                    }

                    strBuff.append("<div class=\"agenda-add\">");
                    strBuff.append("<div class=\"agenda-add-item\">Agregar todos los eventos</div>");
                    strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"ical\">iCalendar</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"outlook\">Outlook</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"google-cal\">Google Calendar</a> </div>");
                    strBuff.append("</div>");
                    strBuff.append("<div class=\"eventos-por-dia\">");
                    strBuff.append("Eventos por día");
                    strBuff.append("</div>");

                    strBuff.append("<div class=\"agenda-economica\">");
                    strBuff.append("<div class=\"agenda-header\">");
                    strBuff.append("<div class=\"column-1\">Fecha</div>");
                    strBuff.append("<div class=\"column-2\">Hora</div>");
                    strBuff.append("<div class=\"column-3\">Lugar</div>");
                    strBuff.append("<div class=\"column-4\">Indicador</div>");
                    strBuff.append("<div class=\"column-5\">Periodo</div>");
                    strBuff.append("<div class=\"column-6\">Estimación Vector</div>");
                    strBuff.append("<div class=\"column-7\">Encuesta Bloomberg</div>");
                    strBuff.append("<div class=\"column-8\">Oficial</div>");
                    strBuff.append("<div class=\"column-9\">Cifra Anterior</div></div>");
                    strBuff.append("<div class=\"agenda-body\">");

                    int idMiVector = 0;
                    try {
                        idMiVector = Integer.parseInt(docTypeBO.getIddocument_type_vector());
                    } catch (Exception e) {
                        idMiVector = 0;
                    }

                    lEvent = AgendaWS.getEventLst(idMiVector);
                    StringBuilder strBuffScript = new StringBuilder();
                    if (lEvent != null) {
                        strBuffScript.append("<script>");
                        strBuffScript.append("var myJSONCal = {");
                        strBuffScript.append("'PRODID': '");
                        strBuffScript.append(prodid);
                        strBuffScript.append("',");
                        strBuffScript.append("'CALNAME': '");
                        strBuffScript.append(calname);
                        strBuffScript.append("',");
                        strBuffScript.append("'EVENTS': [");
                        int cont = 0;
                        for (EventBO event : lEvent) {
                            cont++;
                            strBuff.append("<div class=\"row-").append(cont).append("\">");

                            strBuff.append("<div class=\"column-1\"><div class=\"name\">Fecha</div><div class=\"value\">");
                            strBuff.append(mesDesc[event.getFechaCompleta().get(Calendar.MONTH)]);
                            strBuff.append(" ");
                            strBuff.append(event.getFechaCompleta().get(Calendar.DAY_OF_MONTH));
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-2\"><div class=\"name\">Hora</div><div class=\"value\">");
                            strBuff.append(sdf.format(new Date(event.getFechaCompleta().getTimeInMillis())));
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-3\"><div class=\"name\">Lugar</div><div class=\"value\">");
                            strBuff.append(event.getLugar());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-4\"><div class=\"name\">Indicador</div><div class=\"value\">");
                            strBuff.append(event.getEventoDesc());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"hora-mobile\"><div class=\"name\">Hora</div><div class=\"value\">");
                            strBuff.append(sdf.format(new Date(event.getFechaCompleta().getTimeInMillis())));
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"lugar-mobile\"><div class=\"name\">Lugar</div><div class=\"value\">");
                            strBuff.append(event.getLugar());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-5\"><div class=\"name\">Periodo</div><div class=\"value\">");
                            strBuff.append(event.getPeriodo());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-6\"><div class=\"name\">Estimación Vector</div><div class=\"value\">");
                            strBuff.append(event.getEstimacionVector());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-7\"><div class=\"name\">Encuesta Bloomberg</div><div class=\"value\">");
                            strBuff.append(event.getEstimacionBloomberg());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-8\"><div class=\"name\">Oficial</div><div class=\"value\">");
                            strBuff.append(event.getCifraOficial());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"column-9\"><div class=\"name\">Cifra Anterior</div><div class=\"value\">");
                            strBuff.append(event.getCifraOficialAnt());
                            strBuff.append("</div></div>");

                            strBuff.append("<div class=\"agenda-day-add\">");
                            strBuff.append("<div class=\"agenda-add-item\">Agregar evento</div>");
                            strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(cont - 1).append("\");' class=\"ical\">iCalendar</a> </div>");
                            //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append( cont-1 ).append("\");' class=\"outlook\">Outlook</a> </div>");
                            //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append( cont-1 ).append("\");' class=\"google-cal\">Google Calendar</a> </div>");
                            strBuff.append("</div></div>");

                            strBuffScript.append("{'DTSTAMP': '");
                            Timestamp dtstamp = new Timestamp(event.getFechaCompleta().getTimeInMillis());
                            String fehaICS = String.valueOf(dtstamp.toLocalDateTime());
                            strBuffScript.append(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00Z"));
                            strBuffScript.append("', 'UID': '");
                            strBuffScript.append("me@google.com");
                            strBuffScript.append("', 'DTSTART': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'DTEND': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'SUMMARY': '");
                            strBuffScript.append(event.getSubject());
                            strBuffScript.append("', 'DESCRIPTION': '");
                            strBuffScript.append(event.getEventoDesc());
                            strBuffScript.append("', 'LOCATION': '");
                            strBuffScript.append(event.getIcsDescripction());
                            if (cont == lEvent.size()) {
                                strBuffScript.append("'}");
                            } else {
                                strBuffScript.append("'},");
                            }
                        }
                        strBuffScript.append("]}\n");
                        strBuffScript.append("var urlPDF = '");
                        strBuffScript.append(fileNamePDF.replace("\\", "\\\\"));
                        strBuffScript.append("';\n");
                        strBuffScript.append("</script>");
                    }

                    strBuff.append("</div></div><div class=\"description\">*** Muy importante ** Importante * Poco importante</div>");
                    /*strBuff.append("</td>");
                     strBuff.append("</tr>");
                     strBuff.append("</table>");*/

                    strBuff.append(strBuffScript.toString());

                    break;
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_WEEK:
                    isCalendar = true;
                    strBuff = new StringBuffer("");

                    if (iteracion == 0) {
                        strBuff.append("<h1>").append(docTypeBO.getName()).append("</h1>");
                    }

                    strBuff.append("<div class=\"agenda-add\">");
                    strBuff.append("<div class=\"agenda-add-item\">Agregar todos los eventos</div>");
                    strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"ical\">iCalendar</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"outlook\">Outlook</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"google-cal\">Google Calendar</a> </div>");
                    strBuff.append("</div>");
                    strBuff.append("<div class=\"eventos-por-dia\">");
                    strBuff.append("Eventos por día");
                    strBuff.append("</div>");

                    idMiVector = 0;
                    try {
                        idMiVector = Integer.parseInt(docTypeBO.getIddocument_type_vector());
                    } catch (Exception e) {
                        idMiVector = 0;
                    }

                    lEvent = AgendaWS.getEventLstWeek(idMiVector);
                    Calendar fi = AgendaWS.fechaIni;
                    Calendar ff = AgendaWS.fechaFin;
                    strBuffScript = new StringBuilder();
                    if (lEvent != null) {

                        strBuff.append("<div class=\"agenda-week\">");
                        strBuffScript.append("<script>");
                        strBuffScript.append("var myJSONCal = {");
                        strBuffScript.append("'PRODID': '");
                        strBuffScript.append(prodid);
                        strBuffScript.append("',");
                        strBuffScript.append("'CALNAME': '");
                        strBuffScript.append(calname);
                        strBuffScript.append("',");
                        strBuffScript.append("'EVENTS': [");
                        String diaActual = "";
                        Map<String, Map> lEvenMaps = new HashMap<>();
                        Map<String, String> attEvnt = new HashMap<>();
                        int contCol = 0;
                        for (EventBO event : lEvent) {

                            attEvnt = lEvenMaps.get(event.getFecha());
                            if (attEvnt == null) {
                                attEvnt = new HashMap<>();
                                diaActual = event.getFecha();//event.getFechaCompleta().get(Calendar.DAY_OF_MONTH);
                                attEvnt.put("DIA_SEM", diasSem[event.getFechaCompleta().get(Calendar.DAY_OF_WEEK) - 1]);
                                attEvnt.put("DIA", Integer.toString(event.getFechaCompleta().get(Calendar.DAY_OF_MONTH)));
                                attEvnt.put("DESC_EVEN", "<li>" + (event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()) + "</li>");
                                attEvnt.put("ID", String.valueOf(contCol));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            } else {
                                //attEvnt = lEvenMaps.get(event.getFecha());
                                lEvenMaps.remove(event.getFecha());
                                attEvnt.replace("DESC_EVEN", ("<li>").concat(event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()).concat("</li>").concat(attEvnt.get("DESC_EVEN")));
                                attEvnt.replace("ID", attEvnt.get("ID").concat(",").concat(String.valueOf(contCol)));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            }

                            strBuffScript.append("{'DTSTAMP': '");
                            Timestamp dtstamp = new Timestamp(event.getFechaCompleta().getTimeInMillis());
                            String fehaICS = String.valueOf(dtstamp.toLocalDateTime());
                            strBuffScript.append(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00Z"));
                            strBuffScript.append("', 'UID': '");
                            strBuffScript.append("me@google.com");
                            strBuffScript.append("', 'DTSTART': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'DTEND': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'SUMMARY': '");
                            strBuffScript.append(event.getSubject());
                            strBuffScript.append("', 'DESCRIPTION': '");
                            strBuffScript.append(event.getEventoDesc());
                            strBuffScript.append("', 'LOCATION': '");
                            strBuffScript.append(event.getIcsDescripction());
                            contCol++;
                            if (contCol == lEvent.size()) {
                                strBuffScript.append("'}");
                            } else {
                                strBuffScript.append("'},");
                            }
                        }

                        strBuffScript.append("]}\n");
                        strBuffScript.append("var urlPDF = '");
                        strBuffScript.append(fileNamePDF.replace("\\", "\\\\"));
                        strBuffScript.append("';\n");
                        strBuffScript.append("</script>");

                        Calendar iter = fi;
                        contCol = 0;
                        while (iter.compareTo(ff) <= 0) {
                            if (contCol >= 5) {
                                break;
                            }

                            String key = iter.get(Calendar.YEAR) + "-" + ((iter.get(Calendar.MONTH) + 1) < 10 ? "0" + (iter.get(Calendar.MONTH) + 1) : (iter.get(Calendar.MONTH) + 1)) + "-" + ((iter.get(Calendar.DAY_OF_MONTH)) < 10 ? "0" + (iter.get(Calendar.DAY_OF_MONTH)) : (iter.get(Calendar.DAY_OF_MONTH)));
                            Map<String, String> eventMap = lEvenMaps.get(key);
                            if (eventMap == null) {
                                eventMap = new HashMap<>();
                                eventMap.put("DIA_SEM", diasSem[iter.get(Calendar.DAY_OF_WEEK) - 1]);
                                eventMap.put("DIA", Integer.toString(iter.get(Calendar.DAY_OF_MONTH)));
                                eventMap.put("DESC_EVEN", "");
                                eventMap.put("ID", "*");
                            }

                            contCol++;
                            strBuff.append("<div class=\"weekday\">");
                            strBuff.append("<div class=\"dayname\">");
                            strBuff.append(eventMap.get("DIA_SEM"));
                            strBuff.append("</div>");
                            strBuff.append("<div class=\"daynumber\">");
                            strBuff.append(eventMap.get("DIA"));
                            strBuff.append("</div>");
                            strBuff.append("<div class=\"dayevents\"><ul>");
                            strBuff.append(eventMap.get("DESC_EVEN"));
                            strBuff.append("</ul></div>");
                            if (!eventMap.get("ID").equals("*")) {
                                strBuff.append("<div class=\"agenda-day-add\">");
                                strBuff.append("<div class=\"agenda-add-item\">Agregar evento</div>");
                                strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"ical\">iCalendar</a> </div>");
                                //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"outlook\">Outlook</a> </div>");
                                //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"google-cal\">Google Calendar</a> </div>");
                                strBuff.append("</div>");
                            }
                            strBuff.append("</div>");

                            iter.add(Calendar.DAY_OF_YEAR, 1);
                        }
                        strBuff.append("</div>");
                    }
                    strBuff.append("<hr><div class=\"tablet-mobile-hidden\"><hr></div>");
                    strBuff.append(strBuffScript.toString());
                    break;
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_MONTH:
                    isCalendar = true;
                    strBuff = new StringBuffer("");

                    if (iteracion == 0) {
                        strBuff.append("<h1>").append(docTypeBO.getName()).append("</h1>");
                    }

                    strBuff.append("<div class=\"agenda-add\">");
                    strBuff.append("<div class=\"agenda-add-item\">Agregar todos los eventos</div>");
                    strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"ical\">iCalendar</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"outlook\">Outlook</a> </div>");
                    //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICS();' class=\"google-cal\">Google Calendar</a> </div>");
                    strBuff.append("</div>");
                    strBuff.append("<div class=\"eventos-por-dia\">");
                    strBuff.append("Eventos por día");
                    strBuff.append("</div>");

                    idMiVector = 0;
                    try {
                        idMiVector = Integer.parseInt(docTypeBO.getIddocument_type_vector());
                    } catch (Exception e) {
                        idMiVector = 0;
                    }

                    lEvent = AgendaWS.getEventLstMonth(idMiVector);
                    fi = AgendaWS.fechaIni;
                    //fi.set(2014, 9, 1);
                    ff = AgendaWS.fechaFin;
                    //ff.set(2014, 9, 30);
                    strBuffScript = new StringBuilder();
                    if (lEvent != null) {
                        String diaActual = "";
                        //boolean rellePrin = true;
                        Map<String, Map> lEvenMaps = new HashMap<>();
                        Map<String, String> attEvnt = new HashMap<>();
                        strBuffScript.append("<script>");
                        strBuffScript.append("var myJSONCal = {");
                        strBuffScript.append("'PRODID': '");
                        strBuffScript.append(prodid);
                        strBuffScript.append("',");
                        strBuffScript.append("'CALNAME': '");
                        strBuffScript.append(calname);
                        strBuffScript.append("',");
                        strBuffScript.append("'EVENTS': [");
                        int cont = 0;
                        for (EventBO event : lEvent) {
                            /*if (rellePrin) {
                             Calendar calAux = new GregorianCalendar();
                             calAux.setTimeInMillis(event.getFechaCompleta().getTimeInMillis());
                             int numRell = calAux.get(Calendar.DAY_OF_WEEK) - 2;
                             for (int i = 0; i < numRell; i++) {
                             attEvnt = new HashMap<>();
                             attEvnt.put("DIA_SEM", diasSem[i + 1]);
                             calAux.add(Calendar.DATE, -1);
                             attEvnt.put("DIA", Integer.toString(calAux.get(Calendar.DAY_OF_MONTH)));
                             String key = calAux.get(Calendar.YEAR) + "-" + ( (calAux.get(Calendar.MONTH)+1) < 10 ? "0" + (calAux.get(Calendar.MONTH)+1) : (calAux.get(Calendar.MONTH)+1)) + "-" + calAux.get(Calendar.DAY_OF_MONTH);
                             lEvenMaps.put(key, attEvnt);
                             }
                             rellePrin = false;
                             }*/

                            attEvnt = lEvenMaps.get(event.getFecha());
                            if (attEvnt == null) {
                                attEvnt = new HashMap<>();
                                diaActual = event.getFecha();//event.getFechaCompleta().get(Calendar.DAY_OF_MONTH);
                                attEvnt.put("DIA_SEM", diasSem[event.getFechaCompleta().get(Calendar.DAY_OF_WEEK) - 1]);
                                attEvnt.put("DIA", Integer.toString(event.getFechaCompleta().get(Calendar.DAY_OF_MONTH)));
                                attEvnt.put("DESC_EVEN", "<li>" + (event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()) + "</li>");
                                attEvnt.put("ID", String.valueOf(cont));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            } else {
                                //attEvnt = lEvenMaps.get(event.getFecha());
                                lEvenMaps.remove(event.getFecha());
                                attEvnt.replace("DESC_EVEN", ("<li>").concat(event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()).concat("</li>").concat(attEvnt.get("DESC_EVEN")));
                                attEvnt.replace("ID", attEvnt.get("ID").concat(",").concat(String.valueOf(cont)));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            }

                            strBuffScript.append("{'DTSTAMP': '");
                            Timestamp dtstamp = new Timestamp(event.getFechaCompleta().getTimeInMillis());
                            String fehaICS = String.valueOf(dtstamp.toLocalDateTime());
                            strBuffScript.append(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00Z"));
                            strBuffScript.append("', 'UID': '");
                            strBuffScript.append("me@google.com");
                            strBuffScript.append("', 'DTSTART': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'DTEND': '");
                            strBuffScript.append(event.getFechaCompleta().getTimeZone().toZoneId().getId().concat(":").concat(fehaICS.replaceAll("-", "").replaceAll(":", "").concat("00")));
                            strBuffScript.append("', 'SUMMARY': '");
                            strBuffScript.append(event.getSubject());
                            strBuffScript.append("', 'DESCRIPTION': '");
                            strBuffScript.append(event.getEventoDesc());
                            strBuffScript.append("', 'LOCATION': '");
                            strBuffScript.append(event.getIcsDescripction());

                            cont++;
                            if (cont == lEvent.size()) {
                                strBuffScript.append("'}");
                            } else {
                                strBuffScript.append("'},");
                            }
                        }

                        strBuffScript.append("]}\n");
                        strBuffScript.append("var urlPDF = '");
                        strBuffScript.append(fileNamePDF.replace("\\", "\\\\"));
                        strBuffScript.append("';\n");
                        strBuffScript.append("</script>");

                        int contCol = 0;
                        cont = 0;

                        Calendar iter = fi;
                        if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, 1);
                        } else if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, 2);
                        } else if (iter.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, (iter.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY) * (-1));
                        }

                        strBuff.append("<div class=\"agenda-month\">");

                        int countCol = 0;
                        while (iter.compareTo(ff) <= 0) {

                            if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || iter.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                                iter.add(Calendar.DAY_OF_YEAR, 1);
                                continue;
                            }

                            if (countCol >= 20) {
                                break;
                            }

                            String key = iter.get(Calendar.YEAR) + "-" + ((iter.get(Calendar.MONTH) + 1) < 10 ? "0" + (iter.get(Calendar.MONTH) + 1) : (iter.get(Calendar.MONTH) + 1)) + "-" + ((iter.get(Calendar.DAY_OF_MONTH)) < 10 ? "0" + (iter.get(Calendar.DAY_OF_MONTH)) : (iter.get(Calendar.DAY_OF_MONTH)));
                            Map<String, String> eventMap = lEvenMaps.get(key);
                            if (eventMap == null) {
                                eventMap = new HashMap<>();
                                eventMap.put("DIA_SEM", diasSem[iter.get(Calendar.DAY_OF_WEEK) - 1]);
                                eventMap.put("DIA", Integer.toString(iter.get(Calendar.DAY_OF_MONTH)));
                                eventMap.put("ID", "*");
                            }

                            //for (Map eventMap : lEvenMaps) {
                            if (contCol == 0) {
                                strBuff.append("<div class=\"agenda-week\">");
                            }

                            strBuff.append("<div class=\"weekday\">");
                            strBuff.append("<div class=\"dayname\">");
                            strBuff.append(eventMap.get("DIA_SEM"));
                            strBuff.append("</div>");
                            strBuff.append("<div class=\"daynumber\">");
                            strBuff.append(eventMap.get("DIA"));
                            strBuff.append("</div>");
                            strBuff.append("<div class=\"dayevents\"><ul>");
                            strBuff.append(eventMap.containsKey("DESC_EVEN") ? eventMap.get("DESC_EVEN") : "");
                            strBuff.append("</ul></div>");

                            if (!eventMap.get("ID").equals("*")) {
                                strBuff.append("<div class=\"agenda-day-add\">");
                                strBuff.append("<div class=\"agenda-add-item\">Agregar evento</div>");
                                strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"ical\">iCalendar</a> </div>");
                                //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"outlook\">Outlook</a> </div>");
                                //strBuff.append("<div class=\"agenda-add-item\"><a href=\"#\" onclick='crearICSPorEvento(\"").append(eventMap.get("ID")).append("\");' class=\"google-cal\">Google Calendar</a> </div>");
                                strBuff.append("</div>");

                            }

                            strBuff.append("</div>");

                            if (contCol >= 4) {
                                strBuff.append("</div><hr>");
                                contCol = 0;
                            } else {
                                contCol++;
                            }

                            cont++;
                            iter.add(Calendar.DAY_OF_YEAR, 1);
                            countCol++;
                        }

                        if (contCol > 0) {
                            int diasRestantes = 5 - contCol;
                            boolean empImp = diasRestantes % 2 == 0;
                            for (int i = 0; i < diasRestantes; i++) {
                                strBuff.append("<div class=\"weekday\">");
                                strBuff.append("<div class=\"dayname\">");
                                strBuff.append(diasSem[contCol + i + 1]);
                                strBuff.append("</div>");
                                strBuff.append("<div class=\"daynumber\">");
                                strBuff.append(Integer.toString(i + 1));
                                strBuff.append("</div>");
                                strBuff.append("<div class=\"dayevents\"></div></div>");
                            }
                            strBuff.append("</div>");
                        }
                    }

                    strBuff.append("</div><hr>");
                    strBuff.append(strBuffScript.toString());
                    break;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Utilerias.logger(getClass()).error(e);

        }
        strBuff.append("</br>");
        return strBuff.toString();
    }

    private void addLayout(StringBuffer strBuff, ModuleSectionBO section, ModuleSectionBO parentSection) {

        List<ModuleSectionBO> sectionsChilds = section.getChildSectionsAsList();
        float[] widths = null;
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType())) {
            //  panelParent.add(new JLabel(section.getSectionName()));
            SectionInfoBO secInfo = new SectionInfoBO();
            secInfo.setSectionName(section.getSectionName());
            // tableParent.addCell("test");
            if (parentSection != null) {
                section.setHeight(parentSection.getHeight());
            }
            displayObjects(strBuff, section);
        // panelParent.add(new ScrCell(secInfo));

            // panelParent.setBorder(BorderFactory.createEtchedBorder());
            // panelParent.setFocusable(true);
            //((ScrCell)panelParent).setDrag();
            return;
        }
        switch (section.getType()) {
            case GlobalDefines.SEC_TYPE_COLUMN:
                widths = new float[1];
                widths[0] = section.getWidth();
                break;
            case GlobalDefines.SEC_TYPE_SHEET:
                if (sectionsChilds != null && sectionsChilds.size() > 0) {
                    widths = new float[sectionsChilds.size()];
                    int i = 0;
                    for (ModuleSectionBO secTmp : sectionsChilds) {

                        switch (sectionsChilds.size()) {
                            case 2:
                                if (i == 0) {
                                    secTmp.setHtmlClass("left");
                                } else {
                                    secTmp.setHtmlClass("right");
                                }
                                break;
                            case 3:
                                if (i == 0) {
                                    secTmp.setHtmlClass("third first");
                                } else if (i == 1) {
                                    secTmp.setHtmlClass("third");
                                } else if (i == 2) {
                                    secTmp.setHtmlClass("third last");
                                }
                                break;

                        }

                        if (secTmp == null) {
                            continue;
                        }
                        widths[i++] = secTmp.getWidth();
                    }
                }
                break;
        }

        if (sectionsChilds != null) {
            if (section.getHtmlClass() != null && section.getHtmlClass().isEmpty() == false) {
                strBuff.append("<div  class='").append(section.getHtmlClass()).append("'  >\n");
            }
            for (ModuleSectionBO secTmp : sectionsChilds) {
                if (secTmp == null) {
                    continue;
                }
                switch (section.getType()) {
                    case GlobalDefines.SEC_TYPE_COLUMN:
                        addLayout(strBuff, secTmp, section);
                        break;
                    case GlobalDefines.SEC_TYPE_SHEET:
                        addLayout(strBuff, secTmp, section);
                        break;
                }

                if (secTmp.getType() != null && secTmp.getType().equals(GlobalDefines.SEC_TYPE_CELL)
                        && secTmp.getContentType() != null) {
                    strBuff.append(displayCalendar(secTmp.getContentType()));
                }
            }

            if (section.getHtmlClass() != null && section.getHtmlClass().isEmpty() == false) {
                strBuff.append("</div>\n");
            }
        }
    }

    protected void displayCarrusel_Anterior(StringBuffer strBuff, ModuleSectionBO section) {
        if (section == null) {
            return;
        }
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType()) == false) {
            return;
        }
        strBuff.append("<div style=\"vertical-align: top; width: 100%; float: left;\" class='image' >\n");
        strBuff.append("<ul class='rslides'>");
        for (ObjectInfoBO objectInfoBO : section.getLstObjects()) {
            strBuff.append("<li>\n");
            if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {
                displayImageHTML(strBuff, section, objectInfoBO);
            } else if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_VIDEO) {
                displayVideoHTML(strBuff, section, objectInfoBO);
            } else if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
                this.displayExcelHTML(strBuff, section, objectInfoBO);
            }
            strBuff.append("</li>\n");
        }
        strBuff.append("</ul>\n");
        strBuff.append("</div>\n");
    }

    protected void displayCarrusel(StringBuffer strBuff, ModuleSectionBO section) {
        if (section == null) {
            return;
        }
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType()) == false) {
            return;
        }
        strBuff.append("<div id=\"objcarrousel\" style=\"vertical-align: top; width: 100%; float: left;\" class='image' >\n");
        strBuff.append("<ul class='rslides'>");

        List<ObjectInfoBO> multi = section.getLstObjects();
        Collections.sort(multi, new Comparator<ObjectInfoBO>() {

            @Override
            public int compare(ObjectInfoBO o1, ObjectInfoBO o2) {
                return new Integer(o1.getOrderId()).compareTo(o2.getOrderId());
            }
        });

        for (ObjectInfoBO objectInfoBO : multi) {

            if (objectInfoBO.isDelete()) {
                continue;
            }

            strBuff.append("<li>\n");
            if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {
                displayImageHTML(strBuff, section, objectInfoBO);
            } else if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_VIDEO) {
                displayVideoHTML(strBuff, section, objectInfoBO);
            } else if (objectInfoBO.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
                this.displayExcelHTML(strBuff, section, objectInfoBO);
            }
            strBuff.append("</li>\n");
        }
        strBuff.append("</ul>\n");
        strBuff.append("</div>\n");
    }

    protected void displayObjects(StringBuffer strBuff, ModuleSectionBO section) {
        if (section == null) {
            return;
        }
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType()) == false) {
            return;
        }

        if (section.getHtmlClass() != null && section.getHtmlClass().isEmpty() == false) {
            strBuff.append("<div  class='").append(section.getHtmlClass()).append("'  >\n");
        }

        try {
            if (section.getLstObjects() != null && section.getLstObjects().size() > 0) {

                if (section.getLstObjects().size() > 1) {
                    int elementExist = 0;
                    for (int ii = 0; ii < section.getLstObjects().size(); ii++) {
                        if (section.getLstObjects().get(ii).getFile() == null || section.getLstObjects().get(ii).getFile().isEmpty()) {
                            if (section.getLstObjects().get(ii).getHtmlVideo() == null) {
                                section.getLstObjects().remove(ii);
                                continue;
                            }
                        }

                        if (section.getLstObjects().get(ii).isDelete()) {
                            continue;
                        }

                        elementExist++;
                    }
                    if (elementExist > 1) {
                        displayCarrusel(strBuff, section);
                    } else {
                        displayOneObject(strBuff, section);
                    }
                } else {

                    displayOneObject(strBuff, section);

                }
            } else if (section.getType().isEmpty() || section.getType() != null) {
                ModuleSectionBO modSecBO = moduleSectionResp.get(section.getSectionId());
                strBuff.append("<div class=\"mobile-hidden\" style=\" overflow:auto; width:100%; height:").append(modSecBO.getHeight()).append("px;\"></div>");

            }

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        if (section.getHtmlClass() != null && section.getHtmlClass().isEmpty() == false) {
            strBuff.append("</div>\n");
        }
    }

    protected void displayOneObject(StringBuffer strBuff, ModuleSectionBO section) {
        try {
            ObjectInfoBO objectInfo = section.getLstObjects().get(0);
            ModuleSectionBO modSecBO = moduleSectionResp.get(section.getSectionId());

            if (objectInfo.isDelete()) {
                strBuff.append("<div class=\"mobile-hidden\" style=\" overflow:auto; width:100%; height:").append(modSecBO.getHeight()).append("px;\"></div>");
            } else if (objectInfo != null) {

                if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {
                    displayImageHTML(strBuff, section, objectInfo);
                } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
                    displayExcelHTML(strBuff, section, objectInfo);
                } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_TEXT) {
                    displayTextHTML(strBuff, section, objectInfo);
                } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_VIDEO) {
                    displayVideoHTML(strBuff, section, objectInfo);
                } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_BULLET) {
                    displayBulletHTML(strBuff, section, objectInfo);
                }

            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    protected void displayExcelHTML(StringBuffer strBuff, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {

        try {
            String fileName = objectInfoBO.getFile();
            String fileNameResp = "resp_" + fileName;
            if (fileName == null || fileName.isEmpty()) {
                Utilerias.logger(getClass()).error("Imagen no encontrada");
                return;
            }

            String rutaCompleta = Utilerias.getFilePath(Utilerias.PATH_TYPE.GENERATED_IMAGES) + fileName;
            BufferedImage image = objectInfoBO.getImageThumb() == null ? objectInfoBO.getImage() : objectInfoBO.getImageThumb();
            if(writeImage(image, fileName, rutaCompleta)){
                addSshFile(rutaCompleta);
            }

            BufferedImage imageResp = null;
            String rutaCompletaResp = null;
            if (objectInfoBO.getImageThumb() != null) {
                rutaCompletaResp = Utilerias.getFilePath(Utilerias.PATH_TYPE.GENERATED_IMAGES) + fileNameResp;
                imageResp = objectInfoBO.getImage();
                if(writeImage(imageResp, fileNameResp, rutaCompletaResp)){
                    addSshFile(rutaCompletaResp);
                }
            }
            
            strBuff.append("<div id=\"imagen-wrapperx\" style=\"background-color:#FFF; width:100%; float:left;\">");


            if (objectInfoBO.getTitulo() != null && objectInfoBO.getTitulo().isEmpty() == false) {
                strBuff.append("<h3>").append(objectInfoBO.getTitulo()).append("</h3>\n");
            }

            if (objectInfoBO.getSubTitulo() != null && objectInfoBO.getSubTitulo().isEmpty() == false) {
                strBuff.append("<h4>").append(objectInfoBO.getSubTitulo()).append("</h4>\n");
            }

            strBuff.append("<div class='image'>\n ");
            // Add image 
            if (rutaCompletaResp == null) {
                strBuff.append(" <a alt=\"\" class='fancybox' href='IMG/").append(fileName).append("'>  ");
            } else {
                strBuff.append(" <a alt=\"\" class='fancybox' href='IMG/").append(fileNameResp).append("'>  ");
            }
            strBuff.append("<img alt=\"\"   src='IMG/").append(fileName).append("'>\n");
            strBuff.append("</a>");
            strBuff.append("</div>\n");
            if (objectInfoBO.getComentarios() != null && objectInfoBO.getComentarios().isEmpty() == false) {
                strBuff.append("<div class='description' ><i>\n");
                strBuff.append("Fuente: ");
                strBuff.append(objectInfoBO.getComentarios());
                strBuff.append("</i></div>\n");
            }
         strBuff.append("</div>");
   
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    protected void displayImageHTML(StringBuffer strBuff, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {

        try {
            String fileName = objectInfoBO.getFile();
            String fileNameResp = "resp_" + fileName;

            if (fileName == null || fileName.isEmpty()) {
                Utilerias.logger(getClass()).error("Imagen no encontrada");
                return;
            }

            BufferedImage image = objectInfoBO.getImageThumb() == null ? objectInfoBO.getImage() : objectInfoBO.getImageThumb();
            String rutaCompleta = Utilerias.getFilePath(Utilerias.PATH_TYPE.GENERATED_IMAGES) + fileName;

            if(writeImage(image, fileName, rutaCompleta)){
               addSshFile(rutaCompleta);
            }

            BufferedImage imageResp = null;
            String rutaCompletaResp = null;
            if (objectInfoBO.getImageThumb() != null) {
                rutaCompletaResp = Utilerias.getFilePath(Utilerias.PATH_TYPE.GENERATED_IMAGES) + fileNameResp;
                imageResp = objectInfoBO.getImage();
                if(writeImage(imageResp, fileNameResp, rutaCompletaResp)){
                    addSshFile(rutaCompletaResp);
                }
            }

            strBuff.append("<div id=\"imagen-wrapperx\" style=\"background-color:#FFF; width:100%; float:left;\">");

            if (objectInfoBO.getTitulo() != null && objectInfoBO.getTitulo().isEmpty() == false) {
                strBuff.append("<h3>").append(objectInfoBO.getTitulo()).append("</h3>\n");
            }

            if (objectInfoBO.getSubTitulo() != null && objectInfoBO.getSubTitulo().isEmpty() == false) {
                strBuff.append("<h4>").append(objectInfoBO.getSubTitulo()).append("</h4>\n");
            }

            strBuff.append("<div class='image'>\n ");
            // Add image            
            if (rutaCompletaResp == null) {
                strBuff.append(" <a alt=\"\" class='fancybox' href='IMG/").append(fileName).append("'>  ");
            } else {
                strBuff.append(" <a alt=\"\" class='fancybox' href='IMG/").append(fileNameResp).append("'>  ");
            }
            strBuff.append("<img alt=\"\" src='IMG/").append(fileName).append("'>\n");
            strBuff.append("</a>");
            strBuff.append("</div>\n");

            if (objectInfoBO.getComentarios() != null && objectInfoBO.getComentarios().isEmpty() == false) {
                strBuff.append("<div class='description' ><i>\n");
                strBuff.append("Fuente: ");
                strBuff.append(objectInfoBO.getComentarios());
                strBuff.append("</i></div>\n");
            }
            
            //imagen-wrapper
            strBuff.append("</div>");
            
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    private boolean writeImage(BufferedImage image, String fileName, String rutaCompleta) {
        boolean retValue = true;
        try {
            String ext = FilenameUtils.getExtension(fileName);
            File outputfile = new File(rutaCompleta);
            ImageIO.write(image, ext, outputfile);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            retValue = false;
        }
        return retValue;
    }

    protected void displayVideoHTML(StringBuffer strBuff, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
            strBuff.append("<div id=\"imagen-wrapperx\" style=\"background-color:#FFF; width:100%; float:left;\">");

        try {
            if (objectInfoBO.getTitulo() != null && objectInfoBO.getTitulo().isEmpty() == false) {
                strBuff.append("<h3>").append(objectInfoBO.getTitulo()).append("</h3>\n");
            }
            if (objectInfoBO.getSubTitulo() != null && objectInfoBO.getSubTitulo().isEmpty() == false) {
                strBuff.append("<h4>").append(objectInfoBO.getSubTitulo()).append("</h4>\n");
            }
            strBuff.append("<div class='image'  >\n ");
            ModuleSectionBO modSecBO = moduleSectionResp.get(section.getSectionId());
            String embedVideo = objectInfoBO.getHtmlVideo().replace("{WIDTH}", "100%").replace("{HEIGHT}", String.valueOf(modSecBO.getHeight()) + "px");
            //embedVideo =  embedVideo.replace("http://cdnapi.kaltura.com/html5/html5lib/v1.6.12.27i/mwEmbedLoader.php/p/972151/uiconf_id/8815852", "#");
            strBuff.append(embedVideo);
            strBuff.append("</div>\n");

            if (objectInfoBO.getComentarios() != null && objectInfoBO.getComentarios().isEmpty() == false) {
                strBuff.append("<div class='description' ><i>\n");
                strBuff.append("Fuente: ");
                strBuff.append(objectInfoBO.getComentarios());
                strBuff.append("</i></div>\n");
            }
            
            strBuff.append("</div>");

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    public String readBaseDocHTML(String fileName) {
        String theString = null;
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
                StringWriter writer = new StringWriter();) {
            IOUtils.copy(isr, writer);
            theString = writer.toString();
        } catch (IOException e) {
            Utilerias.logger(getClass()).info(e);
        }
        return theString;
    }

    protected void displayBulletHTML(StringBuffer strBuff, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        try {
            String plainText = objectInfoBO.getPlain_text();
            plainText = Utilerias.html2text(plainText);

            if (type_view == Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY && plainText != null && !plainText.isEmpty()) {

                strBuff.append("<p style=\"margin-bottom: 50px;\">");

                String filename = createAudio(plainText);
                strBuff.append("<audio style=\"width:38%; \" controls=\"\">\n")
                        .append(" <source src=\"")
                        .append(filename)
                        .append("\" type=\"audio/mpeg\">\n")
                        .append(" Este navegador no soporta el componente de audio HTML5. \n")
                        .append("</audio>");
            } else {
                strBuff.append("<p style=\"margin-bottom: 0px;\">");
            }

            if (objectInfoBO.getData() != null && !objectInfoBO.getData().isEmpty()) {
                String textSection = objectInfoBO.getData().replaceAll("#8A0808", "#000000");
                
                List<String> lstPs = UtileriasPDF.parseToList_P_NoFormat(textSection);
                strBuff.append("<ul>");
                for (String p : lstPs) {
                    if(p.trim().isEmpty()){
                        strBuff.append("<p style=\" width: 100%; height: 15px; margin: 0;\">").append(p).append("</p>");
                    }else{
                        strBuff.append("<li>").append(p).append("</li>");
                    }
                }
                strBuff.append("</ul>");
                
                /*textSection = textSection.replaceAll("<body>", "<ul>");
                textSection = textSection.replaceAll("<body>", "</ul>");
                textSection = textSection.replaceAll("<p style=\"margin-top: 0\">", "<li>");
                textSection = textSection.replaceAll("</p>", "</li>");
                
                strBuff.append(removeHTMLheaderTags(textSection));*/
                //strBuff.append(objectInfoBO.getData().replaceFirst("#000000", "#000000"));
            }
            strBuff.append("</p>");
            strBuff.append("<hr style=\"border-top: 1px solid #464646; padding: 2px; margin: 0;\">");
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    protected void displayTextHTML(StringBuffer strBuff, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        
        boolean isHeaderOrSection = false;
        
        try {
            String plainText = objectInfoBO.getPlain_text();
            String closeTagDiv = "";
            plainText = Utilerias.html2text(plainText);

            if (type_view == Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY && plainText != null && !plainText.isEmpty() && objectInfoBO.isHeader() == false && objectInfoBO.isSection() == false) {
                strBuff.append("<p style=\"margin-bottom: 70px;\">");
                String filename = createAudio(plainText);
                strBuff.append("<audio preload=\"none\" style=\"width:38%;  \" controls=\"\">\n")
                        .append(" <source src=\"")
                        .append(filename)
                        .append("\" type=\"audio/mpeg\">\n")
                        .append(" Este navegador no soporta el componente de audio HTML5. \n")
                        .append("</audio>");
            } else {
                ModuleSectionBO modSecBO = moduleSectionResp.get(section.getSectionId());
                
                
                if(objectInfoBO.isHeader() || objectInfoBO.isSection()){ isHeaderOrSection = true; }
                 
                String objHTML = removeHTMLheaderTags(objectInfoBO.getData(),isHeaderOrSection).trim();
                
                if (objHTML.length() != 0) {
                    strBuff.append("<div id=\"inside2_\"   style=\" overflow:auto; width:100%; text-align:justify; float:left; min-height:").append(modSecBO.getHeight()).append("px;\">");
                } else {
                    strBuff.append("<div id=\"inside1_\" class=\"mobile-hidden\" style=\" overflow:auto; width:100%; float:left; min-height:").append(modSecBO.getHeight()).append("px;\">");
                }
                //strBuff.append("<p style=\"margin-bottom: 0px; color:#000; \">");

                closeTagDiv = "</div>";
            }
            if (objectInfoBO.getData() != null && !objectInfoBO.getData().isEmpty()) {
                String textSection = objectInfoBO.getData().replaceAll("#8A0808", "#000000");
                
                if(objectInfoBO.isHeader()){
                    textSection = textSection.replaceAll("<p style=\"margin-top: 0\">", "<p style=\"margin-top: 0; font-size: 25px;\">");
                }else if(objectInfoBO.isSection()){
                    textSection = textSection.replaceFirst("<p style=\"margin-top: 0\">", "<p style=\" margin-top: 0\">");
                    textSection = textSection.replaceAll("<p style=\"margin-top: 0\">", "<p style=\"margin: 0px; font-size: 20px; background-color: #F34700; color: white; padding-left: 10px;\">");
                }
                
                textSection = textSection.replaceAll("<ul>", " ");
                textSection = textSection.replaceAll("</ul>", " ");
                textSection = textSection.replaceAll("<li>", "<p>• ");
                textSection = textSection.replaceAll("</li>", "</p>");
                strBuff.append(removeHTMLheaderTags(textSection,isHeaderOrSection));
            }
            //strBuff.append("</p>");
            strBuff.append(closeTagDiv);

        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
    }

    private String removeHTMLheaderTags(String html,boolean isHeaderOrSection) {
        String result = null;

        if (html != null && !html.isEmpty()) {
            result = html.replaceAll("<html>", "");
            result = result.replaceAll("</html>", "");
            result = result.replaceAll("<head>", "");
            result = result.replaceAll("</head>", "");
            result = result.replaceAll("<body>", "");
            result = result.replaceAll("</body>", "");
            result = result.replaceAll("</html", "");
            result = result.replaceAll("</htm", "");
            result = result.replaceAll("</ht", "");
            result = result.replaceAll("</h", "");
            result = result.replaceAll("</</div></br>", "</div>");
            result = result.replaceAll("</body</div></br>", "");
                                        
        }

        return result;
    }

    private String createAudio(String text) {

        text = Utilerias.html2text(text);
        String fileName = "";
        InputStream sound = null;
        try {

            if (proxy) {

                System.setProperty("http.proxySet", "true");
                System.setProperty("http.proxyHost", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_HOST));
                System.setProperty("http.proxyPort", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_PORT));

            }

            Synthesiser synthesiser = new Synthesiser(Synthesiser.LANG_ES_SPANISH);
            sound = synthesiser.getMP3Data(text);
            byte[] byteArray = IOUtils.toByteArray(sound);
            fileName = Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.AUDIO);
            addSshFile(fileName);
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(byteArray);
                fos.flush();
            }
            /* Generate relative path to sound file */
            File f1 = new File(fileName);
            String name = f1.getName();
            fileName = name;//String.format("%s%s", Utilerias.getProperty(ApplicationProperties.RELATIVE_AUDIO_FOLDER), name);

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
            if (messageSent == false) {
                Utilerias.showMessage(null, "No se generaron los audios del texto, revisar conexión", JOptionPane.INFORMATION_MESSAGE);
                messageSent = true;
            }
        } finally {
            if (proxy) {
                System.setProperty("http.proxySet", "false");
                System.setProperty("http.proxyHost", "");
                System.setProperty("http.proxyPort", "");
            }
        }

        return fileName;
    }

    private String createDirectory() {
        String documentBasePath = Utilerias.getFilePath(Utilerias.PATH_TYPE.DOCUMENT_BASE_DIRECTORY);
        String allHTML = readBaseDocHTML(documentBasePath);
        List<Usuario> lUsuario = UtileriasWS.getDirectorio();
        Collections.sort(lUsuario, new CustomComparator());

        if (lUsuario != null && lUsuario.size() > 0) {
            allHTML = allHTML.replaceAll("@CONTACTOLIDER", createUserLiderDirectory(lUsuario.get(0)).toString());
            StringBuffer strBuffContactos = new StringBuffer();
            boolean primerReg = true;
            for (Usuario u : lUsuario) {
                if (primerReg) {
                    primerReg = false;
                    continue;
                }
                strBuffContactos.append(createUserDirectory(u));
            }
            allHTML = allHTML.replaceAll("@CONTACTO", strBuffContactos.toString());
        } else {
            allHTML = allHTML.replaceAll("@CONTACTOLIDER", "Sin Contactos");
            allHTML = allHTML.replaceAll("@CONTACTO", "");
        }

        OfficeDAO dAO = new OfficeDAO();
        List<OfficeBO> list = dAO.get(null);
        if (list != null && list.size() > 0) {
            StringBuffer strBuffOfficer = new StringBuffer();
            for (OfficeBO o : list) {
                strBuffOfficer.append(createLocationDirectory(o));
            }
            allHTML = allHTML.replaceAll("@UBICACION", strBuffOfficer.toString());
        } else {
            allHTML = allHTML.replaceAll("@UBICACION", "Sin Ubicaciones");
        }

        String fileName = Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.HTML);
        try {
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            bwr.write(allHTML);
            bwr.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        addSshFile(fileName);
        return Utilerias.getFileName(fileName);
    }

    private StringBuffer createUserLiderDirectory(Usuario usuario) {
        StringBuffer usuarioLiderBuff = new StringBuffer("<h2>").append(usuario.getNombre()).append("</h2><div class=\"cargo\"></div>")
                .append("<div class=\"sectores\">").append(loadIndustry(usuario.getUsuarioId())).append("<br>").append(loadEmisoras(usuario.getUsuarioId())).append("</div>")
                .append("<div class=\"contacto\"><div class=\"mail\"><a href=\"mailto:").append(usuario.getCorreo()).append("\"><em>").append(usuario.getCorreo()).append("</em></a></div>")
                .append("<div class=\"tel\"><a href=\"tel:01800-000-5860\">01800-000-5860</a> / x").append(usuario.getExtension()).append("</div></div>");

        return usuarioLiderBuff;
    }

    private StringBuffer createUserDirectory(Usuario usuario) {
        StringBuffer usuarioBuff = new StringBuffer("<div class=\"contacto\"><h2>").append(usuario.getNombre()).append("</h2>")
                .append("<div class=\"sectores\">").append(loadIndustry(usuario.getUsuarioId())).append("<br>").append(loadEmisoras(usuario.getUsuarioId())).append("</div>")
                .append("<div class=\"contacto\"><div class=\"mail\"><a href=\"mailto:").append(usuario.getCorreo()).append("\">").append(usuario.getCorreo()).append("</a></div>")
                .append("<div class=\"tel\"><a href=\"tel:01800-000-5860\">01800-000-5860</a> / x").append(usuario.getExtension()).append("</div></div></div>");

        return usuarioBuff;
    }

    private StringBuffer createLocationDirectory(OfficeBO office) {
        StringBuffer usuarioBuff = new StringBuffer("<div class=\"ubicacion\"><h2>").append(office.getBranch())
                .append("</h2><div class=\"direccion\"> ").append(office.getAddress())
                .append(office.getDistrict().isEmpty() ? "" : ", " + office.getDistrict())
                .append(office.getCity().isEmpty() ? "" : ", " + office.getCity())
                .append(office.getState().isEmpty() ? "" : ", " + office.getState())
                .append(office.getCountry().isEmpty() ? "" : ", " + office.getCountry())
                .append("<br>C.P. ").append(office.getZipCode()).append("</div>")
                .append("<div class=\"tel\"><a href=\"tel:").append(office.getPhone3()).append("\">").append(office.getPhone3()).append("</a></div></div>");

        return usuarioBuff;
    }

    private String loadEmisoras(int idUsuario) {
        String retVal = "";
        SubjectDAO daoSubject = new SubjectDAO();
        List<SubjectBO> listSubject = daoSubject.getByName(null, true);

        IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
        UtileriasWS.setEndpoint(stub);
        if (listSubject != null) {
            try {
                UsuarioSeguimiento usuSeg = stub.getSectorEmisoraPorUsuario(idUsuario);
                ArrayOfUsuarioEmisora aEm = usuSeg.getEmisoras();
                UsuarioEmisora[] ue = aEm.getUsuarioEmisora();
                if (ue != null) {
                    //cmbEmisoras.removeAllItems();
                    for (SubjectBO listSubject1 : listSubject) {
                        for (UsuarioEmisora ue1 : ue) {
                            if (listSubject1.getIdSubject() == Integer.parseInt(ue1.getEmisora())) {
                                SubjectBO bo = new SubjectBO();
                                bo.setName(listSubject1.getName());
                                bo.setIdSubject(listSubject1.getIdSubject());
                                //cmbEmisoras.addItem(bo);
                                retVal += ", " + listSubject1.getName();
                                break;
                            }
                        }
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "El servicio de sectores por emisora no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            }
        }

        if (retVal.isEmpty()) {
            return "";
        }

        return retVal.substring(1);
    }

    private String loadIndustry(int idUsuario) {
        String retVal = "";

        try {
            IndustryDAO dAO = new IndustryDAO();
            List<IndustryBO> list = dAO.get(null);
            if (list != null) {
                IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                UtileriasWS.setEndpoint(stub);

                UsuarioSeguimiento usuSeg = stub.getSectorEmisoraPorUsuario(idUsuario);
                ArrayOfUsuarioSector aUS = usuSeg.getSectores();
                UsuarioSector[] us = aUS.getUsuarioSector();

                for (IndustryBO indBO : list) {
                    for (UsuarioSector us1 : us) {
                        if (indBO.getIdIndustry() == Integer.parseInt(us1.getSector())) {
                            retVal += ", " + indBO.getName();
                            break;
                        }
                    }
                }
            }
        } catch (RemoteException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El servicio de sectores por emisora no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }

        if (retVal.isEmpty()) {
            return "";
        }

        return retVal.substring(1);
    }

    public class CustomComparator implements Comparator<Usuario> {

        @Override
        public int compare(Usuario o1, Usuario o2) {
            return Integer.valueOf(o1.getOrdenDir()).compareTo(o2.getOrdenDir());
        }
    }
}
