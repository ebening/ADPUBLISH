package com.adinfi.formateador.view;

import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.view.dnd.ScrCell;
import com.adinfi.formateador.view.dnd.ScrObjIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Miguel Ramirez
 */
public class TransferibleItem implements Transferable {

    private ScrObjIcon item;

    public TransferibleItem() {
        this.item = null;
    }

    public TransferibleItem(ScrObjIcon item) {
        this.item = item;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : Entro a getTransferDataFlavors() ");
        return new DataFlavor[]{ScrCell.DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor df) {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : Entro a isDataFlavorSupported() ");
        return df.equals(ScrCell.DATA_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : 1.Entro a getTransferData()");
        Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : 2.Entro a getTransferData()" + " : " + df.getDefaultRepresentationClassAsString());
        if (isDataFlavorSupported(ScrCell.DATA_FLAVOR)) {
           Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : 3.Entro a getTransferData()" + " : " + item.getName());
            return item;
        } else {
            Utilerias.logger(getClass()).info(getClass().getSimpleName() + " : 4.Error" + " : " + df);
            throw new UnsupportedFlavorException(df);
        }
    }

}
