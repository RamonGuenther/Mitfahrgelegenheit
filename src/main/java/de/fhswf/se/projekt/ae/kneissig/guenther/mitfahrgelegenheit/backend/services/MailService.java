package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
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

        System.out.println("Hallo: " + route);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail,true, "UTF-8");

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

    @Async
    public void sendBookingCancellation(DriveRoute driveRoute, String passenger) throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail,true, "UTF-8");

        mimeMessageHelper.setFrom("drive.together@outlook.de");
        mimeMessageHelper.setTo(driveRoute.getDriver().getEmail());

        mimeMessageHelper.setText(
                "<h2> Hallo " + driveRoute.getDriver().getFirstName() + ",</h2>" +
                        "<h2>" + passenger +
                        ". hat die Mitfahrt am " + driveRoute.getFormattedDate() + " abgesagt</h2>" +
                        "<p>Deine Route sieht jetzt folgendermaßen aus: </p>" +
                        "<a href=" + driveRoute.getCurrentRouteLink() + "> Route anzeigen </a>" +
                        "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                        "nicht auf diese E-Mail.</p>", true);

        javaMailSender.send(mail);
    }
}
