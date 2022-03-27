package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.PasswordsDoNotMatchException;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static com.google.common.hash.Hashing.sha256;
import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;
import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullOrEmptyCheck;
import static java.lang.String.valueOf;

@Embeddable
public class HashedPassword {

    private final String hash;
    private final String salt;

    public HashedPassword (NewPassword newPassword) throws IllegalArgumentException, PasswordsDoNotMatchException {

        nullCheck (newPassword);
        nullOrEmptyCheck (newPassword.original (), newPassword.confirmation ());

        if (!newPassword.original ().equals (newPassword.confirmation ()))
            throw new PasswordsDoNotMatchException();

        salt = generateSalt ();
        hash = generateHash (newPassword.original (), salt);
    }

    @PersistenceConstructor
    protected HashedPassword (String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    private String generateSalt () {
        SecureRandom random = new SecureRandom ();
        return  valueOf (random.nextInt ());
    }

    private String generateHash (String password, String salt) {
        return sha256 ().hashString (salt + password, StandardCharsets.UTF_8).toString ();
    }

    public Boolean checkPassword (String password) {
        return hash.equals(generateHash(password, salt));
    }
}
