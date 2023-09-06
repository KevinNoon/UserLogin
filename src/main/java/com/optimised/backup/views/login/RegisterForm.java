package com.optimised.backup.views.login;

import com.optimised.backup.data.entity.User;
import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Register")
@Route(value = "Register", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class RegisterForm extends VerticalLayout {
    public void setRegister(User user) {
        binder.setBean(user);
    }

    TextField user_name = new TextField("User Name");
    TextField name = new TextField("Name");
    PasswordField password1 = new PasswordField("Password");
    PasswordField password2 = new PasswordField("Confirm Password");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<User> binder = new BeanValidationBinder<>(User.class);


    public RegisterForm() {
        addClassName("register-form");
        binder.bindInstanceFields(this);
        add(
                user_name,
                name,
                password1,
                password2,
                createButtonsLayout());
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }
    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
    public static abstract class RegisterFormEvent extends ComponentEvent<RegisterForm> {
        private User user;

        protected RegisterFormEvent(RegisterForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends RegisterFormEvent {
        SaveEvent(RegisterForm source, User user) {
            super(source, user);
            System.out.println("Saving user" + " " + user);

        }
    }

    public static class DeleteEvent extends RegisterFormEvent {
        DeleteEvent(RegisterForm source, User user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends RegisterFormEvent {
        CloseEvent(RegisterForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
