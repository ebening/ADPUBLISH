package com.adinfi.formateador.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalToolTipUI;

/**
 *
 * @author Miguel Ramirez
 */
public class ImageToolTipUI extends MetalToolTipUI {

    public BufferedImage img;
    public int height = 200;
    public int width = 200;

    public ImageToolTipUI(BufferedImage image) {
        this.img = image;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        paintComponent2(g);

    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(width, height);
    }

    public void paintComponent2(Graphics g) {
        if(img != null){
        Graphics2D g2d = (Graphics2D) g;
        Image imagen;
        int[] sizes = identificarSizeImagenes(img.getHeight(), img.getWidth());
        int img_height = sizes[0];
        int img_width = sizes[1];

        imagen = img.getScaledInstance(img_width, img_height, Image.SCALE_AREA_AVERAGING);

        g2d.translate(this.width / 2, this.height / 2);
        g2d.translate(-imagen.getWidth(null) / 2, -imagen.getHeight(null) / 2);
        g2d.drawImage(imagen, 0, 0, null);
        }
        
        
    }

    private int heightDeseado(int heightOriginal, int widthOriginal, int widthDeseado) {
        return (widthDeseado * heightOriginal) / widthOriginal;
    }

    private int widthDeseado(int heightOriginal, int widthOriginal, int heightDeseado) {
        return (heightDeseado * widthOriginal) / heightOriginal;
    }

    public int[] identificarSizeImagenes(int height, int width) {
        int[] sizes = new int[2];
        /*if (this.width < width && this.height < height) {
            if (width <= height) {
                sizes[0] = this.height;
                sizes[1] = widthDeseado(height, width, this.height);
            } else if (width > height) {
                sizes[0] = heightDeseado(height, width, this.width);
                sizes[1] = this.width;
            }
        } else if (this.width >= width && this.height >= height) {
            sizes[0] = heightDeseado(height, width, this.width - 20);
            sizes[1] = widthDeseado(height, width, this.height - 20);
        } else {
            sizes[0] = height;
            sizes[1] = width;
        }*/
        
        if(width > (this.width - 5)){
            sizes[1] = (this.width - 5);
        }else{
            sizes[1] = width;
        }
        
        if(height > this.height){
            sizes[0] = this.height;
        }else{
            sizes[0] = height;
        }
        
        return sizes;
    }

}
