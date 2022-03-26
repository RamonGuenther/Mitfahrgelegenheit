package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendSimpleMessage(String passengerName, String driverName, String message, String email, String route) throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail);

        mimeMessageHelper.setFrom("drive.together@outlook.de");
        mimeMessageHelper.setTo(email);

        if (message.isEmpty()) {
            mimeMessageHelper.setText(
                    "<h2> Hallo " + driverName + ",</h2>" +
                            "<h2> es wurde eine Fahrt vom Nutzer " + passengerName + " angefragt. </h2>" +
                            "<p></p>" +
                            "<a href=" + route + "> Route anzeigen </a>" +
                            "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                            "nicht auf diese E-Mail.</p>", true);
        } else {
            mimeMessageHelper.setText(
                    "<h2> Hallo " + driverName + ",</h2>" +
                            "<h2> es wurde eine Fahrt vom Nutzer " + passengerName + " angefragt. </h2>" +
                            "<p></p>" +
                            "<a href=" + route + "> Route anzeigen </a>" +
                            "<p> Nachricht des Nutzers: " + message + "</p>" +
                            "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                            "nicht auf diese E-Mail.</p>", true);
        }

        javaMailSender.send(mail);

    }
}
