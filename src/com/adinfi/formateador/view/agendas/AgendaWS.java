package com.adinfi.formateador.view.agendas;

import com.adinfi.formateador.bos.EventBO;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adinfi.ws.data.CalendarioEconomico;
import com.adinfi.ws.data.Data_Impl;
import com.adinfi.ws.data.EventoCalendario;
import com.adinfi.ws.data.IData;
import com.adinfi.ws.data.IData_Stub;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import org.tempuri.IDataProxy;
/**
 *
 * @author Administrator
 */
public class AgendaWS {
    public static Calendar fechaIni;
    public static Calendar fechaFin;
    public static List<EventBO> getEventLst(int documentTypeIdMiVector){
        return getEventLst(documentTypeIdMiVector, GlobalDefines.AGENDA);
        //getEventLst(GlobalDefines.INDICADORES, GlobalDefines.AGENDA);
    }
    public static List<EventBO> getEventLstMonth(int documentTypeIdMiVector){
        /*List<EventBO> eventLst= getEventLst(GlobalDefines.ASAMBLEAS_DIVIDENDOS, GlobalDefines.AGENDA_MENSUAL);
        eventLst.addAll(getEventLst(GlobalDefines.REPORTES, GlobalDefines.AGENDA_SEMANAL));
        eventLst.addAll(getEventLst(GlobalDefines.FINANCIEROS_EUA, GlobalDefines.AGENDA_SEMANAL));*/
        return getEventLst(documentTypeIdMiVector, GlobalDefines.AGENDA_MENSUAL);
    }
    public static List<EventBO> getEventLstWeek(int documentTypeIdMiVector){
         //List<EventBO> eventLst= getEventLst(GlobalDefines.EVENTOS_PROYECCIONES, GlobalDefines.AGENDA_SEMANAL);
        //eventLst.addAll(getEventLst(GlobalDefines.FINANCIEROS_EUA, GlobalDefines.AGENDA_SEMANAL));
        return getEventLst(documentTypeIdMiVector, GlobalDefines.AGENDA_SEMANAL);
    }
    public static List<EventBO> getEventLst(int tipoDocument, String tipPlantilla){
        List<EventBO> eventLst= new ArrayList<EventBO>();
        try {
            fechaIni = Calendar.getInstance();
            fechaFin = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        
            IData_Stub stub = (IData_Stub) new Data_Impl().getBasicHttpBinding_IData();
            UtileriasWS.setEndpoint(stub);
            
            CalendarioEconomico respuesta = stub.getCalendarioFormateador(tipoDocument, tipPlantilla);
            
            fechaIni.setTime( sdf.parse( respuesta.getFechaIncial() ) );
            fechaFin.setTime( sdf.parse( respuesta.getFechaFinal() ) );
            
            eventLst= merge(respuesta.getEventos().getEventoCalendario());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El servicio de calendario no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AgendaWS.class.getName()).log(Level.INFO, null, ex);
        }
        return eventLst;
    }
    private static List<EventBO> merge(EventoCalendario[] calendarioArray){
        List<EventBO> eventBOLst= new ArrayList<>();
        if(calendarioArray!=null){
            for(EventoCalendario calendario: calendarioArray){
                EventBO eventBO= new EventBO();
                eventBO.setEventoUniqueId(calendario.getEventoUniqueId());
                eventBO.setSemana(calendario.getSemana());
                eventBO.setFecha(calendario.getFecha());
                eventBO.setTiempo(calendario.getTiempo());
                eventBO.setEventoId(calendario.getEventoId());
                eventBO.setLugarId(calendario.getLugarId());
                eventBO.setLugar(calendario.getLugar());
                eventBO.setRanking(calendario.getRanking());
                eventBO.setPeriodo(calendario.getPeriodo());
                eventBO.setEstimacionVector(calendario.getEstimacionVector());
                eventBO.setEstimacionBloomberg(calendario.getEstimacionBloomberg());
                eventBO.setCifraOficial(calendario.getCifraOficial());
                eventBO.setCifraOficialAnt(calendario.getCifraOficialAnt());
                eventBO.setEventoDesc(calendario.getDescripcion());
                eventBO.setEmisora(calendario.getEmisora());
                eventBO.setSubject(calendario.getSubject());
                eventBO.setIcsDescripction(calendario.getIcsDescripcion());
                eventBOLst.add(eventBO);                   
            }
        }
        return eventBOLst;
    }
}
