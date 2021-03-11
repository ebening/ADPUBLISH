package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.seguridad.Perfil;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfPerfil;
import com.adinfi.ws.ArrayOfSeccion;
import com.adinfi.ws.DBResult;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Seccion;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Josue Sanchez
 */
public class Profiles extends javax.swing.JDialog {

    private static final long serialVersionUID = 4648172894076113183L;
    private final String DELETE_MESSAGE = "¿Está seguro de dar de baja el perfil?";
    private Hashtable<String, TreeProfile> table;

    JCheckBoxTree cbt = new JCheckBoxTree();
    
    public Profiles(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        table = new Hashtable<>();
        bttnSave.setVisible(false);
        bttnEdit.setVisible(false);

    }

    @SuppressWarnings("unchecked")
    private void setTree(Seccion[] secciones) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Seleccionar todos");
        DefaultTreeModel modelo = new DefaultTreeModel(root);
        int x = 0;
        for (Seccion o : secciones) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(o.getDescripcion());
            
            //node.get
            
            modelo.insertNodeInto(node, root, x);
            

            ArrayOfSeccion arr = o.getChildren();
            Seccion[] sec = arr.getSeccion();
            int y = 0;
            table.put(Arrays.toString(node.getPath()), new TreeProfile(o.getSeccionId(), o.getIsActiv(), o.getNombre(), o.getDescripcion()));
            for (Seccion s : sec) {
                DefaultMutableTreeNode nodeb = new DefaultMutableTreeNode(s.getDescripcion());
                modelo.insertNodeInto(nodeb, node, y);
                table.put(Arrays.toString(nodeb.getPath()), new TreeProfile(s.getSeccionId(), s.getIsActiv(), s.getNombre(), s.getDescripcion()));
                y++;
            }
        }

        
        cbt.setModel(modelo);
        cbt.updateUI();
        selectedItems(modelo);

        for (int i = 0; i < cbt.getRowCount(); i++) {
            cbt.expandRow(i);
        }
        treeContainer.setViewportView(cbt);
    }

    private void selectedItems(DefaultTreeModel modelo) {
        DefaultMutableTreeNode root_ = (DefaultMutableTreeNode) modelo.getRoot();
        if (root_ != null) {

            for (int i = 0; i < root_.getChildCount(); i++) {

                TreeNode n = root_.getChildAt(i);              
                TreePath tp = cbt.getPathForRow(i + 1);
                
                String keyTable = tp.toString();
                boolean selected = false;
                if (table.get(keyTable) != null) {
                    selected = table.get(keyTable).isSelected();
                }

                cbt.checkNode(tp, selected);
                
                DefaultMutableTreeNode nod = (DefaultMutableTreeNode) tp.getLastPathComponent();
                for (int j = 0; j < nod.getChildCount(); j++) {

                    TreeNode innerNode = nod.getChildAt(j);
                    TreePath tp2 = getPath(innerNode);
                    keyTable = tp2.toString();
                    selected = false;
                    if (table.get(keyTable) != null) {
                        selected = table.get(keyTable).isSelected();
                    }
                    cbt.checkNode(tp2, selected);
                }

            }
        }
    }

    private TreePath getPath(TreeNode treeNode) {
        List<Object> nodes = new ArrayList<>();
        if (treeNode != null) {
            nodes.add(treeNode);
            treeNode = treeNode.getParent();
            while (treeNode != null) {
                nodes.add(0, treeNode);
                treeNode = treeNode.getParent();
            }
        }
        return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
    }

    private List<Perfil> getProfiles() {
        List<Perfil> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfPerfil array = stub.perfiles(GlobalDefines.WS_INSTANCE);
            if (array != null && array.getPerfil() != null) {

                for (com.adinfi.ws.Perfil o : array.getPerfil()) {
                    Perfil p = new Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    list.add(p);
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de perfiles no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
        return list;
    }

    private void fillList(List<Perfil> list) {
        profileList.setListData(list.toArray(new Perfil[list.size()]));
    }

    private boolean saveProfileSections(int idPerfil) {
        boolean saved = false;
        int n = cbt.getRowCount();
        StringBuilder xml = new StringBuilder("<Secciones>");
        for (int i = 1; i < n; i++) {
            TreePath path = cbt.getPathForRow(i);
            if (cbt.isNodeSelected(path)) {
                Integer sectionID = table.get(path.toString()).getIdProfile();
                if (sectionID != null) {
                    xml.append("<Seccion>")
                            .append(sectionID)
                            .append("</Seccion>");
                }
            }
        }
        xml.append("</Secciones>");

        if (xml.toString().isEmpty() == false) {
            int idUsuario = InstanceContext.getInstance().getUsuario().getUsuarioId();
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            try {
                stub.modificarSecciones(GlobalDefines.WS_INSTANCE, idPerfil, xml.toString(), idUsuario, "192.168.56.1");
                saved = true;
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "El servicio de modificar secciones no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(ex);
            }
            getProfileSections();
        }
        return saved;
    }

    public void getProfilesList() {
        List<Perfil> list = getProfiles();
        fillList(list);
    }

    private void deleteProfile(int id) {
        if (id > 0) {
            int cu = containUser(id);
            if (cu != 0) {
                JOptionPane.showMessageDialog(this, "El perfil cuenta con " + cu + " usuarios asociados. No es posible eliminarlo.");
                return;
            }

            String comment;
            //int result = JOptionPane.showConfirmDialog(
            //MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
            comment = JOptionPane.showInputDialog(null, DELETE_MESSAGE + "\nComentario:", "\nComentario:", JOptionPane.QUESTION_MESSAGE);

            //if (comment == JOptionPane.YES_OPTION) {
            if (!comment.isEmpty()) {
                try {
                    String ip = Utilerias.getIP();
                    int idUsuario = InstanceContext.getInstance().getUsuario().getUsuarioId();
                    IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                    UtileriasWS.setEndpoint(stub);
                    DBResult result = stub.eliminarPerfil(
                            GlobalDefines.WS_INSTANCE,
                            id,
                            idUsuario,
                            ip,
                            comment);
                    getProfilesList();
                    treeContainer.setViewportView(null);
                    
                /*    switch (result.getResultCd()) {
                        case "999":
                            JOptionPane.showMessageDialog(this,result.getResultMsg());
                            break;
                        case "000":
                            JOptionPane.showMessageDialog(this,"Se eliminó correctamente");
                            this.dispose();
                            break;
                        default:
                            JOptionPane.showMessageDialog(this,"No se recibio informacion válida");
                            break;
                    } */
                } catch (RemoteException e) {
                    JOptionPane.showMessageDialog(null, "El servicio de eliminar perfil no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                    Utilerias.logger(getClass()).info(e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese un comentario para eliminar.");
            }
        }
    }

    private int containUser(int perfilId) {
        int retVal = 0;
        try {
            List<com.adinfi.formateador.bos.seguridad.Usuario> list;
            list = UtileriasWS.getAllUsers();
            retVal = list.stream().filter((u) -> (u.getPerfilId() == perfilId)).map((_item) -> 1).reduce(retVal, Integer::sum);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            retVal = -1;
        }
        return retVal;
    }

    private void getProfileSections() {
        Object selectedValue = profileList.getSelectedValue();
        int idPerfil = ((Perfil) selectedValue).getPerfilId();
        if (selectedValue != null) {
            try {
                IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                UtileriasWS.setEndpoint(stub);
                ArrayOfSeccion array = stub.secionesPorPerfil(GlobalDefines.WS_INSTANCE, idPerfil);
                Seccion[] secciones = array.getSeccion();
                List seccionesList = Arrays.asList(secciones);
                Collections.reverse(seccionesList);
                secciones = (Seccion[]) seccionesList.toArray(new Seccion[seccionesList.size()]);
                setTree(secciones);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "El servicio de secciones por perfil no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                Utilerias.logger(getClass()).info(e);
            }
        }

    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        profileList = new javax.swing.JList();
        bttnNew = new javax.swing.JButton();
        btnEditProfile = new javax.swing.JButton();
        bttnDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bttnSave = new javax.swing.JButton();
        treeContainer = new javax.swing.JScrollPane();
        bttnEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administración de Privilegios");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        profileList.setName("profileList"); // NOI18N
        profileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profileListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(profileList);

        bttnNew.setText("Nuevo");
        bttnNew.setName("bttnNew"); // NOI18N
        bttnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnNewActionPerformed(evt);
            }
        });

        btnEditProfile.setText("Renombrar");
        btnEditProfile.setName("btnEditProfile"); // NOI18N
        btnEditProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProfileActionPerformed(evt);
            }
        });

        bttnDelete.setText("Eliminar");
        bttnDelete.setName("bttnDelete"); // NOI18N
        bttnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnDeleteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Perfiles");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Secciones");
        jLabel2.setName("jLabel2"); // NOI18N

        bttnSave.setText("Guardar");
        bttnSave.setName("bttnSave"); // NOI18N
        bttnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSaveActionPerformed(evt);
            }
        });

        treeContainer.setName("treeContainer"); // NOI18N

        bttnEdit.setText("Editar");
        bttnEdit.setName("bttnEdit"); // NOI18N
        bttnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(bttnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bttnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(313, 313, 313))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(treeContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bttnEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bttnSave)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bttnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bttnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(treeContainer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bttnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bttnEdit))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnNewActionPerformed
        addProfile add = new addProfile(null, true);
        add.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getProfilesList();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                getProfilesList();
            }
        });

        add.setLocationRelativeTo(null);
        add.setVisible(true);
    }//GEN-LAST:event_bttnNewActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        List<Perfil> list = getProfiles();
        fillList(list);
    }//GEN-LAST:event_formComponentShown

    private void bttnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSaveActionPerformed
        Object selectedValue = profileList.getSelectedValue();

        if (selectedValue != null) {
            int idPerfil = ((Perfil) selectedValue).getPerfilId();
            boolean saved = saveProfileSections(idPerfil);
            if (saved == true) {
                JOptionPane.showMessageDialog(this, "Perfil guardado satisfactoriamente", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
                bttnSave.setVisible(false);
                bttnEdit.setVisible(true);
                treeContainer.setViewportView(null);
                treeContainer.revalidate();
                treeContainer.updateUI();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar perfil", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un perfil");
        }

    }//GEN-LAST:event_bttnSaveActionPerformed

    private void btnEditProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProfileActionPerformed
        Object selectedValue = profileList.getSelectedValue();
        if (selectedValue != null) {
            editProfile edit = new editProfile(null, true, (Perfil) selectedValue);

            edit.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    getProfilesList();
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    getProfilesList();
                }
            });

            edit.setLocationRelativeTo(null);
            edit.setVisible(true);

            int id = edit.getIdProfile();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un perfil");
        }
    }//GEN-LAST:event_btnEditProfileActionPerformed

    private void profileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profileListMouseClicked
        //getProfileSections();
        if (evt.getClickCount() == 2) {
            //jButton2.setVisible(true);
            //jButton4.setVisible(false);
            //treeContainer.setViewportView(null);
            getProfileSections();
            bttnEdit.setVisible(false);
            bttnSave.setVisible(true);
            JViewport jvport = (JViewport)treeContainer.getComponent(0);
            JCheckBoxTree jcbt = (JCheckBoxTree)jvport.getComponent(0);
            jcbt.addClick();
        } else if (evt.getClickCount() == 1) {
            getProfileSections();
            bttnEdit.setVisible(false);
            bttnSave.setVisible(false);
            MouseListener [] mls = cbt.getMouseListeners();
            for(MouseListener m : mls){
                cbt.removeMouseListener(m);
            }
        }
    }//GEN-LAST:event_profileListMouseClicked

    private void bttnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnDeleteActionPerformed
        Object selectedValue = profileList.getSelectedValue();
        if (selectedValue != null) {
            int idPerfil = ((Perfil) selectedValue).getPerfilId();
            deleteProfile(idPerfil);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un perfil");
        }
    }//GEN-LAST:event_bttnDeleteActionPerformed

    private void bttnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnEditActionPerformed
        // TODO add your handling code here:
//        getProfileSections();
//        jButton2.setVisible(false);
//        jButton4.setVisible(true);
    }//GEN-LAST:event_bttnEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditProfile;
    private javax.swing.JButton bttnDelete;
    private javax.swing.JButton bttnEdit;
    private javax.swing.JButton bttnNew;
    private javax.swing.JButton bttnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList profileList;
    private javax.swing.JScrollPane treeContainer;
    // End of variables declaration//GEN-END:variables

    class TreeProfile {

        private int idProfile;
        private boolean selected;
        private String nombre;
        private String descripcion;

        public TreeProfile() {
        }

        public TreeProfile(int idProfile, boolean selected, String nombre, String descripcion) {
            this.idProfile = idProfile;
            this.selected = selected;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public int getIdProfile() {
            return idProfile;
        }

        public void setIdProfile(int idProfile) {
            this.idProfile = idProfile;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         * @return the nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the descripcion
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * @param descripcion the descripcion to set
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

    }

}
