package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SubjectBO;
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
public class SubjectDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SubjectBO> get(String pattern) {
        List<SubjectBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {

            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SubjectBO>> h = new BeanListHandler<>(SubjectBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, null, null, pattern};
            list = (List<SubjectBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (Exception e) {

        }

        return list;
    }
    
    public List<SubjectBO> getByName(String name, boolean esEmisora) {
        List<SubjectBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {

            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SubjectBO>> h = new BeanListHandler<>(SubjectBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION_EMISOR_BY_NAME, null, name, null, esEmisora ? 1 : null, null};
            list = (List<SubjectBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (Exception e) {

        }

        return list;
    }

    public int insertUpdate(SubjectBO bo) {
        List<SubjectBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SubjectBO>> h = new BeanListHandler<>(SubjectBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdSubject(), bo.getName().trim(), bo.getIndustry(), bo.isIssuing(), null};
            list = (List<SubjectBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdSubject();
    }

    public boolean delete(SubjectBO bo) {
        boolean b = false;
        List<SubjectBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SubjectBO>> h = new BeanListHandler<>(SubjectBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIdSubject(), null, null, null, null};
            list = (List<SubjectBO>) run.query(conn, SQL_QUERY, h, obj);
            if (list != null && list.isEmpty() == false) {
                b = !(list.get(StatementConstant.SC_0.get()).isStatus());
            } else {
                b = false;
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    public boolean delete(List<Integer> ids) {
        exception = null;
        boolean b = false;
        try {
            for (int o : ids) {
                b = delete(new SubjectBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPSUBJECT(?, ?, ?, ?, ?, ?)}";
}
