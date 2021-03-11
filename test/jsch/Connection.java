/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class Connection {
    
    
    public static void main(String[] args) {
        try {
            JSch jsch = new JSch();
            
            String host = "204.126.12.152";
            String username = "repoanalisis";
            String password = "ReP04nalisis";
            
            Session session = jsch.getSession(username, host);
            session.setPassword(password);
            
            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            
            
            session.setTimeout(20000);
            System.out.println("Connecting to server...");
            session.connect();
            
            
        } catch (JSchException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
