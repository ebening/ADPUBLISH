
import com.adinfi.formateador.util.Utilerias;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class DateTimeFormat {

    private final DateFormat TWELVE_TF = new SimpleDateFormat("hh:mma");
    private final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");

    /**
     * @param args the command line arguments
     */
    /* Convierte una hora en formato de 24hrs Ejemplo: (4:00PM a 16:00)*/
    private String convertTo24HoursFormat(String twelveHourTime)
            throws ParseException {
        return TWENTY_FOUR_TF.format(TWELVE_TF.parse(twelveHourTime));
    }

    public static void main(String[] args) throws ParseException {
        DateTimeFormat f = new DateTimeFormat();

        String time = "06:20/PM";
        String date = "2015-02-25";
        //String date = "02/02/2015";
        String formatIn = "yyyy-MM-dd";
        String formatOut = "dd/MM/yyyy";       

        System.out.println(f.convertTo24HoursFormat(time.replace("/", "")));

        SimpleDateFormat inFormat = new SimpleDateFormat(formatIn);
        Date d = inFormat.parse(date);
        
        inFormat.applyPattern(formatOut);
        String outFormat = inFormat.format(d);
        System.out.println(outFormat);

    }

}
