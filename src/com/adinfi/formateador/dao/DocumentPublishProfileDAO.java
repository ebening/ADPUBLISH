package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentPublishProfileBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class DocumentPublishProfileDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<DocumentPublishProfileBO> get(int idDocumentType) {
        List<DocumentPublishProfileBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentPublishProfileBO>> h = new BeanListHandler<>(DocumentPublishProfileBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, idDocumentType, null};
            list = (List<DocumentPublishProfileBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insert(DocumentPublishProfileBO bo) {
        List<DocumentPublishProfileBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentPublishProfileBO>> h = new BeanListHandler<>(DocumentPublishProfileBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIddocument_publish_profile(), bo.getIddocument_type(), bo.getIdpublishprofile()};
            list = (List<DocumentPublishProfileBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIddocument_publish_profile();
    }

    public void delete(int Iddocument_type) {
        List<DocumentPublishProfileBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentPublishProfileBO>> h = new BeanListHandler<>(DocumentPublishProfileBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, null, Iddocument_type, null};
            list = (List<DocumentPublishProfileBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_PUBLISH_PROFILE (?, ?, ?, ?) }";

}


