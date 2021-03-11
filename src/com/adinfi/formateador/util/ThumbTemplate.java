/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.bos.TemplateModuleBO;
import com.adinfi.formateador.dao.TemplateDAO;
import com.adinfi.formateador.view.dnd.ScrCell;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Desarrollador
 */
public class ThumbTemplate extends javax.swing.JPanel {

    int templateId;
    /*
     public static void main(String[] args){
     JFrame mainFrame = new JFrame("Java SWING Examples");
     mainFrame.setSize(90,120);             
     mainFrame.setLayout(new GridLayout(1 , 1));      
     mainFrame.addWindowListener(new WindowAdapter() {
     public void windowClosing(WindowEvent windowEvent){
     System.exit(0);
     }        
     });    
     ThumbTemplate th = new ThumbTemplate(1);      
     mainFrame.add(th);        
     //      mainFrame.setVisible(true);  
     try{
          
     // File file = new File("c:/jmpa/fileName.png");
     // if (!file.exists())file.createNewFile();          
  
     th.setPreferredSize(new Dimension(90,100));
     BufferedImage bi = ScreenImage.createImage(th);
     ScreenImage.writeImage(bi, "c:/jmpa/fileName.png");      
      
      
     }catch(Exception e){
     e.printStackTrace();
     }
      
      
        
     } */

    public static void main(String[] args) {
        ThumbTemplate thTemp = new ThumbTemplate();
        //thTemp.genThumbTemplate(1,(short)90,(short)90);
        thTemp.genThumbModules(2, (short) 90, (short) 30);
    }

    /**
     * Creates new form ThumbTemplate
     */
    public ThumbTemplate() {
        // initComponents();
        //this.templateId=templateId;
        // genLayout();

    }

    public void genThumbTemplate(int templateId, short width, short height) {
        JPanel contentPanel = new javax.swing.JPanel();
        contentPanel.setLayout(new java.awt.GridLayout());
        genLayout(templateId, contentPanel);
        String f = Utilerias.getFilePath(Utilerias.PATH_TYPE.THUMB_TEMPLATE_DIR);
        String file = f + templateId + ".png";
        genThumb(contentPanel, width, height, file);
    }

    public void genThumbModules(int templateId, short width, short height) {

        TemplateDAO templateDAO = new TemplateDAO();
        TemplateBO tempBO = templateDAO.getTemplate(templateId);
        List<TemplateModuleBO> lstModules = tempBO.getModulesAsList();
        if (lstModules == null) {
            return;
        }
        JPanel mainPanel = null;
        String file = null;
        ModuleBO moduleBO = null;
        for (TemplateModuleBO tempModBO : lstModules) {
            mainPanel = new JPanel();
            moduleBO = tempModBO.getModule();
            mainPanel.setLayout(new java.awt.GridLayout());

            showLayout(mainPanel, mainPanel, moduleBO.getRootSection());
            String f = Utilerias.getFilePath(Utilerias.PATH_TYPE.THUMB_MODULE_DIR);
            file = f + moduleBO.getRootSectionId() + ".png";
            genThumb(mainPanel, width, height, file);
        }

    }

    public void genThumbModule(int moduleId, short width, short height, String fileNameAdd) {
        TemplateDAO templateDAO = new TemplateDAO();
        JPanel mainPanel = new JPanel();
        ModuleBO moduleBO = templateDAO.getModule(moduleId);
        mainPanel.setLayout(new java.awt.GridLayout());

        String file = "";
        if (moduleBO != null) {
            showLayout(mainPanel, mainPanel, moduleBO.getRootSection());
            String f = Utilerias.getFilePath(Utilerias.PATH_TYPE.THUMB_MODULE_DIR);
            file = f + moduleId + fileNameAdd + ".png";
            genThumb(mainPanel, width, height, file);
        }

    }

