package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

public class ValidationUtility {

    private static final String ADDRESS_PATTERN = "(?<strasse>[A-Za-z_äÄöÖüÜß\\s-.()]+) (?<hausnummer>[\\s\\w]*) (?<postleitzahl>\\d{5}) (?<ort>[A-Za-z_äÄöÖüÜß\\s-.()]+)";

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

    public static void nullOrEmptyCheck (String... ss) throws IllegalArgumentException {
        Arrays.stream (ss).forEach (ValidationUtility::nullOrEmptyCheck);
    }

    public static void addressPatternCheck(String address) throws InvalidAddressException {
        address = address.replace(", Deutschland", "");
        String replacedAddress = address.replace(",", " ");
        Pattern pattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher addressMatcher = pattern.matcher(replacedAddress);
        if(!addressMatcher.find()){
            throw new InvalidAddressException("Keine gültige Adresse.");
        }
    }

    public static void localDateCheck(LocalDate input) throws InvalidDateException {
        if(input.isBefore(LocalDate.now()) || input.equals(LocalDate.now())){
            throw new InvalidDateException("Das Datum darf nicht in der Vergangenheit liegen oder dem heutigen Datum entsprechen");
        }
    }

}
