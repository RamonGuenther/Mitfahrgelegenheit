package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.SpringServletContainerInitializer;

@SpringBootApplication
@EnableAsync
public class MitfahrgelegenheitApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MitfahrgelegenheitApplication.class, args);
    }
}
