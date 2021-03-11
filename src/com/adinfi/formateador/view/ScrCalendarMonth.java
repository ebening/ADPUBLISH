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

public class ScrCalendarMonth extends javax.swing.JPanel {
    private static final Logger LOG = Logger.getLogger(ScrCalendarMonth.class.getName());
    protected ScrDocument parentDoc;
    protected ObjectInfoBO objInfo;
    protected ModuleSectionBO secInfo;
    private int objectId = 0;
    private final String[] columnNames = { "Id", "Fecha", "Hora", "Lugar",
        "Indicador", "Periodo", "Estimación Vector",
        "Encuesta Bloomberg", "Oficial", "Cifra Anterior" };

    public ScrCalendarMonth(ScrDocument scrDoc, ModuleSectionBO secInfo) {
        initComponents();
        this.secInfo = secInfo;
        this.parentDoc = scrDoc;
        if (this.secInfo.getLstObjects() == null) {
            this.secInfo.setLstObjects(new ArrayList<>());
        }
        addObject(GlobalDefines.OBJ_TYPE_CALENDAR_MONTH);
        printMonth();
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
            
            List<EventBO> eventBOLst= AgendaWS.getEventLstMonth(idMiVectorCalendar);
            if(eventBOLst!=null){
                for(EventBO event: eventBOLst){
                    Calendar fecha= event.getFechaCompleta();
                    setEvent(fecha.get(Calendar.DAY_OF_WEEK), fecha.get(Calendar.WEEK_OF_MONTH), event);
                };
            }
        }catch(Exception e){
            LOG.log(Level.INFO, e.getMessage(), e);
        }
    }
    
    private void setEvent(int day, int weekCount, EventBO event){
        switch(weekCount){
            case 1:
                setEventOne(day, event);
                break;
            case 2:
                setEventTwo(day, event);
                break;
            case 3:
                setEventThree(day, event);
                break;
            case 4:
                setEventFourth(day, event);
                break;
            case 5:
                setEventFive(day, event);
                break;
            default:
                break;
        }
    }

    private void setEventOne(int day, EventBO event){
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

    private void setEventTwo(int day, EventBO event){
        switch(day){
        case 1://Domingo
            break;
        case 2://Lunes
            setText(jLunesE_2,event.getEventoDesc());
            break;
        case 3://Martes
            setText(jMartesE_2,event.getEventoDesc());
            break;
        case 4://Miercoles
            setText(jMiercolesE_2,event.getEventoDesc());
            break;
        case 5://Jueves
            setText(jJuevesE_2,event.getEventoDesc());
            break;
        case 6://Viernes
            setText(jViernesE_2,event.getEventoDesc());
            break;
        case 7://Sabado
        default:
            break;
        }
    }

    private void setEventThree(int day, EventBO event){
        switch(day){
        case 1://Domingo
            break;
        case 2://Lunes
            setText(jLunesE_3,event.getEventoDesc());
            break;
        case 3://Martes
            setText(jMartesE_3,event.getEventoDesc());
            break;
        case 4://Miercoles
            setText(jMiercolesE_3,event.getEventoDesc());
            break;
        case 5://Jueves
            setText(jJuevesE_3,event.getEventoDesc());
            break;
        case 6://Viernes
            setText(jViernesE_3,event.getEventoDesc());
            break;
        case 7://Sabado
        default:
            break;
        }
    }

    private void setEventFourth(int day, EventBO event){
        switch(day){
        case 1://Domingo
            break;
        case 2://Lunes
            setText(jLunesE_4,event.getEventoDesc());
            break;
        case 3://Martes
            setText(jMartesE_4,event.getEventoDesc());
            break;
        case 4://Miercoles
            setText(jMiercolesE_4,event.getEventoDesc());
            break;
        case 5://Jueves
            setText(jJuevesE_4,event.getEventoDesc());
            break;
        case 6://Viernes
            setText(jViernesE_4,event.getEventoDesc());
            break;
        case 7://Sabado
        default:
            break;
        }
    }

    private void setEventFive(int day, EventBO event){
        switch(day){
        case 1://Domingo
            break;
        case 2://Lunes
            setText(jLunesE_5,event.getEventoDesc());
            break;
        case 3://Martes
            setText(jMartesE_5,event.getEventoDesc());
            break;
        case 4://Miercoles
            setText(jMiercolesE_5,event.getEventoDesc());
            break;
        case 5://Jueves
            setText(jJuevesE_5,event.getEventoDesc());
            break;
        case 6://Viernes
            setText(jViernesE_5,event.getEventoDesc());
            break;
        case 7://Sabado
        default:
            break;
        }
    }
    
    private void cleanFields(){
        jLunesE_1.setText("");
        jLunesE_2.setText("");
        jLunesE_3.setText("");
        jLunesE_4.setText("");
        jLunesE_5.setText("");
        
        jMartesE_1.setText("");
        jMartesE_2.setText("");
        jMartesE_3.setText("");
        jMartesE_4.setText("");
        jMartesE_5.setText("");
        
        jMiercolesE_1.setText("");
        jMiercolesE_2.setText("");
        jMiercolesE_3.setText("");
        jMiercolesE_4.setText("");
        jMiercolesE_5.setText("");
        
        jJuevesE_1.setText("");
        jJuevesE_2.setText("");
        jJuevesE_3.setText("");
        jJuevesE_4.setText("");
        jJuevesE_5.setText("");
        
        jViernesE_1.setText("");
        jViernesE_2.setText("");
        jViernesE_3.setText("");
        jViernesE_4.setText("");
        jViernesE_5.setText("");
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

    private void printMonth(){
        int weekCount=0;
        Calendar actualMonth= Calendar.getInstance();
        actualMonth.set(Calendar.DAY_OF_MONTH, 1);
        weekCount= actualMonth.get(Calendar.WEEK_OF_YEAR);
        hideDaysFirstWeek(actualMonth.get(Calendar.DAY_OF_WEEK));

        actualMonth.add(Calendar.MONTH, 1);
        weekCount= weekCount - actualMonth.get(Calendar.WEEK_OF_YEAR);
        actualMonth.add(Calendar.DATE, -1);
        hideDaysLastWeek(actualMonth.get(Calendar.DAY_OF_WEEK), weekCount);
    }

    private void hideDaysLastWeek(int day, int weekCount){
        if( weekCount==4){
            switch(day){
            case 1://Domingo
                jLunesE_4.setBackground(Color.LIGHT_GRAY);
            case 2://Lunes
                jMartesE_4.setBackground(Color.LIGHT_GRAY);
            case 3://Martes
                jMiercolesE_4.setBackground(Color.LIGHT_GRAY);
            case 4://Miercoles
                jJuevesE_4.setBackground(Color.LIGHT_GRAY);
            case 5://Jueves
                jViernesE_4.setBackground(Color.LIGHT_GRAY);
            case 6://Viernes
            case 7://Sabado
            default:
                break;
            }
            day= 4;
        }
        switch(day){
        case 1://Domingo
            jLunesE_5.setBackground(Color.LIGHT_GRAY);
        case 2://Lunes
            jMartesE_5.setBackground(Color.LIGHT_GRAY);
        case 3://Martes
            jMiercolesE_5.setBackground(Color.LIGHT_GRAY);
        case 4://Miercoles
            jJuevesE_5.setBackground(Color.LIGHT_GRAY);
        case 5://Jueves
            jViernesE_5.setBackground(Color.LIGHT_GRAY);
        case 6://Viernes
        case 7://Sabado
        default:
            break;
        }
    }

    private void hideDaysFirstWeek(int day){
        switch(day){
        case 7://Sabado
            jViernesE_1.setBackground(Color.LIGHT_GRAY);
        case 6://Viernes
            jJuevesE_1.setBackground(Color.LIGHT_GRAY);
        case 5://Jueves
            jMiercolesE_1.setBackground(Color.LIGHT_GRAY);
        case 4://Miercoles
            jMartesE_1.setBackground(Color.LIGHT_GRAY);
        case 3://Martes
            jLunesE_1.setBackground(Color.LIGHT_GRAY);
        case 2://Lunes
        case 1://Domingo
        default:
            break;
        }
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
        jJuevesT_2 = new javax.swing.JLabel();
        jViernesT_2 = new javax.swing.JLabel();
        jLunesT_2 = new javax.swing.JLabel();
        jMartesT_2 = new javax.swing.JLabel();
        jMiercolesT_2 = new javax.swing.JLabel();
        jMartesT_3 = new javax.swing.JLabel();
        jViernesT_3 = new javax.swing.JLabel();
        jLunesT_3 = new javax.swing.JLabel();
        jMiercolesT_3 = new javax.swing.JLabel();
        jJuevesT_3 = new javax.swing.JLabel();
        jMartesT_4 = new javax.swing.JLabel();
        jViernesT_4 = new javax.swing.JLabel();
        jLunesT_4 = new javax.swing.JLabel();
        jMiercolesT_4 = new javax.swing.JLabel();
        jJuevesT_4 = new javax.swing.JLabel();
        jMartesT_5 = new javax.swing.JLabel();
        jJuevesT_5 = new javax.swing.JLabel();
        jViernesT_5 = new javax.swing.JLabel();
        jMiercolesT_5 = new javax.swing.JLabel();
        jLunesT_5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLunesE_5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jMartesE_5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jMiercolesE_5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jJuevesE_5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jViernesE_5 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jLunesE_4 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jMartesE_4 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jMiercolesE_4 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jJuevesE_4 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jViernesE_4 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jLunesE_3 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jMartesE_3 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jMiercolesE_3 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jJuevesE_3 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jViernesE_3 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jLunesE_2 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jMartesE_2 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jMiercolesE_2 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jJuevesE_2 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jViernesE_2 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jLunesE_1 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jMartesE_1 = new javax.swing.JLabel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jMiercolesE_1 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jJuevesE_1 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
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
        setMinimumSize(new java.awt.Dimension(557, 550));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(557, 550));

        jCargarDatos.setText("Cargar Datos");
        jCargarDatos.setName(""); // NOI18N
        jCargarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCargarDatosActionPerformed(evt);
            }
        });

        jLunesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_1.setText("Lunes");
        jLunesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_1.setName(""); // NOI18N
        jLunesT_1.setOpaque(true);

        jMartesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_1.setText("Martes");
        jMartesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_1.setName(""); // NOI18N
        jMartesT_1.setOpaque(true);

        jMiercolesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_1.setText("Miercoles");
        jMiercolesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_1.setName(""); // NOI18N
        jMiercolesT_1.setOpaque(true);

        jJuevesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_1.setText("Jueves");
        jJuevesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_1.setName(""); // NOI18N
        jJuevesT_1.setOpaque(true);

        jViernesT_1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_1.setText("Viernes");
        jViernesT_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_1.setName(""); // NOI18N
        jViernesT_1.setOpaque(true);

        jJuevesT_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_2.setText("Jueves");
        jJuevesT_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_2.setName(""); // NOI18N
        jJuevesT_2.setOpaque(true);

        jViernesT_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_2.setText("Viernes");
        jViernesT_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_2.setName(""); // NOI18N
        jViernesT_2.setOpaque(true);

        jLunesT_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_2.setText("Lunes");
        jLunesT_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_2.setName(""); // NOI18N
        jLunesT_2.setOpaque(true);

        jMartesT_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_2.setText("Martes");
        jMartesT_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_2.setName(""); // NOI18N
        jMartesT_2.setOpaque(true);

        jMiercolesT_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_2.setText("Miercoles");
        jMiercolesT_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_2.setName(""); // NOI18N
        jMiercolesT_2.setOpaque(true);

        jMartesT_3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_3.setText("Martes");
        jMartesT_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_3.setName(""); // NOI18N
        jMartesT_3.setOpaque(true);

        jViernesT_3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_3.setText("Viernes");
        jViernesT_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_3.setName(""); // NOI18N
        jViernesT_3.setOpaque(true);

        jLunesT_3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_3.setText("Lunes");
        jLunesT_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_3.setName(""); // NOI18N
        jLunesT_3.setOpaque(true);

        jMiercolesT_3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_3.setText("Miercoles");
        jMiercolesT_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_3.setName(""); // NOI18N
        jMiercolesT_3.setOpaque(true);

        jJuevesT_3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_3.setText("Jueves");
        jJuevesT_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_3.setName(""); // NOI18N
        jJuevesT_3.setOpaque(true);

        jMartesT_4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_4.setText("Martes");
        jMartesT_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_4.setName(""); // NOI18N
        jMartesT_4.setOpaque(true);

        jViernesT_4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_4.setText("Viernes");
        jViernesT_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_4.setName(""); // NOI18N
        jViernesT_4.setOpaque(true);

        jLunesT_4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_4.setText("Lunes");
        jLunesT_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_4.setName(""); // NOI18N
        jLunesT_4.setOpaque(true);

        jMiercolesT_4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_4.setText("Miercoles");
        jMiercolesT_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_4.setName(""); // NOI18N
        jMiercolesT_4.setOpaque(true);

        jJuevesT_4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_4.setText("Jueves");
        jJuevesT_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_4.setName(""); // NOI18N
        jJuevesT_4.setOpaque(true);

        jMartesT_5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMartesT_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMartesT_5.setText("Martes");
        jMartesT_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesT_5.setName(""); // NOI18N
        jMartesT_5.setOpaque(true);

        jJuevesT_5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jJuevesT_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jJuevesT_5.setText("Jueves");
        jJuevesT_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesT_5.setName(""); // NOI18N
        jJuevesT_5.setOpaque(true);

        jViernesT_5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jViernesT_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jViernesT_5.setText("Viernes");
        jViernesT_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesT_5.setName(""); // NOI18N
        jViernesT_5.setOpaque(true);

        jMiercolesT_5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMiercolesT_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMiercolesT_5.setText("Miercoles");
        jMiercolesT_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesT_5.setName(""); // NOI18N
        jMiercolesT_5.setOpaque(true);

        jLunesT_5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLunesT_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLunesT_5.setText("Lunes");
        jLunesT_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesT_5.setName(""); // NOI18N
        jLunesT_5.setOpaque(true);

        jScrollPane1.setName(""); // NOI18N

        jLunesE_5.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_5.setToolTipText("");
        jLunesE_5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_5.setAutoscrolls(true);
        jLunesE_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_5.setOpaque(true);
        jScrollPane1.setViewportView(jLunesE_5);

        jScrollPane2.setName(""); // NOI18N

        jMartesE_5.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_5.setToolTipText("");
        jMartesE_5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_5.setAutoscrolls(true);
        jMartesE_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_5.setOpaque(true);
        jScrollPane2.setViewportView(jMartesE_5);

        jScrollPane3.setName(""); // NOI18N

        jMiercolesE_5.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_5.setToolTipText("");
        jMiercolesE_5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_5.setAutoscrolls(true);
        jMiercolesE_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_5.setOpaque(true);
        jScrollPane3.setViewportView(jMiercolesE_5);

        jScrollPane4.setName(""); // NOI18N

        jJuevesE_5.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_5.setToolTipText("");
        jJuevesE_5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_5.setAutoscrolls(true);
        jJuevesE_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_5.setOpaque(true);
        jScrollPane4.setViewportView(jJuevesE_5);

        jScrollPane5.setName(""); // NOI18N

        jViernesE_5.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_5.setToolTipText("");
        jViernesE_5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_5.setAutoscrolls(true);
        jViernesE_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_5.setOpaque(true);
        jScrollPane5.setViewportView(jViernesE_5);

        jScrollPane6.setName(""); // NOI18N

        jLunesE_4.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_4.setToolTipText("");
        jLunesE_4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_4.setAutoscrolls(true);
        jLunesE_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_4.setOpaque(true);
        jScrollPane6.setViewportView(jLunesE_4);

        jScrollPane7.setName(""); // NOI18N

        jMartesE_4.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_4.setToolTipText("");
        jMartesE_4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_4.setAutoscrolls(true);
        jMartesE_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_4.setOpaque(true);
        jScrollPane7.setViewportView(jMartesE_4);

        jScrollPane8.setName(""); // NOI18N

        jMiercolesE_4.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_4.setToolTipText("");
        jMiercolesE_4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_4.setAutoscrolls(true);
        jMiercolesE_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_4.setOpaque(true);
        jScrollPane8.setViewportView(jMiercolesE_4);

        jScrollPane9.setName(""); // NOI18N

        jJuevesE_4.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_4.setToolTipText("");
        jJuevesE_4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_4.setAutoscrolls(true);
        jJuevesE_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_4.setOpaque(true);
        jScrollPane9.setViewportView(jJuevesE_4);

        jScrollPane10.setName(""); // NOI18N

        jViernesE_4.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_4.setToolTipText("");
        jViernesE_4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_4.setAutoscrolls(true);
        jViernesE_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_4.setOpaque(true);
        jScrollPane10.setViewportView(jViernesE_4);

        jScrollPane11.setMaximumSize(new java.awt.Dimension(100, 70));
        jScrollPane11.setMinimumSize(new java.awt.Dimension(100, 70));
        jScrollPane11.setName(""); // NOI18N
        jScrollPane11.setPreferredSize(new java.awt.Dimension(100, 70));

        jLunesE_3.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_3.setToolTipText("");
        jLunesE_3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_3.setAutoscrolls(true);
        jLunesE_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_3.setOpaque(true);
        jScrollPane11.setViewportView(jLunesE_3);

        jScrollPane12.setMaximumSize(new java.awt.Dimension(100, 70));
        jScrollPane12.setMinimumSize(new java.awt.Dimension(100, 70));
        jScrollPane12.setName(""); // NOI18N
        jScrollPane12.setPreferredSize(new java.awt.Dimension(100, 70));

        jMartesE_3.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_3.setToolTipText("");
        jMartesE_3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_3.setAutoscrolls(true);
        jMartesE_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_3.setOpaque(true);
        jScrollPane12.setViewportView(jMartesE_3);

        jScrollPane13.setMaximumSize(new java.awt.Dimension(100, 70));
        jScrollPane13.setMinimumSize(new java.awt.Dimension(100, 70));
        jScrollPane13.setName(""); // NOI18N
        jScrollPane13.setPreferredSize(new java.awt.Dimension(100, 70));

        jMiercolesE_3.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_3.setToolTipText("");
        jMiercolesE_3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_3.setAutoscrolls(true);
        jMiercolesE_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_3.setOpaque(true);
        jScrollPane13.setViewportView(jMiercolesE_3);

        jScrollPane14.setMaximumSize(new java.awt.Dimension(100, 70));
        jScrollPane14.setMinimumSize(new java.awt.Dimension(100, 70));
        jScrollPane14.setName(""); // NOI18N
        jScrollPane14.setPreferredSize(new java.awt.Dimension(100, 70));

        jJuevesE_3.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_3.setToolTipText("");
        jJuevesE_3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_3.setAutoscrolls(true);
        jJuevesE_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_3.setOpaque(true);
        jScrollPane14.setViewportView(jJuevesE_3);

        jScrollPane15.setMaximumSize(new java.awt.Dimension(100, 70));
        jScrollPane15.setMinimumSize(new java.awt.Dimension(100, 70));
        jScrollPane15.setName(""); // NOI18N
        jScrollPane15.setPreferredSize(new java.awt.Dimension(100, 70));

        jViernesE_3.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_3.setToolTipText("");
        jViernesE_3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_3.setAutoscrolls(true);
        jViernesE_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_3.setOpaque(true);
        jScrollPane15.setViewportView(jViernesE_3);

        jScrollPane16.setName(""); // NOI18N

        jLunesE_2.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_2.setToolTipText("");
        jLunesE_2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_2.setAutoscrolls(true);
        jLunesE_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_2.setOpaque(true);
        jScrollPane16.setViewportView(jLunesE_2);

        jScrollPane17.setName(""); // NOI18N

        jMartesE_2.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_2.setToolTipText("");
        jMartesE_2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_2.setAutoscrolls(true);
        jMartesE_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_2.setOpaque(true);
        jScrollPane17.setViewportView(jMartesE_2);

        jScrollPane18.setName(""); // NOI18N

        jMiercolesE_2.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_2.setToolTipText("");
        jMiercolesE_2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_2.setAutoscrolls(true);
        jMiercolesE_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_2.setOpaque(true);
        jScrollPane18.setViewportView(jMiercolesE_2);

        jScrollPane19.setName(""); // NOI18N

        jJuevesE_2.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_2.setToolTipText("");
        jJuevesE_2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_2.setAutoscrolls(true);
        jJuevesE_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_2.setOpaque(true);
        jScrollPane19.setViewportView(jJuevesE_2);

        jScrollPane20.setName(""); // NOI18N

        jViernesE_2.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_2.setToolTipText("");
        jViernesE_2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_2.setAutoscrolls(true);
        jViernesE_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_2.setOpaque(true);
        jScrollPane20.setViewportView(jViernesE_2);

        jScrollPane21.setName(""); // NOI18N

        jLunesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jLunesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLunesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLunesE_1.setToolTipText("");
        jLunesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLunesE_1.setAutoscrolls(true);
        jLunesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLunesE_1.setOpaque(true);
        jScrollPane21.setViewportView(jLunesE_1);

        jScrollPane22.setName(""); // NOI18N

        jMartesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jMartesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMartesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMartesE_1.setToolTipText("");
        jMartesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMartesE_1.setAutoscrolls(true);
        jMartesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMartesE_1.setOpaque(true);
        jScrollPane22.setViewportView(jMartesE_1);

        jScrollPane23.setName(""); // NOI18N

        jMiercolesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jMiercolesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jMiercolesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jMiercolesE_1.setToolTipText("");
        jMiercolesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jMiercolesE_1.setAutoscrolls(true);
        jMiercolesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMiercolesE_1.setOpaque(true);
        jScrollPane23.setViewportView(jMiercolesE_1);

        jScrollPane24.setName(""); // NOI18N

        jJuevesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jJuevesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jJuevesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jJuevesE_1.setToolTipText("");
        jJuevesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jJuevesE_1.setAutoscrolls(true);
        jJuevesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jJuevesE_1.setOpaque(true);
        jScrollPane24.setViewportView(jJuevesE_1);

        jScrollPane25.setName(""); // NOI18N

        jViernesE_1.setBackground(new java.awt.Color(255, 255, 255));
        jViernesE_1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jViernesE_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jViernesE_1.setToolTipText("");
        jViernesE_1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jViernesE_1.setAutoscrolls(true);
        jViernesE_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jViernesE_1.setOpaque(true);
        jScrollPane25.setViewportView(jViernesE_1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLunesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jMartesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jMiercolesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jJuevesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jViernesT_1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCargarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLunesT_2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMartesT_2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMiercolesT_2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jJuevesT_2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jViernesT_2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLunesT_3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMartesT_3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMiercolesT_3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jJuevesT_3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jViernesT_3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLunesT_4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMartesT_4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMiercolesT_4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jJuevesT_4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jViernesT_4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLunesT_5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMartesT_5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMiercolesT_5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jJuevesT_5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jViernesT_5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jCargarDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jViernesT_1)
                    .addComponent(jJuevesT_1)
                    .addComponent(jMiercolesT_1)
                    .addComponent(jMartesT_1)
                    .addComponent(jLunesT_1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLunesT_2)
                    .addComponent(jMartesT_2)
                    .addComponent(jMiercolesT_2)
                    .addComponent(jJuevesT_2)
                    .addComponent(jViernesT_2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLunesT_3)
                    .addComponent(jMartesT_3)
                    .addComponent(jMiercolesT_3)
                    .addComponent(jJuevesT_3)
                    .addComponent(jViernesT_3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLunesT_4)
                    .addComponent(jMartesT_4)
                    .addComponent(jMiercolesT_4)
                    .addComponent(jJuevesT_4)
                    .addComponent(jViernesT_4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLunesT_5)
                    .addComponent(jMartesT_5)
                    .addComponent(jMiercolesT_5)
                    .addComponent(jJuevesT_5)
                    .addComponent(jViernesT_5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void jCargarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCargarDatosActionPerformed
        cleanFields();
        printEvents();
    }//GEN-LAST:event_jCargarDatosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCargarDatos;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jJuevesE_1;
    private javax.swing.JLabel jJuevesE_2;
    private javax.swing.JLabel jJuevesE_3;
    private javax.swing.JLabel jJuevesE_4;
    private javax.swing.JLabel jJuevesE_5;
    private javax.swing.JLabel jJuevesT_1;
    private javax.swing.JLabel jJuevesT_2;
    private javax.swing.JLabel jJuevesT_3;
    private javax.swing.JLabel jJuevesT_4;
    private javax.swing.JLabel jJuevesT_5;
    private javax.swing.JLabel jLunesE_1;
    private javax.swing.JLabel jLunesE_2;
    private javax.swing.JLabel jLunesE_3;
    private javax.swing.JLabel jLunesE_4;
    private javax.swing.JLabel jLunesE_5;
    private javax.swing.JLabel jLunesT_1;
    private javax.swing.JLabel jLunesT_2;
    private javax.swing.JLabel jLunesT_3;
    private javax.swing.JLabel jLunesT_4;
    private javax.swing.JLabel jLunesT_5;
    private javax.swing.JLabel jMartesE_1;
    private javax.swing.JLabel jMartesE_2;
    private javax.swing.JLabel jMartesE_3;
    private javax.swing.JLabel jMartesE_4;
    private javax.swing.JLabel jMartesE_5;
    private javax.swing.JLabel jMartesT_1;
    private javax.swing.JLabel jMartesT_2;
    private javax.swing.JLabel jMartesT_3;
    private javax.swing.JLabel jMartesT_4;
    private javax.swing.JLabel jMartesT_5;
    private javax.swing.JLabel jMiercolesE_1;
    private javax.swing.JLabel jMiercolesE_2;
    private javax.swing.JLabel jMiercolesE_3;
    private javax.swing.JLabel jMiercolesE_4;
    private javax.swing.JLabel jMiercolesE_5;
    private javax.swing.JLabel jMiercolesT_1;
    private javax.swing.JLabel jMiercolesT_2;
    private javax.swing.JLabel jMiercolesT_3;
    private javax.swing.JLabel jMiercolesT_4;
    private javax.swing.JLabel jMiercolesT_5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel jViernesE_1;
    private javax.swing.JLabel jViernesE_2;
    private javax.swing.JLabel jViernesE_3;
    private javax.swing.JLabel jViernesE_4;
    private javax.swing.JLabel jViernesE_5;
    private javax.swing.JLabel jViernesT_1;
    private javax.swing.JLabel jViernesT_2;
    private javax.swing.JLabel jViernesT_3;
    private javax.swing.JLabel jViernesT_4;
    private javax.swing.JLabel jViernesT_5;
    // End of variables declaration//GEN-END:variables
}
