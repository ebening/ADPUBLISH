/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view.administration;


import com.adinfi.formateador.view.administration.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Matz
 */
public class CheckBoxNode extends DefaultMutableTreeNode
{
        private static final long serialVersionUID = 10L;

        private boolean isChecked;

    private Object storedObject;

    private String text;

    public CheckBoxNode()
    {
    }

    public CheckBoxNode(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public CheckBoxNode(boolean isChecked, String text)
    {
        this.isChecked = isChecked;
        this.text = text;
    }

    public CheckBoxNode(boolean isChecked, String text, Object userObject)
    {
        this.storedObject = userObject;
        this.isChecked = isChecked;
        this.text = text;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean check)
    {
        this.isChecked = check;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Object getStoredObject()
    {
        return storedObject;
    }

    public void setStoredObject(Object object)
    {
        this.storedObject = object;
    }
    
    @Override
    public String toString()
    {
        return text == null ? "" : text;
    }
}