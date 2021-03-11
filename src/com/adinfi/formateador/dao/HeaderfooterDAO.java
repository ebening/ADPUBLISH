package com.adinfi.formateador.dao;
import com.adinfi.formateador.bos.HeaderfooterBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class HeaderfooterDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<HeaderfooterBO> get() {
        List<HeaderfooterBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<HeaderfooterBO>> h = new BeanListHandler<>(HeaderfooterBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, null};
            list = (List<HeaderfooterBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(HeaderfooterBO bo) {
        List<HeaderfooterBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<HeaderfooterBO>> h = new BeanListHandler<>(HeaderfooterBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getId(), bo.getHeader(), bo.getFooter()};
            list = (List<HeaderfooterBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getId();
    }

    public boolean deleteHeader(HeaderfooterBO bo) {
        boolean b = false;
        List<HeaderfooterBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<HeaderfooterBO>> h = new BeanListHandler<>(HeaderfooterBO.class);
            Object[] obj = new Object[]{2, bo.getId(), null, null};
            list = (List<HeaderfooterBO>) run.query(conn, SQL_QUERY, h, obj);
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
    
    public boolean deleteFooter(HeaderfooterBO bo) {
        boolean b = false;
        List<HeaderfooterBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<HeaderfooterBO>> h = new BeanListHandler<>(HeaderfooterBO.class);
            Object[] obj = new Object[]{3, bo.getId(), null, null};
            list = (List<HeaderfooterBO>) run.query(conn, SQL_QUERY, h, obj);
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
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPHEADERFOOTER (?, ?, ?, ?) }";

}
