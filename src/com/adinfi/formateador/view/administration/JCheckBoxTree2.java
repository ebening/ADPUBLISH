/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view.administration;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * 
 * @author Matz
 */
public class JCheckBoxTree2 extends JTree
{
        private static final long serialVersionUID = 4741574290658997771L;

        private ArrayList<CheckListener> checkListeners = new ArrayList<CheckListener>(1);

        private boolean autoCheckChildren = false;

        // initialize
        {
                this.setEditable(true);
                this.setCellRenderer(new CheckBoxTreeCellRenderer());
                this.setCellEditor(new CheckBoxTreeCellEditor(this));
        }

        public JCheckBoxTree2()
        {
                super();
        }

        public JCheckBoxTree2(TreeNode root)
        {
                super(root);
        }

        public JCheckBoxTree2(TreeModel newModel)
        {
                super(newModel);
        }

        public JCheckBoxTree2(TreeNode root, boolean asksAllowsChildren)
        {
                super(root, asksAllowsChildren);
        }

        public JCheckBoxTree2(Object[] value)
        {
                super(value);
        }

        public void notifyCheckListeners(CheckBoxNode node)
        {
                for (CheckListener l : checkListeners)
                {
                        l.boxChecked(node);
                }
        }

        public void addCheckListener(CheckListener listener)
        {
                checkListeners.add(listener);
        }

        public boolean isAutoCheckingChildren()
        {
                return this.autoCheckChildren;
        }

        public void setAutoCheckChildren(boolean b)
        {
                this.autoCheckChildren = b;
        }
}

class CheckBoxTreeCellRenderer extends DefaultTreeCellRenderer
{
        private static final long serialVersionUID = 6810909571777178826L;

        private JCheckBox checkbox = new JCheckBox();
        private JLabel label = new JLabel();

        public CheckBoxTreeCellRenderer()
        {
                label.setFont(new Font("Arial", Font.BOLD, 12));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
                        boolean hasFocus)
        {
                if (value instanceof CheckBoxNode)
                {
                        CheckBoxNode node = (CheckBoxNode) value;
                        checkbox.setText(node.toString());
                        checkbox.setSelected(node.isChecked());
                        if (selected)
                                checkbox.setForeground(Color.WHITE);
                        else
                                checkbox.setForeground(Color.BLACK);
                }
                else if (value instanceof DefaultMutableTreeNode)
                {
                        label.setText(((DefaultMutableTreeNode) value).toString());
                        if (selected)
                                label.setForeground(Color.WHITE);
                        else
                                label.setForeground(Color.BLACK);
                        return label;
                }
                return checkbox;
        }

        public JCheckBox getCheckbox()
        {
                return checkbox;
        }
}

class CheckBoxTreeCellEditor extends DefaultCellEditor implements CellEditorListener
{
        private static final long serialVersionUID = 8558677410319176218L;

        private DefaultMutableTreeNode node;

        private JCheckBoxTree2 tree;

        public CheckBoxTreeCellEditor(JCheckBoxTree2 tree)
        {
                super(new JCheckBox());
                this.tree = tree;
                addCellEditorListener(this);
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
        {
                Component editor = super.getComponent();
                if (value instanceof CheckBoxNode)
                {
                        node = (CheckBoxNode) value;
                        ((JCheckBox) editor).setSelected(((CheckBoxNode) node).isChecked());
                        ((JCheckBox) editor).setText(node.toString());
                }

                return editor;
        }

        @Override
        public Object getCellEditorValue()
        {
                JCheckBox editor = (JCheckBox) (super.getComponent());
                if (node instanceof CheckBoxNode)
                        ((CheckBoxNode) node).setChecked(editor.isSelected());
                return node;
        }

        public DefaultMutableTreeNode getNode()
        {
                return node;
        }

        public void setNode(CheckBoxNode node)
        {
                this.node = node;
        }

        public void editingStopped(ChangeEvent e)
        {
                DefaultMutableTreeNode parent = ((CheckBoxTreeCellEditor) e.getSource()).getNode();
                if (parent instanceof CheckBoxNode)
                {
                        if (tree.isAutoCheckingChildren())
                                checkAllNodes((CheckBoxNode) parent);
                        tree.notifyCheckListeners((CheckBoxNode) parent);
                        // if (parent.getParent() != null && parent.isSelected())
                        // ((CheckBoxNode)parent.getParent()).isSelected(true);
                        tree.updateUI();
                }
        }

        private void checkAllNodes(CheckBoxNode root)
        {
                @SuppressWarnings("unchecked")
                Enumeration<CheckBoxNode> children = root.children();
                while (children.hasMoreElements())
                {
                        CheckBoxNode child = children.nextElement();
                        child.setChecked(root.isChecked());
                        checkAllNodes(child);
                }
        }

        public void editingCanceled(ChangeEvent e)
        {
                
        }
}