/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.dnd;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.DlgObjects;
import com.adinfi.formateador.view.DragGestureListenerImp;
import com.adinfi.formateador.view.ScrDocument;
import com.adinfi.formateador.view.ScrObjVideo;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Desarrollador
 */
public class ScrCell extends JPanel implements DropTargetListener {
    private ModuleBO moduleBO;  
    private ModuleSectionBO secInfo;
    private DropTarget dropTarget;
    private boolean acceptableType; // Indicates whether data is acceptable
    private DataFlavor targetFlavor; // Flavor to use for transfer     
    private DlgObjects dlgObjects;
    private ScrDocument parentDoc;
    public static DataFlavor DATA_FLAVOR = new DataFlavor(JPanel.class, JPanel.class.getSimpleName());
    private Integer index;
    private JButton btnImage = null;
    private JButton btnExcel = null;
    private JButton btnVideo = null;
    boolean proxy = Boolean.parseBoolean(Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG));
    
    
    public ScrCell(ScrDocument scrDoc, ModuleSectionBO secInfo, ModuleBO moduleBO) {
        this(scrDoc, secInfo);
        this.moduleBO=moduleBO;
       
    }    
    

    /**
     * Creates new form ScrCell
     *
     * @param scrDoc
     * @param secInfo
     */
    public ScrCell(ScrDocument scrDoc, ModuleSectionBO secInfo) {
        initComponents();
        this.secInfo = secInfo;
        this.parentDoc = scrDoc;
        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<ObjectInfoBO>());
        }
 
        initScreen();
        initPanel();
    }

    private void initPanel() {
        final DragGestureListenerImp dragGestureListenerImp = new DragGestureListenerImp(pObj, new Runnable() {
            @Override
            public void run() {
                //TODO Ejecutar ordwn en lista de objetos
                Utilerias.logger(getClass()).info("DROPPPPPPPPPPPPPPPPPP");
                for(Component c : pObj.getComponents()){
                    if(c instanceof ScrObjIcon){
                        ScrObjIcon obj = (ScrObjIcon) c;
                        Utilerias.logger(getClass()).info(obj.getPosition());
                        Utilerias.logger(getClass()).info(obj.getName());
                        for (ObjectInfoBO objInfo : secInfo.getLstObjects()) {
                            if(objInfo.getFile() != null && objInfo.getFile().equals(obj.getObjectInfo().getFile())){
                                objInfo.setOrderId(obj.getPosition());
                                break;
                            }
                            if(objInfo.getIdVideo() != null && objInfo.getIdVideo().equals(obj.getObjectInfo().getIdVideo())){
                                objInfo.setOrderId(obj.getPosition());
                                break;
                            }
                        }
                    }
                }
            }
        });
        DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(pObj,
                DnDConstants.ACTION_MOVE, dragGestureListenerImp);
    }

    public final void initScreen() {
        popSection = new JPopupMenu();
        popSection.setBorder(new BevelBorder(BevelBorder.RAISED));
        JMenuItem menuItem = new JMenuItem("Imagen");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addObject(GlobalDefines.OBJ_TYPE_IMAGE);
            }
        });
        popSection.add(menuItem);

        menuItem = new JMenuItem("Excel");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addObject(GlobalDefines.OBJ_TYPE_EXCEL);
            }
        });
        popSection.add(menuItem);

        menuItem = new JMenuItem("Video");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(e);
                addObject(GlobalDefines.OBJ_TYPE_VIDEO);
            }
        });
        popSection.add(menuItem);

        popSection.addSeparator();
        menuItem = new JMenuItem("Eliminar Módulo");
        menuItem.setEnabled(false);
        popSection.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteModule();
            }
        });

        popSection.addSeparator();

        menuItem = new JMenuItem("Agregar módulo a colaborativo");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addToCollaborative();
            }
        });
        popSection.add(menuItem);

        /*
         menuItem = new JMenuItem("Pegar");
         popSection.add(menuItem);
         */
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    popSection.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
        initButtons();
        refreshObjects();
        setPanelTools();
    }

    protected void addToCollaborative() {
       // this.parentDoc.addModuleColaborative(this.secInfo.getModuleId());
       if( this.moduleBO.getDocumentModuleId()<=0 ) {       
         JOptionPane.showMessageDialog(this, "El módulo debe ser guardado para que se pueda agregar al documento colaborativo");
         return;         
       }         
         this.parentDoc.addModuleColaborative(this.moduleBO.getDocumentModuleId() );
        
    }

    private static DataFlavor dragAndDropPanelDataFlavor = null;

    /**
     * <p>
     * Returns (creating, if necessary) the DataFlavor representing
     * RandomDragAndDropPanel</p>
     *
     * @return
     */
    public static DataFlavor getDragAndDropPanelDataFlavor() throws Exception {
        // Lazy load/create the flavor
        if (dragAndDropPanelDataFlavor == null) {
            dragAndDropPanelDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=DraggedButton");
        }

        return dragAndDropPanelDataFlavor;
    }

    /**
     * Keep a list of the user-added panels so can re-add
     */
    //private final List<DraggedButton> panels;
    /**
     * <p>
     * Returns the List of user-added panels.</p>
     * <p>
     * Note that for drag and drop, these will be cleared, and the panels will
     * be added back in the correct order!</p>
     *
     * @return
     */
