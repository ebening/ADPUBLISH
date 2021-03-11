package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.IndustryBO;
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
public class IndustryDAO {
    
     private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
     public List<IndustryBO> get(String pattern) {
        List<IndustryBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<IndustryBO>> h = new BeanListHandler<>(IndustryBO.class);
            Object[] obj = new Object[]{0, 0, null, pattern};

            list = (List<IndustryBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }
     
    public int insertUpdate(IndustryBO bo) {
        List<IndustryBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<IndustryBO>> h = new BeanListHandler<>(IndustryBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdIndustry(), bo.getName(), null};
            list = (List<IndustryBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdIndustry();
    }
       
         public boolean delete(IndustryBO bo) {
        boolean b = false;
        List<IndustryBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<IndustryBO>> h = new BeanListHandler<>(IndustryBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIdIndustry(), null, null};
            list = (List<IndustryBO>) run.query(conn, SQL_QUERY, h, obj);
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
            for(int o : ids) {
                b = delete(new IndustryBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }
     
     
     private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPINDUSTRY (?, ?, ?, ?) }";
}


