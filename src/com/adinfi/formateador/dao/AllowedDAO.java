/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.AllowedBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Josue Sanchez
 */
public class AllowedDAO {

    private static final String SQL_QUERY
            = "SELECT \n"
            + "	IDALLOWED, KEY, VALUE, DESC, STATUS \n"
            + "FROM \n"
            + GlobalDefines.DB_SCHEMA + "ALLOWED\n"
            + "WHERE \n"
            + "	STATUS = 1 ";

    public List<AllowedBO> getALL() {
        List<AllowedBO> list = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<AllowedBO>> h = new BeanListHandler<>(AllowedBO.class);
            //16 IN to store procedure
//            Object[] obj = new Object[]{
//                GlobalDefines.SELECT_ACTION,
//                StatementConstant.SC_0.get(),
//                null, null, null, null, null, null, null, null, null, null, null, null, null, pattern};
            list = (List<AllowedBO>) run.query(conn, SQL_QUERY, h, new Object[]{});
        } catch (Exception e) {
            Utilerias.logger(getClass()).trace(e);
        }
        return list;
    }

}
