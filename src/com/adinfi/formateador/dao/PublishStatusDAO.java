package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.PublishStatusBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Josue Sanchez
 */
public class PublishStatusDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    // Devuelve el catalogo de status de publicación
    public List<PublishStatusBO> get() {
        int idDocument = 0;
        List<PublishStatusBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublishStatusBO>> h = new BeanListHandler<>(PublishStatusBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, idDocument};
            list = (List<PublishStatusBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    
    // devuelve el id de la publicacion.
    public List<PublishStatusBO> getStatusPublish(int idDocument) {
        List<PublishStatusBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublishStatusBO>> h = new BeanListHandler<>(PublishStatusBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, idDocument};
            list = (List<PublishStatusBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPpublishStatus (?, ?) }";
}
