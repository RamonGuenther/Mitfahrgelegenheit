package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 *
 */
@Service
public class MailService {

    private final String SENDER_MAIL = "drive.together@outlook.de";
    private final String SENDER_PASSWORT = "Mitfahrgelegenheit1234";

    private static MailService mailsender;
    private final Session session;

    @Autowired
    private TaskExecutor taskExecutor;

    private MailService() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "outlook.office365.com");
        prop.put("mail.smtp.port", "587");

        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_MAIL, SENDER_PASSWORT);
            }
        });

    }

    public static MailService getInstance() {
        if (mailsender == null) {
            mailsender = new MailService();
        }
        return mailsender;
    }


    private void mail(String passengerName, String driverName, String message, String email, String route) throws MessagingException {
        Message m = new MimeMessage(session);
        m.setFrom(new InternetAddress(SENDER_MAIL));

        m.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        m.setSubject("Fahrtanfrage von " + passengerName);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();


        if(message.isEmpty()) {
            mimeBodyPart.setContent(
                    "<h2> Hallo " + driverName + ",</h2>" +
                            "<h2> es wurde eine Fahrt vom Nutzer " + passengerName + " angefragt. </h2>" +
                            "<p></p>" +
                            "<a href=" + route + "> Route anzeigen </a>"+
                            "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                            "nicht auf diese E-Mail.</p>", "text/html; charset=utf-8");
        }
        else{
            mimeBodyPart.setContent(
                    "<h2> Hallo " + driverName + ",</h2>" +
                            "<h2> es wurde eine Fahrt vom Nutzer " + passengerName + " angefragt. </h2>" +
                            "<p></p>" +
                            "<a href=" + route + "> Route anzeigen </a>" +
                            "<p> Nachricht des Nutzers: " + message + "</p>" +
                            "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                            "nicht auf diese E-Mail.</p>", "text/html; charset=utf-8");
        }


        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        m.setContent(multipart);

        Transport.send(m);
    }

    public void sendMail(String passengerName, String driverName, String message, String email, String route){
        taskExecutor.execute(() -> {
            try {
                mail(passengerName,driverName,message,email,route);
            } catch (Exception e) {
                e.printStackTrace();
//                    log.error("Failed to send email to: " + to + " reason: "+e.getMessage());
            }
        });
    }


}
