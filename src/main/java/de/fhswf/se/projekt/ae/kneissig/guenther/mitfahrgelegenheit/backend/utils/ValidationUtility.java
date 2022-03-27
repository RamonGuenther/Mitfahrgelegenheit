package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

public class ValidationUtility {

    public static void nullCheck (Object o) throws IllegalArgumentException {
        if (isNull (o))
            throw new IllegalArgumentException ();
    }

    public static void nullCheck(Object... os) throws IllegalArgumentException {
        Arrays.stream (os).forEach (ValidationUtility::nullCheck);
    }

    public static void nullElementCheck (Collection<?> os) throws IllegalArgumentException {
        nullCheck (os);

        if (os.stream ().anyMatch (Objects::isNull))
            throw new IllegalArgumentException ();
    }

    public static void nullOrEmptyCheck (String s) throws IllegalArgumentException {
        if (isNullOrEmpty (s))
            throw new IllegalArgumentException ();
    }

    public static void nullOrEmptyCheck (String... ss) throws IllegalArgumentException {
        Arrays.stream (ss).forEach (ValidationUtility::nullOrEmptyCheck);
    }

}
