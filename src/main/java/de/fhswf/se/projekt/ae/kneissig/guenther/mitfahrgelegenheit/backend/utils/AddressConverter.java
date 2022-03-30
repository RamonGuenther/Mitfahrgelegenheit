package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressConverter {

//    private static final String ADDRESS_PATTERN = "(?<strasse>[A-Za-z_äÄöÖüÜß\\s-.()]+) (?<hausnummer>[\\s\\w]*) (?<postleitzahl>\\d{5}) (?<ort>[A-Za-z_äÄöÖüÜß\\s-.()]+) (?<land>[a-zA-Z]+)";
    private static final String ADDRESS_PATTERN = "(?<strasse>[A-Za-z_äÄöÖüÜß\\s-.()]+) (?<hausnummer>[\\s\\w]*) (?<postleitzahl>\\d{5}) (?<ort>[A-Za-z_äÄöÖüÜß\\s-.()]+)";


    private String street;
    private String number;
    private String postalCode;
    private String place;

    public AddressConverter(String address) {
        address = address.replace(", Deutschland", "");
        convert(address);
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPlace() {
        return place;
    }

    private void convert(String address) {

        String replacedAddress = address.replace(",", " ");

        Pattern pattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher addressMatcher = pattern.matcher(replacedAddress);

        try{
            addressMatcher.find();
            street = addressMatcher.group("strasse").trim();
            number = addressMatcher.group("hausnummer").trim();
            postalCode = addressMatcher.group("postleitzahl").trim();
            place = addressMatcher.group("ort").trim();
        }
        catch(Exception e){
            throw new IllegalArgumentException("Keine gültige Adresse.");
        }
    }
}