//    protected List<DraggedButton> getRandomDragAndDropPanels() {
//        return panels;
//    }
    /**
     * <p>
     * Removes all components from our root panel and re-adds them.</p>
     * <p>
     * This is important for two things:</p>
     * <ul>
     * <li>Adding a new panel (user clicks on button)</li>
     * <li>Re-ordering panels (user drags and drops a panel to acceptable drop
     * target region)</li>
     * </ul>
     */
    protected void relayout() {
//
//        // Create the constraints, and go ahead and set those
//        // that don't change for components
//        final GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.weighty = 0.0;
//        gbc.fill = GridBagConstraints.NONE;
//
//        int row = 0;
//
//        // Clear out all previously added items
//        rootPanel.removeAll();
//
//        // Add the button
//        gbc.gridy = row++;
//        rootPanel.add(addPanelButton, gbc);
//
//        // Put a lot of room around panels so can drop easily!
//        gbc.insets = new Insets(PANEL_INSETS, PANEL_INSETS, PANEL_INSETS, PANEL_INSETS);
//        
//        // Add the panels, if any
//        for (RandomDragAndDropPanel p : getRandomDragAndDropPanels()) {
//            gbc.gridy = row++;
//            rootPanel.add(p, gbc);
//        }
//
//        // Add a vertical strut to push content to top.
//        gbc.weighty = 1.0;
//        gbc.weightx = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.gridy = row++;
//        Component strut = Box.createVerticalStrut(1);
//        rootPanel.add(strut, gbc);
//
//        this.validate();
//        this.repaint();
    }

    protected void initButtons() {

        btnImage = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnVideo = new javax.swing.JButton();

        btnImage.setRequestFocusEnabled(false);
        btnImage.setBackground(new java.awt.Color(255, 255, 255));
        btnImage.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnImage.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.IMAGE));
        //btnImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        //btnImage.setPreferredSize(new java.awt.Dimension(32, 32));
        btnImage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionImagen();
            }
        });

        btnExcel.setRequestFocusEnabled(false);
        btnExcel.setBackground(new java.awt.Color(255, 255, 255));
        btnExcel.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnExcel.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.EXCEL));
        //btnExcel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.));
        //btnExcel.setPreferredSize(new java.awt.Dimension(32, 32));
        btnExcel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionExcel();
            }
        });

        btnVideo.setRequestFocusEnabled(false);
        btnVideo.setBackground(new java.awt.Color(255, 255, 255));
        btnVideo.setFont(new java.awt.Font("Tahoma", 3, 13));
        btnVideo.setIcon(Utilerias.getImageIcon(Utilerias.ICONS.VIDEO));
        //btnVideo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        //btnVideo.setPreferredSize(new java.awt.Dimension(32, 32));
        btnVideo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionVideo();
            }
        });

    }

    private void actionImagen() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_IMAGE);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void actionExcel() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_EXCEL);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void actionVideo() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addObject(GlobalDefines.OBJ_TYPE_VIDEO);
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    protected void setPanelTools() {
        if (this.secInfo.isText() == false) {
            toolBarButtons.add(btnImage);
            toolBarButtons.add(btnExcel);
            toolBarButtons.add(btnVideo);
        }
    }

    public void setDrag() {
        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
                this, true, null);
        this.setDropTarget(dropTarget);
    }

    public void deleteModule() {
        this.parentDoc.deleteModule(this.secInfo.getSectionParentId());
    }

    // Implementation of the DropTargetListener interface
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);
        // Accept or reject the drag.
        acceptOrRejectDrag(dtde);
    }
    
    protected void updateObject(ObjectInfoBO objInfo) {
        Utilerias.pasarGarbageCollector();
        if (objInfo.getObjType() == GlobalDefines.OBJ_TYPE_VIDEO) {

            ScrObjVideo objVideo = new ScrObjVideo(MainApp.getApplication().getMainFrame(), true);
            objVideo.setObjInfo(objInfo);
            objVideo.initScrObject(objInfo);
            objVideo.setVideoInfo();
            Utilerias.centreDialog(objVideo, true);
            objVideo.setVisible(true);
            


            if (objVideo.isAccept() != null && objVideo.isAccept() == true) {

                if (proxy) {
                    System.setProperty("http.proxyHost", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_HOST));
                    System.setProperty("http.proxyPort", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_PORT));
                }

                String html = objVideo.getHtmlVideo();

                String thumb = objVideo.getThumb();
                BufferedImage thumbBuff = null;
                try {
                    thumbBuff = ImageIO.read(new URL(thumb));
                } catch (IOException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
                objInfo.setImage(thumbBuff);
                objInfo.setThumbnailUrl(thumb);

                objInfo.setHtmlVideo(html);
                objInfo.setTitulo(objVideo.getTituloVideo());
                objInfo.setComentarios(objVideo.getFuenteVideo());

                objInfo.setSubTitulo(objVideo.getSubtituloVideo());
                objInfo.setIdVideo(objVideo.getId_video());
                objInfo.setIdVideoDb(objVideo.getIdvideodb());
                objInfo.setTipoVideo(objVideo.getTipoVideo());

                refreshObjects();
            }
        } else {
            if (dlgObjects == null) {
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    dlgObjects = new DlgObjects();
                    dlgObjects.addKeyListener(new KeyListener() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (dlgObjects.getTabObjects().getSelectedIndex() == 0) {
                                Utilerias.logger(getClass()).info("ObjExcel");
                            } else {
                                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                                    dlgObjects.setPaste(true);
                                }
                                if (e.getKeyCode() == KeyEvent.VK_V && dlgObjects.getPaste()) {
                                    dlgObjects.getScrObjImage().pasteImage();
                                }
                            }
                        }

                        @Override
                        public void keyTyped(KeyEvent e) {
                            ;
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            dlgObjects.setPaste(false);;
                        }

                    });
                    dlgObjects.setFocusable(true);
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).error(ex);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
            Boolean isNew = Boolean.FALSE;
            dlgObjects.setTitle(GlobalDefines.TITULO_APP);
            dlgObjects.setObjType(objInfo.getObjType());
            dlgObjects.setObjInfo(objInfo);
            dlgObjects.setModal(true);
            dlgObjects.initScrObject(objInfo, this.secInfo, isNew);
            dlgObjects.setLocationRelativeTo(MainApp.getApplication().getMainFrame());
            dlgObjects.setSize(new Dimension(900, 700));
            dlgObjects.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            Utilerias.centreDialog(dlgObjects, true);
            dlgObjects.setVisible(true);
            if (dlgObjects.isAccept()) {
                refreshObjects();
            }
        }
    }

    public void deletObject(int objectId) {
        int result = JOptionPane.showConfirmDialog(null, "¿Remover objeto del editor?", "Remover objeto", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        if (this.secInfo.getLstObjects() == null) {
            return;
        }
        for (ObjectInfoBO objInfo : this.secInfo.getLstObjects()) {
            if (objInfo.getObjectId() == objectId) {
                objInfo.setDelete(true);
                if(objInfo.getOrderId() > 0){
                    for(ObjectInfoBO obj : secInfo.getLstObjects()){
                        if(obj.getOrderId() > objInfo.getOrderId()){
                            obj.setOrderId( (obj.getOrderId() - 1) );
                        }
                    }
                }
                //secInfo.getLstObjects().remove(objInfo);
                this.refreshObjects();
                break;
            }
        }
    }

    protected void addObject(int objectType) {
        Utilerias.pasarGarbageCollector();
        if (objectType == GlobalDefines.OBJ_TYPE_VIDEO) {

//            if (dlgKaltura == null) {
//                try {
//                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//                    dlgKaltura = ScrVideoKaltura.getInstance();
//                } catch (Exception ex) {
//                    Utilerias.logger(getClass()).error(ex);
//                } finally {
//                    setCursor(Cursor.getDefaultCursor());
//                }
//            }
            ObjectInfoBO objInfo = new ObjectInfoBO();
            objInfo.setObjType(GlobalDefines.OBJ_TYPE_VIDEO);
            objInfo.setSectionId(this.secInfo.getSectionId());
            //  objInfo.setTemplateId(this.secInfo.getTemplateId());
            //  objInfo.setSectionRootId(this.secInfo.getSectionIdModule );

            ScrObjVideo objVideo = new ScrObjVideo(MainApp.getApplication().getMainFrame(), true);
            objVideo.initScrObject(objInfo);
            Utilerias.centreDialog(objVideo, true);
            objVideo.setVisible(true);
//            dlgKaltura.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
//            dlgKaltura.setLocationRelativeTo(MainApp.getApplication().getMainFrame());
//            dlgKaltura.setTitle(GlobalDefines.TITULO_APP);
//            dlgKaltura.setModal(true);
//            dlgKaltura.initScrObject(objInfo);
//
//            dlgKaltura.pack();
//            dlgKaltura.setVisible(true);
            if (objVideo.isAccept()) {

                if (proxy) {
                    System.setProperty("http.proxyHost", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_HOST));
                    System.setProperty("http.proxyPort", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_PORT));
                }

                objInfo.setHtmlVideo(objVideo.getHtmlVideo());
                String thumb = objVideo.getThumb();
                BufferedImage thumbBuff = null;
                try {
                    thumbBuff = ImageIO.read(new URL(thumb));
                } catch (IOException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }

                objInfo.setImage(thumbBuff);

                objInfo.setTitulo(objVideo.getTituloVideo());
                objInfo.setSubTitulo(objVideo.getSubtituloVideo());
                objInfo.setComentarios(objVideo.getFuenteVideo());
                objInfo.setIdVideo(objVideo.getId_video());
                objInfo.setIdVideoDb(objVideo.getIdvideodb());
                objInfo.setTipoVideo(objVideo.getTipoVideo());
                
                int orden = 0;
                if(this.secInfo.getLstObjects() != null && this.secInfo.getLstObjects().size() > 0){
                    for(ObjectInfoBO obj : this.secInfo.getLstObjects()){
                        if(obj.getOrderId() > orden){
                            orden = obj.getOrderId();
                        }
                    }
                }
                orden++;
                objInfo.setOrderId(orden);

                this.secInfo.getLstObjects().add(objInfo);
                refreshObjects();

                if (proxy) {
                    System.setProperty("http.proxySet", "false");
                    System.setProperty("http.proxyHost", "");
                    System.setProperty("http.proxyPort", "");
                }
            }

        } else {
            //if (dlgObjects == null) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                dlgObjects = new DlgObjects();
                dlgObjects.addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (dlgObjects.getTabObjects().getSelectedIndex() == 0) {
                            Utilerias.logger(getClass()).info("ObjExcel");
                        } else {
                            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                                dlgObjects.setPaste(true);
                            }
                            if (e.getKeyCode() == KeyEvent.VK_V && dlgObjects.getPaste()) {
                                dlgObjects.getScrObjImage().pasteImage();
                            }
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        ;
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        dlgObjects.setPaste(false);;
                    }

                });
                dlgObjects.setFocusable(true);
            } catch (Exception ex) {
                Utilerias.logger(getClass()).error(ex);
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
            //}

            boolean seAgrega = true;
            Utilerias.centreDialog(dlgObjects, true);
            //dlgObjects.setTitle(GlobalDefines.TITULO_APP);
            dlgObjects.setObjType(objectType);

            ObjectInfoBO objInfo = new ObjectInfoBO();
            if (objectType == GlobalDefines.OBJ_TYPE_IMAGE) {
                objInfo.setObjType(GlobalDefines.OBJ_TYPE_IMAGE);
            } else if (objectType == GlobalDefines.OBJ_TYPE_EXCEL) {
                objInfo.setObjType(GlobalDefines.OBJ_TYPE_EXCEL);
            } else if (objectType == GlobalDefines.OBJ_TYPE_TEXT) {
                seAgrega = false;
            } else if (objectType == GlobalDefines.OBJ_TYPE_CALENDAR) {
                seAgrega = false;
            }

            if (seAgrega) {
                objInfo.setSectionId(this.secInfo.getSectionId());
                // objInfo.setTemplateId(this.secInfo.getTemplateId());
                // objInfo.setSectionRootId(this.secInfo.getSectionIdModule());
                objInfo.setWidth(this.secInfo.getWidth());
                objInfo.setHeight(this.secInfo.getHeight());
                Boolean isNew = Boolean.TRUE;
                dlgObjects.setModal(true);
                dlgObjects.initScrObject(objInfo, this.secInfo, isNew);
                dlgObjects.resetScrObject();
                dlgObjects.setSize(new Dimension(900, 700));
                dlgObjects.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
                dlgObjects.setLocationRelativeTo(MainApp.getApplication().getMainFrame());
                dlgObjects.setVisible(true);
                if (dlgObjects.isAccept()) {
                    int orden = 0;
                    if(this.secInfo.getLstObjects() != null && this.secInfo.getLstObjects().size() > 0){
                        for(ObjectInfoBO obj : this.secInfo.getLstObjects()){
                            if(obj.getOrderId() > orden){
                                orden = obj.getOrderId();
                            }
                        }
                    }
                    orden++;
                    objInfo.setOrderId(orden);
                    this.secInfo.getLstObjects().add(objInfo);
                    refreshObjects();
                }
            }
        }
    }
    
    private List<ObjectInfoBO> orderLstObjectsByOrderId(){
        List<ObjectInfoBO> retVal = new ArrayList<>();
        
        try {
            for (final ObjectInfoBO objInfo : secInfo.getLstObjects()) {

                if (objInfo.isDelete()) {
                    continue;
                }
                
                retVal.add(objInfo);
            }
            
            Collections.sort(retVal, new Comparator<ObjectInfoBO>(){

                @Override
                public int compare(ObjectInfoBO o1, ObjectInfoBO o2) {
                    return new Integer(o1.getOrderId()).compareTo(o2.getOrderId());
                }
            });
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        return retVal;
    }

    protected void refreshObjects() {
        boolean seAgrega = true;
        this.pObj.removeAll();
        JButton butt = new JButton("");
        ScrObjIcon scrObjIcon = null;
        for (final ObjectInfoBO objInfo : orderLstObjectsByOrderId()) {

            if (objInfo.isDelete()) {
                continue;
            }
            butt = new JButton();
            scrObjIcon = new ScrObjIcon(this, objInfo);
            butt.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //Execute when button is pressed
                    updateObject(objInfo);
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
                int countIndex = 0;
                if(objInfo.getOrderId() > 0){
                    countIndex = objInfo.getOrderId();
                }else{
                    countIndex = this.pObj == null ? 0 : this.pObj.getComponentCount();
                    countIndex++;
                }
                scrObjIcon.setPosition(countIndex);
                this.pObj.add(scrObjIcon);
            }
            this.pObj.revalidate();
            this.pObj.repaint();
        }
        this.pObj.revalidate();
        this.pObj.repaint();
    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // Check the drop action
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            // Accept the drop and get the transfer data
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();
            try {
                boolean result = dropComponent(transferable);
                dtde.dropComplete(result);
            } catch (IOException | UnsupportedFlavorException e) {
                Utilerias.logger(getClass()).error(e);
                dtde.dropComplete(false);
            }
        } else {
            dtde.rejectDrop();
        }
    }

    // Internal methods start here
    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = false;
        //return true;

        // Reject if the object being transferred
        // or the operations available are not acceptable.
        if (!acceptableType
                || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            dtde.rejectDrag();
        } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            // Not offering copy or move - suggest a copy
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            acceptedDrag = true;
        } else {
            // Offering an acceptable operation: accept
            dtde.acceptDrag(dropAction);
            acceptedDrag = true;
        }
        return acceptedDrag;
    }

    protected void checkTransferType(DropTargetDragEvent dtde) {
        // Only accept a flavor that returns a Component
        acceptableType = false;
        DataFlavor[] fl = dtde.getCurrentDataFlavors();
        for (int i = 0; i < fl.length; i++) {
            Class dataClass = fl[i].getRepresentationClass();

            if (Component.class.isAssignableFrom(dataClass)) {
                // This flavor returns a Component - accept it.
                targetFlavor = fl[i];
                acceptableType = true;
                break;
            }
        }
    }

    protected boolean dropComponent(Transferable transferable)
            throws IOException, UnsupportedFlavorException {
        Object o = transferable.getTransferData(targetFlavor);
        if (o instanceof Component) {
            this.add((Component) o);
            this.validate();
            return true;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popSection = new javax.swing.JPopupMenu();
        popObject = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        panTools = new javax.swing.JPanel();
        toolBarButtons = new javax.swing.JToolBar();
        panObjects = new javax.swing.JPanel();
        jsPanel = new javax.swing.JScrollPane();
        pObj = new javax.swing.JPanel();

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        popObject.add(jMenuItem1);

        jMenu1.setText("jMenu1");
        popObject.add(jMenu1);

        jMenuItem2.setText("jMenuItem2");
        popObject.add(jMenuItem2);

        jMenuItem3.setText("jMenuItem3");

        setMaximumSize(new java.awt.Dimension(2147483647, 50));
        setPreferredSize(new java.awt.Dimension(100, 50));
        setLayout(new java.awt.BorderLayout());

        panTools.setBackground(new java.awt.Color(255, 255, 255));
        panTools.setMaximumSize(new java.awt.Dimension(32767, 50));
        panTools.setMinimumSize(new java.awt.Dimension(10, 25));
        panTools.setPreferredSize(new java.awt.Dimension(100, 25));
        panTools.setLayout(new java.awt.BorderLayout());

        toolBarButtons.setFloatable(false);
        toolBarButtons.setRollover(true);
        panTools.add(toolBarButtons, java.awt.BorderLayout.CENTER);

        add(panTools, java.awt.BorderLayout.NORTH);

        panObjects.setBackground(new java.awt.Color(255, 255, 255));
        panObjects.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        panObjects.setAutoscrolls(true);
        panObjects.setMinimumSize(new java.awt.Dimension(12, 100));
        panObjects.setPreferredSize(new java.awt.Dimension(12, 100));
        panObjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panObjectsMouseReleased(evt);
            }
        });
        panObjects.setLayout(new java.awt.BorderLayout());

        jsPanel.setAutoscrolls(true);

        pObj.setBackground(new java.awt.Color(255, 255, 255));
        pObj.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pObjMouseReleased(evt);
            }
        });
        jsPanel.setViewportView(pObj);

        panObjects.add(jsPanel, java.awt.BorderLayout.CENTER);

        add(panObjects, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void panObjectsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panObjectsMouseReleased
        if (evt.isPopupTrigger()) {
            popSection.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_panObjectsMouseReleased

    private void pObjMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pObjMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            popSection.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_pObjMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jsPanel;
    private javax.swing.JPanel pObj;
    private javax.swing.JPanel panObjects;
    private javax.swing.JPanel panTools;
    private javax.swing.JPopupMenu popObject;
    private javax.swing.JPopupMenu popSection;
    private javax.swing.JToolBar toolBarButtons;
    // End of variables declaration//GEN-END:variables

}
