/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.designer;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.ThumbTemplate;
import com.adinfi.formateador.util.Utilerias;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;

/**
 *
 * @author USUARIO
 */
public final class CellPanel extends javax.swing.JPanel {

    private DrawnItem drawnItem;
    private List<DrawnItem> lstDrawnItems;
    private int numCol;
    private int refCol;
    private int WIDTH_COL;
    private int maxHeightMod;
    private int cdRow;
    private int cdCol;
    private List<ModuleSectionBO> lstModuleSection;
    private List<DrawnItem> lstDIColumns;
    private static final int MAX_WIDTHS = 100;
    private Object[][] data = null;
    private static int DPI=72;
    private boolean showContexMenu=false;
    private boolean editMode=false;
    private boolean viewEditMsg=false;
    private Designer view;
    public int IdModuloCreado;
    public int HEIGHT_MOD_PLAN;
    private int colMax;
    private int cm;

    private String[] columnNames = new String[]{
        "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
    };

    //private MyTableCellRenderer myCellRenderer = null;
    /**
     * Creates new form CellPanel
     */
    public CellPanel(int centimeters, Designer view) {
        initComponents();
        this.view=view;
        cm=centimeters;
        setTableModel(centimeters);
        setConfigurationTable();

    }


    
    public void setShowContexMenu(boolean value){
        this.showContexMenu = value;
    }
    
    public void setEditMode(boolean value){
        this.editMode = value;
    }
    
    public void setViewEditMsg(boolean value){
        this.viewEditMsg = value;
    }
    
    public boolean getViewEditMsg(){
        return viewEditMsg;
    }

    protected void setTableModel(int centimeters) {
        //instance table model

        //create DataSet from input centimeters
        data = new Object[centimeters * 2][columnNames.length];

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        table.setModel(tableModel);

    }

    protected void setConfigurationTable() {
        CustomRenderer cr = new CustomRenderer();
        cr.tab = table;

        table.setTableHeader(null);
        table.setCellSelectionEnabled(true);
        //AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.FOCUS_LOOP_ANIMATION, table);
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.SELECTION, table);
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, table);
        //AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ICON_GLOW, table);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        try {
            int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
            int units = ((int) ((double) INCH / (double) 2.54)); // dots per centimeter
            //List<Integer> widths = getWidths(units);
            for (int i = 1; i < table.getColumnCount() + 1; i++) {
                table.getColumnModel().getColumn(i - 1).setPreferredWidth((i % 2 == 0) ? 19 : 18);
                table.getColumnModel().getColumn(i - 1).setCellRenderer(cr);
            }
            for (int i = 1; i < table.getRowCount(); i++) {
                table.setRowHeight(i - 1, (i % 2 == 0) ? 19 : 18);
            }
        } catch (HeadlessException e) {
        }

