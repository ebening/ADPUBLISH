package com.adinfi.formateador.main;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.dao.QuickViewDAO;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.CustomAction;
import com.adinfi.formateador.view.PanelModulos;
import com.adinfi.formateador.view.PanelPlantillas;
import com.adinfi.ws.ArrayOfSeccion;
import com.adinfi.ws.Seccion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.SwingUtilities;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.AbstractRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;
import static org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority.TOP;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

/**
 *
 * @author Guillermo Trejo
 */
public class RibbonMenu {

    private RibbonApplicationMenuEntryPrimary primary1 = null;
    private boolean ENABLE_ALL_SECTIONS = false;

    public enum MENU_NAMES {

        PRINCIPAL(1),
        NuevoDoc(2),
        AbrirDoc(3),
        GuardarDoc(4),
        GuardarComoDoc(5),
        //Seccion de Editar correcta
        EDITAR(6),
        AgregarPlantillas(7),
        AgregarModulos(8),
        VerPDF(9),
        VerHTML(10),
        EditarDocumento(11),
        //Seccion de Publicar correcta
        PUBLICAR(12),
        EnviarPublicacion(13),
        EnviarDocAdjunto(14),
        EnviarTwitter(15),
        //Seccion de busqueda correcta
        BUSCAR(16),
        BuscarDoc(17),
        //Eliminar busqueda avanzada de perfiles
        //BusquedaAvanzadaDoc(18),

        //Seccion de colaborativo completa
        COLABORAR(19),
        AgregarDocColaborativo(20),
        NuevoModuloColaborativo(21),
        IntegradorDocumentos(22),
        IntegradorModulosColaborativos(33),
        //Seccion de administración completa
        ADMIN(23),
        AdminTipoDoc(24),
        AdminModulos(25),
        AdminPlantillas(26),
        AdminDisclousure(27),
        AdminUsuario(28),
        AdminPerfiles(29),
        AdminCatalogos(30),
        AdminOficinas(31),
        AdminExcel(32),
        AdminReproceso(35);

        private final int value;

        private MENU_NAMES(int value) {
            this.value = value;
        }

        public int getId() {
            return value;
        }
    }

    public static final String PDF_VIEWER = "pdfViewer";
    public static final String HTML_VIEWER = "htmlViewer";

    private HashMap<String, String> actionBinding;
    private HashMap<String, JCommandButton> buttons;
    private static JRibbon ribbon;
    private ArrayOfSeccion secciones;
    //private WindowActions;

    public RibbonMenu() {

    }

