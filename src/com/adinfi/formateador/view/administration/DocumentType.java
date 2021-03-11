package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.DisclosureNameBO;
import com.adinfi.formateador.bos.DocumentPublishProfileBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.OutgoingEmailBO;
import com.adinfi.formateador.bos.PublishProfileBO;
import com.adinfi.formateador.bos.ReturnSelectedTemplates;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.bos.TipoContratoBO;
import com.adinfi.formateador.bos.seguridad.Perfil;
import com.adinfi.formateador.dao.DisclosureNameDAO;
import com.adinfi.formateador.dao.DocumentPublishProfileDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.OutgoingEmailDAO;
import com.adinfi.formateador.dao.PublishProfileDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.DocumentTypeModel;
import com.adinfi.formateador.view.resources.CCheckBox;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfPerfil;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.analisisws.publicador.ArrayOfTipoContrato;
import com.adinfi.ws.analisisws.publicador.IPublicador_Stub;
import com.adinfi.ws.analisisws.publicador.Publicador_Impl;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Guillermo Trejo, Josue Sanchez, Carlos Félix
 *
 */
public class DocumentType extends javax.swing.JDialog {

    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    private final String DELETE_MESSAGE = "¿Desea eliminar?";
    private ReturnSelectedTemplates selectedTemplates = null;
    private List<Integer> templates = new ArrayList<>();
    private List<String> contracts = new ArrayList<>();
    private List<String> profiles = new ArrayList<>();
    private String titleReg = null;
    private String regex = null;
    private boolean nuevo = false;

    /**
     * Creates new form DocumentType
     *
     * @param parent
     * @param modal
     */
    public DocumentType(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initComboPerfiles();
        initComboContrato();
        initComboBoxMarketSearch();
        initComboBoxOutgoingEmail();
        initComboBoxDisclosure();
        initComboBoxMarket();
        initComboPublishProfile();
        txtNomenclatura.setDocument(new JTextFieldLimit(3));
        chkSubtitle.setVisible(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    private void confirmExit() {
        String msj = "Si cierra la ventana sin grabar se perderán los datos, ¿desea continuar?";
        int r = JOptionPane.showConfirmDialog(this, msj, "Confirmacion", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            this.setVisible(false);
        }
    }

    @SuppressWarnings("unchecked")

    /*documentTypeTable*/
    private void addDocumentTypeModel() {
        DocumentTypeModel model = documentTypeTable.getModel() instanceof DefaultTableModel
                ? new DocumentTypeModel()
                : (DocumentTypeModel) documentTypeTable.getModel();

        try {
            documentTypeTable.addMouseListener(documentTypeTableMouseListener());
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            documentTypeTable.setDefaultRenderer(Object.class, stcr);
            fitColumns(documentTypeTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

        documentTypeTable.setModel(model);

    }

    private void resetTableSelected() {
        DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            model.setValueAt(false, i, 0);
        }
    }

    private MouseAdapter documentTypeTableMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                //int row = languageTable.rowAtPoint(e.getPoint());
                int column = documentTypeTable.columnAtPoint(e.getPoint());
                if (column == StatementConstant.SC_0.get()) {
                    // Habilitar e inhabiliar botones de eliminar guardar
                    DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();
                    List<Integer> selectedRows = new ArrayList<>();
                    int length = model.getData() != null ? model.getData().length : 0;
                    for (int i = 0; i < length; i++) {
                        if (model.isCheckedRow(i) == true) {
                            selectedRows.add(model.getUniqueRowIdentify(i));
                        }
                    }
                    if (selectedRows.isEmpty() == true) {
                        /*Cuando no se han seleccionado elementos en el grid*/
                        btnSaveDocumentType.setText(SAVE_LABEL);
                        btnSaveDocumentType.setEnabled(false);
                        btnNewDocumentType.setText(NEW_LABEL);
                        btnNewDocumentType.setEnabled(true);
                        btnDeleteDocumentType.setEnabled(false);
                        btnTitleDocumentType.setEnabled(false);
                    } else if (selectedRows.size() == 1) {
                        btnNewDocumentType.setText(CANCEL_LABEL);
                        btnNewDocumentType.setEnabled(true);
                        btnSaveDocumentType.setEnabled(true);
                        btnSaveDocumentType.setText(EDIT_LABEL);
                        btnDeleteDocumentType.setEnabled(true);
                        //btnTitleDocumentType.setEnabled(true);
                    } else if (selectedRows.size() > 1) {
                        btnNewDocumentType.setText(CANCEL_LABEL);
                        btnNewDocumentType.setEnabled(true);
                        btnSaveDocumentType.setEnabled(false);
                        btnSaveDocumentType.setText(EDIT_LABEL);
                        btnDeleteDocumentType.setEnabled(true);
                        btnTitleDocumentType.setEnabled(false);
                    }
                }
            }
        };
    }

