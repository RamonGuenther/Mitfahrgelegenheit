package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Die Klasse MailService ist für das Versenden von Emails zuständig.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * Die Methode sendDriveRequestMail versendet eine E-Mail an den Ersteller eines Fahrtangebots,
     * wenn ein Benutzer eine Fahrtanfrage zu diesem Fahrtangebot stellt.
     *
     * @param passengerName             Name des Benutzers, der eine Anfrage stellt
     * @param driverName                Name des Fahrers
     * @param message                   Nachricht, die versendet werden soll
     * @param email                     Email-Adresse des Fahrers
     * @param route                     Routenlink für den Fahrer
     * @throws MessagingException       -
     */
    @Async
    public void sendDriveRequestMail(String passengerName, String driverName, String message, String email, String route) throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

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

    /**
     * Die Methode sendBookingCancellation sendet eine E-Mail an den Fahrer, wenn ein Mitfahrer eine
     * Buchung löscht und die Fahrt somit absagt.
     *
     * @param driveRoute            Routenlink, mit der aktualisierten Route
     * @param passenger             Name des Mitfahrers, der aus der Fahrt ausgestiegen ist
     * @throws MessagingException   -
     */
    @Async
    public void sendBookingCancellation(DriveRoute driveRoute, String passenger) throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

        mimeMessageHelper.setFrom("drive.together@outlook.de");
        mimeMessageHelper.setTo(driveRoute.getDriver().getEmail());

        mimeMessageHelper.setText(
                "<h2> Hallo " + driveRoute.getDriver().getFirstName() + ",</h2>" +
                        "<h2>" + passenger +
                        " hat die Mitfahrt am " + driveRoute.getFormattedDate() + " abgesagt</h2>" +
                        "<p>Deine Route sieht jetzt folgendermaßen aus: </p>" +
                        "<a href=" + driveRoute.getCurrentRouteLink() + "> Route anzeigen </a>" +
                        "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                        "nicht auf diese E-Mail.</p>", true);

        javaMailSender.send(mail);
    }

    /**
     * Die Methode sendDriveDeleteMessage sendet eine E-Mail an alle Mitfahrer einer Fahrt, die vom
     * Fahrer gelöscht wurde, damit diese darüber informiert werden, dass die Fahrt nicht mehr stattfindet.
     *
     * @param driveRoute            Fahrt, die vom Fahrer abgesagt wurde
     * @param message               Nachricht an die Mitfahrer
     * @throws MessagingException   -
     */
    @Async
    public void sendDriveDeleteMessage(DriveRoute driveRoute, String message) throws MessagingException {

        List<Booking> bookings = driveRoute.getBookings();

        for (Booking booking : bookings) {

            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mimeMessageHelper.setFrom("drive.together@outlook.de");
            mimeMessageHelper.setTo(booking.getPassenger().getEmail());

            if (message.isEmpty()) {
                mimeMessageHelper.setText(
                        "<h2> Hallo " + booking.getPassenger().getFirstName() + ",</h2>" +
                                "<h2>" + driveRoute.getDriver().getFullName() +
                                " hat die Fahrt für den " + driveRoute.getFormattedDate() + " abgesagt.</h2>" +
                                "<a href=" + driveRoute.getCurrentRouteLink() + "> Route anzeigen </a>" +
                                "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                                "nicht auf diese E-Mail.</p>", true);

            } else {
                mimeMessageHelper.setText(
                        "<h2> Hallo " + booking.getPassenger().getFirstName() + ",</h2>" +
                                "<h2>" + driveRoute.getDriver().getFullName() +
                                " hat die Fahrt für den " + driveRoute.getFormattedDate() + " abgesagt.</h2>" +
                                "<p> Nachricht des Fahrers: " + message + "</p>" +
                                "<a href=" + driveRoute.getCurrentRouteLink() + "> Route anzeigen </a>" +
                                "<p> Dies ist eine automatisch generierte E-Mail von einer System-E-Mail-Adresse. Bitte antworten Sie\n" +
                                "nicht auf diese E-Mail.</p>", true);
            }

            javaMailSender.send(mail);
        }
    }
}
