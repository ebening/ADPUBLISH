package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.editor.PdfDocument;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.MergePDF;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.view.ScrIntegradorDoc;
import com.adinfi.formateador.view.ScrIntegradorMod;
import com.adinfi.formateador.view.publish.SendPublishAttachment;
import com.itextpdf.text.DocumentException;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author Guillermo Trejo
 */
public class ButtonEditor extends DefaultCellEditor {

    public enum ACTION {

        PDF,
        HTML,
        EDIT_DOCUMENT,
        EDIT_PUBLISH,
        NEW_DOCUMENT
    }

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final JXTable table;
    private final ACTION action;
    private List<InputStream> lstCollabPdf;

    public ButtonEditor(JCheckBox checkBox, JXTable table, ACTION action) {
        super(checkBox);
        this.table = table;
        this.action = action;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    private void customAction(int row) {

        SearchTableModel model = (SearchTableModel) table.getModel();
        int rowSel = table.convertRowIndexToModel(row);
        DocumentSearchBO docBO = model.getList().get(rowSel);
        int idDocument = docBO.getDocumentId();

        if (action == ACTION.PDF) {
            if (idDocument > 0 && InstanceContext.getInstance().isActivePDF()) {
                openPDF(row);
            } else if (docBO.isHistory()){
                try {
                    int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Abrir adjunto historico con nombre\n" + docBO.getDocumentName() + "?", GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        BrowserLauncher launcher = new BrowserLauncher();
                        launcher.openURLinBrowser(docBO.getUrl());
                    }
                } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
                    Logger.getLogger(ButtonEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (action == ACTION.HTML) {
            if (idDocument > 0 && InstanceContext.getInstance().isActiveHTML()) {
                openHTML(row);
            }
        } else if (action == ACTION.EDIT_DOCUMENT) {
            if (idDocument > 0 && InstanceContext.getInstance().isActiveEditDocument()) {
                if (docBO.getEnableForDocumentTypeProfile() > 0) {
                    openDocument(row);
                } else {
                    Utilerias.showMessage(MainView.main, "Este usuario no autorizado para editar este tipo de documento.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (action == ACTION.EDIT_PUBLISH) {
            if (idDocument > 0 && InstanceContext.getInstance().isActivePublish()) {
                if (docBO.getEnableForDocumentTypeProfile() > 0) {
                    editPublication(row);
                } else {
                    Utilerias.showMessage(MainView.main, "Este usuario no autorizado para publicar este tipo de documento.", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (InstanceContext.getInstance().isActivePublish()) {
                if (docBO.getEnableForDocumentTypeProfile() > 0) {
                    editPublicationAttach(model.getRowIdPublish(row), docBO.isHistory());
                } else {
                    Utilerias.showMessage(MainView.main, "Este usuario no autorizado para publicar este tipo de documento.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (action == ACTION.NEW_DOCUMENT) {
            if (docBO.getEnableForDocumentTypeProfile() > 0) {
                newDocument(row);
            } else {
                Utilerias.showMessage(MainView.main, "Este usuario no autorizado para publicar este tipo de documento.", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void editPublication(int row) {
        Utilerias.pasarGarbageCollector();
        SearchTableModel model = (SearchTableModel) table.getModel();
        int rowSel = table.convertRowIndexToModel(row);
        DocumentSearchBO docBO = model.getList().get(rowSel);
        int idDocument = docBO.getDocumentId();

        String nombre = docBO.getDocumentName();
        int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Editar publicación del documento con nombre\n" + nombre + "?", GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                if (docBO.isCollaborative()) {
                    if (GlobalDefines.COLLAB_TYPE_DOC.equals(docBO.getCollaborative_Type())) {

                        openDocumentCollabDoc(idDocument, true);

                    } else if (GlobalDefines.COLLAB_TYPE_MOD.equals(docBO.getCollaborative_Type())) {
                        openDocumentCollabMod(idDocument, true);
                    }
                } else {
                    agregarBusqueda(idDocument, nombre);
                    //TODO modificar para nueva version
                    //MainView.main.setDocument(idDocument, nombre);
                    MainView.main.getProgressBar().setVisible(true);
                    MainView.main.getProgressBar().setIndeterminate(true);
                    OpenDocumentWorker workerOpenDoc = new OpenDocumentWorker(idDocument, nombre, true);
                    workerOpenDoc.execute();

                }
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }

        }

    }

    private void editPublicationAttach(int row, boolean isHistory) {
        Utilerias.pasarGarbageCollector();
        SendPublishAttachment attch = new SendPublishAttachment(MainApp.getApplication().getMainFrame(), true, row, isHistory);
        Utilerias.centreDialog(attch, true);
        attch.setVisible(true);

    }

    // abrir - editar documento.
    private void openDocument(int row) {
        Utilerias.pasarGarbageCollector();
        SearchTableModel model = (SearchTableModel) table.getModel();
        int rowSel = table.convertRowIndexToModel(row);
        DocumentSearchBO docBO = model.getList().get(rowSel);
        int idDocument = docBO.getDocumentId();

        String nombre = docBO.getDocumentName();
        int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Editar el documento con nombre\n" + nombre + "?", GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                if (docBO.isCollaborative()) {
                    if (GlobalDefines.COLLAB_TYPE_DOC.equals(docBO.getCollaborative_Type())) {
                        openDocumentCollabDoc(idDocument, false);
                    } else if (GlobalDefines.COLLAB_TYPE_MOD.equals(docBO.getCollaborative_Type())) {
                        openDocumentCollabMod(idDocument);
                    }
                } else {
                    agregarBusqueda(idDocument, nombre);
                    //TODO modificar para nueva version
                    //MainView.main.setDocument(idDocument, nombre);
                    OpenDocumentWorker workerDocument = new OpenDocumentWorker(idDocument, nombre, false);
                    MainView.main.getProgressBar().setVisible(true);
                    MainView.main.getProgressBar().setIndeterminate(true);
                    workerDocument.execute();
                }
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }
    }

    protected void newDocument(int row) {
        Utilerias.pasarGarbageCollector();
        SearchTableModel model = (SearchTableModel) table.getModel();
        int rowSel = table.convertRowIndexToModel(row);
        DocumentSearchBO docBO = model.getList().get(rowSel);

        try {
            boolean b = MainView.main.getScrDocument().newDocument(docBO.getIdMarket(), docBO.getIdDocType());
            if (true == b) {
                MainView.main.setTitle(GlobalDefines.TITULO_APP + "  " + MainView.main.getScrDocument().getDocumentName());
                MainView.main.addToMainPanel(MainView.main.getScrDocument());
            } else {
                MainView.main.setTitle(GlobalDefines.TITULO_APP);
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    protected void openDocumentCollabMod(int documentId) {
        DocumentDAO docDAO = new DocumentDAO();
        try {
            DocumentCollabBO document = (DocumentCollabBO) docDAO.getDocument(documentId, 1,false);
            ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
            scrIntegradorMod.newDocument(document);
            scrIntegradorMod.loadDocument();
            //scrIntegradorDoc.loadDocument(document);

            OpenPublishColabModWorker modWorker = new OpenPublishColabModWorker(scrIntegradorMod, false);
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            modWorker.execute();

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    protected void openDocumentCollabMod(int documentId, boolean openPublish) {
        DocumentDAO docDAO = new DocumentDAO();
        try {
            DocumentCollabBO document = (DocumentCollabBO) docDAO.getDocument(documentId, 1,false);
            ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
            scrIntegradorMod.newDocument(document);
            scrIntegradorMod.loadDocument();
            //scrIntegradorDoc.loadDocument(document);

            OpenPublishColabModWorker modWorker = new OpenPublishColabModWorker(scrIntegradorMod, openPublish);
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            modWorker.execute();

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

    }

    protected void openDocumentCollabDoc(int documentId, boolean openPublish) {
        try {
            CollaborativesDAO collabDAO = new CollaborativesDAO();

            DocumentCollabBO document = collabDAO.getDocument(documentId);
            ScrIntegradorDoc scrIntegradorDoc = new ScrIntegradorDoc();
            scrIntegradorDoc.openDocument(document);
            //scrIntegradorDoc.loadDocument(document);
            //MainView.main.setCollabIntegradorDoc(scrIntegradorDoc);

            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            OpenPublishColabDocWorker collabDocworker = new OpenPublishColabDocWorker(scrIntegradorDoc, openPublish);
            collabDocworker.execute();

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void openPDF(int row) {
        try {
            Utilerias.pasarGarbageCollector();
            SearchTableModel model = (SearchTableModel) table.getModel();
            int rowSel = table.convertRowIndexToModel(row);
            DocumentSearchBO docBO = model.getList().get(rowSel);
            int idDocument = docBO.getDocumentId();

            String nombre = docBO.getDocumentName();
            int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Abrir PDF con nombre\n" + nombre + "?", GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if (docBO.isHistory()){
                    BrowserLauncher launcher = new BrowserLauncher();
                    launcher.openURLinBrowser(docBO.getUrl());
                } else if (docBO.isCollaborative()) {
                    if (GlobalDefines.COLLAB_TYPE_DOC.equals(docBO.getCollaborative_Type())) {
                        //openDocumentCollabDoc(idDocument);
                        PDFCreatorCollaborativeWorker collabWorker = new PDFCreatorCollaborativeWorker(idDocument, docBO);
                        MainView.main.getProgressBar().setVisible(true);
                        MainView.main.getProgressBar().setIndeterminate(true);
                        collabWorker.execute();
                    } else if (GlobalDefines.COLLAB_TYPE_MOD.equals(docBO.getCollaborative_Type())) {
                        //openDocumentCollabMod( idDocument  );
                        //JOptionPane.showMessageDialog(null, "No se pudo generar el PDF de los Módulos Colaborativos.");
                        crearPDF(docBO.getDocumentId(), docBO.getDocumentName(), false);
                    }
                } else {
                    crearPDF(docBO.getDocumentId(), docBO.getDocumentName(), false);
                }
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }

    }

    private void crearPDF(int documentId, String documentName, boolean isCollabType) {

        DocumentDAO dao = new DocumentDAO();
        DocumentBO bo = null;
        try {
            bo = dao.getDocument(documentId, -1,false);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
        }
        PDFCreatorWorker pdfWorker = new PDFCreatorWorker(documentId, documentName, isCollabType, bo, lstCollabPdf);
        MainView.main.getProgressBar().setVisible(true);
        //MainView.main.getProgressBar().setIndeterminate(true);
        pdfWorker.execute();

//
//        try {
//            DocumentDAO dao = new DocumentDAO();
//            DocumentBO bo = dao.getDocument(documentId, -1);
//            try {
//                PdfDocument pdfDoc = new PdfDocument();
//                pdfDoc.createPdfDocument(bo);
//                String fileName = pdfDoc.getFilePath();
//                InputStream inputStream = new FileInputStream(fileName);
//
//                if (isCollabType) {
//
//                    if (lstCollabPdf == null) {
//                        lstCollabPdf = new ArrayList<>();
//                    }
//
//                    lstCollabPdf.add(inputStream);
//
//                } else {
//
//                    /*SwingController controller = new SwingController();
//                    SwingViewBuilder factory = new SwingViewBuilder(controller);
//                    JPanel panel = factory.buildViewerPanel();
//
//                    PDFViewer viewer = new PDFViewer();
//                    viewer.setViewer(panel);
//                    controller.openDocument(inputStream, "", "");
//                    controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false);
//
//                    viewer.setBounds(new Rectangle(600, 400));
//                    Utilerias.centreFrame(viewer, true);
//                    viewer.setVisible(true);*/
//                    BrowserLauncher launcher = new BrowserLauncher();
//                    launcher.openURLinBrowser("file://" + fileName);
//
//                }
//
//            } catch (FileNotFoundException e) {
//                Utilerias.logger(getClass()).error(e);
//            } catch (DocumentException | IOException ex) {
//                Utilerias.logger(getClass()).error(ex);
//            }
//        } catch (Exception exe) {
//            Utilerias.logger(getClass()).error(exe);
//        }
    }

    private void openHTML(int row) {
        try {
            Utilerias.pasarGarbageCollector();
            SearchTableModel model = (SearchTableModel) table.getModel();
            int rowSel = table.convertRowIndexToModel(row);
            DocumentSearchBO docBO = model.getList().get(rowSel);
            int idDocument = docBO.getDocumentId();

            String nombre = docBO.getDocumentName();
            int result = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Abrir HTML con nombre\n" + nombre + "?", GlobalDefines.TITULO_APP, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                HTMLViewWorker workerHTML = new HTMLViewWorker(idDocument);
                MainView.main.getProgressBar().setVisible(true);
                MainView.main.getProgressBar().setIndeterminate(true);
                workerHTML.execute();
            }
        } catch (HeadlessException e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void agregarBusqueda(int idDocument, String nombre) {
        try {
            Properties p = Utilerias.getSettingsFile();
            if (p == null) {
                return;
            }
            //Buscar si ya existe el documento regresar
            if (p.keySet().contains(String.valueOf(idDocument))) {
                return;
            }
            p.setProperty(String.valueOf(idDocument), nombre);
            Utilerias.saveSettingsFile(p);

            //TODO modificar para nueva version
            //MainView.main.refres main.refrescarRecientes();
        } catch (Exception e) {
        }
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            if (table != null
                    && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                customAction(row);
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

class PDFCreatorWorker extends SwingWorker<String, String> {

    int _documentId;
    String _documentName;
    boolean _isCollabType;
    DocumentBO _bo;
    List<InputStream> _lstCollabPdf;

    public PDFCreatorWorker(int documentId, String documentName, boolean isCollabType, DocumentBO bo, List<InputStream> lstCollabPdf) {

        this._documentId = documentId;
        this._documentName = documentName;
        this._isCollabType = isCollabType;
        this._bo = bo;
        this._lstCollabPdf = lstCollabPdf;

    }

    @Override
    protected String doInBackground() {
        String fileName = null;

        try {
            PdfDocument pdfDoc = new PdfDocument();
            if (pdfDoc.createPdfDocument(_bo, false) != null) {
                fileName = pdfDoc.getFilePath();
            }

        } catch (DocumentException | IOException e) {
            Utilerias.logger(getClass()).info(e);
        }

        return fileName;
    }

    @Override
    protected void done() {
        String fileName = null;
        try {
            fileName = get();

            if (fileName != null) {
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser("file://" + fileName);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).info(ex);
        } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }

    }

}

class PDFCreatorCollaborativeWorker extends SwingWorker<List<InputStream>, List<InputStream>> {

    int idDocument;
    DocumentSearchBO docBO;

    public PDFCreatorCollaborativeWorker(int idDocument, DocumentSearchBO docBO) {

        this.idDocument = idDocument;
        this.docBO = docBO;
    }

    @Override
    protected List<InputStream> doInBackground() {

        CollaborativesDAO collabDAO = new CollaborativesDAO();
        DocumentCollabBO document = collabDAO.getDocument(idDocument);
        List<InputStream> lstCollabPdf = new ArrayList<>();

        int cont = 1;
        for (DocumentCollabItemBO coll : document.getLstDocumentCollab()) {
            /*DocumentDAO dao = new DocumentDAO();
            DocumentBO bo = null;
            FileInputStream inputStream = null;*/
            try {
                lstCollabPdf.add( UtileriasSSH.getInstance().crearPDFCollab( coll.getItemDocumentId(), null ) );
            }catch(Exception e){
                Utilerias.logger(getClass()).error(e);
            }
                //bo = dao.getDocument(coll.getDocumentId(), -1);
                /*bo = dao.getDocument(coll.getItemDocumentId(), -1,false);
                PdfDocument pdfDoc = new PdfDocument();

                if (cont < document.getLstDocumentCollab().size()) {
                    pdfDoc.setEliminarDisplosure(true);
                } else {
                    pdfDoc.setEliminarDisplosure(false);
                    DocumentBO boColl = dao.getDocument(docBO.getDocumentId(), -1,false);
                    pdfDoc.setDocBODisplosure(boColl);
                }

                if (pdfDoc.createPdfDocument(bo, false) != null) {
                    String fileName = pdfDoc.getFilePath();
                    Utilerias.logger(getClass()).info("PDF Individual = " + fileName);
                    //inputStream = new FileInputStream(fileName);
                    //lstCollabPdf.add(fileName);
                }

            } catch (SQLException e) {
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
            }
            cont++;*/
        }
        
        try {//Se genera el Directorio con el Disclosure del semanario
            PdfDocument pdfDoc = new PdfDocument();
            
            DocumentDAO docDao = new DocumentDAO();
            
            pdfDoc.createPdfDocumentDisclosureAndDirOnly( docDao.getDocument(idDocument, -1,false) );
            String fileName = pdfDoc.getFilePath();
            lstCollabPdf.add( new FileInputStream(fileName) );
            
        } catch (DocumentException | IOException | SQLException ex) {
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
                DocumentDAO dao = new DocumentDAO();
                DocumentBO bo = dao.getDocument(docBO.getDocumentId(), -1,false);

                String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
                String fileName = dirPDF + docBO.getDocumentId() + "_" + bo.getFileName() + "_" + Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.PDF) + ".pdf";
                output = new FileOutputStream(fileName);
                Utilerias.logger(getClass()).info(fileName);
                MergePDF.concatPDFs(lstCollabPdf, output, true);
                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser("file://" + fileName);
            } catch (FileNotFoundException | BrowserLaunchingInitializingException | UnsupportedOperatingSystemException | SQLException ex) {
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

}

class HTMLViewWorker extends SwingWorker<String, String> {

    int idDocument;

    public HTMLViewWorker(int idDocument) {
        this.idDocument = idDocument;
    }

    @Override
    protected String doInBackground() throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = new Date();

        DocumentDAO dao = new DocumentDAO();
        DocumentBO bo = dao.getDocument(idDocument, -1,false);

        String ruta = UtileriasSSH.getInstance().sendFilesSsh(null, bo, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);
        ruta = "file:///"+ruta;
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
            if (ruta != null) {
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

class OpenDocumentWorker extends SwingWorker<String, String> {

    int idDocument;
    String nombre;
    boolean openPublish;

    public OpenDocumentWorker(int idDocument, String nombre, boolean openPublish) {
        this.idDocument = idDocument;
        this.nombre = nombre;
        this.openPublish = openPublish;
    }

    @Override
    protected String doInBackground() throws Exception {
        publish("");
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        try {
            MainView.main.setDocument(idDocument, nombre, openPublish);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    @Override
    protected void done() {
        MainView.main.getProgressBar().setVisible(false);
    }

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

class OpenPublishColabDocWorker extends SwingWorker<String, String> {

    ScrIntegradorDoc scrIntegradorDoc;
    boolean openPublish;

    public OpenPublishColabDocWorker(ScrIntegradorDoc scrIntegradorDoc, boolean openPublish) {
        this.openPublish = openPublish;
        this.scrIntegradorDoc = scrIntegradorDoc;
    }

    @Override
    protected String doInBackground() throws Exception {
        publish("");
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        try {
            MainView.main.setCollabIntegradorDoc(scrIntegradorDoc, openPublish);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    @Override
    protected void done() {
        MainView.main.getProgressBar().setVisible(false);
    }

}
