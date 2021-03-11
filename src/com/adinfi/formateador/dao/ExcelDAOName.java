package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.CatalogExcelBOName;
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

/**
 *
 * @author Guillermo Trejo
 */
public class ExcelDAOName {

    public List<CatalogExcelBOName> getCatalogExcel(String category) {

        List<CatalogExcelBOName> list = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strGetCatalogExcel);) {

            if (category == null || category.isEmpty() == true) {
                pStmt.setNull(1, Types.VARCHAR);
                pStmt.setNull(2, Types.VARCHAR);
            } else {
                pStmt.setString(1, category);
                pStmt.setString(2, category);
            }
            ResultSet rs = pStmt.executeQuery();

            list = new ArrayList<>();
            while (rs.next()) {
                CatalogExcelBOName bo
                        = new CatalogExcelBOName(rs.getInt("CATALOG_EXCEL_ID"),
                                rs.getString("CATEGORY"));

                list.add(bo);
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return list;
    }

    public int insertCatalogExcel(CatalogExcelBOName bo) {

        int catalogExcelID = 0;
        
        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strInsertCatalogExcel, Statement.RETURN_GENERATED_KEYS);) {

            pStmt.setString(1, bo.getCategory());
            pStmt.setString(2, bo.getExcelName());
            pStmt.setString(3, bo.getHoja());
            pStmt.setString(4, bo.getXY1());
            pStmt.setString(5, bo.getXY2());

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
    
    
  

    private static final String strGetCatalogExcel
            = "SELECT "
            + "	CATALOG_EXCEL_ID,"
            + "	CATEGORY,"
            + "WHERE "
            + "	CATEGORY = ? OR ? IS NULL ";

    private static final String strInsertCatalogExcel
            = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL "
            + " (CATEGORY) "
            + " VALUES (?)";

}
