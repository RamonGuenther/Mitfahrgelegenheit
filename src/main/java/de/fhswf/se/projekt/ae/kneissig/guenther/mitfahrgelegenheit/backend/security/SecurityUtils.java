package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * Die Klasse SecurityUtils bietet Security-Methoden, wie der Prüfung,
 * ob es sich um eine Framework-Internen Anfrage handelt und der
 * Prüfung, ob ein Benutzer eingeloggt ist.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public final class SecurityUtils {

    private SecurityUtils(){

    }

    /**
     * Bestimmt ob es eine frameworkinterne Anfrage von Vaadin ist
     * @param request   Anfrage
     * @return
     */
    static boolean isFrameworkInternalRequest(HttpServletRequest request){
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);

        return parameterValue != null &&
                Stream.of(ServletHelper.RequestType.values()).
                        anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    /**
     * Prüft ob der aktuelle Nutzer eingeloggt ist
     * @return  Gibt zurück, ob der Nutzer eingeloggt ist
     */
    static boolean isUserLoggedIn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}
