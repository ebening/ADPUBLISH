/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.designer;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public final class CellPanelTemplate extends javax.swing.JPanel {

    private final int HEIGHT_MODEL_PX = GlobalDefines.DRAWABLE_HEIGHT;
    private short coor_y = 0;
    public List<ModuleBO> lstModule;
    public boolean showContexMenu;
    public boolean editMode;
    public boolean viewEditMsj;
    public int widthTemplateDesigner;
    public int heightTemplateDesigner;
    
    public CellPanelTemplate(int idModule) {
        initComponents();
        designerPanelTamplate.setLayout(new BoxLayout(designerPanelTamplate, BoxLayout.Y_AXIS));
        showContexMenu = false;
        editMode = false;
        viewEditMsj = false;
    }

    public void addModuleTemplate(int idModule, int size) {
        
        JPanel panel = new JPanel(new BorderLayout());

        if (size <= 0) {
            TemplateDAO tempDAO = new TemplateDAO();
            ModuleBO moduleBO = tempDAO.getModule(idModule);
            if(lstModule == null){
                lstModule = new ArrayList<>();
            }
            
            lstModule.add(moduleBO);
            
            String fileName
                    = new StringBuilder(Utilerias.getFilePath(Utilerias.PATH_TYPE.THUMB_MODULE_DIR))
                    .append(idModule)
                    .append("_B")
                    .append(".")
                    .append(GlobalDefines.png).toString();

            panel.add(new JLabel(new ImageIcon(fileName)), BorderLayout.CENTER);
            panel.setBounds(new Rectangle(100, 100));

            panel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
            designerPanelTamplate.add(panel);

        } else if (size > 0) {
            ModuleBO moduleBO = new ModuleBO();
            moduleBO.setHeight((short)size);
            moduleBO.setName("Titulo");
            moduleBO.setModuleId(0);
            if(lstModule == null){
                lstModule = new ArrayList<>();
            }
            
            JPanel pan = new JPanel() {
                @Override
                public void paint(Graphics g) {
                    int w = getWidth();
                    int h = getHeight();


                    //g.drawLine(0, h-10, w, h-5);
                    g.fillRect(0, h-10, w, 10);
                }
            };

            pan.setSize(new Dimension(580,size));
            pan.setPreferredSize(new Dimension(580,size));
            pan.setBackground(Color.GREEN);
            
            if(lstModule == null)
                lstModule = new ArrayList<ModuleBO>();
            
            if(lstModule.size() > 0){
                List<ModuleBO> modClone = lstModule;
                removeAllModules();
                designerPanelTamplate.add(pan);
                lstModule.add(moduleBO);
                //lstModule.addAll(modClone);
                for(ModuleBO mod: modClone){
                    if(mod.getModuleId() > 0){
                        addModuleTemplate(mod.getModuleId(), 0);
                    }
                }
            }else{
                lstModule.add(moduleBO);
                designerPanelTamplate.add(pan);
            }
            
        }

        designerPanelTamplate.validate();
        designerPanelTamplate.updateUI();
    }

    public void removeModules() {
        if (designerPanelTamplate.getComponents() != null) {
            int size = designerPanelTamplate.getComponents().length;
            designerPanelTamplate.remove(size - 1);
            designerPanelTamplate.updateUI();
        }
    }
    
       public void removeAllModules() {
        lstModule = new ArrayList<ModuleBO>();
                   
        if (designerPanelTamplate.getComponents() != null) {
            designerPanelTamplate.removeAll();
            designerPanelTamplate.updateUI();
        }
    }
    
    public int getItemsPanel() {
     int size = designerPanelTamplate.getComponents().length;
     return size;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        designerPanelTamplate = new javax.swing.JPanel();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jMenuItem1.setText("Eliminar módulo");
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Rellenar con módulo");
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setLayout(new java.awt.BorderLayout());

        designerPanelTamplate.setName("designerPanelTamplate"); // NOI18N
        designerPanelTamplate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                designerPanelTamplateMouseClicked(evt);
            }
        });
        designerPanelTamplate.setLayout(new java.awt.GridLayout(0, 1));
        add(designerPanelTamplate, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void designerPanelTamplateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_designerPanelTamplateMouseClicked
        // TODO add your handling code here:
        //System.out.println("X:"+evt.getX()+" Y:"+evt.getY());
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            if(showContexMenu){
                jPopupMenu1.show(this, evt.getX(), evt.getY());
                coor_y = (short) evt.getY();
            }
        }
    }//GEN-LAST:event_designerPanelTamplateMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(lstModule == null){
            return;
        }
        
        short height = 0;
        for (ModuleBO mBO: lstModule){
            if((mBO.getHeight()+height) > coor_y){
                lstModule.remove(mBO);
                break;
            }else{
                height += mBO.getHeight();
            }
        }
        
        coor_y = 0;
        regeneraPlantilla();
        
        if(editMode){
            viewEditMsj=true;
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        short totHeight=0;
        isTitle = false;
        if(lstModule != null){
            
            for (ModuleBO mBO: lstModule){
                if( "Titulo".equals(mBO.getName().trim()) && mBO.getWidth() == 0 ){
                    isTitle = true;
                    continue;
                }
                totHeight += mBO.getHeight();
            }
        }
        
        int pxToClose = getModHeight(getMaxHeight(), totHeight);
        
        if(pxToClose == 0){
            JOptionPane.showMessageDialog(null, "No existe espacio para rellenar, la plantilla está completa.");
            return;
        }
        
        DesignerModuleDialog dialog = null;
        dialog = new DesignerModuleDialog(MainApp.getApplication().getMainFrame(), true, pxToClose);
        dialog.setDialog(dialog);
        dialog.initDesignarModule();
        dialog.setBounds(new Rectangle(this.widthTemplateDesigner, this.heightTemplateDesigner));//900,700
        Utilerias.centreDialog(dialog, true);
        dialog.setVisible(true);
        if(dialog.getIdModuloCreado()>0)
            addModuleTemplate(dialog.getIdModuloCreado(), 0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private boolean isTitle;
    private short getMaxHeight(){
        if(isTitle){
            if(lstModule != null && lstModule.size() > 0 )
                return (short) (GlobalDefines.DRAWABLE_HEIGHT_PH - lstModule.get(0).getHeight());
            else
                return GlobalDefines.DRAWABLE_HEIGHT_PH;
        }

        return GlobalDefines.DRAWABLE_HEIGHT;
    }
    
    private int getModHeight(int segment, int height){
        int res = 0;
        if(segment >= height){
            res = segment - height;
            return res < 15 ? 0 : res;
        }else{
            return res;
        }
    }
    
    private void regeneraPlantilla(){
        if(designerPanelTamplate == null){
            return;
        }
        
        if(lstModule == null){
            return;
        }
        
        designerPanelTamplate.removeAll();
        
        List<ModuleBO> lstTemp = lstModule;
        lstModule = new ArrayList<ModuleBO>();
        
        for(ModuleBO mBO : lstTemp){
            if(mBO.getModuleId() <= 0){
                addModuleTemplate(0, mBO.getHeight());
            }else{
                addModuleTemplate(mBO.getModuleId(), 0);
            }
        }
        
        
        
        designerPanelTamplate.revalidate();
        designerPanelTamplate.updateUI();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel designerPanelTamplate;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables
}
