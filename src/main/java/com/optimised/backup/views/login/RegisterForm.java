package com.optimised.backup.views.login;

import com.optimised.backup.data.Role;
import com.optimised.backup.data.entity.User;
import com.optimised.backup.data.service.UserService;
import com.optimised.backup.security.SecurityConfiguration;
import com.optimised.backup.views.MainLayout;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;


@PageTitle("Register")
@Route(value = "Register", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class RegisterForm extends VerticalLayout {
    @Autowired
    SecurityConfiguration securityConfiguration;
    @Autowired
    UserService userService;

    TextField user_name = new TextField("User Name");
    TextField name = new TextField("Name");
    PasswordField password1 = new PasswordField("Password");
    PasswordField password2 = new PasswordField("Confirm Password");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    CheckboxGroup<Role> checkboxGroup = new CheckboxGroup<>();
    Binder<User> binder = new BeanValidationBinder<>(User.class);


    public RegisterForm() {
        addClassName("register-form");
        binder.bindInstanceFields(this);
        checkboxGroup.setLabel("Roles");
        checkboxGroup.setItems(Role.ADMIN,Role.USER);
        add(
                user_name,
                name,
                password1,
                password2,
                checkboxGroup,
                createButtonsLayout());
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> closeEvent());
        return new HorizontalLayout(save, close);
    }
    private void validateAndSave() {
        String p1 = password1.getValue().trim();
        String p2 = password2.getValue().trim();
        if (name.getValue().trim().isEmpty()){
            Notification.show("Name must not be empty");
        } else if (user_name.getValue().trim().isEmpty()){
            Notification.show("User name must not be empty");
        }
        else if (p1.isEmpty()){
            Notification.show("Password must not be empty");
        }
        else if (p1.length() < 8){
            Notification.show("Password must have a minimum of 8 characters");
        }else  if (!p1.equals(p2)){
            Notification.show("Passwords much match");
        } else if (checkboxGroup.getSelectedItems().isEmpty()){
            Notification.show("User must have at least one role");
        } else if (userService.findUserByUserName(user_name.getValue()) != null){
            Notification.show("This user name is used");
        }
        else {
            User user = new User();
            user.setName(name.getValue());
            user.setUsername(user_name.getValue());
            String passwordHash = securityConfiguration.passwordEncoder().encode(p1);
            user.setHashedPassword(passwordHash);
            Set<Role> userRoles = checkboxGroup.getSelectedItems();
            user.setRoles(userRoles);
            userService.save(user);
            Notification.show("User registered");
        }
    }
    private void closeEvent(){}


 }
