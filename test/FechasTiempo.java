
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USUARIO
 */
public class FechasTiempo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

DateFormat df = new SimpleDateFormat("hh:mm/a");
Date today = Calendar.getInstance().getTime();        
String reportDate = df.format(today);

System.out.println(reportDate);

    }

}
