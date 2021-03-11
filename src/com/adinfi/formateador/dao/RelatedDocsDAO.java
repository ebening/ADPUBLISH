package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.RelatedDocsBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class RelatedDocsDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<RelatedDocsBO> get(int idDoc) {
        List<RelatedDocsBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<RelatedDocsBO>> h = new BeanListHandler<>(RelatedDocsBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, 0, idDoc, 0};
            list = (List<RelatedDocsBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(RelatedDocsBO bo) {
        List<RelatedDocsBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<RelatedDocsBO>> h = new BeanListHandler<>(RelatedDocsBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdRelatedDoc(), bo.getDocument_id(), bo.getRelated_document_id()};
            list = (List<RelatedDocsBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdRelatedDoc();
    }

    public boolean delete(int idDocument) {
        boolean b = true;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<RelatedDocsBO>> h = new BeanListHandler<>(RelatedDocsBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, 0, idDocument, 0};
            run.query(conn, SQL_QUERY, h, obj);
            } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "spRelatedDocsObjects (?, ?, ?, ?) }";

}