    private Seccion getSeccion(ArrayOfSeccion arrayOfSeccion, int id) {
        Seccion seccion = null;
        try {

            Seccion[] secciones_ = arrayOfSeccion.getSeccion();
            for (int i = 0; i < secciones_.length; i++) {
                int sectionID = secciones_[i].getSeccionId();
                if (sectionID == id) {
                    seccion = secciones_[i];
                    break;
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        return seccion;
    }

    private void enableSections(Seccion[] sec) {

        for (int i = 0; i < sec.length; i++) {
            sec[i].setIsActiv(true);
            if (sec[i].getChildren() != null
                    && sec[i].getChildren().getSeccion() != null
                    && sec[i].getChildren().getSeccion().length != 0) {
                enableSections(sec[i].getChildren().getSeccion());
            }
        }
    }

    public Boolean getMenu(JRibbon ribbon, ArrayOfSeccion arrayOfSeccion) {
        boolean createMenu = false;
        this.ribbon = ribbon;
        this.secciones = arrayOfSeccion;
        createActionBinding();
        createCommandButtons();

        if (secciones == null) {
            return createMenu;
        }

        if (ENABLE_ALL_SECTIONS) {
            enableSections(secciones.getSeccion());
        }

        Seccion seccionPrincipal = getSeccion(arrayOfSeccion, MENU_NAMES.PRINCIPAL.getId());
        Seccion seccionEditar = getSeccion(arrayOfSeccion, MENU_NAMES.EDITAR.getId());
        Seccion seccionPublicar = getSeccion(arrayOfSeccion, MENU_NAMES.PUBLICAR.getId());
        Seccion seccionBuscar = getSeccion(arrayOfSeccion, MENU_NAMES.BUSCAR.getId());
        Seccion seccionColaborar = getSeccion(arrayOfSeccion, MENU_NAMES.COLABORAR.getId());
        Seccion seccionAdmin = getSeccion(arrayOfSeccion, MENU_NAMES.ADMIN.getId());

        addTaskEdit(seccionEditar);
        addTaskPublish(seccionPublicar);
        addTaskFind(seccionBuscar);
        addTaskCollaborative(seccionColaborar);
        addTaskAdmin(seccionAdmin);

        addApplicationMenu(seccionPrincipal);
        addShorcutBar(seccionPrincipal, seccionPublicar);

        return createMenu = true;
    }

    protected void createActionBinding() {

        actionBinding = new HashMap<>();
        actionBinding.put(Utilerias.ICONS.HOME.getCaption(), "goHome");
        actionBinding.put(Utilerias.ICONS.SMALL_HOME.getCaption(), "goHome");
        actionBinding.put(Utilerias.ICONS.NEW.getCaption(), "newDocument");
        actionBinding.put(Utilerias.ICONS.OPEN.getCaption(), "openDocument");
        actionBinding.put(Utilerias.ICONS.BOOKMARK_MODULE.getCaption(), "showDlgModuloColaborativo");
        actionBinding.put(Utilerias.ICONS.SAVE.getCaption(), "saveDocument");
        actionBinding.put(Utilerias.ICONS.SAVE_AS.getCaption(), "saveAsDocument");
        actionBinding.put(Utilerias.ICONS.SESSION_CLOSE.getCaption(), "closeSession");
        actionBinding.put(Utilerias.ICONS.EXIT.getCaption(), "exitApplication");

        //Eventos que se generan con el metodo createSimpleButton
        actionBinding.put(Utilerias.ICONS.PDF.getCaption(), PDF_VIEWER);
        actionBinding.put(Utilerias.ICONS.HTML.getCaption(), HTML_VIEWER);
        actionBinding.put(Utilerias.ICONS.EDIT_DOCUMENT.getCaption(), "editDocument");

        actionBinding.put(Utilerias.ICONS.SEARCH_DOCUMENT.getCaption(), "searchDocument");
        actionBinding.put(Utilerias.ICONS.ADVANCED_SEARCH.getCaption(), "advancedSeardch");

        /*Administración*/
        actionBinding.put(Utilerias.ICONS.ADMIN_DOCUMENT_TYPE.getCaption(), "adminDocumentType");
        actionBinding.put(Utilerias.ICONS.ADMIN_EXCEL.getCaption(), "adminLinkedExcel");
        actionBinding.put(Utilerias.ICONS.ADMIN_CATALOG.getCaption(), "adminCatalogs");
        actionBinding.put(Utilerias.ICONS.ADMIN_USERS.getCaption(), "adminUsers");
        actionBinding.put(Utilerias.ICONS.ADMIN_OFFICE.getCaption(), "openOffice");
        actionBinding.put(Utilerias.ICONS.ADMIN_PROFILE.getCaption(), "showProfiles");
        actionBinding.put(Utilerias.ICONS.ADMIN_DISCLOSURE.getCaption(), "showDisclosure");
        actionBinding.put(Utilerias.ICONS.ADMIN_MODULE.getCaption(), "adminModule");
        actionBinding.put(Utilerias.ICONS.ADMIN_TEMPLATE.getCaption(), "adminTemplate");

        actionBinding.put(Utilerias.ICONS.SEND.getCaption(), "showSendPublish");
        actionBinding.put(Utilerias.ICONS.SEND_EXTERNAL_DOCUMENT.getCaption(), "showSendPublishAttach");
        actionBinding.put(Utilerias.ICONS.SEND_TWEETER.getCaption(), "showSendTwitter");
        actionBinding.put(Utilerias.ICONS.SMALL_NOTIFY.getCaption(), "showSendNotify");

        actionBinding.put(Utilerias.ICONS.BOOKMARK_DOCUMENT.getCaption(), "addToCollabDocumentDoc");

        actionBinding.put(Utilerias.ICONS.INTEGRATE.getCaption(), "newCollabDocumentDoc");
        actionBinding.put(Utilerias.ICONS.INTEGRATE_MOD.getCaption(), "newCollabDocumentMod");
        actionBinding.put(Utilerias.ICONS.INTERFAZ_REPROCESO.getCaption(), "openReproceso");

    }

    protected void createCommandButtons() {

        buttons = new HashMap<>();
        JCommandButton button = null;

        /*
         * Editar
         */
        //Diseño de documento
        button = createPlantillasButton(Utilerias.ICONS.TEMPLATE);
        buttons.put(Utilerias.ICONS.TEMPLATE.getCaption(), button);
        button = createModulosButton(Utilerias.ICONS.MODULE);
        buttons.put(Utilerias.ICONS.MODULE.getCaption(), button);

        //Previsualizar
        button = createSimpleButton(Utilerias.ICONS.PDF, KeyEvent.VK_P);
        buttons.put(Utilerias.ICONS.PDF.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.HTML, KeyEvent.VK_I);
        buttons.put(Utilerias.ICONS.HTML.getCaption(), button);

        //Documento
        button = createSimpleButton(Utilerias.ICONS.EDIT_DOCUMENT, null);
        buttons.put(Utilerias.ICONS.EDIT_DOCUMENT.getCaption(), button);

        /*
         * Publicar
         */
        //Envio de documentos
        button = createSimpleButton(Utilerias.ICONS.SEND, null);
        buttons.put(Utilerias.ICONS.SEND.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.SEND_EXTERNAL_DOCUMENT, null);
        buttons.put(Utilerias.ICONS.SEND_EXTERNAL_DOCUMENT.getCaption(), button);

        //Sociales
        button = createSimpleButton(Utilerias.ICONS.SEND_TWEETER, null);
        buttons.put(Utilerias.ICONS.SEND_TWEETER.getCaption(), button);

        /*
         * Buscar
         */
        button = createSimpleButton(Utilerias.ICONS.SEARCH_DOCUMENT, KeyEvent.VK_F);
        buttons.put(Utilerias.ICONS.SEARCH_DOCUMENT.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADVANCED_SEARCH, KeyEvent.VK_A);
        buttons.put(Utilerias.ICONS.ADVANCED_SEARCH.getCaption(), button);

        /*
         * Integrar
         */
        button = createSimpleButton(Utilerias.ICONS.BOOKMARK_DOCUMENT, null);
        buttons.put(Utilerias.ICONS.BOOKMARK_DOCUMENT.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.BOOKMARK_MODULE, null);
        buttons.put(Utilerias.ICONS.BOOKMARK_MODULE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.INTEGRATE, null);
        buttons.put(Utilerias.ICONS.INTEGRATE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.INTEGRATE_MOD, null);
        buttons.put(Utilerias.ICONS.INTEGRATE_MOD.getCaption(), button);


        /*
         * Administración
         */
        button = createSimpleButton(Utilerias.ICONS.ADMIN_DOCUMENT_TYPE, null);
        buttons.put(Utilerias.ICONS.ADMIN_DOCUMENT_TYPE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_MODULE, null);
        buttons.put(Utilerias.ICONS.ADMIN_MODULE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_TEMPLATE, null);
        buttons.put(Utilerias.ICONS.ADMIN_TEMPLATE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_DISCLOSURE, null);
        buttons.put(Utilerias.ICONS.ADMIN_DISCLOSURE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_USERS, null);
        buttons.put(Utilerias.ICONS.ADMIN_USERS.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_PROFILE, null);
        buttons.put(Utilerias.ICONS.ADMIN_PROFILE.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_CATALOG, null);
        buttons.put(Utilerias.ICONS.ADMIN_CATALOG.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_EXCEL, null);
        buttons.put(Utilerias.ICONS.ADMIN_EXCEL.getCaption(), button);
        button = createSimpleButton(Utilerias.ICONS.ADMIN_OFFICE, null);
        buttons.put(Utilerias.ICONS.ADMIN_OFFICE.getCaption(), button);
        
        button = createSimpleButton(Utilerias.ICONS.INTERFAZ_REPROCESO, null);
        buttons.put(Utilerias.ICONS.INTERFAZ_REPROCESO.getCaption(), button);

    }

    protected JCommandButton createSimpleButton(final Utilerias.ICONS icon, Integer mnemonic) {
        ResizableIcon image = Utilerias.getResizableIcon(icon.toString(), GlobalDefines.DEFAULT_MENU_IMAGE_WIDTH, GlobalDefines.DEFAULT_MENU_IMAGE_HEIGHT);
        JCommandButton button = new JCommandButton(icon.getCaption(), image);
        ActionListener event = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomClickAction(icon.getCaption());
            }
        };
        button.addActionListener(event);
        WindowActions.getInstance().addAction(icon.getCaption(), mnemonic, event);
        return button;
    }

    protected JCommandButton createPlantillasButton(Utilerias.ICONS icon) {
        ResizableIcon image = Utilerias.getResizableIcon(icon.toString(), GlobalDefines.DEFAULT_MENU_IMAGE_WIDTH, GlobalDefines.DEFAULT_MENU_IMAGE_HEIGHT);
        JCommandButton commandPlantillas = new JCommandButton(icon.getCaption(), image);
        commandPlantillas.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
        commandPlantillas.setDisplayState(CommandButtonDisplayState.MEDIUM);
        commandPlantillas.setFlat(false);

        commandPlantillas.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                PanelPlantillas panelPlantillas = new PanelPlantillas();
                return panelPlantillas;
            }
        });
        return commandPlantillas;
    }

    protected JCommandButton createModulosButton(Utilerias.ICONS icon) {
        ResizableIcon image = Utilerias.getResizableIcon(icon.toString(), GlobalDefines.DEFAULT_MENU_IMAGE_WIDTH, GlobalDefines.DEFAULT_MENU_IMAGE_HEIGHT);
        JCommandButton commandModulos = new JCommandButton(icon.getCaption(), image);
        commandModulos.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
        commandModulos.setDisplayState(CommandButtonDisplayState.MEDIUM);
        commandModulos.setFlat(false);

        commandModulos.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                PanelModulos panelModulos = new PanelModulos();

                return panelModulos;
            }
        });
        return commandModulos;
    }

    protected void addTaskEdit(Seccion seccionEditar) {

        if (seccionEditar.getIsActiv() == true) {
            List<AbstractRibbonBand> bands = new ArrayList<>();

            ArrayOfSeccion childs = seccionEditar.getChildren();
            Seccion seccionPlantilla = getSeccion(childs, MENU_NAMES.AgregarPlantillas.getId());
            Seccion seccionModulo = getSeccion(childs, MENU_NAMES.AgregarModulos.getId());

            if (seccionPlantilla.getIsActiv() == true
                    || seccionModulo.getIsActiv() == true) {

                // Diseño de documento
                JRibbonBand designBand = new JRibbonBand("Diseño", null);
                designBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(designBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(designBand.getControlPanel())));

                if (seccionPlantilla.getIsActiv() == true) {
                    designBand.addCommandButton(buttons.get(Utilerias.ICONS.TEMPLATE.getCaption()), TOP);
                }

                if (seccionModulo.getIsActiv() == true) {
                    designBand.addCommandButton(buttons.get(Utilerias.ICONS.MODULE.getCaption()), TOP);
                }

                bands.add(designBand);
            }

            Seccion seccionPDF = getSeccion(childs, MENU_NAMES.VerPDF.getId());
            Seccion seccionHTML = getSeccion(childs, MENU_NAMES.VerHTML.getId());

            if (seccionPDF.getIsActiv() == true
                    || seccionHTML.getIsActiv() == true) {
                // Previsualizar
                JRibbonBand previewBand = new JRibbonBand("Ver", null);
                previewBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(previewBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(previewBand.getControlPanel())));

                if (seccionPDF.getIsActiv() == true) {
                    InstanceContext.getInstance().setActivePDF(seccionPDF.getIsActiv());
                    previewBand.addCommandButton(buttons.get(Utilerias.ICONS.PDF.getCaption()), TOP);
                }

                if (seccionHTML.getIsActiv() == true) {
                    InstanceContext.getInstance().setActiveHTML(seccionHTML.getIsActiv());
                    previewBand.addCommandButton(buttons.get(Utilerias.ICONS.HTML.getCaption()), TOP);
                }

                bands.add(previewBand);
            }

            Seccion seccionEditarDocumento = getSeccion(childs, MENU_NAMES.EditarDocumento.getId());

            if (seccionEditarDocumento.getIsActiv() == true) {
                InstanceContext.getInstance().setActiveEditDocument(seccionEditarDocumento.getIsActiv());
                // Editar
                JRibbonBand editBand = new JRibbonBand("Editar", null);
                editBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(editBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(editBand.getControlPanel())));
                editBand.addCommandButton(buttons.get(Utilerias.ICONS.EDIT_DOCUMENT.getCaption()), TOP);
                bands.add(editBand);
            }

            try {
                RibbonTask task = new RibbonTask("Editar", bands.toArray(new AbstractRibbonBand[bands.size()]));
                ribbon.addTask(task);

            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
    }

    protected void addTaskPublish(Seccion seccionPublicar) {

        if (seccionPublicar.getIsActiv() == true) {
            List<AbstractRibbonBand> bands = new ArrayList<>();

            ArrayOfSeccion childs = seccionPublicar.getChildren();
            Seccion seccionEnviarPublicacion = getSeccion(childs, MENU_NAMES.EnviarPublicacion.getId());
            Seccion seccionEnviarDocAdjunto = getSeccion(childs, MENU_NAMES.EnviarDocAdjunto.getId());

            if (seccionEnviarPublicacion.getIsActiv() == true
                    || seccionEnviarDocAdjunto.getIsActiv() == true) {
                InstanceContext.getInstance().setActivePublish(seccionEnviarPublicacion.getIsActiv());
                // Enviar
                JRibbonBand publichBand = new JRibbonBand("Documentos", null);
                publichBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(publichBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(publichBand.getControlPanel())
                ));

                if (seccionEnviarPublicacion.getIsActiv() == true) {
                    publichBand.addCommandButton(buttons.get(Utilerias.ICONS.SEND.getCaption()), TOP);
                }

                if (seccionEnviarDocAdjunto.getIsActiv() == true) {
                    publichBand.addCommandButton(buttons.get(Utilerias.ICONS.SEND_EXTERNAL_DOCUMENT.getCaption()), TOP);
                }

                bands.add(publichBand);
            }

            Seccion seccionEnviarTwitter = getSeccion(childs, MENU_NAMES.EnviarTwitter.getId());

            if (seccionEnviarTwitter.getIsActiv() == true) {
                // Social
                JRibbonBand socialBand = new JRibbonBand("Medios", null);
                socialBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(socialBand.getControlPanel())/*,
                        new IconRibbonBandResizePolicy(socialBand.getControlPanel())*/
                ));
                socialBand.addCommandButton(buttons.get(Utilerias.ICONS.SEND_TWEETER.getCaption()), TOP);
                bands.add(socialBand);
            }

            try {
                RibbonTask task = new RibbonTask("Publicar", bands.toArray(new AbstractRibbonBand[bands.size()]));
                ribbon.addTask(task);
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
    }

    protected void addTaskFind(Seccion seccionBuscar) {

        if (seccionBuscar.getIsActiv() == true) {

            List<AbstractRibbonBand> bands = new ArrayList<>();

            ArrayOfSeccion childs = seccionBuscar.getChildren();
            Seccion seccionBuscarDoc = getSeccion(childs, MENU_NAMES.BuscarDoc.getId());
            //Seccion seccionBusquedaAvanzadaDoc = getSeccion(childs, MENU_NAMES.BusquedaAvanzadaDoc.getId());

            if (seccionBuscarDoc.getIsActiv() == true /*|| seccionBusquedaAvanzadaDoc.getIsActiv() == true*/) {

                // Enviar
                JRibbonBand findBand = new JRibbonBand("Buscar", null);
                findBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(findBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(findBand.getControlPanel())));

                if (seccionBuscarDoc.getIsActiv() == true) {
                    findBand.addCommandButton(buttons.get(Utilerias.ICONS.SEARCH_DOCUMENT.getCaption()), TOP);
                }

//                if (seccionBusquedaAvanzadaDoc.getIsActiv() == true) {
//                    findBand.addCommandButton(buttons.get(Utilerias.ICONS.ADVANCED_SEARCH.getCaption()), TOP);
//                }
                bands.add(findBand);
            }

            try {
                RibbonTask task = new RibbonTask("Buscar", bands.toArray(new AbstractRibbonBand[bands.size()]));
                ribbon.addTask(task);
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
    }

    protected void addTaskCollaborative(Seccion seccionColaborar) {

        if (seccionColaborar.getIsActiv() == true) {

            ArrayOfSeccion childs = seccionColaborar.getChildren();

            Seccion seccionAgregarDocColaborativo = getSeccion(childs, MENU_NAMES.AgregarDocColaborativo.getId());
            Seccion seccionNuevoModuloColaborativo = getSeccion(childs, MENU_NAMES.NuevoModuloColaborativo.getId());
            Seccion seccionIntegradorDocumentos = getSeccion(childs, MENU_NAMES.IntegradorDocumentos.getId());
            Seccion seccionIntegradoModulosColaborativos = getSeccion(childs, MENU_NAMES.IntegradorModulosColaborativos.getId());

            List<AbstractRibbonBand> bands = new ArrayList<>();

            if (seccionAgregarDocColaborativo.getIsActiv() == true
                    || seccionNuevoModuloColaborativo.getIsActiv() == true) {
                // Enviar
                JRibbonBand collaborativeBand = new JRibbonBand("Colaborar", null);
                collaborativeBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(collaborativeBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(collaborativeBand.getControlPanel())));

                if (seccionAgregarDocColaborativo.getIsActiv() == true) {
                    collaborativeBand.addCommandButton(buttons.get(Utilerias.ICONS.BOOKMARK_DOCUMENT.getCaption()), TOP);
                }

                if (seccionNuevoModuloColaborativo.getIsActiv() == true) {
                    collaborativeBand.addCommandButton(buttons.get(Utilerias.ICONS.BOOKMARK_MODULE.getCaption()), TOP);
                }

                bands.add(collaborativeBand);
            }

            if (seccionIntegradorDocumentos.getIsActiv() == true
                    || seccionIntegradoModulosColaborativos.getIsActiv() == true) {
                JRibbonBand integrateBand = new JRibbonBand("Administrar", null);
                integrateBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(integrateBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(integrateBand.getControlPanel())));
                if (seccionIntegradorDocumentos.getIsActiv() == true) {
                    integrateBand.addCommandButton(buttons.get(Utilerias.ICONS.INTEGRATE.getCaption()), TOP);
                }

                if (seccionIntegradoModulosColaborativos.getIsActiv() == true) {
                    integrateBand.addCommandButton(buttons.get(Utilerias.ICONS.INTEGRATE_MOD.getCaption()), TOP);
                }
                bands.add(integrateBand);
            }

            try {
                RibbonTask task = new RibbonTask("Colaborar", bands.toArray(new AbstractRibbonBand[bands.size()]));
                ribbon.addTask(task);
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
    }

    protected void addTaskAdmin(Seccion seccionAdmin) {
        if (seccionAdmin.getIsActiv() == true) {

            List<AbstractRibbonBand> bands = new ArrayList<>();

            ArrayOfSeccion childs = seccionAdmin.getChildren();

            Seccion seccionAdminTipoDoc = getSeccion(childs, MENU_NAMES.AdminTipoDoc.getId());
            Seccion seccionAdminModulos = getSeccion(childs, MENU_NAMES.AdminModulos.getId());
            Seccion seccionAdminPlantillas = getSeccion(childs, MENU_NAMES.AdminPlantillas.getId());
            Seccion seccionAdminDisclousure = getSeccion(childs, MENU_NAMES.AdminDisclousure.getId());
            Seccion seccionAdminUsuario = getSeccion(childs, MENU_NAMES.AdminUsuario.getId());
            Seccion seccionAdminPerfiles = getSeccion(childs, MENU_NAMES.AdminPerfiles.getId());
            Seccion seccionAdminCatalogos = getSeccion(childs, MENU_NAMES.AdminCatalogos.getId());
            Seccion seccionAdminOficinas = getSeccion(childs, MENU_NAMES.AdminOficinas.getId());
            Seccion seccionAdminExcel = getSeccion(childs, MENU_NAMES.AdminExcel.getId());
            Seccion seccionAdminReproceso = getSeccion(childs, MENU_NAMES.AdminReproceso.getId());

            if (seccionAdminTipoDoc.getIsActiv() == true
                    || seccionAdminModulos.getIsActiv() == true
                    || seccionAdminPlantillas.getIsActiv() == true
                    || seccionAdminDisclousure.getIsActiv() == true
                    || seccionAdminUsuario.getIsActiv() == true
                    || seccionAdminPerfiles.getIsActiv() == true
                    || seccionAdminCatalogos.getIsActiv() == true
                    || seccionAdminOficinas.getIsActiv() == true
                    || seccionAdminExcel.getIsActiv() == true
                    || seccionAdminReproceso.getIsActiv() == true) {

                // Administracion
                JRibbonBand adminBand = new JRibbonBand("Administrar", null);
                adminBand.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                        new CoreRibbonResizePolicies.None(adminBand.getControlPanel()),
                        new IconRibbonBandResizePolicy(adminBand.getControlPanel())));

                if (seccionAdminTipoDoc.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_DOCUMENT_TYPE.getCaption()), TOP);
                }

                if (seccionAdminModulos.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_MODULE.getCaption()), TOP);
                }

                if (seccionAdminPlantillas.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_TEMPLATE.getCaption()), TOP);
                }

                if (seccionAdminDisclousure.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_DISCLOSURE.getCaption()), TOP);
                }

                if (seccionAdminUsuario.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_USERS.getCaption()), TOP);
                }

                if (seccionAdminPerfiles.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_PROFILE.getCaption()), TOP);
                }

                if (seccionAdminCatalogos.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_CATALOG.getCaption()), TOP);
                }

                if (seccionAdminExcel.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_EXCEL.getCaption()), TOP);
                }

                if (seccionAdminOficinas.getIsActiv() == true) {
                    adminBand.addCommandButton(buttons.get(Utilerias.ICONS.ADMIN_OFFICE.getCaption()), TOP);
                }

                bands.add(adminBand);
                
                if(true){
                    // Interfaz
                    JRibbonBand adminProgress = new JRibbonBand("Interfaz", null);
                    adminProgress.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                            new CoreRibbonResizePolicies.None(adminProgress.getControlPanel()),
                            new IconRibbonBandResizePolicy(adminProgress.getControlPanel())));

                    adminProgress.addCommandButton(buttons.get(Utilerias.ICONS.INTERFAZ_REPROCESO.getCaption()), TOP);

                    bands.add(adminProgress);
                }
                
                try {
                    RibbonTask task = new RibbonTask("Administración", bands.toArray(new AbstractRibbonBand[bands.size()]));
                    ribbon.addTask(task);
                } catch (Exception e) {
                    Utilerias.logger(getClass()).error(e);
                }
            }
        }
    }

    protected void addApplicationMenu(Seccion seccionPrincipal) {

        RibbonApplicationMenu menu = new RibbonApplicationMenu();

        /* Ir a Casa */
        RibbonApplicationMenuEntryPrimary goHome = new RibbonApplicationMenuEntryPrimary(
                Utilerias.getIcon(Utilerias.ICONS.HOME), Utilerias.ICONS.HOME.getCaption(), new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CustomClickAction(Utilerias.ICONS.HOME.getCaption());
                    }
                }, JCommandButton.CommandButtonKind.ACTION_ONLY);
        menu.addMenuEntry(goHome);

        if (seccionPrincipal.getIsActiv() == true) {
            try {

                ArrayOfSeccion childs = seccionPrincipal.getChildren();

                Seccion seccionNuevo = getSeccion(childs, MENU_NAMES.NuevoDoc.getId());
                Seccion seccionIntegradoModulosColaborativos = getSeccion(childs, MENU_NAMES.NuevoModuloColaborativo.getId());
                Seccion seccionAbrir = getSeccion(childs, MENU_NAMES.AbrirDoc.getId());
                Seccion seccionGuardar = getSeccion(childs, MENU_NAMES.GuardarDoc.getId());
                Seccion seccionGuardaComo = getSeccion(childs, MENU_NAMES.GuardarComoDoc.getId());

                if (seccionNuevo.getIsActiv() == true
                        || seccionAbrir.getIsActiv() == true
                        || seccionGuardar.getIsActiv() == true
                        || seccionGuardaComo.getIsActiv() == true) {

                    if (seccionNuevo.getIsActiv() == true) {
                        /* Nuevo */
                        RibbonApplicationMenuEntryPrimary nuevo
                                = new RibbonApplicationMenuEntryPrimary(
                                        Utilerias.getIcon(Utilerias.ICONS.NEW), Utilerias.ICONS.NEW.getCaption(), new ActionListener() {

                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                CustomClickAction(Utilerias.ICONS.NEW.getCaption());
                                            }
                                        }, JCommandButton.CommandButtonKind.ACTION_ONLY);
                        WindowActions.getInstance().addAction(Utilerias.ICONS.NEW.getCaption(), KeyEvent.VK_N, null);
                        menu.addMenuEntry(nuevo);
                    }

                    //TODO agregar validación para saber si el el item menu se agrega desde WebService.
                    /* Nuevo modulo colaborativo */
                    RibbonApplicationMenuEntryPrimary nvoModCol
                            = new RibbonApplicationMenuEntryPrimary(
                                    Utilerias.getIcon(Utilerias.ICONS.BOOKMARK_MODULE), Utilerias.ICONS.BOOKMARK_MODULE.getCaption(), new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            CustomClickAction(Utilerias.ICONS.BOOKMARK_MODULE.getCaption());
                                        }
                                    }, JCommandButton.CommandButtonKind.ACTION_ONLY);
                    WindowActions.getInstance().addAction(Utilerias.ICONS.BOOKMARK_MODULE.getCaption(), KeyEvent.VK_N, null);
                    menu.addMenuEntry(nvoModCol);
                    ////////////////////////////////////////////

                    if (seccionAbrir.getIsActiv() == true) {
                        /* Abrir (Busquedas recientes) */
                        primary1 = new RibbonApplicationMenuEntryPrimary(
                                Utilerias.getIcon(Utilerias.ICONS.OPEN), Utilerias.ICONS.OPEN.getCaption(), new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        CustomClickAction(Utilerias.ICONS.OPEN.getCaption());
                                    }
                                }, JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP);
                        WindowActions.getInstance().addAction(Utilerias.ICONS.OPEN.getCaption(), KeyEvent.VK_O, null);
                        documentosRecientes(primary1);
                        menu.addMenuEntry(primary1);
                    }

                    if (seccionGuardar.getIsActiv() == true) {
                        /* Guardar */
                        RibbonApplicationMenuEntryPrimary guardar = new RibbonApplicationMenuEntryPrimary(
                                Utilerias.getIcon(Utilerias.ICONS.SAVE), Utilerias.ICONS.SAVE.getCaption(), new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        CustomClickAction(Utilerias.ICONS.SAVE.getCaption());
                                    }
                                }, JCommandButton.CommandButtonKind.ACTION_ONLY);
                        WindowActions.getInstance().addAction(Utilerias.ICONS.SAVE.getCaption(), KeyEvent.VK_S, null);
                        menu.addMenuEntry(guardar);
                    }

                    if (seccionGuardaComo.getIsActiv() == true) {
                        /* Guardar como */
                        RibbonApplicationMenuEntryPrimary guardarComo = new RibbonApplicationMenuEntryPrimary(
                                Utilerias.getIcon(Utilerias.ICONS.SAVE_AS), Utilerias.ICONS.SAVE_AS.getCaption(), new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        CustomClickAction(Utilerias.ICONS.SAVE_AS.getCaption());
                                    }
                                }, JCommandButton.CommandButtonKind.ACTION_ONLY);
                        menu.addMenuEntry(guardarComo);
                    }
                }

            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }

        /* Cerrar sesion */
        RibbonApplicationMenuEntryPrimary cerrarSesion = new RibbonApplicationMenuEntryPrimary(
                Utilerias.getIcon(Utilerias.ICONS.SESSION_CLOSE), Utilerias.ICONS.SESSION_CLOSE.getCaption(), new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CustomClickAction(Utilerias.ICONS.SESSION_CLOSE.getCaption());
                    }
                }, JCommandButton.CommandButtonKind.ACTION_ONLY);
        menu.addMenuEntry(cerrarSesion);

        /* Salir */
        RibbonApplicationMenuEntryPrimary salir = new RibbonApplicationMenuEntryPrimary(
                Utilerias.getIcon(Utilerias.ICONS.EXIT), Utilerias.ICONS.EXIT.getCaption(), new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CustomClickAction(Utilerias.ICONS.EXIT.getCaption());
                    }
                }, JCommandButton.CommandButtonKind.ACTION_ONLY);
        menu.addMenuEntry(salir);

        ribbon.setApplicationMenu(menu);

    }

    protected void addShorcutBar(Seccion seccionPrincipal, Seccion seccionPublicar) {
        /* Shorcut ir a casa */
        JCommandButton btnHome = new JCommandButton(Utilerias.ICONS.SMALL_HOME.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_HOME));
        btnHome.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CustomClickAction(Utilerias.ICONS.SMALL_HOME.getCaption());
            }
        });
        ribbon.addTaskbarComponent(btnHome);

        /* Shortcuts de nuevo, abrir, guardar, (undo y redo) no presentes en esta versión*/
        if (seccionPrincipal.getIsActiv() == true) {
            try {

                ArrayOfSeccion childs = seccionPrincipal.getChildren();

                Seccion seccionNuevo = getSeccion(childs, MENU_NAMES.NuevoDoc.getId());
                Seccion seccionAbrir = getSeccion(childs, MENU_NAMES.AbrirDoc.getId());
                Seccion seccionGuardar = getSeccion(childs, MENU_NAMES.GuardarDoc.getId());
                Seccion seccionGuardaComo = getSeccion(childs, MENU_NAMES.GuardarComoDoc.getId());

                if (seccionNuevo.getIsActiv() == true
                        || seccionAbrir.getIsActiv() == true
                        || seccionGuardar.getIsActiv() == true
                        || seccionGuardaComo.getIsActiv() == true) {

                    if (seccionNuevo.getIsActiv() == true) {
                        JCommandButton btnNuevo = new JCommandButton(
                                Utilerias.ICONS.SMALL_NEW.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_NEW));
                        btnNuevo.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CustomClickAction(Utilerias.ICONS.SMALL_NEW.getCaption());
                            }
                        });
                        ribbon.addTaskbarComponent(btnNuevo);
                    }

                    if (seccionAbrir.getIsActiv() == true) {
                        JCommandButton btnAbrir = new JCommandButton(
                                Utilerias.ICONS.SMALL_OPEN.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_OPEN));
                        btnAbrir.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CustomClickAction(Utilerias.ICONS.SMALL_OPEN.getCaption());
                            }
                        });
                        ribbon.addTaskbarComponent(btnAbrir);
                    }

                    if (seccionGuardar.getIsActiv() == true) {
                        JCommandButton btnGuardar = new JCommandButton(
                                Utilerias.ICONS.SMALL_SAVE.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_SAVE));
                        btnGuardar.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CustomClickAction(Utilerias.ICONS.SMALL_SAVE.getCaption());
                            }
                        });
                        ribbon.addTaskbarComponent(btnGuardar);
                    }

                    if (seccionGuardaComo.getIsActiv() == true) {
                        JCommandButton btnSaveAs = new JCommandButton(
                                Utilerias.ICONS.SMALL_SAVE_AS.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_SAVE_AS));
                        btnSaveAs.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CustomClickAction(Utilerias.ICONS.SMALL_SAVE_AS.getCaption());
                            }
                        });
                        ribbon.addTaskbarComponent(btnSaveAs);
                    }
                }
            } catch (Exception ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }

        if (seccionPublicar.getIsActiv() == true) {
            ArrayOfSeccion childs = seccionPublicar.getChildren();
            Seccion seccionTwitter = getSeccion(childs, MENU_NAMES.EnviarTwitter.getId());
            if (seccionTwitter.getIsActiv() == true) {
                try {
                    JCommandButton btnTwitt = new JCommandButton(Utilerias.ICONS.SMALL_TWITT.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_TWITT));
                    btnTwitt.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            CustomClickAction(Utilerias.ICONS.SMALL_TWITT.getCaption());
                        }
                    });
                    ribbon.addTaskbarComponent(btnTwitt);
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            }
        }

        JCommandButton btnNotify = new JCommandButton(Utilerias.ICONS.SMALL_NOTIFY.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_NOTIFY));
        btnNotify.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CustomClickAction(Utilerias.ICONS.SMALL_NOTIFY.getCaption());
            }
        });
        ribbon.addTaskbarComponent(btnNotify);

        if (seccionPublicar.getIsActiv() == true) {
            ArrayOfSeccion childs = seccionPublicar.getChildren();
            Seccion seccion = getSeccion(childs, MENU_NAMES.EnviarPublicacion.getId());
            if (seccion.getIsActiv() == true) {
                try {
                    JCommandButton btnSend = new JCommandButton(Utilerias.ICONS.SMALL_SEND.getCaption(), Utilerias.getIcon(Utilerias.ICONS.SMALL_SEND));
                    btnSend.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CustomClickAction(Utilerias.ICONS.SMALL_SEND.getCaption());
                        }
                    });
                    ribbon.addTaskbarComponent(btnSend);
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            }
        }
    }

    public void CustomClickAction(String actionName) {
        CustomAction action = new CustomAction();
        action.executeAction(actionName, actionBinding);
    }

    private void documentosRecientes(RibbonApplicationMenuEntryPrimary primary1) {
        try {
            List<DocumentSearchBO> list = null;
            QuickViewDAO dao = new QuickViewDAO();
            list = dao.get(QuickViewDAO.SEARCH_TYPE.RECIENTES);

            List<RibbonApplicationMenuEntrySecondary> recientes = new ArrayList<>();
            for (DocumentSearchBO docSeBO : list) {
                String key = String.valueOf(docSeBO.getDocumentId());
                RibbonApplicationMenuEntrySecondary secondary
                        = new RibbonApplicationMenuEntrySecondary(Utilerias.getIcon(Utilerias.ICONS.SMALL_NEW_DOCUMENT), docSeBO.getSubjectName() + ":" + docSeBO.getDocumentName(), new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                org.pushingpixels.flamingo.api.common.JCommandMenuButton source = (org.pushingpixels.flamingo.api.common.JCommandMenuButton) e.getSource();
                                if (source != null) {
                                    try {
                                        int idDocument_ = Integer.parseInt(source.getActionKeyTip());
                                        String nombre_ = source.getText();
                                        //TODO modificar para nueva version
                                        MainView.main.setDocument(idDocument_, nombre_);
                                    } catch (NumberFormatException ex) {
                                        Utilerias.logger(getClass()).info(ex);
                                    }
                                }
                            }
                        }, JCommandButton.CommandButtonKind.ACTION_ONLY
                        );
                secondary.setActionKeyTip(key);
                recientes.add(secondary);
            }
            primary1.addSecondaryMenuGroup("Documentos recientes", recientes.toArray(new RibbonApplicationMenuEntrySecondary[recientes.size()]));

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public void refrescarRecientes() {
        documentosRecientes(primary1);
    }

    public static class Menu {

        public enum MENU {

            EDITAR("Editar"),
            PUBLICAR("Publicar"),
            BUSCAR("Buscar"),
            COLABORAR("Colaborar"),
            ADMINISTRACION("Administración");

            final String name;

            MENU(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        public static void setMenu(MENU menu) {
            int taskCount = ribbon.getTaskCount();
            if (taskCount > 0) {
                for (int i = 0; i < taskCount; i++) {
                    RibbonTask currentRibbonTask = ribbon.getTask(i);
                    String ribbonName = currentRibbonTask.getTitle();
                    if (menu.toString().equalsIgnoreCase(ribbonName)) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                ribbon.setSelectedTask(currentRibbonTask);
                            }
                        });
                        break;
                    }
                }
            }
        }
    }

}

class WindowActions {

    private static WindowActions instance;
    protected final List<WindowActions.Action> actions;

    private WindowActions() {
        actions = new ArrayList<>();
    }

    public static synchronized WindowActions getInstance() {
        if (instance == null) {
            instance = new WindowActions();
        }
        return instance;
    }

    public void addAction(String name, Integer mnemonic, EventListener listener) {
        Action a = new Action(name, mnemonic, listener);
        actions.add(a);
    }

    public Action findAction(int keyCode) {
        for (Action a : actions) {
            if (a != null && a.mnemonic != null && a.mnemonic == keyCode) {
                return a;
            }
        }
        return null;
    }

    public List<WindowActions.Action> getActions() {
        return actions;
    }

    static class Action {

        protected String name;
        protected Integer mnemonic;
        protected EventListener listener;

        public Action() {

        }

        public Action(String name, Integer mnemonic, EventListener listener) {
            this.name = name;
            this.mnemonic = mnemonic;
            this.listener = listener;
        }

    }
}
