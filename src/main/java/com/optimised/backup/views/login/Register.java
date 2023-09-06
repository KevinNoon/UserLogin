package com.optimised.backup.views.login;

import com.optimised.backup.data.entity.User;
import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Register")
@Route(value = "Register", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class Register extends VerticalLayout {
    public void setRegister(User user) {
        binder.setBean(user);
    }

    TextField fore_name = new TextField("Fore Name");
    TextField last_name = new TextField("Last Name");
    EmailField email = new EmailField("eMail");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<User> binder = new BeanValidationBinder<>(User.class);


    public Register() {
        addClassName("register-form");
        binder.bindInstanceFields(this);
        add(
                fore_name,
                last_name,
                email,
                createButtonsLayout());
    }

}
