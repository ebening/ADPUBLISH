/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.RelationalFilesBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.bos.seguridad.Usuario;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.RelationalFilesDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.SearchTableCellRenderer;
import com.adinfi.formateador.view.administration.tablemodel.RelationalFilesModel;
import com.adinfi.formateador.view.administration.tablemodel.SearchTableModel;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author USUARIO
 */
public class RelationalFiles extends javax.swing.JDialog {

    UtilDateModel publicationDateFrom = new UtilDateModel();
    UtilDateModel publicationDateTo = new UtilDateModel();
    private SubjectBO selectedValue = null;

    /**
     * Creates new form RelationalFiles
     */
    class SearchDocumentWorker extends SwingWorker<List<DocumentSearchBO>, List<DocumentSearchBO>> {

        private Exception exception;
        private final JProgressBar bar;
        private final DocumentSearchBO docSearchBO;
        private JXTable jxTable;

        SearchDocumentWorker(final JProgressBar bar, DocumentSearchBO docSearchBO) {
            this.bar = bar;
            this.docSearchBO = docSearchBO;
        }

        @Override
        protected List<DocumentSearchBO> doInBackground() {
            List<DocumentSearchBO> list = null;
            try {
                DocumentDAO dao = new DocumentDAO();
                list = dao.getDocumentByCriteria(docSearchBO);
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
                SearchTableModel model = new SearchTableModel(list);
                createJXTable(model);
                bar.setVisible(false);
            }
        }

        private void createJXTable(SearchTableModel model) {
            if (jxTable == null) {
                jxTable = new JXTable();
                jxTable.setHorizontalScrollEnabled(true);
                jxTable.setColumnControlVisible(true);
                int margin = 5;
                jxTable.packTable(margin);
                MouseMotionAdapter mml = new MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Point p = e.getPoint();
                        if (jxTable.columnAtPoint(p) == SearchTableModel.HAND_CURSOR_COLUMN) {
                            jxTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        } else {
                            jxTable.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                jxTable.addMouseMotionListener(mml);
                jxTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                jxTable.addMouseListener(new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        //jxTableMouseClicked(evt);
                    }
                });

                mainScrollPanel.setViewportView(jxTable);
            }

