package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentSearchBO;
import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class QuickViewDAO {

    public enum SEARCH_TYPE {

        FAVORITOS,
        RECIENTES,
        NO_PUBLICADOS
    }

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<DocumentSearchBO> get(SEARCH_TYPE type) {
        List<DocumentSearchBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            int idSearch = 0;
            switch (type) {
                case FAVORITOS:
                    idSearch = 1;
                    break;
                case RECIENTES:
                    idSearch = 2;
                    break;
                case NO_PUBLICADOS:
                    idSearch = 3;
                    break;
            }
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentSearchBO>> h = new BeanListHandler<>(DocumentSearchBO.class);
            Object[] obj = new Object[]{idSearch, InstanceContext.getInstance().getUsuario().getUsuarioId()};
            list = (List<DocumentSearchBO>) run.query(conn, SQL_QUERY, h, obj);
            if (list != null) {
                int perfilID = InstanceContext.getInstance().getUsuario().getPerfilId();
                
                
                DocumentTypeProfileDAO dao = new DocumentTypeProfileDAO();
                List<DocumentTypeProfileBO> lstPerfiles = dao.get(0);
                
                for (DocumentSearchBO lst : list){
                    for (DocumentTypeProfileBO l : lstPerfiles) {
                        if (l.getDocument_id() == lst.getIdDocType() && l.getIdprofile() == perfilID) {
                            lst.setEnableForDocumentTypeProfile(1);
                            break;
                        }
                    }

                }

            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPQUICKVIEW (?, ?) }";

}
