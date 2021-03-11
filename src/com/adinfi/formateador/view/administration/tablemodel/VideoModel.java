/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.util.Utilerias;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vectoran
 */
public class VideoModel extends AbstractTableModel {
    private String thumbnail;
    private String fecha;
    private String titulo;
    private String autor;
    private String categoria;
    private String tipo;
    private String kalturaId;
    
    public VideoModel(){}

    /**
     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /**
     * @return the kalturaId
     */
    public String getKalturaId() {
        return kalturaId;
    }

    /**
     * @param kalturaId the kalturaId to set
     */
    public void setKalturaId(String kalturaId) {
        this.kalturaId = kalturaId;
    }
    
    @Override
    public String toString(){
        return this.titulo;
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        try {
            switch (columnIndex) {
                case 0:
                    String thumbnail = getThumbnail();
                    if (!thumbnail.isEmpty()) {
                        value = loadThumbnail(thumbnail);
                    }
                    break;
                case 1:
                    value = getFecha();
                    break;
                case 2:
                    value = getTitulo();
                    break;
                case 3:
                    value = getAutor();
                    break;
                case 4:
                    value = getCategoria();
                    break;
                case 5:
                    value = getTipo();
                    break;
            }
        } catch (Exception e) {
            value = null;
        }
        return value;
    }
    
    private ImageIcon loadThumbnail(String urlHttp) {
        ImageIcon icon = null;
        BufferedImage resizedImage = null;
        try {
            URL url = new URL(urlHttp);
            BufferedImage img = ImageIO.read(url);
            resizedImage = resize(img, 50, 50);
            icon = new ImageIcon(resizedImage);

        } catch (IOException e) {
            Utilerias.logger(getClass()).info(e);
        }

        return icon;
    }
    
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return bi;
    }
}
