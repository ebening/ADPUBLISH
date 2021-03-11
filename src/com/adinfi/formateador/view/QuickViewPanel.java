package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.dao.QuickViewDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.administration.tablemodel.ButtonEditor;
import com.adinfi.formateador.view.administration.tablemodel.ButtonRenderer;
import com.adinfi.formateador.view.administration.tablemodel.ImageIconRenderer;
import com.adinfi.formateador.view.administration.tablemodel.SearchTableModel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author USUARIO
 */
public class QuickViewPanel extends javax.swing.JPanel {

    public int numPag = 1;
    public int numEPP = 20;
    public List<DocumentSearchBO> lstETot = null;
    /**
     * Creates new form QuickViewPanel
     */
    public QuickViewPanel() {
        initComponents();
    }
    
    public List<DocumentSearchBO> getElementosIniciales(){
        List<DocumentSearchBO> retVal = null;
        numPag = 1;
        try {
            retVal = new ArrayList<>();
            if(lstETot == null || lstETot.size() < 0){
                return retVal;
            }

            for (int i = 0; i< numEPP; i++) {
                if(i >= lstETot.size()){
                    break;
                }
                
                retVal.add(lstETot.get(i));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }
    
    public List<DocumentSearchBO> getElementosAnteriores(){
        List<DocumentSearchBO> retVal = null;
        
        numPag--;
        if(numPag <= 0){
            numPag = 1;
        }
        
        try {
            retVal = new ArrayList<>();
            if(lstETot == null || lstETot.size() < 0){
                return retVal;
            }
            
            int valFin = numPag * numEPP;
            int valIni = valFin - numEPP;

            for (int i = valIni; i< valFin; i++) {
                if(i >= lstETot.size()){
                    break;
                }
                
                retVal.add(lstETot.get(i));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }
    
    public List<DocumentSearchBO> getElementosSiguientes(){
        List<DocumentSearchBO> retVal = null;
        
        numPag++;
        
        int valFin = numPag * numEPP;
        int valIni = valFin - numEPP;
        
        if(lstETot != null && valIni >= lstETot.size()){
            numPag--;
            if(numPag <= 0){
                numPag = 1;
            }
        }
        
        try {
            retVal = new ArrayList<>();
            if(lstETot == null || lstETot.size() < 0){
                return retVal;
            }
            
            valFin = numPag * numEPP;
            valIni = valFin - numEPP;

            for (int i = valIni; i< valFin; i++) {
                if(i >= lstETot.size()){
                    break;
                }
                
                retVal.add(lstETot.get(i));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }
    
    public List<DocumentSearchBO> getElementosFinales(){
        List<DocumentSearchBO> retVal = null;
        
        if(lstETot == null || lstETot.size() < 0){
            return new ArrayList<>();
        }else{
            numPag = (int) lstETot.size() / numEPP;
            int residuo = lstETot.size() % numEPP;
            if(residuo > 0)
                numPag++;
            
            if(numPag <= 0){
                numPag = 1;
            }
        }
        
        try {
            retVal = new ArrayList<>();
            
            int valFin = numPag * numEPP;
            int valIni = valFin - numEPP;

            for (int i = valIni; i< valFin; i++) {
                if(i >= lstETot.size()){
                    break;
                }
                
                retVal.add(lstETot.get(i));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        recentPanel = new javax.swing.JPanel();
        mainScrollPanelRecientes = new javax.swing.JScrollPane();
        favoritePanel = new javax.swing.JPanel();
        mainScrollPanelFavoritos = new javax.swing.JScrollPane();
        noPublishPanel = new javax.swing.JPanel();
        mainScrollPanelNoPublicados = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        btnAdelante = new javax.swing.JButton();
        btnFin = new javax.swing.JButton();
        txtPage = new javax.swing.JTextField();

        setName("Form"); // NOI18N
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(QuickViewPanel.class);
        tabbedPane.setFont(resourceMap.getFont("tabbedPane.font")); // NOI18N
        tabbedPane.setName("tabbedPane"); // NOI18N

        recentPanel.setFont(resourceMap.getFont("recentPanel.font")); // NOI18N
        recentPanel.setName("recentPanel"); // NOI18N
        recentPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                recentPanelComponentShown(evt);
            }
        });
        recentPanel.setLayout(new java.awt.BorderLayout());

        mainScrollPanelRecientes.setName("mainScrollPanelRecientes"); // NOI18N
        mainScrollPanelRecientes.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                mainScrollPanelRecientesComponentShown(evt);
            }
        });
        recentPanel.add(mainScrollPanelRecientes, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(resourceMap.getString("recentPanel.TabConstraints.tabTitle"), recentPanel); // NOI18N

        favoritePanel.setName("favoritePanel"); // NOI18N
        favoritePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                favoritePanelComponentShown(evt);
            }
        });
        favoritePanel.setLayout(new java.awt.BorderLayout());

        mainScrollPanelFavoritos.setName("mainScrollPanelFavoritos"); // NOI18N
        favoritePanel.add(mainScrollPanelFavoritos, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(resourceMap.getString("favoritePanel.TabConstraints.tabTitle"), favoritePanel); // NOI18N

        noPublishPanel.setName("noPublishPanel"); // NOI18N
        noPublishPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                noPublishPanelComponentShown(evt);
            }
        });
        noPublishPanel.setLayout(new java.awt.BorderLayout());

        mainScrollPanelNoPublicados.setName("mainScrollPanelNoPublicados"); // NOI18N
        noPublishPanel.add(mainScrollPanelNoPublicados, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(resourceMap.getString("noPublishPanel.TabConstraints.tabTitle"), noPublishPanel); // NOI18N

        add(tabbedPane, java.awt.BorderLayout.CENTER);
        tabbedPane.getAccessibleContext().setAccessibleName(resourceMap.getString("tabbedPane.AccessibleContext.accessibleName")); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        btnInicio.setText(resourceMap.getString("btnInicio.text")); // NOI18N
        btnInicio.setName("btnInicio"); // NOI18N
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        btnAtras.setText(resourceMap.getString("btnAtras.text")); // NOI18N
        btnAtras.setName("btnAtras"); // NOI18N
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        btnAdelante.setText(resourceMap.getString("btnAdelante.text")); // NOI18N
        btnAdelante.setName("btnAdelante"); // NOI18N
        btnAdelante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdelanteActionPerformed(evt);
            }
        });

        btnFin.setText(resourceMap.getString("btnFin.text")); // NOI18N
        btnFin.setName("btnFin"); // NOI18N
        btnFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinActionPerformed(evt);
            }
        });

        txtPage.setEditable(false);
        txtPage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPage.setText(resourceMap.getString("txtPage.text")); // NOI18N
        txtPage.setName("txtPage"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(570, Short.MAX_VALUE)
                .addComponent(btnInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAtras)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdelante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFin))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdelante)
                    .addComponent(btnFin)
                    .addComponent(btnAtras)
                    .addComponent(btnInicio)
                    .addComponent(txtPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void mainScrollPanelRecientesComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainScrollPanelRecientesComponentShown

    }//GEN-LAST:event_mainScrollPanelRecientesComponentShown

    private void recentPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_recentPanelComponentShown
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar());
        worker.execute();
    }//GEN-LAST:event_recentPanelComponentShown

    private void favoritePanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_favoritePanelComponentShown
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar());
        worker.execute();
    }//GEN-LAST:event_favoritePanelComponentShown

    private void noPublishPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_noPublishPanelComponentShown
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar());
        worker.execute();
    }//GEN-LAST:event_noPublishPanelComponentShown

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        /*MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar());
        worker.execute();*/
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar());
            worker.execute();
        }
    }//GEN-LAST:event_formComponentShown

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        /*MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar());
        worker.execute();*/
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }
    }//GEN-LAST:event_formFocusGained

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar(), getElementosIniciales());
            worker.execute();
        }
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar(), getElementosAnteriores());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar(), getElementosAnteriores());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar(), getElementosAnteriores());
            worker.execute();
        }
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnAdelanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdelanteActionPerformed
        
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar(), getElementosSiguientes());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar(), getElementosSiguientes());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar(), getElementosSiguientes());
            worker.execute();
        }
    }//GEN-LAST:event_btnAdelanteActionPerformed

    private void btnFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinActionPerformed
        
        MainView.main.getProgressBar().setIndeterminate(true);
        MainView.main.getProgressBar().setVisible(true);
        if(tabbedPane.getSelectedIndex() == 0){
            SearchDocumentRecientesWorker worker = new SearchDocumentRecientesWorker(MainView.main.getProgressBar(), getElementosFinales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 1){
            SearchDocumentFavoritosWorker worker = new SearchDocumentFavoritosWorker(MainView.main.getProgressBar(), getElementosFinales());
            worker.execute();
        }else if(tabbedPane.getSelectedIndex() == 2){
            SearchDocumentNoPublicadosWorker worker = new SearchDocumentNoPublicadosWorker(MainView.main.getProgressBar(), getElementosFinales());
            worker.execute();
        }
    }//GEN-LAST:event_btnFinActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdelante;
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnFin;
    private javax.swing.JButton btnInicio;
    private javax.swing.JPanel favoritePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane mainScrollPanelFavoritos;
    private javax.swing.JScrollPane mainScrollPanelNoPublicados;
    private javax.swing.JScrollPane mainScrollPanelRecientes;
    private javax.swing.JPanel noPublishPanel;
    private javax.swing.JPanel recentPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField txtPage;
    // End of variables declaration//GEN-END:variables
    private JXTable jxTableFavoritos;
    private JXTable jxTableRecientes;
    private JXTable jxTableNoEnviados;

    class SearchDocumentFavoritosWorker extends SwingWorker<List<DocumentSearchBO>, List<DocumentSearchBO>> {

        private Exception exception;
        private final JProgressBar bar;
        private List<DocumentSearchBO> lstPag;
        private boolean isPageActive = false;

        SearchDocumentFavoritosWorker(final JProgressBar bar, List<DocumentSearchBO> lstPagInput) {
            this.bar = bar;
            this.lstPag = lstPagInput;
            this.isPageActive = true;
        }

        SearchDocumentFavoritosWorker(final JProgressBar bar) {
            this.bar = bar;
        }

        @Override
        protected List<DocumentSearchBO> doInBackground() {
            List<DocumentSearchBO> list = null;
            try {
                if(isPageActive){
                    list = lstPag;
                }else{
                    QuickViewDAO dao = new QuickViewDAO();
                    list = dao.get(QuickViewDAO.SEARCH_TYPE.FAVORITOS);
                }
            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
            }
            return list;
        }

        @Override
        protected void done() {
            List<DocumentSearchBO> list = null;
            try {
                list = get();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {
                if(!isPageActive){
                    lstETot = list;
                    list = getElementosIniciales();
                }

                SearchTableModel model = new SearchTableModel(list);
                createJXTableFavoritos(model);
                setRenderEditorToColumns(jxTableFavoritos);
                bar.setVisible(false);
            }
        }

        private void setRenderEditorToColumns(JXTable table) {
            
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            //table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_3.get()));

            table.getColumn(StatementConstant.SC_2.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_2.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableFavoritos, ButtonEditor.ACTION.NEW_DOCUMENT));
            
            table.addColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.addColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.addColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));
            
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_0.get()));
            
        }

        private void createJXTableFavoritos(SearchTableModel model) {
            if (jxTableFavoritos == null) {
                jxTableFavoritos = new JXTable();
                jxTableFavoritos.setHorizontalScrollEnabled(true);
                jxTableFavoritos.setColumnControlVisible(true);
                int margin = 5;
                jxTableFavoritos.packTable(margin);
                MouseMotionAdapter mml = new MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Point p = e.getPoint();
                        if (jxTableFavoritos.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                            jxTableFavoritos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        } else {
                            jxTableFavoritos.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                jxTableFavoritos.addMouseMotionListener(mml);
                jxTableFavoritos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                mainScrollPanelFavoritos.setViewportView(jxTableFavoritos);
            }
            jxTableFavoritos.setModel(model);
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            jxTableFavoritos.setDefaultRenderer(Object.class, stcr);
            Utilerias.formatTablex(jxTableFavoritos);
        }
    }

    class SearchDocumentRecientesWorker extends SwingWorker<List<DocumentSearchBO>, List<DocumentSearchBO>> {

        private Exception exception;
        private final JProgressBar bar;
        private List<DocumentSearchBO> lstPag;
        private boolean isPageActive = false;

        SearchDocumentRecientesWorker(final JProgressBar bar, List<DocumentSearchBO> lstPagInput) {
            this.bar = bar;
            this.lstPag = lstPagInput;
            this.isPageActive = true;
        }

        SearchDocumentRecientesWorker(final JProgressBar bar) {
            this.bar = bar;
        }

        @Override
        protected List<DocumentSearchBO> doInBackground() {
            List<DocumentSearchBO> list = null;
            try {
                if(isPageActive){
                    list = lstPag;
                }else{
                    QuickViewDAO dao = new QuickViewDAO();
                    list = dao.get(QuickViewDAO.SEARCH_TYPE.RECIENTES);
                }
            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
            }
            return list;
        }

        @Override
        protected void done() {
            List<DocumentSearchBO> list = null;
            try {
                list = get();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {
                if(!isPageActive){
                    lstETot = list;
                    list = getElementosIniciales();
                }
                
                SearchTableModel model = new SearchTableModel(list);
                createJXTableFavoritos(model);
                setRenderEditorToColumns(jxTableRecientes);
                bar.setVisible(false);
            }
        }

        private void setRenderEditorToColumns(JXTable table) {
            
            table.getColumn(StatementConstant.SC_0.get()).setCellRenderer(new ImageIconRenderer());
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_2.get()));

            table.getColumn(StatementConstant.SC_9.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_9.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableRecientes, ButtonEditor.ACTION.PDF));

            table.getColumn(StatementConstant.SC_10.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_10.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableRecientes, ButtonEditor.ACTION.HTML));

            table.getColumn(StatementConstant.SC_11.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_11.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableRecientes, ButtonEditor.ACTION.EDIT_DOCUMENT));

            table.getColumn(StatementConstant.SC_12.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_12.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableRecientes, ButtonEditor.ACTION.EDIT_PUBLISH));
            
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_13.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_15.get()-1));

            

        }
        


        private void createJXTableFavoritos(SearchTableModel model) {
            if (jxTableRecientes == null) {
                jxTableRecientes = new JXTable();
                jxTableRecientes.setHorizontalScrollEnabled(true);
                jxTableRecientes.setColumnControlVisible(true);
                int margin = 5;
                jxTableRecientes.packTable(margin);
                MouseMotionAdapter mml = new MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Point p = e.getPoint();
                        if (jxTableRecientes.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                            jxTableRecientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        } else {
                            jxTableRecientes.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                jxTableRecientes.addMouseMotionListener(mml);
                jxTableRecientes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                mainScrollPanelRecientes.setViewportView(jxTableRecientes);
            }
            jxTableRecientes.setModel(model);
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            jxTableRecientes.setDefaultRenderer(Object.class, stcr);
            Utilerias.formatTablex(jxTableRecientes);
        }

    }

    class SearchDocumentNoPublicadosWorker extends SwingWorker<List<DocumentSearchBO>, List<DocumentSearchBO>> {

        private Exception exception;
        private final JProgressBar bar;
        private List<DocumentSearchBO> lstPag;
        private boolean isPageActive = false;

        SearchDocumentNoPublicadosWorker(final JProgressBar bar, List<DocumentSearchBO> lstPagInput) {
            this.bar = bar;
            this.lstPag = lstPagInput;
            this.isPageActive = true;
        }

        SearchDocumentNoPublicadosWorker(final JProgressBar bar) {
            this.bar = bar;
        }

        @Override
        protected List<DocumentSearchBO> doInBackground() {
            List<DocumentSearchBO> list = null;
            try {
                if(isPageActive){
                    list = lstPag;
                }else{
                    QuickViewDAO dao = new QuickViewDAO();
                    list = dao.get(QuickViewDAO.SEARCH_TYPE.NO_PUBLICADOS);
                }
            } catch (Exception ex) {
                exception = ex;
                Utilerias.logger(getClass()).info(ex);
            }
            return list;
        }

        @Override
        protected void done() {
            List<DocumentSearchBO> list = null;
            try {
                list = get();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
                exception = ex;
            } finally {
                if(!isPageActive){
                    lstETot = list;
                    list = getElementosIniciales();
                }
                
                SearchTableModel model = new SearchTableModel(list);
                createJXTableFavoritos(model);
                setRenderEditorToColumns(jxTableNoEnviados);
                bar.setVisible(false);
            }
        }

        private void setRenderEditorToColumns(JXTable table) {
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.getColumn(StatementConstant.SC_0.get()).setCellRenderer(new ImageIconRenderer());

            table.getColumn(StatementConstant.SC_9.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_9.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableNoEnviados, ButtonEditor.ACTION.PDF));

            table.getColumn(StatementConstant.SC_10.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_10.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableNoEnviados, ButtonEditor.ACTION.HTML));

            table.getColumn(StatementConstant.SC_11.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_11.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableNoEnviados, ButtonEditor.ACTION.EDIT_DOCUMENT));

            table.getColumn(StatementConstant.SC_12.get()).setCellRenderer(new ButtonRenderer());
            table.getColumn(StatementConstant.SC_12.get()).setCellEditor(new ButtonEditor(new JCheckBox(), jxTableNoEnviados, ButtonEditor.ACTION.EDIT_PUBLISH));
            
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_13.get()));
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_15.get()-1));
        }

        private void createJXTableFavoritos(SearchTableModel model) {
            if (jxTableNoEnviados == null) {
                jxTableNoEnviados = new JXTable();
                jxTableNoEnviados.setHorizontalScrollEnabled(true);
                jxTableNoEnviados.setColumnControlVisible(true);
                int margin = 5;
                jxTableNoEnviados.packTable(margin);
                MouseMotionAdapter mml = new MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Point p = e.getPoint();
                        if (jxTableNoEnviados.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                            jxTableNoEnviados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        } else {
                            jxTableNoEnviados.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                jxTableNoEnviados.addMouseMotionListener(mml);
                jxTableNoEnviados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                mainScrollPanelNoPublicados.setViewportView(jxTableNoEnviados);
            }
            jxTableNoEnviados.setModel(model);
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            jxTableNoEnviados.setDefaultRenderer(Object.class, stcr);
            Utilerias.formatTablex(jxTableNoEnviados);
        }

    }
    
}
