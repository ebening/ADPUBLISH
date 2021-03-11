package com.adinfi.formateador.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Guillermo Trejo
 */
public class PanelThumbTodos extends javax.swing.JPanel {

    public static final short MAX_WIDTH_GAP = 10;
    public static final short MAX_HEIGHT_GAP = 10;
    public static final short MIN_WIDTH_GAP = 5;
    public static final short MIN_HEIGHT_GAP = 5;    
    public static final short DEFAULT_WIDTH_PANEL_SIZE = 90;
    public static final short DEFAULT_HEIGHT_PANEL_SIZE = 90;    
    private JButton button;
    private String fileName;
    private String moduleName;
    private int id;
    private int panelWidth;
    private int panelHeight;
    private Image imgCurrent;
    private ImageIcon originalImageIcon;
    private ImageIcon zoomIn;
    private ImageIcon zoomOut;

    /**
     * Creates new form PanelThumb
     *
     * @param id
     * @param width
     * @param height
     * @param file
     * @param moduleName
     */
    public PanelThumbTodos(int id, short width, short height, String file, String moduleName) {
        initComponents();
        this.id = id;
        this.panelWidth = width;
        this.panelHeight = height;
        this.fileName = file;
        this.moduleName = moduleName;
        this.setPreferredSize(new Dimension(width + MAX_WIDTH_GAP, height + MAX_HEIGHT_GAP));
        setNameLabel();
        setImage();
        initScreen();
        repaint();
    }
    
    private void setNameLabel() {
        label.setText(getModuleName());
    }

    public final void setImage() {
        imgCurrent = new ImageIcon(getFileName()).getImage();
        originalImageIcon = new ImageIcon(imgCurrent);
        this.panelWidth = originalImageIcon.getIconWidth();
        this.panelHeight = originalImageIcon.getIconHeight();
        button = new JButton(originalImageIcon);
        zoomIn = new ImageIcon(imgCurrent.getScaledInstance(panelWidth + MAX_WIDTH_GAP, panelHeight + MAX_HEIGHT_GAP, Image.SCALE_SMOOTH));
        zoomOut = new ImageIcon(imgCurrent.getScaledInstance(panelWidth - MIN_WIDTH_GAP, panelHeight - MIN_WIDTH_GAP, Image.SCALE_SMOOTH));
    }

    protected final void initScreen() {
        getButton().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent evt) {
                getButton().setIcon(zoomIn);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                getButton().setIcon(originalImageIcon);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                getButton().setIcon(zoomOut);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                getButton().setIcon(zoomIn);
            }
        });
        getButton().setOpaque(false);
        getButton().setContentAreaFilled(false);
        getButton().setBorderPainted(false);
        this.add(getButton(), BorderLayout.CENTER);
    }

    public void displayImage(File file) {
        imgCurrent = new ImageIcon(file.getAbsolutePath()).getImage();
        getButton().repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        northPanel = new javax.swing.JPanel();
        checkedTemplate = new javax.swing.JCheckBox();

        setMaximumSize(new java.awt.Dimension(45, 45));
        setMinimumSize(new java.awt.Dimension(45, 45));
        setPreferredSize(new java.awt.Dimension(45, 45));
        setLayout(new java.awt.BorderLayout());

        label.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(label, java.awt.BorderLayout.SOUTH);

        northPanel.add(checkedTemplate);

        add(northPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkedTemplate;
    private javax.swing.JLabel label;
    private javax.swing.JPanel northPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the panelWidth
     */
    public int getPanelWidth() {
        return panelWidth;
    }

    /**
     * @param panelWidth the panelWidth to set
     */
    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    /**
     * @return the panelHeight
     */
    public int getPanelHeight() {
        return panelHeight;
    }

    /**
     * @param panelHeight the panelHeight to set
     */
    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    /**
     * @return the button
     */
    public JButton getButton() {
        return button;
    }

    
    public boolean isChecked() {
        return checkedTemplate.isSelected();
    }

    
    public void setChecked(boolean bSelected) {
        checkedTemplate.setSelected(bSelected);
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
}