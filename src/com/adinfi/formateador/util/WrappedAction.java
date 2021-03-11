/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author jdominguez
 */
abstract class WrappedAction implements Action{
    private Action originalAction;
    private JComponent component;
    private Object actionKey;

    /*
     *  Replace the default Action for the given KeyStroke with a custom Action
     */
    public WrappedAction(JComponent component, KeyStroke keyStroke)
    {
            this.component = component;
            actionKey = getKeyForActionMap(component, keyStroke);

            if (actionKey == null)
            {
                    String message = "no input mapping for KeyStroke: " + keyStroke;
                    throw new IllegalArgumentException(message);
            }

            setActionForKey( actionKey );
    }

    /*
     *  Replace the default Action with a custom Action
     */
    public WrappedAction(JComponent component, Object actionKey)
    {
            this.component = component;
            setActionForKey( actionKey );
    }

    /*
     *  Search the 3 InputMaps to find the KeyStroke binding
     */
    private Object getKeyForActionMap(JComponent component, KeyStroke keyStroke)
    {
            for (int i = 0; i < 3; i++)
            {
            InputMap inputMap = component.getInputMap(i);

                if (inputMap != null)
                {
                            Object key = inputMap.get(keyStroke);

                            if (key != null)
                                    return key;
                    }
        }

            return null;
    }

    /*
     *  Replace the existing Action for the given action key with a
     *  wrapped custom Action
     */
    private void setActionForKey(Object actionKey)
    {
            //  Save the original Action

            this.actionKey = actionKey;
            originalAction = component.getActionMap().get(actionKey);

            if (originalAction == null)
            {
                    String message = "no Action for action key: " + actionKey;
                    throw new IllegalArgumentException(message);
            }

            //  Replace the existing Action with this class

            install();
    }

    /*
     *  Child classes should use this method to invoke the original Action
     */
    public void invokeOriginalAction(ActionEvent e)
    {
            originalAction.actionPerformed(e);
    }

    /*
     *  Install this class as the default Action
     */
    public void install()
    {
            component.getActionMap().put(actionKey, this);
    }

    /*
     *	Restore the original Action as the default Action
     */
    public void unInstall()
    {
            component.getActionMap().put(actionKey, originalAction);
    }
//
//  Delegate the Action interface methods to the original Action
//
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
            originalAction.addPropertyChangeListener(listener);
    }

    @Override
    public Object getValue(String key)
    {
            return originalAction.getValue(key);
    }

    @Override
    public boolean isEnabled()
    {
            return originalAction.isEnabled();
    }

    @Override
    public void putValue(String key, Object newValue)
    {
            originalAction.putValue(key, newValue);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
            originalAction.removePropertyChangeListener(listener);
    }

    @Override
    public void setEnabled(boolean newValue)
    {
            originalAction.setEnabled(newValue);
    }
//
//  Implement some AbstractAction methods
//
    public Object[] getKeys()
    {
            if (originalAction instanceof AbstractAction)
            {
                    AbstractAction abstractAction = (AbstractAction)originalAction;
                    return abstractAction.getKeys();
            }

            return null;
    }

    public PropertyChangeListener[] getPropertyChangeListeners()
    {
            if (originalAction instanceof AbstractAction)
            {
                    AbstractAction abstractAction = (AbstractAction)originalAction;
                    return abstractAction.getPropertyChangeListeners();
            }

            return null;
    }
}
