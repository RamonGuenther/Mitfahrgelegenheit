package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

/**
 * Die Enumeration PageId dient für die Komponenten der Applikation als Unterscheidungs-
 * merkmal um welche View es sich an bestimmten Stellen gerade handelt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public enum PageId {

    SEARCH_DRIVE_VIEW("SearchDriveView"),
    DASHBOARD_VIEW("DashboardView"),
    OFFER_DRIVE_VIEW("OfferDriveView"),
    BOOKINGS_VIEW("BookingsView"),
    COMPLETED_DRIVE_VIEW("CompletedDriveView"),
    OWN_DRIVE_OFFERS_VIEW("OwnDriveOffersView"),
    PROFILE("Profile"),
    SEARCH_DRIVE_RESULT_VIEW("SearchDriveResultView"),
    DRIVE_REQUEST_LIST_VIEW("DriveRequestListView");


    public final String label;

    PageId(String label) {
        this.label = label;
    }
}
