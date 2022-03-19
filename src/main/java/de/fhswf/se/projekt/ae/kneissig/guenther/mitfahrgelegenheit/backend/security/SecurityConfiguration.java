package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";


    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.csrf().disable()
                .requestCache()
                .requestCache(new CustomRequestCache())
                .and()
                .authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .successHandler(ApplicationUrlAuthenticationSuccessHandler())
                .failureUrl(LOGIN_FAILURE_URL)
                .and()
                .logout()
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Bean
    public AuthenticationSuccessHandler ApplicationUrlAuthenticationSuccessHandler(){
        return new ApplicationUrlAuthenticationSuccessHandler();
    }

    // TESTUSER -> Kann mit Ldap-Verbindung nicht mehr verwendet werden!
    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        UserDetails user =
                User
                        .withUsername("user")
                        .password("{noop}password")
                        .roles("USER")
                        .build();

        UserDetails user2 =
                User
                        .withUsername("user2")
                        .password("{noop}password")
                        .roles("USER")
                        .build();

        UserDetails user3 =
                User
                        .withUsername("user3")
                        .password("{noop}password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user, user2, user3);
    }

    /**
     * Schließt die Vaadin-Framework-Kommunikation und statische Assets
     * von Spring Security aus
     * @param web   ...
     */

    @Override
    public void configure (WebSecurity web){

        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**",
                "/api/**");
    }
}
