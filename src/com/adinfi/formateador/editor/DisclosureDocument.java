package com.adinfi.formateador.editor;

import com.adinfi.formateador.bos.DisclosureBO;
import com.adinfi.formateador.bos.LinkedExcelBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DisclosureDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.ExcelDAO;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.emisoras.Emisoras_Impl;
import com.adinfi.ws.emisoras.IEmisoras_Stub;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.DatatypeConverter;
import static jdk.nashorn.internal.codegen.Compiler.LOG;
import org.apache.commons.lang3.text.WordUtils;
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
import org.datacontract.schemas._2004._07.VCB_Analisis_Data_Emisoras.EmisoraRecomendacionLog;

/**
 * @author Carlos Félix
 */
public class DisclosureDocument {

    private final String HTML_DEFUALT = "<div class=\"disclosure\"><a href=\"#\" target=\"_blank\">Disclosures</a></div>";
    private static final Integer MARGIN_CELL_DESCRIPTION = 80;
    private static final Float CELL_FIXED_HEIGHT = 20f;
    private static final Integer LEFT_RIGHT_CELL_MARGIN = 25;
    private static final int MAX_EMISORAS = 8;

    DisclosureDAO disclosureDAO = new DisclosureDAO();
    ExcelDAO excelDAO = new ExcelDAO();
    DocumentTypeDAO docDAO = new DocumentTypeDAO();
    private List<String> sshFiles;

    public DisclosureDocument() {
    }

    public DisclosureDocument(List<String> sshFiles) {
        this.sshFiles = sshFiles;
    }

