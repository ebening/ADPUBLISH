package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishAuthorsBO;
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

public class SendPublishAuthorsDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SendPublishAuthorsBO> get(int idPublish) {
        List<SendPublishAuthorsBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsBO>> h = new BeanListHandler<>(SendPublishAuthorsBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, idPublish , null};
            list = (List<SendPublishAuthorsBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(SendPublishAuthorsBO bo) {
        List<SendPublishAuthorsBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsBO>> h = new BeanListHandler<>(SendPublishAuthorsBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getId_author_publish(), bo.getId_send_publish(), bo.getId_author()};
            list = (List<SendPublishAuthorsBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getId_author_publish();
    }

    public void delete(SendPublishAuthorsBO bo) {
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishAuthorsBO>> h = new BeanListHandler<>(SendPublishAuthorsBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, null, bo.getId_send_publish(), null};
            run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        //return b;
    }

//    public boolean delete(List<Integer> ids) {
//        exception = null;
//        boolean b = false;
//        try {
//            for(int o : ids) {
//                b = delete(new SendPublishAuthorsBO(o));
//            }
//        } catch (Exception e) {
//            Utilerias.logger(getClass()).info(e);
//            exception = e;
//        }
//        return b;
//    }
    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND_AUTHORS(?, ?, ?, ?) }";

    public static void main(String[] args) {

        SendPublishAuthorsDAO d = new SendPublishAuthorsDAO();
        //List<SendPublishAuthorsBO> list = d.get();
        SendPublishAuthorsBO bo = new SendPublishAuthorsBO();
        bo.setId_author(356);
        bo.setId_send_publish(999999999);
        bo.setId_author_publish(5);

        //System.out.println(d.insertUpdate(bo));
        d.delete(bo);

    }

}
