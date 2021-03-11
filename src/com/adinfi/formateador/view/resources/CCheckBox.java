package com.adinfi.formateador.view.resources;

import javax.swing.JCheckBox;

/**
 *
 * @author vectoran
 */
public class CCheckBox extends JCheckBox {
    
    private Object obj;
    
    public CCheckBox (String text, Object obj) {
        super(text);
        this.obj = obj;
    }
    
    public Object getObject(){
        return this.obj;
    }
}
