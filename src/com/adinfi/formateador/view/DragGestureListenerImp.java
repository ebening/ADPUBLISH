package com.adinfi.formateador.view;

import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.dnd.ScrObjIcon;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Miguel Ramirez
 */
public class DragGestureListenerImp implements DragGestureListener, DropTargetListener {

    private JPanel panel;
    private DropTarget dropTarget;
    private ScrObjIcon drop;
    private ScrObjIcon drag;

    private Runnable callback;

    public DragGestureListenerImp(JPanel panel, Runnable callback) {
        this.panel = panel;
        this.callback = callback;
        dropTarget = new DropTarget(
                this.panel, DnDConstants.ACTION_MOVE, this, true);
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        try {
            Cursor cursor = null;
            Point point = event.getDragOrigin();
            //ScrObjIcon item = null;
            Component component = event.getComponent().getComponentAt(point);

            if (event.getDragAction() == DnDConstants.ACTION_MOVE) {
                cursor = DragSource.DefaultMoveDrop;
                if (component instanceof ScrObjIcon) {
                    drag = (ScrObjIcon) component;
                    event.startDrag(cursor, new TransferibleItem(drag));
                } else {
                    Utilerias.logger(getClass()).info("Esto es un panel no se puede arrastrar nada");
                }
            }
        } catch (InvalidDnDOperationException e) {
            System.err.println(e.getMessage() + e.getLocalizedMessage());
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragEnter");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        //Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragOver");
        //Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragOver : " + dtde.getLocation());
        Component component = dtde.getDropTargetContext().getComponent().getComponentAt(dtde.getLocation());
        if (component instanceof JPanel) {
            Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragOver : panel");
        }
        if (component instanceof ScrObjIcon) {
            drop = null;
            drop = (ScrObjIcon) component;
            Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragOver : " + drop.getName());
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dropActionChanged");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : dragExit");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Utilerias.logger(getClass()).info("1." + getClass().getSimpleName() + " : drop");
        Utilerias.logger(getClass()).info("1." + getClass().getSimpleName() + " : Elemento drop :" + drop.getName());
        Utilerias.logger(getClass()).info("1." + getClass().getSimpleName() + " : Elemento drag :" + drag.getName());
        reordenar();
        dtde.dropComplete(true);
        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
        if (callback != null) {
            callback.run();
        }
    }

    private void reordenar() {

        Integer c_aux;
        Integer c_aux2;
        Integer position1 = drag.getPosition();
        Integer position2 = drop.getPosition();

        c_aux = position1;
        c_aux2 = position2;
        position2 = c_aux;
        position1 = c_aux2;

        drag.setPosition(position1);
        drop.setPosition(position2);
        panel.add(drag);

        List<Component> allComponent = Arrays.asList(panel.getComponents());

        Collections.sort(allComponent, new Comparator() {
            @Override
            public int compare(Object t, Object t1) {
                ScrObjIcon item1 = (ScrObjIcon) t;
                ScrObjIcon item2 = (ScrObjIcon) t1;
                return item1.getPosition().compareTo(item2.getPosition());
            }

        });

        for (Component c : allComponent) {
            panel.add(c);
        }
        panel.repaint();
        panel.revalidate();
        panel.updateUI();

    }

}
