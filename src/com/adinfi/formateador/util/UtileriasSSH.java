/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import com.adinfi.formateador.bos.DocumentBO;
import com.adinfi.formateador.bos.DocumentCollabBO;
import com.adinfi.formateador.bos.DocumentCollabItemBO;
import com.adinfi.formateador.bos.DocumentFormatBO;
import com.adinfi.formateador.bos.DocumentTypeBO;
import com.adinfi.formateador.bos.DsAutor;
import com.adinfi.formateador.bos.DsCanal;
import com.adinfi.formateador.bos.DsInputDoc;
import com.adinfi.formateador.bos.DsInputListaDist;
import com.adinfi.formateador.bos.FilePublishAttach;
import com.adinfi.formateador.bos.IndustryBO;
import com.adinfi.formateador.bos.InputDoc;
import com.adinfi.formateador.bos.InputListaDist;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.MarketBO;
import com.adinfi.formateador.bos.PublishStatusBO;
import com.adinfi.formateador.bos.SendPublishAuthorsBO;
import com.adinfi.formateador.bos.SendPublishBO;
import com.adinfi.formateador.bos.SubjectBO;
import com.adinfi.formateador.cm.ExportCM;
import com.adinfi.formateador.dao.CollaborativesDAO;
import com.adinfi.formateador.dao.DocumentDAO;
import com.adinfi.formateador.dao.DocumentTypeDAO;
import com.adinfi.formateador.dao.IndustryDAO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.MarketDAO;
import com.adinfi.formateador.dao.PublishStatusDAO;
import com.adinfi.formateador.dao.SendPublishAuthorsDAO;
import com.adinfi.formateador.dao.SendPublishDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.dao.SubjectDAO;
import com.adinfi.formateador.editor.HTMLDocument;
import com.adinfi.formateador.editor.PdfDocument;
import com.adinfi.formateador.view.publish.SendPublish;
import com.adinfi.ws.analisisws.publicador.CanalDistribucion;
import com.itextpdf.text.DocumentException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.RemoteException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialClob;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author USUARIO
 */
public class UtileriasSSH {

    private static final boolean DISABLE_STRICT_HOST_KEY = true;
    private static UtileriasSSH instance;
    private Exception exception;
    private InputDoc autores;
    private InputListaDist listDist;
    private String fechaProgress;

    public static synchronized UtileriasSSH getInstance() {
        if (instance == null) {
            instance = new UtileriasSSH();
        }
        return instance;
    }

    private UtileriasSSH() {
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Hashtable<String, String> createDirectories(String dir1, String dir2) {
        Hashtable<String, String> paths = new Hashtable<>();
        Session session_ = null;
        try {
            //1. Obtener Session 
            session_ = getSession();
            //2. Ejecutar comandos para creación de directorios
            if (session_ != null) {

                String parentDirectory = "";
                String docs = "";
                String temp = "";
                String fileSeparator = "";
                List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY).get(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    parentDirectory = list.get(StatementConstant.SC_0.get());
                }

                list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY).get(Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    docs = list.get(StatementConstant.SC_0.get());
                }

                list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY).get(Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    temp = list.get(StatementConstant.SC_0.get());
                }

                list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR).get(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    fileSeparator = list.get(StatementConstant.SC_0.get());
                }

                String productionDirectory = String.format("%s%s%s%s", parentDirectory, docs, fileSeparator, dir1);
                String tempDirectory = String.format("%s%s%s%s", parentDirectory, temp, fileSeparator, dir2);

                boolean succeessProductionDir = mkdir(session_, productionDirectory);
                boolean succeessTempDir = mkdir(session_, tempDirectory);

                if (succeessProductionDir == true) {
                    paths.put(Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY.toString(), productionDirectory);
                }

