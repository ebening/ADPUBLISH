package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.EventBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.view.agendas.AgendaWS;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class ScrCalendarTotal extends javax.swing.JPanel {
    private static final Logger LOG = Logger.getLogger(ScrCalendarTotal.class.getName());
    protected ScrDocument parentDoc;
    protected ObjectInfoBO objInfo;
    protected ModuleSectionBO secInfo;
    private int objectId = 0;
    private final String[] columnNames = { "Id", "Fecha", "Hora", "Lugar",
        "Indicador", "Periodo", "Estimación Vector",
        "Encuesta Bloomberg", "Oficial", "Cifra Anterior" };

    public ScrCalendarTotal(ScrDocument scrDoc, ModuleSectionBO secInfo) {
        initComponents();
        this.secInfo = secInfo;
        this.parentDoc = scrDoc;
        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<>());
        }
        addObject(GlobalDefines.OBJ_TYPE_CALENDAR);
        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        jTable1.setDefaultRenderer(Object.class, stcr);
    }
    
    
    protected void addObject(int objectType) {
        objInfo = new ObjectInfoBO();
        boolean existe = false;
        for (ObjectInfoBO objInfo : this.secInfo.getLstObjects()) {
            objectId = objInfo.getObjectId();
            objInfo.setObjectId(objInfo.getObjectId());
            objInfo.setPlain_text(objInfo.getPlain_text());
            objInfo.setData(objInfo.getData());
            objInfo.setObjType(objInfo.getObjType());
            objInfo.setSectionId(objInfo.getSectionId());
            objInfo.setTemplateId(objInfo.getTemplateId());
            existe = true;
            break;
        }

        if (!existe) {
            objInfo.setObjType(GlobalDefines.OBJ_TYPE_CALENDAR);
            objInfo.setSectionId(this.secInfo.getSectionId());
            objInfo.setData(objInfo.getData());
            objInfo.setPlain_text(objInfo.getPlain_text());

            this.secInfo.getLstObjects().add(objInfo);
        }
        refreshObjects();
    }
    
    protected void refreshObjects() {
        for (final ObjectInfoBO objInfo : secInfo.getLstObjects()) {
            this.objInfo = objInfo;
            break;
        }
    }

    public Object[][] getAgendaWSEvents(){
        Object[][] data= {};
        try{
            int idDocType = MainView.main.getScrDocument().getDocBO().getIdDocType();
            DocumentTypeDAO dDAO = new DocumentTypeDAO();
            List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);
            int idMiVectorCalendar = 0;
            
            for(DocumentTypeBO d: dBO){
                if(d.getIddocument_type()==idDocType){
                    idMiVectorCalendar = d.getIddocument_type_vector() != null ? Integer.parseInt( d.getIddocument_type_vector() ) : 0;
                    break;
                }
            }
            
            List<EventBO> eventBOLst= AgendaWS.getEventLst(idMiVectorCalendar);
            if(eventBOLst!=null){
                data= new Object[eventBOLst.size()][10];
                int i= 0;
                for(EventBO eventBO: eventBOLst){
                    data[i][0]= eventBO.getEventoUniqueId();
                    data[i][1]= eventBO.getFecha();
                    data[i][2]= eventBO.getTiempo();
                    data[i][3]= eventBO.getLugar();
                    data[i][4]= eventBO.getEventoDesc();
                    data[i][5]= eventBO.getPeriodo();
                    data[i][6]= eventBO.getEstimacionVector();
                    data[i][7]= eventBO.getEstimacionBloomberg();
                    data[i][8]= eventBO.getCifraOficial();
                    data[i++][9]= eventBO.getCifraOficialAnt();
                }
            }
        }catch(Exception e){
            LOG.log(Level.INFO, e.getMessage(), e);
        }
        return data;
    }
    
    public ScrDocument getParentDoc() {
        return parentDoc;
    }

    public void setParentDoc(ScrDocument parentDoc) {
        this.parentDoc = parentDoc;
    }

    public ObjectInfoBO getObjInfo() {
        return objInfo;
    }

    public void setObjInfo(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
    }
    
    public ModuleSectionBO getSecInfo() {
        return secInfo;
    }

    public void setSecInfo(ModuleSectionBO secInfo) {
        this.secInfo = secInfo;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCargarDatos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setMaximumSize(new java.awt.Dimension(557, 500));
        setPreferredSize(new java.awt.Dimension(557, 500));

        jCargarDatos.setText("Cargar Datos");
        jCargarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCargarDatosActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Fecha", "Hora", "Lugar", "Indicador", "Periodo", "Estimación Vector", "Encuesta Bloomberg", "Oficial", "Cifra Anterior"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCargarDatos))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCargarDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCargarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCargarDatosActionPerformed
        Object[][] data= getAgendaWSEvents();
        DefaultTableModel model= (DefaultTableModel)jTable1.getModel();
        model.setDataVector(data, columnNames);
        model.fireTableDataChanged();
    }//GEN-LAST:event_jCargarDatosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCargarDatos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
