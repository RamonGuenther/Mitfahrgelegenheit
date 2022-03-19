package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum PageId {
    OWN_DRIVE_OFFERS_VIEW("OwnDriveOffersView"),
    SEARCH_DRIVE_RESULT_VIEW("SearchDriveResultView"),
    PROFILE("Profile");

    public final String label;

    PageId(String label) {
        this.label = label;
    }
}
