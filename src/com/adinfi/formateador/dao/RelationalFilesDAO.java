package com.adinfi.formateador.dao;
import com.adinfi.formateador.bos.RelationalFilesBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class RelationalFilesDAO{

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<RelationalFilesBO> get(RelationalFilesBO bo) {
        List<RelationalFilesBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<RelationalFilesBO>> h = new BeanListHandler<>(RelationalFilesBO.class);
            Object[] obj = new Object[]{
                bo.getIdMarket(),
                bo.getIdSubject(),
                bo.getIdLanguage(),
                bo.getIdDocType(),
                bo.getIdIndustry(),
                bo.getAuthor(),
                bo.getDocPublicationDateFrom(),
                bo.getDocPublicationDateTo()};
            list = (List<RelationalFilesBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPRELATEDDOC (?, ?, ?, ?, ?, ?, ?, ?) }";

}