/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.editor;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.html.WebColors;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 *
 * @author USUARIO
 */
public class UtileriasPDF {
    
    private static final Logger LOG = Logger.getLogger(UtileriasPDF.class.getName());
    public static final int WIDTH_IMAGE = 200;
    public static final int HEIGHT_IMAGE = 130;
    
    /* Estilos de titulo */
    public static final BaseColor bgColorTitulo = WebColors.getRGBColor("#FFFFFF");
    public static final BaseColor fgColorTitulo = WebColors.getRGBColor("#7F7F7F");
    public static final Font fontTitulo = FontFactory.getFont("Verdana", 8, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
    
    /* Estilo de subtitulo */
    public static final BaseColor bgColorSubtitulo = WebColors.getRGBColor("#FFFFFF");
    public static final BaseColor fgColorSubtitulo = WebColors.getRGBColor("#7F7F7F");
    public static final Font fontSubtitulo = FontFactory.getFont("Verdana", 8, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL);
        
    /* Estilos de descripción */
    public static final BaseColor bgColorDescripcion = WebColors.getRGBColor("#B2B2B2");
    public static final BaseColor fgColorDescripcion = WebColors.getRGBColor("#7F7F7F");
    public static final Font fontDescripcion = FontFactory.getFont("Verdana", 6, Font.ITALIC);//new Font(Font.FontFamily.HELVETICA, 6f, Font.ITALIC);
    
    /* Estilos de campos de texto */
    public static final Font fontTexto = FontFactory.getFont("Verdana", 8, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL);
    public static final Font fontTextoDisclosure = FontFactory.getFont("Verdana", 6, Font.NORMAL);
    public static final Font fontBullet = FontFactory.getFont("Verdana", 8, Font.ITALIC);//new Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC);
    public static final BaseColor fgColorTexto = BaseColor.BLACK;
    
    /* Estilos de Imagen */
    public static final BaseColor bgColorImagen = BaseColor.WHITE;//WebColors.getRGBColor("#FE6A00");
    
    public static final String TITULO_PDF = "Económico";
    public static final Font fontTituloPDF = FontFactory.getFont("Verdana", 22, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD);
    public static final Font fontSubTituloPDF = FontFactory.getFont("Verdana", 20, Font.NORMAL);
    public static final BaseColor fgColorTituloPDF = WebColors.getRGBColor("#FE6A00");
    
    public static final Font fontTemaPDF = FontFactory.getFont("Verdana", 11, Font.BOLD);//new Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD);
    public static final String TEMA_PDF = "%s";
    
    public static final Font fontContenidoPDF = new Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL);
    
    public static final String TITULO_DOCUMENTO = "Económico";
    
    /* Estilos de encabezado de modulos colaborativos */
    public static final BaseColor bgColorHeader = WebColors.getRGBColor("#FFFFFF");
    public static final BaseColor fgColorHeader = WebColors.getRGBColor("#000000");
    public static final Font fontHeader = FontFactory.getFont("Verdana", 14, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 14f);
    
    /* Estilos de sección de modulos colaborativos */
    public static final BaseColor bgColorSection = WebColors.getRGBColor("#F34700");
    public static final BaseColor fgColorSection = WebColors.getRGBColor("#FFFFFF");
    public static final Font fontSection = FontFactory.getFont("Verdana", 12, Font.NORMAL);//new Font(Font.FontFamily.HELVETICA, 12f);
    
    /* Parse HTML*/
    public static final String HTML_TAG_P =  "p";
    public static final String HTML_TAG_BODY =  "body";
    public static final String TEXT_ALIGN_RIGHT = "right";
    public static final String TEXT_ALIGN_CENTER = "center";
    
    public static List<ChunksAndProperties> parseToList(String html, String tag){
        List<ChunksAndProperties> retVal = new ArrayList<>();
        
        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(tag);
            
            for (Element element : elements) {
                ChunksAndProperties cap = new ChunksAndProperties();
                com.itextpdf.text.Chunk chuck = new Chunk(element.text());
                String align = element.attr("align").isEmpty() || "left".equals(element.attr("align")) ? "justify" : element.attr("align");
                
                cap.setChunk(chuck);
                cap.setAlign(align);
                
                retVal.add(cap);
            }
            
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
        
        return retVal;
    }
    
    public static final int MASK_NADA      = 0b00000;
    public static final int MASK_BOLD      = 0b00001;
    public static final int MASK_ITALIC    = 0b00010;
    public static final int MASK_UNDERLINE = 0b00100;
    public static final int MASK_STRIKE    = 0b01000;
    
    public static List<String> parseToList_P_NoFormat(String html) {
        List<String> rVal = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(HTML_TAG_P);
            if(elements == null || elements.size() <= 0 )
                elements = doc.select(HTML_TAG_BODY);
            
            for (Element element : elements) {
                rVal.add(element.text());
            }

        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        }

        return rVal;
    }
    
    public static List<List<ChunksAndProperties>> parseToList_P(String html) {
        List<List<ChunksAndProperties>> rVal = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(HTML_TAG_P);
            if(elements == null || elements.size() <= 0 )
                elements = doc.select(HTML_TAG_BODY);
            
            for (Element element : elements) {
                rVal.add(chunksParaPedazo(element, MASK_NADA));
            }

        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        }

        return rVal;
    }

    public static List<ChunksAndProperties> chunksParaPedazo(Node elemento, int estado) {
        List<ChunksAndProperties> chunks = new ArrayList<>();
        String align = elemento.attr("align").isEmpty() || "left".equals(elemento.attr("align")) ? "justify" : elemento.attr("align");

        List<Node> nodos = elemento.childNodes();
        for (Node nodo : nodos) {
            if (nodo instanceof TextNode) {
                TextNode nodoTexto = (TextNode) nodo;
                Chunk chunk = new Chunk(nodoTexto.text().trim());
                ChunksAndProperties cap = new ChunksAndProperties();
                cap.setChunk(chunk);
                cap.setAlign(align);
                alterarChunk(cap, estado);
                chunks.add(cap);
            } else if (nodo instanceof DataNode) {
                DataNode nodoDatos = (DataNode) nodo;
                String texto = nodoDatos.getWholeData();
                Chunk chunk = new Chunk(texto.trim());
                ChunksAndProperties cap = new ChunksAndProperties();
                cap.setChunk(chunk);
                cap.setAlign(align);
                alterarChunk(cap, estado);
                chunks.add(cap);
            } else if (nodo instanceof Element) {
                Element subelemento = (Element) nodo;
                String tagName = subelemento.tagName().toLowerCase();
                int estadoNuevo = estado;
                switch (tagName) {
                    case "b":
                        estadoNuevo |= MASK_BOLD;
                        break;
                    case "i":
                        estadoNuevo |= MASK_ITALIC;
                        break;
                    case "u":
                        estadoNuevo |= MASK_UNDERLINE;
                        break;
                    case "strike":
                        estadoNuevo |= MASK_STRIKE;
                        break;
                }
                chunks.addAll(chunksParaPedazo(subelemento, estadoNuevo));
            }
        }

        return chunks;
    }
    
    public static void alterarChunk(ChunksAndProperties cap, int estado) {
        cap.setBold((estado & MASK_BOLD) != 0);
        cap.setItalic((estado & MASK_ITALIC) != 0);
        cap.setUnderline((estado & MASK_UNDERLINE) != 0);
        cap.setStrike((estado & MASK_STRIKE) != 0);
    }

}
