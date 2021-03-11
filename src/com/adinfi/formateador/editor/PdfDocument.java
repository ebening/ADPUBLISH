package com.adinfi.formateador.editor;

/**
 *
 * @author Guillermo Trejo
 */
import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.EventBO;
import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.bos.HeaderfooterBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.OfficeBO;
import com.adinfi.formateador.bos.SectionColModBO;
import com.adinfi.formateador.bos.SectionInfoBO;
import com.adinfi.formateador.bos.SemanarioDocsBO;
import com.adinfi.formateador.bos.SendPublishAuthorsBO;
import com.adinfi.formateador.bos.SendPublishBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.bos.seguridad.Usuario;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.HeaderColModDAO;
import com.adinfi.formateador.dao.HeaderfooterDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.OfficeDAO;
import com.adinfi.formateador.dao.SectionColModDAO;
import com.adinfi.formateador.dao.SendPublishAuthorsDAO;
import com.adinfi.formateador.dao.SendPublishDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.dao.TemplateDAO;
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
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.text.WordUtils;

public class PdfDocument {

    /**
     * A font that is used in the calendar
     */
    protected Font normal;
    /**
     * A font that is used in the calendar
     */
    protected Font small;
    /**
     * A font that is used in the calendar
     */
    protected Font bold;

    private static final Integer MARGIN_CELL_DESCRIPTION = 80;
    private static final Float CELL_FIXED_HEIGHT = 15f;
    private static final Integer LEFT_RIGHT_CELL_MARGIN = 25;
    private static final Logger LOG = Logger.getLogger(PdfDocument.class.getName());
    private static final Rectangle PAGE_SIZE = PageSize.LETTER;
    private static PdfPTable sectionCalendar = null;
    private static PdfPTable titleCalendar = null;
    private static List<PdfPTable> sectionCalendarMultimedia = null;
    private com.itextpdf.text.Image imageBullet;
    private com.itextpdf.text.Image imageTwitter;
    private com.itextpdf.text.Image imageDescrip;
    private com.itextpdf.text.Image imageUbicacionSmall;
    private com.itextpdf.text.Image imageTelefonoSmall;
    private com.itextpdf.text.Image imageMail;
    private com.itextpdf.text.Image imageUbicacion;
    private com.itextpdf.text.Image imageTelefono;
    private com.itextpdf.text.Image imageLibro;
    private static final Float RESIZE_MODULE_FPAGE = 110f;//70f;
    private static final Float RESIZE_MODULE_NPAGE = 60f;
    private Float resizePage = 0f;
    private final String NOMBRE_AUTOR = "NombreAutor";
    private final String CORREO_EXT = "CorreoExt";
    private final int TITULO_COD = 0;
    private final int TIPO_DOC = 1;
    private String _fileName;
    private String _filePath;
    private boolean containCalendar;
    private boolean eliminarDisplosure;
    private DocumentBO docBODisplosure;
    private DocumentBO docBOGlobal;
    private Hashtable<Integer, ModuleSectionBO> moduleSectionResp;
    private boolean agregarDirectorio = false;

    /**
     * Initializes the fonts and collections.
     *
     * @throws DocumentException
     * @throws IOException
     */
    public PdfDocument() throws DocumentException, IOException {
//        // fonts
//        BaseFont bf_normal
//                = BaseFont.createFont("c://windows/fonts/arial.ttf",
//                        BaseFont.WINANSI, BaseFont.EMBEDDED);
//        normal = new Font(bf_normal, 16);
//        small = new Font(bf_normal, 8);
//        BaseFont bf_bold
//                = BaseFont.createFont("c://windows/fonts/arialbd.ttf",
//                        BaseFont.WINANSI, BaseFont.EMBEDDED);
//        bold = new Font(bf_bold, 14);
        docBODisplosure = null;
    }

    public String getFileName() {
        return _fileName;
    }

    public String getFilePath() {
        return _filePath;
    }

    public boolean getEliminarDisplosure() {
        return eliminarDisplosure;
    }

    public void setEliminarDisplosure(boolean eliminarDisplosure) {
        this.eliminarDisplosure = eliminarDisplosure;
    }

    public DocumentBO getDocBODisplosure() {
        return docBODisplosure;
    }

    public void setDocBODisplosure(DocumentBO docBODisplosure) {
        this.docBODisplosure = docBODisplosure;
    }

    public void setAgregarDirectorio(boolean agregarDirectorio) {
        this.agregarDirectorio = agregarDirectorio;
    }

    private boolean isCalendar(DocumentBO docBO) {
        boolean retVal = false;
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();
        List<String> idCalendarExist = new ArrayList<>();
        idCalendarExist.add("12");
        idCalendarExist.add("13");
        idCalendarExist.add("14");

        Integer numHoja = null;
        HojaBO hojaBO = null;
        List<ModuleBO> lstModules = null;
        int orden = 0;
        if (it.hasNext()) {
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

            if (hojaBO != null) {
                lstModules = hojaBO.getLstModules();
                for (ModuleBO moduleBO : lstModules) {
                    if (idCalendarExist.contains(String.valueOf(moduleBO.getModuleId()))) {
                        retVal = true;
                        break;
                    }
                }
            }
        }
        return retVal;
    }

