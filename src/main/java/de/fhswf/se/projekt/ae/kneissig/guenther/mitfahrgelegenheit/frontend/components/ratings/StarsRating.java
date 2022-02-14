package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

/* © Copyright JFancy 2021 www.jfancy.de */
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * Star-Rating Component
 *
 * @author Marcus
 * @author JFancy
 */
@Tag("stars-rating")
@JsModule(value = "starrating.js")
public class StarsRating extends AbstractSinglePropertyField<StarsRating, Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a Star-Rating Component
     */
    public StarsRating() {
        this(1, 5, true);
    }

    /**
     * Creates a Star-Rating Component
     *
     * @param rating inital rating in stars
     */
    public StarsRating(Integer rating) {
        this(rating, 5, true);
    }

    /**
     * Creates a Star-Rating Component
     *
     * @param rating   inital rating in stars
     * @param numstars max amount of stars
     */
    public StarsRating(Integer rating, Integer numstars) {
        this(rating, numstars, true);
    }

    /**
     * Creates a Star-Rating Component
     *
     * @param rating   inital rating in stars
     * @param numstars max amount of stars
     * @param manual   In manuel mode the user is able to change the rating
     */
    public StarsRating(Integer rating, Integer numstars, boolean manual) {
        super("rating", 0, false);
        setRating(rating);
        setNumstars(numstars);
        setManual(manual);
    }

    private static final PropertyDescriptor<Integer, Integer> ratingProperty = PropertyDescriptors
            .propertyWithDefault("rating", 0);

    private static final PropertyDescriptor<Integer, Integer> numstarsProperty = PropertyDescriptors
            .propertyWithDefault("numstars", 0);
    private static final PropertyDescriptor<Boolean, Boolean> manualProperty = PropertyDescriptors
            .propertyWithDefault("manual", false);

    /**
     * Gets the current rating
     *
     * @return amount of stars
     */
    public Integer getRating() {
        return ratingProperty.get(this);
    }

    /**
     * Sets the current rating
     *
     * @param rating Amount of stars
     */
    public void setRating(Integer rating) {
        ratingProperty.set(this, rating);
    }

    public Integer getNumstars() {
        return numstarsProperty.get(this);
    }

    public void setNumstars(Integer numstars) {
        numstarsProperty.set(this, numstars);
    }

    /**
     * Checks it the Component is in manuel mode. (In manuel mode the user is able
     * to change the rating)
     *
     * @return if the component is in manuel mode
     */
    public boolean isManual() {
        return manualProperty.get(this);
    }

    /**
     * Sets the manuel mode. If set to <code>true</code> the user is able to change
     * the rating
     *
     * @param manual true/false
     */
    public void setManual(boolean manual) {
        manualProperty.set(this, manual);
    }
}