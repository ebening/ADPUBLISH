/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SendPublishDistributionListAttachBO;
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
public class SendPublishDistributionListAttachDAO{

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<SendPublishDistributionListAttachBO> get(int id) {
        List<SendPublishDistributionListAttachBO> list = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListAttachBO>> h = new BeanListHandler<>(SendPublishDistributionListAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, id, null};
            list = (List<SendPublishDistributionListAttachBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(SendPublishDistributionListAttachBO bo) {
        List<SendPublishDistributionListAttachBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListAttachBO>> h = new BeanListHandler<>(SendPublishDistributionListAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getId_distribution_attach(), bo.getId_publish(), bo.getId_channel()};
            list = (List<SendPublishDistributionListAttachBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getId_distribution_attach();
    }

    public void delete(SendPublishDistributionListAttachBO bo) {
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<SendPublishDistributionListAttachBO>> h = new BeanListHandler<>(SendPublishDistributionListAttachBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, null, bo.getId_publish(), null};
            run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        //return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENT_SEND_DISTRIBUTION_LIST_ATTACH (?, ?, ?, ?) }";

}
