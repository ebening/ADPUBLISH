package com.adinfi.formateador.view.administration;

import com.adinfi.formateador.bos.OfficeBO;
import com.adinfi.formateador.dao.OfficeDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.main.MainApp;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.administration.tablemodel.OfficeModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Josue Sanchez
 */
public class Office extends javax.swing.JDialog {

    private final int CHARACTER_LIMIT = 15;
    //private static final String ONLY_ALPHANUMERICS = "^(?=.*\\d)(?=.*[A-Za-z])[A-Za-z0-9]{1,15}$";
    private static final String ONLY_ALPHANUMERICS = "^[A-Za-z0-9]*$";
    private final String NEW_LABEL = "Nuevo";
    private final String CANCEL_LABEL = "Cancelar";
    private final String EDIT_LABEL = "Editar";
    private final String SAVE_LABEL = "Guardar";
    private final String DELETE_MESSAGE = "¿Desea eliminar?";
    private final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    private final DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
    private final Pattern onlyAphanumerics;
    
    private final TableModelListener tableML = (TableModelEvent e) -> {
        setButtonsOffice(e.getLastRow());
    };
    
    private final MouseAdapter madapter = new MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = officeTable.rowAtPoint(evt.getPoint());
            int col = officeTable.columnAtPoint(evt.getPoint());
            if (row >= 0 && col > 0) {
                setCurrent(row, col);
                setEnabledData(false);
                //btnNewOffice.setText(NEW_LABEL);
            }
        }
    };
    
    private Matcher matcher;

    public Office(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        onlyAphanumerics = Pattern.compile(ONLY_ALPHANUMERICS);

    }

    private boolean zipCodeValidate(String cp) {
        matcher = onlyAphanumerics.matcher(cp);
        return matcher.matches();

    }

    @SuppressWarnings("unchecked")

    private void addOfficeModel() {
        OfficeModel model = officeTable.getModel() instanceof DefaultTableModel
                ? new OfficeModel()
                : (OfficeModel) officeTable.getModel();
        officeTable.setModel(model);

        try {
          /*  officeTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    //int row = languageTable.rowAtPoint(e.getPoint());
                    int column = officeTable.columnAtPoint(e.getPoint());
                    if (column == StatementConstant.SC_0.get()) {
                        // Habilitar e inhabiliar botones de eliminar guardar
                        OfficeModel model = (OfficeModel) officeTable.getModel();
                        List<Integer> selectedRows = new ArrayList<>();
                        int length = model.getData() != null ? model.getData().length : 0;
                        for (int i = 0; i < length; i++) {
                            if (model.isCheckedRow(i) == true) {
                                selectedRows.add(model.getUniqueRowIdentify(i));
                            }
                        }
                        if (selectedRows.isEmpty() == true) {
                            
                            btnSaveOffice.setText(SAVE_LABEL);
                            btnSaveOffice.setEnabled(false);
                            btnNewOffice.setText(NEW_LABEL);
                            btnNewOffice.setEnabled(true);
                            btnDeleteOffice.setEnabled(false);
                        } else if (selectedRows.size() == 1) {
                            btnNewOffice.setText(CANCEL_LABEL);
                            btnNewOffice.setEnabled(true);
                            btnSaveOffice.setEnabled(true);
                            btnSaveOffice.setText(EDIT_LABEL);
                            btnDeleteOffice.setEnabled(true);
                        } else if (selectedRows.size() > 1) {
                            btnNewOffice.setText(CANCEL_LABEL);
                            btnNewOffice.setEnabled(true);
                            btnSaveOffice.setEnabled(false);
                            btnSaveOffice.setText(EDIT_LABEL);
                            btnDeleteOffice.setEnabled(true);
                        }
                    }
                }
            }); */

            fitColumns(officeTable);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }
    
    private void setButtonsOffice(int lastRow){
        OfficeModel model = (OfficeModel) officeTable.getModel();
        List<Integer> selectedRows = new ArrayList<>();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {
                selectedRows.add(model.getUniqueRowIdentify(i));
            }
        }
        if (selectedRows.isEmpty() == true) {
            /*Cuando no se han seleccionado elementos en el grid*/
            btnSaveOffice.setText(SAVE_LABEL);
            btnSaveOffice.setEnabled(false);
            btnNewOffice.setText(NEW_LABEL);
            btnNewOffice.setEnabled(true);
            btnDeleteOffice.setEnabled(false);
        } else if (selectedRows.size() == 1) {
            btnNewOffice.setText(CANCEL_LABEL);
            btnNewOffice.setEnabled(true);
            btnSaveOffice.setEnabled(true);
            btnSaveOffice.setText(EDIT_LABEL);
            btnDeleteOffice.setEnabled(true);
        } else if (selectedRows.size() > 1) {
            btnNewOffice.setText(CANCEL_LABEL);
            btnNewOffice.setEnabled(true);
            btnSaveOffice.setEnabled(false);
            btnSaveOffice.setText(EDIT_LABEL);
            btnDeleteOffice.setEnabled(true);
        }
        setCurrent(lastRow, 0);
    }

    private int checkedRowVal() {
        int num = 0;
        OfficeModel model = (OfficeModel) officeTable.getModel();
        int length = model.getData() != null ? model.getData().length : 0;
        for (int i = 0; i < length; i++) {
            if (model.isCheckedRow(i) == true) {

                num = model.getUniqueRowIdentify(i);
                break;
            }
        }
        return num;
    }

    private void findOffice(String pattern) {
        try {
            OfficeDAO dAO = new OfficeDAO();
            List<OfficeBO> list = dAO.get(pattern);
            setOfficeModel(list);

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void setOfficeModel(List<OfficeBO> list) {
        OfficeModel officeModel = new OfficeModel(list);
        officeModel.addTableModelListener(tableML);
        officeTable.setModel(officeModel);
        fitColumns(officeTable);
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        leftRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        for (int i = 0; i < officeTable.getColumnCount(); i++) {
            switch (i) {
                case 0:
                    break;
                case 9:
                    break;
                case 10:
                    officeTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
                    break;
                default:
                    officeTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            }

            //Utilerias.logger(getClass()).info((i)+ " "+ officeTable.getColumnModel().getColumn(i).getIdentifier());
        }
        officeTable.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        try {
            officeTable.addMouseListener(madapter);
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }

    }

    private void setCurrent(int row, int col) {

        try {

            OfficeModel model = (OfficeModel) officeTable.getModel();
            Object[][] data = model.getData();

            inputOrder.setText(String.valueOf(data[row][StatementConstant.SC_2.get()]));
            inputBranch.setText(String.valueOf(data[row][StatementConstant.SC_3.get()])); // si
            inputAddress.setText(String.valueOf(data[row][StatementConstant.SC_4.get()]));
            inputDistrict.setText(String.valueOf(data[row][StatementConstant.SC_5.get()]));
            inputZipCode.setText(String.valueOf(data[row][StatementConstant.SC_6.get()]));
            inputCity.setText(String.valueOf(data[row][StatementConstant.SC_7.get()]));
            inputState.setText(String.valueOf(data[row][StatementConstant.SC_8.get()]));
            inputCountry.setText(String.valueOf(data[row][StatementConstant.SC_9.get()]));

            checkNational.setSelected((boolean) data[row][StatementConstant.SC_10.get()]);

            //Get Phones
            inputPhone1.setText(String.valueOf(data[row][StatementConstant.SC_11.get()]));
            inputPhone2.setText(String.valueOf(data[row][StatementConstant.SC_12.get()]));
            inputPhone3.setText(String.valueOf(data[row][StatementConstant.SC_13.get()]));
            inputPhone4.setText(String.valueOf(data[row][StatementConstant.SC_14.get()]));

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    private void fitColumns(JTable table) {
        /* Ajustar columnas de la tabla */
        Utilerias.adjustJTableRowSizes(table);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Utilerias.adjustColumnSizes(table, i, 4);
        }

        //Not to display colums id,fatemodify,status and optional phones
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_12.get() - StatementConstant.SC_1.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_13.get() - StatementConstant.SC_2.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_14.get() - StatementConstant.SC_3.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_15.get() - StatementConstant.SC_4.get()));
        table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_16.get() - StatementConstant.SC_5.get()));

        //table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
    }

    private void deleteOffice() {
        OfficeModel model = officeTable.getModel() instanceof DefaultTableModel
                ? new OfficeModel()
                : (OfficeModel) officeTable.getModel();
        if (model == null) {
            return;
        }
        Object[][] a2 = model.getData();
        if (a2 != null && a2.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (int row = 0; row < a2.length; row++) {
                if (model.isCheckedRow(row) == true) {
                    ids.add(model.getUniqueRowIdentify(row));
                }
            }
            //Confirmar eliminacion 
            if (ids.isEmpty() == false) {
                int result = JOptionPane.showConfirmDialog(
                        MainApp.getApplication().getMainFrame(), DELETE_MESSAGE, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    OfficeDAO dao = new OfficeDAO();
                    boolean delete = dao.delete(ids);
                    String message = delete == true ? "Registros eliminados" : "Ocurrió un error al eliminar\nintente nuevamente.";
                    if (delete) {
                        findOffice(inputOffice.getText());
                        setEnabledData(false);
                        cleanData();
                        btnDeleteOffice.setEnabled(false);
                        btnSaveOffice.setEnabled(false);
                        btnNewOffice.setText(NEW_LABEL);
                    }
                    JOptionPane.showMessageDialog(MainApp.getApplication().getMainFrame(), message, Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Idiomas = new javax.swing.JPanel();
        btnDeleteOffice = new javax.swing.JButton();
        btnSaveOffice = new javax.swing.JButton();
        btnNewOffice = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        officeTable = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        inputOffice = new javax.swing.JTextField();
        searchOffice = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        inputAddress = new javax.swing.JTextField();
        inputBranch = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        checkNational = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        inputCity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        inputState = new javax.swing.JTextField();
        inputZipCode = new javax.swing.JTextField();
        inputDistrict = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        inputPhone4 = new javax.swing.JTextField();
        inputPhone2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        inputCountry = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        inputPhone1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        inputPhone3 = new javax.swing.JTextField();
        inputOrder = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Oficinas");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Idiomas.setName("Idiomas"); // NOI18N

        btnDeleteOffice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteOffice.setText("Eliminar");
        btnDeleteOffice.setEnabled(false);
        btnDeleteOffice.setName("btnDeleteOffice"); // NOI18N
        btnDeleteOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteOfficeActionPerformed(evt);
            }
        });

        btnSaveOffice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSaveOffice.setText("Guardar");
        btnSaveOffice.setEnabled(false);
        btnSaveOffice.setName("btnSaveOffice"); // NOI18N
        btnSaveOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOfficeActionPerformed(evt);
            }
        });

        btnNewOffice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewOffice.setText(NEW_LABEL);
        btnNewOffice.setName("btnNewOffice"); // NOI18N
        btnNewOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOfficeActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("* Campo Requerido");
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        officeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Sucursal*", "Dirección*", "Colonia*", "C.P.*", "Municipio*", "Estado*", "País*", "Nacional*", "Telefono*", "Orden"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        officeTable.setName("officeTable"); // NOI18N
        jScrollPane9.setViewportView(officeTable);

        javax.swing.GroupLayout IdiomasLayout = new javax.swing.GroupLayout(Idiomas);
        Idiomas.setLayout(IdiomasLayout);
        IdiomasLayout.setHorizontalGroup(
            IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IdiomasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IdiomasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(227, 227, 227)
                        .addComponent(btnNewOffice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveOffice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteOffice)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane9)))
        );
        IdiomasLayout.setVerticalGroup(
            IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IdiomasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(IdiomasLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(IdiomasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNewOffice)
                            .addComponent(btnSaveOffice)
                            .addComponent(btnDeleteOffice))))
                .addContainerGap())
        );

        jPanel14.setName("jPanel14"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Búsqueda por Sucursal");
        jLabel5.setName("jLabel5"); // NOI18N

        inputOffice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        inputOffice.setName("inputOffice"); // NOI18N
        inputOffice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputOfficeKeyReleased(evt);
            }
        });

        searchOffice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar_cat.png"))); // NOI18N
        searchOffice.setName("searchOffice"); // NOI18N
        searchOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOfficeActionPerformed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setEnabled(false);
        jPanel15.setName("jPanel15"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel15.setText("Estado*");
        jLabel15.setName("jLabel15"); // NOI18N

        inputAddress.setEnabled(false);
        inputAddress.setName("inputAddress"); // NOI18N

        inputBranch.setEnabled(false);
        inputBranch.setName("inputBranch"); // NOI18N

        jLabel14.setText("Municipio*");
        jLabel14.setName("jLabel14"); // NOI18N

        checkNational.setSelected(true);
        checkNational.setEnabled(false);
        checkNational.setName("checkNational"); // NOI18N
        checkNational.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNationalActionPerformed(evt);
            }
        });

        jLabel11.setText("Dirección*");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel17.setText("Nacional");
        jLabel17.setName("jLabel17"); // NOI18N

        inputCity.setEnabled(false);
        inputCity.setName("inputCity"); // NOI18N

        jLabel10.setText("Sucursal*");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel12.setText("Colonia*");
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText("C.P.*");
        jLabel13.setName("jLabel13"); // NOI18N

        inputState.setEnabled(false);
        inputState.setName("inputState"); // NOI18N

        inputZipCode.addKeyListener(new KeyListener(){

            public void keyTyped(KeyEvent e)

            {if (inputZipCode.getText().length()== CHARACTER_LIMIT)

                e.consume();
            }

            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
            }
        });
        inputZipCode.setEnabled(false);
        inputZipCode.setName("inputZipCode"); // NOI18N
        inputZipCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputZipCodeActionPerformed(evt);
            }
        });

        inputDistrict.setEnabled(false);
        inputDistrict.setName("inputDistrict"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel17))
                .addGap(76, 76, 76)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkNational)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(inputCity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputZipCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputDistrict, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputAddress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputBranch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputState, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(inputBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkNational)
                    .addComponent(jLabel17))
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel19.setText("Teléfono 2");
        jLabel19.setName("jLabel19"); // NOI18N

        inputPhone4.setEnabled(false);
        inputPhone4.setName("inputPhone4"); // NOI18N

        inputPhone2.setEnabled(false);
        inputPhone2.setName("inputPhone2"); // NOI18N

        jLabel18.setText("Teléfono 1*");
        jLabel18.setName("jLabel18"); // NOI18N

        inputCountry.setText("México");
        inputCountry.setEnabled(false);
        inputCountry.setName("inputCountry"); // NOI18N
        inputCountry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputCountryFocusLost(evt);
            }
        });

        jLabel16.setText("País*");
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel20.setText("Larga Distancia");
        jLabel20.setName("jLabel20"); // NOI18N

        inputPhone1.setEnabled(false);
        inputPhone1.setName("inputPhone1"); // NOI18N

        jLabel21.setText("Desde E.U.");
        jLabel21.setName("jLabel21"); // NOI18N

        inputPhone3.setEnabled(false);
        inputPhone3.setName("inputPhone3"); // NOI18N

        inputOrder.setEnabled(false);
        inputOrder.setName("inputOrder"); // NOI18N

        jLabel22.setText("Orden*");
        jLabel22.setName("jLabel22"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18)
                            .addComponent(jLabel16))
                        .addGap(76, 76, 76)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(inputPhone3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputPhone2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputPhone1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputCountry, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputPhone4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(inputCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputPhone3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputPhone4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(inputOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(inputOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchOffice))
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Idiomas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Idiomas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOfficeActionPerformed

        if (btnSaveOffice.getText().equals(SAVE_LABEL)) {
            OfficeBO bo = new OfficeBO();
            boolean required = true;
            StringBuilder camposRequeridos = new StringBuilder();

            if (inputBranch.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nSucursal");
            } else {
                bo.setBranch(inputBranch.getText());
            }

            if (inputAddress.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nDirección");
            } else {
                bo.setAddress(inputAddress.getText());
            }

            if (inputDistrict.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nColonia");
            } else {
                bo.setDistrict(inputDistrict.getText());
            }

            if (inputZipCode.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nC.P.");
            } else {
                if (zipCodeValidate(inputZipCode.getText())) {
                    bo.setZipCode(inputZipCode.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "El CP debe ser Alfanumerico menor igual a 15 caracteres");
                    return;
                }
            }

            if (inputCity.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nMunicipio");
            } else {
                bo.setCity(inputCity.getText());
            }
            if (inputState.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nEstado");
            } else {
                bo.setState(inputState.getText());
            }

            if (inputCountry.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nPaís");
            } else {
                bo.setCountry(inputCountry.getText());
            }

            bo.setNational(checkNational.isSelected());

            if (inputPhone1.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nTeléfono");
            } else {
                bo.setPhone1(inputPhone1.getText());
            }

            if (inputOrder.getText().isEmpty()) {
                required = false;
                camposRequeridos.append("\nOrden");
            } else {
                try {
                    bo.setOrder(parseInt(inputOrder.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El orden debe ser de tipo numérico");
                    return;
                }
            }

            bo.setPhone2(inputPhone2.getText());
            bo.setPhone3(inputPhone3.getText());
            bo.setPhone4(inputPhone4.getText());
            bo.setIdOffice(checkedRowVal());
            int idx = 0;
            OfficeDAO dao = new OfficeDAO();
            if (required == true) {
                int e = 0;

                if (Utilerias.isRepeated(Utilerias.TablesValueNames.OFFICE.name(), "ORDER", inputOrder.getText(), "IDOFFICE", checkedRowVal())) {
                    JOptionPane.showMessageDialog(null, "El número de orden " + inputOrder.getText() + " ya existe, ingrese uno diferente.");
                    return;
                } else if (Utilerias.isRepeated(Utilerias.TablesValueNames.OFFICE.name(),
                        Utilerias.TablesValueNames.OFFICE.getCaption(), inputBranch.getText(), "IDOFFICE", checkedRowVal())) {
                    JOptionPane.showMessageDialog(null, "El nombre de la Sucursal " + inputBranch.getText() + " ya existe, ingrese uno diferente.");
                    return;
                }
                idx = dao.insertUpdate(bo);
            } else {
                JOptionPane.showMessageDialog(null, "Complete los campos requeridos*" + " " + camposRequeridos);
            }

            if (idx > 0) {
                findOffice(inputOffice.getText());
                btnSaveOffice.setText(SAVE_LABEL);
                btnSaveOffice.setEnabled(false);
                btnNewOffice.setText(NEW_LABEL);
                btnNewOffice.setEnabled(true);
                btnDeleteOffice.setEnabled(false);
                setEnabledData(false);
                JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente.");
                cleanData();
            }

        } else {
            setEnabledData(true);
            btnSaveOffice.setText(SAVE_LABEL);
            inputBranch.requestFocusInWindow();
        }

    }//GEN-LAST:event_btnSaveOfficeActionPerformed

    private void btnNewOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOfficeActionPerformed

        if (btnNewOffice.getText().equals(NEW_LABEL)) {
            btnNewOffice.setText(CANCEL_LABEL);
            btnSaveOffice.setEnabled(true);
            btnSaveOffice.setText(SAVE_LABEL);
            btnDeleteOffice.setEnabled(false);
            setEnabledData(true);
            cleanData();
            officeTable.removeMouseListener(madapter);
            officeTable.getModel().removeTableModelListener(tableML);
            inputBranch.requestFocusInWindow();
        } else {
            btnNewOffice.setText(NEW_LABEL);
//            if (checkedRowVal() > 0) { Por incidencia al dar cancelar deja habilidato el boton guardar
//                btnSaveOffice.setEnabled(false);
//            }
            btnSaveOffice.setEnabled(false);
            btnDeleteOffice.setEnabled(false);
            setEnabledData(false);
            cleanData();
            findOffice(null);
        }
    }//GEN-LAST:event_btnNewOfficeActionPerformed

    private void setEnabledData(Boolean b) {
        inputOrder.setEnabled(b);
        inputBranch.setEnabled(b);
        inputAddress.setEnabled(b);
        inputDistrict.setEnabled(b);
        inputZipCode.setEnabled(b);
        inputCity.setEnabled(b);
        inputState.setEnabled(b);
        //inputCountry.setEnabled(b);
        checkNational.setEnabled(b);
        inputPhone1.setEnabled(b);
        inputPhone2.setEnabled(b);
        inputPhone3.setEnabled(b);
        inputPhone4.setEnabled(b);
    }

    private void cleanData() {
        inputOrder.setText(null);
        inputBranch.setText(null);
        inputAddress.setText(null);
        inputDistrict.setText(null);
        inputZipCode.setText(null);
        inputCity.setText(null);
        inputState.setText(null);
        //inputCountry.setText(null);
        //checkNational.setSelected(false);
        inputPhone1.setText(null);
        inputPhone2.setText(null);
        inputPhone3.setText(null);
        inputPhone4.setText(null);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        findOffice(null);
    }//GEN-LAST:event_formWindowOpened

    private void searchOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchOfficeActionPerformed
        findOffice(inputOffice.getText());
    }//GEN-LAST:event_searchOfficeActionPerformed

    private void OfficeInputKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            findOffice(inputOffice.getText());
        }
    }

    private void inputOfficeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputOfficeKeyReleased
        OfficeInputKeyReleased(evt);
    }//GEN-LAST:event_inputOfficeKeyReleased

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        addOfficeModel();
    }//GEN-LAST:event_formComponentShown

    private void btnDeleteOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteOfficeActionPerformed
        deleteOffice();
    }//GEN-LAST:event_btnDeleteOfficeActionPerformed

    private void checkNationalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNationalActionPerformed
        if(checkNational.isSelected()){
        inputCountry.setText("México");
        inputCountry.setEnabled(false);
        }else{
        inputCountry.setText(null);
        inputCountry.setEnabled(true);
        }
    }//GEN-LAST:event_checkNationalActionPerformed

    private void inputZipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputZipCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputZipCodeActionPerformed

    private void inputCountryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputCountryFocusLost
        if(inputCountry.getText().equalsIgnoreCase("mexico") || inputCountry.getText().equalsIgnoreCase("méxico")){
            checkNational.setSelected(true);
        }else{
            checkNational.setSelected(false);
        }
    }//GEN-LAST:event_inputCountryFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Idiomas;
    private javax.swing.JButton btnDeleteOffice;
    private javax.swing.JButton btnNewOffice;
    private javax.swing.JButton btnSaveOffice;
    private javax.swing.JCheckBox checkNational;
    private javax.swing.JTextField inputAddress;
    private javax.swing.JTextField inputBranch;
    private javax.swing.JTextField inputCity;
    private javax.swing.JTextField inputCountry;
    private javax.swing.JTextField inputDistrict;
    private javax.swing.JTextField inputOffice;
    private javax.swing.JTextField inputOrder;
    private javax.swing.JTextField inputPhone1;
    private javax.swing.JTextField inputPhone2;
    private javax.swing.JTextField inputPhone3;
    private javax.swing.JTextField inputPhone4;
    private javax.swing.JTextField inputState;
    private javax.swing.JTextField inputZipCode;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable officeTable;
    private javax.swing.JButton searchOffice;
    // End of variables declaration//GEN-END:variables
}
