package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.CatalogExcelBOCat;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo Trejo
 */
public class ExcelDAOCat {

    public List<CatalogExcelBOCat> getCatalogExcel(String category, int id_category) {

        List<CatalogExcelBOCat> list = null;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strGetCatalogExcel);) {

//            if (category == null || category.isEmpty() == true) {
//                pStmt.setNull(1, Types.VARCHAR);
//                pStmt.setNull(2, Types.VARCHAR);
//            } else {
//                pStmt.setString(1, category);
//                pStmt.setString(2, category);
//            }
            ResultSet rs = pStmt.executeQuery();

            list = new ArrayList<>();
            while (rs.next()) {
                CatalogExcelBOCat bo = new CatalogExcelBOCat(
                        rs.getInt("ID_CATEGORY"), rs.getString("CATEGORY"));
//                                rs.getString("HOJA"),
//                                rs.getString("XY1"),
//                                rs.getString("XY2"),
//                                rs.getDate("FECHA_CREACION"));

                list.add(bo);
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return list;
    }

    public int insertCatalogExcel(CatalogExcelBOCat bo) {

        int catalogExcelID = 1;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strInsertCatalogExcel, Statement.RETURN_GENERATED_KEYS);) {

            //pStmt.setInt(1, bo.getId_category());
            pStmt.setString(1, bo.getCategory());
            pStmt.executeUpdate();

//            ResultSet rsGenerate = pStmt.getGeneratedKeys();
//            if (rsGenerate != null && rsGenerate.next() == true) {
//                catalogExcelID = rsGenerate.getInt(1);
//            }
//            if (rsGenerate != null) {
//                rsGenerate.close();
//            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }
        return catalogExcelID;
    }

    public boolean checkRepeat(CatalogExcelBOCat bo) {

        boolean b = false;

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                PreparedStatement pStmt = conn.prepareStatement(strCheckName, Statement.RETURN_GENERATED_KEYS);) {

            pStmt.setString(1, bo.getCategory());
            ResultSet res = pStmt.executeQuery();

            if (res.next()) {
                b = true;
            }

        } catch (SQLException e) {
            Utilerias.logger(getClass()).error(e);
        }

        return b;
    }

    private static final String strGetCatalogExcel
            = "SELECT DISTINCT ID_CATEGORY,CATEGORY FROM " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL";

    private static final String strInsertCatalogExcel
            = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL "
            + " (CATEGORY) "
            + " VALUES (?)";

    private static final String strCheckName
            = "SELECT CATEGORY "
            + "FROM " + GlobalDefines.DB_SCHEMA + "CATALOG_EXCEL "
            + "WHERE UPPER(CATEGORY) LIKE UPPER(?)";
}
