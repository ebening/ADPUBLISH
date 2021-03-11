/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.SectionColModBO;
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
public class SectionColModDAO {
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public List<SectionColModBO> get(int idHeader) {
        List<SectionColModBO> lSectionsBO = new ArrayList<>();
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            String sqlHeader = "SELECT "
                    + "	IDSECTTIONCOLMOD,"
                    + "	IDHEADERCOLMOD,"
                    + "	IDMARKET,"
                    + " IDDOCUMENT_TYPE,"
                    + "	DESCRIPTION"
                    + "	FROM " + GlobalDefines.DB_SCHEMA + "SECTION_COL_MOD "
                    + " WHERE IDHEADERCOLMOD = " + idHeader
                    + "	ORDER BY DESCRIPTION ";

            ResultSet rsSection = st.executeQuery(sqlHeader);
            while (rsSection.next()) {
                SectionColModBO sectionBO = new SectionColModBO();
                sectionBO.setIdSectionColMod(rsSection.getInt("IDSECTTIONCOLMOD"));
                sectionBO.setIdHeaderColMod(rsSection.getInt("IDHEADERCOLMOD"));
                sectionBO.setIdMarket(rsSection.getInt("IDMARKET"));
                sectionBO.setIdDocumentType(rsSection.getInt("IDDOCUMENT_TYPE"));
                sectionBO.setName(rsSection.getString("DESCRIPTION"));

                lSectionsBO.add(sectionBO);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return lSectionsBO;
    }
    
    public void insert(SectionColModBO headerBO)  throws Exception{
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            StringBuilder sqlSection = new StringBuilder();
            sqlSection.append("INSERT INTO " + GlobalDefines.DB_SCHEMA + "SECTION_COL_MOD (");
            sqlSection.append(" IDMARKET,");
            sqlSection.append(" IDDOCUMENT_TYPE,");
            sqlSection.append(" DESCRIPTION,");
            sqlSection.append(" IDHEADERCOLMOD)");
            sqlSection.append(" VALUES (");
            sqlSection.append(headerBO.getIdMarket());
            sqlSection.append(", ");
            sqlSection.append(headerBO.getIdDocumentType());
            sqlSection.append(", '");
            sqlSection.append(headerBO.getName());
            sqlSection.append("', ");
            sqlSection.append(headerBO.getIdHeaderColMod());
            sqlSection.append(")");
            
            st.execute(sqlSection.toString());
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
            throw new Exception(ex);
        }
    }
    
    public SectionColModBO getSection(int idSection) {
         SectionColModBO sectionBO = new SectionColModBO();
        try (
            Connection conn = ConnectionDB.getInstance().getConnection();
            Statement st = conn.createStatement();) {

            String sqlHeader = "SELECT "
                    + "	IDSECTTIONCOLMOD,"
                    + "	IDHEADERCOLMOD,"
                    + "	IDMARKET,"
                    + " IDDOCUMENT_TYPE,"
                    + "	DESCRIPTION"
                    + "	FROM " + GlobalDefines.DB_SCHEMA +"SECTION_COL_MOD "
                    + " WHERE IDSECTTIONCOLMOD = " + idSection
                    + "	ORDER BY DESCRIPTION ";

            ResultSet rsSection = st.executeQuery(sqlHeader);
            while (rsSection.next()) {
                sectionBO.setIdSectionColMod(rsSection.getInt("IDSECTTIONCOLMOD"));
                sectionBO.setIdHeaderColMod(rsSection.getInt("IDHEADERCOLMOD"));
                sectionBO.setIdMarket(rsSection.getInt("IDMARKET"));
                sectionBO.setIdDocumentType(rsSection.getInt("IDDOCUMENT_TYPE"));
                sectionBO.setName(rsSection.getString("DESCRIPTION"));
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        return sectionBO;
    }
}
