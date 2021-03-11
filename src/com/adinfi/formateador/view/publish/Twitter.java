package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.Usuario;
import com.adinfi.ws.publicador.DBResult;
import com.google.common.collect.ListMultimap;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Josue Sanchez
 */
public class Twitter extends javax.swing.JDialog {

    private String HASH_TAG;
    private int chars;
    private int LIMIT_CHARS = 140;
    private final int ATTACH_SIZE = 23;
    private boolean selectedFile = false;
    private ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.TWITTER_HASHTAG_VALUE);

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    public Twitter(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inputTwitter.setLineWrap(true);
        inputTwitter.setWrapStyleWord(true);
        removeAttchTW.setVisible(false);
        enviarTwitt.setEnabled(false);
        allowHashtagModify();

        List<String> hashtag = list.get("twitter.hashtag.value");
        HASH_TAG = hashtag.get(0);
        inputHashtag.setText(hashtag.get(0));

        // total de caracteres menos el tamaño de caracteres del hashtag
        counterTwitteer.setText(String.valueOf(LIMIT_CHARS));
        chars = Integer.parseInt(counterTwitteer.getText());

        inputTwitter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                countChar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                countChar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                countChar();
            }
        });
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        enviarTwitt = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputTwitter = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        cboAutores = new javax.swing.JComboBox();
        counterTwitteer = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        inputHashtag = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        selectImageTwitter = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        removeAttchTW = new javax.swing.JButton();
        imageInLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Twitter");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setName("jPanel3"); // NOI18N

        enviarTwitt.setText("Enviar");
        enviarTwitt.setName("enviarTwitt"); // NOI18N
        enviarTwitt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarTwittActionPerformed(evt);
            }
        });
        jPanel3.add(enviarTwitt);

        jButton1.setText("Cancelar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        inputTwitter.setColumns(20);
        inputTwitter.setRows(5);
        inputTwitter.setName("inputTwitter"); // NOI18N
        jScrollPane2.setViewportView(inputTwitter);

        jPanel1.setName("jPanel1"); // NOI18N

        cboAutores.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Usuario Logeado" }));
        cboAutores.setEnabled(false);
        cboAutores.setName("cboAutores"); // NOI18N
        cboAutores.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                cboAutoresComponentShown(evt);
            }
        });
        cboAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAutoresActionPerformed(evt);
            }
        });

        counterTwitteer.setBackground(new java.awt.Color(204, 204, 204));
        counterTwitteer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        counterTwitteer.setText("140");
        counterTwitteer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        counterTwitteer.setName("counterTwitteer"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Autor");
        jLabel1.setName("jLabel1"); // NOI18N

        inputHashtag.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputHashtag.setText("#Vector");
        inputHashtag.setEnabled(false);
        inputHashtag.setName("inputHashtag"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputHashtag, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(counterTwitteer, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(counterTwitteer, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputHashtag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane2)
                .addGap(36, 36, 36))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel4.setName("jPanel4"); // NOI18N

        selectImageTwitter.setText("Examinar");
        selectImageTwitter.setName("selectImageTwitter"); // NOI18N
        selectImageTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectImageTwitterActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Twitter");
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        removeAttchTW.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        removeAttchTW.setForeground(new java.awt.Color(255, 0, 51));
        removeAttchTW.setText("X");
        removeAttchTW.setName("removeAttchTW"); // NOI18N
        removeAttchTW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAttchTWActionPerformed(evt);
            }
        });

        imageInLabel.setName("imageInLabel"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeAttchTW, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(removeAttchTW))
                    .addComponent(imageInLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectImageTwitter)
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(selectImageTwitter)
                        .addGap(8, 8, 8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadFileFilter() {
        attachmentTW = new JFileChooser();
        ListMultimap<String, String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.TWITTER_IMAGE_FORMAT);
        FileFilter fileFilter = Utilerias.getFileFilter("Imágenes", list);
        attachmentTW.setFileFilter(fileFilter);
        attachmentTW.addChoosableFileFilter(fileFilter);
        attachmentTW.setAcceptAllFileFilterUsed(false);
        attachmentTW.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private void selectImage() {
        //attachmentTW = new JFileChooser();
        loadFileFilter();

        int returnVal = attachmentTW.showOpenDialog(null);
        File file = attachmentTW.getSelectedFile();

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage resizedImage = resize(image, 50, 50);
                ImageIcon ii = new ImageIcon(resizedImage);
                imageInLabel.setVisible(true);
                imageInLabel.setIcon(ii);
                removeAttchTW.setVisible(true);

                //Restar los caracteres de la imagen 23
                if (attachmentTW != null && selectedFile == false) {
                    int ch = Integer.valueOf(counterTwitteer.getText()) - (StatementConstant.SC_23.get());
                    counterTwitteer.setText(String.valueOf(ch));

                    if (ch < 0) {
                        counterTwitteer.setForeground(Color.RED);
                        enviarTwitt.setEnabled(false);
                    } else {
                        counterTwitteer.setForeground(Color.BLACK);
                        enviarTwitt.setEnabled(true);
                    }

                    selectedFile = true;
                }

            } catch (IOException ex) {
                Utilerias.logger(getClass()).info(ex);
            }
        }
    }

    /* Funcion para contar los caracteres en twitter */
    private void countChar() {
        // limite menos el hashtag menos un espacio.
        String currentHashtag = inputHashtag.getText();
        int limit = (LIMIT_CHARS - currentHashtag.length()) - 1;
        int currentTextLenght = inputTwitter.getText().length();

        if (attachmentTW.getSelectedFile() == null) {

            int n = limit - currentTextLenght;

            if (n < 0) {
                counterTwitteer.setForeground(Color.RED);
                enviarTwitt.setEnabled(false);
            } else {
                counterTwitteer.setForeground(Color.BLACK);
                enviarTwitt.setEnabled(true);
            }

            counterTwitteer.setText(String.valueOf(n));

        } else {
            int n = (limit - currentTextLenght) - ATTACH_SIZE;

            if (n < 0) {
                counterTwitteer.setForeground(Color.RED);
                enviarTwitt.setEnabled(false);
            } else {
                counterTwitteer.setForeground(Color.BLACK);
                enviarTwitt.setEnabled(true);
            }

            counterTwitteer.setText(String.valueOf(n));

        }

        int totalCaracteres = Integer.valueOf(counterTwitteer.getText());

        if (totalCaracteres < 0) {
            counterTwitteer.setForeground(Color.RED);
            enviarTwitt.setEnabled(false);

        } else {
            counterTwitteer.setForeground(Color.BLACK);
            enviarTwitt.setEnabled(true);
        }
    }

    private void sendTweet() {

        DBResult res = null;

        if (validateTwitter()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            Usuario usuario = InstanceContext.getInstance().getUsuario();
            int userID = usuario.getUsuarioId();

            String tweet;
            String twAttach = "";

            if (inputTwitter.getText().toUpperCase().contains(HASH_TAG)) {
                tweet = inputTwitter.getText();
            } else {
                tweet = inputTwitter.getText() + " " + inputHashtag.getText();
            }

            File attach = attachmentTW.getSelectedFile();
            if (attach != null) {
                if (attach.length() <= 3000000) {
                    List<String> attch = new ArrayList<String>();
                    attch.add(attach.toPath().toString());
                    twAttach = Utilerias.encodeTwitterFiles(attch);
                } else {
                    JOptionPane.showMessageDialog(this, "La imagen debe ser menor igual a 3MB");
                    return;
                }
            }
            try {

                // Enviamos el twitt    
                com.adinfi.ws.publicador.IPublicador_Stub stub = (com.adinfi.ws.publicador.IPublicador_Stub) new com.adinfi.ws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
                UtileriasWS.setEndpoint(stub);
                res = stub.enviarTweet(userID, tweet, twAttach);

                if (res.getResultCd().equals("000") && res.getResultMsg().equals("OK")) {
                    JOptionPane.showMessageDialog(this, "Se ha enviado correctamente");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, res.getResultMsg());
                }

            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "El servicio de envío de tweet no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            } finally {
                Cursor.getDefaultCursor();
            }
        } else {
            JOptionPane.showMessageDialog(this, "El tweet debe contener 140 caracteres o menos y no puede ser vacio", "Inane error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* Validar no exceda los 140 caracteres de twitter */

    private boolean validateTwitter() {
        boolean b = true;
        if (inputTwitter.getText().length() > chars || inputTwitter.getText().length() == 0) {
            b = false;
        }
        return b;
    }

    /* IMAGE DATA RESIZE */
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return bi;
    }

    /*Carga los usuarios para agregarlos al combo autores */
    private void loadUsers() {
        cboAutores.removeAllItems();
        cboAutores.addItem(InstanceContext.getInstance().getUsuario().getNombre());
    }

    private void allowHashtagModify() {

        String perfil = InstanceContext.getInstance().getUsuario().getPerfil();

        if (perfil.equals("Administrador")) {
            inputHashtag.setEnabled(true);
        }

    }


    private void enviarTwittActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarTwittActionPerformed
        sendTweet();
    }//GEN-LAST:event_enviarTwittActionPerformed

    private void selectImageTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectImageTwitterActionPerformed
        selectImage();
    }//GEN-LAST:event_selectImageTwitterActionPerformed

    private void removeAttchTWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAttchTWActionPerformed
        imageInLabel.setVisible(false);
        removeAttchTW.setVisible(false);
        attachmentTW.setSelectedFile(null);
        selectedFile = false;

        //Regresar los 23 caracteres de la imagen.
        int chaI = (23 + Integer.valueOf(counterTwitteer.getText()));
        counterTwitteer.setText(String.valueOf(chaI));
        if (chaI < 0) {
            counterTwitteer.setForeground(Color.RED);
            enviarTwitt.setEnabled(false);
        } else {
            counterTwitteer.setForeground(Color.BLACK);
            enviarTwitt.setEnabled(true);
        }
        //countChar();
    }//GEN-LAST:event_removeAttchTWActionPerformed

    private void cboAutoresComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_cboAutoresComponentShown
    }//GEN-LAST:event_cboAutoresComponentShown

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        loadUsers();
        countChar();
    }//GEN-LAST:event_formComponentShown

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int result = JOptionPane.showConfirmDialog(
                MainApp.getApplication().getMainFrame(), "¿Los datos capturados se perderán?, ¿Desea Continuar?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(
                MainApp.getApplication().getMainFrame(), "¿Los datos capturados se perderán?, ¿Desea Continuar?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void cboAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAutoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAutoresActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboAutores;
    private javax.swing.JLabel counterTwitteer;
    private javax.swing.JButton enviarTwitt;
    private javax.swing.JLabel imageInLabel;
    private javax.swing.JTextField inputHashtag;
    private javax.swing.JTextArea inputTwitter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton removeAttchTW;
    private javax.swing.JButton selectImageTwitter;
    // End of variables declaration//GEN-END:variables
JFileChooser attachmentTW = new JFileChooser();
}
