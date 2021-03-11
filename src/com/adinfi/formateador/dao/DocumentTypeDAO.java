/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.dao;

import com.adinfi.formateador.bos.DocumentPublishProfileBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.datacontract.schemas._2004._07.VCB_Analisis_Data.DBResult;
import org.tempuri.IDataProxy;

/**
 *
 * @authors Josué Sanchez, Carlos Felix
 */
public class DocumentTypeDAO {

    public Exception exception;
    public String isIDMiVector;

    Exception ex;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getIsIDMiVector() {
        return isIDMiVector;
    }

    public void setIsIDMiVector(String isIDMiVector) {
        this.isIDMiVector = isIDMiVector;
    }

    public List<DocumentTypeBO> get(String pattern, int pattern2, int collaborative) {
        List<DocumentTypeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeBO>> h = new BeanListHandler<>(DocumentTypeBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, null, null, null, null, null, null, null, null, null, null, null, null, collaborative, null, null, null, pattern, pattern2, null, null, null, null};
            list = (List<DocumentTypeBO>) run.query(conn, SQL_QUERY, h, obj);
            if (list != null) {
                for (DocumentTypeBO type : list) {
                    type.setTemplates(getTemplateByDocumentTypeId(type.getIddocument_type()));
                }
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public List<DocumentTypeBO> get(int idDocumentType) {
        List<DocumentTypeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection()) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeBO>> h = new BeanListHandler<>(DocumentTypeBO.class);
            Object[] obj = new Object[]{GlobalDefines.SELECT_ACTION, idDocumentType, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -1, null, null, null, null};
            list = (List<DocumentTypeBO>) run.query(conn, SQL_QUERY, h, obj);
            if (list != null) {
                for (DocumentTypeBO type : list) {
                    type.setTemplates(getTemplateByDocumentTypeId(type.getIddocument_type()));
                }
            }
        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return list;
    }

    public List<DocumentTypeBO> getDocTypeByMarket(int collaborative, int idMarket) {
        List<DocumentTypeBO> list = new ArrayList<>();

        try (
                Connection conn = ConnectionDB.getInstance().getConnection();
                Statement st = conn.createStatement();) {

            String query = "SELECT IDDOCUMENT_TYPE, NAME, IDMARKET, NOMENCLATURE, IDOUTGOINGEMAIL, IDPUBLISHPROFILE, IDDISCLOSURE, "
                    + "SENDMEDIA,  PDF, SENDEMAIL, HTML,PUBLISH,COLLABORATIVE, SUBTITLE, TITLE, SUBJECT "
                    + "FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TYPE WHERE COLLABORATIVE = " + collaborative + " AND IDMARKET = " + idMarket;

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                DocumentTypeBO bo = new DocumentTypeBO();
                bo.setIddocument_type(rs.getInt("IDDOCUMENT_TYPE"));
                bo.setName(rs.getString("NAME"));
                bo.setIdMarket(rs.getInt("IDMARKET"));
                bo.setNomenclature(rs.getString("NOMENCLATURE"));
                bo.setIdOutgoingEmail(rs.getInt("IDOUTGOINGEMAIL"));
                bo.setIdPublishProfile(rs.getInt("IDPUBLISHPROFILE"));
                bo.setIdDisclosure(rs.getInt("IDDISCLOSURE"));
                bo.setSendMedia(rs.getInt("SENDMEDIA") == 1);
                bo.setPdf(rs.getInt("PDF") == 1);
                bo.setSendEmail(rs.getInt("SENDEMAIL") == 1);
                bo.setHtml(rs.getInt("HTML") == 1);
                bo.setHtml(rs.getInt("PUBLISH") == 1);
                bo.setCollaborative(rs.getInt("COLLABORATIVE") == 1);
                bo.setSubTitle(rs.getInt("SUBTITLE") == 1);
                bo.setTitle(rs.getInt("TITLE") == 1);
                bo.setSubject(rs.getInt("SUBJECT") == 1);
                list.add(bo);
            }
        } catch (SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        return list;
    }

    public int insertUpdate(DocumentTypeBO bo) {

        /* Llamado al WebService */
        /*WS de nuevo documento*/
        try {
            String ipAddress = Utilerias.getIP();
            StringBuilder perfiles = new StringBuilder("<Perfiles>");
            StringBuilder tipoContratos = new StringBuilder("<TipoContratos>");

            IDataProxy proxy = new IDataProxy(Utilerias.getProperty(ApplicationProperties.ANALISIS_WS));

            for (Integer idP : bo.getProfilesPublish()) {
                perfiles.append("<Perfil>");
                perfiles.append(idP.toString());
                perfiles.append("</Perfil>");
            }
            perfiles.append("</Perfiles>");

            for (String id : bo.getContracts()) {
                tipoContratos.append("<TipoContrato>");
                tipoContratos.append(id);
                tipoContratos.append("</TipoContrato>");
            }
            tipoContratos.append("</TipoContratos>");

            DBResult result = null;
            if (bo.getIddocument_type() == 0) {
                result = proxy.nuevoDocumento(bo.getName(), bo.getIdMarket(), -1, bo.getEmail(), perfiles.toString(), tipoContratos.toString(), InstanceContext.getInstance().getUsuario().getUsuarioId(), ipAddress);

            } else {
                result = proxy.actualizaDocumento(Integer.valueOf(bo.getIddocument_type_vector()), bo.getName(), bo.getIdMarket(), -1, bo.getEmail(), perfiles.toString(), tipoContratos.toString(), InstanceContext.getInstance().getUsuario().getUsuarioId(), ipAddress);
                int t = 6;
            }
            if (result == null || result.getResultCd() == null || !(result.getResultCd().equals("000") || result.getResultCd().equals("002"))) {
                exception = new Exception(result == null ? "Error en servicio web" : result.getResultMsg());
                return -1;
            }

            String message = result.getResultMsg();
            String resultCode = result.getResultCd();

            if ((message != null) && (!message.equals("OK")) && (resultCode.equals("000"))) {
                String ids[] = UtileriasWS.getIDSMarketMiVector(message);
                //id que tiene vector como identificador de este tipo de documento
                bo.setIddocument_type_vector(ids[0]);
                //idMiVector
                bo.setIdMiVector(ids[1]);
                this.setIsIDMiVector("");
            } else {
                // si el mensaje regresa error, dejamos los valores insertados anteriormente.
                if ((!bo.getIddocument_type_vector().isEmpty()) && (!bo.getIdMiVector().isEmpty())) {
                    bo.setIddocument_type_vector(bo.getIddocument_type_vector());
                    bo.setIdMiVector(bo.getIdMiVector());
                    this.setIsIDMiVector("");
                } else {
                    // no es update y no regreso un id mi vector eliminamos y enviamos msj al usuario.
                    ex = new Exception("No cuenta con idMiVector");
                    this.setException(ex);

                    this.setIsIDMiVector("No cuenta con idMiVector");
                }

            }
            //}

            //} catch (RemoteException ex) {
        } catch (NumberFormatException | RemoteException ex) {
            JOptionPane.showMessageDialog(null, "El servicio para manejar documentos no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }

        List<DocumentTypeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeBO>> h = new BeanListHandler<>(DocumentTypeBO.class);
            Object[] obj = new Object[]{GlobalDefines.INSERT_UPDATE_ACTION, bo.getIddocument_type(), bo.getName(), bo.getIdMarket(), bo.getNomenclature(), bo.getIdOutgoingEmail(), bo.getIdPublishProfile(), bo.getIdDisclosure(), bo.isSendMedia(), bo.isPdf(), bo.isPublish(), bo.isSendEmail(), bo.isHtml(), bo.isCollaborative(), bo.isSubTitle(), bo.isTitle(), bo.isSubject(), null, null, bo.getIdMiVector(), bo.getTitle_regex(), bo.getTitle_regex_decoded(), bo.getIddocument_type_vector()};
            list = (List<DocumentTypeBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        int documentTypeId = list == null || list.isEmpty() == true ? -1 : list.get(StatementConstant.SC_0.get()).getIddocument_type();
        bo.setIddocument_type(documentTypeId);
        insertUpdateTemplate(bo);
        insertUpdateProfiles(bo);
        insertUpdateContracts(bo);
        insertUpdatePublishProfiles(bo);
        return documentTypeId;
    }

    public boolean delete(DocumentTypeBO bo) {
        boolean b = false;
        List<DocumentTypeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeBO>> h = new BeanListHandler<>(DocumentTypeBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION, bo.getIddocument_type(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
            list = (List<DocumentTypeBO>) run.query(conn, SQL_QUERY, h, obj);
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
                b = delete(new DocumentTypeBO(o));
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }
        return b;
    }

    private static final String SQL_QUERY
            = "{ CALL " + GlobalDefines.DB_SCHEMA + "SPDOCUMENTTYPE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String INSERT_TEMPLATE = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TEMPLATE (DOCUMENT_ID,TEMPLATE_ID,DATEMODIFY) VALUES(?,?,?)";
    private static final String DELETE_TEMPLATE = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TEMPLATE WHERE DOCUMENT_ID=x_x AND TEMPLATE_ID NOT IN(y_y)";
    private static final String DELETE_TEMPLATES = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TEMPLATE WHERE TEMPLATE_ID = ? ";
    private static final String SELECT_TEMPLATE = "SELECT IDDOCUMENT_TEMPLATE, DOCUMENT_ID, TEMPLATE_ID, DATEMODIFY, STATUS FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TEMPLATE WHERE DOCUMENT_ID= ?";

    private static final String SELECT_DOCUMENT = "SELECT DOCUMENT_ID FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_TEMPLATE E WHERE E.TEMPLATE_ID=?";

    private static final String INSERT_CONTRACT = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONTRACT (DOCUMENT_ID,IDCONTRACT,DATEMODIFY) VALUES(?,?,?)";
    private static final String DELETE_CONTRACT = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONTRACT WHERE DOCUMENT_ID=x_x AND IDCONTRACT NOT IN(y_y)";
    private static final String SELECT_CONTRACT = "SELECT IDDOCUMENT_CONTRACT,DOCUMENT_ID,IDCONTRACT,DATEMODIFY,STATUS FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_CONTRACT WHERE DOCUMENT_ID= ?";

    private static final String INSERT_PROFILE = "INSERT INTO " + GlobalDefines.DB_SCHEMA + "DOCUMENT_PROFILE (DOCUMENT_ID,IDPROFILE,DATEMODIFY) VALUES(?,?,?)";
    private static final String DELETE_PROFILE = "DELETE FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_PROFILE WHERE DOCUMENT_ID=x_x AND IDPROFILE NOT IN(y_y)";
    private static final String SELECT_PROFILE = "SELECT IDDOCUMENT_PROFILE,DOCUMENT_ID,IDPROFILE,DATEMODIFY,STATUS FROM " + GlobalDefines.DB_SCHEMA + "DOCUMENT_PROFILE WHERE DOCUMENT_ID= ?";
    private Connection conn = null;

    private PreparedStatement getPst(String query) throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = ConnectionDB.getInstance().getConnection();
        }
        PreparedStatement pst = conn.prepareStatement(query);
        return pst;
    }

    private java.sql.Date getSqlDateNow() {
        java.util.Date date = new java.util.Date();
        Date dateSql = new Date(date.getTime());
        return dateSql;
    }

    public Boolean insertUpdateTemplate(DocumentTypeBO bo) {
        if (bo.getTemplates() == null || bo.getIddocument_type() <= 0) {
            return Boolean.TRUE;
        }
        PreparedStatement pst = null;
        try {
            StringBuilder str = new StringBuilder();
            for (Integer templateId : bo.getTemplates()) {
                str.append(templateId);
                str.append(",");
            }
            List<Integer> lstTemplateExist = new ArrayList<>();
            pst = getPst(SELECT_TEMPLATE);
            pst.setInt(1, bo.getIddocument_type());
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    lstTemplateExist.add(rs.getInt("TEMPLATE_ID"));
                }
            }
            bo.getTemplates().removeAll(lstTemplateExist);
            for (Integer templateId : bo.getTemplates()) {
                pst = getPst(INSERT_TEMPLATE);
                pst.setInt(1, bo.getIddocument_type());
                pst.setInt(2, templateId);
                pst.setDate(3, getSqlDateNow());
                pst.executeUpdate();
            }

            if (str.length() > 0) {
                str.deleteCharAt(str.length() - 1);
                String query = DELETE_TEMPLATE.replaceAll("x_x", "" + bo.getIddocument_type());
                query = query.replaceAll("y_y", str.toString());
                pst = getPst(query);
                pst.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
            return Boolean.FALSE;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return Boolean.TRUE;
    }

    public Boolean insertUpdateContracts(DocumentTypeBO bo) {
        if (bo.getContracts() == null || bo.getIddocument_type() <= 0) {
            return Boolean.TRUE;
        }
        PreparedStatement pst = null;
        try {
            StringBuilder str = new StringBuilder();
            for (String contractId : bo.getContracts()) {
                str.append("'");
                str.append(contractId);
                str.append("',");
            }
            List<String> lstContractInit = new ArrayList<>();
            pst = getPst(SELECT_CONTRACT);
            pst.setInt(1, bo.getIddocument_type());
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    lstContractInit.add(rs.getString("IDCONTRACT"));
                }
            }
            bo.getContracts().removeAll(lstContractInit);
            for (String contractId : bo.getContracts()) {
                pst = getPst(INSERT_CONTRACT);
                pst.setInt(1, bo.getIddocument_type());
                pst.setString(2, contractId);
                pst.setDate(3, getSqlDateNow());
                pst.executeUpdate();
            }

            if (str.length() > 0) {
                str.deleteCharAt(str.length() - 1);
                String query = DELETE_CONTRACT.replaceAll("x_x", "" + bo.getIddocument_type());
                query = query.replaceAll("y_y", str.toString());
                pst = getPst(query);
                pst.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
            return Boolean.FALSE;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return Boolean.TRUE;
    }

    private void insertUpdatePublishProfiles(DocumentTypeBO bo) {

        DocumentPublishProfileBO boPP = new DocumentPublishProfileBO();
        DocumentPublishProfileDAO daoPP = new DocumentPublishProfileDAO();

        int idDocumentType = bo.getIddocument_type();
        daoPP.delete(idDocumentType);

        List<Integer> list = bo.getProfilesPublish();
        
        for (Integer l : list) {
            boPP.setIddocument_type(idDocumentType);
            boPP.setIdpublishprofile(l);
            daoPP.insert(boPP);
        }

    }

    public Boolean insertUpdateProfiles(DocumentTypeBO bo) {
        if (bo.getProfiles() == null || bo.getIddocument_type() <= 0) {
            return Boolean.TRUE;
        }
        PreparedStatement pst = null;
        try {
            StringBuilder str = new StringBuilder();
            for (Integer profileId : bo.getProfiles()) {
                str.append(profileId);
                str.append(",");
            }
            List<Integer> lstProfileInit = new ArrayList<>();
            pst = getPst(SELECT_PROFILE);
            pst.setInt(1, bo.getIddocument_type());
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    lstProfileInit.add(rs.getInt("IDPROFILE"));
                }
            }
            bo.getProfiles().removeAll(lstProfileInit);
            for (Integer profileId : bo.getProfiles()) {
                pst = getPst(INSERT_PROFILE);
                pst.setInt(1, bo.getIddocument_type());
                pst.setInt(2, profileId);
                pst.setDate(3, getSqlDateNow());
                pst.executeUpdate();
            }
            if (str.length() > 0) {
                str.deleteCharAt(str.length() - 1);
                String query = DELETE_PROFILE.replaceAll("x_x", "" + bo.getIddocument_type());
                query = query.replaceAll("y_y", str.toString());
                pst = getPst(query);
                pst.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
            return Boolean.FALSE;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return Boolean.TRUE;
    }

    public List<Integer> getTemplateByDocumentTypeId(int documentTypeId) {
        List<Integer> templateLst = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            pst = getPst(SELECT_TEMPLATE);
            pst.setInt(1, documentTypeId);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    templateLst.add(rs.getInt("TEMPLATE_ID"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return templateLst;
    }

    public List<String> getContractByDocumentTypeId(int documentTypeId) {
        List<String> contractLst = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            pst = getPst(SELECT_CONTRACT);
            pst.setInt(1, documentTypeId);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    contractLst.add(rs.getString("IDCONTRACT"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return contractLst;
    }

    public List<Integer> getProfileByDocumentTypeId(int documentTypeId) {
        List<Integer> profileLst = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            pst = getPst(SELECT_PROFILE);
            pst.setInt(1, documentTypeId);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    profileLst.add(rs.getInt("IDPROFILE"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return profileLst;
    }

    public void linkTemplatetoDocumentType(int idTemplate, int idDocumentType) {
        PreparedStatement pst = null;
        try {
            pst = getPst(INSERT_TEMPLATE);

            pst.setInt(1, idDocumentType);
            pst.setInt(2, idTemplate);
            pst.setDate(3, getSqlDateNow());
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void deleteTemplateWithDocumentType(int idTemplate) {
        PreparedStatement pst = null;
        try {
            pst = getPst(DELETE_TEMPLATES);

            pst.setInt(1, idTemplate);
            
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<Integer> getDocumentTypeByTemplateId(int templateId) {
        List<Integer> templateLst = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            pst = getPst(SELECT_DOCUMENT);
            pst.setInt(1, templateId);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    templateLst.add(rs.getInt("DOCUMENT_ID"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
            exception = ex;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DisclosureDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return templateLst;
    }

    public void deleteHard(int id) {

        DocumentTypeBO bo = new DocumentTypeBO();
        bo.setIddocument_type(id);

        List<DocumentTypeBO> list = null;
        exception = null;
        try (Connection conn = ConnectionDB.getInstance().getConnection();) {
            QueryRunner run = new QueryRunner();
            ResultSetHandler<List<DocumentTypeBO>> h = new BeanListHandler<>(DocumentTypeBO.class);
            Object[] obj = new Object[]{GlobalDefines.DELETE_ACTION_HARD, bo.getIddocument_type(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
            list = (List<DocumentTypeBO>) run.query(conn, SQL_QUERY, h, obj);

        } catch (SQLException e) {
            Utilerias.logger(getClass()).info(e);
            exception = e;
        }

    }

}
