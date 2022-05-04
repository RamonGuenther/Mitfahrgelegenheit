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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.IncorrectPasswordException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidPasswordException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.PasswordsDoNotMatchException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.passwordPatternCheck;

/**
 * Die Klasse PasswordDialog erstellt einen Dialog, mit dem der Benutzer
 * sein passwort ändern kann.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/change-password-dialog.css")
public class PasswordDialog extends Dialog {

    private final User user;
    private final PasswordField oldPassword;
    private final PasswordField newPasswordFirstEntry;
    private final PasswordField newPasswordSecondEntry;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordDialog(UserService userService) {
        passwordEncoder = new BCryptPasswordEncoder();

        setId("password-dialog");
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        this.user = userService.getCurrentUser();

        H1 title = new H1("Passwort ändern");
        title.setId("password-title");

        Label divider = new Label("");
        divider.setId("divider");

        oldPassword = new PasswordField();
        oldPassword.setRequired(true);
        oldPassword.setLabel("Altes Passwort");
        oldPassword.setId("old-password-field");
        oldPassword.addFocusListener(e-> oldPassword.setInvalid(false));

        newPasswordFirstEntry = new PasswordField();
        newPasswordFirstEntry.setRequired(true);
        newPasswordFirstEntry.setLabel("Neues Passwort");
        newPasswordFirstEntry.setHelperText("Ein Passwort muss mindestens 8 Zeichen, 1 Buchstaben und 1 Zahl beinhalten.");
        newPasswordFirstEntry.setClassName("password-field");
        newPasswordFirstEntry.addFocusListener(e-> newPasswordFirstEntry.setInvalid(false));

        newPasswordSecondEntry = new PasswordField();
        newPasswordSecondEntry.setRequired(true);
        newPasswordSecondEntry.setLabel("Passwort wiederholen");
        newPasswordSecondEntry.setClassName("password-field");
        newPasswordSecondEntry.addFocusListener(e-> newPasswordSecondEntry.setInvalid(false));

        Button buttonSavePassword = new Button("Speichern");
        buttonSavePassword.setClassName("password-buttons");
        buttonSavePassword.addClickListener(event -> {
            try {
                if (textFieldCheck()) {
                    passwordCheck();
                    user.setPassword(passwordEncoder.encode(newPasswordSecondEntry.getValue()));
                    userService.save(user);
                    NotificationSuccess.show("Das Passwort wurde geändert");
                    close();
                }

            } catch (IncorrectPasswordException ex) {
                oldPassword.setInvalid(true);
                oldPassword.setErrorMessage(ex.getMessage());
                ex.printStackTrace();

            } catch (PasswordsDoNotMatchException | InvalidPasswordException ex) {
                newPasswordFirstEntry.setInvalid(true);
                newPasswordSecondEntry.setInvalid(true);
                newPasswordFirstEntry.setErrorMessage(ex.getMessage());
                newPasswordSecondEntry.setErrorMessage(ex.getMessage());
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
     *
     * Die Methode passwordCheck prüft die Passworteingaben des Benutzers. Dabei
     * wird geprüft, ob die Eingabe des alten Passworts korrekt ist, und ob die zwei Eingaben
     * des neuen Passworts übereinstimmen.
     *
     * @throws InvalidPasswordException -
     * @throws PasswordsDoNotMatchException -
     * @throws IncorrectPasswordException -
     */
    private void passwordCheck() throws InvalidPasswordException, PasswordsDoNotMatchException, IncorrectPasswordException {
        if (passwordEncoder.matches(oldPassword.getValue(), user.getPassword())) {
            if (newPasswordFirstEntry.getValue().equals(newPasswordSecondEntry.getValue())) {
                passwordPatternCheck(newPasswordFirstEntry.getValue());
            } else {
                throw new PasswordsDoNotMatchException("Die Eingaben für das neue Passwort stimmen nicht überein.");
            }
        } else {
            throw new IncorrectPasswordException("Die Eingabe des alten Passworts ist inkorrekt.");
        }
    }

    /**
     * Die Methode textFieldCheck prüft, ob alle Pflichteingaben erfüllt
     * wurden.
     *
     * @return true oder false
     */
    private boolean textFieldCheck() {
        if (!oldPassword.getValue().isEmpty() && !newPasswordFirstEntry.getValue().isEmpty() && !newPasswordSecondEntry.getValue().isEmpty()) {
            return true;
        } else {
            if(oldPassword.getValue().isEmpty()) {
                oldPassword.setErrorMessage("Altes Passwort bitte eintragen");
                oldPassword.setInvalid(true);
            }
            if(newPasswordFirstEntry.getValue().isEmpty()) {
                newPasswordFirstEntry.setErrorMessage("Neues Passwort bitte eintragen");
                newPasswordFirstEntry.setInvalid(true);
            }
            if(newPasswordSecondEntry.getValue().isEmpty()) {
                newPasswordSecondEntry.setErrorMessage("Passwort bitte wiederholen");
                newPasswordSecondEntry.setInvalid(true);
            }
            return false;
        }
    }
}
