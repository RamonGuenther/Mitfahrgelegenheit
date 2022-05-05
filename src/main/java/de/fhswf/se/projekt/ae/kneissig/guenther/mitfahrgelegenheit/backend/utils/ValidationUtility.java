package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidMailException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidPasswordException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

/**
 * Die Klasse ValidationUtility stellt statische Methoden zur Verfügung, um beispielsweise
 * Objekte auf NULL zu prüfen oder mithilfe eines Regex Patterns bestimmte Strings zu
 * untersuchen nach ihrer Gültigkeit.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class ValidationUtility {

    private static final String ADDRESS_PATTERN = "(?<strasse>[A-Za-z_äÄöÖüÜß\\s-.()]+) (?<hausnummer>[\\s\\w]*) (?<postleitzahl>\\d{5}) (?<ort>[A-Za-z_äÄöÖüÜß\\s-.()]+)";
    private static final String EMAIL_PATTERN = "^([A-Za-z-.0-9]*)?@fh-swf.de$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$";

    public static void nullCheck (Object o) throws IllegalArgumentException {
        if (isNull (o))
            throw new IllegalArgumentException ();
    }

    public static void nullCheck(Object... os) throws IllegalArgumentException {
        Arrays.stream (os).forEach (ValidationUtility::nullCheck);
    }

    public static void nullOrEmptyCheck (String s) throws IllegalArgumentException {
        if (isNullOrEmpty (s))
            throw new IllegalArgumentException ();
    }

    /**
     * Prüft, ob die eingebende Adresse dem gewünschten Format entspricht.
     *
     * @param address Eingabe des Benutzers
     * @throws InvalidAddressException Ungültige Adresse
     */
    public static void addressPatternCheck(String address) throws InvalidAddressException {
        address = address.replace(", Deutschland", "");
        String replacedAddress = address.replace(",", " ");
        Pattern pattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher addressMatcher = pattern.matcher(replacedAddress);
        if(!addressMatcher.find()){
            throw new InvalidAddressException("Keine gültige Adresse.");
        }
    }

    /**
     * Prüft, ob das eingebende Datum in der Vergangenheit liegt.
     *
     * @param input Eingabe des Benutzers
     * @throws InvalidDateException Ungültiges Datum, wenn es in der Vergangenheit liegt
     */
    public static void localDateCheck(LocalDate input) throws InvalidDateException {
        if(input.isBefore(LocalDate.now())){
            throw new InvalidDateException("Das Datum darf nicht in der Vergangenheit liegen oder dem heutigen Datum entsprechen.");
        }
    }

    /**
     * Prüft, ob das eingebende Datum in der Vergangenheit liegt.
     *
     * @param input Eingabe des Benutzers
     * @return boolean, ob es in der Vergangenheit liegt
     */
    public static boolean localDateCheckBoolean(LocalDate input){
        return input.isBefore(LocalDate.now());
    }

    /**
     * Prüft, ob die eingebende Mail dem gewünschten Format entspricht.
     *
     * @param email Eingabe des Benutzers
     * @throws InvalidMailException Ungültige Email
     */
    public static void emailPatternCheck(String email) throws InvalidMailException {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher emailMatcher = pattern.matcher(email);
        if(!emailMatcher.find()){
            throw new InvalidMailException("Keine gültige FH-Mail.");
        }
    }

    /**
     * Prüft, ob das eingebende Passwort dem gewünschten Format entspricht.
     *
     * @param password Eingabe des Benutzers
     * @throws InvalidPasswordException Ungültiges Passwort
     */
    public static void passwordPatternCheck(String password) throws InvalidPasswordException {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordMatcher = pattern.matcher(password);
        if(!passwordMatcher.find()){
            throw new InvalidPasswordException("Kein gültiges Passwort.");
        }
    }

}
