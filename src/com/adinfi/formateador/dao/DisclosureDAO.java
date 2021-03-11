package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DisclosureBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author Carlos Félix
 */
public class DisclosureDAO {
    public enum TYPES{
        RENTA_FIJA(1, "Renta fija"),
        F_CON_EMISORA(2, "Fundamental con emisora"),
        F_SIN_EMISORA(3, "Fundamental sin emisora"),
        SEMANARIO(4, "Semanario"),
        ECONOMICO(5, "Economico");
        private int type=0;
        String name= "";
        TYPES(int id,String name){
            type= id;
            this.name= name;
        }
        public int type(){
            return type;
        }
        public String getFullName(){
            return name;
        }
    }
    private final String INSERT_DISCLOSURE= "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DISCLOSURE (DISCLOSURE_ID,TYPE,INSERT_USER,INSERT_DATE,TEXT_TOP,REGULAR_EXPRESSION,"
            + " PREVIEW,TEXT_BOTTOM) VALUES (?,?,?,?,?,?,?,?)";
    private final String UPDATE_DISCLOSURE= "UPDATE " + GlobalDefines.DB_SCHEMA + "DISCLOSURE SET UPDATE_USER= ?, UPDATE_DATE= ?, TEXT_TOP= ?, REGULAR_EXPRESSION= ?,"
            + " PREVIEW= ?, TEXT_BOTTOM= ? WHERE DISCLOSURE_ID=?";
    private final String GET_DISCLOSURE= "SELECT TYPE,TEXT_TOP,REGULAR_EXPRESSION,PREVIEW,TEXT_BOTTOM FROM " + GlobalDefines.DB_SCHEMA + "DISCLOSURE WHERE DISCLOSURE_ID=?";
    
