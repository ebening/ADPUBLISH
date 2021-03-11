/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.util;

import java.util.Date;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author vectoran
 */
public class TaskEMail {
    
    public boolean enviarMail(String email, String mensaje, String subject) {
    	
     	if(email == null || email.trim().isEmpty() == true){
    		email = "advecfin@vector.com.mx";
    	}
    	    	
    	mensaje = Utilities.EncodingHTML(mensaje);
    
        boolean band = true;

        String host = Utilerias.getProperty(ApplicationProperties.EMAIL_HOSTNAME);
        final String userName = Utilerias.getProperty(ApplicationProperties.EMAIL_CUENTA);
        final String password = Utilerias.getProperty(ApplicationProperties.EMAIL_PASSWORD);
        String port = Utilerias.getProperty(ApplicationProperties.EMAIL_PUERTO);
        String starttls = Utilerias.getProperty(ApplicationProperties.EMAIL_STARTTLS);
    //    String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";//AppConfigManager.getProperty("SENDER-EMAIL-SMTP-SOCKET-CLASS");
        String fallback = Utilerias.getProperty(ApplicationProperties.EMAIL_FALLBACK);

        //AppConfigManager.getProperty("SENDER-EMAIL-SMTP-ALLOW-FALLBACK");

        try {
            java.util.Properties props = null;
            props = System.getProperties();
            props.put("mail.smtp.user", userName);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.debug", true);

            if (!"".equals(port)) {
                props.put("mail.smtp.port", port);
    //            props.put("mail.smtp.socketFactory.port", port);
            }

            if (!"".equals(starttls)) {
                props.put("mail.smtp.starttls.enable", starttls);
            }

    //        if (!"".equals(socketFactoryClass)) {
    //            props.put("mail.smtp.socketFactory.class", socketFactoryClass);
    //        }

            if (!"".equals(fallback)) {
                props.put("mail.smtp.socketFactory.fallback", fallback);
            }
            props.put("mail.smtp.auth.mechanisms","NTLM");
            props.put("mail.smtp.auth.ntlm.domain",Utilerias.getProperty(ApplicationProperties.EMAIL_DOMAIN_MAIL)); // Domain you log into Windows with

            //Session session = Session.getDefaultInstance(props, null);
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });

            MimeMessage msg = new MimeMessage( session);
            msg.setFrom(new InternetAddress(Utilerias.getProperty(ApplicationProperties.EMAIL_CUENTA_CORREO_COMPLETA)));
            msg.setSender(new InternetAddress(Utilerias.getProperty(ApplicationProperties.EMAIL_CUENTA_CORREO_COMPLETA)));
            msg.setSubject(subject);
            msg.setText(mensaje, "ISO-8859-1");
            msg.setSentDate(new Date());
            msg.setHeader("content-Type", "text/html;charset=\"ISO-8859-1\"");

            String [] arrayMsg = email.split(",");

            javax.mail.internet.InternetAddress[] addressTo = new javax.mail.internet.InternetAddress[arrayMsg.length];

            for (int i = 0; i < addressTo.length; i++)
            {
                    addressTo[i] = new javax.mail.internet.InternetAddress(arrayMsg[i]);
            }

            msg.setRecipients(Message.RecipientType.TO, addressTo);

            msg.saveChanges();

            Transport transport = session.getTransport("smtp");

            transport.connect(host, userName, password);
            Transport.send(msg, msg.getAllRecipients());
            transport.close();

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
            return false;
        }

        return band;
    }
}
