package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentTypeProfileBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class DocumentTypeProfileDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<DocumentTypeProfileBO> get(int iddocumentType) {
        List<DocumentTypeProfileBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeProfileBO>> h = new BeanListHandler<>(DocumentTypeProfileBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, iddocumentType};
            list = (List<DocumentTypeProfileBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

 
    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPPROFILEDOCUMENTTYPE (?,?) }";

  

}