            jxTable.setModel(model);
            Utilerias.formatTablex(jxTable);
        }

    }

    public RelationalFiles(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadDatePicker();
        loadCombos();
        searchInContentBar.setVisible(false);
        textAutoCompleter = new TextAutoCompleter(txtTema);
        loadDataSubject();


        txtTema.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                Object o = textAutoCompleter.getItemSelected();
                if (o != null && o instanceof SubjectBO) {
                    selectedValue = (SubjectBO) o;
                    System.out.println(selectedValue.getIdSubject());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Object o = textAutoCompleter.getItemSelected();
                if (o != null && o instanceof SubjectBO) {
                    selectedValue = (SubjectBO) o;
                    System.out.println(selectedValue.getIdSubject());
                }
            }
        });

    }

    private void loadDatePicker() {
        JDatePanelImpl datePanel1 = new JDatePanelImpl(publicationDateFrom);
        JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1);
        panelFecha1.add(BorderLayout.CENTER, datePicker1);

        JDatePanelImpl datePanel2 = new JDatePanelImpl(publicationDateTo);
        JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2);
        panelFecha2.add(BorderLayout.CENTER, datePicker2);
    }

    private void loadCombos() {
        DocumentTypeDAO dDAO = new DocumentTypeDAO();
        List<DocumentTypeBO> lst_dBO = dDAO.get(null, -1, 0);
        cmbTipoDoc.removeAllItems();
        DocumentTypeBO dtBO = new DocumentTypeBO();
        dtBO.setIddocument_type(0);
        dtBO.setName("Todos");
        cmbTipoDoc.addItem(dtBO);
        for (DocumentTypeBO d : lst_dBO) {
            cmbTipoDoc.addItem(d);
        }

        MarketDAO mDAO = new MarketDAO();
        List<MarketBO> mBO_list = mDAO.get(null);
        cmbMercado.removeAllItems();
         MarketBO bo_ = new MarketBO();
        bo_.setName("Seleccione");
        bo_.setIdMiVector_real("-1");
        cmbMercado.addItem(bo_);
        for (MarketBO bo : mBO_list) {
            cmbMercado.addItem(bo);
        }

        LanguageDAO lDAO = new LanguageDAO();
        List<LanguageBO> lBO_lst = lDAO.get(null);
        cmbIdioma.removeAllItems();
        LanguageBO lBO = new LanguageBO();
        lBO.setIdLanguage(0);
        lBO.setName("Todos");
        cmbIdioma.addItem(lBO);
        for (LanguageBO l : lBO_lst) {
            cmbIdioma.addItem(l);
        }

        IndustryDAO iDAO = new IndustryDAO();
        List<IndustryBO> iBO_lst = iDAO.get(null);
        cmbSector.removeAllItems();
        IndustryBO iBO = new IndustryBO();
        iBO.setIdIndustry(0);
        iBO.setName("Todos");
        cmbSector.addItem(iBO);
        for (IndustryBO i : iBO_lst) {
            cmbSector.addItem(i);
        }

        List<Usuario> aut_lst = UtileriasWS.getAuthor();
        cmbAutor.removeAllItems();
        Usuario aut = new Usuario();
        aut.setUsuarioId(0);
        aut.setNombre("Todos");
        cmbAutor.addItem(aut);
        for (Usuario u : aut_lst) {
            cmbAutor.addItem(u);
        }

        txtTema.requestFocusInWindow();
    }

    // Obtener el id del mercado seleccionado
    private int getSelectedMarket() {
        int idMarket = 0;
        Object obj = cmbMercado.getSelectedItem();
        if (obj != null && cmbMercado.getSelectedIndex() != 0) {
            MarketBO bo = (MarketBO) obj;
            idMarket = Integer.parseInt(bo.getIdMiVector_real());
        }
        return idMarket;
    }

    // Obtener el id del Tipo de documento seleccionado
    private int getSelectedDocumentType() {
        int id = 0;
        Object obj = cmbTipoDoc.getSelectedItem();
        if (obj != null && cmbTipoDoc.getSelectedIndex() != 0) {
            DocumentTypeBO bo = (DocumentTypeBO) obj;
            id = bo.getIddocument_type();
        }
        return id;
    }

//-----------------------------------------------------
    //  Obtener el id del tema (Subject)