                if (succeessTempDir == true) {
                    paths.put(Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY.toString(), tempDirectory);
                }
            }
        } finally {
            if (session_ != null) {
                session_.disconnect();
            }
        }
        return paths;
    }
    
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Hashtable<String, String> deleteFileServer(String dir1, String file) {
        Hashtable<String, String> paths = new Hashtable<>();
        Session session_ = null;
        try {
            //1. Obtener Session 
            session_ = getSession();
            //2. Ejecutar comandos para eliminacion de archivo
            if (session_ != null) {

                String parentDirectory = "";
                String fileSeparator = "";
                List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY).get(Utilerias.ALLOWED_KEY.SSH_PARENT_DIRECTORY.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    parentDirectory = list.get(StatementConstant.SC_0.get());
                }

                list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR).get(Utilerias.ALLOWED_KEY.SERVER_FILE_SEPARATOR.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    fileSeparator = list.get(StatementConstant.SC_0.get());
                }

                String productionDirectory = String.format("%s%s%s", parentDirectory, dir1, fileSeparator);

                boolean succeessProductionDir = delete(session_, productionDirectory, file);

                if (succeessProductionDir == true) {
                    paths.put(Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY.toString(), productionDirectory);
                }

            }
        } finally {
            if (session_ != null) {
                session_.disconnect();
            }
        }
        return paths;
    }

    private Session getSession() {
        Session session_ = null;
        List<String> list = null;
        String user = "";
        String pass = "";
        String host = "";
        String portStr = "";
        String timeoutStr = "";
        int port = GlobalDefines.SSH_DEFAULT_PORT;
        int timeout = GlobalDefines.SSH_DEFAULT_TIMEOUT;

        list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_HOST) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_HOST).get(Utilerias.ALLOWED_KEY.SSH_HOST.toString())
                : null;
        if (list != null && list.isEmpty() == false) {
            host = list.get(StatementConstant.SC_0.get());
        }

        list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_USER) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_USER).get(Utilerias.ALLOWED_KEY.SSH_USER.toString())
                : null;
        if (list != null && list.isEmpty() == false) {
            user = list.get(StatementConstant.SC_0.get());
        }

        list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PASS) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PASS).get(Utilerias.ALLOWED_KEY.SSH_PASS.toString())
                : null;
        if (list != null && list.isEmpty() == false) {
            pass = list.get(StatementConstant.SC_0.get());
        }

        list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PORT) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_PORT).get(Utilerias.ALLOWED_KEY.SSH_PORT.toString())
                : null;
        if (list != null && list.isEmpty() == false) {
            portStr = list.get(StatementConstant.SC_0.get());
        }

        list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_TIMEOUT) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.SSH_TIMEOUT).get(Utilerias.ALLOWED_KEY.SSH_TIMEOUT.toString())
                : null;
        if (list != null && list.isEmpty() == false) {
            timeoutStr = list.get(StatementConstant.SC_0.get());
        }

        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException ex) {
        }

        try {
            timeout = Integer.parseInt(timeoutStr);
        } catch (NumberFormatException ex) {
        }

        try {
            JSch jSch = new JSch();
            session_ = jSch.getSession(user, host, port);
            session_.setPassword(pass);

            if (DISABLE_STRICT_HOST_KEY) {
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session_.setConfig(config);
            }
            session_.setTimeout(timeout);
            //session_.connect();
        } catch (JSchException ex) {
            session_ = null;
            exception = ex;
            Utilerias.logger(UtileriasSSH.class).info(ex);
        }
        return session_;
    }

    private boolean mkdir(Session session_, String directory) {
        boolean success = false;
        try {
            String command = String.format("%s%s", "mkdir -p ", directory);

            Channel channel = session_.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    Utilerias.logger(UtileriasSSH.class).info(" Exit-status: " + channel.getExitStatus());
                    success = true;
                    break;
                }
            }
            channel.disconnect();
        } catch (JSchException | IOException ex) {
            success = false;
            exception = ex;
            Utilerias.logger(UtileriasSSH.class).info(ex);
        }
        return success;
    }
    
    private boolean delete(Session session_, String directory, String file) {
        boolean success = false;
        try {
            String command = String.format("%s%s%s", "rm -f ", directory, file);
            System.out.println(command);
            Channel channel = session_.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    Utilerias.logger(UtileriasSSH.class).info(" Exit-status: " + channel.getExitStatus());
                    success = true;
                    break;
                }
            }
            channel.disconnect();
        } catch (JSchException | IOException ex) {
            success = false;
            exception = ex;
            Utilerias.logger(UtileriasSSH.class).info(ex);
        }
        return success;
    }

    public List<String> transferFiles(
            Hashtable<String, String> rootDir,
            List<File> localFiles,
            Utilerias.ALLOWED_KEY directoryType) {

        List<String> remotePaths = null;
        Session session_ = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        try {
            if (localFiles == null) {
                return remotePaths;
            }

            if (rootDir == null) {
                return remotePaths;
            }

            //1. Obtener Session 
            session_ = UtileriasSSH.getInstance().getSession();
            //2. Ejecutar comandos para copiar archivos locales a servidor remoto
            channel = session_.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            String sftpworkingdir = rootDir.get(directoryType.toString());
                        
            for (File currentFile : localFiles) {
                channelSftp.cd(sftpworkingdir);
                channelSftp.put(new FileInputStream(currentFile), currentFile.getName());
            }

        } catch (JSchException | SftpException e) {
            Utilerias.logger(UtileriasSSH.class).info(e);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UtileriasSSH.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (session_ != null) {
                session_.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
        }
        return remotePaths;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    public static void main(String[] args) {
        // 1. Gnerar las rutas de la forma 
        /*
            + Docs (REPOSITORIO DE DOCUMENTOS EN PRODUCCION) //Esta configurado en base de datos ya sabe que es el root
                +- AÑO (AAAA)
                    +- MES (MM)
                        +- IDIOMA (NOMENCLATURA DE IDIOMA)
                            +- MERCADO (NOMENCLATURA DE MERCADO)
                                +- TIPO_DOCUMENTO (NOMENCLATURA DE  TIPO DOCUMENTO)
                                    +- IMG (REPOSITORIO DE IMÁGENES DE LAS PUBLICACIONES)
                                    -- AUDIOS (REPOSITORIO DE AUDIOS DE LAS PUBLICACIONES)
                                    -- HTMLS (NOMBRE DE ARCHIVO EN FORMATO GUI)
                                    -- PDFS   (NOMBRE DE ARCHIVO EN FORMATO GUI)
        
        En base al documento ver archivo SendPublish linea 1579
        
        La fecha de publicacion es en SendBO {AÑO, MES}
        Idioma = DocumentBO
        Mercado = DocumentBO
        Tipo Documento = DocumentBO
        
        -----
        IMG
        AUDIOS
        HTMLS
        PDF 
        -----
        Son la lista de objetos que hay que regresar cuando se le da clic en PDF y HTML 
        
        
        */
        
        
        // Conectar y crear archivos si no existen 
//        Hashtable<String, String> table = UtileriasSSH.getInstance().createDirectories("Hola/Mundo/Adios", "a/b/c");
//        System.out.println(table);
//        //Copiar archivos de publicación desde el cliente al servidor.
//        List<File> localFiles = new ArrayList<>();
//        localFiles.add(new File("C:\\Users\\USUARIO\\Documents\\AdinfiSoftware\\VEC-413-Formateador\\VEC-413-Formateador\\trunk\\files\\html\\_generate\\html_1404426591347.html"));

        
        // Aqui se mandan dos constantes al final del métod para indicar si se guardan documentos en Docs o en TEMP
        /*
            SSH_ROOT_DOCS_DIRECTORY("ssh.root.docs.directory"),
            SSH_ROOT_TEMP_DIRECTORY("ssh.root.temp.directory"),
        */
//        UtileriasSSH.getInstance().transferFiles(table, localFiles, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
        UtileriasSSH.getInstance().sendHeaderOrFooter(new File("C:\\Users\\vectoran\\Pictures\\bullet.png"), false);
    }
    
    public boolean sendHeaderOrFooter(File file, boolean isHeader){
        if(isHeader){
            try {
                File f = new File(file.getParentFile() + System.getProperty("file.separator") + "header.png");
                
                BufferedImage img = ImageIO.read(file);
                ImageIO.write(img, "png", f);
                
                sendFileSsh(f, "img");
            } catch (IOException ex) {
                Logger.getLogger(UtileriasSSH.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                File f = new File(file.getParentFile() + System.getProperty("file.separator") + "footer.png");
                
                BufferedImage img = ImageIO.read(file);
                ImageIO.write(img, "png", f);
                
                sendFileSsh(f, "img");
            } catch (IOException ex) {
                Logger.getLogger(UtileriasSSH.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
    
    public boolean sendFileSsh(File fileToSend, String dirInAnalisis){
        
        try {
            List<File> files = new ArrayList<File>();
            files.add(fileToSend);
            Hashtable<String, String> dir = deleteFileServer(dirInAnalisis, fileToSend.getName());
            UtileriasSSH.getInstance().transferFiles(dir, files, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            return false;
        }
        
        return true;
    }
    
    private void copyFile(String origen, String destino) throws IOException {
        Path FROM = Paths.get(origen);
        Path TO = Paths.get(destino);
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        Files.copy(FROM, TO, options);
    }
    
    public List<FilePublishAttach> sendFilesAttachSsh(SendPublishBO sendBO, DocumentBO docBO, String fecha, List<File> localFiles, boolean isPublishAttach, int secuencia, InputDoc autores, InputListaDist listaDist, String fechaProgress){
        this.autores = autores; 
        this.listDist = listaDist;
        this.fechaProgress = fechaProgress;
        return sendFilesAttachSsh(sendBO, docBO, fecha, localFiles, isPublishAttach, secuencia);
    }
    
    public List<FilePublishAttach> sendFilesAttachSsh(SendPublishBO sendBO, DocumentBO docBO, String fecha, List<File> localFiles, boolean isPublishAttach, int secuencia){
        List<FilePublishAttach> retVal = new ArrayList<>();
        
        List<File> resFile = new ArrayList<>();
        int i = secuencia;
        for (File locfile : localFiles) {
            try {
                i++;
                String unico = "_" + i + "_";
                String destino = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR) + sendBO.getIdDocument_send() + unico + "ATTACH." + FilenameUtils.getExtension(locfile.getName());//locfile.getName();
                copyFile(locfile.getPath(), destino);
                resFile.add(new File(destino));
            } catch (Exception e) {
                Utilerias.logger(getClass()).error(e);
            }
        }
        
        String[] vfecha=fecha.split("/");
        String ruta = vfecha[2] + "/" + vfecha[1] + "/ADJ";
        Hashtable<String, String> tb = UtileriasSSH.getInstance().createDirectories(ruta, ruta);
        UtileriasSSH.getInstance().transferFiles(tb, resFile, Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY);
        
        String rutaPrefix = "http://www.vectoronline.com.mx/analisis/Docs/";
                
        List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS) != null
                ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS).get(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS.toString())
                : Collections.emptyList();
        if (list.isEmpty() == false) {
            rutaPrefix = list.get(StatementConstant.SC_0.get());
        }
        
        rutaPrefix += ruta + "/";
        
        String rutaContent = null;
        List<String> lstFileContent = new ArrayList<>();
        for (File file : resFile) {
            if(rutaContent == null)
                rutaContent = rutaPrefix + file.getName();
            
            retVal.add( new FilePublishAttach(file.getName(), FilenameUtils.getExtension(file.getName()), rutaPrefix + file.getName()) );
            lstFileContent.add(file.getPath());
        }
        
        if(isPublishAttach){
            //importToContent(sendBO, docBO, lstFileContent, rutaContent, fecha, isPublishAttach, rutaContent);
            importToProgress(sendBO, docBO, lstFileContent, rutaContent, fecha, isPublishAttach, rutaContent);
        }
        
        return retVal;
    }
    
    public String sendFilesSsh(SendPublishBO sendBO, DocumentBO docBO, String fecha, Utilerias.ALLOWED_KEY type, InputDoc autores, InputListaDist listaDist, String fechaProgress){
        this.autores = autores; 
        this.listDist = listaDist;
        this.fechaProgress = fechaProgress;
        return sendFilesSsh(sendBO, docBO, fecha, type);
    }
    
    public String sendFilesSsh(SendPublishBO sendBO, DocumentBO docBO, String fecha, Utilerias.ALLOWED_KEY type){
        //List<String> sshFiles = getSshFiles(docBO);
        
        DocumentTypeDAO dDAO = new DocumentTypeDAO();
        List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);
        String nomDoctype="";
        String DocType="";
        for(DocumentTypeBO d: dBO){
            if(d.getIddocument_type()==docBO.getIdDocType()){
                
                if(d.isHtml()){
                    nomDoctype=d.getNomenclature();
                    DocType=d.getName();
                    break;
                }else{
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Utilerias.showMessage(null, "No es posible generar Documento HTML para este tipo de Documento", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                Utilerias.logger(getClass()).error(ex);
                            }
                        }
                    });
                    return null;
                }
            }
        }
        
        LanguageDAO lDAO=new LanguageDAO();
        List<LanguageBO> lBO = lDAO.get(null);
        String nomLanguage="";
        String language="";
        for(LanguageBO l: lBO){
            if(l.getIdLanguage()==docBO.getIdLanguage()){
                nomLanguage=l.getNomenclature();
                language=l.getName();
                break;
            }
        }

        MarketDAO mDAO = new MarketDAO();
        List<MarketBO> mBO = mDAO.get(null);
        String nomMarket = "";
        String market = "";
        for(MarketBO m: mBO){
            m.setIdMarket(Integer.parseInt(m.getIdMiVector_real()));
            if(m.getIdMarket() == docBO.getIdMarket()){
                nomMarket = m.getNomenclature();
                market = m.getName();
                break;
            }
        }

        String[] vfecha=fecha.split("/");

        int numEnvioDoc = docBO.getDocumentId();
        
        String ruta = vfecha[2] + "/" + vfecha[1] + "/" + nomLanguage + "/" + nomMarket + "/" + nomDoctype + "/" + numEnvioDoc;
        String rutaImg = vfecha[2] + "/" + vfecha[1] + "/" + nomLanguage + "/" + nomMarket + "/" + nomDoctype + "/" + numEnvioDoc + "/IMG";

        //TODO 
        Hashtable<String, String> tb = UtileriasSSH.getInstance().createDirectories(ruta, ruta);
        Hashtable<String, String> tb_img = UtileriasSSH.getInstance().createDirectories(rutaImg, rutaImg);
        //System.out.println(table);
        //Copiar archivos de publicación desde el cliente al servidor.
        List<File> localFiles = new ArrayList<>();
        List<File> localFilesImg = new ArrayList<>();
        
        List<String> sshFiles = docBO.isCollaborative() && GlobalDefines.COLLAB_TYPE_DOC.equals(docBO.getCollaborativeType()) ? getSshCollabFiles(docBO, fecha, type) : getSshFiles(docBO, type, null);
        String retValCustom = "";
        Utilerias.pasarGarbageCollector();
        for(String file: sshFiles){
            System.out.println(file);
            if(Utilerias.isFileImage(file)){
                localFilesImg.add(new File(file));
            }else{
                localFiles.add(new File(file));
                retValCustom = file;
            }
        }

        //TODO
        if(type==Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY){
            deleteFileServer( "Docs/" + ruta, "*.*");
            deleteFileServer( "Docs/" + rutaImg, "*.*");
        }else if(docBO.getDocumentId() > 0){
                deleteFileServer( "TEMP/" + ruta, "*.*");
                deleteFileServer( "TEMP/" + rutaImg, "*.*");
        }
        Utilerias.pasarGarbageCollector();
        UtileriasSSH.getInstance().transferFiles(tb, localFiles, type);
        UtileriasSSH.getInstance().transferFiles(tb_img, localFilesImg, type);
        
        String retVal="";
        String pdfPub = "";
        if(sshFiles.size() <= 0){
            retVal = "ERROR";
        }else{
            if(type==Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY){
                retVal = "http://www.vectoronline.com.mx/analisis/Docs/";
                
                List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS).get(Utilerias.ALLOWED_KEY.PUBLISH_URL_DOCS.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    retVal = list.get(StatementConstant.SC_0.get());
                } 
                
                try{
                    DocumentDAO docDAO = new DocumentDAO();
                    if(docDAO.getPublishDocumentName(docBO.getDocumentId()) == null){
                        String name = Utilerias.getFileName(sshFiles.get(sshFiles.size()-1));
                        docDAO.savePublishDocumentName(docBO, name);
                        docBO.setPublishName(name);
                    }
                }catch(Exception e){
                    Utilerias.logger(getClass()).error(e);
                }
                
            }else{
                retVal = "http://www.vectoronline.com.mx/analisis/TEMP/";
                
                List<String> list = Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_TEMP) != null
                        ? Utilerias.getAllowedValues(Utilerias.ALLOWED_KEY.PUBLISH_URL_TEMP).get(Utilerias.ALLOWED_KEY.PUBLISH_URL_TEMP.toString())
                        : null;
                if (list != null && list.isEmpty() == false) {
                    retVal = list.get(StatementConstant.SC_0.get());
                }  
            }
            //TODO retVal = "file:///" + sshFiles.get(sshFiles.size()-1);//+= ruta + "/" + Utilerias.getFileName(sshFiles.get(sshFiles.size()-1));
            pdfPub = retVal + ruta + "/" + Utilerias.getFileName(sshFiles.get(0));
            retVal += ruta + "/" + Utilerias.getFileName(sshFiles.get(sshFiles.size()-1));
        }
        
        Utilerias.pasarGarbageCollector();
        if(type==Utilerias.ALLOWED_KEY.SSH_ROOT_DOCS_DIRECTORY){
            
            //importToContent(sendBO, docBO, sshFiles, retVal, fecha, false, pdfPub);
            if( sendBO != null 
                    && ( sendBO.getIdStatus_publish() == 2 
                            || sendBO.getIdStatus_publish() == 3 
                            || sendBO.getIdStatus_publish() == 4 ) )
                importToProgress(sendBO, docBO, sshFiles, retVal, fecha, false, pdfPub);
            
            /*java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMddHHmmssS");
            String valorUnico = sdf.format(new Date());
            
            String fileZipGen = Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR_GENERATE) + valorUnico + ".zip";
            FileToZip fZip = new FileToZip(sshFiles, fileZipGen);
            //fZip.zipFileByMyList();
            fZip.zipFiles();
            
            SubjectDAO sDAO = new SubjectDAO();
            List<SubjectBO> sBO = sDAO.get(null);
            String subject = "";
            int industryId = 0;
            boolean isEmisora = false;
            for (SubjectBO s: sBO){
                if(docBO.getIdSubject()==s.getIdSubject()){
                    industryId = s.getIndustry();
                    isEmisora = s.isIssuing();
                    subject = s.getName();
                    break;
                }
            }
            
            IndustryDAO iDAO = new IndustryDAO();
            List<IndustryBO> iBO = iDAO.get(null);
            String industry="";
            for(IndustryBO i: iBO){
                if(industryId==i.getIdIndustry()){
                    industry=i.getName();
                    break;
                }
            }
            
            PublishStatusDAO pDAO = new PublishStatusDAO();
            List<PublishStatusBO> pBO = pDAO.get();
            String publishStatus="";
            for(PublishStatusBO p: pBO){
                if(sendBO.getIdStatus_publish()==p.getIdpublish_status()){
                    publishStatus = p.getName();
                    break;
                }
            }
            
            ArrayList<String> list = new ArrayList<String>();
            list.add("ZIP");
            list.add("PDF");
            
            DocumentFormatBO dfBO = new DocumentFormatBO();
            
            dfBO.setAuthor_name(docBO.getAuthorName());
            //dfBO.setContentmanagerEstatus();
            dfBO.setDate_publish(reorgDate(fecha));
            //dfBO.setDoc_Url(retVal);
            
            dfBO.setDocument_id(docBO.getDocumentId());
            dfBO.setDocument_type_name(DocType);
            
            dfBO.setIdDocument_send(sendBO.getIdDocument_send());
            dfBO.setIdDocument_type(docBO.getIdDocType());
            dfBO.setIdIndustry(industryId);
            dfBO.setIdLanguage(docBO.getIdLanguage());
            dfBO.setIdMarket(docBO.getIdMarket());
            dfBO.setIdStatus_publish(sendBO.getIdStatus_publish());
            
            dfBO.setIndustry_name(industry);
            dfBO.setIsEmisora(isEmisora? 1:0);
            
            dfBO.setLanguage_name(language);
            dfBO.setMarket_name(market);
            dfBO.setPublish_status_name(publishStatus);
            dfBO.setSubject_name(subject);
            
            dfBO.setUrlHtmlForm(retVal);
            dfBO.setUrlHtmlVector(fileZipGen);
            
            //dfBO.setUrlPdfForm();
            dfBO.setUrlPdfVector(sshFiles.get(0));
            
            //dfBO.setUrlType();
            dfBO.setUrlTypes(list);
            dfBO.setPid(sendBO.getPid());
            dfBO.setPidZip(sendBO.getPidzip());
            
              //ZIP, PDF
            Utilerias.pasarGarbageCollector(); 
            ExportCM e = new ExportCM();
            int succesfulConnection = e.connectToServer();
            List<String> pid = null;
            if (succesfulConnection != -1) {
                pid = e.importDocument(dfBO);
                if (pid != null) {
                    try {
                        sendBO.setPid(pid.get(0));
                        sendBO.setPidzip(pid.get(1));
                        SendPublishDAO dao = new SendPublishDAO();
                        dao.insertUpdate(sendBO);
                    } catch (Exception ex) {
                        Utilerias.logger(getClass()).error(ex);
                    }
                    Utilerias.logger(ExportCM.class).info("Documento importado");
                } else {
                    Utilerias.logger(ExportCM.class).info("Documento no importado");
                }
            } else {
                 Utilerias.logger(ExportCM.class).info("No hay Conexion a Content");
            }   */         
        }        
        //return retVal;
        return retValCustom;
    } 
    
    public boolean importToContent(SendPublishBO sendBO, DocumentBO docBO, List<String> sshFiles, String rutaPublica, String fecha, boolean isPublishAttach, String pdfPub){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMddHHmmssS");
        String valorUnico = sdf.format(new Date());

        String fileZipGen = Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR_GENERATE) + valorUnico + ".zip";
        //FileToZip fZip = new FileToZip(sshFiles, fileZipGen);
        //fZip.zipFileByMyList();
        //fZip.zipFiles();
        
        DocumentTypeDAO dDAO = new DocumentTypeDAO();
        List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);
        String DocType="";
        for(DocumentTypeBO d: dBO){
            if(d.getIddocument_type()==docBO.getIdDocType()){
                DocType=d.getName();
                break;
            }
        }
        
        LanguageDAO lDAO=new LanguageDAO();
        List<LanguageBO> lBO = lDAO.get(null);
        String language="";
        for(LanguageBO l: lBO){
            if(l.getIdLanguage()==docBO.getIdLanguage()){
                language=l.getName();
                break;
            }
        }

        MarketDAO mDAO = new MarketDAO();
        List<MarketBO> mBO = mDAO.get(null);
        String market = "";
        for(MarketBO m: mBO){
            m.setIdMarket(Integer.parseInt(m.getIdMiVector_real()));
            if(m.getIdMarket() == docBO.getIdMarket()){
                market = m.getName();
                break;
            }
        }

        SubjectDAO sDAO = new SubjectDAO();
        List<SubjectBO> sBO = sDAO.get(null);
        String subject = "";
        int industryId = 0;
        boolean isEmisora = false;
        for (SubjectBO s: sBO){
            if(docBO.getIdSubject()==s.getIdSubject()){
                industryId = s.getIndustry();
                isEmisora = s.isIssuing();
                subject = s.getName();
                break;
            }
        }

        IndustryDAO iDAO = new IndustryDAO();
        List<IndustryBO> iBO = iDAO.get(null);
        String industry="";
        for(IndustryBO i: iBO){
            if(industryId==i.getIdIndustry()){
                industry=i.getName();
                break;
            }
        }

        PublishStatusDAO pDAO = new PublishStatusDAO();
        List<PublishStatusBO> pBO = pDAO.get();
        String publishStatus="";
        for(PublishStatusBO p: pBO){
            if(sendBO.getIdStatus_publish()==p.getIdpublish_status()){
                publishStatus = p.getName();
                break;
            }
        }

        ArrayList<String> list = new ArrayList<String>();
        //list.add("ZIP");
        list.add("PDF");

        DocumentFormatBO dfBO = new DocumentFormatBO();

        dfBO.setAuthor_name(docBO.getAuthorName());
        //dfBO.setContentmanagerEstatus();
        dfBO.setDate_publish(reorgDate(fecha));
        //dfBO.setDoc_Url(retVal);

        dfBO.setDocument_id(docBO.getDocumentId());
        
        dfBO.setDocument_type_name(DocType);

        dfBO.setIdDocument_send(sendBO.getIdDocument_send());
        dfBO.setIdDocument_type(docBO.getIdDocType());
        dfBO.setIdIndustry(industryId);
        dfBO.setIdLanguage(docBO.getIdLanguage());
        dfBO.setIdMarket(docBO.getIdMarket());
        dfBO.setIdStatus_publish(sendBO.getIdStatus_publish());

        dfBO.setIndustry_name(industry);
        dfBO.setIsEmisora(isEmisora? 1:0);

        dfBO.setLanguage_name(language);
        dfBO.setMarket_name(market);
        dfBO.setPublish_status_name(publishStatus);
        dfBO.setSubject_name(subject);

        dfBO.setUrlHtmlForm(rutaPublica);
        //dfBO.setUrlHtmlVector(fileZipGen);

        //dfBO.setUrlPdfForm();
        dfBO.setUrlPdfVector(sshFiles.get(0));

        //dfBO.setUrlType();
        dfBO.setUrlTypes(list);
        dfBO.setPid(sendBO.getPid());
        dfBO.setPidZip(sendBO.getPidzip());
        
        dfBO.setFlagHistory("1");
        dfBO.setDocumentTitle(docBO.getDocumentName());
        dfBO.setMailContent(sendBO.getTextString());
        dfBO.setDocumentContent(sendBO.getDocumentContent());
        dfBO.setAuthorMail(sendBO.getCorreoAutor());
        dfBO.setPdfUrl(pdfPub);
        dfBO.setPublishAttach(isPublishAttach);
          //ZIP, PDF

        ExportCM e = new ExportCM();
        int succesfulConnection = e.connectToServer();
        List<String> pid = null;
        if (succesfulConnection != -1) {
            pid = e.importDocument(dfBO);
            if (pid != null) {
                try {
                    if(isPublishAttach == false){
                        sendBO.setPid(pid.get(0));
                        //sendBO.setPidzip(pid.get(1));
                        SendPublishDAO dao = new SendPublishDAO();
                        dao.insertUpdate(sendBO);
                    }
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).error(ex);
                }
                Utilerias.logger(ExportCM.class).info("Documento importado");
            } else {
                Utilerias.logger(ExportCM.class).info("Documento no importado");
            }
        } else {
             Utilerias.logger(ExportCM.class).info("No hay Conexion a Content");
        } 
        
        return true;
    }
    
    public boolean importToProgress(SendPublishBO sendBO, DocumentBO docBO, List<String> sshFiles, String rutaPublica, String fecha, boolean isPublishAttach, String pdfPub){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMddHHmmssS");
        String valorUnico = sdf.format(new Date());

        String fileZipGen = Utilerias.getFilePath(Utilerias.PATH_TYPE.HTML_DIR_GENERATE) + valorUnico + ".zip";
        //FileToZip fZip = new FileToZip(sshFiles, fileZipGen);
        //fZip.zipFileByMyList();
        //fZip.zipFiles();
        
        DocumentTypeDAO dDAO = new DocumentTypeDAO();
        List<DocumentTypeBO> dBO = dDAO.get(null, -1, 0);
        String DocType="";
        int IdDocTypeVector = 0;
        for(DocumentTypeBO d: dBO){
            if(d.getIddocument_type()==docBO.getIdDocType()){
                DocType=d.getName();
                
                if( d.getIddocument_type_vector() != null)
                    IdDocTypeVector =  Integer.parseInt(d.getIddocument_type_vector());
                
                if( d.isSendEmail() == false )
                    return true;//Si el tipo de documento no permite envio de publicacion se aborta el proceso de progress
                    
                break;
            }
        }
        
        LanguageDAO lDAO=new LanguageDAO();
        List<LanguageBO> lBO = lDAO.get(null);
        String language="";
        for(LanguageBO l: lBO){
            if(l.getIdLanguage()==docBO.getIdLanguage()){
                language=l.getName();
                break;
            }
        }

        MarketDAO mDAO = new MarketDAO();
        List<MarketBO> mBO = mDAO.get(null);
        String market = "";
        for(MarketBO m: mBO){
            m.setIdMarket(Integer.parseInt(m.getIdMiVector_real()));
            if(m.getIdMarket() == docBO.getIdMarket()){
                market = m.getName();
                break;
            }
        }

        SubjectDAO sDAO = new SubjectDAO();
        List<SubjectBO> sBO = sDAO.get(null);
        String subject = "";
        int industryId = 0;
        boolean isEmisora = false;
        for (SubjectBO s: sBO){
            if(docBO.getIdSubject()==s.getIdSubject()){
                industryId = s.getIndustry();
                isEmisora = s.isIssuing();
                subject = s.getName();
                break;
            }
        }

        IndustryDAO iDAO = new IndustryDAO();
        List<IndustryBO> iBO = iDAO.get(null);
        String industry="";
        for(IndustryBO i: iBO){
            if(industryId==i.getIdIndustry()){
                industry=i.getName();
                break;
            }
        }

        PublishStatusDAO pDAO = new PublishStatusDAO();
        List<PublishStatusBO> pBO = pDAO.get();
        String publishStatus="";
        for(PublishStatusBO p: pBO){
            if(sendBO.getIdStatus_publish()==p.getIdpublish_status()){
                publishStatus = p.getName();
                break;
            }
        }

        ArrayList<String> list = new ArrayList<String>();
        //list.add("ZIP");
        list.add("PDF");

        DocumentFormatBO dfBO = new DocumentFormatBO();

        dfBO.setAuthor_name(docBO.getAuthorName());
        //dfBO.setContentmanagerEstatus();
        dfBO.setDate_publish(fechaProgress);
        //dfBO.setDoc_Url(retVal);

        dfBO.setDocument_id(docBO.getDocumentId());
        
        dfBO.setDocument_type_name(DocType);

        dfBO.setIdDocument_send(sendBO.getIdDocument_send());
        dfBO.setIdDocument_type(IdDocTypeVector);
        dfBO.setIdIndustry(industryId);
        dfBO.setIdLanguage(docBO.getIdLanguage());
        dfBO.setIdMarket(docBO.getIdMarket());
        dfBO.setIdStatus_publish(sendBO.getIdStatus_publish());

        dfBO.setIndustry_name(industry);
        dfBO.setIsEmisora(isEmisora? 1:0);

        dfBO.setLanguage_name(language);
        dfBO.setMarket_name(market);
        dfBO.setPublish_status_name(publishStatus);
        dfBO.setSubject_name(subject);

        dfBO.setUrlHtmlForm(rutaPublica);
        //dfBO.setUrlHtmlVector(fileZipGen);

        //dfBO.setUrlPdfForm();
        dfBO.setUrlPdfVector(sshFiles.get(0));

        //dfBO.setUrlType();
        dfBO.setUrlTypes(list);
        dfBO.setPid(sendBO.getPid());
        dfBO.setPidZip(sendBO.getPidzip());
        
        dfBO.setFlagHistory("1");
        dfBO.setDocumentTitle(docBO.getDocumentName());
        dfBO.setMailContent(sendBO.getTextString());
        dfBO.setDocumentContent(sendBO.getDocumentContent());
        dfBO.setAuthorMail(sendBO.getCorreoAutor());
        dfBO.setPdfUrl(pdfPub);
        dfBO.setPublishAttach(isPublishAttach);
          //ZIP, PDF
        
        JsonServicePublicationProgress serviceProgress = new JsonServicePublicationProgress(true);
        String s = serviceProgress.getPublicationProgressResponse("nuevo", dfBO, autores, listDist);
        Utilerias.logger(getClass()).info(s);
        
        return true;
    }
    
    public List<String> getSshCollabFiles(DocumentBO docBO, String fecha, Utilerias.ALLOWED_KEY type){
        List<String> retVal = new ArrayList<>();
        
        DocumentDAO dao = new DocumentDAO();
        
        CollaborativesDAO collabDAO = new CollaborativesDAO();
        DocumentCollabBO document = collabDAO.getDocument(docBO.getDocumentId());
        
        if(docBO.getLstDocumentCollab() != null && docBO.getLstDocumentCollab().size() > 0){
            document.setLstDocumentCollab(docBO.getLstDocumentCollab());
        }

        //PDF
        List<InputStream> isPDFs = new ArrayList<>();
        Map<Integer, String> urlDoc = new HashMap<>();
        
        DocumentBO docBoColl = null;
        
        try {
            docBoColl = dao.getDocument(docBO.getDocumentId(), -1,false);
        } catch (SQLException ex) {
            Logger.getLogger(UtileriasSSH.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int i=0;
        for(DocumentCollabItemBO coll : document.getLstDocumentCollab()){
            Utilerias.pasarGarbageCollector();
            try {
                
                String rutaHTMLColl = getHTMLCollab(coll.getItemDocumentId());
                if(rutaHTMLColl != null)
                    urlDoc.put(coll.getItemDocumentId(), rutaHTMLColl);
                else{//Si no existe el html publicado se tendra que generar su publicacion en Docs o TMP
                    //Crear registro de publicacion como guardado
                    //Subir al FTP los archivos para guardar el nombre de la publicacion y la ruta
                    DocumentBO dcBO = dao.getDocument(coll.getItemDocumentId(), -1,false);
                    
                    //Si es vista previa solo generar la ruta del archivo
                    if( Utilerias.ALLOWED_KEY.SSH_ROOT_TEMP_DIRECTORY.equals( type ) ){
                        
                        rutaHTMLColl = sendFilesSsh(null, dcBO, fecha, type);
                        urlDoc.put(coll.getItemDocumentId(), rutaHTMLColl);
                        
                    } else {
                    
                        SendPublishDAO SendPublisDao = new SendPublishDAO();
                        SendPublishBO sendBO = new SendPublishBO();

                        try {
                            List<SendPublishBO> list = SendPublisDao.get(dcBO.getDocumentId());

                            //Si ya existe el registro de publicacion lo toma y solo regenera la ruta
                            if (list != null && list.size() > 0) {
                                sendBO = list.get(0);

                                rutaHTMLColl = sendFilesSsh(null, dcBO, fecha, type);

                                //Registro de ruta generada
                                SendPublisDao.UpdateURL(sendBO.getIdDocument_send(), rutaHTMLColl);

                                urlDoc.put(coll.getItemDocumentId(), rutaHTMLColl);

                            }else{

                                String cnt = getContenido(dcBO);
                                Clob clCont = new SerialClob( cnt.toCharArray() );
                                Clob clMail = null;
                                if(cnt.length() > Utilerias.getMaxCharMail())
                                    clMail = new SerialClob( cnt.substring(0, Utilerias.getMaxCharMail()).toCharArray() );
                                else
                                    clMail = new SerialClob( cnt.toCharArray() );

                                sendBO.setIdStatus_publish(StatementConstant.SC_2.get());//Publicado
                                sendBO.setIdSubject(dcBO.getIdSubject());
                                sendBO.setIdDocument(dcBO.getDocumentId());
                                sendBO.setTitle(dcBO.getDocumentName());
                                sendBO.setText(clMail);
                                sendBO.setTextString(cnt);
                                sendBO.setDocumentContent(clCont);
                                sendBO.setScheduled(false);
                                sendBO.setDate_publish(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));

                                //Registro de Publicacion
                                int id = SendPublisDao.insertUpdate(sendBO);
                                sendBO.setIdDocument_send(id);
                                
                                //Guardar autor
                                SendPublishAuthorsDAO autDao = new SendPublishAuthorsDAO();
                                SendPublishAuthorsBO autBo = new SendPublishAuthorsBO();
                                autBo.setId_send_publish(id);
                                autDao.delete(autBo);
                                autBo.setId_author(dcBO.getIdAuthor());
                                autDao.insertUpdate(autBo);

                                //Se genera la publicacion en el FTP y se registra en progress si se permite
                                rutaHTMLColl = sendFilesSsh(sendBO, dcBO, fecha, type, getAutorJson(dcBO.getIdAuthor()), getPublishListJson(), null);

                                //Registro de ruta generada
                                SendPublisDao.UpdateURL(id, rutaHTMLColl);

                                urlDoc.put(coll.getItemDocumentId(), rutaHTMLColl);

                            }
                        } catch (Exception e) {
                            Utilerias.logger(getClass()).error(e);
                        }

                    }
                    
                }
                
                InputStream isPDF = crearPDFCollab( coll.getItemDocumentId(), rutaHTMLColl );
                if(isPDF != null){
                    isPDFs.add( isPDF );
                }
                
                //List<String> htmlResult =  getSshFiles(dao.getDocument(coll.getItemDocumentId(), -1,false), type, docBoColl);
                //urlDoc.put(coll.getItemDocumentId(), htmlResult.get(htmlResult.size()-1));
                
                i++;
                Utilerias.logger(getClass()).info("Documento Procesado ("+ i +"/"+ document.getLstDocumentCollab().size() +") :" + rutaHTMLColl);
               
                //retVal.addAll(htmlResult);
            } catch (SQLException ex) {
                Utilerias.logger(getClass()).error(ex);
            } catch (Exception e){
                Utilerias.logger(getClass()).error(e);
            }
        }
        
        try {//Se genera el Directorio con el Disclosure del semanario
            PdfDocument pdfDoc = new PdfDocument();
            
            pdfDoc.createPdfDocumentDisclosureAndDirOnly(docBO);
            String fileName = pdfDoc.getFilePath();
            isPDFs.add( new FileInputStream(fileName) );
            
        } catch (DocumentException | IOException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        
        String dirPDF = Utilerias.getFilePath(Utilerias.PATH_TYPE.PDFS_DIR);
        String fName = docBO.getDocumentId() + "_" + docBO.getFileName() + "_" + Utilerias.getGeneratedFileName(Utilerias.GENERATED_IMAGE_TYPE.PDF) + ".pdf";
        String fileNamePdf = dirPDF + fName;
        OutputStream output = null;
        
        try {
            output = new FileOutputStream(fileNamePdf);
        } catch (FileNotFoundException ex) {
            Utilerias.logger(getClass()).error(ex);
        }

        MergePDF.concatPDFs(isPDFs, output, true);
        retVal.add( 0, fileNamePdf);
        
        //HTML
        Utilerias.pasarGarbageCollector();
        HTMLDocument html = new HTMLDocument();
        String fileNameHtml = html.createHTMLSemanario(docBO.getDocumentId(), urlDoc, docBO, fName);
        retVal.addAll(html.getSshFiles());
        retVal.add(fileNameHtml);
        
        return retVal;
    }
    
    public List<String> getSshFiles(DocumentBO docBO, Utilerias.ALLOWED_KEY type, DocumentBO docBoColl){
        HTMLDocument htmlDoc;
        PdfDocument pdfDoc;
        List<String> sshFiles = new ArrayList<>();
        try {
            pdfDoc = new PdfDocument();
            htmlDoc = new HTMLDocument();
                        
            htmlDoc.setDocBODisplosure(docBoColl);
            pdfDoc.setDocBODisplosure(docBoColl);
            pdfDoc.setEliminarDisplosure(false);
            pdfDoc.setAgregarDirectorio(false);
            if(pdfDoc.createPdfDocument(docBO, true) == null){
                htmlDoc.createHTMLDocument(docBO, type, null, null);
            }else{
                String fileName = pdfDoc.getFilePath();
                sshFiles.add(fileName);
                
                htmlDoc.createHTMLDocument(docBO, type, pdfDoc.getFileName(), pdfDoc.getFilePath());
                
                if(pdfDoc.createPdfDocumentNoDisclosure(docBO, true) != null){
                    sshFiles.add(pdfDoc.getFilePath());
                }
            }
            sshFiles.addAll(htmlDoc.getSshFiles());
     
        } catch (IOException | DocumentException ex) {
            Utilerias.logger(getClass()).error(ex);
        } catch (Exception e){
            Utilerias.logger(getClass()).error(e);
        }
        
        return sshFiles;
    }
    
    private String reorgDate(String fecha){
        String retVal="";
        java.text.SimpleDateFormat sdf_in = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.SimpleDateFormat sdf_out = new java.text.SimpleDateFormat("yyyy/MM/dd");
        
        try {
            Date d = sdf_in.parse(fecha);
            retVal = sdf_out.format(d);
        } catch (ParseException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        
        return retVal;
    }
    
    public InputStream crearPDFCollab(int documentId, String urlOpcional){
        InputStream inputStream = null;
        try {
            DocumentDAO dao = new DocumentDAO();
            
            //Obtenemos la publicacion si la tiene
            SendPublishBO boSend = null;
            
            if( urlOpcional != null ){
                boSend = new SendPublishBO();
                boSend.setUrl(urlOpcional);
            }else{
                boSend = obtenPublicacion(documentId);
            }
            
            if(boSend == null){
                
                //Si no esta publicado lo generamos
                DocumentBO bo = dao.getDocument(documentId, -1,false);
                PdfDocument pdfDoc = new PdfDocument();

                /*if(docBoColl == null){
                    pdfDoc.setEliminarDisplosure(true);
                }else{
                    pdfDoc.setEliminarDisplosure(false);
                    pdfDoc.setAgregarDirectorio(true);
                    pdfDoc.setDocBODisplosure(docBoColl);
                }*/

                if(pdfDoc.createPdfDocumentNoDisclosure(bo, true) != null){
                    String fileName = pdfDoc.getFilePath();
                    inputStream = new FileInputStream(fileName);
                }
                
            }else{//Se obtiene el archivo de la publicacion
                if(boSend.getUrl() != null && boSend.getUrl().trim().isEmpty() == false){
                
                    String fileName = boSend.getUrl();
                    String[] vfileName = fileName.split("/");
                    
                    String pdfReplace = "ND_" + documentId + ".pdf";
                    String htmlRegex = vfileName[vfileName.length-1];
                
                    fileName = fileName.replaceAll(htmlRegex, pdfReplace);
                    
                    try{
                        URL url = new URL(fileName);
                        inputStream = url.openStream();
                    }catch(IOException ex){
                        DocumentBO bo = dao.getDocument(documentId, -1,false);
                        PdfDocument pdfDoc = new PdfDocument();

                        if(pdfDoc.createPdfDocumentNoDisclosure(bo, true) != null){
                            String fName = pdfDoc.getFilePath();
                            inputStream = new FileInputStream(fName);
                        }
                    }
                    
                }else{//Si no esta en la publicacion se genera
                    DocumentBO bo = dao.getDocument(documentId, -1,false);
                    PdfDocument pdfDoc = new PdfDocument();
                    
                    if(pdfDoc.createPdfDocumentNoDisclosure(bo, true) != null){
                        String fileName = pdfDoc.getFilePath();
                        inputStream = new FileInputStream(fileName);
                    }
                }
                
            }

        } catch (FileNotFoundException e) {
            Utilerias.logger(getClass()).error(e);
        } catch (DocumentException | IOException | SQLException ex) {
            Utilerias.logger(getClass()).error(ex);
        }
        
        return inputStream;
    }
    
    private String getHTMLCollab(int documentId){
        String retVal = null;
        
        SendPublishBO boSend = obtenPublicacion(documentId);
        
        if(boSend == null){
                
            return null;
            
        }else{//Se obtiene el archivo de la publicacion
            if(boSend.getUrl() != null && boSend.getUrl().trim().isEmpty() == false){
                
                String fileName = boSend.getUrl();
                
                try{
                    
                    URL url = new URL(fileName);
                    url.openStream();
                    retVal = fileName;
                    
                }catch(IOException ex){
                    
                    return null;
                    
                }
                
            }else{
                
                return null;
            }

        }
        
        return retVal;
    }
    
    private SendPublishBO obtenPublicacion(int idDocument){
        SendPublishBO retVal = null;
        try {
            
            SendPublishDAO daoB = new SendPublishDAO();
            List<SendPublishBO> list = daoB.get(idDocument);
            
            if (list != null && list.size() > 0) {
                
                retVal = list.get(0);
                
            }else{
                
                retVal =  null;
                
            }
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
            retVal =  null;
        }
        
        return retVal;
    }
    
    private InputDoc getAutorJson(int idAutor){
        InputDoc inputDoc = new InputDoc();
        
        try {
            com.adinfi.ws.IAccess_Stub stub = (com.adinfi.ws.IAccess_Stub) new com.adinfi.ws.Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            com.adinfi.ws.ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);
            
            if (array != null && array.getUsuario() != null) {
                DsInputDoc dsInputDoc = new DsInputDoc();
                List<DsAutor> tdsInputDocAutores = new ArrayList<>();
                
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    
                    if(idAutor == o.getUsuarioId()){
                        
                        DsAutor dsAutor = new DsAutor();
                        dsAutor.AutorId = o.getUsuarioId();
                        dsAutor.Autor = o.getNombre();
                        dsAutor.AutorEmail = o.getCorreo();
                        tdsInputDocAutores.add(dsAutor);
                        
                    }
                    
                }
                
                dsInputDoc.tdsInputDocAutores = tdsInputDocAutores;
                inputDoc.dsInputDoc = dsInputDoc;
            }
        }catch(Exception e){
            Utilerias.logger(getClass()).error(e);
        }
        
        return inputDoc;
    }
    
    private InputListaDist getPublishListJson(){
        InputListaDist inputListaDist = new InputListaDist();
        
        try {
            
            com.adinfi.ws.analisisws.publicador.IPublicador_Stub stub = (com.adinfi.ws.analisisws.publicador.IPublicador_Stub) new com.adinfi.ws.analisisws.publicador.Publicador_Impl().getBasicHttpBinding_IPublicador();
            UtileriasWS.setEndpoint(stub);
            com.adinfi.ws.analisisws.publicador.ArrayOfCanalDistribucion array = stub.listaDistribucion(GlobalDefines.WS_INSTANCE);
            com.adinfi.ws.analisisws.publicador.CanalDistribucion[] canal = array.getCanalDistribucion();
            
            List<DsCanal> tdsInputListaDist = new ArrayList<>();

            for (com.adinfi.ws.analisisws.publicador.CanalDistribucion o2 : canal) {
                
                if(o2.getChildren() != null && o2.getChildren().getCanalDistribucion() != null){
                    
                    for(com.adinfi.ws.analisisws.publicador.CanalDistribucion child : o2.getChildren().getCanalDistribucion()){
                        
                        if(child.getIsDefault()){
                            
                            String chanel = "";
                            if(child.getCanalId().intValue() < 10)
                                chanel = "0" + child.getCanalId();
                            else
                                chanel = child.getCanalId().toString();
                            
                            chanel = o2.getCanalId().toString() + chanel;
                            
                            DsCanal dsCanal = new DsCanal();
                            dsCanal.CanalId = Integer.valueOf(chanel);
                            tdsInputListaDist.add(dsCanal);
                            
                        }
                        
                    }
                    
                }
                
            }
            
            DsInputListaDist dsInputListaDist = new DsInputListaDist();
            dsInputListaDist.tdsInputListaDist = tdsInputListaDist;
            inputListaDist.dsInputListaDist = dsInputListaDist;
            
        }catch(RemoteException | NumberFormatException e){
            Utilerias.logger(getClass()).error(e);
        }
        
        return inputListaDist;
    }
    
    private String getContenido(DocumentBO docBO) {
        StringBuilder html = new StringBuilder();
        String texto = "";
        HTMLDocument doc = new HTMLDocument();
        List<String> documentsTxt = doc.getTextFromDocument(docBO);
        if (documentsTxt != null && documentsTxt.isEmpty() == false) {
            html.append("<html> <head> </head> <body>");
            for (String docs : documentsTxt) {
                html.append(removeHTMLheaderTags(docs.trim()));
            }
            html.append("</body></html>");
            texto = html.toString();
            texto = texto.replace("#8A0808", "#000000");
        }
        
        return texto;
    }
    
    private String removeHTMLheaderTags(String html) {
        String result = null;
        
        if (html != null && !html.isEmpty()) {
            result = html.replaceAll("<html>", "");
            result = result.replaceAll("</html>", "");
            result = result.replaceAll("<head>", "");
            result = result.replaceAll("</head>", "");
            result = result.replaceAll("<body>", "");
            result = result.replaceAll("</body>", "");
            result = result.replaceAll("<title>", "");
            result = result.replaceAll("</title>", "");
            result = result.replaceAll("</html", "");
        }
        
        return result;
    }
}
