package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Die Klasse CustomRequestCache speichert die Anfrage des Nutzers,
 * damit er dorthin weitergeleitet werden kann, sobald die Anmeldung
 * abgeschlossen ist.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class CustomRequestCache extends HttpSessionRequestCache {

    /**
     * Speichert nicht authentifizierte Anforderungen, damit der Benutzer
     * auf die Seite umgeleitet werden kann auf die er zugreifen wollte,
     * sobald er angemeldet ist
     *
     * @param request   Anfrage
     * @param response  Antwort
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response){

        if(!SecurityUtils.isFrameworkInternalRequest(request)){
            super.saveRequest(request, response);
        }
    }
}