    private boolean isCalendarNext(List<ModuleBO> lstModules) {
        boolean retVal = false;
        List<String> idCalendarExist = new ArrayList<>();
        idCalendarExist.add("12");
        idCalendarExist.add("13");
        idCalendarExist.add("14");

        for (ModuleBO moduleBO : lstModules) {
            if (idCalendarExist.contains(String.valueOf(moduleBO.getModuleId()))) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }
    
    public DocumentBO createPdfDocumentNoDisclosure(DocumentBO docBO, boolean isGenByHTML) throws IOException, DocumentException {
        eliminarDisplosure = true;
        return createPdfDocument(docBO, isGenByHTML);
    }
    
    public DocumentBO createPdfDocumentDisclosureAndDirOnly(DocumentBO docBO) throws IOException, DocumentException {

        if (docBO == null) {
            return null;
        }

        int documentId = docBO.getDocumentId();
        String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
        String fileName = "DD_" + documentId + ".pdf";
        
        _fileName = fileName;
        fileName = dirPDF + fileName;
        _filePath = fileName;

        Rectangle pagesize = new Rectangle(PAGE_SIZE);
        Document document = new Document(pagesize, 15f, 15f, 60f, 45f);
        PdfWriter writer = null;

        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        containCalendar = false;
        
        try {
            SubjectBO subjectName = null;
            SubjectDAO dao = new SubjectDAO();
            List<SubjectBO> list = dao.get(null);
            DisclosureDocument disclosure = new DisclosureDocument();
            
            for (int themeId = 0; themeId < list.size(); themeId++) {
                if (list.get(themeId).getIdSubject() == docBO.getIdSubject()) {
                    subjectName = list.get(themeId);
                    break;
                }
            }

            disclosure.addDisclosure(document, docBO.getIdDocType(), subjectName, containCalendar);

            document.newPage();
            document.add(displayDirectory());
            document.newPage();
            document.add(displayLocation());
            
            document.close();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return docBO;
    }

    public DocumentBO createPdfDocument(DocumentBO docBO, boolean isGenByHTML) throws IOException, DocumentException {

        if (docBO == null) {
            return null;
        }

        DocumentTypeDAO dDAO = new DocumentTypeDAO();
        List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);
        for (DocumentTypeBO d : dBO) {
            if (d.getIddocument_type() == docBO.getIdDocType()) {

                if (!d.isPdf()) {
                    if (!isGenByHTML) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utilerias.showMessage(null, "No es posible generar Documento PDF para este tipo de Documento", JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception ex) {
                                    Utilerias.logger(getClass()).error(ex);
                                }
                            }
                        });
                    }
                    return null;
                }
                break;
            }
        }

        docBOGlobal = docBO;
        int documentId = docBO.getDocumentId();
        String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
        String fileName = documentId + "_" + docBO.getFileName() + "_" + Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.PDF) + ".pdf";
        
        if(eliminarDisplosure)
            fileName = "ND_" + documentId + ".pdf";
        
        _fileName = fileName;
        fileName = dirPDF + fileName;
        _filePath = fileName;

        Rectangle pagesize = new Rectangle(PAGE_SIZE);
        Document document = new Document(pagesize, 15f, 15f, 60f, 45f);
        PdfWriter writer = null;

        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

        crearHeaderFooter(document, writer, docBO);
        document.open();
        PdfPTable table = null;
        Boolean first = false;
        containCalendar = false;

        //String documentName = docBO.getDocumentName();
        if (!isCalendar(docBO)) {
            PdfPTable tableChild = getDocumentTitle(docBO);
            if (tableChild != null) {
                document.add(tableChild);
                //document.newPage();
                first = true;
            }
        } else {
            first = true;
        }

        if (docBO.getMapHojas() == null || docBO.getMapHojas().size() <= 0) {
            return null;
        }
        int numeroTotalHojas = docBO.getNumHojas();
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();

        Integer numHoja = null;
        HojaBO hojaBO = null;
        List<ModuleBO> lstModules = null;
        TemplateDAO tmpDAO = new TemplateDAO();
        int i = 0;
        int orden = 0;
        resizePage = RESIZE_MODULE_FPAGE;
        while (it.hasNext()) {
            Utilerias.pasarGarbageCollector();
            if( GlobalDefines.COLLAB_TYPE_MOD.equals( docBO.getCollaborativeType() ) == false ){
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
            }else{
                numHoja = (Integer) it.next();
            }
            hojaBO = docBO.getMapHojas().get(numHoja);
            if (hojaBO == null) {
                continue;
            }
            table = new PdfPTable(1);
            table.setSkipFirstHeader(true);
            table.setSkipLastFooter(true);

            //PdfPCell cell = new PdfPCell(); 
            //cell.AddElement(t); 
            //cell.setBorderWidthBottom(1f);
            //cell.setBorderWidthLeft(1f); 
            //cell.setBorderWidthTop(1f); 
            //cell.setBorderWidthRight(1f); 
            //PdfPTable t1 = new PdfPTable(1); 
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            table.setWidthPercentage(90f);
            //TODO nuevo para errores
            table.setSplitLate(false);
            table.setSplitRows(false);
            table.setExtendLastRow(false);

            lstModules = hojaBO.getLstModules();
            if (first == false && i != numeroTotalHojas) {//&& !containCalendar){// && !isCalendarNext(lstModules)) {
                document.setMargins(15f, 15f, 50f, 5f);//15f, 15f, 65f, 45f
                document.newPage();

            }

            if (!first) {
                resizePage = RESIZE_MODULE_NPAGE;
            }
            //first = false;

            if (lstModules != null) {
                /* Manejo de espacios de la impresion de modulos en la hoja del pdf
                 Si se mueven los margenes Top y Bottom se tiene que reconfigurar esta parte
                 */
                short h = 0;
                boolean isResize = false;
                for (ModuleBO m : lstModules) {
                    h += m.getHeight();
                }
                if (h > (GlobalDefines.DRAWABLE_HEIGHT - resizePage)) {
                    isResize = true;
                }
                int imod = 0;
                Float resizePending = resizePage;
                resizePage = 0f;

                boolean agregoCalendarioColl = false;
                for (ModuleBO moduleBO : lstModules) {
                    moduleSectionResp = tmpDAO.getModulesSectionsByModuleId(moduleBO.getModuleId());

                    if (imod == lstModules.size() - 1 && isResize) {
                        resizePage = resizePending;
                    }
                    imod++;

                    if (docBO.isCollaborative()) {
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

                            try {
                                HeaderColModBO headerBO = new HeaderColModDAO().getHeader(moduleBO.getIdHeader());
                                PdfPTable headChild = getHeaderMod(headerBO.getName());
                                document.add(headChild);
                            } catch (Exception e) {
                                Utilerias.logger(getClass()).info(e);
                            }

                        }

                        if (moduleBO.getIdSection() > 0) {
                            try {
                                SectionColModBO sectionBO = new SectionColModDAO().getSection(moduleBO.getIdSection());
                                PdfPTable sectionChild = getSectionMod(sectionBO.getName());
                                document.add(sectionChild);

                            } catch (Exception e) {
                                Utilerias.logger(getClass()).info(e);
                            }
                        }*/
                    }
                    addLayout(table, moduleBO.getRootSection(), null);
                    containCalendar = false;
                    if (sectionCalendar != null) {
                        containCalendar = true;

                        if (i == 0) {
                            document.add(titleCalendar);
                        }

                        document.add(sectionCalendar);
                        if (sectionCalendarMultimedia != null) {
                            for (int imul = 0; imul < sectionCalendarMultimedia.size(); imul++) {
                                if (sectionCalendarMultimedia.get(imul) != null) {
                                    document.add(sectionCalendarMultimedia.get(imul));
                                }
                            }
                        }
                        sectionCalendar = null;
                        sectionCalendarMultimedia = null;
                        agregoCalendarioColl = true;
                    }
                }
                //table.addCell(cell);
                if (!agregoCalendarioColl) {
                    document.add(table);
                }
            }
            i++;

            first = false;
        }
        try {
            if (!eliminarDisplosure) {
                SubjectBO subjectName = null;
                SubjectDAO dao = new SubjectDAO();
                List<SubjectBO> list = dao.get(null);
                DisclosureDocument disclosure = new DisclosureDocument();

                if (docBODisplosure == null) {
                    for (int themeId = 0; themeId < list.size(); themeId++) {
                        if (list.get(themeId).getIdSubject() == docBO.getIdSubject()) {
                            subjectName = list.get(themeId);
                            break;
                        }
                    }

                    disclosure.addDisclosure(document, docBO.getIdDocType(), subjectName, containCalendar);
                } else {
                    for (int themeId = 0; themeId < list.size(); themeId++) {
                        if (list.get(themeId).getIdSubject() == docBODisplosure.getIdSubject()) {
                            subjectName = list.get(themeId);
                            break;
                        }
                    }

                    disclosure.addDisclosure(document, docBODisplosure.getIdDocType(), subjectName, containCalendar);

                    if (agregarDirectorio) {
                        document.newPage();
                        document.add(displayDirectory());
                        document.newPage();
                        document.add(displayLocation());
                    }
                }
            }
            document.close();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return docBO;
    }

    protected void crearHeaderFooter(Document document, PdfWriter writer, DocumentBO docBO) {
        try {
            writer.setBoxSize("art", new Rectangle(5, 5, 787, 42));
            HeaderFooter event = new HeaderFooter();
            event.docBO = docBO;
            writer.setPageEvent(event);
        } catch (Exception ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    protected PdfPTable displayExcelPDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPTable tableChild = new PdfPTable(1);
        tableChild.setSplitLate(false);
        tableChild.setSplitRows(false);
        tableChild.setExtendLastRow(false);
        int espacioTextos = 65;

        try {
            tableChild.setWidths(new float[]{Float.parseFloat(String.valueOf(section.getWidth()))});

            //String fileName = objectInfoBO.getFile2();
            // Add titulo
            Font fTit = UtileriasPDF.fontTitulo;
            fTit.setColor(UtileriasPDF.fgColorTitulo);
            Paragraph p = new Paragraph(objectInfoBO.getTitulo() == null || objectInfoBO.getTitulo().isEmpty() ? "" : objectInfoBO.getTitulo(), fTit);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell cellTitulo = new PdfPCell();
            cellTitulo.addElement(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setFixedHeight(CELL_FIXED_HEIGHT);
            cellTitulo.setPadding(1f);
            tableChild.addCell(cellTitulo);

            // Add subtitulo 
            if (objectInfoBO.getSubTitulo() != null && !objectInfoBO.getSubTitulo().isEmpty()) {
                Font fSub = UtileriasPDF.fontSubtitulo;
                fSub.setColor(UtileriasPDF.fgColorSubtitulo);
                Paragraph p1 = new Paragraph(objectInfoBO.getSubTitulo() == null || objectInfoBO.getSubTitulo().isEmpty() ? "" : objectInfoBO.getSubTitulo(), fSub);
                p1.setAlignment(Element.ALIGN_JUSTIFIED);
                PdfPCell cellSubtitulo = new PdfPCell();
                cellSubtitulo.addElement(p1);
                cellSubtitulo.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
                cellSubtitulo.setBorder(Rectangle.NO_BORDER);
                cellSubtitulo.setFixedHeight(CELL_FIXED_HEIGHT);
                cellSubtitulo.setPadding(1f);
                tableChild.addCell(cellSubtitulo);
                espacioTextos = 75;
            }

            // Add image            
            BufferedImage bi_ = objectInfoBO.getImageThumb() == null ? objectInfoBO.getImage() : objectInfoBO.getImageThumb(); //Image.getInstance(fileName);

            Image image1 = Image.getInstance(bi_, null);
            if (image1 != null) {
                //float ancho = image1.getWidth();
                //float alto = image1.getHeight();

                float maxWidth = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                float maxHeight = section.getHeight() - MARGIN_CELL_DESCRIPTION;

                //float old_x = ancho;
                //float old_y = alto;
                float new_w = maxWidth;
                float new_h = maxHeight;

                /*float new_w = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                 float new_h = section.getHeight() - resizePage - CELL_FIXED_HEIGHT;

                 if (image1.getWidth() > new_w || image1.getHeight() > new_h){
                 image1.scaleToFit(new_w, new_h);
                 }*/
                float ratio1 = image1.getWidth() / new_w;
                float ratio2 = image1.getHeight() / new_h;

                float thumb_w = 0, thumb_h = 0;
                if (ratio1 > ratio2) {
                    thumb_w = new_w;
                    thumb_h = image1.getHeight() / ratio1;
                } else {
                    thumb_w = image1.getWidth() / ratio2;
                    thumb_h = new_h;
                }
                //image1.scaleAbsolute((float) Math.floor(thumb_w-5), (float) Math.floor(thumb_h-5));
                image1.scaleAbsoluteHeight((float) (section.getHeight() - espacioTextos));//Math.floor(thumb_h-5));
                image1.scaleAbsoluteWidth((float) ((section.getWidth() - 10) * 0.9));
                try {//FIXME Bloque Try para esperar la actualizacion de la escalacion de la imagen
                    Thread.sleep(50);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }

                PdfPCell cellImage = new PdfPCell(image1);
                cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                //cellImage.setBackgroundColor(UtileriasPDF.bgColorImagen);
                cellImage.setBorder(Rectangle.NO_BORDER);
                cellImage.setPaddingTop(5f);
                cellImage.setPaddingBottom(3f);

                //cellImage.setFixedHeight(section.getHeight() - MARGIN_CELL_DESCRIPTION);
                tableChild.addCell(cellImage);
            }

            // Add comments
            Font fCom = UtileriasPDF.fontDescripcion;
            fCom.setColor(UtileriasPDF.fgColorDescripcion);
            Paragraph p2 = new Paragraph(objectInfoBO.getComentarios() == null || objectInfoBO.getComentarios().isEmpty() ? " Fuente: " : " Fuente: " + objectInfoBO.getComentarios(), fCom);
            p2.setLeading(7f);
            p2.add(0, new Chunk(getDescripImage(), 0, 0));
            p2.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell cellDescripcion = new PdfPCell();
            cellDescripcion.addElement(p2);
            //cellDescripcion.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcion.setBorder(Rectangle.NO_BORDER);
            cellDescripcion.setNoWrap(false);
            cellDescripcion.setFixedHeight(15f);

            if (sectionCalendar != null) {
                cellDescripcion.setFixedHeight(25f);
            }

            cellDescripcion.setVerticalAlignment(Element.ALIGN_CENTER);
            cellDescripcion.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableChild.addCell(cellDescripcion);

            PdfPCell cellDescripcionDown = new PdfPCell();
            //cellDescripcion.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcionDown.setBorder(Rectangle.NO_BORDER);
            cellDescripcionDown.setNoWrap(false);
            cellDescripcionDown.setFixedHeight(CELL_FIXED_HEIGHT);
            cellDescripcionDown.setVerticalAlignment(Element.ALIGN_TOP);
            cellDescripcionDown.setHorizontalAlignment(Element.ALIGN_LEFT);
            //tableChild.addCell(cellDescripcionDown);

            if (sectionCalendar == null) {
                tableParent.addCell(tableChild);
            }
        } catch (BadElementException | IOException ex) {
            LOG.log(Level.ALL, ex.getMessage());
        } catch (DocumentException ex) {
            Logger.getLogger(PdfDocument.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableChild;
    }

    protected PdfPTable displayImagePDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPTable tableChild = new PdfPTable(1);
        //TODO nuevo para errores
        tableChild.setSplitLate(false);
        tableChild.setSplitRows(false);
        tableChild.setExtendLastRow(false);
        int espacioTextos = 65;

        try {
            tableChild.setTotalWidth(new float[]{Float.parseFloat(String.valueOf(section.getWidth()))});
            //String fileName = objectInfoBO.getFile();

            // Add titulo
            Font fTit = UtileriasPDF.fontTitulo;
            fTit.setColor(UtileriasPDF.fgColorTitulo);
            Paragraph p = new Paragraph(objectInfoBO.getTitulo() == null || objectInfoBO.getTitulo().isEmpty() ? "" : objectInfoBO.getTitulo(), fTit);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell cellTitulo = new PdfPCell();
            cellTitulo.addElement(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellTitulo.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setPadding(1f);
            cellTitulo.setFixedHeight(CELL_FIXED_HEIGHT);
            tableChild.addCell(cellTitulo);

            // Add subtitulo 
            if (objectInfoBO.getSubTitulo() != null && !objectInfoBO.getSubTitulo().isEmpty()) {
                Font fSub = UtileriasPDF.fontSubtitulo;
                fSub.setColor(UtileriasPDF.fgColorSubtitulo);
                Paragraph p1 = new Paragraph(objectInfoBO.getSubTitulo(), fSub);
                p1.setAlignment(Element.ALIGN_JUSTIFIED);
                PdfPCell cellSubtitulo = new PdfPCell();
                cellSubtitulo.addElement(p1);
                cellSubtitulo.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
                cellSubtitulo.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
                cellSubtitulo.setPadding(1f);
                cellSubtitulo.setFixedHeight(CELL_FIXED_HEIGHT);
                tableChild.addCell(cellSubtitulo);
                espacioTextos = 75;
            }

            // Add image            
            BufferedImage bi_ = objectInfoBO.getImageThumb() == null ? objectInfoBO.getImage() : objectInfoBO.getImageThumb(); //Image.getInstance(fileName);

            Image image1 = Image.getInstance(bi_, null);
            if (image1 != null) {
                //float ancho = image1.getWidth();
                //float alto = image1.getHeight();

                float maxWidth = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                float maxHeight = section.getHeight() - MARGIN_CELL_DESCRIPTION;

                //float old_x = ancho;
                //float old_y = alto;
                float new_w = maxWidth;
                float new_h = maxHeight;

                /*float new_w = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                 float new_h = section.getHeight() - resizePage - CELL_FIXED_HEIGHT;

                 if (image1.getWidth() > new_w || image1.getHeight() > new_h){
                 image1.scaleToFit(new_w, new_h);
                 }*/
                float ratio1 = image1.getWidth() / new_w;
                float ratio2 = image1.getHeight() / new_h;

                float thumb_w = 0, thumb_h = 0;
                if (ratio1 > ratio2) {
                    thumb_w = new_w;
                    thumb_h = image1.getHeight() / ratio1;
                } else {
                    thumb_w = image1.getWidth() / ratio2;
                    thumb_h = new_h;
                }
                //image1.scaleAbsolute((float) Math.floor(thumb_w-5), (float) Math.floor(thumb_h-5));
                image1.scaleAbsoluteHeight((float) (section.getHeight() - espacioTextos));//Math.floor(thumb_h-5));
                image1.scaleAbsoluteWidth((float) ((section.getWidth() - 10) * 0.9));
                try {//FIXME Bloque Try para esperar la actualizacion de la escalacion de la imagen
                    Thread.sleep(50);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }

                PdfPCell cellImage = new PdfPCell(image1);
                cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                //cellImage.setBackgroundColor(UtileriasPDF.bgColorImagen);
                cellImage.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
                cellImage.setPaddingTop(5f);
                cellImage.setPaddingBottom(3f);

                //cellImage.setFixedHeight(image1.getHeight());// - MARGIN_CELL_DESCRIPTION);
                tableChild.addCell(cellImage);
            }

            // Add comments
            Font fCom = UtileriasPDF.fontDescripcion;
            fCom.setColor(UtileriasPDF.fgColorDescripcion);
            Paragraph p2 = new Paragraph(objectInfoBO.getComentarios() == null || objectInfoBO.getComentarios().isEmpty() ? " Fuente: " : " Fuente: " + objectInfoBO.getComentarios(), fCom);
            p2.setLeading(7f);
            Image imageDes = getDescripImage();
            imageDes.scaleAbsoluteHeight(5f);
            imageDes.scaleAbsoluteWidth(5f);
            p2.add(0, new Chunk(imageDes, 0, 0));
            p2.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell cellDescripcion = new PdfPCell();
            cellDescripcion.addElement(p2);
            //cellDescripcion.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcion.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
            cellDescripcion.setFixedHeight(15f);

            if (sectionCalendar != null) {
                cellDescripcion.setFixedHeight(25f);
            }

            cellDescripcion.setVerticalAlignment(Element.ALIGN_CENTER);
            cellDescripcion.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            tableChild.addCell(cellDescripcion);

            PdfPCell cellDescripcionDown = new PdfPCell();
            //cellDescripcionDown.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcionDown.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
            cellDescripcionDown.setNoWrap(false);
            //cellDescripcion.setFixedHeight(CELL_FIXED_HEIGHT);
            cellDescripcionDown.setVerticalAlignment(Element.ALIGN_TOP);
            cellDescripcionDown.setHorizontalAlignment(Element.ALIGN_LEFT);
            //tableChild.addCell(cellDescripcionDown);

            if (sectionCalendar == null) {
                tableParent.addCell(tableChild);
            }

        } catch (BadElementException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfDocument.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableChild;
    }

    protected void displayTextPDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPCell pdfPCell = null;
        try {
            String textSection = objectInfoBO.getData();
            textSection = textSection.replaceAll("<ul>", " ");
            textSection = textSection.replaceAll("</ul>", " ");
            textSection = textSection.replaceAll("<li>", "<p>• ");
            textSection = textSection.replaceAll("</li>", "</p>");

            List<List<ChunksAndProperties>> ps = UtileriasPDF.parseToList_P(textSection);
            /*if(ps.size() <= 0)
             ps = UtileriasPDF.parseToList(sBuff.toString(), UtileriasPDF.HTML_TAG_BODY);*/

            pdfPCell = new PdfPCell();
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPCell.setNoWrap(false);
            pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);

            Font font = new Font(UtileriasPDF.fontTexto);
            font.setColor(UtileriasPDF.fgColorTexto);

            for (int k = 0; k < ps.size(); ++k) {

                Paragraph para = new Paragraph();
                para.setFont(font);
                para.setLeading(12f);

                int numPara = 0;
                for (ChunksAndProperties pC : ps.get(k)) {

                    /*if(pC.getChunk().getContent().trim().isEmpty())
                     continue;*/
                    Chunk chunk = pC.getChunk();

                    if (!chunk.getContent().trim().isEmpty() && numPara < (ps.get(k).size() - 1)) {
                        chunk.append(" ");
                    }
                    Font fontChunk = null;
                    if (objectInfoBO.isHeader()) {
                        fontChunk = new Font(UtileriasPDF.fontHeader);
                        fontChunk.setColor(UtileriasPDF.fgColorHeader);
                    } else if (objectInfoBO.isSection()) {
                        fontChunk = new Font(UtileriasPDF.fontSection);
                        fontChunk.setColor(UtileriasPDF.fgColorSection);
                        chunk.setBackground(UtileriasPDF.bgColorSection);
                    } else {
                        fontChunk = new Font(UtileriasPDF.fontTexto);
                        fontChunk.setColor(UtileriasPDF.fgColorTexto);
                        fontChunk.setStyle((pC.isBold() ? Font.BOLD : Font.NORMAL) | (pC.isItalic() ? Font.ITALIC : Font.NORMAL) | (pC.isUnderline() ? Font.UNDERLINE : Font.NORMAL) | (pC.isStrike() ? Font.STRIKETHRU : Font.NORMAL));
                    }

                    chunk.setFont(fontChunk);

                    para.add(chunk);
                }

                if (UtileriasPDF.TEXT_ALIGN_RIGHT.equals(ps.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_RIGHT);
                } else if (UtileriasPDF.TEXT_ALIGN_CENTER.equals(ps.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_CENTER);
                } else {
                    para.setAlignment(Element.ALIGN_JUSTIFIED);
                }

                if ((k + 1) < ps.size() && ps.get(k + 1).get(0).getChunk().toString().trim().isEmpty()) {
                    boolean aplicarEspacio = true;
                    for (int i = 0; i < ps.get(k + 1).size(); i++) {
                        if (ps.get(k + 1).get(i).getChunk().toString().trim().isEmpty() == false) {
                            aplicarEspacio = false;
                            break;
                        }
                    }

                    if (aplicarEspacio) {
                        para.setSpacingAfter(11.5f);
                    }
                }

                pdfPCell.addElement(para);
                if (objectInfoBO.isHeader()) {
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                } else if (objectInfoBO.isSection()) {
                    pdfPCell.setBackgroundColor(UtileriasPDF.bgColorSection);
                    pdfPCell.setPaddingLeft(5f);
                    pdfPCell.setPaddingTop(5f);
                }

                numPara++;
            }

            tableParent.addCell(pdfPCell);
        } catch (Exception ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    protected void displayBulletByPsPDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPCell pdfPCell = null;
        try {
            String textSection = objectInfoBO.getData();
            textSection = textSection.replaceAll("<ul>", " ");
            textSection = textSection.replaceAll("</ul>", " ");
            textSection = textSection.replaceAll("<li>", "<p>");
            textSection = textSection.replaceAll("</li>", "</p>");

            List<List<ChunksAndProperties>> ps = UtileriasPDF.parseToList_P(textSection);
            /*if(ps.size() <= 0)
             ps = UtileriasPDF.parseToList(sBuff.toString(), UtileriasPDF.HTML_TAG_BODY);*/

            pdfPCell = new PdfPCell();
            pdfPCell.setBorder(Rectangle.BOTTOM);
            pdfPCell.setBorderColor(WebColors.getRGBColor("#464646"));
            pdfPCell.setBorderWidth(0.5f);
            pdfPCell.setNoWrap(false);
            pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);

            Image image1 = getBulletImage();
            image1.scaleAbsoluteHeight(25f);
            image1.scaleAbsoluteWidth(25f);

            com.itextpdf.text.List lstRes;
            lstRes = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10f);
            lstRes.setListSymbol(new Chunk(image1, 0, 0));

            for (int k = 0; k < ps.size(); ++k) {

                //Paragraph para = new Paragraph();
                //para.setFont(font);
                //para.setLeading(12f);
                com.itextpdf.text.ListItem litem = new com.itextpdf.text.ListItem();
                for (ChunksAndProperties pC : ps.get(k)) {

                    /*if(pC.getChunk().getContent().trim().isEmpty())
                     continue;*/
                    Chunk chunk = pC.getChunk();

                    Font fontChunk = new Font(UtileriasPDF.fontBullet);
                    fontChunk.setColor(UtileriasPDF.fgColorTexto);
                    //fontChunk.setStyle( (pC.isBold() ? Font.BOLD : Font.NORMAL) | (pC.isItalic() ? Font.ITALIC : Font.NORMAL) | (pC.isUnderline() ? Font.UNDERLINE : Font.NORMAL) | (pC.isStrike() ? Font.STRIKETHRU : Font.NORMAL));
                    chunk.setFont(fontChunk);

                    //para.add(chunk);
                    litem.add(chunk);

                    if (chunk.getContent().isEmpty()) {
                        litem.setSpacingAfter(5f);
                        litem.setSpacingBefore(5f);
                    }
                }

                litem.setAlignment(Element.ALIGN_JUSTIFIED);
                //litem.setSpacingAfter(1f);
                //litem.setSpacingBefore(1f);
                litem.setLeading(10f);
                lstRes.add(litem);

                /*if ( UtileriasPDF.TEXT_ALIGN_RIGHT.equals( ps.get(k).get(0).getAlign() ) )
                 para.setAlignment(Element.ALIGN_RIGHT);
                 else if ( UtileriasPDF.TEXT_ALIGN_CENTER.equals( ps.get(k).get(0).getAlign() ) )
                 para.setAlignment(Element.ALIGN_CENTER);
                 else
                 para.setAlignment(Element.ALIGN_JUSTIFIED);
                
                 if((k+1) < ps.size() && ps.get(k+1).get(0).getChunk().toString().trim().isEmpty())
                 para.setSpacingAfter(10f);*/
            }
            pdfPCell.addElement(lstRes);

            tableParent.addCell(pdfPCell);
        } catch (Exception ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    protected void displayBulletPDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPCell pdfPCell = null;
        try {
            StringBuilder sBuff = new StringBuilder(objectInfoBO.getData());
            //Document document = new Document();
            StyleSheet st = new StyleSheet();
            //st.loadTagStyle("body", "font-family", "arial, sans-serif");
            //st.loadTagStyle("body", "leading", "10px");
            st.loadTagStyle("body", "size", "10px");
            //st.loadStyle("body", "background-color", "#FF8040");

            //st.loadTagStyle("p", "font-family", "arial, sans-serif");
            //st.loadTagStyle("p", "leading", "10px");
            st.loadTagStyle("p", "size", "10px");

            StringReader stringReader = new StringReader(sBuff.toString());
            List<Element> p = HTMLWorker.parseToList(stringReader, st);

            pdfPCell = new PdfPCell();
            pdfPCell.setBorder(Rectangle.BOTTOM);
            pdfPCell.setBorderColor(WebColors.getRGBColor("#464646"));
            pdfPCell.setBorderWidth(0.5f);
            pdfPCell.setNoWrap(false);
            pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);

            Font font = new Font(UtileriasPDF.fontBullet);
            font.setColor(UtileriasPDF.fgColorTexto);

            Image image1 = getBulletImage();
            image1.scaleAbsoluteHeight(25f);
            image1.scaleAbsoluteWidth(25f);

            com.itextpdf.text.List lstRes;
            lstRes = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10f);
            lstRes.setListSymbol(new Chunk(image1, 0, 0));

            for (int k = 0; k < p.size(); ++k) {
                if (p.get(k) instanceof com.itextpdf.text.List) {
                    com.itextpdf.text.List listaHTML = (com.itextpdf.text.List) p.get(k);

                    if (listaHTML.isEmpty()) {
                        continue;
                    }

                    for (Element li : listaHTML.getItems()) {
                        if (li.getChunks().isEmpty()) {
                            continue;
                        }
                        Chunk chuckItem = li.getChunks().get(0);
                        chuckItem.setFont(font);
                        com.itextpdf.text.ListItem litem = new com.itextpdf.text.ListItem(chuckItem);
                        litem.setAlignment(Element.ALIGN_JUSTIFIED);
                        litem.setSpacingAfter(1f);
                        litem.setSpacingBefore(1f);
                        lstRes.add(litem);
                    }

                    //pdfPCell.addElement(lstRes);
                } else {
                    if (p.get(k).getChunks().get(0) != null && p.get(k).getChunks().get(0).getContent().trim().length() > 1) {
                        Chunk chuckItem = new Chunk(p.get(k).getChunks().get(0));
                        chuckItem.setFont(font);
                        com.itextpdf.text.ListItem litem = new com.itextpdf.text.ListItem(chuckItem);
                        litem.setAlignment(Element.ALIGN_JUSTIFIED);
                        litem.setSpacingAfter(1f);
                        litem.setSpacingBefore(1f);
                        lstRes.add(litem);
                    }
                }
            }
            pdfPCell.addElement(lstRes);
            //pdfPCell.setBackgroundColor(new BaseColor(255, 204, 153));

            tableParent.addCell(pdfPCell);
        } catch (IOException ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    protected PdfPTable displayVideoPDF(PdfPTable tableParent, ModuleSectionBO section, ObjectInfoBO objectInfoBO) {
        PdfPTable tableChild = new PdfPTable(1);
        //TODO nuevo para errores
        tableChild.setSplitLate(false);
        tableChild.setSplitRows(false);
        tableChild.setExtendLastRow(false);
        int espacioTextos = 60;

        try {
            String fileName = objectInfoBO.getThumbnailUrl();

            // Add titulo
            Font fTit = UtileriasPDF.fontTitulo;
            fTit.setColor(UtileriasPDF.fgColorTitulo);
            Paragraph p = new Paragraph(objectInfoBO.getTitulo() == null || objectInfoBO.getTitulo().isEmpty() ? "" : objectInfoBO.getTitulo(), fTit);
            p.setAlignment(Element.ALIGN_JUSTIFIED);

            PdfPCell cellTitulo = new PdfPCell();
            cellTitulo.addElement(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setFixedHeight(CELL_FIXED_HEIGHT);
            cellTitulo.setPadding(1f);
            tableChild.addCell(cellTitulo);

            // Add subtitulo 
            if (objectInfoBO.getSubTitulo() != null && !objectInfoBO.getSubTitulo().isEmpty()) {
                Font fSub = UtileriasPDF.fontSubtitulo;
                fSub.setColor(UtileriasPDF.fgColorSubtitulo);
                Paragraph p1 = new Paragraph(objectInfoBO.getSubTitulo() == null || objectInfoBO.getSubTitulo().isEmpty() ? "" : objectInfoBO.getSubTitulo(), fSub);
                p1.setAlignment(Element.ALIGN_JUSTIFIED);
                PdfPCell cellSubtitulo = new PdfPCell();
                cellSubtitulo.addElement(p1);
                cellSubtitulo.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
                cellSubtitulo.setBorder(Rectangle.NO_BORDER);
                cellSubtitulo.setFixedHeight(CELL_FIXED_HEIGHT);
                cellSubtitulo.setPadding(1f);
                tableChild.addCell(cellSubtitulo);
                espacioTextos = 70;
            }

            // Add image            
            BufferedImage bi_ = objectInfoBO.getImageThumb() == null ? objectInfoBO.getImage() : objectInfoBO.getImageThumb(); //Image.getInstance(fileName);

            Image image1 = Image.getInstance(bi_, null);
            if (image1 != null) {
                //float ancho = image1.getWidth();
                //float alto = image1.getHeight();

                float maxWidth = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                float maxHeight = section.getHeight() - MARGIN_CELL_DESCRIPTION;

                //float old_x = ancho;
                //float old_y = alto;
                float new_w = maxWidth;
                float new_h = maxHeight;

                /*float new_w = section.getWidth() - LEFT_RIGHT_CELL_MARGIN;
                 float new_h = section.getHeight() - resizePage - CELL_FIXED_HEIGHT;

                 if (image1.getWidth() > new_w || image1.getHeight() > new_h){
                 image1.scaleToFit(new_w, new_h);
                 }*/
                float ratio1 = image1.getWidth() / new_w;
                float ratio2 = image1.getHeight() / new_h;

                float thumb_w = 0, thumb_h = 0;
                if (ratio1 > ratio2) {
                    thumb_w = new_w;
                    thumb_h = image1.getHeight() / ratio1;
                } else {
                    thumb_w = image1.getWidth() / ratio2;
                    thumb_h = new_h;
                }
                //image1.scaleAbsolute((float) Math.floor(thumb_w-5), (float) Math.floor(thumb_h-5));
                image1.scaleAbsoluteHeight((float) (section.getHeight() - espacioTextos));
                image1.scaleAbsoluteWidth((float) ((section.getWidth() - 10) * 0.9));
                try {//FIXME Bloque Try para esperar la actualizacion de la escalacion de la imagen
                    Thread.sleep(50);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }

                PdfPCell cellImage = new PdfPCell(image1);
                cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellImage.setBackgroundColor(UtileriasPDF.bgColorImagen);
                cellImage.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
                cellImage.setPaddingTop(5f);
                cellImage.setPaddingBottom(3f);

                //cellImage.setFixedHeight(image1.getHeight());// - MARGIN_CELL_DESCRIPTION);
                tableChild.addCell(cellImage);
            }

            // Add comments
            Font fCom = UtileriasPDF.fontDescripcion;
            fCom.setColor(UtileriasPDF.fgColorDescripcion);
            Paragraph p2 = new Paragraph(objectInfoBO.getComentarios() == null || objectInfoBO.getComentarios().isEmpty() ? " Fuente: " : " Fuente: " + objectInfoBO.getComentarios(), fCom);
            p2.setLeading(7f);
            p2.setAlignment(Element.ALIGN_JUSTIFIED);
            p2.add(0, new Chunk(getDescripImage(), 0, 0));
            PdfPCell cellDescripcion = new PdfPCell();
            cellDescripcion.addElement(p2);
            //cellDescripcion.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcion.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
            cellDescripcion.setFixedHeight(15f);
            if (sectionCalendar != null) {
                cellDescripcion.setFixedHeight(25f);
            }
            cellDescripcion.setVerticalAlignment(Element.ALIGN_CENTER);
            cellDescripcion.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cellDescripcionDown = new PdfPCell();
            //cellDescripcionDown.setBackgroundColor(UtileriasPDF.bgColorDescripcion);
            cellDescripcionDown.setBorder(Rectangle.NO_BORDER);//(Rectangle.NO_BORDER);
            cellDescripcionDown.setNoWrap(false);
            cellDescripcionDown.setVerticalAlignment(Element.ALIGN_TOP);
            cellDescripcionDown.setHorizontalAlignment(Element.ALIGN_LEFT);
            //tableChild.addCell(cellDescripcionDown);

            tableChild.addCell(cellDescripcion);

            if (sectionCalendar == null) {
                tableParent.addCell(tableChild);
            }

        } catch (BadElementException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return tableChild;
    }

    protected void displayObjects(PdfPTable tableParent, ModuleSectionBO section) {
        if (section == null) {
            return;
        }
        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType()) == false) {
            return;
        }
        ObjectInfoBO objectInfo = null;

        PdfPCell cell = null;

        try {
            if (section.getLstObjects() != null && section.getLstObjects().size() > 0) {
                objectInfo = section.getLstObjects().get(0);

                if (objectInfo != null) {
                    if (sectionCalendar != null && sectionCalendarMultimedia == null) {
                        sectionCalendarMultimedia = new ArrayList<>();
                    }

                    if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {
                        objectInfo = getObjectInfoBOValido(section.getLstObjects());
                        if (sectionCalendar == null) {
                            displayImagePDF(tableParent, section, objectInfo);
                        } else {
                            PdfPTable tableChild = displayImagePDF(tableParent, section, objectInfo);
                            tableChild.setWidthPercentage(90f);
                            sectionCalendarMultimedia.add(tableChild);
                        }
                    } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
                        objectInfo = getObjectInfoBOValido(section.getLstObjects());
                        if (sectionCalendar == null) {
                            displayExcelPDF(tableParent, section, objectInfo);
                        } else {
                            PdfPTable tableChild = displayExcelPDF(tableParent, section, objectInfo);
                            tableChild.setWidthPercentage(90f);
                            sectionCalendarMultimedia.add(tableChild);
                        }
                    } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_TEXT) {
                        displayTextPDF(tableParent, section, objectInfo);
                    } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_VIDEO) {
                        objectInfo = getObjectInfoBOValido(section.getLstObjects());
                        if (sectionCalendar == null) {
                            displayVideoPDF(tableParent, section, objectInfo);
                        } else {
                            PdfPTable tableChild = displayVideoPDF(tableParent, section, objectInfo);
                            tableChild.setWidthPercentage(90f);
                            sectionCalendarMultimedia.add(tableChild);
                        }
                    } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_CALENDAR) {
                        sectionCalendar = displayCalendar(section);
                    } else if (objectInfo.getObjType() == GlobalDefines.OBJ_TYPE_BULLET) {
                        //displayBulletPDF(tableParent, section, objectInfo);
                        displayBulletByPsPDF(tableParent, section, objectInfo);
                    }
                }
            }

        } catch (Exception ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }

    protected ObjectInfoBO getObjectInfoBOValido(List<ObjectInfoBO> listObjBO) {
        List<ObjectInfoBO> listTemp = listObjBO;

        Collections.sort(listTemp, new Comparator<ObjectInfoBO>() {

            @Override
            public int compare(ObjectInfoBO o1, ObjectInfoBO o2) {
                return new Integer(o1.getOrderId()).compareTo(o2.getOrderId());
            }
        });

        for (ObjectInfoBO objBO : listTemp) {
            if (objBO.getHtmlVideo() != null || objBO.getFile() != null || objBO.getFile().isEmpty() == false) {
                if (objBO.isDelete() == false) {
                    return objBO;
                }
            }
        }

        return null;
    }

    protected PdfPTable getDocumentTitle(DocumentBO docBO) {
        List<String> titulos = Utilerias.getTitulo(docBO);
        PdfPTable tableChild = new PdfPTable(1);
        try {
            tableChild.setWidthPercentage(90f);
            tableChild.setSkipFirstHeader(true);
            tableChild.setSkipLastFooter(true);
            tableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableChild.setSplitLate(false);
            tableChild.setSplitRows(false);
            tableChild.setExtendLastRow(false);

            // Add espacio Header1 <> Header2
            PdfPCell cellSpace = new PdfPCell();
            cellSpace.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellSpace.setBorder(Rectangle.NO_BORDER);
            cellSpace.setColspan(1);
            cellSpace.setPaddingTop(0);
            cellSpace.setPaddingBottom(0);
            cellSpace.setFixedHeight(getSpaceHeaderToHeader(docBO));
            tableChild.addCell(cellSpace);

            // Add titulo
            Font fTit = UtileriasPDF.fontTituloPDF;
            fTit.setColor(UtileriasPDF.fgColorTituloPDF);
            Phrase p = new Phrase(titulos.get(TIPO_DOC), fTit);
            PdfPCell cellTitulo = new PdfPCell(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setColspan(1);
            cellTitulo.setPaddingTop(5);
            cellTitulo.setPaddingBottom(0);
            cellTitulo.setFixedHeight(39f);
            tableChild.addCell(cellTitulo);

            //Validar contenido del tema
            String tema = titulos.get(TITULO_COD).trim();
            tema = ":".equals(tema) ? "" : tema;

            // Add tema 
            Font fSub = UtileriasPDF.fontTemaPDF;
            fSub.setColor(BaseColor.BLACK);
            Phrase p1 = new Phrase(String.format(UtileriasPDF.TEMA_PDF, tema), fSub);
            PdfPCell cellSubtitulo = new PdfPCell(p1);
            cellSubtitulo.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
            cellSubtitulo.setBorder(Rectangle.NO_BORDER);
            cellSubtitulo.setPaddingTop(5);
            cellSubtitulo.setPaddingBottom(0);
            cellSubtitulo.setFixedHeight(60);//
            tableChild.addCell(cellSubtitulo);
            tableChild.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableChild.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        } catch (Exception ex) {
            tableChild = null;
            Utilerias.logger(getClass()).error(ex);
        }
        return tableChild;
    }

    private float getSpaceHeaderToHeader(DocumentBO docBO) {
        float retVal = 0f;
        try {
            TemplateDAO tmpDAO = new TemplateDAO();
            Set set = docBO.getMapHojas().keySet();
            Iterator it = set.iterator();

            Integer numHoja = null;
            HojaBO hojaBO = null;
            List<ModuleBO> lstModules = null;
            if (it.hasNext()) {
                numHoja = (Integer) it.next();
                hojaBO = docBO.getMapHojas().get(numHoja);

                if (hojaBO != null) {
                    lstModules = hojaBO.getLstModules();
                    if (lstModules != null) {
                        int templateId = lstModules.get(0).getTemplateId();
                        if (templateId > 0) {
                            TemplateBO plantilla = tmpDAO.getTemplate(templateId);
                            retVal = plantilla.getHeightTitle();
                        } else {
                            retVal = 0f;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal = 0f;
        }
        return retVal;
    }

    protected PdfPTable getDocumentTitleCalendar(String titulo) {
        PdfPTable tableChild = new PdfPTable(1);
        try {
            tableChild.setWidthPercentage(90f);
            tableChild.setSkipFirstHeader(true);
            tableChild.setSkipLastFooter(true);
            tableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableChild.setSplitLate(false);
            tableChild.setSplitRows(false);
            tableChild.setExtendLastRow(false);

            // Add titulo
            Font fTit = UtileriasPDF.fontTituloPDF;
            fTit.setColor(UtileriasPDF.fgColorTituloPDF);
            Phrase p = new Phrase(titulo, fTit);
            PdfPCell cellTitulo = new PdfPCell(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setColspan(1);
            cellTitulo.setPaddingBottom(15f);
            tableChild.addCell(cellTitulo);

            tableChild.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableChild.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
        } catch (Exception ex) {
            tableChild = null;
            Utilerias.logger(getClass()).error(ex);
        }
        return tableChild;
    }

    protected PdfPTable getFooterAutor(DocumentBO docBO) {
        PdfPTable tableChild = new PdfPTable(3);
        try {
            tableChild.setWidthPercentage(90f);
            tableChild.setSkipFirstHeader(true);
            tableChild.setSkipLastFooter(true);
            tableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableChild.setSplitLate(false);
            tableChild.setSplitRows(false);
            tableChild.setExtendLastRow(false);

            // Add titulo
            /*Font fTit = UtileriasPDF.fontTituloPDF;
             fTit.setColor(UtileriasPDF.fgColorTituloPDF);
             Phrase p = new Phrase(UtileriasPDF.TITULO_DOCUMENTO, fTit);
             PdfPCell cellTitulo = new PdfPCell(p);
             cellTitulo.setBackgroundColor(UtileriasPDF.bgColorTitulo);
             cellTitulo.setBorder(Rectangle.NO_BORDER);
             cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
             cellTitulo.setColspan(1);
             cellTitulo.setPaddingTop(5);
             cellTitulo.setPaddingBottom(5);
             tableChild.addCell(cellTitulo);*/
            // Add tema 
            Font fSub = UtileriasPDF.fontTexto;// new Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL);
            fSub.setColor(BaseColor.BLACK);

            Phrase p1 = new Phrase(String.format(UtileriasPDF.TEMA_PDF, getFechaFooter()), fSub);

            PdfPCell impresionFecha = new PdfPCell(p1);
            impresionFecha.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
            impresionFecha.setBorder(Rectangle.NO_BORDER);
            impresionFecha.setVerticalAlignment(Element.ALIGN_CENTER);
            impresionFecha.setPaddingTop(4);
            tableChild.addCell(impresionFecha);

            Font fSub2 = UtileriasPDF.fontTexto;// new Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL);
            fSub2.setColor(BaseColor.BLACK);

            Map<String, List<String>> lstAutores = getAutor(docBO);

            PdfPCell impresionNombre = new PdfPCell();
            int cont = 0;
            if (lstAutores != null) {
                for (String nombre : lstAutores.get(NOMBRE_AUTOR)) {
                    Phrase pname = new Phrase(String.format(UtileriasPDF.TEMA_PDF, nombre), fSub2);
                    impresionNombre.addElement(pname);
                    if (cont >= 2) {
                        break;
                    } else {
                        cont++;
                    }
                }
            }

            Phrase pTwitter = new Phrase(String.format(UtileriasPDF.TEMA_PDF, " @adinfi"), fSub2);
            pTwitter.add(0, new Chunk(getTwitterImage(), 0, 0));
            impresionNombre.addElement(pTwitter);
            impresionNombre.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
            impresionNombre.setBorder(Rectangle.NO_BORDER);
            impresionNombre.setVerticalAlignment(Element.ALIGN_TOP);
            impresionNombre.setHorizontalAlignment(Element.ALIGN_LEFT);
            impresionNombre.setPadding(0);
            tableChild.addCell(impresionNombre);

            PdfPCell impresionCorreo = new PdfPCell();
            cont = 0;
            if (lstAutores != null) {
                for (String correx : lstAutores.get(CORREO_EXT)) {
                    Phrase pcorrex = new Phrase(String.format(UtileriasPDF.TEMA_PDF, correx), fSub2);
                    impresionCorreo.addElement(pcorrex);
                    if (cont >= 2) {
                        break;
                    } else {
                        cont++;
                    }
                }
            }

            Phrase p32 = new Phrase(String.format(UtileriasPDF.TEMA_PDF, "Teléfono: (81) 8345-9584"), fSub2);
            impresionCorreo.addElement(p32);
            impresionCorreo.setBackgroundColor(UtileriasPDF.bgColorSubtitulo);
            impresionCorreo.setBorder(Rectangle.NO_BORDER);
            impresionCorreo.setPadding(0);
            tableChild.addCell(impresionCorreo);

            tableChild.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableChild.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        } catch (Exception ex) {
            tableChild = null;
            Utilerias.logger(getClass()).error(ex);
        }
        return tableChild;
    }

    private Map<String, List<String>> getAutor(DocumentBO docBO) {
        Map<String, List<String>> retVal = new HashMap<>();
        List<String> retValNom = new ArrayList<>();
        List<String> retValCE = new ArrayList<>();

        if (docBO == null) {
//            retValNom.add(InstanceContext.getInstance().getUsuario().getNombre());
//            retValCE.add(InstanceContext.getInstance().getUsuario().getCorreo() + " x" + InstanceContext.getInstance().getUsuario().getExtension());
              retValNom.add("AdPublish");
              retValCE.add("adpublish@adinfi.com");
            retVal.put(NOMBRE_AUTOR, retValNom);
            retVal.put(CORREO_EXT, retValCE);
            return retVal;
        }

        SendPublishDAO spDAO = new SendPublishDAO();
        SendPublishAuthorsDAO spaDAO = new SendPublishAuthorsDAO();
        List<SendPublishBO> spBOLst = spDAO.get(docBO.getDocumentId());
        if (spBOLst == null || spBOLst.size() <= 0 || docBO.getDocumentId() == 0) {
            //retValNom.add(InstanceContext.getInstance().getUsuario().getNombre());
            //retValCE.add(InstanceContext.getInstance().getUsuario().getCorreo() + " x" + InstanceContext.getInstance().getUsuario().getExtension());
            retValNom.add("AdPublish");
            retValCE.add("adpublish@adinfi.com");
            retVal.put(NOMBRE_AUTOR, retValNom);
            retVal.put(CORREO_EXT, retValCE);
            return retVal;
        }

        SendPublishBO spBO = spBOLst.get(0);
        List<SendPublishAuthorsBO> spaBOLst = spaDAO.get(spBO.getIdDocument_send());
        if (spaBOLst == null || spaBOLst.size() <= 0) {
            return null;
        }

        List<com.adinfi.formateador.bos.seguridad.Usuario> result = new ArrayList<>();
        //list = UtileriasWS.getAllUsers();
        Usuario usr = new Usuario();
        usr.setNombre("AdPublish");
        usr.setCorreo("adpublish@adinfi.com");
        usr.setUsuarioId(1);
        usr.setUsuarioNT("adPublish");
        usr.setExtension("101");
        result.add(usr);
        
//        List<com.adinfi.formateador.bos.seguridad.Usuario> result = new ArrayList<>();
//        for (SendPublishAuthorsBO autSend : spaBOLst) {
//            for (Usuario us : list) {
//                if (us.getUsuarioId() == autSend.getId_author()) {
//                    result.add(us);
//                    break;
//                }
//            }
//        }

        if (result.size() <= 0) {
            return null;
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

        for (Usuario user : result) {
            retValNom.add(user.getNombre());
            retValCE.add(user.getCorreo() + " x" + user.getExtension());
        }

        retVal.put(NOMBRE_AUTOR, retValNom);
        retVal.put(CORREO_EXT, retValCE);
        return retVal;
    }

    private String getFechaFooter() {
        String retVal;
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        retVal = format.format(now);
        return WordUtils.capitalize(retVal);
    }

    protected PdfPTable getHeaderMod(String headerName) {
        PdfPTable tableChild = new PdfPTable(1);
        try {
            tableChild.setWidthPercentage(100f);
            tableChild.setSkipFirstHeader(true);
            tableChild.setSkipLastFooter(true);
            tableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableChild.setSplitLate(false);
            tableChild.setSplitRows(false);
            tableChild.setExtendLastRow(false);

            // Add titulo
            Font fTit = UtileriasPDF.fontHeader;
            fTit.setColor(UtileriasPDF.fgColorHeader);
            Phrase p = new Phrase(headerName, fTit);
            PdfPCell cellTitulo = new PdfPCell(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorHeader);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setColspan(1);
            cellTitulo.setPaddingTop(5);
            cellTitulo.setPaddingBottom(5);
            tableChild.addCell(cellTitulo);

        } catch (Exception ex) {
            tableChild = null;
            Utilerias.logger(getClass()).error(ex);
        }
        return tableChild;
    }

    protected PdfPTable getSectionMod(String sectionName) {
        PdfPTable tableChild = new PdfPTable(1);
        try {
            tableChild.setWidthPercentage(30f);
            tableChild.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            tableChild.setSkipFirstHeader(true);
            tableChild.setSkipLastFooter(true);
            tableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableChild.setSplitLate(false);
            tableChild.setSplitRows(false);
            tableChild.setExtendLastRow(false);

            // Add titulo
            Font fTit = UtileriasPDF.fontSection;
            fTit.setColor(UtileriasPDF.fgColorSection);
            Phrase p = new Phrase(sectionName, fTit);
            PdfPCell cellTitulo = new PdfPCell(p);
            cellTitulo.setBackgroundColor(UtileriasPDF.bgColorSection);
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setVerticalAlignment(Element.ALIGN_TOP);
            cellTitulo.setColspan(1);
            cellTitulo.setPaddingTop(5);
            cellTitulo.setPaddingBottom(5);
            tableChild.addCell(cellTitulo);
        } catch (Exception ex) {
            tableChild = null;
            Utilerias.logger(getClass()).error(ex);
        }
        return tableChild;
    }

    private void addLayout(PdfPTable tableParent, ModuleSectionBO section, ModuleSectionBO parentSection) {

        List<ModuleSectionBO> sectionsChilds = section.getChildSectionsAsList();
        int numCols = 1;
        float[] widths = null;
        switch (section.getType()) {
            case GlobalDefines.SEC_TYPE_SHEET:
                if (sectionsChilds != null && sectionsChilds.size() > 0) {
                    widths = new float[sectionsChilds.size()];
                    int i = 0;
                    for (ModuleSectionBO secTmp : sectionsChilds) {
                        if (secTmp == null) {
                            continue;
                        }
                        widths[i++] = secTmp.getWidth();
                    }
                }
                if (sectionsChilds != null && sectionsChilds.size() > 0) {
                    numCols = sectionsChilds.size();
                }
                break;
            case GlobalDefines.SEC_TYPE_COLUMN:
                widths = new float[1];
                widths[0] = section.getWidth();
                break;
            case GlobalDefines.SEC_TYPE_CELL:
                ModuleSectionBO modSecBO = moduleSectionResp.get(section.getSectionId());
                section.setHeight(modSecBO.getHeight());
                if (parentSection.getChildSectionsAsList() == null || parentSection.getChildSectionsAsList().isEmpty()) {
                    SectionInfoBO secInfo = new SectionInfoBO();
                    secInfo.setSectionName(section.getSectionName());
                    displayObjects(tableParent, section);
                } else {
                    //Iteración para hijos de parent.
                    PdfPTable child = new PdfPTable(1);
                    child.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    SectionInfoBO secInfo = new SectionInfoBO();
                    secInfo.setSectionName(section.getSectionName());
                    displayObjects(child, section);
                    tableParent.addCell(child);
                }
                return;
        }

        PdfPTable content = null;
        PdfPTable child = null;
        resizePage = 0f;
        if (sectionsChilds != null) {
            content = new PdfPTable(numCols);
            try {
                if (widths != null) {
                    content.setWidths(widths);
                }

            } catch (DocumentException ex) {
                LOG.log(Level.ALL, ex.getMessage());
            }

            for (ModuleSectionBO secTmp : sectionsChilds) {
                if (secTmp == null) {
                    continue;
                }
                child = new PdfPTable(1);
                child.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                if (GlobalDefines.SEC_TYPE_CELL.equals(secTmp.getType())) {
                    ModuleSectionBO modSecBO = moduleSectionResp.get(secTmp.getSectionId());
                    secTmp.setHeight(modSecBO.getHeight());
                    secTmp.setHeight((short) (secTmp.getHeight() - (resizePage / sectionsChilds.size())));
                }

                addLayout(child, secTmp, section);
                PdfPCell cell = new PdfPCell(child);
                cell.setBorder(Rectangle.NO_BORDER);
                switch (section.getType()) {
                    case GlobalDefines.SEC_TYPE_COLUMN:
                        cell.setFixedHeight(secTmp.getHeight());
                        break;
                    case GlobalDefines.SEC_TYPE_SHEET:
                        break;
                }

                content.addCell(cell);
            }
            PdfPCell cellPar = new PdfPCell(content);
            section.setHeight((short) (section.getHeight() - resizePage));
            cellPar.setFixedHeight(section.getHeight());
            cellPar.setBorder(Rectangle.NO_BORDER);
            tableParent.addCell(cellPar);
        }
    }

    private DocumentTypeBO getIdMiVectorByDocumentType() {
        DocumentTypeBO docTypeBO = null;

        try {
            DocumentTypeDAO dDAO = new DocumentTypeDAO();
            List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);

            for (DocumentTypeBO d : dBO) {
                if (d.getIddocument_type() == docBOGlobal.getIdDocType()) {
                    docTypeBO = d;
                    break;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

        return docTypeBO;
    }

    private PdfPTable displayDirectory() {
        Font fontHeader = FontFactory.getFont("Verdana", 14, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 14f, Font.NORMAL);
        fontHeader.setColor(BaseColor.BLACK);

        Font fontContTit = FontFactory.getFont("Verdana", 8, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL);
        fontContTit.setColor(BaseColor.BLACK);

        Font fontPrimCont = FontFactory.getFont("Verdana", 12, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL);
        fontPrimCont.setColor(WebColors.getRGBColor("#FF6532"));

        Font fontCont = FontFactory.getFont("Verdana", 6, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 6f, Font.NORMAL);
        fontCont.setColor(BaseColor.BLACK);

        PdfPTable dir = new PdfPTable(1);
        dir.setWidthPercentage(90f);
        dir.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell hojaDir = new PdfPCell();
        //hojaDir.setFixedHeight(650f);
        //hojaDir.setBackgroundColor(BaseColor.ORANGE);
        hojaDir.setPadding(0f);
        hojaDir.setBorder(Rectangle.NO_BORDER);

        /* Titulo de Directorio */
        PdfPTable t_uno = new PdfPTable(1);
        t_uno.setWidthPercentage(100f);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        Phrase phrase = new Phrase(new Chunk(getLibroImage(), 0, 0));
        phrase.add(new Phrase("Dirección de Análisis", fontHeader));
        cell.setPhrase(phrase);
        t_uno.addCell(cell);
        hojaDir.addElement(t_uno);

        /*Usuarios del Directorio*/
        List<Usuario> lUsuario = UtileriasWS.getDirectorio();
        Collections.sort(lUsuario, new CustomComparator());

        /* Contenido de Contactos */
        PdfPTable tcont = new PdfPTable(4);
        tcont.setWidthPercentage(100f);

        int cont = 0;
        if (lUsuario != null && lUsuario.size() > 0) {
            boolean primerReg = true;
            for (Usuario usuario : lUsuario) {
                if (primerReg) {
                    /* Primer Contacto */
                    t_uno = new PdfPTable(1);
                    t_uno.setWidthPercentage(100f);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.setPhrase(new Phrase(usuario.getNombre(), fontPrimCont));
                    t_uno.addCell(cell);

                    PdfPTable t_tres = new PdfPTable(3);
                    t_tres.setWidthPercentage(100f);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.setPhrase(new Phrase(usuario.getPerfil(), fontCont));
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.setPhrase(new Phrase(loadIndustry(usuario.getUsuarioId()), fontCont));
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    Phrase pmail = new Phrase(new Chunk(getMailImage(), 0, 0));
                    pmail.add(new Phrase("  " + usuario.getCorreo(), fontCont));
                    cell.setPhrase(pmail);
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.setPhrase(new Phrase("", fontCont));
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.setPhrase(new Phrase(loadEmisoras(usuario.getUsuarioId()), fontCont));
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    Phrase ptel = new Phrase(new Chunk(getTelefonoImage(), 0, 0));
                    ptel.add(new Phrase("  01800-000-5860 / x" + usuario.getExtension(), fontCont));
                    cell.setPhrase(ptel);
                    t_tres.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(WebColors.getRGBColor("#F1F3F7"));
                    cell.addElement(t_tres);
                    t_uno.addCell(cell);
                    hojaDir.addElement(t_uno);

                    primerReg = false;
                    continue;
                }
                //resto
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                Phrase contTit = new Phrase(usuario.getNombre(), fontContTit);
                Phrase contCgo = new Phrase(usuario.getPerfil(), fontCont);
                Phrase contSec = new Phrase(loadIndustry(usuario.getUsuarioId()), fontCont);
                Phrase contEmi = new Phrase(loadEmisoras(usuario.getUsuarioId()), fontCont);
                Phrase contCor = new Phrase(new Chunk(getMailImage(), 0, 0));
                contCor.add(new Phrase("  " + usuario.getCorreo(), fontCont));
                contCor.setLeading(10f);
                com.itextpdf.text.Image img = getTelefonoImage();
                img.scaleToFit(8f, 8f);
                Phrase contTel = new Phrase(new Chunk(img, 0, 0));
                contTel.add(new Phrase("  01800-000-5860 / x" + usuario.getExtension(), fontCont));
                contTel.setLeading(10f);
                cell.addElement(contTit);
                cell.addElement(contCgo);
                cell.addElement(contSec);
                cell.addElement(contEmi);
                cell.addElement(contCor);
                cell.addElement(contTel);
                tcont.addCell(cell);
                cont++;
                if (cont > 3) {
                    cont = 0;
                }
            }

            if (cont < 4 && cont > 0) {
                for (int i = cont; i < 4; i++) {
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    //cell.setPhrase(new Phrase("Contacto 5.", fontHeader));
                    tcont.addCell(cell);
                }
            }
        }

        hojaDir.addElement(tcont);

        dir.addCell(hojaDir);
        return dir;
    }

    private PdfPTable displayLocation() {
        Font fontHeader = FontFactory.getFont("Verdana", 14, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 14f, Font.NORMAL);
        fontHeader.setColor(BaseColor.BLACK);

        Font fontContTit = FontFactory.getFont("Verdana", 8, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL);
        fontContTit.setColor(BaseColor.BLACK);

        Font fontPrimCont = FontFactory.getFont("Verdana", 12, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL);
        fontPrimCont.setColor(WebColors.getRGBColor("#FF6532"));

        Font fontCont = FontFactory.getFont("Verdana", 6, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 6f, Font.NORMAL);
        fontCont.setColor(BaseColor.BLACK);

        PdfPTable dir = new PdfPTable(1);
        dir.setWidthPercentage(90f);
        dir.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell hojaDir = new PdfPCell();
        //hojaDir.setFixedHeight(650f);
        //hojaDir.setBackgroundColor(BaseColor.ORANGE);
        hojaDir.setPadding(0f);
        hojaDir.setBorder(Rectangle.NO_BORDER);

        /* Contenido de Ubicaciones */
        PdfPTable tcont = new PdfPTable(4);
        tcont.setWidthPercentage(100f);

        /* Titulo de Ubicacion */
        PdfPTable t_uno = new PdfPTable(1);
        t_uno.setWidthPercentage(100f);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(WebColors.getRGBColor("#ffefea"));
        Phrase pub = new Phrase(new Chunk(getUbicacionImage(), 0, 0));
        pub.add(new Phrase("Ubicación", fontHeader));
        cell.setPhrase(pub);
        t_uno.addCell(cell);
        hojaDir.addElement(t_uno);

        /* Ubicaciones */
        tcont = new PdfPTable(5);
        tcont.setWidthPercentage(100f);

        OfficeDAO dAO = new OfficeDAO();
        List<OfficeBO> list = dAO.get(null);
        int cont = 0;
        if (list != null && list.size() > 0) {
            for (OfficeBO office : list) {

                Phrase titSuc = new Phrase(office.getBranch().toUpperCase(), fontCont);

                StringBuffer direccion = new StringBuffer(office.getAddress())
                        .append(office.getDistrict().isEmpty() ? "" : ", " + office.getDistrict())
                        .append(office.getCity().isEmpty() ? "" : ", " + office.getCity())
                        .append(office.getState().isEmpty() ? "" : ", " + office.getState())
                        .append(office.getCountry().isEmpty() ? "" : ", " + office.getCountry());

                Phrase imgUb = new Phrase(new Chunk(getUbicacionSmallImage(), 0, 0));
                Phrase dirSuc = new Phrase(" " + direccion.toString(), fontCont);
                imgUb.add(dirSuc);
                imgUb.setLeading(10f);
                Phrase cpSuc = new Phrase("C.P. " + office.getZipCode(), fontCont);
                Phrase imgTel = new Phrase(new Chunk(getTelefonoSmallImage(), 0, 0));
                Phrase telSuc = new Phrase(" " + office.getPhone3(), fontCont);
                imgTel.add(telSuc);
                imgTel.setLeading(10f);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setMinimumHeight(60f);
                cell.addElement(titSuc);
                cell.addElement(imgUb);
                //cell.addElement(dirSuc);
                cell.addElement(cpSuc);
                cell.addElement(imgTel);
                //cell.addElement(telSuc);
                cell.setBackgroundColor(WebColors.getRGBColor("#ffefea"));
                tcont.addCell(cell);
                cont++;
                if (cont > 4) {
                    cont = 0;
                }
            }
            if (cont < 5 && cont > 0) {
                for (int i = cont; i < 5; i++) {
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    //cell.setPhrase(new Phrase("Contacto 5.", fontHeader));
                    cell.setBackgroundColor(WebColors.getRGBColor("#ffefea"));
                    tcont.addCell(cell);
                }
            }
        }

        hojaDir.addElement(tcont);

        dir.addCell(hojaDir);
        return dir;
    }

    private PdfPTable displayCalendar(ModuleSectionBO section) {
        PdfPTable calendar = new PdfPTable(1);
        calendar.setWidthPercentage(90f);
        calendar.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        titleCalendar = new PdfPTable(1);
        titleCalendar.setWidthPercentage(90f);
        titleCalendar.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        DocumentTypeBO docTypeBO = getIdMiVectorByDocumentType();

        PdfPTable content = null;
        final String[] diasSem = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        List<EventBO> lEvent = new ArrayList<>();
        try {
            switch (section.getContentType()) {
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR:
                    Font fontHeader = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
                    fontHeader.setColor(WebColors.getRGBColor("#596E81"));
                    Font fontColumn = new Font(Font.FontFamily.HELVETICA, 8f);
                    content = new PdfPTable(9);
                    content.setWidths(new float[]{7f, 6f, 9f, 28f, 9f, 11f, 11f, 9f, 10f});
                    content.setWidthPercentage(100f);

                    final String[] mesDesc = {"Ene.", "Feb.", "Mar.", "Abr.", "May.", "Jun.", "Jul.", "Ago.", "Sep.", "Oct.", "Nov.", "Dic."};
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase("FECHA", fontHeader));
                    cell.setBackgroundColor(WebColors.getRGBColor("#D0E0EF"));
                    cell.setBorderColor(WebColors.getRGBColor("#CECECE"));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("HORA", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("LUGAR", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("INDICADOR", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("PERIODO", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("ESTIMACIÓN VECTOR", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("ENCUESTA BLOOMBERG", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("OFICIAL", fontHeader));
                    content.addCell(cell);

                    cell.setPhrase(new Phrase("CIFRA ANTERIOR", fontHeader));
                    content.addCell(cell);

                    int idMiVector = 0;
                    try {
                        idMiVector = Integer.parseInt(docTypeBO.getIddocument_type_vector());
                    } catch (Exception e) {
                        idMiVector = 0;
                    }

                    lEvent = AgendaWS.getEventLst(idMiVector);
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setMinimumHeight(15f);
                    if (lEvent != null) {
                        int cont = 0;
                        for (EventBO event : lEvent) {
                            if (cont >= 40) {
                                break;
                            }

                            cell.setPhrase(new Phrase(mesDesc[event.getFechaCompleta().get(Calendar.MONTH)].concat(" ").concat(String.valueOf(event.getFechaCompleta().get(Calendar.DAY_OF_MONTH))), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(sdf.format(new Date(event.getFechaCompleta().getTimeInMillis())), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getLugar(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getEventoDesc(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getPeriodo(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getEstimacionVector(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getEstimacionBloomberg(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getCifraOficial(), fontColumn));
                            content.addCell(cell);

                            cell.setPhrase(new Phrase(event.getCifraOficialAnt(), fontColumn));
                            content.addCell(cell);
                            cont++;
                        }
                    }

                    titleCalendar.addCell(getDocumentTitleCalendar(docTypeBO.getName()));
                    PdfPCell hojaContent = new PdfPCell();
                    hojaContent.setFixedHeight(section.getHeight() + 30);
                    hojaContent.addElement(content);
                    hojaContent.setPadding(0f);
                    hojaContent.setBorder(Rectangle.NO_BORDER);
                    calendar.addCell(hojaContent);

                    break;
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_WEEK:
                    content = new PdfPTable(5);
                    content.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});
                    content.setWidthPercentage(90f);

                    fontColumn = new Font(Font.FontFamily.HELVETICA, 8f);
                    fontColumn.setColor(BaseColor.GRAY);
                    Font fontHeaderW = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
                    fontHeaderW.setColor(UtileriasPDF.bgColorHeader);

                    fontHeader = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
                    fontHeader.setColor(BaseColor.GRAY);

                    idMiVector = 0;
                    try {
                        idMiVector = Integer.parseInt(docTypeBO.getIddocument_type_vector());
                    } catch (Exception e) {
                        idMiVector = 0;
                    }

                    lEvent = AgendaWS.getEventLstWeek(idMiVector);
                    Calendar fi = AgendaWS.fechaIni;
                    Calendar ff = AgendaWS.fechaFin;

                    if (lEvent != null) {
                        PdfPTable subContent = new PdfPTable(2);
                        subContent.setWidthPercentage(90f);
                        String diaActual = "";
                        Map<String, Map> lEvenMaps = new HashMap<>();
                        Map<String, String> attEvnt = new HashMap<>();
                        for (EventBO event : lEvent) {

                            if (!diaActual.equals(event.getFecha())) {
                                attEvnt = new HashMap<>();
                                diaActual = event.getFecha();//event.getFechaCompleta().get(Calendar.DAY_OF_MONTH);
                                attEvnt.put("DIA_SEM", diasSem[event.getFechaCompleta().get(Calendar.DAY_OF_WEEK) - 1]);
                                attEvnt.put("DIA", Integer.toString(event.getFechaCompleta().get(Calendar.DAY_OF_MONTH)));
                                attEvnt.put("DESC_EVEN", event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc());
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            } else {
                                attEvnt = lEvenMaps.get(event.getFecha());
                                lEvenMaps.remove(event.getFecha());
                                attEvnt.replace("DESC_EVEN", (event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()).concat("\n").concat(attEvnt.get("DESC_EVEN")));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            }

                        }

                        int contCol = 0;
                        PdfPCell subCell = new PdfPCell();
                        Calendar iter = fi;
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
                            }

                            //for (Map eventMap : lEvenMaps) {
                            subContent = new PdfPTable(2);
                            subContent.setWidths(new float[]{85f, 15f});
                            if (contCol % 2 > 0) {
                                //strBuff.append("<table class='element-day element-day-shadow'>");
                                subCell.setBackgroundColor(WebColors.getRGBColor("#EEEEEE"));
                            } else {
                                //strBuff.append("<table class='element-day'>");
                                subCell.setBackgroundColor(BaseColor.WHITE);
                            }

                            subCell.setMinimumHeight(15F);
                            subCell.setColspan(1);
                            subCell.setPhrase(new Phrase(eventMap.get("DIA_SEM").toString(), fontHeader));
                            subCell.setBorderColor(WebColors.getRGBColor("#CECECE"));
                            subContent.addCell(subCell);

                            subCell.setPhrase(new Phrase(eventMap.get("DIA").toString(), fontHeaderW));
                            subCell.setBackgroundColor(BaseColor.BLACK);
                            subContent.addCell(subCell);

                            //subCell.setBackgroundColor(BaseColor.WHITE);
                            if (contCol % 2 > 0) {
                                //strBuff.append("<table class='element-day element-day-shadow'>");
                                subCell.setBackgroundColor(WebColors.getRGBColor("#EEEEEE"));
                            } else {
                                //strBuff.append("<table class='element-day'>");
                                subCell.setBackgroundColor(BaseColor.WHITE);
                            }
                            contCol++;

                            subCell.setPhrase(new Phrase(eventMap.get("DESC_EVEN").toString(), fontColumn));
                            subCell.setColspan(2);
                            subCell.setFixedHeight(250f);
                            subContent.addCell(subCell);

                            cell = new PdfPCell(subContent);
                            content.addCell(cell);

                            iter.add(Calendar.DAY_OF_YEAR, 1);
                        }

                    }

                    titleCalendar.addCell(getDocumentTitleCalendar(docTypeBO.getName()));
                    calendar.addCell(content);
                    break;
                case GlobalDefines.SEC_CONTENT_TYPE_CALENDAR_MONTH:
                    content = new PdfPTable(5);
                    content.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});
                    content.setWidthPercentage(90f);
                    fontColumn = new Font(Font.FontFamily.HELVETICA, 8f);
                    fontColumn.setColor(BaseColor.GRAY);
                    fontHeaderW = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
                    fontHeaderW.setColor(UtileriasPDF.bgColorHeader);

                    fontHeader = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
                    fontHeader.setColor(BaseColor.GRAY);

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
                    if (lEvent != null) {
                        PdfPTable subContent = new PdfPTable(2);
                        subContent.setWidthPercentage(90f);
                        String diaActual = "";
                        //boolean rellePrin = true;
                        Map<String, Map> lEvenMaps = new HashMap<>();
                        Map<String, String> attEvnt = new HashMap<>();

                        int cont = 0;

                        for (EventBO event : lEvent) {
                            /*if (rellePrin){
                             Calendar calAux = new GregorianCalendar();
                             calAux.setTimeInMillis(event.getFechaCompleta().getTimeInMillis());
                             int numRell = calAux.get(Calendar.DAY_OF_WEEK) - 2;
                             for (int i = 0; i < numRell; i++){
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
                                attEvnt.put("DESC_EVEN", event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc());
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            } else {
                                //attEvnt = lEvenMaps.get(event.getFecha());
                                lEvenMaps.remove(event.getFecha());
                                attEvnt.replace("DESC_EVEN", (event.getEventoDesc().trim().isEmpty() ? event.getEmisora() : event.getEventoDesc()).concat("\n").concat(attEvnt.get("DESC_EVEN")));
                                lEvenMaps.put(event.getFecha(), attEvnt);
                            }

                        }

                        int contCol = 0;
                        cont = 0;
                        PdfPCell subCell = new PdfPCell();

                        Calendar iter = fi;
                        if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, 1);
                        } else if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, 2);
                        } else if (iter.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                            iter.add(Calendar.DAY_OF_YEAR, (iter.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY) * (-1));
                        }

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

                            subContent = new PdfPTable(2);
                            subContent.setWidths(new float[]{85f, 15f});
                            if (contCol % 2 > 0) {
                                //strBuff.append("<table class='element-day element-day-shadow'>");
                                subCell.setBackgroundColor(WebColors.getRGBColor("#EEEEEE"));
                            } else {
                                //strBuff.append("<table class='element-day'>");
                                subCell.setBackgroundColor(BaseColor.WHITE);
                            }
                            subCell.setBorderColor(WebColors.getRGBColor("#CECECE"));
                            subCell.setMinimumHeight(15F);
                            subCell.setColspan(1);
                            subCell.setPhrase(new Phrase(eventMap.get("DIA_SEM").toString(), fontHeader));
                            subContent.addCell(subCell);

                            subCell.setPhrase(new Phrase(eventMap.get("DIA").toString(), fontHeaderW));
                            subCell.setBackgroundColor(BaseColor.BLACK);
                            subContent.addCell(subCell);

                            //subCell.setBackgroundColor(BaseColor.WHITE);
                            if (contCol % 2 > 0) {
                                //strBuff.append("<table class='element-day element-day-shadow'>");
                                subCell.setBackgroundColor(WebColors.getRGBColor("#EEEEEE"));
                            } else {
                                //strBuff.append("<table class='element-day'>");
                                subCell.setBackgroundColor(BaseColor.WHITE);
                            }

                            subCell.setPhrase(eventMap.containsKey("DESC_EVEN") ? new Phrase(eventMap.get("DESC_EVEN").toString(), fontColumn) : new Phrase("", fontColumn));
                            subCell.setColspan(2);
                            subCell.setFixedHeight(100f);
                            subContent.addCell(subCell);

                            cell = new PdfPCell(subContent);
                            content.addCell(cell);

                            if (contCol >= 4) {
                                contCol = 0;
                            } else {
                                contCol++;
                            }
                            iter.add(Calendar.DAY_OF_YEAR, 1);
                            countCol++;
                        }

                        if (contCol > 0) {
                            int diasRestantes = 5 - contCol;
                            for (int i = 0; i < diasRestantes; i++) {
                                subContent = new PdfPTable(2);
                                subContent.setWidths(new float[]{90f, 10f});

                                contCol++;
                                subCell.setMinimumHeight(15F);
                                subCell.setColspan(1);
                                subCell.setPhrase(new Phrase(diasSem[contCol], fontHeader));
                                subContent.addCell(subCell);

                                subCell.setPhrase(new Phrase(Integer.toString(i + 1), fontHeaderW));
                                subCell.setBackgroundColor(BaseColor.BLACK);
                                subContent.addCell(subCell);

                                subCell.setBackgroundColor(BaseColor.WHITE);

                                subCell.setColspan(2);
                                subCell.setFixedHeight(100f);
                                subContent.addCell(subCell);

                                cell = new PdfPCell(subContent);
                                content.addCell(cell);

                            }
                        }
                    }
                    titleCalendar.addCell(getDocumentTitleCalendar(docTypeBO.getName()));
                    calendar.addCell(content);
                    break;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        return calendar;
    }

    public String createSemanarioPdfDocument(int documentId) throws IOException,
            DocumentException {

        String fileName = null;
        try {

            CollaborativesDAO collabDAO = new CollaborativesDAO();
            DocumentCollabBO collabDocBO = collabDAO.getDocument(documentId);
            if (collabDocBO == null) {
                return null;
            }

            List<DocumentCollabItemBO> lstDocs = collabDocBO.getLstDocumentCollab();
            DocumentDAO documentDAO = new DocumentDAO();

            String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
            fileName = dirPDF + "SEMANARIO_" + documentId + "_" + collabDocBO.getDocumentName() + ".pdf";

            Rectangle pagesize = new Rectangle(PAGE_SIZE);
            Document document = new Document(pagesize, 15f, 15f, 40f, 15f);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            crearHeaderFooter(document, writer, null);
            document.open();

            DocumentBO docItem;
            for (DocumentCollabItemBO docBO : lstDocs) {

                docItem = documentDAO.getDocument(docBO.getItemDocumentId(), docBO.getItemVersion(),false);
                if (docItem == null) {
                    continue;
                }

                createPdfDocumentSemanario(docItem, document);
            }
            document.close();

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }

        return fileName;
    }

    public SemanarioDocsBO createSemanarioPdfDocument1(int idSemanario) throws IOException,
            DocumentException {
        DocumentDAO docDAO = new DocumentDAO();
        SemanarioDocsBO semanarioDocsBO = docDAO.getSemanarioDocs(idSemanario);
        if (semanarioDocsBO == null || semanarioDocsBO.getDocumentsBO().isEmpty()) {
            return null;
        }

        int documentId = semanarioDocsBO.getIdSemanario();
        String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
        String fileName = dirPDF + "SEMANARIO_" + documentId + "_" + semanarioDocsBO.getNombreSemanario() + ".pdf";

        Rectangle pagesize = new Rectangle(PAGE_SIZE);
        Document document = new Document(pagesize, 15f, 15f, 40f, 15f);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        crearHeaderFooter(document, writer, null);
        document.open();

        for (DocumentBO docBO : semanarioDocsBO.getDocumentsBO()) {
            createPdfDocumentSemanario(docBO, document);
        }
        document.close();
        return semanarioDocsBO;
    }

    private DocumentBO createPdfDocumentSemanario(DocumentBO docBO, Document document) throws IOException,
            DocumentException {
        if (docBO == null) {
            return null;
        }
        PdfPTable table;
        Boolean first = false;

        //String documentName = docBO.getDocumentName();
        PdfPTable tableChild = getDocumentTitle(docBO);
        if (tableChild != null) {
            document.add(tableChild);
            first = true;
        }
        if (docBO.getMapHojas() == null || docBO.getMapHojas().size() <= 0) {
            return null;
        }
        int numeroTotalHojas = docBO.getNumHojas();
        Set set = docBO.getMapHojas().keySet();
        Iterator it = set.iterator();

        Integer numHoja = null;
        HojaBO hojaBO = null;
        List<ModuleBO> lstModules = null;
        int i = 0;
        while (it.hasNext()) {
            numHoja = (Integer) it.next();
            hojaBO = docBO.getMapHojas().get(numHoja);
            if (hojaBO == null) {
                continue;
            }
            table = new PdfPTable(1);
            table.setSkipFirstHeader(true);
            table.setSkipLastFooter(true);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.setWidthPercentage(100f);
            //TODO nuevo para errores
            table.setSplitLate(false);
            table.setSplitRows(false);
            table.setExtendLastRow(false);

            lstModules = hojaBO.getLstModules();
            if (first == false && i != numeroTotalHojas) {
                document.newPage();
            }
            first = false;

            if (lstModules != null) {
                for (ModuleBO moduleBO : lstModules) {
                    addLayout(table, moduleBO.getRootSection(), null);
                }
                document.add(table);
            }
            i++;
        }
        return docBO;
    }

    class HeaderFooter extends PdfPageEventHelper {

        public DocumentBO docBO;

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable tableHeader = new PdfPTable(1);
//            PdfPTable tableFooter = new PdfPTable(1);
            try {
                tableHeader.setWidths(new int[]{5/*, 24, 2*/});
                tableHeader.setTotalWidth(580);
                tableHeader.setLockedWidth(true);
                tableHeader.getDefaultCell().setFixedHeight(20);
                tableHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                //Header
                //PdfPCell cellHeader = new PdfPCell(Image.getInstance("header_letter.png"));
                BufferedImage header = loadHeader();

                Image header_ = Image.getInstance(header, null);
                //header_.scaleToFit(520, 35);
                header_.scaleAbsoluteWidth(522);
                header_.scaleAbsoluteHeight(40);//(35);
//                Image footer_ = Image.getInstance(footer, null);
                PdfPCell cellHeader = new PdfPCell(header_);
                cellHeader.setBorder(Rectangle.NO_BORDER);
                cellHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellHeader.setPaddingRight(20f);
                tableHeader.addCell(cellHeader);

//                tableFooter.setWidths(new int[]{5/*, 24, 2*/});
//                tableFooter.setTotalWidth(612);
//                tableFooter.setLockedWidth(true);
//                tableFooter.getDefaultCell().setBorder(Rectangle.BOTTOM);
                //Footer
//                PdfPCell cellFooter = new PdfPCell(footer_);
//                cellFooter.setBorder(Rectangle.NO_BORDER);
//                tableFooter.addCell(cellFooter);
                tableHeader.writeSelectedRows(0, -1, 0, 780 /*1000*/, writer.getDirectContent());
//                tableFooter.writeSelectedRows(0, -1, 0, 60, writer.getDirectContent());
                if (document.getPageNumber() == 1) {
                    PdfPTable tableChild2 = new PdfPTable(2);
                    tableChild2.setWidthPercentage(90f);
                    tableChild2 = getFooterAutor(docBO);
                    //tableChild2.setWidths(new int[]{5/*, 24, 2*/});
                    tableChild2.setTotalWidth(580);
                    tableChild2.setLockedWidth(true);
                    tableChild2.getDefaultCell().setFixedHeight(20);
                    tableChild2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    tableChild2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tableChild2.writeSelectedRows(0, -1, 46, 60, writer.getDirectContent());
                }
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        private BufferedImage loadHeader() {
            BufferedImage header = null;
            HeaderfooterDAO dao = new HeaderfooterDAO();
            List<HeaderfooterBO> list = dao.get();
            try {
                if (list == null || list.isEmpty()) {
                    return header;
                }
                header = ImageIO.read(list.get(StatementConstant.SC_0.get()).getHeader().getBinaryStream());
//                footer = ImageIO.read(list.get(StatementConstant.SC_0.get()).getFooter().getBinaryStream());
            } catch (IOException | SQLException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
            return header;
        }

    }

    private com.itextpdf.text.Image getBulletImage() {
        if (this.imageBullet != null) {
            return this.imageBullet;
        }

        this.imageBullet = Utilerias.getImagePDF(GlobalDefines.PDF_IMAGE_BULLET);
        return this.imageBullet;
    }

    private com.itextpdf.text.Image getTwitterImage() {
        if (this.imageTwitter != null) {
            return this.imageTwitter;
        }

        this.imageTwitter = Utilerias.getImagePDF(GlobalDefines.PDF_IMAGE_TWITTER);
        this.imageTwitter.scaleToFit(8f, 8f);
        return this.imageTwitter;
    }

    private com.itextpdf.text.Image getDescripImage() {
        if (this.imageDescrip != null) {
            return this.imageDescrip;
        }

        this.imageDescrip = Utilerias.getImagePDF(GlobalDefines.PDF_IMAGE_DESCRIPTION);
        return this.imageDescrip;
    }

    private com.itextpdf.text.Image getUbicacionSmallImage() {
        if (this.imageUbicacionSmall != null) {
            return this.imageUbicacionSmall;
        }

        this.imageUbicacionSmall = Utilerias.getImagePDF("i-ubicaciones-small.png");
        this.imageUbicacionSmall.scaleToFit(6f, 6f);
        return this.imageUbicacionSmall;
    }

    private com.itextpdf.text.Image getTelefonoSmallImage() {
        if (this.imageTelefonoSmall != null) {
            return this.imageTelefonoSmall;
        }

        this.imageTelefonoSmall = Utilerias.getImagePDF("i-tel-small.png");
        this.imageTelefonoSmall.scaleToFit(6f, 6f);
        return this.imageTelefonoSmall;
    }

    private com.itextpdf.text.Image getUbicacionImage() {
        if (this.imageUbicacion != null) {
            return this.imageUbicacion;
        }

        this.imageUbicacion = Utilerias.getImagePDF("i-ubicaciones.png");
        this.imageUbicacion.scaleToFit(8f, 10f);
        return this.imageUbicacion;
    }

    private com.itextpdf.text.Image getTelefonoImage() {
        if (this.imageTelefono != null) {
            return this.imageTelefono;
        }

        this.imageTelefono = Utilerias.getImagePDF("i-tel.png");
        return this.imageTelefono;
    }

    private com.itextpdf.text.Image getMailImage() {
        if (this.imageMail != null) {
            return this.imageMail;
        }

        this.imageMail = Utilerias.getImagePDF("i-mail.png");
        this.imageMail.scaleToFit(10f, 8f);
        return this.imageMail;
    }

    private com.itextpdf.text.Image getLibroImage() {
        if (this.imageLibro != null) {
            return this.imageLibro;
        }

        this.imageLibro = Utilerias.getImagePDF("i-direccion.png");
        this.imageLibro.scaleToFit(8f, 10f);
        return this.imageLibro;
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
