package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.datacontract.schemas._2004._07.VCB_Analisis_Data.DBResult;
import org.tempuri.IDataProxy;

public class MarketDAO {

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<MarketBO> get(String pattern) {
        List<MarketBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<MarketBO>> h = new BeanListHandler<>(MarketBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, StatementConstant.SC_0.get(), null, null, null, pattern, null};
            list = (List<MarketBO>) run.query(conn, SQL_QUERY, h, obj);
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public int insertUpdate(MarketBO bo) {
        List<MarketBO> list = null;
        exception = null;
        //Guardar en Web Service Mercado
        //saveMarketWS(bo);
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<MarketBO>> h = new BeanListHandler<>(MarketBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIdMarket(), bo.getName(), bo.getNomenclature(), bo.getIdMiVector(), null, bo.getIdMiVector_real()};
            list = (List<MarketBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list == null || list.isEmpty() == true ? -1 : Integer.parseInt(list.get(StatementConstant.SC_0.get()).getIdMiVector_real());
    }

    public String[] saveMarketWS(MarketBO bo) {
        boolean saved = false;
        String[] ids = new String[2];

        try {
            String ip = Utilerias.getIP();
            IDataProxy proxy = new IDataProxy(Utilerias.getProperty(ApplicationProperties.ANALISIS_WS));

            if (bo.getIdMarket() > 0) {
                //Actualizacion de mercado
                // TODO Pendiente 
                /*DBResult result = proxy.actualizaDocumento(Integer.MIN_VALUE, ip, Integer.MIN_VALUE, Integer.MIN_VALUE, ip, ip, ip, Integer.MIN_VALUE, ip)
                 if (result.getResultCd() == "000") {

                 }*/
            } else {
                //Nuevo Mercado
                DBResult result = proxy.nuevoCategoria(bo.getName(), InstanceContext.getInstance().getUsuario().getUsuarioId(), ip);
                if (result.getResultCd().equals("000")) {
                    try {
                        //Obtener ids de mercado e idMiVector
                        String returnMessage = result.getResultMsg();
                        ids = UtileriasWS.getIDSMarketMiVector(returnMessage);

                    } catch (Exception e) {
                        Utilerias.logger(getClass()).info(e);
                    }
                } else {
                    exception = new Exception(result.getResultMsg());
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de categoria no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(e);
        }
        return ids;
    }

    public boolean delete(MarketBO bo) {
        boolean b = false;
        List<MarketBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<MarketBO>> h = new BeanListHandler<>(MarketBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIdMarket(), null, null, null, null, null};
            list = (List<MarketBO>) run.query(conn, SQL_QUERY, h, obj);
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
            for (int o : ids) {
                b = delete(new MarketBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPMARKET (?,?,?,?,?,?,?) }";

}
