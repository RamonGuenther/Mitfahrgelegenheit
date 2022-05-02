package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

/**
 * Die Enumeration PageId dient für die Komponenten der Applikation als Unterscheidungs-
 * merkmal um welche View es sich an bestimmten Stellen gerade handelt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public enum PageId {
    OWN_DRIVE_OFFERS_VIEW("OwnDriveOffersView"),
    SEARCH_DRIVE_RESULT_VIEW("SearchDriveResultView"),
    PROFILE("Profile"),
    DRIVE_REQUEST_LIST_VIEW("DriveRequestListView"),

    OFFER_DRIVE_VIEW("OfferDriveView");

    public final String label;

    PageId(String label) {
        this.label = label;
    }
}
