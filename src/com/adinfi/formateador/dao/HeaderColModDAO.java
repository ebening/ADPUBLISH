/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.HeaderColModBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADINFI
 */
public class HeaderColModDAO {
    public List<HeaderColModBO> get(int idMarket, int idDoc){
        List<HeaderColModBO> lHeadersBO = new ArrayList<>();
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            String sqlHeader = "SELECT                 "
                    + "	IDHEADERCOLMOD,             "
                    + "	IDMARKET,           "
                    + " IDDOCUMENT_TYPE,  "
                    + "	DESCRIPTION   "
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "HEADER_COL_MOD    "
                    + " WHERE IDMARKET = " + idMarket
                    + " AND IDDOCUMENT_TYPE = " + idDoc
                    + "	ORDER BY DESCRIPTION ";

            ResultSet rsHeader = st.executeQuery(sqlHeader);
            while (rsHeader.next()) {
                HeaderColModBO headerBO = new HeaderColModBO();
                headerBO.setIdHeaderColMod(rsHeader.getInt("IDHEADERCOLMOD"));
                headerBO.setIdMarket(rsHeader.getInt("IDMARKET"));
                headerBO.setIdDocumentType(rsHeader.getInt("IDDOCUMENT_TYPE"));
                headerBO.setName(rsHeader.getString("DESCRIPTION"));

                lHeadersBO.add(headerBO);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return lHeadersBO;
    }
    
    public boolean validateName(int idMarket, int docType, String name){
        boolean valido = false;
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            String sqlHeader = "SELECT COUNT(IDHEADERCOLMOD) AS IDS"
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "HEADER_COL_MOD    "
                    + "	WHERE IDMARKET = " + idMarket
                    + "	AND IDDOCUMENT_TYPE = " + docType
                    + "	AND UPPER(DESCRIPTION) = UPPER('" + name + "')";

            ResultSet rsHeader = st.executeQuery(sqlHeader);
            while (rsHeader.next()) {
                int ids = rsHeader.getInt("IDS");
                if (ids == 0) {
                    valido = true;
                }
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return valido;
    }
    
    public void insert(HeaderColModBO headerBO) throws Exception{
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            StringBuilder sqlHeader = new StringBuilder();
            sqlHeader.append("INSERT INTO " + GlobalDefines.DB_SCHEMA + "HEADER_COL_MOD (");
            sqlHeader.append(" IDMARKET,");
            sqlHeader.append(" IDDOCUMENT_TYPE,");
            sqlHeader.append(" DESCRIPTION)");
            sqlHeader.append(" VALUES (");
            sqlHeader.append(headerBO.getIdMarket());
            sqlHeader.append(", ");
            sqlHeader.append(headerBO.getIdDocumentType());
            sqlHeader.append(", '");
            sqlHeader.append(headerBO.getName());
            sqlHeader.append("')");
            
            st.execute(sqlHeader.toString());
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            throw new Exception(ex);
        }
    }
    
    public HeaderColModBO getHeader(int idHeader){
        HeaderColModBO headerBO = new HeaderColModBO();
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            String sqlHeader = "SELECT                 "
                    + "	IDHEADERCOLMOD,             "
                    + "	IDMARKET,           "
                    + " IDDOCUMENT_TYPE,  "
                    + "	DESCRIPTION   "
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "HEADER_COL_MOD    "
                    + " WHERE IDHEADERCOLMOD = " + idHeader
                    + "	ORDER BY DESCRIPTION ";

            ResultSet rsHeader = st.executeQuery(sqlHeader);
            while (rsHeader.next()) {
                headerBO.setIdHeaderColMod(rsHeader.getInt("IDHEADERCOLMOD"));
                headerBO.setIdMarket(rsHeader.getInt("IDMARKET"));
                headerBO.setIdDocumentType(rsHeader.getInt("IDDOCUMENT_TYPE"));
                headerBO.setName(rsHeader.getString("DESCRIPTION"));
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return headerBO;
    }
}
