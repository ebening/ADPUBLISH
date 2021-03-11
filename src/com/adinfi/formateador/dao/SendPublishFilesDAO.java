package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishFilesBO;
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
public class SendPublishFilesDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int insertUpdate(SendPublishFilesBO bo) {
        List<SendPublishFilesBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishFilesBO>> h = new BeanListHandler<>(SendPublishFilesBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIddocument_send_files(), bo.getIddocument_send(), bo.getFilename(), bo.getFile(), bo.getUid()};
            list = (List<SendPublishFilesBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIddocument_send_files();
    }

    public List<SendPublishFilesBO> get(int idPublish) {
        List<SendPublishFilesBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishFilesBO>> h = new BeanListHandler<>(SendPublishFilesBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, idPublish, null, null, null};
            list = (List<SendPublishFilesBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }

        return list;
    }
    
      public boolean clean(int idPublish) {
          boolean b = true;
        List<SendPublishFilesBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishFilesBO>> h = new BeanListHandler<>(SendPublishFilesBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION_HARD, null, idPublish, null, null, null};
            list = (List<SendPublishFilesBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
            b = false;
        }

        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND_FILES (?, ?, ?, ?, ?, ?) }";

}
