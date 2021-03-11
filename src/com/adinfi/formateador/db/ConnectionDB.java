/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.db;

import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Desarrollador
 */
public class ConnectionDB {

    private static volatile ConnectionDB instance = null;
    private boolean dbConfigured = false;
    private Connection connection;
    private boolean errorOcurred;
    private ComboPooledDataSource cpds;

    private ConnectionDB() {
        if (dbConfigured == false) {
            try {
                initDBParameters();
                errorOcurred = false;
            } catch (SQLException ex) {
                errorOcurred = true;
            }
        }
    }

    public static ConnectionDB getInstance() {
        if (instance == null) {
            synchronized (ConnectionDB.class) {
                if (instance == null) {
                    instance = new ConnectionDB();
                }
            }
        }
        return instance;
    }

    private void initDBParameters() throws SQLException {
        dbConfigured = false;
        String driverClassName = Utilerias.getProperty(ApplicationProperties.JDBC_DRIVER);
        if (driverClassName == null || driverClassName.isEmpty()) {
            throw new SQLException("Driver class name wrong.");
        }
        try {
            cpds = new ComboPooledDataSource();
            getCpds().setDriverClass(driverClassName); //loads the jdbc driver

            String user = Utilerias.getProperty(ApplicationProperties.DB_USER);
            String pass = Utilerias.getProperty(ApplicationProperties.DB_PASS);
            String url = Utilerias.getProperty(ApplicationProperties.URL_CONNECTION);

            getCpds().setJdbcUrl(url);
            getCpds().setUser(user);
            getCpds().setPassword(pass);

            // the settings below are optional -- c3p0 can work with defaults
            int min = Utilerias.getProperty(ApplicationProperties.DB_POOL_MINPOOLSIZE).isEmpty()
                    ? GlobalDefines.DEFAULT_DB_POOL_MINPOOLSIZE
                    : Integer.parseInt(Utilerias.getProperty(ApplicationProperties.DB_POOL_MINPOOLSIZE));
            int acquire = Utilerias.getProperty(ApplicationProperties.DB_POOL_ACQUIREINCREMENT).isEmpty()
                    ? GlobalDefines.DEFAULT_DB_POOL_ACQUIREINCREMENT
                    : Integer.parseInt(Utilerias.getProperty(ApplicationProperties.DB_POOL_ACQUIREINCREMENT));
            int max = Utilerias.getProperty(ApplicationProperties.DB_POOL_MAXPOOLSIZE).isEmpty()
                    ? GlobalDefines.DEFAULT_DB_POOL_MAXPOOLSIZE
                    : Integer.parseInt(Utilerias.getProperty(ApplicationProperties.DB_POOL_MAXPOOLSIZE));
            int logTimeout = Utilerias.getProperty(ApplicationProperties.DB_LOGIN_TIMEOUT).isEmpty()
                    ? GlobalDefines.DEFAULT_DB_POOL_MAXPOOLSIZE
                    : Integer.parseInt(Utilerias.getProperty(ApplicationProperties.DB_LOGIN_TIMEOUT));
            getCpds().setMinPoolSize(min);
            getCpds().setAcquireIncrement(acquire);
            getCpds().setMaxPoolSize(max);
            //getCpds().setLoginTimeout(logTimeout);
            dbConfigured = true;
            // The DataSource cpds is now a fully configured and usable pooled DataSource 
        } catch (PropertyVetoException ex) {
            Utilerias.logger(getClass()).fatal(ex);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        connection = getCpds().getConnection();
        if (connection != null && connection.isClosed() == false) {
            return connection;
        } else {
            throw new SQLException("java.sql.SQLException: Error conection");
        }
    }

    /**
     * @return the errorOcurred
     */
    public boolean isErrorOcurred() {
        return errorOcurred;
    }

    /**
     * @return the dbConfigured
     */
    public boolean isDbConfigured() {
        return dbConfigured;
    }

    /**
     * @return the cpds
     */
    public ComboPooledDataSource getCpds() {
        return cpds;
    }
}
