package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.PublishProfileBO;
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
 * @author USUARIO
 */
public class PublishProfileDAO {
    
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    
      public List<PublishProfileBO> get() {
        List<PublishProfileBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublishProfileBO>> h = new BeanListHandler<>(PublishProfileBO.class);
            list = (List<PublishProfileBO>) run.query(conn, SQL_QUERY, h);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }
      
     private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPPUBLISHPROFILE () }";
}
