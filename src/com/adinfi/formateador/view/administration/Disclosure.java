package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.DisclosureBO;
import com.adinfi.formateador.bos.LinkedExcelBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.DisclosureDAO;
import com.adinfi.formateador.dao.ExcelDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.DlgObjects;
import com.adinfi.formateador.view.ScrDocument;
import com.adinfi.formateador.view.ScrObjExcel;
import com.adinfi.formateador.view.dnd.ScrCell;
import com.adinfi.formateador.view.dnd.ScrObjIcon;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfUsuario;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Usuario;
import com.hexidec.ekit.EkitCore;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

/**
 *
 * @author Carlos Félix
 */
public class Disclosure extends javax.swing.JDialog {

    private IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
    private DlgObjects dlgObject1 = new DlgObjects(true);
    private DlgObjects dlgObject2 = new DlgObjects(true);
    private DlgObjects dlgObject3 = new DlgObjects(true);
    private DlgObjects dlgObject4 = new DlgObjects(true);
    private DlgObjects dlgObject5 = new DlgObjects(true);
    private DlgObjects dlgObject6 = new DlgObjects(true);
    private DlgObjects dlgObject7 = new DlgObjects(true);
    private DlgObjects dlgObject8 = new DlgObjects(true);

    private ModuleSectionBO secInfoExcel_1 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_2 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_3 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_4 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_5 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_6 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_7 = new ModuleSectionBO();
    private ModuleSectionBO secInfoExcel_8 = new ModuleSectionBO();

    private boolean rentaFija = false;
    private boolean fundSinEmisora = false;
    private boolean fundConEmisora = false;
    private boolean economico = false;
    private boolean semanario = false;

    private DisclosureDAO dao = null;
    private ExcelDAO excelDAO = null;

    /**
     * Creates new form Disclosure
     */
    public Disclosure(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        UtileriasWS.setEndpoint(stub);
        dao = new DisclosureDAO();
        excelDAO = new ExcelDAO();
        secInfoExcel_1.setLstObjects(new ArrayList<>());
        secInfoExcel_2.setLstObjects(new ArrayList<>());
        secInfoExcel_3.setLstObjects(new ArrayList<>());
        secInfoExcel_4.setLstObjects(new ArrayList<>());
        secInfoExcel_5.setLstObjects(new ArrayList<>());
        secInfoExcel_6.setLstObjects(new ArrayList<>());
        secInfoExcel_7.setLstObjects(new ArrayList<>());
        secInfoExcel_8.setLstObjects(new ArrayList<>());
        initComponents();
        initScreen();
    }

