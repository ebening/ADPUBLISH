package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.administration.JTabbedPane_withoutPaintedTabs;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Desarrollador
 */
public class DlgObjects extends javax.swing.JDialog {

    private boolean accept;
    private ObjectInfoBO objInfo;
    private ScrObjImage scrObjImage;
    private ScrObjExcel scrObjExcel;
    private boolean paste;
    private boolean isDisclosure;

    /**
     * Creates new form DlgObjects
     */
    public DlgObjects(boolean isDisclosure) {
        super();
        this.accept = false;
        initComponents();
        initScreen();
        this.isDisclosure = isDisclosure;
    }

    /**
     * Creates new form DlgObjects
     */
    public DlgObjects() {
        super();
        this.accept = false;
        initComponents();
        initScreen();
        scrObjImage.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("imagen");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                ;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("imagen");
            }

        });
    }

    public ScrObjExcel getScrObjExcel() {
        if (scrObjExcel == null) {
            return new ScrObjExcel();
        }
        return scrObjExcel;
    }

    public final void initScreen() {
        scrObjImage = new ScrObjImage();
        panImagen.add(scrObjImage);
        scrObjExcel = new ScrObjExcel();
        panExcel.add(scrObjExcel);
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public void setObjType(int objType) {
        switch (objType) {
            case GlobalDefines.OBJ_TYPE_EXCEL:
                btnPreview.setVisible(true);
                tabObjects.setEnabledAt(0, true);
                tabObjects.setSelectedComponent(this.panExcel);
                tabObjects.setEnabledAt(1, false);
                break;
            case GlobalDefines.OBJ_TYPE_IMAGE:
                btnPreview.setVisible(false);
                tabObjects.setEnabledAt(1, true);
                tabObjects.setSelectedComponent(this.panImagen);
                tabObjects.setEnabledAt(0, false);
                break;
        }
    }

    public void resetScrObject() {

        /*
         scrObjImage.setObjInfo(null);
         // panImagen.add(scrObjImage);  
         scrObjImage.getEdFileName().setText( ""  );   
         scrObjImage.refreshFile(); */
    }

    public ObjectInfoBO findDatosIniciales(ModuleSectionBO secInfo) {
        if (secInfo == null) {
            return null;
        }
        if (secInfo.getLstObjects() == null || secInfo.getLstObjects().size() <= 0) {
            return null;
        }
        ObjectInfoBO objectInfoBO = secInfo.getLstObjects().get(0);
        return objectInfoBO;
    }

    public void initScrObject(ObjectInfoBO objInfo, ModuleSectionBO secInfo) {
        initScrObject(objInfo, secInfo, null);
    }

    public void initScrObject(ObjectInfoBO objInfo, ModuleSectionBO secInfo, Boolean isNew) {
        initScrObject(objInfo, secInfo, isNew, false);
    }

    public void initScrObject(ObjectInfoBO objInfo, ModuleSectionBO secInfo, Boolean isNew, Boolean isDisclosure) {
        this.objInfo = objInfo;
        this.setObjType(objInfo.getObjType());
        String titulo = "";
        String subtitulo = "";
        String comentarios = "";

        ObjectInfoBO boTmp = findDatosIniciales(secInfo);
        if (boTmp != null && (isNew == null || !isNew.booleanValue())) {
            titulo = boTmp.getTitulo();
            subtitulo = boTmp.getSubTitulo();
            comentarios = boTmp.getComentarios();
        }

        if (objInfo.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {
            scrObjImage.setObjInfo(objInfo);

            if (objInfo.getTitulo() != null && objInfo.getTitulo().isEmpty() == false) {
                scrObjImage.getEdTitulo().setText(objInfo.getTitulo());
            } else {
                scrObjImage.getEdTitulo().setText(titulo);
            }

            if (objInfo.getSubTitulo() != null && objInfo.getSubTitulo().isEmpty() == false) {
                scrObjImage.getEdSubtitulo().setText(objInfo.getSubTitulo());
            } else {
                scrObjImage.getEdSubtitulo().setText(subtitulo);
            }

            if (objInfo.getComentarios() != null && objInfo.getComentarios().isEmpty() == false) {
                scrObjImage.getEdDescripcion().setText(objInfo.getComentarios());
            } else {
                scrObjImage.getEdDescripcion().setText(comentarios);
            }

            scrObjImage.setImage(objInfo.getImageThumb() == null ? objInfo.getImage() : objInfo.getImageThumb());
            scrObjImage.setRespImage(null);//Para editar se reinicia el respaldo
            scrObjImage.getEdFileName().setText(objInfo.getFile());
            scrObjImage.refreshFile();

        } else if (objInfo.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL) {
            scrObjExcel.setObjInfo(objInfo);
            scrObjExcel.getEdPath().setText(objInfo.getFile());
            scrObjExcel.getEdRangoIni().setText(objInfo.getRangoIni());
            scrObjExcel.getEdRangoFin().setText(objInfo.getRangoFin());

            if (objInfo.getTitulo() != null && objInfo.getTitulo().isEmpty() == false) {
                scrObjExcel.getEdTitulo().setText(objInfo.getTitulo());
            } else {
                scrObjExcel.getEdTitulo().setText(titulo);
            }

            if (objInfo.getSubTitulo() != null && objInfo.getSubTitulo().isEmpty() == false) {
                scrObjExcel.getEdSubtitulo().setText(objInfo.getSubTitulo());
            } else {
                scrObjExcel.getEdSubtitulo().setText(subtitulo);
            }

            if (objInfo.getComentarios() != null && objInfo.getComentarios().isEmpty() == false) {
                scrObjExcel.getEdDescripcion().setText(objInfo.getComentarios());
            } else {
                scrObjExcel.getEdDescripcion().setText(comentarios);
            }

            //TODO: Ajuste de formato de vista solicitado para Disclosure
            scrObjExcel.getjLabel5().setVisible(!isDisclosure);
            scrObjExcel.getEdTitulo().setVisible(!isDisclosure);
            scrObjExcel.getjLabel6().setVisible(!isDisclosure);
            scrObjExcel.getEdSubtitulo().setVisible(!isDisclosure);
            scrObjExcel.getjLabel7().setVisible(!isDisclosure);
            scrObjExcel.getEdDescripcion().setVisible(!isDisclosure);
            scrObjExcel.getEdRango().setEditable(isDisclosure);
            scrObjExcel.getjLabel8().setVisible(!isDisclosure);
            scrObjExcel.getCboIdiomas().setVisible(!isDisclosure);
            //TODO: Se dejan con espacio para pasar la validación del entrada
            if (isDisclosure) {
                scrObjExcel.getEdTitulo().setText(" ");
                scrObjExcel.getEdSubtitulo().setText(" ");
                scrObjExcel.getEdDescripcion().setText(" ");
            }

            if (objInfo.getImage() != null) {
                scrObjExcel.setIsCutImage(false);
                scrObjExcel.setImage(objInfo.getImageThumb() == null ? objInfo.getImage() : objInfo.getImageThumb());
                scrObjExcel.setThumbImage(null);//reiniciar respaldo
                scrObjExcel.displayImage();
            } else {
                scrObjExcel.setIsCutImage(false);
                scrObjExcel.setImage(null);
                scrObjExcel.setThumbImage(null);//reiniciar respaldo
            }

            if(objInfo.getIdLinkedExcel() > 0){
                scrObjExcel.cargarNombreCategoria(objInfo.getIdLinkedExcel());
            }
            /*if (objInfo.getFile() != null && objInfo.getFile().isEmpty() == false
             && objInfo.getRangoIni() != null && objInfo.getRangoIni().isEmpty() == false) {
             scrObjExcel.pasteExcel();
             }*/
        }

    }

    public ObjectInfoBO getObjInfo() {
        return objInfo;
    }

    public void setObjInfo(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
    }

    public void aceptObjImage() {
        String source = this.scrObjImage.getEdFileName().getText();
        String rutaArchivo = Utilerias.copyFile(source, Utilerias.PATH_TYPE.GENERATED_IMAGES);
        String nombreArchivo = Utilerias.getFileName(rutaArchivo);
        this.objInfo.setFile(nombreArchivo == null ? source : nombreArchivo);
        //this.objInfo.setFile2(rutaArchivo);

        this.objInfo.setTitulo(this.scrObjImage.getEdTitulo().getText());
        this.objInfo.setSubTitulo(this.scrObjImage.getEdSubtitulo().getText());
        this.objInfo.setComentarios(this.scrObjImage.getEdDescripcion().getText());

        if (scrObjImage.isCutImage()) {
            this.objInfo.setImage(this.scrObjImage.getRespImage());
            this.objInfo.setImageThumb(this.scrObjImage.getImage());
        } else {
            this.objInfo.setImage(this.scrObjImage.getImage());
            this.objInfo.setImageThumb(null);
        }
    }

    public void aceptObjExcel() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if ( !scrObjExcel.isCutImage() 
                    && scrObjExcel.getNombreLEBO() != null 
                    && scrObjExcel.getEdPath().getText() != null 
                    && scrObjExcel.getEdPath().getText().isEmpty() == false) {
                scrObjExcel.pasteExcel();
                if(scrObjExcel.isErrorDetectado()){
                    JOptionPane.showMessageDialog(this, "Se genero un error al actualizar la información del Excel Vinculado. Revise lo siguiente: \n"
                                                        + " Este seleccionado una Categoria y un Nombre de Excel.\n "
                                                        + " La ruta del excel este completa y accesible en su red.");
                }
            }
            String rutaArchivo = this.scrObjExcel.getCurrentFileImg();
            String nombreArchivo = Utilerias.getFileName(rutaArchivo);
            this.objInfo.setFile(nombreArchivo);
            if (scrObjExcel.isCutImage()) {
                this.objInfo.setImage(scrObjExcel.getThumbImage());
                this.objInfo.setImageThumb(scrObjExcel.getImage());
            } else {
                this.objInfo.setImage(scrObjExcel.getImage());
                this.objInfo.setImageThumb(null);
            }
            //this.objInfo.setFile2(rutaArchivo);
            this.objInfo.setRangoIni(this.scrObjExcel.getEdRangoIni().getText());
            this.objInfo.setRangoFin(this.scrObjExcel.getEdRangoFin().getText());
            this.objInfo.setTitulo(this.scrObjExcel.getEdTitulo().getText());
            this.objInfo.setSubTitulo(this.scrObjExcel.getEdSubtitulo().getText());
            this.objInfo.setComentarios(this.scrObjExcel.getEdDescripcion().getText());
            
            if(this.scrObjExcel.getNombreLEBO() != null)
                this.objInfo.setIdLinkedExcel(this.scrObjExcel.getNombreLEBO().getIdLinkedExcel());

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }

    }

    public void setObjInfo() {
        switch (tabObjects.getSelectedIndex()) {
            case 0:
                aceptObjExcel();
                break;
            case 1:
                aceptObjImage();
                break;
            case 2:
                aceptObjImage();
                break;
            case 4:
                break;
        }
    }

    protected boolean validateInputs() {
        if (objInfo.getObjType() == GlobalDefines.OBJ_TYPE_IMAGE) {

            if (scrObjImage.getEdTitulo().getText() == null || scrObjImage.getEdTitulo().getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar el título. ");
                return false;
            }

            if (scrObjImage.getEdDescripcion().getText() == null || scrObjImage.getEdDescripcion().getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar la fuente. ");
                return false;
            }

            if (scrObjImage.getEdTitulo().getText().length() > 100) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar un titulo menor a 100 caracteres. ");
                return false;
            }

            if (scrObjImage.getEdSubtitulo().getText().isEmpty() == true) {
                if (scrObjImage.getEdSubtitulo().getText().length() > 100) {
                    JOptionPane.showMessageDialog(this, "Favor de proporcionar un subtitulo menor a 100 caracteres. ");
                    return false;
                }
            }

            if (scrObjImage.getEdDescripcion().getText().length() > 300) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar fuente menor a 300 caracteres. ");
                return false;
            }
            
            if(scrObjImage.isPrepareToCutIamge()){
                int result = JOptionPane.showConfirmDialog(null, "No se ha aplicado el corte de la seleccion, este cambio no se vera reflejado. ¿Esta seguro de continuar?."
                                                            , Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return false;
                }
            }

        } else if (objInfo.getObjType() == GlobalDefines.OBJ_TYPE_EXCEL && isDisclosure == false) {

            if (scrObjExcel.getEdTitulo().getText() == null || scrObjExcel.getEdTitulo().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar el título. ");
                return false;
            }

            if (scrObjExcel.getEdDescripcion().getText() == null || scrObjExcel.getEdDescripcion().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar la fuente. ");
                return false;

            }

            if (scrObjExcel.getEdTitulo().getText().length() > 100) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar un titulo menor a 100 caracteres. ");
                return false;
            }

            if (scrObjExcel.getEdSubtitulo().getText().isEmpty() == true) {
                if (scrObjExcel.getEdSubtitulo().getText().length() > 100) {
                    JOptionPane.showMessageDialog(this, "Favor de proporcionar un subtitulo menor a 100 caracteres. ");
                    return false;
                }
            }

            if (scrObjExcel.getEdDescripcion().getText().length() > 300) {
                JOptionPane.showMessageDialog(this, "Favor de proporcionar fuente menor a 300 caracteres. ");
                return false;
            }
            
            if(scrObjExcel.isPrepareToCutIamge()){
                int result = JOptionPane.showConfirmDialog(null, "No se ha aplicado el corte de la seleccion, este cambio no se vera reflejado. ¿Esta seguro de continuar?."
                                                            , Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tabObjects = new JTabbedPane_withoutPaintedTabs();
        panExcel = new javax.swing.JPanel();
        panImagen = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnPreview = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        tabObjects.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabObjectsStateChanged(evt);
            }
        });

        panExcel.setBackground(new java.awt.Color(255, 255, 255));
        panExcel.setLayout(new java.awt.GridLayout(1, 0));
        tabObjects.addTab("Seccion de Excel", panExcel);

        panImagen.setLayout(new java.awt.GridLayout(1, 0));
        tabObjects.addTab("Imagen", panImagen);

        jPanel1.add(tabObjects);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());

        btnPreview.setText("Previsualizar");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        jPanel3.add(btnPreview);

        btnOk.setText("Aceptar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        jPanel3.add(btnOk);

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel3.add(btnCancel);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("*Campos requeridos");
        jPanel4.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        if (validateInputs() == false) {
            return;
        }
        setObjInfo();
        this.setAccept(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        this.accept = false;
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowOpened

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                ;
            }

            @Override
            public void keyTyped(KeyEvent e) {
                ;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                ;
            }

        });
    }//GEN-LAST:event_formComponentShown

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        if (scrObjExcel != null) {
            scrObjExcel.previewImage();
        }
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void tabObjectsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabObjectsStateChanged
        System.out.println(tabObjects.getSelectedIndex());
        int sIndex = tabObjects.getSelectedIndex();
        if (sIndex == 0) {
            setTitle("Insertar Excel");
        }

        if (sIndex == 1) {
            setTitle("Insertar Imagen");
        }

    }//GEN-LAST:event_tabObjectsStateChanged

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel panExcel;
    private javax.swing.JPanel panImagen;
    private javax.swing.JTabbedPane tabObjects;
    // End of variables declaration//GEN-END:variables

    public ScrObjImage getScrObjImage() {
        return scrObjImage;
    }

    public void setScrObjImage(ScrObjImage scrObjImage) {
        this.scrObjImage = scrObjImage;
    }

    public JTabbedPane getTabObjects() {
        return tabObjects;
    }

    public void setTabObjects(JTabbedPane tabObjects) {
        this.tabObjects = tabObjects;
    }

    public boolean getPaste() {
        return paste;
    }

    public void setPaste(boolean paste) {
        this.paste = paste;
    }

}
