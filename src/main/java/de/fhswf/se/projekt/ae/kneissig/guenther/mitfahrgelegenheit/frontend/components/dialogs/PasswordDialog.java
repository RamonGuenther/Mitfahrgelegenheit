package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordDialog extends Dialog {

    private final UserService userService;
    private final User user;
    private final TextField oldPassword;
    private final TextField newPasswordFirstEntry;
    private final TextField newPasswordSecondEntry;

    private final BCryptPasswordEncoder passwordEncoder;


    public PasswordDialog(UserService userService, BCryptPasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.user = userService.getCurrentUser();

        H1 title = new H1("Passwort ändern");

        oldPassword = new TextField();
        oldPassword.setLabel("Altes Passwort");

        newPasswordFirstEntry = new TextField();
        newPasswordFirstEntry.setLabel("Neues Passwort");

        newPasswordSecondEntry = new TextField();
        newPasswordSecondEntry.setLabel("Passwort wiederholen");

        Button buttonSavePassword = new Button("Speichern");
        buttonSavePassword.addClickListener(event -> {
            if(textFieldCheck()){
                if(passwordCheck()){
                    user.setPassword(passwordEncoder.encode(newPasswordSecondEntry.getValue()));
                    userService.save(user);
                    close();
                }
            }
        });
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.addClickListener(event -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonSavePassword, buttonCancel);

        VerticalLayout verticalLayout = new VerticalLayout(
                title,
                oldPassword,
                newPasswordFirstEntry,
                newPasswordSecondEntry,
                buttonLayout);

        add(verticalLayout);
    }

    private boolean passwordCheck(){
        if(passwordEncoder.matches(oldPassword.getValue(), user.getPassword())){

            if(newPasswordFirstEntry.getValue().equals(newPasswordSecondEntry.getValue())){
                return true;
            }
            else {
                NotificationError.show("Die Eingaben für das neue Passwort stimmen nicht überein.");
                return false;
            }
        }
        else{
            NotificationError.show("Die Eingabe des alten Passworts ist inkorrekt.");
            return false;
        }
    }

    private boolean textFieldCheck() {
        if (!oldPassword.getValue().isEmpty() &&
                !newPasswordFirstEntry.getValue().isEmpty() &&
                !newPasswordSecondEntry.getValue().isEmpty()) {
            return true;
        }
        else {
            NotificationError.show("Es müssen alle Felder ausgefüllt werden");
            return false;
        }
    }

}
