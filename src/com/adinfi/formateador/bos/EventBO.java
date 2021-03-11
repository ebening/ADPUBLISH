/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.bos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class EventBO {

    public double getEventoUniqueId() {
        return EventoUniqueId;
    }

    public void setEventoUniqueId(double EventoUniqueId) {
        this.EventoUniqueId = EventoUniqueId;
    }

    public double getEventoId() {
        return EventoId;
    }

    public void setEventoId(double EventoId) {
        this.EventoId = EventoId;
    }

    public int getSemana() {
        return Semana;
    }

    public void setSemana(int Semana) {
        this.Semana = Semana;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setTiempo(String Tiempo) {
        this.Tiempo = Tiempo;
    }

    public String getEmisora() {
        return Emisora;
    }

    public void setEmisora(String Emisora) {
        this.Emisora = Emisora;
    }

    public String getEventoDesc() {
        return EventoDesc;
    }

    public void setEventoDesc(String EventoDesc) {
        this.EventoDesc = EventoDesc;
    }

    public String getEventoDescIng() {
        return EventoDescIng;
    }

    public void setEventoDescIng(String EventoDescIng) {
        this.EventoDescIng = EventoDescIng;
    }

    public int getLugarId() {
        return LugarId;
    }

    public void setLugarId(int LugarId) {
        this.LugarId = LugarId;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String Lugar) {
        this.Lugar = Lugar;
    }

    public String getLugarIng() {
        return LugarIng;
    }

    public void setLugarIng(String LugarIng) {
        this.LugarIng = LugarIng;
    }

    public int getTipoEventoId() {
        return TipoEventoId;
    }

    public void setTipoEventoId(int TipoEventoId) {
        this.TipoEventoId = TipoEventoId;
    }

    public String getTipoEvento() {
        return TipoEvento;
    }

    public void setTipoEvento(String TipoEvento) {
        this.TipoEvento = TipoEvento;
    }

    public String getEntidad() {
        return Entidad;
    }

    public void setEntidad(String Entidad) {
        this.Entidad = Entidad;
    }

    public int getRanking() {
        return Ranking;
    }

    public void setRanking(int Ranking) {
        this.Ranking = Ranking;
    }

    public String getCicloId() {
        return CicloId;
    }

    public void setCicloId(String CicloId) {
        this.CicloId = CicloId;
    }

    public String getPeriodo() {
        return Periodo;
    }

    public void setPeriodo(String Periodo) {
        this.Periodo = Periodo;
    }

    public String getEstimacionVector() {
        return EstimacionVector;
    }

    public void setEstimacionVector(String EstimacionVector) {
        this.EstimacionVector = EstimacionVector;
    }

    public String getEstimacionBloomberg() {
        return EstimacionBloomberg;
    }

    public void setEstimacionBloomberg(String EstimacionBloomberg) {
        this.EstimacionBloomberg = EstimacionBloomberg;
    }

    public String getCifraOficial() {
        return CifraOficial;
    }

    public void setCifraOficial(String CifraOficial) {
        this.CifraOficial = CifraOficial;
    }

    public String getCifraOficialAnt() {
        return CifraOficialAnt;
    }

    public void setCifraOficialAnt(String CifraOficialAnt) {
        this.CifraOficialAnt = CifraOficialAnt;
    }
    
    public Calendar getFechaCompleta(){
        try {
            Calendar cal=Calendar.getInstance();
            Date fechaAux;
            if(getTiempo()!=null&&getTiempo().trim().length()>=5){
                fechaAux= sdf2.parse(getFecha() + " " + getTiempo());
            }else{
                fechaAux= sdf.parse(getFecha());
            }
            cal.setTime(fechaAux);
            return cal;
        } catch (Exception ex) {
            Logger.getLogger(EventBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    public String getIcsDescripction() {
        return IcsDescripction;
    }

    public void setIcsDescripction(String IcsDescripction) {
        this.IcsDescripction = IcsDescripction;
    }
    
    
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private double EventoUniqueId;
    private double EventoId;  
    private int Semana;  
    private String Fecha;  
    private String Tiempo;  
    private String Emisora;  
    private String EventoDesc; 
    private String EventoDescIng; 
    private int LugarId; 
    private String Lugar; 
    private String LugarIng; 
    private int TipoEventoId; 
    private String TipoEvento; 
    private String Entidad; 
    private int Ranking; 
    private String CicloId; 
    private String Periodo; 
    private String EstimacionVector; 
    private String EstimacionBloomberg; 
    private String CifraOficial;
    private String CifraOficialAnt;
    private String Subject;
    private String IcsDescripction;
}