package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.DocumentTypeProfileDAO;
import com.adinfi.formateador.util.InstanceContext;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Guillermo Trejo
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        int rowSel = table.convertRowIndexToModel(row);
        SearchTableModel model = (SearchTableModel) table.getModel();
        DocumentSearchBO docBO = model.getList().get(rowSel);
        int idDocument = docBO.getDocumentId();
        int isAllowedProfile = docBO.getEnableForDocumentTypeProfile();
        
        
        

        if (hasFocus) {
            setBackground(UIManager.getDefaults().getColor("Table.focusCellBackground"));
        } else if (isSelected) {
            setBackground(UIManager.getDefaults().getColor("Table.selectionBackground"));
        } else {
            setBackground(UIManager.getDefaults().getColor("Table.background"));
        }

        if ((idDocument < 0) && (!value.equals("Editar Publicación"))) {
            if(docBO.isHistory() && value.equals("PDF")){
                setEnabled(docBO.isHistory());
            }else{
                setEnabled(false);
            }
            setText((value == null) ? "" : value.toString());
            return this;
        } else {

            switch (value.toString()) {
                case "PDF":
                    setEnabled(InstanceContext.getInstance().isActivePDF());
                    setEnabled(docBO.getEnableForDocumentTypeProfile() == 1);
                    break;
                case "HTML":
                    setEnabled(InstanceContext.getInstance().isActiveHTML());
                    setEnabled(docBO.getEnableForDocumentTypeProfile() == 1);
                    break;
                case "Editar documento":
                    setEnabled(InstanceContext.getInstance().isActiveEditDocument());
                    // si no esta autorizado el perfil del usuario con el tipo de documento se bloquea .
                    if(isAllowedProfile == 0){setEnabled(false);}
                    setEnabled(docBO.getEnableForDocumentTypeProfile() == 1);
                    break;
                case "Editar Publicación":
                    setEnabled(InstanceContext.getInstance().isActivePublish());
                    // si no esta autorizado el perfil del usuario con el tipo de documento se bloquea .
                    if(isAllowedProfile == 0){setEnabled(false);}
                    setEnabled(docBO.getEnableForDocumentTypeProfile() == 1);
                    if (docBO.isHistory()){setEnabled(docBO.isHistory());}
                    break;

                default:
                    setEnabled(true);

            }
        }

        setText((value == null) ? "" : value.toString());
        return this;

    }
    
}
