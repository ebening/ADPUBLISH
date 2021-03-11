/*
 * MainApp.java
 */
package com.adinfi.formateador.main;

import com.adinfi.formateador.db.ConnectionDB;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.InstanceContext;
import com.adinfi.formateador.util.StringEncrypter;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.Login;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.Usuario;
import java.awt.Component;
import java.rmi.RemoteException;
import java.util.EventObject;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.swingx.JXTable;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel;

/**
 * The main class of the application.
 */
public class MainApp extends SingleFrameApplication {

    public void closeSession() {
        //Eliminar sesion (usuario contraseña) de la aplicación.
        String pathFile = Utilerias.getFilePath(Utilerias.PATH_TYPE.PASSWORD_XML);
        Utilerias.removeFile(pathFile);
        closeApplication();
    }
    
    public void closeApplication() {
        shutdown();
    }

    @Override
    protected void initialize(String[] args) {
        Utilerias.loadJNISource();
    }
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setLookAndFeel();
                Boolean validateUser = validateUser();
                if (validateUser == true) {
                    show(new MainView(MainApp.getApplication()));
                    addExitListener(new ExitListener() {

                        @Override
                        public boolean canExit(EventObject e) {
                            boolean bOkToExit = false;
                            Component source = (Component) e.getSource();
                            bOkToExit = JOptionPane.showConfirmDialog(source, "¿Está seguro de salir de la aplicación?")
                                    == JOptionPane.YES_OPTION;
                            return bOkToExit;
                        }

                        @Override
                        public void willExit(EventObject event) {

                        }

                    });
                }
            }
        });
    }

    protected void setLookAndFeel() {
        
        try {
            UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.FOCUS_LOOP_ANIMATION, JTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.SELECTION, JTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JTable.class);
            
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.SELECTION, JXTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JXTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JXTable.class);
            AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, JXTable.class);
            
            //AnimationConfigurationManager.getInstance().(AnimationFacet.ICON_GLOW, JTable.class);
        } catch (UnsupportedLookAndFeelException e) {
            Utilerias.logger(getClass()).info(e);
        }
        
    }

    protected boolean validateUser() {
        boolean success;
        /* Validacion de LDAP por medio de Usuario y contrasenia */
        boolean settingsSaved = isPasswordSaved();
        if (settingsSaved == false) {
            Login login = new Login(null, true);
            login.setIconImage(Utilerias.getImage(Utilerias.ICONS.APP_RIBBON_ICON));
            login.setTitle(Utilerias.getProperty(ApplicationProperties.APP_TITLE));
            Utilerias.centreDialog(login, true);
            login.setVisible(true);
            Usuario user = login.getUsuario();
            //UserSession.getInstance().setUser(user);
            InstanceContext.getInstance().setUsuario(user);
            success = user != null;
        } else {
            success = settingsSaved;
        }
        return success;
    }

    private boolean isPasswordSaved() {
        boolean success = false;
        try {
            Properties p = Utilerias.getPasswordFile();
            String user = (String) p.get(GlobalDefines.LOGIN_COOKIE_USER);
            String pass = (String) p.get(GlobalDefines.LOGIN_COOKIE_PASS);
            
            /*
            String userArray = StringEncrypter.decryptDES(user);
            String passArray = StringEncrypter.decryptDES(pass);
            */
            
            String userArray = StringEncrypter.decryptAES(user.getBytes("UTF-8"));
            String passArray = StringEncrypter.decryptAES(pass.getBytes("UTF-8"));
            
            
            if ( (userArray == null || userArray.isEmpty()) == true 
                    || (passArray == null || passArray.isEmpty() == true) ) {
                return success;
            }
            
            LoginBackgroundWorker lbw = new LoginBackgroundWorker(userArray, passArray);
            success = lbw.doInBackground();
            Usuario usuario = lbw.getUser();
            //UserSession.getInstance().setUser(usuario);
            InstanceContext.getInstance().setUsuario(usuario);
            
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
        return success;
    }

    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    @Override
    protected void shutdown() {
        //The default shutdown saves session window state.
        super.shutdown();
        /**
         * Llamar método para cierre de sesión en Servicio Web
         */
        closeWSSession();
        closePoolConnection();
        System.exit(0);
    }

    private void closeWSSession() {
        if (InstanceContext.getInstance().getUsuario() == null) {
            return;
        }
        Thread closeThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
                    UtileriasWS.setEndpoint(stub);
                    stub.logOut(InstanceContext.getInstance().getUsuario().getUsuarioId());
                    Utilerias.logger(getClass()).info("Close session");
                } catch (RemoteException ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
            }
        });
        closeThread.start();
    }

    private void closePoolConnection() {
        if (ConnectionDB.getInstance().getCpds() != null) {
            ConnectionDB.getInstance().getCpds().close();
        }
    }

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of MainApp
     */
    public static MainApp getApplication() {
        return Application.getInstance(MainApp.class);
    }

    /**
     * Main method launching the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(MainApp.class, args);
    }
}
