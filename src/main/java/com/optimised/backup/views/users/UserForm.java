package com.optimised.backup.views.users;

import com.optimised.backup.data.Role;
import com.optimised.backup.data.entity.User;
import com.optimised.backup.data.service.UserService;
import com.optimised.backup.security.SecurityConfiguration;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.Set;

public class UserForm extends FormLayout {

    public void setUser(User user) {
        binder.setBean(user);
    }
    TextField name = new TextField("Name");
    TextField username = new TextField("User Name");
    PasswordField password1 = new PasswordField("Password");
    PasswordField password2 = new PasswordField("Confirm Password");
    CheckboxGroup<Role> roles = new CheckboxGroup<>();
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<User> binder = new BeanValidationBinder<>(User.class);
    private final UserService userService;
    private final SecurityConfiguration securityConfiguration;

    public UserForm(UserService userService, SecurityConfiguration securityConfiguration) {
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
        addClassName("user-form");
        roles.setLabel("Roles");
        roles.setItems(Role.ADMIN,Role.USER);
        binder.bindInstanceFields(this);
        add(
                name,
                username,
                password1,
                password2,
                roles,
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
            System.out.println(this.binder.getBean().getName().isEmpty());
            String p1 = password1.getValue().trim();
            String p2 = password2.getValue().trim();
            if (name.getValue().trim().isEmpty()){
                Notification.show("Name must not be empty");
            } else if (username.getValue().trim().isEmpty()){
                Notification.show("User name must not be empty");
            }
            else if (p1.isEmpty()){
                Notification.show("Password must not be empty");
            }
            else if (p1.length() < 8){
                Notification.show("Password must have a minimum of 8 characters");
            }else  if (!p1.equals(p2)){
                Notification.show("Passwords much match");
            } else if (roles.getSelectedItems().isEmpty()){
                Notification.show("User must have at least one role");
            } else if (this.userService.findUserByUserName(username.getValue()) != null){
                Notification.show("This user name is used");
            }
            else {
                User user = new User();
                user.setName(name.getValue());
                user.setUsername(username.getValue());
                String passwordHash = this.securityConfiguration.passwordEncoder().encode(p1);
                user.setHashedPassword(passwordHash);
                Set<Role> userRoles = roles.getSelectedItems();
                user.setRoles(userRoles);
                userService.save(user);
                Notification.show("User registered");
                fireEvent(new SaveEvent(this, binder.getBean()));
            }
        }
    }
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);

            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);

        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
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
