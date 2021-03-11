/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.ImageDataBO;
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
public class ImageDataDAO {
    
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    
      public List<ImageDataBO> get(int idImageInfo) {
        List<ImageDataBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<ImageDataBO>> h = new BeanListHandler<>(ImageDataBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, StatementConstant.SC_0.get(),null,null,idImageInfo};
            list = (List<ImageDataBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }
      
        public int insertUpdate(ImageDataBO bo) {
        List<ImageDataBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<ImageDataBO>> h = new BeanListHandler<>(ImageDataBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION,bo.getIdImageData(),bo.getIdImageInfo(),bo.getImage(),0};
            list = (List<ImageDataBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdImageData();
    }
    
     private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPIMAGEDATA (?, ?, ?, ?, ?) }";     
}
