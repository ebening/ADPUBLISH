/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webservices;

import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import org.datacontract.schemas._2004._07.VCB_Analisis_Data.Emisora;
import org.datacontract.schemas._2004._07.VCB_Analisis_Data_Emisoras.EmisoraRecomendacionLog;
import org.tempuri.IEmisorasProxy;

/**
 *
 * @author USUARIO
 */
public class Emisoras {
    
    public static void main(String[] args) {
        try {
             org.tempuri.IEmisorasProxy proxy = new IEmisorasProxy(Utilerias.getProperty(ApplicationProperties.EMISORAS_WS));
             Emisora [] emisoras = proxy.emisorasConRecomendacion("");
             String emisorasXML = "<Emisoras>" +
                              
                                "<Emisora>Bonos</Emisora>" +
                                "</Emisoras>";
             EmisoraRecomendacionLog[] emisorasRecom = proxy.logRecomendacionesFundamentales(emisorasXML);
             int i = 0;
   
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de log de recomendaciones fundamentales no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