    private void setDocumentTypeModel(List<DocumentTypeBO> list) {
        DocumentTypeModel documentTypeModel = new DocumentTypeModel(list);
        documentTypeTable.setModel(documentTypeModel);
        fitColumns(documentTypeTable);

        try {
            documentTypeTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (!nuevo) {
                        int row = documentTypeTable.rowAtPoint(evt.getPoint());
                        int col = documentTypeTable.columnAtPoint(evt.getPoint());
                        if (row >= 0 && col >= 0) {
                            setCurrent(row, col);
                            setEnabledFields(false);
                            btnNewDocumentType.setText(NEW_LABEL);
                            if (checkedRowVal() == 0) {
                                btnSaveDocumentType.setEnabled(false);
                            }
                        }
                    }
                }
            });
            documentTypeTable.addMouseListener(documentTypeTableMouseListener());
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private int checkedRowVal() {
        int num = 0;
        DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {

                num = model.getUniqueRowIdentify(i);
                break;
            }
        }
        return num;
    }

    private String checkedRowValIdMiVector() {
        String num = "";
        DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {

                num = model.getIdMiVector(i);
                break;
            }
        }
        return num;
    }

    private String checkedRowValIdDocuementType_Vector() {
        String num = "";
        DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {

                num = model.getIdDocumentTypeVector(i);
                break;
            }
        }
        return num;
    }

    private void deleteDocumentTypeModel() {
        DocumentTypeModel model = documentTypeTable.getModel() instanceof DefaultTableModel
                ? new DocumentTypeModel()
                : (DocumentTypeModel) documentTypeTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    DocumentTypeDAO dao = new DocumentTypeDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        Object obj0 = listMarketSearch.getSelectedItem();
                        MarketBO marketb = (MarketBO) obj0;
                        int idms = Integer.parseInt(marketb.getIdMiVector_real());
                        findDocumentType(inputDocumentTypeSearch.getText(), idms);
                        cleanFields();
                        setEnabledFields(false);
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

    }

    private void setCurrent(int row, int col) {

        DocumentTypeModel model = (DocumentTypeModel) documentTypeTable.getModel();

        Object[][] data = model.getData();

        /*Seleccionar el nombre y devolverlo al campo txtNombre*/
        txtNombre.setText(String.valueOf(data[row][StatementConstant.SC_2.get()]));

        /*Seleccionar el mercado y devolverlo al combo listMercado*/
        String mercado = (String) data[row][StatementConstant.SC_3.get()];
        /*Seleccionar el item en el combo mercado*/
        int n = listMercado.getItemCount();
        for (int i = 0; i < n; i++) {
            MarketBO obj = (MarketBO) listMercado.getItemAt(i);
            if (obj.getName().equals(mercado)) {
                listMercado.setSelectedIndex(i);
                break;
            }
        }

        /* Seleccionar la nomenclatura y devolverla a txt */
        txtNomenclatura.setText(String.valueOf(data[row][StatementConstant.SC_4.get()]));

        /*Setear ID Mi Vector*/
        String result = String.valueOf(data[row][StatementConstant.SC_18.get()]);
        txtIdMiVector.setText(result);

        /*Seleccionar el mercado y devolverlo al combo listOutgoingEmail*/
        String email = (String) data[row][StatementConstant.SC_5.get()];
        /*Seleccionar el item en el combo listOutgoingEmail*/
        int n2 = listOutgoingEmail.getItemCount();
        for (int i2 = 0; i2 < n2; i2++) {
            OutgoingEmailBO obj2 = (OutgoingEmailBO) listOutgoingEmail.getItemAt(i2);
            if (obj2.getEmail().equals(email)) {
                listOutgoingEmail.setSelectedIndex(i2);
                break;
            }
        }

        /*Seleccionar el perfil de publicación y devolverlo al combo listPublishProfile*/
        int perfil = (int) data[row][StatementConstant.SC_7.get()];
        /*Seleccionar el item en el combo listPublishProfile*/
//        int n3 = listPublishProfile.getItemCount();
//        for (int i3 = 0; i3 < n3; i3++) {
//            PublishProfileBO obj3 = (PublishProfileBO) listPublishProfile.getItemAt(i3);
//            if (obj3.getIdPublishProfile() == perfil) {
//                listPublishProfile.setSelectedIndex(i3);
//                break;
//            }
//        }

        /*Seleccionar el perfil de publicación y devolverlo al combo listDisclosure*/
        int disclosure = (int) data[row][StatementConstant.SC_8.get()];
        /*Seleccionar el item en el combo listPublishProfile*/
        int n4 = listDisclosure.getItemCount();
        for (int i4 = 0; i4 < n4; i4++) {
            DisclosureNameBO obj4 = (DisclosureNameBO) listDisclosure.getItemAt(i4);
            if (obj4.getIdDisclosure_name() == disclosure) {
                listDisclosure.setSelectedIndex(i4);
                break;
            }
        }

        chkSend.setSelected((boolean) data[row][StatementConstant.SC_9.get()]);
        chkPublish.setSelected((boolean) data[row][StatementConstant.SC_10.get()]);
        chkCollaborative.setSelected((boolean) data[row][StatementConstant.SC_11.get()]);
        chkSendEmail.setSelected((boolean) data[row][StatementConstant.SC_12.get()]);
        chkSubject.setSelected((boolean) data[row][StatementConstant.SC_13.get()]);
        chkTitle.setSelected((boolean) data[row][StatementConstant.SC_14.get()]);
        chkSubtitle.setSelected((boolean) data[row][StatementConstant.SC_15.get()]);
        chkHTML.setSelected((boolean) data[row][StatementConstant.SC_16.get()]);
        chkPDF.setSelected((boolean) data[row][StatementConstant.SC_17.get()]);
        regex = (String) data[row][StatementConstant.SC_20.get()];

        int documentTypeId = (int) data[row][StatementConstant.SC_1.get()];
        DocumentTypeDAO dao = new DocumentTypeDAO();
        setTemplateSelected(dao, documentTypeId);
        setProfileSelected(dao, documentTypeId);
        setContractSelected(dao, documentTypeId);
        selectComboPublishProfile(documentTypeId);
    }
    private List<Integer> templateLst = new ArrayList<>();

    private void setTemplateSelected(DocumentTypeDAO dao, int documentTypeId) {
        templateLst = dao.getTemplateByDocumentTypeId(documentTypeId);
        TemplateDAO tempDAO = new TemplateDAO();
        final List<TemplateBO> lstTempBO = tempDAO.getTemplates();
        if (lstTempBO == null) {
            return;
        }
        StringBuilder text = new StringBuilder();
        lstTempBO.stream().filter((tempBO) -> (templateLst.contains(tempBO.getTemplateId()))).map((tempBO) -> {
            text.append(tempBO.getTemplateName());
            return tempBO;
        }).forEach((_item) -> {
            text.append(",");
        });
        txtTemplates.setText(text.toString());
    }

    private void setProfileSelected(DocumentTypeDAO dao, int documentTypeId) {
        List<Integer> profileLst = dao.getProfileByDocumentTypeId(documentTypeId);
        initComboPerfiles(profileLst);
    }

    private void setContractSelected(DocumentTypeDAO dao, int documentTypeId) {
        List<String> contractLst = dao.getContractByDocumentTypeId(documentTypeId);
        initComboContrato(contractLst);
    }

    private void findDocumentType(String pattern, int pattern2) {
        try {
            DocumentTypeDAO dAO = new DocumentTypeDAO();
            List<DocumentTypeBO> list = dAO.get(pattern, pattern2, 0);
            setDocumentTypeModel(list);
            cleanFields();
            btnSaveDocumentType.setText(SAVE_LABEL);
            btnSaveDocumentType.setEnabled(false);
            btnNewDocumentType.setText(NEW_LABEL);
            btnNewDocumentType.setEnabled(true);
            btnDeleteDocumentType.setEnabled(false);
            btnTitleDocumentType.setEnabled(false);
            if (list == null || list.isEmpty()) {
                JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), "No se encontraron coincidencias para esta búsqueda", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void fitColumns(JTable table) {
        /* Ajustar columnas de la tabla */
        Utilerias.adjustJTableRowSizes(table);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Utilerias.adjustColumnSizes(table, i, 4);
        }
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_6.get() - StatementConstant.SC_1.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_7.get() - StatementConstant.SC_2.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_8.get() - StatementConstant.SC_3.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_9.get() - StatementConstant.SC_4.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_10.get() - StatementConstant.SC_5.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_11.get() - StatementConstant.SC_6.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_12.get() - StatementConstant.SC_7.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_13.get() - StatementConstant.SC_8.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_14.get() - StatementConstant.SC_9.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_15.get() - StatementConstant.SC_10.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_16.get() - StatementConstant.SC_11.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_17.get() - StatementConstant.SC_12.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_18.get() - StatementConstant.SC_13.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_19.get() - StatementConstant.SC_14.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_20.get() - StatementConstant.SC_15.get()));
    }

    private void initComboContrato() {
        initComboContrato(new ArrayList<>());
    }

    private void initComboContrato(List<String> contractLst) {
        List<TipoContratoBO> list = new ArrayList<>();
        try {
            IPublicador_Stub stub = (IPublicador_Stub) new Publicador_Impl().getBasicHttpBinding_IPublicador();
            UtileriasWS.setEndpoint(stub);
            ArrayOfTipoContrato arrayOfTipoContrato = stub.tiposContratos();
            if (arrayOfTipoContrato != null && arrayOfTipoContrato.getTipoContrato() != null) {
                for (com.adinfi.ws.analisisws.publicador.TipoContrato o : arrayOfTipoContrato.getTipoContrato()) {
                    TipoContratoBO tc = new TipoContratoBO(o.getNombre(), o.getTipoContratoId(), o.getEsActivo());
                    list.add(tc);
                }
                cboContrato.removeAllItems();
                List<String> auxLst = new ArrayList<>();
                for (TipoContratoBO o : list) {
                    for (String contract : contractLst) {
                        if (contract.compareTo(o.getTipoContratoId()) == 0) {
                            auxLst.add(o.toString());
                            break;
                        }
                    }
                    cboContrato.addItem(new JCheckBox(o.toString()));
                }
                for (int i = 0; i < cboContrato.getItemCount(); i++) {
                    JCheckBox check = (JCheckBox) cboContrato.getItemAt(i);
                    boolean bSelected = false;
                    if (auxLst.contains(check.getText())) {
                        bSelected = true;
                    }
                    check.setSelected(bSelected);
                }
            }
            /*cboContrato.addItemListener(new ItemListener() {
             @Override
             public void itemStateChanged(ItemEvent e) {
             cboContratoItemStateChanged(e);
             }
             });*/
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "El servicio de contratos no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void initComboBoxMarketSearch() {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo_ = new MarketBO();
        bo_.setName("Seleccione");
        bo_.setIdMarket(-1);
        bo_.setIdMiVector_real("-1");
        listMarketSearch.addItem(bo_);
        list.stream().forEach((bo) -> {
            listMarketSearch.addItem(bo);
        });
    }

    private void initComboBoxMarket() {
        MarketDAO dao = new MarketDAO();
        List<MarketBO> list = dao.get(null);
        MarketBO bo_ = new MarketBO();
        bo_.setName("Seleccione");
        bo_.setIdMarket(-1);
        bo_.setIdMiVector_real("-1");
        listMercado.addItem(bo_);
        list.stream().forEach((bo) -> {
            listMercado.addItem(bo);
        });
    }

    private void initComboBoxOutgoingEmail() {
        /*Asignar Valores por Default*/
        OutgoingEmailBO b = new OutgoingEmailBO();
        b.setEmail("Seleccione");
        b.setIdOutgoingEmail(-1);
        listOutgoingEmail.addItem(b);

        OutgoingEmailDAO dao = new OutgoingEmailDAO();
        List<OutgoingEmailBO> list = dao.get(null);

        list.stream().forEach((bo) -> {
            listOutgoingEmail.addItem(bo);
        });

    }

    private void initComboBoxDisclosure() {
        DisclosureNameBO b = new DisclosureNameBO();
        b.setName("Seleccione");
        b.setIndentifier("-1");
        listDisclosure.addItem(b);

        DisclosureNameDAO dao = new DisclosureNameDAO();
        List<DisclosureNameBO> list = dao.get();

        list.stream().forEach((bo) -> {
            listDisclosure.addItem(bo);
        });
    }

    private void initComboPublishProfile() {

        listPublishProfile.removeAllItems();
        //PublishProfileBO b = new PublishProfileBO();
//        b.setIdPublishProfile(-1);
//        b.setName("Seleccione");
//        listPublishProfile.addItem(b);

        PublishProfileDAO dao = new PublishProfileDAO();
        List<PublishProfileBO> list = dao.get();

        list.stream().forEach((bo) -> {
            // listPublishProfile.addItem(bo);

            listPublishProfile.addItem(new CCheckBox(bo.getName(), bo));
        });

        //UnselectAll 
        for (int i = 0; i < listPublishProfile.getItemCount(); i++) {
            CCheckBox cb = (CCheckBox) listPublishProfile.getItemAt(i);
            cb.setSelected(false);
        }

    }

    private void selectComboPublishProfile(int idDocumentType) {

        DocumentPublishProfileDAO daoPP = new DocumentPublishProfileDAO();
        List<DocumentPublishProfileBO> listPP = daoPP.get(idDocumentType);

        listPP.stream().forEach((ls) -> {
            for (int i = 0; i < listPublishProfile.getItemCount(); i++) {
                CCheckBox cb = (CCheckBox) listPublishProfile.getItemAt(i);
                PublishProfileBO bo = (PublishProfileBO) cb.getObject();
                if (bo.getIdPublishProfile() == ls.getIdpublishprofile()) {
                    cb.setSelected(true);
                }
            }
        });

    }

    private void initComboPerfiles() {
        initComboPerfiles(new ArrayList<>());
    }

    private void initComboPerfiles(List<Integer> profileLst) {
        List<com.adinfi.formateador.bos.seguridad.Perfil> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfPerfil array = stub.perfiles(GlobalDefines.WS_INSTANCE);
            if (array != null && array.getPerfil() != null) {
                for (com.adinfi.ws.Perfil o : array.getPerfil()) {
                    com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    list.add(p);
                }
                cboProfile.removeAllItems();
                List<String> auxLst = new ArrayList<>();
                for (Perfil o : list) {
                    for (Integer profileId : profileLst) {
                        if (profileId == o.getPerfilId()) {
                            auxLst.add(o.toString());
                            break;
                        }
                    }
                    cboProfile.addItem(new JCheckBox(o.toString()));
                }
                for (int i = 0; i < cboProfile.getItemCount(); i++) {
                    JCheckBox check = (JCheckBox) cboProfile.getItemAt(i);
                    boolean bSelected = false;
                    if (auxLst.contains(check.getText())) {
                        bSelected = true;
                    }
                    check.setSelected(bSelected);
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de perfiles no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setEnabledFields(boolean b) {
        txtNombre.setEnabled(b);
        listMercado.setEnabled(b);
        txtNomenclatura.setEnabled(b);
        listOutgoingEmail.setEnabled(b);
//      txtIdMiVector.setEnabled(b);
//      txtTemplates.setEnabled(b);
        btnTemplates.setEnabled(b);
        cboProfile.setEnabled(b);
        cboContrato.setEnabled(b);
        listPublishProfile.setEnabled(b);
        listDisclosure.setEnabled(b);
        chkSend.setEnabled(b);
        chkPublish.setEnabled(b);
        chkCollaborative.setEnabled(b);
        chkSendEmail.setEnabled(b);
        chkSubject.setEnabled(b);
        chkTitle.setEnabled(b);
        chkSubtitle.setEnabled(b);
        chkHTML.setEnabled(b);
        chkPDF.setEnabled(b);
    }

    private void cleanFields() {

        listMercado.setSelectedIndex(-1);
        txtNombre.setText(null);
        txtNomenclatura.setText(null);
        listOutgoingEmail.setSelectedIndex(-1);
        txtIdMiVector.setText(null);
        txtTemplates.setText(null);
        templateLst = new ArrayList<>();
        profiles = new ArrayList<>();
        contracts = new ArrayList<>();
        initComboPerfiles();
        initComboContrato();
        initComboPublishProfile();
        listDisclosure.setSelectedIndex(-1);
        chkSend.setSelected(false);
        chkPublish.setSelected(false);
        chkCollaborative.setSelected(false);
        chkSendEmail.setSelected(true);
        chkSubject.setSelected(false);
        chkTitle.setSelected(false);
        chkSubtitle.setSelected(false);
        chkHTML.setSelected(true);
        chkPDF.setSelected(true);
    }
    
    private boolean isUpdate = false;

    private void insertUpdate() {
        switch (btnSaveDocumentType.getText()) {
            case SAVE_LABEL:
                DocumentTypeBO bo = new DocumentTypeBO();
                boolean required = true;
                //idTipo de documento bd local
                bo.setIddocument_type(checkedRowVal());
                // IdMivector e IdDocumentTypeVector para update
                bo.setIdMiVector(checkedRowValIdMiVector());
                bo.setIddocument_type_vector(checkedRowValIdDocuementType_Vector());
                StringBuilder fields = new StringBuilder();
                if (txtNombre.getText().isEmpty()) {
                    required = false;
                    fields.append("Tipo de Documento");
                    fields.append("\n");
                } else {
                    bo.setName(txtNombre.getText());
                }
                Object obj1 = listMercado.getSelectedItem();
                if (obj1 != null) {
                    MarketBO marketbo = (MarketBO) obj1;
                    int idm = Integer.valueOf(marketbo.getIdMiVector_real());
                    if (idm == -1) {
                        required = false;
                        fields.append("Mercado");
                        fields.append("\n");
                    } else {
                        bo.setIdMarket(idm);
                    }
                } else {
                    required = false;
                    fields.append("Mercado");
                    fields.append("\n");
                }
                if (txtNomenclatura.getText().isEmpty()) {
                    required = false;
                    fields.append("Nomenclatura");
                    fields.append("\n");
                } else {
                    bo.setNomenclature(txtNomenclatura.getText());
                }
                Object obj0 = listMarketSearch.getSelectedItem();
                if (obj0 != null) {
                    MarketBO marketb = (MarketBO) obj0;
                    int idms = Integer.parseInt(marketb.getIdMiVector_real());
                    bo.setMarketIDSearch(idms);
                }
                Object obj2 = listOutgoingEmail.getSelectedItem();
                if (obj2 != null) {
                    OutgoingEmailBO out = (OutgoingEmailBO) obj2;
                    int ido = out.getIdOutgoingEmail();
                    if (ido == -1) {
                        required = false;
                        fields.append("Correo saliente");
                        fields.append("\n");
                    } else {
                        bo.setIdOutgoingEmail(ido);
                        bo.setEmail(out.getEmail());
                    }
                } else {
                    required = false;
                    fields.append("Correo saliente");
                    fields.append("\n");
                }
                Object obj3 = listDisclosure.getSelectedItem();
                if (obj3 != null) {
                    DisclosureNameBO ds = (DisclosureNameBO) obj3;
                    int idds = ds.getIdDisclosure_name();
                    bo.setIdDisclosure(idds);
                }
                profiles = new ArrayList<>();
                for (int i = 0; i < cboProfile.getItemCount(); i++) {
                    JCheckBox check = (JCheckBox) cboProfile.getItemAt(i);
                    if (check.isSelected()) {
                        if (!profiles.contains(check.getText())) {
                            profiles.add(check.getText());
                        }
                    } else {
                        profiles.remove(check.getText());
                    }
                }
                if (profiles.size() > 0) {
                    IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                    UtileriasWS.setEndpoint(stub);
                    ArrayOfPerfil array = null;
                    try {
                        array = stub.perfiles(GlobalDefines.WS_INSTANCE);
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null, "El servicio de perfiles no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(DocumentType.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (array != null && array.getPerfil() != null) {
                        for (com.adinfi.ws.Perfil o : array.getPerfil()) {
                            profiles.stream().filter((check) -> (o.getNombre().compareTo(check) == 0)).forEach((_item) -> {
                                bo.getProfiles().add(o.getPerfilId());
                            });
                        }
                    }
                } else {
                    required = false;
                    fields.append("Perfiles");
                    fields.append("\n");
                }
                contracts = new ArrayList<>();
                for (int i = 0; i < cboContrato.getItemCount(); i++) {
                    JCheckBox check = (JCheckBox) cboContrato.getItemAt(i);
                    if (check.isSelected()) {
                        if (!contracts.contains(check.getText())) {
                            contracts.add(check.getText());
                        }
                    } else {
                        contracts.remove(check.getText());
                    }
                }
                if (contracts.size() > 0) {
                    IPublicador_Stub stub = (IPublicador_Stub) new Publicador_Impl().getBasicHttpBinding_IPublicador();
                    UtileriasWS.setEndpoint(stub);
                    ArrayOfTipoContrato arrayOfTipoContrato = null;
                    try {
                        arrayOfTipoContrato = stub.tiposContratos();
                    } catch (RemoteException ex) {
                        Logger.getLogger(DocumentType.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (arrayOfTipoContrato != null && arrayOfTipoContrato.getTipoContrato() != null) {
                        for (com.adinfi.ws.analisisws.publicador.TipoContrato o : arrayOfTipoContrato.getTipoContrato()) {
                            contracts.stream().filter((check) -> (o.getNombre().compareTo(check) == 0)).forEach((_item) -> {
                                bo.getContracts().add(o.getTipoContratoId());
                            });
                        }
                    }
                }   // Obtener Perfiles de publicacion.
                List<Integer> pp = new ArrayList<>();
                for (int i = 0; i < listPublishProfile.getItemCount(); i++) {
                    CCheckBox cb = (CCheckBox) listPublishProfile.getItemAt(i);
                    if (cb.isSelected()) {
                        PublishProfileBO boP = (PublishProfileBO) cb.getObject();
                        pp.add(boP.getIdPublishProfile());
                        bo.setIdPublishProfile(boP.getIdPublishProfile());
                    }
                }
                if (!pp.isEmpty()) {
                    bo.setProfilesPublish(pp);
                } else {
                    required = false;
                    fields.append("Perfiles de publicación");
                    fields.append("\n");
                }
                bo.setSendMedia(chkSend.isSelected());
                bo.setPdf(chkPDF.isSelected());
                bo.setPublish(chkPublish.isSelected());
                bo.setSendEmail(chkSendEmail.isSelected());
                bo.setHtml(chkHTML.isSelected());
                bo.setCollaborative(chkCollaborative.isSelected());
                bo.setSubTitle(chkSubtitle.isSelected());
                bo.setTitle(chkTitle.isSelected());
                bo.setSubject(chkSubject.isSelected());
                if (titleReg != null) {
                    bo.setTitle_regex(titleReg);
                    bo.setTitle_regex_decoded(decodeTitleRegEx(titleReg));
                }   //bo.setProfiles(profiles);
                //bo.setContracts(contracts);
                bo.setTemplates(templates);
                bo.setSelectedTemplates(selectedTemplates);
                //Object [] objs =cboProfile.getSelectedObjects();
                DocumentTypeDAO dao = new DocumentTypeDAO();
                // revisar el tamaño del titulo del tipo de documento y limitarlo a 40 caracteres.
                if (bo.getName() != null) {
                    if (bo.getName().length() > 40) {
                        fields.append("\nNombre del tipo de documento debe ser menor igual a 40 caracteres");
                        required = false;
                    }
                }
                if (required == true) {

                    if (!isUpdate
                            && Utilerias.isRepeated(Utilerias.TablesValueNames.DOCUMENT_TYPE.name(),
                                    Utilerias.TablesValueNames.DOCUMENT_TYPE.getCaption(), txtNombre.getText(), "IDDOCUMENT_TYPE", bo.getIddocument_type())) {
                        JOptionPane.showMessageDialog(null, "El nombre del Tipo de Documento " + txtNombre.getText() + " ya existe, ingrese uno diferente.");
                        return;
                    }

                    int id = dao.insertUpdate(bo);
                    if ((id > 0) && (!dao.getIsIDMiVector().contains("idMiVector"))) {
                        btnNewDocumentType.setText(NEW_LABEL);
                        btnSaveDocumentType.setEnabled(false);
                        cleanFields();
                        setEnabledFields(false);
                        Object obj00 = listMarketSearch.getSelectedItem();
                        MarketBO marketb = (MarketBO) obj00;
                        executeSearch();
                        isUpdate = false;
                        JOptionPane.showMessageDialog(this, "Se guardó Satisfactoriamente");
                    } else if (id > 0 && dao.getIsIDMiVector().contains("idMiVector")) {

                        //Borrar el registro debido a que queda inusable sin el idMiVector
                        DocumentTypeDAO daoType = new DocumentTypeDAO();
                        daoType.deleteHard(id);
                        JOptionPane.showMessageDialog(this, "No fue posible registrar este tipo de documento, debido a que existía anteriormente.");
                        findDocumentType(null, 0);

                    } else {
                        JOptionPane.showMessageDialog(this, dao.getException().getMessage() + "<- ");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Campos requeridos en blanco, favor de llenar la informacion:* \n" + fields.toString());
                }
                break;
            case EDIT_LABEL:
                isUpdate = true;
                setEnabledFields(true);
                btnSaveDocumentType.setText(SAVE_LABEL);
                btnNewDocumentType.setText(CANCEL_LABEL);
                btnTitleDocumentType.setEnabled(true);
                break;
        }
    }

    private String decodeTitleRegEx(String titleReg) {

        // code here 
        return titleReg;
    }

    private void executeSearch() {
        Object obj00 = listMarketSearch.getSelectedItem();
        int idms = -1;
        MarketBO marketb = (MarketBO) obj00;
        if (marketb.getIdMiVector_real() != null) {
            idms = Integer.valueOf(marketb.getIdMiVector_real());
        }
        findDocumentType(inputDocumentTypeSearch.getText(), idms);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtIdMiVector = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNomenclatura = new javax.swing.JTextField();
        cboProfile = new JComboCheckBox();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cboContrato = new JComboCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        listMercado = new javax.swing.JComboBox();
        txtTemplates = new javax.swing.JTextField();
        btnTemplates = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        listPublishProfile = new JComboCheckBox();
        Busqueda = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        listMarketSearch = new javax.swing.JComboBox();
        inputDocumentTypeSearch = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        listOutgoingEmail = new javax.swing.JComboBox();
        listDisclosure = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        chkSend = new javax.swing.JCheckBox();
        chkPublish = new javax.swing.JCheckBox();
        chkCollaborative = new javax.swing.JCheckBox();
        chkSendEmail = new javax.swing.JCheckBox();
        chkSubject = new javax.swing.JCheckBox();
        chkTitle = new javax.swing.JCheckBox();
        chkSubtitle = new javax.swing.JCheckBox();
        chkHTML = new javax.swing.JCheckBox();
        chkPDF = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        documentTypeTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        btnTitleDocumentType = new javax.swing.JButton();
        btnNewDocumentType = new javax.swing.JButton();
        btnSaveDocumentType = new javax.swing.JButton();
        btnDeleteDocumentType = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DocumentType.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        txtIdMiVector.setText(resourceMap.getString("txtIdMiVector.text")); // NOI18N
        txtIdMiVector.setEnabled(false);
        txtIdMiVector.setName("txtIdMiVector"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtNomenclatura.setText(resourceMap.getString("txtNomenclatura.text")); // NOI18N
        txtNomenclatura.setEnabled(false);
        txtNomenclatura.setName("txtNomenclatura"); // NOI18N
        txtNomenclatura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomenclaturaKeyReleased(evt);
            }
        });

        cboProfile.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        cboProfile.setEnabled(false);
        cboProfile.setName("cboProfile"); // NOI18N

        txtNombre.setText(resourceMap.getString("txtNombre.text")); // NOI18N
        txtNombre.setEnabled(false);
        txtNombre.setName("txtNombre"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        cboContrato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        cboContrato.setEnabled(false);
        cboContrato.setName("cboContrato"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        listMercado.setEnabled(false);
        listMercado.setName("listMercado"); // NOI18N

        txtTemplates.setEditable(false);
        txtTemplates.setText(resourceMap.getString("txtTemplates.text")); // NOI18N
        txtTemplates.setEnabled(false);
        txtTemplates.setName("txtTemplates"); // NOI18N

        btnTemplates.setText(resourceMap.getString("btnTemplates.text")); // NOI18N
        btnTemplates.setEnabled(false);
        btnTemplates.setName("btnTemplates"); // NOI18N
        btnTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplatesActionPerformed(evt);
            }
        });

        jLabel10.setFont(resourceMap.getFont("jLabel10.font")); // NOI18N
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        listPublishProfile.setFont(resourceMap.getFont("listPublishProfile.font")); // NOI18N
        listPublishProfile.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        listPublishProfile.setName("listPublishProfile"); // NOI18N
        listPublishProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listPublishProfileActionPerformed(evt);
            }
        });

        Busqueda.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("Búsqueda.border.title"))); // NOI18N
        Busqueda.setName("Búsqueda"); // NOI18N

        jLabel11.setFont(resourceMap.getFont("jLabel11.font")); // NOI18N
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setFont(resourceMap.getFont("jLabel12.font")); // NOI18N
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        listMarketSearch.setFont(resourceMap.getFont("listMarketSearch.font")); // NOI18N
        listMarketSearch.setName("listMarketSearch"); // NOI18N
        listMarketSearch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listMarketSearchItemStateChanged(evt);
            }
        });
        listMarketSearch.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                listMarketSearchComponentShown(evt);
            }
        });
        listMarketSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listMarketSearchActionPerformed(evt);
            }
        });

        inputDocumentTypeSearch.setFont(resourceMap.getFont("inputDocumentTypeSearch.font")); // NOI18N
        inputDocumentTypeSearch.setName("inputDocumentTypeSearch"); // NOI18N
        inputDocumentTypeSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputDocumentTypeSearchKeyReleased(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jButton10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton10KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout BusquedaLayout = new javax.swing.GroupLayout(Busqueda);
        Busqueda.setLayout(BusquedaLayout);
        BusquedaLayout.setHorizontalGroup(
            BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputDocumentTypeSearch)
                    .addComponent(listMarketSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BusquedaLayout.setVerticalGroup(
            BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BusquedaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BusquedaLayout.createSequentialGroup()
                        .addComponent(inputDocumentTypeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listMarketSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addComponent(jLabel11))
                .addContainerGap())
        );

        listOutgoingEmail.setEnabled(false);
        listOutgoingEmail.setName("listOutgoingEmail"); // NOI18N
        listOutgoingEmail.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listOutgoingEmailItemStateChanged(evt);
            }
        });
        listOutgoingEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOutgoingEmailMouseClicked(evt);
            }
        });
        listOutgoingEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listOutgoingEmailActionPerformed(evt);
            }
        });

        listDisclosure.setFont(resourceMap.getFont("listDisclosure.font")); // NOI18N
        listDisclosure.setEnabled(false);
        listDisclosure.setName("listDisclosure"); // NOI18N

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        chkSend.setText(resourceMap.getString("chkSend.text")); // NOI18N
        chkSend.setEnabled(false);
        chkSend.setName("chkSend"); // NOI18N

        chkPublish.setText(resourceMap.getString("chkPublish.text")); // NOI18N
        chkPublish.setEnabled(false);
        chkPublish.setName("chkPublish"); // NOI18N

        chkCollaborative.setText(resourceMap.getString("chkCollaborative.text")); // NOI18N
        chkCollaborative.setEnabled(false);
        chkCollaborative.setName("chkCollaborative"); // NOI18N

        chkSendEmail.setFont(resourceMap.getFont("chkSendEmail.font")); // NOI18N
        chkSendEmail.setText(resourceMap.getString("chkSendEmail.text")); // NOI18N
        chkSendEmail.setEnabled(false);
        chkSendEmail.setName("chkSendEmail"); // NOI18N

        chkSubject.setFont(resourceMap.getFont("chkSubject.font")); // NOI18N
        chkSubject.setText(resourceMap.getString("chkSubject.text")); // NOI18N
        chkSubject.setEnabled(false);
        chkSubject.setName("chkSubject"); // NOI18N
        chkSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSubjectActionPerformed(evt);
            }
        });

        chkTitle.setFont(resourceMap.getFont("chkTitle.font")); // NOI18N
        chkTitle.setText(resourceMap.getString("chkTitle.text")); // NOI18N
        chkTitle.setEnabled(false);
        chkTitle.setName("chkTitle"); // NOI18N
        chkTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTitleActionPerformed(evt);
            }
        });

        chkSubtitle.setFont(resourceMap.getFont("chkSubtitle.font")); // NOI18N
        chkSubtitle.setText(resourceMap.getString("chkSubtitle.text")); // NOI18N
        chkSubtitle.setEnabled(false);
        chkSubtitle.setName("chkSubtitle"); // NOI18N

        chkHTML.setText(resourceMap.getString("chkHTML.text")); // NOI18N
        chkHTML.setEnabled(false);
        chkHTML.setName("chkHTML"); // NOI18N

        chkPDF.setText(resourceMap.getString("chkPDF.text")); // NOI18N
        chkPDF.setEnabled(false);
        chkPDF.setName("chkPDF"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        documentTypeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "Nombre*", "Mercado*", "Nomenclatura*", "Correo Saliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        documentTypeTable.setName("documentTypeTable"); // NOI18N
        jScrollPane2.setViewportView(documentTypeTable);
        if (documentTypeTable.getColumnModel().getColumnCount() > 0) {
            documentTypeTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("documentTypeTable.columnModel.title0")); // NOI18N
            documentTypeTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("documentTypeTable.columnModel.title1")); // NOI18N
            documentTypeTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("documentTypeTable.columnModel.title2")); // NOI18N
            documentTypeTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("documentTypeTable.columnModel.title3")); // NOI18N
            documentTypeTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("documentTypeTable.columnModel.title4")); // NOI18N
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Busqueda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(listDisclosure, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNombre)
                            .addComponent(listMercado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNomenclatura)
                            .addComponent(listOutgoingEmail, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtIdMiVector)
                            .addComponent(cboProfile, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboContrato, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtTemplates, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTemplates))
                            .addComponent(listPublishProfile, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(chkSend))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkPDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chkPublish)
                                            .addComponent(chkSendEmail)
                                            .addComponent(chkSubject)
                                            .addComponent(chkTitle)
                                            .addComponent(chkSubtitle)
                                            .addComponent(chkHTML)
                                            .addComponent(chkCollaborative))
                                        .addGap(0, 0, Short.MAX_VALUE))))))))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(chkSend, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listMercado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(chkPublish))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomenclatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(chkSendEmail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listOutgoingEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(chkSubject))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdMiVector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(chkTitle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTemplates)
                            .addComponent(jLabel3)
                            .addComponent(chkCollaborative))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(chkHTML))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(chkPDF))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listPublishProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(chkSubtitle))
                        .addGap(8, 8, 8)
                        .addComponent(listDisclosure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
        );

        Busqueda.getAccessibleContext().setAccessibleName(resourceMap.getString("Búsqueda.AccessibleContext.accessibleName")); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N

        btnTitleDocumentType.setFont(resourceMap.getFont("btnTitleDocumentType.font")); // NOI18N
        btnTitleDocumentType.setText(resourceMap.getString("btnTitleDocumentType.text")); // NOI18N
        btnTitleDocumentType.setEnabled(false);
        btnTitleDocumentType.setName("btnTitleDocumentType"); // NOI18N
        btnTitleDocumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTitleDocumentTypeActionPerformed(evt);
            }
        });

        btnNewDocumentType.setText(NEW_LABEL);
        btnNewDocumentType.setName("btnNewDocumentType"); // NOI18N
        btnNewDocumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewDocumentTypeActionPerformed(evt);
            }
        });

        btnSaveDocumentType.setText(resourceMap.getString("btnSaveDocumentType.text")); // NOI18N
        btnSaveDocumentType.setEnabled(false);
        btnSaveDocumentType.setName("btnSaveDocumentType"); // NOI18N
        btnSaveDocumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDocumentTypeActionPerformed(evt);
            }
        });

        btnDeleteDocumentType.setFont(resourceMap.getFont("btnDeleteDocumentType.font")); // NOI18N
        btnDeleteDocumentType.setText(resourceMap.getString("btnDeleteDocumentType.text")); // NOI18N
        btnDeleteDocumentType.setEnabled(false);
        btnDeleteDocumentType.setName("btnDeleteDocumentType"); // NOI18N
        btnDeleteDocumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDocumentTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(274, 274, 274)
                .addComponent(btnNewDocumentType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSaveDocumentType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeleteDocumentType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTitleDocumentType)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteDocumentType)
                    .addComponent(btnSaveDocumentType)
                    .addComponent(btnTitleDocumentType)
                    .addComponent(btnNewDocumentType))
                .addContainerGap())
        );
        jLayeredPane1.setLayer(btnTitleDocumentType, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnNewDocumentType, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnSaveDocumentType, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnDeleteDocumentType, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 754, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jLayeredPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplatesActionPerformed
        DocumentTypeDAO dAO = new DocumentTypeDAO();
        List<DocumentTypeBO> typesList = dAO.get(null, -1, 0);
        SelectTemplate plantilla = new SelectTemplate(null, true, templateLst, typesList);
        Utilerias.centreDialog(plantilla, true);
        plantilla.setVisible(true);
        templates = new ArrayList<>();
        selectedTemplates = plantilla.getSelectedTemplates();
        if (selectedTemplates != null) {
            txtTemplates.setText(selectedTemplates.toString());
            selectedTemplates.getItems().stream().forEach((item) -> {
                templates.add(item.getId());
            });
        }
    }//GEN-LAST:event_btnTemplatesActionPerformed

    private void btnTitleDocumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTitleDocumentTypeActionPerformed
        DocumentTypeTitle title = new DocumentTypeTitle(null, true);
        Utilerias.centreDialog(title, true);
        if (regex != null) {
            title.setRegEx(regex);
        }
        title.setVisible(true);

        titleReg = title.returRegEx();
    }//GEN-LAST:event_btnTitleDocumentTypeActionPerformed

    private void chkSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSubjectActionPerformed

    }//GEN-LAST:event_chkSubjectActionPerformed

    private void chkTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTitleActionPerformed

    }//GEN-LAST:event_chkTitleActionPerformed

    private void btnNewDocumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewDocumentTypeActionPerformed
        isUpdate = false;
        if (btnNewDocumentType.getText().equals(NEW_LABEL)) {
            setEnabledFields(true);
            cleanFields();
            Object obj00 = listMarketSearch.getSelectedItem();
            btnNewDocumentType.setText(CANCEL_LABEL);
            btnSaveDocumentType.setEnabled(true);
            btnSaveDocumentType.setText(SAVE_LABEL);
            btnDeleteDocumentType.setEnabled(false);
            /*Boton de titulo*/
            btnTitleDocumentType.setEnabled(true);
            txtNombre.requestFocusInWindow();
            nuevo = true;
        } else {
            nuevo = false;
            btnNewDocumentType.setText(NEW_LABEL);
            btnSaveDocumentType.setEnabled(false);
            cleanFields();
            resetTableSelected();
            /*Boton de Titulo */
            btnDeleteDocumentType.setEnabled(false);
            btnTitleDocumentType.setEnabled(false);
            setEnabledFields(false);
        }
    }//GEN-LAST:event_btnNewDocumentTypeActionPerformed

    private void listMarketSearchComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_listMarketSearchComponentShown
    }//GEN-LAST:event_listMarketSearchComponentShown

    private void listMarketSearchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listMarketSearchItemStateChanged

    }//GEN-LAST:event_listMarketSearchItemStateChanged

    private void listMarketSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listMarketSearchActionPerformed

    }//GEN-LAST:event_listMarketSearchActionPerformed

    private void listOutgoingEmailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOutgoingEmailMouseClicked

    }//GEN-LAST:event_listOutgoingEmailMouseClicked

    private void listOutgoingEmailItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listOutgoingEmailItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_listOutgoingEmailItemStateChanged

    private void listOutgoingEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listOutgoingEmailActionPerformed

    }//GEN-LAST:event_listOutgoingEmailActionPerformed

    private void btnSaveDocumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDocumentTypeActionPerformed
        insertUpdate();
    }//GEN-LAST:event_btnSaveDocumentTypeActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        addDocumentTypeModel();
        Object obj00 = listMarketSearch.getSelectedItem();
        MarketBO marketb = (MarketBO) obj00;
        int idms = Integer.parseInt(marketb.getIdMiVector_real());
        findDocumentType(null, idms);
    }//GEN-LAST:event_formComponentShown

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        executeSearch();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnDeleteDocumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDocumentTypeActionPerformed
        deleteDocumentTypeModel();
    }//GEN-LAST:event_btnDeleteDocumentTypeActionPerformed

    private void jButton10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton10KeyReleased

    }//GEN-LAST:event_jButton10KeyReleased

    private void inputDocumentTypeSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputDocumentTypeSearchKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Object obj00 = listMarketSearch.getSelectedItem();
            MarketBO marketb = (MarketBO) obj00;
            int idms = Integer.parseInt(marketb.getIdMiVector_real());
            findDocumentType(inputDocumentTypeSearch.getText(), idms);
        }
    }//GEN-LAST:event_inputDocumentTypeSearchKeyReleased

    private void listPublishProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPublishProfileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listPublishProfileActionPerformed

    private void txtNomenclaturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomenclaturaKeyReleased
        String c = txtNomenclatura.getText();
        String _regex = "[^A-Za-z0-9]+";
        if (Pattern.compile(_regex).matcher(c).find()) {
            if (txtNomenclatura.getText().length() == 1) {
                txtNomenclatura.setText("");
            } else {
                txtNomenclatura.setText(txtNomenclatura.getText().substring(0, txtNomenclatura.getText().length() - 1));
            }
            JOptionPane.showMessageDialog(this, "Nomenclatura: No debe contener caracteres especiales ni espacios");
        }
    }//GEN-LAST:event_txtNomenclaturaKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Busqueda;
    private javax.swing.JButton btnDeleteDocumentType;
    private javax.swing.JButton btnNewDocumentType;
    private javax.swing.JButton btnSaveDocumentType;
    private javax.swing.JButton btnTemplates;
    private javax.swing.JButton btnTitleDocumentType;
    private javax.swing.JComboBox cboContrato;
    private javax.swing.JComboBox cboProfile;
    private javax.swing.JCheckBox chkCollaborative;
    private javax.swing.JCheckBox chkHTML;
    private javax.swing.JCheckBox chkPDF;
    private javax.swing.JCheckBox chkPublish;
    private javax.swing.JCheckBox chkSend;
    private javax.swing.JCheckBox chkSendEmail;
    private javax.swing.JCheckBox chkSubject;
    private javax.swing.JCheckBox chkSubtitle;
    private javax.swing.JCheckBox chkTitle;
    private javax.swing.JTable documentTypeTable;
    private javax.swing.JTextField inputDocumentTypeSearch;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox listDisclosure;
    private javax.swing.JComboBox listMarketSearch;
    private javax.swing.JComboBox listMercado;
    private javax.swing.JComboBox listOutgoingEmail;
    private javax.swing.JComboBox listPublishProfile;
    private javax.swing.JTextField txtIdMiVector;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNomenclatura;
    private javax.swing.JTextField txtTemplates;
    // End of variables declaration//GEN-END:variables

    class JTextFieldLimit extends PlainDocument {

        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        JTextFieldLimit(int limit, boolean upper) {
            super();
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