    protected void genThumb(JPanel th, short width, short height, String file) {
        try {
            th.setPreferredSize(new Dimension(width, height));
            BufferedImage bi = ScreenImage.createImage(th);
            ScreenImage.writeImage(bi, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage genThumbPrev(JPanel th, short width, short height) {
        BufferedImage bi = null;
        try {
            th.setPreferredSize(new Dimension(width, height));
            bi = ScreenImage.createImage(th);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bi;
    }

    protected void genLayout(int templateId, JPanel mainPanel) {
        TemplateDAO templateDAO = new TemplateDAO();
        TemplateBO tempBO = templateDAO.getTemplate(templateId);
        List<TemplateModuleBO> lstModules = tempBO.getModulesAsList();
        if (lstModules == null) {
            return;
        }
        mainPanel.setLayout(new GridLayout(lstModules.size(), 1));
        for (TemplateModuleBO moduleBO : lstModules) {

            showLayout(mainPanel, mainPanel, moduleBO.getModule().getRootSection());

        }

    }

    private void showLayout(JPanel mainPanel, JPanel panelParent, ModuleSectionBO section
    ) {

        if (section == null) {
            return;
        }

        List<ModuleSectionBO> sectionsChilds = section.getChildSectionsAsList();

        int width = 0;
        int height = 0;

        if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType())) {
     //  panelParent.add(new JLabel(section.getSectionName()));
            //  SectionInfoBO secInfo=new SectionInfoBO();

            // secInfo.setSectionName(section.getSectionName() );
            JPanel childPanel = new JPanel();
            childPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

            if ("B".equals(section.getContentType())) {
                childPanel.setBackground(new java.awt.Color(35, 81, 130));
            } else if ("T".equals(section.getContentType())) {
                childPanel.setBackground(new java.awt.Color(202, 201, 198));
            } else {
                childPanel.setBackground(new java.awt.Color(254, 106, 0));
            }

            childPanel.setOpaque(true);

            if (panelParent == null) {
                JPanel panelTmp = new JPanel();
                //JPanel childPanel=new JPanel();
                childPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

                panelTmp.setLayout(new GridLayout());
                panelTmp.add(/*new ScrCell(section)*/childPanel);
                // panelTmp.setBorder(BorderFactory.createEtchedBorder());
                // panelTmp.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));         
                mainPanel.add(panelTmp);
            } else {
                panelParent.add(/*new ScrCell(section)*/childPanel);
                //  panelParent.setBorder(BorderFactory.createEtchedBorder());
                panelParent.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
            }

            // panelParent.setFocusable(true);
            //((ScrCell)panelParent).setDrag();
            return;
        }

        if (GlobalDefines.SEC_TYPE_COLUMN.equals(section.getType())) {
            width = section.getWidth();
        } else if (GlobalDefines.SEC_TYPE_SHEET.equals(section.getType())) {
            height = section.getHeight();
        }

        JPanel panel = new JPanel();
        // panel.setBackground(Color.darkGray);
        // panel.setBorder(BorderFactory.createEtchedBorder());
        // panel.setSize(100,100);
        if (panelParent != null) {
            panelParent.add(panel);
        } else {
            // contentPanel.setBorder(BorderFactory.createEtchedBorder());
            contentPanel.add(panel);
        }

        // panel.sets
        // GridBagLayout layout = new GridBagLayout();
        GridLayout layout = new GridLayout();

        // GridBagConstraints gbc = new GridBagConstraints();
        if (GlobalDefines.SEC_TYPE_COLUMN.equals(section.getType())) {
            // gbc.fill = GridBagConstraints.VERTICAL;     
            layout = new GridLayout(sectionsChilds.size(), 1);
        } else if (GlobalDefines.SEC_TYPE_SHEET.equals(section.getType())) {
            // gbc.fill = GridBagConstraints.HORIZONTAL;       
            layout = new GridLayout(1, sectionsChilds.size());

        }
        panel.setLayout(layout);

        int i = 0;
        if (sectionsChilds != null) {
            for (ModuleSectionBO secTmp : sectionsChilds) {
                if (secTmp == null) {
                    continue;
                }
                JPanel content = null;
                if (GlobalDefines.SEC_TYPE_CELL.equals(section.getType())) {
                    content = new ScrCell(null, null);
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                } else {
                    content = new JPanel(new GridLayout(1, 1));
                    content.setTransferHandler(null);
                    content.setFocusable(false);
                }

                // content.setBorder(BorderFactory.createEtchedBorder());
                if (GlobalDefines.SEC_TYPE_COLUMN.equals(section.getType())) {
                    panel.add(content);
                } else if (GlobalDefines.SEC_TYPE_SHEET.equals(section.getType())) {
                    panel.add(content);
                }

                showLayout(mainPanel, content, secTmp);

                i++;

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout());

        contentPanel.setLayout(new java.awt.GridLayout());
        add(contentPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    // End of variables declaration//GEN-END:variables
}
