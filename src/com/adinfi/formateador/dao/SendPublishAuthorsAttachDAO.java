package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishAuthorsAttachBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class SendPublishAuthorsAttachDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SendPublishAuthorsAttachBO> get(int idPublish) {
        List<SendPublishAuthorsAttachBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsAttachBO>> h = new BeanListHandler<>(SendPublishAuthorsAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, idPublish , null};
            list = (List<SendPublishAuthorsAttachBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(SendPublishAuthorsAttachBO bo) {
        List<SendPublishAuthorsAttachBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsAttachBO>> h = new BeanListHandler<>(SendPublishAuthorsAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getId_authors_attach(), bo.getId_send_publish_attach(), bo.getId_author()};
            list = (List<SendPublishAuthorsAttachBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getId_authors_attach();
    }

    public void delete(SendPublishAuthorsAttachBO bo) {
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsAttachBO>> h = new BeanListHandler<>(SendPublishAuthorsAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, null, bo.getId_send_publish_attach(), null};
            run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        //return b;
    }


    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND_ATTACH_AUTHORS(?, ?, ?, ?) }"; 

}
