package com.adinfi.formateador.dao;
import com.adinfi.formateador.bos.OfficeBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class OfficeDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<OfficeBO> get(String pattern) {
        List<OfficeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<OfficeBO>> h = new BeanListHandler<>(OfficeBO.class);
            //16 IN to store procedure
            Object[] obj = new 
            Object[]{ GlobalDefines.SELECT_ACTION,
                     StatementConstant.SC_0.get(),
                     null, null, null,null, null, null,null, null, null,null, null, null,null,pattern };
            list = (List<OfficeBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (Exception e) {
            //Utilerias.logger(getClass()).trace(e);
            e.printStackTrace();
            exception = e;
            
        }
        return list;
    }
    
    public int insertUpdate(OfficeBO bo){
      List<OfficeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<OfficeBO>> h = new BeanListHandler<>(OfficeBO.class);
            Object[] obj = new
            Object[]{
                     GlobalDefines.INSERT_UPDATE_ACTION,
                     bo.getIdOffice(),
                     bo.getOrder(),
                     bo.getBranch(),
                     bo.getAddress(),
                     bo.getDistrict(),
                     bo.getZipCode(),
                     bo.getCity(),
                     bo.getState(),
                     bo.getCountry(),
                     bo.isNational(),
                     bo.getPhone1(),
                     bo.getPhone2(),
                     bo.getPhone3(),
                     bo.getPhone4(),
                     null};
            list = (List<OfficeBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdOffice();
    }
    
   public boolean delete(OfficeBO bo) {
        boolean b = false;
        List<OfficeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<OfficeBO>> h = new BeanListHandler<>(OfficeBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION,bo.getIdOffice(),null, null, null,null, null, null,null, null, null,null, null, null,null,null};
            list = (List<OfficeBO>) run.query(conn, SQL_QUERY, h, obj);
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
                
                b = delete(new OfficeBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{call " + GlobalDefines.DB_SCHEMA + "SPOFFICE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

}