package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class SendPublishDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SendPublishBO> get(int idDocument) {
        List<SendPublishBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishBO>> h = new BeanListHandler<>(SendPublishBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, idDocument, null, null, null, null, null, null, null, null, null, null};
            list = (List<SendPublishBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

//    private java.sql.Clob stringToClob(String source) {
//        try {
//            return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
//        } catch (Exception e) {
//            Utilerias.logger(getClass()).info(e);
//            return null;
//        }
//    }
    public int insertUpdate(SendPublishBO bo) {
        List<SendPublishBO> list = null;
        exception = null;
        int isScheduled = (bo.isScheduled()) ? 1 : 0;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishBO>> h = new BeanListHandler<>(SendPublishBO.class);
            Object[] obj = new Object[]{
                GlobalDefines.INSERT_UPDATE_ACTION,
                bo.getIdDocument_send(),
                bo.getIdStatus_publish(),
                bo.getIdDocument(),
                bo.getIdSubject(),
                bo.getTitle(),
                bo.getText(),
                isScheduled,
                bo.getDate(),
                bo.getTime(),
                bo.getDate_publish(),
                bo.getUrl(),
                bo.getPid(),
                bo.getPidzip()
            };
            list = (List<SendPublishBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdDocument_send();
    }
    
        public int UpdateURL(int idDocSend, String url) {
        List<SendPublishBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishBO>> h = new BeanListHandler<>(SendPublishBO.class);
            Object[] obj = new Object[]{
                StatementConstant.SC_6.get(),
                idDocSend,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                url,
                null,
                null
            };
            list = (List<SendPublishBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdDocument_send();
    }
        
            public int UpdateStatus(int idDocSend, int Status) {
        List<SendPublishBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishBO>> h = new BeanListHandler<>(SendPublishBO.class);
            Object[] obj = new Object[]{
                StatementConstant.SC_4.get(),
                idDocSend,
                Status,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            };
            list = (List<SendPublishBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdDocument_send();
    }   

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
}
