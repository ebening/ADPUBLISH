package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.LinkedExcelBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guillermo Trejo
 */
public class ExcelDAO {
    
    public LinkedExcelBO getFromCtegory(int id_category, int id_name) {
        LinkedExcelBO bo = null;
        Connection conn= null;
        PreparedStatement pStmt= null;
        try{
            conn= ConnectionDB.getInstance().getConnection();
            pStmt= conn.prepareStatement(strGetExcelbyCatbyName);
            pStmt.setInt(1, id_category);
            pStmt.setInt(2, id_category);
            pStmt.setInt(3, id_name);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                bo= new LinkedExcelBO(rs.getInt("IDLINKEDEXCEL"),
                            rs.getString("CATEGORY"),
                            rs.getString("NAME"),
                            rs.getString("PATH"),
                            rs.getBoolean("ISCELL"),
                            rs.getString("SHEET"),
                            rs.getString("XY1"),
                            rs.getString("XY2")
                    );
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bo;
    }

    public List<LinkedExcelBO> getFromCtegory(Integer id_category) {

        List<LinkedExcelBO> list = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strGetExcelbyCat);) {

            if (id_category == null || id_category == 0) {
                pStmt.setNull(1, Types.VARCHAR);
                pStmt.setNull(2, Types.VARCHAR);
            } else {
                pStmt.setInt(1, id_category);
            }
            ResultSet rs = pStmt.executeQuery();

            list = new ArrayList<>();
            while (rs.next()) {
                LinkedExcelBO bo
                        = new LinkedExcelBO(rs.getInt("IDLINKEDEXCEL"),
                                rs.getString("CATEGORY"),
                                rs.getString("NAME"),
                                rs.getString("PATH"),
                                rs.getBoolean("ISCELL"),
                                rs.getString("SHEET"),
                                rs.getString("XY1"),
                                rs.getString("XY2")
                        );
                bo.setRange(rs.getString("RANGE"));
                list.add(bo);
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return list;
    }
    
    public LinkedExcelBO getLinkedExcelBO(int idLinkedExcel) {

        LinkedExcelBO retVal = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strGetExcelLinked);) {


            pStmt.setInt(1, idLinkedExcel);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                retVal = new LinkedExcelBO(rs.getInt("IDLINKEDEXCEL"),
                                rs.getString("CATEGORY"),
                                rs.getString("NAME"),
                                rs.getString("PATH"),
                                rs.getBoolean("ISCELL"),
                                rs.getString("SHEET"),
                                rs.getString("XY1"),
                                rs.getString("XY2")
                        );
                retVal.setRange(rs.getString("RANGE"));
                retVal.setId_category(rs.getInt("ID_CATEGORY"));
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
            retVal = null;
        }
        return retVal;
    }

    public List<LinkedExcelBO> getExcel() {

        List<LinkedExcelBO> list = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strGetExcel);) {

            ResultSet rs = pStmt.executeQuery();

            list = new ArrayList<>();
            while (rs.next()) {
                LinkedExcelBO bo = new LinkedExcelBO(
                        rs.getInt("IDLINKEDEXCEL"),
                        rs.getInt("ID_CATEGORY"),
                        rs.getString("CATEGORY"),
                        rs.getString("NAME"),
                        rs.getString("PATH"),
                        rs.getBoolean("ISCELL"),
                        rs.getString("SHEET"),
                        rs.getString("XY1"),
                        rs.getString("XY2"),
                        rs.getString("RANGE")
                );

                list.add(bo);
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return list;
    }
    
    public LinkedExcelBO getCategoriesById(int id_category) {
        LinkedExcelBO linkedExcelBO = null;
        Connection conn= null;
        PreparedStatement pStmt= null;
        try{
            conn = ConnectionDB.getInstance().getConnection();
            pStmt = conn.prepareStatement(strGetCategoriesById);
            pStmt.setInt(1, id_category);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                linkedExcelBO= new LinkedExcelBO();
                linkedExcelBO.setCategory(rs.getString("CATEGORY"));
                linkedExcelBO.setId_category(rs.getInt("ID_CATEGORY"));
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return linkedExcelBO;
    }

    public List<LinkedExcelBO> getAllCtegories() {

        List<LinkedExcelBO> list = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement pStmt = conn.createStatement();) {

            ResultSet rs = pStmt.executeQuery(strGetALLCategories);

            list = new ArrayList<>();
            while (rs.next()) {
                LinkedExcelBO bo = new LinkedExcelBO();
                bo.setCategory(rs.getString("CATEGORY"));
                bo.setId_category(rs.getInt("ID_CATEGORY"));
                list.add(bo);
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return list;
    }

    public int insertCatalogExcel(LinkedExcelBO bo) {

        int catalogExcelID = 0;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strInsertCatalogExcel, Statement.RETURN_GENERATED_KEYS);) {

            pStmt.setString(1, bo.getCategory());
            pStmt.setString(2, bo.getName());
            pStmt.setBoolean(3, bo.isIsCell());
            pStmt.setString(4, bo.getSheet());
            pStmt.setString(5, bo.getXY1());
            pStmt.setString(6, bo.getXY2());

            pStmt.executeUpdate();

            ResultSet rsGenerate = pStmt.getGeneratedKeys();
            if (rsGenerate != null && rsGenerate.next() == true) {
                catalogExcelID = rsGenerate.getInt(1);
            }
            if (rsGenerate != null) {
                rsGenerate.close();
            }

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return catalogExcelID;
    }

    public int insertCatalogExcelDAO(LinkedExcelBO bo) {

        int catalogExcelID = 0;

        if (bo.getIdLinkedExcel() > 0) {

            //UPDATE
            try (
                    Connection conn = ConnectionDB.getInstance().getConnection();
                    PreparedStatement pStmt = conn.prepareStatement(strUpdateCatalogExcelDAO, Statement.RETURN_GENERATED_KEYS);) {

                pStmt.setInt(1, bo.getId_category());
                pStmt.setString(2, bo.getName());
                pStmt.setString(3, bo.getPath());
                pStmt.setBoolean(4, bo.isIsCell());
                pStmt.setString(5, bo.getSheet());
                pStmt.setString(6, bo.getXY1());
                pStmt.setString(7, bo.getXY2());
                pStmt.setString(8, bo.getRange());
                pStmt.setInt(9, bo.getIdLinkedExcel());

                pStmt.executeUpdate();

                ResultSet rsGenerate = pStmt.getGeneratedKeys();
                if (rsGenerate != null && rsGenerate.next() == true) {
                    catalogExcelID = rsGenerate.getInt(1);
                }
                if (rsGenerate != null) {
                    rsGenerate.close();
                }

            } catch (SQLException e) {
                Utilerias.logger(getClass()).error(e);
            }
        } else {

            //INSERT
            try (
                    Connection conn = ConnectionDB.getInstance().getConnection();
                    PreparedStatement pStmt = conn.prepareStatement(strInsertCatalogExcelDAO, Statement.RETURN_GENERATED_KEYS);) {

                pStmt.setInt(1, bo.getId_category());
                pStmt.setString(2, bo.getName());
                pStmt.setString(3, bo.getPath());
                pStmt.setBoolean(4, bo.isIsCell());
                pStmt.setString(5, bo.getSheet());
                pStmt.setString(6, bo.getXY1());
                pStmt.setString(7, bo.getXY2());
                pStmt.setString(8, bo.getRange());

                pStmt.executeUpdate();

                ResultSet rsGenerate = pStmt.getGeneratedKeys();
                if (rsGenerate != null && rsGenerate.next() == true) {
                    catalogExcelID = rsGenerate.getInt(1);
                }
                if (rsGenerate != null) {
                    rsGenerate.close();
                }

            } catch (SQLException e) {
                Utilerias.logger(getClass()).error(e);
            }

        }

        return catalogExcelID;
    }

    public boolean deleteLinkedExcel(int idLinkedExcel) {
        boolean b = true;
        
              //UPDATE
            try (
                    Connection conn = ConnectionDB.getInstance().getConnection();
                    PreparedStatement pStmt = conn.prepareStatement(strUpdateCatalogExcelDeleteDAO, Statement.RETURN_GENERATED_KEYS);) {

                pStmt.setInt(1, idLinkedExcel);
                int r = pStmt.executeUpdate();


            } catch (SQLException e) {
                Utilerias.logger(getClass()).error(e);
            }

        return b;
    }


    private static final String strGetALLCategories
            = new StringBuffer("SELECT ")
            .append("DISTINCT ")
            .append("CATEGORY,ID_CATEGORY")
            .append(" FROM ")
            .append(" " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL ")
            .append(" WHERE ")
            .append(" STATUS = 1 ")
            .toString();
    
    private static final String strGetCategoriesById
            = new StringBuffer("SELECT ")
            .append("CATEGORY,ID_CATEGORY")
            .append(" FROM ")
            .append(" " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL ")
            .append(" WHERE ")
            .append(" STATUS = 1 ")
            .append(" AND ID_CATEGORY= ? ")
            .toString();

    private static final String strInsertCatalogExcel
            = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL "
            + " (CATEGORY, NAME, ISCELL, SHEET, XY1, XY2) "
            + " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String strInsertCatalogExcelDAO
            = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL (ID_CATEGORY,NAME,PATH,ISCELL,SHEET,XY1,XY2,RANGE) \n"
            + "VALUES (?,?,?,?,?,?,?,?)";

    private static final String strUpdateCatalogExcelDAO
            = "    UPDATE " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL SET"
            + "	ID_CATEGORY = ?"
            + "	,NAME   = ?"
            + "	,PATH   = ?"
            + "	,ISCELL = ?"
            + "	,SHEET  = ?"
            + "	,XY1    = ?"
            + "	,XY2    = ?"
            + "	,RANGE  = ?"
            + "WHERE  IDLINKEDEXCEL = ?";
    
        private static final String strUpdateCatalogExcelDeleteDAO
            = " UPDATE " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL SET "
            + " STATUS = 0 "
            + " WHERE  IDLINKEDEXCEL = ? ";

    private static final String strGetExcel = "SELECT L.IDLINKEDEXCEL, L.ID_CATEGORY, CAT.CATEGORY, L.NAME, L.PATH, L.ISCELL, L.RANGE, L.SHEET, L.XY1, L.XY2 FROM " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL AS L \n"
            + "INNER JOIN " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL AS CAT ON CAT.ID_CATEGORY = L.ID_CATEGORY WHERE L.STATUS = 1 ORDER BY CAT.CATEGORY, UPPER(L.NAME)";

    private static final String strGetExcelbyCat = "SELECT L.IDLINKEDEXCEL, L.ID_CATEGORY, CAT.CATEGORY, L.NAME, L.PATH, L.ISCELL, L.RANGE, L.SHEET, L.XY1, L.XY2 FROM " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL AS L \n"
            + "INNER JOIN " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL AS CAT ON CAT.ID_CATEGORY = L.ID_CATEGORY WHERE L.STATUS = 1 AND L.ID_CATEGORY = ? ";
    
    private static final String strGetExcelbyCatbyName = "SELECT L.IDLINKEDEXCEL, L.ID_CATEGORY, CAT.CATEGORY, L.NAME, L.PATH, L.ISCELL, L.RANGE, L.SHEET, L.XY1, L.XY2 FROM " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL AS L \n"
            + "INNER JOIN " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL AS CAT ON CAT.ID_CATEGORY = ? "
            + " WHERE L.STATUS = 1 AND CAT.ID_CATEGORY = ? AND L.IDLINKEDEXCEL= ? ";
    
    private static final String strGetExcelLinked = " SELECT L.IDLINKEDEXCEL, L.ID_CATEGORY, CAT.CATEGORY, L.NAME, L.PATH, L.ISCELL, L.RANGE, L.SHEET, L.XY1, L.XY2 FROM " + GlobalDefines.DB_SCHEMA + "LINKEDEXCEL AS L "
            + " INNER JOIN " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL AS CAT ON CAT.ID_CATEGORY = L.ID_CATEGORY WHERE L.IDLINKEDEXCEL = ?";

}
