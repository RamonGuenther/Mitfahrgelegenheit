package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidPasswordException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.passwordPatternCheck;

/**
 * Die Klasse PasswordDialog erstellt einen Dialog, mit dem der Benutzer
 * sein passwort ändern kann.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/change-password-dialog.css")
public class PasswordDialog extends Dialog {

    private final UserService userService;
    private final User user;
    private final PasswordField oldPassword;
    private final PasswordField newPasswordFirstEntry;
    private final PasswordField newPasswordSecondEntry;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordDialog(UserService userService, BCryptPasswordEncoder passwordEncoder){

        setId("password-dialog");
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.user = userService.getCurrentUser();

        H1 title = new H1("Passwort ändern");
        title.setId("password-title");

        Label divider = new Label("");
        divider.setId("divider");

        oldPassword = new PasswordField();
        oldPassword.setRequired(true);
        oldPassword.setLabel("Altes Passwort");
        oldPassword.setId("old-password-field");

        newPasswordFirstEntry = new PasswordField();
        newPasswordFirstEntry.setRequired(true);
        newPasswordFirstEntry.setLabel("Neues Passwort");
        newPasswordFirstEntry.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        newPasswordFirstEntry.setHelperText("Ein Passwort muss mindestens 8 Zeichen, 1 Buchstaben und 1 Zahl beinhalten.");
        newPasswordFirstEntry.setErrorMessage("Kein gültiges Passwort");
        newPasswordFirstEntry.setClassName("password-field");

        newPasswordSecondEntry = new PasswordField();
        newPasswordSecondEntry.setRequired(true);
        newPasswordSecondEntry.setLabel("Passwort wiederholen");
        newPasswordSecondEntry.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        newPasswordSecondEntry.setErrorMessage("Kein gültiges Passwort");
        newPasswordSecondEntry.setClassName("password-field");

        Button buttonSavePassword = new Button("Speichern");
        buttonSavePassword.setClassName("password-buttons");
        buttonSavePassword.addClickListener(event -> {
            try {
                if (textFieldCheck()) {
                    if (passwordCheck()) {
                        user.setPassword(passwordEncoder.encode(newPasswordSecondEntry.getValue()));
                        userService.save(user);
                        NotificationSuccess.show("Das Passwort wurde geändert");
                        close();
                    }
                }
            }
            catch (InvalidPasswordException ex){
                NotificationError.show(ex.getMessage());
                ex.printStackTrace();
            }
        });
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setClassName("password-buttons");
        buttonCancel.addClickListener(event -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonSavePassword, buttonCancel);
        buttonLayout.setId("password-button-layout");

        VerticalLayout verticalLayout = new VerticalLayout(
                title,
                oldPassword,
                divider,
                newPasswordFirstEntry,
                newPasswordSecondEntry,
                buttonLayout);

        add(verticalLayout);
    }

    /**
     * Die Methode passwordCheck prüft die Passworteingaben des Benutzers. Dabei
     * wird geprüft, ob die Eingabe des alten Passworts korrekt ist, und ob die zwei Eingaben
     * des neuen Passworts übereinstimmen.
     *
     * @return                              true oder false
     * @throws InvalidPasswordException     -
     */
    private boolean passwordCheck() throws InvalidPasswordException {
        if(passwordEncoder.matches(oldPassword.getValue(), user.getPassword())){

            if(newPasswordFirstEntry.getValue().equals(newPasswordSecondEntry.getValue())){
                passwordPatternCheck(newPasswordFirstEntry.getValue());
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

    /**
     * Die Methode textFieldCheck prüft, ob alle Pflichteingaben erfüllt
     * wurden.
     *
     * @return                          true oder false
     */
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
