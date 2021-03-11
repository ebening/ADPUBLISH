package com.adinfi.formateador.view;

import java.awt.image.BufferedImage;
import javax.swing.JToolTip;

public class ImageToolTip extends JToolTip {
    
    public ImageToolTip(BufferedImage image) {
        setUI(new ImageToolTipUI(image));
    }
}
