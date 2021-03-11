package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import com.google.common.collect.ListMultimap;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Desarrollador
 */
public class ScrObjImage extends javax.swing.JPanel {

    private static final Logger LOG = Logger.getLogger(ScrObjImage.class.getName());

    private final JFileChooser chooser = new JFileChooser();
    private JPanel panImage;
    private ObjectInfoBO objInfo;
    private NavigableImagePanel navigableImagePanel;
    private BufferedImage image;
    private BufferedImage respImage;
    private boolean isCutImage;
    private boolean prepareToCutIamge;

    public ScrObjImage() {
        initComponents();
        initScreen();
        fillCboIdiomas();
    }

    private void fillCboIdiomas() {
        LanguageDAO lanDao = new LanguageDAO();
        List<LanguageBO> listLan = lanDao.get(null);
        LanguageBO lanBO = new LanguageBO();
        /*Agregando valores por default al combo*/
        lanBO.setName("Sin Corrección");
        lanBO.setIdLanguage(-1);
        cboIdiomas.addItem(lanBO);
        for (LanguageBO lanBO2 : listLan) {
            if("es".equals(lanBO2.getNomenclature().toLowerCase().trim()) || "en".equals(lanBO2.getNomenclature().toLowerCase().trim()))
                cboIdiomas.addItem(lanBO2);
        }
        cboIdiomas.setSelectedIndex(1);
        activarIdioma();
    }

    private final void initScreen() {
        cboIdiomasActionPerformed(null);
        loadFileFilter();
    }

