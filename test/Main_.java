
import com.darkprograms.speech.synthesiser.Synthesiser;
import com.gtranslate.Audio;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main_ {

    public static void main(String[] args) throws Exception {
        //new Main_().call("Hola mundo, como estas");
        /*
        String s = "La tecnología nunca dejará de sorprendernos, y a no ser que seas un nerd de la informática y la electrónica, hay muchas cosas que de seguro no sabes cómo funcionan. Pero por supuesto es posible hablar sobre tecnología sin usar un lenguaje complicado y sin recurrir a términos que parecen tomados de un diccionario alienígena. Veamos a continuación algunos hechos sorprendentes del mundo de la tecnología:"
                + "\n"
                + "1) La electricidad inalámbrica existe. La firma eCoudpled ha encontrado una forma segura de utilizarla gracias a los principios de la inducción electromagnética. Se espera que pueda llegar al mercado dentro de algunos años."
                + "\n"
                + "2) La primer computadora “portátil” pesaba nada menos que 11,8 kilogramos y media 52 centímetros de ancho. Una moderna como la Toshiba Kira pesa solo 1,3 kilogramos y mide 33 centímetros de ancho."
                + "\n"
                + "3) Hay más poder de procesamiento en una calculadora moderna que en la computadora que utilizaba el Apolo 11 en su viaje a la luna."
                + "\n"
                + "4) La primer computadora de la historia pesaba 30 toneladas y media el doble de alto que una persona promedio.";
                */
        
        String s = "De acuerdo con información proporcionada por Banxico, al 27 de enero,las Siefores y Sociedades de Inversión (SSI) mantienen el 54% del total emitido en udibonos y son el principal tenedor de estos instrumentos.En orden de importancia, le siguen otros residentes en el país con 20%; aseguradoras, 15%; extranjeros, 9%; banca, 1% y garantías 1%.\n" +
"\n" +
"Conforme a los datos desglosados por vencimiento publicados por Banxico, se observa que la tenencia de las SSI se situó al 23 de enero en 95.5 miles de millones de udis (mmu); y del 17 al 23 de enero, las SSI aumentaron su tenencia en 0.14 mmu. Durante la semana de análisis, las SSI reasignaron su cartera al salir de los tramos medios (5 a 10 años por 0.09mmu) y largos (10 a 30 años por 0.23mmu) y entrar en la parte corta de la curva (hasta 5 años por 0.4mmu). Los principales movimientos que realizaron estos inversionistas fueron: salida de flujos del S17 por 0.4mmu y entrada por 0.7mmu en el S16. En lo que va del año, las SSI han invertido 2.0mmu en Udibonos: 0.5, 0.7, y 0.8mmu en la parte delantera, media y trasera de la curva, en el mismo orden. De esta forma, en lo qu va del año, las SSI han invertido más en el S22 con entradas por 1.1mmu, mientras que han desinvertido del S19 por 0.5mmu.\n" +
"\n" +
"Por otro lado, en la semana de referencia, la entrada de flujos foráneos al mercado de Udibonos duró poco tiempo, puesto que en la semana de referencia se registró una salida por 0.05mmu (recordemos que tras seis semanas de salida de flujos, la semana previa a la de referencia, se registró entrada por 0.5mmu). Lo anterior fue resultado del sesgo d venta que mantuvieron los extranjeros en los instrumentos con duraciones cortas y medias con salidas por 0.16 y 0.01mmu), mismas que contrarrestaron la mayor demanda por papeles con vencimientos largos (entrada por 0.12mmu). Los principales movimientos que realizaron estos inversionistas fueron los siguientes: salida de flujos del S14 por 0.2mmu y entrada en el S35 por 0.1mmu. En lo que va del año, los flujos de extranjeros continúan en terreno negativo al ubicarse en -1.8mmu, de los cuales la principal salida se ha dado en los tramos cortos de la curva por 1.7mmu, y el restante en la parte trasera de la curva.";
        
        
        Audio audio = Audio.getInstance();
//        InputStream sound  = audio.getAudio("Hola mundo", Language.SPANISH);

        Synthesiser synthesiser = new Synthesiser(Synthesiser.LANG_ES_SPANISH);
        InputStream sound = synthesiser.getMP3Data(s);

        //audio.play(sound);

       

        byte[] byteArray = inputStreamToByteArray(sound);
        byteArrayToFile(byteArray, "C:\\Users\\USUARIO\\Desktop\\NAAGDFVB343_01.mp3");
    }

//    public static byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
//        byte[] result;
//        try (InputStreamReader in = new InputStreamReader(inStream)) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int next = inStream.read();
//            while (next > -1) {
//                baos.write(next);
//                next = in.read();
//            }   result = baos.toByteArray();
//        baos.flush();
//        }
//        return result;
//    }
    public static byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inStream.read(buffer)) > 0) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static void byteArrayToFile(byte[] byteArray, String outFilePath) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(outFilePath)) {
            fos.write(byteArray);
        }
    }

}