//        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent event) {
//                if (table.getSelectedRow() > -1) {
//                    // print first column value from selected row
//                    System.out.println(table.getValueAt(table.getSelectedRow(), 0));
//                }
//            }
//        });
//        table.setCellSelectionEnabled(true);
//        table.setColumnSelectionAllowed(true);
//        table.setRowSelectionAllowed(true);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    protected List<Integer> getWidths(int units) {
        List<Integer> widths = new ArrayList<>();
        int middleUnits = units / 2;
        widths.add(1);
        widths.add(18);
        widths.add(37);
        for (int i = 4; i < MAX_WIDTHS; i++) {
            widths.add((i % 2 == 0) ? (middleUnits + widths.get(i - 2)) : (widths.get(i - 3) + units));
        }
        return widths;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        table = new javax.swing.JTable();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jMenuItem1.setText("Eliminar componente");
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenu1.setText("Cambiar componente");
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem2.setText("Cambiar a texto");
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Cambiar a multimedia");
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Cambiar a bullet");
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jPopupMenu1.add(jMenu1);

        setLayout(new java.awt.BorderLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
            }
        ));
        table.setDoubleBuffered(true);
        table.setName("table"); // NOI18N
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableMouseReleased(evt);
            }
        });
        add(table, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            cdCol = table.columnAtPoint(evt.getPoint());
            cdRow = table.rowAtPoint(evt.getPoint());
            //System.out.println("Right Click");
            //System.out.println(cdRow + "-" + cdCol);
            
            if(showContexMenu)
                jPopupMenu1.show(table, evt.getX(), evt.getY());
        }else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1){
            if(view!=null){
                view.genPreview();
            }
        }
    }//GEN-LAST:event_tableMouseClicked

    private void tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMousePressed
        // TODO add your handling code here:
        mousePressed(evt);
    }//GEN-LAST:event_tableMousePressed

    private void tableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseReleased
        // TODO add your handling code here:
        mouseReleased(evt);
    }//GEN-LAST:event_tableMouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        eliminarComponente();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        cambiarComponenteTexto();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        cambiarComponenteMultimedia();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        cambiarComponenteBullet();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    void cambiarComponenteTexto(){
        if(lstDrawnItems != null && lstDrawnItems.size() > 0){
            //lstDrawnItems.remove(lstDrawnItems.size()-1);
            for(int i=0; i<lstDrawnItems.size(); i++){
                if(cdRow >= lstDrawnItems.get(i).getRowPress() && cdRow <= lstDrawnItems.get(i).getRowRel()
                   && cdCol >= lstDrawnItems.get(i).getColPress() && cdCol <= lstDrawnItems.get(i).getColRel()){
                    lstDrawnItems.get(i).setTipo(Designer.SELECTED_COMPONENT.TEXT);
//                    table.repaint();
//                    table.revalidate();
//                    table.updateUI();
                    if(editMode){
                        editMode=false;
                        viewEditMsg=true;
                    }
                    if(view!=null){
                        view.genPreview();
                    }
                }
            }
        }
    }
    
    void cambiarComponenteMultimedia(){
        if(lstDrawnItems != null && lstDrawnItems.size() > 0){
            //lstDrawnItems.remove(lstDrawnItems.size()-1);
            for(int i=0; i<lstDrawnItems.size(); i++){
                if(cdRow >= lstDrawnItems.get(i).getRowPress() && cdRow <= lstDrawnItems.get(i).getRowRel()
                   && cdCol >= lstDrawnItems.get(i).getColPress() && cdCol <= lstDrawnItems.get(i).getColRel()){
                    lstDrawnItems.get(i).setTipo(Designer.SELECTED_COMPONENT.MULTIMEDIA);
//                    table.repaint();
//                    table.revalidate();
//                    table.updateUI();
                    if(editMode){
                        editMode=false;
                        viewEditMsg=true;
                    }
                    if(view!=null){
                        view.genPreview();
                    }
                }
            }
        }
    }
    
    void cambiarComponenteBullet(){
        if(lstDrawnItems != null && lstDrawnItems.size() > 0){
            //lstDrawnItems.remove(lstDrawnItems.size()-1);
            for(int i=0; i<lstDrawnItems.size(); i++){
                if(cdRow >= lstDrawnItems.get(i).getRowPress() && cdRow <= lstDrawnItems.get(i).getRowRel()
                   && cdCol >= lstDrawnItems.get(i).getColPress() && cdCol <= lstDrawnItems.get(i).getColRel()){
                    lstDrawnItems.get(i).setTipo(Designer.SELECTED_COMPONENT.BULLET);
                    if(editMode){
                        editMode=false;
                        viewEditMsg=true;
                    }
                    if(view!=null){
                        view.genPreview();
                    }
                }
            }
        }
    }
    
    void eliminarComponente(){      
        if(lstDrawnItems != null && lstDrawnItems.size() > 0){
            int result = JOptionPane.showConfirmDialog(null, "Se va eliminar el componente seleccionado. ¿Esta seguro de continuar?.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                //lstDrawnItems.remove(lstDrawnItems.size()-1);
                for(int i=0; i<lstDrawnItems.size(); i++){
                    if(cdRow >= lstDrawnItems.get(i).getRowPress() && cdRow <= lstDrawnItems.get(i).getRowRel()
                       && cdCol >= lstDrawnItems.get(i).getColPress() && cdCol <= lstDrawnItems.get(i).getColRel()){
                        lstDrawnItems.remove(i);
                        //table.repaint();
                        //this.revalidate();
                        //this.updateUI();
                        if(editMode){
                            editMode=false;
                            viewEditMsg=true;
                        }
                        if(view!=null){
                            view.genPreview();
                        }
                    }
                }
            }
        }
    }
    
    void mousePressed(MouseEvent evt){
        int column = table.columnAtPoint(evt.getPoint());
        int row = table.rowAtPoint(evt.getPoint());
        
        drawnItem = new DrawnItem();
        drawnItem.setTipo(Designer.getSelectedComponent());
        drawnItem.setRowPress(row);
        drawnItem.setColPress(column);
        
        //System.out.print(" MousePressRow: " + row);
        //System.out.println(" MousePressCol: " + column);
    }
    
    void mouseReleased(MouseEvent evt){
        int column = table.columnAtPoint(evt.getPoint());
        int row = table.rowAtPoint(evt.getPoint());
       
        //if(drawnItem.getRowPress() != row && drawnItem.getColPress() != column){
        if(row > drawnItem.getRowPress() && column > drawnItem.getColPress() && showContexMenu){
            
            drawnItem.setRowRel(row);
            drawnItem.setColRel(column);
            
            String resVal = validComponentInput();
            if(resVal != null){
                JOptionPane.showMessageDialog(null, resVal);
                return;
            }
            
            if(lstDrawnItems == null)
                lstDrawnItems = new ArrayList<>();
            
            DrawnItem di = drawnItem;
            lstDrawnItems.add(di);
            
            if(editMode){
                editMode=false;
                viewEditMsg=true;
            }
            if(view!=null){
                view.genPreview();
            }
        }else if(column < 0 || row < 0){
            JOptionPane.showMessageDialog(null, "Debe terminar la selección del componente en el área de la cuadricula.");
            return;
        }
        
        //System.out.print(" MouseRelRow: " + row);
        //System.out.println(" MouseRelCol: " + column);

        
    }
    
    private String validComponentInput(){
        if(lstDrawnItems == null || lstDrawnItems.size() <= 0)
            return null;
        
        for (DrawnItem di : lstDrawnItems) {
            String res=null;

            res = esquinaDeComponenteDentroDeComponente(di, drawnItem);
            if (res != null)
                return res;

            res = esquinaDeComponenteDentroDeComponente(drawnItem, di);
            if (res != null)
                return res;

            DrawnItem diInv = new DrawnItem();
            diInv.setRowPress(drawnItem.getRowPress());
            diInv.setColPress(drawnItem.getColRel());
            diInv.setRowRel(drawnItem.getRowRel());
            diInv.setColRel(drawnItem.getColPress());
            res = esquinaDeComponenteDentroDeComponente(di, diInv);
            if (res != null)
                return res;
        }
        
        return null;
    }
    
    private String esquinaDeComponenteDentroDeComponente(DrawnItem c, DrawnItem i){
        if(c.getColPress() <= i.getColPress() && c.getColRel() >= i.getColPress() &&
           c.getRowPress() <= i.getRowPress() && c.getRowRel() >= i.getRowPress()){
           return "No se pueden empalmar los componentes.";
        }else if(c.getColPress() <= i.getColRel() && c.getColRel() >= i.getColRel() &&
                 c.getRowPress() <= i.getRowRel() && c.getRowRel() >= i.getRowRel()){
            return "No se pueden empalmar los componentes.";
        }
        return null;
    }
    
    public void deshacer(){
        if(lstDrawnItems != null && lstDrawnItems.size() > 0)
            lstDrawnItems.remove(lstDrawnItems.size()-1);

    }
    
    public void genThumbs(){
        TemplateDAO templateDAO=new TemplateDAO(); 
        ModuleBO moduleBO=templateDAO.getModule(27);
        
        short hres=30;
        if(moduleBO.getHeight() > 200){
            hres=90;
        }
        
        ThumbTemplate thTemp=new ThumbTemplate();
        thTemp.genThumbModule(moduleBO.getModuleId(), (short)90, hres, "");
        
        thTemp.genThumbModule(moduleBO.getModuleId(), moduleBO.getWidth(), moduleBO.getHeight(), "_B");
    }
    
    boolean grabarModulo(String moduleName){
        ModuleBO mbo = new ModuleBO();
        mbo.setName(moduleName);
        
        //lista de secciones
        List<ModuleSectionBO> lstmsBO = new ArrayList<>();
        //agregar el C
        for (ModuleSectionBO ms: lstModuleSection){
            if("C".equals(ms.getTipo())){
                ModuleSectionBO msBO = new ModuleSectionBO();
                msBO.setTipo(ms.getTipo());
                msBO.setHeight(ms.getHeight());
                
                mbo.setHeight(ms.getHeight());
                if(ms.getWidth() > GlobalDefines.DRAWABLE_WIDTH){
                    mbo.setWidth(GlobalDefines.DRAWABLE_WIDTH);
                    msBO.setWidth(GlobalDefines.DRAWABLE_WIDTH);
                }else{
                    mbo.setWidth(ms.getWidth());
                    msBO.setWidth(ms.getWidth());
                }
                
                lstmsBO.add(msBO);
                break;
            }
        }
        
        //agregar el R
        short totwidth = 0;
        short totheight = 0;
        for (ModuleSectionBO ms: lstModuleSection){
            if("R".equals(ms.getTipo())){
                ModuleSectionBO msBO = new ModuleSectionBO();
                msBO.setTipo(ms.getTipo());
                msBO.setWidth(ms.getWidth());
                msBO.setHeight(ms.getHeight());
                
                lstmsBO.add(msBO);
                totwidth += msBO.getWidth();
                if(msBO.getHeight() > totheight)
                    totheight = msBO.getHeight();
                //agregar el N
                for (ModuleSectionBO msn: lstModuleSection){
                    if("N".equals(msn.getTipo()) && ms.getSectionId()==msn.getSectionParentId()){
                        ModuleSectionBO msBON = new ModuleSectionBO();
                        msBON.setTipo(msn.getTipo());
                        msBON.setContentType(msn.getContentType());
                        msBON.setWidth(msn.getWidth());
                        msBON.setHeight(msn.getHeight());
                        msBON.setInitCoor(msn.getInitCoor());
                        msBON.setEndCoor(msn.getEndCoor());

                        lstmsBO.add(msBON);
                    }
                }
            }
        }
        
        //Eliminacion de excesos de tamaño
        if(totwidth > GlobalDefines.DRAWABLE_WIDTH || totheight > GlobalDefines.DRAWABLE_HEIGHT || totheight > HEIGHT_MOD_PLAN){
            short maxheight = 0;
            
            if(HEIGHT_MOD_PLAN > 0 && totheight > HEIGHT_MOD_PLAN){
                maxheight = (short) HEIGHT_MOD_PLAN;
            }else{
                maxheight = (short) GlobalDefines.DRAWABLE_HEIGHT;
            }
            short maxwidth = (short) GlobalDefines.DRAWABLE_WIDTH;
            
            short dif_w = (short) (totwidth - maxwidth);
            short dif_h = 0;
            if(totheight > maxheight)
                dif_h = (short) (totheight - maxheight);
            
            int numR = 0;
            for (ModuleSectionBO ms: lstmsBO){
                if("R".equals(ms.getTipo())){
                    numR++;
                    int contR = 0;
                    boolean afectaNHeight = false;
                    for (ModuleSectionBO msn: lstmsBO){
                        if( contR == numR && "N".equals(msn.getTipo()) ){
                            short reswidth = (short) (msn.getWidth() - dif_w);
                            msn.setWidth(reswidth);
                            
                            if(afectaNHeight == false){
                                short resheight = (short) (msn.getHeight() - dif_h);
                                msn.setHeight(resheight);
                                afectaNHeight = true;
                            }
                            
                        }else if("R".equals(msn.getTipo())){
                            contR++;
                        }
                        
                        if(contR > numR)
                            break;
                    }
                    short reswidth = (short) (ms.getWidth() - dif_w);
                    short resheight = (short) (ms.getHeight() - dif_h);
                    ms.setWidth(reswidth);
                    ms.setHeight(resheight);

                }/*else if("C".equals(ms.getTipo())){
                    //short reswidth = (short) (ms.getWidth() - dif_w);
                    short resheight = (short) (ms.getHeight() - dif_h);
                    //ms.setWidth(reswidth);
                    ms.setHeight(resheight);
                    mbo.setHeight(resheight);
                }*/
            }
        }
        
        short maxH = 0;
        for (ModuleSectionBO ms: lstmsBO){
            if("R".equals(ms.getTipo())){
                if(ms.getHeight() > maxH){
                    maxH = ms.getHeight();
                }
            }
        }
        lstmsBO.get(0).setHeight(maxH);
        mbo.setHeight(maxH);
        
        mbo.setLstSectionsText(lstmsBO);
        
        TemplateDAO tempDAO = new TemplateDAO();
        List<String> res=tempDAO.addModuleJLGB(mbo);
        
        if(res!=null && res.size()>0){
            JOptionPane.showMessageDialog(null, res.get(0));
            IdModuloCreado=Integer.parseInt(res.get(1));
        }else{
            JOptionPane.showMessageDialog(null, "Ocurrio un error inesperado al guardar.");
        }
        return true;
    }
    
    public boolean validateModule (String moduleName, int modId){
        //validar que se llene el grid con componentes
        if(lstDrawnItems == null){
            JOptionPane.showMessageDialog(null, "El Modulo no tiene componentes");
            return false;
        }
        
        if(editMode){
            TemplateDAO templateDAO=new TemplateDAO();
            if(templateDAO.editModule(modId, moduleName)){
                JOptionPane.showMessageDialog(null, "El Módulo se guardo satisfactoriamente.");
            }else{
                JOptionPane.showMessageDialog(null, "Ocurrió un error al actualizar el Módulo.");
            }
        }else{

            lstDIColumns = new ArrayList<DrawnItem>();
            numCol=0;
            refCol=0;

            lstModuleSection = new ArrayList<ModuleSectionBO>();
            if(validateColumnIntegrator( 0, 0)){

                System.out.println("Tipo=C, Width="+(table.getColumnCount())*(0.5)+", Height="+(table.getRowCount())*(0.5));
                double w = (table.getColumnCount())*(0.5); 
                w = Math.round( ((w*10)*(DPI))/25.4);

                //alto del modulo
                int hMod = 0;
                for (DrawnItem di : lstDrawnItems){
                    if(di.getColPress() == 0 && di.getRowRel() > hMod){
                        hMod = di.getRowRel();
                    }
                }

                double h = (hMod+1)*(0.5);
                h = Math.round( ((h*10)*(DPI))/25.4);
                ModuleSectionBO msBOC = new ModuleSectionBO();
                msBOC.setTipo("C");
                int wi=(int) w;
                int hi=(int) h;
                if(wi > GlobalDefines.DRAWABLE_WIDTH){
                    wi = GlobalDefines.DRAWABLE_WIDTH;
                }
                msBOC.setWidth(Short.parseShort(String.valueOf(wi)));
                msBOC.setHeight(Short.parseShort(String.valueOf(hi)));
                lstModuleSection.add(msBOC);

                for (int i = 0; i <= numCol; i++) {
                    double totHeight=0;
                    double totWidth=0;
                    for (DrawnItem di : lstDIColumns){
                        if(di.getNumCol() == i){
                            double width=((di.getColRel()-di.getColPress()+1)*(0.5));
                            double height=((di.getRowRel()-di.getRowPress()+1)*(0.5));

                            width=Math.round( ((width*10)*(DPI))/25.4);
                            height=Math.round( ((height*10)*(DPI))/25.4);
                            totHeight+=height;
                            totWidth=width;

                            System.out.println("Tipo=N, heiht=("+di.getRowPress()+"-"+di.getRowRel()+")"+height+", width=("+di.getColPress()+"-"+di.getColRel()+")"+width);
                            ModuleSectionBO msBON = new ModuleSectionBO();
                            msBON.setTipo("N");
                            int win=(int) width;
                            int hin=(int) height;
                            msBON.setWidth(Short.parseShort(String.valueOf(win)));
                            msBON.setHeight(Short.parseShort(String.valueOf(hin)));

                            if(di.getTipo() == Designer.SELECTED_COMPONENT.BULLET)
                                msBON.setContentType("B");
                            else if (di.getTipo() == Designer.SELECTED_COMPONENT.TEXT)
                                msBON.setContentType("T");

                            msBON.setSectionParentId(i);
                            msBON.setInitCoor(di.getRowPress() + "," + di.getColPress());
                            msBON.setEndCoor(di.getRowRel() + "," + di.getColRel());

                            lstModuleSection.add(msBON);
                        }
                    }
                    System.out.println(" Col "+i+" Tipo=R height="+totHeight+" width="+totWidth );
                    System.out.println("---------------------------------------");

                    ModuleSectionBO msBOR = new ModuleSectionBO();
                    msBOR.setTipo("R");
                    int wir=(int) totWidth;
                    int hir=(int) totHeight;
                    msBOR.setWidth(Short.parseShort( String.valueOf(wir)));
                    msBOR.setHeight(Short.parseShort( String.valueOf(hir)));

                    msBOR.setSectionId(i);

                    lstModuleSection.add(msBOR);
                }  

                grabarModulo(moduleName);
            }else{
                return false;
            }
        }
        
        return true;
    }
    
    boolean validateColumnIntegrator(int row, int col){
                
        //Encontrar elemento inicial de columna
        DrawnItem diMasterColumn = null;
        for (DrawnItem di : lstDrawnItems){
            if(di.getRowPress() == row && di.getColPress() == col){
                diMasterColumn = di;
                break;
            }
        }
        
        //validar si se encontro el elemento
        if(diMasterColumn == null){
            JOptionPane.showMessageDialog(null, "El Módulo esta mal formado. Los nodos no deben tener espacios en blanco entre si,\nlas columnas de nodos formadas deben contemplar todo el alto del modulo y la \nforma final del modulo debe ser rectangular.");
            return false;
        }
              
        //Obtener alto maximo del modulo con respecto a la primera columna
        if(row == 0 && col == 0){
            maxHeightMod=0;
            Set<String> totCol = new HashSet<>();
            for (DrawnItem di : lstDrawnItems){
                if(di.getColPress() == col && di.getRowRel() > maxHeightMod){
                    maxHeightMod = di.getRowRel();
                }
                
                totCol.add(String.valueOf(di.getColPress()));

            }
            WIDTH_COL = diMasterColumn.getColRel() - diMasterColumn.getColPress();
            refCol=numCol;
            
            colMax=-1;
            for (String s: totCol){
                colMax++;
            }
        }else if( diMasterColumn.getRowRel() > maxHeightMod ){
            JOptionPane.showMessageDialog(null, "El Módulo esta mal formado. Las columnas de nodos formadas deben contemplar todo el alto del modulo \ny la forma final del modulo debe ser rectangular.");
            return false;
        }
        
        if(refCol!=numCol){
            WIDTH_COL = diMasterColumn.getColRel() - diMasterColumn.getColPress();
            refCol=numCol;
        }
        
        //validar si el elemento maestro contempla todo el ancho del moculo
        if( diMasterColumn.getRowRel() == maxHeightMod ){
            //evaluar si faltan espacios por validar
            //evalua si llego al final del width y del height asi como si es parte de la ultima columna
            if(diMasterColumn.getColRel() == (table.getColumnCount()-1) 
                    && (diMasterColumn.getColRel() - diMasterColumn.getColPress()) == WIDTH_COL
                    && numCol == colMax){
                //respaldar con la columna respectiva
                diMasterColumn.setNumCol(numCol);
                DrawnItem di = diMasterColumn;
                lstDIColumns.add(di);
                
                //JOptionPane.showMessageDialog(this, "El Modulo es Valido");
                return true;
            }else{
                //respaldar con la columna respectiva
                diMasterColumn.setNumCol(numCol);
                DrawnItem di = diMasterColumn;
                lstDIColumns.add(di);
                
                numCol++;
                //moverse a la siguiente columna
                return validateColumnIntegrator( 0, (diMasterColumn.getColRel() + 1) );
            }
        }else{
            //respaldar con la columna respectiva
            diMasterColumn.setNumCol(numCol);
            DrawnItem di = diMasterColumn;
            lstDIColumns.add(di);
            
            //moverse al siguiente componente en la columna
            return validateColumnIntegrator( (diMasterColumn.getRowRel() + 1), diMasterColumn.getColPress());
        }
    }
    
    public void viewModule(List<ModuleSectionBO> nodes){
        lstDrawnItems = new ArrayList<>();
        try {
            for (ModuleSectionBO mBO : nodes){
                for (ModuleSectionBO msBO : mBO.getChildSectionsAsList()){
                    if("N".equals(msBO.getType())){
                        String[] initCoor = msBO.getInitCoor().split(",");
                        String[] endCoor = msBO.getEndCoor().split(",");

                        DrawnItem di=new DrawnItem();
                        di.setRowPress(Integer.parseInt(initCoor[0]));
                        di.setColPress(Integer.parseInt(initCoor[1]));
                        di.setRowRel(Integer.parseInt(endCoor[0]));
                        di.setColRel(Integer.parseInt(endCoor[1]));

                        if(msBO.getContentType() == null)
                            di.setTipo(Designer.SELECTED_COMPONENT.MULTIMEDIA);
                        else if("T".equals(msBO.getContentType()))
                            di.setTipo(Designer.SELECTED_COMPONENT.TEXT);
                        else
                            di.setTipo(Designer.SELECTED_COMPONENT.BULLET);

                        lstDrawnItems.add(di);
                    }
                }
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    
    class CustomRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 6703872492730589499L;
        public String Val1;
        public JTable tab;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (hasFocus) {
                setBackground(Color.BLACK);
                //System.out.print(" Row: " + row);
                //System.out.println(" Col: " + column);
                //setBorder(new LineBorder(Color.RED));
            } else if (isSelected) {
                Color color = table.getBackground();
                Designer.SELECTED_COMPONENT selected = Designer.getSelectedComponent();
                switch (selected) {
                    case BULLET:
                        color = new java.awt.Color(35, 81, 130);
                        break;

                    case MULTIMEDIA:
                        color = new java.awt.Color(254, 106, 0);
                        break;

                    case TEXT:
                        color = new java.awt.Color(202, 201, 198);
                        break;
                }

                setBackground(color);

            } else {
                setBackground(table.getBackground());
            }
            
            if(lstDrawnItems != null){
                boolean pintar = false;

                for (DrawnItem dItem : lstDrawnItems){
                  if(dItem.getRowPress() < dItem.getRowRel()){//hacia abajo
                      if(dItem.getColPress() < dItem.getColRel()){//hacia enfrente

                          if ( row >= dItem.getRowPress() && row <= dItem.getRowRel() &&
                               column >= dItem.getColPress() && column <= dItem.getColRel() ){
                             pintar = true; 
                          }

                      }else{//hacia atras

                          if ( row >= dItem.getRowPress() && row <= dItem.getRowRel() &&
                               column <= dItem.getColPress() && column >= dItem.getColRel() ){
                             pintar = true; 
                          }

                      }
                  }else{//hacia arriba
                      if(dItem.getColPress() < dItem.getColRel()){//hacia enfrente

                          if ( row <= dItem.getRowPress() && row >= dItem.getRowRel() &&
                               column <= dItem.getColPress() && column >= dItem.getColRel() ){
                             pintar = true; 
                          }

                      }else{//hacia atras

                          if ( row <= dItem.getRowPress() && row >= dItem.getRowRel() &&
                               column <= dItem.getColPress() && column >= dItem.getColRel() ){
                             pintar = true; 
                          }

                      }
                  }
                  
                  if ( pintar ){
                        Color color = Color.PINK;
                        Designer.SELECTED_COMPONENT selected = dItem.getTipo();
                        switch (selected) {
                              case BULLET:
                                  color = new java.awt.Color(35, 81, 130);
                                  break;

                              case MULTIMEDIA:
                                  color = new java.awt.Color(254, 106, 0);
                                  break;

                              case TEXT:
                                  color = new java.awt.Color(202, 201, 198);
                                  break;
                        }
                        this.setOpaque(true);
                        this.setBackground(color);
                        this.setForeground(color);
                  } else {
                       // Restaurar los valores por defecto
                  }
                  pintar=false;
                }

                
            }else{
                
            }
            return this;
        }
    }
    
    class DrawnItem{
        private Designer.SELECTED_COMPONENT tipo;
        private int rowPress;
        private int colPress;
        private int rowRel;
        private int colRel;
        private int numCol;
        
        public void setTipo(Designer.SELECTED_COMPONENT value){this.tipo = value;}
        public Designer.SELECTED_COMPONENT getTipo(){return this.tipo;}
        
        public void setRowPress(int value){this.rowPress = value;}
        public int getRowPress(){return this.rowPress;}
        
        public void setColPress(int value){this.colPress = value;}
        public int getColPress(){return this.colPress;}
        
        public void setRowRel(int value){this.rowRel = value;}
        public int getRowRel(){return this.rowRel;}
        
        public void setColRel(int value){this.colRel = value;}
        public int getColRel(){return this.colRel;}
        
        public void setNumCol(int value){this.numCol = value;}
        public int getNumCol(){return this.numCol;}
    }
}