    protected String getDisclosurePDF(DisclosureDAO.TYPES type, SubjectBO theme) {
        String allHTML = "";
        try {
            String documentBasePath = Utilerias.getFilePath(Utilerias.PATH_TYPE.DISCLOSURE_PDF_PATH);
            allHTML = readBaseDocHTML(documentBasePath);

            allHTML = setValues(type, allHTML, theme);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return allHTML;
    }

    List<SubjectBO> lstSubject;

    protected String getDisclosure(int documentType, SubjectBO theme, List<SubjectBO> lstSubject) {
        this.lstSubject = lstSubject;
        String disclosureHTML = "";
        try {
            DisclosureDAO.TYPES type = disclosureDAO.getDisclosureType(documentType);
            if (type == null) {
                return "";
            }

            String fileName = Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.DISCLOSURE);
            String documentBasePath = Utilerias.getFilePath(Utilerias.PATH_TYPE.DISCLOSURE_PATH);
            String allHTML = readBaseDocHTML(documentBasePath);

            if (sshFiles == null) {
                sshFiles = new ArrayList<>();
            }

            sshFiles.add(fileName);

            allHTML = setValues(type, allHTML, theme);

            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            if (bwr != null) {
                bwr.write(allHTML);
                bwr.flush();
            }
            disclosureHTML = HTML_DEFUALT.replace("#", Utilerias.getFileName(fileName));
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return disclosureHTML;
    }

    private String setValues(DisclosureDAO.TYPES type, String allHTML, SubjectBO theme) {
        String fechaHoy = Utilerias.formatDate(new Date(), "dd/MM/yyyy");
        allHTML = allHTML.replaceAll("@FECHA_HOY", fechaHoy);

        DisclosureBO disclosureBO = disclosureDAO.getDisclosure(type);
        allHTML = allHTML.replaceAll("@TITULO", "Revelación de los análisis"/*type.getFullName()*/);
        allHTML = allHTML.replaceAll("@SUBTITULO", "Disclosure"/*type.getFullName()*/);
        allHTML = allHTML.replaceAll("@COMPONENTE_TEXTO_TOP", cleanHTML(disclosureBO.getTextTop()));
        if (type != DisclosureDAO.TYPES.RENTA_FIJA) {
            allHTML = allHTML.replaceAll("@COMPONENTE_TEXTO_BOTTOM", cleanHTML(disclosureBO.getTextBottom()));
            allHTML = allHTML.replaceAll("@EXCEL_BOTTOM", getExcelControl(disclosureBO.getExcelFin()));
            if (type == DisclosureDAO.TYPES.F_CON_EMISORA && theme != null && theme.isIssuing()) {
                allHTML = allHTML.replaceAll("@EXCEL_TOP", "<div style=\"width: 60%; float: left;\">" + getExcelControl(disclosureBO.getExcelIni()) + "</div>");
                allHTML = allHTML.replaceAll("@EMISORAS", getEmisora(theme.getName()));
            } else {
                allHTML = allHTML.replaceAll("@EXCEL_TOP", "<div>" + getExcelControl(disclosureBO.getExcelIni()) + "</div>");
                allHTML = allHTML.replaceAll("@EMISORAS", "");
            }
        } else {
            allHTML = allHTML.replaceAll("@COMPONENTE_TEXTO_BOTTOM", "");
            allHTML = allHTML.replaceAll("@EXCEL_TOP", "");
            allHTML = allHTML.replaceAll("@EXCEL_BOTTOM", "");
            allHTML = allHTML.replaceAll("@EMISORAS", "");
        }
        if (this.lstSubject != null && this.lstSubject.size() > 0) {
            allHTML = allHTML.replace("@TAB_BOTTOM", getEmisorasCollab());
        } else {
            allHTML = allHTML.replace("@TAB_BOTTOM", "");
        }

        return allHTML;
    }

    private String getEmisora(String theme) {
        String retVal = "<div class=\"contacto\" style=\"margin: 0px 5px 5px 5px;\"><h2 style=\"font-size: 1em;\">" + theme + "</h2><div class=\"contacto\" style=\" border: solid; padding: 5px; overflow: auto;\">";

        List<String> emi = getEmisorasFundamentalesHTML(theme);

        if (emi == null || emi.size() < 1) {
            return "";
        }

        for (String e : emi) {
            retVal += "<div class=\"sectores\">" + agregarTagUL(e) + "</div>";
        }
        retVal += "</div></div>";

        return retVal;
    }

    private String getEmisorasCollab() {
        String retVal = "";

        for (SubjectBO sub : this.lstSubject) {
            String res = getEmisora(sub);

            if (res == null) {
                continue;
            }

            retVal += res;
        }

        if (retVal == null) {
            return "";
        }

        return "<h2 style=\"margin: 0;\">Revelación de los análisis</h2><h3>(Disclosures)</h3>" + retVal;
    }

    private String getEmisora(SubjectBO sub) {
        if(sub == null || sub.getName() == null || sub.getName().trim().isEmpty())
            return null;
        
        String retVal = "<div class=\"contacto\" style=\"width: 30%; margin: 5px;\"><h2 style=\"font-size: 1em;\">" + sub.getName() + "</h2><div class=\"contacto\" style=\" border: solid; padding: 5px; overflow: auto;\">";

        List<String> emi = getEmisorasFundamentalesHTML(sub.getName());

        if (emi == null || emi.size() <= 1) {
            return null;
        }

        for (String e : emi) {
            retVal += "<div class=\"sectores\">" + agregarTagUL(e) + "</div>";
        }
        retVal += "</div></div>";

        return retVal;
    }

    private String agregarTagUL(String p) {
        if (p == null || p.isEmpty()) {
            return p;
        }

        p = "<ul style=\"list-style: none; PADDING-RIGHT: 0px;   PADDING-LEFT: 0px;   FLOAT: left;   PADDING-BOTTOM: 0px;   MARGIN: 0;   WIDTH: 100%;   PADDING-TOP: 0px;   LIST-STYLE-TYPE: none; font-size: 0.8em;\">" + p + "</ul>";
        return p;
    }

    private String getEmisoras(String theme) {
        StringBuilder emisorasHTML = new StringBuilder();
        List<EmisoraRecomendacionLog> emisorasLst = UtileriasWS.getEmisorasFundamentales(theme);
        Map<String, List<EmisoraRecomendacionLog>> emisorasOrd = new HashMap<>();
        List<String> encabezados = new ArrayList<>();
        if (emisorasLst != null) {
            for (EmisoraRecomendacionLog emisora : emisorasLst) {
                if (emisorasOrd.containsKey(emisora.getEmisora())) {
                    if (emisorasOrd.get(emisora.getEmisora()).size() < MAX_EMISORAS) {
                        emisorasOrd.get(emisora.getEmisora()).add(emisora);
                    }
                } else {
                    List<EmisoraRecomendacionLog> emisLst = new ArrayList<>();
                    emisLst.add(emisora);
                    encabezados.add(emisora.getEmisora());
                    emisorasOrd.put(emisora.getEmisora(), emisLst);
                }
            }
            if (encabezados.size() > 0) {
                for (String encabezado : encabezados) {
                    List<EmisoraRecomendacionLog> emisoras = emisorasOrd.get(encabezado);
                    System.out.println(encabezado);
                    for (EmisoraRecomendacionLog emisora : emisoras) {
                        System.out.print(emisora.getFecha());
                        System.out.print("<==>");
                        System.out.print(emisora.getRecomendacion());
                        System.out.print("<==>");
                        System.out.println(emisora.getPO());
                    }
                }
            }
        }
        return emisorasHTML.toString();
    }

    private PdfPCell getTableEmisorasFundamentalesCompleta(String theme) {
        PdfPCell pdfPCellMain = new PdfPCell();
        pdfPCellMain.setBorder(Rectangle.NO_BORDER);
        pdfPCellMain.setNoWrap(false);

        PdfPTable tableParent = new PdfPTable(1);
        tableParent.setWidthPercentage(100f);
        tableParent.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = new Font(UtileriasPDF.fontTextoDisclosure);
        font.setColor(UtileriasPDF.fgColorTexto);
        font.setStyle(Font.BOLD);

        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pdfPCell.setNoWrap(false);
        try {
            Paragraph pgp = new Paragraph(theme + " Historia de recomendaciones");
            pgp.setAlignment(Element.ALIGN_JUSTIFIED);
            pgp.setFont(font);
            pdfPCell.addElement(pgp);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        tableParent.addCell(pdfPCell);

        PdfPCell pdfPCellTable = new PdfPCell();
        pdfPCellTable.setBorder(Rectangle.BOX);
        pdfPCellTable.setNoWrap(false);
        pdfPCellTable.setPaddingTop(1f);
        pdfPCellTable.addElement(getTableEmisorasFundamentales(theme));
        tableParent.addCell(pdfPCellTable);

        pdfPCellMain.addElement(tableParent);

        return pdfPCellMain;
    }

    private PdfPTable getTableEmisorasFundamentales(String theme) {
        List<String> emisoras = getEmisorasFundamentales(theme);

        PdfPTable tableParent = new PdfPTable(3);
        tableParent.setWidthPercentage(100f);
        tableParent.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = new Font(UtileriasPDF.fontTextoDisclosure);
        font.setColor(UtileriasPDF.fgColorTexto);

        try {
            for (String emisora : emisoras) {
                //List<Element> p = HTMLWorker.parseToList(new StringReader(emisora), stE);
                List<ChunksAndProperties> p = UtileriasPDF.parseToList(emisora, UtileriasPDF.HTML_TAG_P);
                int c = 0;
                for (int k = 0; k < p.size(); ++k) {
                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setNoWrap(false);
                    pdfPCell.setPadding(0f);

                    Paragraph pgp = new Paragraph(p.get(k).getChunk());
                    if (c == 2) {
                        pgp.setAlignment(Element.ALIGN_RIGHT);
                        pdfPCell.setPaddingRight(2f);
                    }
                    pgp.setFont(font);
                    pdfPCell.addElement(pgp);

                    tableParent.addCell(pdfPCell);
                    c++;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

        return tableParent;
    }

    private List<String> getEmisorasFundamentales(String theme) {
        List<String> retVal = new ArrayList<>();
        IEmisoras_Stub stub = (IEmisoras_Stub) new Emisoras_Impl().getBasicHttpBinding_IEmisoras();
        UtileriasWS.setEndpoint(stub);
        try {
            String res = "<p>Fecha</p>"
                    //retVal+="<p>"+emisora.getEmisoraRecomendacionLog()[i].getEmisora()+"</p>";
                    + "<p>Recomendación</p>"
                    + "<p>Precio Objetivo</p>";
            retVal.add(res);
            com.adinfi.ws.emisoras.ArrayOfEmisoraRecomendacionLog emisora = stub.logRecomendacionesFundamentales("<Emisoras><Emisora>" + theme + "</Emisora></Emisoras>");
            for (int i = 0; i < emisora.getEmisoraRecomendacionLog().length; i++) {
                String rec = WordUtils.capitalize(emisora.getEmisoraRecomendacionLog()[i].getRecomendacion().toLowerCase());
                rec = rec.substring(0, (rec.length() > 13 ? 13 : (rec.length())));
                res = "<p>" + emisora.getEmisoraRecomendacionLog()[i].getFecha() + "</p>"
                        //retVal+="<p>"+emisora.getEmisoraRecomendacionLog()[i].getEmisora()+"</p>";
                        + "<p>" + rec + "</p>"
                        + "<p>" + emisora.getEmisoraRecomendacionLog()[i].getPO() + "</p>";
                retVal.add(res);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de log de recomendaciones fundamentales no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DisclosureDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    private List<String> getEmisorasFundamentalesHTML(String theme) {
        List<String> retVal = new ArrayList<>();
        IEmisoras_Stub stub = (IEmisoras_Stub) new Emisoras_Impl().getBasicHttpBinding_IEmisoras();
        UtileriasWS.setEndpoint(stub);
        try {
            String res = "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 25%;   PADDING-TOP: 2px;\">Fecha</li>"
                    //retVal+="<p>"+emisora.getEmisoraRecomendacionLog()[i].getEmisora()+"</p>";
                    + "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 50%;   PADDING-TOP: 2px;\">Recomendación</li>"
                    + "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 15%;   PADDING-TOP: 2px;\">Precio Objetivo</li>";
            retVal.add(res);
            com.adinfi.ws.emisoras.ArrayOfEmisoraRecomendacionLog emisora = stub.logRecomendacionesFundamentales("<Emisoras><Emisora>" + theme + "</Emisora></Emisoras>");
            for (int i = 0; i < emisora.getEmisoraRecomendacionLog().length; i++) {
                String rec = WordUtils.capitalize(emisora.getEmisoraRecomendacionLog()[i].getRecomendacion().toLowerCase());
                rec = rec.substring(0, (rec.length() > 13 ? 13 : (rec.length())));
                res = "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 25%;   PADDING-TOP: 2px;\">" + emisora.getEmisoraRecomendacionLog()[i].getFecha() + "</li>"
                        //retVal+="<p>"+emisora.getEmisoraRecomendacionLog()[i].getEmisora()+"</p>";
                        + "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 50%;   PADDING-TOP: 2px;\">" + rec + "</li>"
                        + "<li style=\" PADDING-RIGHT: 2px;   DISPLAY: inline;   PADDING-LEFT: 2px;   FLOAT: left;   PADDING-BOTTOM: 2px;   WIDTH: 15%;   PADDING-TOP: 2px; text-align: right;\">" + emisora.getEmisoraRecomendacionLog()[i].getPO() + "</li>";
                retVal.add(res);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de log de recomendaciones fundamentales no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DisclosureDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    private String getExcelControl(DisclosureBO.ExcelBO excelBO) {
        String excelStr = "";
        try {
            ObjectInfoBO objInfo = initExcel(excelBO);

            excelStr = displayExcelHTML(objInfo);
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return excelStr;
    }

    protected String displayExcelHTML(ObjectInfoBO objectInfoBO) {
        StringBuilder strBuff = new StringBuilder();
        try {
            if (objectInfoBO != null) {
                String fileName = objectInfoBO.getFile2();
                /**
                 * if (objectInfoBO.getTitulo() != null &&
                 * objectInfoBO.getTitulo().isEmpty() == false) {
                 * strBuff.append("<h3>").append(objectInfoBO.getTitulo()).append("</h3>\n");
                 * }
                 */

                /**
                 * if (objectInfoBO.getSubTitulo() != null &&
                 * objectInfoBO.getSubTitulo().isEmpty() == false) {
                 * strBuff.append("<h4>").append(objectInfoBO.getSubTitulo()).append("</h4>\n");
                 * }
                 */
                strBuff.append("<div class='image'>\n ");
                // Add image
                strBuff.append("<img    src='data:image/png;base64,").append(fileName);
                strBuff.append("' width= ").append(objectInfoBO.getWidth());
                strBuff.append(" height= ").append(objectInfoBO.getHeight());
                strBuff.append(" >\n");
                strBuff.append("</a>");
                strBuff.append("</div>\n");
                /**
                 * if (objectInfoBO.getComentarios() != null &&
                 * objectInfoBO.getComentarios().isEmpty() == false) {
                 * strBuff.append("<div class='description' >\n");
                 * strBuff.append(objectInfoBO.getComentarios());
                 * strBuff.append("</div>\n"); }
                 */
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return strBuff.toString();
    }

    protected ObjectInfoBO initExcel(DisclosureBO.ExcelBO excelBO) {
        if (excelBO == null) {
            return null;
        }
        ObjectInfoBO objInfo = getObjectInfoBO(excelBO);
        //Shell shell= null;
        try {
            /*if (!SwingUtilities.isEventDispatchThread()) 
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        Shell shell = null;
                        try {
                            shell = new Shell();
                            shell.setSize(600, 400);
                            shell.setText("Excel Window");
                            shell.setLayout(new FillLayout());
                            shell.setMenuBar(new Menu(shell, SWT.BAR));
                            createPartControl1(shell, objInfo.getFile(), objInfo.getRangoIni(), objInfo.getRangoFin());
                            shell.open();
                            shell.close();
                        } catch (Exception e) {
                            Utilerias.logger(getClass()).info(e);
                        } finally {
                            if (shell != null) {
                                try {
                                    shell.close();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                });
            else{
                Shell shell = null;
                try {
                    shell = new Shell();
                    shell.setSize(600, 400);
                    shell.setText("Excel Window");
                    shell.setLayout(new FillLayout());
                    shell.setMenuBar(new Menu(shell, SWT.BAR));
                    createPartControl1(shell, objInfo.getFile(), objInfo.getRangoIni(), objInfo.getRangoFin());
                    shell.open();
                    shell.close();
                } catch (Exception e) {
                    Utilerias.logger(getClass()).info(e);
                } finally {
                    if (shell != null) {
                        try {
                            shell.close();
                        } catch (Exception e) {
                        }
                    }
                }     
            }*/

            objInfo.setFile2(genExcellPaste(objInfo));
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            /*if (shell != null) {
             try{
             shell.close();
             }catch(Exception e){}
             }*/
        }
        return objInfo;
    }

    public void createPartControl1(Composite parent, String fileName, String rangoIni, String rangoFin) {

        OleClientSite site = null;
        OleAutomation xls = null;
        OleFrame frame = null;
        try {
            frame = new OleFrame(parent, SWT.NONE);
            File f = new File(fileName);
            site = new OleClientSite(frame, SWT.NONE, f);
            xls = new OleAutomation(site);

            int[] ids = xls.getIDsOfNames(new String[]{"ActiveSheet"});

            Utilerias.logger(getClass()).info(ids[0]);
            Variant sheet = xls.getProperty(ids[0]);

            // Den Range erstellen
            int rangeId = sheet.getAutomation().getIDsOfNames(new String[]{"Range"})[0];
            Utilerias.logger(getClass()).info("RANGE ID =" + rangeId);
            Variant[] arguments_1 = new Variant[2];
            arguments_1[0] = new Variant(rangoIni);
            arguments_1[1] = new Variant(rangoFin);
            Variant range = sheet.getAutomation().getProperty(rangeId, arguments_1);

            int[] selectId = range.getAutomation().getIDsOfNames(new String[]{"Select"});
            range.getAutomation().invoke(selectId[0]);

            int[] copyId = range.getAutomation().getIDsOfNames(new String[]{"Copy"});
            range.getAutomation().invoke(copyId[0]);
        } catch (SWTError e) {
            Utilerias.logger(getClass()).info("Unable to open activeX control");
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        } finally {
            if (site != null) {
                try {
                    site.dispose();
                } catch (Exception e) {
                }
            }
            if (xls != null) {
                try {
                    xls.dispose();
                } catch (Exception e) {
                }
            }
            if (frame != null) {
                frame.dispose();
            }
            site = null;
            xls = null;
            frame = null;
        }
    }

    public String genExcellPaste(ObjectInfoBO objInfo) {
        String fileName = "";
        try {
            //No mover
            /*Transferable transferable = Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getContents(null);
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();*/

            BufferedImage img = objInfo.getImage();//(BufferedImage) clip.getContents(null).getTransferData(DataFlavor.imageFlavor);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            fileName = DatatypeConverter.printBase64Binary(imageInByte);
            objInfo.setWidth((short) img.getWidth());
            objInfo.setHeight((short) img.getHeight());
        } catch (HeadlessException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return fileName;
    }

    protected ObjectInfoBO getObjectInfoBO(DisclosureBO.ExcelBO excelBO) {
        ObjectInfoBO objInfo = new ObjectInfoBO();
        try {
            LinkedExcelBO nameBO = excelDAO.getFromCtegory(excelBO.getCategoryId(), excelBO.getNameId());
            objInfo.setComentarios(excelBO.getDescripcion());
            objInfo.setTitulo(excelBO.getTitulo());
            objInfo.setSubTitulo(excelBO.getSubTitulo());
            objInfo.setCategoryId(excelBO.getCategoryId());
            objInfo.setNameId(excelBO.getNameId());
            objInfo.setImage(excelBO.getImage());
            objInfo.setFile(nameBO.getPath());
            objInfo.setRangoIni(nameBO.getXY1());
            objInfo.setRangoFin(nameBO.getXY2());
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return objInfo;
    }

    private String cleanHTML(String previewStr) {
        if (previewStr == null) {
            return "";
        }
        previewStr = previewStr.replaceAll("<html>", "");
        previewStr = previewStr.replaceAll("<head>", "");
        previewStr = previewStr.replaceAll("<body>", "");
        previewStr = previewStr.replaceAll("</body>", "");
        previewStr = previewStr.replaceAll("</head>", "");
        previewStr = previewStr.replaceAll("</html>", "");
        return previewStr.trim();
    }

    private String readBaseDocHTML(String fileName) {
        String content = null;
        File file = new File(fileName);
        try (FileReader reader = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
        } catch (IOException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return content;
    }

    protected void addDisclosure(Document document, int documentType, SubjectBO theme, boolean isCalendar) {
        if (document == null) {
            return;
        }
        try {
            float marginBottom = Utilerias.marginBottomDisclosurePdf();
            document.setMargins(15f, 15f, 50f, marginBottom);
            try {
                if (!SwingUtilities.isEventDispatchThread()) {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                clearClipboard();
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                Utilerias.logger(getClass()).info(e);
                            }
                        }
                    });
                }else{
                    try {
                        clearClipboard();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Utilerias.logger(getClass()).info(e);
                    }
                }
            } catch (Exception e) {
                Utilerias.logger(getClass()).debug(e);
            }
            
            Font fontContet = new Font(UtileriasPDF.fontTextoDisclosure);
            fontContet.setColor(UtileriasPDF.fgColorTexto);

            //if (!isCalendar) {
                document.newPage();
            //}

            DisclosureDAO.TYPES type = disclosureDAO.getDisclosureType(documentType);
            if (type == null) {
                return;
            }

            DisclosureBO disclosureBO = disclosureDAO.getDisclosure(type);

            PdfPTable tableParent = new PdfPTable(1);
            tableParent.setWidthPercentage(90f);

            /**
             * Se define el Encabezado del Disclosure
             */
            PdfPCell pdfPCellT = new PdfPCell();
            pdfPCellT.setNoWrap(false);
            pdfPCellT.setBorder(Rectangle.NO_BORDER);

            Font fontDisTit = new Font(UtileriasPDF.fontTituloPDF);
            fontDisTit.setColor(UtileriasPDF.fgColorTexto);

            Paragraph pgpT = new Paragraph("Revelación de los análisis", fontDisTit);
            pgpT.setAlignment(Element.ALIGN_JUSTIFIED);

            pdfPCellT.addElement(pgpT);

            Font fontDisSubTit = new Font(UtileriasPDF.fontSubTituloPDF);
            fontDisSubTit.setColor(UtileriasPDF.fgColorTexto);

            Paragraph pgpST = new Paragraph("Disclosure", fontDisSubTit);
            pgpST.setAlignment(Element.ALIGN_JUSTIFIED);
            pgpST.setSpacingAfter(10f);

            pdfPCellT.addElement(pgpST);
            pdfPCellT.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            tableParent.addCell(pdfPCellT);

            document.add(tableParent);

            /**
             * Primer seccion del contenido
             */
            tableParent = new PdfPTable(1);
            tableParent.setWidthPercentage(90f);

            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setNoWrap(false);
            pdfPCell.setBorder(Rectangle.NO_BORDER);

            List<List<ChunksAndProperties>> p = UtileriasPDF.parseToList_P(cleanHTML(disclosureBO.getTextTop()));

            for (int k = 0; k < p.size(); ++k) {
                Paragraph para = new Paragraph();
                para.setFont(fontContet);
                para.setLeading(12f);
                for (ChunksAndProperties pC : p.get(k)) {

                    if (pC.getChunk().getContent().trim().isEmpty()) {
                        continue;
                    }

                    Chunk chunk = pC.getChunk();

                    Font fontChunk = new Font(UtileriasPDF.fontTextoDisclosure);
                    fontChunk.setColor(UtileriasPDF.fgColorTexto);
                    fontChunk.setStyle((pC.isBold() ? Font.BOLD : Font.NORMAL) | (pC.isItalic() ? Font.ITALIC : Font.NORMAL) | (pC.isUnderline() ? Font.UNDERLINE : Font.NORMAL) | (pC.isStrike() ? Font.STRIKETHRU : Font.NORMAL));
                    chunk.setFont(fontChunk);

                    para.add(chunk);
                }

                if (UtileriasPDF.TEXT_ALIGN_RIGHT.equals(p.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_RIGHT);
                } else if (UtileriasPDF.TEXT_ALIGN_CENTER.equals(p.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_CENTER);
                } else {
                    para.setAlignment(Element.ALIGN_JUSTIFIED);
                }

                if ((k + 1) < p.size() && p.get(k + 1).get(0).getChunk().toString().trim().isEmpty()) {
                    para.setSpacingAfter(10f);
                }

                pdfPCell.addElement(para);
            }

            pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            tableParent.addCell(pdfPCell);

            document.add(tableParent);

            /**
             * Seccion de Excel y Emisoras recomendadas
             */
            if (type == DisclosureDAO.TYPES.F_CON_EMISORA && theme != null && theme.isIssuing()) {
                tableParent = new PdfPTable(new float[]{313f, 209f});
            } else {
                tableParent = new PdfPTable(1);
            }

            tableParent.setWidthPercentage(90f);
            tableParent.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (type == DisclosureDAO.TYPES.F_CON_EMISORA && theme != null && theme.isIssuing()) {
                displayExcelPDF(tableParent, disclosureBO.getExcelIni(), type, 313f);
                tableParent.addCell(getTableEmisorasFundamentalesCompleta(theme.getName()));
            } else {
                displayExcelPDF(tableParent, disclosureBO.getExcelIni(), type, 522f);
            }

            document.add(tableParent);

            /**
             * Seccion final de texto del Disclosure
             */
            tableParent = new PdfPTable(1);
            tableParent.setWidthPercentage(90f);

            PdfPCell pdfPCellB = new PdfPCell();
            pdfPCellB.setBorder(Rectangle.NO_BORDER);
            pdfPCellB.setNoWrap(false);

            List<List<ChunksAndProperties>> pB = UtileriasPDF.parseToList_P(disclosureBO.getTextBottom());

            for (int k = 0; k < pB.size(); ++k) {
                Paragraph para = new Paragraph();
                para.setFont(fontContet);
                para.setLeading(12f);
                for (ChunksAndProperties pC : pB.get(k)) {

                    if (pC.getChunk().getContent().trim().isEmpty()) {
                        continue;
                    }

                    Chunk chunk = pC.getChunk();

                    Font fontChunk = new Font(UtileriasPDF.fontTextoDisclosure);
                    fontChunk.setColor(UtileriasPDF.fgColorTexto);
                    fontChunk.setStyle((pC.isBold() ? Font.BOLD : Font.NORMAL) | (pC.isItalic() ? Font.ITALIC : Font.NORMAL) | (pC.isUnderline() ? Font.UNDERLINE : Font.NORMAL) | (pC.isStrike() ? Font.STRIKETHRU : Font.NORMAL));
                    chunk.setFont(fontChunk);

                    para.add(chunk);
                }

                if (UtileriasPDF.TEXT_ALIGN_RIGHT.equals(pB.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_RIGHT);
                } else if (UtileriasPDF.TEXT_ALIGN_CENTER.equals(pB.get(k).get(0).getAlign())) {
                    para.setAlignment(Element.ALIGN_CENTER);
                } else {
                    para.setAlignment(Element.ALIGN_JUSTIFIED);
                }

                if ((k + 1) < pB.size() && pB.get(k + 1).get(0).getChunk().toString().trim().isEmpty()) {
                    para.setSpacingAfter(10f);
                }

                pdfPCellB.addElement(para);
            }
            pdfPCellB.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            tableParent.addCell(pdfPCellB);

            document.add(tableParent);

            /**
             * Seccion de Excel final
             */
            tableParent = new PdfPTable(1);
            tableParent.setWidthPercentage(90f);
            tableParent.setHorizontalAlignment(Element.ALIGN_CENTER);

            displayExcelPDF(tableParent, disclosureBO.getExcelFin(), type, 522f);

            document.add(tableParent);

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }

    protected ObjectInfoBO initExcelPDF(DisclosureBO.ExcelBO excelBO) {
        if (excelBO == null) {
            return null;
        }
        ObjectInfoBO objInfo = getObjectInfoBO(excelBO);
        Shell shell = null;
        try {
            shell = new Shell();
            shell.setSize(600, 400);
            shell.setText("Excel Window");
            shell.setLayout(new FillLayout());
            shell.setMenuBar(new Menu(shell, SWT.BAR));
            createPartControl1(shell, objInfo.getFile(), objInfo.getRangoIni(), objInfo.getRangoFin());
            shell.open();
            shell.close();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            if (shell != null) {
                try {
                    shell.dispose();
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }
                shell = null;
            }
        }
        return objInfo;
    }

    public BufferedImage genExcellPastePDF() {
        BufferedImage img = null;
        Transferable transferable = null;
        Clipboard clip = null;
        try {
            //No mover
            transferable = Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getContents(null);
            clip = Toolkit.getDefaultToolkit().getSystemClipboard();

            img = (BufferedImage) clip.getContents(null).getTransferData(DataFlavor.imageFlavor);
        } catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            if (transferable != null) {
                transferable = null;
            }
            if (clip != null) {
                clip = null;
            }
        }
        return img;
    }

    public void clearClipboard() {
        try {
            // Recoge el Clipboard del sistema.
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Transferable() {

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.imageFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (!DataFlavor.imageFlavor.equals(flavor)) {
                        throw new UnsupportedFlavorException(flavor);
                    }
                    java.awt.Image imagenInterna = new ImageIcon(getClass().getResource("/img/adpublish-icon.png")).getImage();
                    //Image image = Image.getInstance("/img/camera1.png");
                    return imagenInterna;
                }
            }, null);

        } catch (HeadlessException ex) {
            Utilerias.logger(getClass()).error(ex);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        //return img;
    }

    protected void displayExcelPDF(PdfPTable tableParent, DisclosureBO.ExcelBO excelBO, DisclosureDAO.TYPES type, Float widthDisplay) {
        if (excelBO == null) {
            return;
        }

        PdfPTable tableChild = new PdfPTable(1);
        tableChild.setSplitLate(false);
        tableChild.setSplitRows(false);
        tableChild.setExtendLastRow(false);

        PdfPCell cellContent = new PdfPCell(tableChild);
        cellContent.setBorder(Rectangle.NO_BORDER);
        cellContent.setHorizontalAlignment(Element.ALIGN_LEFT);
        try {
            /*if (!SwingUtilities.isEventDispatchThread()){
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            initExcelPDF(excelBO);
                        } catch (Exception e) {
                            Utilerias.logger(getClass()).info(e);
                        }
                    }
                });
            }else{
                try {
                    initExcelPDF(excelBO);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).info(e);
                }
            }*/
            //try { Thread.sleep(1000); } catch (Exception e) { }
            BufferedImage buffImage = excelBO.getImage();// genExcellPastePDF();

            Image image1 = null;
            try {
                image1 = Image.getInstance(buffImage, null);
            } catch (Exception e) {
                image1 = null;
                widthDisplay = 1f;
                LOG.log(Level.ALL, e.getMessage());
            }

            tableChild.setWidths(new float[]{widthDisplay});

            if (image1 != null) {
                float ancho = image1.getWidth();
                float alto = image1.getHeight();

                float thumb_w = widthDisplay, thumb_h = 0;

                float por_fal = 0;
                float por_img = ancho * 100 / 250;

                if (widthDisplay > ancho) {
                    thumb_h = por_fal * alto / 100;
                } else if (widthDisplay < ancho) {
                    thumb_h = 100 * alto / por_img;
                } else {
                    thumb_h = alto;
                }

                //image1.scaleToFit((float) Math.floor(thumb_w), (float) Math.floor(thumb_h));
                image1.scaleAbsoluteWidth(thumb_w);
                image1.scaleAbsoluteHeight(thumb_h);

                try {//FIXME Bloque Try para esperar la actualizacion de la escalacion de la imagen
                    Thread.sleep(50);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }

                PdfPCell cellImage = new PdfPCell(image1);
                cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellImage.setBackgroundColor(UtileriasPDF.bgColorImagen);
                cellImage.setBorder(Rectangle.NO_BORDER);

                //cellImage.setFixedHeight(alto - MARGIN_CELL_DESCRIPTION);
                tableChild.addCell(cellImage);
            }

            tableParent.setSpacingAfter(CELL_FIXED_HEIGHT);
            tableParent.setSpacingBefore(CELL_FIXED_HEIGHT);

            tableParent.addCell(cellContent);
        } catch (BadElementException ex) {
            LOG.log(Level.ALL, ex.getMessage());
        } catch (DocumentException ex) {
            Logger.getLogger(PdfDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LOG.log(Level.ALL, ex.getMessage());
        }
    }
}