//-----------------------------------------------------
    // Obtener el id del Lenguaje seleccionado
    private int getSelectedLanguage() {
        int id = 0;
        Object obj = cmbIdioma.getSelectedItem();
        if (obj != null && cmbIdioma.getSelectedIndex() != 0) {
            LanguageBO bo = (LanguageBO) obj;
            id = bo.getIdLanguage();
        }
        return id;
    }

    // Obtener el id del sector seleccionado
    private int getSelectedIndustry() {
        int id = 0;
        Object obj = cmbSector.getSelectedItem();
        if (obj != null && cmbSector.getSelectedIndex() != 0) {
            IndustryBO bo = (IndustryBO) obj;
            id = bo.getIdIndustry();
        }
        return id;
    }

    // Obtener el nombre del author seleccionado
    private String getSelectedAuthor() {
        String name = "";
        Object obj = cmbAutor.getSelectedItem();
        if (obj != null && cmbAutor.getSelectedIndex() != 0) {
            com.adinfi.ws.Usuario usr = (com.adinfi.ws.Usuario) obj;
            name = usr.getNombre();
        }
        return name;
    }

    // obtener fecha inicial
    public Date getDateFrom() {
        Date from = publicationDateFrom.getValue();
        Date publicationFrom = null;
        if (from != null) {
            publicationFrom = from;
        }

        return publicationFrom;
    }

    public Date getDateTo() {
        Date to = publicationDateTo.getValue();
        Date publicationTo = null;
        if (to != null) {
            publicationTo = to;
        }

        return publicationTo;
    }

    private void searchCM() {
        searchInContentBar.setVisible(true);
        RelationalFilesWorker worker = new RelationalFilesWorker();
        worker.execute();
    }

    private void addSubjectModel() {
        RelationalFilesModel model = relationalFilesTable.getModel() instanceof DefaultTableModel
                ? new RelationalFilesModel()
                : (RelationalFilesModel) relationalFilesTable.getModel();
        relationalFilesTable.setModel(model);

        try {
            relationalFilesTable.addMouseListener(new java.awt.event.MouseAdapter() {
                List<Integer> selectedRows = new ArrayList<>();
            });

            fitColumns(relationalFilesTable);
            SearchTableCellRenderer stcr = new SearchTableCellRenderer();
            relationalFilesTable.setDefaultRenderer(Object.class, stcr);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void setSubjectModel(List<RelationalFilesBO> list) {
        RelationalFilesModel model = new RelationalFilesModel(list);
        relationalFilesTable.setModel(model);
        fitColumns(relationalFilesTable);
    }

    private void fitColumns(JTable table) {
        try {
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.LEFT);
            table.getColumnModel().getColumn(2).setCellRenderer(render);
            
            // remover de la vista la columna con el id del documento.
            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_10.get()));


            //Utilerias.formatTable(table);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public Map selectedRows() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        RelationalFilesModel model = relationalFilesTable.getModel() instanceof DefaultTableModel
                ? new RelationalFilesModel()
                : (RelationalFilesModel) relationalFilesTable.getModel();
        if (model == null) {
            return null;
        }

        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    map.put(model.getUniqueRowIdentify(row), model.getTitleofDoc(row));
                }
            }
        }
        return map;
    }

    private void relate() {
        Map<Integer, String> map = selectedRows();
        //DocumentBO docBO = MainView.main.getScrDocument().getDocBO();

        if (map.size() > 0) {

            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Debe Seleccionar un documento para relacionar");
        }

    }

    private void cleanData() {
        cmbMercado.setSelectedIndex(0);
        txtTema.setText(null);
        cmbIdioma.setSelectedIndex(0);
        cmbTipoDoc.setSelectedIndex(0);
        cmbSector.setSelectedIndex(0);
        cmbAutor.setSelectedIndex(0);
        publicationDateFrom.setValue(null);
        publicationDateTo.setValue(null);
    }

    private void loadDataSubject() {
        SubjectDAO dao = new SubjectDAO();
        SubjectBO bo = new SubjectBO();
        List<SubjectBO> list = dao.get(null);

        for (SubjectBO lst : list) {
            bo = new SubjectBO();
            bo.setName(lst.getName());
            bo.setIdSubject(lst.getIdSubject());
            textAutoCompleter.addItem(bo);
        }
    }

    private SubjectBO loadSubjectObjectByString(String s) {
        SubjectBO bo = null;
        SubjectDAO dao = new SubjectDAO();

        List<SubjectBO> list = dao.get(s);
        if (list != null && list.size() > 0) {
            bo = list.get(0);
        }
        return bo;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panHeader = new javax.swing.JPanel();
        cmbMercado = new javax.swing.JComboBox();
        cmbTipoDoc = new javax.swing.JComboBox();
        txtTema = new javax.swing.JTextField();
        cmbSector = new javax.swing.JComboBox();
        cmbIdioma = new javax.swing.JComboBox();
        cmbAutor = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panelFecha1 = new javax.swing.JPanel();
        panelFecha2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        panCenter = new javax.swing.JPanel();
        mainScrollPanel = new javax.swing.JScrollPane();
        relationalFilesTable = new javax.swing.JTable();
        panFooter = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        searchInContentBar = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Documentos Relacionados");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        panHeader.setName("panHeader"); // NOI18N

        cmbMercado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMercado.setName("cmbMercado"); // NOI18N
        cmbMercado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMercadoItemStateChanged(evt);
            }
        });

        cmbTipoDoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTipoDoc.setName("cmbTipoDoc"); // NOI18N

        txtTema.setName("txtTema"); // NOI18N
        txtTema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTemaFocusGained(evt);
            }
        });

        cmbSector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSector.setName("cmbSector"); // NOI18N
        cmbSector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSectorActionPerformed(evt);
            }
        });

        cmbIdioma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbIdioma.setName("cmbIdioma"); // NOI18N
        cmbIdioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbIdiomaActionPerformed(evt);
            }
        });

        cmbAutor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAutor.setName("cmbAutor"); // NOI18N
        cmbAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAutorActionPerformed(evt);
            }
        });

        jLabel1.setText("Mercado:");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Tipo de documento:");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("Tema:");
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText("Sector:");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText("Idioma:");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText("Autor:");
        jLabel6.setName("jLabel6"); // NOI18N

        panelFecha1.setName("panelFecha1"); // NOI18N
        panelFecha1.setLayout(new java.awt.BorderLayout());

        panelFecha2.setName("panelFecha2"); // NOI18N
        panelFecha2.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Buscar");
        jButton1.setMaximumSize(new java.awt.Dimension(83, 23));
        jButton1.setMinimumSize(new java.awt.Dimension(83, 23));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(83, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Limpiar");
        jButton2.setMaximumSize(new java.awt.Dimension(83, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(83, 23));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(83, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setText("Rango Fecha:");
        jLabel7.setName("jLabel7"); // NOI18N

        javax.swing.GroupLayout panHeaderLayout = new javax.swing.GroupLayout(panHeader);
        panHeader.setLayout(panHeaderLayout);
        panHeaderLayout.setHorizontalGroup(
            panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHeaderLayout.createSequentialGroup()
                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panHeaderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panHeaderLayout.createSequentialGroup()
                                .addComponent(cmbMercado, 0, 550, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addGap(13, 13, 13))
                            .addGroup(panHeaderLayout.createSequentialGroup()
                                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbIdioma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTema))
                                .addGap(65, 65, 65)
                                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHeaderLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(16, 16, 16))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHeaderLayout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHeaderLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)))
                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panHeaderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbTipoDoc, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbSector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panHeaderLayout.createSequentialGroup()
                        .addComponent(panelFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbAutor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panHeaderLayout.setVerticalGroup(
            panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panHeaderLayout.createSequentialGroup()
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMercado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(cmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panHeaderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panCenter.setName("panCenter"); // NOI18N

        mainScrollPanel.setName("mainScrollPanel"); // NOI18N

        relationalFilesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        relationalFilesTable.setName("relationalFilesTable"); // NOI18N
        mainScrollPanel.setViewportView(relationalFilesTable);

        javax.swing.GroupLayout panCenterLayout = new javax.swing.GroupLayout(panCenter);
        panCenter.setLayout(panCenterLayout);
        panCenterLayout.setHorizontalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1078, Short.MAX_VALUE)
            .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))
        );
        panCenterLayout.setVerticalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
            .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
        );

        panFooter.setName("panFooter"); // NOI18N

        jButton3.setText("Relacionar");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cerrar");
        jButton4.setMaximumSize(new java.awt.Dimension(83, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(83, 23));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(83, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        searchInContentBar.setIndeterminate(true);
        searchInContentBar.setName("searchInContentBar"); // NOI18N

        javax.swing.GroupLayout panFooterLayout = new javax.swing.GroupLayout(panFooter);
        panFooter.setLayout(panFooterLayout);
        panFooterLayout.setHorizontalGroup(
            panFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchInContentBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panFooterLayout.setVerticalGroup(
            panFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFooterLayout.createSequentialGroup()
                .addGroup(panFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFooterLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchInContentBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFooterLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jLabel8.setText("Solamente se pueden relacionar documentos enviados");
        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(panFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAutorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAutorActionPerformed

    private void cmbIdiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIdiomaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbIdiomaActionPerformed

    private void cmbSectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSectorActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cleanData();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        relate();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        addSubjectModel();
    }//GEN-LAST:event_formComponentShown

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchCM();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmbMercadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMercadoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object obj = cmbMercado.getSelectedItem();
            MarketBO bo = (MarketBO) obj;
            int id = Integer.parseInt(bo.getIdMiVector_real());

            if (id > 0) {

                cmbTipoDoc.removeAllItems();

                DocumentTypeDAO dao = new DocumentTypeDAO();
                List<DocumentTypeBO> list = dao.get(null, -1, 0);
                DocumentTypeBO docBO = new DocumentTypeBO();
                docBO.setName("Todos");
                docBO.setIddocument_type(-1);
                cmbTipoDoc.addItem(docBO);
                for (DocumentTypeBO docBO_ : list) {
                    if (docBO_.getIdMarket() == id) {
                        cmbTipoDoc.addItem(docBO_);
                    }
                }

            } else {
                cmbTipoDoc.setSelectedIndex(-1);
            }
        }
    }//GEN-LAST:event_cmbMercadoItemStateChanged

    private void txtTemaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTemaFocusGained

    }//GEN-LAST:event_txtTemaFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbAutor;
    private javax.swing.JComboBox cmbIdioma;
    private javax.swing.JComboBox cmbMercado;
    private javax.swing.JComboBox cmbSector;
    private javax.swing.JComboBox cmbTipoDoc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane mainScrollPanel;
    private javax.swing.JPanel panCenter;
    private javax.swing.JPanel panFooter;
    private javax.swing.JPanel panHeader;
    private javax.swing.JPanel panelFecha1;
    private javax.swing.JPanel panelFecha2;
    private javax.swing.JTable relationalFilesTable;
    private javax.swing.JProgressBar searchInContentBar;
    private javax.swing.JTextField txtTema;
    // End of variables declaration//GEN-END:variables
    private TextAutoCompleter textAutoCompleter;

    class RelationalFilesWorker extends SwingWorker<List<RelationalFilesBO>, List<RelationalFilesBO>> {

        RelationalFilesWorker() {

        }

        @Override
        protected List<RelationalFilesBO> doInBackground() {
            List<RelationalFilesBO> list = new ArrayList<RelationalFilesBO>();

            RelationalFilesDAO dao = new RelationalFilesDAO();
            RelationalFilesBO bo = new RelationalFilesBO();

            bo.setIdMarket(getSelectedMarket());
            bo.setIdSubject(0);

            if ((!txtTema.getText().isEmpty()) && (selectedValue instanceof SubjectBO)) {
                bo.setIdSubject(selectedValue.getIdSubject());
            } else {
                if (!txtTema.getText().isEmpty()) {
                    selectedValue = loadSubjectObjectByString(txtTema.getText());
                    if (selectedValue != null) {
                        bo.setIdSubject(selectedValue.getIdSubject());
                    }
                }
            }

            bo.setIdLanguage(getSelectedLanguage());

            bo.setIdDocType(getSelectedDocumentType());

            bo.setIdIndustry(getSelectedIndustry());

            bo.setAuthor(getSelectedAuthor());
            bo.setDocPublicationDateFrom(getDateFrom());
            bo.setDocPublicationDateTo(getDateFrom());

            list = dao.get(bo);
            selectedValue = null;

            return list;
        }

        @Override
        protected void done() {
            List<RelationalFilesBO> list = null;
            try {
                list = get();
            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
            }
            try {
                setSubjectModel(list);
            } finally {
                searchInContentBar.setVisible(false);
            }
        }
    }

}
