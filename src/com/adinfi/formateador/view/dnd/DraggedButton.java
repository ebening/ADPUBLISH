package com.adinfi.formateador.view.dnd;

import com.adinfi.formateador.util.Utilerias;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JButton;

/**
 *
 * @author Guillermo Trejo
 */
public class DraggedButton {//extends JButton implements Transferable {

//    private static int counter = 0;
//    
//    public DraggedButton() {
//        // Add the listener which will export this panel for dragging
//        this.addMouseListener(new DraggableMouseListener());
//
//        // Add the handler, which negotiates between drop target and this 
//        // draggable panel
//        this.setTransferHandler(new DragAndDropTransferHandler());
//
//        // Create a label with this panel's number
//        counter++;
//        
//        setText("EJEMPLO DE BOTON " + counter);
//
//        // This won't take the entire width for easy drag and drop
//        
//        this.setPreferredSize(new Dimension(100, 100));
//        this.setMinimumSize(new Dimension(100, 100));
//    }
//    
//
//    /**
//     * <p>
//     * One of three methods defined by the Transferable interface.</p>
//     * <p>
//     * If multiple DataFlavor's are supported, can choose what Object to
//     * return.</p>
//     * <p>
//     * In this case, we only support one: the actual JPanel.</p>
//     * <p>
//     * Note we could easily support more than one. For example, if supports text
//     * and drops to a JTextField, could return the label's text or any arbitrary
//     * text.</p>
//     *
//     * @param flavor
//     * @return
//     */
//    @Override
//    public Object getTransferData(DataFlavor flavor) {
//
//        Utilerias.logger(getClass()).info("Step 7 of 7: Returning the data from the Transferable object. In this case, the actual panel is now transfered!");
//
//        DataFlavor thisFlavor = null;
//
//        try {
//            thisFlavor = ScrCell.getDragAndDropPanelDataFlavor();
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info("Problem lazy loading: " + ex.getMessage());
//            return null;
//        }
//
//        // For now, assume wants this class... see loadDnD
//        if (thisFlavor != null && flavor.equals(thisFlavor)) {
//            return DraggedButton.this;
//        }
//
//        return null;
//    }
//
//    /**
//     * <p>
//     * One of three methods defined by the Transferable interface.</p>
//     * <p>
//     * Returns supported DataFlavor. Again, we're only supporting this actual
//     * Object within the JVM.</p>
//     * <p>
//     * For more information, see the JavaDoc for DataFlavor.</p>
//     *
//     * @return
//     */
//    @Override
//    public DataFlavor[] getTransferDataFlavors() {
//
//        DataFlavor[] flavors = {null};
//
//        Utilerias.logger(getClass()).info("Step 4 of 7: Querying for acceptable DataFlavors to determine what is available. Our example only supports our custom RandomDragAndDropPanel DataFlavor.");
//
//        try {
//            flavors[0] = ScrCell.getDragAndDropPanelDataFlavor();
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info("Problem lazy loading: " + ex.getMessage());
//            return null;
//        }
//
//        return flavors;
//    }
//
//    /**
//     * <p>
//     * One of three methods defined by the Transferable interface.</p>
//     * <p>
//     * Determines whether this object supports the DataFlavor. In this case,
//     * only one is supported: for this object itself.</p>
//     *
//     * @param flavor
//     * @return True if DataFlavor is supported, otherwise false.
//     */
//    @Override
//    public boolean isDataFlavorSupported(DataFlavor flavor) {
//
//        Utilerias.logger(getClass()).info("Step 6 of 7: Verifying that DataFlavor is supported.  Our example only supports our custom RandomDragAndDropPanel DataFlavor.");
//
//        DataFlavor[] flavors = {null};
//        try {
//            flavors[0] = ScrCell.getDragAndDropPanelDataFlavor();
//        } catch (Exception ex) {
//            Utilerias.logger(getClass()).info("Problem lazy loading: " + ex.getMessage());
//            return false;
//        }
//
//        for (DataFlavor f : flavors) {
//            if (f.equals(flavor)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