    private final void loadFileFilter() {
        ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.TWITTER_IMAGE_FORMAT);
        FileFilter fileFilter = Utilerias.getFileFilter("Archivo de imágenes", list);
        //chooser.setFileFilter(fileFilter);
        chooser.addChoosableFileFilter(fileFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public void initPanImage(BufferedImage image_) {
        try {
            navigableImagePanel = new NavigableImagePanel(image_);
            navigableImagePanel.setClipSize(0, 0);
            containerPanel.removeAll();
            containerPanel.add(navigableImagePanel, BorderLayout.CENTER);
            containerPanel.revalidate();
            containerPanel.updateUI();
        } catch (IOException ex) {
            Logger.getLogger(ScrObjImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refreshFile() {
        displayImage();
    }

    public void selectFile() {
        int state = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        if (file != null && state == JFileChooser.APPROVE_OPTION) {
            edFileName.setText(file.getAbsolutePath());
            displayImage(file);
            setCutImage(false);
            rbAuto.setSelected(true);
        } else if (state == JFileChooser.CANCEL_OPTION) {

        }
    }

    public void displayImage(File file) {
        try {
            BufferedImage bi = ImageIO.read(file);
            if (bi != null) {
                image = bi;
                displayImage();
            } else {
                image = null;
            }
        } catch (IOException e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    public void displayImage(BufferedImage bi) {
        if (bi != null) {
            respImage = image;
            image = bi;
            displayImage();
        } else {
            image = null;
        }
    }

    public void displayImage() {
        try {
            if (image == null) {
                return;
            }
            
            initPanImage(image);
            //Estos valores se leen del modulo para ajustarlo 
            navigableImagePanel.setImage(image);
                        
            navigableImagePanel.setNavigationImageEnabled(false);
            navigableImagePanel.repaint();
        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    public void pasteImage() {
        String fileName = Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.IMAGE);
        this.objInfo.setFile(fileName);
        this.edFileName.setText(fileName);
        try {
            Utilerias.genFilePaste(fileName);
            this.objInfo.setFile(fileName);
            File file = new File(fileName);
            displayImage(file);
            setCutImage(false);
            /*panImage.repaint();
            panImage.revalidate();
            panImage.updateUI();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void activarIdioma(){
        int i = cboIdiomas.getSelectedIndex();
        String language;
        switch (i) {
            case 1:
                language = "es";
                break;
            case 2:
                language = "en";
                break;
            default:
                try {
                    SpellChecker.unregister(edTitulo);
                    SpellChecker.unregister(edSubtitulo);
                    SpellChecker.unregister(edDescripcion);
                } catch (Exception ex) {
                    LOG.log(Level.FINE, ex.getMessage());
                }
                language = null;
                break;
        }

        if (language == null) {
            return;
        }

        try {
            // Create user dictionary in the current working directory of your application
            SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
            // Load the configuration from the file dictionaries.cnf and 
            // use the current locale or the first language as default
            // You can download the dictionary files from 
            // http://sourceforge.net/projects/jortho/files/Dictionaries/                        
            URL is = new URL(Utilerias.rutaDiccionario());
            SpellChecker.registerDictionaries(is, language);
            // enable the spell checking on the text component with all features

            
            SpellChecker.register(edTitulo);
            SpellChecker.register(edSubtitulo);
            SpellChecker.register(edDescripcion);

            SpellCheckerOptions sco = new SpellCheckerOptions();
            sco.setCaseSensitive(true);
            sco.setSuggestionsLimitMenu(10);
            sco.setLanguageDisableVisible(false);
            sco.setIgnoreWordsWithNumbers(true);
            JPopupMenu popup = SpellChecker.createCheckerPopup(sco);

            edTitulo.addMouseListener(new PopupListener(popup));
            edSubtitulo.addMouseListener(new PopupListener(popup));
            edDescripcion.addMouseListener(new PopupListener(popup));

        } catch (NullPointerException | MalformedURLException ex) {
            LOG.log(Level.FINE, ex.getMessage());
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

        btnGroupImagen = new javax.swing.ButtonGroup();
        topPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboIdiomas = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        edFileName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        edSubtitulo = new javax.swing.JTextField();
        edDescripcion = new javax.swing.JTextField();
        edTitulo = new javax.swing.JTextField();
        rbAuto = new javax.swing.JRadioButton();
        rbSelection = new javax.swing.JRadioButton();
        mainPanelImage = new javax.swing.JPanel();
        cutPanel = new javax.swing.JPanel();
        btnCut = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        containerPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(330, 142));
        setLayout(new java.awt.BorderLayout());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Fuente*");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Archivo de imagen");
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 16));

        cboIdiomas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                cboIdiomasComponentShown(evt);
            }
        });
        cboIdiomas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIdiomasActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Subtítulo");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Título*");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Corrector ortográfico");

        edFileName.setMinimumSize(new java.awt.Dimension(120, 22));
        edFileName.setPreferredSize(new java.awt.Dimension(200, 22));

        jButton1.setText("Examinar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnGroupImagen.add(rbAuto);
        rbAuto.setSelected(true);
        rbAuto.setText("Autoajustable");
        rbAuto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbAutoItemStateChanged(evt);
            }
        });

        btnGroupImagen.add(rbSelection);
        rbSelection.setText("Cortar selección");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(edFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rbAuto)
                                .addGap(18, 18, 18)
                                .addComponent(rbSelection)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addComponent(edSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edDescripcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboIdiomas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(edFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edTitulo)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboIdiomas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbAuto)
                    .addComponent(rbSelection)))
        );

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(268, Short.MAX_VALUE))
        );

        add(topPanel, java.awt.BorderLayout.WEST);

        mainPanelImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanelImage.setLayout(new java.awt.BorderLayout());

        btnCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cut.png"))); // NOI18N
        btnCut.setEnabled(false);
        btnCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCutActionPerformed(evt);
            }
        });

        jButton2.setText("Pegar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnZoomIn.setText("»| |«");
        btnZoomIn.setEnabled(false);
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        btnZoomOut.setText("|« »|");
        btnZoomOut.setEnabled(false);
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cutPanelLayout = new javax.swing.GroupLayout(cutPanel);
        cutPanel.setLayout(cutPanelLayout);
        cutPanelLayout.setHorizontalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cutPanelLayout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        cutPanelLayout.setVerticalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cutPanelLayout.createSequentialGroup()
                .addGroup(cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnZoomIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnZoomOut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanelImage.add(cutPanel, java.awt.BorderLayout.NORTH);

        containerPanel.setLayout(new java.awt.BorderLayout());
        mainPanelImage.add(containerPanel, java.awt.BorderLayout.CENTER);

        add(mainPanelImage, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectFile();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboIdiomasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIdiomasActionPerformed
        activarIdioma();
    }//GEN-LAST:event_cboIdiomasActionPerformed

    private void cboIdiomasComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_cboIdiomasComponentShown

    }//GEN-LAST:event_cboIdiomasComponentShown

    private void btnCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCutActionPerformed
        try{
            final BufferedImage bi = ImageClipperNavigablePanel.getInstance().getCutImage();
            
            if(bi==null){
                return;
            }
        
            containerPanel.removeAll();
            containerPanel.revalidate();
            containerPanel.updateUI();
            mainPanelImage.revalidate();
            mainPanelImage.updateUI();
            /*JPanel p = new JPanel() {

                @Override
                public void paint(Graphics g) {
                    // POSITION OF THE PICTURE

                    int parentWidth = containerPanel.getWidth();//mainPanelImage.getWidth();
                    int parentHeight = containerPanel.getHeight();//mainPanelImage.getHeight();

                    int imageWidth = bi.getWidth();
                    int imageHeight = bi.getHeight();

                    int a1 = parentWidth / 2;
                    int a2 = imageWidth / 2;

                    int b1 = parentHeight / 2;
                    int b2 = imageHeight / 2;

                    g.drawImage(bi, a1 - a2, b1 - b2, imageWidth, imageHeight, null);
                }
            };
            p.setDoubleBuffered(true);
            containerPanel.add(p, BorderLayout.CENTER);
            containerPanel.revalidate();
            containerPanel.repaint();*/

            displayImage(bi);
    //        try{
    //            File f = new File(edFileName.getText());
    //            if (f.canExecute()) {
    //                f.delete();
    //            }
    //            f.createNewFile();
    //            ImageIO.write(bi, "png", f);
    //        } catch (HeadlessException | IOException ex) {
    //            Utilerias.logger(Utilerias.class).error(ex);
    //        }
            setCutImage(true);
            rbAuto.setSelected(true);
        }catch(IllegalArgumentException ex){
            Utilerias.logger(getClass()).error(ex);
            JOptionPane.showMessageDialog(null, "No se pudo generar el corte de la imagen, asegurese que el margen \n"
                                              + "de corte en color rojo este dentro de los bordes de la imagen.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
            JOptionPane.showMessageDialog(null, "No se pudo generar el corte de la imagen, asegurese que el margen \n"
                                              + "de corte en color rojo este dentro de los bordes de la imagen.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnCutActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        pasteImage();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void rbAutoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbAutoItemStateChanged
        if (rbAuto.isSelected() == true) {
            btnCut.setEnabled(false);
            btnZoomIn.setEnabled(false);
            btnZoomOut.setEnabled(false);
            prepareToCutIamge =  false;
            if(navigableImagePanel != null)
                navigableImagePanel.setClipSize(0, 0);
        } else {
            btnCut.setEnabled(true);
            btnZoomIn.setEnabled(true);
            btnZoomOut.setEnabled(true);
            prepareToCutIamge =  true;
            if(navigableImagePanel != null){
                final short panelContentWidth = (short) navigableImagePanel.getImage().getWidth();//500;
                final short panelContentHeight = (short) navigableImagePanel.getImage().getHeight();//560 ;
                short clipW = objInfo.getWidth();
                short clipH = objInfo.getHeight();
                if(objInfo.getWidth() >= panelContentWidth){
                    double dif = (objInfo.getWidth() - panelContentWidth);
                    double por = (dif*100/objInfo.getWidth());
                    double porH = (objInfo.getHeight()*(por/100));
                    clipH = (short)(objInfo.getHeight()-porH);
                    clipW = panelContentWidth;
                }
                if(objInfo.getHeight() >= panelContentHeight){
                    double dif = (objInfo.getHeight() - panelContentHeight);
                    double por = (dif*100/objInfo.getHeight());
                    double porW = (objInfo.getWidth()*(por/100));
                    clipW = (short)(objInfo.getWidth()-porW);
                    clipH = panelContentHeight;
                }
                               
                double scale = navigableImagePanel.getScale();
                if(scale > 1)
                    scale = 1;
                short width = (short)(clipW * scale);
                short height = (short)(clipH * scale);
                
                navigableImagePanel.setSizeClipByScale(width, height);
            }
        }
    }//GEN-LAST:event_rbAutoItemStateChanged

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        // TODO add your handling code here:
        ImageClipperNavigablePanel.getInstance().zoomInOut(true);
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        // TODO add your handling code here:
        ImageClipperNavigablePanel.getInstance().zoomInOut(false);
    }//GEN-LAST:event_btnZoomInActionPerformed

    public Boolean documentoVacio() {
        return edFileName.getText() == null || edFileName.getText().isEmpty() == true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCut;
    private javax.swing.ButtonGroup btnGroupImagen;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JComboBox cboIdiomas;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JPanel cutPanel;
    private javax.swing.JTextField edDescripcion;
    private javax.swing.JTextField edFileName;
    private javax.swing.JTextField edSubtitulo;
    private javax.swing.JTextField edTitulo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanelImage;
    private javax.swing.JRadioButton rbAuto;
    private javax.swing.JRadioButton rbSelection;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables

    public JTextField getEdFileName() {
        return edFileName;
    }

    public JTextField getEdDescripcion() {
        return edDescripcion;
    }

    public void setEdDescripcion(JTextField edDescripcion) {
        this.edDescripcion = edDescripcion;
    }

    public JTextField getEdSubtitulo() {
        return edSubtitulo;
    }

    public void setEdSubtitulo(JTextField edSubtitulo) {
        this.edSubtitulo = edSubtitulo;
    }

    public JTextField getEdTitulo() {
        return edTitulo;
    }

    public void setEdTitulo(JTextField edTitulo) {
        this.edTitulo = edTitulo;
    }

    public void setEdFileName(JTextField edFileName) {
        this.edFileName = edFileName;
    }

    public ObjectInfoBO getObjInfo() {
        return objInfo;
    }

    public void setObjInfo(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
        rbAuto.setSelected(true);
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the thumbImage
     */
    public BufferedImage getRespImage() {
        return respImage;
    }

    /**
     * @param thumbImage the thumbImage to set
     */
    public void setRespImage(BufferedImage respImage) {
        this.respImage = respImage;
    }
    
    public boolean isCutImage() {
        return isCutImage;
    }

    public void setCutImage(boolean isCutImage) {
        this.isCutImage = isCutImage;
    }

    public boolean isPrepareToCutIamge() {
        return prepareToCutIamge;
    }
}
