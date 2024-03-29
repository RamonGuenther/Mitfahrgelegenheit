package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Die Klasse MyUserPrincipal ist dafür da einen Benutzer aus der
 * Datenbank zu laden. Sie stellt ein UserDetails-Objekt dar,
 * welches die Benutzerdaten für die Security enthält.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class MyUserPrincipal implements UserDetails {

    private User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}