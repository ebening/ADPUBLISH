/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author Desarrollador
 */
public class DragButton extends JButton{
   private int typeObj;     

    public int getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(int typeObj) {
        this.typeObj = typeObj;
    }
   public DragButton(int typeObj){
       super();
       this.typeObj=typeObj;
       
       this.setTransferHandler(new TransferHandler("text"));
       
       MouseListener ml = new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                 JComponent jc = (JComponent)e.getSource();
                 TransferHandler th = jc.getTransferHandler();
                 th.exportAsDrag(jc, e, TransferHandler.COPY);
                  
            }
       };
       this.addMouseListener(ml); 
   } 
}
