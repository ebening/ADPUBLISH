package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.PublicityBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class PublicityDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<PublicityBO> get() {
        List<PublicityBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublicityBO>> h = new BeanListHandler<>(PublicityBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, null, null, null};
            list = (List<PublicityBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(PublicityBO bo) {
        List<PublicityBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublicityBO>> h = new BeanListHandler<>(PublicityBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdPublicity(), bo.getText(), bo.getImage(), bo.getUrl(), bo.getDescription()};
            list = (List<PublicityBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdPublicity();
    }

    public boolean delete(PublicityBO bo) {
        boolean b = false;
        List<PublicityBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<PublicityBO>> h = new BeanListHandler<>(PublicityBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIdPublicity(), null, null, null, null};
            list = (List<PublicityBO>) run.query(conn, SQL_QUERY, h, obj);
            if (list != null && list.isEmpty() == false) {
                b = true;
            } else {
                b = false;
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPPUBLICITY (?, ?, ?, ?, ?, ?) }";

}
