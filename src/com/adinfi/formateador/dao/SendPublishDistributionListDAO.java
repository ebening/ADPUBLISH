/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishDistributionListBO;
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
 * @author vectoran
 */
public class SendPublishDistributionListDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SendPublishDistributionListBO> get(int id) {
        List<SendPublishDistributionListBO> list = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListBO>> h = new BeanListHandler<>(SendPublishDistributionListBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, id, null};
            list = (List<SendPublishDistributionListBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(SendPublishDistributionListBO bo) {
        List<SendPublishDistributionListBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListBO>> h = new BeanListHandler<>(SendPublishDistributionListBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getId_distribution(), bo.getId_publish(), bo.getId_channel()};
            list = (List<SendPublishDistributionListBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getId_distribution();
    }

    public void delete(SendPublishDistributionListBO bo) {
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListBO>> h = new BeanListHandler<>(SendPublishDistributionListBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, null, bo.getId_publish(), null};
            run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        //return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND_DISTRIBUTION_LIST (?, ?, ?, ?) }";

}
