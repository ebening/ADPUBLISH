/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.TwitterBO;
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
public class TwitterDAO {

    private Exception exception;

    public List<TwitterBO> get(int idDoc) {
        List<TwitterBO> list = null;

        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner runer = new QueryRunner();
            ResultSetHandler<List<TwitterBO>> h = new BeanListHandler<>(TwitterBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, idDoc, null, null, null, null, null, null, null, null, null};
            list = (List<TwitterBO>) runer.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public List<TwitterBO> getByPublish(int idPublish) {
        List<TwitterBO> list = null;

        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner runer = new QueryRunner();
            ResultSetHandler<List<TwitterBO>> h = new BeanListHandler<>(TwitterBO.class);
            Object[] obj = new Object[]{StatementConstant.SC_11.get(), null, null, null, null, null, null, null, null, idPublish, null, null, null};
            list = (List<TwitterBO>) runer.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(TwitterBO bo) {
        List<TwitterBO> list = null;
        exception = null;

        int hasDoc = (bo.isHasDocument()) ? 1 : 0;
        int isSend = (bo.isIsSend()) ? 1 : 0;

        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<TwitterBO>> h = new BeanListHandler<>(TwitterBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdTwitter(), hasDoc, bo.getIdDocument(), bo.getIdUsuario(), bo.getTweet(), bo.getAttachment(), bo.getPath(), isSend, bo.getId_publish_attach(), null, null, bo.getEncodedTw()};
            list = (List<TwitterBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdTwitter();
    }

    private static final String SQL_QUERY
            = "CALL " + GlobalDefines.DB_SCHEMA + "SPTWITTER(?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