    private void initScreen() {
        try {
            setTitle("Disclosure");
            setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/disclosure.png")).getImage());
            initKitCoreElements();
            printBDValues();
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void printBDValues() {
        DisclosureBO bo = dao.getDisclosure(DisclosureDAO.TYPES.RENTA_FIJA);
        rentaFija(bo);
        bo = dao.getDisclosure(DisclosureDAO.TYPES.F_SIN_EMISORA);
        fundSinEmisora(bo);
        bo = dao.getDisclosure(DisclosureDAO.TYPES.F_CON_EMISORA);
        fundConEmisora(bo);
        bo = dao.getDisclosure(DisclosureDAO.TYPES.SEMANARIO);
        semanario(bo);
        bo = dao.getDisclosure(DisclosureDAO.TYPES.ECONOMICO);
        economico(bo);
    }

    private void rentaFija(DisclosureBO bo) {
        if (bo.getType() > 0) {
            rentaFija = true;
        }
        initGenerico(bo, (EkitCore) jText2_4.getComponent(1), txtTX3, (EkitCore) jText2_5.getComponent(1), null);
    }

    private void fundSinEmisora(DisclosureBO bo) {
        if (bo.getType() > 0) {
            fundSinEmisora = true;
        }
        initGenerico(bo, (EkitCore) jText2_8.getComponent(1), txtTX4, (EkitCore) jText2_7.getComponent(1), (EkitCore) jText2_6.getComponent(1));
        initExcel(secInfoExcel_3, jExcel2_3, dlgObject3, bo.getExcelIni());
        initExcel(secInfoExcel_4, jExcel2_4, dlgObject4, bo.getExcelFin());
    }

    protected void initExcel(ModuleSectionBO moduleSectionBO, JPanel jPanel, DlgObjects dlgObjects, DisclosureBO.ExcelBO excelBO) {
        if (excelBO == null) {
            return;
        }
        Utilerias.centreDialog(dlgObjects, true);
        dlgObjects.setTitle(GlobalDefines.TITULO_APP);
        dlgObjects.setObjType(GlobalDefines.OBJ_TYPE_EXCEL);

        ObjectInfoBO objInfo = getObjectInfoBO(excelBO);
        objInfo.setObjType(GlobalDefines.OBJ_TYPE_EXCEL);

        dlgObjects.setObjInfo(objInfo);
        dlgObjects.setModal(true);
        dlgObjects.initScrObject(objInfo, moduleSectionBO, null, Boolean.TRUE);
        dlgObjects.resetScrObject();
        dlgObjects.setSize(new Dimension(900, 700));
        dlgObjects.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        dlgObjects.setLocationRelativeTo(MainApp.getApplication().getMainFrame());

        moduleSectionBO.getLstObjects().add(objInfo);
        try {
            LinkedExcelBO categoriaLinked = excelDAO.getCategoriesById(excelBO.getCategoryId());
            LinkedExcelBO item;
            for (int i = 0; i < dlgObjects.getScrObjExcel().getCboCategoria().getItemCount(); i++) {
                item = (LinkedExcelBO) dlgObjects.getScrObjExcel().getCboCategoria().getItemAt(i);
                if (item.getId_category() == categoriaLinked.getId_category()) {
                    dlgObjects.getScrObjExcel().getCboCategoria().setSelectedItem(item);
                    dlgObjects.getScrObjExcel().getNombreExcel();
                    break;
                }
            }
            LinkedExcelBO nombreLinked = excelDAO.getFromCtegory(excelBO.getCategoryId(), excelBO.getNameId());
            for (int i = 0; i < dlgObjects.getScrObjExcel().getCboName().getItemCount(); i++) {
                item = (LinkedExcelBO) dlgObjects.getScrObjExcel().getCboName().getItemAt(i);
                if (item.getIdLinkedExcel() == nombreLinked.getIdLinkedExcel()) {
                    dlgObjects.getScrObjExcel().getCboName().setSelectedItem(item);
                    dlgObjects.getScrObjExcel().getLinkedExcelInfo();
                    break;
                }
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        refreshObjects(moduleSectionBO, jPanel, dlgObjects);
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

    private DisclosureBO.ExcelBO getExcelBO(JPanel excelPanel, DlgObjects dlgObjects, boolean isDelete) {
        DisclosureBO.ExcelBO excelBO = new DisclosureBO.ExcelBO();
        
        if(isDelete){
            excelBO.setDelete(isDelete);
            return excelBO;
        }
        
        try {
            ScrObjIcon excelIcon = (ScrObjIcon) excelPanel.getComponent(0);
            ObjectInfoBO objectInfo = excelIcon.getObjectInfo();
            excelBO.setTitulo(objectInfo.getTitulo());
            excelBO.setSubTitulo(objectInfo.getSubTitulo());
            excelBO.setDescripcion(objectInfo.getComentarios());
            excelBO.setImage(objectInfo.getImage());

            LinkedExcelBO categoriaLinked = (LinkedExcelBO) dlgObjects.getScrObjExcel().getCboCategoria().getSelectedItem();
            if (categoriaLinked == null) {
                excelBO.setCategoryId(objectInfo.getCategoryId());
            } else {
                excelBO.setCategoryId(categoriaLinked.getId_category());
            }
            LinkedExcelBO nameLinked = (LinkedExcelBO) dlgObjects.getScrObjExcel().getCboName().getSelectedItem();
            if (nameLinked == null) {
                excelBO.setNameId(objectInfo.getNameId());
            } else {
                excelBO.setNameId(nameLinked.getIdLinkedExcel());
            }
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
            excelBO = null;
        }
        return excelBO;
    }

    private void fundConEmisora(DisclosureBO bo) {
        if (bo.getType() > 0) {
            fundConEmisora = true;
        }
        initGenerico(bo, (EkitCore) jText2_1.getComponent(1), txtTX2, (EkitCore) jText2_2.getComponent(1), (EkitCore) jText2_3.getComponent(1));
        initExcel(secInfoExcel_1, jExcel2_1, dlgObject1, bo.getExcelIni());
        initExcel(secInfoExcel_2, jExcel2_2, dlgObject2, bo.getExcelFin());
    }

    private void semanario(DisclosureBO bo) {
        if (bo.getType() > 0) {
            semanario = true;
        }
        initGenerico(bo, (EkitCore) jText2_9.getComponent(1), txtTX5, (EkitCore) jText2_10.getComponent(1), (EkitCore) jText2_11.getComponent(1));
        initExcel(secInfoExcel_6, jExcel2_6, dlgObject6, bo.getExcelIni());
        initExcel(secInfoExcel_5, jExcel2_5, dlgObject5, bo.getExcelFin());
    }

    private void economico(DisclosureBO bo) {
        if (bo.getType() > 0) {
            economico = true;
        }
        initGenerico(bo, (EkitCore) jText2_12.getComponent(1), txtTX6, (EkitCore) jText2_13.getComponent(1), (EkitCore) jText2_14.getComponent(1));
        initExcel(secInfoExcel_8, jExcel2_8, dlgObject8, bo.getExcelIni());
        initExcel(secInfoExcel_7, jExcel2_7, dlgObject7, bo.getExcelFin());
    }

    private void initGenerico(DisclosureBO bo, EkitCore top, TextField expression, EkitCore preview, EkitCore bottom) {
        top.getTextPane().setText(bo.getTextTop());
        if (bo.getRegularExpression() != null && !bo.getRegularExpression().isEmpty()) {
            expression.setText(bo.getRegularExpression());
        }
        preview.getTextPane().setText(bo.getPreview());
        if (bottom != null) {
            bottom.getTextPane().setText(bo.getTextBottom());
        }
    }

    private void initKitCoreElements() {
        getEkitCoreInstance(jText2_1, 760, 250, true);
        getEkitCoreInstance(jText2_2, 500, 100, false);
        getEkitCoreInstance(jText2_3, 760, 100, false);

        getEkitCoreInstance(jText2_4, 760, 250, true);
        getEkitCoreInstance(jText2_5, 500, 100, false);

        getEkitCoreInstance(jText2_8, 760, 250, true);
        getEkitCoreInstance(jText2_7, 500, 100, false);
        getEkitCoreInstance(jText2_6, 760, 100, false);

        getEkitCoreInstance(jText2_9, 760, 250, true);
        getEkitCoreInstance(jText2_10, 500, 100, false);
        getEkitCoreInstance(jText2_11, 760, 100, false);

        getEkitCoreInstance(jText2_12, 760, 250, true);
        getEkitCoreInstance(jText2_13, 500, 100, false);
        getEkitCoreInstance(jText2_14, 760, 100, false);
    }

    private void getEkitCoreInstance(JPanel jPanel, int width, int height, boolean aditionalTools) {
        /*String sDocument = null;
         String sStyleSheet = null;
         String sRawDocument = null;
         URL urlStyleSheet = null;
         boolean includeToolBar = true;
         boolean base64 = false;
         boolean enterBreak = false;
         boolean showViewSource = false;
         boolean showMenuIcons = false;
         boolean editModeExclusive = true;
         String sLanguage = null;
         String sCountry = null;
         boolean debugMode = false;
         boolean multiBar = true;

         EkitCore ekitCore = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
         */
        Vector<String> v = new Vector<>();
        v.add(EkitCore.KEY_TOOL_COPY);
        v.add(EkitCore.KEY_TOOL_PASTEX);
        v.add(EkitCore.KEY_TOOL_CUT);

        v.add(EkitCore.KEY_TOOL_BOLD);
        v.add(EkitCore.KEY_TOOL_ITALIC);
        v.add(EkitCore.KEY_TOOL_UNDERLINE);

        if (aditionalTools) {
            v.add(EkitCore.KEY_TOOL_ULIST);
            v.add(EkitCore.KEY_TOOL_OLIST);

            v.add(EkitCore.KEY_TOOL_UNDO);
            v.add(EkitCore.KEY_TOOL_REDO);
        }
        EkitCore ekitCore = Utilerias.getEkitCore();
        Utilerias.addEkitcoreLayout(jPanel, ekitCore, v, null, null, null, null, null);

        JTextPane sourceText = ekitCore.getTextPane();
        JTextArea jTextArea = ekitCore.getSourcePane();

        /*jPanel.removeAll();
         jPanel.setLayout(new GridBagLayout());
         jPanel.setPreferredSize(new java.awt.Dimension(width, height));
         jPanel.setMaximumSize(new java.awt.Dimension(width, height));
         jPanel.setMinimumSize(new java.awt.Dimension(width, height));

         GridBagConstraints gbc = new GridBagConstraints();
         gbc.anchor = GridBagConstraints.NORTH;
         gbc.fill = GridBagConstraints.HORIZONTAL;
         gbc.gridheight = 1;
         gbc.gridwidth = 1;
         gbc.weightx = 1.0;
         gbc.weighty = 0.0;
         gbc.gridx = 1;

         JToolBar customBar = ekitCore.customizeToolBar(EkitCore.TOOLBAR_MAIN, v, true);
         jPanel.add(customBar, gbc);
         gbc.anchor = GridBagConstraints.SOUTH;
         gbc.fill = GridBagConstraints.BOTH;
         gbc.weighty = 1.0;
         gbc.gridy = 1;
         jPanel.add(ekitCore, gbc);*/
    }

    protected void addObject(int objectType, ModuleSectionBO moduleSectionBO, JPanel jPanel, DlgObjects dlgObjects) {
        if (jPanel.getComponentCount() > 0 && moduleSectionBO.getLstObjects().size() >= 1) {
            JOptionPane.showMessageDialog(this, "Solo se puede agregar un elemento");
            return;
        }
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } catch (Exception ex) {
            Utilerias.logger(getClass()).error(ex);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }

        Utilerias.centreDialog(dlgObjects, true);
        dlgObjects.setTitle(GlobalDefines.TITULO_APP);
        dlgObjects.setObjType(objectType);

        ObjectInfoBO objInfo = new ObjectInfoBO();
        objInfo.setObjType(GlobalDefines.OBJ_TYPE_EXCEL);

        dlgObjects.setObjInfo(objInfo);
        dlgObjects.setModal(true);
        moduleSectionBO.getLstObjects().clear();
        dlgObjects.initScrObject(objInfo, moduleSectionBO, null, Boolean.TRUE);
        dlgObjects.resetScrObject();
        dlgObjects.setSize(new Dimension(900, 700));
        dlgObjects.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        dlgObjects.setLocationRelativeTo(MainApp.getApplication().getMainFrame());
        dlgObjects.setVisible(true);
        if (dlgObjects.isAccept()) {
            moduleSectionBO.getLstObjects().add(getObjInfo(dlgObjects.getScrObjExcel()));
            refreshObjects(moduleSectionBO, jPanel, dlgObjects);
        }
    }

    private ObjectInfoBO getObjInfo(ScrObjExcel scrObjExcel) {
        ObjectInfoBO objInfo = new ObjectInfoBO();
        LinkedExcelBO linkedExcelBO = (LinkedExcelBO) scrObjExcel.getCboCategoria().getSelectedItem();
        LinkedExcelBO selectedItem = (LinkedExcelBO) scrObjExcel.getCboName().getSelectedItem();
        if (linkedExcelBO != null) {
            linkedExcelBO.getId_category();
        }
        if (selectedItem != null) {
            selectedItem.getIdLinkedExcel();
        }
        objInfo.setFile(scrObjExcel.getEdPath().getText());
        objInfo.setRangoIni(scrObjExcel.getEdRangoIni().getText());
        objInfo.setRangoFin(scrObjExcel.getEdRangoFin().getText());
        objInfo.setTitulo(scrObjExcel.getEdTitulo().getText());
        objInfo.setSubTitulo(scrObjExcel.getEdSubtitulo().getText());
        objInfo.setComentarios(scrObjExcel.getEdDescripcion().getText());
        objInfo.setImage(scrObjExcel.getImage());
        objInfo.setImageThumb(scrObjExcel.getThumbImage());
        return objInfo;
    }

    public void refreshObjects(ModuleSectionBO moduleSectionBO, JPanel jPanel, DlgObjects dlgObject) {
        boolean seAgrega = true;
        jPanel.removeAll();
        JButton butt = new JButton("");
        ScrObjIcon scrObjIcon;
        for (final ObjectInfoBO objInfo : moduleSectionBO.getLstObjects()) {

            if (objInfo.isDelete()) {
                continue;
            }
            butt = new JButton();
            scrObjIcon = new ScrObjIcon(new ScrCell(new ScrDocument(), moduleSectionBO), objInfo, this, moduleSectionBO, jPanel, dlgObject);
            butt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Execute when button is pressed
                    updateObject(objInfo, moduleSectionBO, jPanel, dlgObject);
                }
            });
            switch (objInfo.getObjType()) {
                case GlobalDefines.OBJ_TYPE_EXCEL:
                    butt.setText("Excel");
                    break;
                case GlobalDefines.OBJ_TYPE_IMAGE:
                    butt.setText("Imagen");
                    break;
                case GlobalDefines.OBJ_TYPE_TEXT:
                    butt.setText("Text");
                    seAgrega = false;
                    break;
                case GlobalDefines.OBJ_TYPE_VIDEO:
                    butt.setText("Video");
                    break;

            }
            if (seAgrega) {
                int countIndex = jPanel == null ? 0 : jPanel.getComponentCount();
                scrObjIcon.setPosition(countIndex++);
                jPanel.add(scrObjIcon);
            }
            jPanel.revalidate();
            jPanel.repaint();
        }
        jPanel.revalidate();
        jPanel.repaint();
    }

    public void updateObject(ObjectInfoBO objInfo, ModuleSectionBO moduleSectionBO, JPanel jPanel, DlgObjects dlgObjects) {
        if (dlgObjects == null) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                dlgObjects = new DlgObjects();
            } catch (Exception ex) {
                Utilerias.logger(getClass()).error(ex);
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        dlgObjects.setTitle(GlobalDefines.TITULO_APP);
        dlgObjects.setObjType(objInfo.getObjType());
        dlgObjects.setObjInfo(objInfo);
        dlgObjects.setModal(true);
        dlgObjects.initScrObject(objInfo, moduleSectionBO);
        dlgObjects.setLocationRelativeTo(MainApp.getApplication().getMainFrame());
        dlgObjects.setSize(new Dimension(900, 700));
        dlgObjects.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
        Utilerias.centreDialog(dlgObjects, true);
        dlgObjects.setVisible(true);
        if (dlgObjects.isAccept()) {
            refreshObjects(moduleSectionBO, jPanel, dlgObjects);
        }
    }

    private void assignTX(EkitCore init, EkitCore preview, String tx) {
        String initStr = init.getTextPane().getText();

        String previewStr = preview.getTextPane().getText();
        previewStr = previewStr.replaceAll("<html>", "");
        previewStr = previewStr.replaceAll("<head>", "");
        previewStr = previewStr.replaceAll("<body>", "");
        previewStr = previewStr.replaceAll("</body>", "");
        previewStr = previewStr.replaceAll("</head>", "");
        previewStr = previewStr.replaceAll("</html>", "");

        Pattern p = Pattern.compile("\\b" + tx + "\\b");
        Matcher m = p.matcher(initStr);

        initStr = m.replaceAll(previewStr.trim());
        //initStr = initStr.replaceAll(tx, );
        init.getTextPane().setText(initStr);
    }

    private String getUsuarios() {
        StringBuilder usuarios = new StringBuilder();
        try {
            ArrayOfUsuario arrayUsuarios = stub.buscarUsuarios("ANALISIS", 0, null);
            if (arrayUsuarios != null && arrayUsuarios.getUsuario() != null && arrayUsuarios.getUsuario().length > 0) {
                Usuario[] usuariosLst = arrayUsuarios.getUsuario();
                for (Usuario usuario : usuariosLst) {
                    if (usuario.getIsAutor() != null && usuario.getIsAutor().booleanValue()) {
                        usuarios.append(usuario.getNombre());
                        usuarios.append(" (");
                        usuarios.append(usuario.getCorreo());
                        usuarios.append(", ext ");
                        usuarios.append(usuario.getExtension());
                        usuarios.append("), ");
                    }
                }
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Disclosure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios.toString();
    }

    private DisclosureBO getBO(EkitCore top, TextField expression, EkitCore preview, DisclosureDAO.TYPES type) {
        DisclosureBO bo = new DisclosureBO();
        return getBO(bo, top, expression, preview, type);
    }

    private DisclosureBO getBO(EkitCore top, TextField expression, EkitCore preview, EkitCore bottom, DisclosureDAO.TYPES type) {
        DisclosureBO bo = new DisclosureBO();
        bo = getBO(bo, top, expression, preview, type);
        return getBO(bo, bottom);
    }

    private DisclosureBO getBO(DisclosureBO bo, EkitCore top, TextField expression, EkitCore preview, DisclosureDAO.TYPES type) {
        bo.setType(type.type());
        bo.setTextTop(top.getTextPane().getText());
        bo.setRegularExpression(expression.getText());
        bo.setPreview(preview.getTextPane().getText());
        return bo;
    }

    private DisclosureBO getBO(DisclosureBO bo, EkitCore bottom) {
        bo.setTextBottom(bottom.getTextPane().getText());
        return bo;
    }

    private boolean saveRentaFija() {
        DisclosureBO bo = getBO((EkitCore) jText2_4.getComponent(1), txtTX3, (EkitCore) jText2_5.getComponent(1), DisclosureDAO.TYPES.RENTA_FIJA);
        Boolean bRet = Boolean.FALSE;
        if (rentaFija) {
            bRet = dao.updateDisclosure(bo, DisclosureDAO.TYPES.RENTA_FIJA);
        } else {
            bRet = dao.insertDisclosure(bo, DisclosureDAO.TYPES.RENTA_FIJA);
        }
        return bRet.booleanValue();
    }

    public boolean saveFundSinEmisora() {
        DisclosureBO bo = getBO((EkitCore) jText2_8.getComponent(1), txtTX4, (EkitCore) jText2_7.getComponent(1), (EkitCore) jText2_6.getComponent(1), DisclosureDAO.TYPES.F_SIN_EMISORA);
        Boolean bRet = Boolean.FALSE;
        bo.setExcelIni(getExcelBO(jExcel2_3, dlgObject3, jExcel2_3.getComponentCount() <= 0));
        bo.setExcelFin(getExcelBO(jExcel2_4, dlgObject4, jExcel2_4.getComponentCount() <= 0));
        if (fundSinEmisora) {
            bRet = dao.updateDisclosure(bo, DisclosureDAO.TYPES.F_SIN_EMISORA);
        } else {
            bRet = dao.insertDisclosure(bo, DisclosureDAO.TYPES.F_SIN_EMISORA);
        }
        return bRet.booleanValue();
    }

    public boolean saveFundConEmisora() {
        DisclosureBO bo = getBO((EkitCore) jText2_1.getComponent(1), txtTX2, (EkitCore) jText2_2.getComponent(1), (EkitCore) jText2_3.getComponent(1), DisclosureDAO.TYPES.F_CON_EMISORA);
        Boolean bRet = Boolean.FALSE;
        bo.setExcelIni(getExcelBO(jExcel2_1, dlgObject1, jExcel2_1.getComponentCount() <= 0));
        bo.setExcelFin(getExcelBO(jExcel2_2, dlgObject2, jExcel2_2.getComponentCount() <= 0));
        if (fundConEmisora) {
            bRet = dao.updateDisclosure(bo, DisclosureDAO.TYPES.F_CON_EMISORA);
        } else {
            bRet = dao.insertDisclosure(bo, DisclosureDAO.TYPES.F_CON_EMISORA);
        }
        return bRet.booleanValue();
    }

    private boolean saveSemanario() {
        DisclosureBO bo = getBO((EkitCore) jText2_9.getComponent(1), txtTX5, (EkitCore) jText2_10.getComponent(1), (EkitCore) jText2_11.getComponent(1), DisclosureDAO.TYPES.SEMANARIO);
        Boolean bRet = Boolean.FALSE;
        bo.setExcelIni(getExcelBO(jExcel2_6, dlgObject6, jExcel2_6.getComponentCount() <= 0));
        bo.setExcelFin(getExcelBO(jExcel2_5, dlgObject5, jExcel2_5.getComponentCount() <= 0));
        if (semanario) {
            bRet = dao.updateDisclosure(bo, DisclosureDAO.TYPES.SEMANARIO);
        } else {
            bRet = dao.insertDisclosure(bo, DisclosureDAO.TYPES.SEMANARIO);
        }
        return bRet.booleanValue();
    }

    private boolean saveEconomico() {
        DisclosureBO bo = getBO((EkitCore) jText2_12.getComponent(1), txtTX6, (EkitCore) jText2_13.getComponent(1), (EkitCore) jText2_14.getComponent(1), DisclosureDAO.TYPES.ECONOMICO);
        Boolean bRet = Boolean.FALSE;
        bo.setExcelIni(getExcelBO(jExcel2_8, dlgObject8, jExcel2_8.getComponentCount() <= 0));
        bo.setExcelFin(getExcelBO(jExcel2_7, dlgObject7, jExcel2_7.getComponentCount() <= 0));
        if (economico) {
            bRet = dao.updateDisclosure(bo, DisclosureDAO.TYPES.ECONOMICO);
        } else {
            bRet = dao.insertDisclosure(bo, DisclosureDAO.TYPES.ECONOMICO);
        }
        return bRet.booleanValue();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFundSinEmisora = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jText2_4 = new javax.swing.JPanel();
        txtTX3 = new java.awt.TextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jText2_5 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jText2_6 = new javax.swing.JPanel();
        jExcel2_3 = new javax.swing.JPanel();
        jExcel2_4 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jText2_7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTX4 = new java.awt.TextField();
        jText2_8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTX2 = new java.awt.TextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jExcel2_1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jExcel2_2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jText2_1 = new javax.swing.JPanel();
        jText2_2 = new javax.swing.JPanel();
        jText2_3 = new javax.swing.JPanel();
        jEmisoras = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jText2_9 = new javax.swing.JPanel();
        txtTX5 = new java.awt.TextField();
        jText2_10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jExcel2_5 = new javax.swing.JPanel();
        jExcel2_6 = new javax.swing.JPanel();
        jText2_11 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jText2_12 = new javax.swing.JPanel();
        txtTX6 = new java.awt.TextField();
        jText2_13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jExcel2_7 = new javax.swing.JPanel();
        jExcel2_8 = new javax.swing.JPanel();
        jText2_14 = new javax.swing.JPanel();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jFundSinEmisora.setToolTipText("");
        jFundSinEmisora.setMaximumSize(new java.awt.Dimension(800, 722));
        jFundSinEmisora.setMinimumSize(new java.awt.Dimension(800, 722));
        jFundSinEmisora.setName("jFundSinEmisora"); // NOI18N
        jFundSinEmisora.setPreferredSize(new java.awt.Dimension(800, 722));

        jPanel1.setName("jPanel1"); // NOI18N

        jText2_4.setBackground(new java.awt.Color(255, 255, 255));
        jText2_4.setMaximumSize(new java.awt.Dimension(760, 250));
        jText2_4.setMinimumSize(new java.awt.Dimension(760, 250));
        jText2_4.setName("jText2_4"); // NOI18N

        javax.swing.GroupLayout jText2_4Layout = new javax.swing.GroupLayout(jText2_4);
        jText2_4.setLayout(jText2_4Layout);
        jText2_4Layout.setHorizontalGroup(
            jText2_4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_4Layout.setVerticalGroup(
            jText2_4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtTX3.setName("txtTX3"); // NOI18N
        txtTX3.setText("CODE_TX");

        jLabel6.setText("Reemplazar con:");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText("Encontrar:");
        jLabel7.setName("jLabel7"); // NOI18N

        jText2_5.setBackground(new java.awt.Color(255, 255, 255));
        jText2_5.setMaximumSize(new java.awt.Dimension(500, 100));
        jText2_5.setMinimumSize(new java.awt.Dimension(500, 100));
        jText2_5.setName("jText2_5"); // NOI18N
        jText2_5.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jText2_5Layout = new javax.swing.GroupLayout(jText2_5);
        jText2_5.setLayout(jText2_5Layout);
        jText2_5Layout.setHorizontalGroup(
            jText2_5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jText2_5Layout.setVerticalGroup(
            jText2_5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton10.setText("Sustituir Texto");
        jButton10.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton10.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton10.setName("jButton10"); // NOI18N
        jButton10.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Consultar Analistas");
        jButton11.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton11.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton11.setName("jButton11"); // NOI18N
        jButton11.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton13.setText("Cancelar");
        jButton13.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton13.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton13.setName("jButton13"); // NOI18N
        jButton13.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("Guardar");
        jButton14.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton14.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton14.setName("jButton14"); // NOI18N
        jButton14.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jText2_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTX3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jText2_5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jText2_4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTX3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jText2_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(275, 275, 275)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jFundSinEmisora.addTab("Renta fija", jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(800, 697));
        jPanel2.setMinimumSize(new java.awt.Dimension(800, 697));
        jPanel2.setName("jPanel2"); // NOI18N

        jButton8.setText("Cancelar");
        jButton8.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton8.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton8.setName("jButton8"); // NOI18N
        jButton8.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Guardar");
        jButton9.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton9.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton9.setName("jButton9"); // NOI18N
        jButton9.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jText2_6.setBackground(new java.awt.Color(255, 255, 255));
        jText2_6.setMaximumSize(new java.awt.Dimension(760, 100));
        jText2_6.setMinimumSize(new java.awt.Dimension(760, 100));
        jText2_6.setName("jText2_6"); // NOI18N
        jText2_6.setPreferredSize(new java.awt.Dimension(760, 100));

        javax.swing.GroupLayout jText2_6Layout = new javax.swing.GroupLayout(jText2_6);
        jText2_6.setLayout(jText2_6Layout);
        jText2_6Layout.setHorizontalGroup(
            jText2_6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_6Layout.setVerticalGroup(
            jText2_6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jExcel2_3.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_3.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_3.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_3.setName("jExcel2_3"); // NOI18N
        jExcel2_3.setPreferredSize(new java.awt.Dimension(100, 100));

        jExcel2_4.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_4.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_4.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_4.setName("jExcel2_4"); // NOI18N

        jButton12.setText("Excel Vinculado 1");
        jButton12.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton12.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton12.setName("jButton12"); // NOI18N
        jButton12.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton15.setText("Excel Vinculado 2");
        jButton15.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton15.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton15.setName("jButton15"); // NOI18N
        jButton15.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Sustituir Texto");
        jButton16.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton16.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton16.setName("jButton16"); // NOI18N
        jButton16.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Consultar Analistas");
        jButton17.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton17.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton17.setName("jButton17"); // NOI18N
        jButton17.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jText2_7.setBackground(new java.awt.Color(255, 255, 255));
        jText2_7.setMaximumSize(new java.awt.Dimension(500, 100));
        jText2_7.setMinimumSize(new java.awt.Dimension(500, 100));
        jText2_7.setName("jText2_7"); // NOI18N
        jText2_7.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jText2_7Layout = new javax.swing.GroupLayout(jText2_7);
        jText2_7.setLayout(jText2_7Layout);
        jText2_7Layout.setHorizontalGroup(
            jText2_7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jText2_7Layout.setVerticalGroup(
            jText2_7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel8.setText("Encontrar:");
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText("Reemplazar con:");
        jLabel9.setToolTipText("");
        jLabel9.setName("jLabel9"); // NOI18N

        txtTX4.setName("txtTX4"); // NOI18N
        txtTX4.setText("CODE_TX");

        jText2_8.setBackground(new java.awt.Color(255, 255, 255));
        jText2_8.setMaximumSize(new java.awt.Dimension(760, 250));
        jText2_8.setMinimumSize(new java.awt.Dimension(760, 250));
        jText2_8.setName("jText2_8"); // NOI18N

        javax.swing.GroupLayout jText2_8Layout = new javax.swing.GroupLayout(jText2_8);
        jText2_8.setLayout(jText2_8Layout);
        jText2_8Layout.setHorizontalGroup(
            jText2_8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_8Layout.setVerticalGroup(
            jText2_8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jText2_8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jText2_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jExcel2_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jExcel2_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtTX4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jText2_7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jText2_8, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTX4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jText2_7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jExcel2_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jExcel2_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jText2_6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jFundSinEmisora.addTab("Fundamental sin emisora", jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(800, 600));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 600));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel5.setText("Encontrar:");
        jLabel5.setName("jLabel5"); // NOI18N

        txtTX2.setName("txtTX2"); // NOI18N
        txtTX2.setText("CODE_TX");

        jButton4.setText("Sustituir Texto");
        jButton4.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton4.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Consultar Analistas");
        jButton5.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton5.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jExcel2_1.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_1.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_1.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_1.setName("jExcel2_1"); // NOI18N
        jExcel2_1.setPreferredSize(new java.awt.Dimension(100, 100));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));
        jPanel6.setName("jPanel6"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton1.setText("Excel Vinculado 1");
        jButton1.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jExcel2_2.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_2.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_2.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_2.setName("jExcel2_2"); // NOI18N

        jButton3.setText("Excel Vinculado 2");
        jButton3.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton3.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jText2_1.setBackground(new java.awt.Color(255, 255, 255));
        jText2_1.setMaximumSize(new java.awt.Dimension(760, 250));
        jText2_1.setMinimumSize(new java.awt.Dimension(760, 250));
        jText2_1.setName("jText2_1"); // NOI18N
        jText2_1.setPreferredSize(new java.awt.Dimension(760, 250));

        javax.swing.GroupLayout jText2_1Layout = new javax.swing.GroupLayout(jText2_1);
        jText2_1.setLayout(jText2_1Layout);
        jText2_1Layout.setHorizontalGroup(
            jText2_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_1Layout.setVerticalGroup(
            jText2_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jText2_2.setBackground(new java.awt.Color(255, 255, 255));
        jText2_2.setMaximumSize(new java.awt.Dimension(500, 100));
        jText2_2.setMinimumSize(new java.awt.Dimension(500, 100));
        jText2_2.setName("jText2_2"); // NOI18N
        jText2_2.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jText2_2Layout = new javax.swing.GroupLayout(jText2_2);
        jText2_2.setLayout(jText2_2Layout);
        jText2_2Layout.setHorizontalGroup(
            jText2_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jText2_2Layout.setVerticalGroup(
            jText2_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jText2_3.setBackground(new java.awt.Color(255, 255, 255));
        jText2_3.setMaximumSize(new java.awt.Dimension(760, 100));
        jText2_3.setMinimumSize(new java.awt.Dimension(760, 100));
        jText2_3.setName("jText2_3"); // NOI18N
        jText2_3.setPreferredSize(new java.awt.Dimension(760, 100));

        javax.swing.GroupLayout jText2_3Layout = new javax.swing.GroupLayout(jText2_3);
        jText2_3.setLayout(jText2_3Layout);
        jText2_3Layout.setHorizontalGroup(
            jText2_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_3Layout.setVerticalGroup(
            jText2_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jEmisoras.setBackground(new java.awt.Color(204, 255, 255));
        jEmisoras.setMaximumSize(new java.awt.Dimension(500, 100));
        jEmisoras.setMinimumSize(new java.awt.Dimension(500, 100));
        jEmisoras.setName("jEmisoras"); // NOI18N
        jEmisoras.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jEmisorasLayout = new javax.swing.GroupLayout(jEmisoras);
        jEmisoras.setLayout(jEmisorasLayout);
        jEmisorasLayout.setHorizontalGroup(
            jEmisorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jEmisorasLayout.setVerticalGroup(
            jEmisorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel4.setText("Reemplazar con:");
        jLabel4.setName("jLabel4"); // NOI18N

        jButton6.setText("Cancelar");
        jButton6.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton6.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton6.setName("jButton6"); // NOI18N
        jButton6.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Guardar");
        jButton7.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton7.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton7.setName("jButton7"); // NOI18N
        jButton7.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtTX2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jText2_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jExcel2_1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jExcel2_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(356, 356, 356))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jEmisoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jText2_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jText2_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jText2_1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTX2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jExcel2_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jExcel2_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jText2_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jEmisoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jText2_3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(396, 396, 396))))
        );

        jText2_3.getAccessibleContext().setAccessibleName("");

        jFundSinEmisora.addTab("Fundamental con emisora", jPanel3);

        jPanel4.setName("jPanel4"); // NOI18N

        jText2_9.setBackground(new java.awt.Color(255, 255, 255));
        jText2_9.setMaximumSize(new java.awt.Dimension(760, 250));
        jText2_9.setMinimumSize(new java.awt.Dimension(760, 250));
        jText2_9.setName("jText2_9"); // NOI18N

        javax.swing.GroupLayout jText2_9Layout = new javax.swing.GroupLayout(jText2_9);
        jText2_9.setLayout(jText2_9Layout);
        jText2_9Layout.setHorizontalGroup(
            jText2_9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_9Layout.setVerticalGroup(
            jText2_9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtTX5.setName("txtTX5"); // NOI18N
        txtTX5.setText("CODE_TX");

        jText2_10.setBackground(new java.awt.Color(255, 255, 255));
        jText2_10.setMaximumSize(new java.awt.Dimension(500, 100));
        jText2_10.setMinimumSize(new java.awt.Dimension(500, 100));
        jText2_10.setName("jText2_10"); // NOI18N
        jText2_10.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jText2_10Layout = new javax.swing.GroupLayout(jText2_10);
        jText2_10.setLayout(jText2_10Layout);
        jText2_10Layout.setHorizontalGroup(
            jText2_10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jText2_10Layout.setVerticalGroup(
            jText2_10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel10.setText("Reemplazar con:");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText("Encontrar:");
        jLabel11.setName("jLabel11"); // NOI18N

        jButton18.setText("Excel Vinculado 1");
        jButton18.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton18.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton18.setName("jButton18"); // NOI18N
        jButton18.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setText("Excel Vinculado 2");
        jButton19.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton19.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton19.setName("jButton19"); // NOI18N
        jButton19.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("Agregar Texto");
        jButton20.setActionCommand("Sustituir Texto");
        jButton20.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton20.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton20.setName("jButton20"); // NOI18N
        jButton20.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText("Consultar Analistas");
        jButton21.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton21.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton21.setName("jButton21"); // NOI18N
        jButton21.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jExcel2_5.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_5.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_5.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_5.setName("jExcel2_5"); // NOI18N

        jExcel2_6.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_6.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_6.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_6.setName("jExcel2_6"); // NOI18N
        jExcel2_6.setPreferredSize(new java.awt.Dimension(100, 100));

        jText2_11.setBackground(new java.awt.Color(255, 255, 255));
        jText2_11.setMaximumSize(new java.awt.Dimension(760, 100));
        jText2_11.setMinimumSize(new java.awt.Dimension(760, 100));
        jText2_11.setName("jText2_11"); // NOI18N
        jText2_11.setPreferredSize(new java.awt.Dimension(760, 100));

        javax.swing.GroupLayout jText2_11Layout = new javax.swing.GroupLayout(jText2_11);
        jText2_11.setLayout(jText2_11Layout);
        jText2_11Layout.setHorizontalGroup(
            jText2_11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_11Layout.setVerticalGroup(
            jText2_11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jButton22.setText("Guardar");
        jButton22.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton22.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton22.setName("jButton22"); // NOI18N
        jButton22.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setText("Cancelar");
        jButton23.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton23.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton23.setName("jButton23"); // NOI18N
        jButton23.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jText2_9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jText2_11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTX5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jExcel2_6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jExcel2_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jText2_10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jText2_9, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jText2_10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTX5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jExcel2_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jExcel2_6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(9, 9, 9)
                .addComponent(jText2_11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jFundSinEmisora.addTab("Semanario", jPanel4);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(780, 690));

        jText2_12.setBackground(new java.awt.Color(255, 255, 255));
        jText2_12.setMaximumSize(new java.awt.Dimension(760, 250));
        jText2_12.setMinimumSize(new java.awt.Dimension(760, 250));
        jText2_12.setName("jText2_12"); // NOI18N

        javax.swing.GroupLayout jText2_12Layout = new javax.swing.GroupLayout(jText2_12);
        jText2_12.setLayout(jText2_12Layout);
        jText2_12Layout.setHorizontalGroup(
            jText2_12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_12Layout.setVerticalGroup(
            jText2_12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtTX6.setName("txtTX6"); // NOI18N
        txtTX6.setText("CODE_TX");

        jText2_13.setBackground(new java.awt.Color(255, 255, 255));
        jText2_13.setMaximumSize(new java.awt.Dimension(500, 100));
        jText2_13.setMinimumSize(new java.awt.Dimension(500, 100));
        jText2_13.setName("jText2_13"); // NOI18N
        jText2_13.setPreferredSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jText2_13Layout = new javax.swing.GroupLayout(jText2_13);
        jText2_13.setLayout(jText2_13Layout);
        jText2_13Layout.setHorizontalGroup(
            jText2_13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jText2_13Layout.setVerticalGroup(
            jText2_13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel12.setText("Reemplazar con:");
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText("Encontrar:");
        jLabel13.setName("jLabel13"); // NOI18N

        jButton24.setText("Excel Vinculado 1");
        jButton24.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton24.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton24.setName("jButton24"); // NOI18N
        jButton24.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setText("Excel Vinculado 2");
        jButton25.setMaximumSize(new java.awt.Dimension(120, 20));
        jButton25.setMinimumSize(new java.awt.Dimension(120, 20));
        jButton25.setName("jButton25"); // NOI18N
        jButton25.setPreferredSize(new java.awt.Dimension(120, 20));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setText("Sustituir Texto");
        jButton26.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton26.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton26.setName("jButton26"); // NOI18N
        jButton26.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setText("Consultar Analistas");
        jButton27.setMaximumSize(new java.awt.Dimension(130, 20));
        jButton27.setMinimumSize(new java.awt.Dimension(130, 20));
        jButton27.setName("jButton27"); // NOI18N
        jButton27.setPreferredSize(new java.awt.Dimension(130, 20));
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jExcel2_7.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_7.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_7.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_7.setName("jExcel2_7"); // NOI18N

        jExcel2_8.setBackground(new java.awt.Color(204, 255, 204));
        jExcel2_8.setMaximumSize(new java.awt.Dimension(100, 100));
        jExcel2_8.setMinimumSize(new java.awt.Dimension(100, 100));
        jExcel2_8.setName("jExcel2_8"); // NOI18N
        jExcel2_8.setPreferredSize(new java.awt.Dimension(100, 100));

        jText2_14.setBackground(new java.awt.Color(255, 255, 255));
        jText2_14.setMaximumSize(new java.awt.Dimension(760, 100));
        jText2_14.setMinimumSize(new java.awt.Dimension(760, 100));
        jText2_14.setName("jText2_14"); // NOI18N
        jText2_14.setPreferredSize(new java.awt.Dimension(760, 100));

        javax.swing.GroupLayout jText2_14Layout = new javax.swing.GroupLayout(jText2_14);
        jText2_14.setLayout(jText2_14Layout);
        jText2_14Layout.setHorizontalGroup(
            jText2_14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jText2_14Layout.setVerticalGroup(
            jText2_14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jButton28.setText("Guardar");
        jButton28.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton28.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton28.setName("jButton28"); // NOI18N
        jButton28.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Cancelar");
        jButton29.setMaximumSize(new java.awt.Dimension(100, 20));
        jButton29.setMinimumSize(new java.awt.Dimension(100, 20));
        jButton29.setName("jButton29"); // NOI18N
        jButton29.setPreferredSize(new java.awt.Dimension(100, 20));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jText2_12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jText2_14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel12))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jExcel2_8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jExcel2_7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtTX6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jText2_13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jText2_12, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jText2_13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTX6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jExcel2_7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jExcel2_8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(9, 9, 9)
                .addComponent(jText2_14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jFundSinEmisora.addTab("Económico", jPanel5);

        getContentPane().add(jFundSinEmisora, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        EkitCore ekitCoreComp = (EkitCore) jText2_2.getComponent(1);
        ekitCoreComp.getTextPane().setText(getUsuarios());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_1, jExcel2_1, dlgObject1);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_2, jExcel2_2, dlgObject2);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        assignTX((EkitCore) jText2_1.getComponent(1), (EkitCore) jText2_2.getComponent(1), txtTX2.getText());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        assignTX((EkitCore) jText2_4.getComponent(1), (EkitCore) jText2_5.getComponent(1), txtTX3.getText());
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        EkitCore ekitCoreComp = (EkitCore) jText2_5.getComponent(1);
        ekitCoreComp.getTextPane().setText(getUsuarios());
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_3, jExcel2_3, dlgObject3);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_4, jExcel2_4, dlgObject4);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        assignTX((EkitCore) jText2_8.getComponent(1), (EkitCore) jText2_7.getComponent(1), txtTX4.getText());
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        EkitCore ekitCoreComp = (EkitCore) jText2_7.getComponent(1);
        ekitCoreComp.getTextPane().setText(getUsuarios());
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_6, jExcel2_6, dlgObject6);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_5, jExcel2_5, dlgObject5);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        assignTX((EkitCore) jText2_9.getComponent(1), (EkitCore) jText2_10.getComponent(1), txtTX5.getText());
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        EkitCore ekitCoreComp = (EkitCore) jText2_10.getComponent(1);
        ekitCoreComp.getTextPane().setText(getUsuarios());
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_8, jExcel2_8, dlgObject8);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL, secInfoExcel_7, jExcel2_7, dlgObject7);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        assignTX((EkitCore) jText2_12.getComponent(1), (EkitCore) jText2_13.getComponent(1), txtTX6.getText());
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        EkitCore ekitCoreComp = (EkitCore) jText2_13.getComponent(1);
        ekitCoreComp.getTextPane().setText(getUsuarios());
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (saveRentaFija()) {
            rentaFija = true;
            JOptionPane.showMessageDialog(this, "Renta fija guardado exitoso");
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (saveFundSinEmisora()) {
            fundSinEmisora = true;
            JOptionPane.showMessageDialog(this, "Fundamental sin emisora guardado exitoso");
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (saveFundConEmisora()) {
            fundConEmisora = true;
            JOptionPane.showMessageDialog(this, "Fundamental con emisora guardado exitoso");
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        if (saveSemanario()) {
            semanario = true;
            JOptionPane.showMessageDialog(this, "Semanario guardado exitoso");
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar");
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        if (saveEconomico()) {
            economico = true;
            JOptionPane.showMessageDialog(this, "Economico guardado exitoso");
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar");
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jEmisoras;
    private javax.swing.JPanel jExcel2_1;
    private javax.swing.JPanel jExcel2_2;
    private javax.swing.JPanel jExcel2_3;
    private javax.swing.JPanel jExcel2_4;
    private javax.swing.JPanel jExcel2_5;
    private javax.swing.JPanel jExcel2_6;
    private javax.swing.JPanel jExcel2_7;
    private javax.swing.JPanel jExcel2_8;
    private javax.swing.JTabbedPane jFundSinEmisora;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jText2_1;
    private javax.swing.JPanel jText2_10;
    private javax.swing.JPanel jText2_11;
    private javax.swing.JPanel jText2_12;
    private javax.swing.JPanel jText2_13;
    private javax.swing.JPanel jText2_14;
    private javax.swing.JPanel jText2_2;
    private javax.swing.JPanel jText2_3;
    private javax.swing.JPanel jText2_4;
    private javax.swing.JPanel jText2_5;
    private javax.swing.JPanel jText2_6;
    private javax.swing.JPanel jText2_7;
    private javax.swing.JPanel jText2_8;
    private javax.swing.JPanel jText2_9;
    private java.awt.TextField txtTX2;
    private java.awt.TextField txtTX3;
    private java.awt.TextField txtTX4;
    private java.awt.TextField txtTX5;
    private java.awt.TextField txtTX6;
    // End of variables declaration//GEN-END:variables
}
