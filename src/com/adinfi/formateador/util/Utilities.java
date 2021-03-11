package com.adinfi.formateador.util;
    
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utilities {
	
	private static final String DATE_FORMAT_NOW = "dd-MM-yyyy";	
	private static final String DATE_FORMAT_NOW2 = "yyyy-MM-dd";	
	private static final String TIME_FORMAT_NOW = "HH:mm:ss";
	private static final String DATE_FORMAT_TIMESTAB = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Este metodo obtiene una cadena que representa 
	 * la fecha actual con un formato "dd-MM-yyyy".
	 * @return String - De la fecha actual.
	 */
	public static String nowDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
        
        	
	/**
	 * Este metodo obtiene una cadena que representa 
	 * la fecha actual con un formato "dd-MM-yyyy".
	 * @return String - De la fecha actual.
	 */
	public static String nowDateSQL(String dateStr) {
            String fecha="";
            DateFormat fromFormat = new SimpleDateFormat("yyyy/MM/dd");
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy");
            toFormat.setLenient(false);            
            java.util.Date date;
            try {
                date = fromFormat.parse(dateStr);
                System.out.println(toFormat.format(date));              
                fecha = toFormat.format(date);              
            } catch (ParseException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
            return fecha;            
	}
      
        /**
        public static void main(String[] args) {
        Utilities.nowDateSQL("2014/07/21");
    }
    **/ 
	
	/**
	 * Este metodo obtiene una cadena que representa 
	 * la fecha actual con un formato "yyyy-dd-mm".
	 * @return String - De la fecha actual.
	 */
	public static String nowDate2(String strDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW2);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Este metodo obtiene una cadena que representa
	 * la hora actual con un formato "HH:mm:ss".
	 * @return String - De la hora actual.
	 */
	public static String nowTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Este metodo obtiene una cadena que representa 
	 * la fecha y hora actual como TimeStamp en formato 
	 * "yyyy-MM-dd HH:mm:ss" para ser almacenada en DB2.
	 * @return String - De la fecha actual.
	 */
	public static String nowTimeStamp() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TIMESTAB);
		return sdf.format(cal.getTime());
	}
	/**
	 * Este metodo genera un sql.Date a partir de 
	 * un String de fecha con formato "yyyy-MM-dd".
	 * @param strDate - String con la fecha.
	 * @return sql.Date de la fecha ingresada.
	 */
	public static Date parserSqlDate(String strDate) {
		strDate = strDate.replaceAll("/", "-");
		Date date = java.sql.Date.valueOf(strDate);
		return date;
	}
	
	
	
	/**
	 * Metodo que crea un objeto TimeStamp a partir 
	 * de un String en formato "yyyy-MM-dd kk:mm:ss".
	 * @param strDate - String con la fecha.
	 * @return Objeto TimeStamp de la fecha ingresada.
	 */
	public static Timestamp parseSqlTimeStamp(String strDate){
		strDate = strDate.replaceAll("/", "-");
		Timestamp date = java.sql.Timestamp.valueOf(strDate);
		return date;
	}
	
	/**
	 * Metodo que recibe una fecha en String con formato 
	 * "dd-MM-yyyy" y devuelve una cadena que representa 
	 * la misma fecha pero con formato "yyyy-MM-dd".
	 * @param fecha - String de fecha.
	 * @return String de fecha en formato yyyy-MM-dd.
	 */
	public static String convertDateFormat(String fecha){
		
		String[] datos = fecha.split("\\-");
		String nuevaFecha = datos[2]+"-"+datos[1]+"-"+datos[0];
		
		return nuevaFecha;
	}
	
	/**
	 * Método que recibe un TimeStamp en String con formato
	 * "dd-MM-yyyy HH:mm:ss" y devuelve una cadena que representa
	 * el mismo TimeStamp pero con formato "yyyy-MM-dd HH:mm:ss".
	 * @param fecha - String del TimeStamp.
	 * @return String de TimeStamp con formato "yyyy-MM-dd HH:mm:ss".
	 */
	public static String convertTimeStampFormat(String fecha){
		String[] partes = fecha.split("\\ ");
		
		String[] date = partes[0].split("\\-");
		String nuevaFecha = date[2]+"-"+date[1]+"-"+date[0];
		
		String time = partes[1];
		nuevaFecha += " "+time;
		
		return nuevaFecha;
	}
	
	/**
	 * Este metodo recive una fecha en formato dd-MM-yyyy
	 * y la cambia a un formato yyyy-MM-dd requerido por DB2.
	 * @param fecha - Fecha a cambiar.
	 * @return fecha en formato para DB2 en forma de cadena.
	 */
	public static String getStringDateSql(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		String date = calendar.get(Calendar.YEAR)+"-"+completeData((calendar.get(Calendar.MONTH)+1))+"-"+completeData(calendar.get(Calendar.DAY_OF_MONTH));
		return date;
	}
	
	
	/**
	 * Metodo que obtiene un TimeStamp obtenido
	 * desde DB2 y lo convierte en un String con
	 * formato para "dd-MM-yyyy hh:mmaa" para
	 * ser mostrado en las diferentes pantallas.
	 * @param fecha - Objeto TimeStamp de DB2.
	 * @return String con la fecha del TimeStamp.
	 */
	public static String getStringTimeStamp(Timestamp fecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mmaa");
		String date = sdf.format(cal.getTime());
						
		return date;
	}
	
	/**
	 * Método que convierte un String que representa una
	 * fecha de tipo SQL Date a una fecha de tipo TimeStamp
	 * en formato de String mostrando la primera hora, minuto
	 * y segundo de la fecha ingresada..
	 * 
	 * Adicionalmente, si el prametro 'endOfDay' es igual a 'true'
	 * la fecha regresada representa la primera hora, minuto y 
	 * segundo del dia siguiente a la fecha ingresada.
	 * 
	 * @param sqlDate - String con formato de fecha Sql (yyyy-mm-dd).
	 * @param endOfDay - Si representa el fin del dia.
	 * @return String con la fecha en formato de TimeStamp (yyyy-mm-dd hh:mm:ss).
	 */
	public static String getStringTimeStampFromStringSQLDate(String sqlDate, boolean endOfDay){
		String timeStampString = "";
		
		if(sqlDate != null && !sqlDate.isEmpty()){
			Date date = java.sql.Date.valueOf(sqlDate);
			
			if(endOfDay){
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_YEAR,1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				java.sql.Date sqlTommorow = new java.sql.Date(cal.getTimeInMillis());
				
				java.sql.Timestamp timeStampDate = new Timestamp(sqlTommorow.getTime());
				timeStampString = timeStampDate.toString();
				
			}else{
				java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
				timeStampString = timeStampDate.toString();
			}
		}
		return timeStampString;
	}
	
	public static String completeData(int number) {
		if(number < 10){
			return "0"+number;
		}
		return ""+number;
	}
	
	
	public static boolean FileCopy(String sourceFile, String destinationFile) 
			throws IOException {	
		File inFile = new File(sourceFile);
		File outFile = new File(destinationFile);

		FileInputStream in = new FileInputStream(inFile);
		FileOutputStream out = new FileOutputStream(outFile);

		int c;
		while( (c = in.read() ) != -1)
			out.write(c);

		in.close();
		out.close();
		
		inFile.delete();
		return true;
	}
	

	
	public static String getLoteActual(){
		String lote = "";
		
        try {
        	Calendar c = Calendar.getInstance();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			c.setTime( sdf.parse(Utilities.nowDate()));
			
			c.setFirstDayOfWeek(6);
			
			int semana = c.get( Calendar.WEEK_OF_YEAR );
			int mes = c.get( Calendar.MONTH );
			int anio = c.get( Calendar.YEAR);
		
			StringBuffer sb = new StringBuffer();
			sb.append(String.valueOf(anio));
			sb.append("-");
			
			if(semana <= 9){
				sb.append("0");
			}
			
			sb.append(String.valueOf(semana));
			lote = sb.toString();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
		return lote;
        }
	
	/**
	 * Método que convierte la cadena ingresada
	 * en un objeto de tipo Long.
	 * @param cadena - String con el numero a convertir.
	 * @return Objeto Long.
	 * @throws NumberFormatException
	 */
	public static Long convertToLong(String cadena) throws NumberFormatException{
		Long numero = Long.valueOf(cadena);
		return numero;
	}
	
	/**
	 * Método que convierte la cadena ingresada
	 * en un objeto de tipo BigInteger.
	 * @param cadena - String con el numero a convertir.
	 * @return Objeto BigInteger.
	 * @throws NumberFormatException
	 */
	public static BigInteger convertToBigInteger(String cadena) throws NumberFormatException{
		BigInteger numero = new BigInteger(cadena);
		return numero;
	}
	
	/**
	 * Método que convierte la cadena ingresada
	 * en un objeto de tipo Integer.
	 * @param cadena - String con el numero a convertir.
	 * @return Objeto Integer.
	 * @throws NumberFormatException
	 */
	public static Integer convertToInteger(String cadena) throws NumberFormatException{
		Integer numero = new Integer(cadena);
		return numero;
	}
	
	/**
	 * Método que convierte la cadena ingresada
	 * en un objeto de tipo BigDecimal.
	 * @param cadena - String con el numero a convertir.
	 * @return Objeto BigDecimal.
	 * @throws NumberFormatException
	 */
	public static BigDecimal convertToBigDecimal(String cadena) throws NumberFormatException{
		BigDecimal numero = new BigDecimal(cadena);
		return numero;
	}	
	
	/**
	 * 
	 * @param s_cadena
	 * @param s_caracteres
	 * @return
	 */
	public static String EliminaCaracteres(String s_cadena, String s_caracteres)
	{
	  String nueva_cadena = "";
	  Character caracter = null;
	  boolean valido = true;
	  /* Va recorriendo la cadena s_cadena y copia a la cadena que va a regresar,
	
	     sólo los caracteres que no estén en la cadena s_caracteres */
	  for (int i=0; i<s_cadena.length(); i++)
	      {
	       valido = true;
	       for (int j=0; j<s_caracteres.length(); j++)
	           {
	            caracter = s_caracteres.charAt(j);
	            if (s_cadena.charAt(i) == caracter)
	            {
	            	valido = false;
	                break;
	               }
	           }
	       if (valido)
	           nueva_cadena += s_cadena.charAt(i);
	      }
	  return nueva_cadena;
	}
        
    public static String EncodingHTML(String valorCampo){
        valorCampo = valorCampo.replaceAll("á","&aacute;");
        valorCampo = valorCampo.replaceAll("é","&eacute;");
        valorCampo = valorCampo.replaceAll("í","&iacute;");
        valorCampo = valorCampo.replaceAll("ó","&oacute;");
        valorCampo = valorCampo.replaceAll("ú","&uacute;");
        valorCampo = valorCampo.replaceAll("Á","&Aacute;");
        valorCampo = valorCampo.replaceAll("É","&Eacute;");
        valorCampo = valorCampo.replaceAll("Í","&Iacute;");
        valorCampo = valorCampo.replaceAll("Ó","&Oacute;");
        valorCampo = valorCampo.replaceAll("Ú","&Uacute;");

        valorCampo = valorCampo.replaceAll("¿","&iquest;");
        valorCampo = valorCampo.replaceAll("ñ","&ntilde;");
        valorCampo = valorCampo.replaceAll("Ñ","&Ntilde;");

        return valorCampo;
    }
}