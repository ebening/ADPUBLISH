package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.EventBO;
import com.adinfi.formateador.bos.ModuleSectionBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.main.MainView;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.view.agendas.AgendaWS;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class ScrCalendarWeek extends javax.swing.JPanel {
    private static final Logger LOG = Logger.getLogger(ScrCalendarWeek.class.getName());
    protected ScrDocument parentDoc;
    protected ObjectInfoBO objInfo;
    protected ModuleSectionBO secInfo;
    private int objectId = 0;
    private final String[] columnNames = { "Id", "Fecha", "Hora", "Lugar",
        "Indicador", "Periodo", "Estimación Vector",
        "Encuesta Bloomberg", "Oficial", "Cifra Anterior" };

    public ScrCalendarWeek(ScrDocument scrDoc, ModuleSectionBO secInfo) {
        initComponents();
        this.secInfo = secInfo;
        this.parentDoc = scrDoc;
        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<>());
        }
        addObject(GlobalDefines.OBJ_TYPE_CALENDAR_WEEK);
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

    private void printEvents(){
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
            
            List<EventBO> eventBOLst= AgendaWS.getEventLstWeek(idMiVectorCalendar);
            if(eventBOLst!=null){
                for(EventBO event: eventBOLst){
                    Calendar fecha= event.getFechaCompleta();
                    setEvent(fecha.get(Calendar.DAY_OF_WEEK), event);
                };
            }
        }catch(Exception e){
            LOG.log(Level.INFO, e.getMessage(), e);
        }
    }

    private void setEvent(int day, EventBO event){
        switch(day){
        case 1://Domingo
            break;
        case 2://Lunes
            setText(jLunesE_1,event.getEventoDesc());
            break;
        case 3://Martes
            setText(jMartesE_1,event.getEventoDesc());
            break;
        case 4://Miercoles
            setText(jMiercolesE_1,event.getEventoDesc());
            break;
        case 5://Jueves
            setText(jJuevesE_1,event.getEventoDesc());
            break;
        case 6://Viernes
            setText(jViernesE_1,event.getEventoDesc());
            break;
        case 7://Sabado
        default:
            break;
        }
    }
    
    private void cleanFields(){
        jLunesE_1.setText("");
        jMartesE_1.setText("");
        jMiercolesE_1.setText("");
        jJuevesE_1.setText("");
        jViernesE_1.setText("");
    }

    private void setText(JLabel label, String text){
        String textLabel= label.getText();
        textLabel= textLabel.replaceAll("<html>", "");
        textLabel= textLabel.replaceAll("</html>", "");
        if(textLabel.length()==0){
            label.setText(text);
        }else{
            label.setText(text+"<br/>"+label.getText());
        }
        label.setText("<html>"+label.getText()+"</html>");
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

        jFrame1 = new javax.swing.JFrame();
        jCargarDatos = new javax.swing.JButton();
        jLunesT_1 = new javax.swing.JLabel();
        jMartesT_1 = new javax.swing.JLabel();
        jMiercolesT_1 = new javax.swing.JLabel();
        jJuevesT_1 = new javax.swing.JLabel();
        jViernesT_1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jMartesE_1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMiercolesE_1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLunesE_1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jJuevesE_1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jViernesE_1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setAutoscrolls(true);
        setMaximumSize(new java.awt.Dimension(557, 550));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(557, 500));

        jCargarDatos.setText("Cargar Datos");
        jCargarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCargarDatosActionPerformed(evt);
            }
        });

        jLunesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_1.setText("Lunes");
        jLunesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_1.setMaximumSize(new java.awt.Dimension(44, 16));
        jLunesT_1.setMinimumSize(new java.awt.Dimension(44, 16));
        jLunesT_1.setOpaque(true);
        jLunesT_1.setPreferredSize(new java.awt.Dimension(44, 16));

        jMartesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_1.setText("Martes");
        jMartesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_1.setMaximumSize(new java.awt.Dimension(44, 16));
        jMartesT_1.setMinimumSize(new java.awt.Dimension(44, 16));
        jMartesT_1.setOpaque(true);
        jMartesT_1.setPreferredSize(new java.awt.Dimension(44, 16));

        jMiercolesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_1.setText("Miercoles");
        jMiercolesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_1.setMaximumSize(new java.awt.Dimension(44, 16));
        jMiercolesT_1.setMinimumSize(new java.awt.Dimension(44, 16));
        jMiercolesT_1.setOpaque(true);
        jMiercolesT_1.setPreferredSize(new java.awt.Dimension(44, 16));

        jJuevesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_1.setText("Jueves");
        jJuevesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_1.setMaximumSize(new java.awt.Dimension(44, 16));
        jJuevesT_1.setMinimumSize(new java.awt.Dimension(44, 16));
        jJuevesT_1.setOpaque(true);
        jJuevesT_1.setPreferredSize(new java.awt.Dimension(44, 16));

        jViernesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_1.setText("Viernes");
        jViernesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_1.setOpaque(true);

        jMartesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_1.setToolTipText("");
        jMartesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_1.setAutoscrolls(true);
        jMartesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_1.setOpaque(true);
        jScrollPane2.setViewportView(jMartesE_1);

        jMiercolesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_1.setToolTipText("");
        jMiercolesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_1.setAutoscrolls(true);
        jMiercolesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_1.setOpaque(true);
        jScrollPane1.setViewportView(jMiercolesE_1);

        jScrollPane3.setViewportView(jScrollPane1);

        jLunesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_1.setToolTipText("");
        jLunesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_1.setAutoscrolls(true);
        jLunesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_1.setOpaque(true);
        jScrollPane4.setViewportView(jLunesE_1);

        jJuevesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_1.setToolTipText("");
        jJuevesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_1.setAutoscrolls(true);
        jJuevesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_1.setOpaque(true);
        jScrollPane5.setViewportView(jJuevesE_1);

        jViernesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_1.setToolTipText("");
        jViernesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_1.setAutoscrolls(true);
        jViernesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_1.setOpaque(true);
        jScrollPane6.setViewportView(jViernesE_1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLunesT_1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMartesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMiercolesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jJuevesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCargarDatos)
                            .addComponent(jViernesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jJuevesT_1, jLunesT_1, jMartesT_1, jMiercolesT_1, jViernesT_1});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCargarDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMartesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMiercolesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLunesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jJuevesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jViernesT_1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(349, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCargarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCargarDatosActionPerformed
        cleanFields();
        printEvents();
    }//GEN-LAST:event_jCargarDatosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCargarDatos;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jJuevesE_1;
    private javax.swing.JLabel jJuevesT_1;
    private javax.swing.JLabel jLunesE_1;
    private javax.swing.JLabel jLunesT_1;
    private javax.swing.JLabel jMartesE_1;
    private javax.swing.JLabel jMartesT_1;
    private javax.swing.JLabel jMiercolesE_1;
    private javax.swing.JLabel jMiercolesT_1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel jViernesE_1;
    private javax.swing.JLabel jViernesT_1;
    // End of variables declaration//GEN-END:variables
}
