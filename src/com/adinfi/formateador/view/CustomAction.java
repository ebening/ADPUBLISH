package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.bos.HojaBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.editor.PdfDocument;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.administration.Catalogs;
import com.adinfi.formateador.view.administration.Disclosure;
import com.adinfi.formateador.view.administration.DocumentType;
import com.adinfi.formateador.view.administration.LinkedExcel;
import com.adinfi.formateador.view.administration.Office;
import com.adinfi.formateador.view.administration.Profiles;
import com.adinfi.formateador.view.administration.Reprocess;
import com.adinfi.formateador.view.administration.Users;
import com.adinfi.formateador.view.designer.Designer;
import com.adinfi.formateador.view.designer.TemplateDesigner;
import com.adinfi.formateador.view.publish.Notify;
import com.adinfi.formateador.view.publish.SendPublish;
import com.adinfi.formateador.view.publish.SendPublishAttachment;
import com.adinfi.formateador.view.publish.Twitter;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfSeccion;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Seccion;
import com.itextpdf.text.DocumentException;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Guillermo Trejo
 */
public class CustomAction {

    public CustomAction() {

    }

    public void executeAction(String action, HashMap<String, String> map) {
        String methodName = map.get(action);
        if (methodName == null || methodName.isEmpty() == true) {
            Utilerias.logger(getClass()).error(new UnsupportedOperationException("Action = " + action));
        } else {
            try {
                //no paramater
                Class noparams[] = {};
                //load the AppTest at runtime
                Class cls = Class.forName("com.adinfi.formateador.view.CustomAction");
                Object obj = cls.newInstance();

                //call the printIt method
                //Method method = cls.getDeclaredMethod(methodName);
                Method method = this.getClass().getDeclaredMethod(methodName);
                method.invoke(obj, (Object[]) noparams);
            } catch (SecurityException | NoSuchMethodException ex) {
                Utilerias.logger(getClass()).error(ex);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
                Utilerias.logger(getClass()).error(ex.getCause());
            } catch (ClassNotFoundException | InstantiationException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }
    }

    public void executeAction(String methodName) {
        if (methodName == null || methodName.isEmpty() == true) {
            Utilerias.logger(getClass()).error(new UnsupportedOperationException("Action = " + methodName));
        } else {
            try {
                //no paramater
                Class noparams[] = {};
                //load the AppTest at runtime
                Class cls = Class.forName("com.adinfi.formateador.view.CustomAction");
                Object obj = cls.newInstance();
                //call the printIt method
                //Method method = cls.getDeclaredMethod(methodName);
                Method method = this.getClass().getDeclaredMethod(methodName);
                method.invoke(obj, (Object[]) noparams);
            } catch (SecurityException | NoSuchMethodException ex) {
                Utilerias.logger(getClass()).error(ex);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Utilerias.logger(getClass()).error(ex);
            } catch (ClassNotFoundException | InstantiationException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
        }
    }

    //Simple action
    protected void goHome() {
        Utilerias.pasarGarbageCollector();
        int result = JOptionPane.showConfirmDialog(MainView.main, "¿Está seguro de regresar al Inicio?", "Formateador", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            MainView.main.viewQuickViewPanel();
            MainView.main.getScrDocument().setDocBO(null);
            MainView.main.getMainPanel().removeAll();
            MainView.main.setTitle(GlobalDefines.TITULO_APP);
            MainView.main.initInfoLabel("", "", "");
        }
    }

    protected void newDocument() {
        try {
            Utilerias.pasarGarbageCollector();
            boolean b = MainView.main.getScrDocument().newDocument(false);
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

    protected void newCollabDocumentDoc() {
       // newCollabDocument(GlobalDefines.TIPO_CAND_DOC);
        MainView.main.newCollabDocument(GlobalDefines.TIPO_CAND_DOC,false);
    }

    protected void newCollabDocumentMod() {
        //newCollabDocument(GlobalDefines.TIPO_CAND_MODULE);
        MainView.main.newCollabDocument(GlobalDefines.TIPO_CAND_MODULE,false);
    }

    protected void addToCollabDocumentDoc() {
        Utilerias.pasarGarbageCollector();
        if (MainView.main.getScrDocument() == null || MainView.main.getScrDocument().getDocBO() == null) {
            Utilerias.showMessage(MainView.main, "Se debe tener abierto un documento para usar esta opción", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (MainView.main.getScrDocument().getDocBO().getDocumentId() <= 0) {
            Utilerias.showMessage(MainView.main, "Se debe tener abierto un documento existente para usar esta opción", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<DocumentSearchBO> lstDocsAdd = null;
        DocumentSearchBO docBO = new DocumentSearchBO();
        Object[] row = null;

        docBO.setDocVersionId(MainView.main.getScrDocument().getDocBO().getDocVersionId());
        docBO.setDocumentName(MainView.main.getScrDocument().getDocBO().getDocumentName());
        lstDocsAdd = new ArrayList<>();
        lstDocsAdd.add(docBO);

        DlgAddDocumentsCollab dlgAddDocs = new DlgAddDocumentsCollab(MainApp.getApplication().getMainFrame(), true);

        dlgAddDocs.setTitle("Agregar documentos colaborativos ");
        dlgAddDocs.setDocumentsAdd(lstDocsAdd);
        dlgAddDocs.setLocation(200, 200);
        dlgAddDocs.setVisible(true);

    }

    //Simple action
    public void newCollabDocument(String tipo) {
        try {
            Utilerias.pasarGarbageCollector();
            DlgSaveCollab dlgSave = new DlgSaveCollab(MainApp.getApplication().getMainFrame(), true, tipo);
            dlgSave.setLocationRelativeTo(dlgSave);
            dlgSave.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            //dlgSave.setTitle(GlobalDefines.TITULO_APP);
            dlgSave.setCollborative(true);
            dlgSave.setVisible(true);
            if (dlgSave.isOk() == false) {
                return;
            }

            DocumentCollabBO document = new DocumentCollabBO();
            document.setCollaborative(true);

            int idms;
            Object obj = dlgSave.getCmbMercado().getSelectedItem();
            if (obj != null) {
                MarketBO marketb = (MarketBO) obj;
                idms = Integer.parseInt(marketb.getIdMiVector_real());
                document.setIdMarket(idms);
                document.setMarketBO(marketb);
            } else {
                return;
            }

            int idDocType;
            Object obj2 = dlgSave.getCmbTipo().getSelectedItem();
            DocumentTypeBO tipoBO = null;
            if (obj2 != null) {
                tipoBO = (DocumentTypeBO) obj2;
                idDocType = tipoBO.getIddocument_type();
                document.setIdDocType(idDocType);
                document.setTipoBO(tipoBO);
            } else {
                return;
            }

            int idLanguage;
            Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
            if (obj3 != null) {
                LanguageBO languageBO = (LanguageBO) obj3;
                idLanguage = languageBO.getIdLanguage();
                document.setIdLanguage(idLanguage);
            } else {
                return;
            }

            int idSubject;
            SubjectBO subjectBO = dlgSave.getSelectedValueSubject();//CboSubject().getSelectedItem();
            if (subjectBO != null) {
                idSubject = subjectBO.getIdSubject();
                document.setIdSubject(idSubject);
            }/* else {
                return;
            }*/

            document.setDocumentName(dlgSave.getEdNombredoc().getText());
            document.setFileName(dlgSave.getEdFileName().getText());
            document.setFavorite(dlgSave.isFavorite().isSelected());

            if (GlobalDefines.TIPO_CAND_DOC.equals(tipo)) {

                ScrIntegradorDoc scrIntegradorDoc = new ScrIntegradorDoc();
                scrIntegradorDoc.newDocument(document);
                //scrIntegradorDoc.loadDocument(document);
                MainView.main.setCollabIntegradorDoc(scrIntegradorDoc);

            } else {

                ScrIntegradorMod scrIntegradorMod = new ScrIntegradorMod();
                scrIntegradorMod.newDocument(document);
                //scrIntegradorDoc.loadDocument(document);
                MainView.main.setCollabIntegradorMod(scrIntegradorMod);
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    protected void openDocument() {
        searchDocument();
    }

    protected void exitApplication() {
        int result = JOptionPane.showConfirmDialog(MainView.main, "¿Está seguro de salir de la aplicación?", "Formateador", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            MainApp.getApplication().closeApplication();
        }
    }

    protected void pdfViewer() {
        Utilerias.pasarGarbageCollector();
        DocumentBO docBO = MainView.main.getScrDocument().getDocBO();

        if (docBO == null) {
            if (MainView.main.getMainPanel().getComponentCount() > 0) {
                if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorDoc) {
                    ScrIntegradorDoc intDoc = (ScrIntegradorDoc) MainView.main.getMainPanel().getComponent(0);
                    docBO = intDoc.getDocument();

                    PDFCreatorCollaborativeWorker collabWorker = new PDFCreatorCollaborativeWorker(docBO.getDocumentId(), docBO, intDoc.getListDocuments());
                    MainView.main.getProgressBar().setVisible(true);
                    MainView.main.getProgressBar().setIndeterminate(true);
                    collabWorker.execute();
                } else if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorMod) {
                    try {
                        docBO = ((ScrIntegradorMod) MainView.main.getMainPanel().getComponent(0)).getDocument();

                        DocumentDAO docDAO = new DocumentDAO();
                        docBO = docDAO.getDocument(docBO.getDocumentId(), -1,false);

                        MainView.main.getProgressBar().setVisible(true);
                        MainView.main.getProgressBar().setIndeterminate(true);
                        PDFViewerWorker pDFViewerWorker = new PDFViewerWorker(docBO);
                        pDFViewerWorker.execute();
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    Utilerias.logger(getClass()).info("The software does not have documents to show");
                    JOptionPane.showMessageDialog(null, "Es necesario tener un documento activo para editar", "No es posible continuar",JOptionPane.WARNING_MESSAGE);
                }
            } else {
                Utilerias.logger(getClass()).info("The software does not have documents to show");
                JOptionPane.showMessageDialog(null, "Es necesario tener un documento activo para editar", "No es posible continuar",JOptionPane.WARNING_MESSAGE);
            }
        } else {
            MainView.main.getProgressBar().setVisible(true);
            MainView.main.getProgressBar().setIndeterminate(true);
            PDFViewerWorker pDFViewerWorker = new PDFViewerWorker(docBO);
            pDFViewerWorker.execute();
        }
//
//        DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
//        if (docBO == null) {
//            Utilerias.logger(getClass()).info("The software does not have documents to show");
//        } else {
//
//            PDFViewerWorker pDFViewerWorker = new PDFViewerWorker(docBO);
//            pDFViewerWorker.run();
//            try {
//                PdfDocument pdfDoc = new PdfDocument();
//                pdfDoc.createPdfDocument(docBO);
//                String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
//                String fileName = dirPDF + docBO.getDocumentId() + "_" + docBO.getFileName() + ".pdf";
//                InputStream inputStream = new FileInputStream(fileName);
//                SwingController controller = new SwingController();
//                SwingViewBuilder factory = new SwingViewBuilder(controller);
//                JPanel panel = factory.buildViewerPanel();
//
//                PDFViewer viewer = new PDFViewer();
//                viewer.setViewer(panel);
//                controller.openDocument(inputStream, "", "");
//                controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false);
//
//                viewer.setBounds(new Rectangle(600, 400));
//                Utilerias.centreFrame(viewer, true);
//                viewer.setVisible(true);
//
//            } catch (FileNotFoundException e) {
//                Utilerias.logger(getClass()).error(e);
//            } catch (DocumentException | IOException ex) {
//                Utilerias.logger(getClass()).error(ex);
//            }
//        }
    }

    public void pdfViewerFromDocument(DocumentBO docBO) {

//        PDFViewerWorker pDFViewerWorker = new PDFViewerWorker(docBO);
//        MainView.main.getProgressBar().setVisible(true);
//        MainView.main.getProgressBar().setIndeterminate(true);
//        pDFViewerWorker.run();
//        if (docBO == null) {
//            Utilerias.logger(getClass()).info("The software does not have documents to show");
//        } else {
//            try {
//                PdfDocument pdfDoc = new PdfDocument();
//                pdfDoc.createPdfDocument(docBO);
//                String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
//                String fileName = dirPDF + docBO.getDocumentId() + "_" + docBO.getFileName() + ".pdf";
//                InputStream inputStream = new FileInputStream(fileName);
//                SwingController controller = new SwingController();
//                SwingViewBuilder factory = new SwingViewBuilder(controller);
//                JPanel panel = factory.buildViewerPanel();
//
//                PDFViewer viewer = new PDFViewer();
//                viewer.setViewer(panel);
//                controller.openDocument(inputStream, "", "");
//                controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false);
//
//                viewer.setBounds(new Rectangle(600, 400));
//                Utilerias.centreFrame(viewer, true);
//                viewer.setVisible(true);
//
//            } catch (FileNotFoundException e) {
//                Utilerias.logger(getClass()).error(e);
//            } catch (DocumentException | IOException ex) {
//                Utilerias.logger(getClass()).error(ex);
//            }
//        }
    }

    protected void editDocument() {
        Utilerias.pasarGarbageCollector();
        DocumentBO docbo = MainView.main.getScrDocument().getDocBO();
        if (docbo == null) {
            JOptionPane.showMessageDialog(null, "Es necesario tener un documento activo para editar", "No es posible continuar",JOptionPane.WARNING_MESSAGE);
            return;
        }
        DlgUpdate dlgSave = new DlgUpdate(MainApp.getApplication().getMainFrame(), true, true);
        Utilerias.centreDialog(dlgSave, true);
        dlgSave.setVisible(true);

        if (dlgSave.isOk() == false) {
            return;
        }
        MainView.main.getScrDocument().getDocBO().setDocumentName(dlgSave.getEdNombredoc().getText());

        int idms;
        Object obj = dlgSave.getCmbMercado().getSelectedItem();
        if (obj != null) {
            MarketBO marketb = (MarketBO) obj;
            idms = Integer.parseInt(marketb.getIdMiVector_real());
            MainView.main.getScrDocument().getDocBO().setIdMarket(idms);
        } else {
            return;
        }

        int idDocType;
        Object obj2 = dlgSave.getCmbTipo().getSelectedItem();
        DocumentTypeBO tipoBO = null;
        if (obj2 != null) {
            tipoBO = (DocumentTypeBO) obj2;
            idDocType = tipoBO.getIddocument_type();
            MainView.main.getScrDocument().getDocBO().setIdDocType(idDocType);
        } else {
            return;
        }

        int idLanguage;
        Object obj3 = dlgSave.getcboIdioma().getSelectedItem();
        if (obj3 != null) {
            LanguageBO languageBO = (LanguageBO) obj3;
            idLanguage = languageBO.getIdLanguage();
            MainView.main.getScrDocument().getDocBO().setIdLanguage(idLanguage);
        } else {
            return;
        }

        int idSubject;
        //Object obj4 = dlgSave.getCboSubject().getSelectedItem();
        SubjectBO subjectBO = dlgSave.getSelectedValueSubject();
        if (subjectBO != null) {
            //subjectBO = (SubjectBO) obj4;
            idSubject = subjectBO.getIdSubject();
            MainView.main.getScrDocument().getDocBO().setIdSubject(idSubject);
        } else { // Se elimina el tema
            idSubject = 0;
            MainView.main.getScrDocument().getDocBO().setIdSubject(idSubject);
        }

        MainView.main.getScrDocument().setDocumentName(dlgSave.getEdNombredoc().getText());
        MainView.main.getScrDocument().getDocBO().setFileName(dlgSave.getEdFileName().getText());

        //MainView.main.getScrDocument().contentPanel.removeAll();
        //this.tabHojas.removeAll();
        //this.addHoja(true);
        MainView.main.getScrDocument().getDocBO().setFavorite(dlgSave.isFavorite().isSelected());

        MainView.main.initInfoLabel(tipoBO == null ? "" : tipoBO.getName(), subjectBO == null ? "" : subjectBO.getName(), dlgSave.getEdNombredoc().getText());

        if (MainView.main.getScrDocument().saveDocument()) {
            Utilerias.showMessage(MainView.main, "Documento guardado correctamente", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Utilerias.showMessage(MainView.main, "Error al guardar el documento", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void searchDocument() {
        Utilerias.pasarGarbageCollector();
        int idDocument = MainView.main.getScrDocument().getDocumentId();
        if (idDocument != -1) {
            int result = JOptionPane.showConfirmDialog(
                    MainApp.getApplication().getMainFrame(), "¿Cerrar documento en area de trabajo?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        MainView.main.setSearchPanel();
        MainView.main.getScrDocument().setDocBO(null);
        MainView.main.getMainPanel().removeAll();
        MainView.main.setTitle(GlobalDefines.TITULO_APP);
        MainView.main.initInfoLabel("", "", "");
        //MainView.main.removeAll();

        //docBOCollab = null;
    }

    protected void adminDocumentType() {
        Utilerias.pasarGarbageCollector();
        DocumentType dt = new DocumentType(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);           
    }

    protected void adminLinkedExcel() {
        Utilerias.pasarGarbageCollector();
        LinkedExcel dialog = new LinkedExcel(MainView.main, true);
        dialog.setLocationRelativeTo(MainView.main);
        dialog.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        //dialog.setTitle(GlobalDefines.TITULO_APP);
        dialog.setVisible(true);
    }

    protected void adminCatalogs() {
        Utilerias.pasarGarbageCollector();
        Catalogs dt = new Catalogs(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }

    protected void adminUsers() {
        Utilerias.pasarGarbageCollector();
        Users dt = new Users(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }

    protected void closeSession() {
        int response = JOptionPane.showConfirmDialog(MainApp.getApplication().getMainFrame(), "¿Está seguro de cerrar la sesión?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Utilerias.closeSession();
            MainApp.getApplication().closeSession();
        }
    }

    protected void openOffice() {
        Utilerias.pasarGarbageCollector();
        Office dt = new Office(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }
    
    protected void openReproceso() {
        Utilerias.pasarGarbageCollector();
        Reprocess rep = new Reprocess(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(rep, true);
        rep.setVisible(true);
    }

    protected void showProfiles() {
        Utilerias.pasarGarbageCollector();
        Profiles dt = new Profiles(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }

    protected void showDisclosure() {
        Utilerias.pasarGarbageCollector();
        Disclosure dt = new Disclosure(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }

    /*Administrar módulo*/
    protected void adminModule() {
        Utilerias.pasarGarbageCollector();
        if (MainView.main.getScrDocument() != null
                && MainView.main.getScrDocument().getDocumentId() != 0) {
            int result = JOptionPane.showConfirmDialog(
                    MainApp.getApplication().getMainFrame(), "¿Cerrar documento en area de trabajo?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                Designer designer = new Designer();
                MainView.main.setCustomComponent(designer);
                MainView.main.setTitle(GlobalDefines.TITULO_APP);
                MainView.main.initInfoLabel("", "", "");
            }
        }
    }

    /*Administrar Plantilla*/
    protected void adminTemplate() {
        Utilerias.pasarGarbageCollector();
        if (MainView.main.getScrDocument() != null
                && MainView.main.getScrDocument().getDocumentId() != 0) {
            int result = JOptionPane.showConfirmDialog(
                    MainApp.getApplication().getMainFrame(), "¿Cerrar documento en area de trabajo?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                TemplateDesigner templateDesigner = new TemplateDesigner();
                MainView.main.setCustomComponent(templateDesigner);
                MainView.main.setTitle(GlobalDefines.TITULO_APP);
                MainView.main.initInfoLabel("", "", "");
            }
        }
    }

    /*Publicar*/
    protected void showSendPublish() {
        Utilerias.pasarGarbageCollector();
        HojaBO hojaBO = null;
        Integer numHoja = null;

        if (MainView.main.getScrDocument().getDocBO() != null) {

            if (MainView.main.getScrDocument().getDocBO().getDocumentId() > 0) {

                SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
                Utilerias.centreDialog(dt, true);
                dt.setVisible(dt.showDialog);

            } else {
                Set set = MainView.main.getScrDocument().getDocBO().getMapHojas().keySet();
                Iterator it = set.iterator();
                List<ModuleBO> lstModules = null;

                while (it.hasNext()) {
                    numHoja = (Integer) it.next();
                    hojaBO = MainView.main.getScrDocument().getDocBO().getMapHojas().get(numHoja);
                    break;
                }

                lstModules = hojaBO.getLstModules();
                if (lstModules != null) {

                    MainView.main.getProgressBar().setIndeterminate(true);
                    MainView.main.getProgressBar().setVisible(true);
                    SaveDocumentWorker worker = new SaveDocumentWorker(true);
                    worker.execute();

                } else {
                    Utilerias.showMessage(MainView.main, "El Documento esta vacio", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else {
            Utilerias.showMessage(MainView.main, "No hay documento activo para enviar", JOptionPane.ERROR_MESSAGE);
        }

    }

    /*Publicar con Adjunto*/
    protected void showSendPublishAttach() {
        Utilerias.pasarGarbageCollector();
        SendPublishAttachment dt = new SendPublishAttachment(MainApp.getApplication().getMainFrame(), true, 0, false);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }


    /*Twitter*/ 
    protected void showSendTwitter() {
        Utilerias.pasarGarbageCollector();
        Twitter dt = new Twitter(MainApp.getApplication().getMainFrame(), true);
        Utilerias.centreDialog(dt, true);
        dt.setVisible(true);
    }

    /*Enviar Notificación*/
    protected void showSendNotify() {
        Utilerias.pasarGarbageCollector();
        if (MainView.main.getScrDocument() == null || MainView.main.getScrDocument().getDocBO() == null) {
            Utilerias.showMessage(MainView.main, "Se debe tener abierto un documento para usar esta opción", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Notify nfy = new Notify(MainApp.getApplication().getMainFrame(), true);
            Utilerias.centreDialog(nfy, true);
            nfy.setVisible(true);
        }
    }

    //Worker actions 
    protected void saveDocument() {
        Utilerias.pasarGarbageCollector();
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SaveDocumentWorker worker = new SaveDocumentWorker();
        worker.execute();
    }

    protected void showDlgModuloColaborativo() {
        try {
            Utilerias.pasarGarbageCollector();
            boolean b = MainView.main.getScrDocument().newDocument(true);
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

    protected void saveAsDocument() {
        
        Utilerias.pasarGarbageCollector();
        if( MainView.main.getMainPanel()!=null && 
            MainView.main.getMainPanel().getComponents()!=null &&
            MainView.main.getMainPanel().getComponents().length>0 &&
            MainView.main.getMainPanel().getComponents()[0]!=null    ){
            
            if( MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorDoc ){
                MainView.main.newCollabDocument(GlobalDefines.TIPO_CAND_DOC, true);                
                return ;
            
            }else if(  MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorMod  ){
                /*
                scrIntMod=(ScrIntegradorMod)MainView.main.getMainPanel().getComponents()[0];
                scrIntMod.saveDocument();
                */
                 MainView.main.newCollabDocument(GlobalDefines.TIPO_CAND_MODULE , true);      
                return ;

                
            }
        }
        
        
        
        
        
        
        int idDocument = MainView.main.getScrDocument().getDocumentId();
        if (idDocument == -1) {
            return;
        }

        if (MainView.main.getScrDocument().getDocBO().isCollaborative()) {
            JOptionPane.showMessageDialog(null, "Este documento es de tipo Módulo Colaborativo por lo que no genera copia.");
            return;
        }

        DlgUpdate dl = new DlgUpdate(MainApp.getApplication().getMainFrame(), true, false);
        Utilerias.centreDialog(dl, true);
        dl.setVisible(true);

        if (dl.isOk()) {
            MainView.main.getProgressBar().setIndeterminate(true);
            MainView.main.getProgressBar().setVisible(true);
            SaveAsDocumentWorker worker = new SaveAsDocumentWorker(dl);
            worker.execute();
        }
    }

    protected void htmlViewer() {
        Utilerias.pasarGarbageCollector();
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        HTMLViewerWorker worker = new HTMLViewerWorker();
        worker.execute();
    }

}

class SaveDocumentWorker extends SwingWorker<Boolean, Boolean> {

    boolean openPublish = false;

    SaveDocumentWorker() {

    }

    SaveDocumentWorker(boolean openPublish) {
        this.openPublish = openPublish;
    }

    @Override
    protected Boolean doInBackground() {
        if (MainView.main == null) {
            return false;
        }

        ScrIntegradorDoc scrIntDoc=null;
        ScrIntegradorMod scrIntMod=null;
        MainView.main.getScrDocument().saveTexts();
       // System.out.println(MainView.main.getMainPanel().getComponents()[0].getClass().getName());
        if( MainView.main.getMainPanel()!=null && 
            MainView.main.getMainPanel().getComponents()!=null &&
            MainView.main.getMainPanel().getComponents().length>0 &&
            MainView.main.getMainPanel().getComponents()[0]!=null    ){
            
            if( MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorDoc ){
                scrIntDoc=(ScrIntegradorDoc)MainView.main.getMainPanel().getComponents()[0];
                scrIntDoc.saveDocument();
                
                return true;
            
            }else if(  MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorMod  ){
                scrIntMod=(ScrIntegradorMod)MainView.main.getMainPanel().getComponents()[0];
                scrIntMod.saveDocument();
                
                return true;

                
            }
        }
        
        
        return MainView.main.getScrDocument().saveDocument();
    }

    @Override
    protected void done() {
        
        if( MainView.main.getMainPanel()!=null && 
            MainView.main.getMainPanel().getComponents()!=null &&
            MainView.main.getMainPanel().getComponents().length>0 &&
            MainView.main.getMainPanel().getComponents()[0]!=null    ){
            
            if( MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorDoc ||
                MainView.main.getMainPanel().getComponents()[0] instanceof ScrIntegradorMod   ){
                
                return ;
            
            }
        
        }
        
        
        
        Boolean b = false;
        try {
            b = get();
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        try {
            if (b != null && b == true) {
                if (openPublish == false) {
                    Utilerias.showMessage(MainView.main, "Documento guardado correctamente", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    SendPublish dt = new SendPublish(MainApp.getApplication().getMainFrame(), true);
                    Utilerias.centreDialog(dt, true);
                    dt.setVisible(dt.showDialog);
                }
            } else {
                Utilerias.showMessage(MainView.main, "Error al guardar el documento", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }
    }
}

class SaveAsDocumentWorker extends SwingWorker<Boolean, Boolean> {

    private DlgUpdate dl;
    private boolean openPublish = false;

    SaveAsDocumentWorker() {

    }

    SaveAsDocumentWorker(boolean openPublish) {
        this.openPublish = openPublish;
    }

    SaveAsDocumentWorker(DlgUpdate dl) {
        this.dl = dl;
    }

    @Override
    protected Boolean doInBackground() {
        if (MainView.main == null) {
            return false;
        }
        MainView.main.getScrDocument().saveTexts();
        return MainView.main.getScrDocument().saveDocumentAs(dl);
    }

    @Override
    protected void done() {
        Boolean b = false;
        try {
            b = get();
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        try {
            if (b != null && b == true) {
                Utilerias.showMessage(MainView.main, "Documento guardado correctamente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Utilerias.showMessage(MainView.main, "Error al guardar el documento", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }
    }
}

class HTMLViewerWorker extends SwingWorker<String, String> {

    HTMLViewerWorker() {

    }

    @Override
    protected String doInBackground() {
        String s = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fecha = new Date();
            //HTMLDocument htmlDoc = new HTMLDocument();
            DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
            if (docBO == null) {
                if (MainView.main.getMainPanel().getComponentCount() > 0) {
                    if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorDoc) {
                        ScrIntegradorDoc intDoc = (ScrIntegradorDoc) MainView.main.getMainPanel().getComponent(0);
                        docBO = intDoc.getDocument();
                        HTMLViewWorker workerHTML = new HTMLViewWorker(docBO.getDocumentId(), intDoc.getListDocuments());
                        MainView.main.getProgressBar().setVisible(true);
                        MainView.main.getProgressBar().setIndeterminate(true);
                        workerHTML.execute();
                    } else if (MainView.main.getMainPanel().getComponent(0) instanceof ScrIntegradorMod) {
                        docBO = ((ScrIntegradorMod) MainView.main.getMainPanel().getComponent(0)).getDocument();

                        DocumentDAO docDAO = new DocumentDAO();
                        docBO = docDAO.getDocument(docBO.getDocumentId(), -1,false);

                        String fileName = UtileriasSSH.getInstance().sendFilesSsh(null, docBO, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);//htmlDoc.createHTMLDocument(docBO);
                        if (fileName == null || fileName.isEmpty() == true) {
                            s = null;
                        } else {
                            s = fileName;//"file://" + fileName;
                        }
                    } else {
                        Utilerias.logger(getClass()).info("The software does not have documents to show");
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "Es necesario tener un documento activo para editar", "No es posible continuar",JOptionPane.WARNING_MESSAGE);
                        });    
                    }
                } else {
                    Utilerias.logger(getClass()).info("The software does not have documents to show");
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Es necesario tener un documento activo para editar", "No es posible continuar",JOptionPane.WARNING_MESSAGE);
                    }); 
                }
            } else {
                String fileName = UtileriasSSH.getInstance().sendFilesSsh(null, docBO, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);//htmlDoc.createHTMLDocument(docBO);
                if (fileName == null || fileName.isEmpty() == true) {
                    s = null;
                } else {
                    s = fileName;//"file://" + fileName;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        return s;
    }

    @Override
    protected void done() {
        String s = null;
        if (Desktop.isDesktopSupported() == true) {
            try {
                s = get();
                if (s != null) {
                    try {
                        //URI uri = URI.create(s);
                        BrowserLauncher launcher = new BrowserLauncher();
                        launcher.openURLinBrowser(s/*uri.toString()*/);
                    } catch (Exception ex) {
                        Utilerias.logger(getClass()).info(ex);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
            } finally {
                MainView.main.getProgressBar().setVisible(false);
            }
        }
    }
}

class PDFViewerWorker extends SwingWorker<String, String> {

    private final DocumentBO docBO;

    PDFViewerWorker(DocumentBO bo) {
        this.docBO = bo;
    }

    @Override
    protected String doInBackground() throws Exception {
        String fileName = null;
        try {
            PdfDocument pdfDoc = new PdfDocument();
            if (pdfDoc.createPdfDocument(docBO, false) != null) {
                fileName = pdfDoc.getFilePath();
            }

        } catch (FileNotFoundException e) {
            Utilerias.logger(getClass()).error(e);
        } catch (DocumentException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return fileName;
    }

    @Override
    protected void done() {
        String fileName = null;
        try {
            fileName = get();

            if (Desktop.isDesktopSupported() == true && fileName != null) {

                BrowserLauncher launcher = new BrowserLauncher();
                launcher.openURLinBrowser("file://" + fileName);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Utilerias.logger(getClass()).info(ex);
        } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
            Logger.getLogger(PDFViewerWorker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MainView.main.getProgressBar().setVisible(false);
        }

    }

}