    private final String INSERT_EXCEL= "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DISCLOSURE_EXCEL (DISCLOSURE_ID,ORDER_ID,INSERT_USER,INSERT_DATE,"
            + " CATEGORY_ID,NAME_ID,TITULO,SUBTITULO,DESCRIPCION,IMAGE) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private final String UPDATE_EXCEL= "UPDATE " + GlobalDefines.DB_SCHEMA + "DISCLOSURE_EXCEL SET UPDATE_USER= ?,UPDATE_DATE= ?,CATEGORY_ID= ?,NAME_ID= ?,"
            + " TITULO= ?,SUBTITULO= ?,DESCRIPCION= ?, IMAGE = ? WHERE ORDER_ID= ? AND DISCLOSURE_ID= ?";
    private final String DELETE_EXCEL= "DELETE " + GlobalDefines.DB_SCHEMA + "DISCLOSURE_EXCEL WHERE ORDER_ID= ? AND DISCLOSURE_ID= ?";
    private final String GET_EXCEL= "SELECT CATEGORY_ID,NAME_ID,TITULO,SUBTITULO,DESCRIPCION, IMAGE FROM " + GlobalDefines.DB_SCHEMA + "DISCLOSURE_EXCEL "
            + " WHERE ORDER_ID= ? AND DISCLOSURE_ID= ?";
    private final String GET_DISCLOSURE_TYPE= "SELECT DS.IDENTIFIER FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE D "
            + "LEFT JOIN " + GlobalDefines.DB_SCHEMA + "DISCLOSURE_NAME DS ON D.IDDISCLOSURE= DS.IDDISCLOSURE_NAME WHERE D.IDDOCUMENT_TYPE= ?";
    private Connection conn = null;
    private PreparedStatement getPst(String query) throws SQLException{
        if(conn==null||conn.isClosed()){
            conn = ConnectionDB.getInstance().getConnection();
        }
        PreparedStatement pst= conn.prepareStatement(query);
        return pst;
    }
    private java.sql.Date getSqlDateNow(){
        java.util.Date date= new java.util.Date();
        Date dateSql= new Date(date.getTime());
        return dateSql;
    }
    public TYPES getDisclosureType(int documentType){
        DisclosureBO.ExcelBO excelBO= null;
        PreparedStatement pst= null;
        try {
            pst= getPst(GET_DISCLOSURE_TYPE);
            pst.setInt(1, documentType);
            
            ResultSet rs= pst.executeQuery();
            if(rs!=null&&rs.next()){
                int disclosureType=rs.getInt("IDENTIFIER");
                switch(disclosureType){
                    case 1:return TYPES.RENTA_FIJA;
                    case 2:return TYPES.F_CON_EMISORA;
                    case 3:return TYPES.F_SIN_EMISORA;
                    case 4:return TYPES.SEMANARIO;
                    case 5:return TYPES.ECONOMICO;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(pst!=null){
                try {
                    pst.close();
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
        return null;
    }
    public Boolean updateDisclosure(DisclosureBO disclosureBO, TYPES type){
        PreparedStatement pst= null;
        try {
            pst= getPst(UPDATE_DISCLOSURE);
            pst.setInt(1, disclosureBO.getUpdateUser());
            pst.setDate(2, getSqlDateNow());
            pst.setClob(3, new javax.sql.rowset.serial.SerialClob(disclosureBO.getTextTop().toCharArray()));
            pst.setString(4, disclosureBO.getRegularExpression());
            pst.setClob(5, new javax.sql.rowset.serial.SerialClob(disclosureBO.getPreview().toCharArray()));
            pst.setClob(6, new javax.sql.rowset.serial.SerialClob(disclosureBO.getTextBottom().toCharArray()));
            pst.setInt(7, type.type);
            pst.executeUpdate();
            saveExcel(disclosureBO, type);
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        } finally {
            if(pst!=null){
                try {
                    pst.close();
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
        return Boolean.TRUE;
    }
    public Boolean insertDisclosure(DisclosureBO disclosureBO, TYPES type){
        PreparedStatement pst= null;
        try {
            pst= getPst(INSERT_DISCLOSURE);
            pst.setInt(1, type.type);
            pst.setInt(2, type.type);
            pst.setInt(3, disclosureBO.getInsertUser());
            pst.setDate(4, getSqlDateNow());
            pst.setClob(5, new javax.sql.rowset.serial.SerialClob(disclosureBO.getTextTop().toCharArray()));
            pst.setString(6, disclosureBO.getRegularExpression());
            pst.setClob(7, new javax.sql.rowset.serial.SerialClob(disclosureBO.getPreview().toCharArray()));
            pst.setClob(8, new javax.sql.rowset.serial.SerialClob(disclosureBO.getTextBottom().toCharArray()));
            pst.executeUpdate();
            saveExcel(disclosureBO, type);
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        } finally {
            if(pst!=null){
                try {
                    pst.close();
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
        return Boolean.TRUE;
    }
    public void saveExcel(DisclosureBO disclosureBO, TYPES type){
        if(type != TYPES.RENTA_FIJA){
            DisclosureBO disclosureBOAux= new DisclosureBO();
            if(disclosureBO.getExcelIni()!=null){
                disclosureBOAux.setExcelIni(getExcel(1, type));
                if(disclosureBOAux.getExcelIni()!=null || disclosureBO.getExcelIni().isDelete()){
                    updateExcel(disclosureBO.getExcelIni(), 1, type);
                }else{
                    insertExcel(disclosureBO.getExcelIni(), 1, type);
                }
            }
            if(disclosureBO.getExcelFin()!=null){
                disclosureBOAux.setExcelFin(getExcel(2, type));
                if(disclosureBOAux.getExcelFin()!=null || disclosureBO.getExcelFin().isDelete()){
                    updateExcel(disclosureBO.getExcelFin(), 2, type);
                }else{
                    insertExcel(disclosureBO.getExcelFin(), 2, type);
                }
            }
        }
    }
    public void insertExcel(DisclosureBO.ExcelBO excelBO, int orderId, TYPES type){
        PreparedStatement pst= null;
        try{
            pst= getPst(INSERT_EXCEL);
            pst.setInt(1, type.type);
            pst.setInt(2, orderId);
            pst.setInt(3, excelBO.getInsertUser());
            pst.setDate(4, getSqlDateNow());
            pst.setInt(5, excelBO.getCategoryId());
            pst.setInt(6, excelBO.getNameId());
            pst.setString(7, excelBO.getTitulo());
            pst.setString(8, excelBO.getSubTitulo());
            pst.setString(9, excelBO.getDescripcion());
            
            byte imageArray[] = Utilerias.imageToByteArray(excelBO.getImage());
            if (imageArray == null) {
                pst.setNull(10, java.sql.Types.BLOB);
            } else {
                pst.setBlob(10, new SerialBlob(imageArray));
            }
            
            pst.executeUpdate();
        }catch(Exception ex){
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pst!=null){
                try {
                    pst.close();
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
    }
    public void updateExcel(DisclosureBO.ExcelBO excelBO, int orderId, TYPES type){
        PreparedStatement pst= null;
        try{
            if(excelBO.isDelete()){
                pst= getPst(DELETE_EXCEL);
                pst.setInt(1, orderId);
                pst.setInt(2, type.type);
                pst.executeUpdate();
            }else{
                pst= getPst(UPDATE_EXCEL);
                pst.setInt(1, excelBO.getUpdateUser());
                pst.setDate(2, getSqlDateNow());
                pst.setInt(3, excelBO.getCategoryId());
                pst.setInt(4, excelBO.getNameId());
                pst.setString(5, excelBO.getTitulo());
                pst.setString(6, excelBO.getSubTitulo());
                pst.setString(7, excelBO.getDescripcion());
                
                byte imageArray[] = Utilerias.imageToByteArray(excelBO.getImage());
                if (imageArray == null) {
                    pst.setNull(8, java.sql.Types.BLOB);
                } else {
                    pst.setBlob(8, new SerialBlob(imageArray));
                }
                
                pst.setInt(9, orderId);
                pst.setInt(10, type.type);
                pst.executeUpdate();
            }
        }catch(Exception ex){
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(pst!=null){
                try {
                    pst.close();
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
    }
    public DisclosureBO.ExcelBO getExcel(int orderId, TYPES type){
        DisclosureBO.ExcelBO excelBO= null;
        PreparedStatement pst= null;
        try {
            pst= getPst(GET_EXCEL);
            pst.setInt(1, orderId);
            pst.setInt(2, type.type);
            
            ResultSet rs= pst.executeQuery();
            if(rs!=null&&rs.next()){
                excelBO= new DisclosureBO.ExcelBO();
                excelBO.setCategoryId(rs.getInt("CATEGORY_ID"));
                excelBO.setNameId(rs.getInt("NAME_ID"));
                excelBO.setTitulo(rs.getString("TITULO"));
                excelBO.setSubTitulo(rs.getString("SUBTITULO"));
                excelBO.setDescripcion(rs.getString("DESCRIPCION"));
                
                byte[] imageArray = rs.getBytes("IMAGE");
                excelBO.setImage(Utilerias.byteArrayToImage(imageArray));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(pst!=null){
                try {
                    pst.close();
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
        return excelBO;
    }
    public DisclosureBO getDisclosure(TYPES type){
        DisclosureBO disclosureBO= new DisclosureBO();
        PreparedStatement pst= null;
        try {
            pst= getPst(GET_DISCLOSURE);
            pst.setInt(1, type.type);
            
            ResultSet rs= pst.executeQuery();
            if(rs!=null&&rs.next()){
                disclosureBO.setType(rs.getInt("TYPE"));
                disclosureBO.setTextTop(rs.getString("TEXT_TOP"));
                disclosureBO.setRegularExpression(rs.getString("REGULAR_EXPRESSION"));
                disclosureBO.setPreview(rs.getString("PREVIEW"));
                disclosureBO.setTextBottom(rs.getString("TEXT_BOTTOM"));
                if(type != TYPES.RENTA_FIJA){
                    disclosureBO.setExcelIni(getExcel(1, type));
                    disclosureBO.setExcelFin(getExcel(2, type));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(pst!=null){
                try {
                    pst.close();
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
        return disclosureBO;
    }
}