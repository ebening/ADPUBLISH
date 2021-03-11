/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.TextEditorBO;
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
public class TextEditorDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<TextEditorBO> get() {
        List<TextEditorBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<TextEditorBO>> h = new BeanListHandler<>(TextEditorBO.class);
            Object[] obj = new Object[]{
                GlobalDefines.SELECT_ACTION,
                StatementConstant.SC_1.get(),
                null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
            };
            list = (List<TextEditorBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int Update(TextEditorBO bo) {
        List<TextEditorBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<TextEditorBO>> h = new BeanListHandler<>(TextEditorBO.class);
            Object[] obj = new Object[] {
                GlobalDefines.INSERT_UPDATE_ACTION, 
                StatementConstant.SC_1.get(), 
                bo.isCopy(), bo.isPaste(), bo.isPasteNoFormat(), bo.isCut(), bo.isBold(), bo.isUnderline(), bo.isItalic(), bo.isUndo(), bo.isRedo(),
                bo.isLeftalign(), bo.isCenteralign(), bo.isRightalign(), bo.isUnorderlist(), bo.isOrderlist(), bo.isUnicodecharacter(), bo.isMathsymbols(), bo.isStrikethrough()
            };
            list = (List<TextEditorBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIdTextEditor();
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPTEXTEDITOR (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
}
