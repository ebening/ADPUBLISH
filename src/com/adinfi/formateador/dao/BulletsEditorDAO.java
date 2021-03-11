/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.BulletsEditorBO;
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
public class BulletsEditorDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<BulletsEditorBO> get() {
        List<BulletsEditorBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<BulletsEditorBO>> h = new BeanListHandler<>(BulletsEditorBO.class);
            Object[] obj = new Object[]{
                GlobalDefines.SELECT_ACTION,
                StatementConstant.SC_1.get(),
                null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
            };
            list = (List<BulletsEditorBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int Update(BulletsEditorBO bo) {
        List<BulletsEditorBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<BulletsEditorBO>> h = new BeanListHandler<>(BulletsEditorBO.class);
            Object[] obj = new Object[] {
                GlobalDefines.INSERT_UPDATE_ACTION, 
                StatementConstant.SC_1.get(), 
                bo.isCopy(), bo.isPaste(), bo.isPasteNoFormat(), bo.isCut(), bo.isBold(), bo.isUnderline(), bo.isItalic(), bo.isUndo(), bo.isRedo(),
                bo.isLeftalign(), bo.isCenteralign(), bo.isRightalign(), bo.isUnorderlist(), bo.isOrderlist(), bo.isUnicodecharacter(), bo.isMathsymbols(), bo.isStrikethrough()
            };
            list = (List<BulletsEditorBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdTextEditor();
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA +"spTextEditorBullet(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
}
