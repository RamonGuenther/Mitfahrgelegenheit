package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Die Klasse ApplicationUrlAuthenticationSuccessHandler kümmert sich um
 * das richtige Routing nach dem Einloggen. Loggt sich ein Benutzer das
 * erste Mal ein, wird er zu einer Registrierungsseite weitergeleitet.
 * Andernfalls erscheint direkt die Startseite der Applikation.
 *
 * Quelle / Vorlage: https://www.baeldung.com/spring_redirect_after_login
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class ApplicationUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String DEFAULT_SUCCESS_URL = "/dashboard";
    private static final String USERDATA_NEEDED_URL = "/benutzerdaten";

    @Autowired
    UserService userService;

    protected Log logger = LogFactory.getLog(this.getClass());

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        if(!userService.findBenutzerByUsername(authentication.getName()).isFirstLogin()){
            return USERDATA_NEEDED_URL;
        }
        else{
            User user = userService.findBenutzerByUsername(authentication.getName());
            user.setLastLogin(LocalDateTime.now());
            userService.save(user);
            return DEFAULT_SUCCESS_URL;
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}