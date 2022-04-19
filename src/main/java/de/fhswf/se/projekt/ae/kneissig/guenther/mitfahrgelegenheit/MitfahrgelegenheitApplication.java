package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@CssImport(value = "/themes/mitfahrgelegenheit/buttons.css", themeFor = "vaadin-button")
@CssImport(value = "/themes/mitfahrgelegenheit/radiobutton.css", themeFor = "vaadin-radio-button")
@CssImport(value = "/themes/mitfahrgelegenheit/checkbox.css", themeFor = "vaadin-checkbox")
public class MitfahrgelegenheitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitfahrgelegenheitApplication.class, args);
    }

}
