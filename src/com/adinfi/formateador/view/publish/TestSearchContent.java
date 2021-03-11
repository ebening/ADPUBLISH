

package com.adinfi.formateador.view.publish;

import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.ws.searchcm.Result;

/**
 *
 * @author vectoran
 */
public class TestSearchContent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Result res = UtileriasWS.documentSearch(
                null,  // String idDocumento
                null,  // String idMercado
                null,  // String Decha inicial
                null,  // String fecha final
                null,  // String tema 
                null,  // String sector
                0,  // integer idSector
                null,  // String indioma
                null,  // String estado
                null,  // String Autor
                null,  // String Palabra clave
                null,  // String OrdenColumna
                0,  // Iteger tipoOrden
                0,      //iPag
                2000 );    // maxCant
        
        int f = 3;
        
        
        
    }
    
}
