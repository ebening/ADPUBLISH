package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.ImageInfoBO;
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
public class ImageInfoDAO {
    
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
        public List<ImageInfoBO> get(String pattern) {
        List<ImageInfoBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<ImageInfoBO>> h = new BeanListHandler<>(ImageInfoBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, StatementConstant.SC_0.get(),null,null,null,null,null,pattern};
            list = (List<ImageInfoBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }
        
        public int insertUpdate(ImageInfoBO bo) {
        List<ImageInfoBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<ImageInfoBO>> h = new BeanListHandler<>(ImageInfoBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION,bo.getIdImageInfo(),bo.getCategory(),bo.getName(),bo.getDescription(),bo.getAutor(),bo.getSource(),null};
            list = (List<ImageInfoBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdImageInfo();
    }
        
         public boolean delete(ImageInfoBO bo) {
        boolean b = false;
        List<ImageInfoBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<ImageInfoBO>> h = new BeanListHandler<>(ImageInfoBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIdImageInfo(),null,null,null,null,null,null};
            list = (List<ImageInfoBO>) run.query(conn, SQL_QUERY, h, obj);
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
                b = delete(new ImageInfoBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }
        
         private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPIMAGEINFO (?, ?, ?, ?, ?, ?, ?, ?) }";
}
