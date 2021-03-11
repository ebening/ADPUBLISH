package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasSSH;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.administration.JComboCheckBox;
import com.adinfi.formateador.view.publish.*;
import com.adinfi.formateador.view.resources.CCheckBox;
import com.adinfi.ws.publicador.DBResult;
import com.hexidec.ekit.EkitCore;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

/**
 *
 * @author Josue Sanchez
 */
public class Notify extends javax.swing.JDialog {

    private EkitCore ekitCore;
    private JTextPane sourceText;

    public Notify(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void init() {
        loadUsers();
        initTextEditor();
        subject.setText("");
    }

    private void initTextEditor() {
        try {
            Vector<String> v = Utilerias.getOptionsToEkitcore(Utilerias.EKIT_CORE_TYPE.TEXT);
            ekitCore = Utilerias.getEkitCore();
            sourceText = ekitCore.getTextPane();
            add(ekitCore, BorderLayout.CENTER);

            cboIdiomas = new JComboBox();
            cboIdiomas.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Sin corrección", "Español", "Inglés"}));
            cboIdiomas.setSelectedIndex(1);
            cboIdiomas.setSize(50, 20);
            cboIdiomas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cboIdiomasActionPerformed(e);
                }
            });
            Utilerias.addEkitcoreLayout(textEditorCointainer, ekitCore, v, cboIdiomas, null, null, null, null);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void cboIdiomasActionPerformed(java.awt.event.ActionEvent evt) {
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
                    SpellChecker.unregister(sourceText);
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).info(ex);
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
            SpellChecker.register(sourceText);
            SpellCheckerOptions sco = new SpellCheckerOptions();
            sco.setCaseSensitive(true);
            sco.setSuggestionsLimitMenu(10);
            sco.setLanguageDisableVisible(false);
            sco.setIgnoreWordsWithNumbers(true);
            JPopupMenu popup = SpellChecker.createCheckerPopup(sco);
            sourceText.addMouseListener(new PopupListener(popup));

        } catch (NullPointerException | MalformedURLException ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void loadUsers() {
        try {
            com.adinfi.ws.IAccess_Stub stub = (com.adinfi.ws.IAccess_Stub) new com.adinfi.ws.Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            com.adinfi.ws.ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);

            if (array != null && array.getUsuario() != null) {
                com.adinfi.ws.Usuario[] users = array.getUsuario();

                cboAutores.removeAllItems();
                List<String> auxLst = new ArrayList<>();
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    //com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    com.adinfi.formateador.bos.seguridad.Usuario u
                            = new com.adinfi.formateador.bos.seguridad.Usuario(
                                    false,
                                    o.getUsuarioId(),
                                    o.getUsuarioNT(),
                                    o.getNombre(),
                                    o.getCorreo(),
                                    o.getExtension(),
                                    o.getFechaAlta(),
                                    o.getUltimoAcceso(),
                                    o.getIsAutor(),
                                    o.getPerfilId(),
                                    o.getPerfil(),
                                    o.getMiVectorId(),
                                    o.getIsDirectorio()
                            );
                    cboAutores.addItem(new CCheckBox(u.toString(), u));

                    //cboAutores.addItem(new JCheckBox(u.toString()));
                }
                /*
                 for(int i=0;i<cboAutores.getItemCount();i++){
                 JCheckBox check= (JCheckBox)cboAutores.getItemAt(i);
                 boolean bSelected= false;
                 if(auxLst.contains(check.getText())){
                 bSelected= true;
                 }
                 check.setSelected(bSelected);
                 }
                 */
            }

            for (int i = 0; i < cboAutores.getItemCount(); i++) {
                CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                cb.setSelected(false);
            }
        } catch (RemoteException e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private int getAutor() {
        CCheckBox obj = (CCheckBox) cboAutores.getSelectedItem();
        com.adinfi.formateador.bos.seguridad.Usuario o = (com.adinfi.formateador.bos.seguridad.Usuario) obj.getObject();
        return o.getUsuarioId();
    }

    private String getAuthors() {

        String authors = "";

        if (cboAutores.getItemCount() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("<AUTORES>");
            for (int i = 0; i < cboAutores.getItemCount(); i++) {
                CCheckBox cb = (CCheckBox) cboAutores.getItemAt(i);
                if (cb.isSelected()) {
                    com.adinfi.formateador.bos.seguridad.Usuario doc = (com.adinfi.formateador.bos.seguridad.Usuario) cb.getObject();
                    sb.append("<AUTOR_ID>");
                    sb.append(doc.getUsuarioId());
                    sb.append("</AUTOR_ID>");
                }
            }
            sb.append("</AUTORES>");
            authors = sb.toString();
        }

        return authors;
    }

    private void saveNotify() {

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        subject = new javax.swing.JTextField();
        cboAutores = new JComboCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        btnGen = new javax.swing.JButton();
        textEditorCointainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Notificación");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setText("Notificar a:");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Subject:");
        jLabel2.setName("jLabel2"); // NOI18N

        subject.setName("subject"); // NOI18N

        cboAutores.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        cboAutores.setName("cboAutores"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jButton3.setText("Enviar");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3);

        jButton1.setText("Cancelar");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel3.setText("Link del Documento:");
        jLabel3.setName("jLabel3"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        btnGen.setText("Gen URL");
        btnGen.setName("btnGen"); // NOI18N
        btnGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGen))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGen))
                .addContainerGap())
        );

        textEditorCointainer.setName("textEditorCointainer"); // NOI18N
        textEditorCointainer.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(textEditorCointainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 292, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(31, 31, 31)
                                        .addComponent(subject)))))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(textEditorCointainer, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
    }//GEN-LAST:event_formComponentShown

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        int idlogged = 0;
        String email1 = ekitCore.getTextPane().getText();
        int id = getAutor();
        String subj = subject.getText();
        DBResult res = null;

        String autores = getAuthors();

        DocumentBO docBO = MainView.main.getScrDocument().getDocBO();

        boolean b = true;

        if (email1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El mensaje no debe ir vacio.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.ERROR_MESSAGE);
            b = false;
        }
        
        if(subject.getText().isEmpty()){
          JOptionPane.showMessageDialog(this, "El tema no debe ir vacio.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.ERROR_MESSAGE);
          b = false;
        }
        
        if (MainView.main.getScrDocument() == null || MainView.main.getScrDocument().getDocBO() == null) {
            Utilerias.showMessage(MainView.main, "Se debe tener abierto un documento para enviar una notificación", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (id <= 0) {
            JOptionPane.showMessageDialog(this, "Debe elegir al menos un destinatario.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.ERROR_MESSAGE);
            b = false;
        }

        if (b) {

            try {
                com.adinfi.ws.publicador.IPublicador_Stub stub = (com.adinfi.ws.publicador.IPublicador_Stub) new com.adinfi.ws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
                UtileriasWS.setEndpoint(stub);
                res = stub.enviarNotificacion(
                        String.valueOf(idlogged),
                        autores,
                        subj,
                        docBO != null && docBO.getSubjectBO() != null ? docBO.getSubjectBO().getName() : null,
                        docBO != null ? docBO.getDocumentName() : null,
                        email1,
                        jTextField2.getText().trim());
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "El servicio de notificación no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            }

            if (res.getResultCd().equals("000") && res.getResultMsg().equals("OK")) {
                //guardar en bd
                //        try {
                //            Clob cl = new SerialClob(text.toCharArray());
                //            boSend.setText(cl);
                //        } catch (SQLException ex) {
                //            Logger.getLogger(SendPublish.class.getName()).log(Level.SEVERE, null, ex);
                //        }

                JOptionPane.showMessageDialog(null, "Se envio correctamente");
                dispose();

            } else {
                //no se guardo ni se envio.
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "Se perderán todos los datos que haya capturado, ¿esta seguro de que sea cancelar ?", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            //limpiar datos de la pantalla
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenActionPerformed
        // TODO add your handling code here:
        genURL();
    }//GEN-LAST:event_btnGenActionPerformed

    private void genURL() {
        DocumentBO docBO = MainView.main.getScrDocument().getDocBO();
        if (docBO == null) {
            return;
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = new Date();

        String rutaURL = UtileriasSSH.getInstance().sendFilesSsh(null, docBO, sdf.format(fecha), Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY);
        jTextField2.setText(rutaURL == null ? "" : rutaURL);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGen;
    private javax.swing.JComboBox cboAutores;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField subject;
    private javax.swing.JPanel textEditorCointainer;
    // End of variables declaration//GEN-END:variables
    private JComboBox cboIdiomas;

}
